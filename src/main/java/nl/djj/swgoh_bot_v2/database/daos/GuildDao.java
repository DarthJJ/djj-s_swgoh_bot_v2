package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Guild;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface GuildDao extends Dao<Guild, Integer> {
    String className = GuildDao.class.getName();

    Guild getById(final int guildId) throws RetrieveError;

    void save(final Guild guild) throws InsertionError;

}
