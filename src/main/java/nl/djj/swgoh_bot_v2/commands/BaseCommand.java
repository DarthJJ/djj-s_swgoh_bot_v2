package nl.djj.swgoh_bot_v2.commands;

import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;

import java.util.Map;

/**
 * @author DJJ
 */
public abstract class BaseCommand {
    /**
     * @return command name.
     */
    public abstract String getName();

    /**
     * @return required level.
     */
    public abstract int getRequiredLevel();

    /**
     * @return command description.
     */
    public abstract String getDescription();

    /**
     * @return category.
     */
    public abstract String getCategory();

    /**
     * @return flags of the command.
     */
    public abstract Map<String, Flag> getFlags();

    /**
     * @return enabled.
     */
    public abstract boolean isEnabled();

    /**
     * Sets the command enabled or not.
     */
    public abstract void setEnabled(final boolean isEnabled);

    /**
     * Creates the flags for the command.
     */
    public abstract void createFlags();

    /**
     * Handles the command;
     */
    public abstract void handleMessage(final Message message);

    /**
     * Constructor.
     */
    public BaseCommand() {
        super();
    }
}
