//
// Composant client des contrôles de l'API Discord sur la page d'accueil.
//

"use client";

import { useState, ChangeEvent } from "react";

export default function HomePage()
{
	// Déclaration des variables d'état.
	const [ code, setCode ] = useState( 0 );
	const [ delay, setDelay ] = useState( 0 );
	const [ token, setToken ] = useState( "0" );
	const [ status, setStatus ] = useState( "online" );
	const [ message, setMessage ] = useState( "" );
	const [ password, setPassword ] = useState( "" );
	const [ typingTimer, setTypingTimer ] = useState<NodeJS.Timer>();

	// Permet de récupérer l'URL de l'application.
	const getUrl = () => ( process.env.NODE_ENV === "production" ? window.location.pathname : "http://localhost:8080/" );

	// Permet de mettre à jour le mot de passe.
	const updatePassword = ( event: ChangeEvent<HTMLInputElement> ) => setPassword( event.target.value );

	// Permet de mettre à jour le message qui sera envoyé.
	const updateMessage = ( event: ChangeEvent<HTMLTextAreaElement> ) => setMessage( event.target.value );

	// Permet de mettre à jour le token qui sera utilisé.
	const updateToken = ( event: ChangeEvent<HTMLSelectElement> ) => setToken( event.target.value );

	// Permet de mettre à jour le statut de présence qui sera utilisé.
	const updateStatus = ( event: ChangeEvent<HTMLSelectElement> ) => setStatus( event.target.value );

	// Permet de mettre à jour le délai entre chaque envoi de message.
	const updateDelay = ( event: ChangeEvent<HTMLInputElement> ) =>
	{
		setDelay( parseInt( event.target.value, 10 ) );
	};

	// Permet de démarrer la simulation d'écriture.
	const startTyping = () =>
	{
		const typing = () =>
		{
			fetch( `${ getUrl() }api/typing?secret=${ password }` )
				.then( ( response ) => setCode( response.status ) );
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
		}
	};

	// Permet de démarrer la simulation de présence.
	const startPresence = () =>
	{
		fetch( `${ getUrl() }api/heartbeat?secret=${ password }&token=${ token }&status=${ status }&state=1` )
			.then( ( response ) => setCode( response.status ) );
	};

	// Permet d'arrêter la simulation de présence.
	const stopPresence = () =>
	{
		fetch( `${ getUrl() }api/heartbeat?secret=${ password }&state=0` )
			.then( ( response ) => setCode( response.status ) );
	};

	// Permet d'envoyer un message au travers de l'API de Discord.
	const sendMessage = () =>
	{
		// Arrêt de la simulation d'écriture.
		stopTyping();

		// Envoi du message avec ou sans délai.
		fetch( `${ getUrl() }api/message?secret=${ password }&message=${ message }&delay=${ delay }` )
			.then( ( response ) =>
			{
				setCode( response.status );
			} );

		// Réinitialisation des champs de saisie.
		setDelay( 0 );
		setMessage( "" );
	};

	// Affichage du rendu HTML de la page.
	return (
		<section className="flex flex-col gap-4 items-start m-4">
			<h1 className="text-3xl font-bold underline">État d&apos;écriture</h1>

			<button
				type="button" onClick={startTyping}
				className="bg-green-500 rounded-lg p-2"
			>
				Appuyez ici pour simuler l&apos;écriture
			</button>

			<button
				type="button" onClick={stopTyping}
				className="bg-red-500 rounded-lg p-2"
			>
				Appuyez ici pour arrêter la simulation
			</button>

			<h1 className="text-3xl font-bold underline">Envoi de messages</h1>

			<textarea value={message} onChange={updateMessage} placeholder="Message" className="border-2 border-black" />

			<button
				type="button" onClick={sendMessage}
				className="bg-blue-500 rounded-lg p-2"
			>
				Appuyez ici pour envoyer le message
			</button>

			<input
				type="number" value={delay} onChange={updateDelay}
				placeholder="Délai en secondes" className="border-2 border-black"
			/>

			<h1 className="text-3xl font-bold underline">Activité en ligne</h1>

			<button
				type="button" onClick={startPresence}
				className="bg-green-500 rounded-lg p-2"
			>
				Appuyez ici pour simuler la présence
			</button>

			<button
				type="button" onClick={stopPresence}
				className="bg-red-500 rounded-lg p-2"
			>
				Appuyez ici pour arrêter la présence
			</button>

			<select className="border-2 border-black" onChange={updateToken}>
				<option value="0">Token 1</option>
				<option value="1">Token 2</option>
			</select>

			<select className="border-2 border-black" onChange={updateStatus}>
				<option value="online">En ligne</option>
				<option value="idle">Inactif</option>
				<option value="dnd">Ne pas déranger</option>
				<option value="offline">Hors ligne</option>
			</select>

			<h2 className="text-2xl font-bold underline">Sécurité</h2>

			<input
				type="password" value={password} onChange={updatePassword}
				placeholder="Mot de passe" className="border-2 border-black"
			/>

			Dernier code HTTP : {code}
		</section>
	);
}