//
// Route vers la page d'accueil du site.
//
import { useState } from "react";

export default function Home()
{
	// Déclaration des variables.
	const [ code, setCode ] = useState( 0 );
	const [ delay, setDelay ] = useState( 0 );
	const [ message, setMessage ] = useState( "" );
	const [ password, setPassword ] = useState( "" );
	const [ typingTimer, setTypingTimer ] = useState<NodeJS.Timer>();

	// Permet de mettre à jour le mot de passe.
	const updatePassword = ( event: React.ChangeEvent<HTMLInputElement> ) =>
	{
		setPassword( event.target.value );
	};

	// Permet de mettre à jour le message qui sera envoyé.
	const updateMessage = ( event: React.ChangeEvent<HTMLTextAreaElement> ) =>
	{
		setMessage( event.target.value );
	};

	// Permet de mettre à jour le délai entre chaque envoi de message.
	const updateDelay = ( event: React.ChangeEvent<HTMLInputElement> ) =>
	{
		setDelay( parseInt( event.target.value ) );
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
		setTimeout( () =>
		{
			fetch( window.location.pathname + "api/message?secret=" + password + "&message=" + message )
				.then( response => setCode( response.status ) );

			setMessage( "" );

			console.log( "Requête de simulation de messages envoyée." );
		}, delay * 1000 );
	};

	// Affichage du rendu HTML de la page.
	return (


		<section className="flex flex-col gap-4 items-start m-4">
			<h1 className="text-3xl font-bold underline">État d'écriture</h1>

			<button onClick={startTyping} className="bg-green-500 rounded-lg p-2">Appuyez ici pour simuler l'écriture</button>

			<button onClick={stopTyping} className="bg-red-500 rounded-lg p-2">Appuyez ici pour arrêter la simulation</button>

			<h1 className="text-3xl font-bold underline">Envoi de messages</h1>

			<textarea value={message} onChange={updateMessage} placeholder="Message" className="border-2 border-black" />

			<button onClick={sendMessage} className="bg-blue-500 rounded-lg p-2">Appuyez ici pour envoyer le message</button>

			<input type="number" value={delay} onChange={updateDelay} placeholder="Délai en secondes" className="border-2 border-black" />

			<h1 className="text-3xl font-bold underline">Activité en ligne</h1>

			<button onClick={startPresence} className="bg-green-500 rounded-lg p-2">Appuyez ici pour simuler la présence</button>

			<button onClick={stopPresence} className="bg-red-500 rounded-lg p-2">Appuyez ici pour arrêter la présence</button>

			<h2 className="text-2xl font-bold underline">Sécurité</h2>

			<input type="password" value={password} onChange={updatePassword} placeholder="Mot de passe" className="border-2 border-black" />

			Dernier code HTTP : {code}
		</section>
	);
}