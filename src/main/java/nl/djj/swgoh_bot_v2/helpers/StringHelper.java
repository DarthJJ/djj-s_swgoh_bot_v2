package nl.djj.swgoh_bot_v2.helpers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author DJJ
 */
public final class StringHelper {
    private static final Pattern ALLYCODE_REGEX = Pattern.compile("\\d\\d\\d-?\\d\\d\\d-?\\d\\d\\d");
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";

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
    public static boolean isInvalidAllycode(final String allycode) {
        return !ALLYCODE_REGEX.matcher(allycode).matches();
    }

    /**
     * Gets the current DateTime.
     *
     * @return a string representation.
     */
    public static String getCurrentDateTimeAsString() {
        return getCurrentDateTime().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    /**
     * Gets the current datetime.
     *
     * @return a LocalDateTime object.
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneId.of("Europe/Amsterdam"));
    }

    /**
     * Gets the current date.
     *
     * @return a LocalDate object.
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now(ZoneId.of("Europe/Amsterdam"));
    }

    /**
     * Gets the current date.
     *
     * @return a string representation.
     */
    public static String getCurrentDateAsString() {
        return getCurrentDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    /**
     * Formats a string to a localDate.
     *
     * @param dateString the string to format.
     * @return a localDate object.
     */
    public static LocalDate stringToLocalDate(final String dateString) {
        final Instant instant = Instant.parse(dateString);
        final LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
        return result.toLocalDate();
    }

    /**
     * Formats the given DateTime to a string.
     *
     * @param dateTime the localDateTime object.
     * @return a string representation.
     */
    public static String localDateTimeToString(final LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    /**
     * Formats a string to a LocalDateTime.
     *
     * @param dateString the string to format.
     * @return a localDateTime object.
     */
    public static LocalDateTime stringToLocalDateTime(final String dateString) {
        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    /**
     * Format a number as string with "thousands" points.
     *
     * @param number the number to format.
     * @return the formatted number.
     */
    public static String formatNumber(final int number) {
        return new DecimalFormat("#,###,###", DecimalFormatSymbols.getInstance(Locale.GERMAN)).format(number);
    }

    /**
     * Removes special chars from the messageID.
     *
     * @param channel the channel to strip.
     * @return the stripped channel.
     */
    public static String stripMessageChannel(final String channel) {
        return channel.replace("<", "").replace(">", "").replace("#", "");
    }

    /**
     * Strips the Discord ID to the basic number.
     *
     * @param tag the tagged ID.
     * @return a stripped String.
     */
    public static String getDiscordIdFromTag(final String tag) {
        return tag.replace("@", "").replace("!", "").replace("<", "").replace(">", "");
    }

    /**
     * Converts every first char of a word to a capital.
     *
     * @param toCapitalize the string to capitalize.
     * @return a proper capitalized string.
     */
    public static String capitalizeEveryWord(final String toCapitalize) {
        final char[] charArray = toCapitalize.toCharArray();
        boolean foundSpace = true;
        for (int i = 0; i < charArray.length; i++) {
            if (Character.isLetter(charArray[i])) {
                if (foundSpace) {
                    charArray[i] = Character.toUpperCase(charArray[i]);
                    foundSpace = false;
                }
            } else {
                foundSpace = true;
            }
        }
        return String.valueOf(charArray);
    }
}
