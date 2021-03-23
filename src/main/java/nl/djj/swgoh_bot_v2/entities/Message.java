package nl.djj.swgoh_bot_v2.entities;

import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * @author DJJ
 */
public class Message {

    private final String author;
    private final String[] args;
    private final int authorLevel;
    private final MessageChannel channel;

    /**
     * Constructor;
     *
     * @param author      the author of the message.
     * @param args        the arguments of the message.
     * @param authorLevel the authLevel of the author.
     * @param channel     the channel to answer to.
     */
    public Message(final String author, final String[] args, final int authorLevel, final MessageChannel channel) {
        super();
        this.author = author;
        this.args = args;
        this.authorLevel = authorLevel;
        this.channel = channel;
    }

    public String getAuthor() {
        return author;
    }

    public String[] getArgs() {
        return args;
    }

    public int getAuthorLevel() {
        return authorLevel;
    }

    public MessageChannel getChannel() {
        return this.channel;
    }
}
