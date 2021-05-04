package nl.djj.swgoh_bot_v2.commands;

import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author DJJ
 */
public abstract class BaseCommand {

    //CHECKSTYLE.OFF: VisibilityModifierCheck
    protected final transient Logger logger;
    protected final transient ImplHelper implHelper;
    protected transient String name;
    protected transient Permission requiredLevel;
    protected transient String description;
    protected transient String[] aliases;
    protected transient CommandCategory category;
    protected transient Map<String, Flag> flags;
    protected transient boolean flagRequired;
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
        this.flags = new TreeMap<>();
    }

    /**
     * Called when an unknown flag is passed to the command.
     *
     * @param message the message.
     */
    public void unknownFlag(final Message message) {
        message.error("This is not a valid flag, please use '**" + message.getGuildPrefix() + "help " + name + "**'");
    }

    public void missingRegistration(final Message message) {
        message.error("You need to be registered with the bot to use this command. please use: '**" + message.getGuildPrefix() + "help register**'");
    }

    /**
     * @return command name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return list of aliases for the command
     */
    public String[] getAliases() {
        return Arrays.copyOf(aliases, aliases.length);
    }

    /**
     * @return required level.
     */
    public Permission getRequiredLevel() {
        return requiredLevel;
    }

    /**
     * @return command description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return category.
     */
    public CommandCategory getCategory() {
        return category;
    }

    /**
     * @return flags of the command.
     */
    public Map<String, Flag> getFlags() {
        return flags;
    }

    /**
     * @return required.
     */
    public boolean isFlagRequired() {
        return flagRequired;
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
