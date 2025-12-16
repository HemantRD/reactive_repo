package com.vinsys.hrms.idp.enumconstant;

public enum IdpFlowHistoryStatus {

    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    PENDING("Pending"),
    IN_REVIEW("In Review"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    ABORTED("Aborted");

    private final String value;

    IdpFlowHistoryStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}