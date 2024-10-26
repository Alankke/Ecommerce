FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/E-commerce-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]