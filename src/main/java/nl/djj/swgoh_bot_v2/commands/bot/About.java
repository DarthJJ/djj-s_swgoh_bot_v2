package nl.djj.swgoh_bot_v2.commands.bot;

import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.MessageHelper;

/**
 * @author DJJ
 **/
public class About extends BaseCommand {

    /**
     * Constructor.
     *
     * @param logger     the logger.
     * @param implHelper the implHelper.
     **/
    public About(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
    }

    @Override
    public void createFlags() {
            //Not applicable for this command.
    }

    @Override
    public void handleMessage(final Message message) {
        final int noOfServers = this.implHelper.getConfigImpl().
        message.done(MessageHelper.formatAboutMessage());
    }
}
