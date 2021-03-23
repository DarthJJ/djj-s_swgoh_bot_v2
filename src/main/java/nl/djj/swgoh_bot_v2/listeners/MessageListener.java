package nl.djj.swgoh_bot_v2.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.CommandLoader;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class MessageListener extends ListenerAdapter {
    private final transient Logger logger;
    private final transient String className = this.getClass().getSimpleName();
    private final CommandLoader commands;
    /**
     * Constructor.
     */
    public MessageListener(final Logger logger, final CommandLoader commands) {
        super();
        this.logger = logger;
        this.commands = commands;
    }

    /**
     * Fired when an message is received.
     * @param event the message event.
     */
    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.getMessage().getContentDisplay().startsWith("!#")) {
            return;
        }
        //TODO: make this nicer.
        String message = event.getMessage().getContentDisplay();
        message = message.replace("!#", "");
        final String flag = message.split(" ")[0];
        message = message.replace(flag, "");
        final BaseCommand command = commands.getCommand(flag);
        command.handleMessage(new Message(event.getAuthor().getId(), message.split(" "), 1, event.getChannel() ));
    }
}
