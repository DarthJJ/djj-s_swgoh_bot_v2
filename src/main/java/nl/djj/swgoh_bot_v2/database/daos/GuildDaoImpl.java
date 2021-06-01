package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Guild;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.List;

/**
 * @author DJJ
 **/
public class GuildDaoImpl extends BaseDaoImpl<Guild, Integer> implements GuildDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public GuildDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, Guild.class);
    }

    @Override
    public Guild getById(final int guildId) throws RetrieveError {
        try {
            return this.queryForId(guildId);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getById", exception);
        }
    }

    @Override
    public void save(final Guild guild) throws InsertionError {
        try {
            this.createOrUpdate(guild);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception);
        }
    }

    @Override
    public Guild getByDiscordId(final String guildId) throws RetrieveError {
        try {
            final List<Guild> result = this.queryForEq("discord_id", guildId);
            if (result.isEmpty()) {
                throw new RetrieveError(CLASS_NAME, "getByDiscordId", new SQLException("Nothing found"));
            }
            return result.get(0);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getByDiscordId", exception);
        }
    }
}
