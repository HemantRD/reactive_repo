package com.vinsys.hrms.idp.enumconstant;

public enum IdpFlowHistoryEmployeeRole {

    TEAM_MEMBER_SELF("Team Member (Self)"),
    LINE_MANAGER("Line Manager"),
    REPORTING_MANAGER("Reporting Manager"),
    HEAD_OF_DEPARTMENT("Head of Department"),
    HEAD_OF_TD("Head of TD"),
    HEAD_OF_HR("Head of HR");

    private final String value;

    IdpFlowHistoryEmployeeRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}