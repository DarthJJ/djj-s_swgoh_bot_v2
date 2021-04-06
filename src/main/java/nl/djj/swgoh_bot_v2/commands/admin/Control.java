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
    private static final transient String FLAG_ENABLE_MAINTENANCE = "enableMaintenance";
    private static final transient String FLAG_DISABLE_MAINTENANCE = "disableMaintenance";

    /**
     * Constructor.
     *
     * @param logger     the logger to use.
     * @param implHelper the DB connection.
     */
    public Control(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        name = "control";
        requiredLevel = Permission.ADMINISTRATOR;
        description = "All the admin control commands of the bot, aka the danger-zone";
        aliases = new String[]{
                "ctrl"
        };
        category = CommandCategory.ADMIN;
        flags = new HashMap<>();
        flagRequired = true;
    }

    @Override
    public void createFlags() {
        flags.put(FLAG_ENABLE, new Flag(FLAG_ENABLE, "Enables a command", name, FLAG_ENABLE, " <command name>"));
        flags.put(FLAG_DISABLE, new Flag(FLAG_DISABLE, "Disables a command", name, FLAG_DISABLE, " <command name>"));
        flags.put(FLAG_UPDATE_DB, new Flag(FLAG_UPDATE_DB, "Updates the DB", name, FLAG_UPDATE_DB));
        flags.put(FLAG_SHUTDOWN, new Flag(FLAG_SHUTDOWN, "Shuts down the bot, be careful, you can't restart via Discord", name, FLAG_SHUTDOWN));
        flags.put(FLAG_ENABLE_MAINTENANCE, new Flag(FLAG_ENABLE_MAINTENANCE, "Puts the bot in maintenance mode", name, FLAG_ENABLE_MAINTENANCE));
        flags.put(FLAG_DISABLE_MAINTENANCE, new Flag(FLAG_DISABLE_MAINTENANCE, "Puts the bot in normal mode", name, FLAG_DISABLE_MAINTENANCE));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_ENABLE -> this.implHelper.getCommandImpl().enableCommand(message);
            case FLAG_DISABLE -> this.implHelper.getCommandImpl().disableCommand(message);
            case FLAG_UPDATE_DB -> this.implHelper.getControlImpl().createDatabase(message);
            case FLAG_ENABLE_MAINTENANCE -> this.implHelper.getControlImpl().enableUpdateMode(message);
            case FLAG_DISABLE_MAINTENANCE -> this.implHelper.getControlImpl().disableUpdateMode(message);
            case FLAG_SHUTDOWN -> this.implHelper.getControlImpl().closeBot();
            default -> message.error("This is not a valid flag, use '" + message.getGuildPrefix() + " help " + name + "'");
        }
    }
}
