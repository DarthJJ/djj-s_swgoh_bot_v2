package nl.djj.swgoh_bot_v2.command_impl;

import net.dv8tion.jda.api.entities.Role;
import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.config.enums.GalacticLegends;
import nl.djj.swgoh_bot_v2.config.enums.Permission;
import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.compare.GLUnit;
import nl.djj.swgoh_bot_v2.entities.compare.PlayerGLStatus;
import nl.djj.swgoh_bot_v2.entities.db.*;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;
import nl.djj.swgoh_bot_v2.helpers.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author DJJ
 */
public class ProfileImpl extends BaseImpl {
    private final transient HttpHelper httpHelper;
    private final transient ImplHelper implHelper;

    /**
     * @param logger     the logger.
     * @param dao        the DAO.
     * @param implHelper the ImplHelper.
     */
    public ProfileImpl(final Logger logger, final DAO dao, final ImplHelper implHelper) {
        super(logger, dao, ProfileImpl.class.getName());
        this.httpHelper = new HttpHelper(logger);
        this.implHelper = implHelper;
    }

    /**
     * @param discordId the discordId to search for.
     * @return the found allycode.
     */
    public int getAllycodeByDiscord(final String discordId) {
        try {
            final Player player = this.dao.playerDao().getByDiscordId(discordId);
            if (player == null) {
                return -1;
            }
            return player.getAllycode();
        } catch (final RetrieveError retrieveError) {
            logger.error(className, "getAllycodeByDiscord", retrieveError.getMessage());
            return -1;
        }
    }

    private Player getAndUpdateProfileData(final Message message, final int swgohIdentifier) throws InsertionError, RetrieveError, HttpRetrieveError {
        final Player user;
        final int swgohId;
        if (swgohIdentifier == -1) {
            user = dao.playerDao().getById(message.getAllycode());
            swgohId = user.getAllycode();
        } else {
            swgohId = swgohIdentifier;
        }
        return getAndUpdatePlayer(swgohId);
    }

    /**
     * Gets the player and inserts it in the DB.
     *
     * @param allycode the allycode.
     * @return a player object.
     * @throws InsertionError    when something goes wrong inserting.
     * @throws RetrieveError     when something goes wrong retrieving.
     * @throws HttpRetrieveError the something goes wrong retrieving.
     */
    public Player getAndUpdatePlayer(final int allycode) throws InsertionError, RetrieveError, HttpRetrieveError {
        final JSONObject playerData = httpHelper.getJsonObject(SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + allycode);
        final JSONObject playerInfo = playerData.getJSONObject("data");
        final JSONArray playerUnits = playerData.getJSONArray("units");
        final Player player = insertProfile(playerInfo, null);
        final Map<String, List<?>> data = this.implHelper.getUnitImpl().jsonToPlayerUnits(playerUnits, player);
        logger.debug(className, "Inserting all units");
        dao.playerUnitDao().saveAll((List<PlayerUnit>) data.get("units"));
        logger.debug(className, "Inserting all abilities");
        dao.unitAbilityDao().saveAll((List<UnitAbility>) data.get("abilities"));
        return player;
    }

    /**
     * Register a user based on the message.
     *
     * @param message the message.
     */
    public void registerUser(final Message message) {
        try {
            if (dao.playerDao().exists(message.getAllycode())) {
                message.error("You are already registered");
                return;
            }
            final int allycode = Integer.parseInt(String.join(", ", message.getArgs()).replace("-", ""));
            if (StringHelper.isInvalidAllycode(String.valueOf(allycode))) {
                message.error("Allycode validation error, syntax: xxx-xxx-xxx");
                return;
            }
            this.httpHelper.getJsonObject(SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + allycode);
            Permission permission = Permission.USER;
            if (message.getAuthorId().equals(BotConstants.OWNER_ID)) {
                permission = Permission.ADMINISTRATOR;
            }
            dao.playerDao().save(new Player(allycode, message.getAuthor(), message.getAuthorId(), permission));
            message.done("You are successfully registered!\nuse: **" + message.getGuildPrefix() + "help** to see all available commands");
        } catch (final RetrieveError | InsertionError error) {
            message.error(error.getMessage());
        } catch (final HttpRetrieveError error) {
            message.error("No profile found on SWGOH.gg with allycode");
        }

    }

    /**
     * Unregister a user.
     *
     * @param message the message.
     */
    public void unregisterUser(final Message message) {
        try {
            if (dao.playerDao().exists(message.getAllycode())) {
                dao.playerDao().delete(message.getAllycode());
                message.done("You are now unregistered");
            } else {
                message.error("You are not registered by the bot, cannot unregister");
            }
        } catch (final RetrieveError | DeletionError exception) {
            message.error(exception.getMessage());
        }
    }


