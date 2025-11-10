package com.vinsys.hrms.employee.service.impl.attendance.processors;

public class AttendanceProcessorFactory {

	public IAttendanceProcessor getAttendanceProcessor(Long orgid, AttendanceProcessorDependencies dependencies) {
		if (orgid == 1) {
			return new VinsysAttendanceProcessor(dependencies);
		} else if (orgid == 2) {
			return new LryptAttendanceProcessor(dependencies);
		} else {
			throw new RuntimeException("No Such Organization Processor Exists");
		}
	}

}
