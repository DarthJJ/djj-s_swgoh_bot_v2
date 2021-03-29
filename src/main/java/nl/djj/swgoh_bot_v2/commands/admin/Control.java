package nl.djj.swgoh_bot_v2.commands.admin;

import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
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
    private static final String DESCRIPTION = "All the admin control commands of the bot, aka the danger-zone";
    private static final String[] ALIASES = {
            "up"
    };
    private static final CommandCategory CATEGORY = CommandCategory.ADMIN;
    private static final Map<String, Flag> FLAGS = new HashMap<>();
    private boolean enabled;
    private static final boolean FLAG_REQUIRED = true;
    private static final transient String FLAG_ENABLE = "enable";
    private static final transient String FLAG_DISABLE = "disable";
    private static final transient String FLAG_UPDATE_DB = "updateDb";

    /**
     * Constructor.
     *
     * @param logger     the logger to use.
     * @param implHelper the DB connection.
     */
    public Control(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
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
        final Flag enable = new Flag(FLAG_ENABLE, "Enables a command", NAME, FLAG_ENABLE, " <command name>");
        FLAGS.put(FLAG_ENABLE, enable);
        final Flag disable = new Flag(FLAG_DISABLE, "Disables a command", NAME, FLAG_DISABLE, " <command name>");
        FLAGS.put(FLAG_DISABLE, disable);
        final Flag updateDb = new Flag(FLAG_UPDATE_DB, "Updates the DB", NAME, FLAG_UPDATE_DB);
        FLAGS.put(FLAG_UPDATE_DB, updateDb);
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_ENABLE -> this.implHelper.getCommandImpl().enableCommand(message);
            case FLAG_DISABLE -> this.implHelper.getCommandImpl().disableCommand(message);
            case FLAG_UPDATE_DB -> this.implHelper.getControlImpl().createDatabase(message);
            default -> message.getChannel().sendMessage("This flag doesn't exist").queue();
        }
    }
}
