package com.vinsys.hrms.employee.service.impl.processors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeaveConfigurator {

	@Bean
	public LeaveProcessorFactory leaveProcessor() {
		return new LeaveProcessorFactory();
	}
}
