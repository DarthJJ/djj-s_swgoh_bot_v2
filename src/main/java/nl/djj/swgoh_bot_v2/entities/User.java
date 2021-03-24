package nl.djj.swgoh_bot_v2.entities;

import nl.djj.swgoh_bot_v2.config.Permission;

/**
 * @author DJJ
 */
public class User {
    private final String allycode;
    private final Permission permission;
    private final String username;
    private final String discordId;

    /**
     * @param allycode  the allycode of the user.
     * @param permLevel the permission level associated by the user.
     * @param username  the username of the user.
     * @param discordId the discordId of the user.
     */
    public User(final String allycode, final int permLevel, final String username, final String discordId) {
        super();
        this.allycode = allycode;
        this.permission = Permission.valueOf(permLevel);
        this.username = username;
        this.discordId = discordId;
    }

    public String getAllycode() {
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
}
