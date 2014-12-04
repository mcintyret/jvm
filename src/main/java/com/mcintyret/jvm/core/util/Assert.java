package com.mcintyret.jvm.core.util;

public class Assert {

    public static <T> T assertNotNull(T ref) {
        return assertNotNull(ref, "Should not have been null but was");
    }

    public static <T> T assertNotNull(T ref, String message) {
        if (ref == null) {
            throw new AssertionError(message);
        }
        return ref;
    }
}
