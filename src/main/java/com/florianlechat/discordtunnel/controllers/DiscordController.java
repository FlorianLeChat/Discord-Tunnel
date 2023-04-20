package com.florianlechat.discordtunnel.controllers;

import java.net.URL;
import java.util.Base64;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.net.HttpURLConnection;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscordController
{
	// Permet de générer un nombre arbitraire en Base64.
    public static String generateNonce()
	{
		// On définit la longueur du nombre arbitraire
		//  avant de créer un tableau de bytes pour le stocker.
        int nonceLength = 19;
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

	// Permet d'effectuer une requête HTTP vers l'API de Discord
	//  afin de simuler le fait qu'un utilisateur est en train de
	//  taper un message.
	//  Source : https://discord.com/developers/docs/resources/channel#trigger-typing-indicator
	@GetMapping(path = "/api/typing")
	public ResponseEntity<String> SendTyping(HttpServletRequest request) throws IOException
	{
		// On vérifie d'abord si le mot de passe est correct.
		String password = request.getParameter("secret");

		if (password == null || password.isEmpty() || !password.equals("laurine"))
		{
			return ResponseEntity.status(401).build();
		}

		// On effectue ensuite la requête HTTP.
		URL url = new URL("https://discord.com/api/v9/channels/1097906775291859027/typing");
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

		connection.connect();

		// On retourne enfin le résultat de la requête.
		return ResponseEntity.status(connection.getResponseCode()).build();
	}

	// Permet d'effectuer une requête HTTP vers l'API de Discord
	//  afin de simuler le fait qu'un utilisateur envoie un message
	//  dans un salon.
	//  Source : https://discord.com/developers/docs/resources/channel#create-message
	@GetMapping(path = "/api/message")
	public ResponseEntity<String> SendMessage(HttpServletRequest request) throws IOException
	{
		// On vérifie d'abord si le mot de passe est correct.
		String password = request.getParameter("secret");

		if (password == null || password.isEmpty() || !password.equals("laurine"))
		{
			return ResponseEntity.status(401).build();
		}

		// On effectue ensuite la requête HTTP.
		URL url = new URL("https://discord.com/api/v9/channels/1097906775291859027/messages");
		String body = "{\"content\":\"" + request.getParameter("message") + "\",\"nonce\":\"" + generateNonce() + "\",\"tts\":false,\"flags\":0}";
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

		// On écrit alors le corps de la requête avant
		//  de l'envoyer.
		OutputStream os = connection.getOutputStream();
		os.write(body.getBytes("UTF-8"));
		os.close();

		connection.connect();

		// On retourne enfin le résultat de la requête.
		return ResponseEntity.status(connection.getResponseCode()).build();
	}

	// Permet d'effectuer une requête HTTP vers l'API de Discord
	//  afin de simuler le fait qu'un utilisateur soit connecté.
	@GetMapping(path = "/api/heartbeat")
	public ResponseEntity<String> SendHeartbeat(HttpServletRequest request) throws IOException
	{
		// On vérifie d'abord si le mot de passe est correct.
		String password = request.getParameter("secret");

		if (password == null || password.isEmpty() || !password.equals("laurine"))
		{
			return ResponseEntity.status(401).build();
		}

		// On effectue ensuite la requête HTTP.
		URL url = new URL("https://discord.com/api/v9/science");
		long epoch = System.currentTimeMillis();
		String body = "{\"token\":\"MTA5NzkwNTA4ODAxOTg0NTI2MA.tTzqjz544OwGVb7m-ITFBqn2_fY\",\"events\":[{\"type\":\"client_heartbeat\",\"properties\":{\"client_track_timestamp\":" + epoch + ",\"client_heartbeat_session_id\":\"f184b204-8f95-425b-a430-4b4d0b843176\",\"client_heartbeat_initialization_timestamp\":1681976469887,\"client_heartbeat_version\":7,\"client_performance_memory\":0,\"accessibility_features\":524544,\"rendered_locale\":\"fr\",\"accessibility_support_enabled\":false,\"client_uuid\":\"jFCEUNyKPA8tUHCe69ScnYcBAAAFAAAA\",\"client_send_timestamp\":" + epoch + "}}]}";
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

		// On écrit alors le corps de la requête avant
		//  de l'envoyer.
		OutputStream os = connection.getOutputStream();
		os.write(body.getBytes("UTF-8"));
		os.close();

		connection.connect();

		// On retourne enfin le résultat de la requête.
		return ResponseEntity.status(connection.getResponseCode()).build();
	}
}