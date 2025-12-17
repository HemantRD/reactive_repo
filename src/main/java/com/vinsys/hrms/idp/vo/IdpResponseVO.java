package com.vinsys.hrms.idp.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic IDP Response VO with type safety
 * Replaces multiple response VOs with a single generic structure
 *
 * @param <T> The type of data being returned
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpResponseVO<T> {

    /** Response status: "success" or "error" */
    private String status;

    /** Response message (optional) */
    private String message;

    /** Response code (optional, for error handling) */
    private Integer code;

    /** The actual response data */
    private T data;

    /** Pagination info (optional) */
    private PaginationInfo pagination;

    /**
     * Factory method for success response
     */
    public static <T> IdpResponseVO<T> success(T data) {
        return IdpResponseVO.<T>builder()
                .status("success")
                .data(data)
                .build();
    }

    /**
     * Factory method for success response with message
     */
    public static <T> IdpResponseVO<T> success(T data, String message) {
        return IdpResponseVO.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Factory method for error response
     */
    public static <T> IdpResponseVO<T> error(String message) {
        return IdpResponseVO.<T>builder()
                .status("error")
                .message(message)
                .build();
    }

    /**
     * Factory method for error response with code
     */
    public static <T> IdpResponseVO<T> error(Integer code, String message) {
        return IdpResponseVO.<T>builder()
                .status("error")
                .code(code)
                .message(message)
                .build();
    }

    /**
     * Pagination information
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationInfo {
        private Integer page;
        private Integer size;
        private Long total;
        private Integer totalPages;
    }

    /**
     * Wrapper for list data with pagination
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListData<T> {
        private List<T> items;
        private Long total;
    }
}
