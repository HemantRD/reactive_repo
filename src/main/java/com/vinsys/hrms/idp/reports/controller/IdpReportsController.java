package com.vinsys.hrms.idp.reports.controller;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.helper.ResponseGenerator;
import com.vinsys.hrms.idp.reports.service.IIdpReportsService;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/idp")
public class IdpReportsController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final IIdpReportsService idpReportsService;
    private final BackendProperties props;

    public IdpReportsController(final IIdpReportsService idpReportsService,
                                final BackendProperties props) {
        this.idpReportsService = idpReportsService;
        this.props = props;
    }

    @PostMapping("getTopTrainingTopicsRequested")
    HRMSBaseResponse<?> getTopTrainingRequested(@RequestParam(required = false) String keyword,
                                                @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
        HRMSBaseResponse<?> response;
        try {
            response = idpReportsService.getTopTrainingRequested(keyword, pageable);
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

    @PostMapping("getTopTrainingTopicsRequested/{viewType}")
    ResponseEntity<byte[]> getTopTrainingTopicsRequestedScreenExcel(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable,
            @PathVariable("viewType") String viewType) {
        try {
            byte[] response = idpReportsService.getTopTrainingRequestedExcel(viewType, keyword, pageable);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment").filename("TopTrainingTopicsRequested.xlsx").build());

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

    @PostMapping("getTrainingCostReportMemberWise")
    HRMSBaseResponse<?> getCostMemberWise(@RequestParam(required = false) String keyword,
                                          @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable,
                                          @RequestParam String reportType) {
        HRMSBaseResponse<?> response;
        try {
            response = idpReportsService.getCostMemberWise(keyword, pageable, reportType);
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

    @PostMapping("getTrainingCostReportMemberWise/{viewType}")
    ResponseEntity<byte[]> getCostMemberWiseExcel(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable,
            @PathVariable("viewType") String viewType,
            @RequestParam String reportType) {
        try {
            byte[] response = idpReportsService.getCostMemberWiseExcel(viewType, keyword, pageable, reportType);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String fileName = "MemberWiseCost_Report_" + reportType.toUpperCase() + ".xlsx";
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

    @PostMapping("getTrainingCostReportTrainingTopicWise")
    HRMSBaseResponse<?> getCostTrainingWise(@RequestParam(required = false) String keyword,
                                            @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable,
                                            @RequestParam String reportType) {
        HRMSBaseResponse<?> response;
        try {
            response = idpReportsService.getCostTrainingWise(keyword, pageable, reportType);
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

    @PostMapping("getTrainingCostReportTrainingTopicWise/{viewType}")
    ResponseEntity<byte[]> getCostTrainingWiseExcel(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable,
            @PathVariable("viewType") String viewType,
            @RequestParam String reportType) {
        try {
            byte[] response = idpReportsService.getCostTrainingWiseExcel(viewType, keyword, pageable, reportType);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String fileName = "TrainingWiseCost_Report_" + reportType.toUpperCase() + ".xlsx";
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
