package nl.djj.swgoh_bot_v2.exceptions;

import nl.djj.swgoh_bot_v2.Main;

/**
 * @author DJJ
 */
public class DeletionError extends BaseError {

    /**
     * Constructor.
     *
     * @param className  the className.
     * @param methodName the method name.
     * @param exception  the error.
     */
    public DeletionError(final String className, final String methodName, final Exception exception) {
        super();
        Main.getLogger().error(className, methodName, exceptionToString(exception));
    }
}
