package nl.djj.swgoh_bot_v2.commands;

import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DJJ
 */
public class Ping extends BaseCommand {
    private static final String NAME = "Ping";
    private static final int REQUIRED_LEVEL = 2;
    private static final String DESCRIPTION = "This an example description";
    private static final CommandCategory CATEGORY = CommandCategory.BOT;
    private static final Map<String, Flag> FLAGS = new HashMap<>();
    private boolean enabled;

    /**
     * Constructor.
     *
     */
    public Ping() {
        super();
    }

    @Override
    public void handleMessage(final Message message){
        message.getChannel().sendMessage("pong!").queue();
    }

    @Override
    public void setEnabled(final boolean enabled){
        this.enabled = enabled;
    }

    @Override
    public void createFlags() {
        final Flag ping = new Flag(NAME, "Pongs to your ping", "When needed it pings back");
        FLAGS.put(NAME, ping);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getRequiredLevel() {
        return REQUIRED_LEVEL;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getCategory() {
        return CATEGORY.getName();
    }

    @Override
    public Map<String, Flag> getFlags() {
        return FLAGS;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
