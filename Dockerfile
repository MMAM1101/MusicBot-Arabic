FROM eclipse-temurin:11-jre

WORKDIR /bot

RUN apt-get update && apt-get install -y --no-install-recommends curl ca-certificates && rm -rf /var/lib/apt/lists/*

RUN LATEST=$(curl -s https://api.github.com/repos/jagrosh/MusicBot/releases/latest | grep -o '"tag_name":"[^"]*"' | sed 's/"tag_name":"//;s/"//') && \
    curl -fL "https://github.com/jagrosh/MusicBot/releases/download/${LATEST}/JMusicBot-${LATEST}.jar" -o JMusicBot.jar && \
    echo "Downloaded JMusicBot ${LATEST}"

COPY application.conf ./application.conf
COPY start.sh ./start.sh
RUN chmod +x start.sh

CMD ["./start.sh"]
