package net.sevecek.util;

import java.util.concurrent.*;

public class ThreadUtils {

    public static void sleep(long millis) throws CancellationException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new CancellationException();
        }
    }
}
