package nl.djj.swgoh_bot_v2.entities.db;

import java.time.LocalDateTime;

/**
 * @author DJJ
 */
public class Guild {
    private final transient String name;
    private final transient int identifier;
    private final transient int galacticPower;
    private final transient int members;
    private final transient LocalDateTime lastUpdated;

    /**
     * Constructor.
     * @param name the name of the guild.
     * @param identifier the ID of the guild.
     * @param galacticPower the GP of the guild.
     * @param members the amount of members.
     * @param lastUpdated the last updated Date.
     */
    public Guild(final String name, final int identifier, final int galacticPower, final int members, final LocalDateTime lastUpdated) {
        this.name = name;
        this.identifier = identifier;
        this.galacticPower = galacticPower;
        this.members = members;
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public int getIdentifier() {
        return identifier;
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
}
