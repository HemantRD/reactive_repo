package com.vinsys.hrms.employee.service.impl.empdetails.processors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmploymentDetailsConfigurator {

	@Bean
	public EmploymentDetailsProcessorFactory personnelDetailsProcessor() {
		return new EmploymentDetailsProcessorFactory();
	}
}
