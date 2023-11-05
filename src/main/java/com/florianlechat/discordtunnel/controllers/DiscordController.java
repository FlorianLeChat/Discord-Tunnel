package com.florianlechat.discordtunnel.controllers;

import com.florianlechat.discordtunnel.components.ServerProperties;

import java.io.IOException;
import java.io.OutputStream;

import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;

import java.util.Base64;
import java.security.SecureRandom;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class DiscordController
{
	// Initialisation du WebSocket de Discord.
	final static DiscordGateway gateway = DiscordGateway.getInstance();

	// Nombre de caractères écrits par seconde (simulation par un humain).
	final static Integer CHARACTERS_PER_SECOND = 5;

	// Jetons d'authentification pour les différents utilisateurs.
	@Autowired
	private ServerProperties discordTokens;

	// Génération d'un nombre aléatoire en Base64.
	public static String generateNonce()
	{
		// On définit la longueur du nombre arbitraire
		//  avant de créer un tableau de bytes pour le stocker.
		Integer nonceLength = 19;
		byte[] nonceBytes = new byte[nonceLength];

		// On utilise ensuite SecureRandom pour générer des
		//  bytes aléatoires.
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(nonceBytes);

		// On encode ensuite ces bytes en Base64 pour obtenir
		//  une chaîne de caractères.
		String nonce = Base64.getUrlEncoder().withoutPadding().encodeToString(nonceBytes);

		// On tronque enfin la chaîne de caractères pour
		//  obtenir la longueur souhaitée.
		nonce = nonce.substring(0, nonceLength);

		return nonce;
	}

	// Envoi une requête HTTP vers l'API de Discord afin de simuler
	//  le fait qu'un utilisateur est en train de taper un message.
	//  Source : https://discord.com/developers/docs/resources/channel#trigger-typing-indicator
	@GetMapping(path = "/api/typing")
	public ResponseEntity<String> SendTyping(HttpServletRequest request) throws IOException, URISyntaxException
	{
		// On vérifie d'abord si le mot de passe est correct.
		String password = request.getParameter("secret");

		if (password == null || password.isEmpty() || !password.equals("laurine"))
		{
			return ResponseEntity.status(401).build();
		}

		// On effectue ensuite la requête HTTP.
		URL url = new URI("https://discord.com/api/v9/channels/1097906775291859027/typing").toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("POST");

		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("accept-language", "fr-FR,fr;q=0.9");
		connection.setRequestProperty("authorization", "MTA5NzkwNTA4ODAxOTg0NTI2MA.GJIDh4.-C6QaRjfJ0yigjAAuC1XCdM9ThmcESjhbOr358");
		connection.setRequestProperty("cache-control", "no-cache");
		connection.setRequestProperty("pragma", "no-cache");
		connection.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"112\", \"Google Chrome\";v=\"112\", \"Not:A-Brand\";v=\"99\"");
		connection.setRequestProperty("sec-ch-ua-mobile", "?0");
		connection.setRequestProperty("sec-ch-ua-platform", "\"macOS\"");
		connection.setRequestProperty("sec-fetch-dest", "empty");
		connection.setRequestProperty("sec-fetch-mode", "cors");
		connection.setRequestProperty("sec-fetch-site", "same-origin");
		connection.setRequestProperty("x-debug-options", "bugReporterEnabled");
		connection.setRequestProperty("x-discord-locale", "fr");
		connection.setRequestProperty("x-super-properties", "eyJvcyI6Ik1hYyBPUyBYIiwiYnJvd3NlciI6IkNocm9tZSIsImRldmljZSI6IiIsInN5c3RlbV9sb2NhbGUiOiJmci1GUiIsImJyb3dzZXJfdXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChNYWNpbnRvc2g7IEludGVsIE1hYyBPUyBYIDEwXzE1XzcpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS8xMTIuMC4wLjAgU2FmYXJpLzUzNy4zNiIsImJyb3dzZXJfdmVyc2lvbiI6IjExMi4wLjAuMCIsIm9zX3ZlcnNpb24iOiIxMC4xNS43IiwicmVmZXJyZXIiOiIiLCJyZWZlcnJpbmdfZG9tYWluIjoiIiwicmVmZXJyZXJfY3VycmVudCI6IiIsInJlZmVycmluZ19kb21haW5fY3VycmVudCI6IiIsInJlbGVhc2VfY2hhbm5lbCI6InN0YWJsZSIsImNsaWVudF9idWlsZF9udW1iZXIiOjE5MDUxMSwiY2xpZW50X2V2ZW50X3NvdXJjZSI6bnVsbCwiZGVzaWduX2lkIjowfQ==");
		connection.setRequestProperty("referrer", "https://discord.com/channels/@me/1097906775291859027");
		connection.setRequestProperty("referrerPolicy", "strict-origin-when-cross-origin");
		connection.setRequestProperty("body", null);
		connection.setRequestProperty("mode", "cors");
		connection.setRequestProperty("credentials", "include");
		connection.setFixedLengthStreamingMode(0);

		connection.setDoInput(true);
		connection.setDoOutput(true);

		// On retourne enfin le résultat de la requête.
		return ResponseEntity.status(connection.getResponseCode()).build();
	}

	// Envoi une requête HTTP vers l'API de Discord afin de simuler
	//  le fait qu'un utilisateur envoie un message dans un salon.
	//  Source : https://discord.com/developers/docs/resources/channel#create-message
	@GetMapping(path = "/api/message")
	public ResponseEntity<String> SendMessage(HttpServletRequest request) throws IOException, URISyntaxException
	{
		// On vérifie d'abord si le mot de passe est correct.
		String password = request.getParameter("secret");

		if (password == null || password.isEmpty() || !password.equals("laurine"))
		{
			return ResponseEntity.status(401).build();
		}

		// On vérifie ensuite si un délai et un message ont été spécifiés.
		String delay = request.getParameter("delay");
		String message = request.getParameter("message");

		if (delay == null || message == null || delay.isEmpty() || message.isEmpty() || !delay.matches("[0-9]+"))
		{
			return ResponseEntity.status(400).build();
		}

		// On calcule le temps pour écrire le message en fonction de sa longueur
		//  avant de l'ajouter au délai existant.
		Integer length = message.replaceAll("\\s+", "").length();
		Integer writing = Math.round(length / CHARACTERS_PER_SECOND);
		Integer cooldown = Integer.parseInt(delay);
		cooldown = Math.max(cooldown + writing, cooldown);

		// On affiche une simulation de l'écriture du message si nécessaire.
		if (writing > 0)
		{
			SendTyping(request);

			// Si le message est trop long, on renvoie une simulation de l'écriture
			//  après 7 secondes pour continuer l'affichage de l'indicateur.
			if (writing >= 7)
			{
				new Thread(() ->
				{
					try
					{
						Thread.sleep(7000);
						SendTyping(request);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}).start();
			}
		}

		// On impose alors un quelconque délai avant l'envoi du message.
		if (cooldown > 0)
		{
			try
			{
				Thread.sleep(cooldown * 1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		// On effectue après la requête HTTP.
		URL url = new URI("https://discord.com/api/v9/channels/1097906775291859027/messages").toURL();
		String body = "{\"content\":\"" + message.trim() + "\",\"nonce\":\"" + generateNonce() + "\",\"tts\":false,\"flags\":0}";
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("POST");

		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("accept-language", "fr,fr-FR;q=0.9,en-US;q=0.8");
		connection.setRequestProperty("authorization", "MTA5NzkwNTA4ODAxOTg0NTI2MA.GJIDh4.-C6QaRjfJ0yigjAAuC1XCdM9ThmcESjhbOr358");
		connection.setRequestProperty("cache-control", "no-cache");
		connection.setRequestProperty("content-type", "application/json");
		connection.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"112\", \"Google Chrome\";v=\"112\", \"Not:A-Brand\";v=\"99\"");
		connection.setRequestProperty("sec-ch-ua-mobile", "?0");
		connection.setRequestProperty("sec-ch-ua-platform", "\"macOS\"");
		connection.setRequestProperty("sec-fetch-dest", "empty");
		connection.setRequestProperty("sec-fetch-mode", "cors");
		connection.setRequestProperty("sec-fetch-site", "same-origin");
		connection.setRequestProperty("x-debug-options", "bugReporterEnabled");
		connection.setRequestProperty("x-discord-locale", "fr");
		connection.setRequestProperty("x-super-properties", "eyJvcyI6Ik1hYyBPUyBYIiwiYnJvd3NlciI6IkNocm9tZSIsImRldmljZSI6IiIsInN5c3RlbV9sb2NhbGUiOiJmci1GUiIsImJyb3dzZXJfdXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChNYWNpbnRvc2g7IEludGVsIE1hYyBPUyBYIDEwXzE1XzcpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS8xMTIuMC4wLjAgU2FmYXJpLzUzNy4zNiIsImJyb3dzZXJfdmVyc2lvbiI6IjExMi4wLjAuMCIsIm9zX3ZlcnNpb24iOiIxMC4xNS43IiwicmVmZXJyZXIiOiIiLCJyZWZlcnJpbmdfZG9tYWluIjoiIiwicmVmZXJyZXJfY3VycmVudCI6IiIsInJlZmVycmluZ19kb21haW5fY3VycmVudCI6IiIsInJlbGVhc2VfY2hhbm5lbCI6InN0YWJsZSIsImNsaWVudF9idWlsZF9udW1iZXIiOjE5MDUxMSwiY2xpZW50X2V2ZW50X3NvdXJjZSI6bnVsbCwiZGVzaWduX2lkIjowfQ==");
		connection.setRequestProperty("referrer", "https://discord.com/channels/@me/1097906775291859027");
		connection.setRequestProperty("referrerPolicy", "strict-origin-when-cross-origin");
		connection.setRequestProperty("mode", "cors");
		connection.setRequestProperty("credentials", "include");
		connection.setFixedLengthStreamingMode(body.getBytes("UTF-8").length);

		connection.setDoInput(true);
		connection.setDoOutput(true);

		// On écrit dans ce cas le corps de la requête avant
		//  de l'envoyer.
		try (OutputStream os = connection.getOutputStream())
		{
			os.write(body.getBytes("UTF-8"));
			os.close();
		}

		// On retourne enfin le résultat de la requête.
		return ResponseEntity.status(connection.getResponseCode()).build();
	}

	// Envoi une requête HTTP vers l'API de Discord afin de simuler
	//  le fait qu'un utilisateur soit connecté.
	@GetMapping(path = "/api/heartbeat")
	public ResponseEntity<String> SendHeartbeat(HttpServletRequest request) throws IOException
	{
		// On vérifie d'abord si le mot de passe est correct.
		String password = request.getParameter("secret");

		if (password == null || password.isEmpty() || !password.equals("laurine"))
		{
			return ResponseEntity.status(401).build();
		}

		// On vérifie ensuite si un état a été spécifié.
		String state = request.getParameter("state");

		if (state == null || state.isEmpty())
		{
			return ResponseEntity.status(400).build();
		}

		// On effectue alors la liaison aux WebSockets de Discord.
		if (state.equals("1"))
		{
			// On vérifie après que le statut de présence a bien été spécifié.
			String status = request.getParameter("status");

			if (status == null || status.isEmpty())
			{
				return ResponseEntity.status(400).build();
			}

			// On récupère le jeton d'authentification qui doit être utilisé.
			String token = request.getParameter("token");

			if (token == null)
			{
				// Valeur par défaut.
				token = "0";
			}

			gateway.connect(discordTokens.getToken(Integer.parseInt(token)), status);
		}
		else
		{
			gateway.close();
		}

		// On retourne enfin le résultat de la liaison.
		return ResponseEntity.status(200).build();
	}
}