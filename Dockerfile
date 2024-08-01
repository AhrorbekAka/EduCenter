FROM openjdk:8
COPY target/*.jar educenterserver.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "educenterserver.jar"]