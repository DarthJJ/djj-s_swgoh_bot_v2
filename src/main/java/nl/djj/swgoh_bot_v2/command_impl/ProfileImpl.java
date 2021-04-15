package nl.djj.swgoh_bot_v2.command_impl;

import net.dv8tion.jda.api.entities.Role;
import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.swgoh.SwgohProfile;
import nl.djj.swgoh_bot_v2.entities.Unit;
import nl.djj.swgoh_bot_v2.entities.db.User;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLDeletionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.MessageHelper;
import nl.djj.swgoh_bot_v2.helpers.StringHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author DJJ
 */
public class ProfileImpl {
    private final transient HttpHelper httpHelper;
    private final transient DatabaseHandler dbHandler;
    private final transient ImplHelper implHelper;
    private static final String KEY_DATA = "data";
    private static final String KEY_UNITS = "units";

    /**
     * @param logger     the logger.
     * @param dbHandler  the DB handler.
     * @param implHelper the ImplHelper.
     */
    public ProfileImpl(final Logger logger, final DatabaseHandler dbHandler, final ImplHelper implHelper) {
        super();
        this.dbHandler = dbHandler;
        this.httpHelper = new HttpHelper(logger);
        this.implHelper = implHelper;
    }

    private JSONObject getProfileData(final Message message) {
        final User user;
        try {
            user = dbHandler.getByDiscordId(message.getAuthorId());
        } catch (final SQLRetrieveError error) {
            message.error(error.getMessage());
            return null;
        }
        try {
            return httpHelper.getJsonObject(SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + user.getAllycode());
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
            return null;
        }
    }

    /**
     * Gets the profile data for the given allycode.
     * @param allycode the allycode.
     * @return the profile data as JSON.
     */
    public JSONObject getProfileData(final String allycode) {
        try {
            return httpHelper.getJsonObject(SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + allycode);
        } catch (final HttpRetrieveError error) {
            return null;
        }
    }

    /**
     * Register a user based on the message.
     *
     * @param message the message.
     */
    public void registerUser(final Message message) {
        if (dbHandler.isUserRegistered(message.getAuthorId())) {
            message.getChannel().sendMessage("You are already registered").queue();
            return;
        }
        final String allycode = String.join(", ", message.getArgs()).replace("-", "");
        if (StringHelper.isInvalidAllycode(allycode) || allycode.isEmpty()) {
            message.getChannel().sendMessage("Allycode validation error, syntax: <xxx-xxx-xxx>").queue();
            return;
        }
        try {
            this.httpHelper.getJsonObject(SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + allycode);
            this.dbHandler.insertUser(new User(allycode, Permission.USER, message.getAuthor(), message.getAuthorId()));
            message.getChannel().sendMessage("You are successfully registered").queue();
        } catch (final SQLInsertionError error) {
            message.getChannel().sendMessage("Something went wrong in the DB, please contact the bot Dev").queue();
        } catch (final HttpRetrieveError error) {
            message.getChannel().sendMessage("No profile found on SWGOH.gg with allycode: " + allycode).queue();
        }

    }

    /**
     * Unregister a user.
     *
     * @param message the message.
     */
    public void unregisterUser(final Message message) {
        if (dbHandler.isUserRegistered(message.getAuthorId())) {
            try {
                this.dbHandler.removeUser(message.getAuthorId());
                message.getChannel().sendMessage("You are now unregistered").queue();
            } catch (final SQLDeletionError error) {
                message.getChannel().sendMessage("Something went wrong in the DB, please contact the bot Dev").queue();
            }
        } else {
            message.getChannel().sendMessage("There is no registration for you").queue();
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
            final User user = dbHandler.getByDiscordId(discordId);
            if (user != null) {
                return user.getPermission().getLevel() <= requiredLevel.getLevel();
            }
            return requiredLevel == Permission.USER;
        } catch (final SQLRetrieveError error) {
            return requiredLevel == Permission.USER;
        }
    }

    /**
     * gets the generic info profile.
     *
     * @param message the message.
     */
    public void genericInfo(final Message message) {
        final JSONObject playerData = getProfileData(message);
        if (playerData == null) {
            return;
        }
        final JSONObject playerProfile = playerData.getJSONObject(KEY_DATA);
        message.done(MessageHelper.formatSwgohProfile(SwgohProfile.initFromJson(playerProfile)));
    }

    /**
     * Compares the user's profile to the given allycode.
     *
     * @param message the message.
     */
    public void compare(final Message message) {
        final JSONObject playerData = getProfileData(message);
        if (playerData == null) {
            return;
        }
        if (message.getArgs().isEmpty()) {
            message.error("Please provide an allycode to compare with");
            return;
        }
        if (StringHelper.isInvalidAllycode(message.getArgs().get(0))) {
            message.getChannel().sendMessage("Allycode validation error, syntax: <xxx-xxx-xxx>").queue();
            return;
        }
        final JSONObject rivalData;
        try {
            rivalData = httpHelper.getJsonObject(SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + message.getArgs().get(0));
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
            return;
        }
        message.done(MessageHelper.formatProfileCompare(implHelper.getUnitImpl().compareProfiles(playerData, rivalData)));
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
        dbHandler.updatePresence(userId, username, StringHelper.getCurrentDateTimeAsString());
    }

    /**
     * Checks the users relics.
     *
     * @param message the message Object.
     */
    //CHECKSTYLE.OFF: NPathComplexityCheck
    public void relic(final Message message) {
        final JSONObject playerData = getProfileData(message);
        if (playerData == null) {
            return;
        }
        final SwgohProfile profile = SwgohProfile.initFromJson(playerData.getJSONObject(KEY_DATA));
        final JSONArray unitsData = playerData.getJSONArray(KEY_UNITS);
        final List<Unit> units = new ArrayList<>();
        for (int i = 0; i < unitsData.length(); i++) {
            final JSONObject unitData = unitsData.getJSONObject(i);
            units.add(Unit.initFromJson(unitData.getJSONObject(KEY_DATA)));
        }
        profile.setUnits(units);
        int relicLevel = SwgohConstants.MAX_RELIC_TIER;
        try {
            if (!message.getArgs().isEmpty()) {
                relicLevel = Integer.parseInt(String.join("", message.getArgs()));
            }
        } catch (final NumberFormatException exception) {
            message.error("Relic number is not valid, please use a valid number between '1' and '" + SwgohConstants.MAX_RELIC_TIER + "'");
            return;
        }
        message.done(MessageHelper.formatProfileRelic(implHelper.getUnitImpl().checkRelicLevel(profile.getUnits(), relicLevel), relicLevel, profile.getName()));
    }

    /**
     * Inserts a profile for the user.
     * @param playerData the player data.
     * @param guildId the guild of the user.
     * @throws SQLInsertionError when something goes wrong.
     */
    public void insertProfile(final JSONObject playerData, final int guildId) throws SQLInsertionError{
        final int allycode = playerData.getInt("ally_code");
        final String name = playerData.getString("name");
        final int galacticPower = playerData.getInt("galactic_power");
        final String url = SwgohGgEndpoint.ENDPOINT + playerData.getString("url");
        final String lastUpdated = playerData.getString("last_updated");
        this.dbHandler.insertPlayer(new Player(allycode, name, galacticPower, url, lastUpdated, guildId));

    }
    //CHECKSTYLE.ON: NPathComplexityCheck
}
