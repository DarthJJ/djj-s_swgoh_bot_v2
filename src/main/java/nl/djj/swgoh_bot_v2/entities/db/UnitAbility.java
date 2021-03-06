package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.UnitAbilityDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "unit_abilities", daoClass = UnitAbilityDaoImpl.class)
public class UnitAbility {
    @DatabaseField(id = true)
    private transient String identifier;
    @DatabaseField(foreign = true, columnName = "player_unit", foreignAutoRefresh = true)
    private transient PlayerUnit playerUnit;
    @DatabaseField(foreign = true, columnName = "base_ability", foreignAutoRefresh = true)
    private transient Ability baseAbility;
    @DatabaseField
    private transient int level;

    /**
     * Constructor.
     **/
    public UnitAbility() {

    }

    /**
     * Constructor.
     *
     * @param playerUnit  the PlayerUnit which has this ability.
     * @param baseAbility the base ability.
     * @param level       the current level.
     */
    public UnitAbility(final PlayerUnit playerUnit, final Ability baseAbility, final int level) {
        this.identifier = baseAbility.getIdentifier() + "_" + playerUnit.getIdentifier();
        this.playerUnit = playerUnit;
        this.baseAbility = baseAbility;
        this.level = level;
    }

    public PlayerUnit getPlayerUnit() {
        return playerUnit;
    }

    public Ability getBaseAbility() {
        return baseAbility;
    }

    public int getLevel() {
        return level;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }
}
