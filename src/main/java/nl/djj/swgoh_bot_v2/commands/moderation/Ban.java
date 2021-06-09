package nl.djj.swgoh_bot_v2.commands.moderation;

import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.enums.CommandCategory;
import nl.djj.swgoh_bot_v2.config.enums.Permission;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 **/
public class Ban extends BaseCommand {
    private static final transient String FLAG_ADD = "add";
    private static final transient String FLAG_REMOVE = "remove";
    private static final transient String FLAG_STATUS = "status";

    /**
     * Constructor.
     *
     * @param logger     the logger.
     * @param implHelper the implHelper.
     **/
    public Ban(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        name = "ban";
        requiredLevel = Permission.MODERATOR;
        description = "Tools for (un-)banning users from the bot";
        aliases = new String[]{
                "ban"
        };
        category = CommandCategory.MODERATION;
        flagRequired = true;
    }

    @Override
    public void createFlags() {
        flags.p
    }

    @Override
    public void handleMessage(final Message message) {

    }
}
