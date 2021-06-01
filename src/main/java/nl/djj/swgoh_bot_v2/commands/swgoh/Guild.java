package nl.djj.swgoh_bot_v2.commands.swgoh;

import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.enums.CommandCategory;
import nl.djj.swgoh_bot_v2.config.enums.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class Guild extends BaseCommand {
    private static final transient String FLAG_GP = "gp";
    private static final transient String FLAG_RELIC = "relic";
    private static final transient String FLAG_COMPARE = "compare";
    private static final transient String FLAG_GL = "gl";
    private static final transient String FLAG_UNIT = "unit";

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
        flags.put(FLAG_GP, new Flag(FLAG_GP, "Sorts al the members based on GP", true, name, FLAG_GP));
        flags.put(FLAG_RELIC, new Flag(FLAG_RELIC, "Shows all members with the amount of toons below or at the given relic level", true, name, FLAG_RELIC, " <relicLevel>"));
        flags.put(FLAG_COMPARE, new Flag(FLAG_COMPARE, "Compares 1 guild to another", true, name, FLAG_COMPARE, "<guildId/allycode>"));
        flags.put(FLAG_GL, new Flag(FLAG_GL, "Gets the GL status of the guild", true, name, FLAG_GL, "<GL>"));
        flags.put(FLAG_UNIT, new Flag(FLAG_UNIT, "Gets an overview of the guild for the given unit", true, name, FLAG_UNIT, "<unitId, name, abbreviation>"));

    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_GP -> this.implHelper.getGuildImpl().gpOverview(message);
            case FLAG_RELIC -> this.implHelper.getGuildImpl().relicOverview(message);
            case FLAG_COMPARE -> this.implHelper.getGuildImpl().compare(message);
            case FLAG_GL -> this.implHelper.getGuildImpl().glOverview(message);
            case FLAG_UNIT -> this.implHelper.getGuildImpl().unitOverview(message);
            default -> unknownFlag(message);
        }
    }
}
