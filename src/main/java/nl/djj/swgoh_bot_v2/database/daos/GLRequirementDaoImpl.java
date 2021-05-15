package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.GLRequirement;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * @author DJJ
 **/
public class GLRequirementDaoImpl extends BaseDaoImpl<GLRequirement, Integer> implements GLRequirementDao {

    /**
     * Constructor.
     *
     * @param connection the DB Connection.
     **/
    public GLRequirementDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, GLRequirement.class);
    }

    @Override
    public List<GLRequirement> getForEvent(final String eventId) throws RetrieveError {
        try {
            return this.queryForEq("glEvent", eventId.toUpperCase(Locale.ROOT));
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getForEvent", exception.getMessage());
        }
    }

    @Override
    public void save(final GLRequirement requirement) throws InsertionError {
        try {
            this.createOrUpdate(requirement);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception.getMessage());
        }
    }

    @Override
    public void clear() throws DeletionError {
        try {
            this.updateRaw("DELETE FROM glRequirements WHERE 1 = 1;");
        } catch (final SQLException exception) {
            throw new DeletionError(CLASS_NAME, "clear", exception.getMessage());
        }
    }
}
