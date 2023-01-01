#!/usr/bin/env bash

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

cd ..
echo "Running Apps"
if [[ $1 == no-install ]]; then
    echo "Skipping mvn install"
else
    mvn clean install
fi
java -jar discovery-server/target/*.jar > logs/discovery-server.log &
echo "sleeping 10 sec"
sleep 10
echo "done sleeping"
java -jar project-service/target/*.jar > logs/project-service.log &
echo "exec"
java -jar api-gateway/target/*.jar > logs/api-gateway.log
