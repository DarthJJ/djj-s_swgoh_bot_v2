package nl.djj.swgoh_bot_v2;

import nl.djj.swgoh_bot_v2.database.Database;
import io.github.cdimascio.dotenv.Dotenv;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.listeners.MessageListener;
import nl.djj.swgoh_bot_v2.listeners.ReadyListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

/**
 * @author DJJ
 */
public final class Main extends ListenerAdapter {

    private final transient Database database;
    private final Logger logger;
    private final String className = this.getClass().getSimpleName();

    /**
     * Main entry for the bot.
     *
     * @param args arguments passed if needed
     */
    public static void main(final String[] args) {
        new Main();
    }

    private Main() {
        super();
        final Dotenv dotenv = Dotenv.load();
        logger = new Logger(Boolean.parseBoolean(dotenv.get("BETA_MODE")));
        initializeDiscord(dotenv.get("BETA_DISCORD_TOKEN"));
        database = new Database(logger);
        closeBot(); //TODO: remove after fixing codeCheck
    }

    private void initializeDiscord(final String token) {
        try {
            final JDABuilder builder = JDABuilder.createDefault(token);
            builder.addEventListeners(new MessageListener(), new ReadyListener());
            builder.build();
        } catch (final LoginException exception) {
            logger.error(className, exception.getMessage());
        }
    }

    private void closeBot() {
        logger.info(className, "Closing Bot");
        database.closeDb();
        System.exit(0);
    }
}
