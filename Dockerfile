FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/*.jar app.jar
ADD ./user-content/default-project-icon /user-content/default-project-icon
ENTRYPOINT ["java", "-jar", "/app.jar"]