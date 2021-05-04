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
    String className = FarmingLocationDao.class.getName();

    List<FarmingLocation> getByUnitId(final String unitId) throws RetrieveError;

    List<FarmingLocation> getByLocation(final String location) throws RetrieveError;

    void save(final FarmingLocation farmingLocation) throws InsertionError;
}
