# :left_right_arrow: Discord Tunnel

## In French

> [!CAUTION]
> **Ce projet n'est plus maintenu depuis mars 2024. Si vous comptez utiliser la totalité ou une partie du code source, veuillez noter que les dépendances ne seront plus à jour et que vous vous exposez à des vulnérabilités de sécurité si des mesures adéquates ne sont pas mises en place.**

Initialement créé en mars 2023 comme un entraînement au langage Java et au [*framework* Spring](https://spring.io/projects/spring-framework) pour mes études, j'ai rapidement eu l'idée de créer une passerelle/tunnel sous forme d'un petit site Internet vers l'[API Discord](https://discord.com/developers/docs/topics/gateway) sans utiliser une quelconque bibliothèque tierce. Le site Internet est alors capable de se connecter à l'API de Discord comme si vous le faisiez habituellement dans l'application ou dans le site officiel. Une fois connecté, le site Internet propose plusieurs fonctionnalités (**limitées**) mais simples à utiliser :

- <ins>Simulation de l'état d'écriture</ins> : il est possible d'activer ou de désactiver une simulation de l'état d'écriture (« XXX est en train d'écrire... ») de manière infinie même si vous n'écrivez strictement rien.
- <ins>Mise à jour de l'état de présence</ins> : des options sont disponibles afin de modifier l'état de présence (en ligne, inactif, ne pas déranger...) et d'inscrire un message de statut relatif ou non à son activité.
- <ins>Envoi de messages</ins> : tout est dans le titre, il est également réalisable d'envoyer des messages avec ou non un délai d'envoi. L'envoi n'est pas instantané et le site s'occupe d'activer l'état d'écriture durant le temps nécessaire avant d'envoyer le texte (comme si un humain était en train de taper sur le clavier).

> [!NOTE]
> Voici les exigences pour exécuter le site Internet :
> * [**Toute** version de NodeJS LTS maintenue](https://github.com/nodejs/release#release-schedule)
> * [**Toute** version de Java LTS maintenue](https://www.oracle.com/java/technologies/java-se-support-roadmap.html)

> [!TIP]
> Pour essayer le projet, il suffit d'installer l'ensemble des dépendances nécessaires avec la commande `npm install` (nécessite [NodeJS](https://nodejs.org/en/download)), de compiler les fichiers statiques de NextJS avec `npm run build` puis de lancer le serveur de développement Java (nécessite [Java](https://www.java.com/en/download/)). Une image Docker est aussi disponible pour tester ce projet pour les personnes les plus expérimentées ! 🐳

> [!WARNING]
> L'entièreté du code de ce projet est commenté dans ma langue natale (en français) et n'est pas voué à être traduit en anglais étant donné qu'il s'agissait d'un projet privé personnel.

___

## In English

> [!CAUTION]
> **This project is no longer maintained since March 2024. If you intend to run the whole or some part of my source code, please be aware some dependencies will be outdated and you expose yourself to security vulnerabilities if proper safeguards are not enforced.**

Initially created in March 2023 as a practice course in Java and [*framework* Spring](https://spring.io/projects/spring-framework) for my studies, I quickly had the idea of creating a gateway/tunnel as a little website towards the [Discord API](https://discord.com/developers/docs/topics/gateway) without using any third-party library. The website is then able to connect itself to the Discord API just like you would normally do with the desktop application or the official website. Once connected, your website offers several (**limited**) but easy-to-use features:

- <ins>Write state simulation</ins>: you can enable or disable write state simulation ("XXX is writing...") indefinitely, even if you're not writing anything.
- <ins>Update presence status</ins>: there are options for updating presence status (online, inactive, do not disturb...), with or without a status message relating to your activity.
- <ins>Sending messages</ins>: everything is in the title, it's also practical to send messages with or without a delay. Sending is not immediate, and the website takes care of enabling a write state before sending your message (just like a human typing on a keyboard).

> [!NOTE]
> Here are the requirements to run the website:
> * [**Any** maintained NodeJS LTS versions](https://github.com/nodejs/release#release-schedule)
> * [**Any** maintained Java LTS versions](https://www.oracle.com/java/technologies/java-se-support-roadmap.html)

> [!TIP]
> To test the project, you simply have to install all the necessary dependencies with `npm install` command (requires [NodeJS](https://nodejs.org/en/download)), compile NextJS static files with `npm run build` and then launch the Java development server (requires [Java](https://www.java.com/en/download/)). A Docker image is also available to test this project for more experienced people! 🐳

> [!WARNING]
> The whole code of this project is commented in my native language (in French) and will not be translated in English since this was a personal private project.

![Discord-Tunnel](https://github.com/FlorianLeChat/Discord-Tunnel/assets/26360935/11e5742c-cfe9-4e30-8097-eff9b3ef0abb)
