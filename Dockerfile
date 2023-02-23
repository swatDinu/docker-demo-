FROM openjdk:8-alpine
add target/docker-compose-demo-1.0-SNAPSHOT.jar demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]