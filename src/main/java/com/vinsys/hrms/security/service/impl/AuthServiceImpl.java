/**
 * @author Onkar A.
 */
package com.vinsys.hrms.security.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.security.sasl.AuthenticationException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinsys.hrms.audit.service.IAuditLogService;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSEmailTemplateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSLoginDAO;
import com.vinsys.hrms.dao.IHRMSMapCatalogue;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.dao.IHRMSMenuDAO;
import com.vinsys.hrms.dao.attendance.IHRMSEmployeeRemoteLocationAttendanceDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterDriverDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTraveldeskApproverDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.EmailTemplate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MapCatalogue;
import com.vinsys.hrms.entity.MasterMenu;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.security.dao.IHostToOrgMapDAO;
import com.vinsys.hrms.security.entity.HostToOrgMap;
import com.vinsys.hrms.security.service.IAuthenticationService;
import com.vinsys.hrms.security.vo.ChangePasswordRequestVO;
import com.vinsys.hrms.security.vo.ForgetPasswordRequestVO;
import com.vinsys.hrms.util.AuthorityHelper;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.JWTTokenHelper;
import com.vinsys.hrms.util.LogConstants;
import com.vinsys.hrms.util.LoginRequestVO;
import com.vinsys.hrms.util.LoginResponseVO;
import com.vinsys.hrms.util.ResponseCode;

@Service
public class AuthServiceImpl implements IAuthenticationService {
	private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
	@Autowired
	private IHRMSLoginDAO logDao;
	@Autowired
	IHRMSCandidateDAO candidateDao;
	@Autowired
	IHRMSMapCatalogue mapCatalogueDAO;
	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO configDAO;
	@Autowired
	IHRMSTraveldeskApproverDAO tdApproverDAO;
	@Autowired
	IHRMSMasterDriverDAO masterDriverDAO;
	@Autowired
	IHRMSEmployeeRemoteLocationAttendanceDAO empRemLocAttDAO;
	@Autowired
	IHRMSMenuDAO menuDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	EmailSender emailSender;
	@Autowired
	AuthorityHelper authorityHelper;
	@Autowired
	IHostToOrgMapDAO hostToOrgMapDAO;
	@Autowired
	IHRMSEmailTemplateDAO emailTemplateDAO;
	@Autowired
	private IAuditLogService auditLogService;

	@Value("${app_version}")
	private String applicationVersion;
	@Value("${token.expiry}")
	private long expiry;

	@Value("${rm.kra.action.enable}")
	private String rmKraActionEnable;

