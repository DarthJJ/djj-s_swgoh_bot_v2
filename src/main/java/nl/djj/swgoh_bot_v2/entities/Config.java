package nl.djj.swgoh_bot_v2.entities;

/**
 * @author DJJ
 */
public class Config {

    private String prefix;
    private String modLogChannel;
    private String modRole;
    private String adminRole;
    private boolean systemNotice;
    private int guildId;
    private String botNotifyChannel;
    private String lastOnlineIgnoreRole;

    /**
     * Constructor.
     */
    public Config() {

    }

    /**
     * Returns a config with default values.
     *
     * @param guildId the guild id.
     */
    public Config(final int guildId) {
        this.prefix = "!#";
        this.modLogChannel = "";
        this.modRole = "";
        this.adminRole = "";
        this.systemNotice = false;
        this.botNotifyChannel = "";
        this.lastOnlineIgnoreRole = "";
        this.guildId = guildId;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public void setModLogChannel(final String modLogChannel) {
        this.modLogChannel = modLogChannel;
    }

    public void setModRole(final String modRole) {
        this.modRole = modRole;
    }

    public void setAdminRole(final String adminRole) {
        this.adminRole = adminRole;
    }

    public void setSystemNotice(final boolean systemNotice) {
        this.systemNotice = systemNotice;
    }

    public void setGuildId(final int guildId) {
        this.guildId = guildId;
    }

    public void setBotNotifyChannel(final String botNotifyChannel) {
        this.botNotifyChannel = botNotifyChannel;
    }

    public void setLastOnlineIgnoreRole(final String lastOnlineIgnoreRole) {
        this.lastOnlineIgnoreRole = lastOnlineIgnoreRole;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getModLogChannel() {
        return modLogChannel;
    }

    public String getModRole() {
        return modRole;
    }

    public String getAdminRole() {
        return adminRole;
    }

    public boolean isSystemNotice() {
        return systemNotice;
    }

    public int getGuildId() {
        return guildId;
    }

    public String getBotNotifyChannel() {
        return botNotifyChannel;
    }

    public String getLastOnlineIgnoreRole() {
        return lastOnlineIgnoreRole;
    }
}
