package nl.djj.swgoh_bot_v2.entities.db;

/**
 * @author DJJ
 */
public class UnitAbility {
    private final transient String abilityId;
    private final transient int allycode;
    private final transient int guildId;
    private final transient int level;
    private final transient boolean zeta;
    private final transient boolean omega;

    /**
     * Constructor.
     * @param abilityId ID of the ability.
     * @param allycode the allycode of the owner of the unit.
     * @param guildId the guild of the owner of the unit.
     * @param level the level of the ability.
     */
    public UnitAbility(final String abilityId, final int allycode, final int guildId, final int level, final boolean zeta, final boolean omega) {
        this.abilityId = abilityId;
        this.allycode = allycode;
        this.guildId = guildId;
        this.level = level;
        this.zeta = zeta;
        this.omega = omega;
    }

    public String getAbilityId() {
        return abilityId;
    }

    public int getAllycode() {
        return allycode;
    }

    public int getGuildId() {
        return guildId;
    }

    public int getLevel() {
        return level;
    }

    public boolean isZeta() {
        return zeta;
    }

    public boolean isOmega() {
        return omega;
    }
}
