package com.vinsys.hrms.employee.service.impl.personaldetails.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VinsysPersonalDetailsProcessor extends AbstractPersonalDetailsProcessor {

	private Logger log = LoggerFactory.getLogger(VinsysPersonalDetailsProcessor.class);
	private PersonalDetailsProcessorDependencies leaveProcessorDependencies;

	public VinsysPersonalDetailsProcessor(PersonalDetailsProcessorDependencies leaveProcessorDependencies) {
		super(leaveProcessorDependencies);
		this.leaveProcessorDependencies = leaveProcessorDependencies;
	}


}
