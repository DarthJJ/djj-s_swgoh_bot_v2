package nl.djj.swgoh_bot_v2.entities.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import nl.djj.swgoh_bot_v2.config.enums.Permission;
import nl.djj.swgoh_bot_v2.database.custom_persistors.LocalDateTimePersister;
import nl.djj.swgoh_bot_v2.database.custom_persistors.PermissionPersister;
import nl.djj.swgoh_bot_v2.database.daos.PlayerDaoImpl;

import java.time.LocalDateTime;

/**
 * @author DJJ
 **/
@DatabaseTable(tableName = "players", daoClass = PlayerDaoImpl.class)
public class Player {
    @DatabaseField(id = true)
    private transient int allycode;
    @DatabaseField(unique = true, columnName = "discord_id")
    private transient String discordId;
    @DatabaseField
    private transient String name;
    @DatabaseField(columnName = "galactic_power")
    private transient int galacticPower;
    @DatabaseField
    private transient String url;
    @DatabaseField(persisterClass = LocalDateTimePersister.class, columnName = "last_updated")
    private transient LocalDateTime lastUpdated;
    @DatabaseField(persisterClass = LocalDateTimePersister.class, columnName = "last_updated_swgoh")
    private transient LocalDateTime lastUpdatedSwgoh;
    @ForeignCollectionField(columnName = "player_units")
    private transient ForeignCollection<PlayerUnit> playerUnits;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private transient Guild guild;
    @DatabaseField(columnName = "guild_name")
    private transient String guildName;
    @DatabaseField(persisterClass = PermissionPersister.class)
    private transient Permission permission;
    @DatabaseField
    private transient String username;
    @DatabaseField(columnName = "allowed_to_create_tickets")
    private transient boolean allowedToCreateTickets;

    /**
     * Constructor.
     **/
    public Player() {
        super();
    }

    /**
     * Constructor.
     * @param allycode allycode of the user.
     * @param username the username of the user.
     */
    public Player(final int allycode, final String username) {
        this.allycode = allycode;
        this.username = username;
        this.permission = Permission.USER;
        this.allowedToCreateTickets = true;
    }

    /**
     * Constructor.
     * @param allycode the allycode of the user.
     * @param username the username of the user.
     * @param discordId the discord id of the user.
     * @param permission the permission level of the user.
     */
    public Player(final int allycode, final String username, final String discordId, final Permission permission) {
        super();
        this.allycode = allycode;
        this.username = username;
        this.discordId = discordId;
        this.permission = permission;
        this.allowedToCreateTickets = true;

    }

    /**
     * Constructor.
     * @param allycode the allycode of the user.
     * @param name the name of the user.
     * @param galacticPower the GP of the user.
     * @param url the swgoh url of the user.
     * @param lastUpdated the last updated in DB.
     * @param lastUpdatedSwgoh the last updated on swgoh.gg.
     */
    public Player(final int allycode, final String name, final int galacticPower, final String url, final LocalDateTime lastUpdated, final LocalDateTime lastUpdatedSwgoh) {
        super();
        this.allycode = allycode;
        this.name = name;
        this.galacticPower = galacticPower;
        this.url = url;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedSwgoh = lastUpdatedSwgoh;
    }

    public int getAllycode() {
        return allycode;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getDiscordId() {
        return discordId;
    }

    public String getName() {
        return name;
    }

    public int getGalacticPower() {
        return galacticPower;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public LocalDateTime getLastUpdatedSwgoh() {
        return lastUpdatedSwgoh;
    }

    public ForeignCollection<PlayerUnit> getPlayerUnits() {
        return playerUnits;
    }

    public Guild getGuild() {
        return guild;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAllowedToCreateTickets() {
        return allowedToCreateTickets;
    }

    public void setAllycode(final int allycode) {
        this.allycode = allycode;
    }

    public void setDiscordId(final String discordId) {
        this.discordId = discordId;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setGalacticPower(final int galacticPower) {
        this.galacticPower = galacticPower;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public void setLastUpdated(final LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setLastUpdatedSwgoh(final LocalDateTime lastUpdatedSwgoh) {
        this.lastUpdatedSwgoh = lastUpdatedSwgoh;
    }

    public void setPlayerUnits(final ForeignCollection<PlayerUnit> playerUnits) {
        this.playerUnits = playerUnits;
    }

    public void setGuild(final Guild guild) {
        this.guild = guild;
    }

    public void setPermission(final Permission permission) {
        this.permission = permission;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setAllowedToCreateTickets(final boolean allowedToCreateTickets) {
        this.allowedToCreateTickets = allowedToCreateTickets;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(final String guildName) {
        this.guildName = guildName;
    }
}
