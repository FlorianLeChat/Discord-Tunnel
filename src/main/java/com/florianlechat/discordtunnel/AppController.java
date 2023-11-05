package com.florianlechat.discordtunnel;

import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController
{
	// Ajout du contenu statique pour le site web.
	@RequestMapping()
	public String index() throws IOException
	{
		return "index.html";
	}
}