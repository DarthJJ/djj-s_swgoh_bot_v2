package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Flag;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;

/**
 * @author DJJ
 **/
public class FlagDaoImpl extends BaseDaoImpl<Flag, String> implements FlagDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public FlagDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, Flag.class);
    }

    @Override
    public boolean isEnabled(final String flagName) {
        try {
            return this.getByName(flagName).isEnabled();
        } catch (final RetrieveError exception) {
            return false;
        }
    }

    @Override
    public void save(final Flag flag) throws InsertionError {
        try {
            this.createOrUpdate(flag);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception);
        }
    }

    @Override
    public Flag getByName(final String name) throws RetrieveError {
        try {
            return this.queryForId(name);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getByName", exception);
        }
    }

    @Override
    public boolean exists(final String flagName) {
        try {
            return this.idExists(flagName);
        } catch (final SQLException exception) {
            return false;
        }
    }
}
