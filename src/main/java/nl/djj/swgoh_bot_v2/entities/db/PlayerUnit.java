package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.PlayerUnitDaoImpl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "player_units", daoClass = PlayerUnitDaoImpl.class)
public class PlayerUnit {
    @DatabaseField(id = true)
    private transient String identifier;
    @DatabaseField(foreign = true, uniqueCombo = true, foreignAutoRefresh = true)
    private transient Player player;
    @DatabaseField(foreign = true, uniqueCombo = true, foreignAutoRefresh = true)
    private transient Unit unit;
    @DatabaseField
    private transient int rarity;
    @DatabaseField
    private transient int level;
    @DatabaseField(columnName = "galactic_power")
    private transient int galacticPower;
    @DatabaseField
    private transient int gear;
    @DatabaseField(columnName = "gear_pieces")
    private transient int gearPieces;
    @DatabaseField
    private transient int relic;
    @DatabaseField
    private transient int speed;
    @ForeignCollectionField(eager = true)
    private transient Collection<UnitAbility> abilities;

    /**
     * Constructor.
     **/
    public PlayerUnit() {

    }

    /**
     * Constructor.
     *
     * @param player        the owner.
     * @param unit          the base unit.
     * @param rarity        the rarity level.
     * @param level         the level of the unit.
     * @param galacticPower the GP.
     * @param gear          the gear level.
     * @param gearPieces    the equipped gear pieces.
     * @param relic         the relic level.
     * @param speed         the speed.
     */
    public PlayerUnit(final Player player, final Unit unit, final int rarity, final int level, final int galacticPower, final int gear, final int gearPieces, final int relic, final int speed) {
        this.identifier = unit.getBaseId() + "_" + player.getAllycode();
        this.player = player;
        this.unit = unit;
        this.level = level;
        this.rarity = rarity;
        this.galacticPower = galacticPower;
        this.gear = gear;
        this.gearPieces = gearPieces;
        this.relic = relic;
        this.speed = speed;
        this.abilities = new ArrayList<>();
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

    public int getLevel() {
        return level;
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

    public Collection<UnitAbility> getAbilities() {
        return abilities;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

}
