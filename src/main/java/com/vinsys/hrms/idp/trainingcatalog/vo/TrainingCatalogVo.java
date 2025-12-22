package com.vinsys.hrms.idp.trainingcatalog.vo;

import java.util.List;

public class TrainingCatalogVo {
    private Long id;
    private String trainingCode;
    private String topicName;
    private Integer competencyTypeId;
    private Integer competencySubTypeId;
    private Boolean isInternal;
    private Double costPerPersonIndividual;
    private Double costPerPersonGroup;
    private Double costPerGroup;
    private Integer minPersonInGroup;
    private Integer maxPersonInGroup;
    private Boolean isCertificationCourse;
    private Integer priority;
    private Integer durationInHours;
    private List<String> keywords;
    private String remark;
    private Boolean status;

    public TrainingCatalogVo() {

    }

    public TrainingCatalogVo(final Long id, final String trainingCode, final String topicName,
                             final Integer competencyTypeId, final Integer competencySubTypeId, final Boolean isInternal,
                             final Double costPerPersonIndividual, final Double costPerPersonGroup, final Double costPerGroup,
                             final Integer minPersonInGroup, final Integer maxPersonInGroup, final Boolean isCertificationCourse,
                             final Integer priority, final Integer durationInHours, final String remark, final String status) {
        this.id = id;
        this.trainingCode = trainingCode;
        this.topicName = topicName;
        this.competencyTypeId = competencyTypeId;
        this.competencySubTypeId = competencySubTypeId;
        this.isInternal = isInternal;
        this.costPerPersonIndividual = costPerPersonIndividual;
        this.costPerPersonGroup = costPerPersonGroup;
        this.costPerGroup = costPerGroup;
        this.minPersonInGroup = minPersonInGroup;
        this.maxPersonInGroup = maxPersonInGroup;
        this.isCertificationCourse = isCertificationCourse;
        this.priority = priority;
        this.durationInHours = durationInHours;
        this.remark = remark;
        this.status = status.equalsIgnoreCase("Y");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingCode() {
        return trainingCode;
    }

    public void setTrainingCode(String trainingCode) {
        this.trainingCode = trainingCode;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getCompetencyTypeId() {
        return competencyTypeId;
    }

    public void setCompetencyTypeId(Integer competencyTypeId) {
        this.competencyTypeId = competencyTypeId;
    }

    public Integer getCompetencySubTypeId() {
        return competencySubTypeId;
    }

    public void setCompetencySubTypeId(Integer competencySubTypeId) {
        this.competencySubTypeId = competencySubTypeId;
    }

    public Boolean getInternal() {
        return isInternal;
    }

    public void setInternal(Boolean internal) {
        isInternal = internal;
    }

    public Double getCostPerPersonIndividual() {
        return costPerPersonIndividual;
    }

    public void setCostPerPersonIndividual(Double costPerPersonIndividual) {
        this.costPerPersonIndividual = costPerPersonIndividual;
    }

    public Double getCostPerPersonGroup() {
        return costPerPersonGroup;
    }

    public void setCostPerPersonGroup(Double costPerPersonGroup) {
        this.costPerPersonGroup = costPerPersonGroup;
    }

    public Double getCostPerGroup() {
        return costPerGroup;
    }

    public void setCostPerGroup(Double costPerGroup) {
        this.costPerGroup = costPerGroup;
    }

    public Integer getMinPersonInGroup() {
        return minPersonInGroup;
    }

    public void setMinPersonInGroup(Integer minPersonInGroup) {
        this.minPersonInGroup = minPersonInGroup;
    }

    public Integer getMaxPersonInGroup() {
        return maxPersonInGroup;
    }

    public void setMaxPersonInGroup(Integer maxPersonInGroup) {
        this.maxPersonInGroup = maxPersonInGroup;
    }

    public Boolean getCertificationCourse() {
        return isCertificationCourse;
    }

    public void setCertificationCourse(Boolean certificationCourse) {
        isCertificationCourse = certificationCourse;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(Integer durationInHours) {
        this.durationInHours = durationInHours;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
