package com.vinsys.hrms.kra.vo;

import java.util.List;

public class AiMsBackgroundAnalysisRequestInvokerVO {
    private List<String> roles;
    private Long loggedInEmployeeId;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Long getLoggedInEmployeeId() {
        return loggedInEmployeeId;
    }

    public void setLoggedInEmployeeId(Long loggedInEmployeeId) {
        this.loggedInEmployeeId = loggedInEmployeeId;
    }

}
