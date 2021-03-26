package nl.djj.swgoh_bot_v2.exceptions;

import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class ValidationError extends Exception {
    /**
     * Constructor.
     *
     * @param className the className.
     * @param message   exception message.
     * @param logger    the logger.
     */
    public ValidationError(final String className, final String message, final Logger logger) {
        super(message);
        logger.error(className, message);
    }
}
