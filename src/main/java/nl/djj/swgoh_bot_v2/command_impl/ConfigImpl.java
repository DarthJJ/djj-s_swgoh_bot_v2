package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.db.Config;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.MessageHelper;
import nl.djj.swgoh_bot_v2.helpers.StringHelper;

/**
 * @author DJJ
 */
public class ConfigImpl {
    private final transient String className = this.getClass().getName();
    private final transient DAO dao;
    private final transient Logger logger;
    private final transient HttpHelper httpHelper;

    /**
     * Constructor.
     *
     * @param dao    the DB Connection
     * @param logger the logger.
     */
    public ConfigImpl(final DAO dao, final Logger logger) {
        super();
        this.dao = dao;
        this.logger = logger;
        this.httpHelper = new HttpHelper(logger);
    }

    /**
     * Gets the ignore role for the guild.
     *
     * @param guildId the guild.
     * @return the role or an empty string.
     */
    public String getIgnoreRole(final String guildId) {
        try {
            return dao.configDao().getById(guildId).getIgnoreRole();
        } catch (final RetrieveError error) {
            return "";
        }
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
        try {
            final Config config = getConfig(message.getGuildId());
            config.setPrefix(message.getArgs().get(0));
            dao.configDao().save(config);
            message.done("Prefix updated to: " + config.getPrefix());
        } catch (final InsertionError | RetrieveError exception) {
            logger.error(className, "setPrefix", exception.getMessage());
        }
    }

    private Config getConfig(final String discordId) throws RetrieveError, InsertionError {
        Config config = dao.configDao().getById(discordId);
        if (config == null) {
            config = new Config(discordId);
            dao.configDao().save(config);
        }
        return config;
    }

    /**
     * Get's the prefix for the given guild.
     *
     * @param guildId the guild.
     * @return prefix.
     */
    public String getPrefix(final String guildId) {
        try {
            return getConfig(guildId).getPrefix();
        } catch (final RetrieveError | InsertionError exception) {
            logger.error(className, "getPrefix", exception.getMessage());
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
        try {
            this.httpHelper.getJsonObject(SwgohGgEndpoint.GUILD_ENDPOINT.getUrl() + message.getArgs().get(0));
            final Config config = getConfig(message.getGuildId());
            config.setSwgohId(Integer.parseInt(message.getArgs().get(0)));
            message.getChannel().sendMessage("Verifying the guild with SWGOH\nPlease wait.").queue();
            dao.configDao().save(config);
            message.done("Guild ID set");
        } catch (final HttpRetrieveError | InsertionError | RetrieveError exception) {
            logger.error(className, "setGuildId", exception.getMessage());
            message.error(exception.getMessage());
        }
    }

    /**
     * Sets the ignore role for the guild.
     *
     * @param message the guild info.
     */
    public void setIgnoreRole(final Message message) {
        if (message.getArgs().isEmpty()) {
            message.error("Please tag a role");
            return;
        }
        try {
            final Config config = getConfig(message.getGuildId());
            config.setIgnoreRole(message.getArgs().get(0).replace("@", ""));
            dao.configDao().save(config);
            message.done("Updated the Ignore role");
        } catch (final InsertionError | RetrieveError exception) {
            logger.error(className, "setIgnoreRole", exception.getMessage());
            message.error(exception.getMessage());
        }
    }

    /**
     * Gets the config for the given guild.
     *
     * @param message the message containing the guild info.
     */
    public void showConfig(final Message message) {
        try {
            message.done(MessageHelper.formatConfig(getConfig(message.getGuildId())));
        } catch (final InsertionError | RetrieveError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Sets the notify channel for the bot for the guild.
     *
     * @param message contains the guild info.
     */
    public void setNotifyChannel(final Message message) {
        if (message.getArgs().isEmpty()) {
            message.error("Please tag a channel");
            return;
        }
        try {
            final Config config = getConfig(message.getGuildId());
            config.setNotifyChannel(StringHelper.stripMessageChannel(message.getAltArgs().get(0)));
            dao.configDao().save(config);
            message.done("Notification Channel updated");
        } catch (final InsertionError | RetrieveError exception) {
            logger.error(className, "setNotifyChannel", exception.getMessage());
            message.error(exception.getMessage());
        }
    }
}
