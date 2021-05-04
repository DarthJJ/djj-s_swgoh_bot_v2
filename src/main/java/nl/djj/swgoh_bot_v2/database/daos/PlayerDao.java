package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface PlayerDao extends Dao<Player, Integer> {
    String className = PlayerDao.class.getName();

    Player getById(final int allycode) throws RetrieveError;

    void save(final Player player) throws InsertionError;

    Player getByDiscordId(final String discordId) throws RetrieveError;

    boolean exists(final int allycode) throws RetrieveError;

    void delete(final int allycode) throws DeletionError;
}
