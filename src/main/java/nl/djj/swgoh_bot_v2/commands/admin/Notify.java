package nl.djj.swgoh_bot_v2.commands.admin;

import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 **/
public class Notify extends BaseCommand {
    private static final transient String FLAG_MESSAGE = "message";

    /**
     * Constructor.
     *
     * @param logger     the logger.
     * @param implHelper the implHelper.
     **/
    public Notify(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        name = "notify";
        requiredLevel = Permission.ADMINISTRATOR;
        description = "Allows for notifying all guilds";
        aliases = new String[]{
                "nt"
        };
        category = CommandCategory.ADMIN;
        flagRequired = true;
    }

    @Override
    public void createFlags() {
        flags.put(FLAG_MESSAGE, new Flag(FLAG_MESSAGE, "Sends a message to the guilds in the notify channel", true, name, FLAG_MESSAGE, "<message>"));
    }

    @Override
    public void handleMessage(final Message message) {
//        if (FLAG_MESSAGE.equals(message.getFlag())) {
//            this.implHelper.getControlImpl().sendMessage(message, String.join(" ", message.getArgs()));
//        } else {
//            message.error("this is not a valid flag, use '" + message.getGuildPrefix() + "help " + name + "'");
//        }
    }
}
