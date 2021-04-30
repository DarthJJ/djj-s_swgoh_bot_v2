package nl.djj.swgoh_bot_v2.config.enums;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author DJJ
 **/
public enum GalacticLegends {
    REY("Rey", "rey"),
    KYLO("Kylo", "kylo"),
    JML("Jedi Master Luke Skywalker", "jml"),
    JKL("Jedi Knight Luke", "jkl"),
    SEE("Sith Eternal Emperor", "see");

    private final String name;
    private final String key;

    GalacticLegends(final String name, final String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public String getKey() {
        return key;
    }

    /**
     * Get GL by key.
     *
     * @param key the key value.
     * @return a GL.
     */
    public static GalacticLegends getByKey(final String key) {
        return GalacticLegends.valueOf(key.toUpperCase(Locale.ROOT));
    }

    /**
     * Get GL by name.
     *
     * @param name the name value.
     * @return a GL.
     */
    public static GalacticLegends getByName(final String name) {
        return Arrays.stream(values())
                .filter(galacticLegends -> galacticLegends.name.equals(name))
                .findFirst().orElse(null);
    }

    /**
     * Checks whether the given key is an GL event.
     *
     * @param key the key.
     * @return a boolean to represent the event exists.
     */
    public static boolean isEvent(final String key) {
        return Arrays.stream(values())
                .filter(galacticLegends -> galacticLegends.key.equalsIgnoreCase(key))
                .count() == 1;
    }

    /**
     * Gets all the available keys.
     *
     * @return a list of keys.
     */
    public static String getKeys() {
        final StringBuilder keyString = new StringBuilder();
        for (final GalacticLegends gl : values()) {
            keyString.append(gl.getKey()).append('|');
        }
        return keyString.toString();
    }
}
