# User Registration Service

#### Build the JAR using Maven
``
mvn clean install
``

#### Build the Docker Image using the Dockerfile
``
docker build --tag nikkinicholasromero/user-registration-service-image:0.1 .
``

#### Push the Docker Image to Docker Hub
``
docker push nikkinicholasromero/user-registration-service-image:0.1
``

#### Start a Docker Container using the Docker Image
``
docker run -d -t -p 127.0.0.1:8080:8080 --name user-registration-service nikkinicholasromero/user-registration-service-image:0.1
``

#### Connect to the bash of Docker Container 
``
docker exec -it user-registration-service /bin/bash
``

#### Start the Java Application inside the Docker Container
``
java -jar -Dspring.profiles.active=mock user-registration-service-1.0-SNAPSHOT.jar
``
