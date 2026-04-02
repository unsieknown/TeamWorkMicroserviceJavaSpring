FROM eclipse-temurin:21-alpine

RUN addgroup -S teamwork && adduser -S -D -h /home/teamwork -G teamwork -s /sbin/nologin teamwork

WORKDIR /app

ARG JAR_FILE=target/teamwork_team-service.jar
COPY --chown=teamwork:teamwork ${JAR_FILE} app.jar

RUN mkdir -p /data && chown -R teamwork:teamwork /data

USER teamwork

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]