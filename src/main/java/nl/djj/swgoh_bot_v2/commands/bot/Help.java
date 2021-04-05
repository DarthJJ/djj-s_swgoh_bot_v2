package nl.djj.swgoh_bot_v2.commands.bot;

import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.HashMap;

/**
 * @author DJJ
 */
public abstract class Help extends BaseCommand {

    /**
     * Constructor.
     *
     * @param logger     the logger.
     * @param implHelper the implHelper.
     */
    public Help(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        NAME = "help";
        REQUIRED_LEVEL = Permission.USER;
        DESCRIPTION = "Register or unregister with the bot.";
        ALIASES = new String[]{
                "reg"
        };
        CATEGORY = CommandCategory.BOT;
        FLAGS = new HashMap<>();
        FLAG_REQUIRED = false;
        createFlags();
    }

    @Override
    public void createFlags() {
        //Not Needed
    }

    @Override
    public void handleMessage(final Message message) {
        handleRequest(message);
    }

    /**
     * Called to handle the help message.
     *
     * @param message the help message.
     */
    public abstract void handleRequest(final Message message);
}
