FROM openjdk:17-jdk-slim-buster
#CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE_PATH=*.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java","-jar","app.jar"]