package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class CommandImpl {
    private final transient String className = this.getClass().getSimpleName();
    private final transient Logger logger;
    private final transient DatabaseHandler dbHandler;

    /**
     * @param logger the logger.
     * @param dbHandler DB handler.
     */
    public CommandImpl(final Logger logger, final DatabaseHandler dbHandler) {
        super();
        this.logger = logger;
        this.dbHandler = dbHandler;
    }

    /**
     * Retrieves the enabled status for a command.
     * @param commandName the command.
     * @return boolean value.
     */
    public boolean getCommandEnabledStatus(final String commandName) {
        return dbHandler.getCommandEnabledStatus(commandName);
    }

    /**
     * Enable the command.
     * @param message the message.
     */
    public void enableCommand(final Message message) {
        final String enableCommand = String.join(", ", message.getArgs());
        try {
            dbHandler.enableCommand(enableCommand);
            message.done("Command: " + enableCommand + " enabled!");
        } catch (final SQLInsertionError error) {
            logger.error(className, error.getMessage());
            message.error(error.getMessage());
        }
    }

    /**
     * Disable a command.
     * @param message the message.
     */
    public void disableCommand(final Message message) {
        final String enableCommand = String.join(", ", message.getArgs());
        try {
            dbHandler.disableCommand(enableCommand);
            message.done("Command: " + enableCommand + " disabled!");
        } catch (final SQLInsertionError error) {
            logger.error(className, error.getMessage());
            message.error(error.getMessage());
        }
    }
}
