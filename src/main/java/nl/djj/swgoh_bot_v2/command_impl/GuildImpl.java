package nl.djj.swgoh_bot_v2.command_impl;

import net.dv8tion.jda.api.entities.MessageChannel;
import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.config.enums.GalacticLegends;
import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.compare.GuildCompare;
import nl.djj.swgoh_bot_v2.entities.compare.PlayerGLStatus;
import nl.djj.swgoh_bot_v2.entities.db.*;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;
import nl.djj.swgoh_bot_v2.helpers.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;
import java.util.*;

import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toMap;

/**
 * @author DJJ
 */
public class GuildImpl extends BaseImpl {
    private final transient HttpHelper httpHelper;
    private final transient ImplHelper implHelper;

    /**
     * The constructor.
     *
     * @param logger     the logger.
     * @param dao        the DB connection.
     * @param implHelper the implHelper.
     */
    public GuildImpl(final Logger logger, final DAO dao, final ImplHelper implHelper) {
        super(logger, dao, GuildImpl.class.getName());
        this.httpHelper = new HttpHelper(logger);
        this.implHelper = implHelper;
    }

    private int getAndUpdateGuildData(final String discordId, final int swgohIdentifier, final MessageChannel channel) throws RetrieveError, InsertionError, HttpRetrieveError {
        Guild guild;
        final int swgohId;
        if (swgohIdentifier == -1) {
            swgohId = dao.configDao().getById(discordId).getSwgohId();
            guild = dao.guildDao().getById(swgohId);
            if (guild != null && guild.getLastUpdated() != null && Duration.between(guild.getLastUpdated(), StringHelper.getCurrentDateTime()).toHours() < BotConstants.MAX_DATA_AGE) {
                return swgohId;
            }
        } else {
            swgohId = swgohIdentifier;
        }
        if (channel != null) {
            channel.sendMessage("Guild data is older than: " + BotConstants.MAX_DATA_AGE + " hours\nRefreshing from SWGOH.gg\n**PLEASE STAND BY, THIS CAN TAKE UP TO A MINUTE**").queue();
        }
        final JSONObject guildData = httpHelper.getJsonObject(SwgohGgEndpoint.GUILD_ENDPOINT.getUrl() + swgohId);
        final JSONObject guildInfo = guildData.getJSONObject("data");
        final JSONArray players = guildData.getJSONArray("players");
        final String guildName = guildInfo.getString("name");
        final int guildMembers = guildInfo.getInt("member_count");
        final int guildGp = guildInfo.getInt("galactic_power");
        guild = new Guild(swgohId, discordId, guildName, guildGp, guildMembers, StringHelper.getCurrentDateTime(), StringHelper.getCurrentDateTime());
        this.dao.guildDao().save(guild);
        final List<PlayerUnit> playerUnits = new ArrayList<>();
        final List<UnitAbility> unitAbilities = new ArrayList<>();
        for (int i = 0; i < players.length(); i++) {
            final JSONObject playerJson = players.getJSONObject(i);
            final JSONArray playerUnitData = playerJson.getJSONArray("units");
            final Player player = this.implHelper.getProfileImpl().insertProfile(playerJson.getJSONObject("data"), guild);
            final Map<String, List<?>> data = this.implHelper.getUnitImpl().jsonToPlayerUnits(playerUnitData, player);
            playerUnits.addAll((List<PlayerUnit>) data.get("units"));
            unitAbilities.addAll((List<UnitAbility>) data.get("abilities"));
        }
        logger.debug(className, "Inserting all units");
        dao.playerUnitDao().saveAll(playerUnits);
        logger.debug(className, "Inserting all abilities");
        dao.unitAbilityDao().saveAll(unitAbilities);
        return guild.getIdentifier();
    }

