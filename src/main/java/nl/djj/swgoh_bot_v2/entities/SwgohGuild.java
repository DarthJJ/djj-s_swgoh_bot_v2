package nl.djj.swgoh_bot_v2.entities;

import org.json.JSONObject;

public final class SwgohGuild {
    private final transient String id;

    private SwgohGuild(final String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public static SwgohGuild initFromJson(final JSONObject guildData) {
        return new SwgohGuild("5039");
    }
}
