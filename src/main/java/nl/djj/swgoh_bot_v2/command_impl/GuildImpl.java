package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Guild;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.SwgohGuild;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import org.json.JSONObject;

/**
 * @author DJJ
 */
public class GuildImpl {
    private final transient HttpHelper httpHelper;
    private final transient DatabaseHandler dbHandler;
    private final transient ImplHelper implHelper;

    public GuildImpl(final Logger logger, final DatabaseHandler dbHandler, final ImplHelper implHelper) {
        super();
        this.dbHandler = dbHandler;
        this.httpHelper = new HttpHelper(logger);
        this.implHelper = implHelper;
    }

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
            System.out.println(guildData);
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
        }
    }
}
