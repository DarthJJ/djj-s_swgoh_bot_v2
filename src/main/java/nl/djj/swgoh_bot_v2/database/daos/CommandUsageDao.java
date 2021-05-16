package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.CommandUsage;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface CommandUsageDao extends Dao<CommandUsage, Integer> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = CommandUsageDao.class.getName();

    /**
     * Gets the command usage per name.
     * @param commandName the name of the command.
     * @param flagName the flag of the command.
     * @return a commandUsage object.
     * @throws RetrieveError when something goes wrong.
     */
    CommandUsage getByName(final String commandName, final String flagName) throws RetrieveError;

    /**
     * Updates the usage with +1 of the given command.
     * @param commandName the name of the command.
     * @param flagName the flag of the command.
     * @throws InsertionError when something goes wrong.
     */
    void updateUsage(final String commandName, final String flagName) throws InsertionError;

    /**
     * Saves a commandUsage object.
     * @param commandUsage the object.
     * @throws InsertionError when something goes wrong.
     */
    void save(final CommandUsage commandUsage) throws InsertionError;
}
