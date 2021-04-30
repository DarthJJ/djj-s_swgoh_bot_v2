package nl.djj.swgoh_bot_v2.entities.db;

/**
 * @author DJJ
 **/
public class FarmingLocation {
    private final transient String unitId;
    private final transient String type;
    private final transient String location;
    private final transient String node;
    private final transient boolean preferred;

    /**
     * The Constructor.
     * @param unitId the unitId.
     * @param type the type of location.
     * @param location the location.
     * @param node the node (if applicable).
     * @param preferred if this is the preferred location.
     */
    public FarmingLocation(final String unitId, final String type, final String location, final String node, final boolean preferred) {
        this.unitId = unitId;
        this.type = type;
        this.location = location;
        this.node = node;
        this.preferred = preferred;
    }

    public String getUnitId() {
        return unitId;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public String getNode() {
        return node;
    }

    public boolean isPreferred() {
        return preferred;
    }
}
