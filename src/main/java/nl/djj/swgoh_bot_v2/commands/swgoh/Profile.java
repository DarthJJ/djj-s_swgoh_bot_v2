package nl.djj.swgoh_bot_v2.commands.swgoh;

import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
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
public class Profile extends BaseCommand {
    private static final String NAME = "profile";
    private static final Permission REQUIRED_LEVEL = Permission.USER;
    private static final String DESCRIPTION = "All swgoh profile related commands";
    private static final String[] ALIASES = {
            "pr"
    };
    private static final CommandCategory CATEGORY = CommandCategory.SWGOH;
    private static final Map<String, Flag> FLAGS = new HashMap<>();
    private boolean enabled;
    private static final boolean FLAG_REQUIRED = true;
    private static final transient String FLAG_GENERIC = "generic";
    private static final transient String FLAG_ARENA = "arena";
    private static final transient String FLAG_RELIC = "relic";

    /**
     * Creates a SWGOH profile object.
     *
     * @param logger     the logger.
     * @param implHelper the implHelper.
     */
    public Profile(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
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
        FLAGS.put(FLAG_GENERIC, new Flag(FLAG_GENERIC, "Fetches the SWGOH profile for the user", NAME, FLAG_GENERIC));
        FLAGS.put(FLAG_ARENA, new Flag(FLAG_ARENA, "Fetches the SWGOH Arena information for the user", NAME, FLAG_ARENA));
        FLAGS.put(FLAG_RELIC, new Flag(FLAG_RELIC, "Get's relic information about the users roster. optional parameter for level select", NAME, FLAG_RELIC, "-f <level> (inclusive)"));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_GENERIC -> this.implHelper.getProfileImpl().genericInfo(message);
            case FLAG_ARENA -> this.implHelper.getProfileImpl().toonArena(message);
            case FLAG_RELIC ->  this.implHelper.getProfileImpl().relic(message);
            default -> message.error("This is not a valid flag, use '" + message.getGuildPrefix() + " help " + NAME + "'");
        }
    }
}
