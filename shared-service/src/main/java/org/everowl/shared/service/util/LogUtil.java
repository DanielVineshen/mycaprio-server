package org.everowl.shared.service.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for logging errors in methods.
 * This class provides a centralized way to log exceptions that occur during method execution.
 */
@Slf4j
public class LogUtil {
    /**
     * Logs an error that occurred in a method.
     * This method automatically retrieves the calling method's name and logs it along with the exception details.
     *
     * @param e The exception representing the error to be logged.
     */
    public static void logMethodError(Exception e) {
        // Retrieve the name of the calling method using StackTraceUtils
        String callingMethodName = StackTraceUtils.getCallingMethodName();

        // Log the error with the calling method name and exception details
        log.error("{} method: {}", callingMethodName, e.toString());
    }
}