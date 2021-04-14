package nl.djj.swgoh_bot_v2.entities.compare;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DJJ
 */
public class ProfileCompare {
    private transient String name = "";
    private transient String guild = "";
    private transient int galacticPower;
    private transient int toonGp;
    private transient int shipGp;
    private transient int zetas;
    private transient int g13;
    private transient int g12;
    private final transient Map<Integer, Integer> relics;
    private final transient Map<String, UnitCompare> units;

    /**
     * The Constructor.
     */
    public ProfileCompare() {
        super();
        relics = new ConcurrentHashMap<>();
        units = new ConcurrentHashMap<>();
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setGuild(final String guild) {
        this.guild = guild;
    }

    public void setGalacticPower(final int galacticPower) {
        this.galacticPower = galacticPower;
    }

    public void setToonGp(final int toonGp) {
        this.toonGp = toonGp;
    }

    public void setShipGp(final int shipGp) {
        this.shipGp = shipGp;
    }

    /**
     * Updates the zeta count.
     * @param amount the amount to add.
     */
    public void addZeta(final int amount) {
        this.zetas = this.zetas + amount;
    }

    /**
     * Adds 1 to the G13 counter.
     */
    public void addG13() {
        this.g13++;
    }

    /**
     * Updates the relic count.
     * @param relicLevel the relic level to add.
     */
    public void addRelic(final int relicLevel) {
        final int realRelicLevel = Math.max(-1, relicLevel - 2);
        relics.merge(realRelicLevel, 1, Integer::sum);
    }

    /**
     * Adds a unit to the compare list.
     * @param unitData the data of the unit.
     */
    public void addUnit(final JSONObject unitData) {
        final UnitCompare compare = new UnitCompare(unitData.getString("name"),
                unitData.getString("base_id"),
                unitData.getInt("rarity"),
                unitData.getInt("power"),
                unitData.getInt("gear_level"),
                unitData.getInt("relic_tier"),
                unitData.getJSONArray("zeta_abilities").length(),
                unitData.getJSONObject("stats").getInt("5"));
        units.put(compare.getBaseId(), compare);
    }

    /**
     * Adds 1 to the G12 counter.
     */
    public void addG12() {
        this.g12++;
    }

    public String getName() {
        return name;
    }

    public String getGuild() {
        return guild;
    }

    public int getGalacticPower() {
        return galacticPower;
    }

    public int getToonGp() {
        return toonGp;
    }

    public int getShipGp() {
        return shipGp;
    }

    public int getZetas() {
        return zetas;
    }

    public int getG13() {
        return g13;
    }

    public int getG12() {
        return g12;
    }

    public Map<Integer, Integer> getRelics() {
        return relics;
    }

    public Map<String, UnitCompare> getUnits() {
        return units;
    }
}
