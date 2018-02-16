package net.sevecek.util.swing;

import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
import net.sevecek.util.*;

import static net.sevecek.util.swing.ErrorPanel.ErrorMessage;

public class SwingExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger logger = Logger.getLogger(SwingExceptionHandler.class.getName());
    static {
        ErrorPanel.logger = logger;
    }

    public static void install() {
        install(null);
    }


    public static void install(ResourceBundle resourceBundle) {
        Thread.setDefaultUncaughtExceptionHandler(
                new SwingExceptionHandler(resourceBundle));
    }

    //------------------------------------------------------------------------

    private ResourceBundle resourceBundle;
    private ResourceBundle internalResourceBundle;


    private SwingExceptionHandler(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        Locale locale;
        if (resourceBundle != null) {
            locale = resourceBundle.getLocale();
        } else {
            locale = Locale.getDefault();
        }
        internalResourceBundle = ErrorPanel.resolveInternalResourceBundle(locale);
    }


    @Override
    public void uncaughtException(Thread t, final Throwable error) {
        if (error instanceof ThreadDeath || error instanceof InterruptedException || error instanceof CancellationException) {
            // Silently ignore the exception
            return;
        }
        final ErrorMessage message = prepareMessage(error);
        if (EventQueue.isDispatchThread()) {
            ErrorPanel.showErrorDialog(message, resourceBundle, internalResourceBundle);
        } else {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        ErrorPanel.showErrorDialog(message, resourceBundle, internalResourceBundle);
                    }
                });
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                // Not much more to do, because we are in an Exception handler.
                // Just exit
            } catch (InvocationTargetException ex) {
                // Not much more we can do here except log the exception
                logger.log(
                        Level.WARNING,
                        "Failed to display the error message",
                        ex.getCause());
            }
        }
    }


    private ErrorMessage prepareMessage(Throwable error) {
        if (error instanceof ApplicationPublicException) {
            ApplicationPublicException ex = (ApplicationPublicException) error;
            String message = ExceptionUtils.formatErrorMessageForUI(ex.getErrorCode(), ex.getErrorMessageParameters(), resourceBundle);

            Throwable cause = error.getCause();
            if (cause == null) {
                return new ErrorMessage(message);
            }
            String stackTrace = prepareStackTraceString(cause);
            return new ErrorMessage(message, stackTrace, cause);
        }

        String message;
        if (error instanceof ApplicationInternalException) {
            message = error.getMessage();
        } else {
            message = error.getClass().getSimpleName() + ": " + error.getMessage();
        }
        String stackTrace = prepareStackTraceString(error);
        return new ErrorMessage(message, stackTrace, error);
    }


    private String prepareStackTraceString(Throwable error) {
        StringWriter stackTraceWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stackTraceWriter);
        error.printStackTrace(writer);
        writer.close();
        String completeListing = stackTraceWriter.toString();
        return completeListing;
    }

}
