package nl.djj.swgoh_bot_v2.commands.bot;

import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 **/
public class Report extends BaseCommand {
    private static final transient String FLAG_CREATE = "create";
    private static final transient String FLAG_STATUS = "status";
    private static final transient String FLAG_DISALLOW = "disallow";

    /**
     * Constructor.
     *
     * @param implHelper the implHelper.
     * @param logger     the logger.
     **/
    public Report(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        name = "report";
        requiredLevel = Permission.USER;
        description = "Creates an issue or retrieves the implementation status.";
        aliases = new String[]{
                "rep"
        };
        category = CommandCategory.BOT;
        flagRequired = true;
    }

    @Override
    public void createFlags() {
        flags.put(FLAG_CREATE, new Flag(FLAG_CREATE, "Creates an issue on github", true, name, FLAG_CREATE, "<ticket description>"));
        flags.put(FLAG_STATUS, new Flag(FLAG_STATUS, "Gets the status of an earlier created issue", true, name, FLAG_STATUS, "<ticketNumber>"));
        flags.put(FLAG_DISALLOW, new Flag(FLAG_DISALLOW, "**MODERATOR ONLY** Disallows the tagged user of creating tickets", true, name, FLAG_DISALLOW, "<userTag>"));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_CREATE -> this.implHelper.getReportImpl().createIssue(message);
            case FLAG_STATUS -> this.implHelper.getReportImpl().issueStatus(message);
            case FLAG_DISALLOW -> this.implHelper.getReportImpl().disallowUser(message);
            default -> unknownFlag(message);
        }
    }
}
