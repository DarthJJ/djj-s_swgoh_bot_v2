package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Abbreviation;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * @author DJJ
 **/
public class AbbreviationDaoImpl extends BaseDaoImpl<Abbreviation, Integer> implements AbbreviationDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public AbbreviationDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, Abbreviation.class);
    }

    @Override
    public List<Abbreviation> getById(final int identifier) throws RetrieveError {
        try {
            return this.queryForEq("unitId", identifier);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getById", exception);
        }
    }

    @Override
    public String getByUnitId(final String unitId) throws RetrieveError {
        try {
            final List<Abbreviation> result = this.queryForEq("unit_id", unitId);
            if (result == null || result.isEmpty()) {
                return unitId;
            }
            return result.get(0).getAbbreviation();
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getByUnitId", exception);
        }
    }

    @Override
    public void save(final Abbreviation abbreviation) throws InsertionError {
        try {
            this.createOrUpdate(abbreviation);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "saveAbbreviation", exception);
        }
    }

    @Override
    public void clear() throws DeletionError {
        try {
            this.updateRaw("DELETE FROM abbreviations WHERE 1 = 1;");
        } catch (final SQLException exception) {
            throw new DeletionError(CLASS_NAME, "clear", exception);
        }
    }

    @Override
    public String resolveUnitId(String searchKey) throws RetrieveError {
        try {
            final String query = "SELECT unit_id " +
                    "FROM abbreviations " +
                    "WHERE unit_id ILIKE ?" +
                    "OR abbreviation ILIKE ?" +
                    "OR unit_name ILIKE ?";
            final GenericRawResults<String[]> results= this.queryRaw(query, searchKey, searchKey, searchKey);
            return results.getResults().get(0)[0];
        } catch (final SQLException | IndexOutOfBoundsException exception){
            throw new RetrieveError(CLASS_NAME, "resolveUnitId", exception);
        }
    }
}
