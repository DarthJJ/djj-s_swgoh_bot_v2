package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Guild;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface GuildDao extends Dao<Guild, Integer> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = GuildDao.class.getName();

    /**
     * Gets a guild by the id.
     *
     * @param guildId the guild id.
     * @return a guild object.
     * @throws RetrieveError when something goes wrong.
     */
    Guild getById(final int guildId) throws RetrieveError;

    /**
     * Saves the guild.
     *
     * @param guild the guild object.
     * @throws InsertionError when something goes wrong.
     */
    void save(final Guild guild) throws InsertionError;

    /**
     * Gets a guild by Discord ID.
     *
     * @param guildId the Discord ID.
     * @return a guild object.
     */
    Guild getByDiscordId(String guildId) throws RetrieveError;
}
