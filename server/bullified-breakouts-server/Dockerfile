# base image
FROM java:8

# where a Spring Boot application creates working directories for Tomcat by default
VOLUME /tmp

# add jar to image
ADD bullified-breakouts-service.jar bullified-breakouts-service.jar

#update timestamp
RUN bash -c 'touch /bullified-breakouts-service.jar'

# run the jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/bullified-breakouts-service.jar"]