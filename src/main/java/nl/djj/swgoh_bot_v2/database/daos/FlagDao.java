package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Flag;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface FlagDao extends Dao<Flag, String> {
    /**
     * The name of the implementation;.
     */
    String CLASS_NAME = FlagDao.class.getName();

    /**
     * Checks whether the flag is enabled.
     *
     * @param flagName      the name of the flag.
     * @param parentCommand the name of the parentCommand
     * @return a boolean value.
     */
    boolean isEnabled(final String parentCommand, final String flagName);

    /**
     * Saves the flag to the DB.
     *
     * @param flag the flag.
     * @throws InsertionError when something goes wrong.
     */
    void save(final Flag flag) throws InsertionError;

    /**
     * Gets a flag by name.
     *
     * @param name          the name of the flag.
     * @param parentCommand the name of the parentCommand.
     * @return the flag object.
     * @throws RetrieveError when something goes wrong.
     */
    Flag getByName(final String parentCommand, final String name) throws RetrieveError;

    /**
     * Checks whether the flag exists.
     *
     * @param flagName      the name to search for.
     * @param parentCommand the name of the parent Command.
     * @return a boolean value.
     */
    boolean exists(final String parentCommand, final String flagName);
}
