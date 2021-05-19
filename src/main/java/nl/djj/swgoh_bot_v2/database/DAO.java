package nl.djj.swgoh_bot_v2.database;

import com.j256.ormlite.support.ConnectionSource;
import nl.djj.swgoh_bot_v2.database.daos.*;

import java.sql.SQLException;

/**
 * @author DJJ
 **/
public class DAO {
    private final transient AbilityDao ability;
    private final transient UnitDao unit;
    private final transient AbbreviationDao abbreviation;
    private final transient FarmingLocationDao farmingLocation;
    private final transient GuildDao guild;
    private final transient PlayerDao player;
    private final transient PlayerUnitDao playerUnit;
    private final transient UnitAbilityDao unitAbility;
    private final transient ConfigDao config;
    private final transient PresenceDao presence;
    private final transient GLRequirementDao glRequirement;
    private final transient CommandDao command;
    private final transient CommandUsageDao commandUsage;

    /**
     * Constructor.
     *
     * @param connection the connectionSource.
     **/
    public DAO(final ConnectionSource connection) throws SQLException {
        ability = new AbilityDaoImpl(connection);
        unit = new UnitDaoImpl(connection);
        abbreviation = new AbbreviationDaoImpl(connection);
        farmingLocation = new FarmingLocationDaoImpl(connection);
        guild = new GuildDaoImpl(connection);
        player = new PlayerDaoImpl(connection);
        playerUnit = new PlayerUnitDaoImpl(connection);
        unitAbility = new UnitAbilityDaoImpl(connection);
        unitAbility.setObjectCache(true);
        config = new ConfigDaoImpl(connection);
        presence = new PresenceDaoImpl(connection);
        glRequirement = new GLRequirementDaoImpl(connection);
        command = new CommandDaoImpl(connection);
        commandUsage = new CommandUsageDaoImpl(connection);

    }

    /**
     * @return the Ability DAO.
     */
    public AbilityDao abilityDao() {
        return ability;
    }

    /**
     * @return the Unit DAO.
     */
    public UnitDao unitDao() {
        return unit;
    }

    /**
     * @return the Abbreviation DAO.
     */
    public AbbreviationDao abbreviationDao() {
        return abbreviation;
    }

    /**
     * @return the FarmingLocation DAO.
     */
    public FarmingLocationDao farmingLocationDao() {
        return farmingLocation;
    }

    /**
     * @return the Guild DAO.
     */
    public GuildDao guildDao() {
        return guild;
    }

    /**
     * @return the Player DAO.
     */
    public PlayerDao playerDao() {
        return player;
    }

    /**
     * @return the PlayerUnit DAO.
     */
    public PlayerUnitDao playerUnitDao() {
        return playerUnit;
    }

    /**
     * @return the UnitAbility DAO.
     */
    public UnitAbilityDao unitAbilityDao() {
        return unitAbility;
    }

    /**
     * @return the Config DAO.
     */
    public ConfigDao configDao() {
        return config;
    }

    /**
     * @return the Presence DAO.
     */
    public PresenceDao presenceDao() {
        return presence;
    }

    /**
     * @return the GL Requirement DAO.
     */
    public GLRequirementDao glRequirementDao() {
        return glRequirement;
    }

    /**
     * @return the Command DAO.
     */
    public CommandDao commandDao() {
        return command;
    }

    /**
     * @return the CommandUsage DAO.
     */
    public CommandUsageDao commandUsageDao() {
        return commandUsage;
    }
}
