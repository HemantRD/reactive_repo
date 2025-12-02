package com.vinsys.hrms.idp.progress.controller;

import com.vinsys.hrms.audit.service.IAuditLogService;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.helper.ResponseGenerator;
import com.vinsys.hrms.idp.progress.service.IProgressService;
import com.vinsys.hrms.spring.BackendProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/idp")
public class ProgressController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final IProgressService progressService;
    private final BackendProperties props;
    private final IAuditLogService auditLogService;

    public ProgressController(final IProgressService progressService,
                              final BackendProperties props, final IAuditLogService auditLogService) {
        this.progressService = progressService;
        this.props = props;
        this.auditLogService = auditLogService;
    }

    @PostMapping(value = "trainingProgress/update")
    HRMSBaseResponse<?> uploadBulkProgressExcel(@RequestParam("file") MultipartFile requestFile) {
        HRMSBaseResponse<?> response;
        try {
            auditLogService.setActionHeader("IDP_TrainingProgressUpdateExcel");
            response = progressService.uploadBulkProgressExcel(requestFile);
            response.setApplicationVersion(props.getApp_version());
        } catch (HRMSException e) {
            log.error(e.getMessage(), e);
            response = ResponseGenerator.getValidationResponse(e, props.getApp_version());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = ResponseGenerator.getErrorResponse(e, props.getApp_version());
        }
        return response;
    }
}
