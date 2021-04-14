package nl.djj.swgoh_bot_v2.entities.db;

/**
 * @author DJJ
 */
public class Ability {
    private final transient String baseId;
    private final transient String name;
    private final transient int tierMax;
    private final transient boolean zeta;
    private final transient boolean omega;
    private final transient String unitBaseId;

    /**
     * Creates an ability object.
     * @param baseId the id of the ability.
     * @param name the name of the ability.
     * @param tierMax the maximum level of the ability.
     * @param zeta if it's a zeta.
     * @param omega if it's an omega.
     * @param unitBaseId the base ID of the unit.
     */
    public Ability(final String baseId, final String name, final int tierMax, final boolean zeta, final boolean omega, final String unitBaseId) {
        this.baseId = baseId;
        this.name = name;
        this.tierMax = tierMax;
        this.zeta = zeta;
        this.omega = omega;
        this.unitBaseId = unitBaseId;
    }

    public String getBaseId() {
        return baseId;
    }

    public String getName() {
        return name;
    }

    public int getTierMax() {
        return tierMax;
    }

    public boolean isZeta() {
        return zeta;
    }

    public boolean isOmega() {
        return omega;
    }

    public String getUnitBaseId() {
        return unitBaseId;
    }
}
