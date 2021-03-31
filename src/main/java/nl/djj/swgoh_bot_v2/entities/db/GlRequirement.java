package nl.djj.swgoh_bot_v2.entities.db;

import org.json.JSONObject;

/**
 * @author DJJ
 */
public class GlRequirement {
    private transient String glEvent;
    private transient String baseId;
    private transient int gearLevel;
    private transient int relicLevel;

    /**
     * The constructor.
     */
    public GlRequirement() {
        super();
    }

    public String getGlEvent() {
        return glEvent;
    }

    public void setGlEvent(final String glEvent) {
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
        requirement.setGlEvent(glEvent);
        requirement.setGearLevel(data.getInt("gearLevel"));
        requirement.setRelicLevel(data.getInt("relicTier"));
        return requirement;
    }
}
