package com.vinsys.hrms.idp.enumconstant;

public enum IdpConfigKeyName {

    TD_BUDGET("TD_BUDGET"),

    EMAIL_TEMPLATE_INFO_LM("EMAIL_TEMPLATE_INFO_LM"),
    EMAIL_TEMPLATE_INFO_FH("EMAIL_TEMPLATE_INFO_FH"),
    EMAIL_TEMPLATE_INFO_HOD("EMAIL_TEMPLATE_INFO_HOD"),
    EMAIL_TEMPLATE_INFO_HTD("EMAIL_TEMPLATE_INFO_HTD"),

    EMAIL_TEMPLATE_FU_LM("EMAIL_TEMPLATE_FU_LM"),
    EMAIL_TEMPLATE_FU_FH("EMAIL_TEMPLATE_FU_FH"),
    EMAIL_TEMPLATE_FU_HOD("EMAIL_TEMPLATE_FU_HOD"),
    EMAIL_TEMPLATE_FU_HTD("EMAIL_TEMPLATE_FU_HTD");

    private final String value;

    IdpConfigKeyName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}