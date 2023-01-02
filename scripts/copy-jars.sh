#!/bin/bash
echo "Copying Jars"
cd ..
mkdir -p jars
input="$(pwd)/scripts/all-services"
while read -r line
do
  cp "$(pwd)/$line/target"/*.jar "$(pwd)/jars"
done < "$input"