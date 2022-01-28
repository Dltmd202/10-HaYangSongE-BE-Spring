x#!/bin/bash

REPOSITORY=/home/ec2-user/app/step1
PROJECT_NAME=10-HaYangSongE-BE-Spring

cd $REPOSITORY/$PROJECT_NAME/

echo "> Git PUll"

git pull

echo "> build start"

./gradlew build

echo "> step1 change dir"

cd $REPOSITORY

echo "> build copy"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*jar $REPOSITORY/

echo "> check current pid"

CURRENT_PID=$(pgrep -fl $PROJECT_NAME)

echo "current application pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> not exisiting application"
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> new application deploy"

JAR_NAME=$(ls -tr $REPOSITORY | grep jar | tail -n 1)

echo "> JAR NAME $JAR_NAME"

nohup java -jar \
       -Dspring.config.location=classpath:/application.yml,/home/ec2-user/app/application-db.yml \
        $REPOSITORY/$JAR_NAME 2>&1 &