package nl.djj.swgoh_bot_v2.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author DJJ
 **/
public class BaseError extends Exception {

    /**
     * Constructor.
     **/
    public BaseError() {
        super();
    }

    /**
     * Formats the given exception stacktrace to a string for printing purposes.
     * @param exception the exception containing the stacktrace.
     * @return a string representation.
     */
    public String exceptionToString(final Exception exception) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
