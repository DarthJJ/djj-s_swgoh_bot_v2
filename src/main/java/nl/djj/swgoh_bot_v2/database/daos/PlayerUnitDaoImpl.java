package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.Main;
import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.entities.db.Guild;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

/**
 * @author DJJ
 **/
public class PlayerUnitDaoImpl extends BaseDaoImpl<PlayerUnit, String> implements PlayerUnitDao {

    /**
     * Constructor.
     *
     * @param connection the DB connection.
     **/
    public PlayerUnitDaoImpl(final ConnectionSource connection) throws SQLException {
        super(connection, PlayerUnit.class);
    }

    @Override
    public void save(final PlayerUnit playerUnit) throws InsertionError {
        try {
            this.createOrUpdate(playerUnit);
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "save", exception);
        }
    }

    @Override
    public void saveAll(final List<PlayerUnit> playerUnits) throws InsertionError {
        try {
            final File file = new File("player_units.csv");
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8)) {
                for (final PlayerUnit unit : playerUnits) {
                    writer.append(unit.getIdentifier());
                    writer.append(";");
                    writer.append(Integer.toString(unit.getPlayer().getAllycode()));
                    writer.append(";");
                    writer.append(unit.getUnit().getBaseId());
                    writer.append(";");
                    writer.append(Integer.toString(unit.getRarity()));
                    writer.append(";");
                    writer.append(Integer.toString(unit.getGalacticPower()));
                    writer.append(";");
                    writer.append(Integer.toString(unit.getGear()));
                    writer.append(";");
                    writer.append(Integer.toString(unit.getGearPieces()));
                    writer.append(";");
                    writer.append(Integer.toString(unit.getRelic()));
                    writer.append(";");
                    writer.append(Integer.toString(unit.getSpeed()));
                    writer.append("\n");
                }
                writer.flush();
            }
            final String query = String.format("COPY player_units_x(identifier, player_id, unit_id, rarity, galactic_power, gear, gear_pieces, relic, speed) " +
                    "FROM '%s'" +
                    "DELIMITER ';'" +
                    "CSV", file.getAbsolutePath());
            if (!Main.getDebug()) {
                this.executeRaw("CREATE TEMP TABLE player_units_x AS SELECT * FROM player_units LIMIT 0");
                this.executeRaw(query);
                this.executeRaw("INSERT INTO player_units (identifier, player_id, unit_id, rarity, galactic_power, gear, gear_pieces, relic, speed) " +
                        "SELECT identifier, player_id, unit_id, rarity, galactic_power, gear, gear_pieces, relic, speed " +
                        "FROM player_units_x " +
                        "ON CONFLICT (identifier) " +
                        "DO " +
                        "UPDATE " +
                        "SET " +
                        "player_id = excluded.player_id," +
                        "unit_id = excluded.unit_id," +
                        "rarity = excluded.rarity," +
                        "galactic_power = excluded.galactic_power," +
                        "gear = excluded.gear," +
                        "gear_pieces = excluded.gear_pieces," +
                        "relic = excluded.relic," +
                        "speed = excluded.speed;");
                this.executeRaw("DROP TABLE player_units_x");
                file.delete();
            } else {
                Main.getLogger().debug(CLASS_NAME, "Not inserting unitAbilities due to running on Windows.");
            }
        } catch (final SQLException | IOException exception) {
            throw new InsertionError(CLASS_NAME, "saveAll", exception);
        }
    }

    @Override
    public List<PlayerUnit> getAllForPlayer(final Player player) throws RetrieveError {
        try {
            return this.queryForEq("player_id", player.getAllycode());
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getForPlayer", exception);
        }
    }

    @Override
    public PlayerUnit getForPlayer(final Player player, final String baseId) throws RetrieveError {
        try {
            return this.queryForId(baseId + "_" + player.getAllycode());
        } catch (final SQLException | ArrayIndexOutOfBoundsException exception) {
            throw new RetrieveError(CLASS_NAME, "getForPlayer", exception);
        }
    }

    @Override
    public int getGearCount(final Player player, final int gearLevel) throws RetrieveError {
        final QueryBuilder<PlayerUnit, String> queryBuilder = this.queryBuilder();
        try {
            final PreparedQuery<PlayerUnit> query = queryBuilder.where().eq("gear", gearLevel).and().eq("player_id", player.getAllycode()).prepare();
            return this.query(query).size();
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getGearCount", exception);
        }
    }

    @Override
    public int getZetaCount(final Player player, @Nullable final String unitId) throws RetrieveError {
        String query = "SELECT count(*) FROM unit_abilities as t1 " +
                "INNER JOIN player_units AS t2 " +
                "ON t2.identifier = t1.player_unit " +
                "INNER JOIN abilities AS t3 " +
                "ON t3.identifier = t1.base_ability " +
                "WHERE t2.player_id = ? ::INTEGER " +
                "AND t3.zeta = true " +
                "AND t1.level = t3.tier_max ";
        if (unitId != null) {
            query += "AND t2.unit_id = ?";
        }
        try {
            final List<String> args = new ArrayList<>();
            args.add(Integer.toString(player.getAllycode()));
            if (unitId != null) {
                args.add(unitId);
            }
            return (int) this.queryRawValue(query, args.toArray(new String[0]));
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getZetaCount", exception);
        }
    }

    @Override
    public int getGearCount(final Guild guild, final int gearLevel, @Nullable final String unitId) throws RetrieveError {
        String query = "SELECT COUNT(*) " +
                "FROM player_units AS t1 " +
                "INNER JOIN players AS t2 " +
                "ON t2.allycode = t1.player_id " +
                "WHERE t2.guild_id = ? ::INTEGER " +
                "AND t1.gear = ? ::INTEGER ";
        if (unitId != null) {
            query += "AND t1.unit_id = ?";
        }
        try {
            final List<String> args = new ArrayList<>();
            args.add(Integer.toString(guild.getIdentifier()));
            args.add(Integer.toString(gearLevel));
            if (unitId != null) {
                args.add(unitId);
            }

            return (int) this.queryRawValue(query, args.toArray(new String[0]));
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getGearCount", exception);
        }
    }

    @Override
    public int getZetaCount(final Guild guild, @Nullable final String unitId) throws RetrieveError {
        String query = "SELECT count(*) FROM unit_abilities as t1 " +
                "INNER JOIN player_units AS t2  " +
                "ON t2.identifier = t1.player_unit " +
                "INNER JOIN abilities AS t3  " +
                "ON t3.identifier = t1.base_ability " +
                "INNER JOIN players AS t4 " +
                "ON t4.allycode = t2.player_id " +
                "WHERE t4.guild_id = ? ::INTEGER " +
                "AND t3.zeta = true " +
                "AND t1.level = t3.tier_max ";
        if (unitId != null) {
            query += "AND t2.unit_id = ?";
        }
        try {
            final List<String> args = new ArrayList<>();
            args.add(Integer.toString(guild.getIdentifier()));
            if (unitId != null) {
                args.add(unitId);
            }
            return (int) this.queryRawValue(query, args.toArray(new String[0]));
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getZetaCount", exception);
        }

    }

    @Override
    public Map<Integer, Integer> getRelics(final Guild guild) throws RetrieveError {
        final Map<Integer, Integer> returnValue = new LinkedHashMap<>();
        try {
            for (final int level : SwgohConstants.RELIC_LEVELS) {
                final String query = "SELECT count(t2.relic) AS relics " +
                        "FROM players AS t1 " +
                        "INNER JOIN player_units AS t2 " +
                        "ON t2.player_id = t1.allycode " +
                        "WHERE t1.guild_id = ? ::INTEGER " +
                        "AND t2.relic = ? ::INTEGER";
                returnValue.put(level, (int) this.queryRawValue(query, Integer.toString(guild.getIdentifier()), Integer.toString(level)));
            }
            return returnValue;
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getRelics", exception);
        }
    }

    @Override
    public int getRelicCountForUnit(final Guild guild, final int level, @Nullable final String baseId) throws RetrieveError {
        try {
            String query = "SELECT count(t2.relic) AS relics " +
                    "FROM players AS t1 " +
                    "INNER JOIN player_units AS t2 " +
                    "ON t2.player_id = t1.allycode " +
                    "WHERE t1.guild_id = ? ::INTEGER " +
                    "AND t2.relic = ? ::INTEGER ";
            if (baseId != null) {
                query += "AND t2.unit_id = ?";
            }
            final List<String> args = new ArrayList<>();
            args.add(Integer.toString(guild.getIdentifier()));
            args.add(Integer.toString(level));
            if (baseId != null) {
                args.add(baseId);
            }
            return (int) this.queryRawValue(query, args.toArray(new String[0]));
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getRelicCountForUnit", exception);
        }
    }

    @Override
    public int getRarityCountForUnit(final Guild guild, final int level, @Nullable final String baseId) throws RetrieveError {
        try {
            String query = "SELECT count(t2.relic) AS relics " +
                    "FROM players AS t1 " +
                    "INNER JOIN player_units AS t2 " +
                    "ON t2.player_id = t1.allycode " +
                    "WHERE t1.guild_id = ? ::INTEGER " +
                    "AND t2.rarity = ? ::INTEGER ";
            if (baseId != null) {
                query += "AND t2.unit_id = ?";
            }
            final List<String> args = new ArrayList<>();
            args.add(Integer.toString(guild.getIdentifier()));
            args.add(Integer.toString(level));
            if (baseId != null) {
                args.add(baseId);
            }
            return (int) this.queryRawValue(query, args.toArray(new String[0]));
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getRarityCountForUnit", exception);
        }
    }

    @Override
    public Map<Integer, Integer> getRelics(final Player player) throws RetrieveError {
        try {
            final Map<Integer, Integer> returnValue = new TreeMap<>();
            for (final int level : SwgohConstants.RELIC_LEVELS) {
                final int count = this.queryForFieldValuesArgs(Map.of(
                        "player_id", player.getAllycode(),
                        "relic", level
                )).size();
                returnValue.put(level, count);
            }
            return returnValue;
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getRelics", exception);
        }
    }
}
