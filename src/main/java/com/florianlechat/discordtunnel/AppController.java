package com.florianlechat.discordtunnel;

import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController
{
	// Permet de servir le contenu statique du site.
	@RequestMapping("/")
	public String index() throws IOException
	{
		return "index.html";
	}
}