package nl.djj.swgoh_bot_v2.entities.db;

import nl.djj.swgoh_bot_v2.config.Permission;

import java.time.LocalDateTime;

/**
 * @author DJJ
 */
public class User {
    private final int allycode;
    private final Permission permission;
    private final String username;
    private final String discordId;
    private final LocalDateTime lastUpdated;

    /**
     * @param allycode    the allycode of the user.
     * @param permLevel   the permission level associated by the user.
     * @param username    the username of the user.
     * @param discordId   the discordId of the user.
     * @param lastUpdated the lastUpdated date.
     */
    public User(final int allycode, final int permLevel, final String username, final String discordId, final LocalDateTime lastUpdated) {
        super();
        this.allycode = allycode;
        this.permission = Permission.valueOf(permLevel);
        this.username = username;
        this.discordId = discordId;
        this.lastUpdated = lastUpdated;
    }

    /**
     * @param allycode    the allycode of the user.
     * @param permission  the permission associated by the user.
     * @param username    the username of the user.
     * @param discordId   the discordId of the user.
     * @param lastUpdated the lastUpdated date.
     */
    public User(final int allycode, final Permission permission, final String username, final String discordId, final LocalDateTime lastUpdated) {
        super();
        this.allycode = allycode;
        this.permission = permission;
        this.username = username;
        this.discordId = discordId;
        this.lastUpdated = lastUpdated;
    }

    public int getAllycode() {
        return allycode;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getUsername() {
        return username;
    }

    public String getDiscordId() {
        return discordId;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
