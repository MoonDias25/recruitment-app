FROM maven:3.9-eclipse-temurin-26 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:26-jdk

WORKDIR /app

COPY --from=build /app/target/server-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]