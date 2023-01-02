#!/usr/bin/env bash

echo "finish this"

sleep 10000000
java -jar discovery-server/target/*.jar > logs/discovery-server.log &
echo "sleeping 10 sec"
sleep 10
echo "done sleeping"
java -jar project-service/target/*.jar > logs/project-service.log &
echo "exec"
java -jar api-gateway/target/*.jar > logs/api-gateway.log
