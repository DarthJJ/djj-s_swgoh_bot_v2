package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Unit;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.Locale;

/**
 * @author DJJ
 **/
public class UnitDaoImpl extends BaseDaoImpl<Unit, String> implements UnitDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public UnitDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, Unit.class);
    }

    @Override
    public Unit getById(final String unitId) throws RetrieveError {
        try {
            return this.queryForId(unitId.toUpperCase(Locale.ROOT));
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getById", exception.getMessage());
        }
    }

    @Override
    public void save(final Unit unit) throws InsertionError {
        try {
            this.createOrUpdate(unit);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception.getMessage());
        }
    }
}
