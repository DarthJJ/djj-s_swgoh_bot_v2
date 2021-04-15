package nl.djj.swgoh_bot_v2.exceptions;

import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class HttpRetrieveError extends Exception {
    /**
     * Constructor.
     *
     * @param className  className.
     * @param message    the error.
     * @param methodName the methodName.
     * @param logger     the logger.
     */
    public HttpRetrieveError(final String className, final String methodName, final String message, final Logger logger) {
        super(message);
        logger.error(className, methodName, message);
    }
}
