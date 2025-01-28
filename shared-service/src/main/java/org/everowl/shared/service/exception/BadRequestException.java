package org.everowl.shared.service.exception;

import lombok.Getter;
import org.everowl.shared.service.enums.ErrorCode;

/**
 * Custom exception class for handling bad request scenarios.
 * This exception carries an ErrorCode and optionally additional result data.
 */
@Getter
public class BadRequestException extends RuntimeException {

    /**
     * The error code associated with this exception.
     */
    private final ErrorCode errorCode;

    /**
     * Additional result data associated with this exception.
     * This field is optional and may be null.
     */
    private Object result;

    /**
     * Constructs a new BadRequestException with the specified error code.
     *
     * @param errorCode the ErrorCode representing the specific error condition
     */
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new BadRequestException with the specified error code and result data.
     *
     * @param errorCode the ErrorCode representing the specific error condition
     * @param result    additional data associated with the exception
     */
    public BadRequestException(ErrorCode errorCode, Object result) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.result = result;
    }
}