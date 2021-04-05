package nl.djj.swgoh_bot_v2.entities;

/**
 * @author DJJ
 */
public class Guild {
    private final transient String swgohId;
    private final transient String discordId;
    private transient String prefix;
    private transient String moderationRole;
    private transient String ignoreRole;
    private transient String notifyChannel;
    private transient String botLoggingChannel;


    public Guild(final String swgohId, final String discordId) {
        super();
        this.swgohId = swgohId;
        this.discordId = discordId;
    }

    public String getSwgohId() {
        return this.swgohId;
    }

    public String getDiscordId() {
        return this.discordId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getModerationRole() {
        return moderationRole;
    }

    public void setModerationRole(String moderationRole) {
        this.moderationRole = moderationRole;
    }

    public String getIgnoreRole() {
        return ignoreRole;
    }

    public void setIgnoreRole(String ignoreRole) {
        this.ignoreRole = ignoreRole;
    }

    public String getNotifyChannel() {
        return notifyChannel;
    }

    public void setNotifyChannel(String notifyChannel) {
        this.notifyChannel = notifyChannel;
    }

    public String getBotLoggingChannel() {
        return botLoggingChannel;
    }

    public void setBotLoggingChannel(String botLoggingChannel) {
        this.botLoggingChannel = botLoggingChannel;
    }
}