    public void unitOverview(final Message message) {
        if (message.getArgs().isEmpty()) {
            message.error("Please provide a searchKey");
            return;
        }
        try {
            final Unit unit = dao.unitDao().getById(dao.abbreviationDao().resolveUnitId(String.join(" ", message.getArgs())));
            final Guild guild = dao.guildDao().getByDiscordId(message.getGuildId());
            Map<String, PlayerUnit> guildData = new TreeMap<>();
            for (final Player player : guild.getPlayers()) {
                final PlayerUnit playerUnit = dao.playerUnitDao().getForPlayer(player, unit.getBaseId());
                guildData.put(player.getName(), Objects.requireNonNullElseGet(playerUnit, () -> new PlayerUnit(player, unit, 0, 0, 0, 0, 0, 0)));
//                message.done(MessageHelper.formatGuildUnitData(guild, guildData));
            }


        } catch (final RetrieveError error) {
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
            final Map<String, Integer> relicOverview = dao.playerDao().getRelicForGuild(guildId, relicLevel);
            message.done(MessageHelper.formatGuildRelicOverview(relicOverview, relicLevel));
        } catch (final RetrieveError | HttpRetrieveError | InsertionError error) {
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
            final Map<String, Integer> gpOverview = dao.playerDao().getGpForGuild(guildId);
            message.done(MessageHelper.formatGuildGPOverview(gpOverview));
        } catch (final RetrieveError | HttpRetrieveError | InsertionError error) {
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

            final List<GlRequirement> requirements = dao.glRequirementDao().getForEvent(glEvent.getKey());
            Map<String, PlayerGLStatus> playerStatus = new TreeMap<>();
            for (final Player player : dao.guildDao().getById(guildId).getPlayers()) {
                playerStatus.put(player.getName(), this.implHelper.getProfileImpl().getGlStatus(glEvent.getName(), player, requirements));
            }

            playerStatus = playerStatus.entrySet().stream()
                    .sorted(reverseOrder(Comparator.comparing((Map.Entry<String, PlayerGLStatus> m) -> m.getValue().getTotalCompleteness()))
                            .thenComparing(Map.Entry::getKey))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new));


            message.done(MessageHelper.formatGuildGLStatus(glEvent.getName(), playerStatus));
        } catch (final RetrieveError | InsertionError | HttpRetrieveError error) {
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
        try {
            if (code.length() >= SwgohConstants.ALLYCODE_LENGTH) {
                if (StringHelper.isInvalidAllycode(code)) {
                    message.error("Please use the proper format <xxx-xxx-xxx>");
                    return;
                }
                final JSONObject userData = this.httpHelper.getJsonObject(SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + Integer.parseInt(code));
                if (userData == null) {
                    message.error("Invalid allycode, not a SWGOH profile");
                    return;
                } else {
                    code = Integer.toString(userData.getJSONObject("data").getInt("guild_id"));
                }
            }

            final int playerGuildId = getAndUpdateGuildData(message.getGuildId(), -1, message.getChannel());
            final GuildCompare playerGuild = createProfile(playerGuildId);
            final int rivalGuildId = getAndUpdateGuildData(null, Integer.parseInt(code), null);
            final GuildCompare rivalGuild = createProfile(rivalGuildId);
            message.done(MessageHelper.formatGuildCompare(playerGuild, rivalGuild));
        } catch (final InsertionError | RetrieveError | HttpRetrieveError error) {
            message.error(error.getMessage());
        }
    }
    //CHECKSTYLE.ON: NPathComplexity

    private GuildCompare createProfile(final int guildId) throws RetrieveError {
        final Guild guild = this.dao.guildDao().getById(guildId);
        final int g13 = this.dao.playerUnitDao().getGearCount(guild, SwgohConstants.GEAR_LEVEL_13, null);
        final int g12 = this.dao.playerUnitDao().getGearCount(guild, SwgohConstants.GEAR_LEVEL_12, null);
        final int zetas = this.dao.playerUnitDao().getZetaCount(guild, null);
        final GuildCompare profile = new GuildCompare(guild, g13, g12, zetas);
        profile.setRelics(this.dao.playerUnitDao().getRelics(guild));

        for (final Map.Entry<String, String> entry : SwgohConstants.COMPARE_TOONS.entrySet()) {
            final int sevenStars = this.dao.playerUnitDao().getRarityCountForUnit(guild, 7, entry.getKey());
            final int sixStars = this.dao.playerUnitDao().getRarityCountForUnit(guild, 6, entry.getKey());
            final int unitG13 = this.dao.playerUnitDao().getGearCount(guild, SwgohConstants.GEAR_LEVEL_13, entry.getKey());
            final int unitG12 = this.dao.playerUnitDao().getGearCount(guild, SwgohConstants.GEAR_LEVEL_12, entry.getKey());
            final int unitZetas = this.dao.playerUnitDao().getZetaCount(guild, entry.getKey());
            final int relic5plus = this.dao.playerUnitDao().getRelicCountForUnit(guild, 5, entry.getKey());
            profile.addUnitProfile(entry.getKey(), new GuildCompare.UnitProfile(entry.getValue(), sevenStars, sixStars, unitG13, unitG12, unitZetas, relic5plus));
        }
        return profile;
    }
}
