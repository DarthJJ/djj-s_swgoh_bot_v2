package nl.djj.swgoh_bot_v2.commands.admin;

import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.HashMap;

/**
 * @author DJJ
 */
public class Control extends BaseCommand {
    private static final transient String FLAG_ENABLE = "enable";
    private static final transient String FLAG_DISABLE = "disable";
    private static final transient String FLAG_UPDATE_DB = "updateDb";
    private static final transient String FLAG_SHUTDOWN = "shutdown";

    /**
     * Constructor.
     *
     * @param logger     the logger to use.
     * @param implHelper the DB connection.
     */
    public Control(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        NAME = "control";
        REQUIRED_LEVEL = Permission.ADMINISTRATOR;
        DESCRIPTION = "All the admin control commands of the bot, aka the danger-zone";
        ALIASES = new String[]{
                "ctrl"
        };
        CATEGORY = CommandCategory.ADMIN;
        FLAGS = new HashMap<>();
        FLAG_REQUIRED = true;
        createFlags();
    }

    @Override
    public void createFlags() {
        FLAGS.put(FLAG_ENABLE, new Flag(FLAG_ENABLE, "Enables a command", NAME, FLAG_ENABLE, " <command name>"));
        FLAGS.put(FLAG_DISABLE, new Flag(FLAG_DISABLE, "Disables a command", NAME, FLAG_DISABLE, " <command name>"));
        FLAGS.put(FLAG_UPDATE_DB, new Flag(FLAG_UPDATE_DB, "Updates the DB", NAME, FLAG_UPDATE_DB));
        FLAGS.put(FLAG_SHUTDOWN, new Flag(FLAG_SHUTDOWN, "Shuts down the bot, be careful, you can't restart via Discord", NAME, FLAG_SHUTDOWN));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_ENABLE -> this.implHelper.getCommandImpl().enableCommand(message);
            case FLAG_DISABLE -> this.implHelper.getCommandImpl().disableCommand(message);
            case FLAG_UPDATE_DB -> this.implHelper.getControlImpl().createDatabase(message);
            case FLAG_SHUTDOWN -> this.implHelper.getControlImpl().closeBot();
            default -> message.error("This is not a valid flag, use '" + message.getGuildPrefix() + " help " + NAME + "'");
        }
    }
}
