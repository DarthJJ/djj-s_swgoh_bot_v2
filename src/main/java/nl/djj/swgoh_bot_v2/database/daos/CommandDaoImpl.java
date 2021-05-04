package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Command;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;

/**
 * @author DJJ
 **/
public class CommandDaoImpl extends BaseDaoImpl<Command, String> implements CommandDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public CommandDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, Command.class);
    }

    @Override
    public boolean isEnabled(final String commandName) {
        try {
            return this.getByName(commandName).isEnabled();
        } catch (final RetrieveError exception) {
            return false;
        }
    }

    @Override
    public void save(final Command command) throws InsertionError {
        try {
            this.createOrUpdate(command);
        } catch (final SQLException exception) {
            throw new InsertionError(className, "save", exception.getMessage());
        }
    }

    @Override
    public Command getByName(final String name) throws RetrieveError {
        try {
            return this.queryForId(name);
        } catch (final SQLException exception) {
            throw new RetrieveError(className, "getByName", exception.getMessage());
        }
    }

    @Override
    public boolean exist(final String commandName) {
        try {
            return this.idExists(commandName);
        } catch (final SQLException exception){
            return false;
        }
    }
}
