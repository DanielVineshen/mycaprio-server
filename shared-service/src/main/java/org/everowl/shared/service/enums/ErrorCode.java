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
    FILE_NOT_FOUND(4015, "File not found."),
    FILE_SIZE_ERROR(4016, "File size exceeds maximum limit of 5MB."),
    FILE_TYPE_ERROR(4017, "Only JPEG and PNG file types are allowed."),
    VOUCHER_NOT_EXIST(4018, "Voucher does not exist."),
    STORE_NOT_EXIST(4019, "Store does not exist."),
    LOGIN_ID_EXIST(4020, "Username already exist."),
    TIER_NOT_EXIST(4021, "Tier does not exist."),
    NOT_ENOUGH_POINTS(4022, "Not enough points available."),
    VOUCHER_CANNOT_PURCHASE(4023, "Voucher cannot be purchased."),
    STORE_CUSTOMER_VOUCHER_NOT_EXIST(4024, "Store customer voucher does not exist."),
    VOUCHER_ALREADY_EXPIRED(4024, "Store customer voucher does not exist."),
    VOUCHER_REDEEMED_EXPIRED(4025, "Voucher already redeemed/expired."),
    SMS_QUOTA_REACHED(4026, "Reached maximum message request for today. Please try again tomorrow."),
    SMS_NOT_VALID(4027, "SMS code provided is not valid."),
    VOUCHER_CODE_EXPIRED(4028, "Five minutes timer for the code has expired. Please ask the customer to try again."),

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
    UNSUPPORTED_MEDIA_TYPE_EXCEPTION(5006, "Unsupported media type exception occurred."),
    SMS_SEND_EXCEPTION(5007, "Something went wrong when sending sms."),
    JSON_CONVERSION_EXCEPTION(5008, "Failed to convert object to JSON."),
    JSON_PROCESSING_EXCEPTION(5009, "Failed to convert JSON to object."),
    ENCRYPTING_CODE_EXCEPTION(5010, "Something went wrong when encrypting code."),
    DECRYPTING_CODE_EXCEPTION(5011, "Something went wrong when encrypting code.");

    /**
     * The numeric code associated with the error.
     */
    private final int code;

    /**
     * The descriptive message associated with the error.
     */
    private final String message;
}

