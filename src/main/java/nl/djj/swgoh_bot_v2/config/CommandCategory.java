package nl.djj.swgoh_bot_v2.config;

/**
 * @author DJJ
 */
public enum CommandCategory {
    SWGOH("SWGOH"),
    BOT("Bot"),
    ADMIN("Admin");

    private final String name;

    CommandCategory(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
