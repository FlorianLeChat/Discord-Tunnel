//
// Route vers la page d'accueil du site.
//
import { useState } from "react";

export default function Home()
{
	// Déclaration des variables.
	const [ code, setCode ] = useState( 0 );
	const [ timer, setTimer ] = useState<NodeJS.Timer>();
	const [ message, setMessage ] = useState( "" );
	const [ password, setPassword ] = useState( "" );

	const updatePassword = ( event: React.ChangeEvent<HTMLInputElement> ) =>
	{
		setPassword( event.target.value );
	};

	const updateMessage = ( event: React.ChangeEvent<HTMLInputElement> ) =>
	{
		setMessage( event.target.value );
	};

	// Permet de démarrer l'envoi de requêtes à l'API de Discord.
	const startSimulation = () =>
	{
		setTimer( setInterval( () =>
		{
			fetch( window.location.pathname + "api/typing?secret=" + password )
				.then( response => setCode( response.status ) );

			console.log( "Requête de simulation d'écriture envoyée." );
		}, 5000 ) );

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

	// Permet d'envoyer un message au travers de l'API de Discord.
	const sendMessage = () =>
	{
		setTimer( setInterval( () =>
		{
			fetch( window.location.pathname + "api/message?secret=" + password + "&message=" + message )
				.then( response => setCode( response.status ) );

			console.log( "Requête de simulation de messages envoyée." );
		}, 5000 ) );
	};

	// Affichage du rendu HTML de la page.
	return (
		<section>
			<button onClick={startSimulation}>Appuyez ici pour simuler l'écriture</button>

			<button onClick={stopSimulation}>Appuyez ici pour arrêter la simulation</button>

			<button onClick={sendMessage}>Appuyez ici pour envoyer le message</button>

			<input type="text" value={password} onChange={updateMessage} placeholder="Message" />

			<input type="text" value={password} onChange={updatePassword} placeholder="Mot de passe" />

			Dernier code HTTP : {code}
		</section>
	);
}