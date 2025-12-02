package com.vinsys.hrms.employee.service.impl.personaldetails.processors;

public class PersonalDetailsProcessorFactory {

	public IPersonalDetailsProcessor getPersonalProcessor(Long orgid, PersonalDetailsProcessorDependencies dependencies) {
		
			return new VinsysPersonalDetailsProcessor(dependencies);
		
	}

}
