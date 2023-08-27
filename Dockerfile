# syntax=docker/dockerfile:1

# Image Docker de Java 17.
FROM eclipse-temurin:17-jdk-alpine

# Création d'un utilisateur non-administrateur.
RUN addgroup -S java && adduser -S java -G java

# Installation de NodeJS, NPM et cURL.
RUN apk --no-cache add nodejs npm curl

# Définition du répertoire front-end.
WORKDIR /usr/src/app/src/main/ui

# Copie des fichiers de configuration de NPM.
COPY --chown=java:java src/main/ui/package*.json .

# Installation des dépendances front-end.
RUN --mount=type=cache,target=.npm \
	npm set cache .npm && \
	npm install

# Définition du répertoire back-end.
WORKDIR /usr/src/app

# Change ownership of the installed dependencies
RUN chown -R java:java src/main/ui/node_modules

# Copie du reste des fichiers.
COPY --chown=java:java . .

# Conversion des fins de ligne Windows vers Linux.
# https://stackoverflow.com/a/62813766
RUN apk add --no-cache dos2unix && dos2unix mvnw

# Compilation générale du site.
RUN cd src/main/ui && npm run build

# Suppression des dépendances de développement.
RUN npm prune --production

# Modification des droits d'accès du programme final.
RUN chown -R java:java target
RUN chown -R java:java src/main/ui/.next

# Basculement vers l'utilisateur non-administrateur.
USER java

# Exposition du port 8080.
EXPOSE 8080

# Lancement de l'application.
CMD ["java", "-Xms256M -Xmx1g", "-jar", "target/discord-tunnel-1.0.0.jar"]