package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author DJJ
 **/
public class PlayerUnitDaoImpl extends BaseDaoImpl<PlayerUnit, Integer> implements PlayerUnitDao {

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
            final List<PlayerUnit> found = this.queryForFieldValuesArgs(Map.of(
                    "player_id", playerUnit.getPlayer().getAllycode(),
                    "unit_id", playerUnit.getUnit().getBaseId()
            ));
            if (found.size() > 0){
                playerUnit.setIdentifier(found.get(0).getIdentifier());
            }
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
            return this.queryForFieldValuesArgs(Map.of(
                    "player_id", player.getAllycode(),
                    "unit_id", baseId
            )).get(0);
        } catch (final SQLException | ArrayIndexOutOfBoundsException exception){
            throw new RetrieveError(className, "getForPlayer", exception.getMessage());
        }
    }
}
