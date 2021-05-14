package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Guild;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author DJJ
 **/
public interface PlayerUnitDao extends Dao<PlayerUnit, String> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = PlayerUnitDao.class.getName();

    /**
     * Saves a playerUnit in the db.
     * @param playerUnit the playerUnit.
     * @throws InsertionError when something goes wrong.
     */
    void save(final PlayerUnit playerUnit) throws InsertionError;

    /**
     * Saves a list of playerUnits to the DB.
     * @param playerUnits the list of playerUnits.
     * @throws InsertionError when something goes wrong.
     */
    void saveAll(final List<PlayerUnit> playerUnits) throws InsertionError;

    /**
     * Gets all playerUnits for the given player.
     * @param player the player to search for.
     * @return a list of playerUnits.
     * @throws RetrieveError when something goes wrong.
     */
    List<PlayerUnit> getAllForPlayer(final Player player) throws RetrieveError;

    /**
     * Gets a single playerUnit for the given player.
     * @param player the player to search for.
     * @param baseId the baseId to filter on.
     * @return a playerUnit object.
     * @throws RetrieveError when something goes wrong.
     */
    PlayerUnit getForPlayer(final Player player, final String baseId) throws RetrieveError;

    /**
     * Gets the amount of playerUnits at or above the gearLevel.
     * @param player the player to search for.
     * @param gearLevel the gearLevel.
     * @return a value.
     * @throws RetrieveError when something goes wrong.
     */
    int getGearCount(final Player player, final int gearLevel) throws RetrieveError;

    /**
     * Gets the amount of zetas a player has.
     * @param player the player to search for.
     * @param baseId the unit to filter on (can be nulled).
     * @return the amount of zetas.
     * @throws RetrieveError when something goes wrong.
     */
    int getZetaCount(final Player player, @Nullable final String baseId) throws RetrieveError;

    /**
     * Gets tge amount of playerUnits at or above the gearLevel for the given guild.
     * @param guild the guild.
     * @param gearLevel the gearLevel.
     * @param unitId the unitId (can be nulled to get for all units).
     * @return a value.
     * @throws RetrieveError when something goes wrong.
     */
    int getGearCount(final Guild guild, final int gearLevel, @Nullable final String unitId) throws RetrieveError;

    /**
     * Gets the amount of zetas the guild has.
     * @param guild the guild.
     * @param unitId the unitId (can be nulled to get all zetas)
     * @return the number of zetas in the guild.
     * @throws RetrieveError when something goes wrong.
     */
    int getZetaCount(final Guild guild, @Nullable final String unitId) throws RetrieveError;

    /**
     * Gets the relics for the guild.
     * @param guild the guild.
     * @return a map of relic levels and the amount.
     * @throws RetrieveError when something goes wrong.
     */
    Map<Integer, Integer> getRelics(final Guild guild) throws RetrieveError;

    /**
     * Gets the number of relics for the guild.
     * @param guild the guild.
     * @param level the level to filter.
     * @param baseId the unit to filter ( can be nulled for all units)
     * @return a number.
     * @throws RetrieveError when something goes wrong.
     */
    int getRelicCountForUnit(final Guild guild, final int level, @Nullable final String baseId) throws RetrieveError;

    /**
     * Gets the number of units at the rarity level for the guild.
     * @param guild the guild.
     * @param level the level to filter.
     * @param baseId the unit to filter (can be nulled for all units).
     * @return a number.
     * @throws RetrieveError when something goes wrong.
     */
    int getRarityCountForUnit(final Guild guild, final int level, @Nullable final String baseId) throws RetrieveError;

    /**
     * Gets the relics for the player.
     * @param player the player.
     * @return a map of relic levels and the amount.
     * @throws RetrieveError when something goes wrong.
     */
    Map<Integer, Integer> getRelics(final Player player) throws RetrieveError;
}
