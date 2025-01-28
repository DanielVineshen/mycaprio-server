package org.everowl.shared.service.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Aspect for measuring execution time of methods annotated with @MeasureTime.
 * This aspect logs the time taken by the annotated methods.
 */
@Component
@Aspect
@Slf4j
public class MeasureTimeAdvice {

    /**
     * Measures and logs the execution time of methods annotated with @MeasureTime.
     *
     * @param point The join point representing the intercepted method.
     * @return The result of the method execution.
     * @throws Throwable If an error occurs during method execution.
     */
    @Around("@annotation(MeasureTime)")
    public Object measureTime(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        // Execute the intercepted method
        Object result = point.proceed();
        stopWatch.stop();

        // Log the execution time
        log.info("Time taken by {}() method is {} ms",
                point.getSignature().getName(),
                stopWatch.getTotalTimeMillis());

        return result;
    }
}