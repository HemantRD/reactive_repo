package com.vinsys.hrms.idp.t_types.controller;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.helper.ResponseGenerator;
import com.vinsys.hrms.idp.t_types.service.TTypesService;
import com.vinsys.hrms.idp.t_types.vo.TTypesStatus;
import com.vinsys.hrms.idp.t_types.vo.TTypesVo;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/catalog/types")
public class TTypesController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TTypesService tTypesService;
    private final BackendProperties props;


    public TTypesController(final TTypesService tTypesService,
                            final BackendProperties props) {
        this.tTypesService = tTypesService;
        this.props = props;
    }

    @PostMapping
    HRMSBaseResponse<String> addUpdateTrainingType(@RequestBody TTypesVo trainingCatalogVo) {
        HRMSBaseResponse<String> response;
        try {
            response = tTypesService.addUpdateTrainingType(trainingCatalogVo);
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
    HRMSBaseResponse<String> updateTrainingTypeStatus(@RequestBody TTypesStatus tClassificationsStatus) {
        HRMSBaseResponse<String> response;
        try {
            response = tTypesService.updateTrainingTypeStatus(tClassificationsStatus);
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
            response = tTypesService.getById(id);
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
    HRMSBaseResponse<?> getAllTrainingTypes(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
        HRMSBaseResponse<?> response;
        try {
            response = tTypesService.getAllTrainingTypes(keyword, pageable);
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
    ResponseEntity<byte[]> getAllTrainingTypesExcel(
            @RequestParam(required = false) String keyword) {
        try {
            byte[] response = tTypesService.getAllTrainingTypesExcel(keyword);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String fileName = "TrainingTypesReport.xlsx";
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
