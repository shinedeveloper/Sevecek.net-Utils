package net.sevecek.util;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;
import org.springframework.context.*;

public class ExceptionUtils {

    /**
     * <p>
     * Wraps passed-in Throwable into an unchecked ApplicationInternalException
     * (a subclass of RuntimeException) (if necessary) and re-throws it.
     * </p>
     * <p>
     * If the passed-in Throwable is:
     * <ul>
     *   <li>RuntimeException, it's re-thrown as is</li>
     *   <li>Error, it's re-thrown as is</li>
     *   <li>InvocationTargetException, it's unwrapped and re-thrown</li>
     *   <li>ExecutionException, it's unwrapped and re-thrown</li>
     *   <li>InterruptedException, it's translated to CancellationException and re-thrown</li>
     *   <li>all other Throwables are wrapped in ApplicationInternalException and re-thrown</li>
     * </ul>
     * </p>
     * <p>
     * Recommended usage is:
     * </p>
     * <pre>
     * import net.sevecek.util.ExceptionUtils;
     *
     *
     * try {
     *     // Some code
     * } catch(Throwable th) {
     *     throw ExceptionUtils.rethrowAsUnchecked(th);
     * }
     * </pre>
     *
     * @param th The exception to be wrapped as ApplicationInternalException and re-thrown
     * @return Declared as {@link java.lang.RuntimeException} instead of <code>void</code>
     *         so that you can use <code>throw</code>
     *         clause to terminate the catch block. Don't worry,
     *         that throw will never be reached because this method will
     *         always throw an unchecked exception
     *
     * @throws net.sevecek.util.ApplicationPublicException
     * @throws net.sevecek.util.ApplicationInternalException
     * @throws java.lang.Error
     * @throws java.lang.RuntimeException
     */
    public static RuntimeException rethrowAsUnchecked(Throwable th) throws ApplicationInternalException, Error, RuntimeException {
        if (th == null) throw new NullPointerException();

        boolean beingUnwrapped;
        do {
            beingUnwrapped = false;
            if (th instanceof InvocationTargetException) {
                InvocationTargetException ex = (InvocationTargetException) th;
                th = ex.getCause();
                beingUnwrapped = true;
            }
            if (th instanceof ExecutionException) {
                ExecutionException ex = (ExecutionException) th;
                th = ex.getCause();
                beingUnwrapped = true;
            }
        } while (beingUnwrapped);

        if (th instanceof InterruptedException) {
            Thread.currentThread().interrupt();
            th = new CancellationException();
        }

        if (th instanceof RuntimeException) {
            throw (RuntimeException) th;
        } else if (th instanceof Error) {
            throw (Error) th;
        } else {
            throw new ApplicationInternalException(th);
        }
    }


    /**
     * <p>
     * An alternative for {@link java.util.concurrent.Future#get()} which doesn't throw checked exceptions.
     * </p>
     * <p>
     * More precisely, it:
     * <ul>
     *   <li>Calls <code>future.get()</code></li>
     *   <li>If there is a normal result, it's returned</li>
     *   <li>If there is an exception, it's rethrown as unchecked using {@link ExceptionUtils#rethrowAsUnchecked(Throwable)}</li>
     * </ul>
     * </p>
     *
     * @param future The {@link java.util.concurrent.Future} to be queried
     */
    public static <E> E getFutureResultOrUncheckedException(Future<E> future) throws ApplicationInternalException, Error, RuntimeException {
        if (future == null) throw new NullPointerException();
        try {
            E result = future.get();
            return result;
        } catch (Exception ex) {
            throw ExceptionUtils.rethrowAsUnchecked(ex);
        }
    }


    public static String formatErrorMessageForLoggingButNotForUserUI(String errorCode, Object[] errorMessageParameters) {
        String errorMessageWithPlaceholders =
                prepareErrorMessageWithoutResourceBundle(errorCode);
        return MessageFormat.format(errorMessageWithPlaceholders, errorMessageParameters);
    }


    public static String formatErrorMessageForUI(String errorCode, Object[] errorMessageParameters, ResourceBundle messages) {
        String errorMessageWithPlaceholders = null;
        if (messages != null) {
            try {
                errorMessageWithPlaceholders = messages.getString(errorCode);
            } catch (MissingResourceException ex) {
                // This is OK, it will just go to the fall back mechanism
            }
        }
        if (errorMessageWithPlaceholders == null) {
            errorMessageWithPlaceholders =
                    prepareErrorMessageWithoutResourceBundle(errorCode);
        }
        return MessageFormat.format(errorMessageWithPlaceholders, errorMessageParameters);
    }


    public static String formatErrorMessageForUI(String errorCode, Object[] errorMessageParameters, MessageSource springMessageSource, Locale language) {
        String errorMessageWithPlaceholders = null;
        if (springMessageSource != null) {
            try {
                errorMessageWithPlaceholders = springMessageSource.getMessage(errorCode, errorMessageParameters, language);
            } catch (RuntimeException ex) {
                // This is OK, it will just go to the fall back mechanism
            }
        }
        if (errorMessageWithPlaceholders == null) {
            errorMessageWithPlaceholders =
                    prepareErrorMessageWithoutResourceBundle(errorCode);
        }
        return MessageFormat.format(errorMessageWithPlaceholders, errorMessageParameters);
    }


    private static String prepareErrorMessageWithoutResourceBundle(String errorCode) {
        StringBuilder errorBuilder = new StringBuilder(errorCode);

        int lastDelimiter = errorBuilder.lastIndexOf("->");
        if (lastDelimiter > -1) {
            errorBuilder.delete(0, lastDelimiter + "->".length());
        }

        for (int i = 0; i < errorBuilder.length(); i++) {
            if (errorBuilder.charAt(i) == '_') {
                errorBuilder.setCharAt(i, ' ');
            }
        }

        return errorBuilder.toString();
    }
}
