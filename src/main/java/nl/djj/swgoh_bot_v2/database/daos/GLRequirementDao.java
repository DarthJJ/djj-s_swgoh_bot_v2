package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.GlRequirement;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.util.List;

/**
 * @author DJJ
 **/
public interface GLRequirementDao extends Dao<GlRequirement, Integer> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = GLRequirementDao.class.getName();

    /**
     * Gets all the requirements for a given event.
     * @param eventId the id of the event.
     * @return a list of requirements.
     * @throws RetrieveError when something goes wrong.
     */
    List<GlRequirement> getForEvent(final String eventId) throws RetrieveError;

    /**
     * Saves a requirement to the DB.
     * @param requirement the requirement.
     * @throws InsertionError when something goes wrong.
     */
    void save(final GlRequirement requirement) throws InsertionError;

    /**
     * Clears the table.
     * @throws DeletionError when something goes wrong.
     */
    void clear() throws DeletionError;

}
