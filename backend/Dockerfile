# Use the specified Maven image with Amazon Corretto JDK as the base image
FROM maven:3.9.5-amazoncorretto-17 as build

# Set the working directory in the Docker image
WORKDIR /app

# Copy the Maven wrapper from the host to the image
COPY mvnw mvnw.cmd .mvn /app/

# Copy the pom.xml file to download dependencies
COPY pom.xml /app/

# Optionally, if your build is slow and you want to improve subsequent builds,
# you can uncomment the following lines to enable dependency caching:
# RUN ./mvnw dependency:go-offline -B

# Copy the source code from the host to the image
COPY src /app/src

# Build the application using Maven
RUN ./mvnw clean package -DskipTests

# Use Amazon Corretto JDK as the runtime base image
FROM amazoncorretto:17

# Set the working directory in the runtime image
WORKDIR /app

# Copy the built artifact from the build stage to the runtime image
COPY --from=build /app/target/*.jar backend.jar

# Expose the port on which the application will run
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "backend.jar"]
