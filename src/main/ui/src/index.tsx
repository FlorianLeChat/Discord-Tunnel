// Importation de la feuille de style CSS.
import "./index.scss";

// Importation de React et de ses dépendances.
import { createRoot } from "react-dom/client";
import { StrictMode, lazy } from "react";

// Importation des composants React.
const Home = lazy( () => import( "./components/Home" ) );

// Génération de la page.
const root = createRoot( document.querySelector( "body > main" ) as Element );
root.render(
	<StrictMode>
		<Home />
	</StrictMode>
);