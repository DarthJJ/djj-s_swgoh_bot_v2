package nl.djj.swgoh_bot_v2.command_impl;

import net.dv8tion.jda.api.entities.Role;
import nl.djj.swgoh_bot_v2.config.*;
import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.compare.GLUnit;
import nl.djj.swgoh_bot_v2.entities.compare.PlayerGLStatus;
import nl.djj.swgoh_bot_v2.entities.db.GLRequirement;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.entities.db.Presence;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;
import nl.djj.swgoh_bot_v2.helpers.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author DJJ
 */
public class ProfileImpl {
    private final transient HttpHelper httpHelper;
    private final transient DAO dao;
    private final transient ImplHelper implHelper;
    private final transient Logger logger;
    private final transient String className = this.getClass().getName();

    /**
     * @param logger     the logger.
     * @param implHelper the ImplHelper.
     */
    public ProfileImpl(final Logger logger, final DAO dao, final ImplHelper implHelper) {
        super();
        this.dao = dao;
        this.logger = logger;
        this.httpHelper = new HttpHelper(logger);
        this.implHelper = implHelper;
    }

    public int getAllycodeByDiscord(final String discordId) {
        try {
            return this.dao.playerDao().getByDiscordId(discordId).getAllycode();
        } catch (RetrieveError retrieveError) {
            return -1;
        }
    }

    private int getAndUpdateProfileData(final Message message, final int swgohIdentifier) throws InsertionError, RetrieveError, HttpRetrieveError {
        final Player user;
        final int swgohId;
        if (swgohIdentifier == -1) {
            user = dao.playerDao().getById(message.getAllycode());
            swgohId = user.getAllycode();
        } else {
            swgohId = swgohIdentifier;
        }
        final JSONObject playerData = httpHelper.getJsonObject(SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + swgohId);
        final JSONObject playerInfo = playerData.getJSONObject("data");
        final JSONArray playerUnits = playerData.getJSONArray("units");
        final Player player = insertProfile(playerInfo);
        this.implHelper.getUnitImpl().insertUnits(playerUnits, player);
        return swgohId;
    }

    /**
     * Register a user based on the message.
     *
     * @param message the message.
     */
    public void registerUser(final Message message) {
        try {
            if (dao.playerDao().exists(message.getAllycode())) {
                message.getChannel().sendMessage("You are already registered").queue();
                return;
            }
            final int allycode = Integer.parseInt(String.join(", ", message.getArgs()).replace("-", ""));
            if (StringHelper.isInvalidAllycode(String.valueOf(allycode))) {
                message.getChannel().sendMessage("Allycode validation error, syntax: <xxx-xxx-xxx>").queue();
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
            return dao.playerDao().getByDiscordId(discordId).getPermission().getLevel() <= requiredLevel.getLevel();
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
            final int swgohId = getAndUpdateProfileData(message, -1);
            final int rivalId = getAndUpdateProfileData(null, Integer.parseInt(message.getArgs().get(0)));
//            message.done(MessageHelper.formatProfileCompare(implHelper.getUnitImpl().compareProfiles(playerData, rivalData))); //TODO: implement
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
            final int allycode = getAndUpdateProfileData(message, -1);
            final GalacticLegends glEvent = GalacticLegends.getByKey(message.getArgs().get(0));
            final List<GLRequirement> requirements = dao.glRequirementDao().getForEvent(glEvent.getName());
            message.done(MessageHelper.formatPlayerGLStatus(getGlStatus(glEvent.getName(), dao.playerDao().getById(allycode), requirements)));
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
    public PlayerGLStatus getGlStatus(final String eventName, final Player player, final List<GLRequirement> requirements) throws RetrieveError {
        final List<GLUnit> compares = new ArrayList<>();
        double totalCompletion = 0.0;
        for (final GLRequirement requirement : requirements) {
            final PlayerUnit unit = dao.playerUnitDao().getForPlayer(player, requirement.getBaseId());
            final long zetas = unit.getAbilities().stream().filter(unitAbility -> unitAbility.getBaseAbility().isZeta() && unitAbility.getBaseAbility().getTierMax() == unitAbility.getLevel()).count();
            final GLUnit compare = new GLUnit(unit.getUnit().getName(), unit.getGear(), unit.getGearPieces(), unit.getRelic(), unit.getRarity(), (int) zetas);
            compare.setCompleteness(CalculationHelper.calculateCompletion(unit.getRarity(), compare.getGearLevel(), compare.getRelicLevel(), unit.getGearPieces(),
                    SwgohConstants.MAX_RARITY_LEVEL, requirement.getRelicLevel(), requirement.getGearLevel()));
            totalCompletion += compare.getCompleteness();
            compares.add(compare);
        }
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
     * @throws InsertionError When the player object couldn't be inserted into the DB.
     * @throws RetrieveError  When the created object couldn't be retrieved from the DB.
     */
    public Player insertProfile(final JSONObject playerData) throws InsertionError, RetrieveError {
        final int allycode = playerData.getInt("ally_code");
        final String name = playerData.getString("name");
        final int galacticPower = playerData.getInt("galactic_power");
        final String url = SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + playerData.getString("url");
        final LocalDateTime lastUpdated = StringHelper.getCurrentDateTime();
        final LocalDateTime lastUpdatedSwgoh = StringHelper.parseSwgohDate(playerData.getString("last_updated"));
        final Player player = dao.playerDao().getById(allycode);
        player.setGalacticPower(galacticPower);
        player.setUrl(url);
        player.setName(name);
        player.setLastUpdated(lastUpdated);
        player.setLastUpdatedSwgoh(lastUpdatedSwgoh);
        this.dao.playerDao().save(player);
        return dao.playerDao().getById(allycode);
    }
}
