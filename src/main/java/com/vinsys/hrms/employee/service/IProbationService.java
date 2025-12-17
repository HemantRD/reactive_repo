package com.vinsys.hrms.employee.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.vo.ProbationFeedbackDetailsVO;
import com.vinsys.hrms.employee.vo.ProbationFeedbackVO;
import com.vinsys.hrms.exception.HRMSException;

public interface IProbationService {

	
	HRMSBaseResponse<?> submitProbationFeedback(ProbationFeedbackVO request) throws HRMSException;

	HRMSBaseResponse<List<ProbationFeedbackVO>> getProbationFeedbackList(Pageable pageable) throws HRMSException;
	
	HRMSBaseResponse<ProbationFeedbackDetailsVO> getProbationFeedbackListForEmployee(Long empid) throws HRMSException;

	HRMSBaseResponse<ProbationFeedbackVO> getProbationFeedback(Long employeeId, Long feedbackId) throws HRMSException;

}
