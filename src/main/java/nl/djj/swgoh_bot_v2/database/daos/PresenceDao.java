package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Presence;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;

/**
 * @author DJJ
 **/
public interface PresenceDao extends Dao<Presence, Integer> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = PresenceDao.class.getName();

    /**
     * Adds a presence update to the DB.
     * @param presence the presence update.
     * @throws InsertionError when something goes wrong.
     */
    void add(final Presence presence) throws InsertionError;
}
