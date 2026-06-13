#!/bin/sh

if [ -z "$TOKEN" ]; then
  echo "ERROR: TOKEN environment variable is not set!"
  exit 1
fi

if [ -z "$OWNER" ]; then
  echo "ERROR: OWNER environment variable is not set!"
  exit 1
fi

PREFIX="${PREFIX:-!}"

java -Dnogui=true \
     -Dconfig.file=/bot/application.conf \
     -DTOKEN="$TOKEN" \
     -DOWNER="$OWNER" \
     -DPREFIX="$PREFIX" \
     -jar JMusicBot.jar
