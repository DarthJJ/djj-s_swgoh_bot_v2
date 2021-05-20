package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Unit;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.util.List;
import java.util.Map;

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

    /**
     * Saves a list of unit objets to the DB.
     * @param units the list of units.
     * @throws InsertionError when something goes wrong.
     */
    void saveAll(final List<Unit> units) throws InsertionError;

    /**
     * Gets all the units in the DB.
     * @return a list with all the units.
     * @throws RetrieveError when something goes wrong.
     */
    List<Unit> getAll() throws RetrieveError;

    /**
     * Gets all the units in the DB as a map.
     * @return the map.
     * @throws RetrieveError when something goes wrong.
     */
    Map<String, Unit> getAllAsMap() throws RetrieveError;
}
