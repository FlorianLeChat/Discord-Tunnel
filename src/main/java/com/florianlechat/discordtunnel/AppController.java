package com.florianlechat.discordtunnel;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController
{
	// Permet de rediriger l'ensemble des requêtes vers
	//	le serveur NextJS utilisé pour servir le front-end.
	@RequestMapping({"/"})
	public String index(HttpServletResponse response) throws IOException
	{
		response.sendRedirect("http://localhost:3000");

		return "Redirecting to http://localhost:3000";
	}
}