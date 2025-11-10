package com.vinsys.hrms.util;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.security.AuthInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class JWTTokenHelper {

	private static final Logger log = LoggerFactory.getLogger(JWTTokenHelper.class);
	public static String SECURITY_KEY = UUID.randomUUID().toString();

	public static String generateToken(LoginEntity entity, long expiry) {
		log.info(LogConstants.ENTERED.template(), "generateToken");
		try {
			TokenFields tokenFields = getDetailsToGenerateToken(entity);
			return generateTokenProcess(entity, expiry, tokenFields);
		} catch (Exception e) {
			log.error("Error while generating token: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private static String generateTokenProcess(LoginEntity entity, long expiry, TokenFields tokenFields) {
		log.info(LogConstants.ENTERED.template(), "generateTokenProcess");
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECURITY_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		
		List<String> roles = tokenFields.getRoles();
		List<String> priorityOrder = Arrays.asList(IHRMSConstants.HR, IHRMSConstants.MANAGER, IHRMSConstants.EMPLOYEE, IHRMSConstants.HOD);
		List<String> sortedRoles = new ArrayList<>();

		for (String role : priorityOrder) {
		    if (roles.contains(role)) {
		        sortedRoles.add(role);
		    }
		}
		for (String role : roles) {
		    if (!priorityOrder.contains(role)) {
		        sortedRoles.add(role);
		    }
		}
		
		JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString()).claim("username", entity.getUsername())
				.claim("employeeId", tokenFields.getLoggedInEmpId())
				.claim("roles", sortedRoles)
				.claim("candidateId", entity.getCandidate().getId()).setIssuedAt(now)
				.claim("organizationId", tokenFields.getOrganizationId())
				.claim("divisionId", tokenFields.getDivisionId()).claim("branchId", tokenFields.getBranchId())
				.claim("rmMailId", tokenFields.getRmMailId()).claim("rmName", tokenFields.getRmName())
				.claim("candidateName", tokenFields.getCandidateName())
				.claim("branchName", tokenFields.getBranchName())
				.claim("designation", tokenFields.getDesignation())
				.claim("departmentName", tokenFields.getDepartmentName())
				.claim("divisioName", tokenFields.getDivisionName())
				.claim("city", tokenFields.getCity())
				.claim("empCode", tokenFields.getEmpCode())
				.claim("candidatePhoto", tokenFields.getCandidatePhoto()).setSubject(IHRMSConstants.TOKEN_SUBJECT)
				.setIssuer(IHRMSConstants.TOKEN_ISSUER).signWith(signatureAlgorithm, signingKey);

		if (expiry > 0) {
			long expMillis = nowMillis + expiry;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		log.info(LogConstants.EXITED.template(), "generateTokenProcess");
		return builder.compact();
	}

	private static TokenFields getDetailsToGenerateToken(LoginEntity entity) {
		log.info(LogConstants.ENTERED.template(), "getDetailsToGenerateToken");
		TokenFields tokenFields = new TokenFields();
		tokenFields.setRoles(getRoles(entity));
		getCandidateDetailsForToken(tokenFields, entity);
		getEmployeeDetailsForToken(tokenFields, entity);
		log.info(LogConstants.EXITED.template(), "getDetailsToGenerateToken");
		return tokenFields;

	}

	private static void getCandidateDetailsForToken(TokenFields tokenFields, LoginEntity entity) {
		log.info(LogConstants.ENTERED.template(), "getCandidateDetailsForToken");
		if (!HRMSHelper.isNullOrEmpty(entity.getCandidate())) {
			tokenFields.setOrganizationId(entity.getOrganization().getId());
			if (!HRMSHelper.isNullOrEmpty(entity.getCandidate().getCandidateProfessionalDetail())) {
				tokenFields.setBranchId(entity.getCandidate().getCandidateProfessionalDetail().getBranch().getId());
				tokenFields.setBranchName(entity.getCandidate().getCandidateProfessionalDetail().getBranch().getBranchName());
				tokenFields.setCity(entity.getCandidate().getCandidateProfessionalDetail().getCity().getCityName());
				tokenFields.setDivisionId(entity.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
				tokenFields.setDivisionName(entity.getCandidate().getCandidateProfessionalDetail().getDivision().getDivisionName());
				tokenFields.setDepartmentName(
						entity.getCandidate().getCandidateProfessionalDetail().getDepartment().getDepartmentName());
				tokenFields.setDesignation(entity.getCandidate().getCandidateProfessionalDetail().getDesignation().getDesignationName());
			}
			if (!HRMSHelper.isNullOrEmpty(entity.getCandidate().getCandidatePersonalDetail())) {
				tokenFields.setCandidatePhoto(entity.getCandidate().getCandidatePersonalDetail().getCandidatePhoto());
			}
			tokenFields
					.setCandidateName(entity.getCandidate().getFirstName() + " " + entity.getCandidate().getLastName());
		}
		log.info(LogConstants.EXITED.template(), "getCandidateDetailsForToken");

	}

	private static void getEmployeeDetailsForToken(TokenFields tokenFields, LoginEntity entity) {
		log.info(LogConstants.ENTERED.template(), "getEmployeeDetailsForToken");
		if (!HRMSHelper.isNullOrEmpty(entity.getCandidate().getEmployee())) {
			tokenFields.setLoggedInEmpId(entity.getCandidate().getEmployee().getId());
			tokenFields.setEmpCode(entity.getCandidate().getEmployee().getEmployeeCode());
			Employee reportingManager = !HRMSHelper
					.isNullOrEmpty(entity.getCandidate().getEmployee().getEmployeeReportingManager())
							? entity.getCandidate().getEmployee().getEmployeeReportingManager().getReporingManager()
							: null;
			if (!HRMSHelper.isNullOrEmpty(reportingManager)) {
				tokenFields.setRmMailId(reportingManager.getOfficialEmailId());
				tokenFields.setRmName(reportingManager.getCandidate().getFirstName() + " "
						+ reportingManager.getCandidate().getLastName());
			}

		}
		log.info(LogConstants.EXITED.template(), "getEmployeeDetailsForToken");
	}

	private static List<String> getRoles(LoginEntity entity) {
		List<String> roles = new ArrayList<>();
		for (LoginEntityType loginEntityType : entity.getLoginEntityTypes()) {
			log.info("loginEntityType::" + loginEntityType.getRole().getRoleName());
			roles.add(loginEntityType.getRole().getRoleName());
		}
		return roles;
	}

	public static boolean validateJWTToken(String token) {
		boolean isValidToken = true;
		String decodedToken = new String(Base64.decodeBase64(token.getBytes()));
		try {
			Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECURITY_KEY))
					.parseClaimsJws(decodedToken).getBody();
		} catch (ExpiredJwtException e) {
			log.error(e.getMessage());
			isValidToken = false;
		} catch (SignatureException e) {
			log.error(e.getMessage());
			isValidToken = false;
		} catch (Exception e) {
			log.error(e.getMessage());
			isValidToken = false;
		}
		return isValidToken;
	}

	public static String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}

	public static AuthInfo claimsToAuthInfo(Claims claims) {
		Long employeeId = Long.valueOf(claims.get("employeeId", Integer.class));
		List<String> roles = (List<String>) claims.get("roles");
		Long candidateId = Long.valueOf(claims.get("candidateId", Integer.class));
		String username = claims.get("username", String.class);
		List<Long> roleIds = (List<Long>) claims.get("roleIds");
		Long orgId = Long.valueOf(claims.get("organizationId", Integer.class));
		return new AuthInfo(employeeId, roles, candidateId, username, roleIds, orgId);
	}

	public static Claims getLoggedInEmpDetail(String token) {
		Claims claims = null;
		if (token != null) {
			String decodedToken = new String(Base64.decodeBase64(token.getBytes()));
			try {
				claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECURITY_KEY))
						.parseClaimsJws(decodedToken).getBody();
			} catch (Exception e) {
				log.error(e.getMessage());
				e.getMessage();
			}
		}
		return claims;
	}

}
