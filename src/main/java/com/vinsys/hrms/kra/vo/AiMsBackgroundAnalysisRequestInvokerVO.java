package com.vinsys.hrms.kra.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vinsys.hrms.kra.entity.KraCycle;

import java.util.List;

public class AiMsBackgroundAnalysisRequestInvokerVO {
    private String role;
    private List<String> roles;
    private String purpose;
    private String levelOfAnalysis;
    private Long loggedInEmployeeId;
    private Long ratingId;
    private KraCycle cycle;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getLevelOfAnalysis() {
        return levelOfAnalysis;
    }

    public void setLevelOfAnalysis(String levelOfAnalysis) {
        this.levelOfAnalysis = levelOfAnalysis;
    }

    public Long getLoggedInEmployeeId() {
        return loggedInEmployeeId;
    }

    public void setLoggedInEmployeeId(Long loggedInEmployeeId) {
        this.loggedInEmployeeId = loggedInEmployeeId;
    }

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public KraCycle getCycle() {
        return cycle;
    }

    public void setCycle(KraCycle cycle) {
        this.cycle = cycle;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AiMsBackgroundAnalysisProgressStatus {
        private String purpose;
        private String status;
        private Integer totalEmployeeCount;
        private Integer processedEmployeeCount;
        private Double progressPercentage;
        private String processingStartedOn;
        private String lastStatusUpdatedOn;

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getTotalEmployeeCount() {
            return totalEmployeeCount;
        }

        public void setTotalEmployeeCount(Integer totalEmployeeCount) {
            this.totalEmployeeCount = totalEmployeeCount;
        }

        public Integer getProcessedEmployeeCount() {
            return processedEmployeeCount;
        }

        public void setProcessedEmployeeCount(Integer processedEmployeeCount) {
            this.processedEmployeeCount = processedEmployeeCount;
        }

        public Double getProgressPercentage() {
            return progressPercentage;
        }

        public void setProgressPercentage(Double progressPercentage) {
            this.progressPercentage = progressPercentage;
        }

        public String getProcessingStartedOn() {
            return processingStartedOn;
        }

        public void setProcessingStartedOn(String processingStartedOn) {
            this.processingStartedOn = processingStartedOn;
        }

        public String getLastStatusUpdatedOn() {
            return lastStatusUpdatedOn;
        }

        public void setLastStatusUpdatedOn(String lastStatusUpdatedOn) {
            this.lastStatusUpdatedOn = lastStatusUpdatedOn;
        }

    }
}
