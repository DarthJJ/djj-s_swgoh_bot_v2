#!/bin/bash
DIR=../LIVE/
if [ ! -d "$DIR" ]; then
  mkdir "$DIR"
fi
DIR=../LIVE/database/
if [ ! -d "$DIR" ]; then
  mkdir "$DIR"
fi
DIR=../LIVE/log/
if [ ! -d "$DIR" ]; then
  mkdir "$DIR"
fi
sudo cp djj-bot.service /etc/systemd/system/djj-bot.service
echo "=============================================================="
echo "don't forget to create and .env file with the appropriate vars"
echo "=============================================================="