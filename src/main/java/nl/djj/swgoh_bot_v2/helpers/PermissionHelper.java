package nl.djj.swgoh_bot_v2.helpers;

import nl.djj.swgoh_bot_v2.config.Config;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.database.UserHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.User;

/**
 * @author DJJ
 */
public class PermissionHelper {
    private final transient UserHandler userHandler;
    private final transient Logger logger;

    /**
     * Constructor.
     *
     * @param userHandler the userHandler.
     * @param logger      the logger.
     */
    public PermissionHelper(final UserHandler userHandler, final Logger logger) {
        super();
        this.userHandler = userHandler;
        this.logger = logger;
    }

    /**
     * @param message       the message with the command.
     * @param requiredLevel the level required for the command.
     * @return if it is allowed.
     */
    public boolean isAllowed(final Message message, final Permission requiredLevel) {
        if (message.getAuthorId().equals(Config.OWNER_ID)) {
            return true;
        }
        final User user = userHandler.getByDiscordId(message.getAuthorId());
        if (user == null) {
            if (requiredLevel == Permission.USER) {
                return true;
            }
        } else if (user.getPermission().getLevel() <= requiredLevel.getLevel()) {
            return true;
        }

        message.getChannel().sendMessage("You are not allowed to execute this command").queue();
        logger.permission(message);
        return false;
    }
}
