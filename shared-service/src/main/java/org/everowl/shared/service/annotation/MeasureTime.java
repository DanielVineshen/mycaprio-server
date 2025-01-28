package org.everowl.shared.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark methods for execution time measurement.
 * When applied to a method, it indicates that the method's execution time
 * should be measured and potentially logged or monitored.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MeasureTime {
    // This annotation does not define any elements
}