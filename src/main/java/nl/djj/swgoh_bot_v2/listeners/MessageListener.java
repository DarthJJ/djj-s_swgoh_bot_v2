package nl.djj.swgoh_bot_v2.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.database.Database;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.CommandHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.PermissionHelper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author DJJ
 */
public class MessageListener extends ListenerAdapter {
    private final transient Logger logger;
    private final transient CommandHelper commands;
    private final transient PermissionHelper permissionHelper;
    private final transient Database database;

    /**
     * Constructor.
     *
     * @param logger           the logger to use.
     * @param commands         the helper for the commands.
     * @param permissionHelper the helper for permissions.
     * @param database the DB connection.
     */
    public MessageListener(final Logger logger, final Database database, final CommandHelper commands, final PermissionHelper permissionHelper) {
        super();
        this.logger = logger;
        this.commands = commands;
        this.permissionHelper = permissionHelper;
        this.database = database;
    }

    /**
     * Fired when an message is received.
     *
     * @param event the message event.
     */
    //CHECKSTYLE.OFF: NPathComplexityCheck //TODO: remove this/ simplify this
    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getMessage().getMentionedUsers().size() > 0 && event.getMessage().getMentionedUsers().get(0).getId().equals(event.getJDA().getSelfUser().getId())) {
            event.getMessage().getChannel().sendMessage("My prefix is '!#'").queue(); //TODO: get prefix for guild
            return;
        }
        if (event.getAuthor().isBot() || !event.getMessage().getContentDisplay().startsWith("!#")) { //TODO: Get prefix for guild
            return;
        }
        //TODO: make this nicer.
        String messageContent = event.getMessage().getContentDisplay();
        messageContent = messageContent.replace("!#", "");
        final List<String> args =  new LinkedList<>(Arrays.asList(messageContent.split(" ")));
        final String commandName = args.get(0);
        args.remove(0);
        final BaseCommand command = commands.getCommand(commandName);
        if (command == null) {
            event.getChannel().sendMessage("This command doesn't exist").queue();
            return;
        }
        if (command.isFlagRequired() && args.size() < 1) {
            event.getChannel().sendMessage("Missing flags for this command").queue();
            return;
        }
        final String flag = args.get(0);
        args.remove(0);
        final Message message = new Message(event.getAuthor().getId(), event.getAuthor().getName(), command.getName(), flag, args, 1, event.getChannel());
        if (permissionHelper.isAllowed(message, command.getRequiredLevel())) {
            logger.command(message);
            command.handleMessage(message);
        }
        //CHECKSTYLE.ON: NPathComplexityCheck
    }
}
