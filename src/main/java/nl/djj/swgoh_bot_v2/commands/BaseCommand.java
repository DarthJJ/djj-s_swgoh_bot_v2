package nl.djj.swgoh_bot_v2.commands;

import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
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
public abstract class BaseCommand {

    //CHECKSTYLE.OFF: VisibilityModifierCheck
    protected final transient Logger logger;
    protected final transient ImplHelper implHelper;
    protected transient String NAME;
    protected transient Permission REQUIRED_LEVEL;
    protected transient String DESCRIPTION;
    protected transient String[] ALIASES;
    protected transient CommandCategory CATEGORY;
    protected transient Map<String, Flag> FLAGS;
    protected boolean enabled;
    protected boolean FLAG_REQUIRED;
    //CHECKSTYLE.ON: VisibilityModifierCheck

    /**
     * Constructor.
     *
     * @param logger     the logger to use;
     * @param implHelper the DB to use;
     */
    public BaseCommand(final Logger logger, final ImplHelper implHelper) {
        super();
        this.logger = logger;
        this.implHelper = implHelper;
        this.FLAGS = new HashMap<>();
    }

    /**
     * @return command name.
     */
    public String getName() {
        return NAME;
    }

    /**
     * @return list of aliases for the command
     */
    public String[] getAliases() {
        return Arrays.copyOf(ALIASES, ALIASES.length);
    }

    /**
     * @return required level.
     */
    public Permission getRequiredLevel() {
        return REQUIRED_LEVEL;
    }

    /**
     * @return command description.
     */
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * @return category.
     */
    public CommandCategory getCategory() {
        return CATEGORY;
    }

    /**
     * @return flags of the command.
     */
    public Map<String, Flag> getFlags() {
        return FLAGS;
    }

    /**
     * @return enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the command enabled or not.
     *
     * @param isEnabled boolean value.
     */
    public void setEnabled(final boolean isEnabled) {
        this.enabled = isEnabled;
    }

    /**
     * @return required.
     */
    public boolean isFlagRequired() {
        return FLAG_REQUIRED;
    }

    /**
     * Creates the flags for the command.
     */
    public abstract void createFlags();

    /**
     * Handles the command.
     *
     * @param message the message to handle.
     */
    public abstract void handleMessage(final Message message);
}
