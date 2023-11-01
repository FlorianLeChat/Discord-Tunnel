# syntax=docker/dockerfile:1

# Image Docker de NodeJS
# https://hub.docker.com/_/node
FROM node:lts-alpine

# Définition de l'espace de travail
WORKDIR /usr/src/app

# Copie de l'ensemble des fichiers du projet
COPY --chown=node:node . .

# Définition du répertoire front-end
WORKDIR /usr/src/app/src/main/ui

# Installation de l'ensemble des dépendances
# Utilisation d'un cache pour éviter de réinstaller les dépendances à chaque version
RUN --mount=type=cache,target=.npm \
	npm set cache .npm && \
	npm install

# Compilation des fichiers front-end
RUN npm run build

# Suppression des dépendances de développement
RUN npm prune --production

# Image Docker de Java 21
# https://hub.docker.com/_/eclipse-temurin
FROM eclipse-temurin:21-jdk-alpine

# Création d'un utilisateur non-administrateur
RUN addgroup -S java && adduser -S java -G java

# Définition de l'espace de travail
WORKDIR /usr/src/app

# Copie des fichiers précédemment compilés
COPY --from=0 --chown=java:java /usr/src/app ./

# Conversion des fins de ligne Windows vers Linux
# https://stackoverflow.com/a/62813766
RUN apk --no-cache add curl dos2unix && dos2unix mvnw

# Compilation des fichiers back-end
RUN ./mvnw clean install -DskipTests

# Basculement vers l'utilisateur non-administrateur
USER java

# Lancement de l'application
CMD ["java", "-Xms256M", "-Xmx1g", "-jar", "target/discord-tunnel-1.0.0.jar"]