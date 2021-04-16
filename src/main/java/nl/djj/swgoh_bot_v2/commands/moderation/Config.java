package nl.djj.swgoh_bot_v2.commands.moderation;

import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.HashMap;

/**
 * @author DJJ
 */
public class Config extends BaseCommand {
    private static final transient String FLAG_SET_GUILD = "setSwgohGuild";
    private static final transient String FLAG_GET_CONFIG = "getConfig";
    private static final transient String FLAG_SET_PREFIX = "setPrefix";
    private static final transient String FLAG_SET_IGNORE_ROLE = "setIgnoreRole";
    private static final transient String FLAG_SET_NOTIFY_CHANNEL = "setNotifyChannel";

    /**
     * Constructor.
     *
     * @param logger     the logger.
     * @param implHelper the implHelper.
     */
    public Config(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        name = "config";
        requiredLevel = Permission.MODERATOR;
        description = "All Bot config related tasks";
        aliases = new String[]{
                "config"
        };
        category = CommandCategory.MODERATION;
        flags = new HashMap<>();
        flagRequired = true;
    }

    @Override
    public void createFlags() {
        flags.put(FLAG_SET_GUILD, new Flag(FLAG_SET_GUILD, "Sets the SWGOH guild id for the discord Guild", name, FLAG_SET_GUILD, "<guildId>"));
        flags.put(FLAG_GET_CONFIG, new Flag(FLAG_GET_CONFIG, "Gets the config for the discord Guild", name, FLAG_GET_CONFIG));
        flags.put(FLAG_SET_PREFIX, new Flag(FLAG_SET_PREFIX, "Sets the prefix for the current guild", name, FLAG_SET_PREFIX, "<prefix>"));
        flags.put(FLAG_SET_IGNORE_ROLE, new Flag(FLAG_SET_IGNORE_ROLE, "Sets the ignore role for the bot presence functionality.", name, FLAG_SET_IGNORE_ROLE, "<@RoleTag>"));
        flags.put(FLAG_SET_NOTIFY_CHANNEL, new Flag(FLAG_SET_NOTIFY_CHANNEL, "Sets the notify channel for the bot", name, FLAG_SET_NOTIFY_CHANNEL, "<#ChannelTag>"));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_SET_GUILD -> this.implHelper.getConfigImpl().setGuildId(message);
            case FLAG_GET_CONFIG -> this.implHelper.getConfigImpl().showConfig(message);
            case FLAG_SET_PREFIX -> this.implHelper.getConfigImpl().setPrefix(message);
            case FLAG_SET_IGNORE_ROLE -> this.implHelper.getConfigImpl().setIgnoreRole(message);
            case FLAG_SET_NOTIFY_CHANNEL -> this.implHelper.getConfigImpl().setNotifyChannel(message);
            default -> message.error("This is not a valid flag, use '" + message.getGuildPrefix() + "help " + name + "'");
        }
    }
}