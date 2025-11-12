package com.vinsys.hrms.directonboarding.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.directonboarding.services.IDirectOnboardingService;
import com.vinsys.hrms.directonboarding.vo.DirectOnboardingRequest;
import com.vinsys.hrms.directonboarding.vo.DirectOnboardingResponse;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.security.AuthInfo;
import com.vinsys.hrms.security.SecurityFilter;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class DirectOnboardingController implements IDirectOnboardingController {
	IDirectOnboardingService onboardingService;
	IHRMSEmployeeDAO employeeDAO;

	public DirectOnboardingController(@RequestBody IDirectOnboardingService onboardingService,
			IHRMSEmployeeDAO employeeDAO) {
		this.onboardingService = onboardingService;
		this.employeeDAO = employeeDAO;
	}

	@PostMapping(path = "onboard", produces = "application/json", consumes = "application/json")
	public HRMSBaseResponse<DirectOnboardingResponse> directOnBoard(DirectOnboardingRequest request) {
		HRMSBaseResponse<DirectOnboardingResponse> response = new HRMSBaseResponse<>();
		DirectOnboardingResponse directOnboardingResponse = new DirectOnboardingResponse();
		try {
			onboardingService.onboard(request);
		} catch (IllegalArgumentException e) {
			response.setResponseMessage(request.getValidationMessage());
			response.setResponseCode(1500);
			response.setResponseBody(null);
		}
		response.setResponseBody(directOnboardingResponse);
		return response;
	}

	@PostMapping(path = "list")
	public List<Employee> list() {
		AuthInfo info = SecurityFilter.TL_CLAIMS.get();
		Employee employee = employeeDAO.findEmpCandByEmpId(info.getEmployeeId());
		Long orgId = employee.getEmployeeDivision().getDivision().getOrganization().getId();
		Long branchId = employee.getEmployeeBranch().getId();
		List<Employee> employees = employeeDAO.findByOrgIdBrachId(orgId, branchId);
		return employees;
	}
}
