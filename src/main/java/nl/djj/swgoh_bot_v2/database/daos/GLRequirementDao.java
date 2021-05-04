package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.GLRequirement;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.sql.SQLException;
import java.util.List;

/**
 * @author DJJ
 **/
public interface GLRequirementDao extends Dao<GLRequirement, Integer> {
    String className = GLRequirementDao.class.getName();

    List<GLRequirement> getForEvent(final String eventId) throws RetrieveError;

    void save(final GLRequirement requirement) throws InsertionError;

    void clear() throws DeletionError;

}
