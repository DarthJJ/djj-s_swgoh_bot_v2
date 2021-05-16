package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.djj.swgoh_bot_v2.database.daos.CommandUsageDaoImpl;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "commandUsage", daoClass = CommandUsageDaoImpl.class)
public class CommandUsage {
    @DatabaseField(columnName = "id", generatedId = true)
    private transient int identifier;
    @DatabaseField(uniqueCombo = true)
    private transient String commandName;
    @DatabaseField(uniqueCombo = true)
    private transient String flagName;
    @DatabaseField
    private transient int usage;

    /**
     * Constructor.
     **/
    public CommandUsage() {

    }

    /**
     * Constructor.
     * @param commandName the commandName;
     * @param flagName the commandFlag;
     */
    public CommandUsage(final String commandName, final String flagName) {
        this.commandName = commandName;
        this.flagName = flagName;
        this.usage = 1;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getFlagName() {
        return flagName;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(final int usage) {
        this.usage = usage;
    }

    public int getIdentifier() {
        return identifier;
    }
}
