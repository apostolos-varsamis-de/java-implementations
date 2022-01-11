#!/usr/bin/env bash

set -o errexit
set -o nounset


CID=$(docker ps -a | grep 'motion-api' | awk '{print $1}')
if [ ! -z "$CID" ]
then
  echo -e "\e[34mStop running docker\e[0m"
  docker stop $CID
  echo -e "\e[34mremove existing docker\e[0m"
  docker rm $CID
fi

mvn clean install -DskipTests=true

KAFKA_BROKERS="kafka1.192.168.31.41.nip.io:9092,kafka2.192.168.31.42.nip.io:9092,kafka3.192.168.31.43.nip.io:9092"

docker build -t motion-api-service .
docker run \
  -e KAFKA_BOOTSTRAP_SERVERS="${KAFKA_BROKERS}" \
  -p 8080:8080 \
  --name motion-api \
  motion-api-service
