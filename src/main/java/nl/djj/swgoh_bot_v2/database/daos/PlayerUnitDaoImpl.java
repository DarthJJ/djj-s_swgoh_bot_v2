package nl.djj.swgoh_bot_v2.database.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.entities.db.Guild;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;

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
            throw new InsertionError(CLASS_NAME, "save", exception.getMessage());
        }
    }

    @Override
    public void saveAll(final List<PlayerUnit> playerUnits) throws InsertionError {
        try {
            this.callBatchTasks((Callable<Void>) () -> {
                for (final PlayerUnit playerUnit : playerUnits) {
                    this.save(playerUnit);
                }
                return null;
            });
        } catch (final SQLException exception) {
            throw new InsertionError(CLASS_NAME, "saveAll", exception.getMessage());
        }
    }

    @Override
    public List<PlayerUnit> getAllForPlayer(final Player player) throws RetrieveError {
        try {
            return this.queryForEq("player_id", player.getAllycode());
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getForPlayer", exception.getMessage());
        }
    }

    @Override
    public PlayerUnit getForPlayer(final Player player, final String baseId) throws RetrieveError {
        try {
            return this.queryForId(baseId + "_" + player.getAllycode());
        } catch (final SQLException | ArrayIndexOutOfBoundsException exception) {
            throw new RetrieveError(CLASS_NAME, "getForPlayer", exception.getMessage());
        }
    }

    @Override
    public int getGearCount(final Player player, final int gearLevel) throws RetrieveError {
        final QueryBuilder<PlayerUnit, String> queryBuilder = this.queryBuilder();
        try {
            final PreparedQuery<PlayerUnit> query = queryBuilder.where().eq("gear", gearLevel).and().eq("player_id", player.getAllycode()).prepare();
            return this.query(query).size();
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getGearCount", exception.getMessage());
        }
    }

    @Override
    public int getZetaCount(final Player player) throws RetrieveError {
        final String query = "SELECT count() FROM unitAbilities as t1 " +
                "INNER JOIN playerUnits AS t2 " +
                "INNER JOIN abilities AS t3 " +
                "WHERE t2.player_id = ? " +
                "AND t2.identifier = t1.playerUnit_id " +
                "AND t3.identifier = t1.baseAbility_id " +
                "AND t3.zeta = 1 " +
                "AND t1.level = t3.tierMax ";
        try {
            return (int) this.queryRawValue(query, Integer.toString(player.getAllycode()));
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getZetaCount", exception.getMessage());
        }
    }

    @Override
    public int getGearCount(final Guild guild, final int gearLevel, @Nullable final String unitId) throws RetrieveError {
        String query = "SELECT COUNT() " +
                "FROM playerUnits AS t1 " +
                "INNER JOIN players AS t2 " +
                "WHERE t2.guild_id = ? " +
                "AND t2.allycode = t1.player_id " +
                "AND t1.gear = ? ";
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
            throw new RetrieveError(CLASS_NAME, "getGearCount", exception.getMessage());
        }
    }

    @Override
    public int getZetaCount(final Guild guild, @Nullable final String unitId) throws RetrieveError {
        String query = "SELECT count() FROM unitAbilities as t1 " +
                "INNER JOIN playerUnits AS t2  " +
                "INNER JOIN abilities AS t3  " +
                "INNER JOIN players AS t4 " +
                "WHERE t4.guild_id = ? " +
                "AND t2.player_id = t4.allycode " +
                "AND t2.identifier = t1.playerUnit_id  " +
                "AND t3.identifier = t1.baseAbility_id " +
                "AND t3.zeta = 1 " +
                "AND t1.level = t3.tierMax ";
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
            throw new RetrieveError(CLASS_NAME, "getZetaCount", exception.getMessage());
        }

    }

    @Override
    public Map<Integer, Integer> getRelics(final Guild guild) throws RetrieveError {
        final Map<Integer, Integer> returnValue = new LinkedHashMap<>();
        try {
            for (final int level : SwgohConstants.RELIC_LEVELS) {
                final String query = "SELECT count(t2.relic) AS relics " +
                        "FROM players AS t1 " +
                        "INNER JOIN playerUnits AS t2 " +
                        "WHERE t1.guild_id = ? " +
                        "AND t2.player_id = t1.allycode " +
                        "AND t2.relic = ? ";
                returnValue.put(level, (int) this.queryRawValue(query, Integer.toString(guild.getIdentifier()), Integer.toString(level)));
            }
            return returnValue;
        } catch (final SQLException exception) {
            throw new RetrieveError(CLASS_NAME, "getRelics", exception.getMessage());
        }
    }

    @Override
    public int getRelicCountForUnit(final Guild guild, final int level, @Nullable final String baseId) throws RetrieveError {
        try {
            String query = "SELECT count(t2.relic) AS relics " +
                    "FROM players AS t1 " +
                    "INNER JOIN playerUnits AS t2 " +
                    "WHERE t1.guild_id = ? " +
                    "AND t2.player_id = t1.allycode " +
                    "AND t2.relic = ? ";
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
            throw new RetrieveError(CLASS_NAME, "getRelicCountForUnit", exception.getMessage());
        }
    }

    @Override
    public int getRarityCountForUnit(final Guild guild, final int level, @Nullable final String baseId) throws RetrieveError {
        try {
            String query = "SELECT count(t2.rarity) AS rarity " +
                    "FROM players AS t1 " +
                    "INNER JOIN playerUnits AS t2 " +
                    "WHERE t1.guild_id = ? " +
                    "AND t2.player_id = t1.allycode " +
                    "AND t2.rarity = ? ";
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
            throw new RetrieveError(CLASS_NAME, "getRarityCountForUnit", exception.getMessage());
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
            throw new RetrieveError(CLASS_NAME, "getRelics", exception.getMessage());
        }
    }
}
