package com.vinsys.hrms.directonboard.service.processors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectOnBoardConfigurator {

	@Bean
	public DirectOnBoardProcessorFactory personnelDetailsProcessor() {
		return new DirectOnBoardProcessorFactory();
	}
}
