package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.Config;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
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

    public void setPrefix(final Message message) {
        if (message.getArgs().isEmpty()) {
            message.error("Please provide a prefix to set");
            return;
        }
        try {
            dbHandler.setPrefixForGuild(message.getGuildId(), message.getArgs().get(0));
            message.done("Prefix updated to: '" + message.getArgs().get(0) + "'");
        } catch (final SQLInsertionError error) {
            message.error(error.getMessage());
        }
    }

    public String getPrefix(final String guildId){
        try {
            return dbHandler.getPrefixForGuild(guildId);
        } catch (final SQLRetrieveError error) {
            return Config.DEFAULT_PREFIX;
        }
    }

    public void setGuildId(final Message message) {
        if (message.getArgs().isEmpty()) {
            message.error("Please provide a guild ID to set");
            return;
        }
        try {
            this.httpHelper.getJsonObject(SwgohGgEndpoint.GUILD_ENDPOINT.getUrl() + message.getArgs().get(0));
            dbHandler.setSwgohIdForGuild(message.getGuildId(), message.getArgs().get(0));
            message.done("Guild ID set");
        } catch (final SQLInsertionError | HttpRetrieveError error) {
            message.error(error.getMessage());
        }
    }

    public void getConfig(final Message message) {
        //TODO: implement correct.
    }
}
