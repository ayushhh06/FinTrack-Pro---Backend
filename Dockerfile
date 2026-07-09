# Stage 1: Build the application using Eclipse Temurin
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .

# Explicitly grant execute rights to the Maven wrapper within the container environment
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application using a lightweight production runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
