package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.FarmingLocationDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "farmingLocations", daoClass = FarmingLocationDaoImpl.class)
public class FarmingLocation {
    @DatabaseField(generatedId = true)
    private transient int identifier;
    @DatabaseField(foreign = true)
    private transient Unit unit;
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
     * @param identifier the identifier.
     * @param unit the unit.
     * @param location the location.
     * @param node the node (if applicable).
     * @param preferred if it's preferred.
     */
    public FarmingLocation(final int identifier, final Unit unit, final String location, final String node, final boolean preferred) {
        this.identifier = identifier;
        this.unit = unit;
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
