FROM openjdk:11
EXPOSE 8080
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} tgbot.jar
ENTRYPOINT ["java","-jar","/tgbot.jar"]
