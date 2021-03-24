package nl.djj.swgoh_bot_v2.config;


import java.util.Arrays;

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

    public static Permission valueOf(int level) {
        return Arrays.stream(values())
                .filter(permission -> permission.level == level)
                .findFirst().orElse(Permission.USER);
    }

    public int getLevel() {
        return this.level;
    }

    public String getName() {
        return this.name;
    }
}
