FROM openjdk:8-jdk-alpine
EXPOSE 8080

WORKDIR root/
ARG JAR_FILE=build/libs/cinema-*.jar
ADD ${JAR_FILE} ./application.jar

ENTRYPOINT ["java", "-server", "-Xms256M", "-Xmx1024M",\
            "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=dump.hprof",\
            "-jar", "-Dspring.profiles.active=docker","/root/application.jar"]