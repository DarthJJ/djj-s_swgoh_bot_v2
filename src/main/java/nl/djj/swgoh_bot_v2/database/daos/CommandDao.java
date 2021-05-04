package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Command;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface CommandDao extends Dao<Command, String> {
    String className = CommandDao.class.getName();

    boolean isEnabled(final String commandName);

    void save(final Command command) throws InsertionError;

    Command getByName(final String name) throws RetrieveError;

    boolean exist(final String commandName);
}
