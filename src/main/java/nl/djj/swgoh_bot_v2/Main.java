package nl.djj.swgoh_bot_v2;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
import nl.djj.swgoh_bot_v2.database.Database;
import io.github.cdimascio.dotenv.Dotenv;
import nl.djj.swgoh_bot_v2.helpers.CommandLoader;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.listeners.EventListener;
import nl.djj.swgoh_bot_v2.listeners.ReadyListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

/**
 * @author DJJ
 */
public final class Main extends ListenerAdapter {
    private final transient boolean debug;
    private final transient Database database;
    private final transient Logger logger;
    private final transient CommandLoader commandLoader;
    private final transient ImplHelper implHelper;
    private final transient String className = this.getClass().getSimpleName();

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
        debug = Boolean.parseBoolean(dotenv.get("DEBUG_MODE"));
        logger = new Logger(debug);
        database = new Database(logger);
        implHelper = new ImplHelper(logger, database.getDatabaseHandler()) {
            @Override
            public void closeBot() {
                Main.this.closeBot();
            }
        };
        commandLoader = new CommandLoader(implHelper, logger);
        database.createDatabase();
        if (debug) {
            initializeDiscord(dotenv.get("BETA_DISCORD_TOKEN"));
        } else {
            initializeDiscord(dotenv.get("PUBLIC_DISCORD_TOKEN"));
        }
        logger.info(className, "Bot Ready!");
    }

    private void initializeDiscord(final String token) {
        try {
            final JDABuilder builder = JDABuilder.createDefault(token);
            builder.addEventListeners(new EventListener(logger, commandLoader, implHelper), new ReadyListener(logger));
            if (debug) {
                builder.setActivity(Activity.listening("Being developed"));
            } else {
                builder.setActivity(Activity.streaming("Star Wars", "https://www.youtube.com/watch?v=YddwkMJG1Jo"));
            }
            builder.setMemberCachePolicy(MemberCachePolicy.ALL);
            builder.enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS);
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
