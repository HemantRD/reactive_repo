package com.vinsys.hrms;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class CORSFilter implements Filter {
	private static final String ONE_HOUR = "3600";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "*");

		if (req instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) req;
			if (httpServletRequest.getHeader(HttpHeaders.ORIGIN) != null
					&& httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name())
					&& httpServletRequest.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null) {
				return;
			}
		}
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
	}
}