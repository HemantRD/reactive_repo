package com.vinsys.hrms.employee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.directonboard.vo.AddressDetailsRequestVo;
import com.vinsys.hrms.employee.service.IPersonalDetailsService;
import com.vinsys.hrms.employee.vo.CertificationDetailsVO;
import com.vinsys.hrms.employee.vo.CertificationVO;
import com.vinsys.hrms.employee.vo.ContactNoVo;
import com.vinsys.hrms.employee.vo.EducationVO;
import com.vinsys.hrms.employee.vo.EducationalDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyVO;
import com.vinsys.hrms.employee.vo.HealthDetailsVO;
import com.vinsys.hrms.employee.vo.ProfileVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author Onkar A.
 */

@RestController
@RequestMapping("personaldetailsv2")
public class PersonalDetailsController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IPersonalDetailsService personalDetailsService;
	@Value("${app_version}")
	private String applicationVersion;

	@Operation(summary = "API for get Profile Details", description = "This API used to Display Profile deatils on dashboard ")
	@GetMapping("profile")
	HRMSBaseResponse<ProfileVO> getProfileDetails() {
		HRMSBaseResponse<ProfileVO> response = new HRMSBaseResponse<>();
		try {
			response = personalDetailsService.getProfileDetails();
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("address")
	HRMSBaseResponse<AddressDetailsRequestVo> getAddress(Long candidateId) {
		HRMSBaseResponse<AddressDetailsRequestVo> response = new HRMSBaseResponse();
		try {
			response = personalDetailsService.getAddress(candidateId);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("education")
	HRMSBaseResponse<EducationVO> getEducationalDetails(@RequestParam(required = false) Long candidateId) {
		HRMSBaseResponse<EducationVO> response = new HRMSBaseResponse<>();
		try {
			response = personalDetailsService.getEducationalDetails(candidateId);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setApplicationVersion(applicationVersion);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setApplicationVersion(applicationVersion);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("certification")
	HRMSBaseResponse<CertificationVO> getCertificationDetails(@RequestParam(required = false) Long candidateId) {
		HRMSBaseResponse<CertificationVO> response = new HRMSBaseResponse<>();
		try {
			response = personalDetailsService.getCertificationDetails(candidateId);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("healthreport")
	HRMSBaseResponse<HealthDetailsVO> getHealthDetails(@RequestParam(required = false) Long candidateId) {
		HRMSBaseResponse<HealthDetailsVO> response = new HRMSBaseResponse<>();
		try {
			response = personalDetailsService.getHealthDetails(candidateId);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("family")
	HRMSBaseResponse<FamilyVO> getFamilyDetails(@RequestParam(required = false) Long candidateId) {
		HRMSBaseResponse<FamilyVO> response = new HRMSBaseResponse<>();
		try {
			response = personalDetailsService.getFamilyDetails(candidateId);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "API for add Certification", description = "This API used for add Certification of Candidate")
	@PostMapping("addcertification")
	HRMSBaseResponse<?> addCertification(@RequestBody CertificationDetailsVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = personalDetailsService.addCertification(request);
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

	@Operation(summary = "you can add education details using this API", description = "please add your education details using this API")
	@PostMapping(value = "addqualification")
	HRMSBaseResponse<?> addEducationDetails(@RequestBody EducationalDetailsVO educationDetails) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = personalDetailsService.addEducationDetails(educationDetails);
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

	@Operation(summary = "you can add Family details using this API", description = "please add your Family details using this API")
	@PostMapping(value = "addfamily")
	HRMSBaseResponse<?> addEducationDetails(@RequestBody FamilyDetailsVO familyDetails) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = personalDetailsService.addfamilyDetails(familyDetails);
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
	
	/* @Author: Akanksha G.
	 * */
	
	
	@Operation(summary = "API to update employee mobile number", description = "API to update employee mobile number")
	@PostMapping("updatecontact")
	HRMSBaseResponse<?> updateContact(@RequestBody ContactNoVo request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = personalDetailsService.updateContact(request);
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
