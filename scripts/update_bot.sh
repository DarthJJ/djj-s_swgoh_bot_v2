#!/usr/bin/bash
# exit when any command fails
set -e

# keep track of the last executed command
trap 'last_command=$current_command; current_command=$BASH_COMMAND' DEBUG
# echo an error message before exiting
trap 'echo "\"${last_command}\" command filed with exit code $?."' EXIT
echo "=============================================================="
echo "Fetching latest changes from Github"
echo "=============================================================="
git pull
echo "=============================================================="
echo "Creating Jar file"
echo "=============================================================="
./gradlew clean assemble shadowJar
echo "=============================================================="
echo "Stopping bot"
echo "=============================================================="
./scripts/stop.sh
echo "=============================================================="
echo "Replacing Jar file"
echo "=============================================================="
BOT_FILE=./LIVE/bot.jar/
if [ ! -f "$BOT_FILE" ]; then
rm "$BOT_FILE"
fi
cp build/libs/swgoh_bot_v2-?.?-SNAPSHOT-all.jar $BOT_FILE
echo "=============================================================="
echo "updating changelog"
echo "=============================================================="
rm LIVE/changelog.json
cp changelog.json LIVE/changelog.json
echo "=============================================================="
echo "starting bot"
echo "=============================================================="
./scripts/start.sh
echo "=============================================================="
echo "bot started, update done"
echo "=============================================================="