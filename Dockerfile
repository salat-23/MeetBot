FROM openjdk:11
WORKDIR /var/app
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} tgbot.jar
COPY bot-data.xml bot-data.xml
COPY confirmations.xml confirmations.xml
EXPOSE 8080
ENTRYPOINT ["java","-jar","/tgbot.jar"]
