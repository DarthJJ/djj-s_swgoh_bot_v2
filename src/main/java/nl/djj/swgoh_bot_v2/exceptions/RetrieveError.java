package nl.djj.swgoh_bot_v2.exceptions;

import nl.djj.swgoh_bot_v2.Main;

/**
 * @author DJJ
 */
public class RetrieveError extends Exception {

    /**
     * Constructor.
     *
     * @param className the className.
     * @param methodName the method name.
     * @param message   the error.
     */
    public RetrieveError(final String className, final String methodName, final String message) {
        super(message);
        Main.getLogger().error(className, methodName, message);
    }
}
