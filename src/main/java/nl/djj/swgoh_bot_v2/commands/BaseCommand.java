package nl.djj.swgoh_bot_v2.commands;

import nl.djj.swgoh_bot_v2.commandImpl.ImplHelper;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.Map;

/**
 * @author DJJ
 */
public abstract class BaseCommand {

    //CHECKSTYLE.OFF: VisibilityModifierCheck
    protected final transient Logger logger;
    protected final transient ImplHelper implHelper;
    //CHECKSTYLE.ON: VisibilityModifierCheck

    /**
     * Constructor.
     *
     * @param logger   the logger to use;
     * @param implHelper the DB to use;
     */
    public BaseCommand(final Logger logger, final ImplHelper implHelper) {
        super();
        this.logger = logger;
        this.implHelper = implHelper;
    }

    /**
     * @return command name.
     */
    public abstract String getName();

    /**
     * @return list of aliases for the command
     */
    public abstract String[] getAliases();

    /**
     * @return required level.
     */
    public abstract Permission getRequiredLevel();

    /**
     * @return command description.
     */
    public abstract String getDescription();

    /**
     * @return category.
     */
    public abstract CommandCategory getCategory();

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
     *
     * @param isEnabled boolean value.
     */
    public abstract void setEnabled(final boolean isEnabled);

    /**
     * @return required.
     */
    public abstract boolean isFlagRequired();

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
