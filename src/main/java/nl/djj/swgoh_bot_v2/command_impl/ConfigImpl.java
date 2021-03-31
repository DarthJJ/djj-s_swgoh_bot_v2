package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.database.DatabaseHandler;

/**
 * @author DJJ
 */
public class ConfigImpl {
    private final transient DatabaseHandler dbHandler;

    /**
     * Constructor.
     *
     * @param dbHandler the DB Connection
     */
    public ConfigImpl(final DatabaseHandler dbHandler) {
        super();
        this.dbHandler = dbHandler;
    }

    /**
     * Gets the ignore role for the guild.
     * @param guildId the guild.
     * @return the role or an empty string.
     */
    public String getIgnoreRole(final String guildId) {
        return ""; //TODO: Implement correct
    }
}
