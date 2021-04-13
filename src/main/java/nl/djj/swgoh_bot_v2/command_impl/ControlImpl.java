package nl.djj.swgoh_bot_v2.command_impl;

import net.dv8tion.jda.api.entities.TextChannel;
import nl.djj.swgoh_bot_v2.Main;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.List;

/**
 * @author DJJ
 */
public abstract class ControlImpl {
    private final transient String className = this.getClass().getSimpleName();
    private final transient Logger logger;
    private final transient DatabaseHandler dbHandler;
    private static final transient String START_UPDATE = "Update starting, please refrain from bot usage.";
    private static final transient String STOP_UPDATE = "Update done, please enjoy the bot.";

    /**
     * @param logger    the logger.
     * @param dbHandler the DB handler.
     */
    public ControlImpl(final Logger logger, final DatabaseHandler dbHandler) {
        super();
        this.dbHandler = dbHandler;
        this.logger = logger;
    }

    /**
     * Create / update the database.
     *
     * @param message the message.
     */
    public void createDatabase(final Message message) {
        dbHandler.createDatabase();
        logger.info(className, "Updating / Creating the DB");
        message.done("Database has been updated!");
    }

    /**
     * Enables update mode.
     *
     * @param message message object.
     */
    public void enableUpdateMode(final Message message) {
        Main.setMaintenanceMode(true);
        toggleUpdateMode(message, START_UPDATE);
    }

    /**
     * Disables update mode.
     *
     * @param message message object.
     */
    public void disableUpdateMode(final Message message) {
        Main.setMaintenanceMode(false);
        toggleUpdateMode(message, STOP_UPDATE);
    }


    private void toggleUpdateMode(final Message message, final String string) {
        try {
            final List<String> channels = dbHandler.getAllGuildNotifyChannels();

            for (final String channel : channels) {
                final TextChannel textChannel = message.getChannel().getJDA().getTextChannelById(channel);
                if (textChannel != null) {
                    textChannel.sendMessage(string).queue();
                }
            }
        } catch (final SQLRetrieveError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * overridden for bot closure.
     */
    public abstract void closeBot();
}
