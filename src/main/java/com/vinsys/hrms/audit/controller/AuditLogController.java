package com.vinsys.hrms.audit.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.audit.service.IAuditLogService;
import com.vinsys.hrms.audit.vo.AuditLogRequestVo;
import com.vinsys.hrms.audit.vo.AuditLogVO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.vo.KraListRequestVo;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/log")
public class AuditLogController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IAuditLogService auditLogService;
	@Value("${app_version}")
	private String applicationVersion;

	@GetMapping("auditlog")
	public HRMSBaseResponse<List<AuditLogVO>> getAuditLogList(Pageable pageable) {
        HRMSBaseResponse<List<AuditLogVO>> response = new HRMSBaseResponse<>();
        try {
            response = auditLogService.getAuditLogList(pageable);
            response.setApplicationVersion(applicationVersion);
        } catch (Exception e) {
        	
            response.setResponseCode(1500);
            response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
        }
        return response;
    }
	
	@Operation(summary = "get audit log List with filter and serach", description = "This api is use to get audit log list with filter")
	@PostMapping("auditlogreport")
	public HRMSBaseResponse<List<AuditLogVO>> getAuditLogReportList(@RequestBody AuditLogRequestVo request, Pageable pageable) {
		HRMSBaseResponse<List<AuditLogVO>> response = new HRMSBaseResponse<>();
		try {
			response = auditLogService.getAuditLogReportList(request,pageable);
			response.setApplicationVersion(applicationVersion);
		}catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}
	
	@Operation(summary = "Download Audit Log Report", description = "This API is used to download the Audit Log report in Excel format")
	@PostMapping("downloadauditreport")
	public ResponseEntity<byte[]> downloadAuditReports(@RequestBody AuditLogRequestVo request) throws HRMSException {
	    try {
	        byte[] response = auditLogService.downloadAuditLogReport(request);

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDisposition(ContentDisposition.builder("attachment")
	                .filename("Audit_Log_Report.xlsx")
	                .build());

	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(response);

	    } catch (HRMSException e) {
	        log.error("HRMSException: {}", e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(("Error: " + e.getResponseMessage()).getBytes());
	    } catch (Exception e) {
	        log.error("Exception: {}", e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
	    }
	}
}
