/**
 * Onkar A.
 */
package com.vinsys.hrms.security.service;

import java.security.NoSuchAlgorithmException;

import javax.security.sasl.AuthenticationException;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.vo.ChangePasswordRequestVO;
import com.vinsys.hrms.security.vo.ForgetPasswordRequestVO;
import com.vinsys.hrms.util.LoginRequestVO;
import com.vinsys.hrms.util.LoginResponseVO;

public interface IAuthenticationService {

	HRMSBaseResponse<?> changePassword(ChangePasswordRequestVO request) throws HRMSException, NoSuchAlgorithmException;

	HRMSBaseResponse<LoginResponseVO> login(LoginRequestVO request) throws HRMSException, AuthenticationException, NoSuchAlgorithmException, Exception;

	HRMSBaseResponse<?> forgetPassword(ForgetPasswordRequestVO request) throws HRMSException, NoSuchAlgorithmException;

}
