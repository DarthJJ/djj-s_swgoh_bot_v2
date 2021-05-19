package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.entities.db.UnitAbility;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author DJJ
 **/
public class UnitAbilityDaoImpl extends BaseDaoImpl<UnitAbility, Integer> implements UnitAbilityDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public UnitAbilityDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, UnitAbility.class);
    }

    @Override
    public List<UnitAbility> getForUnit(final String unitId) throws RetrieveError {
        try {
            return this.queryForEq("playerUnit_id", unitId);
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getForUnit", exception);
        }
    }

    @Override
    public void save(final UnitAbility ability) throws InsertionError {
        try {
            this.createOrUpdate(ability);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception);
        }
    }

    @Override
    public void saveAll(final List<UnitAbility> abilities) throws InsertionError {
        try {
            File file = new File("test.csv");
            FileWriter writer = new FileWriter(file);
            for(final UnitAbility ability : abilities){
                writer.append(ability.getIdentifier());
                writer.append(';');
                writer.append(ability.getPlayerUnit().getIdentifier());
                writer.append(';');
                writer.append(ability.getBaseAbility().getIdentifier());
                writer.append(';');
                writer.append(Integer.toString(ability.getLevel()));
                writer.append('\n');
            }
            writer.flush();
            writer.close();
            final String query = String.format("COPY unit_abilities(identifier,player_unit, base_ability, level) " +
                    "FROM '%s'" +
                    "DELIMITER ';'" +
                    "CSV", file.getAbsolutePath());
            if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("nix")){
                this.executeRaw(query);
                file.delete();
            }
        } catch (final SQLException | IOException exception) {
            throw new InsertionError(CLASS_NAME, "saveAll", exception);
        }
    }
}


