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
     -Dtoken="$TOKEN" \
     -Downer="$OWNER" \
     -Dprefix="$PREFIX" \
     -jar JMusicBot.jar
