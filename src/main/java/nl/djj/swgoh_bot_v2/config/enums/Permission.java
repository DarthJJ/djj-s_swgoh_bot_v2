package nl.djj.swgoh_bot_v2.config.enums;


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

    /**
     *
     * @param level to get the permission for.
     * @return the according permission or the default.
     */
    public static Permission valueOf(final int level) {
        return Arrays.stream(values())
                .filter(permission -> permission.level == level)
                .findFirst().orElse(Permission.USER);
    }

    /**
     * @return the level of the permission.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     *
     * @return the name of the permission.
     */
    public String getName() {
        return this.name;
    }
}
