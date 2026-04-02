FROM eclipse-temurin:21-alpine

RUN addgroup -S teamwork && adduser -S -D -h /home/teamwork -G teamwork -s /sbin/nologin teamwork

WORKDIR /app

RUN chown teamwork:teamwork /app

ARG JAR_FILE=target/teamwork_auth-service.jar
COPY --chown=teamwork:teamwork ${JAR_FILE} app.jar

RUN mkdir -p /keys && chown -R teamwork:teamwork /keys

USER teamwork

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]