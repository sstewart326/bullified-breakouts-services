FROM java:8
VOLUME /tmp
RUN mkdir -p /usr/src/images
ADD bullified-breakouts-service.jar bullified-breakouts-service.jar
RUN bash -c 'touch /bullified-breakouts-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/bullified-breakouts-service.jar"]