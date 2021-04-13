package nl.djj.swgoh_bot_v2.entities.db;

/**
 * @author DJJ
 */
public class PlayerUnit {
    private final transient int allycode;
    private final transient int guildId;
    private final transient String baseId;
    private final transient int rarity;
    private final transient int galacticPower;
    private final transient int gear;
    private final transient int relic;
    private final transient int zetas;
    private final transient int speed;

    /**
     * Constructor.
     * @param allycode allycode.
     * @param guildId guild id.
     * @param baseId the base id.
     * @param rarity the star level.
     * @param galacticPower the galactic power.
     * @param gear the gear level.
     * @param relic the relic level.
     * @param zetas the zeta count.
     * @param speed the base speed.
     */
    public PlayerUnit(final int allycode, final int guildId, final String baseId, final int rarity, final int galacticPower, final int gear, final int relic, final int zetas, final int speed) {
        this.allycode = allycode;
        this.guildId = guildId;
        this.baseId = baseId;
        this.rarity = rarity;
        this.galacticPower = galacticPower;
        this.gear = gear;
        this.relic = relic;
        this.zetas = zetas;
        this.speed = speed;
    }


    public int getAllycode() {
        return allycode;
    }

    public String getBaseId() {
        return baseId;
    }

    public int getRarity() {
        return rarity;
    }

    public int getGalacticPower() {
        return galacticPower;
    }

    public int getGear() {
        return gear;
    }

    public int getRelic() {
        return relic;
    }

    public int getZetas() {
        return zetas;
    }

    public int getSpeed() {
        return speed;
    }

    public int getGuildId() {
        return guildId;
    }
}
