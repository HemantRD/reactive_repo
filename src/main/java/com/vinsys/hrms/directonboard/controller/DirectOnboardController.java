package com.vinsys.hrms.directonboard.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.directonboard.service.IDirectOnBoardService;
import com.vinsys.hrms.directonboard.vo.AddressDetailsRequestVo;
import com.vinsys.hrms.directonboard.vo.CurrentEmploymentRequestVO;
import com.vinsys.hrms.directonboard.vo.DeleteFileRequestVO;
import com.vinsys.hrms.directonboard.vo.EmailCheckVo;
import com.vinsys.hrms.directonboard.vo.FileUploadRequestVO;
import com.vinsys.hrms.directonboard.vo.PreviousEmploymentRequestVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailListVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailsRequestVO;
import com.vinsys.hrms.directonboard.vo.ValidationVO;
import com.vinsys.hrms.employee.vo.BankDetailsVO;
import com.vinsys.hrms.employee.vo.CertificationDetailsVO;
import com.vinsys.hrms.employee.vo.EducationalDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyDetailsVO;
import com.vinsys.hrms.employee.vo.HealthDetailsVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.vo.EmpIdVo;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/directonboard")
public class DirectOnboardController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app_version}")
	private String applicationVersion;

	@Autowired
	IDirectOnBoardService onboardService;

	@Operation(summary = "API for add profile details", description = "This API used to add profile details of candidate.")
	@PostMapping("profiledetails")
	HRMSBaseResponse<?> addProfileDetails(@RequestBody ProfileDetailsRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = onboardService.addProfileDetails(request);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "API for add previous employment details", description = "This API used to add previous employment details of candidate.")
	@PostMapping("previousemployment")
	HRMSBaseResponse<?> addPreviousEmployment(@RequestBody PreviousEmploymentRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = onboardService.addPreviousEmployment(request);
			response.setApplicationVersion(applicationVersion);
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

	@GetMapping("profilelist")
	@Operation(summary = "API for get profile list", description = "This API used to get profile list of candidate.")
	HRMSBaseResponse<List<ProfileDetailListVO>> getAllEmployee(
			@RequestParam(required = false) String keyword,
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
		HRMSBaseResponse<List<ProfileDetailListVO>> response = new HRMSBaseResponse<>();
		try {
			response = onboardService.getAllEmployeeList(keyword,pageable);
			response.setApplicationVersion(applicationVersion);
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

	@GetMapping("profile")
	@Operation(summary = "API for get profile details", description = "This API used to get profile details of candidate by candidate id.")
	HRMSBaseResponse<ProfileDetailVO> getProfileDetail(@RequestParam(required = true) Long candidateId) {
		HRMSBaseResponse<ProfileDetailVO> response = new HRMSBaseResponse<>();
		try {
			response = onboardService.getProfileDetail(candidateId);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "API for add bank details", description = "This API used to add bank details of candidate.")
	@PostMapping("bankdetail")
	HRMSBaseResponse<?> addBankDetails(@RequestBody BankDetailsVO bankDetail) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = onboardService.saveBankDetail(bankDetail);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "API for add family details", description = "This API used to add family details of candidate.")
	@PostMapping("familydetail")
	HRMSBaseResponse<?> addFamilyDetails(@RequestBody FamilyDetailsVO familyDetail) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = onboardService.saveFamilyDetail(familyDetail);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "API for add current employment details", description = "This API used to add current employment details of candidate.")
	@PostMapping("currentemployment")
	HRMSBaseResponse<?> addCurrentEmployment(@RequestBody CurrentEmploymentRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = onboardService.addCurrentEmployment(request);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "API for add Education Details", description = "This API used to add education details of candidate.")
	@PostMapping(value = "educationdetails")
	HRMSBaseResponse<?> addEducationDetails(@RequestBody EducationalDetailsVO educationDetails) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = onboardService.addEducationDetails(educationDetails);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "API for add Certification Details", description = "This API used to add certification details of candidate.")
	@PostMapping(value = "certificationdetails")
	HRMSBaseResponse<?> addCertificationDetails(
			@RequestBody(required = false) CertificationDetailsVO certificationDetails) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = onboardService.addCertificationDetails(certificationDetails);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);

		}
		return response;
	}

	@Operation(summary = "API for add health details", description = "This API used to add health details of candidate.")
	@PostMapping("healthdetail")
	HRMSBaseResponse<?> saveHealthDetail(@RequestBody HealthDetailsVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = onboardService.saveHealthDetail(request);
			response.setApplicationVersion(applicationVersion);
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


	@Operation(summary = "API for delete profile details", description = "This API used to delete profile details of candidate.")
	@PostMapping("deleteprofile")
	HRMSBaseResponse<?> deleteProfileDetail(@RequestParam(required = true) Long candidateId) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = onboardService.deleteProfile(candidateId);
			response.setApplicationVersion(applicationVersion);
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

	@GetMapping("validation")
	@Operation(summary = "API for rquired flag", description = "This API will return required flag by candidate Id.This flag will use for UI to display/hide button or tab.")
	HRMSBaseResponse<ValidationVO> getDirectOnboardingValidationFlags(@RequestParam(required = true) Long candidateId) {
		HRMSBaseResponse<ValidationVO> response = new HRMSBaseResponse<>();
		try {
			response = onboardService.getDirectOnboardingValidationFlags(candidateId);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "API for add Address Details", description = "This API used to add Address details of candidate.")
	@PostMapping(value = "addressdetails")
	HRMSBaseResponse<?> addAddressDetails(@RequestBody AddressDetailsRequestVo addressVo) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = onboardService.addAddressDetails(addressVo);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);

		}
		return response;
	}
	
	
	@Operation(summary = "API for upload file", description = "This API use to upload file.")
	@PostMapping(value = "uploadfile")
	HRMSBaseResponse<?> uploadFile(@RequestParam("file") MultipartFile[] requestFile,@RequestPart("request") FileUploadRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = onboardService.uploadFile(requestFile,request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);

		}
		return response;
	}
	
	@Operation(summary = "This API is use to delete file", description = " for deleting file or any document")
	@PostMapping(value = "deletefile")
	HRMSBaseResponse<?> deleteFile(@RequestBody DeleteFileRequestVO deleteFileVO) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = onboardService.deleteFile(deleteFileVO);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);

		}
		return response;
	}
	
	
	

