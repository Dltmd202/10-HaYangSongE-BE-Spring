#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PORT=$(find_idle_port)

echo "> Health Check Start"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile "
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'prod' | wc -l)
  echo > "> current response: ${UP_COUNT}"

  if [ ${UP_COUNT} -ge 0 ]
  then
      echo "> Successful Health check"
      switch_proxy
      break
  else
      echo "> no Health check"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Health check 실패. "
    echo "> terminate deploy no connect with nignx"
    exit 1
  fi

  echo "> Health check connected fail. retrying..."
  sleep 10
done