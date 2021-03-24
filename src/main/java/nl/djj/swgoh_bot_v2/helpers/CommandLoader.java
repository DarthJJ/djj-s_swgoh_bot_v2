package nl.djj.swgoh_bot_v2.helpers;

import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.commands.Ping;
import nl.djj.swgoh_bot_v2.database.CommandHelper;

import java.util.*;

/**
 * @author DJJ
 */
public class CommandLoader {

    private final Map<String, BaseCommand> commands;
    private final Map<String, String> aliases;
    private final CommandHelper commandHelper;

    /**
     * The Constructor.
     */
    public CommandLoader(final CommandHelper commandHelper) {
        super();
        this.commands = new HashMap<>();
        this.aliases = new HashMap<>();
        this.commandHelper = commandHelper;
        initializeCommands(new ArrayList<>(Arrays.asList(new Ping())));
    }

    private void initializeCommands(final List<BaseCommand> toLoad) {
        for (final BaseCommand command : toLoad) {
            if (commandHelper.getCommandEnabledStatus(command.getName())) {
                command.setEnabled(true);
                commands.put(command.getName().toLowerCase(), command);
                aliases.put(command.getName().toLowerCase(), command.getName().toLowerCase());
                if (command.getAliases().length > 0) {
                    for (final String alias : command.getAliases()) {
                        aliases.put(alias.toLowerCase(), command.getName().toLowerCase());
                    }
                }
            }
        }
    }

    public BaseCommand getCommand(final String name) {
        final String commandName = aliases.get(name.toLowerCase());
        if (commandName == null){
            return null;
        }
        final BaseCommand command = commands.get(commandName);
        if (command != null) {
            return command;
        }
        return commands.get(name.toLowerCase());
    }
}
