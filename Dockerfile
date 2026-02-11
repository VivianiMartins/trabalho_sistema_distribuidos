FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/sistema-distribuido-rmi-1.0-SNAPSHOT.jar app.jar

# RMI usa 1099, Spark usa 8080. Vamos expor as duas.
EXPOSE 1099 8080

# O comando de entrada padrão (pode ser sobrescrito pelo docker-compose)
CMD ["java", "-jar", "app.jar"]