package org.everowl.shared.service.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * Utility class for retrieving and logging system memory usage information.
 * This class provides methods to access and display both heap and non-heap memory usage.
 */
@Slf4j
public class MemoryInfo {
    /**
     * Prints the current memory usage of the system.
     * This method retrieves heap and non-heap memory usage information from the MemoryMXBean
     * and logs it using the configured logger.
     */
    public static void printMemoryUsage() {
        // Obtain the MemoryMXBean to access memory usage statistics
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        // Retrieve heap memory usage
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();

        // Retrieve non-heap memory usage
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        // Log heap memory usage information
        log.info("Heap Memory: {}", heapMemoryUsage.toString());

        // Log non-heap memory usage information
        log.info("Non-Heap Memory: {}", nonHeapMemoryUsage.toString());
    }
}