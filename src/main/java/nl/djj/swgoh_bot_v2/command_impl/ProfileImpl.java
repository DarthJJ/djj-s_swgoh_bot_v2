package nl.djj.swgoh_bot_v2.command_impl;

import net.dv8tion.jda.api.entities.Role;
import nl.djj.swgoh_bot_v2.config.Config;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.SwgohProfile;
import nl.djj.swgoh_bot_v2.entities.User;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLDeletionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.MessageHelper;
import nl.djj.swgoh_bot_v2.helpers.StringHelper;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * @author DJJ
 */
public class ProfileImpl {
    private final transient HttpHelper httpHelper;
    private final transient DatabaseHandler dbHandler;
    private final transient ImplHelper implHelper;

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
        if (!StringHelper.validateAllycode(allycode) || allycode.isEmpty()) {
            message.getChannel().sendMessage("Allycode validation errror, syntax: <xxx-xxx-xxx>").queue();
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
        if (discordId.equals(Config.OWNER_ID)) {
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
        final User user;
        try {
            user = dbHandler.getByDiscordId(message.getAuthorId());
        } catch (final SQLRetrieveError error) {
            message.error(error.getMessage());
            return;
        }
        try {
            final JSONObject playerData = httpHelper.getJsonObject(SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + user.getAllycode());
            final JSONObject playerProfile = playerData.getJSONObject("data");
            message.done(MessageHelper.formatSwgohProfile(SwgohProfile.initFromJson(playerProfile, implHelper.getUnitImpl())));
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Gets the toon arena info.
     *
     * @param message the message.
     */
    public void toonArena(final Message message) {
        final User user;
        try {
            user = dbHandler.getByDiscordId(message.getAuthorId());
        } catch (final SQLRetrieveError error) {
            message.error(error.getMessage());
            return;
        }
        try {
            final JSONObject playerData = httpHelper.getJsonObject(SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + user.getAllycode());
            final JSONObject playerProfile = playerData.getJSONObject("data");
            message.done(MessageHelper.formatArenaProfile(SwgohProfile.initFromJson(playerProfile, implHelper.getUnitImpl())));
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Updates the presence of user.
     * @param guildId the guild.
     * @param userId the userId.
     * @param username the username.
     * @param roles the roles of the user.
     */
    public void updatePresence(final String guildId, final String userId, final String username, final List<Role> roles) {
        final String guildIgnoreRole = implHelper.getConfigImpl().getIgnoreRole(guildId);
        for (final Role role : roles) {
            if (role.getName().toLowerCase(Locale.ENGLISH).equals(guildIgnoreRole.toLowerCase(Locale.ROOT))) {
                return;
            }
        }
        dbHandler.updatePresence(userId, username, StringHelper.getCurrentDateTime());
    }
}
