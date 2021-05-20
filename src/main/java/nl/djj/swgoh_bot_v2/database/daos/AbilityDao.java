package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Ability;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.util.List;
import java.util.Map;

/**
 * @author DJJ
 **/
public interface AbilityDao extends Dao<Ability, String> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = AbilityDao.class.getName();

    /**
     * gets an ability for the given id.
     *
     * @param identifier the id.
     * @return the ability.
     * @throws RetrieveError when something goes wrong.
     */
    Ability getById(final String identifier) throws RetrieveError;

    /**
     * Gets all the abilities for the given unit.
     *
     * @param unitId the unit id to search for.
     * @return a list with abilities.
     * @throws RetrieveError when something goes wrong.
     */
    List<Ability> getByUnitId(final String unitId) throws RetrieveError;

    /**
     * Gets all abilities in a map, key = abilityId.
     *
     * @return a map with abilities.
     * @throws RetrieveError when something goes wrong.
     */
    Map<String, Ability> getAll() throws RetrieveError;

    /**
     * Saves the unit ability to the DB.
     *
     * @param ability the ability to save.
     * @throws InsertionError when something goes wrong.
     */
    void save(final Ability ability) throws InsertionError;

    /**
     * Saves a list of unit abilities to the DB.
     *
     * @param abilities the list of abilities.
     * @throws InsertionError when something goes wrong.
     */
    void saveAll(final List<Ability> abilities) throws InsertionError;
}
