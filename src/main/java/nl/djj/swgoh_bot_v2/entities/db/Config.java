package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.database.daos.ConfigDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "config", daoClass = ConfigDaoImpl.class)
public class Config {
    @DatabaseField(id = true)
    private transient String guildId;
    @DatabaseField
    private transient int swgohId;
    @DatabaseField
    private transient String prefix;
    @DatabaseField
    private transient String moderationRole;
    @DatabaseField
    private transient String ignoreRole;
    @DatabaseField
    private transient String notifyChannel;
    @DatabaseField
    private transient String botLoggingChannel;

    /**
     * Constructor.
     **/
    public Config() {

    }

    public Config(final String guildId){
        super();
        this.guildId = guildId;
        this. swgohId = -1;
        this. prefix = BotConstants.DEFAULT_PREFIX;
        this.moderationRole = "";
        this.ignoreRole = "";
        this.notifyChannel = "";
        this.botLoggingChannel = "";
    }

    public String getGuildId() {
        return guildId;
    }

    public int getSwgohId() {
        return swgohId;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getModerationRole() {
        return moderationRole;
    }

    public String getIgnoreRole() {
        return ignoreRole;
    }

    public String getNotifyChannel() {
        return notifyChannel;
    }

    public String getBotLoggingChannel() {
        return botLoggingChannel;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public void setSwgohId(final int swgohId) {
        this.swgohId = swgohId;
    }

    public void setIgnoreRole(final String ignoreRole) {
        this.ignoreRole = ignoreRole;
    }

    public void setNotifyChannel(final String notifyChannel) {
        this.notifyChannel = notifyChannel;
    }
}
