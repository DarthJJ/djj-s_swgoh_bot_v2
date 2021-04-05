package nl.djj.swgoh_bot_v2.entities;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nl.djj.swgoh_bot_v2.helpers.MessageHelper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author DJJ
 */
public final class Message {

    private final String messageId;
    private final String authorId;
    private final String author;
    private final String guildPrefix;
    private final String guildId;
    private final String command;
    private final String flag;
    private final List<String> args;
    private final MessageChannel channel;

    /**
     * Constructor.
     *
     * @param messageId   the message.
     * @param guildPrefix the guild Prefix.
     * @param authorId    the authorId of the message.
     * @param author      the author name.
     * @param guildId     the id of the guild.
     * @param args        the arguments of the message.
     * @param flag        the flag of the message.
     * @param channel     the channel to answer to.
     * @param command     the command of the message.
     */
    private Message(final String messageId, final String authorId, final String author, final String guildId, final String guildPrefix, final String command, final String flag, final List<String> args, final MessageChannel channel) {
        super();
        this.messageId = messageId;
        this.authorId = authorId;
        this.author = author;
        this.guildId = guildId;
        this.guildPrefix = guildPrefix;
        this.flag = flag;
        this.args = args;
        this.command = command;
        this.channel = channel;
    }

    /**
     * Adds the working status to a message.
     */
    public void working() {
        MessageHelper.addWorkingReaction(this);
    }

    /**
     * Adds the error status to a message and sends the error message.
     *
     * @param message the error message.
     */
    public void error(final String message) {
        MessageHelper.addErrorReaction(this);
        send(message);
    }

    /**
     * Adds the done status to a message and sends the error message.
     *
     * @param message the done message.
     */
    public void done(final String message) {
        MessageHelper.addDoneReaction(this);
        send(message);
    }

    /**
     * Adds the done status to a message and sends the error message.
     *
     * @param message the done embed.
     */
    public void done(final MessageEmbed message) {
        MessageHelper.addDoneReaction(this);
        send(message);
    }

    /**
     * Adds the done status to a message and sends the error message.
     *
     * @param messages A list of embeds..
     */
    public void done(final List<MessageEmbed> messages) {
        MessageHelper.addDoneReaction(this);
        for (final MessageEmbed embed : messages) {
            send(embed);
        }
    }

    public String getMessageId() {
        return messageId;
    }

    private void send(final String message) {
        getChannel().sendMessage(message).queue();
    }

    private void send(final MessageEmbed message) {
        getChannel().sendMessage(message).queue();
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getGuildPrefix() {
        return guildPrefix;
    }

    public String getFlag() {
        return flag;
    }

    public String getCommand() {
        return command;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getArgs() {
        return args;
    }

    public MessageChannel getChannel() {
        return this.channel;
    }

    public String getGuildId() {
        return this.guildId;
    }

    /**
     * Inits a message from the messageEvent.
     *
     * @param event       the event.
     * @param guildPrefix the guild prefix.
     * @return a message object.
     */
    public static Message initFromEvent(final MessageReceivedEvent event, final String guildPrefix) {
        final String authorId = event.getAuthor().getId();
        final String author = event.getAuthor().getName();
        final String messageID = event.getMessageId();
        final String guildId = event.getGuild().getId();
        final String messageContent = event.getMessage().getContentDisplay().replace(guildPrefix, "");

        final List<String> args = new LinkedList<>(Arrays.asList(messageContent.split(" ")));
        final String commandName;
        if (args.isEmpty()) {
            commandName = "";
        } else {
            commandName = args.get(0);
            args.remove(commandName);
        }
        final String flag;
        if (args.isEmpty()) {
            flag = "";
        } else {
            flag = args.get(0);
            args.remove(flag);
        }
        return new Message(messageID, authorId, author, guildId, guildPrefix, commandName, flag, args, event.getChannel());
    }
}
