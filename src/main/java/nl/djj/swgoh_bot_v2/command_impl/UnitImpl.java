package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.compare.ProfileCompare;
import nl.djj.swgoh_bot_v2.entities.Unit;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.entities.db.UnitAbility;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DJJ
 */
public class UnitImpl {
    private final transient DatabaseHandler dbHandler;

    /**
     * Constructor.
     *
     * @param dbHandler the DB connection.
     */
    public UnitImpl(final DatabaseHandler dbHandler) {
        super();
        this.dbHandler = dbHandler;
    }

    /**
     * Returns a name for the given ID.
     *
     * @param identifier the ID.
     * @return the Name.
     */
    public String getUnitNameForId(final String identifier) {
        try {
            return dbHandler.getUnitNameForId(identifier);
        } catch (final SQLRetrieveError error) {
            return identifier;
        }
    }

    /**
     * Checks the relics levels for a list of units.
     *
     * @param units      the unit list.
     * @param relicLevel the relic level.
     * @return a list with toons who have failed the check.
     */
    public Map<String, Integer> checkRelicLevel(final List<Unit> units, final int relicLevel) {
        final Map<String, Integer> unitsBelow = new ConcurrentHashMap<>();
        for (final Unit unit : units) {
            if (unit.getGearLevel() >= SwgohConstants.MAX_GEAR_LEVEL && unit.getRelicLevel() - 2 <= relicLevel) {
                unitsBelow.put(unit.getName(), unit.getRelicLevel() - 2);
            }
        }
        return unitsBelow;
    }

    /**
     * Checks the relics levels for a list of units.
     *
     * @param units      the unit list.
     * @param relicLevel the relic level.
     * @return a list with toons who have failed the check.
     */
    public Map<String, Integer> checkRelicLevel(final JSONArray units, final int relicLevel) {
        final Map<String, Integer> unitsBelow = new ConcurrentHashMap<>();
        for (int i = 0; i < units.length(); i++) {
            final JSONObject unitData = units.getJSONObject(i).getJSONObject("data");
            if (unitData.getInt("gear_level") >= SwgohConstants.MAX_GEAR_LEVEL && unitData.getInt("relic_tier") - 2 <= relicLevel) {
                unitsBelow.put(unitData.getString("name"), unitData.getInt("relic_tier") - 2);
            }
        }
        return unitsBelow;
    }

    /**
     * Creates the profile compares.
     *
     * @param playerData the data of the player.
     * @param rivalData  the data of the rival.
     * @return an array of profileCompares.
     */
    public ProfileCompare[] compareProfiles(final JSONObject playerData, final JSONObject rivalData) {
        final ProfileCompare player = new ProfileCompare();
        final ProfileCompare rival = new ProfileCompare();
        final JSONObject playerJson = playerData.getJSONObject("data");
        player.setGalacticPower(playerJson.getInt("galactic_power"));
        player.setShipGp(playerJson.getInt("ship_galactic_power"));
        player.setToonGp(playerJson.getInt("character_galactic_power"));
        player.setName(playerJson.getString("name"));
        player.setGuild(playerJson.getString("guild_name"));
        final JSONObject rivalJson = rivalData.getJSONObject("data");
        rival.setGalacticPower(rivalJson.getInt("galactic_power"));
        rival.setShipGp(rivalJson.getInt("ship_galactic_power"));
        rival.setToonGp(rivalJson.getInt("character_galactic_power"));
        rival.setName(rivalJson.getString("name"));
        rival.setGuild(rivalJson.getString("guild_name"));
        createUnitProfile(playerData.getJSONArray("units"), player);
        createUnitProfile(rivalData.getJSONArray("units"), rival);
        return new ProfileCompare[]{player, rival};
    }

    /**
     * Creates a unit profile.
     *
     * @param unitData the data.
     * @param profile  the profile.
     */
    public void createUnitProfile(final JSONArray unitData, final ProfileCompare profile) {
        for (int i = 0; i < unitData.length(); i++) {
            final JSONObject unitJson = unitData.getJSONObject(i).getJSONObject("data");
            if (SwgohConstants.COMPARE_TOONS.containsKey(unitJson.getString("base_id"))) {
                profile.addUnit(unitJson);
            }
            profile.addZeta(unitJson.getJSONArray("zeta_abilities").length());
            if (unitJson.getInt("gear_level") == SwgohConstants.GEAR_LEVEL_13) {
                profile.addG13();
            }
            if (unitJson.getInt("gear_level") == SwgohConstants.GEAR_LEVEL_12) {
                profile.addG12();
            }
            profile.addRelic(unitJson.getInt("relic_tier"));
        }
    }

    /**
     * Adds the playerUnits to the DB.
     *
     * @param playerUnits the units,
     * @param allycode    the allycode.
     * @param guildId     the guildId.
     * @throws SQLInsertionError when something goes wrong.
     */
    public void insertUnits(final JSONArray playerUnits, final int allycode, final int guildId) throws SQLInsertionError {
        final List<PlayerUnit> units = new ArrayList<>();
        for (int i = 0; i < playerUnits.length(); i++) {
            final JSONObject unitData = playerUnits.optJSONObject(i).getJSONObject("data");
            final String baseId = unitData.getString("base_id");
            final int rarity = unitData.getInt("rarity");
            final int galacticPower = unitData.getInt("power");
            final int gear = unitData.getInt("gear_level");
            final int relic = Math.max(-1, unitData.getInt("relic_tier") - 2);
            final int speed = unitData.getJSONObject("stats").getInt("5");
            final JSONArray abilityData = unitData.getJSONArray("ability_data");
            final JSONArray gearPiecesArray = unitData.getJSONArray("gear");
            int gearPieces = 0;
            for (int j = 0; j < gearPiecesArray.length(); j++) {
                if (gearPiecesArray.getJSONObject(j).getBoolean("is_obtained")) {
                    gearPieces++;
                }
            }
            final PlayerUnit playerUnit = new PlayerUnit(allycode, guildId, baseId, rarity, galacticPower, gear, gearPieces, relic, speed);
            for (int j = 0; j < abilityData.length(); j++) {
                final String abilityId = abilityData.getJSONObject(j).getString("id");
                final int level = abilityData.getJSONObject(j).getInt("ability_tier");
                playerUnit.addAbility(new UnitAbility(abilityId, allycode, guildId, level));
            }
//            this.dbHandler.insertPlayerUnit(playerUnit);
            units.add(playerUnit);
        }
        this.dbHandler.insertPlayerUnits(units);
    }
}
