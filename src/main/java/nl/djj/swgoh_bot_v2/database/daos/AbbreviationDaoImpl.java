package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Abbreviation;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.List;

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
    public List<Abbreviation> getByUnitId(final int unitId) throws RetrieveError {
        try {
            return this.queryForEq("unitId", unitId);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getByUnitId", exception.getMessage());
        }
    }

    @Override
    public void save(final Abbreviation abbreviation) throws InsertionError {
        try {
            this.createOrUpdate(abbreviation);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "saveAbbreviation", exception.getMessage());
        }
    }

    @Override
    public void clear() throws DeletionError {
        try {
            this.updateRaw("DELETE FROM abbreviations WHERE 1 = 1;");
        } catch (final SQLException exception) {
            throw new DeletionError(CLASS_NAME, "clear", exception.getMessage());
        }
    }
}
