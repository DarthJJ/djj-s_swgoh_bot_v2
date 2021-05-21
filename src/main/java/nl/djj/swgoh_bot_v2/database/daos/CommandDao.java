package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Command;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface CommandDao extends Dao<Command, String> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = CommandDao.class.getName();

    /**
     * Checks whether the command is enabled.
     * @param commandName the name of the command.
     * @return a boolean value.
     */
    boolean isEnabled(final String commandName);

    /**
     * Saves the command to the DB.
     * @param command the command.
     * @throws InsertionError when something goes wrong.
     */
    void save(final Command command) throws InsertionError;

    /**
     * Gets a command by name.
     * @param name the name of the command.
     * @return the command object.
     * @throws RetrieveError when something goes wrong.
     */
    Command getByName(final String name) throws RetrieveError;

    /**
     * Checks whether the command exists.
     * @param commandName the name to search for.
     * @return a boolean value.
     */
    boolean exist(final String commandName);
}
