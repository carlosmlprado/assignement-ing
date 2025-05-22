FROM amazoncorretto:21-alpine-jdk
LABEL maintainer="Carlos Prado"
COPY target/mortgage-0.0.1-SNAPSHOT.jar mortgage.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "mortgage.jar"]