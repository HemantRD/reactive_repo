package com.vinsys.hrms.kra.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiMsDepartmentLevelAnalysisResponseVO {

    private String status;
    private Integer code;
    private String message;
    private String error;
    private String generatedAt;
    private List<DepartmentDataVo> departments;
    private OverallSummaryVO overallSummary;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

    public List<DepartmentDataVo> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentDataVo> departments) {
        this.departments = departments;
    }

    public OverallSummaryVO getOverallSummary() {
        return overallSummary;
    }

    public void setOverallSummary(OverallSummaryVO overallSummary) {
        this.overallSummary = overallSummary;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DepartmentDataVo {
        private Long id;
        private String departmentName;
        private Double aiDepartmentAvgRating;
        private String aiDepartmentAvgRatingLabel;
        private AiFeedbackObjectVO aiFeedbackObj;
        private List<EmployeeDataVo> departmentEmployees;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public AiFeedbackObjectVO getAiFeedbackObj() {
            return aiFeedbackObj;
        }

        public void setAiFeedbackObj(AiFeedbackObjectVO aiFeedbackObj) {
            this.aiFeedbackObj = aiFeedbackObj;
        }

        public List<EmployeeDataVo> getDepartmentEmployees() {
            return departmentEmployees;
        }

        public void setDepartmentEmployees(List<EmployeeDataVo> departmentEmployees) {
            this.departmentEmployees = departmentEmployees;
        }

        public Double getAiDepartmentAvgRating() {
            return aiDepartmentAvgRating;
        }

        public void setAiDepartmentAvgRating(Double aiDepartmentAvgRating) {
            this.aiDepartmentAvgRating = aiDepartmentAvgRating;
        }

        public String getAiDepartmentAvgRatingLabel() {
            return aiDepartmentAvgRatingLabel;
        }

        public void setAiDepartmentAvgRatingLabel(String aiDepartmentAvgRatingLabel) {
            this.aiDepartmentAvgRatingLabel = aiDepartmentAvgRatingLabel;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EmployeeDataVo {
        private Long kraId;
        private Long employeeId;
        private AiFeedbackObjectVO aiFeedbackObj;

        public Long getKraId() {
            return kraId;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public AiFeedbackObjectVO getAiFeedbackObj() {
            return aiFeedbackObj;
        }

        public void setKraId(Long kraId) {
            this.kraId = kraId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public void setAiFeedbackObj(AiFeedbackObjectVO aiFeedbackObj) {
            this.aiFeedbackObj = aiFeedbackObj;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OverallSummaryVO {
        private AiFeedbackObjectVO aiFeedbackObj;

        public AiFeedbackObjectVO getAiFeedbackObj() {
            return aiFeedbackObj;
        }

        public void setAiFeedbackObj(AiFeedbackObjectVO aiFeedbackObj) {
            this.aiFeedbackObj = aiFeedbackObj;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AiFeedbackObjectVO {
        private String summary;
        private String aiFeedback;
        private String aiDraft;
        private String feedbackType;
        private Double aiRatingValue;
        private String aiRatingLabel;

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getAiFeedback() {
            return aiFeedback;
        }

        public void setAiFeedback(String aiFeedback) {
            this.aiFeedback = aiFeedback;
        }

        public String getAiDraft() {
            return aiDraft;
        }

        public void setAiDraft(String aiDraft) {
            this.aiDraft = aiDraft;
        }

        public String getFeedbackType() {
            return feedbackType;
        }

        public void setFeedbackType(String feedbackType) {
            this.feedbackType = feedbackType;
        }

        public Double getAiRatingValue() {
            return aiRatingValue;
        }

        public void setAiRatingValue(Double aiRatingValue) {
            this.aiRatingValue = aiRatingValue;
        }

        public String getAiRatingLabel() {
            return aiRatingLabel;
        }

        public void setAiRatingLabel(String aiRatingLabel) {
            this.aiRatingLabel = aiRatingLabel;
        }
    }
}
