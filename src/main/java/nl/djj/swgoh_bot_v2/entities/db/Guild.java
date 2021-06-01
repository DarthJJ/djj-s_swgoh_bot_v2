package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.command_impl.GuildImpl;
import nl.djj.swgoh_bot_v2.database.custom_persistors.LocalDateTimePersister;

import java.time.LocalDateTime;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "guilds", daoClass = GuildImpl.class)
public class Guild {
    @DatabaseField(id = true)
    private transient int identifier;
    @DatabaseField(unique = true, columnName = "discord_id")
    private transient String discordId;
    @DatabaseField
    private transient String name;
    @DatabaseField(columnName = "galactic_power")
    private transient int galacticPower;
    @DatabaseField
    private transient int members;
    @DatabaseField(persisterClass = LocalDateTimePersister.class, columnName = "last_updated")
    private transient LocalDateTime lastUpdated;
    @DatabaseField(persisterClass = LocalDateTimePersister.class, columnName = "last_swgoh_updated")
    private transient LocalDateTime lastSwgohUpdated;
    @ForeignCollectionField()
    private transient ForeignCollection<Player> players;

    /**
     * Constructor.
     **/
    public Guild() {

    }

    /**
     * Constructor.
     *
     * @param identifier       the guild ID.
     * @param name             the guild name.
     * @param galacticPower    the total GP.
     * @param members          the amount of members.
     * @param lastUpdated      the last time it was updated on SWGOH.
     * @param discordId        the discordId.
     * @param lastSwgohUpdated when last updated on SWGOH.gg.
     */
    public Guild(final int identifier, final String discordId, final String name, final int galacticPower, final int members, final LocalDateTime lastUpdated, final LocalDateTime lastSwgohUpdated) {
        this.identifier = identifier;
        this.discordId = discordId;
        this.name = name;
        this.galacticPower = galacticPower;
        this.members = members;
        this.lastUpdated = lastUpdated;
        this.lastSwgohUpdated = lastSwgohUpdated;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getDiscordId() {
        return discordId;
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

    public LocalDateTime getLastSwgohUpdated() {
        return lastSwgohUpdated;
    }

    public ForeignCollection<Player> getPlayers() {
        return players;
    }

}
