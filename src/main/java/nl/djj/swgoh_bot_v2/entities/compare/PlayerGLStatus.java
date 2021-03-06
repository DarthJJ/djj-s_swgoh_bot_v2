package nl.djj.swgoh_bot_v2.entities.compare;

import java.util.List;

/**
 * @author DJJ
 **/
public class PlayerGLStatus {

    private final transient String GlEvent;
    private final transient List<GLUnit> units;
    private final transient double totalCompleteness;

    /**
     * The Constructor.
     * @param glEvent the GL Event.
     * @param units the units.
     * @param totalCompleteness the totalCompleteness.
     */
    public PlayerGLStatus(final String glEvent, final List<GLUnit> units, final double totalCompleteness) {
        this.GlEvent = glEvent;
        this.units = units;
        this.totalCompleteness = totalCompleteness;
    }

    public String getGlEvent() {
        return GlEvent;
    }

    public List<GLUnit> getUnits() {
        return units;
    }

    public double getTotalCompleteness() {
        return totalCompleteness;
    }
}
