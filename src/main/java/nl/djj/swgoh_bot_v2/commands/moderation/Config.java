package nl.djj.swgoh_bot_v2.commands.moderation;

import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.HashMap;

public class Config extends BaseCommand {
    private static final transient String FLAG_SET_GUILD = "setSwgohGuild";
    private static final transient String FLAG_GET_CONFIG = "getConfig";
    private static final transient String FLAG_SET_PREFIX = "setPrefix";

    public Config(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        NAME = "config";
        REQUIRED_LEVEL = Permission.MODERATOR;
        DESCRIPTION = "All Bot config related tasks";
        ALIASES = new String[]{
                "config"
        };
        CATEGORY = CommandCategory.MODERATION;
        FLAGS = new HashMap<>();
        FLAG_REQUIRED = true;
        createFlags();
    }

    @Override
    public void createFlags() {
        FLAGS.put(FLAG_SET_GUILD, new Flag(FLAG_SET_GUILD, "Sets the SWGOH guild id for the discord Guild", NAME, FLAG_SET_GUILD, "<guildId>"));
        FLAGS.put(FLAG_GET_CONFIG, new Flag(FLAG_GET_CONFIG, "Gets the config for the discord Guild", NAME, FLAG_GET_CONFIG));
        FLAGS.put(FLAG_SET_PREFIX, new Flag(FLAG_SET_PREFIX, "Sets the prefix for the current guild", NAME, FLAG_SET_PREFIX, "<prefix>"));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_SET_GUILD -> this.implHelper.getConfigImpl().setGuildId(message);
            case FLAG_GET_CONFIG -> this.implHelper.getConfigImpl().getConfig(message);
            case FLAG_SET_PREFIX -> this.implHelper.getConfigImpl().setPrefix(message);
            default -> message.error("This is not a valid flag, use '" + message.getGuildPrefix() + "help " + NAME + "'");
        }
    }
}
