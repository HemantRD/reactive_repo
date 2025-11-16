package com.vinsys.hrms.idp.enumconstant;

public enum IdpFlowHistoryActionType {

    PENDING("Pending"),
    SUBMITTED("Submitted"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    COMPLETED("Completed"),
    ABORTED("Aborted");

    private final String value;

    IdpFlowHistoryActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}