package nl.djj.swgoh_bot_v2.entities.db;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DJJ
 */
public class PlayerUnit {
    private final transient int allycode;
    private final transient int level;
    private final transient int guildId;
    private final transient String baseId;
    private final transient int rarity;
    private final transient int galacticPower;
    private final transient int gear;
    private final transient int gearPieces;
    private final transient int relic;
    private final transient int speed;
    private final transient List<UnitAbility> abilities;
    private transient int zetaCount;

    /**
     * Constructor.
     *
     * @param allycode      allycode.
     * @param guildId       guild id.
     * @param baseId        the base id.
     * @param rarity        the star level.
     * @param galacticPower the galactic power.
     * @param gear          the gear level.
     * @param gearPieces    the amount of gearPieces.
     * @param relic         the relic level.
     * @param speed         the base speed.
     */
    public PlayerUnit(final int allycode, final int level, final int guildId, final String baseId, final int rarity, final int galacticPower, final int gear, final int gearPieces, final int relic, final int speed) {
        this.allycode = allycode;
        this.level = level;
        this.guildId = guildId;
        this.baseId = baseId;
        this.rarity = rarity;
        this.galacticPower = galacticPower;
        this.gear = gear;
        this.gearPieces = gearPieces;
        this.relic = relic;
        this.speed = speed;
        this.abilities = new ArrayList<>();
    }


    public int getAllycode() {
        return allycode;
    }

    public int getLevel() {
        return level;
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

    public int getGearPieces() {
        return gearPieces;
    }

    public int getRelic() {
        return relic;
    }

    public int getSpeed() {
        return speed;
    }

    public int getGuildId() {
        return guildId;
    }

    /**
     * Add an ability to this unit.
     * @param ability the ability to add.
     */
    public void addAbility(final UnitAbility ability) {
        this.abilities.add(ability);
    }

    public List<UnitAbility> getAbilities() {
        return this.abilities;
    }

    public long getZetaCount(){
        return zetaCount;
    }

    public void setZetaCount(final int zetaCount) {
        this.zetaCount = zetaCount;
    }
}
