package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.UnitAbilityDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "unitAbilities", daoClass = UnitAbilityDaoImpl.class)
public class UnitAbility {
    @DatabaseField(generatedId = true)
    private transient int identifier;
    @DatabaseField(foreign = true, uniqueCombo = true)
    private transient PlayerUnit playerUnit;
    @DatabaseField(foreign = true, uniqueCombo = true)
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
     * @param playerUnit the PlayerUnit which has this ability.
     * @param baseAbility the base ability.
     * @param level the current level.
     */
    public UnitAbility(final PlayerUnit playerUnit, final Ability baseAbility, final int level) {
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

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final int identifier) {
        this.identifier = identifier;
    }
}
