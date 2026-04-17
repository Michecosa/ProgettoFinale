# 1. Scegliamo l'immagine base con Java 21 (Runtime Environment)
# Aggiornato da 17 a 21 per matchare il tuo progetto VS Code.
# 'alpine' garantisce un'immagine finale piccolissima e veloce.
FROM eclipse-temurin:21-jre-alpine

# 2. Creiamo una directory di lavoro interna al container
# Tutti i comandi successivi (COPY, ENTRYPOINT) partiranno da qui.
WORKDIR /app

# 3. Copiamo il JAR generato dal comando './mvnw clean package'
# Usiamo l'asterisco *.jar così non dobbiamo cambiare il Dockerfile 
# ogni volta che cambia la versione nel pom.xml.
COPY target/*.jar app.jar

# 4. Esponiamo la porta 8080 (documentativo)
# Utile per far capire a chi legge che l'app risponde su questa porta.
EXPOSE 8080

# 5. Comando di avvio in formato "Exec"
# È il modo migliore perché permette a Docker di chiudere l'app 
# in modo "gentile" (graceful shutdown) quando dai il comando stop.
ENTRYPOINT ["java", "-jar", "app.jar"]