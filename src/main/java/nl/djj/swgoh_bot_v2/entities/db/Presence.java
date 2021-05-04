package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.customPersistors.LocalDateTimePersister;
import nl.djj.swgoh_bot_v2.database.daos.PresenceDaoImpl;

import java.time.LocalDateTime;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "presence", daoClass = PresenceDaoImpl.class)
public class Presence {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String userId;
    @DatabaseField
    private String username;
    @DatabaseField(persisterClass = LocalDateTimePersister.class)
    private LocalDateTime timestampt;

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
        this.timestampt = timestamp;
    }
}
