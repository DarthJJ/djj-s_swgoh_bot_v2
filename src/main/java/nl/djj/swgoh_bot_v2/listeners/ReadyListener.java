package nl.djj.swgoh_bot_v2.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

/**
 * @author DJJ
 */
public class ReadyListener implements EventListener {

    /**
     * Constructor.
     */
    public ReadyListener() {
        super();
    }

    /**
     * Called when an event is fired.
     * @param event the event fired.
     */
    @Override
    public void onEvent(final GenericEvent event) {
        if (event instanceof ReadyEvent) {
            System.out.println("API Ready");
        }
    }
}
