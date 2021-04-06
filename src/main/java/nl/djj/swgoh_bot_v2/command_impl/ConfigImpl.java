package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Config;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class ConfigImpl {
    private final transient DatabaseHandler dbHandler;
    private final transient HttpHelper httpHelper;

    /**
     * Constructor.
     *
     * @param dbHandler the DB Connection
     * @param logger    the logger.
     */
    public ConfigImpl(final DatabaseHandler dbHandler, final Logger logger) {
        super();
        this.dbHandler = dbHandler;
        this.httpHelper = new HttpHelper(logger);
    }

    /**
     * Gets the ignore role for the guild.
     *
     * @param guildId the guild.
     * @return the role or an empty string.
     */
    public String getIgnoreRole(final String guildId) {
        return ""; //TODO: Implement correct
    }

    /**
     * Set's the prefix for the given guild.
     *
     * @param message the message containing the guild info.
     */
    public void setPrefix(final Message message) {
        if (message.getArgs().isEmpty()) {
            message.error("Please provide a prefix to set");
            return;
        }
        Config config;
        try {
            config = dbHandler.getConfig(message.getGuildId());
        } catch (final SQLRetrieveError error) {
            config = new Config(message.getGuildId());
        }
        config.setPrefix(message.getArgs().get(0));
        updateConfig(config, message);
    }

    private void updateConfig(final Config config, final Message message) {
        try {
            dbHandler.updateConfig(config);
            message.done("Config updated!");
        } catch (final SQLInsertionError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Get's the prefix for the given guild.
     *
     * @param guildId the guild.
     * @return prefix.
     */
    public String getPrefix(final String guildId) {
        try {
            return dbHandler.getPrefixForGuild(guildId);
        } catch (final SQLRetrieveError error) {
            return BotConstants.DEFAULT_PREFIX;
        }
    }

    /**
     * Sets the guild id.
     *
     * @param message the message containing the guild info.
     */
    public void setGuildId(final Message message) {
        if (message.getArgs().isEmpty()) {
            message.error("Please provide a prefix to set");
            return;
        }
        Config config;
        try {
            config = dbHandler.getConfig(message.getGuildId());
            this.httpHelper.getJsonObject(SwgohGgEndpoint.GUILD_ENDPOINT.getUrl() + message.getArgs().get(0));
        } catch (final SQLRetrieveError error) {
            config = new Config(message.getGuildId());
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
            return;
        }
        config.setSwgohId(message.getArgs().get(0));
        updateConfig(config, message);
    }

    /**
     * Gets the config for the given guild.
     *
     * @param message the message containing the guild info.
     */
    public void showConfig(final Message message) {
//        try {
//            message.done(MessageHelper.formatConfig(dbHandler.getConfig(message.getGuildId())));
//        } catch (final SQLRetrieveError error) {
//            message.error(error.getMessage());
//        }
    }
}
