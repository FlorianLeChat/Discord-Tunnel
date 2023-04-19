package com.florianlechat.discordtunnel.controllers;

import java.net.URL;
import java.io.IOException;
import java.net.HttpURLConnection;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscordController
{
	// Permet d'effectuer une requête HTTP vers l'API de Discord
	//	afin de simuler le fait qu'un utilisateur est en train de
	//	taper un message.
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
		connection.setRequestProperty("accept-language", "fr,fr-FR;q=0.9,en-US;q=0.8");
		connection.setRequestProperty("authorization", "MTA5NzkwNTA4ODAxOTg0NTI2MA.Gn2uds.jHJQ_WN2WIF7fOOuobeVVb1eAIa2Ut29GQpA-w");
		connection.setRequestProperty("sec-fetch-dest", "empty");
		connection.setRequestProperty("sec-fetch-mode", "cors");
		connection.setRequestProperty("sec-fetch-site", "same-origin");
		connection.setRequestProperty("x-debug-options", "bugReporterEnabled");
		connection.setRequestProperty("x-discord-locale", "fr");
		connection.setRequestProperty("x-super-properties", "eyJvcyI6Ik1hYyBPUyBYIiwiYnJvd3NlciI6IkNocm9tZSIsImRldmljZSI6IiIsInN5c3RlbV9sb2NhbGUiOiJmci1GUiIsImJyb3dzZXJfdXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChNYWNpbnRvc2g7IEludGVsIE1hYyBPUyBYIDEwXzE1XzcpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS8xMTIuMC4wLjAgU2FmYXJpLzUzNy4zNiIsImJyb3dzZXJfdmVyc2lvbiI6IjExMi4wLjAuMCIsIm9zX3ZlcnNpb24iOiIxMC4xNS43IiwicmVmZXJyZXIiOiIiLCJyZWZlcnJpbmdfZG9tYWluIjoiIiwicmVmZXJyZXJfY3VycmVudCI6IiIsInJlZmVycmluZ19kb21haW5fY3VycmVudCI6IiIsInJlbGVhc2VfY2hhbm5lbCI6InN0YWJsZSIsImNsaWVudF9idWlsZF9udW1iZXIiOjE5MDUxMSwiY2xpZW50X2V2ZW50X3NvdXJjZSI6bnVsbCwiZGVzaWduX2lkIjowfQ==");
		connection.setRequestProperty("referrer", "https://discord.com/channels/@me/1097906775291859027");
		connection.setRequestProperty("referrerPolicy", "strict-origin-when-cross-origin");
		connection.setRequestProperty("Credentials", "include");
		connection.setRequestProperty("Content-Length", "0");

		connection.setDoInput(true);
		connection.setDoOutput(true);

		connection.connect();

		// On retourne enfin le résultat de la requête.
		return ResponseEntity.status(connection.getResponseCode()).build();
	}
}