package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.SwgohConfig;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Unit;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     *
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

    /**
     * Checks the relics levels for a list of units.
     * @param units the unit list.
     * @param relicLevel the relic level.
     * @return a list with toons who have failed the check.
     */
    public Map<String, Integer> checkRelicLevel(final List<Unit> units, final int relicLevel) {
        final Map<String, Integer> unitsBelow = new HashMap<>();
        for (final Unit unit : units) {
            if (unit.getGearLevel() >= SwgohConfig.MAX_GEAR_LEVEL && unit.getRelicLevel() - 2 <= relicLevel) {
                unitsBelow.put(unit.getName(), unit.getRelicLevel() - 2);
            }
        }
        return unitsBelow;
    }
}
