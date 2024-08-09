# Use a imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Adiciona um volume apontando para /tmp
VOLUME /tmp

# Copia o jar da aplicação para o container
COPY target/Pagamento-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8081
EXPOSE 8080

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "/app.jar"]