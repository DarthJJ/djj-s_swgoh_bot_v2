package nl.djj.swgoh_bot_v2.entities;

/**
 * @author DJJ
 */
public class UnitCompare {
    private final String name;
    private final String baseId;
    private final int rarity;
    private final int galacticPower;
    private final int gear;
    private final int relic;
    private final int zetas;
    private final int speed;

    /**
     * The constructor.
     * @param name unit name.
     * @param baseId unit baseId.
     * @param rarity unit stars.
     * @param galacticPower unit GP.
     * @param gear unit gear.
     * @param relic unit relic.
     * @param zetas unit zeta count.
     * @param speed unit speed.
     */
    public UnitCompare(final String name, final String baseId, final int rarity, final int galacticPower, final int gear, final int relic, final int zetas, final int speed) {
        this.name = name;
        this.baseId = baseId;
        this.rarity = rarity;
        this.galacticPower = galacticPower;
        this.gear = gear;
        this.relic = Math.max(-1, relic - 2);
        this.zetas = zetas;
        this.speed = speed;
    }

    /**
     * Constructor.
     */
    public UnitCompare() {
        this.name = "";
        this.baseId = "";
        this.rarity = 0;
        this.galacticPower = 0;
        this.gear = 0;
        this.relic = -1;
        this.zetas = 0;
        this.speed = 0;
    }

    public String getName() {
        return name;
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
}
