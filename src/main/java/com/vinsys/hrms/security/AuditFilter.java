package com.vinsys.hrms.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.vinsys.hrms.audit.entity.AuditLog;
import com.vinsys.hrms.audit.service.Impl.AsyncLoggerService;
import com.vinsys.hrms.constants.ERecordStatus;

public class AuditFilter extends OncePerRequestFilter {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired()
	ThreadLocal<Header> threadLocal;
	@Autowired
	AsyncLoggerService asyncLoggerService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("*****ENTERED IN AUDIT FILTER*********");
		// ************NOT TO BE DELETED**********
		request.getParameter("file");
		// ***************************************
		HttpServletRequest wrappedRequest = new BufferedServletRequestWrapper((HttpServletRequest) request);
		HttpServletResponse wrappedResponse = new BufferedServletResponseWrapper((HttpServletResponse) response);
		ObjectMapper mapper = new ObjectMapper();
		AuditLog auditLog = new AuditLog();

		auditLog.setCreatedBy(wrappedRequest.getHeader("user-id"));
		auditLog.setCreatedDate(new Date());
		auditLog.setIsActive(ERecordStatus.Y.name());
		auditLog.setRequestUrl(wrappedRequest.getRequestURL().toString());
		auditLog.setRequestMethod(wrappedRequest.getMethod());
		
		filterChain.doFilter(wrappedRequest, wrappedResponse);

		auditLog.setActionName(threadLocal.get() != null ? threadLocal.get().getActionName() : null);
		auditLog.setActionBy(threadLocal.get() != null ? threadLocal.get().getActionBy() : null);
		auditLog.setActionOn(threadLocal.get() != null ? threadLocal.get().getActionOn() : null);
		threadLocal.remove();

		try {
			JsonNode responseBody = JsonNodeFactory.instance.objectNode();
			try {
				responseBody = mapper.readTree(((BufferedServletResponseWrapper) wrappedResponse).getContent());
			} catch (Exception e) {
				e.printStackTrace();
			}
			 // Set statusCode and ttResponseMessage after reading requestBody
		    if (responseBody.hasNonNull("responseCode")) {
		        auditLog.setStatusCode(responseBody.get("responseCode").asText());
		    } else {
		        auditLog.setStatusCode("");
		    }
		    if (responseBody.hasNonNull("responseMessage")) {
	        auditLog.setResponseMessage(responseBody.get("responseMessage").asText());
	    } else {
	        auditLog.setResponseMessage(((BufferedServletResponseWrapper) wrappedResponse).getContent());
	    }
		    
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		asyncLoggerService.saveAuditData(auditLog);

		// asyncLogger.testAsyncExe();
		System.out.println("*****EXIT FROM AUDIT FILTER*********");
	}

}
