package nl.djj.swgoh_bot_v2.commands.bot;

import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

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
        name = "help";
        requiredLevel = Permission.USER;
        description = "Register or unregister with the bot.";
        aliases = new String[]{
                "reg"
        };
        category = CommandCategory.BOT;
        flagRequired = false;
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
