package nl.djj.swgoh_bot_v2.listeners;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.CommandLoader;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author DJJ
 */
public class EventListener extends ListenerAdapter {
    private final transient Logger logger;
    private final transient CommandLoader commands;
    private final transient ImplHelper implHelper;

    /**
     * Constructor.
     *
     * @param logger     the logger to use.
     * @param commands   the helper for the commands.
     * @param implHelper the helper for command impl.
     */
    public EventListener(final Logger logger, final CommandLoader commands, final ImplHelper implHelper) {
        super();
        this.logger = logger;
        this.commands = commands;
        this.implHelper = implHelper;
    }

    /**
     * Fired when an message is received.
     *
     * @param event the message event.
     */
    //CHECKSTYLE.OFF: NPathComplexityCheck //TODO: remove this/ simplify this
    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        final String guildPrefix = implHelper.getConfigImpl().getPrefix(event.getGuild().getId());
        if (event.getMessage().getMentionedUsers().size() > 0 && event.getMessage().getMentionedUsers().get(0).getId().equals(event.getJDA().getSelfUser().getId())) {
            event.getMessage().getChannel().sendMessage("My prefix is '" + guildPrefix + "'").queue();
            return;
        }
        if (event.getAuthor().isBot() || !event.getMessage().getContentDisplay().startsWith(guildPrefix)) {
            return;
        }

        final Message message = Message.initFromEvent(event, guildPrefix);
        message.working();
        final BaseCommand command = commands.getCommand(message.getCommand(), event.getAuthor().getId().equals(BotConstants.OWNER_ID));
        if (command == null) {
            message.error("This command doesn't exist, please use: '" + guildPrefix + " help");
            return;
        }
        if (command.isFlagRequired() && message.getFlag().isEmpty()) {
            message.error("Missing flags for this command, use the help function: " + guildPrefix + "help " + command.getName());
            return;
        }

        if (implHelper.getProfileImpl().isAllowed(message.getAuthorId(), command.getRequiredLevel())) {
            logger.command(message);
            command.handleMessage(message);
        } else {
            logger.permission(message);
            message.error("You are not allowed to do this. naughty boy");
        }
        //CHECKSTYLE.ON: NPathComplexityCheck
    }

    @Override
    public void onUserUpdateOnlineStatus(final @NotNull UserUpdateOnlineStatusEvent event) {
        super.onUserUpdateOnlineStatus(event);
        if (event.getNewOnlineStatus() == OnlineStatus.OFFLINE || event.getNewOnlineStatus() == OnlineStatus.IDLE) {
            return;
        }
        implHelper.getProfileImpl().updatePresence(event.getGuild().getId(), event.getMember().getId(), event.getMember().getEffectiveName(), event.getMember().getRoles());
    }
}
