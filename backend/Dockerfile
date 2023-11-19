# Build stage with Maven and Amazon Corretto 17
FROM maven:3.9.5-amazoncorretto-17 as build

# Set the working directory
WORKDIR /app
# Copy the project files to the container
COPY . /app

# Build the application with Maven
RUN mvn clean package -DskipTests

# Run stage with Amazon Corretto 17
FROM amazoncorretto:17

# Copy the built artifact from the build stage to the run stage
COPY --from=build /app/target/*.jar /backend.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/backend.jar"]
