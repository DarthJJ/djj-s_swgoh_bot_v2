package nl.djj.swgoh_bot_v2.commands.admin;

import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.database.ControlHandler;
import nl.djj.swgoh_bot_v2.database.HandlerInterface;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DJJ
 */
public class Control extends BaseCommand {
    private static final String NAME = "Control";
    private static final Permission REQUIRED_LEVEL = Permission.ADMINISTRATOR;
    private static final String DESCRIPTION = "All the admin cintrol commands of the bot, aka the danger-zone";
    private static final String[] ALIASES = {
            "up"
    };
    private static final CommandCategory CATEGORY = CommandCategory.ADMIN;
    private static final Map<String, Flag> FLAGS = new HashMap<>();
    private boolean enabled;
    private static final boolean FLAG_REQUIRED = true;
    private static final transient String FLAG_ENABLE = "enable";
    private static final transient String FLAG_DISABLE = "disable";

    /**
     * Constructor.
     *
     * @param logger         the logger to use.
     * @param controlHandler the DB connection.
     */
    public Control(final Logger logger, final HandlerInterface controlHandler) {
        super(logger, controlHandler);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getAliases() {
        return Arrays.copyOf(ALIASES, ALIASES.length);
    }

    @Override
    public Permission getRequiredLevel() {
        return REQUIRED_LEVEL;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public CommandCategory getCategory() {
        return CATEGORY;
    }

    @Override
    public Map<String, Flag> getFlags() {
        return FLAGS;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(final boolean isEnabled) {
        this.enabled = isEnabled;
    }

    @Override
    public boolean isFlagRequired() {
        return FLAG_REQUIRED;
    }

    @Override
    public void createFlags() {
        final Flag enable = new Flag(FLAG_ENABLE, "Enables a command", "usage: " + NAME + " " + FLAG_ENABLE + " <command name>");
        FLAGS.put(FLAG_ENABLE, enable);
        final Flag disable = new Flag(FLAG_DISABLE, "Disables a command", "usage: " + NAME + " " + FLAG_DISABLE + " <command name>");
        FLAGS.put(FLAG_DISABLE, disable);
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_ENABLE -> {
                final String enableCommand = String.join(", ", message.getArgs());
                if (((ControlHandler) this.dbHandler).enableCommand(enableCommand)) {
                    message.getChannel().sendMessage("Command: " + enableCommand + " is successfully enabled").queue();
                    return;
                }
                message.getChannel().sendMessage("Something went wrong, check the logs").queue();
            }
            case FLAG_DISABLE -> {
                final String disableCommand = String.join(", ", message.getArgs());
                if (((ControlHandler) this.dbHandler).disableCommand(disableCommand)) {
                    message.getChannel().sendMessage("Command: " + disableCommand + " is successfully disabled").queue();
                    return;
                }
                message.getChannel().sendMessage("Something went wrong, check the logs").queue();
            }
            default -> message.getChannel().sendMessage("This flag doesn't exist").queue();
        }
    }
}
