package nl.djj.swgoh_bot_v2.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author DJJ
 **/
public class BaseError extends Exception{

    /**
     * Constructor.
     **/
    public BaseError() {
        super();
    }

    public String exceptionToString(final Exception exception){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
