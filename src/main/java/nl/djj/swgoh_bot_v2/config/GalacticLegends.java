package nl.djj.swgoh_bot_v2.config;

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

    public static GalacticLegends getByKey(final String key) {
        return GalacticLegends.valueOf(key.toUpperCase(Locale.ROOT));
    }

    public static GalacticLegends getByName(final String name) {
        return Arrays.stream(values())
                .filter(galacticLegends -> galacticLegends.name.equals(name))
                .findFirst().orElse(null);
    }



    public static boolean isEvent(final String key) {
        return Arrays.stream(values())
                .filter(galacticLegends -> galacticLegends.key.equalsIgnoreCase(key))
                .count() == 1;
    }

    public static String getKeys() {
        return REY + " | " + KYLO + " | " + JML + " | " + JKL + " | " + SEE;
    }
}
