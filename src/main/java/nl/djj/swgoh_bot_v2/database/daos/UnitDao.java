package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.Dao;
import nl.djj.swgoh_bot_v2.entities.db.Unit;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

/**
 * @author DJJ
 **/
public interface UnitDao extends Dao<Unit, String> {
    String className = UnitDao.class.getName();

    Unit getById(final String id) throws RetrieveError;

    void save(final Unit unit) throws InsertionError;
}
