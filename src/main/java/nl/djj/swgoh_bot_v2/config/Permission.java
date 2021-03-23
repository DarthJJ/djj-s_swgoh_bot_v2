package nl.djj.swgoh_bot_v2.config;

/**
 * @author DJJ
 */
public enum Permission {
    ADMINISTRATOR(0, "Administrator"),
    MODERATOR(1, "Moderator"),
    USER(2, "User");

    private final int level;
    private final String name;

    Permission(final int level, final String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return this.level;
    }

    public String getName() {
        return this.name;
    }
}
