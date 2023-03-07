//
// Route vers la page d'accueil du site.
//
import { useState } from "react";

export default function Home()
{
	// Déclaration des variables.
	const [ code, setCode ] = useState( 0 );
	const [ timer, setTimer ] = useState<NodeJS.Timer>();
	const [ password, setPassword ] = useState( "" );

	const updatePassword = ( event: React.ChangeEvent<HTMLInputElement> ) =>
	{
		setPassword( event.target.value );
	};

	// Permet d'envoyer une requête à l'API de Discord pour simuler l'écriture d'un message.
	const runApiRequest = () =>
	{
		fetch( window.location.pathname + "api/typing?secret=" + password )
			.then( response => setCode( response.status ) );

		console.log( "runApiRequest" );
	};

	// Permet de démarrer l'envoi de requêtes à l'API de Discord.
	const startSimulation = () =>
	{
		setTimer( setInterval( runApiRequest, 5000 ) );

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

	// Affichage du rendu HTML de la page.
	return (
		<section>
			<button onClick={startSimulation}>Appuyez ici pour simuler l'écriture</button>

			<button onClick={stopSimulation}>Appuyez ici pour arrêter la simulation</button>

			<input type="text" value={password} onChange={updatePassword} />

			Dernier code HTTP : {code}
		</section>
	);
}