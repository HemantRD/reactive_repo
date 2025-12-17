package com.vinsys.hrms.idp.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

/**
 * Generic IDP Request/Response VO
 * Consolidates all IDP operations into a single, flexible structure
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpRequestVO {

    // ===== IDP Master Fields =====
    private Long id;
    private Long activeFlowId;
    private Long employeeId;
    private String employeeName;
    private Long departmentId;
    private Long divId;
    private String position;
    private String function;
    private String grade;
    private String department;
    private String designation;
    private Instant createdDate;
    private String status;
    private String recordStatus;

    // ===== Workflow & Actions =====
    private String action; // For submit, approve, reject operations
    private List<String> allowedActions;

    // ===== Nested Collections =====
    @Valid
    private List<IdpDetailRequestVO> idpDetails;

    @Valid
    private List<IdpFlowRequestVO> flowDetails;

    @Valid
    private List<IdpProgressRequestVO> progressDetails;

    // ===== Filter/Query Parameters =====
    private String role; // For filtering in getAllIdp
    private List<Long> employeeIds; // For bulk operations
    private List<String> statuses; // For filtering by multiple statuses

    /**
     * Nested VO for IDP Detail information
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdpDetailRequestVO {
        private Long id;
        private Long competencyTypeId;
        private Long competencySubTypeId;
        private String competencyTypeName;
        private String competencySubTypeName;
        private String devGoal;
        private String devActions;
        private Long trainingId;
        private String trainingName;
        private String status;
        private String comment;
        private String priority;
        private Object trainingObj; // For flexible training data
        private String completionCertificateFilePath;
        private List<IdpProgressRequestVO> progressDetails;
    }

    /**
     * Nested VO for Flow/Workflow information
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdpFlowRequestVO {
        private Long id;
        private Long ownerEmployeeId;
        private String ownerEmployeeName;
        private String ownerEmployeeRole;
        private Instant startDate;
        private String action;
        private Instant actionDate;
        private String status;
        private String recordStatus;
    }

    /**
     * Nested VO for Progress tracking
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdpProgressRequestVO {
        private Long id;
        private Long idpDetailId;
        private String progressUpdate;
        private Instant updateDate;
        private String status;
        private Integer percentComplete;
    }
}
