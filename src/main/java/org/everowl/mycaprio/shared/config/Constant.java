package org.everowl.mycaprio.shared.config;

/**
 * Utility class containing constant values used throughout the application.
 * This class should not be instantiated.
 */
public class Constant {

    /**
     * Message template for DataAccessException errors.
     * Format: class name, method name, exception message.
     */
    public static final String DATA_ACCESS_EXCEPTION_MESSAGE = "DataAccessException occurred in %s %s : %s";

    /**
     * Message template for general exceptions.
     * Format: class name, method name, exception message.
     */
    public static final String GENERAL_EXCEPTION_MESSAGE = "An exception occurred in %s %s: %s";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Constant() {
        throw new AssertionError("Constant class should not be instantiated.");
    }
}