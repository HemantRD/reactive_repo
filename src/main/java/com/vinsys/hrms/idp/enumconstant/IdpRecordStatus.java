package com.vinsys.hrms.idp.enumconstant;

public enum IdpRecordStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    IdpRecordStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}