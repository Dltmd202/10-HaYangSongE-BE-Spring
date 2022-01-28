ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=10-HaYangSongE-BE-Spring

cp $REPOSITORY/zip/*.jar $REPOSITORY/

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

nohup java -jar \
    -Dspring.config.location=classpath:/application.yml,classpath:/application-$IDLE_PROFILE.yml, \
     /home/ec2-user/app/application-db.yml,  /home/ec2-user/app/application-jwt.yml\
    -Dspring.profiles.active=$IDLE_PROFILE \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &