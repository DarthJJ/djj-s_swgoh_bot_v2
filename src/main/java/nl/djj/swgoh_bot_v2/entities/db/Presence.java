package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.custom_persistors.LocalDateTimePersister;
import nl.djj.swgoh_bot_v2.database.daos.PresenceDaoImpl;

import java.time.LocalDateTime;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "presence", daoClass = PresenceDaoImpl.class)
public class Presence {
    @DatabaseField(columnName = "id", generatedId = true)
    private transient int identifier;
    @DatabaseField
    private transient String userId;
    @DatabaseField
    private transient String username;
    @DatabaseField(persisterClass = LocalDateTimePersister.class)
    private transient LocalDateTime timestamp;

    /**
     * Constructor.
     **/
    public Presence() {

    }

    /**
     * Constructor.
     * @param userId the discordId.
     * @param username the username.
     * @param timestamp the timestamp.
     */
    public Presence( final String userId, final String username, final LocalDateTime timestamp) {
        this.userId = userId;
        this.username = username;
        this.timestamp = timestamp;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
