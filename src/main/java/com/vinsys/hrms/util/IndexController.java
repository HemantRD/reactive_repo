package com.vinsys.hrms.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
public class IndexController implements ErrorController {
	private static final String PATH = "/error";

	@RequestMapping(value = PATH)
	public void error(HttpServletResponse response) {
		try {
			response.sendRedirect("/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getErrorPath() {
		return PATH;
	}
}