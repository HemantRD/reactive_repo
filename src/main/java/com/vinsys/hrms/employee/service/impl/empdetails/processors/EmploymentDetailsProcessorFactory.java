package com.vinsys.hrms.employee.service.impl.empdetails.processors;

public class EmploymentDetailsProcessorFactory {

	public IEmploymentDetailsProcessor getEmploymentDetailsProcessor(Long orgid, EmploymentDetailsProcessorDependencies dependencies) {
			return new VinsysEmploymentDetailsProcessor(dependencies);
		
	}

}
