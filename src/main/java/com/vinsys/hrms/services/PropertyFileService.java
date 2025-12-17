package com.vinsys.hrms.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.HRMSPropertyFileLoader;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/propertyRefresh")

public class PropertyFileService {

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String refereshConfiguration() throws NoSuchMethodException, Exception {
		try {

			HRMSPropertyFileLoader.refreshConfig();
			return HRMSHelper.sendSuccessResponse(IHRMSConstants.successMessage, IHRMSConstants.successCode);

		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;

	}

}
