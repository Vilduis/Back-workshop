# ─────────────────────────────────────────────
# STAGE 1: Build con Maven
# ─────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar archivos de dependencias primero (mejor caché)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copiar el resto del código y compilar
COPY src ./src
RUN mvn clean package -DskipTests -q

# ─────────────────────────────────────────────
# STAGE 2: Imagen final liviana solo con JRE
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiar el JAR generado en la etapa de build
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Puerto que expone Render
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

