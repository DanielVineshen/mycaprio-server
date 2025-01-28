package org.everowl.shared.service.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Utility class for handling mathematical operations related to BigDecimal values.
 * This class provides methods for safe division and rounding of BigDecimal values,
 * and calculating comparison percentages between two BigDecimal values.
 */
public class MathsUtil {
    /**
     * Safely divides two BigDecimal values and rounds the result.
     * If an exception occurs during the division, it returns BigDecimal.ZERO.
     *
     * @param value1       The dividend BigDecimal value.
     * @param value2       The divisor BigDecimal value.
     * @param scale        The scale of the result, i.e., the number of digits to the right of the decimal point.
     * @param roundingMode The rounding mode to apply.
     * @return The result of the division, or BigDecimal.ZERO if an exception occurs.
     */
    public static BigDecimal safeDivideAndRound(BigDecimal value1, BigDecimal value2, int scale, RoundingMode roundingMode) {
        try {
            return value1.divide(value2, scale, roundingMode);
        } catch (Exception e) {
            // Return 0 in case of any exception during division
            return BigDecimal.ZERO;
        }
    }

    /**
     * Safely divides two BigDecimal values using a specified MathContext.
     * If an exception occurs during the division, it returns BigDecimal.ZERO.
     *
     * @param value1      The dividend BigDecimal value.
     * @param value2      The divisor BigDecimal value.
     * @param mathContext The MathContext specifying precision and rounding mode.
     * @return The result of the division, or BigDecimal.ZERO if an exception occurs.
     */
    public static BigDecimal safeDivideAndRound(BigDecimal value1, BigDecimal value2, MathContext mathContext) {
        try {
            return value1.divide(value2, mathContext);
        } catch (Exception e) {
            // Return 0 in case of any exception during division
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calculates the comparison percentage between two BigDecimal values.
     * The percentage represents the relative change from the previous value to the current value.
     *
     * @param totalPreviousVal The previous total value.
     * @param totalCurrentVal  The current total value.
     * @return The comparison percentage as a double. Returns 0 if totalPreviousVal is zero.
     */
    public static double getComparisonPercentage(BigDecimal totalPreviousVal, BigDecimal totalCurrentVal) {
        // Ensure that the previous value is not zero to avoid division by zero
        if (totalPreviousVal.compareTo(BigDecimal.ZERO) != 0) {
            // Calculate the difference between current and previous values
            BigDecimal difference = totalCurrentVal.subtract(totalPreviousVal);

            // Calculate the percentage difference
            BigDecimal comparison = difference
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalPreviousVal, 2, RoundingMode.HALF_UP);

            // Return the double value of the comparison percentage
            return comparison.doubleValue();
        } else {
            // Handle the case where totalPreviousVal is zero by returning 0
            return 0;
        }
    }
}