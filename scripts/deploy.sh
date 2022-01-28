x#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=10-HaYangSongE-BE-Spring

echo "> build copy"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

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

echo "> Add execution permission to $JAR_NAME"

chmod +x $JAR_NAME

echo "> $JAR_NAME execute"




nohup java -jar \
       -Dspring.config.location=classpath:/application.yml,/home/ec2-user/app/application-db.yml \
        $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &