package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.util.List;

/**
 * @author DJJ
 **/
public interface PlayerUnitDao extends Dao<PlayerUnit, Integer> {
    String className = PlayerUnitDao.class.getName();

    void save(final PlayerUnit playerUnit) throws InsertionError;

    List<PlayerUnit> getAllForPlayer(final Player player) throws RetrieveError;

    PlayerUnit getForPlayer(final Player player, final String baseId) throws RetrieveError;
}
