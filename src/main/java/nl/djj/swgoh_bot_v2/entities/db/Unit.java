package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.UnitDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "units", daoClass = UnitDaoImpl.class)
public class Unit {
    @DatabaseField(id = true)
    private transient String baseId;
    @DatabaseField
    private transient String name;
    @DatabaseField
    private transient String alignment;
    @DatabaseField
    private transient boolean character;
    @ForeignCollectionField()
    private transient ForeignCollection<Ability> abilities;
    @ForeignCollectionField()
    private transient ForeignCollection<Abbreviation> abbreviations;
    @ForeignCollectionField()
    private transient ForeignCollection<FarmingLocation> farmingLocations;

    /**
     * Constructor.
     **/
    public Unit() {

    }

    /**
     * Constructor.
     *
     * @param baseId    the base id of the toon.
     * @param name      the name of the toon.
     * @param alignment the alignment of the unit.
     * @param character if the char is a toon.
     */
    public Unit(final String baseId, final String name, final String alignment, final boolean character) {
        super();
        this.baseId = baseId;
        this.name = name;
        this.alignment = alignment;
        this.character = character;
    }

    public String getBaseId() {
        return baseId;
    }

    public String getName() {
        return name;
    }

    public String getAlignment() {
        return alignment;
    }

    public boolean isCharacter() {
        return character;
    }

    public ForeignCollection<Ability> getAbilities() {
        return abilities;
    }

    public ForeignCollection<Abbreviation> getAbbreviations() {
        return abbreviations;
    }

    public ForeignCollection<FarmingLocation> getFarmingLocations() {
        return farmingLocations;
    }
}
