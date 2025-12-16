package com.vinsys.hrms.employee.service.impl.personaldetails.processors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonalDetailsConfigurator {

	@Bean
	public PersonalDetailsProcessorFactory personnelDetailsProcessor() {
		return new PersonalDetailsProcessorFactory();
	}
}
