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

}
