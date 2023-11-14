#Development stage
FROM eclipse-temurin:17-jre-ubi9-minimal
WORKDIR /app

#Build Stage
FROM maven:latest as build
WORKDIR /app
COPY . .
RUN mvn compile
RUN mvn package -DskipTests