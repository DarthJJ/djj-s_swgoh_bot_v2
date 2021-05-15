package nl.djj.swgoh_bot_v2.commands.swgoh;

import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
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
    private static final transient String FLAG_RELIC = "relic";
    private static final transient String FLAG_COMPARE = "compare";
    private static final transient String FLAG_GL = "gl";
    private static final transient String FLAG_SPEED_MODS = "speedmods";

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
        flags.put(FLAG_RELIC, new Flag(FLAG_RELIC, "Gets relic information about the users roster. optional parameter for level select", true, name, FLAG_RELIC, "<level> (inclusive)"));
        flags.put(FLAG_COMPARE, new Flag(FLAG_COMPARE, "Compares your profile to the given allycode", true, name, FLAG_COMPARE, "<allycode>"));
        flags.put(FLAG_GL, new Flag(FLAG_GL, "Gets the status for the given GL", true, name, FLAG_GL, "<glName>"));
        flags.put(FLAG_SPEED_MODS, new Flag(FLAG_SPEED_MODS, "Gets an overview of the speedmods equipped on your toons", true, name, FLAG_SPEED_MODS));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_RELIC -> this.implHelper.getProfileImpl().relic(message);
            case FLAG_COMPARE -> this.implHelper.getProfileImpl().compare(message);
            case FLAG_GL -> this.implHelper.getProfileImpl().glStatus(message);
            case FLAG_SPEED_MODS -> this.implHelper.getProfileImpl().speedMods(message);
            default -> unknownFlag(message);
        }
    }
}
