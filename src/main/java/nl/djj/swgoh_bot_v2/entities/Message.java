package nl.djj.swgoh_bot_v2.entities;

import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

/**
 * @author DJJ
 */
public class Message {

    private final String authorId;
    private final String author;
    private final String command;
    private final String flag;
    private final List<String> args;
    private final int authorLevel;
    private final MessageChannel channel;

    /**
     * Constructor.
     *
     * @param authorId    the authorId of the message.
     * @param author      the author name.
     * @param args        the arguments of the message.
     * @param flag        the flag of the message.
     * @param authorLevel the authLevel of the authorId.
     * @param channel     the channel to answer to.
     * @param command     the command of the message.
     */
    public Message(final String authorId, final String author, final String command, final String flag, final List<String> args, final int authorLevel, final MessageChannel channel) {
        super();
        this.authorId = authorId;
        this.author = author;
        this.flag = flag;
        this.args = args;
        this.command = command;
        this.authorLevel = authorLevel;
        this.channel = channel;
    }

    public String getAuthorId() {
        return authorId;
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

    public int getAuthorLevel() {
        return authorLevel;
    }

    public MessageChannel getChannel() {
        return this.channel;
    }
}