///Excel upload
	
	

	@Operation(summary = "API for upload excel file", description = "This API use to upload excel file.")
	@PostMapping(value = "uploadexcel")
	HRMSBaseResponse<?> uploadExcelFile(@RequestParam("file") MultipartFile requestFile) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = onboardService.uploadExcelFile(requestFile);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);

		}
		return response;
	}
	
	@Operation(summary = "API to check candidate email", description = "This API use to check whether candidate email id alredy exist.")
	@PostMapping(value = "candidateemail")
	HRMSBaseResponse<?> candidateEmailIdCheck(@RequestBody EmailCheckVo request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = onboardService.candidateEmailIdCheck(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);

		}
		return response;
	}
	
	
	@Operation(summary = "API to check employee email", description = "This API use to check whether employee email id alredy exist.")
	@PostMapping(value = "employeeemail")
	HRMSBaseResponse<?> employeeEmailIdCheck(@RequestBody EmailCheckVo request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = onboardService.employeeEmailIdCheck(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);

		}
		return response;
	}
	
	@Operation(summary = "API to check employee code ", description = "This API use to check whether employee code alredy exist.")
	@PostMapping(value = "empcode")
	HRMSBaseResponse<?> employeeCodeCheck(@RequestBody EmailCheckVo request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = onboardService.employeeCodeCheck(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);

		}
		return response;
	}
	
	/* @author: Akanksha Gaikwad
	 * API to delete employee data from whole system ***/
	
	@Operation(summary = "API to delete multiple employee data ", description = "This API to delete multiple employee data")
	@PostMapping(value = "deleteemployee")
	HRMSBaseResponse<?> deleteEmployee(@RequestBody EmpIdVo request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = onboardService.deleteEmployee(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);

		}
		return response;
	}
	
	
}
