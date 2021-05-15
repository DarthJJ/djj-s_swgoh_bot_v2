package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.FarmingLocation;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.util.List;

/**
 * @author DJJ
 **/
public interface FarmingLocationDao extends Dao<FarmingLocation, Integer> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = FarmingLocationDao.class.getName();

    /**
     * Gets a list of locations for the given unit id.
     * @param unitId the unit id.
     * @return a list of locations.
     * @throws RetrieveError when something goes wrong.
     */
    List<FarmingLocation> getByUnitId(final String unitId) throws RetrieveError;

    /**
     * Gets a list of locations based on the location.
     * @param location the location.
     * @return a list.
     * @throws RetrieveError when something goes wrong.
     */
    List<FarmingLocation> getByLocation(final String location) throws RetrieveError;

    /**
     * Saves a location to the DB.
     * @param farmingLocation the location.
     * @throws InsertionError when something goes wrong.
     */
    void save(final FarmingLocation farmingLocation) throws InsertionError;
}
