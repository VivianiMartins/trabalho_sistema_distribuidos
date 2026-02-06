FROM openjdk:17-jdk-slim
WORKDIR /app
# Copia o JAR gerado pelo Maven (note o nome do arquivo)
COPY target/sistema-distribuido-rmi-1.0-SNAPSHOT.jar app.jar