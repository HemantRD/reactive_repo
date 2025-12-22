package com.vinsys.hrms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.IHRMSConstants;

import net.sf.webdav.WebdavServlet;

@Configuration
public class FilterConfigurations {

	@Value("${rootDirectory}")
	private String rootDirectory;
	@Value("${system.env}")
	private String systemEnv;

	@Autowired
	private SecurityFilter securityFilter;

	@Bean
	FilterRegistrationBean<Filter> corsFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new CORSFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setOrder(0);
		return filterRegistrationBean;
	}

	@Bean
	FilterRegistrationBean<Filter> registerSecurityFilter() {
		Collection<String> pattern = new ArrayList<>();
		pattern.add("/*");
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>(securityFilter);
		filterRegistrationBean.addInitParameter(IHRMSConstants.SYSTEM_ENV, systemEnv);
		filterRegistrationBean.setOrder(1);
		return filterRegistrationBean;
	}

	@Bean
	ServletRegistrationBean<Servlet> webdav() {
		ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>();
		Map<String, String> map = new HashMap<>();
		map.put("rootpath", rootDirectory);
		bean.setInitParameters(map);
		bean.setServlet(new WebdavServlet());
		bean.setUrlMappings(Arrays.asList("/webdav/*"));
		bean.setLoadOnStartup(1);

		return bean;
	}
}
