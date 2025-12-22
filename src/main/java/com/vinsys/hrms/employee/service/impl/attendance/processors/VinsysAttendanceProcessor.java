package com.vinsys.hrms.employee.service.impl.attendance.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VinsysAttendanceProcessor extends AbstractAttendanceProcessor {

	private Logger log = LoggerFactory.getLogger(VinsysAttendanceProcessor.class);
	private AttendanceProcessorDependencies processorDependencies;

	public VinsysAttendanceProcessor(AttendanceProcessorDependencies detailsProcessorDependencies) {
		super(detailsProcessorDependencies);
		this.processorDependencies = detailsProcessorDependencies;
	}

}
