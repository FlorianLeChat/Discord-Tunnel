package com.florianlechat.discordtunnel.components;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@Configuration("discordTokens")
@ConfigurationProperties(prefix = "tunnel")
public class ServerProperties
{
    private Map<String, String> tokens;

	// Définition/récupération d'un seul jeton.
	public String setToken(String token)
	{
		String key = String.valueOf(this.tokens.size());

		this.tokens.put(key, token);

		return key;
	}

	public String getToken(int index)
	{
		return (String) this.tokens.values().toArray()[index];
	}

	// Définition/récupération de l'ensemble des jetons.
	public Map<String, String> setTokens(Map<String, String> tokens)
	{
		this.tokens = tokens;

		return this.tokens;
	}

	public Map<String, String> getTokens()
	{
		return this.tokens;
	}
}