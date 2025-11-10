package com.vinsys.hrms.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties
@Validated
public class BackendProperties {
    @NotBlank
    private String app_version;
    @NotBlank
    private String idpEmailSendScheduler;
    @NotBlank
    private String idpEmailReminderQueueScheduler;


    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getIdpEmailSendScheduler() {
        return idpEmailSendScheduler;
    }

    public void setIdpEmailSendScheduler(String idpEmailSendScheduler) {
        this.idpEmailSendScheduler = idpEmailSendScheduler;
    }

    public String getIdpEmailReminderQueueScheduler() {
        return idpEmailReminderQueueScheduler;
    }

    public void setIdpEmailReminderQueueScheduler(String idpEmailReminderQueueScheduler) {
        this.idpEmailReminderQueueScheduler = idpEmailReminderQueueScheduler;
    }
}
