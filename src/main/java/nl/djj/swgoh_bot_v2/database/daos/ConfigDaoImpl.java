package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Config;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;

/**
 * @author DJJ
 **/
public class ConfigDaoImpl extends BaseDaoImpl<Config, String> implements ConfigDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public ConfigDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, Config.class);
    }

    @Override
    public Config getById(final String discordId) throws RetrieveError {
        try {
            return this.queryForId(discordId);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getById", exception.getMessage());
        }
    }

    @Override
    public void save(final Config config) throws InsertionError {
        try {
            this.createOrUpdate(config);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception.getMessage());
        }
    }
}