    /**
     * @param discordId     the discordId.
     * @param requiredLevel the level required for the command.
     * @return if it is allowed.
     */
    public boolean isAllowed(final String discordId, final Permission requiredLevel) {
        if (discordId.equals(BotConstants.OWNER_ID)) {
            return true;
        }
        try {
            final Player player = dao.playerDao().getByDiscordId(discordId);
            if (player == null) {
                return requiredLevel == Permission.USER;
            }
            return player.getPermission().getLevel() <= requiredLevel.getLevel();
        } catch (final RetrieveError exception) {
            return requiredLevel == Permission.USER;
        }
    }

    /**
     * Compares the user's profile to the given allycode.
     *
     * @param message the message.
     */
    public void compare(final Message message) {
        try {
            if (message.getArgs().isEmpty()) {
                message.error("Please provide an allycode to compare with");
                return;
            }
            if (StringHelper.isInvalidAllycode(message.getArgs().get(0))) {
                message.getChannel().sendMessage("Allycode validation error, syntax: <xxx-xxx-xxx>").queue();
                return;
            }
            final Player player = getAndUpdateProfileData(message, -1);
            final Player rival = getAndUpdateProfileData(null, Integer.parseInt(message.getArgs().get(0)));
            message.done(MessageHelper.formatProfileCompare(implHelper.getUnitImpl().compareProfiles(player.getAllycode(), rival.getAllycode())));
        } catch (final InsertionError | RetrieveError | HttpRetrieveError exception) {
            message.error(exception.getMessage());
        }
    }

    /**
     * Updates the presence of user.
     *
     * @param guildId  the guild.
     * @param userId   the userId.
     * @param username the username.
     * @param roles    the roles of the user.
     */
    public void updatePresence(final String guildId, final String userId, final String username, final List<Role> roles) {
        final String guildIgnoreRole = implHelper.getConfigImpl().getIgnoreRole(guildId);
        for (final Role role : roles) {
            if (role.getName().toLowerCase(Locale.ENGLISH).equals(guildIgnoreRole.toLowerCase(Locale.ROOT))) {
                return;
            }
        }
        try {
            dao.presenceDao().add(new Presence(userId, username, StringHelper.getCurrentDateTime()));
        } catch (final InsertionError exception) {
            logger.error(className, "updatePresence", exception.getMessage());
        }
    }

