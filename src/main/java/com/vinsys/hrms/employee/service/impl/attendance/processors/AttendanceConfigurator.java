package com.vinsys.hrms.employee.service.impl.attendance.processors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AttendanceConfigurator {

	@Bean
	public AttendanceProcessorFactory personnelDetailsProcessor() {
		return new AttendanceProcessorFactory();
	}
}
