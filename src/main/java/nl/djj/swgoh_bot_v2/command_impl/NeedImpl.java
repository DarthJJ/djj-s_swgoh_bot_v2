package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.db.FarmingLocation;
import nl.djj.swgoh_bot_v2.entities.db.Player;
import nl.djj.swgoh_bot_v2.entities.db.PlayerUnit;
import nl.djj.swgoh_bot_v2.entities.db.Unit;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;
import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.MessageHelper;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author DJJ
 **/
public class NeedImpl extends BaseImpl {
    private final transient ImplHelper implHelper;

    /**
     * Constructor.
     *
     * @param logger     the logger.
     * @param dao        the dao.
     * @param implHelper the implHelper
     **/
    public NeedImpl(final Logger logger, final DAO dao, final ImplHelper implHelper) {
        super(logger, dao, NeedImpl.class.getName());
        this.implHelper = implHelper;
    }

    /**
     * Gets the overview for the light side battles.
     *
     * @param message the original message.
     */
    public void lightSide(final Message message) {
        final String locationName = "Light Side Battles";
        try {
            final List<FarmingLocation> locations = dao.farmingLocationDao().getByLocation(locationName);
            calculateNeed(message, locationName, locations);
        } catch (final RetrieveError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Gets the overview for the dark side battles.
     *
     * @param message the original message.
     */
    public void darkSide(final Message message) {
        final String locationName = "Dark Side Battles";
        try {
            final List<FarmingLocation> locations = dao.farmingLocationDao().getByLocation(locationName);
            calculateNeed(message, locationName, locations);
        } catch (final RetrieveError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Gets the overview for the fleet battles.
     *
     * @param message the original message.
     */
    public void fleet(final Message message) {
        final String locationName = "Fleet Battles";
        try {
            final List<FarmingLocation> locations = dao.farmingLocationDao().getByLocation(locationName);
            calculateNeed(message, locationName, locations);
        } catch (final RetrieveError error) {
            message.error(error.getMessage());
        }
    }


    /**
     * Gets the overview for the cantina battles.
     *
     * @param message the original message.
     */
    public void cantina(final Message message) {
        final String locationName = "Cantina Battles";
        try {
            final List<FarmingLocation> locations = dao.farmingLocationDao().getByLocation(locationName);
            calculateNeed(message, locationName, locations);
        } catch (final RetrieveError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Gets the overview for all units.
     *
     * @param message the original message.
     */
    public void all(final Message message) {
        try {
            final Player player = implHelper.getProfileImpl().getAndUpdatePlayer(message.getAllycode());
            final List<Unit> units = dao.unitDao().getAll();
            final Map<String, Integer> result = new TreeMap<>();
            for (final Unit unit : units) {
                final PlayerUnit playerUnit = dao.playerUnitDao().getForPlayer(player, unit.getBaseId());
                if (playerUnit == null) {
                    result.put(unit.getName(), 0);
                } else {
                    if (playerUnit.getRarity() < SwgohConstants.MAX_RARITY_LEVEL) {
                        result.put(unit.getName(), playerUnit.getRarity());
                    }

                }
            }
            final double percentage = ((double) (units.size() - result.size())) / (double) units.size();
            message.done(MessageHelper.formatNeedMessage(result, "all", percentage));
        } catch (final RetrieveError | InsertionError | HttpRetrieveError error) {
            message.error(error.getMessage());
        }
    }

    private void calculateNeed(final Message message, final String locationName, final List<FarmingLocation> locations) {
        try {
            final Player player = implHelper.getProfileImpl().getAndUpdatePlayer(message.getAllycode());
            final Map<String, Integer> result = new TreeMap<>();
            for (final FarmingLocation location : locations) {
                final PlayerUnit unit = dao.playerUnitDao().getForPlayer(player, location.getUnit().getBaseId());
                if (unit == null) {
                    result.put(location.getUnit().getName(), 0);
                } else {
                    if (unit.getRarity() < SwgohConstants.MAX_RARITY_LEVEL) {
                        result.put(location.getUnit().getName(), unit.getRarity());
                    }
                }
            }
            final double percentage = ((double) (locations.size() - result.size())) / (double) locations.size();
            message.done(MessageHelper.formatNeedMessage(result, locationName, percentage));
        } catch (final RetrieveError | InsertionError | HttpRetrieveError error) {
            message.error(error.getMessage());
        }
    }
}
