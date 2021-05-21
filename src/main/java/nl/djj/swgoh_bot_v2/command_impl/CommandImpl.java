package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class CommandImpl extends BaseImpl {

    /**
     * @param logger the logger.
     * @param dao    DB handler.
     */
    public CommandImpl(final Logger logger, final DAO dao) {
        super(logger, dao, CommandImpl.class.getName());
    }

    /**
     * Retrieves the enabled status for a command.
     *
     * @param commandName the command.
     * @return boolean value.
     */
    public boolean getCommandEnabledStatus(final String commandName) {
        return dao.commandDao().isEnabled(commandName);
    }

    /**
     * Enable the command.
     *
     * @param message the message.
     */
    public void enableCommand(final Message message) {
//        final String enableCommand = String.join(", ", message.getArgs());
//        try {
//            dbHandler.enableCommand(enableCommand);
//            message.done("Command: " + enableCommand + " enabled!");
//        } catch (final InsertionError error) {
//            logger.error(className, "enableCommand", error.getMessage());
//            message.error(error.getMessage());
//        }
    }

    /**
     * Disable a command.
     *
     * @param message the message.
     */
    public void disableCommand(final Message message) {
//        final String enableCommand = String.join(", ", message.getArgs());
//        try {
//            dbHandler.disableCommand(enableCommand);
//            message.done("Command: " + enableCommand + " disabled!");
//        } catch (final InsertionError error) {
//            logger.error(className, "disableCommand", error.getMessage());
//            message.error(error.getMessage());
//        }
    }

    /**
     * Updates the command usage.
     *
     * @param command the command.
     * @param flag    the flag.
     */
    public void updateCommandUsage(final String command, final String flag) {
        try {
            dao.commandUsageDao().updateUsage(command, flag);
        } catch (final InsertionError error) {
            logger.error(className, "updateCommandUsage", error.getMessage());
        }
    }

    /**
     * Retrieves the enabled status for the flag.
     * @param name then name of the flag.
     * @param parentCommand  the name of the parentCommand.
     * @return a boolean value.
     */
    public boolean getFlagEnabledStatus(final String parentCommand, final String name) {
        return dao.flagDao().isEnabled(parentCommand, name);
    }
}
