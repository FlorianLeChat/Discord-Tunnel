//
// Route vers la page d'accueil du site.
//
import { useState } from "react";

export default function Home()
{
	// Déclaration des variables.
	const [ code, setCode ] = useState( 0 );
	const [ message, setMessage ] = useState( "" );
	const [ password, setPassword ] = useState( "" );
	const [ typingTimer, setTypingTimer ] = useState<NodeJS.Timer>();

	const updatePassword = ( event: React.ChangeEvent<HTMLInputElement> ) =>
	{
		setPassword( event.target.value );
	};

	const updateMessage = ( event: React.ChangeEvent<HTMLInputElement> ) =>
	{
		setMessage( event.target.value );
	};

	// Permet de démarrer la simulation d'écriture.
	const startTyping = () =>
	{
		const typing = () =>
		{
			fetch( window.location.pathname + "api/typing?secret=" + password )
				.then( response => setCode( response.status ) );

			console.log( "Requête de simulation d'écriture envoyée." );
		};

		typing();

		setTypingTimer( setInterval( typing, 10000 ) );
	};

	// Permet d'arrêter la simulation d'écriture.
	const stopTyping = () =>
	{
		if ( typingTimer )
		{
			clearInterval( typingTimer );

			console.log( "Requête de simulation d'écriture arrêtée." );
		}
	};

	// Permet de démarrer la simulation de présence.
	const startPresence = () =>
	{
		fetch( window.location.pathname + "api/heartbeat?secret=" + password + "&state=1" )
			.then( response => setCode( response.status ) );

		console.log( "Requête de simulation de présence envoyée." );
	};

	// Permet d'arrêter la simulation de présence.
	const stopPresence = () =>
	{
		fetch( window.location.pathname + "api/heartbeat?secret=" + password + "&state=0" )
			.then( response => setCode( response.status ) );

		console.log( "Requête de simulation de présence arrêtée." );
	};

	// Permet d'envoyer un message au travers de l'API de Discord.
	const sendMessage = () =>
	{
		fetch( window.location.pathname + "api/message?secret=" + password + "&message=" + message )
			.then( response => setCode( response.status ) );

		setMessage( "" );

		console.log( "Requête de simulation de messages envoyée." );
	};

	// Affichage du rendu HTML de la page.
	return (
		<section>
			<h1>État d'écriture</h1>

			<button onClick={startTyping}>Appuyez ici pour simuler l'écriture</button>

			<button onClick={stopTyping}>Appuyez ici pour arrêter la simulation</button>

			<h1>Envoi de messages</h1>

			<input type="text" value={message} onChange={updateMessage} placeholder="Message" />

			<button onClick={sendMessage}>Appuyez ici pour envoyer le message</button>

			<h1>Activité en ligne</h1>

			<button onClick={startPresence}>Appuyez ici pour simuler la présence</button>

			<button onClick={stopPresence}>Appuyez ici pour arrêter la présence</button>

			<h2>Sécurité</h2>

			<input type="password" value={password} onChange={updatePassword} placeholder="Mot de passe" />

			Dernier code HTTP : {code}
		</section>
	);
}