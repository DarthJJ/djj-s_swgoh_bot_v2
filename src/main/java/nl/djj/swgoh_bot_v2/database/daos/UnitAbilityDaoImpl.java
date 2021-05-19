package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.Main;
import nl.djj.swgoh_bot_v2.entities.db.UnitAbility;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

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
            final File file = new File("unit_abilities.csv");
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8)) {
                for (final UnitAbility ability : abilities) {
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
            }
            final String query = String.format("COPY unit_abilities_x(identifier,player_unit, base_ability, level) " +
                    "FROM '%s'" +
                    "DELIMITER ';'" +
                    "CSV", file.getAbsolutePath());
            if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("nux")) {
                this.executeRaw("CREATE TEMP TABLE unit_abilities_x AS SELECT * FROM unit_abilities LIMIT 0");
                this.executeRaw(query);
                this.executeRaw("UPDATE unit_abilities SET " +
                        "player_unit = unit_abilities_x.player_unit, " +
                        "base_ability = unit_abilities_x.base_ability, " +
                        "level = unit_abilities_x.level " +
                        "FROM unit_abilities_x " +
                        "WHERE unit_abilities.identifier = unit_abilities_x.identifier;");
                this.executeRaw("DROP TABLE unit_abilities_x");
                file.delete();
            } else {
                Main.getLogger().debug(CLASS_NAME, "Not inserting unitAbilities due to running on Windows.");
            }
        } catch (final SQLException | IOException exception) {
            throw new InsertionError(CLASS_NAME, "saveAll", exception);
        }
    }
}


