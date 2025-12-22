package com.vinsys.hrms.idp.trainingcatalog.controller;

import com.vinsys.hrms.audit.service.IAuditLogService;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.helper.ResponseGenerator;
import com.vinsys.hrms.idp.trainingcatalog.service.ITrainingCatalogService;
import com.vinsys.hrms.idp.trainingcatalog.vo.*;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainingCatalog")
public class TrainingCatalogController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ITrainingCatalogService trainingCatalogService;
    private final BackendProperties props;
    private final IAuditLogService auditLogService;


    public TrainingCatalogController(final ITrainingCatalogService trainingCatalogService,
                                     final BackendProperties props, final IAuditLogService auditLogService) {
        this.trainingCatalogService = trainingCatalogService;
        this.props = props;
        this.auditLogService = auditLogService;
    }

    @PostMapping
    HRMSBaseResponse<String> addUpdateTrainingCatalog(@RequestBody TrainingCatalogVo trainingCatalogVo) {
        HRMSBaseResponse<String> response;
        try {
            auditLogService.setActionHeader("IDP_AddUpdateTraining");
            response = trainingCatalogService.addUpdateTrainingCatalog(trainingCatalogVo);
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

    @PostMapping(value = "updateStatus")
    HRMSBaseResponse<String> updateTrainingStatus(@RequestBody TrainingCatalogStatus trainingCatalogStatus) {
        HRMSBaseResponse<String> response;
        try {
            auditLogService.setActionHeader("IDP_UpdateTrainingStatus");
            response = trainingCatalogService.updateTrainingStatus(trainingCatalogStatus);
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

    @GetMapping
    HRMSBaseResponse<?> getById(@RequestParam(value = "id") Long id) {
        HRMSBaseResponse<?> response;
        try {
            response = trainingCatalogService.getById(id);
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

    @PostMapping("list")
    HRMSBaseResponse<?> getAllTrainingCatalogs(
            @RequestBody TrainingCatalogListingReq request,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
        HRMSBaseResponse<?> response;
        try {
            response = trainingCatalogService.getAllTrainingCatalogs(request, pageable);
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

    @PostMapping("list/download")
    ResponseEntity<byte[]> getAllTrainingCatalogsExcel(
            @RequestBody TrainingCatalogListingReq request) {
        try {
            byte[] response = trainingCatalogService.getAllTrainingCatalogsExcel(request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String fileName = "TrainingCatalogList_Report.xlsx";
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment").filename(fileName).build());

            return ResponseEntity.ok().headers(headers).body(response);
        } catch (HRMSException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error: " + e.getResponseMessage()).getBytes());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
        }
    }

    @GetMapping("competencyTypes")
    HRMSBaseResponse<?> getCompetencyTypes() {
        HRMSBaseResponse<List<CompetencyDDLVo>> response = trainingCatalogService.getCompetencyTypes();
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @GetMapping("competencySubTypes")
    HRMSBaseResponse<List<CompetencyDDLVo>> getCompetencySubTypes(@RequestParam(value = "id") Integer id) {
        HRMSBaseResponse<List<CompetencyDDLVo>> response = trainingCatalogService.getCompetencySubTypes(id);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @PostMapping(value = "searchTopics")
    HRMSBaseResponse<?> searchTopics(@RequestBody SearchTopicReq request,
                                     @RequestParam(required = false) String keyword,
                                     @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
        HRMSBaseResponse<?> response;
        try {
            auditLogService.setActionHeader("IDP_searchTopics");
            response = trainingCatalogService.searchTopics(request, keyword, pageable);
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

    @PostMapping(value = "budget")
    HRMSBaseResponse<String> addUpdateTrainingBudget(@RequestBody TrainingBudgetVo trainingBudgetVo) {
        HRMSBaseResponse<String> response;
        try {
            auditLogService.setActionHeader("IDP_AddUpdateTrainingBudget");
            response = trainingCatalogService.addUpdateTrainingBudget(trainingBudgetVo);
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

    @PostMapping(value = "budget/updateStatus")
    HRMSBaseResponse<String> updateTrainingBudgetStatus(@RequestBody TrainingBudgetStatus trainingBudgetStatus) {
        HRMSBaseResponse<String> response;
        try {
            auditLogService.setActionHeader("IDP_UpdateTrainingBudgetStatus");
            response = trainingCatalogService.updateTrainingBudgetStatus(trainingBudgetStatus);
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

    @GetMapping("budget/list")
    HRMSBaseResponse<?> getAllTrainingBudgets(
            @RequestParam(required = false) Integer year,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
        HRMSBaseResponse<?> response;
        try {
            response = trainingCatalogService.getAllTrainingBudgets(year, pageable);
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

    @GetMapping("budget/list/download")
    ResponseEntity<byte[]> getAllTrainingBudgetsExcel(
            @RequestParam(required = false) Integer year) {
        try {
            byte[] response = trainingCatalogService.getAllTrainingBudgetsExcel(year);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String fileName = "TrainingBudgetList_Report.xlsx";
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment").filename(fileName).build());

            return ResponseEntity.ok().headers(headers).body(response);
        } catch (HRMSException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error: " + e.getResponseMessage()).getBytes());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
        }
    }

}
