package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Guild;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author DJJ
 **/
public interface PlayerUnitDao extends Dao<PlayerUnit, String> {
    String className = PlayerUnitDao.class.getName();

    void save(final PlayerUnit playerUnit) throws InsertionError;

    void saveAll(final List<PlayerUnit> playerUnits) throws InsertionError;

    List<PlayerUnit> getAllForPlayer(final Player player) throws RetrieveError;

    PlayerUnit getForPlayer(final Player player, final String baseId) throws RetrieveError;

    int getGearCount(final Player player, final int gearLevel) throws RetrieveError;

    int getZetaCount(final Player player) throws RetrieveError;

    int getGearCount(final Guild guild, final int gearLevel, @Nullable final String unitId) throws RetrieveError;

    int getZetaCount(final Guild guild, @Nullable final String unitId) throws RetrieveError;

    Map<Integer, Integer> getRelics(final Guild guild) throws RetrieveError;

    int getRelicCountForUnit(final Guild guild, final int level, @Nullable final String baseId) throws RetrieveError;

    int getRarityCountForUnit(final Guild guild, final int level, @Nullable final String baseId) throws RetrieveError;

    Map<Integer, Integer> getRelics(final Player player) throws RetrieveError;
}
