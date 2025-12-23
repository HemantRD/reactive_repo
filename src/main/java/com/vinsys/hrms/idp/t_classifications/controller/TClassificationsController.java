package com.vinsys.hrms.idp.t_classifications.controller;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.helper.ResponseGenerator;
import com.vinsys.hrms.idp.t_classifications.service.TClassificationsService;
import com.vinsys.hrms.idp.t_classifications.vo.TClassificationsStatus;
import com.vinsys.hrms.idp.t_classifications.vo.TClassificationsVo;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/catalog/classification")
public class TClassificationsController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TClassificationsService tClassificationsService;
    private final BackendProperties props;


    public TClassificationsController(final TClassificationsService tClassificationsService,
                                      final BackendProperties props) {
        this.tClassificationsService = tClassificationsService;
        this.props = props;
    }

    @PostMapping
    HRMSBaseResponse<String> addUpdateTrainingClassification(@RequestBody TClassificationsVo trainingCatalogVo) {
        HRMSBaseResponse<String> response;
        try {
            response = tClassificationsService.addUpdateTrainingClassification(trainingCatalogVo);
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
    HRMSBaseResponse<String> updateTrainingClassificationStatus(@RequestBody TClassificationsStatus tClassificationsStatus) {
        HRMSBaseResponse<String> response;
        try {
            response = tClassificationsService.updateTrainingClassificationStatus(tClassificationsStatus);
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
            response = tClassificationsService.getById(id);
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

    @GetMapping("list")
    HRMSBaseResponse<?> getAllTrainingClassifications(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
        HRMSBaseResponse<?> response;
        try {
            response = tClassificationsService.getAllTrainingClassifications(keyword, pageable);
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
    ResponseEntity<byte[]> getAllTrainingClassificationsExcel(
            @RequestParam(required = false) String keyword) {
        try {
            byte[] response = tClassificationsService.getAllTrainingClassificationsExcel(keyword);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String fileName = "TrainingClassificationsReport.xlsx";
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
