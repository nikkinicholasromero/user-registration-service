FROM openjdk:8
EXPOSE 8080
COPY ./target/user-registration-service-1.0-SNAPSHOT.jar /usr/src/
WORKDIR /usr/src/
