package org.cafebabe.model.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A basic ID generator used to assign unique IDs to objects.
 * These IDs are used for saving and loading circuits, in order to
 * combine component back together correctly.
 */
@SuppressWarnings("PMD.ClassNamingConventions")
public final class IdGenerator {

    private static AtomicLong counter = new AtomicLong();

    private IdGenerator() {}

    public static long getNewId() {
        return counter.getAndIncrement();
    }

    public static void setStartingValue(int startingValue) {
        int start = startingValue;
        if (startingValue < 0) {
            start = 0;
        }
        counter.set(start);
    }
}
