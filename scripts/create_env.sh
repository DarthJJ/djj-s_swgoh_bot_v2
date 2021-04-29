#!usr/bin/bash
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
echo "=============================================================="
echo "don't forget to create and .env file with the appropriate vars"
echo "=============================================================="