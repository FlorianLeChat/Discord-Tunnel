//
// Page utilisée pour générer le contenu statique HTML de la page.
// 	Source : https://nextjs.org/docs/advanced-features/custom-app
//

// Importation de la feuille de style CSS globale.
import "./index.css";

// Importation du normalisateur TypeScript.
import "@total-typescript/ts-reset";

// Importation des dépendances.
import Head from "next/head";
import { useRouter } from "next/router";

// Importation des types.
import type { AppProps } from "next/app";

export default function DiscordTunnel( { Component, pageProps }: AppProps )
{
	// Déclaration des constantes.
	const { basePath } = useRouter();

	// Génération de la structure de la page.
	return (
		<>
			<Head>
				{/* Méta-données du document */}
				<meta charSet="utf-8" />
				<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover" />

				{/* Titre du document */}
				<title>Discord Tunnel</title>

				{/* Icônes et manifeste du document */}
				<link rel="icon" type="image/webp" sizes="16x16" href={basePath + "/assets/favicons/16x16.webp"} />
				<link rel="icon" type="image/webp" sizes="32x32" href={basePath + "/assets/favicons/32x32.webp"} />
				<link rel="icon" type="image/webp" sizes="48x48" href={basePath + "/assets/favicons/48x48.webp"} />
				<link rel="icon" type="image/webp" sizes="192x192" href={basePath + "/assets/favicons/192x192.webp"} />
				<link rel="icon" type="image/webp" sizes="512x512" href={basePath + "/assets/favicons/512x512.webp"} />

				<link rel="apple-touch-icon" href={basePath + "/assets/favicons/180x180.webp"} />
				<link rel="manifest" href={basePath + "/manifest.json"} />
			</Head>

			{/* Affichage du composant demandé */}
			<main>
				<Component {...pageProps} />
			</main>
		</>
	);
};