package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.AbbreviationDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "abbreviations", daoClass = AbbreviationDaoImpl.class)
public class Abbreviation {
    @DatabaseField(generatedId = true)
    private transient int identifier;
    @DatabaseField(canBeNull = false, foreign = true, uniqueCombo = true)
    private transient Unit unit;
    @DatabaseField(uniqueCombo = true)
    private transient String abbreviation;
    @DatabaseField(columnName = "unit_name")
    private transient String unitName;

    /**
     * Constructor.
     **/
    public Abbreviation() {

    }

    /**
     * Constructor.
     * @param unit the unit.
     * @param abbreviation the abbreviation for the unit.
     */
    public Abbreviation(final Unit unit, final String abbreviation) {
        this.unit = unit;
        this.abbreviation = abbreviation;
        this.unitName = unit.getName();
    }

    public Unit getUnit() {
        return unit;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getUnitName() {
        return unitName;
    }
}
