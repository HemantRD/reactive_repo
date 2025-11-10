package com.vinsys.hrms.employee.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.service.ISeprationService;
import com.vinsys.hrms.employee.vo.CandidateLetterVO;
import com.vinsys.hrms.employee.vo.CheckListByApproverVO;
import com.vinsys.hrms.employee.vo.ChecklistStatusVO;
import com.vinsys.hrms.employee.vo.ChecklistSubmitVo;
import com.vinsys.hrms.employee.vo.EmployeeSeparationRequestVO;
import com.vinsys.hrms.employee.vo.EmployeeSeprationDetailsResponseVO;
import com.vinsys.hrms.employee.vo.FeedbackQuestionVO;
import com.vinsys.hrms.employee.vo.SubmittedEmployeeFeedbackVO;
import com.vinsys.hrms.entity.EmployeeFeedback;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/separationv2")
public class SeprationController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app_version}")
	private String applicationVersion;

	@Autowired
	ISeprationService separationService;

	@Operation(summary = "API for apply separation", description = "This API used to apply resignation for employee.")
	@PostMapping("applyseparation")
	HRMSBaseResponse<?> applySeparation(@RequestBody EmployeeSeparationRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = separationService.applySeparation(request);
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

	@GetMapping("manager/resignedemployee")
	HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getResignedEmployeeList(Pageable pageable) {
		HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> response = new HRMSBaseResponse<>();
		try {
			response = separationService.getResignedEmployeeList(pageable);
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

	@GetMapping("resignedemployee")
	HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getResignedEmployeeDetails(Pageable pageable) {
		HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> response = new HRMSBaseResponse<>();
		try {
			response = separationService.getResignedEmployeeDetails(pageable);
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

	@Operation(summary = "API for cancel separation", description = "This API used to cancel resignation for employee.")
	@PostMapping("cancelseparation")
	HRMSBaseResponse<?> cancelSeparation(@RequestBody EmployeeSeparationRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = separationService.cancelSeparation(request);
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

	@Operation(summary = "API for RO APPROVED separation", description = "This API used for RO approved resignation for employee.")
	@PostMapping("manager/approve")
	HRMSBaseResponse<?> approveResignation(@RequestBody EmployeeSeparationRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = separationService.approveResignation(request);
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

	@Operation(summary = "API for RO Reject separation", description = "This API used for RO Reject resignation for employee.")
	@PostMapping("manager/reject")
	HRMSBaseResponse<?> rejectResignation(@RequestBody EmployeeSeparationRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = separationService.rejectResignation(request);
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

	/*
	 * @Operation(summary = "API for get checklist", description =
	 * "This API used for getting resigned employeee checklist.")
	 * 
	 * @GetMapping("checklistV2")
	 * HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>>
	 * getChecklist(Pageable pageable) {
	 * HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> response = new
	 * HRMSBaseResponse<>(); try { response =
	 * separationService.getResignedEmployeeCheckList(pageable); } catch
	 * (HRMSException e) { log.error(e.getMessage(), e);
	 * response.setResponseCode(e.getResponseCode());
	 * response.setResponseMessage(e.getResponseMessage());
	 * response.setApplicationVersion(applicationVersion); } catch (Exception e) {
	 * log.error(e.getMessage(), e); response.setResponseCode(1500);
	 * response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500)); }
	 * return response; }
	 */

	@Operation(summary = "API for get checklist", description = "This API used for getting resigned employeee checklist.")
	@GetMapping("checklistV2")
	HRMSBaseResponse<CheckListByApproverVO> getChecklist(@RequestParam(required = false) Long employeeId,
			@RequestParam(required = false) Long catalogueId) {
		HRMSBaseResponse<CheckListByApproverVO> response = new HRMSBaseResponse<>();
		try {
			response = separationService.getResignedEmployeeCheckList(employeeId, catalogueId);
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

	@RequestMapping(value = "checklistV2/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HRMSBaseResponse<List<ChecklistStatusVO>> getChecklistStatus(@RequestParam(required = true) Long employeeId,
			@RequestParam(required = true) Long seprationId) {
		HRMSBaseResponse<List<ChecklistStatusVO>> response = new HRMSBaseResponse<>();
		try {
			response = separationService.getChecklistStatus(employeeId,seprationId);
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

	@GetMapping("org/resignedemployee")
	HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getAllResignedEmployeeList(Pageable pageable) {
		HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> response = new HRMSBaseResponse<>();
		try {
			response = separationService.getAllResignedEmployeeList(pageable);
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

	@Operation(summary = "API for org level APPROVED separation", description = "This API used for org level approved resignation for employee.")
	@PostMapping("org/approve")
	HRMSBaseResponse<?> approveOrgLevelResignation(@RequestBody EmployeeSeparationRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = separationService.approveOrgLevelResignation(request);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setApplicationVersion(applicationVersion);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "API for submit separation checklist", description = "This API used to submit separation checklist.")
	@PostMapping("checklistV2")
	HRMSBaseResponse<?> submitChecklistV2(@RequestBody List<ChecklistSubmitVo> request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = separationService.submitChecklistV2(request);
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

	@Operation(summary = "API for org level Reject separation", description = "This API used for org level reject resignation for employee.")
	@PostMapping("org/reject")
	HRMSBaseResponse<?> rejectOrgLevelResignation(@RequestBody EmployeeSeparationRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = separationService.rejectOrgLevelResignation(request);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setApplicationVersion(applicationVersion);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	
	
	@GetMapping("employee/getseparationdocumentlist")
	HRMSBaseResponse<List<CandidateLetterVO>> getSeparationDocumentListForEmployee() {
		HRMSBaseResponse<List<CandidateLetterVO>> response = new HRMSBaseResponse<>();
		try {
			response = separationService.getSeparationDocumentList();
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
	
	@Operation(summary = "API for get feedback", description = "This API used for getting resigned employeee feedback.")
	@GetMapping("feedbackquestionV2")
	HRMSBaseResponse<List<FeedbackQuestionVO>> getFeedBackQuestions() {
		HRMSBaseResponse<List<FeedbackQuestionVO>> response = new HRMSBaseResponse<>();
		try {
			response = separationService.getFeedBackQuestions();
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
	
	@Operation(summary = "API for org level employee separation feedback", description = "This API used for org level employee separation feedback.")
	@PostMapping("employeefeedbackV2")
	HRMSBaseResponse<?> createEmployeeFeedback(@RequestBody SubmittedEmployeeFeedbackVO empFb) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = separationService.createEmployeeFeedback(empFb);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setApplicationVersion(applicationVersion);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}
	
	@GetMapping("approver/resignedemployeelist")
	HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getResignedEmployeeListForApprovers(@RequestParam(required = true)String catalougeName,Pageable pageable) {
		HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> response = new HRMSBaseResponse<>();
		try {
			response = separationService.getResignedEmployeeListForApprovers(catalougeName,pageable);
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
	
	@Operation(summary = "API for get Employee feedback", description = "This API used for getting resigned employeee feedback.")
	@GetMapping("getemployeefeedback")
	HRMSBaseResponse<List<EmployeeFeedback>> getEmployeeFeedBack(@RequestParam(required = false)Long employeeId) {
		HRMSBaseResponse<List<EmployeeFeedback>> response = new HRMSBaseResponse<>();
		try {
			response = separationService.getEmployeeFeedback(employeeId);
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
