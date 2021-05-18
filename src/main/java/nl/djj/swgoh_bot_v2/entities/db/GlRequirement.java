package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.config.enums.GalacticLegends;
import nl.djj.swgoh_bot_v2.database.custom_persistors.GalacticLegendsPersister;
import nl.djj.swgoh_bot_v2.database.daos.GLRequirementDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "gl_requirements", daoClass = GLRequirementDaoImpl.class)
public class GlRequirement {
    @DatabaseField(columnName = "id", generatedId = true)
    private transient int identifier;
    @DatabaseField(uniqueCombo = true, columnName = "gl_event", persisterClass = GalacticLegendsPersister.class)
    private transient GalacticLegends glEvent;
    @DatabaseField(uniqueCombo = true, columnName = "base_id")
    private transient String baseId;
    @DatabaseField(columnName = "gear_level")
    private transient int gearLevel;
    @DatabaseField(columnName = "relic_level")
    private transient int relicLevel;

    /**
     * Constructor.
     **/
    public GlRequirement() {

    }

    /**
     * Constructor.
     * @param glEvent the GL Event.
     * @param baseId the unit baseId.
     * @param gearLevel the required gearLevel.
     * @param relicLevel the required relicLevel.
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

    public String getBaseId() {
        return baseId;
    }

    public int getGearLevel() {
        return gearLevel;
    }

    public int getRelicLevel() {
        return relicLevel;
    }

    public int getIdentifier() {
        return identifier;
    }
}
