package org.everowl.mycaprio.shared.enums;

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
    BUILDING_NOT_EXIST(4015, "Building does not exist."),
    DASHBOARD_LAYOUT_NOT_EXIST(4016, "Dashboard layout does not exist."),
    INVALID_LOGO_TYPE(4017, "Invalid logo type provided."),
    RESOURCE_ELECTRICITY_ENERGY_SOURCE_NOT_EXIST(4018, "Resource for electricity energy source does not exist."),
    WIDGET_DATA_NOT_EXIST(4019, "Widget data does not exist."),
    INVALID_DATE_RANGE(4020, "Date range provided is not valid."),
    RESOURCE_NOT_EXIST(4021, "Resource does not exist."),
    INVALID_MATCHING_BUILDING_LEVEL(4022, "New building level cannot match the old building level."),
    BUILDING_LEVEL_NOT_EXIST(4023, "Building level does not exist."),
    BUILDING_LEVEL_BEING_USED_BY_SENSORS(4024, "Building level is currently being used by one or more sensors."),
    SENSOR_SERIAL_NUMBER_EXISTS(4025, "Sensor serial number already exists."),
    ENERGY_SOURCE_NOT_EXIST(4026, "Energy source does not exist."),
    PURPOSE_NOT_EXIST_OR_INVALID(4027, "Purpose chosen does not exist or does not belong to the energy source chosen."),
    FACILITY_NOT_EXIST_OR_INVALID(4028, "Facility chosen does not exist or does not belong to the purpose chosen."),
    VIRTUAL_SENSOR_LOCKED(4029, "Virtual sensor serial number is temporarily locked. Please try again in a few minutes."),
    VIRTUAL_SENSOR_EXPRESSION_AND_LIST_FIELDS_REQUIRED(4030, "Virtual sensor requires both expression and list fields."),
    VIRTUAL_SENSOR_COMPLEXITY_TOO_HIGH(4031, "Virtual sensor complexity is too high, please reduce the number of sensor used."),
    VIRTUAL_SENSOR_EXPRESSION_AND_LIST_FIELDS_INVALID(4032, "Virtual sensor expression and list provided are not valid."),
    VIRTUAL_SENSOR_LIST_ITEMS_MISSING(4033, "Virtual sensor list items are missing in the expression."),
    SENSOR_SERIAL_NUMBER_NOT_EXIST(4034, "Sensor serial number does not exist."),
    RESOURCE_TYPE_AND_VALUE_EXISTS(4035, "Resource type and resource value chosen already exist."),
    RESOURCE_VALUE_INVALID(4036, "Resource value chosen is invalid."),
    BELONGS_TO_ENERGY_SOURCE_NOT_EXIST(4037, "Belongs to provided does not exist in energy source list."),
    BELONGS_TO_PURPOSE_NOT_EXIST(4038, "Belongs to provided does not exist in purpose list."),
    RESOURCE_TYPE_UPDATE_NOT_ALLOWED(4039, "Resource type of energy source cannot be updated."),
    ENERGY_SOURCE_USED_BY_SENSORS(4040, "Energy source is currently being used by one or more sensors."),
    PURPOSE_USED_BY_SENSORS(4041, "Purpose currently being used by one or more sensors."),
    FACILITY_USED_BY_SENSORS(4042, "Facility is currently being used by one or more sensors."),
    EXPRESSION_INVALID_RESULT(4043, "Expression and list provided cannot be deducted with a valid result."),
    SENSOR_RECORD_NOT_EXIST(4044, "A sensor record does not exist."),
    FILE_UPLOAD_TYPE_ERROR(4045, "Only JPEG/PNG file types are allowed to be uploaded."),
    FILE_UPLOAD_SIZE_ERROR(4046, "File size must be less than or equal to 5MB."),
    FILE_NOT_FOUND(4047, "Image file does not exist."),
    DEVICE_CONTROLLER_NOT_EXIST(4048, "Device controller does not exist."),
    DEVICE_CONTROLLER_SENSOR_IN_USED(4049, "Sensor for device controller already in used."),
    PROTOCOL_NOT_FOUND(4050, "Protocol connection does not exist."),
    CONTROLLER_ID_PROTOCOL_EXIST(4051, "Controller ID is already in use for the specified protocol type."),
    RESOURCE_REF_VAL_CANNOT_UPDATE(4052, "Resource reference values cannot be updated."),
    ENV_DEVICE_ID_EXIST(4053, "Environmental device ID already exist."),
    ENV_DEVICE_METADATA_AND_ENV_TYPE_EXISTS(4054, "A building level with the chosen environment type already exist."),
    ENV_DEVICE_ID_NOT_EXIST(4055, "Environmental device ID does not exist."),
    ENV_DEVICE_METADATA_NOT_EXIST(4056, "Environmental device metadata does not exist."),
    INVALID_ACTION_TYPE(4057, "Modbus action type does not exist."),
    INVALID_SET_ROOM_TEMPERATURE(4058, "Ensure set room temperature value is between 16℃ -30℃."),
    BUILDING_ID_ALREADY_EXIST(4059, "Building ID already exist."),
    SENSOR_METADATA_NOT_EXIST(4060, "Sensor metadata does not exist."),
    SENSOR_SN_IN_USE(4061, "Sensor serial number already in used."),
    EHP_CODE_IN_USE(4062, "Ehp code already in used."),
    EHP_PROFILE_NOT_EXIST(4063, "Ehp profile does not exist."),
    SENSOR_METADATA_DOES_NOT_BELONG_TO_BUILDING(4064, "Sensor metadata chosen does not belong to the building."),
    DEVICE_CONTROLLER_AND_EHP_PROFILE_DOES_NOT_BELONG_TO_BUILDING(4065, "Device controller and ehp profile chosen does not belong to the same building."),
    SENSOR_NOT_ALLOWED(4066, "Sensor serial number chosen is not allowed."),
    TARIFF_TYPE_NOT_EXIST(4067, "Tariff type does not exist."),
    WEATHER_NOT_FOUND(4068, "Weather data cannot be acquired."),
    GEOTHERMAL_PROFILE_NOT_EXIST(4069, "Geothermal profile does not exist."),
    BOILER_PROFILE_NOT_EXIST(4070, "Boiler profile does not exist."),
    BOILER_SENSOR_LOCKED(4071, "Boiler sensor serial number is temporarily locked. Please try again in a few minutes."),
    MODBUS_DEVICE_LOCKED(4072, "Modbus device protocol code is temporarily locked. Please try again in a few minutes."),
    TOKEN_DOES_NOT_EXIST(4073, "Token does not exist."),
    PAGINATION_VALUES_INVALID(4074, "Pagination values provided are invalid."),
    TARIFF_TYPE_CANNOT_MODIFIED(4075, "Tariff type cannot be modified."),
    SENSOR_LOG_NOT_EXIST(4076, "Sensor log ID does not exist."),
    FLAT_RATE_TARIFF_RECORD_NOT_EXIST(4077, "Flat rate tariff record does not exist."),
    BLOCK_TARIFF_RECORD_NOT_EXIST(4078, "Block tariff record does not exist."),
    FLAT_RATE_TARIFF_RECORD_CANNOT_MODIFIED(4079, "Flat rate tariff record cannot be modified."),
    BLOCK_TARIFF_RECORD_CANNOT_MODIFIED(4080, "Block tariff record cannot be modified."),
    BUILDING_SECTION_NOT_EXIST(4081, "Building section does not exist."),
    BUILDING_UNIT_NOT_EXIST(4082, "Building unit does not exist."),

    /**
     * Custom error codes (40xxx range).
     */
    SENSOR_RECORD_NOT_BELONG_BUILDING(40001, "A sensor record does not belong to this building."),
    SENSOR_RECORD_VIRTUAL_PROHIBITION(40002, "A sensor record is a virtual sensor and is not allowed to be used in another virtual sensor."),
    VIRTUAL_SENSOR_RECORD_INCOMPATIBLE(40003, "A sensor record is not compatible with the virtual sensor unit type."),
    BUILDING_AND_SECTION_NAME_EXISTS(40004, "A building and a section name already exist."),
    BUILDING_SECTION_AND_LEVEL_NAME_EXISTS(40005, "A building section and a level name already exist."),
    BUILDING_LEVEL_AND_UNIT_NAME_EXISTS(40006, "A building level and a unit name already exist."),

    /**
     * Server-side errors (5xxx range).
     */
    PROCESSING_ERROR(5000, "Something went wrong when processing request."),
    UNCAUGHT_EXCEPTION(5001, "An uncaught exception occurred when processing request."),
    DATA_ACCESS_EXCEPTION(5002, "Data access exception occurred."),
    ENTITY_NOT_FOUND_EXCEPTION(5003, "Entity not found exception occurred."),
    FILE_UPLOAD_ERROR(5004, "Something went wrong when uploading the file."),
    FILE_DELETE_ERROR(5005, "Something went wrong when deleting the file."),
    MODBUS_READ_ERROR(5006, "Something went wrong when reading the device Modbus."),
    MODBUS_WRITE_ERROR(5007, "Something went wrong when writing to the device Modbus."),
    MODBUS_INSTANCE_NOT_EXIST(5008, "Modbus service instance does not exist."),
    UNSUPPORTED_MEDIA_TYPE_EXCEPTION(5009, "Unsupported media type exception occurred.");

    /**
     * The numeric code associated with the error.
     */
    private final int code;

    /**
     * The descriptive message associated with the error.
     */
    private final String message;
}

