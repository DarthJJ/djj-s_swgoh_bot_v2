package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;

/**
 * @author DJJ
 **/
public class PlayerDaoImpl extends BaseDaoImpl<Player, Integer> implements PlayerDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public PlayerDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, Player.class);
    }

    @Override
    public Player getById(final int allycode) throws RetrieveError {
        try {
            return this.queryForId(allycode);
        } catch (final SQLException exception) {
            throw new RetrieveError(className, "getById", exception.getMessage());
        }
    }

    @Override
    public void save(final Player player) throws InsertionError {
        try {
            this.createOrUpdate(player);
        } catch (final SQLException exception) {
            throw new InsertionError(className, "save", exception.getMessage());
        }
    }

    @Override
    public Player getByDiscordId(final String discordId) throws RetrieveError {
        try {
            return this.queryForEq("discordId", discordId).get(0);
        } catch (final SQLException | IndexOutOfBoundsException exception) {
            throw new RetrieveError(className, "getByDiscordId", exception.getMessage());
        }
    }

    @Override
    public boolean exists(final int allycode) throws RetrieveError {
        try {
            return this.idExists(allycode);
        } catch (final SQLException exception) {
            throw new RetrieveError(className, "userExists", exception.getMessage());
        }
    }

    @Override
    public void delete(final int allycode) throws DeletionError {
        try {
            this.deleteById(allycode);
        } catch (final SQLException exception) {
            throw new DeletionError(className, "delete", exception.getMessage());
        }
    }
}
