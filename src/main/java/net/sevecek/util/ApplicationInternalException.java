package net.sevecek.util;

import java.text.MessageFormat;

public class ApplicationInternalException extends RuntimeException {

    public ApplicationInternalException() {
    }


    public ApplicationInternalException(String message, Object... errorMessageParameters) {
        super(MessageFormat.format(message, errorMessageParameters));
    }


    public ApplicationInternalException(Throwable cause, String message, Object... errorMessageParameters) {
        super(MessageFormat.format(message, errorMessageParameters), cause);
    }


    /**
     * <p>
     * Wraps an original Throwable into ApplicationInternalException
     * adopting the original stack trace and error message.
     * So the ApplicationInternalException is almost transparent.
     * </p>
     * <p>
     * This method is package local.
     * Use {@link net.sevecek.util.ExceptionUtils#rethrowAsUnchecked(Throwable)}
     * to invoke it, because that method will not wrap
     * {@link java.lang.Error}s and other {@link java.lang.RuntimeException}s
     * </p>
     *
     * @param cause Original Throwable which stack trace and error message will be used
     */
    ApplicationInternalException(Throwable cause) {
        super(cause.getMessage(), cause);
        setStackTrace(cause.getStackTrace());
    }

    /**
     * @deprecated Don't use this version of the constructor
     *             because it could easily be mis-interpreted
     *             as the other constructor
     *             {@link #ApplicationInternalException(String errorCode, Object... errorMessageParameters)}.<br/>
     *             Use ApplicationInternalException(Throwable cause, String errorCode) instead.
     */
    @Deprecated
    public ApplicationInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
