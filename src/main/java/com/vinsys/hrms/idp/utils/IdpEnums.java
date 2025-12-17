package com.vinsys.hrms.idp.utils;

public class IdpEnums {


    public enum Status {
        PENDING("Pending");
        private String key;

        Status(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public enum EmailType {
        INFORMATION("Information"),
        FOLLOWUP("Followup");
        private String key;

        EmailType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public enum ReportType {
        SUMMARY("summary"),
        DETAILED("detailed");
        private String key;

        ReportType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public enum EmailTemplates {
        EMAIL_TEMPLATE_INFO_TM("EMAIL_TEMPLATE_INFO_TM"),
        EMAIL_TEMPLATE_INFO_LM("EMAIL_TEMPLATE_INFO_LM"),
        EMAIL_TEMPLATE_INFO_TDHEAD("EMAIL_TEMPLATE_INFO_TDHEAD"),
        EMAIL_TEMPLATE_FU_TM("EMAIL_TEMPLATE_FU_TM"),
        EMAIL_TEMPLATE_FU_LM("EMAIL_TEMPLATE_FU_LM"),
        EMAIL_TEMPLATE_FU_TDHEAD("EMAIL_TEMPLATE_FU_TDHEAD");
        private String key;

        EmailTemplates(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

}
