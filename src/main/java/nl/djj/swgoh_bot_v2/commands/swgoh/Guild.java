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
    private static final transient String FLAG_GP = "gp";

    /**
     * Creates a SWGOH guild object.
     *
     * @param logger     the logger.
     * @param implHelper the implHelper.
     */
    public Guild(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        name = "guild";
        requiredLevel = Permission.USER;
        description = "All SWGOH guild related commands";
        aliases = new String[]{
                "gg"
        };
        category = CommandCategory.SWGOH;
        flagRequired = true;
    }

    @Override
    public void createFlags() {
        flags.put(FLAG_GENERIC, new Flag(FLAG_GENERIC, "Fetches the SWGOH guild for the user", name, FLAG_GENERIC));
        flags.put(FLAG_GP, new Flag(FLAG_GP, "Sorts al the members based on GP", name, FLAG_GP));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_GENERIC -> this.implHelper.getGuildImpl().genericInfo(message);
            case FLAG_GP -> this.implHelper.getGuildImpl().gpOverview(message);
            default -> message.error("This is not a valid flag, use '" + message.getGuildPrefix() + " help " + name + "'");
        }
    }
}