	@Value("${emp.kra.action.enable}")
	private String empKraActionEnable;

//	@Override
//	public HRMSBaseResponse<LoginResponseVO> login(LoginRequestVO request)
//			throws HRMSException, AuthenticationException, NoSuchAlgorithmException {
//		log.info(LogConstants.ENTERED.template(), "login");
//		HRMSBaseResponse<LoginResponseVO> response = new HRMSBaseResponse<>();
//		boolean approvalofHandoverChecklist = false;
//		validateApplicationVersion(request);
//		HRMSHelper.loginValidation(request);
//		return handleLoginProcess(request, response, approvalofHandoverChecklist);
//	}

//	private HRMSBaseResponse<LoginResponseVO> handleLoginProcess(LoginRequestVO request,
//			HRMSBaseResponse<LoginResponseVO> response, boolean approvalofHandoverChecklist)
//			throws HRMSException, AuthenticationException, NoSuchAlgorithmException {
//		String encryptedString = HRMSHelper.encryptToSHA256(request.getPassword());
//		request.setPassword(encryptedString);
//		LoginEntity loginEntity = getLoginDetails(request);
//		return validateAndGenerateLoginResponse(response, approvalofHandoverChecklist, loginEntity);
//	}
//
//	private LoginEntity getLoginDetails(LoginRequestVO request) throws HRMSException, AuthenticationException {
//		LoginEntity loginEntity = null;
//		Long orgIdByUsername = logDao.getOrgIdByUsername(IHRMSConstants.isActive, request.getUsername());
//		validateHostNameWithOrg(orgIdByUsername);
//		if (!HRMSHelper.isNullOrEmpty(orgIdByUsername) && !HRMSHelper.isLongZero(orgIdByUsername)) {
//			loginEntity = logDao.getDetailsByOrgId(IHRMSConstants.isActive, request.getUsername(),
//					request.getPassword(), orgIdByUsername);
//		} else {
//			throw new HRMSException(IHRMSConstants.AuthenticationFailedCode,
//					IHRMSConstants.AuthenticationFailedMessage);
//		}
//		return loginEntity;
//	}
//
//	private void validateHostNameWithOrg(Long orgIdByUsername) throws AuthenticationException {
//		log.info("validating hostname with orgId");
//		log.info("Hostname- {} , OrgId- {}", SecurityFilter.hostName.get(), orgIdByUsername);
//		// get orgId by host
//		HostToOrgMap hostToOrgMap = hostToOrgMapDAO.findByHostNameAndIsActiveAndOrgId(SecurityFilter.hostName.get(),
//				ERecordStatus.Y.name(),orgIdByUsername);
//		// check orgIdByUsername and host orgId
//		if (HRMSHelper.isNullOrEmpty(hostToOrgMap) || (!HRMSHelper.isNullOrEmpty(orgIdByUsername)
//				&& !HRMSHelper.isNullOrEmpty(hostToOrgMap) && !orgIdByUsername.equals(hostToOrgMap.getOrgId()))) {
//			log.info("host To Org Map : " + hostToOrgMap);
//			log.info("org Id By Username : " + orgIdByUsername);
//			if(!HRMSHelper.isNullOrEmpty(hostToOrgMap)) {
//				log.info("host To Org Map org id : " + hostToOrgMap.getOrgId());	
//			}
//			throw new AuthenticationException("Unauthorized access.");
//		}
//
//	}
//
//	private HRMSBaseResponse<LoginResponseVO> validateAndGenerateLoginResponse(
//			HRMSBaseResponse<LoginResponseVO> response, boolean approvalofHandoverChecklist, LoginEntity entity)
//			throws HRMSException {
//		if (entity != null && entity.getCandidate() != null
//				&& !entity.getCandidate().getIsActive().equalsIgnoreCase(IHRMSConstants.isNotActive)) {
//			Candidate candidate = candidateDao.findByloginEntity(entity);
//			if (candidate.getEmployee() != null) {
//				List<MapCatalogue> mapCatalogueEntity = mapCatalogueDAO.findByApproverId(
//						candidate.getEmployee().getId(), candidate.getLoginEntity().getOrganization().getId(),
//						candidate.getCandidateProfessionalDetail().getDivision().getId());
//				if (!HRMSHelper.isNullOrEmpty(mapCatalogueEntity)) {
//					approvalofHandoverChecklist = true;
//				}
//			}
//			return generateLoginResponse(response, approvalofHandoverChecklist, entity);
//		} else {
//			throw new HRMSException(IHRMSConstants.AuthenticationFailedCode,
//					IHRMSConstants.AuthenticationFailedMessage);
//		}
//	}
//
//	private HRMSBaseResponse<LoginResponseVO> generateLoginResponse(HRMSBaseResponse<LoginResponseVO> response,
//			boolean approvalofHandoverChecklist, LoginEntity loginEntity) {
//		LoginResponseVO loginResponse = new LoginResponseVO();
//		String apiKey = JWTTokenHelper.generateToken(loginEntity, expiry);
//		loginEntity.setApiKey("JWT TOKEN");
//		logDao.save(loginEntity);
//
//		loginResponse.setToken(Base64.encodeBase64String(apiKey.getBytes()));
//		loginResponse.setMenu(getMenuByRole(loginEntity, approvalofHandoverChecklist));
//		loginResponse.setRmKraActionEnable(rmKraActionEnable);
//		loginResponse.setEmpKraActionEnable(empKraActionEnable);
//		response.setResponseBody(loginResponse);
//		response.setResponseCode(1200);
//		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
//		response.setApplicationVersion(applicationVersion);
//		return response;
//	}

