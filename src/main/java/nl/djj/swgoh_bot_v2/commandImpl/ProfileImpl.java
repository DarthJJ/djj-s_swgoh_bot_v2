package nl.djj.swgoh_bot_v2.commandImpl;

import nl.djj.swgoh_bot_v2.config.Config;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.User;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLDeletionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.StringHelper;

public class ProfileImpl {
    private final transient HttpHelper httpHelper;
    private final transient DatabaseHandler dbHandler;

    public ProfileImpl(final Logger logger, final DatabaseHandler dbHandler) {
        super();
        this.dbHandler = dbHandler;
        this.httpHelper = new HttpHelper(logger);
    }

    private User getByDiscordId(final String discordId) {
        return this.dbHandler.getByDiscordId(discordId);
    }

    public void registerUser(final Message message) {
        if (isUserRegistered(message.getAuthorId())) {
            message.getChannel().sendMessage("You are already registered").queue();
            return;
        }
        final String allycode = String.join(", ", message.getArgs()).replace("-", "");
        if (!StringHelper.validateAllycode(allycode) || "".equals(allycode)) {
            message.getChannel().sendMessage("Allycode validation errror, syntax: <xxx-xxx-xxx>").queue();
            return;
        }
        try {
            this.httpHelper.getJsonObject(SwgohGgEndpoint.PLAYER_ENDPOINT.getUrl() + allycode);
            this.dbHandler.insertUser(new User(allycode, Permission.USER.getLevel(), message.getAuthor(), message.getAuthorId()));
            message.getChannel().sendMessage("You are successfully registered").queue();
        } catch (final SQLInsertionError error) {
            message.getChannel().sendMessage("Something went wrong in the DB, please contact the bot Dev").queue();
        } catch (final HttpRetrieveError error) {
            message.getChannel().sendMessage("No profile found on SWGOH.gg with allycode: " + allycode).queue();
        }

    }

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
        final User user = getByDiscordId(discordId);
        if (user == null) {
            if (requiredLevel == Permission.USER) {
                return true;
            }
        } else if (user.getPermission().getLevel() <= requiredLevel.getLevel()) {
            return true;
        }
        return false;
    }
}
