package nl.djj.swgoh_bot_v2.commandImpl;

import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.helpers.Logger;

public class CommandImpl {

    private final transient Logger logger;
    private final transient DatabaseHandler dbHandler;


    public CommandImpl(final Logger logger, final DatabaseHandler dbHandler) {
        super();
        this.logger = logger;
        this.dbHandler = dbHandler;
    }

    public boolean getCommandEnabledStatus(final String commandName) {
        return dbHandler.getCommandEnabledStatus(commandName);
    }

    public void enableCommand(final Message message) {
        final String enableCommand = String.join(", ", message.getArgs());
        try {
            dbHandler.enableCommand(enableCommand);
            message.getChannel().sendMessage("Command: " + enableCommand + " enabled!").queue();
        } catch (final SQLInsertionError error) {
            message.getChannel().sendMessage(error.getMessage()).queue();
        }
    }

    public void disableCommand(final Message message) {
        final String enableCommand = String.join(", ", message.getArgs());
        try {
            dbHandler.disableCommand(enableCommand);
            message.getChannel().sendMessage("Command: " + enableCommand + " disabled!").queue();
        } catch (final SQLInsertionError error) {
            message.getChannel().sendMessage(error.getMessage()).queue();
        }
    }
}
