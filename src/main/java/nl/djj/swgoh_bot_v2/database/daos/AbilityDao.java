package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Ability;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.util.List;

/**
 * @author DJJ
 **/
public interface AbilityDao extends Dao<Ability, String> {
    String className = AbilityDao.class.getName();

    Ability getById(final String identifier) throws RetrieveError;

    List<Ability> getByUnitId(final String unitId) throws RetrieveError;

    void save(final Ability ability) throws InsertionError;
}
