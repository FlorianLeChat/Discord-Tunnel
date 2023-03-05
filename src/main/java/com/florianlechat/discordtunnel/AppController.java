package com.florianlechat.discordtunnel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController
{
	@GetMapping("/")
	public String index()
	{
		return "forward:/index.html";
	}
}