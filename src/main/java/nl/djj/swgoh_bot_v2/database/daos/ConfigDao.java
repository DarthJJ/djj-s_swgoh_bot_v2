package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Config;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface ConfigDao extends Dao<Config, String> {
    String className = ConfigDao.class.getName();

    Config getById(final String discordId) throws RetrieveError;

    void save(final Config config) throws InsertionError;
}
