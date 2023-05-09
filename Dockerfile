FROM openjdk:17
WORKDIR /seonghoon/chat
ARG JAR_FILE=build/libs/chat.jar
COPY ${JAR_FILE} chat.jar
ENTRYPOINT ["java","-jar","./chat.jar"]