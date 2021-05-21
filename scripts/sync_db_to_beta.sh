#!/usr/bin/env
set -e

echo "=============================================================="
echo "Please truncate the unit_abilities and player_units tables first"
echo "=============================================================="
read -p "Press enter once done!"
echo "=============================================================="
echo "Copying the unit_abilities table"
echo "=============================================================="
pg_dump -a -t unit_abilities swgoh_bot | psql swgoh_bot_beta
echo "=============================================================="
echo "Copying the player_units table"
echo "=============================================================="
pg_dump -a -t player_units swgoh_bot | psql swgoh_bot_beta
echo "=============================================================="
echo "Done"
echo "=============================================================="