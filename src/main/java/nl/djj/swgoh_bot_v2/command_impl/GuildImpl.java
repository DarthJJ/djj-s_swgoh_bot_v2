package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.GuildCompare;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.SwgohGuild;
import nl.djj.swgoh_bot_v2.entities.db.Guild;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.MessageHelper;
import nl.djj.swgoh_bot_v2.helpers.StringHelper;
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

    private JSONObject getGuildData(final String code) {
        try {
            return httpHelper.getJsonObject(SwgohGgEndpoint.GUILD_ENDPOINT.getUrl() + code);
        } catch (final HttpRetrieveError error) {
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

    /**
     * Compares the guild with the given allycode / guild id.
     *
     * @param message the message.
     */
    //CHECKSTYLE.OFF: NPathComplexity
    public void compare(final Message message) {
        if (message.getArgs().isEmpty()) {
            message.error("Provide an allycode or guild id to compare to.");
            return;
        }
        String code = message.getArgs().get(0);
        if (code.length() >= SwgohConstants.ALLYCODE_LENGTH) {
            if (!StringHelper.validateAllycode(code)) {
                message.error("Please use the proper format <xxx-xxx-xxx>");
                return;
            }
            final JSONObject userData = this.implHelper.getProfileImpl().getProfileData(code);
            if (userData == null) {
                message.error("Invalid allycode, not a SWGOH profile");
                return;
            } else {
                code = Integer.toString(userData.getJSONObject("data").getInt("guild_id"));
            }
        }
        final JSONObject rivalData = getGuildData(code);
        final JSONObject guildData = getGuildData(message);
        if (rivalData == null || guildData == null) {
            message.error("No data received for either guild or rival");
            return;
        }
        try {
            final int playerGuildId = insertGuildData(guildData);
            final GuildCompare playerGuild = createProfile(playerGuildId);
            final int rivalGuildId = insertGuildData(rivalData);
            final GuildCompare rivalGuild = createProfile(rivalGuildId);
            message.done(MessageHelper.formatGuildCompare(playerGuild, rivalGuild));
        } catch (final SQLInsertionError | SQLRetrieveError error) {
            message.error(error.getMessage());
        }
    }
    //CHECKSTYLE.ON: NPathComplexity

    private GuildCompare createProfile(final int guildId) throws SQLRetrieveError {
        final Guild guild = this.dbHandler.getGuild(guildId);
        final int g13 = this.dbHandler.getGearLevelCountForGuild(guild.getIdentifier(), 13, null);
        final int g12 = this.dbHandler.getGearLevelCountForGuild(guild.getIdentifier(), 12, null);
        final int zetas = this.dbHandler.getZetaCountForGuild(guild.getIdentifier(), null);
        final GuildCompare profile = new GuildCompare(guild, g13, g12, zetas);
        for (int i = 0; i < SwgohConstants.RELIC_LEVELS.length; i++) {
            final int level = SwgohConstants.RELIC_LEVELS[i];
            profile.addRelic(level, this.dbHandler.getRelicLevelCountForGuild(guild.getIdentifier(), level, true, null));
        }
        for (final Map.Entry<String, String> entry : SwgohConstants.COMPARE_TOONS.entrySet()) {
            final int sevenStars = this.dbHandler.getStarLevelCountForGuild(guild.getIdentifier(), 7, entry.getKey());
            final int sixStars = this.dbHandler.getStarLevelCountForGuild(guild.getIdentifier(), 6, entry.getKey());
            final int unitG13 = this.dbHandler.getGearLevelCountForGuild(guild.getIdentifier(), 13, entry.getKey());
            final int unitG12 = this.dbHandler.getGearLevelCountForGuild(guild.getIdentifier(), 12, entry.getKey());
            final int unitZetas = this.dbHandler.getZetaCountForGuild(guild.getIdentifier(), entry.getKey());
            final int relic5plus = this.dbHandler.getRelicLevelCountForGuild(guild.getIdentifier(), 5, false, entry.getKey());
            profile.addUnitProfile(entry.getKey(), new GuildCompare.UnitProfile(entry.getValue(), sevenStars, sixStars, unitG13, unitG12, unitZetas, relic5plus));
        }
        return profile;
    }

    private int insertGuildData(final JSONObject guildData) throws SQLInsertionError {
        final JSONObject guildInfo = guildData.getJSONObject("data");
        final JSONArray players = guildData.getJSONArray("players");
        final String guildName = guildInfo.getString("name");
        final int guildMembers = guildInfo.getInt("member_count");
        final int guildGp = guildInfo.getInt("galactic_power");
        final int guildId = guildInfo.getInt("id");
        final Guild guild = new Guild(guildName, guildId, guildGp, guildMembers);
        this.dbHandler.insertGuild(guild);
        for (int i = 0; i < players.length(); i++) {
            final JSONObject playerJson = players.getJSONObject(i);
            final JSONArray playerUnits = playerJson.getJSONArray("units");
            this.implHelper.getProfileImpl().insertProfile(playerJson.getJSONObject("data"), guild.getIdentifier());
            this.implHelper.getUnitImpl().insertUnits(playerUnits, playerJson.getJSONObject("data").getInt("ally_code"), guildId);
        }
        return guild.getIdentifier();
    }
}
