package com.vinsys.hrms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.vinsys.hrms.security.Header;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
@SecurityScheme(name = "Authorization", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
////@PropertySource(value="${HRMSCONFIG}")
public class Launcher {

	public static void main(String[] args) {
		SpringApplication.run(Launcher.class, args);

	}
	@Bean
	public ThreadLocal<Header> threadLocal() {
		return new ThreadLocal<Header>();
	}

}
