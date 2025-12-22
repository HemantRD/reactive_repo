package com.vinsys.hrms.util;

import org.springframework.stereotype.Component;

import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.reimbursement.vo.AddClaimRequestVO;

@Component
public class AddClaimAuthorityHelper {

	public void addClaimInputValidations(AddClaimRequestVO request) throws HRMSException {

		if (HRMSHelper.isLongZero(request.getClaimType().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Claim Type Id");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(request.getClaimType().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Claim Type Id");
		}
		if (HRMSHelper.isLongZero(request.getRequestId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Request Id");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(request.getRequestId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Request Id");
		}
		if (HRMSHelper.isLongZero(request.getClaimType().getClaimCategory())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Claim Category Id");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(request.getClaimType().getClaimCategory()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Claim Category Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getExpenseDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Expense Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getExpenseDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Expense Date");
		}

		if (!HRMSHelper.isNotFutureDate(request.getExpenseDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1614));
		}

		if (HRMSHelper.isFloatZero(request.getExpenseAmount())
				|| HRMSHelper.isNullOrEmpty(request.getExpenseAmount())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Expense Amount");
		}

		if (request.getExpenseAmount() < 1
				|| !HRMSHelper.regexMatcher(String.valueOf(request.getExpenseAmount()), "[0-9.]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Expense Amount");
		}

		
		if (HRMSHelper.isNullOrEmpty(request.getComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Requestor Comment ");
		}

		if (!HRMSHelper.regexMatcher(request.getComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Requestor Comment ");
		}
		
		
	}

}
