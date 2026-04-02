FROM eclipse-temurin:21-alpine

RUN addgroup -S teamwork && adduser -S -D -h /home/teamwork -s /sbin/nologin teamwork

WORKDIR /app

RUN chown teamwork:teamwork /app

ARG JAR_FILE=target/teamwork_gateway-server.jar
COPY --chown=teamwork:teamwork ${JAR_FILE} app.jar

USER teamwork

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]