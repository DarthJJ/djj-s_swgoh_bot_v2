package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.AbilityDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "abilities", daoClass = AbilityDaoImpl.class)
public class Ability {
    @DatabaseField(id = true)
    private transient String identifier;
    @DatabaseField
    private transient String name;
    @DatabaseField
    private transient int tierMax;
    @DatabaseField
    private transient boolean zeta;
    @DatabaseField
    private transient boolean omega;
    @DatabaseField(foreign = true)
    private transient Unit unit;

    /**
     * Constructor.
     **/
    public Ability() {

    }

    /**
     * Creates an ability object.
     *
     * @param identifier the id of the ability.
     * @param name       the name of the ability.
     * @param tierMax    the maximum level of the ability.
     * @param zeta       if it's a zeta.
     * @param omega      if it's an omega.
     * @param unit       the base ID of the unit.
     */
    public Ability(final String identifier, final String name, final int tierMax, final boolean zeta, final boolean omega, final Unit unit) {
        this.identifier = identifier;
        this.name = name;
        this.tierMax = tierMax;
        this.zeta = zeta;
        this.omega = omega;
        this.unit = unit;
    }

    public String getIdentifier() {
        return identifier;
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

    public Unit getUnit() {
        return unit;
    }
}