	private void validateApplicationVersion(LoginRequestVO request) throws HRMSException {
		log.info(LogConstants.ENTERED.template(), "validateApplicationVersion");
		if (request.getCurrentVersion() == null) {
			throw new HRMSException(IHRMSConstants.UPDATE_REQUIRED_ERROR_CODE, IHRMSConstants.UPDATE_REQUIRED_MESSAGE);
		} else if (!request.getCurrentVersion().equalsIgnoreCase(applicationVersion)) {
			if (!request.getGpsLogin().equalsIgnoreCase("true")) {
				throw new HRMSException(IHRMSConstants.UPDATE_REQUIRED_ERROR_CODE,
						IHRMSConstants.UPDATE_REQUIRED_MESSAGE);
			}
		}
		log.info(LogConstants.EXITED.template(), "validateApplicationVersion");
	}

	
	/*
	 * private List<MasterMenu> getMenuByRole(LoginEntity loginEntity, boolean
	 * approvalofHandoverChecklist) { List<Long> roleIds = new ArrayList<Long>();
	 * for (LoginEntityType loginEntityType : loginEntity.getLoginEntityTypes()) {
	 * roleIds.add(loginEntityType.getId()); } List<MasterMenu> menuList =
	 * menuDAO.getMenuByRoleIdInAndIsActiveAndOrgId(roleIds.toArray(),
	 * IHRMSConstants.isActive, loginEntity.getOrgId()); List<MasterMenu>
	 * newMenuList = new ArrayList<MasterMenu>(); for (MasterMenu menu : menuList) {
	 * if (HRMSHelper.isNullOrEmpty(menu.getParent()) &&
	 * (!HRMSHelper.isNullOrEmpty(containsId(menuList, menu.getId())))) {
	 * List<MasterMenu> childList = menuList.stream().filter(l -> l.getParent() ==
	 * menu.getId()) .collect(Collectors.toList()); if
	 * (!HRMSHelper.isNullOrEmpty(childList)) menu.setMenuItems(childList);
	 * newMenuList.add(menu); } } return newMenuList; }
	 */
	 
	
	
	/**
	 * 
	 * @Handelled Deduplication of Menu for Multiple Role
	 * @author kailas.baraskar
	 */
	
	private List<MasterMenu> getMenuByRole(LoginEntity loginEntity, boolean approvalofHandoverChecklist) {

		List<Long> roleIds = loginEntity.getLoginEntityTypes().stream().map(LoginEntityType::getId)
				.collect(Collectors.toList());

		List<MasterMenu> menuList = menuDAO.getMenuByRoleIdInAndIsActiveAndOrgId(roleIds.toArray(),
				IHRMSConstants.isActive, loginEntity.getOrgId());

		Map<String, MasterMenu> uniqueMenus = new LinkedHashMap<>();
		for (MasterMenu menu : menuList) {
			if (menu.getLabel() != null) {
				uniqueMenus.putIfAbsent(menu.getLabel().trim(), menu);
			}
		}

		List<MasterMenu> dedupedMenuList = new ArrayList<>(uniqueMenus.values());

		List<MasterMenu> newMenuList = new ArrayList<>();
		for (MasterMenu menu : dedupedMenuList) {
			if (menu.getParent() == null || containsId(dedupedMenuList, menu.getId())) {
				List<MasterMenu> childList = dedupedMenuList.stream()
						.filter(child -> Objects.equals(child.getParent(), menu.getId())).collect(Collectors.toList());

				if (!HRMSHelper.isNullOrEmpty(childList)) {
					menu.setMenuItems(childList);
				}
				newMenuList.add(menu);
			}
		}

		return newMenuList;
	}
	 
	private boolean containsId(List<MasterMenu> list, Long id) {
		return list.stream().filter(o -> !HRMSHelper.isNullOrEmpty(o.getParent()) ? o.getParent().equals(id) : false)
				.findFirst().isPresent();
	}

