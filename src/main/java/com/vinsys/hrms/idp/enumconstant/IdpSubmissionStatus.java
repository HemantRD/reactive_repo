package com.vinsys.hrms.idp.enumconstant;

public enum IdpSubmissionStatus {

    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    CLOSE("Close");

    private final String value;

    IdpSubmissionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}