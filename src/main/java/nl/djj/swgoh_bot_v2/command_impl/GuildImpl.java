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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
        final JSONObject guildData = getGuildData(message);
        if (guildData == null) {
            return;
        }
        final SwgohGuild guild = SwgohGuild.initFromJson(guildData);
        message.done(MessageHelper.formatGuildSwgohProfile(guild));
    }

    /**
     * creates an Relic overview for the guild.
     *
     * @param message the guild info.
     */
    public void relicOverview(final Message message) {
        if (message.getArgs().isEmpty()) {
            message.error("Please provide a relic level");
            return;
        }
        final JSONObject guildData = getGuildData(message);
        if (guildData != null) {
            final JSONArray playersData = guildData.getJSONArray("players");
            Map<String, Integer> playerResults = new ConcurrentHashMap<>();
            for (int i = 0; i < playersData.length(); i++) {
                final JSONObject playerData = playersData.getJSONObject(i);
                final Map<String, Integer> playerResult = this.implHelper.getUnitImpl().checkRelicLevel(playerData.getJSONArray("units"), Integer.parseInt(message.getArgs().get(0)));
                playerResults.put(playerData.getJSONObject("data").getString("name"), playerResult.size());
            }
            playerResults = playerResults.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            message.done(MessageHelper.formatGuildRelicOverview(playerResults, message.getArgs().get(0)));
        }
    }

    private JSONObject getGuildData(final Message message) {
        final String guildId;
        try {
            guildId = dbHandler.getSwgohIdByGuildId(message.getGuildId());
        } catch (final SQLRetrieveError error) {
            message.error(error.getMessage());
            return null;
        }
        try {
            return httpHelper.getJsonObject(SwgohGgEndpoint.GUILD_ENDPOINT.getUrl() + guildId);
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
            return null;
        }
    }

    /**
     * Gets the GP overview for the guild.
     *
     * @param message the message.
     */
    public void gpOverview(final Message message) {
        final JSONObject guildData = getGuildData(message);
        if (guildData != null) {
            final JSONArray playerData = guildData.getJSONArray("players");
            Map<String, Integer> members = new ConcurrentHashMap<>();
            for (int i = 0; i < playerData.length(); i++) {
                final JSONObject player = playerData.getJSONObject(i).getJSONObject("data");
                members.put(player.getString("name"), player.getInt("galactic_power"));
            }
            members = members.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            message.done(MessageHelper.formatGuildGPOverview(members));
        }
    }
}
