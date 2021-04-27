package nl.djj.swgoh_bot_v2.database;

import com.healthmarketscience.sqlbuilder.dbspec.basic.*;

/**
 * @author DJJ
 */

public class TableNames {

    //CHECKSTYLE.OFF: VisibilityModifierCheck
    final transient DbSpec spec = new DbSpec();
    final transient DbSchema schema = spec.addDefaultSchema();
    //Abbreviation Table --> Abbreviations of all users.
    final transient DbTable ABBREVIATIONS = schema.addTable("abbreviations");
    final transient DbColumn ABBREVIATION_BASE_ID = ABBREVIATIONS.addColumn("baseId", "string", null);
    final transient DbColumn ABBREVIATION_NAME = ABBREVIATIONS.addColumn("abbreviation", "string", null);

    //Command Table --> Enabled status of all the commands in the bot.
    final transient DbTable COMMAND = schema.addTable("command");
    final transient DbColumn COMMAND_NAME = COMMAND.addColumn("commandName", "string", null);
    final transient DbColumn COMMAND_ENABLED = COMMAND.addColumn("enabled", "integer", null);

    //UnitInfo Table --> unit information for all the units.
    final transient DbTable UNIT_INFO = schema.addTable("unitInfo");
    final transient DbColumn UNIT_BASE_ID = UNIT_INFO.addColumn("baseId", "string", null);
    final transient DbColumn UNIT_NAME = UNIT_INFO.addColumn("name", "string", null);
    final transient DbColumn UNIT_ALIGNMENT = UNIT_INFO.addColumn("alignment", "string", null);
    final transient DbColumn UNIT_IS_CHARACTER = UNIT_INFO.addColumn("isCharacter", "integer", null);

    //User Table --> Bot User information.
    final transient DbTable USER = schema.addTable("user");
    final transient DbColumn USER_ALLYCODE = USER.addColumn("allycode", "string", null);
    final transient DbColumn USER_DISCORD_ID = USER.addColumn("discordId", "string", null);
    final transient DbColumn USER_USERNAME = USER.addColumn("username", "string", null);
    final transient DbColumn USER_PERMISSION_LEVEL = USER.addColumn("permissionLevel", "string", null);

    //GLRequirements Table --> GL Requirements.
    final transient DbTable GL_REQUIREMENTS = schema.addTable("glRequirements");
    final transient DbColumn GL_REQ_BASE_ID = GL_REQUIREMENTS.addColumn("baseId", "string", null);
    final transient DbColumn GL_REQ_EVENT = GL_REQUIREMENTS.addColumn("glEvent", "string", null);
    final transient DbColumn GL_REQ_RELIC_LEVEL = GL_REQUIREMENTS.addColumn("relicLevel", "integer", null);
    final transient DbColumn GL_REQ_GEAR_LEVEL = GL_REQUIREMENTS.addColumn("gearLevel", "integer", null);

    //Presence Table --> The last time the bot saw an user online/
    final transient DbTable PRESENCE = schema.addTable("presence");
    final transient DbColumn PRESENCE_DISCORD_ID = PRESENCE.addColumn("discordId", "string", null);
    final transient DbColumn PRESENCE_NAME = PRESENCE.addColumn("name", "string", null);
    final transient DbColumn PRESENCE_DATE = PRESENCE.addColumn("date", "string", null);

    //Config --> The bot config for a guild.
    final transient DbTable CONFIG = schema.addTable("config");
    final transient DbColumn CONFIG_DISCORD_ID = CONFIG.addColumn("discordId", "integer", null);
    final transient DbColumn CONFIG_SWGOH_ID = CONFIG.addColumn("swgohId", "integer", null);
    final transient DbColumn CONFIG_PREFIX = CONFIG.addColumn("prefix", "string", null);
    final transient DbColumn CONFIG_MOD_ROLE = CONFIG.addColumn("modRole", "string", null);
    final transient DbColumn CONFIG_IGNORE_ROLE = CONFIG.addColumn("ignoreRole", "string", null);
    final transient DbColumn CONFIG_NOTIFY_CHANNEL = CONFIG.addColumn("botNotifyChannel", "string", null);
    final transient DbColumn CONFIG_BOT_LOG_CHANNEL = CONFIG.addColumn("botLoggingChannel", "string", null);

