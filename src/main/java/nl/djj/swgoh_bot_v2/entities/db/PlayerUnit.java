package nl.djj.swgoh_bot_v2.entities.db;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DJJ
 */
public class PlayerUnit {
    private final transient int allycode;
    private final transient int guildId;
    private final transient String baseId;
    private final transient int rarity;
    private final transient int galacticPower;
    private final transient int gear;
    private final transient int relic;
    private final transient int speed;
    private final transient List<UnitAbility> abilities;

    /**
     * Constructor.
     *
     * @param allycode      allycode.
     * @param guildId       guild id.
     * @param baseId        the base id.
     * @param rarity        the star level.
     * @param galacticPower the galactic power.
     * @param gear          the gear level.
     * @param relic         the relic level.
     * @param speed         the base speed.
     */
    public PlayerUnit(final int allycode, final int guildId, final String baseId, final int rarity, final int galacticPower, final int gear, final int relic, final int speed) {
        this.allycode = allycode;
        this.guildId = guildId;
        this.baseId = baseId;
        this.rarity = rarity;
        this.galacticPower = galacticPower;
        this.gear = gear;
        this.relic = relic;
        this.speed = speed;
        this.abilities = new ArrayList<>();
    }


    public int getAllycode() {
        return allycode;
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
}