package nl.djj.swgoh_bot_v2.command_impl;

import net.dv8tion.jda.api.entities.TextChannel;
import nl.djj.swgoh_bot_v2.Main;
import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.List;

/**
 * @author DJJ
 */
public abstract class ControlImpl {
    private final transient String className = this.getClass().getSimpleName();
    private final transient Logger logger;
    private final transient DAO dao;
    private static final transient String START_UPDATE = "Update starting, please refrain from bot usage.";
    private static final transient String STOP_UPDATE = "Update done, please enjoy the bot.";

    /**
     * @param logger    the logger.
     * @param dao the DB handler.
     */
    public ControlImpl(final Logger logger, final DAO dao) {
        super();
        this.dao = dao;
        this.logger = logger;
    }

    /**
     * Create / update the database.
     *
     * @param message the message.
     */
    public void createDatabase(final Message message) {
//        dbHandler.createDatabase();
//        logger.info(className, "Updating / Creating the DB");
//        message.done("Database has been updated!");
    }

    /**
     * Enables update mode.
     *
     * @param message message object.
     */
    public void enableUpdateMode(final Message message) {
        Main.setMaintenanceMode(true);
        sendNotification(message, START_UPDATE);
    }

    /**
     * Disables update mode.
     *
     * @param message message object.
     */
    public void disableUpdateMode(final Message message) {
        Main.setMaintenanceMode(false);
        sendNotification(message, STOP_UPDATE);
    }


    private void sendNotification(final Message message, final String string) {
//        try {
//            final List<String> channels = dbHandler.getAllGuildNotifyChannels();
//
//            for (final String channel : channels) {
//                final TextChannel textChannel = message.getChannel().getJDA().getTextChannelById(channel);
//                if (textChannel != null) {
//                    textChannel.sendMessage(string).queue();
//                }
//            }
//            message.done("Notification sent");
//        } catch (final RetrieveError error) {
//            message.error(error.getMessage());
//        }
    }

    /**
     * overridden for bot closure.
     */
    public abstract void closeBot();

    /**
     * Sends a notification to all guilds.
     * @param message the message to use.
     * @param string the message.
     */
    public void sendMessage(final Message message, final String string) {
        sendNotification(message, string);
    }
}
