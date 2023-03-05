// Importation de la feuille de style CSS.
import "./index.css";

// Importation de React et de ses dépendances.
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

// Génération de la page.
const root = createRoot( document.querySelector( "body > main" ) );
root.render(
	<StrictMode>
		<h1>Ceci est un test</h1>
	</StrictMode>
);