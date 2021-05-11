package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Config;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface ConfigDao extends Dao<Config, String> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = ConfigDao.class.getName();

    /**
     * Gets the config for the given discord server id.
     * @param discordId the id.
     * @return a config object.
     * @throws RetrieveError when something goes wrong .
     */
    Config getById(final String discordId) throws RetrieveError;

    /**
     * Saves a config.
     * @param config the config to save.
     * @throws InsertionError when something goes wrong.
     */
    void save(final Config config) throws InsertionError;
}
