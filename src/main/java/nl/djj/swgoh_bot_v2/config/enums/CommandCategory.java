package nl.djj.swgoh_bot_v2.config.enums;

/**
 * @author DJJ
 */
public enum CommandCategory {
    SWGOH("SWGOH"),
    BOT("Bot"),
    ADMIN("Admin"),
    MODERATION("Moderation");

    private final String name;

    CommandCategory(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
