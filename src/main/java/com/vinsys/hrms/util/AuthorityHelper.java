package com.vinsys.hrms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.vo.ChangePasswordRequestVO;
import com.vinsys.hrms.security.vo.ForgetPasswordRequestVO;

/**
 * @author Onkar A
 *
 * 
 */
@Service
public class AuthorityHelper {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	public void validateChangePasswordInput(ChangePasswordRequestVO request) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request.getNewPassword()) || HRMSHelper.isNullOrEmpty(request.getOldPassword())
				|| HRMSHelper.isNullOrEmpty(request.getUsername())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Username or password");
		}
		if (request.getOldPassword().trim().equals(request.getNewPassword().trim())) {
		    throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Old Password and New Password cannot be the same ");
		}
		boolean newPassword = HRMSHelper.regexMatcher(request.getNewPassword(), "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$");
		if (!HRMSHelper.isNullOrEmpty(request.getNewPassword())) {
			if (!newPassword) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " New password contains A lowercase letter, a capital (uppercase) letter, a number,  minimum of 8 characters, maximum of 16 characters, special character. ");
			}
		}

	}

	public void validateForgetPasswordInput(ForgetPasswordRequestVO request) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getEmployeeId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}
		if (HRMSHelper.isNullOrEmpty(request.getOfficialEmailId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}
		if (!HRMSHelper.validateEmail(request.getOfficialEmailId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}
		if (HRMSHelper.isNullOrEmpty(request.getDob())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

		if (!HRMSHelper.validateDateFormate(request.getDob())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}
	
	

}
