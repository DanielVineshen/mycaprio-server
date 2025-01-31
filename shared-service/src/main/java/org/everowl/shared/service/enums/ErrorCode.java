package org.everowl.shared.service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing various error codes and their associated messages used throughout the application.
 * Each error code consists of a numeric code and a descriptive message.
 */
@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * Successful operation.
     */
    SUCCESS(2000, "Success"),

    /**
     * Client-side errors (4xxx range).
     */
    INVALID_INPUT_FORMAT(4000, "Invalid input format was provided."),
    INVALID_INPUT_VALUES(4001, "Invalid input values were provided."),
    INVALID_CONSTRAINT_VALUES(4002, "Invalid constraint values were provided."),
    USER_NOT_AUTHORIZED(4003, "User is not authorized."),
    USER_ACCOUNT_DELETED(4004, "User account has been deleted."),
    INVALID_REFRESH_TOKEN(4005, "Refresh token is not valid."),
    USER_NOT_EXIST(4006, "User does not exist."),
    INVALID_ACCESS_TOKEN(4007, "Access token is not valid."),
    MISSING_AUTH_TOKEN_HEADER(4008, "Missing authorization token header."),
    CATEGORY_TYPE_INVALID(4009, "Category type is not valid"),
    USER_ROLE_INVALID(4010, "User role is not valid."),
    USER_NOT_PERMITTED(4011, "User is not permitted to perform this action."),
    INVALID_CREDENTIALS(4012, "Invalid credentials were provided."),
    DECODING_ERROR(4013, "There was a problem decoding/decrypting the response body."),
    MAPPING_ERROR(4014, "There was a problem mapping the response body."),
    LOGIN_ID_EXIST(4020, "Username already exist."),

    /**
     * Custom error codes (40xxx range).
     */


    /**
     * Server-side errors (5xxx range).
     */
    PROCESSING_ERROR(5000, "Something went wrong when processing request."),
    UNCAUGHT_EXCEPTION(5001, "An uncaught exception occurred when processing request."),
    DATA_ACCESS_EXCEPTION(5002, "Data access exception occurred."),
    ENTITY_NOT_FOUND_EXCEPTION(5003, "Entity not found exception occurred."),
    FILE_UPLOAD_ERROR(5004, "Something went wrong when uploading the file."),
    FILE_DELETE_ERROR(5005, "Something went wrong when deleting the file."),
    UNSUPPORTED_MEDIA_TYPE_EXCEPTION(5006, "Unsupported media type exception occurred.");

    /**
     * The numeric code associated with the error.
     */
    private final int code;

    /**
     * The descriptive message associated with the error.
     */
    private final String message;
}

