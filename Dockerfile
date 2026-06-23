# Usa una imagen base pequeña con solo el Java Runtime Environment (JRE).
FROM eclipse-temurin:21-jre-alpine

# Define el directorio de trabajo dentro del contenedor.
WORKDIR /app

# Copia el archivo JAR compilado (el que ya tienes en target).
COPY target/proGanaderia-1.0-SNAPSHOT-shaded.jar app.jar

# Expone el puerto por defecto de tu aplicación.
EXPOSE 8080

# Comando para ejecutar el JAR en modo HEADLESS (-Djava.awt.headless=true)
ENTRYPOINT ["java","-Djava.awt.headless=true","-jar","/app/app.jar"]