package com.vinsys.hrms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.employee.vo.ApplyGrantLeaveRequestVO;
import com.vinsys.hrms.employee.vo.ApplyLeaveRequestVO;
import com.vinsys.hrms.employee.vo.CertificationDetailsVO;
import com.vinsys.hrms.employee.vo.EducationalDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyDetailsVO;
import com.vinsys.hrms.employee.vo.LeaveCalculationRequestVO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.vo.MasterLeavePolicyVO;

@Service
public class EmployeeAuthorityHelper {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void applyLeaveInputValidation(Employee employee, ApplyLeaveRequestVO request) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getLeaveTypeId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Levae Type");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(request.getLeaveApplied().getLeaveTypeId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave Type");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.validateSession(request.getLeaveApplied().getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.validateSession(request.getLeaveApplied().getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getNoOfDays())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
		}

		if (HRMSHelper.validateFloatNO(String.valueOf(request.getLeaveApplied().getNoOfDays()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForApply())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Apply ");
		}

		if (!HRMSHelper.validateLeaveReason(request.getLeaveApplied().getReasonForApply())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Apply ");
		}

	}

	public void cancleLeaveInputValidation(ApplyLeaveRequestVO request) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getLeaveTypeId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Levae Type");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getNoOfDays())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForApply())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Cancle ");
		}

//		boolean ReasonForApplyLeave = HRMSHelper.regexMatcher(request.getLeaveApplied().getReasonForApply(),
//				"[a-zA-Z0-9.-;, ]{1,250}$");
//		if (!HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForApply())) {
//			if (!ReasonForApplyLeave) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For cancle ");
//			}
//		}

		if (!HRMSHelper.validateLeaveReason(request.getLeaveApplied().getReasonForApply())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Cancle ");
		}

	}

	public void withdrawLeaveInputValidation(ApplyLeaveRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getLeaveApplied().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getLeaveTypeId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//			}
//
//		if (HRMSHelper.isLongZero(request.getLeaveApplied().getLeaveTypeId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
//		}
//
//		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getFromDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
//		}
//
//		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getToDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
//		}
//		
//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getNoOfDays()), "^\\d*\\.?\\d+$")) {
//			         throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days ");
//			}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getNoOfDays())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
//		}

//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForApply())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Withdrwal ");
//		}
//
//     	if (!HRMSHelper.regexMatcher(request.getLeaveApplied().getReasonForApply(),"[a-zA-Z0-9.-;, ]{1,250}$")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Withdrwal ");
//			}
		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForWithdrawn())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Withdrwal ");
		}

//		if (!HRMSHelper.regexMatcher(request.getLeaveApplied().getReasonForWithdrawn(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Withdrwal ");
//		}

		if (!HRMSHelper.validateLeaveReason(request.getLeaveApplied().getReasonForWithdrawn())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Withdrwal ");
		}

	}

	public void approvedLeaveInputValidation(ApplyLeaveRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getLeaveApplied().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getLeaveTypeId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//			}
//
//		if (HRMSHelper.isLongZero(request.getLeaveApplied().getLeaveTypeId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
//		}
//
//		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getFromDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
//		}
//
//		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getToDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getNoOfDays())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
//		}
//		
//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getNoOfDays()), "^\\d*\\.?\\d+$")) {
//	         throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days ");
//	}
//		
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForApply())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Approved ");
//		}
//
//     	if (!HRMSHelper.regexMatcher(request.getLeaveApplied().getReasonForApply(),"[a-zA-Z0-9.-;, ]{1,250}$")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Approved ");
//			}

	}

	public void rejectLeaveInputValidation(ApplyLeaveRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getLeaveApplied().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getLeaveTypeId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//			}
//
//		if (HRMSHelper.isLongZero(request.getLeaveApplied().getLeaveTypeId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
//		}
//
//		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getFromDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
//		}
//
//		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getToDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getNoOfDays())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
//		}
//		
//		
		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForReject())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Reject ");
		}

//		if (!HRMSHelper.regexMatcher(request.getLeaveApplied().getReasonForReject(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Reject ");
//		}

		if (!HRMSHelper.validateLeaveReason(request.getLeaveApplied().getReasonForReject())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Reject ");
		}

	}

	public void withdrawApprovedLeaveInputValidation(ApplyLeaveRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getLeaveApplied().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getLeaveTypeId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//			}
//
//		if (HRMSHelper.isLongZero(request.getLeaveApplied().getLeaveTypeId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
//		}
//
//		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getFromDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
//		}
//
//		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getToDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
//		}
//
//	
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getNoOfDays())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
//		}
//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForApply())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For withdraw Approved Leave ");
//		}
//
//     	if (!HRMSHelper.regexMatcher(request.getLeaveApplied().getReasonForApply(),"[a-zA-Z0-9.-;, ]{1,250}$")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For withdraw Approved Leave ");
//			}

	}

	public void WithdrawRejectLeaveInputValidation(ApplyLeaveRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getLeaveApplied().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveApplied().getLeaveTypeId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
		}

		if (HRMSHelper.isLongZero(request.getLeaveApplied().getLeaveTypeId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getLeaveApplied().getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getNoOfDays())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
		}

