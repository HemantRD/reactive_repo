package com.vinsys.hrms.kra.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiMsEmployeeFeedbackAnalysisResponseVO {
    private Long kraId;
    private String summary;
    private String aiFeedback;
    private String feedbackType;
    private List<ObjectiveFeedbackVo> objectivesFeedbackList;
    private String status;
    private Long code;
    private String message;
    private String generatedAt;

    public Long getKraId() {
        return kraId;
    }

    public void setKraId(Long kraId) {
        this.kraId = kraId;
    }

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

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public List<ObjectiveFeedbackVo> getObjectivesFeedbackList() {
        return objectivesFeedbackList;
    }

    public void setObjectivesFeedbackList(List<ObjectiveFeedbackVo> objectivesFeedbackList) {
        this.objectivesFeedbackList = objectivesFeedbackList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ObjectiveFeedbackVo {
        private Long id;
        private String summary;
        private String aiFeedback;
        private String aiDraft;
        private String feedbackType;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

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
    }
}
