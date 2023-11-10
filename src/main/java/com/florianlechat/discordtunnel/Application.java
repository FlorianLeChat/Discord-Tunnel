package com.florianlechat.discordtunnel;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.io.FileReader;
import java.io.BufferedReader;
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
			// On tente d'abord le fichier contenant la requête.
			File file = new File("src/main/resources/request.txt");

			if (file.exists())
			{
				// Si le fichier existe, on lit ensuite seulement la première ligne.
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				reader.close();

				// On effectue enfin la connexion au WebSocket.
				logMessage("Connexion automatique au WebSocket...");

				URL url = new URI(line).toURL();
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				connection.getResponseCode();
    	    }
		};
	}
}