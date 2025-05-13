FROM openjdk:17-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY *.csv /tmp
ENTRYPOINT ["java", "--add-exports", "java.base/jdk.internal.misc=ALL-UNNAMED", "-jar", "/app.jar"]