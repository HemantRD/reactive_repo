package com.vinsys.hrms.employee.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
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

public interface ISeprationService {

	HRMSBaseResponse<?> applySeparation(EmployeeSeparationRequestVO voEmployeeSeparationDetails)
			throws HRMSException, ParseException;

	HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getResignedEmployeeList(Pageable pageable)
			throws HRMSException;

	HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getResignedEmployeeDetails(Pageable pageable)
			throws HRMSException;

	HRMSBaseResponse<?> approveResignation(EmployeeSeparationRequestVO voEmployeeSeparationDetails)
			throws HRMSException;

	HRMSBaseResponse<?> cancelSeparation(EmployeeSeparationRequestVO voEmployeeSeparationDetails)
			throws HRMSException, ParseException;

	HRMSBaseResponse<?> rejectResignation(EmployeeSeparationRequestVO voEmployeeSeparationDetails) throws HRMSException;

	HRMSBaseResponse<CheckListByApproverVO> getResignedEmployeeCheckList(Long employeeId, Long catalogueId)
			throws HRMSException;

	HRMSBaseResponse<List<ChecklistStatusVO>> getChecklistStatus(Long employeeId, Long seprationId) throws HRMSException;

	HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getAllResignedEmployeeList(Pageable pageable)
			throws HRMSException;
	
	HRMSBaseResponse<?> approveOrgLevelResignation(EmployeeSeparationRequestVO voEmployeeSeparationDetails)
			throws HRMSException, ParseException;


	HRMSBaseResponse<?> submitChecklistV2(List<ChecklistSubmitVo> request) throws HRMSException;
	
	HRMSBaseResponse<?> rejectOrgLevelResignation(EmployeeSeparationRequestVO voEmployeeSeparationDetails)
			throws HRMSException, ParseException;
	
	HRMSBaseResponse<List<CandidateLetterVO>> getSeparationDocumentList()throws HRMSException;
	
	HRMSBaseResponse<List<FeedbackQuestionVO>> getFeedBackQuestions() throws HRMSException;


	HRMSBaseResponse<?> createEmployeeFeedback(SubmittedEmployeeFeedbackVO empFb) throws HRMSException;

	

	HRMSBaseResponse<List<EmployeeFeedback>> getEmployeeFeedback(Long employeeId) throws HRMSException;

	/***********
	 * API to get resigned employee list for approvers
	 ********************/
	HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getResignedEmployeeListForApprovers(String catalougeName,
			Pageable pageable) throws HRMSException;;
	

}
