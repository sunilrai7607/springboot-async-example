FROM openjdk:8-jdk-alpine
MAINTAINER Sunil Rai <sunilrai7607@gmail.com>
VOLUME /app
ARG version
ENV version_number=$version
COPY ./build/libs/springboot-async-example-$version_number.jar springboot-async-example.jar
ENTRYPOINT ["java", "-jar","/springboot-async-example.jar"]