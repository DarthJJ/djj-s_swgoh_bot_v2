package nl.djj.swgoh_bot_v2.commands.swgoh;

import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class Guild extends BaseCommand {
    private static final transient String FLAG_GENERIC = "generic";

    /**
     * Creates a SWGOH guild object.
     *
     * @param logger     the logger.
     * @param implHelper the implHelper.
     */
    public Guild(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        NAME = "guild";
        REQUIRED_LEVEL = Permission.USER;
        DESCRIPTION = "All SWGOH guild related commands";
        ALIASES = new String[]{
                "gg"
        };
        CATEGORY = CommandCategory.SWGOH;
        FLAG_REQUIRED = true;
        createFlags();
    }

    @Override
    public void createFlags() {
        FLAGS.put(FLAG_GENERIC, new Flag(FLAG_GENERIC, "Fetches the SWGOH guild for the user", NAME, FLAG_GENERIC));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_GENERIC -> this.implHelper.getGuildImpl().genericInfo(message);
            default -> message.error("This is not a valid flag, use '" + message.getGuildPrefix() + " help " + NAME + "'");
        }
    }
}
