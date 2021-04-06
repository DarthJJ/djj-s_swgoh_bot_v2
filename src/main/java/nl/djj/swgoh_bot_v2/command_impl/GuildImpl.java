package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.SwgohGuild;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.MessageHelper;
import org.json.JSONObject;

/**
 * @author DJJ
 */
public class GuildImpl {
    private final transient HttpHelper httpHelper;
    private final transient DatabaseHandler dbHandler;
    private final transient ImplHelper implHelper;

    /**
     * The constructor.
     *
     * @param logger     the logger.
     * @param dbHandler  the DB connection.
     * @param implHelper the implHelper.
     */
    public GuildImpl(final Logger logger, final DatabaseHandler dbHandler, final ImplHelper implHelper) {
        super();
        this.dbHandler = dbHandler;
        this.httpHelper = new HttpHelper(logger);
        this.implHelper = implHelper;
    }

    /**
     * Generic info for the guild.
     *
     * @param message the guild.
     */
    public void genericInfo(final Message message) {
        final String guildId;
        try {
            guildId = dbHandler.getSwgohIdByGuildId(message.getGuildId());
        } catch (final SQLRetrieveError error) {
            message.error(error.getMessage());
            return;
        }
        try {
            final JSONObject guildData = httpHelper.getJsonObject(SwgohGgEndpoint.GUILD_ENDPOINT.getUrl() + guildId);
            final SwgohGuild guild = SwgohGuild.initFromJson(guildData);
            message.done(MessageHelper.formatGuildSwgohProfile(guild));
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * GP Overview for the guild.
     *
     * @param message the guild.
     */
    public void gpOverview(final Message message) {
        final String guildId;
        try {
            guildId = dbHandler.getSwgohIdByGuildId(message.getGuildId());
        } catch (final SQLRetrieveError error) {
            message.error(error.getMessage());
            return;
        }
        try {
            final JSONObject guildData = httpHelper.getJsonObject(SwgohGgEndpoint.GUILD_ENDPOINT.getUrl() + guildId);
            message.done(MessageHelper.formatGuildGPOverview(implHelper.getProfileImpl().getGuildGp(guildData.getJSONArray("players"))));
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
        }
    }
}
