package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.customPersistors.LocalDateTimePersister;

import java.time.LocalDateTime;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "guilds")
public class Guild {
    @DatabaseField(id = true)
    private transient int identifier;
    @DatabaseField
    private transient String name;
    @DatabaseField
    private transient int galacticPower;
    @DatabaseField
    private transient int members;
    @DatabaseField(persisterClass = LocalDateTimePersister.class)
    private transient LocalDateTime lastUpdated;
    @ForeignCollectionField(eager = false)
    private transient ForeignCollection<Player> players;

    /**
     * Constructor.
     **/
    public Guild() {

    }

    /**
     * Constructor.
     * @param identifier the guild ID.
     * @param name the guild name.
     * @param galacticPower the total GP.
     * @param members the amount of members.
     * @param lastUpdated the last time it was updated on SWGOH.
     */
    public Guild(final int identifier, final String name, final int galacticPower, final int members, final LocalDateTime lastUpdated) {
        this.identifier = identifier;
        this.name = name;
        this.galacticPower = galacticPower;
        this.members = members;
        this.lastUpdated = lastUpdated;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public int getGalacticPower() {
        return galacticPower;
    }

    public int getMembers() {
        return members;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public ForeignCollection<Player> getPlayers() {
        return players;
    }
}
