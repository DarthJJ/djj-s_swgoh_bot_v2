package nl.djj.swgoh_bot_v2;

import ch.qos.logback.classic.Level;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import nl.djj.swgoh_bot_v2.config.GithubConstants;
import nl.djj.swgoh_bot_v2.database.Database;
import nl.djj.swgoh_bot_v2.exceptions.InitializationError;
import nl.djj.swgoh_bot_v2.helpers.CommandLoader;
import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.listeners.EventListener;
import nl.djj.swgoh_bot_v2.listeners.ReadyListener;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

/**
 * @author DJJ
 */
public final class Main {
    private final transient boolean debug;
    private static transient Logger logger;
    private transient CommandLoader commandLoader;
    private transient ImplHelper implHelper;
    private final transient String className = this.getClass().getSimpleName();
    private static boolean MAINTENANCE_MODE;

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
        final Dotenv dotenv = Dotenv.configure().filename(".env").load();
        GithubConstants.init(dotenv.get("GITHUB_OWNER"), dotenv.get("GITHUB_REPO"), dotenv.get("GITHUB_OAUTH"));
        debug = Boolean.parseBoolean(dotenv.get("DEBUG_MODE"));
        logger = new Logger(debug);
        try {
            final String username = dotenv.get("DB_USER");
            final String password = dotenv.get("DB_PASS");
            final String discordToken;
            final String dbName;
            if (debug) {
                discordToken = dotenv.get("BETA_DISCORD_TOKEN");
                dbName = dotenv.get("DB_NAME_BETA");
            } else {
                discordToken = dotenv.get("PUBLIC_DISCORD_TOKEN_V2");
                dbName = dotenv.get("DB_NAME");
            }
            final Database database = new Database(logger, username, password, dbName);
            database.createDatabase();
            implHelper = new ImplHelper(logger, database.dao());
            commandLoader = new CommandLoader(implHelper, logger, database.dao());
            initializeDiscord(discordToken);
            logger.info(className, "Bot Ready!");
        } catch (final InitializationError exception) {
            logger.error(className, "Main", exception.getMessage());
        }
    }

    private void initializeDiscord(final String token) throws InitializationError {
        try {
            final JDABuilder builder = JDABuilder.createDefault(token);
            builder.addEventListeners(new EventListener(commandLoader, implHelper), new ReadyListener());
            if (debug) {
                builder.setActivity(Activity.listening("Being developed"));
            } else {
                builder.setActivity(Activity.streaming("Star Wars", "https://www.youtube.com/watch?v=YddwkMJG1Jo"));
            }
            builder.setMemberCachePolicy(MemberCachePolicy.ALL);
            builder.enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS);
            builder.build();
            ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)).setLevel(Level.ERROR);
        } catch (final LoginException exception) {
            throw new InitializationError(className, "InitializeDiscord", exception.getMessage());
        }
    }


    /**
     * @return the application Logger.
     */
    public static Logger getLogger() {
        return logger;
    }

    /***
     * @return Maintenance mode.
     */
    public static boolean isMaintenanceMode() {
        return MAINTENANCE_MODE;
    }

    /**
     * @param maintenanceMode the maintenance mode.
     */
    public static void setMaintenanceMode(final boolean maintenanceMode) {
        MAINTENANCE_MODE = maintenanceMode;
    }
}
