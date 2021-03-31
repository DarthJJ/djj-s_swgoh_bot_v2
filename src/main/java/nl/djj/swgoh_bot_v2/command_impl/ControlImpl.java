package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public abstract class ControlImpl {
    private final transient String className = this.getClass().getSimpleName();
    private final transient Logger logger;
    private final transient DatabaseHandler dbHandler;

    /**
     * @param logger the logger.
     * @param dbHandler the DB handler.
     */
    public ControlImpl(final Logger logger, final DatabaseHandler dbHandler) {
        super();
        this.dbHandler = dbHandler;
        this.logger = logger;
    }

    /**
     * Create / update the database.
     * @param message the message.
     */
    public void createDatabase(final Message message) {
        dbHandler.createDatabase();
        logger.info(className, "Updating / Creating the DB");
        message.done("Database has been updated!");
    }

    /**
     * overridden for bot closure.
     */
    public abstract void closeBot();
}
