package nl.djj.swgoh_bot_v2.helpers;

import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.commands.admin.Control;
import nl.djj.swgoh_bot_v2.commands.admin.Notify;
import nl.djj.swgoh_bot_v2.commands.admin.Update;
import nl.djj.swgoh_bot_v2.commands.bot.Changelog;
import nl.djj.swgoh_bot_v2.commands.bot.Help;
import nl.djj.swgoh_bot_v2.commands.bot.Register;
import nl.djj.swgoh_bot_v2.commands.bot.Report;
import nl.djj.swgoh_bot_v2.commands.moderation.Config;
import nl.djj.swgoh_bot_v2.commands.swgoh.Guild;
import nl.djj.swgoh_bot_v2.commands.swgoh.Need;
import nl.djj.swgoh_bot_v2.commands.swgoh.Profile;
import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.db.Command;
import nl.djj.swgoh_bot_v2.exceptions.InitializationError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DJJ
 */
public class CommandLoader {

    private final transient Map<String, BaseCommand> commands;
    private final transient Map<String, String> aliases;
    private final transient ImplHelper implHelper;
    private final transient DAO dao;
    private final transient String className = this.getClass().getSimpleName();
    private final transient Logger logger;

    /**
     * The Constructor.
     *
     * @param implHelper the command handler to use.
     * @param logger     the logger to use.
     * @param dao        the DB connection.
     */
    public CommandLoader(final ImplHelper implHelper, final Logger logger, final DAO dao) throws InitializationError {
        super();
        try {
            this.commands = new TreeMap<>();
            this.aliases = new TreeMap<>();
            this.implHelper = implHelper;
            this.dao = dao;
            this.logger = logger;
            initializeCommands(new ArrayList<>(Arrays.asList(
                    new Update(logger, implHelper),
                    new Control(logger, implHelper),
                    new Register(logger, implHelper),
                    new Profile(logger, implHelper),
                    new Guild(logger, implHelper),
                    new Config(logger, implHelper),
                    new Changelog(logger, implHelper),
                    new Notify(logger, implHelper),
                    new Report(logger, implHelper),
                    new Need(logger, implHelper),
                    new Help(logger, implHelper) {
                        @Override
                        public void handleRequest(final Message message) {
                            handleHelpRequest(message);
                        }
                    }
            )));
        } catch (final InsertionError exception) {
            throw new InitializationError(className, "Constructor", exception);
        }
    }

    private void initializeCommands(final List<BaseCommand> toLoad) throws InsertionError {
        for (final BaseCommand command : toLoad) {
            command.createFlags();
            if (!dao.commandDao().exist(command.getName())) {
                dao.commandDao().save(new Command(command.getName(), false));
            }
            for (final Map.Entry<String, Flag> entry : command.getFlags().entrySet()) {
                if (!dao.flagDao().exists(entry.getValue().getName())) {
                    dao.flagDao().save(new nl.djj.swgoh_bot_v2.entities.db.Flag(entry.getValue().getName(), command.getName(), false));
                }
            }
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

    //CHECKSTYLE.OFF: NPathComplexity
    private void handleHelpRequest(final Message message) {
        final BaseCommand command = getCommand(message.getFlag());
        if (!message.getFlag().isEmpty() && command == null) {
            message.error("This command seems invalid, please use '" + message.getGuildPrefix() + " help' for all available commands");
            return;
        }
        if (command != null) {
            if (this.implHelper.getProfileImpl().isAllowed(message.getAuthorId(), command.getRequiredLevel())) {
                final Map<String, Flag> flags = new LinkedHashMap<>();
                for (final Map.Entry<String, Flag> entry : command.getFlags().entrySet()){
                    if (this.implHelper.getCommandImpl().getFlagEnabledStatus(entry.getKey())){
                        flags.put(entry.getKey(), entry.getValue());
                    }
                }
                message.done(MessageHelper.formatSpecificHelpText(command.getName(), command.getDescription(), flags, message.getGuildPrefix()));
                return;
            }
            message.error("You are not authorized for this command.\nNaughty boy / girl / Black Hawk");
            return;
        }
        final Map<String, List<BaseCommand>> helpText = new ConcurrentHashMap<>();
        for (final Map.Entry<String, BaseCommand> entry : commands.entrySet()) {
            if (!this.implHelper.getProfileImpl().isAllowed(message.getAuthorId(), entry.getValue().getRequiredLevel())) {
                continue;
            }
            if (!this.implHelper.getCommandImpl().getCommandEnabledStatus(entry.getValue().getName())) {
                continue;
            }
            if (helpText.containsKey(entry.getValue().getCategory().getName())) {
                final List<BaseCommand> groupedCommands = helpText.get(entry.getValue().getCategory().getName());
                groupedCommands.add(entry.getValue());
                helpText.put(entry.getValue().getCategory().getName(), groupedCommands);
            } else {
                helpText.put(entry.getValue().getCategory().getName(), new ArrayList<>() {{
                    add(entry.getValue());
                }});
            }
        }
        message.done(MessageHelper.formatGenericHelpText(helpText));
    }
    //CHECKSTYLE.ON: NPathComplexity

    private BaseCommand getCommand(final String name) {
        final String commandName = aliases.get(name.toLowerCase(Locale.ENGLISH));
        if (commandName == null) {
            return null;
        }
        return commands.get(commandName);
    }

    /**
     * @param name     the name to search for.
     * @param authorId the authorId.
     * @return the command found.
     */
    public BaseCommand getCommand(final String name, final String authorId) {
        final BaseCommand command = getCommand(name);
        if (command != null && (this.implHelper.getCommandImpl().getCommandEnabledStatus(command.getName()) || authorId.equals(BotConstants.OWNER_ID))) {
            return command;
        }
        return null;
    }

    /**
     * @param command the baseCommand.
     * @param name the name of the flag.
     * @param authorId the author id.
     * @return if enabled yes or no.
     */
    public boolean isFlagEnabled(final BaseCommand command, final String name, final String authorId) {
        final Flag flag = command.getFlags().get(name);
        return flag != null && (this.implHelper.getCommandImpl().getFlagEnabledStatus(name) || authorId.equals(BotConstants.OWNER_ID));
    }
}
