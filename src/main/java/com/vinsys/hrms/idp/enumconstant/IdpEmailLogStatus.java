package com.vinsys.hrms.idp.enumconstant;

public enum IdpEmailLogStatus {

    PENDING("Pending"),
    SENDING("Sending"),
    SENT("Sent");

    private final String value;

    IdpEmailLogStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}