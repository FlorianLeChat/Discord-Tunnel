package com.florianlechat.discordtunnel.controllers;

import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

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
import jakarta.websocket.CloseReason.CloseCode;

@ClientEndpoint
public class DiscordGateway
{
	private String token;
	private String status;
	private String activity;
	private Session session;
	private Boolean receivedAck = true;
	private static final DiscordGateway instance = new DiscordGateway();
	private static final String DISCORD_GATEWAY_URL = "wss://gateway.discord.gg/?v=10&encoding=json";
	private static final String DISCORD_WEBHOOK_URL = "https://discord.com/api/webhooks/1139161244142682162/4DoYNP5NRGQvPWoVdnPX_N-dp1HJqbIG7i6gvTimykMxtnfX5uyZ94NkYcTx0mvUd3FJ";

	// Récupération de l'instance unique de la classe.
	public synchronized static DiscordGateway getInstance()
	{
		return instance;
	}

	// Journalisation des messages en indiquant la date et l'heure.
	private void logMessage(String message)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("Europe/Paris"));

		System.out.println("[" + dateFormat.format(new Date()) + "] " + message);
	}

	// Connexion au WebSocket de Discord.
	//  Source : https://discord.com/developers/docs/topics/gateway#connections
	public void connect(String token, String status, String activity)
	{
		// On ferme d'abord la connexion si elle est déjà ouverte.
		close();

		// On enregistre ensuite le jeton d'accès et le statut de l'utilisateur.
		this.token = token;
		this.status = status;
		this.activity = activity;

		// On tente après de se connecter au WebSocket de Discord.
		try
		{
			WebSocketContainer container = jakarta.websocket.ContainerProvider.getWebSocketContainer();

			container.setDefaultMaxTextMessageBufferSize(1024 * 1024 * 64);
			container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024 * 64);
			container.connectToServer(this, new URI(DISCORD_GATEWAY_URL));
		}
		catch (DeploymentException | URISyntaxException | IOException e)
		{
			e.printStackTrace();
		}

		// On envoie enfin une notification sur Discord.
		try
		{
			String content = "{\"embeds\": [{\"title\": \"État Discord Tunnel\", \"color\": 5763719, \"description\": \"Connexion au WebSocket.\"}]}";

			URL url = new URI(DISCORD_WEBHOOK_URL).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("content-type", "application/json");
			connection.setDoOutput(true);

			try (OutputStream os = connection.getOutputStream())
			{
				os.write(content.getBytes("UTF-8"));
				os.close();
			}

			connection.getResponseCode();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// Déconnexion du WebSocket de Discord.
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

		session = null;
	}

	// Connexion puis authentification avec un jeton d'accès créé par un utilisateur.
	@OnOpen
	public void onWebSocketConnect(Session session)
	{
		this.session = session;

		logMessage("Connexion au WebSocket de Discord réussie.");

		Pattern pattern = Pattern.compile("[\\p{So}]");
		Matcher matcher = pattern.matcher(this.activity);

		String emoji = matcher.find() ? matcher.group() : null;
		String message = emoji != null ? this.activity.replaceFirst(emoji, "") : this.activity;

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
					.put("activities", new JSONArray()
						.put(new JSONObject()
							.put("type", 4)
							.put("name", "Custom Status")
							.put("state", message)
							.put("emoji", new JSONObject()
								.put("name", emoji)
							)
						)
					)
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

	// Réception des messages en provenance du WebSocket.
	//  Source : https://discord.com/developers/docs/topics/opcodes-and-status-codes#gateway-gateway-opcodes
	@OnMessage
	public void onWebSocketText(String message)
	{
		// On initialise d'abord la séquence de maintien de
		//  connexion si le code d'opération est 10.
		JSONObject messageJson = new JSONObject(message);

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
					if (session == null || !session.isOpen())
					{
						return;
					}

					// On vérifie ensuite si le WebSocket de Discord a répondu
					//  au dernier message de maintien de connexion.
					if (!receivedAck)
					{
						// Si ce n'est pas le cas, on ferme la connexion
						//  et on en ouvre une nouvelle.
						logMessage("Le WebSocket de Discord n'a pas répondu au message de maintien de connexion.");
						logMessage("Reconnexion du WebSocket de Discord...");

						close();
						connect(token, status, activity);

						return;
					}

					receivedAck = false;

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
			// Filtrage des utilisateurs observés.
			JSONObject presenceData = messageJson.getJSONObject("d");
			JSONObject userData = presenceData.getJSONObject("user");

			logMessage("Réception d'un changement de présence : " + message);

			if (!userData.has("username") || !userData.getString("username").equals("laurinepearl"))
			{
				return;
			}

			try
			{
				// Envoi d'une notification sur Discord.
				String status = presenceData.getString("status");
				String content = "{\"embeds\": [{\"title\": \"Alerte Discord Tunnel\", \"color\": 3447003, \"description\": \"Nouveau statut : « " + status + " »\"}]}";

				URL url = new URI(DISCORD_WEBHOOK_URL).toURL();
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				connection.setRequestMethod("POST");
				connection.setRequestProperty("content-type", "application/json");
				connection.setDoOutput(true);

				try (OutputStream os = connection.getOutputStream())
				{
					os.write(content.getBytes("UTF-8"));
					os.close();
				}

				connection.getResponseCode();
			}
			catch (Exception e)
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

			close();
			connect(token, status, activity);
		}
		// On réceptionne enfin les messages d'acquittement
		//  du message de maintien de connexion.
		else if (messageJson.getInt("op") == 11)
		{
			receivedAck = true;
		}
	}

	// Réception des messages de déconnexion du WebSocket.
	@OnClose
	public void onWebSocketClose(CloseReason reason)
	{
		logMessage("Déconnexion du WebSocket de Discord : " + reason);

		// On envoie d'abord une notification sur Discord.
		try
		{
			String content = "{\"embeds\": [{\"title\": \"État Discord Tunnel\", \"color\": 15548997, \"description\": \"Déconnexion du WebSocket.\"}]}";

			URL url = new URI(DISCORD_WEBHOOK_URL).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("content-type", "application/json");
			connection.setDoOutput(true);

			try (OutputStream os = connection.getOutputStream())
			{
				os.write(content.getBytes("UTF-8"));
				os.close();
			}

			connection.getResponseCode();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// On force enfin la reconnexion en fonction du code de fermeture.
		//  Source : https://stackoverflow.com/a/19305172
		CloseCode closeCode = reason.getCloseCode();

		if (closeCode == CloseReason.CloseCodes.CLOSED_ABNORMALLY || closeCode == CloseReason.CloseCodes.GOING_AWAY)
		{
			logMessage("Reconnexion du WebSocket de Discord...");

			connect(token, status, activity);
		}
	}

	// Réception des messages d'erreur du WebSocket.
	@OnError
	public void onWebSocketError(Throwable cause)
	{
		System.err.println("Erreur du WebSocket de Discord : " + cause);
	}

	// Envoi d'un message quelconque au WebSocket.
	private void send(String message)
	{
		try
		{
			session.getBasicRemote().sendText(message);
		}
		catch (IOException | NotYetConnectedException e)
		{
			e.printStackTrace();
		}
	}
}