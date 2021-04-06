package nl.djj.swgoh_bot_v2.entities;

import org.json.JSONObject;

/**
 * @author DJJ
 */
public final class SwgohGuild {
    private final transient String identifier;

    private SwgohGuild(final String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Init from JSON data.
     * @param guildData the JSON data.
     * @return the SWGOH Guild object.
     */
    public static SwgohGuild initFromJson(final JSONObject guildData) {
        return new SwgohGuild("5039");
    }
}
