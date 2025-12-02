package com.vinsys.hrms.security.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.service.IAuthenticationService;
import com.vinsys.hrms.security.vo.ChangePasswordRequestVO;
import com.vinsys.hrms.security.vo.ForgetPasswordRequestVO;
import com.vinsys.hrms.util.LoginRequestVO;
import com.vinsys.hrms.util.LoginResponseVO;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author Onkar A.
 * 
 */
@RestController
@RequestMapping("authv2")
public class AuthController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IAuthenticationService authService;

	@Value("${token.expiry}")
	private long expiry;
	@Value("${app_version}")
	private String applicationVersion;

	@PostMapping("login")
	public HRMSBaseResponse<LoginResponseVO> loginCheck(@RequestBody LoginRequestVO request,
			HttpServletResponse httpResponse) throws HRMSException {
		HRMSBaseResponse<LoginResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = authService.login(request);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseMessage(e.getMessage());
			response.setResponseCode(1500);
		}
		return response;
	}

	@Operation(summary = "Change Password", description = "")
	@PostMapping("changepassword")
	HRMSBaseResponse<?> changePassword(@RequestBody ChangePasswordRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = authService.changePassword(request);
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

	@Operation(summary = "forget Password", description = "")
	@PostMapping("forgetpassword")
	HRMSBaseResponse<?> forgetPassword(@RequestBody ForgetPasswordRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = authService.forgetPassword(request);
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

}
