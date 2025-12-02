package com.vinsys.hrms.traveldesk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.traveldesk.service.IBPMRequsetService;
import com.vinsys.hrms.traveldesk.vo.BPMListResponseVO;
import com.vinsys.hrms.util.ResponseCode;

/**
 * @author Onkar A
 *
 * 
 */
@RestController
@RequestMapping("bpm")
public class BPMRequestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app_version}")
	private String applicationVersion;
	@Autowired
	IBPMRequsetService bpmRequsetService;

	@GetMapping("bpmdetails")
	HRMSBaseResponse<?> getBPMDetails(@RequestParam Long bpmNumber) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = bpmRequsetService.getBPMDetails(bpmNumber);
		} catch (HRMSException e) {
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
	
	
	@GetMapping("list")
	HRMSBaseResponse<BPMListResponseVO> getBPMList(@RequestParam Long bpmNumber) {
		HRMSBaseResponse<BPMListResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = bpmRequsetService.getBPMList(bpmNumber);
		} catch (HRMSException e) {
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

}
