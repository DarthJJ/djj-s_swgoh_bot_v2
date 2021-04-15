package nl.djj.swgoh_bot_v2.exceptions;

import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class SQLInsertionError extends Exception {

    /**
     * Constructor.
     *
     * @param className the className.
     * @param methodName the method name.
     * @param message   the error.
     * @param logger    the logger.
     */
    public SQLInsertionError(final String className, final String methodName, final String message, final Logger logger) {
        super(message);
        logger.error(className, methodName, message);
    }
}
