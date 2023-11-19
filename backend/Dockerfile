# Start with a base image that includes Java (e.g., OpenJDK)
FROM openjdk:17-jdk-slim as build

# Set the working directory in the Docker image
WORKDIR /app

# Copy the Maven Wrapper from your project into the Docker image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml and src code to the container
COPY pom.xml .
COPY src src

RUN chmod +x ./mvnw
# Use the Maven Wrapper to package the Spring Boot application
# with all its dependencies into a single executable JAR file
RUN ./mvnw package -DskipTests

# For the final image, use a smaller base image (JRE is enough to run the application)
FROM openjdk:17-jdk-alpine as production

# Set the deployment directory
WORKDIR /app

# Copy the packaged JAR file from the build stage to the deployment directory
COPY --from=build /app/target/*.jar backend.jar

# Expose the port on which your application will run
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "backend.jar"]

