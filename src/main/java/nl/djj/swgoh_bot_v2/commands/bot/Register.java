package nl.djj.swgoh_bot_v2.commands.bot;

import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.database.HandlerInterface;
import nl.djj.swgoh_bot_v2.database.UserHandler;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.exceptions.ValidationError;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DJJ
 */
public class Register extends BaseCommand {
    private static final String NAME = "Register";
    private static final Permission REQUIRED_LEVEL = Permission.USER;
    private static final String DESCRIPTION = "Register or unregister with the bot.";
    private static final String[] ALIASES = {
            "reg"
    };
    private static final CommandCategory CATEGORY = CommandCategory.BOT;
    private static final Map<String, Flag> FLAGS = new HashMap<>();
    private boolean enabled;
    private static final boolean FLAG_REQUIRED = true;
    private static final transient String FLAG_ADD = "add";
    private static final transient String FLAG_REMOVE = "remove";

    /**
     * Constructor.
     *
     * @param logger          the logger.
     * @param registerHandler the DB connection.
     */
    public Register(final Logger logger, final HandlerInterface registerHandler) {
        super(logger, registerHandler);
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
        final Flag add = new Flag(FLAG_ADD, "Register to the bot", "usage: " + NAME + " " + FLAG_ADD + " <allycode: xxx-xxx-xxx / xxxxxxxxx>");
        FLAGS.put(add.getName(), add);
        final Flag remove = new Flag(FLAG_REMOVE, "Unregister to the bot", "usage: " + NAME + " " + FLAG_REMOVE);
        FLAGS.put(remove.getName(), remove);
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_ADD:
                registerUser(message);
                break;
            case FLAG_REMOVE:
                break;
            default:
                message.getChannel().sendMessage("No valid flag was passed").queue();
                break;
        }
    }

    private void registerUser(final Message message) {
        if (((UserHandler) this.dbHandler).isRegistered(message.getAuthorId())) {
            message.getChannel().sendMessage("You are already registered").queue();
            return;
        }
        try {
            final String allycode = String.join(", ", message.getArgs()).replace("-", "");
            if ("".equals(allycode)) {
                message.getChannel().sendMessage("Missing allycode").queue();
                return;
            }
            ((UserHandler) this.dbHandler).registerUser(message.getAuthor(), message.getAuthorId(), allycode);
            message.getChannel().sendMessage("You are successfully registered").queue();
        } catch (final ValidationError | HttpRetrieveError | SQLInsertionError error) {
            message.getChannel().sendMessage(error.getMessage()).queue();
        }
    }
}
