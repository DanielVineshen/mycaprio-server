package org.everowl.mycaprio.shared.exception;

import org.springframework.dao.DataAccessException;

import static org.everowl.mycaprio.shared.config.Constant.DATA_ACCESS_EXCEPTION_MESSAGE;
import static org.everowl.mycaprio.shared.config.Constant.GENERAL_EXCEPTION_MESSAGE;

/**
 * Utility class for handling database-related exceptions and executing database operations.
 */
public class DatabaseException {
    /**
     * Executes a database operation and handles potential exceptions.
     *
     * @param <T>         The return type of the database operation.
     * @param callable    The database operation to execute.
     * @param serviceName The name of the service executing the operation.
     * @param methodName  The name of the method executing the operation.
     * @return The result of the database operation.
     * @throws RuntimeException if a DataAccessException or any other exception occurs.
     */
    public static <T> T execute(Callable<T> callable, String serviceName, String methodName) {
        try {
            return callable.call();
        } catch (DataAccessException e) {
            // Wrap DataAccessException with more context about the failed operation
            throw new RuntimeException(String.format(DATA_ACCESS_EXCEPTION_MESSAGE, serviceName, methodName, e.getMessage()));
        } catch (Exception e) {
            // Wrap any other exception with general error information
            throw new RuntimeException(String.format(GENERAL_EXCEPTION_MESSAGE, serviceName, methodName, e.getMessage()), e);
        }
    }

    /**
     * Functional interface for database operations that may throw an exception.
     *
     * @param <T> The return type of the database operation.
     */
    @FunctionalInterface
    public interface Callable<T> {
        /**
         * Executes the database operation.
         *
         * @return The result of the database operation.
         * @throws Exception if an error occurs during the operation.
         */
        T call() throws Exception;
    }
}