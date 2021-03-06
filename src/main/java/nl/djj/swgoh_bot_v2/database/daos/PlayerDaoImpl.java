package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
            throw new RetrieveError(CLASS_NAME, "getById", exception);
        }
    }

    @Override
    public void save(final Player player) throws InsertionError {
        try {
            this.createOrUpdate(player);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception);
        }
    }

    @Override
    public Player getByDiscordId(final String discordId) throws RetrieveError {
        try {
            final List<Player> found = this.queryForEq("discord_id", discordId);
            if (found != null && !found.isEmpty()) {
                return found.get(0);
            }
            return null;
        } catch (final SQLException | IndexOutOfBoundsException exception) {
            throw new RetrieveError(CLASS_NAME, "getByDiscordId", exception);
        }
    }

    @Override
    public boolean exists(final int allycode) throws RetrieveError {
        try {
            return this.idExists(allycode);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "userExists", exception);
        }
    }

    @Override
    public void delete(final int allycode) throws DeletionError {
        try {
            this.deleteById(allycode);
        } catch (final SQLException exception) {
            throw new DeletionError(CLASS_NAME, "delete", exception);
        }
    }

    @Override
    public Map<String, Integer> getGpForGuild(final int guildId) throws RetrieveError {
        try {
            final String query = "SELECT name, galactic_power " +
                    "FROM players " +
                    "WHERE guild_id = ? ::INTEGER " +
                    "ORDER BY galactic_power DESC";
            final GenericRawResults<String[]> results = this.queryRaw(query, Integer.toString(guildId));
            final Map<String, Integer> returnValue = new LinkedHashMap<>();
            for (final String[] result : results) {
                returnValue.put(result[0], Integer.parseInt(result[1]));
            }
            return returnValue;
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getGpForGuild", exception);
        }
    }

    @Override
    public Map<String, Integer> getRelicForGuild(final int guildId, final int relicLevel) throws RetrieveError {
        try {
            final String query = "SELECT t1.name, count(t2.relic) AS relics " + "FROM players AS t1 " +
                    "INNER JOIN player_units AS t2 " +
                    "WHERE t1.guild_id = ? " +
                    "AND t2.player_id = t1.allycode " +
                    "AND t2.relic <= ? " +
                    "AND t2.relic > -1 " +
                    "GROUP BY t1.name " +
                    "ORDER BY relics DESC;";
            final GenericRawResults<String[]> results = this.queryRaw(query, Integer.toString(guildId), Integer.toString(relicLevel));
            final Map<String, Integer> returnValue = new LinkedHashMap<>();
            for (final String[] result : results) {
                returnValue.put(result[0], Integer.parseInt(result[1]));
            }
            return returnValue;
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getRelicForGuild", exception);
        }
    }

    @Override
    public boolean isPlayerAllowedToCreateTickets(final int allycode) {
        try {
            final Player player = this.getById(allycode);
            if (player != null) {
                return player.isAllowedToCreateTickets();
            }
            return false;
        } catch (final RetrieveError error) {
            return false;
        }
    }

    @Override
    public void disallowTicketCreation(final String discordId) throws InsertionError {
        try {
            final Player player = this.getByDiscordId(discordId);
            if (player == null) {
                return;
            }
            player.setAllowedToCreateTickets(false);
            this.save(player);

        } catch (final RetrieveError error) {
            throw new InsertionError(CLASS_NAME, "disallowTicketCreation", error);
        }
    }
}
