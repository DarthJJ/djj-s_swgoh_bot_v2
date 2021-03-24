package nl.djj.swgoh_bot_v2.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.CommandHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.PermissionHelper;

/**
 * @author DJJ
 */
public class MessageListener extends ListenerAdapter {
    private final transient Logger logger;
    private final transient String className = this.getClass().getSimpleName();
    private final CommandHelper commands;
    private final PermissionHelper permissionHelper;

    /**
     * Constructor.
     */
    public MessageListener(final Logger logger, final CommandHelper commands, final PermissionHelper permissionHelper) {
        super();
        this.logger = logger;
        this.commands = commands;
        this.permissionHelper = permissionHelper;
    }

    /**
     * Fired when an message is received.
     *
     * @param event the message event.
     */
    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getMessage().getMentionedUsers().size() > 0 && event.getMessage().getMentionedUsers().get(0).getId().equals(event.getJDA().getSelfUser().getId())) {
            event.getMessage().getChannel().sendMessage("My prefix is '!#'").queue(); //TODO: get prefix for guild
            return;
        }
        if (event.getAuthor().isBot() || !event.getMessage().getContentDisplay().startsWith("!#")) {//TODO: Get prefix for guild
            return;
        }
        //TODO: make this nicer.
        String messageContent = event.getMessage().getContentDisplay();
        messageContent = messageContent.replace("!#", "");
        final String flag = messageContent.split(" ")[0];
        messageContent = messageContent.replace(flag, "");
        final BaseCommand command = commands.getCommand(flag);
        final Message message = new Message(event.getAuthor().getId(), messageContent.split(" "), 1, event.getChannel());
        if (permissionHelper.isAllowed(message, command.getRequiredLevel())) {
            command.handleMessage(message);
        }
    }
}
