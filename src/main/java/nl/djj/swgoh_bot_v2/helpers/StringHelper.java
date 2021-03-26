package nl.djj.swgoh_bot_v2.helpers;

import java.util.regex.Pattern;

/**
 * @author DJJ
 */
public final class StringHelper {
    private static final Pattern ALLYCODE_REGEX = Pattern.compile("\\d\\d\\d-?\\d\\d\\d-?\\d\\d\\d");

    /**
     * Constructor.
     */
    private StringHelper() {
        super();
    }

    /**
     * Verifies the allycode.
     *
     * @param allycode the allycode.
     * @return the status.
     */
    public static boolean validateAllycode(final String allycode) {
        return ALLYCODE_REGEX.matcher(allycode).matches();
    }
}
