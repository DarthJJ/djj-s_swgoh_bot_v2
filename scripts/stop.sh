#!/usr/bin/bash

search_terms='bot.jar'

kill $(ps aux | grep "$search_terms" | grep -v 'grep' | awk '{print $2}')

exit 0;