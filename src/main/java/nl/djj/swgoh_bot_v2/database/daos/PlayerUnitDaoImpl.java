package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author DJJ
 **/
public class PlayerUnitDaoImpl extends BaseDaoImpl<PlayerUnit, String> implements PlayerUnitDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public PlayerUnitDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, PlayerUnit.class);
    }

    @Override
    public void save(final PlayerUnit playerUnit) throws InsertionError {
        try {
//            final List<PlayerUnit> found = this.queryForFieldValuesArgs(Map.of(
//                    "player_id", playerUnit.getPlayer().getAllycode(),
//                    "unit_id", playerUnit.getUnit().getBaseId()
//            ));
//            if (found.size() > 0) {
//                playerUnit.setIdentifier(found.get(0).getIdentifier());
//            }
            this.createOrUpdate(playerUnit);
        } catch (final SQLException exception) {
            throw new InsertionError(className, "save", exception.getMessage());
        }
    }

    @Override
    public List<PlayerUnit> getAllForPlayer(final Player player) throws RetrieveError {
        try {
            return this.queryForEq("player_id", player.getAllycode());
        } catch (final SQLException exception) {
            throw new RetrieveError(className, "getForPlayer", exception.getMessage());
        }
    }

    @Override
    public PlayerUnit getForPlayer(final Player player, final String baseId) throws RetrieveError {
        try {
            return this.queryForId(baseId + "_" + player.getAllycode());
        } catch (final SQLException | ArrayIndexOutOfBoundsException exception) {
            throw new RetrieveError(className, "getForPlayer", exception.getMessage());
        }
    }

    @Override
    public int getGearCount(final Player player, final int gearLevel) throws RetrieveError {
        final QueryBuilder<PlayerUnit, String> queryBuilder = this.queryBuilder();
        try {
            PreparedQuery<PlayerUnit> query = queryBuilder.where().eq("gear", gearLevel).and().eq("player_id", player.getAllycode()).prepare();
            return this.query(query).size();
        } catch (final SQLException exception) {
            throw new RetrieveError(className, "getGearCount", exception.getMessage());
        }
    }

    @Override
    public int getZetaCount(final Player player) throws RetrieveError {
        String query = "SELECT count() FROM unitAbilities as t1 ";
        query += "INNER JOIN playerUnits AS t2 ";
        query += "INNER JOIN abilities AS t3 ";
        query += "WHERE t2.player_id = ? ";
        query += "AND t2.identifier = t1.playerUnit_id ";
        query += "AND t3.identifier = t1.baseAbility_id ";
        query += "AND t3.zeta = 1 ";
        query += "AND t1.level = t3.tierMax ";
        try {
            return (int) this.queryRawValue(query, Integer.toString(player.getAllycode()));
        } catch (final SQLException exception) {
            throw new RetrieveError(className, "getZetaCount", exception.getMessage());
        }
    }

    @Override
    public Map<Integer, Integer> getRelics(final Player player) throws RetrieveError {
        try {
            final Map<Integer, Integer> returnValue = new TreeMap<>();
            for (int level : SwgohConstants.RELIC_LEVELS) {
                final int count = this.queryForFieldValuesArgs(Map.of(
                        "player_id", player.getAllycode(),
                        "relic", level
                )).size();
                returnValue.put(level, count);
            }
            return returnValue;
        } catch (final SQLException exception) {
            throw new RetrieveError(className, "getRelics", exception.getMessage());
        }
    }
}
