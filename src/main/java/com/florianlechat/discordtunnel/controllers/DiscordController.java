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
	public ResponseEntity<String> MakeHttpRequest(HttpServletRequest request) throws IOException
	{
		// On vérifie d'abord si le mot de passe est correct.
		String password = request.getParameter("secret");

		if (password == null || password.isEmpty() || !password.equals("laurine"))
		{
			return ResponseEntity.status(401).build();
		}

		// On effectue ensuite la requête HTTP.
		URL url = new URL("https://discord.com/api/v9/channels/771427562484138025/typing");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("POST");

		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("accept-language", "fr,fr-FR;q=0.9,en-US;q=0.8");
		connection.setRequestProperty("authorization", "MTgzMjcyNDExMTY3MzI2MjA5.Gbllme.mppZHM4ZllEWVg1cWgzTzEyU1hTeFZNNXZkQTF2VURBZGQ5ZTljNzV3YzRKMWhXaHd3NFNzZUpRcTJuajYtR3BLNlVueFd1a2JQYWRvT0d2");
		connection.setRequestProperty("sec-fetch-dest", "empty");
		connection.setRequestProperty("sec-fetch-mode", "cors");
		connection.setRequestProperty("sec-fetch-site", "same-origin");
		connection.setRequestProperty("x-debug-options", "bugReporterEnabled");
		connection.setRequestProperty("x-discord-locale", "fr");
		connection.setRequestProperty("x-super-properties", "eyJvcyI6IldpbmRvd3MiLCJicm93c2VyIjoiRGlzY29yZCBDbGllbnQiLCJyZWxlYXNlX2NoYW5uZWwiOiJzdGFibGUiLCJjbGllbnRfdmVyc2lvbiI6IjEuMC45MDExIiwib3NfdmVyc2lvbiI6IjEwLjAuMTkwNDUiLCJvc19hcmNoIjoieDY0Iiwic3lzdGVtX2xvY2FsZSI6ImZyIiwiY2xpZW50X2J1aWxkX251bWJlciI6MTc4NTkwLCJuYXRpdmVfYnVpbGRfbnVtYmVyIjoyOTU4NCwiY2xpZW50X2V2ZW50X3NvdXJjZSI6bnVsbCwiZGVzaWduX2lkIjowfQ==");
		connection.setRequestProperty("referrer", "https://discord.com/channels/401777889449607178/771427562484138025");
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