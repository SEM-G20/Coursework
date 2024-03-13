FROM openjdk:latest
COPY ./target/coursework.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "coursework.jar", "db:3306", "30000"]