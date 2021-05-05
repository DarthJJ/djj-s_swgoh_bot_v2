package nl.djj.swgoh_bot_v2.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.database.daos.*;
import nl.djj.swgoh_bot_v2.entities.db.Config;

import java.sql.SQLException;

/**
 * @author DJJ
 **/
public class DAO {
    private final transient AbilityDao abilityDao;
    private final transient UnitDao unitDao;
    private final transient AbbreviationDao abbreviationDao;
    private final transient FarmingLocationDao farmingLocationDao;
    private final transient GuildDao guildDao;
    private final transient PlayerDao playerDao;
    private final transient PlayerUnitDao playerUnitDao;
    private final transient UnitAbilityDao unitAbilityDao;
    private final transient ConfigDao configDao;
    private final transient PresenceDao presenceDao;
    private final transient GLRequirementDao glRequirementDao;
    private final transient CommandDao commandDao;
    private final transient CommandUsageDao commandUsageDao;

    /**
     * Constructor.
     *
     * @param connection the connectionSource.
     **/
    public DAO(final ConnectionSource connection) throws SQLException {
        abilityDao = new AbilityDaoImpl(connection);
        unitDao = new UnitDaoImpl(connection);
        abbreviationDao = new AbbreviationDaoImpl(connection);
        farmingLocationDao = new FarmingLocationDaoImpl(connection);
        guildDao = new GuildDaoImpl(connection);
        playerDao = new PlayerDaoImpl(connection);
        playerUnitDao = new PlayerUnitDaoImpl(connection);
        unitAbilityDao = new UnitAbilityDaoImpl(connection);
        configDao = new ConfigDaoImpl(connection);
        presenceDao = new PresenceDaoImpl(connection);
        glRequirementDao = new GLRequirementDaoImpl(connection);
        commandDao = new CommandDaoImpl(connection);
        commandUsageDao = new CommandUsageDaoImpl(connection);

    }

    /**
     * @return the Ability DAO.
     */
    public AbilityDao abilityDao() {
        return abilityDao;
    }

    /**
     * @return the Unit DAO.
     */
    public UnitDao unitDao() {
        return unitDao;
    }

    /**
     * @return the Abbreviation DAO.
     */
    public AbbreviationDao abbreviationDao() {
        return abbreviationDao;
    }

    /**
     * @return the FarmingLocation DAO.
     */
    public FarmingLocationDao farmingLocationDao() {
        return farmingLocationDao;
    }

    /**
     * @return the Guild DAO.
     */
    public GuildDao guildDao() {
        return guildDao;
    }

    /**
     * @return the Player DAO.
     */
    public PlayerDao playerDao() {
        return playerDao;
    }

    /**
     * @return the PlayerUnit DAO.
     */
    public PlayerUnitDao playerUnitDao() {
        return playerUnitDao;
    }

    /**
     * @return the UnitAbility DAO.
     */
    public UnitAbilityDao unitAbilityDao() {
        return unitAbilityDao;
    }

    /**
     * @return the Config DAO.
     */
    public ConfigDao configDao() {
        return configDao;
    }

    /**
     * @return the Presence DAO.
     */
    public PresenceDao presenceDao() {
        return presenceDao;
    }

    /**
     * @return the GL Requirement DAO.
     */
    public GLRequirementDao glRequirementDao() {
        return glRequirementDao;
    }

    /**
     * @return the Command DAO.
     */
    public CommandDao commandDao() {
        return commandDao;
    }

    /**
     * @return the CommandUsage DAO.
     */
    public CommandUsageDao commandUsageDao() {
        return commandUsageDao;
    }
}
