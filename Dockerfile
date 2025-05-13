# Build stage
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Production stage
FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:MaxMetaspaceSize=64m"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
