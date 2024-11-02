package org.example.cache.utils;

import org.example.cache.exceptions.CachingProjectException;

public class VerifyUtil {

    private VerifyUtil() {
    }

    public static void verify(boolean condition, String message) {
        if (!condition) {
            throw new CachingProjectException(message);
        }
    }

}
