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
    String className = AbbreviationDao.class.getName();

    List<Abbreviation> getByPlayerUnitId(final int unitId) throws RetrieveError;

    void save(final Abbreviation abbreviation) throws InsertionError;

    void clear() throws DeletionError;
}
