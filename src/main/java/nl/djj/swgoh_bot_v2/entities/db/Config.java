package nl.djj.swgoh_bot_v2.entities.db;

import java.util.Objects;

/**
 * @author DJJ
 */
public class Config {
    private transient String swgohId;
    private final transient String discordId;
    private transient String prefix;
    private transient String moderationRole;
    private transient String ignoreRole;
    private transient String notifyChannel;
    private transient String botLoggingChannel;

    /**
     * Constructor.
     *
     * @param swgohId   the SWGOH ID.
     * @param discordId the Discord ID.
     */
    public Config(final String swgohId, final String discordId) {
        super();
        this.swgohId = swgohId;
        this.discordId = discordId;
    }

    /**
     * Constructor.
     *
     * @param discordId the Discord ID.
     */
    public Config(final String discordId) {
        super();
        this.discordId = discordId;
    }

    public void setSwgohId(final String swgohId) {
        this.swgohId = Objects.requireNonNullElse(swgohId, "");
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

    public void setPrefix(final String prefix) {
        this.prefix = Objects.requireNonNullElse(prefix, "");
    }

    public String getModerationRole() {
        return moderationRole;
    }

    public void setModerationRole(final String moderationRole) {
        this.moderationRole = Objects.requireNonNullElse(moderationRole, "");
    }

    public String getIgnoreRole() {
        return ignoreRole;
    }

    public void setIgnoreRole(final String ignoreRole) {
        this.ignoreRole = Objects.requireNonNullElse(ignoreRole, "");
    }

    public String getNotifyChannel() {
        return notifyChannel;
    }

    public void setNotifyChannel(final String notifyChannel) {
        this.notifyChannel = Objects.requireNonNullElse(notifyChannel, "");
    }

    public String getBotLoggingChannel() {
        return botLoggingChannel;
    }

    public void setBotLoggingChannel(final String botLoggingChannel) {
        this.botLoggingChannel = Objects.requireNonNullElse(botLoggingChannel, "");
    }
}
