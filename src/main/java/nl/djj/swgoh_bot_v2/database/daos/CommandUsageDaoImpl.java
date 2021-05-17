package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.CommandUsage;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author DJJ
 **/
public class CommandUsageDaoImpl extends BaseDaoImpl<CommandUsage, Integer> implements CommandUsageDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public CommandUsageDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, CommandUsage.class);
    }

    @Override
    public CommandUsage getByName(final String commandName, final String flagName) throws RetrieveError {
        try {
            return this.queryForFieldValuesArgs(Map.of(
                    "commandName", commandName,
                    "flagName", flagName
            )).get(0);
        } catch (final SQLException | IndexOutOfBoundsException exception) {
            throw new RetrieveError(CLASS_NAME, "getByName", exception);
        }
    }

    @Override
    public void updateUsage(final String commandName, final String flagName) throws InsertionError {
        CommandUsage usage;
        try {
            usage = this.getByName(commandName, flagName);
            usage.setUsage(usage.getUsage() + 1);
        } catch (final RetrieveError exception) {
            usage = new CommandUsage(commandName, flagName);
        }
        this.save(usage);

    }

    @Override
    public void save(final CommandUsage commandUsage) throws InsertionError {
        try {
            this.createOrUpdate(commandUsage);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception);
        }
    }
}
