package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Abbreviation;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.util.List;

/**
 * @author DJJ
 **/
public interface AbbreviationDao extends Dao<Abbreviation, Integer> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = AbbreviationDao.class.getName();

    /**
     * Gets the abbreviation for the given unit id.
     * @param identifier the unit id to search for.
     * @return a list with abbreviations.
     * @throws RetrieveError when something goes wrong.
     */
    List<Abbreviation> getById(final int identifier) throws RetrieveError;

    /**
     * Gets the abbreviation for the given unit.
     * @param unitId the unit to search for.
     * @return an abbreviation.
     * @throws RetrieveError when something goes wrong.
     */
    String getByUnitId(final String unitId) throws RetrieveError;

    /**
     * Saves the abbreviation in the DB.
     * @param abbreviation the abbreviation.
     * @throws InsertionError when something goes wrong.
     */
    void save(final Abbreviation abbreviation) throws InsertionError;

    /**
     * Deletes the Table.
     * @throws DeletionError when something goes wrong.
     */
    void clear() throws DeletionError;
}
