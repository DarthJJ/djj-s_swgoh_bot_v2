package nl.djj.swgoh_bot_v2.entities;

import org.json.JSONObject;

/**
 * @author DJJ
 */
public final class Unit {
    private final transient String baseId;
    private final transient String name;
    private final transient int gearLevel;
    private final transient int relicLevel;
    private final transient int level;
    private final transient int rarity;

    /**
     * Constructor.
     *
     * @param baseId     the base id.
     * @param name       the name.
     * @param gearLevel  the gear level.
     * @param relicLevel the relic level.
     * @param level      the level.
     * @param rarity     the rarity.
     */
    private Unit(final String baseId, final String name, final int gearLevel, final int relicLevel, final int level, final int rarity) {
        super();
        this.baseId = baseId;
        this.name = name;
        this.gearLevel = gearLevel;
        this.relicLevel = relicLevel;
        this.level = level;
        this.rarity = rarity;
    }

    public String getBaseId() {
        return baseId;
    }

    public String getName() {
        return name;
    }

    public int getGearLevel() {
        return gearLevel;
    }

    public int getRelicLevel() {
        return relicLevel;
    }

    public int getLevel() {
        return level;
    }

    public int getRarity() {
        return rarity;
    }

    /**
     * Init from JSON.
     *
     * @param unitData JSON data.
     * @return unit object.
     */
    public static Unit initFromJson(final JSONObject unitData) {
        final String baseId = unitData.getString("base_id");
        final String name = unitData.getString("name");
        final int gearLevel = unitData.getInt("gear_level");
        final int relicLevel = unitData.getInt("relic_tier");
        final int level = unitData.getInt("level");
        final int rarity = unitData.getInt("rarity");
        return new Unit(baseId, name, gearLevel, relicLevel, level, rarity);
    }
}