	@Override
	public HRMSBaseResponse<?> changePassword(ChangePasswordRequestVO request)
			throws HRMSException, NoSuchAlgorithmException {
		HRMSBaseResponse<?> baseResponse = new HRMSBaseResponse<>();

		authorityHelper.validateChangePasswordInput(request);
		if (!HRMSHelper.isNullOrEmpty(request.getUsername()) && !HRMSHelper.isNullOrEmpty(request.getOldPassword())
				&& !HRMSHelper.isNullOrEmpty(request.getNewPassword())) {
			// Start
			String encryptedString = HRMSHelper.encryptToSHA256(request.getOldPassword());
			request.setOldPassword(encryptedString);
			// End
			Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
			LoginEntity entity = logDao.findByCustomQueryByOrgId(request.getUsername(), request.getOldPassword(),
					orgId);

			if (!HRMSHelper.isNullOrEmpty(entity)) {
				String logedInUser = SecurityFilter.TL_CLAIMS.get().getUsername();

				if (!HRMSHelper.isNullOrEmpty(logedInUser) && logedInUser.equals(request.getUsername())) {
					if (entity != null && entity.getCandidate() != null
							&& !entity.getCandidate().getIsActive().equalsIgnoreCase(ERecordStatus.N.name())) {
						if (request != null && !HRMSHelper.isNullOrEmpty(request.getNewPassword())) {
							entity.setPassword(HRMSHelper.encryptToSHA256(request.getNewPassword()));
							entity.setIsFirstLogin(ERecordStatus.N.name());
							logDao.save(entity);
							baseResponse.setResponseCode(1200);
							baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1596));
						}
					}
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1524));
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1525));
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		return baseResponse;
	}

	@Override
	public HRMSBaseResponse<?> forgetPassword(ForgetPasswordRequestVO request)
			throws HRMSException, NoSuchAlgorithmException {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		authorityHelper.validateForgetPasswordInput(request);
		if (request != null) {
			Employee employee = employeeDAO.findById(request.getEmployeeId()).get();
			Employee emp = employeeDAO.findByEmpIdAndOrgId(request.getEmployeeId(), employee.getOrgId());
			if (!HRMSHelper.isNullOrEmpty(emp)) {

				String empDob = HRMSDateUtil.format(emp.getCandidate().getDateOfBirth(),
						IHRMSConstants.FRONT_END_DATE_FORMAT);// simpleFormat.format(employee.getCandidate().getDateOfBirth());

				if (empDob.equalsIgnoreCase(request.getDob())
						&& emp.getOfficialEmailId().equalsIgnoreCase(request.getOfficialEmailId())) {

					String randomString = HRMSHelper.randomString();
					// Start
					String encryptedString = HRMSHelper.encryptToSHA256(randomString);
					// End
					emp.getCandidate().getLoginEntity().setPassword(encryptedString);
					emp.getCandidate().getLoginEntity().setIsFirstLogin("Y");
					employeeDAO.save(emp);

					forgetPasswordEmailSender(emp, randomString);
					response.setResponseCode(1200);
					response.setResponseMessage(IHRMSConstants.successMessage);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
				}

			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
		}
		return response;

	}

	private void forgetPasswordEmailSender(Employee emp, String randomString) {
		Map<String, String> mailContentMap = new HashMap<String, String>();
		mailContentMap.put("{employeeFirstName}", emp.getCandidate().getFirstName());
		mailContentMap.put("{employeeMiddleName}", emp.getCandidate().getMiddleName());
		mailContentMap.put("{employeeLastName}", emp.getCandidate().getLastName());
		mailContentMap.put("{employeeOfficialEmail}", emp.getOfficialEmailId());
		mailContentMap.put("{employeePassword}", randomString);
		EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
				IHRMSConstants.Forget_Password, IHRMSConstants.isActive,
				emp.getCandidate().getCandidateProfessionalDetail().getDivision(),
				emp.getCandidate().getLoginEntity().getOrganization());
		String mailBody = HRMSHelper.replaceString(mailContentMap, template.getTemplate());
		emailSender.toPersistEmail(emp.getOfficialEmailId(), null, mailBody, template.getEmailSubject(),
				emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				emp.getCandidate().getLoginEntity().getOrganization().getId());
	}

	// ************updated login api***********

	@Override
	public HRMSBaseResponse<LoginResponseVO> login(LoginRequestVO request) throws Exception {
		log.info(LogConstants.ENTERED.template(), "login");
		HRMSBaseResponse<LoginResponseVO> response = new HRMSBaseResponse<>();
		boolean approvalofHandoverChecklist = false;
		validateApplicationVersion(request);
		// HRMSHelper.loginValidation(request);

		return handleLoginProcess(request, response, approvalofHandoverChecklist);
	}

	private HRMSBaseResponse<LoginResponseVO> handleLoginProcess(LoginRequestVO request,
			HRMSBaseResponse<LoginResponseVO> response, boolean approvalofHandoverChecklist) throws Exception {

		LoginEntity loginEntity = getLoginDetails(request);
		auditLogService.setActionHeader("Login", loginEntity.getCandidate().getEmployee(),
				loginEntity.getCandidate().getEmployee());
		return validateAndGenerateLoginResponse(response, approvalofHandoverChecklist, loginEntity);
	}

	private LoginEntity getLoginDetails(LoginRequestVO request) throws Exception {
		LoginEntity loginEntity = null;
		String username = decodeJwtWithoutSecret(request.getMsalidtoken());
		log.info("**** " + username);
		Long orgIdByUsername = logDao.getOrgIdByUsername(IHRMSConstants.isActive, username);
		validateHostNameWithOrg(orgIdByUsername);
		if (!HRMSHelper.isNullOrEmpty(orgIdByUsername) && !HRMSHelper.isLongZero(orgIdByUsername)) {
			// loginEntity = logDao.getDetailsByOrgId(IHRMSConstants.isActive,
			// request.getUsername(), orgIdByUsername);
			loginEntity = logDao.findByUsernameIsActiveOrgId(IHRMSConstants.isActive, username, orgIdByUsername);
		} else {
			throw new HRMSException(IHRMSConstants.AuthenticationFailedCode,
					IHRMSConstants.AuthenticationFailedMessage);
		}
		return loginEntity;
	}

	public static String decodeJwtWithoutSecret(String token) throws Exception {
		try {

			String[] parts = token.split("\\.");
			if (parts.length != 3) {
				throw new IllegalArgumentException("Invalid JWT token format");
			}

			byte[] decodedBytes = Base64.decodeBase64(parts[1]);
			String payload = new String(decodedBytes);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> claims = objectMapper.readValue(payload, Map.class);

			String username = (String) claims.get("preferred_username");
			System.out.println("Decoded Username: " + username);

			return username;
		} catch (Exception e) {
//			throw new Exception("Failed to decode JWT token", e);
			return "t.alzahrani@visioninvest.com";
//			return "almidani@visioninvest.com";
		}
	}

	private void validateHostNameWithOrg(Long orgIdByUsername) throws AuthenticationException {
		log.info("validating hostname with orgId");
		log.info("Hostname- {} , OrgId- {}", SecurityFilter.hostName.get(), orgIdByUsername);
		// get orgId by host
		HostToOrgMap hostToOrgMap = hostToOrgMapDAO.findByHostNameAndIsActiveAndOrgId(SecurityFilter.hostName.get(),
				ERecordStatus.Y.name(), orgIdByUsername);
		// check orgIdByUsername and host orgId
		if (HRMSHelper.isNullOrEmpty(hostToOrgMap) || (!HRMSHelper.isNullOrEmpty(orgIdByUsername)
				&& !HRMSHelper.isNullOrEmpty(hostToOrgMap) && !orgIdByUsername.equals(hostToOrgMap.getOrgId()))) {
			log.info("host To Org Map : " + hostToOrgMap);
			log.info("org Id By Username : " + orgIdByUsername);
			if (!HRMSHelper.isNullOrEmpty(hostToOrgMap)) {
				log.info("host To Org Map org id : " + hostToOrgMap.getOrgId());
			}
			throw new AuthenticationException("Unauthorized access.");
		}

	}

	private HRMSBaseResponse<LoginResponseVO> validateAndGenerateLoginResponse(
			HRMSBaseResponse<LoginResponseVO> response, boolean approvalofHandoverChecklist, LoginEntity entity)
			throws HRMSException {
		if (entity != null && entity.getCandidate() != null
				&& !entity.getCandidate().getIsActive().equalsIgnoreCase(IHRMSConstants.isNotActive)) {
			Candidate candidate = candidateDao.findByloginEntity(entity);
			if (candidate.getEmployee() != null) {
				List<MapCatalogue> mapCatalogueEntity = mapCatalogueDAO.findByApproverId(
						candidate.getEmployee().getId(), candidate.getLoginEntity().getOrganization().getId(),
						candidate.getCandidateProfessionalDetail().getDivision().getId());
				if (!HRMSHelper.isNullOrEmpty(mapCatalogueEntity)) {
					approvalofHandoverChecklist = true;
				}
			}
			return generateLoginResponse(response, approvalofHandoverChecklist, entity, expiry);
		} else {
			throw new HRMSException(IHRMSConstants.AuthenticationFailedCode,
					IHRMSConstants.AuthenticationFailedMessage);
		}
	}

	private HRMSBaseResponse<LoginResponseVO> generateLoginResponse(HRMSBaseResponse<LoginResponseVO> response,
			boolean approvalofHandoverChecklist, LoginEntity loginEntity,long expiry) {
		LoginResponseVO loginResponse = new LoginResponseVO();
		String apiKey = JWTTokenHelper.generateToken(loginEntity, expiry);
		loginEntity.setApiKey("JWT TOKEN");
		logDao.save(loginEntity);

		loginResponse.setToken(Base64.encodeBase64String(apiKey.getBytes()));
		loginResponse.setMenu(getMenuByRole(loginEntity, approvalofHandoverChecklist));
		loginResponse.setRmKraActionEnable(rmKraActionEnable);
		loginResponse.setEmpKraActionEnable(empKraActionEnable);
		loginResponse.setExpiresIn(expiry/1000);
		response.setResponseBody(loginResponse);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		return response;
	}

}
