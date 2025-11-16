package com.vinsys.hrms.kra.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class KraCeoDashboardResponseVO {

    private List<AggregatedScoreVO> departmentScores;
    private AggregatedScoreVO organizationScore;
    private AggregatedScoreVO departmentScore;

    public List<AggregatedScoreVO> getDepartmentScores() {
        return departmentScores;
    }

    public void setDepartmentScores(List<AggregatedScoreVO> departmentScores) {
        this.departmentScores = departmentScores;
    }

    public AggregatedScoreVO getOrganizationScore() {
        return organizationScore;
    }

    public void setOrganizationScore(AggregatedScoreVO organizationScore) {
        this.organizationScore = organizationScore;
    }

    public AggregatedScoreVO getDepartmentScore() {
        return departmentScore;
    }

    public void setDepartmentScore(AggregatedScoreVO departmentScore) {
        this.departmentScore = departmentScore;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AggregatedScoreVO {
        private Long departmentId;
        private String departmentName;
        private Double aggRating;
        private String aggRatingLabel1;
        private String aggRatingLabel2;
        private String aiFeedback;

        public Long getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(Long departmentId) {
            this.departmentId = departmentId;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public Double getAggRating() {
            return aggRating;
        }

        public void setAggRating(Double aggRating) {
            this.aggRating = aggRating;
        }

        public String getAggRatingLabel1() {
            return aggRatingLabel1;
        }

        public void setAggRatingLabel1(String aggRatingLabel1) {
            this.aggRatingLabel1 = aggRatingLabel1;
        }

        public String getAggRatingLabel2() {
            return aggRatingLabel2;
        }

        public void setAggRatingLabel2(String aggRatingLabel2) {
            this.aggRatingLabel2 = aggRatingLabel2;
        }

        public String getAiFeedback() {
            return aiFeedback;
        }

        public void setAiFeedback(String aiFeedback) {
            this.aiFeedback = aiFeedback;
        }
    }
}
