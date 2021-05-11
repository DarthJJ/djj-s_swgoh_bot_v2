package nl.djj.swgoh_bot_v2.entities.compare;

import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DJJ
 */
public class ProfileCompare {
    private transient String name = "";
    private transient String guild = "";
    private transient int galacticPower;
    private transient int zetas;
    private transient int g13;
    private transient int g12;
    private transient Map<Integer, Integer> relics;
    private final transient Map<String, UnitCompare> units;

    /**
     * The Constructor.
     */
    public ProfileCompare() {
        super();
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

    public void setZetas(final int zetas) {
        this.zetas = zetas;
    }

    public void setG13(final int g13) {
        this.g13 = g13;
    }

    public void setG12(final int g12) {
        this.g12 = g12;
    }

    public void setRelics(final Map<Integer, Integer> relics) {
        this.relics = relics;
    }

    /**
     * Adds a unit to the compare list.
     *
     * @param unitData the data of the unit.
     * @param baseId   the unit baseId.
     */
    public void addUnit(final PlayerUnit unitData, final String baseId) {
        final UnitCompare compare;
        if (unitData == null) {
            compare = new UnitCompare("", baseId, -1, -1, -1, -1, -1, -1);
        } else {
            compare = new UnitCompare(unitData.getUnit().getName(),
                    unitData.getUnit().getBaseId(),
                    unitData.getRarity(),
                    unitData.getGalacticPower(),
                    unitData.getGear(),
                    unitData.getRelic(),
                    0, //TODO: replace with actual value.
                    unitData.getSpeed());
        }
        units.put(baseId, compare);
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
