#!/usr/bin/bash

set -e

# keep track of the last executed command
trap 'last_command=$current_command; current_command=$BASH_COMMAND' DEBUG
# echo an error message before exiting
trap 'echo "\"${last_command}\" command filed with exit code $?."' EXIT

search_terms='bot.jar'

kill $(ps aux | grep "$search_terms" | grep -v 'grep' | awk '{print $2}')