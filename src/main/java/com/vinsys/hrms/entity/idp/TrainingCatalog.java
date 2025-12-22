package com.vinsys.hrms.entity.idp;

import com.vinsys.hrms.entity.AuditBase;

import javax.persistence.*;

@Entity
@Table(name = "tbl_mst_idp_training_cateloge")
public class TrainingCatalog extends AuditBase {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "seq_mst_idp_training_cateloge",
            sequenceName = "seq_mst_idp_training_cateloge", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_idp_training_cateloge")
    private Long id;

    @Column(name = "training_code", length = 50, nullable = false)
            private String trainingCode;

    @Column(name = "topic_name", length = 50, nullable = false)
    private String topicName;

    @Column(name = "competency_type_id", nullable = false)
    private Integer competencyTypeId;

    @Column(name = "competency_sub_type_id", nullable = false)
    private Integer competencySubTypeId;

    @Column(name = "is_internal", nullable = false)
    private Boolean isInternal;

    @Column(name = "cost_per_person_individual", nullable = false)
    private Double costPerPersonIndividual;

    @Column(name = "cost_per_person_group", nullable = false)
    private Double costPerPersonGroup;

    @Column(name = "cost_per_group", nullable = false)
    private Double costPerGroup;

    @Column(name = "min_person_in_group", nullable = false)
    private Integer minPersonInGroup;

    @Column(name = "max_person_in_group", nullable = false)
    private Integer maxPersonInGroup;

    @Column(name = "is_certification_course", nullable = false)
    private Boolean isCertificationCourse;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "duration_in_hours", nullable = false)
    private Integer durationInHours;

    public static long getSerialversionuid() {
        return serialVersionUID;
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
}
