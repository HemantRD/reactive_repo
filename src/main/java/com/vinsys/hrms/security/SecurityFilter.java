package com.vinsys.hrms.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.security.service.IAuthorizationService;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.JWTTokenHelper;
import com.vinsys.hrms.util.ResponseCode;

import io.jsonwebtoken.Claims;

////@PropertySource(value="${HRMSCONFIG}")
@Component
public class SecurityFilter implements Filter {
	public static final ThreadLocal<AuthInfo> TL_CLAIMS = new ThreadLocal<>();
	private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
	private static final String AUTH_TOKEN = "Authorization";
	private static final String APPLICATION_JSON = "application/json";
	private FilterConfig filterConfig = null;
	public static final ThreadLocal<String> hostName = new ThreadLocal<>();

	@Autowired
	private URLWhiteListingHelper whiteListingHelper;

	@Autowired
	private IAuthorizationService authorizationService;

	@Value("${app_version}")
	private String applicationVersion;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("Hostname {}", request.getServerName());
		logger.info("Protocol {} {}", request.getProtocol(),request.getScheme());
		hostName.set(request.getServerName());

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String requestedURI = httpServletRequest.getRequestURI();
		Claims claims = JWTTokenHelper.getLoggedInEmpDetail(JWTTokenHelper.parseJwt((HttpServletRequest) request));
		if (claims != null) {
			TL_CLAIMS.set(JWTTokenHelper.claimsToAuthInfo(claims));
		}
		if ("1".equals(filterConfig.getInitParameter(IHRMSConstants.SYSTEM_ENV))) {
			if (whiteListingHelper.isWhiteListedV2(requestedURI)) {
				chain.doFilter(request, response);
			} else {
				AuthInfo info = TL_CLAIMS.get();
				if (info != null) {
					logger.debug(info + "");
					logger.info("##################### SECURITY AUTHORIZER ##################");
					List<String> roles = info.getRoles();
					if (authorizationService.isAuthorized(requestedURI, roles)) {
						logger.info("You are Authorized");
					} else {
						logger.info("You are Not Authorized");
					}
				}
				if (validateApiKeyPresence(httpServletRequest)) { // key header validation
					try {
						String authToken = httpServletRequest.getHeader(AUTH_TOKEN);
						String token = authToken.substring(7, authToken.length());
						if (JWTTokenHelper.validateJWTToken(token)) {
							logger.info("##################### SECURITY AUTHORIZER ##################");
							chain.doFilter(request, response);
						} else {
							throw new InvalidTokenException(IHRMSConstants.TOKEN_NOT_VALID);
						}
					} catch (InvalidTokenException e) {
						httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						httpServletResponse.setContentType(APPLICATION_JSON);
						sendErrorMessage(httpServletResponse);
						e.printStackTrace();
					} catch (NestedServletException | FileSizeLimitExceededException
							| MaxUploadSizeExceededException e) {
						httpServletResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
						httpServletResponse.setContentType(APPLICATION_JSON);
						sendMaxFileSizeErrorMessage(httpServletResponse);
						e.printStackTrace();
					} catch (Exception e) {
						httpServletResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
						httpServletResponse.setContentType(APPLICATION_JSON);
						sendErrorMessage(httpServletResponse);
						e.printStackTrace();
					}
				} else {
					sendErrorMessage(httpServletResponse);
				}
			}
		} else {
			chain.doFilter(request, response);
		}
		TL_CLAIMS.remove();
	}

	private void sendErrorMessage(HttpServletResponse httpServletResponse) throws IOException {

		PrintWriter out = httpServletResponse.getWriter();
		out.println("{\r\n" + "  \"status\": \"FAILED\",\r\n" + "  \"msg\":\"" + IHRMSConstants.TOKEN_NOT_VALID
				+ "\"\r\n" + "}");
	}

	private void sendMaxFileSizeErrorMessage(HttpServletResponse httpServletResponse) throws IOException {

		PrintWriter out = httpServletResponse.getWriter();
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		response.setResponseCode(1592);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1592));
		response.setApplicationVersion(applicationVersion);
		out.println(objectMapper.writeValueAsString(response));
	}

	private boolean validateApiKeyPresence(HttpServletRequest httpServletRequest) {
		logger.info("************validateApiKeyPresence****************");
		String token = httpServletRequest.getHeader(AUTH_TOKEN);
//		logger.info(token);
		if (null != token && token.length() != 0)
			return true;
		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

}
