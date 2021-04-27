package nl.djj.swgoh_bot_v2.database;

import com.healthmarketscience.sqlbuilder.*;
import nl.djj.swgoh_bot_v2.config.GalacticLegends;
import nl.djj.swgoh_bot_v2.entities.compare.GlCompare;
import nl.djj.swgoh_bot_v2.entities.db.*;
import nl.djj.swgoh_bot_v2.exceptions.SQLDeletionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.StringHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DJJ
 */
//CHECKSTYLE.OFF: MultipleStringLiteralsCheck
public abstract class DatabaseHandler extends TableNames {
    private final transient String className = this.getClass().getName();
    private final transient Statement statement;
    private final transient Logger logger;
    private final transient Connection connection;

    /**
     * @param logger     the logger.
     * @param statement  db statement.
     * @param connection the connection to start and stop transactions.
     */
    public DatabaseHandler(final Logger logger, final Statement statement, final Connection connection) {
        super();
        this.logger = logger;
        this.statement = statement;
        this.connection = connection;
    }

    /**
     * Needs to be overridden in order to enable it.
     */
    public abstract void createDatabase();

    /**
     * @param commandName the command name.
     * @return the enabled status.
     */
    public boolean getCommandEnabledStatus(final String commandName) {
        try {
            String query = new SelectQuery()
                    .addColumns(COMMAND_ENABLED)
                    .addCondition(BinaryCondition.equalTo(COMMAND_NAME, commandName))
                    .validate().toString();
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                final boolean enabled = result.getBoolean(COMMAND_ENABLED.getName());
                result.close();
                return enabled;
            }
            query = new InsertQuery(COMMAND)
                    .addColumn(COMMAND_NAME, commandName)
                    .addColumn(COMMAND_ENABLED, false)
                    .validate().toString();
            statement.executeUpdate(query);
            return false;
        } catch (final SQLException exception) {
            logger.error(className, "getCommandEnabledStatus", exception.getMessage());
            return false;
        }
    }

    /**
     * Update the abbreviation table.
     *
     * @param abbreviations the list of abbreviations.
     */
    public void updateAbbreviations(final List<Abbreviation> abbreviations) throws SQLInsertionError {
        String query;
        try {
            query = new DeleteQuery(ABBREVIATIONS)
                    .addCondition(BinaryCondition.equalTo(1, 1))
                    .validate().toString();
            statement.executeUpdate(query);
            for (final Abbreviation abbreviation : abbreviations) {
                query = new InsertQuery(ABBREVIATIONS)
                        .addColumn(ABBREVIATION_BASE_ID, abbreviation.getBaseId())
                        .addColumn(ABBREVIATION_NAME, abbreviation.getAbbreviation())
                        .validate().toString();
                statement.executeUpdate(query);
            }
        } catch (final SQLException exception) {
            logger.error(className, "updateAbbreviations", exception.getMessage());
            throw new SQLInsertionError(className, "updateAbbreviations", exception.getMessage(), logger);
        }
    }

    /**
     * Updates the abilities.
     *
     * @param abilities the list.
     * @throws SQLInsertionError when something goes wrong.
     */
    public void updateAbilities(final List<Ability> abilities) throws SQLInsertionError {
        String query;
        try {
            query = new DeleteQuery(ABILITY)
                    .addCondition(BinaryCondition.equalTo(1, 1))
                    .validate().toString();
            statement.executeUpdate(query);
            for (final Ability ability : abilities) {
                query = new InsertQuery(ABILITY)
                        .addColumn(ABILITY_ID, ability.getBaseId())
                        .addColumn(ABILITY_NAME, ability.getName().replace("'", "''"))
                        .addColumn(ABILITY_TIER_MAX, ability.getTierMax())
                        .addColumn(ABILITY_IS_ZETA, ability.isZeta())
                        .addColumn(ABILITY_IS_OMEGA, ability.isOmega())
                        .addColumn(ABILITY_UNIT_ID, ability.getUnitBaseId())
                        .validate().toString();
                query = makeReplace(query);
                statement.executeUpdate(query);
            }
        } catch (final SQLException exception) {
            throw new SQLInsertionError(className, "updateAbilities", exception.getMessage(), logger);
        }
    }

    /**
     * Updates the GL Requirements.
     *
     * @param requirements the requirements.
     * @throws SQLInsertionError when somethings goes wrong.
     */
    public void updateGlRequirements(final List<GlRequirement> requirements) throws SQLInsertionError {
        String query;
        try {
            query = new DeleteQuery(GL_REQUIREMENTS)
                    .addCondition(BinaryCondition.equalTo(1, 1))
                    .validate().toString();
            statement.executeUpdate(query);
            for (final GlRequirement requirement : requirements) {
                query = new InsertQuery(GL_REQUIREMENTS)
                        .addColumn(GL_REQ_BASE_ID, requirement.getBaseId())
                        .addColumn(GL_REQ_EVENT, requirement.getGlEvent().getKey())
                        .addColumn(GL_REQ_GEAR_LEVEL, requirement.getGearLevel())
                        .addColumn(GL_REQ_RELIC_LEVEL, requirement.getRelicLevel())
                        .validate().toString();
                statement.executeUpdate(query);
            }
        } catch (final SQLException exception) {
            throw new SQLInsertionError(className, "updateGlRequirements", exception.getMessage(), logger);
        }
    }

    /**
     * Update the unit table.
     *
     * @param units the list of units.
     */
    public void updateUnits(final List<UnitInfo> units) throws SQLInsertionError {
        String query;
        try {
            query = new DeleteQuery(UNIT_INFO)
                    .addCondition(BinaryCondition.equalTo(1, 1))
                    .validate().toString();
            statement.executeUpdate(query);
            for (final UnitInfo unitInfo : units) {
                query = new InsertQuery(UNIT_INFO)
                        .addColumn(UNIT_BASE_ID, unitInfo.getBaseId())
                        .addColumn(UNIT_NAME, unitInfo.getName())
                        .addColumn(UNIT_ALIGNMENT, unitInfo.getAlignment().getAlignment())
                        .addColumn(UNIT_IS_CHARACTER, unitInfo.isCharacter())
                        .validate().toString();
                statement.executeUpdate(query);
            }
        } catch (final SQLException exception) {
            throw new SQLInsertionError(className, "updateUnits", exception.getMessage(), logger);
        }
    }

    /**
     * Retrieves a user by discordId.
     *
     * @param discordId the discordId.
     * @return a user.
     */
    public User getByDiscordId(final String discordId) throws SQLRetrieveError {
        logger.debug(className, "Retrieving a user by discordId");
        try {
            final String query = new SelectQuery()
                    .addColumns(USER_ALLYCODE, USER_USERNAME, USER_PERMISSION_LEVEL)
                    .addCondition(BinaryCondition.equalTo(USER_DISCORD_ID, discordId))
                    .validate().toString();
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                final int allycode = result.getInt(USER_ALLYCODE.getName());
                final String username = result.getString(USER_USERNAME.getName());
                final int permissionLevel = result.getInt(USER_PERMISSION_LEVEL.getName());
                return new User(allycode, permissionLevel, username, discordId, StringHelper.getCurrentDateTime());
            }
            throw new SQLRetrieveError(className, "getByDiscordId", "No user found", logger);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getByDiscordId", exception.getMessage(), logger);
        }
    }

    /**
     * Check whether the user is already registered.
     *
     * @param discordId the ID to search for.
     * @return boolean.
     */
    public boolean isUserRegistered(final String discordId) {
        logger.debug(className, "Checking if a user is registered");
        try {
            final String query = new SelectQuery()
                    .addColumns(USER_ALLYCODE)
                    .addCondition(BinaryCondition.equalTo(USER_DISCORD_ID, discordId))
                    .validate().toString();
            final ResultSet result = statement.executeQuery(query);
            return result.next();
        } catch (final SQLException exception) {
            logger.error(className, "isUserRegistered", exception.getMessage());
            return false;
        }
    }

    /**
     * Inserts an user.
     *
     * @param user the user.
     * @throws SQLInsertionError when something goes wrong.
     */
    public void insertUser(final User user) throws SQLInsertionError {
        logger.debug(className, "Inserting a user");
        try {
            final String query = new InsertQuery(USER)
                    .addColumn(USER_ALLYCODE, user.getAllycode())
                    .addColumn(USER_DISCORD_ID, user.getDiscordId())
                    .addColumn(USER_USERNAME, user.getUsername())
                    .addColumn(USER_PERMISSION_LEVEL, user.getPermission().getLevel())
                    .validate().toString();
            if (this.statement.executeUpdate(query) != IS_UPDATED) {
                throw new SQLInsertionError(className, "insertUser", "Something went wrong in the DB, try again later", logger);
            }
        } catch (final SQLException exception) {
            throw new SQLInsertionError(className, "insertUser", exception.getMessage(), logger);
        }
    }

    /**
     * Removes a user.
     *
     * @param discordId the ID to remove.
     * @throws SQLDeletionError when something goes wrong.
     */
    public void removeUser(final String discordId) throws SQLDeletionError {
        logger.debug(className, "Removing a user");
        try {
            final String query = new DeleteQuery(USER)
                    .addCondition(BinaryCondition.equalTo(USER_DISCORD_ID, discordId))
                    .validate().toString();
            if (this.statement.executeUpdate(query) != IS_UPDATED) {
                throw new SQLDeletionError(className, "removeUser", "Something went wrong in the DB, try again later", logger);
            }
        } catch (final SQLException exception) {
            throw new SQLDeletionError(className, "removeUser", exception.getMessage(), logger);
        }
    }

    /**
     * Enable a command.
     *
     * @param commandName the command to enable.
     */
    public void enableCommand(final String commandName) throws SQLInsertionError {
        toggleCommand(commandName, true);
    }

    /**
     * Disable a command.
     *
     * @param commandName the command to disable.
     */
    public void disableCommand(final String commandName) throws SQLInsertionError {
        toggleCommand(commandName, false);
    }

    private void toggleCommand(final String commandName, final boolean status) throws SQLInsertionError {
        logger.debug(className, "Toggling a command");
        final String query = new UpdateQuery(COMMAND)
                .addSetClause(COMMAND_ENABLED, status)
                .addCondition(BinaryCondition.equalTo(COMMAND_NAME, commandName))
                .validate().toString();
        try {
            if (statement.executeUpdate(query) != IS_UPDATED) {
                throw new SQLInsertionError(className, "toggleCommand", "Invalid command name passed: " + commandName, logger);
            }
        } catch (final SQLException exception) {
            throw new SQLInsertionError(className, "toggleCommand", exception.getMessage(), logger);
        }
    }

    /**
     * Updates the config for the guild.
     *
     * @param config the updated Config.
     * @throws SQLInsertionError when something goes wrong.
     */
    public void updateConfig(final Config config) throws SQLInsertionError {
        logger.debug(className, "Updating Config");
        String query = new InsertQuery(CONFIG)
                .addColumn(CONFIG_DISCORD_ID, config.getDiscordId())
                .addColumn(CONFIG_SWGOH_ID, config.getSwgohId())
                .addColumn(CONFIG_PREFIX, config.getPrefix())
                .addColumn(CONFIG_MOD_ROLE, config.getModerationRole())
                .addColumn(CONFIG_IGNORE_ROLE, config.getIgnoreRole())
                .addColumn(CONFIG_NOTIFY_CHANNEL, config.getNotifyChannel())
                .addColumn(CONFIG_BOT_LOG_CHANNEL, config.getBotLoggingChannel())
                .validate().toString();
        query = makeReplace(query);
        try {
            if (this.statement.executeUpdate(query) != IS_UPDATED) {
                throw new SQLInsertionError(className, "updateConfig", "Something went wrong in the DB, try again later", logger);
            }
        } catch (final SQLException exception) {
            throw new SQLInsertionError(className, "updateConfig", exception.getMessage(), logger);
        }
    }

    /**
     * Gets the unit name for the given id.
     *
     * @param identifier the id.
     * @return the unit name.
     * @throws SQLRetrieveError when something goes wrong.
     */
    public String getUnitNameForId(final String identifier) throws SQLRetrieveError {
        logger.debug(className, "Retrieving Unit name for ID");
        final String query = new SelectQuery()
                .addColumns(UNIT_NAME)
                .addCondition(BinaryCondition.equalTo(UNIT_BASE_ID, identifier))
                .validate().toString();
        try {
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                return result.getString(UNIT_NAME.getName());
            }
            throw new SQLRetrieveError(className, "getUnitNameForId", "No such unit exists", logger);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getUnitNameForId", exception.getMessage(), logger);
        }
    }

    /**
     * Retrieves the config for the guildId.
     *
     * @param guildId the guildId.
     * @return the config.
     * @throws SQLRetrieveError when something goes wrong.
     */
    public Config getConfig(final String guildId) throws SQLRetrieveError {
        logger.debug(className, "Retrieving Guild Config");
        final String query = new SelectQuery()
                .addColumns(CONFIG_SWGOH_ID,
                        CONFIG_PREFIX,
                        CONFIG_BOT_LOG_CHANNEL,
                        CONFIG_DISCORD_ID,
                        CONFIG_IGNORE_ROLE,
                        CONFIG_MOD_ROLE,
                        CONFIG_NOTIFY_CHANNEL)
                .addCondition(BinaryCondition.equalTo(CONFIG_DISCORD_ID, guildId))
                .validate().toString();
        try {
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                final Config config = new Config(guildId);
                config.setSwgohId(result.getString(CONFIG_SWGOH_ID.getName()));
                config.setPrefix(result.getString(CONFIG_PREFIX.getName()));
                config.setBotLoggingChannel(result.getString(CONFIG_BOT_LOG_CHANNEL.getName()));
                config.setIgnoreRole(result.getString(CONFIG_IGNORE_ROLE.getName()));
                config.setModerationRole(result.getString(CONFIG_MOD_ROLE.getName()));
                config.setNotifyChannel(result.getString(CONFIG_NOTIFY_CHANNEL.getName()));
                return config;
            }
            throw new SQLRetrieveError(className, "getConfig", "No config for the guild set", logger);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getConfig", exception.getMessage(), logger);
        }
    }

    /**
     * Get's the prefix for the guild.
     *
     * @param guildId the guildId.
     * @return a prefix when found.
     * @throws SQLRetrieveError when something goes wrong.
     */
    public String getPrefixForGuild(final String guildId) throws SQLRetrieveError {
        logger.debug(className, "Retrieving Guild Prefix");
        final String query = new SelectQuery()
                .addColumns(CONFIG_PREFIX)
                .addCondition(BinaryCondition.equalTo(CONFIG_DISCORD_ID, guildId))
                .validate().toString();
        try {
            final ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                final String prefix = resultSet.getString(CONFIG_PREFIX.getName());
                if (prefix != null) {
                    return prefix;
                }
            }
            throw new SQLRetrieveError(className, "getPrefixForGuild", "No prefix set this guild", logger);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getPrefixForGuild", exception.getMessage(), logger);
        }
    }

    /**
     * Gets the ignore role for the guild.
     *
     * @param guildId the id of the guild.
     * @return the ignore role.
     * @throws SQLRetrieveError when something goes wrong.
     */
    public String getIgnoreRoleForGuild(final String guildId) throws SQLRetrieveError {
        logger.debug(className, "Retrieving Guild Ingore role");
        final String query = new SelectQuery()
                .addColumns(CONFIG_IGNORE_ROLE)
                .addCondition(BinaryCondition.equalTo(CONFIG_DISCORD_ID, guildId))
                .validate().toString();
        try {
            final ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                final String prefix = resultSet.getString(CONFIG_IGNORE_ROLE.getName());
                if (prefix != null) {
                    return prefix;
                }
            }
            throw new SQLRetrieveError(className, "getIgnoreRoleForGuild", "No Ignore role set this guild", logger);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getIgnoreRoleForGuild", exception.getMessage(), logger);
        }
    }

    /**
     * Gets the SWGOH id of the guild.
     *
     * @param guildId the Discord Guild ID.
     * @return the SWGOH id.
     * @throws SQLRetrieveError when nothing exists.
     */
    public int getSwgohIdByGuildId(final String guildId) throws SQLRetrieveError {
        logger.debug(className, "Retrieving Guild SWGOH ID");
        final String query = new SelectQuery()
                .addColumns(CONFIG_SWGOH_ID)
                .addCondition(BinaryCondition.equalTo(CONFIG_DISCORD_ID, guildId))
                .validate().toString();
        try {
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                return result.getInt(CONFIG_SWGOH_ID.getName());
            }
            throw new SQLRetrieveError(className, "getSwgohIdByGuildId", "Your guild is not registered, contact your mods", logger);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getSwgohIdByGuildId", exception.getMessage(), logger);
        }
    }

    /**
     * Updates the presence of a user.
     *
     * @param userId   the userId.
     * @param username the username.
     * @param now      the date.
     */
    public void updatePresence(final String userId, final String username, final String now) {
        logger.debug(className, "Updating a user presence. | username: " + username);
        String query = new InsertQuery(PRESENCE)
                .addColumn(PRESENCE_DISCORD_ID, userId)
                .addColumn(PRESENCE_NAME, username)
                .addColumn(PRESENCE_DATE, now)
                .validate().toString();
        query = makeReplace(query);
        try {
            statement.executeUpdate(query);
        } catch (final SQLException exception) {
            logger.error(className, "updatePresence", exception.getMessage());
        }
    }

    /**
     * Gets all the notification channels for the guild.
     *
     * @return a list with channels.
     */
    public List<String> getAllGuildNotifyChannels() throws SQLRetrieveError {
        logger.debug(className, "Retrieving Guild Notification Channels");
        final List<String> returnValue = new ArrayList<>();
        final String query = new SelectQuery()
                .addColumns(CONFIG_NOTIFY_CHANNEL)
                .validate().toString();
        try {
            final ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                final String channel = result.getString(CONFIG_NOTIFY_CHANNEL.getName());
                if (channel != null) {
                    returnValue.add(channel);
                }
            }
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getAllGuildNotifyChannels", exception.getMessage(), logger);
        }
        return returnValue;
    }

    /**
     * Get the GP overview for an guild.
     *
     * @param guildId the guild ID.
     * @return a list with users and their GP.
     * @throws SQLRetrieveError when something goes wrong.
     */
    public Map<String, Integer> getGuildGPOverview(final int guildId) throws SQLRetrieveError {
        logger.debug(className, "Getting Guild GP Overview");
        final String query = new SelectQuery()
                .addColumns(PLAYER_NAME, PLAYER_GP)
                .addCondition(BinaryCondition.equalTo(PLAYER_GUILD_ID, guildId))
                .addOrdering(PLAYER_GP, OrderObject.Dir.DESCENDING)
                .validate().toString();
        try {
            final Map<String, Integer> returnValue = new LinkedHashMap<>();
            final ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                returnValue.put(result.getString(PLAYER_NAME.getName()), result.getInt(PLAYER_GP.getName()));
            }
            return returnValue;
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getGuildGpOverview", exception.getMessage(), logger);
        }
    }

    /**
     * Gets the relic overview for the guild.
     *
     * @param guildId    the guild ID.
     * @param relicLevel the minimum relic level to filter.
     * @return a list with users and their relic count.
     * @throws SQLRetrieveError when something goes wrong.
     */
    public Map<String, Integer> getGuildRelicOverview(final int guildId, final int relicLevel) throws SQLRetrieveError {
        logger.debug(className, "Getting Guild Relic overview");
        final String query = new SelectQuery()
                .addColumns(PLAYER_NAME)
                .addAliasedColumn(FunctionCall.count().addColumnParams(PLAYER_UNIT_ALLYCODE), "relicCount")
                .addJoins(SelectQuery.JoinType.INNER, PLAYER_UNIT_JOIN)
                .addCondition(BinaryCondition.equalTo(PLAYER_UNIT_GUILD_ID, guildId))
                .addCondition(BinaryCondition.greaterThanOrEq(PLAYER_UNIT_RELIC, relicLevel))
                .addGroupings(PLAYER_NAME)
                .addCustomOrdering("relicCount", OrderObject.Dir.DESCENDING)
                .validate().toString();
        try {
            final Map<String, Integer> returnValue = new LinkedHashMap<>();
            final ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                returnValue.put(result.getString(PLAYER_NAME.getName()), result.getInt("relicCount"));
            }
            return returnValue;
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getGuildGpOverview", exception.getMessage(), logger);
        }
    }

    /**
     * Inserts a guild object.
     *
     * @param guild the guild.
     */
    public void insertGuild(final Guild guild) throws SQLInsertionError {
        logger.debug(className, "Inserting Guild");
        String query = new InsertQuery(GUILD)
                .addColumn(GUILD_ID, guild.getIdentifier())
                .addColumn(GUILD_NAME, guild.getName())
                .addColumn(GUILD_GP, guild.getGalacticPower())
                .addColumn(GUILD_MEMBERS, guild.getMembers())
                .addColumn(GUILD_LAST_UPDATED, StringHelper.localDateTimeToString(guild.getLastUpdated()))
                .validate().toString();
        query = makeReplace(query);
        try {
            statement.executeUpdate(query);
        } catch (final SQLException exception) {
            throw new SQLInsertionError(className, "insertGuild", exception.getMessage(), logger);
        }
    }

    /**
     * Retrieves the guild for the given ID.
     *
     * @param guildId the guild id.
     * @return a guild object.
     * @throws SQLRetrieveError when something goes wrong.
     */
    public Guild getGuild(final int guildId) throws SQLRetrieveError {
        logger.debug(className, "Retrieving Guild");
        final String query = new SelectQuery()
                .addColumns(GUILD_NAME, GUILD_GP, GUILD_MEMBERS, GUILD_LAST_UPDATED)
                .addCondition(BinaryCondition.equalTo(GUILD_ID, guildId))
                .validate().toString();
        try {
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                final String name = result.getString(GUILD_NAME.getName());
                final int galacticPower = result.getInt(GUILD_GP.getName());
                final int members = result.getInt(GUILD_MEMBERS.getName());
                final LocalDateTime lastUpdated = StringHelper.stringToLocalDateTime(result.getString(GUILD_LAST_UPDATED.getName()));
                return new Guild(name, guildId, galacticPower, members, lastUpdated);
            }
            throw new SQLRetrieveError(className, "getGuild", "Something went wrong retrieving the guild information", logger);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getGuild", exception.getMessage(), logger);
        }
    }

    /**
     * Retrieves the relic level count for a guild.
     *
     * @param guildId    the guild ID.
     * @param relicLevel the relic level to search for.
     * @param equalTo    whether it should be equal to the level or greater or equal to.
     * @param unitId     the unit id.
     * @return a count.
     * @throws SQLRetrieveError when something goes wrong.
     */
    public int getRelicLevelCountForGuild(final int guildId, final int relicLevel, final boolean equalTo,
                                          final String unitId) throws SQLRetrieveError {
        logger.debug(className, "Retrieving Guild Relic Count");
        final SelectQuery selectQuery = new SelectQuery()
                .addCustomColumns(FunctionCall.count().addColumnParams(PLAYER_UNIT_RELIC))
                .addCondition(BinaryCondition.equalTo(PLAYER_UNIT_GUILD_ID, guildId));

        if (equalTo) {
            selectQuery.addCondition(BinaryCondition.equalTo(PLAYER_UNIT_RELIC, relicLevel));
        } else {
            selectQuery.addCondition(BinaryCondition.greaterThan(PLAYER_UNIT_RELIC, relicLevel));
        }
        if (unitId != null) {
            selectQuery.addCondition(BinaryCondition.equalTo(PLAYER_UNIT_BASE_ID, unitId));
        }
        final String query = selectQuery.validate().toString();
        try {
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                return result.getInt(1);
            }
            throw new SQLRetrieveError(className, "getRelicLevelCountForGuild", "Something went wrong retrieving the guild information", logger);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getRelicLevelCountForGuild", exception.getMessage(), logger);
        }
    }

    /**
     * Retrieves the zeta count for a guild.
     *
     * @param guildId the guild ID.
     * @param unitId  the unit ID.
     * @return a count.
     * @throws SQLRetrieveError when something goes wrong.
     */
    public int getZetaCountForGuild(final int guildId, final String unitId) throws SQLRetrieveError {
        logger.debug(className, "Retrieving Guild Zeta Count");
        final SelectQuery selectQuery = new SelectQuery()
                .addCustomColumns(FunctionCall.count().addColumnParams())
                .addCondition(BinaryCondition.equalTo(UNIT_ABILITY_GUILD_ID, guildId))
                .addJoins(SelectQuery.JoinType.INNER, ZETA_JOIN)
                .addCondition(BinaryCondition.equalTo(ABILITY_IS_ZETA, true))
                .addCondition(BinaryCondition.equalTo(ABILITY_TIER_MAX, UNIT_ABILITY_LEVEL));
        if (unitId != null) {
            selectQuery.addCondition(BinaryCondition.equalTo(ABILITY_UNIT_ID, unitId));
        }
        final String query = selectQuery.validate().toString();
        try {
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                return result.getInt(1);
            }
            throw new SQLRetrieveError(className, "getZetaCountForGuild", "Something went wrong retrieving the guild information", logger);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getZetaCountForGuild", exception.getMessage(), logger);
        }
    }

    /**
     * Gets the rarity count for the guild.
     *
     * @param guildId   the guild ID.
     * @param starLevel the star level.
     * @param unitId    the unit.
     * @return a count.
     * @throws SQLRetrieveError when something goes wrong.
     */
    public int getStarLevelCountForGuild(final int guildId, final int starLevel, final String unitId) throws
            SQLRetrieveError {
        logger.debug(className, "Retrieving Guild Rarity Count");
        final SelectQuery selectQuery = new SelectQuery()
                .addCustomColumns(FunctionCall.count().addColumnParams(PLAYER_UNIT_RARITY))
                .addCondition(BinaryCondition.equalTo(PLAYER_UNIT_GUILD_ID, guildId))
                .addCondition(BinaryCondition.equalTo(PLAYER_UNIT_RARITY, starLevel));
        if (unitId != null) {
            selectQuery.addCondition(BinaryCondition.equalTo(PLAYER_UNIT_BASE_ID, unitId));
        }
        final String query = selectQuery.validate().toString();
        try {
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                return result.getInt(1);
            }
            throw new SQLRetrieveError(className, "getStarLevelCountForGuild", "Something went wrong retrieving the guild information", logger);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getStarLevelCountForGuild", exception.getMessage(), logger);
        }
    }

    /**
     * Gets the gear level count for the guild.
     *
     * @param guildId   the guild.
     * @param gearLevel the gear level.
     * @param unitId    the unit.
     * @return a count.
     * @throws SQLRetrieveError when something goes wrong .
     */
    public int getGearLevelCountForGuild(final int guildId, final int gearLevel, final String unitId) throws
            SQLRetrieveError {
        logger.debug(className, "Retrieving Guild Gear Count");
        final SelectQuery selectQuery = new SelectQuery()
                .addCustomColumns(FunctionCall.count().addColumnParams(PLAYER_UNIT_RELIC))
                .addCondition(BinaryCondition.equalTo(PLAYER_UNIT_GUILD_ID, guildId))
                .addCondition(BinaryCondition.equalTo(PLAYER_UNIT_GEAR, gearLevel));
        if (unitId != null) {
            selectQuery.addCondition(BinaryCondition.equalTo(PLAYER_UNIT_BASE_ID, unitId));
        }
        final String query = selectQuery.validate().toString();
        try {
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                return result.getInt(1);
            }
            throw new SQLRetrieveError(className, "getGearLeveLCountForGuild", "Something went wrong retrieving the guild information", logger);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getGearLevelCountForGuild", exception.getMessage(), logger);
        }
    }

    /**
     * Inserts a player.
     *
     * @param player the player.
     * @throws SQLInsertionError when something goes wrong.
     */
    public void insertPlayer(final Player player) throws SQLInsertionError {
        logger.debug(className, "Inserting player");
        String query = new InsertQuery(PLAYER)
                .addColumn(PLAYER_ALLYCODE, player.getAllycode())
                .addColumn(PLAYER_NAME, player.getName())
                .addColumn(PLAYER_GP, player.getGalacticPower())
                .addColumn(PLAYER_URL, player.getUrl())
                .addColumn(PLAYER_LAST_UPDATED, player.getLastUpdated())
                .addColumn(PLAYER_LAST_UPDATED_SWGOH, player.getLastUpdatedSwgoh())
                .addColumn(PLAYER_GUILD_ID, player.getGuildId())
                .validate().toString();
        query = makeReplace(query);
        try {
            statement.executeUpdate(query);
        } catch (final SQLException exception) {
            throw new SQLInsertionError(className, "insertPlayer", exception.getMessage(), logger);
        }
    }

    /**
     * Inserts a playerUnit list.
     *
     * @param playerUnits the playerUnit list.
     * @throws SQLInsertionError when something goes wrong.
     */
    public void insertPlayerUnits(final List<PlayerUnit> playerUnits) throws SQLInsertionError {
        logger.debug(className, "Inserting list of player units");
        try {
            connection.setAutoCommit(false);
            final Statement statement = connection.createStatement();
            for (final PlayerUnit playerUnit : playerUnits) {
                logger.debug(className, "Inserting Player Unit");
                String query = new InsertQuery(PLAYER_UNIT)
                        .addColumn(PLAYER_UNIT_ALLYCODE, playerUnit.getAllycode())
                        .addColumn(PLAYER_UNIT_GUILD_ID, playerUnit.getGuildId())
                        .addColumn(PLAYER_UNIT_BASE_ID, playerUnit.getBaseId())
                        .addColumn(PLAYER_UNIT_RARITY, playerUnit.getRarity())
                        .addColumn(PLAYER_UNIT_GP, playerUnit.getGalacticPower())
                        .addColumn(PLAYER_UNIT_GEAR, playerUnit.getGear())
                        .addColumn(PLAYER_UNIT_RELIC, playerUnit.getRelic())
                        .addColumn(PLAYER_UNIT_SPEED, playerUnit.getSpeed())
                        .validate().toString();
                query = makeReplace(query);
                for (final UnitAbility ability : playerUnit.getAbilities()) {
                    this.insertUnitAbility(ability, statement);
                }
                statement.executeUpdate(query);
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (final SQLException exception) {
            throw new SQLInsertionError(className, "insertPlayerUnit", exception.getMessage(), logger);
        }
    }

    /**
     * Inserts the ability data of a player unit.
     *
     * @param unitAbility the playerUnit ability data.
     * @throws SQLInsertionError when something goes wrong.
     */
    public void insertUnitAbility(final UnitAbility unitAbility, final Statement statement) throws SQLInsertionError {
        logger.debug(className, "Inserting Unit Ability");
        String query = new InsertQuery(UNIT_ABILITY)
                .addColumn(UNIT_ABILITY_ABILITY_ID, unitAbility.getAbilityId())
                .addColumn(UNIT_ABILITY_ALLYCODE, unitAbility.getAllycode())
                .addColumn(UNIT_ABILITY_GUILD_ID, unitAbility.getGuildId())
                .addColumn(UNIT_ABILITY_LEVEL, unitAbility.getLevel())
                .validate().toString();
        query = makeReplace(query);
        try {
            statement.executeUpdate(query);
        } catch (final SQLException exception) {
            throw new SQLInsertionError(className, "insertUnitAbility", exception.getMessage(), logger);
        }
    }

    public List<GlRequirement> getGlRequirements(final GalacticLegends event) throws SQLRetrieveError {
        logger.debug(className, "Getting GL Requirements for: " + event.getName());
        final String query = new SelectQuery()
                .addColumns(GL_REQ_BASE_ID, GL_REQ_GEAR_LEVEL, GL_REQ_RELIC_LEVEL)
                .addCondition(BinaryCondition.equalTo(GL_REQ_EVENT, event.getKey()))
                .validate().toString();
        try {
            final ResultSet result = statement.executeQuery(query);
            final List<GlRequirement> requirements = new ArrayList<>();
            while (result.next()) {
                requirements.add(new GlRequirement(event, result.getString(GL_REQ_BASE_ID.getName()), result.getInt(GL_REQ_GEAR_LEVEL.getName()), result.getInt(GL_REQ_RELIC_LEVEL.getName())));
            }
            return requirements;
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getGLRequiremennts", exception.getMessage(), logger);
        }
    }

    public GlCompare GetGLCompareUnitForPlayer(final String baseId, final int allycode) throws SQLRetrieveError {
        logger.debug(className, "Getting playerUnit: " + baseId + " | for allycode: " + allycode);
        final String query = new SelectQuery()
                .addColumns(PLAYER_UNIT_GEAR, PLAYER_UNIT_RELIC, PLAYER_UNIT_RARITY, PLAYER_UNIT_GEAR_PIECES)
                .addCondition(BinaryCondition.equalTo(PLAYER_UNIT_BASE_ID, baseId))
                .addCondition(BinaryCondition.equalTo(PLAYER_UNIT_ALLYCODE, allycode))
                .validate().toString();
        try {
            final String unitName = getUnitNameForId(baseId);
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                final int gear = result.getInt(PLAYER_UNIT_GEAR.getName());
                final int gearPieces = result.getInt(PLAYER_UNIT_GEAR_PIECES.getName());
                final int relic = result.getInt(PLAYER_UNIT_RELIC.getName());
                final int rarity = result.getInt(PLAYER_UNIT_RARITY.getName());
                return new GlCompare(unitName, gear, gearPieces, relic, rarity, getZetaCountForPlayerUnit(allycode, baseId));
            }
            return new GlCompare(unitName, -1, -1, -1, -1, -1);
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getUnitForPlayer", exception.getMessage(), logger);
        }
    }

    public int getZetaCountForPlayerUnit(final int allycode, final String unitId) throws SQLRetrieveError {
        logger.debug(className, "Retrieving Guild Zeta Count");
        final SelectQuery selectQuery = new SelectQuery()
                .addCustomColumns(FunctionCall.count().addColumnParams())
                .addCondition(BinaryCondition.equalTo(UNIT_ABILITY_ALLYCODE, allycode))
                .addJoins(SelectQuery.JoinType.INNER, ZETA_JOIN)
                .addCondition(BinaryCondition.equalTo(ABILITY_IS_ZETA, true))
                .addCondition(BinaryCondition.equalTo(ABILITY_TIER_MAX, UNIT_ABILITY_LEVEL));
        if (unitId != null) {
            selectQuery.addCondition(BinaryCondition.equalTo(ABILITY_UNIT_ID, unitId));
        }
        final String query = selectQuery.validate().toString();
        try {
            final ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                return result.getInt(1);
            }
            return -1;
        } catch (final SQLException exception) {
            throw new SQLRetrieveError(className, "getZetaCountForGuild", exception.getMessage(), logger);
        }
    }
    //CHECKSTYLE.ON: MultipleStringLiteralsCheck
}
