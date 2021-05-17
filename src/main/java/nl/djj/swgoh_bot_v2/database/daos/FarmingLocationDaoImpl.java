package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.FarmingLocation;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.List;

/**
 * @author DJJ
 **/
public class FarmingLocationDaoImpl extends BaseDaoImpl<FarmingLocation, Integer> implements FarmingLocationDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public FarmingLocationDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, FarmingLocation.class);
    }

    @Override
    public List<FarmingLocation> getByUnitId(final String unitId) throws RetrieveError {
        try {
            return this.queryForEq("unitId", unitId);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getByUnitId", exception);
        }
    }

    @Override
    public List<FarmingLocation> getByLocation(final String location) throws RetrieveError {
        try {
            return this.queryForEq("location", location);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getByLocation", exception);
        }
    }

    @Override
    public void save(final FarmingLocation farmingLocation) throws InsertionError {
        try {
            this.createOrUpdate(farmingLocation);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception);
        }
    }
}
