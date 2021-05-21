package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.FlagDao;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "flags", daoClass = FlagDao.class)
public class Flag {
    @DatabaseField(id = true)
    private transient String identifier;
    @DatabaseField(uniqueCombo = true)
    private transient String name;
    @DatabaseField(uniqueCombo = true, columnName = "parent_command")
    private transient String parentCommand;
    @DatabaseField
    private transient boolean enabled;

    /**
     * Constructor.
     **/
    public Flag() {

    }

    /**
     * Constructor.
     *
     * @param name          the name of the flag.
     * @param parentCommand the parent command.
     * @param enabled       the enabled status.
     */
    public Flag(final String name, final String parentCommand, final boolean enabled) {
        this.identifier = parentCommand + "_" + name;
        this.name = name;
        this.parentCommand = parentCommand;
        this.enabled = enabled;
    }


    public String getName() {
        return name;
    }

    public String getParentCommand() {
        return parentCommand;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
