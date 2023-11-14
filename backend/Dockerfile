# Development stage
FROM eclipse-temurin:17-jre-ubi9-minimal as development
WORKDIR /app

# Build Stage
FROM maven:latest as build
WORKDIR /app
COPY . .
RUN mvn compile
RUN mvn package -DskipTests

# Production stage
FROM eclipse-temurin:17-jre-ubi9-minimal as production
WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]