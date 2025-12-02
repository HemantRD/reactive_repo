package com.vinsys.hrms.idp.enumconstant;

public enum IdpStatus {
    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    IN_REVIEW("In Review"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    ABORTED("Aborted");

    private final String value;

    IdpStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}