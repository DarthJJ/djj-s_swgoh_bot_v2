package nl.djj.swgoh_bot_v2.helpers;

import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.database.UserHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.User;

public class PermissionHelper {
    final UserHandler userHandler;

    public PermissionHelper(final UserHandler userHandler) {
        super();
        this.userHandler = userHandler;
    }

    public boolean isAllowed(final Message message, final Permission requiredLevel) {
        final User user = userHandler.getByDiscordId(message.getAuthor());
        if (user.getPermission().getLevel() <= requiredLevel.getLevel()){
            return true;
        }

        message.getChannel().sendMessage("You are not allowed to execute this command").queue();
        return false;
    }
}
