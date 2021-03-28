package nl.djj.swgoh_bot_v2.commandImpl;

import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

public class ControlImpl {
    private final transient Logger logger;
    private final transient DatabaseHandler dbHandler;

    public ControlImpl(final Logger logger, final DatabaseHandler dbHandler) {
        super();
        this.dbHandler = dbHandler;
        this.logger = logger;
    }

    public void createDatabase(Message message) {
        dbHandler.createDatabase();
        message.getChannel().sendMessage("Database updated").queue();
    }
}
