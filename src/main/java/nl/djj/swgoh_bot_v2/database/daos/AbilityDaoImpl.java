package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.Ability;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

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
            throw new RetrieveError(CLASS_NAME, "getById", exception);
        }
    }

    @Override
    public List<Ability> getByUnitId(final String unitId) throws RetrieveError {
        try {
            return this.queryForEq("unit_id", unitId);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getByUnitId", exception);
        }
    }

    @Override
    public Map<String, Ability> getAll() throws RetrieveError {
        try {
            return queryForAll().stream().collect(Collectors.toMap(Ability::getIdentifier, ability -> ability));
        } catch (final SQLException exception){
            throw new RetrieveError(CLASS_NAME, "getAll", exception);
        }
    }

    @Override
    public void save(final Ability ability) throws InsertionError {
        try {
            this.createOrUpdate(ability);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception);
        }
    }

    @Override
    public void saveAll(final List<Ability> abilities) throws InsertionError {
        try {
            this.callBatchTasks((Callable<Void>) () -> {
                for (final Ability ability : abilities) {
                    this.save(ability);
                }
                return null;
            });
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "saveAll", exception);
        }
    }
}
