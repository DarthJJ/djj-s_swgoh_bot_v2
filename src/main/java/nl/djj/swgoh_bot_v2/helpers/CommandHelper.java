package nl.djj.swgoh_bot_v2.helpers;

import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.commands.admin.Control;
import nl.djj.swgoh_bot_v2.commands.admin.Update;
import nl.djj.swgoh_bot_v2.commands.bot.Register;
import nl.djj.swgoh_bot_v2.database.CommandHandler;
import nl.djj.swgoh_bot_v2.database.Database;

import java.util.*;

/**
 * @author DJJ
 */
public class CommandHelper {

    private final transient Map<String, BaseCommand> commands;
    private final transient Map<String, String> aliases;
    private final transient CommandHandler commandHandler;
    private final transient String className = this.getClass().getSimpleName();
    private final transient Logger logger;

    /**
     * The Constructor.
     *
     * @param commandHandler the command handler to use.
     * @param logger         the logger to use.
     * @param database       the DB connection.
     */
    public CommandHelper(final CommandHandler commandHandler, final Logger logger, final Database database) {
        super();
        this.commands = new HashMap<>();
        this.aliases = new HashMap<>();
        this.commandHandler = commandHandler;
        this.logger = logger;
        initializeCommands(new ArrayList<>(Arrays.asList(
                new Update(logger, database.getUpdateHandler()),
                new Control(logger, database.getControlHandler()),
                new Register(logger, database.getUserHandler()))));
    }

    private void initializeCommands(final List<BaseCommand> toLoad) {
        for (final BaseCommand command : toLoad) {
            command.setEnabled(commandHandler.getCommandEnabledStatus(command.getName()));
            commands.put(command.getName().toLowerCase(Locale.ENGLISH), command);
            logger.info(className, "Loaded command: " + command.getName());
            aliases.put(command.getName().toLowerCase(Locale.ENGLISH), command.getName().toLowerCase(Locale.ENGLISH));
            if (command.getAliases().length > 0) {
                for (final String alias : command.getAliases()) {
                    aliases.put(alias.toLowerCase(Locale.ENGLISH), command.getName().toLowerCase(Locale.ENGLISH));
                }
            }
        }
    }

    /**
     * @param name the name to search for.
     * @param ownerOverride if the issuer is the botOwner.
     * @return the command found.
     */
    public BaseCommand getCommand(final String name, final boolean ownerOverride) {
        final String commandName = aliases.get(name.toLowerCase(Locale.ENGLISH));
        if (commandName == null) {
            return null;
        }
        final BaseCommand command = commands.get(commandName);
        if (command != null && (command.isEnabled() || ownerOverride)) {
            return command;
        }
        return null;
    }
}
