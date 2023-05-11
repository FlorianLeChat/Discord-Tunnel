# syntax=docker/dockerfile:1

# Image Docker de Java 17.
FROM eclipse-temurin:17-jdk-alpine

# Création d'un utilisateur non-administrateur.
RUN addgroup -S java && adduser -S java -G java

# Installation de NodeJS et NPM.
RUN apk add --no-cache nodejs npm

# Définition du répertoire front-end.
WORKDIR /usr/src/app/src/main/ui

# Copie des fichiers de configuration de NPM.
COPY src/main/ui/package*.json .

# Installation des dépendances front-end.
RUN --mount=type=cache,target=.npm \
	npm set cache .npm && \
	npm install

# Définition du répertoire back-end.
WORKDIR /usr/src/app

# Copie du reste des fichiers.
COPY --chown=java:java . .

# Compilation générale du site.
RUN cd src/main/ui && npm run build

# Basculement vers l'utilisateur non-administrateur.
USER java

# Modification des droits d'accès des fichiers statiques.
RUN chown -R java:java /usr/src/app/src/main/ui/build

# Exposition du port 8080.
EXPOSE 8080

# Lancement de l'application.
CMD ["java", "-jar", "../../../target/discord-tunnel-1.0.0.jar"]