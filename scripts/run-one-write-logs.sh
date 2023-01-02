#!/bin/bash
cd ..
mkdir -p logs
java -jar "$(pwd)/$1/target"/*.jar > "$(pwd)/logs/$1".log