    /**
     * Get's the GL status for a player.
     *
     * @param message the message.
     */
    public void glStatus(final Message message) {
        if (message.getArgs().isEmpty() || !GalacticLegends.isEvent(message.getArgs().get(0))) {
            message.error("Invalid GL name given, please use: \n```" + GalacticLegends.getKeys() + "```");
            return;
        }
        try {
            final Player player = getAndUpdateProfileData(message, -1);
            final GalacticLegends glEvent = GalacticLegends.getByKey(message.getArgs().get(0));
            final List<GlRequirement> requirements = dao.glRequirementDao().getForEvent(glEvent.getKey());
            message.done(MessageHelper.formatPlayerGLStatus(getGlStatus(glEvent.getName(), dao.playerDao().getById(player.getAllycode()), requirements)));
        } catch (final RetrieveError | InsertionError | HttpRetrieveError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Formats a GL status for the player.
     *
     * @param eventName    the GL name.
     * @param player       the player.
     * @param requirements the list of requirements.
     * @return a PlayerGLStatus object.
     * @throws RetrieveError when something goes wrong .
     */
    public PlayerGLStatus getGlStatus(final String eventName, final Player player, final List<GlRequirement> requirements) throws RetrieveError {
        final List<GLUnit> compares = new ArrayList<>();
        double totalCompletion = 0.0;
        for (final GlRequirement requirement : requirements) {
            final PlayerUnit unit = dao.playerUnitDao().getForPlayer(player, requirement.getBaseId());
            final GLUnit compare;
            final String abbreviation = dao.abbreviationDao().getByUnitId(requirement.getBaseId());
            if (unit == null) {
                compare = new GLUnit(requirement.getBaseId(), abbreviation, 0, 0, 0, 0, 0);
                compare.setCompleteness(0.0);
            } else {
                final long zetas = unit.getAbilities().stream().filter(unitAbility -> unitAbility.getBaseAbility().isZeta() && unitAbility.getBaseAbility().getTierMax() == unitAbility.getLevel()).count();
                compare = new GLUnit(requirement.getBaseId(), abbreviation, unit.getGear(), unit.getGearPieces(), unit.getRelic(), unit.getRarity(), (int) zetas);
                compare.setCompleteness(CalculationHelper.calculateCompletion(unit.getRarity(), compare.getGearLevel(), compare.getRelicLevel(), unit.getGearPieces(),
                        SwgohConstants.MAX_RARITY_LEVEL, requirement.getRelicLevel(), requirement.getGearLevel()));
            }
            totalCompletion += compare.getCompleteness();
            compares.add(compare);
        }
        compares.sort(Comparator.comparing(GLUnit::getCompleteness));
        return new PlayerGLStatus(eventName, compares, totalCompletion / requirements.size());
    }

    /**
     * Checks the users relics.
     *
     * @param message the message Object.
     */
    public void relic(final Message message) {
        try {
            getAndUpdateProfileData(message, -1);
            int relicLevel = SwgohConstants.MAX_RELIC_TIER;
            if (!message.getArgs().isEmpty()) {
                relicLevel = Integer.parseInt(message.getArgs().get(0));
            }
            message.done(MessageHelper.formatProfileRelic(implHelper.getUnitImpl().checkRelicLevel(message.getAllycode(), relicLevel), relicLevel, message.getAuthor()));
        } catch (final InsertionError | RetrieveError | HttpRetrieveError exception) {
            message.error(exception.getMessage());
        }
    }

    /**
     * Inserts a profile for the user.
     *
     * @param playerData the player data.
     * @param guild      the guild.
     * @return a player object.
     * @throws InsertionError When the player object couldn't be inserted into the DB.
     * @throws RetrieveError  When the created object couldn't be retrieved from the DB.
     */
    public Player insertProfile(final JSONObject playerData, final Guild guild) throws InsertionError, RetrieveError {
        logger.debug(className, "Inserting player in the DB");
        final int allycode = playerData.getInt("ally_code");
        final String name = playerData.getString("name");
        final int galacticPower = playerData.getInt("galactic_power");
        final String url = SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + playerData.getString("url").substring(1, playerData.getString("url").length() - 1);
        final LocalDateTime lastUpdated = StringHelper.getCurrentDateTime();
        final LocalDateTime lastUpdatedSwgoh = StringHelper.parseSwgohDate(playerData.getString("last_updated"));
        Player player = dao.playerDao().getById(allycode);
        if (player == null) {
            player = new Player(allycode, name);
        }
        if (playerData.has("guild_id")) {
            player.setGuild(dao.guildDao().getById(playerData.getInt("guild_id")));
        } else {
            player.setGuild(guild);
        }
        if (playerData.has("guild_name")) {
            player.setGuildName(playerData.getString("guild_name"));
        }
        player.setGalacticPower(galacticPower);
        player.setUrl(url);
        player.setName(name);
        player.setLastUpdated(lastUpdated);
        player.setLastUpdatedSwgoh(lastUpdatedSwgoh);
        this.dao.playerDao().save(player);
        return dao.playerDao().getById(allycode);
    }

    /**
     * Get's the speedmods for an user.
     *
     * @param message the message.
     */
    public void speedMods(final Message message) {
        try {
            final JSONObject modData = httpHelper.getJsonObject(String.format(SwgohGgEndpoint.MOD_ENDPOINT.getUrl(), message.getAllycode()));
            final JSONArray mods = modData.getJSONArray("mods");
            int garbage = 0;
            int plus10 = 0;
            int plus15 = 0;
            int plus20 = 0;
            int plus25 = 0;
            for (int i = 0; i < mods.length(); i++) {
                final JSONObject mod = mods.getJSONObject(i);
                final JSONArray secondaryStats = mod.getJSONArray("secondary_stats");
                for (int j = 0; j < secondaryStats.length(); j++) {
                    final JSONObject secondaryStat = secondaryStats.getJSONObject(j);
                    if ("Speed".equals(secondaryStat.getString("name"))) {
                        final int speed = Integer.parseInt(secondaryStat.getString("display_value"));
                        final int speed10 = 10;
                        final int speed15 = 15;
                        final int speed20 = 20;
                        final int speed25 = 25;
                        if (speed < speed10) {
                            garbage++;
                        } else if (speed < speed15) {
                            plus10++;
                        } else if (speed < speed20) {
                            plus15++;
                        } else if (speed < speed25) {
                            plus20++;
                        } else {
                            plus25++;
                        }
                    }
                }
            }
            message.done(MessageHelper.formatModMessage(garbage, plus10, plus15, plus20, plus25));
        } catch (final HttpRetrieveError exception) {
            message.error(exception.getMessage());
        }
    }
}
