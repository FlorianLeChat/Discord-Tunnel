//
// Route vers la page principale du site.
//  Source : https://nextjs.org/docs/app/building-your-application/routing/pages-and-layouts#pages
//

import type { Metadata } from "next";
import { Suspense, lazy } from "react";

const HomePage = lazy( () => import( "@/components/home-page" ) );

export const metadata: Metadata = {
	title: "Discord Tunnel",
	viewport: "width=device-width, initial-scale=1, viewport-fit=cover",
	manifest: "manifest.json",
	icons: [
		{
			url: "assets/favicons/16x16.webp",
			type: "image/webp",
			sizes: "16x16"
		},
		{
			url: "assets/favicons/32x32.webp",
			type: "image/webp",
			sizes: "32x32"
		},
		{
			url: "assets/favicons/48x48.webp",
			type: "image/webp",
			sizes: "48x48"
		},
		{
			url: "assets/favicons/180x180.webp",
			type: "image/webp",
			sizes: "180x180"
		},
		{
			url: "assets/favicons/192x192.webp",
			type: "image/webp",
			sizes: "192x192"
		},
		{
			url: "assets/favicons/512x512.webp",
			type: "image/webp",
			sizes: "512x512"
		}
	]
};

export default function Page()
{
	// Affichage du rendu HTML de la page.
	return (
		<Suspense fallback={<p>Chargement en cours...</p>}>
			<HomePage />
		</Suspense>
	);
}