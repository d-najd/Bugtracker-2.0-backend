FROM eclipse-temurin:17-jdk-alpine-3.23 AS builder
WORKDIR /build

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests

## Stage 2
FROM eclipse-temurin:17-jre-alpine-3.23 AS runner
WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

COPY ./user-content/default-project-icon user-content/default-project-icon

ENTRYPOINT ["java", "-jar", "app.jar"]
