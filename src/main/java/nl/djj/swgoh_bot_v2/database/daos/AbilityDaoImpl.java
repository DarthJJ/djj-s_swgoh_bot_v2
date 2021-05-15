package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Ability;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.List;

/**
 * @author DJJ
 **/
public class AbilityDaoImpl extends BaseDaoImpl<Ability, String> implements AbilityDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public AbilityDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, Ability.class);
    }

    @Override
    public Ability getById(final String identifier) throws RetrieveError {
        try {
            return this.queryForId(identifier);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getById", exception.getMessage());
        }
    }

    @Override
    public List<Ability> getByUnitId(final String unitId) throws RetrieveError {
        try {
            return this.queryForEq("unitId", unitId);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getByUnitId", exception.getMessage());
        }
    }

    @Override
    public void save(final Ability ability) throws InsertionError {
        try {
            this.createOrUpdate(ability);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception.getMessage());
        }
    }
}
