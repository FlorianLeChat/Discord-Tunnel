//
// Composant client des contrôles de l'API Discord sur la page d'accueil.
//

"use client";

import { useState, type ChangeEvent } from "react";

export default function HomePage()
{
	// Déclaration des variables d'état.
	const [ code, setCode ] = useState( 0 );
	const [ delay, setDelay ] = useState( 0 );
	const [ token, setToken ] = useState( "0" );
	const [ status, setStatus ] = useState( "online" );
	const [ message, setMessage ] = useState( "" );
	const [ activity, setActivity ] = useState( "" );
	const [ password, setPassword ] = useState( "" );
	const [ typingTimer, setTypingTimer ] = useState<NodeJS.Timeout>();

	// Récupère l'URL de l'application.
	const getUrl = () => ( process.env.NODE_ENV === "production"
		? window.location.pathname
		: "http://localhost:8080/" );

	// Mise à jour du mot de passe qui sera utilisé.
	const updatePassword = ( event: ChangeEvent<HTMLInputElement> ) =>
	{
		setPassword( event.target.value );
	};

	// Mise à jour de l'activity qui sera définit.
	const updateActivity = ( event: ChangeEvent<HTMLInputElement> ) =>
	{
		setActivity( event.target.value );
	};

	// Mise à jour du message qui sera envoyé.
	const updateMessage = ( event: ChangeEvent<HTMLTextAreaElement> ) =>
	{
		setMessage( event.target.value );
	};

	// Mise à jour du jeton d'authentification qui sera utilisé.
	const updateToken = ( event: ChangeEvent<HTMLSelectElement> ) =>
	{
		setToken( event.target.value );
	};

	// Mise à jour du statut de présence qui sera définit.
	const updateStatus = ( event: ChangeEvent<HTMLSelectElement> ) =>
	{
		setStatus( event.target.value );
	};

	// Mise à jour du délai entre chaque envoi de message.
	const updateDelay = ( event: ChangeEvent<HTMLInputElement> ) =>
	{
		setDelay( parseInt( event.target.value, 10 ) );
	};

	// Démarrage de la simulation d'écriture.
	const startTyping = () =>
	{
		const typing = () =>
		{
			fetch( `${ getUrl() }api/typing?secret=${ password }` ).then( ( response ) => setCode( response.status ) );
		};

		typing();

		setTypingTimer( setInterval( typing, 10000 ) );
	};

	// Arrêt de la simulation d'écriture.
	const stopTyping = () =>
	{
		if ( typingTimer )
		{
			clearInterval( typingTimer );
		}
	};

	// Démarrage de la simulation de présence.
	const startPresence = () =>
	{
		fetch(
			`${ getUrl() }api/heartbeat?secret=${ password }&token=${ token }&status=${ status }&activity=${ activity }&state=1`
		).then( ( response ) => setCode( response.status ) );
	};

	// Arrêt de la simulation de présence.
	const stopPresence = () =>
	{
		fetch( `${ getUrl() }api/heartbeat?secret=${ password }&state=0` ).then(
			( response ) => setCode( response.status )
		);
	};

	// Envoi d'un message à travers l'API de Discord.
	const sendMessage = () =>
	{
		// Arrêt de la simulation d'écriture.
		stopTyping();

		// Envoi du message avec ou sans délai.
		fetch(
			`${ getUrl() }api/message?secret=${ password }&message=${ message }&delay=${ delay }`
		).then( ( response ) => setCode( response.status ) );

		// Réinitialisation des champs de saisie.
		setDelay( 0 );
		setMessage( "" );
	};

	// Affichage du rendu HTML de la page.
	return (
		<section className="flex flex-col gap-4 items-start m-4">
			<h1 className="text-3xl font-bold underline">
				État d&lsquo;écriture
			</h1>

			<button
				type="button"
				onClick={startTyping}
				className="bg-green-500 rounded-lg p-2"
			>
				Appuyez ici pour simuler l&lsquo;écriture
			</button>

			<button
				type="button"
				onClick={stopTyping}
				className="bg-red-500 rounded-lg p-2"
			>
				Appuyez ici pour arrêter la simulation
			</button>

			<h1 className="text-3xl font-bold underline">Envoi de messages</h1>

			<textarea
				value={message}
				onChange={updateMessage}
				className="border-2 border-black"
				placeholder="Message"
			/>

			<button
				type="button"
				onClick={sendMessage}
				className="bg-blue-500 rounded-lg p-2"
			>
				Appuyez ici pour envoyer le message
			</button>

			<input
				type="number"
				value={delay}
				onChange={updateDelay}
				className="border-2 border-black"
				placeholder="Délai en secondes"
			/>

			<h1 className="text-3xl font-bold underline">Activité en ligne</h1>

			<button
				type="button"
				onClick={startPresence}
				className="bg-green-500 rounded-lg p-2"
			>
				Appuyez ici pour simuler la présence
			</button>

			<button
				type="button"
				onClick={stopPresence}
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

			<input
				type="text"
				value={activity}
				onChange={updateActivity}
				className="border-2 border-black"
				placeholder="Salut tout le monde !"
			/>

			<h2 className="text-2xl font-bold underline">Sécurité</h2>

			<input
				type="password"
				value={password}
				onChange={updatePassword}
				className="border-2 border-black"
				placeholder="Mot de passe"
			/>

			Dernier code HTTP : {code}
		</section>
	);
}