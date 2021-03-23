package nl.djj.swgoh_bot_v2.helpers;

import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.commands.Ping;
import nl.djj.swgoh_bot_v2.database.CommandHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DJJ
 */
public class CommandLoader {

    private final Map<String, BaseCommand> commands;
    private final CommandHelper commandHelper;
    /**
     * The Constructor.
     */
    public CommandLoader(final CommandHelper commandHelper) {
        super();
        commands = new HashMap<>();
        this.commandHelper = commandHelper;
        initializeCommands();
    }

    private void initializeCommands(){
        final Ping ping = new Ping();
        if (commandHelper.getCommandEnabledStatus(ping.getName())){
            ping.setEnabled(true);
            commands.put(ping.getName().toLowerCase(), ping);
        }
    }

    public BaseCommand getCommand(final String name){
        return commands.get(name.toLowerCase());
    }
}
