package nl.djj.swgoh_bot_v2.commands.bot;

import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class Register extends BaseCommand {
    private static final transient String FLAG_ADD = "add";
    private static final transient String FLAG_REMOVE = "remove";

    /**
     * Constructor.
     *
     * @param logger     the logger.
     * @param implHelper the DB connection.
     */
    public Register(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        name = "register";
        requiredLevel = Permission.USER;
        description = "Register or unregister with the bot.";
        aliases = new String[]{
                "reg"
        };
        category = CommandCategory.BOT;
        flagRequired = true;
    }

    @Override
    public void createFlags() {
        flags.put(FLAG_ADD, new Flag(FLAG_ADD, "Register to the bot", false, name, FLAG_ADD, " <allycode: xxx-xxx-xxx / xxxxxxxxx>"));
        flags.put(FLAG_REMOVE, new Flag(FLAG_REMOVE, "Unregister from the bot", true, name, FLAG_REMOVE));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_ADD -> this.implHelper.getProfileImpl().registerUser(message);
            case FLAG_REMOVE -> this.implHelper.getProfileImpl().unregisterUser(message);
            default -> unknownFlag(message);
        }
    }
}
