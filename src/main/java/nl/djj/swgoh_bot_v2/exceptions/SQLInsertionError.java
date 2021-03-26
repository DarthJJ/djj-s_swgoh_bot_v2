package nl.djj.swgoh_bot_v2.exceptions;

import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class SQLInsertionError extends Exception {

    /**
     * Constructor.
     *
     * @param className the classname.
     * @param message   the error.
     * @param logger    the logger.
     */
    public SQLInsertionError(final String className, final String message, final Logger logger) {
        super(message);
        logger.error(className, message);
    }
}
