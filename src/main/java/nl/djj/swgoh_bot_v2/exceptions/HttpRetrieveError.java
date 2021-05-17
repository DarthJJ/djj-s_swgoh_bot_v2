package nl.djj.swgoh_bot_v2.exceptions;

import nl.djj.swgoh_bot_v2.Main;

/**
 * @author DJJ
 */
public class HttpRetrieveError extends BaseError {
    /**
     * Constructor.
     *
     * @param className  className.
     * @param exception    the error.
     * @param methodName the methodName.
     */
    public HttpRetrieveError(final String className, final String methodName, final Exception exception) {
        super();
        Main.getLogger().error(className, methodName, exceptionToString(exception));
    }
}
