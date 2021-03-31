package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;

/**
 * @author DJJ
 */
public class UnitImpl {
    private final transient DatabaseHandler dbHandler;

    /**
     * Constructor.
     *
     * @param dbHandler the DB connection.
     */
    public UnitImpl(final DatabaseHandler dbHandler) {
        super();
        this.dbHandler = dbHandler;
    }

    /**
     * Returns a name for the given ID.
     * @param id the ID.
     * @return the Name.
     */
    public String getUnitNameForId(final String id) {
        try {
            return dbHandler.getUnitNameForId(id);
        } catch (final SQLRetrieveError error) {
            return id;
        }
    }
}
