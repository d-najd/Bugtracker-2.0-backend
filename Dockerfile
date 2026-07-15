FROM maven:4.0.0-rc-5-eclipse-temurin-17-alpine AS builder
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

## Stage 2
FROM eclipse-temurin:17-jre-alpine-3.23 AS runner
WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

COPY ./user-content/default-project-icon /user-content/default-project-icon

ENTRYPOINT ["java", "-jar", "app.jar"]
