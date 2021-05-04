package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.entities.compare.ProfileCompare;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.entities.db.UnitAbility;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author DJJ
 */
public class UnitImpl {
    private final transient String className = this.getClass().getName();
    private final transient DAO dao;
    private final transient Logger logger;

    /**
     * Constructor.
     *
     * @param dao the DB connection.
     */
    public UnitImpl(final DAO dao, final Logger logger) {
        super();
        this.dao = dao;
        this.logger = logger;
    }

    /**
     * Returns a name for the given ID.
     *
     * @param identifier the ID.
     * @return the Name.
     */
    public String getUnitNameForId(final String identifier) {
        try {
            return dao.unitDao().getById(identifier).getName();
        } catch (final RetrieveError error) {
            return identifier;
        }
    }

    /**
     * Checks the relics levels for a list of units.
     *
     * @param allycode   the allycode of the player.
     * @param relicLevel the relic level.
     * @return a list with toons who have failed the check.
     */
    public Map<String, Integer> checkRelicLevel(final int allycode, final int relicLevel) {
        final Map<String, Integer> unitsBelow = new ConcurrentHashMap<>();
        try {
            for (final PlayerUnit unit : dao.playerUnitDao().getAllForPlayer(dao.playerDao().getById(allycode))) {
                if (unit.getGear() >= SwgohConstants.MAX_GEAR_LEVEL && unit.getRelic() <= relicLevel) {
                    unitsBelow.put(unit.getUnit().getName(), unit.getRelic());
                }
            }
        } catch (final RetrieveError exception) {
            logger.error(className, "checkRelicLevel", exception.getMessage());
        }
        return unitsBelow.entrySet().stream().sorted((e1,e2)->
                e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * Creates the profile compares.
     *
     * @param playerId the data of the player.
     * @param rivalId  the data of the rival.
     * @return an array of profileCompares.
     */
    public ProfileCompare[] compareProfiles(final int playerId, final int rivalId) {
        final ProfileCompare player = new ProfileCompare();
        final ProfileCompare rival = new ProfileCompare();
//        final JSONObject playerJson = playerData.getJSONObject("data");
//        player.setGalacticPower(playerJson.getInt("galactic_power"));
//        player.setShipGp(playerJson.getInt("ship_galactic_power"));
//        player.setToonGp(playerJson.getInt("character_galactic_power"));
//        player.setName(playerJson.getString("name"));
//        player.setGuild(playerJson.getString("guild_name"));
//        final JSONObject rivalJson = rivalData.getJSONObject("data");
//        rival.setGalacticPower(rivalJson.getInt("galactic_power"));
//        rival.setShipGp(rivalJson.getInt("ship_galactic_power"));
//        rival.setToonGp(rivalJson.getInt("character_galactic_power"));
//        rival.setName(rivalJson.getString("name"));
//        rival.setGuild(rivalJson.getString("guild_name"));
//        createUnitProfile(playerData.getJSONArray("units"), player);
//        createUnitProfile(rivalData.getJSONArray("units"), rival);
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
     * @param player      the player.
     * @throws RetrieveError  When retrieving from the DB goes wrong.
     * @throws InsertionError When storing in the DB goes wrong.
     */
    public void insertUnits(final JSONArray playerUnits, final Player player) throws RetrieveError, InsertionError {
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
            dao.playerUnitDao().save(new PlayerUnit(player, dao.unitDao().getById(baseId), rarity, galacticPower, gear, gearPieces, relic, speed));
            final PlayerUnit unit = dao.playerUnitDao().getForPlayer(player, baseId);
            for (int j = 0; j < abilityData.length(); j++) {
                String abilityId = abilityData.getJSONObject(j).getString("id");
                if ("uniqueskill_GALACTICLEGEND01".equals(abilityId)){
                    abilityId+= "_" + baseId;
                }
                final int level = abilityData.getJSONObject(j).getInt("ability_tier");
                dao.unitAbilityDao().save(new UnitAbility(unit, dao.abilityDao().getById(abilityId), level));
            }
        }
    }
}
