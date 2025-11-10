package com.vinsys.hrms.employee.service.impl.processors;

public class LeaveProcessorFactory {

	public ILeaveProcessor getLeaveProcessor(Long orgid, LeaveProcessorDependencies dependencies) {
		if (orgid == 1) {
			return new VinsysLeaveProcessor(dependencies);
		} else if (orgid == 2) {
			return new LryptLeaveProcessor(dependencies);
		} else {
			throw new RuntimeException("No Such Organization Processor Exists");
		}
	}
	
}
