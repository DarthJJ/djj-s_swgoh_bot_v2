package nl.djj.swgoh_bot_v2.entities.db;

import nl.djj.swgoh_bot_v2.config.enums.Permission;

/**
 * @author DJJ
 */
public class User {
    private final int allycode;
    private final Permission permission;
    private final String username;
    private final String discordId;
    private final boolean allowedToCreateTickets;

    /**
     * @param allycode    the allycode of the user.
     * @param permission  the permission associated by the user.
     * @param username    the username of the user.
     * @param discordId   the discordId of the user.
     * @param allowedToCreateTickets If the user can create tickets on github.
     */
    public User(final int allycode, final Permission permission, final String username, final String discordId, final boolean allowedToCreateTickets) {
        super();
        this.allycode = allycode;
        this.permission = permission;
        this.username = username;
        this.discordId = discordId;
        this.allowedToCreateTickets = allowedToCreateTickets;
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

    public boolean isAllowedToCreateTickets() {
        return allowedToCreateTickets;
    }
}
