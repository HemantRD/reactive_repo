package com.vinsys.hrms.idp.enumconstant;

public enum IdpFlowHistoryActionType {

    PENDING("Pending"),
    SUBMIT("Submit"),
    APPROVE("Approve"),
    REJECT("Reject"),
    COMPLETE("Complete"),
    ABORT("Abort");

    private final String value;

    IdpFlowHistoryActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}