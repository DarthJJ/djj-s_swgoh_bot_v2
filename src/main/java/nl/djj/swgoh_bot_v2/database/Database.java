package nl.djj.swgoh_bot_v2.database;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CreateTableQuery;
import com.healthmarketscience.sqlbuilder.DropQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.dbspec.Constraint;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbConstraint;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author DJJ
 */
public class Database extends TableNames {
    private transient Connection connection;
    private final transient Logger logger;
    private final transient String className = this.getClass().getSimpleName();
    private transient DatabaseHandler databaseHandler;


    /**
     * Constructor.
     *
     * @param logger the program logger.
     */
    public Database(final Logger logger) {
        super();
        this.logger = logger;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database/data.sqlite");
            connection.endRequest();
            this.databaseHandler = new DatabaseHandler(logger, connection.createStatement(), connection) {
                @Override
                public void createDatabase() {
                    Database.this.createDatabase();
                }
            };
        } catch (final SQLException exception) {
            logger.error(className, "Constructor", exception.getMessage());
        }
    }

    public DatabaseHandler getDatabaseHandler() {
        return this.databaseHandler;
    }

    /**
     * Creates the database when called.
     */
    //CHECKSTYLE.OFF: MultipleStringLiteralsCheck
    public void createDatabase() {
        try {
            //Command Table
            if (checkIfTableExists(COMMAND.getName())) {
                COMMAND_NAME.addConstraint(new DbConstraint(COMMAND_NAME, "CommandNamePK", Constraint.Type.PRIMARY_KEY));
                COMMAND_ENABLED.addConstraint(new DbConstraint(COMMAND_ENABLED, "EnabledNotNull", Constraint.Type.NOT_NULL));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(COMMAND, true).validate().toString());
            }
            //Unit Table
            if (checkIfTableExists(UNIT_INFO.getName())) {
                UNIT_BASE_ID.addConstraint(new DbConstraint(UNIT_BASE_ID, "UnitBaseIdPK", Constraint.Type.PRIMARY_KEY));
                UNIT_NAME.addConstraint(new DbConstraint(UNIT_NAME, "UnitNameNotNull", Constraint.Type.NOT_NULL));
                UNIT_ALIGNMENT.addConstraint(new DbConstraint(UNIT_ALIGNMENT, "UnitAlignmentNotNull", Constraint.Type.NOT_NULL));
                UNIT_IS_CHARACTER.addConstraint(new DbConstraint(UNIT_IS_CHARACTER, "UnitIsCharacterNotNull", Constraint.Type.NOT_NULL));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(UNIT_INFO, true).validate().toString());
            }
            //Abbreviations Table
            if (checkIfTableExists(ABBREVIATIONS.getName())) {
                ABBREVIATION_BASE_ID.addConstraint(new DbConstraint(ABBREVIATION_BASE_ID, "AbbreviationBaseIdPK", Constraint.Type.NOT_NULL));
                ABBREVIATION_NAME.addConstraint(new DbConstraint(ABBREVIATION_NAME, "AbbreviationNotNull", Constraint.Type.NOT_NULL));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(ABBREVIATIONS, true).validate().toString());
            }
            //User Table
            if (checkIfTableExists(USER.getName())) {
                USER_ALLYCODE.addConstraint(new DbConstraint(USER_ALLYCODE, "UserAllycodePK", Constraint.Type.PRIMARY_KEY));
                USER_DISCORD_ID.addConstraint(new DbConstraint(USER_DISCORD_ID, "UserDiscordIdNotNull", Constraint.Type.NOT_NULL));
                USER_USERNAME.addConstraint(new DbConstraint(USER_USERNAME, "UserUsernameNotNull", Constraint.Type.NOT_NULL));
                USER_PERMISSION_LEVEL.addConstraint(new DbConstraint(USER_PERMISSION_LEVEL, "UserPermissionLevel", Constraint.Type.NOT_NULL));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(USER, true).validate().toString());
            }
            //GL Requirements
            if (checkIfTableExists(GL_REQUIREMENTS.getName())) {
                GL_REQ_BASE_ID.addConstraint(new DbConstraint(GL_REQ_BASE_ID, "GlReqBaseIdPK", Constraint.Type.NOT_NULL));
                GL_REQ_EVENT.addConstraint(new DbConstraint(GL_REQ_EVENT, "GlReqEventNotNull", Constraint.Type.NOT_NULL));
                GL_REQ_RELIC_LEVEL.addConstraint(new DbConstraint(GL_REQ_RELIC_LEVEL, "GlReqRelicLevelNotNull", Constraint.Type.NOT_NULL));
                GL_REQ_GEAR_LEVEL.addConstraint(new DbConstraint(GL_REQ_GEAR_LEVEL, "GlReqGearLevelNotNull", Constraint.Type.NOT_NULL));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(GL_REQUIREMENTS, true).validate().toString());
            }
            //Presence
            if (checkIfTableExists(PRESENCE.getName())) {
                PRESENCE_DISCORD_ID.addConstraint(new DbConstraint(PRESENCE_DISCORD_ID, "PresenceDiscordIdPK", Constraint.Type.PRIMARY_KEY));
                PRESENCE_NAME.addConstraint(new DbConstraint(PRESENCE_NAME, "PresenceNameNotNull", Constraint.Type.NOT_NULL));
                PRESENCE_DATE.addConstraint(new DbConstraint(PRESENCE_DATE, "PresenceDateNotNull", Constraint.Type.NOT_NULL));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(PRESENCE, true).validate().toString());
            }
            //Guild
            if (checkIfTableExists(CONFIG.getName())) {
                CONFIG.addConstraint(new DbConstraint(CONFIG, "ConfigPK", Constraint.Type.PRIMARY_KEY, CONFIG_DISCORD_ID));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(CONFIG, true).validate().toString());
            }
            //PlayerUnit
            if (checkIfTableExists(PLAYER_UNIT.getName())) {
                PLAYER_UNIT.addConstraint(new DbConstraint(PLAYER_UNIT, "PlayerUnitPK", Constraint.Type.PRIMARY_KEY, PLAYER_UNIT_ALLYCODE, PLAYER_UNIT_BASE_ID));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(PLAYER_UNIT, true).validate().toString());
            }
            //Player
            if (checkIfTableExists(PLAYER.getName())) {
                PLAYER.addConstraint(new DbConstraint(PLAYER, "PlayerPK", Constraint.Type.PRIMARY_KEY, PLAYER_ALLYCODE));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(PLAYER, true).validate().toString());
            }
            //Guild
            if (checkIfTableExists(GUILD.getName())) {
                GUILD.addConstraint(new DbConstraint(GUILD, "GuildPK", Constraint.Type.PRIMARY_KEY, GUILD_ID));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(GUILD, true).validate().toString());
            }
            //Ability
            if(checkIfTableExists(ABILITY.getName())){
                ABILITY.addConstraint(new DbConstraint(ABILITY, "AbilityPK", Constraint.Type.PRIMARY_KEY, ABILITY_ID));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(ABILITY, true).validate().toString());
            }
            //UnitAbility
            if(checkIfTableExists(UNIT_ABILITY.getName())){
                UNIT_ABILITY.addConstraint(new DbConstraint(UNIT_ABILITY, "UnitAbilityPK", Constraint.Type.PRIMARY_KEY, UNIT_ABILITY_ABILITY_ID, UNIT_ABILITY_ALLYCODE));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(UNIT_ABILITY, true).validate().toString());
            }
            if(checkIfTableExists(COMMAND_USAGE.getName())){
                COMMAND_USAGE.addConstraint(new DbConstraint(COMMAND_USAGE, "CommandUsagePK", Constraint.Type.PRIMARY_KEY, COMMAND_USAGE_NAME, COMMAND_USAGE_FLAG));
                this.connection.createStatement().executeUpdate(new CreateTableQuery(COMMAND_USAGE, true).validate().toString());
            }
        } catch (final SQLException exception) {
            logger.error(className, "createDatabase", exception.getMessage());
        }
    }

    private boolean checkIfTableExists(final String tableName) {
        final SelectQuery checkQuery = new SelectQuery().addColumns(SQLITE_MASTER_NAME)
                .addCondition(BinaryCondition.equalTo(SQLITE_MASTER_TYPE, "table"))
                .addCondition(BinaryCondition.equalTo(SQLITE_MASTER_NAME, tableName));
        try {
            if (connection.createStatement().executeQuery(checkQuery.validate().toString()).next()) {
                logger.info(className, "Skipping: " + tableName + " already exists");
                return false;
            }
            return true;
        } catch (final SQLException exception) {
            logger.error(className, "CheckIfTableExists", exception.getMessage());
            return false;
        }
    }

    /**
     * Deletes the database when called.
     */
    public void deleteDatabase() {
        try {
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, COMMAND.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, PRESENCE.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, UNIT_INFO.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, ABBREVIATIONS.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, USER.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, GL_REQUIREMENTS.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, CONFIG.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, PLAYER_UNIT.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, PLAYER.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, GUILD.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, PLAYER_UNIT.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, ABILITY.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, UNIT_ABILITY.getName()).validate().toString());
            this.connection.createStatement().executeUpdate(new DropQuery(DropQuery.Type.TABLE, COMMAND_USAGE.getName()).validate().toString());
        } catch (final SQLException exception) {
            logger.error(className, "deleteDatabase", exception.getMessage());
        }
    }
    //CHECKSTYLE.ON: MultipleStringLiteralsCheck

    /**
     * Closes the DB connection.
     */
    public void closeDb() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (final SQLException exception) {
            logger.error(className, "closeDb", exception.getMessage());
        }
    }
}
