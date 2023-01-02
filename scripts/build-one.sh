#!/bin/bash
cd ..
mkdir -p jars
a=$(pwd)
cd "$a/$1"
mvn clean install
cd "$a"
cp "$a/$1/target"/*.jar "$a/jars"