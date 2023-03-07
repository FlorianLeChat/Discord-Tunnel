package com.florianlechat.discordtunnel;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class Application
{
	// Point d'entrée de l'application.
	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
}