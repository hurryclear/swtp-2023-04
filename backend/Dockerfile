#Development Stage
FROM maven:3.9.5-amazoncorretto-17 as development
WORKDIR /app

#Build Stage
FROM development as build
COPY . .
RUN mvn clean package -DskipTests

#Production Stage
FROM amazoncorretto:17 as production
WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]
