package com.vinsys.hrms.idp.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A generic wrapper for API responses used across the IDP module.
 * <p>
 * Provides a consistent structure with {@code status} and {@code data}
 * fields, along with static factory methods for success and error responses.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponseVO {

    /** Response status (e.g., "success", "error"). */
    private String status;

    /** Payload data returned from the API (can be any object type). */
    private Object data;

    /**
     * Factory method for a successful response.
     *
     * @param data the response payload
     * @return a success response containing the provided data
     */
    public static GenericResponseVO success(Object data) {
        return new GenericResponseVO("success", data);
    }

    /**
     * Factory method for an error response.
     *
     * @param data the error details or message
     * @return an error response containing the provided data
     */
    public static GenericResponseVO error(Object data) {
        return new GenericResponseVO("error", data);
    }
}