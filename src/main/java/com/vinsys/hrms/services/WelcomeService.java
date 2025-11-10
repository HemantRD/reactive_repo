package com.vinsys.hrms.services;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.entity.CandidateFamilyDetail;
import com.vinsys.hrms.util.HRMSHelper;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/welcome")
public class WelcomeService {

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody

	public String welome() {
		try {
			return HRMSHelper.createJsonString(new CandidateFamilyDetail());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
