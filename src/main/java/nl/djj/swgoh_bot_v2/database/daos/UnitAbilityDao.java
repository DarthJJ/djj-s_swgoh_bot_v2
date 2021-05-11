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
    String className = UnitAbilityDao.class.getName();

    List<UnitAbility> getForUnit(final String unitId) throws RetrieveError;

    void save(final UnitAbility ability) throws InsertionError;

    void saveAll(final List<UnitAbility> abilities) throws InsertionError;
}
