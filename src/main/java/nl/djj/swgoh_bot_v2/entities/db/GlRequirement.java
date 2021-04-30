package nl.djj.swgoh_bot_v2.entities.db;

import nl.djj.swgoh_bot_v2.config.enums.GalacticLegends;
import org.json.JSONObject;

/**
 * @author DJJ
 */
public class GlRequirement {
    private transient GalacticLegends glEvent;
    private transient String baseId;
    private transient int gearLevel;
    private transient int relicLevel;

    /**
     * The constructor.
     */
    public GlRequirement() {
        super();
    }

    /**
     * The Constructor.
     * @param glEvent the event.
     * @param baseId the unit baseID.
     * @param gearLevel the gear level.
     * @param relicLevel the relic level.
     */
    public GlRequirement(final GalacticLegends glEvent, final String baseId, final int gearLevel, final int relicLevel) {
        this.glEvent = glEvent;
        this.baseId = baseId;
        this.gearLevel = gearLevel;
        this.relicLevel = relicLevel;
    }

    public GalacticLegends getGlEvent() {
        return glEvent;
    }

    public void setGlEvent(final GalacticLegends glEvent) {
        this.glEvent = glEvent;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(final String baseId) {
        this.baseId = baseId;
    }

    public int getGearLevel() {
        return gearLevel;
    }

    public void setGearLevel(final int gearLevel) {
        this.gearLevel = gearLevel;
    }

    public int getRelicLevel() {
        return relicLevel;
    }

    public void setRelicLevel(final int relicLevel) {
        this.relicLevel = relicLevel;
    }

    /**
     * Inits an GL Requirement object from json data.
     * @param data the json data.
     * @param glEvent the name of the GL.
     * @return a GL Requirement object.
     */
    public static GlRequirement initFromJson(final JSONObject data, final String glEvent) {
        final GlRequirement requirement = new GlRequirement();
        requirement.setBaseId(data.getString("baseId"));
        requirement.setGlEvent(GalacticLegends.getByName(glEvent));
        requirement.setGearLevel(data.getInt("gearLevel"));
        requirement.setRelicLevel(data.getInt("relicTier"));
        return requirement;
    }
}
