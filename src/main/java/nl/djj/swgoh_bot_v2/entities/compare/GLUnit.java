package nl.djj.swgoh_bot_v2.entities.compare;

/**
 * @author DJJ
 **/
public class GLUnit {
    private final transient String unitName;
    private final transient int gearLevel;
    private final transient int gearPieces;
    private final transient int relicLevel;
    private final transient int rarity;
    private final transient int zetas;
    private transient double completeness;

    /**
     * Constructor.
     *
     * @param unitName   the unit name.
     * @param gearLevel  the gearLevel.
     * @param gearPieces the gearPieces.
     * @param relicLevel the relicLevel.
     * @param rarity     the rarity level.
     * @param zetas      the zetas.
     */
    public GLUnit(final String unitName, final int gearLevel, final int gearPieces, final int relicLevel, final int rarity, final int zetas) {
        this.unitName = unitName;
        this.gearLevel = gearLevel;
        this.gearPieces = gearPieces;
        this.relicLevel = relicLevel;
        this.rarity = rarity;
        this.zetas = zetas;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setCompleteness(final double completeness) {
        this.completeness = completeness;
    }

    public int getGearPieces() {
        return gearPieces;
    }

    public int getGearLevel() {
        return gearLevel;
    }

    public int getRelicLevel() {
        return relicLevel;
    }

    public int getRarity() {
        return rarity;
    }

    public int getZetas() {
        return zetas;
    }

    public double getCompleteness() {
        return completeness;
    }
}
