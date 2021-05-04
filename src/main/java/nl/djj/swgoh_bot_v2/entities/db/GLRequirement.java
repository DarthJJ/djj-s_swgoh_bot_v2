package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.config.GalacticLegends;
import nl.djj.swgoh_bot_v2.database.customPersistors.GalacticLegendsPersister;
import nl.djj.swgoh_bot_v2.database.customPersistors.PermissionPersister;
import nl.djj.swgoh_bot_v2.database.daos.GLRequirementDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "glRequirements", daoClass = GLRequirementDaoImpl.class)
public class GLRequirement {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(uniqueCombo = true, persisterClass = GalacticLegendsPersister.class)
    private GalacticLegends glEvent;
    @DatabaseField(uniqueCombo = true)
    private String baseId;
    @DatabaseField
    private int gearLevel;
    @DatabaseField
    private int relicLevel;

    /**
     * Constructor.
     **/
    public GLRequirement() {

    }

    /**
     * Constructor.
     * @param glEvent the GL Event.
     * @param baseId the unit baseId.
     * @param gearLevel the required gearLevel.
     * @param relicLevel the required relicLevel.
     */
    public GLRequirement(final GalacticLegends glEvent, final String baseId, final int gearLevel, final int relicLevel) {
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
}
