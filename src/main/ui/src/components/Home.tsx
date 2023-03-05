//
// Composant de la page d'accueil du site.
//
import { useState } from "react";
import "./Home.scss";

export default function Home()
{
	// Déclaration des variables.
	const [ code, setCode ] = useState( 0 );
	let timer: NodeJS.Timer;

	// Permet d'envoyer une requête à l'API de Discord pour simuler l'écriture d'un message.
	const runApiRequest = () =>
	{
		fetch( "discord" )
			.then( response => setCode( response.status ) );

		console.log( "runApiRequest" );
	};

	// Permet de démarrer l'envoi de requêtes à l'API de Discord.
	const startSimulation = () =>
	{
		timer = setInterval( runApiRequest, 5000 );

		alert( "Simulation démarrée." );
	};

	// Permet d'arrêter l'envoi de requêtes à l'API de Discord.
	const stopSimulation = () =>
	{
		if ( timer )
		{
			clearInterval( timer );

			alert( "Simulation arrêtée." );
		}
	};

	// Affichage du rendu HTML du composant.
	return (
		<section id="Home">
			<button onClick={startSimulation}>Appuyez ici pour simuler l'écriture.</button>

			<button onClick={stopSimulation}>Appuyez ici pour arrêter la simulation.</button>

			Dernier code HTTP : {code}
		</section>
	);
}