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

    @Override
    public Map<String, Integer> getGpForGuild(final int guildId) throws RetrieveError{
        try {
            String query = "SELECT name, galacticPower ";
            query += "FROM players ";
            query += "WHERE guild_id = ? ";
            query += "ORDER BY galacticPower DESC";
            final GenericRawResults<String[]> results = this.queryRaw(query,Integer.toString(guildId));
            final Map<String, Integer> returnValue = new LinkedHashMap<>();
            for (final String[] result : results){
                returnValue.put(result[0], Integer.parseInt(result[1]));
            }
            return returnValue;
        } catch (final SQLException exception){
            throw new RetrieveError(className, "getGpForGuild", exception.getMessage());
        }
    }

    @Override
    public Map<String, Integer> getRelicForGuild(final int guildId, final int relicLevel) throws RetrieveError {
        try {
            String query = "SELECT t1.name, count(t2.relic) AS relics ";
             query += "FROM players AS t1 ";
             query += "INNER JOIN playerUnits AS t2 ";
             query += "WHERE t1.guild_id = ? ";
             query += "AND t2.player_id = t1.allycode ";
             query += "AND t2.relic <= ? ";
             query += "AND t2.relic > -1 ";
             query += "GROUP BY t1.name ";
             query += "ORDER BY relics DESC;";
            final GenericRawResults<String[]> results = this.queryRaw(query,Integer.toString(guildId), Integer.toString(relicLevel));
            final Map<String, Integer> returnValue = new LinkedHashMap<>();
            for (final String[] result : results){
                returnValue.put(result[0], Integer.parseInt(result[1]));
            }
            return returnValue;
        } catch (final SQLException exception) {
            throw new RetrieveError(className, "getRelicForGuild", exception.getMessage());
        }
    }
}
