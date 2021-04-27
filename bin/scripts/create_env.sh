#!/bin/bash
DIR=../live/
if [ ! -d "$DIR" ]; then
  mkdir "$DIR"
fi
DIR=../live/database/
if [ ! -d "$DIR" ]; then
  mkdir "$DIR"
fi
DIR=../live/log/
if [ ! -d "$DIR" ]; then
  mkdir "$DIR"
fi
sudo cp djj-bot.service /etc/systemd/system/djj-bot.service
echo "=============================================================="
echo "don't forget to create and .env file with the appropriate vars"
echo "=============================================================="