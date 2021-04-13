package nl.djj.swgoh_bot_v2.entities.db;

/**
 * @author DJJ
 */
public class Player {
    private final transient int allycode;
    private final transient String name;
    private final transient int galacticPower;
    private final transient String url;
    private final transient String lastUpdated;
    private final transient int guildId;

    /**
     * Constructor.
     * @param allycode the allycode.
     * @param name the name.
     * @param galacticPower the GP.
     * @param url the swgoh url.
     * @param lastUpdated the lastUpdated date.
     * @param guildId the guildId.
     */
    public Player(final int allycode, final String name, final int galacticPower, final String url, final String lastUpdated, final int guildId) {
        this.allycode = allycode;
        this.name = name;
        this.galacticPower = galacticPower;
        this.url = url;
        this.lastUpdated = lastUpdated;
        this.guildId = guildId;
    }

    public int getAllycode() {
        return allycode;
    }

    public String getName() {
        return name;
    }

    public int getGalacticPower() {
        return galacticPower;
    }

    public String getUrl() {
        return url;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public int getGuildId() {
        return guildId;
    }
}
