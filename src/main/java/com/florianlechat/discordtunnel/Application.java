package com.florianlechat.discordtunnel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application
{
	// Point d'entrée de l'application.
	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
}