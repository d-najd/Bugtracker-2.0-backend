#!/bin/bash
cd ..
a=$(pwd)
mvn clean install
cd "$a"
sh copy-jars.sh