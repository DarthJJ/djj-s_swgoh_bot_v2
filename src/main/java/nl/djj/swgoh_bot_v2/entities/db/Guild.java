package nl.djj.swgoh_bot_v2.entities.db;

/**
 * @author DJJ
 */
public class Guild {
    private final transient String name;
    private final transient int identifier;
    private final transient int galacticPower;
    private final transient int members;

    /**
     * Constructor.
     * @param name the name of the guild.
     * @param identifier the ID of the guild.
     * @param galacticPower the GP of the guild.
     * @param members the amount of members.
     */
    public Guild(final String name, final int identifier, final int galacticPower, final int members) {
        this.name = name;
        this.identifier = identifier;
        this.galacticPower = galacticPower;
        this.members = members;
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
}
