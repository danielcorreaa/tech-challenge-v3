# Etapa 1: build (usando Maven)
FROM maven:3.9.6-eclipse-temurin-17 AS build


WORKDIR /app

# Copia arquivos do projeto
COPY pom.xml .
COPY src ./src

# Build do projeto sem testes
RUN mvn clean package -DskipTests

# Etapa 2: runtime (apenas o JAR final)
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copia apenas o .jar da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Render fornece a variável PORT automaticamente
ENV PORT=8080
EXPOSE 8080

# Start da aplicação
ENTRYPOINT ["java","-jar","app.jar", "--spring.profiles.active=prod"]
