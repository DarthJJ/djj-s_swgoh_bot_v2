package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.util.Map;

/**
 * @author DJJ
 **/
public interface PlayerDao extends Dao<Player, Integer> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = PlayerDao.class.getName();

    /**
     * Gets a player object by the allycode.
     * @param allycode the allycode.
     * @return a player object.
     * @throws RetrieveError when something goes wrong.
     */
    Player getById(final int allycode) throws RetrieveError;

    /**
     * Saves the player to the DB.
     * @param player the player object.
     * @throws InsertionError when something goes wrong.
     */
    void save(final Player player) throws InsertionError;

    /**
     * Gets a player object by the discordId.
     * @param discordId the discordId.
     * @return a player object.
     * @throws RetrieveError when something goes wrong.
     */
    Player getByDiscordId(final String discordId) throws RetrieveError;

    /**
     * Checks whether a player exists in the DB.
     * @param allycode the allycode of the player.
     * @return a boolean.
     * @throws RetrieveError when something goes wrong.
     */
    boolean exists(final int allycode) throws RetrieveError;

    /**
     * Deletes a player.
     * @param allycode the allycode of the player.
     * @throws DeletionError when something goes wrong.
     */
    void delete(final int allycode) throws DeletionError;

    /**
     * Gets a GP overview for the guild.
     * @param guildId  the id of guild.
     * @return a list of players + GPs.
     * @throws RetrieveError when something goes wrong.
     */
    Map<String, Integer> getGpForGuild(int guildId) throws RetrieveError;

    /**
     * Gets a relic overview for the guild.
     * @param guildId the id of the guild.
     * @param relicLevel the level to cap on.
     * @return a list of players + #of relics.
     * @throws RetrieveError when something goes wrong.
     */
    Map<String, Integer> getRelicForGuild(int guildId, int relicLevel) throws RetrieveError;

    /**
     * Checks whether the user can create a ticket.
     * @param allycode the allycode to search for.
     * @return a boolean.
     */
    boolean isPlayerAllowedToCreateTickets(final int allycode);

    /**
     * Disallows the given userId to create tickets.
     * @param discordId the user to disallow.
     */
    void disallowTicketCreation(String discordId) throws InsertionError;
}
