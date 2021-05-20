package nl.djj.swgoh_bot_v2.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Log;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import nl.djj.swgoh_bot_v2.entities.db.*;
import nl.djj.swgoh_bot_v2.exceptions.InitializationError;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.sql.SQLException;

/**
 * @author DJJ
 **/
public class Database {
    private final transient String className = this.getClass().getName();
    private final transient Logger logger;
    private final transient ConnectionSource connection;
    private final transient DAO dao;

    /**
     * Constructor.
     *
     * @param logger   the logger.
     * @param username the username for DB access.
     * @param address  the DB location.
     * @param password the password for DB access.
     * @param dbName   the DB to connect to.
     **/
    public Database(final Logger logger, final String username, final String address, final String password, final String dbName) throws InitializationError {
        try {
            this.logger = logger;
            com.j256.ormlite.logger.Logger.setGlobalLogLevel(Log.Level.ERROR);
            final String databaseUrl = String.format("jdbc:postgresql://%s/%s?user=%s&password=%s&ssl=false", address, dbName, username, password);
            connection = new JdbcConnectionSource(databaseUrl);
            dao = new DAO(connection);
        } catch (final SQLException exception) {
            throw new InitializationError(className, "Constructor", exception);
        }
    }

    /**
     * Called to created the database.
     */
    //CHECKSTYLE.OFF: CyclomaticComplexityCheck
    //CHECKSTYLE.OFF: NPathComplexityCheck
    public void createDatabase() {
        logger.info(className, "creating Database");
        try {
            if (!dao.abilityDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, Ability.class);
            }
            if (!dao.abbreviationDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, Abbreviation.class);
            }
            if (!dao.commandDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, Command.class);
            }
            if (!dao.commandUsageDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, CommandUsage.class);
            }
            if (!dao.configDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, Config.class);
            }
            if (!dao.farmingLocationDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, FarmingLocation.class);
            }
            if (!dao.flagDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, Flag.class);
            }
            if (!dao.glRequirementDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, GlRequirement.class);
            }
            if (!dao.guildDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, Guild.class);
            }
            if (!dao.playerDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, Player.class);
            }
            if (!dao.playerUnitDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, PlayerUnit.class);
            }
            if (!dao.presenceDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, Presence.class);
            }
            if (!dao.unitDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, Unit.class);
            }
            if (!dao.unitAbilityDao().isTableExists()) {
                TableUtils.createTableIfNotExists(connection, UnitAbility.class);
            }
        } catch (final SQLException exception) {
            logger.error(className, "createDatabase", exception.getMessage());
        }
    }
    //CHECKSTYLE.ON: CyclomaticComplexityCheck
    //CHECKSTYLE.ON: NPathComplexityCheck

    public DAO dao() {
        return dao;
    }
}
