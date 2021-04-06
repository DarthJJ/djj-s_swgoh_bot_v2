package nl.djj.swgoh_bot_v2.helpers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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

    /**
     * Gets the current DateTime.
     *
     * @return a string representation.
     */
    public static String getCurrentDateTime() {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        final LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    /**
     * Format a number as string with "thousands" points.
     * @param number the number to format.
     * @return the formatted number.
     */
    public static String formatNumber(final int number) {
        return new DecimalFormat("#,###,###", DecimalFormatSymbols.getInstance(Locale.GERMAN)).format(number);
    }

    /**
     * Removes special chars from the messageID.
     * @param channel the channel to strip.
     * @return the stripped channel.
     */
    public static String stripMessageChannel(final String channel) {
        return channel.replace("<", "").replace(">", "").replace("#", "");
    }
}
