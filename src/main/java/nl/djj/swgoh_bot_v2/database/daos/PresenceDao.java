package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Presence;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;

/**
 * @author DJJ
 **/
public interface PresenceDao extends Dao<Presence, Integer> {
    String className = PresenceDao.class.getName();

    void add(final Presence presence) throws InsertionError;
}
