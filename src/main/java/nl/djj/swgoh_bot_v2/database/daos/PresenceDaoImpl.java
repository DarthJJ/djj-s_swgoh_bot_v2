package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Presence;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;

import java.sql.SQLException;

/**
 * @author DJJ
 **/
public class PresenceDaoImpl extends BaseDaoImpl<Presence, Integer> implements PresenceDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public PresenceDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, Presence.class);
    }

    @Override
    public void add(final Presence presence) throws InsertionError {
        try {
            this.create(presence);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "add", exception);
        }
    }
}
