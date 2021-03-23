package nl.djj.swgoh_bot_v2.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * @author DJJ
 */
public class MessageListener extends ListenerAdapter {

    /**
     * Constructor.
     */
    public MessageListener() {
        super();
    }

    /**
     * Fired when an message is received.
     * @param event the message event.
     */
    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        System.out.println(event.getMessage());
    }
}
