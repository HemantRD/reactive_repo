package com.vinsys.hrms.kra.vo;

import java.util.List;

public class AiMsBackgroundAnalysisRequestInvokerVO {
    private String role;
    private List<String> roles;
    private String purpose;
    private Long loggedInEmployeeId;
    private Long ratingId;

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

}
