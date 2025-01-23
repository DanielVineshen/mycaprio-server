package org.everowl.mycaprio.shared.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.everowl.mycaprio.shared.dto.BaseErrorResponseBodyModel;
import org.everowl.mycaprio.shared.enums.ErrorCode;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

/**
 * Global exception handler for the application.
 * Handles various exceptions and returns appropriate HTTP responses.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles HttpMessageNotReadableException.
     *
     * @param ex The caught HttpMessageNotReadableException
     * @return ResponseEntity with error details and BAD_REQUEST status
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleMissingRequestBody(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException: {}", ex.getMessage());

        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ErrorCode.INVALID_INPUT_FORMAT, "Invalid input format was provided");

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException.
     *
     * @param ex The caught MethodArgumentNotValidException
     * @return ResponseEntity with error details and BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .sorted()
                .toList();

        // Use only the first error message in the response
        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ErrorCode.INVALID_INPUT_VALUES, errors.get(0));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalArgumentException.
     *
     * @param ex The caught IllegalArgumentException
     * @return ResponseEntity with error details and BAD_REQUEST status
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException with message: {}", ex.getMessage());

        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ErrorCode.INVALID_INPUT_VALUES, ex.getMessage());

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MissingServletRequestParameterException.
     *
     * @param ex The caught MissingServletRequestParameterException
     * @return ResponseEntity with error details and BAD_REQUEST status
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("MissingServletRequestParameterException with message: {}", ex.getMessage());

        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ErrorCode.INVALID_INPUT_VALUES, ex.getMessage());

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ConstraintViolationException.
     *
     * @param ex The caught ConstraintViolationException
     * @return ResponseEntity with error details and BAD_REQUEST status
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

        Collections.sort(errors);

        // Use only the first error message in the response
        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ErrorCode.INVALID_CONSTRAINT_VALUES, errors.get(0));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles NotAuthorizedException.
     *
     * @param ex The caught NotAuthorizedException
     * @return ResponseEntity with error details and UNAUTHORIZED status
     */
    @ExceptionHandler(value = {NotAuthorizedException.class})
    public ResponseEntity<Object> handleNotAuthorizedException(NotAuthorizedException ex) {
        log.error("NotAuthorizedException with message: {}", ex.getMessage());

        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ErrorCode.USER_NOT_AUTHORIZED, ex.getResult());

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles BadRequestException.
     *
     * @param ex The caught BadRequestException
     * @return ResponseEntity with error details and BAD_REQUEST status
     */
    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        log.error("BadRequestException with message: {}", ex.getMessage());

        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ex.getErrorCode(), ex.getResult());

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ForbiddenException.
     *
     * @param ex The caught ForbiddenException
     * @return ResponseEntity with error details and FORBIDDEN status
     */
    @ExceptionHandler(value = {ForbiddenException.class})
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException ex) {
        log.error("ForbiddenException with message: {}", ex.getMessage());

        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ex.getErrorCode(), ex.getResult());

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handles NotFoundException.
     *
     * @param ex The caught NotFoundException
     * @return ResponseEntity with error details and NOT_FOUND status
     */
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        log.error("NotFoundException with message: {}", ex.getMessage());

        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ex.getErrorCode(), ex.getResult());

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles RuntimeException.
     *
     * @param ex The caught RuntimeException
     * @return ResponseEntity with error details and INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        String errorCode = generateErrorCode();
        log.error("Error code: {} for RuntimeException with message: {}", errorCode, ex.getMessage());

        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ErrorCode.PROCESSING_ERROR, "Runtime exception occurred - " + errorCode);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles any uncaught Exception.
     *
     * @param ex The caught Exception
     * @return ResponseEntity with error details and BAD_GATEWAY status
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception ex) {
        String errorCode = generateErrorCode();
        log.error("Error code: {} for Exception with message: {}", errorCode, ex.getMessage());

        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ErrorCode.UNCAUGHT_EXCEPTION, "An uncaught exception occurred when processing request - " + errorCode);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_GATEWAY);
    }

    /**
     * Handles DataAccessException.
     *
     * @param ex The caught DataAccessException
     * @return ResponseEntity with error details and SERVICE_UNAVAILABLE status
     */
    @ExceptionHandler(value = {DataAccessException.class})
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
        String errorCode = generateErrorCode();
        log.error("Error code: {} for DataAccessException with message: {}", errorCode, ex.getMessage());

        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ErrorCode.DATA_ACCESS_EXCEPTION, "Data access exception occurred - " + errorCode);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Handles EntityNotFoundException.
     *
     * @param ex The caught EntityNotFoundException
     * @return ResponseEntity with error details and INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        String errorCode = generateErrorCode();
        log.error("Error code: {} for EntityNotFoundException with message: {}", errorCode, ex.getMessage());

        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ErrorCode.ENTITY_NOT_FOUND_EXCEPTION, "Entity not found exception occurred - " + errorCode);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles HttpMediaTypeNotSupportedException.
     *
     * @param ex The caught HttpMediaTypeNotSupportedException
     * @return ResponseEntity with error details and UNSUPPORTED_MEDIA_TYPE status
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        BaseErrorResponseBodyModel errorResponse = new BaseErrorResponseBodyModel(ErrorCode.UNSUPPORTED_MEDIA_TYPE_EXCEPTION, "Unsupported media type exception occurred - The content type is not supported. Please use a valid content type.");

        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Generates a unique error code.
     *
     * @return A 16-character string representing the error code
     */
    private String generateErrorCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}