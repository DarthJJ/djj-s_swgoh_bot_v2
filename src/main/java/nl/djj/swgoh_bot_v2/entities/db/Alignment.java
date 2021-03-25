package nl.djj.swgoh_bot_v2.entities.db;

import java.util.Arrays;

/**
 * @author DJJ
 */
public enum Alignment {
    DARKSIDE("Dark Side"),
    LIGHTSIDE("Light Side"),
    UNKNOWN("Missing");

    private final String alignment;

    Alignment(final String alignment) {
        this.alignment = alignment;
    }


    /**
     * Gets the alignment based on the string value.
     * @param alignmentString the string value.
     * @return the alignment.
     */
    public static Alignment getByString(final String alignmentString) {
        return Arrays.stream(values())
                .filter(alignment -> alignment.alignment.equals(alignmentString))
                .findFirst().orElse(UNKNOWN);
    }

    public String getAlignment() {
        return this.alignment;
    }
}
