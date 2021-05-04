package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.CommandUsage;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface CommandUsageDao extends Dao<CommandUsage, Integer> {
    String className = CommandUsageDao.class.getName();

    CommandUsage getByName(final String commandName, final String flagName) throws RetrieveError;

    void updateUsage(final String commandName, final String flagName) throws InsertionError;

    void save(final CommandUsage commandUsage) throws InsertionError;
}
