package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.Config;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.User;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLDeletionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.StringHelper;

/**
 * @author DJJ
 */
public class ProfileImpl {
    private final transient HttpHelper httpHelper;
    private final transient DatabaseHandler dbHandler;

    /**
     * @param logger    the logger.
     * @param dbHandler the DB handler.
     */
    public ProfileImpl(final Logger logger, final DatabaseHandler dbHandler) {
        super();
        this.dbHandler = dbHandler;
        this.httpHelper = new HttpHelper(logger);
    }

    /**
     * Register a user based on the message.
     *
     * @param message the message.
     */
    public void registerUser(final Message message) {
        if (isUserRegistered(message.getAuthorId())) {
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
        if (isUserRegistered(message.getAuthorId())) {
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


    private boolean isUserRegistered(final String discordId) {
        return this.dbHandler.isUserRegistered(discordId);
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
}
