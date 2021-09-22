/**
 * 
 */
package org.iman.Heimdallr.exception;

/**
 * @author ey
 *
 */
public class DataConversionException extends Exception{

    private static final long serialVersionUID = -4185219455602291736L;

    public DataConversionException() {
        super();
    }

    public DataConversionException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DataConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataConversionException(String message) {
        super(message);
    }

    public DataConversionException(Throwable cause) {
        super(cause);
    }

    
}