    //Player Unit --> All Units of a certain player.
    final transient DbTable PLAYER_UNIT = schema.addTable("playerUnit");
    final transient DbColumn PLAYER_UNIT_ALLYCODE = PLAYER_UNIT.addColumn("allycode", "string", null);
    final transient DbColumn PLAYER_UNIT_BASE_ID = PLAYER_UNIT.addColumn("baseId", "string", null);
    final transient DbColumn PLAYER_UNIT_GUILD_ID = PLAYER_UNIT.addColumn("guildId", "integer", null);
    final transient DbColumn PLAYER_UNIT_RARITY = PLAYER_UNIT.addColumn("rarity", "integer", null);
    final transient DbColumn PLAYER_UNIT_GP = PLAYER_UNIT.addColumn("gp", "integer", null);
    final transient DbColumn PLAYER_UNIT_GEAR = PLAYER_UNIT.addColumn("gear", "integer", null);
    final transient DbColumn PLAYER_UNIT_GEAR_PIECES = PLAYER_UNIT.addColumn("gearPieces", "integer", null);
    final transient DbColumn PLAYER_UNIT_RELIC = PLAYER_UNIT.addColumn("relic", "integer", null);
    final transient DbColumn PLAYER_UNIT_SPEED = PLAYER_UNIT.addColumn("speed", "integer", null);

    //Player --> Information for a certain player.
    final transient DbTable PLAYER = schema.addTable("player");
    final transient DbColumn PLAYER_ALLYCODE = PLAYER.addColumn("allycode", "integer", null);
    final transient DbColumn PLAYER_NAME = PLAYER.addColumn("name", "string", null);
    final transient DbColumn PLAYER_GP = PLAYER.addColumn("gp", "integer", null);
    final transient DbColumn PLAYER_URL = PLAYER.addColumn("url", "string", null);
    final transient DbColumn PLAYER_LAST_UPDATED = PLAYER.addColumn("lastUpdated", "string", null);
    final transient DbColumn PLAYER_LAST_UPDATED_SWGOH = PLAYER.addColumn("lastUpdatedSwgoh", "string", null);
    final transient DbColumn PLAYER_GUILD_ID = PLAYER.addColumn("guildId", "integer", null);

    //Guild --> Information for a certain guild.
    final transient DbTable GUILD = schema.addTable("guild");
    final transient DbColumn GUILD_ID = GUILD.addColumn("id", "integer", null);
    final transient DbColumn GUILD_NAME = GUILD.addColumn("name", "string", null);
    final transient DbColumn GUILD_GP = GUILD.addColumn("gp", "integer", null);
    final transient DbColumn GUILD_MEMBERS = GUILD.addColumn("members", "integer", null);
    final transient DbColumn GUILD_LAST_UPDATED = GUILD.addColumn("lastUpdated", "string", null);

    //Ability
    final transient DbTable ABILITY = schema.addTable("ability");
    final transient DbColumn ABILITY_ID = ABILITY.addColumn("abilityId", "string", null);
    final transient DbColumn ABILITY_NAME = ABILITY.addColumn("name", "string", null);
    final transient DbColumn ABILITY_TIER_MAX = ABILITY.addColumn("tierMax", "integer", null);
    final transient DbColumn ABILITY_IS_ZETA = ABILITY.addColumn("isZeta", "integer", null);
    final transient DbColumn ABILITY_IS_OMEGA = ABILITY.addColumn("isOmega", "integer", null);
    final transient DbColumn ABILITY_UNIT_ID = ABILITY.addColumn("unitId", "string", null);

    //UnitAbility
    final transient DbTable UNIT_ABILITY = schema.addTable("unitAbility");
    final transient DbColumn UNIT_ABILITY_ABILITY_ID = UNIT_ABILITY.addColumn("abilityId", "string", null);
    final transient DbColumn UNIT_ABILITY_ALLYCODE = UNIT_ABILITY.addColumn("allycode", "integer", null);
    final transient DbColumn UNIT_ABILITY_GUILD_ID = UNIT_ABILITY.addColumn("guildId", "integer", null);
    final transient DbColumn UNIT_ABILITY_LEVEL = UNIT_ABILITY.addColumn("level", "integer", null);

    //Master Table
    final transient DbTable SQLITE_MASTER = schema.addTable("sqlite_master");
    final transient DbColumn SQLITE_MASTER_TYPE = SQLITE_MASTER.addColumn("type", "string", null);
    final transient DbColumn SQLITE_MASTER_NAME = SQLITE_MASTER.addColumn("name", "string", null);

    final transient DbJoin ZETA_JOIN = spec.addJoin(null, ABILITY.getName(), null, UNIT_ABILITY.getName(), ABILITY_ID.getName());
    final transient DbJoin PLAYER_UNIT_JOIN = spec.addJoin(null, PLAYER.getName(), null, PLAYER_UNIT.getName(), PLAYER_ALLYCODE.getName());
    final transient DbJoin UNIT_NAME_JOIN = spec.addJoin(null, UNIT_INFO.getName(), null, PLAYER_UNIT.getName(), UNIT_BASE_ID.getName());
    static final transient int IS_UPDATED = 1;

    /**
     * Constructor.
     */
    public TableNames() {
        super();
    }

    String makeReplace(final String query) {
        return query.replace("INSERT", "REPLACE");//Hack to overcome the missing replace keyword in the lib. TODO: clone the lib and make REPLACE statement
    }
    //CHECKSTYLE.ON: VisibilityModifierCheck
}
