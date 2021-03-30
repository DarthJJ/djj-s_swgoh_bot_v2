package nl.djj.swgoh_bot_v2.commands.bot;

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
public abstract class Help extends BaseCommand {
    private static final String NAME = "Help";
    private static final Permission REQUIRED_LEVEL = Permission.USER;
    private static final String DESCRIPTION = "Register or unregister with the bot.";
    private static final String[] ALIASES = {
            "reg"
    };
    private static final CommandCategory CATEGORY = CommandCategory.BOT;
    private static final Map<String, Flag> FLAGS = new HashMap<>();
    private boolean enabled;
    private static final boolean FLAG_REQUIRED = false;

    /**
     * Constructor.
     * @param logger the logger.
     * @param implHelper the implHelper.
     */
    public Help(final Logger logger, final ImplHelper implHelper) {
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
        //Not needed for this command
    }

    @Override
    public void handleMessage(final Message message) {
        handleRequest(message);
    }

    /**
     * Called to handle the help message.
     * @param message the help message.
     */
    public abstract void handleRequest(final Message message);
}
