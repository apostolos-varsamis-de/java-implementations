FROM adoptopenjdk/openjdk11:x86_64-ubuntu-jre-11.0.12_7

MAINTAINER Apostolos Varsamis <apostolosb@varsamis.de>

USER root

ENV APP_FOLDER=/app
WORKDIR $APP_FOLDER

COPY motion-service-write/target/motion-service-write-*.jar $APP_FOLDER/app.jar
RUN chown -R 1001:0 $APP_FOLDER

USER 1001

EXPOSE 8080
CMD ["/bin/bash", "-c", "java -Dspring.main.banner-mode=off -Djava.security.egd=file:/dev/./urandom -jar $APP_FOLDER/app.jar"]
