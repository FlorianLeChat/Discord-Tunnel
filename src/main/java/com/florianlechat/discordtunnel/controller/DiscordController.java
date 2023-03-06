package com.florianlechat.discordtunnel.controller;

import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class DiscordController
{
	@GetMapping(path = "/api/typing")
	public ResponseEntity<String> MakeHttpRequest() throws IOException
	{
		URL url = new URL("https://www.google.com");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("GET");

		int responseCode = connection.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK)
		{
			return ResponseEntity.ok("GET request worked.");
		}

		return ResponseEntity.status(500).body("GET request failed.");
	}
}