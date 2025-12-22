package com.vinsys.hrms.employee.service.impl.empdetails.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VinsysEmploymentDetailsProcessor extends AbstractEmploymentDetailsProcessor {

	private Logger log = LoggerFactory.getLogger(VinsysEmploymentDetailsProcessor.class);
	private EmploymentDetailsProcessorDependencies processorDependencies;

	public VinsysEmploymentDetailsProcessor(EmploymentDetailsProcessorDependencies detailsProcessorDependencies) {
		super(detailsProcessorDependencies);
		this.processorDependencies = detailsProcessorDependencies;
	}

}
