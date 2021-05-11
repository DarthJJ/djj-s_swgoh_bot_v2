package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Unit;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface UnitDao extends Dao<Unit, String> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = UnitDao.class.getName();

    /**
     * Gets an unit by id.
     * @param unitId the unit id.
     * @return a Unit object.
     * @throws RetrieveError when something goes wrong.
     */
    Unit getById(final String unitId) throws RetrieveError;

    /**
     * Saves a unit object to the DB.
     * @param unit the unit object.
     * @throws InsertionError when something goes wrong.
     */
    void save(final Unit unit) throws InsertionError;
}
