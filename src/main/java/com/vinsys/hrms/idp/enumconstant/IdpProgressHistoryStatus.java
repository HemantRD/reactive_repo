package com.vinsys.hrms.idp.enumconstant;

public enum IdpProgressHistoryStatus {

    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    ABORTED("Aborted");

    private final String value;

    IdpProgressHistoryStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}