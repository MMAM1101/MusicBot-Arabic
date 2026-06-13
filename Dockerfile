FROM maven:3.9.6-eclipse-temurin-11 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests -q

FROM eclipse-temurin:11-jre

WORKDIR /bot

COPY --from=builder /app/target/JMusicBot-Snapshot-All.jar ./JMusicBot.jar
COPY start.sh ./start.sh
RUN chmod +x start.sh

CMD ["./start.sh"]
