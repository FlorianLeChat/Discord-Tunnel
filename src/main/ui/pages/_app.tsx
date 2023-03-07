//
// Page utilisée pour générer le contenu statique HTML de la page.
// 	Source : https://nextjs.org/docs/advanced-features/custom-app
//

// Importation de la feuille de style CSS globale.
import "./index.scss";

// Importation des dépendances.
import Head from "next/head";

// Importation des types.
import type { AppProps } from "next/app";

export default function DiscordTunnel( { Component, pageProps }: AppProps )
{
	// Génération de la structure de la page.
	return (
		<>
			<Head>
				{/* Méta-données du document */}
				<meta charSet="utf-8" />
				<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover" />

				{/* Titre du document */}
				<title>Discord Tunnel</title>
			</Head>

			{/* Affichage du composant demandé */}
			<main>
				<Component {...pageProps} />
			</main>
		</>
	);
};