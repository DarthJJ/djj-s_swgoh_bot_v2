package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.UnitAbility;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author DJJ
 **/
public class UnitAbilityDaoImpl extends BaseDaoImpl<UnitAbility, Integer> implements UnitAbilityDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public UnitAbilityDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, UnitAbility.class);
    }

    @Override
    public List<UnitAbility> getForUnit(final String unitId) throws RetrieveError {
        try {
            return this.queryForEq("playerUnit_id", unitId);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getForUnit", exception.getMessage());
        }
    }

    @Override
    public void save(final UnitAbility ability) throws InsertionError {
        try {
            final List<UnitAbility> found = this.queryForFieldValuesArgs(Map.of(
                    "playerUnit_id", ability.getPlayerUnit().getIdentifier(),
                    "baseAbility_id", ability.getBaseAbility().getIdentifier()
            ));
            if (!found.isEmpty()) {
                ability.setIdentifier(found.get(0).getIdentifier());
            }
            this.createOrUpdate(ability);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception.getMessage());
        }
    }

    @Override
    public void saveAll(final List<UnitAbility> abilities) throws InsertionError {
        try {
            this.callBatchTasks((Callable<Void>) () -> {
                for (final UnitAbility ability : abilities) {
                    this.save(ability);
                }
                return null;
            });
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "saveAll", exception.getMessage());
        }
    }
}