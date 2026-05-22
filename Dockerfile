FROM maven:3.9-amazoncorretto-21 AS builder
WORKDIR /api

COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests package

FROM amazoncorretto:21
WORKDIR /app
COPY --from=builder /api/target/*.jar app.jar

# Railway rutea al puerto que tengas en Networking → Target Port.
# Si lo dejas en 8081 ahí, este EXPOSE coincide y application.yml
# ${PORT:8081} cierra el lazo aunque Railway no inyecte PORT.
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
