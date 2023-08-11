package com.florianlechat.discordtunnel.controllers;

import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.nio.channels.NotYetConnectedException;

import jakarta.websocket.OnOpen;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.Session;
import jakarta.websocket.OnMessage;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.WebSocketContainer;
import jakarta.websocket.DeploymentException;

@ClientEndpoint
public class DiscordGateway
{
	private String token;
	private String status;
	private Session session;
	private Boolean receivedAck = true;
	private static final String DISCORD_GATEWAY_URL = "wss://gateway.discord.gg/?v=10&encoding=json";
	private static final String DISCORD_WEBHOOK_URL = "https://discord.com/api/webhooks/1139161244142682162/4DoYNP5NRGQvPWoVdnPX_N-dp1HJqbIG7i6gvTimykMxtnfX5uyZ94NkYcTx0mvUd3FJ";

	// Permet de journaliser les messages en indiquant la date et l'heure.
	private void logMessage(String message)
	{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("[" + dateFormat.format(new Date()) + "] " + message);
    }

	// Permet de se connecter au WebSocket de Discord.
	//  Source : https://discord.com/developers/docs/topics/gateway#connections
	public void connect(String token, String status)
	{
		try
		{
			WebSocketContainer container = jakarta.websocket.ContainerProvider.getWebSocketContainer();

			this.token = token;

			container.setDefaultMaxTextMessageBufferSize(1024 * 1024 * 64);
			container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024 * 64);
			container.connectToServer(this, new URI(DISCORD_GATEWAY_URL));
		}
		catch (DeploymentException | URISyntaxException | IOException e)
		{
			e.printStackTrace();
		}
	}

	// Permet de déconnecter au WebSocket de Discord.
	public void close()
	{
		if (session != null && session.isOpen())
		{
			try
			{
				session.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	// Permet de se connecter puis de s'authentifier avec un jeton
	//  d'accès créé par un utilisateur.
	@OnOpen
	public void onWebSocketConnect(Session session)
	{
		this.session = session;

		logMessage("Connexion au WebSocket de Discord réussie.");

		JSONObject identifyJson = new JSONObject()
			// Code de l'opération d'identification.
			.put("op", 2)

			// Données de l'opération d'identification.
			.put("d", new JSONObject()
				// Jeton d'accès de l'utilisateur.
				.put("token", this.token)
				// Souscription aux événements.
				.put("intents", 256) // GUILD_PRESENCES.

				// Informations de présence.
				.put("presence", new JSONObject()
					.put("afk", false)
					.put("status", this.status)
				)

				// Informations sur le client.
				.put("properties", new JSONObject()
					.put("os", "linux")
					.put("device", "Discord-Tunnel")
					.put("browser", "Discord-Tunnel")
				)
			);

		send(identifyJson.toString());
	}

	// Permet de recevoir les messages en provenance du WebSocket.
	//  Source : https://discord.com/developers/docs/topics/opcodes-and-status-codes#gateway-gateway-opcodes
	@OnMessage
	public void onWebSocketText(String message)
	{
		JSONObject messageJson = new JSONObject(message);

		// On initialise d'abord la séquence de maintien de
		//  connexion si le code d'opération est 10.
		if (messageJson.getInt("op") == 10)
		{
			// On crée alors un nouveau thread pour envoyer les
			//  messages de maintien de connexion à intervalle
			//  régulier (défini par Discord) et indéfiniment.
			new Thread(() ->
			{
				while (true)
				{
					// On impose après un délai d'attente avant d'envoyer
					//  le message de maintien de connexion.
					try
					{
						Thread.sleep(messageJson.getJSONObject("d").getLong("heartbeat_interval"));
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					// On vérifie par la même occasion si la connexion
					//  est toujours ouverte.
					if (session == null || !this.session.isOpen())
					{
						return;
					}

					// On vérifie ensuite si le WebSocket de Discord a répondu
					//  au dernier message de maintien de connexion.
					if (!this.receivedAck)
					{
						// Si ce n'est pas le cas, on ferme la connexion
						//  et on en ouvre une nouvelle.
						logMessage("Le WebSocket de Discord n'a pas répondu au message de maintien de connexion.");
						logMessage("Reconnexion du WebSocket de Discord...");

						close();
						connect(this.token, this.status);

						return;
					}

					this.receivedAck = false;

					// Dans le cas contraire, on envoie un message de maintien
					//  de connexion périodique.
					JSONObject heartbeatJson = new JSONObject()
						.put("op", 1)
						.put("d", "null");

					send(heartbeatJson.toString());

					logMessage("Envoi d'un message de maintien de connexion périodique : " + message);
				}
			}).start();

			logMessage("Réception du message d'initialisation de la connexion : " + message);
		}
		// On traite ensuite les notifications de présence.
		else if (messageJson.getInt("op") == 0 && messageJson.getString("t").equals("PRESENCE_UPDATE"))
		{
			try
			{
				// Filtrage des utilisateurs observés.
				JSONObject presenceData = messageJson.getJSONObject("d");
				JSONObject userData = presenceData.getJSONObject("user");

				logMessage("Réception d'un changement de présence : " + message);

				if (!userData.has("username") || !userData.getString("username").equals("florian4016"))
				{
					return;
				}

				// Envoi d'une notification sur Discord.
				String status = presenceData.getString("status");
				String content = "{\"embeds\": [{\"title\": \"Alerte Discord Tunnel\", \"color\": 16711680, \"description\": \"Nouveau statut : « " + status + " »\"}]}";

				URL url = new URL(DISCORD_WEBHOOK_URL);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				connection.setRequestMethod("POST");
				connection.setRequestProperty("content-type", "application/json");

				connection.setDoInput(true);
				connection.setDoOutput(true);

				try (OutputStream os = connection.getOutputStream())
				{
					os.write(content.getBytes("UTF-8"));
					os.close();
				}

				connection.connect();
			}
			catch (IOException|JSONException e)
			{
				e.printStackTrace();
			}
		}
		// On envoie un message de maintien de connexion
		//  instantanément si le code d'opération est 1.
		else if (messageJson.getInt("op") == 1)
		{
			JSONObject heartbeatJson = new JSONObject()
				.put("op", 1)
				.put("d", "null");

			send(heartbeatJson.toString());

			logMessage("Envoi d'un message de maintien de connexion instantané : " + message);
		}
		// On force la reconnexion si le code d'opération est 7 ou 9.
		else if (messageJson.getInt("op") == 7 || messageJson.getInt("op") == 9)
		{
			logMessage("Le WebSocket de Discord a demandé une reconnexion.");

			this.close();
			this.connect(this.token, this.status);
		}
		// On réceptionne enfin les messages d'acquittement
		//  du message de maintien de connexion.
		else if (messageJson.getInt("op") == 11)
		{
			this.receivedAck = true;
		}
	}

	// Permet de recevoir les messages de déconnexion du WebSocket.
	@OnClose
	public void onWebSocketClose(CloseReason reason)
	{
		logMessage("Déconnexion du WebSocket de Discord : " + reason);

		// On force la reconnexion si la fermeture du WebSocket est anormale.
		//  Source : https://stackoverflow.com/a/19305172
		if (reason.getCloseCode() == CloseReason.CloseCodes.CLOSED_ABNORMALLY)
		{
			logMessage("Reconnexion du WebSocket de Discord...");

			this.connect(this.token, this.status);
		}
	}

	// Permet de recevoir les messages d'erreur du WebSocket.
	@OnError
	public void onWebSocketError(Throwable cause)
	{
		System.err.println("Erreur du WebSocket de Discord : " + cause);
	}

	// Permet d'envoyer un message quelconque au WebSocket.
	private void send(String message)
	{
		try
		{
			this.session.getBasicRemote().sendText(message);
		}
		catch (IOException | NotYetConnectedException e)
		{
			e.printStackTrace();
		}
	}
}