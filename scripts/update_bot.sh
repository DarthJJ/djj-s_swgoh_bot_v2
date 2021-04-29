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
stopBotV2
echo "=============================================================="
echo "Replacing Jar file"
echo "=============================================================="
rm LIVE/bot.jar
cp build/libs/swgoh_bot_v2-?.?-SNAPSHOT-all.jar LIVE/bot.jar
echo "=============================================================="
echo "updating changelog"
echo "=============================================================="
rm LIVE/changelog.json
cp changelog.json LIVE/changelog.json
echo "=============================================================="
echo "starting bot"
echo "=============================================================="
startBotv2
echo "=============================================================="
echo "bot started, update done"
echo "=============================================================="