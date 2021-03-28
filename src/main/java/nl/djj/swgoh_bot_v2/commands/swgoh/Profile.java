package nl.djj.swgoh_bot_v2.commands.swgoh;

import nl.djj.swgoh_bot_v2.commandImpl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.HashMap;
import java.util.Map;

public class Profile extends BaseCommand {
    private static final String NAME = "Profile";
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

    public Profile(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Permission getRequiredLevel() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public CommandCategory getCategory() {
        return null;
    }

    @Override
    public Map<String, Flag> getFlags() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean isEnabled) {

    }

    @Override
    public boolean isFlagRequired() {
        return false;
    }

    @Override
    public void createFlags() {

    }

    @Override
    public void handleMessage(Message message) {

    }
}
