package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.CommandDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "commands", daoClass = CommandDaoImpl.class)
public class Command {
    @DatabaseField(id = true)
    private transient String name;
    @DatabaseField
    private transient boolean enabled;

    /**
     * Constructor.
     **/
    public Command() {

    }

    /**
     * Constructor.
     * @param name the command name.
     * @param enabled the enabled status.
     */
    public Command(final String name, final boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
