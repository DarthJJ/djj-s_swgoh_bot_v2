package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.UnitAbility;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.util.List;

/**
 * @author DJJ
 **/
public interface UnitAbilityDao extends Dao<UnitAbility, Integer> {
    /**
     * The name of the implementation.
     */
    String CLASS_NAME = UnitAbilityDao.class.getName();

    /**
     * Gets the unit abilities for the given unit.
     * @param unitId the unitId.
     * @return a list with abilities.
     * @throws RetrieveError when something goes wrong.
     */
    List<UnitAbility> getForUnit(final String unitId) throws RetrieveError;

    /**
     * Saves an unitAbility to the DB.
     * @param ability the ability to save.
     * @throws InsertionError when something goes wrong.
     */
    void save(final UnitAbility ability) throws InsertionError;

    /**
     * Saves a list of abilities to the DB.
     * @param abilities the list to save.
     * @throws InsertionError when something goes wrong.
     */
    void saveAll(final List<UnitAbility> abilities) throws InsertionError;
}
