#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${DIR}

version="1.0"

for file in target/*.jar
do
    version=`echo ${file} | sed 's/.*\-\([0-9\.]\+\)\.jar/\1/'`
done

echo booting version ${version}
java -jar ${DIR}/target/ym_gaTool-${version}.jar