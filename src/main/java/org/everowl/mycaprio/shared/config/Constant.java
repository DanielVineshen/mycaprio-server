package org.everowl.mycaprio.shared.config;

import java.math.BigDecimal;

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
     * Conversion factor from Watt-hours (Wh) to Kilowatt-hours (kWh).
     */
    public static final int WH_TO_KWH = 1000;

    /**
     * Conversion factor from cubic centimeters (cm³) to cubic meters (m³).
     */
    public static final int CM3_TO_M3 = 1000;

    /**
     * Fixed constant value for now, value derived from the LBEMS website
     */
    public static final BigDecimal EMISSION_FACTOR = BigDecimal.valueOf(0.15418194288);

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Constant() {
        throw new AssertionError("Constant class should not be instantiated.");
    }
}