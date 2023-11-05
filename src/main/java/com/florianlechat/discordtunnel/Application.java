package com.florianlechat.discordtunnel;

import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;
import static com.florianlechat.discordtunnel.controllers.DiscordGateway.logMessage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application
{
	// Point d'entrée de l'application.
	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}

	// Lancement automatique de la connexion au WebSocket
	//  en utilisant les derniers paramètres de connexion.
	@Bean
	public CommandLineRunner CommandLineRunnerBean()
	{
		return (args) ->
		{
			logMessage("Connexion automatique au WebSocket...");

			URL url = new URI("http://localhost:8080/api/heartbeat?secret=laurine&token=1&status=idle&activity=%F0%9F%98%AA%20Gar%C3%A7on%20stupide..&state=1").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.getResponseCode();
		};
	}
}