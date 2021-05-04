package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.PlayerUnitDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "playerUnits", daoClass = PlayerUnitDaoImpl.class)
public class PlayerUnit {
    @DatabaseField(generatedId = true)
    private transient int identifier;
    @DatabaseField(foreign = true, uniqueCombo = true, foreignAutoRefresh = true)
    private transient Player player;
    @DatabaseField(foreign = true, uniqueCombo = true, foreignAutoRefresh = true)
    private transient Unit unit;
    @DatabaseField
    private transient int rarity;
    @DatabaseField
    private transient int galacticPower;
    @DatabaseField
    private transient int gear;
    @DatabaseField
    private transient int gearPieces;
    @DatabaseField
    private transient int relic;
    @DatabaseField
    private transient int speed;
    @ForeignCollectionField()
    private transient ForeignCollection<UnitAbility> abilities;

    /**
     * Constructor.
     **/
    public PlayerUnit() {

    }

    /**
     * Constructor.
     * @param player the owner.
     * @param unit the base unit.
     * @param rarity the rarity level.
     * @param galacticPower the GP.
     * @param gear the gear level.
     * @param gearPieces the equipped gear pieces.
     * @param relic the relic level.
     * @param speed the speed.
     */
    public PlayerUnit(final Player player, final Unit unit, final int rarity, final int galacticPower, final int gear, final int gearPieces, final int relic, final int speed) {
        this.player = player;
        this.unit = unit;
        this.rarity = rarity;
        this.galacticPower = galacticPower;
        this.gear = gear;
        this.gearPieces = gearPieces;
        this.relic = relic;
        this.speed = speed;
    }

    public Player getPlayer() {
        return player;
    }

    public Unit getUnit() {
        return unit;
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

    public int getGearPieces() {
        return gearPieces;
    }

    public int getRelic() {
        return relic;
    }

    public int getSpeed() {
        return speed;
    }

    public ForeignCollection<UnitAbility> getAbilities() {
        return abilities;
    }

    public void setIdentifier(final int identifier) {
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return identifier;
    }
}
