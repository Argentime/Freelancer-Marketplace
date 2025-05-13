# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Production stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS="-Xms128m -Xmx220m -XX:MaxMetaspaceSize=100m"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]