//		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForApply())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For withdraw Reject Leave ");
//		}
//
//     	if (!HRMSHelper.regexMatcher(request.getLeaveApplied().getReasonForApply(),"[a-zA-Z0-9.-;, ]{1,250}$")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For withdraw Reject Leave ");
//			}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getApproverCommentOnWithdrawn())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " To Reason For withdraw Reject Leave ");
		}

//		if (!HRMSHelper.regexMatcher(request.getLeaveApplied().getApproverCommentOnWithdrawn(),
//				"[a-zA-Z0-9.-;, ]{1,250}$")) {
//			throw new HRMSException(1501,
//					ResponseCode.getResponseCodeMap().get(1501) + " To Reason For withdraw Reject Leave ");
//		}

		if (!HRMSHelper.validateLeaveReason(request.getLeaveApplied().getApproverCommentOnWithdrawn())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " To Reason For withdraw Reject Leave ");
		}

	}

	public void applyGrantLeaveInputValidation(ApplyGrantLeaveRequestVO request) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveTypeId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Levae Type");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(request.getLeaveTypeId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave Type");
		}

		if (HRMSHelper.isNullOrEmpty(request.getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.validateSession(request.getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.validateSession(request.getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getNoOfDays())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
		}

		if (HRMSHelper.validateFloatNO(String.valueOf(request.getNoOfDays()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
		}

		if (HRMSHelper.isNullOrEmpty(request.getReasonForApply())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Apply ");
		}

		if (!HRMSHelper.validateLeaveReason(request.getReasonForApply())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Apply ");
		}

	}

	public void calculateGrantLeaveInputValidation(LeaveCalculationRequestVO request) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " - FromDate");
		}

		if (!HRMSHelper.validateDateFormate(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " - ToDate");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLeaveTypeId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Levae Type");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(request.getLeaveTypeId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave Type");
		}

		if (HRMSHelper.isNullOrEmpty(request.getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.validateSession(request.getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.validateSession(request.getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

	}

	public void calculateLeaveInputValidation(LeaveCalculationRequestVO requestVo) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(requestVo.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (HRMSHelper.isNullOrEmpty(requestVo.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (!HRMSHelper.validateDateFormate(requestVo.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " - FromDate");
		}

		if (!HRMSHelper.validateDateFormate(requestVo.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " - ToDate");
		}

		if (HRMSHelper.isNullOrEmpty(requestVo.getLeaveTypeId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Levae Type");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(requestVo.getLeaveTypeId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave Type");
		}

		if (HRMSHelper.isNullOrEmpty(requestVo.getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.validateSession(requestVo.getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.isNullOrEmpty(requestVo.getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.validateSession(requestVo.getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

	}

	/**
	 * 
	 * @param employee
	 * @param request
	 * 
	 *                    This code will check applied leave request is valid or not
	 *                    by policy
	 * @param leavePolicy
	 * @throws HRMSException
	 */
	public void validateLeavePolicy(Employee employee, ApplyLeaveRequestVO request, MasterLeavePolicyVO leavePolicy)
			throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(leavePolicy)) {
			if (leavePolicy.getCcRequired().equals(ERecordStatus.Y.name())
					&& HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getCc())) {
				throw new HRMSException(1508, ResponseCode.getResponseCodeMap().get(1508));
			}
		}
	}

	public void grantLeaveApprovedInputValidation(ApplyGrantLeaveRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveTypeId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//			}
//
//		if (HRMSHelper.isLongZero(request.getLeaveTypeId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//		}

//		if (HRMSHelper.isNullOrEmpty(request.getFromDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
//		}
//
//		if (!HRMSHelper.validateDateFormate(request.getFromDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getToDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
//		}
//
//		if (!HRMSHelper.validateDateFormate(request.getToDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getFromSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
//		}
//
//		if (HRMSHelper.validateSession(request.getFromSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getToSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
//		}
//
//		if (HRMSHelper.validateSession(request.getToSession())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
//		}
//
//		if (HRMSHelper.isNullOrEmpty(request.getNoOfDays())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
//		}
//
//		if (HRMSHelper.validateFloatNO(String.valueOf(request.getNoOfDays()))) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
//		}

//		if (HRMSHelper.isNullOrEmpty(request.getReasonForApply())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Grant Leave Approved ");
//		}
//
//		boolean ReasonForApplyLeave = HRMSHelper.regexMatcher(request.getReasonForApply(), "[a-zA-Z0-9.-;, ]{1,250}$");
//		if (!HRMSHelper.isNullOrEmpty(request.getReasonForApply())) {
//			if (!ReasonForApplyLeave) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Grant Leave Approved ");
//			}
//		}

	}

	public void cancleGrantLeaveInputValidation(ApplyGrantLeaveRequestVO request) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.validateSession(request.getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.validateSession(request.getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getNoOfDays())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
		}

		if (HRMSHelper.validateFloatNO(String.valueOf(request.getNoOfDays()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
		}

		if (HRMSHelper.isNullOrEmpty(request.getReasonForApply())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Cancle ");
		}

//		boolean ReasonForApplyLeave = HRMSHelper.regexMatcher(request.getReasonForApply(), "[a-zA-Z0-9.-;, ]{1,250}$");
//		if (!HRMSHelper.isNullOrEmpty(request.getReasonForApply())) {
//			if (!ReasonForApplyLeave) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Cancle ");
//			}
//		}

		if (!HRMSHelper.validateLeaveReason(request.getReasonForApply())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Cancle ");
		}
	}

	public void validateEducationDetails(EducationalDetailsVO educationDetails) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(educationDetails.getDegree())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Degree");
		}

		if (HRMSHelper.isNullOrEmpty(educationDetails.getModeOfEducation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mode of education");
		}

		if (HRMSHelper.isNullOrEmpty(educationDetails.getInstituteName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Institute Name");
		}

		if (HRMSHelper.isNullOrEmpty(educationDetails.getBoardUniversity())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Board/University name");
		}

		if (HRMSHelper.isNullOrEmpty(educationDetails.getStateLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Location");
		}

		if (HRMSHelper.isNullOrEmpty(educationDetails.getGradeDivisionPercentage())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade/Division/Percentage");
		}

		boolean degree = HRMSHelper.regexMatcher(educationDetails.getDegree(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(educationDetails.getDegree())) {
			if (!degree) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Degree ");
			}
		}

		boolean specialization = HRMSHelper.regexMatcher(educationDetails.getSubjectOfSpecialization(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(educationDetails.getSubjectOfSpecialization())) {
			if (!specialization) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Subject Of Specialization ");
			}
		}

		boolean instituteName = HRMSHelper.regexMatcher(educationDetails.getInstituteName(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(educationDetails.getInstituteName())) {
			if (!instituteName) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Institute Name ");
			}
		}

		boolean boardUniversity = HRMSHelper.regexMatcher(educationDetails.getBoardUniversity(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(educationDetails.getBoardUniversity())) {
			if (!boardUniversity) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Board/University ");
			}
		}

		boolean stateLocation = HRMSHelper.regexMatcher(educationDetails.getStateLocation(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(educationDetails.getStateLocation())) {
			if (!stateLocation) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Location ");
			}
		}

		boolean gradeDivisionPercentage = HRMSHelper.regexMatcher(educationDetails.getGradeDivisionPercentage(),
				"^(?!\\s)[a-zA-Z0-9.,;\\s+%+-]{1,48}[a-zA-Z0-9.,;\\s+%+-](?!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(educationDetails.getGradeDivisionPercentage())) {
			if (!gradeDivisionPercentage) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " grade Division Percentage ");
			}
		}

		boolean academicAchievements = HRMSHelper.regexMatcher(educationDetails.getAcademicAchievements(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(educationDetails.getAcademicAchievements())) {
			if (!academicAchievements) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "  Academic Achievements");
			}
		}

		boolean modeOfEducation = HRMSHelper.regexMatcher(educationDetails.getModeOfEducation(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(educationDetails.getModeOfEducation())) {
			if (!modeOfEducation) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "  Mode of Education");
			}
		}
		if (!HRMSHelper.validateDateFormate(educationDetails.getPassingYearMonth())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Passing Year Month");
		}

	}

	public void validateCertificationDetails(CertificationDetailsVO certificationDetail) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(certificationDetail.getCertificationName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Certification Name");
		}

		if (HRMSHelper.isNullOrEmpty(certificationDetail.getCertificationType())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Certification Type");
		}

		if (HRMSHelper.isNullOrEmpty(certificationDetail.getNameOfInstitute())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Name of Institute");
		}

		if (HRMSHelper.isNullOrEmpty(certificationDetail.getModeOfEducation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mode of Education ");
		}

		if (HRMSHelper.isNullOrEmpty(certificationDetail.getPercentageGrade())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade/Percentage ");
		}

		boolean certificationName = HRMSHelper.regexMatcher(certificationDetail.getCertificationName(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(certificationDetail.getCertificationName())) {
			if (!certificationName) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Certification Name");
			}
		}

		boolean certificationType = HRMSHelper.regexMatcher(certificationDetail.getCertificationType(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(certificationDetail.getCertificationType())) {
			if (!certificationType) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Certification Type");
			}
		}

		boolean nameOfInstitute = HRMSHelper.regexMatcher(certificationDetail.getNameOfInstitute(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(certificationDetail.getNameOfInstitute())) {
			if (!nameOfInstitute) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Name of Institute");
			}
		}

		boolean modeOfEducation = HRMSHelper.regexMatcher(certificationDetail.getModeOfEducation(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(certificationDetail.getModeOfEducation())) {
			if (!modeOfEducation) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mode of Education");
			}
		}

		boolean gradePercentage = HRMSHelper.regexMatcher(certificationDetail.getPercentageGrade(),
				"^(?!\\s)[a-zA-Z0-9.,;\\s+%+-]{1,48}[a-zA-Z0-9.,;\\s+%+-](?!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(certificationDetail.getPercentageGrade())) {
			if (!gradePercentage) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade/Percentage");
			}
		}
		if (!HRMSHelper.validateDateFormate(certificationDetail.getCertificationDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Certification Date");
		}
		if (!HRMSHelper.validateDateFormate(certificationDetail.getCertificationValidityDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Certification Validity Date");
		}

	}

	public void withdrawGrantLeaveInputValidation(ApplyGrantLeaveRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getLeaveTypeId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//			}
//
//		if (HRMSHelper.isLongZero(request.getLeaveTypeId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Leave type Id ");
//		}

		if (HRMSHelper.isNullOrEmpty(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.validateSession(request.getFromSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.validateSession(request.getToSession())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}

		if (HRMSHelper.isNullOrEmpty(request.getNoOfDays())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days");
		}

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getNoOfDays()), "^\\d*\\.?\\d+$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " No Of Days ");
		}

//		if (HRMSHelper.isNullOrEmpty(request.getReasonForApply())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Grant Leave Approved ");
//		}
//
//		boolean ReasonForApplyLeave = HRMSHelper.regexMatcher(request.getReasonForApply(), "[a-zA-Z0-9.-;, ]{1,250}$");
//		if (!HRMSHelper.isNullOrEmpty(request.getReasonForApply())) {
//			if (!ReasonForApplyLeave) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Grant Leave Approved ");
//			}
//		}

	}

	public void rejectGrantLeaveInputValidation(ApplyGrantLeaveRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getReasonForCancel())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Grant Leave Reject ");
		}

//		boolean ReasonForApplyLeave = HRMSHelper.regexMatcher(request.getReasonForCancel(), "[a-zA-Z0-9.-;, ]{1,250}$");
//		if (!ReasonForApplyLeave) {
//			throw new HRMSException(1501,
//					ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Grant Leave Reject ");
//		}

		if (!HRMSHelper.validateLeaveReason(request.getReasonForCancel())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Grant Leave Reject ");
		}
	}

	public void validateFamilyDetails(FamilyDetailsVO familyDetails) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(familyDetails.getFirstName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " First Name ");
		}

		if (HRMSHelper.isNullOrEmpty(familyDetails.getLastName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Last Name ");
		}

		if (HRMSHelper.isNullOrEmpty(familyDetails.getRelationship())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Relationship ");
		}
		if (HRMSHelper.isNullOrEmpty(familyDetails.getGender())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Gender ");
		}

		if (HRMSHelper.isNullOrEmpty(familyDetails.getDependent())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Dependent ");
		}
		if (HRMSHelper.isNullOrEmpty(familyDetails.getContactNo1())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Contact Number ");
		}
//		if(!HRMSHelper.validateDateFormate(familyDetails.getDateOfBirth())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Certification Date");
//		}

		boolean firstName = HRMSHelper.regexMatcher(familyDetails.getFirstName(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(familyDetails.getFirstName())) {
			if (!firstName) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " First Name ");
			}
		}

	}

	public void downloadTeamLeavesReportInputValidation(String fromDate, String toDate) throws HRMSException, ParseException {
		if (HRMSHelper.isNullOrEmpty(fromDate)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From date.");
		} else {
			if (!HRMSHelper.validateDateFormate(fromDate)) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From date.");
			}
		}
		if (HRMSHelper.isNullOrEmpty(toDate)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To date.");
		} else {
			if (!HRMSHelper.validateDateFormate(toDate)) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To date.");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(fromDate) && !HRMSHelper.isNullOrEmpty(toDate)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);

			Date fromDate1 = dateFormat.parse(fromDate);
			Date toDate1 = dateFormat.parse(toDate);

			if (fromDate1.after(toDate1)) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1502));
			}
		}
	}
}
