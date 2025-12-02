package com.vinsys.hrms.kra.entity;

import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDivision;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_trn_kra_aggregate_scores")
public class KraAggregateScores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "org_id")
    private Long orgId;
    @Column(name = "year")
    private Long year;
    @Column(name = "mst_kra_cycle_id")
    private Long kraCycleId;
    @Column(name = "level_of_aggregation")
    private String levelOfAggregation;
    @Column(name = "branch_id")
    private Long branchId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private MasterDepartment department;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    private MasterDivision division;
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "sub_category_id")
    private Long subCategoryId;
    @Column(name = "aggregate_self_rating")
    private Double aggregateSelfRating;
    @Column(name = "aggregate_rm_rating")
    private Double aggregateRmRating;
    @Column(name = "aggregate_calibrated_rating")
    private Double aggregateCalibratedRating;
    @Column(name = "hod_ai_comment")
    private String hodAiComment;
    @Column(name = "hr_ai_comment")
    private String hrAiComment;
    @Column(name = "ceo_ai_comment")
    private String ceoAiComment;
    @Column(name = "calibrate_ai_comment")
    private String calibrateAiComment;
    @Column(name = "modified_date")
    private Date modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getKraCycleId() {
        return kraCycleId;
    }

    public void setKraCycleId(Long kraCycleId) {
        this.kraCycleId = kraCycleId;
    }

    public String getLevelOfAggregation() {
        return levelOfAggregation;
    }

    public void setLevelOfAggregation(String levelOfAggregation) {
        this.levelOfAggregation = levelOfAggregation;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public MasterDepartment getDepartment() {
        return department;
    }

    public void setDepartment(MasterDepartment department) {
        this.department = department;
    }

    public MasterDivision getDivision() {
        return division;
    }

    public void setDivision(MasterDivision division) {
        this.division = division;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Double getAggregateSelfRating() {
        return aggregateSelfRating;
    }

    public void setAggregateSelfRating(Double aggregateSelfRating) {
        this.aggregateSelfRating = aggregateSelfRating;
    }

    public Double getAggregateRmRating() {
        return aggregateRmRating;
    }

    public void setAggregateRmRating(Double aggregateRmRating) {
        this.aggregateRmRating = aggregateRmRating;
    }

    public Double getAggregateCalibratedRating() {
        return aggregateCalibratedRating;
    }

    public void setAggregateCalibratedRating(Double aggregateCalibratedRating) {
        this.aggregateCalibratedRating = aggregateCalibratedRating;
    }

    public String getHodAiComment() {
        return hodAiComment;
    }

    public void setHodAiComment(String hodAiComment) {
        this.hodAiComment = hodAiComment;
    }

    public String getHrAiComment() {
        return hrAiComment;
    }

    public void setHrAiComment(String hrAiComment) {
        this.hrAiComment = hrAiComment;
    }

    public String getCeoAiComment() {
        return ceoAiComment;
    }

    public void setCeoAiComment(String ceoAiComment) {
        this.ceoAiComment = ceoAiComment;
    }

    public String getCalibrateAiComment() {
        return calibrateAiComment;
    }

    public void setCalibrateAiComment(String calibrateAiComment) {
        this.calibrateAiComment = calibrateAiComment;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
