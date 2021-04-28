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
public class Profile extends BaseCommand {
    private static final transient String FLAG_GENERIC = "generic";
    private static final transient String FLAG_RELIC = "relic";
    private static final transient String FLAG_COMPARE = "compare";
    private static final transient String FLAG_GL = "gl";

    /**
     * Creates a SWGOH profile object.
     *
     * @param logger     the logger.
     * @param implHelper the implHelper.
     */
    public Profile(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        name = "profile";
        requiredLevel = Permission.USER;
        description = "All SWGOH profile related commands";
        aliases = new String[]{
                "pr"
        };
        category = CommandCategory.SWGOH;
        flagRequired = true;
    }

    @Override
    public void createFlags() {
        flags.put(FLAG_GENERIC, new Flag(FLAG_GENERIC, "Fetches the SWGOH profile for the user", name, FLAG_GENERIC));
        flags.put(FLAG_RELIC, new Flag(FLAG_RELIC, "Get's relic information about the users roster. optional parameter for level select", name, FLAG_RELIC, "<level> (inclusive)"));
        flags.put(FLAG_COMPARE, new Flag(FLAG_COMPARE, "Compares your profile to the given allycode", name, FLAG_COMPARE, "<allycode>"));
        flags.put(FLAG_GL, new Flag(FLAG_GL, "Gets the status for the given GL", name, FLAG_GL, "<glName>"));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_GENERIC -> this.implHelper.getProfileImpl().genericInfo(message);
            case FLAG_RELIC -> this.implHelper.getProfileImpl().relic(message);
            case FLAG_COMPARE ->  this.implHelper.getProfileImpl().compare(message);
            case FLAG_GL -> this.implHelper.getProfileImpl().glStatus(message);
            default -> unknownFlag(message);
        }
    }
}
