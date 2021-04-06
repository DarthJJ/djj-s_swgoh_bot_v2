package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Unit;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
     * @param identifier the ID.
     * @return the Name.
     */
    public String getUnitNameForId(final String identifier) {
        try {
            return dbHandler.getUnitNameForId(identifier);
        } catch (final SQLRetrieveError error) {
            return identifier;
        }
    }

    /**
     * Checks the relics levels for a list of units.
     * @param units the unit list.
     * @param relicLevel the relic level.
     * @return a list with toons who have failed the check.
     */
    public Map<String, Integer> checkRelicLevel(final List<Unit> units, final int relicLevel) {
        final Map<String, Integer> unitsBelow = new ConcurrentHashMap<>();
        for (final Unit unit : units) {
            if (unit.getGearLevel() >= SwgohConstants.MAX_GEAR_LEVEL && unit.getRelicLevel() - 2 <= relicLevel) {
                unitsBelow.put(unit.getName(), unit.getRelicLevel() - 2);
            }
        }
        return unitsBelow;
    }
}
