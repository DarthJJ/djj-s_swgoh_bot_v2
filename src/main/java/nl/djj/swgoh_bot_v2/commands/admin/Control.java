package nl.djj.swgoh_bot_v2.commands.admin;

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
public class Control extends BaseCommand {
    private static final transient String FLAG_ENABLE = "enable";
    private static final transient String FLAG_DISABLE = "disable";
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
        flagRequired = true;
    }

    @Override
    public void createFlags() {
        flags.put(FLAG_ENABLE, new Flag(FLAG_ENABLE, "Enables a command", true, name, FLAG_ENABLE, " <command name>"));
        flags.put(FLAG_DISABLE, new Flag(FLAG_DISABLE, "Disables a command", true, name, FLAG_DISABLE, " <command name>"));
        flags.put(FLAG_ENABLE_MAINTENANCE, new Flag(FLAG_ENABLE_MAINTENANCE, "Puts the bot in maintenance mode", true, name, FLAG_ENABLE_MAINTENANCE));
        flags.put(FLAG_DISABLE_MAINTENANCE, new Flag(FLAG_DISABLE_MAINTENANCE, "Puts the bot in normal mode", true, name, FLAG_DISABLE_MAINTENANCE));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_ENABLE -> this.implHelper.getCommandImpl().enableCommand(message);
            case FLAG_DISABLE -> this.implHelper.getCommandImpl().disableCommand(message);
            case FLAG_ENABLE_MAINTENANCE -> this.implHelper.getControlImpl().enableUpdateMode(message);
            case FLAG_DISABLE_MAINTENANCE -> this.implHelper.getControlImpl().disableUpdateMode(message);
            default -> unknownFlag(message);
        }
    }
}
