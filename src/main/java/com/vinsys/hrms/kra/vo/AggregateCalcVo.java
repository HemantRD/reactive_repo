package com.vinsys.hrms.kra.vo;

public class AggregateCalcVo {
    private Long deptId;
    private Long divId;
    private Long organizationId;
    private Double selfRating;
    private Double managerRating;
    private Double calibratedRating;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDivId() {
        return divId;
    }

    public void setDivId(Long divId) {
        this.divId = divId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Double getSelfRating() {
        return selfRating;
    }

    public void setSelfRating(Double selfRating) {
        this.selfRating = selfRating;
    }

    public Double getManagerRating() {
        return managerRating;
    }

    public void setManagerRating(Double managerRating) {
        this.managerRating = managerRating;
    }

    public Double getCalibratedRating() {
        return calibratedRating;
    }

    public void setCalibratedRating(Double calibratedRating) {
        this.calibratedRating = calibratedRating;
    }
}
