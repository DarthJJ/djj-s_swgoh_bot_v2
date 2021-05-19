package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.GlRequirement;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * @author DJJ
 **/
public class GLRequirementDaoImpl extends BaseDaoImpl<GlRequirement, Integer> implements GLRequirementDao {

    /**
     * Constructor.
     *
     * @param connection the DB Connection.
     **/
    public GLRequirementDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, GlRequirement.class);
    }

    @Override
    public List<GlRequirement> getForEvent(final String eventId) throws RetrieveError {
        try {
            return this.queryForEq("gl_event", eventId.toUpperCase(Locale.ROOT));
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getForEvent", exception);
        }
    }

    @Override
    public void save(final GlRequirement requirement) throws InsertionError {
        try {
            this.createOrUpdate(requirement);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception);
        }
    }
}
