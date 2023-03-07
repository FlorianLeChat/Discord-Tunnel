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
		URL url = new URL("https://www.google.com");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("GET");

		// On retourne enfin le résultat de la requête.
		return ResponseEntity.status(connection.getResponseCode()).build();
	}
}