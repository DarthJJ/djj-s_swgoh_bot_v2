package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.FarmingLocationDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "farming_locations", daoClass = FarmingLocationDaoImpl.class)
public class FarmingLocation {
    @DatabaseField(columnName = "id", generatedIdSequence = "farming_locations_id_seq")
    private transient int identifier;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private transient Unit unit;
    @DatabaseField
    private transient String type;
    @DatabaseField
    private transient String location;
    @DatabaseField
    private transient String node;
    @DatabaseField
    private transient boolean preferred;

    /**
     * Constructor.
     **/
    public FarmingLocation() {

    }

    /**
     * Constructor.
     *
     * @param unit      the unit.
     * @param type      the farming type.
     * @param location  the location.
     * @param node      the node (if applicable).
     * @param preferred if it's preferred.
     */
    public FarmingLocation(final Unit unit, final String type, final String location, final String node, final boolean preferred) {
        this.unit = unit;
        this.type = type;
        this.location = location;
        this.node = node;
        this.preferred = preferred;
    }

    public int getIdentifier() {
        return identifier;
    }

    public Unit getUnit() {
        return unit;
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
