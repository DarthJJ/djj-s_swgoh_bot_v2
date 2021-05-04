package nl.djj.swgoh_bot_v2.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import nl.djj.swgoh_bot_v2.Main;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class ReadyListener implements EventListener {
    private final transient Logger logger;
    private final transient String className = this.getClass().getSimpleName();
    /**
     * Constructor.
     */
    public ReadyListener() {
        super();
        this.logger = Main.getLogger();
    }

    /**
     * Called when an event is fired.
     * @param event the event fired.
     */
    @Override
    public void onEvent(final GenericEvent event) {
        if (event instanceof ReadyEvent) {
            logger.info(className, "API Ready");
        }
    }
}
