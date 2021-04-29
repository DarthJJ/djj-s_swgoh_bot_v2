package nl.djj.swgoh_bot_v2.command_impl;

import net.dv8tion.jda.api.entities.MessageChannel;
import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.config.GalacticLegends;
import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.compare.CompareUnit;
import nl.djj.swgoh_bot_v2.entities.compare.GuildCompare;
import nl.djj.swgoh_bot_v2.entities.compare.PlayerGLStatus;
import nl.djj.swgoh_bot_v2.entities.db.GlRequirement;
import nl.djj.swgoh_bot_v2.entities.db.Guild;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.MessageHelper;
import nl.djj.swgoh_bot_v2.helpers.StringHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
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

    private int getAndUpdateGuildData(final String discordId, final int swgohIdentifier, final MessageChannel channel) throws HttpRetrieveError, SQLRetrieveError, SQLInsertionError {
        Guild guild;
        final int swgohId;
        if (swgohIdentifier == -1) {
            guild = dbHandler.getGuild(dbHandler.getSwgohIdByGuildId(discordId));
            swgohId = guild.getIdentifier();
            if (Duration.between(guild.getLastUpdated(), StringHelper.getCurrentDateTime()).toHours() < BotConstants.MAX_DATA_AGE) {
                return swgohId;
            }
        } else {
            swgohId = swgohIdentifier;
        }
        if (channel != null) {
            channel.sendMessage("Guild data is older than: " + BotConstants.MAX_DATA_AGE + " hours\nRefreshing from SWGOH.gg").queue();
        }
        final JSONObject guildData = httpHelper.getJsonObject(SwgohGgEndpoint.GUILD_ENDPOINT.getUrl() + swgohId);
        final JSONObject guildInfo = guildData.getJSONObject("data");
        final JSONArray players = guildData.getJSONArray("players");
        final String guildName = guildInfo.getString("name");
        final int guildMembers = guildInfo.getInt("member_count");
        final int guildGp = guildInfo.getInt("galactic_power");
        final int guildId = guildInfo.getInt("id");
        final LocalDateTime lastUpdated = StringHelper.getCurrentDateTime();
        guild = new Guild(guildName, guildId, guildGp, guildMembers, lastUpdated);
        this.dbHandler.insertGuild(guild);
        for (int i = 0; i < players.length(); i++) {
            final JSONObject playerJson = players.getJSONObject(i);
            final JSONArray playerUnits = playerJson.getJSONArray("units");
            this.implHelper.getProfileImpl().insertProfile(playerJson.getJSONObject("data"), guild.getIdentifier());
            this.implHelper.getUnitImpl().insertUnits(playerUnits, playerJson.getJSONObject("data").getInt("ally_code"), guildId);
        }
        return guildId;
    }

    /**
     * Generic info for the guild.
     *
     * @param message the guild.
     */
    public void genericInfo(final Message message) {
        try {
            final int guildId = getAndUpdateGuildData(message.getGuildId(), -1, message.getChannel());
            final Guild guild = this.dbHandler.getGuild(guildId);
            message.done(MessageHelper.formatGuildSwgohProfile(guild));
        } catch (final HttpRetrieveError | SQLRetrieveError | SQLInsertionError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * creates an Relic overview for the guild.
     *
     * @param message the guild info.
     */
    public void relicOverview(final Message message) {
        if (message.getArgs().isEmpty()) {
            message.getArgs().add("1");
        }
        try {
            final int relicLevel = Integer.parseInt(message.getArgs().get(0));
            final int guildId = getAndUpdateGuildData(message.getGuildId(), -1, message.getChannel());
            final Map<String, Integer> relicOverview = this.dbHandler.getGuildRelicOverview(guildId, relicLevel);
            message.done(MessageHelper.formatGuildRelicOverview(relicOverview, relicLevel));
        } catch (final SQLRetrieveError | HttpRetrieveError | SQLInsertionError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Gets the GP overview for the guild.
     *
     * @param message the message.
     */
    public void gpOverview(final Message message) {
        try {
            final int guildId = getAndUpdateGuildData(message.getGuildId(), -1, message.getChannel());
            final Map<String, Integer> gpOverview = dbHandler.getGuildGPOverview(guildId);
            message.done(MessageHelper.formatGuildGPOverview(gpOverview));
        } catch (final SQLRetrieveError | HttpRetrieveError | SQLInsertionError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Get's the GL status of the guild.
     *
     * @param message the message.
     */
    public void glOverview(final Message message) {
        if (message.getArgs().isEmpty()) {
            message.error("Please provide an valid GL");
            return;
        }
        if (!GalacticLegends.isEvent(message.getArgs().get(0))) {
            message.error("Invalid GL name given, please use: \n```" + GalacticLegends.getKeys() + "```");
            return;
        }
        try {
            final int guildId = getAndUpdateGuildData(message.getGuildId(), -1, message.getChannel());
            final GalacticLegends glEvent = GalacticLegends.getByKey(message.getArgs().get(0));

            final List<GlRequirement> requirements = dbHandler.getGlRequirements(glEvent);
            final List<Integer> guildAllyCodes = dbHandler.getMembersOfGuild(guildId);
            final Map<String, PlayerGLStatus> playerStatus = new TreeMap<>();
            for (final int allycode : guildAllyCodes) {
                final String playerName = dbHandler.getPlayerNameForAllycode(allycode);
                playerStatus.put(playerName, this.implHelper.getProfileImpl().getGlStatus(glEvent.getName(), allycode, requirements));
            }
            message.done(MessageHelper.formatGuildGLStatus(glEvent.getName(), playerStatus));
        } catch (final SQLRetrieveError | HttpRetrieveError | SQLInsertionError error) {
            message.error(error.getMessage());
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
            if (StringHelper.isInvalidAllycode(code)) {
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
        try {
            final int playerGuildId = getAndUpdateGuildData(message.getGuildId(), -1, message.getChannel());
            final GuildCompare playerGuild = createProfile(playerGuildId);
            final int rivalGuildId = getAndUpdateGuildData(null, Integer.parseInt(code), null);
            final GuildCompare rivalGuild = createProfile(rivalGuildId);
            message.done(MessageHelper.formatGuildCompare(playerGuild, rivalGuild));
        } catch (final SQLInsertionError | SQLRetrieveError | HttpRetrieveError error) {
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

    public void unitOverview(final Message message) {
        if (message.getArgs().isEmpty()){
            message.error("Please provide an searchKey");
            return;
        }
        try {
            final String unitId = dbHandler.resolveUnitId(String.join(" ", message.getArgs()));
            final int guildId = getAndUpdateGuildData(message.getGuildId(), -1, message.getChannel());
            final List<Integer> allyCodes = dbHandler.getMembersOfGuild(guildId);

            Map<String, PlayerUnit> guildData = new TreeMap<>();

            for (final Integer allycode : allyCodes) {
                final PlayerUnit unitData = dbHandler.getPlayerUnit(unitId, allycode);
                final String playerName = dbHandler.getPlayerNameForAllycode(allycode);
                guildData.put(playerName, unitData);
            }
            guildData = guildData.entrySet().stream()
                    .sorted((e1, e2)-> Integer.compare(e2.getValue().getLevel(), e1.getValue().getLevel()))
                    .sorted((e1, e2)-> Integer.compare(e2.getValue().getRelic(), e1.getValue().getRelic()))
                    .sorted((e1, e2)-> Integer.compare(e2.getValue().getGear(), e1.getValue().getGear()))
                    .sorted((e1, e2)-> Integer.compare(e2.getValue().getGearPieces(), e1.getValue().getGearPieces()))
                    .sorted((e1, e2)-> Integer.compare(e2.getValue().getGalacticPower(), e1.getValue().getGalacticPower()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2)-> e1, LinkedHashMap::new));
            message.done(MessageHelper.formatGuildUnitOver(guildData, dbHandler.getUnitNameForId(unitId)));
        } catch (final SQLRetrieveError | HttpRetrieveError | SQLInsertionError error){
            message.error(error.getMessage());
        }
    }
}
