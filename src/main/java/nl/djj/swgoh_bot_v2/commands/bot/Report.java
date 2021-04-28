package nl.djj.swgoh_bot_v2.commands.bot;

import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
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
        flags.put(FLAG_CREATE, new Flag(FLAG_CREATE, "Creates an issue on github", name, FLAG_CREATE, "<ticket description>"));
        flags.put(FLAG_STATUS, new Flag(FLAG_STATUS, "Gets the status of an earlier created issue", name, FLAG_STATUS, "<ticketNumber>"));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_CREATE -> this.implHelper.getReportImpl().createIssue(message);
            case FLAG_STATUS -> this.implHelper.getReportImpl().issueStatus(message);
            default -> unknownFlag(message);
        }
    }
}
