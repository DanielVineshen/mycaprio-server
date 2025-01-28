package org.everowl.shared.service.util;

/**
 * Utility class for retrieving information about the current call stack.
 * This class provides methods to access details about calling methods in the execution stack.
 */
public class StackTraceUtils {
    /**
     * Retrieves the name of the method that directly called the method invoking this utility.
     *
     * This method uses the current thread's stack trace to determine the calling method's name.
     * It skips the first two elements of the stack trace:
     * - The first element (index 0) is always the getStackTrace method itself
     * - The second element (index 1) is the current method (getCallingMethodName)
     * - The third element (index 2) is the method we're interested in - the actual calling method
     *
     * @return The name of the calling method as a String.
     */
    public static String getCallingMethodName() {
        // Get the current thread's stack trace
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // Return the name of the method at index 2 (the calling method)
        return stackTrace[2].getMethodName();
    }
}