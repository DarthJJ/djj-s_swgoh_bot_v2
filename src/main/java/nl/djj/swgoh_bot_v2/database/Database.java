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
     * @param logger the logger.
     **/
    public Database(final Logger logger) throws InitializationError {
        try {
            this.logger = logger;
            com.j256.ormlite.logger.Logger.setGlobalLogLevel(Log.Level.ERROR);
            final String databaseUrl = "jdbc:sqlite:database/data.sqlite";
            connection = new JdbcConnectionSource(databaseUrl);
            dao = new DAO(connection);
        } catch (final SQLException exception) {
            throw new InitializationError(className, "Constructor", exception.getMessage());
        }
    }

    /**
     * Called to created the database.
     */
    public void createDatabase() {
        logger.info(className, "creating Database");
        try {
            TableUtils.createTableIfNotExists(connection, Ability.class);
            TableUtils.createTableIfNotExists(connection, Abbreviation.class);
            TableUtils.createTableIfNotExists(connection, Command.class);
            TableUtils.createTableIfNotExists(connection, CommandUsage.class);
            TableUtils.createTableIfNotExists(connection, Config.class);
            TableUtils.createTableIfNotExists(connection, FarmingLocation.class);
            TableUtils.createTableIfNotExists(connection, GLRequirement.class);
            TableUtils.createTableIfNotExists(connection, Guild.class);
            TableUtils.createTableIfNotExists(connection, Player.class);
            TableUtils.createTableIfNotExists(connection, PlayerUnit.class);
            TableUtils.createTableIfNotExists(connection, Presence.class);
            TableUtils.createTableIfNotExists(connection, Unit.class);
            TableUtils.createTableIfNotExists(connection, UnitAbility.class);
        } catch (final SQLException exception) {
            logger.error(className, "createDatabase", exception.getMessage());
        }
    }

    public DAO dao() {
        return dao;
    }
}
