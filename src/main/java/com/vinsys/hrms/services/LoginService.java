package com.vinsys.hrms.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSLoginDAO;
import com.vinsys.hrms.dao.IHRMSMapCatalogue;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.dao.IHRMSMenuDAO;
import com.vinsys.hrms.dao.attendance.IHRMSEmployeeRemoteLocationAttendanceDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterDriverDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTraveldeskApproverDAO;
import com.vinsys.hrms.datamodel.HRMSLoginRequest;
import com.vinsys.hrms.datamodel.VOCandidate;
import com.vinsys.hrms.datamodel.VOLoginEntity;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MapCatalogue;
import com.vinsys.hrms.entity.MasterMenu;
import com.vinsys.hrms.entity.MasterOrganizationEmailConfig;
import com.vinsys.hrms.entity.attendance.EmployeeRemoteLocationAttendanceDetail;
import com.vinsys.hrms.entity.traveldesk.MasterDriver;
import com.vinsys.hrms.entity.traveldesk.MasterTraveldeskApprover;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.JWTTokenHelper;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/login")
public class LoginService {

	@Autowired

	private IHRMSLoginDAO logDao;
	@Autowired
	IHRMSCandidateDAO candidateDao;
	@Autowired
	IHRMSMapCatalogue mapCatalogueDAO;

	@Value("${token.expiry}")
	private long expiry;

	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO configDAO;

	@Value("${app_version}")
	private String applicationVersion;

	@Autowired
	IHRMSTraveldeskApproverDAO tdApproverDAO;

	@Autowired
	IHRMSMasterDriverDAO masterDriverDAO;

	@Autowired
	IHRMSEmployeeRemoteLocationAttendanceDAO empRemLocAttDAO;
	@Autowired
	IHRMSMenuDAO menuDAO;

	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	@RequestMapping(path = "/loginCheck", method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String loginCheck(@RequestBody HRMSLoginRequest request, HttpServletResponse httpResponse)
			throws IOException {

		try {

			if (request.getCurrentVersion() == null) {
				throw new HRMSException(IHRMSConstants.UPDATE_REQUIRED_ERROR_CODE,
						IHRMSConstants.UPDATE_REQUIRED_MESSAGE);
			} else if (!request.getCurrentVersion().equalsIgnoreCase(applicationVersion)) {
				if (!request.getGpsLogin().equalsIgnoreCase("true")) {
					throw new HRMSException(IHRMSConstants.UPDATE_REQUIRED_ERROR_CODE,
							IHRMSConstants.UPDATE_REQUIRED_MESSAGE);
				}
			}
			HRMSHelper.loginValidation(request);
			// Start
			String encryptedString = HRMSHelper.encryptToSHA256(request.getPassword());
			request.setPassword(encryptedString);
			// End

			// FIRST TIME LOGIN IS NOT IMPLEMENTED YET
			LoginEntity entity = logDao.findByCustomQuery(request.getUsername(), request.getPassword());

			if (entity != null && entity.getCandidate() != null
					&& !entity.getCandidate().getIsActive().equalsIgnoreCase(IHRMSConstants.isNotActive)) {
				// if (entity != null ) {

				if (request != null && !HRMSHelper.isNullOrEmpty(request.getNewPassword())) {

					entity.setPassword(HRMSHelper.encryptToSHA256(request.getNewPassword()));
					entity.setIsFirstLogin("N");
//					entity.setLastUpdatedPassword(new Date(System.currentTimeMillis()));

					logDao.save(entity);
					return HRMSHelper.sendSuccessResponse(IHRMSConstants.PasswordChangeSuccesfullyMessage,
							IHRMSConstants.successCode);
				}

				if (entity.getOrganization() != null) {

					if (entity.getOrganization().getSubscription() != null) {

						/**
						 * Implement code to check subscription for organization
						 */
						/*
						 * Date endDate = entity.getOrganization().getSubscription().getEndDate(); int
						 * diff = new Date().compareTo(endDate); if (diff > 0) { throw new
						 * HRMSException(IHRMSConstants.SubscriptionExpiredCode,
						 * IHRMSConstants.SubscriptionExpiredMessage);
						 * 
						 * }
						 */
					} else {

						throw new HRMSException(IHRMSConstants.NotSubscribedCode, IHRMSConstants.NotSubscribedMessage);
					}

				} else {

					throw new HRMSException(IHRMSConstants.OrgNotRegisteredCode,
							IHRMSConstants.OrgNotRegisteredMessage);
				}

				/*
				 * List<MasterMenu> listOfMenu = new ArrayList<MasterMenu>(); MasterMenu menu =
				 * menuDao.findById(1); listOfMenu.add(menu); MasterMenu menu2 =
				 * menuDao.findById(4); listOfMenu.add(menu2);
				 */

				VOLoginEntity voLoginEntity = HRMSResponseTranslator.translateToLoginResponse(entity);
				Candidate candidate = candidateDao.findByloginEntity(entity);

				Candidate candForCandEmpStatus = candidateDao.findCandidateEmploymentStatusByCandId(candidate.getId());
				if (!HRMSHelper.isNullOrEmpty(candForCandEmpStatus)
						&& !HRMSHelper.isNullOrEmpty(candForCandEmpStatus.getEmploymentType()) && !HRMSHelper
								.isNullOrEmpty(candForCandEmpStatus.getEmploymentType().getEmploymentTypeName())) {
					candidate.setCandidateEmploymentStatus(
							candForCandEmpStatus.getEmploymentType().getEmploymentTypeName());
				}

				VOCandidate voCandidate = null;
				if (candidate != null) {
					voCandidate = HRMSResponseTranslator.translateToCandidateBriefModle(candidate);
				}
				voLoginEntity.setCandidate(voCandidate);
				Date joiningDate = voCandidate.getCandidateProfessionalDetail().getDateOfJoining();
				/*
				 * voLoginEntity.setNoOfDaysToConfirm(HRMSDateUtil.getDifferenceInDays(new
				 * Date() , joiningDate));
				 */
				/**
				 * This is use to check whether Login Employee is org level or not
				 */

				/**
				 * Modified by Nitin on 13th march *Reason -: Earlier it was checking employee
				 * is org level or not,but if a candidate is not employee condition was missed
				 */
				/*** modification start - Nitin */
				if (voLoginEntity.getCandidate().getEmployee() != null) {
					List<MasterOrganizationEmailConfig> masterOrganizationEmailConfigEntity = configDAO
							.findBYorgLevelEmployeeAndOrgId(voLoginEntity.getCandidate().getEmployee().getId(),
									SecurityFilter.TL_CLAIMS.get().getOrgId());
					if (!HRMSHelper.isNullOrEmpty(masterOrganizationEmailConfigEntity))
						voLoginEntity.setIsOrgLevel(true);

					int probationPeriod = 0;
					if (!HRMSHelper
							.isNullOrEmpty(voLoginEntity.getCandidate().getEmployee().getEmployeeProbationPeriod())) {
						probationPeriod = Integer
								.parseInt(voLoginEntity.getCandidate().getEmployee().getEmployeeProbationPeriod());
					}

					Date tempDate = new Date();
					Calendar cal = Calendar.getInstance();
					cal.setTime(joiningDate);
					cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) + probationPeriod));
					tempDate = HRMSDateUtil.setTimeStampToZero(cal.getTime());
					long noOfDaysLeftToConfirm = HRMSDateUtil.getDifferenceInDays(new Date(), tempDate);
					voLoginEntity.setNoOfDaysToConfirm(noOfDaysLeftToConfirm);

				}

				/*** modification end - Nitin */

				/*** Existing code start -: Rohan */
				// List<MasterOrganizationEmailConfig> masterOrganizationEmailConfigEntity =
				// configDAO
				// .findBYorgLevelEmployee(voLoginEntity.getCandidate().getEmployee().getId());
				// if(!HRMSHelper.isNullOrEmpty(masterOrganizationEmailConfigEntity))
				// voLoginEntity.setIsOrgLevel(true);
				/*** Existing code end -: Rohan */

				/**
				 * End
				 */

				/**
				 * This below code is use to show Approve handover checklist menu to employee
				 */
				// logger.info("Employee Id==
				// "+voLoginEntity.getCandidate().getEmployee().getId());
				/**
				 * Modified by Nitin on 13th march *Reason -: Earlier it was checking employee
				 * is org level or not,but if a candidate is not employee condition was missed
				 */
				/** modification start - Nitin */
				if (voLoginEntity.getCandidate().getEmployee() != null) {
					List<MapCatalogue> mapCatalogueEntity = mapCatalogueDAO.findByApproverId(
							voLoginEntity.getCandidate().getEmployee().getId(),
							candidate.getLoginEntity().getOrganization().getId(),
							candidate.getCandidateProfessionalDetail().getDivision().getId());
					if (!HRMSHelper.isNullOrEmpty(mapCatalogueEntity)) {
						voLoginEntity.setApprovalofHandoverChecklist(true);
					}
				}
				/** modification end -Nitin */

				/** Existing code start -: Rohan */

				// MapCatalogue
				// mapCatalogueEntity=mapCatalogueDAO.findByApproverId(voLoginEntity.getCandidate().getEmployee().getId());
				// if(!HRMSHelper.isNullOrEmpty(mapCatalogueEntity)) {
				// voLoginEntity.setApprovalofHandoverChecklist(true);
				// }

				/*** Existing code end -: Rohan */

				/**
				 * End
				 */

				if (!HRMSHelper.isNullOrEmpty(voLoginEntity.getCandidate().getEmployee())) {
					List<MasterTraveldeskApprover> tdApprovers = tdApproverDAO.findLoggedInEmployeeApproverOrNot(
							voLoginEntity.getCandidate().getEmployee().getId(),
							candidate.getLoginEntity().getOrganization().getId(),
							candidate.getCandidateProfessionalDetail().getDivision().getId());
					if (!HRMSHelper.isNullOrEmpty(tdApprovers)) {
						for (MasterTraveldeskApprover traveldeskApprover : tdApprovers) {
							if (traveldeskApprover.getApproverType().equals(IHRMSConstants.APPROVER_TYPE_TRAVELDESK)) {
								voLoginEntity.setHRTravelDesk(true);
							} else {
								voLoginEntity.setManagerTravelDesk(true);
							}
						}
					}
				}

				// checking if employee a cab driver?
				if (!HRMSHelper.isNullOrEmpty(voLoginEntity.getCandidate().getEmployee())) {
					MasterDriver driver = masterDriverDAO.findDriverByEmployeeId(
							voLoginEntity.getCandidate().getEmployee().getId(), IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(driver)) {
						voLoginEntity.setDriverTravelDesk(true);
					} else {
						voLoginEntity.setDriverTravelDesk(false);
					}
				} else {
					voLoginEntity.setDriverTravelDesk(false);
				}

				// next added by SSW on 03 Dec 2018 for checking of logged in employee
				// is Allowed for remote Attendance or not
				if (!HRMSHelper.isNullOrEmpty(voLoginEntity.getCandidate().getEmployee())) {
					EmployeeRemoteLocationAttendanceDetail empRemLocAtt = empRemLocAttDAO
							.getEmployeeRemoteLocationDetailByEmpId(IHRMSConstants.isActive,
									voLoginEntity.getCandidate().getEmployee().getId());
					if (!HRMSHelper.isNullOrEmpty(empRemLocAtt) && empRemLocAtt.getIsRemoteLocationAttendanceAllowed()
							.equals(IHRMSConstants.MOBILE_REMOTE_LOCATION_ATTENDANCE_ALLOWED_Y)) {
						voLoginEntity.setRemoteLocationAttendanceAllowed(true);
					} else if (!HRMSHelper.isNullOrEmpty(empRemLocAtt)
							&& empRemLocAtt.getIsRemoteLocationAttendanceAllowed()
									.equals(IHRMSConstants.MOBILE_REMOTE_LOCATION_ATTENDANCE_ALLOWED_N)) {
						voLoginEntity.setRemoteLocationAttendanceAllowed(false);
					} else {
						voLoginEntity.setRemoteLocationAttendanceAllowed(false);
					}
				} else {
					voLoginEntity.setRemoteLocationAttendanceAllowed(false);
				}

				// up to this added by SSW on 03 dec 2018

				// next added by SSW on 15DEC2018 for : checking if IMEI number is present in
				// against the customer
				// or not
				if (voLoginEntity.isRemoteLocationAttendanceAllowed()) {
					if (!HRMSHelper.isNullOrEmpty(candidate)
							&& !HRMSHelper.isNullOrEmpty(candidate.getCandidatePersonalDetail())
							&& !HRMSHelper.isNullOrEmpty(candidate.getCandidatePersonalDetail().getMappedIMEI())) {
						if (candidate.getCandidatePersonalDetail().getMappedIMEI().length() > 13
								&& candidate.getCandidatePersonalDetail().getMappedIMEI().length() < 19) {
							voLoginEntity.setImeiNumberPresent(true);
						}
					}
				}

				// upt to this added on 15DEC2018

				voLoginEntity.setMenu(getMenuByRole(entity, voLoginEntity));
				voLoginEntity.setResponseCode(IHRMSConstants.successCode);
				voLoginEntity.setResponseMessage(IHRMSConstants.successMessage);

				// String apiKey = UUID.randomUUID().toString();
				String apiKey = JWTTokenHelper.generateToken(entity, expiry);
				String userName = entity.getUsername();
				entity.setApiKey("JWT TOKEN");
				logDao.save(entity);
				voLoginEntity.setToken(Base64.encodeBase64String(apiKey.getBytes()));
//				httpResponse.setHeader(IHRMSConstants.AUTH_KEY,
//						Base64.encodeBase64String((apiKey + "|" + userName).getBytes()));

				// httpResponse.setHeader(IHRMSConstants.AUTH_KEY,Base64.encodeBase64String(apiKey.getBytes()));

				voLoginEntity.setApplicationVersion(applicationVersion);
				return HRMSHelper.createJsonString(voLoginEntity);
			} else {

				if (entity != null && entity.getCandidate() != null
						&& entity.getCandidate().getIsActive().equalsIgnoreCase(IHRMSConstants.isNotActive)) {
					throw new HRMSException(IHRMSConstants.AuthenticationFailedCode,
							IHRMSConstants.EMP_RESIGNED_AUTHENTICATION_FAILED);
				}

				throw new HRMSException(IHRMSConstants.AuthenticationFailedCode,
						IHRMSConstants.AuthenticationFailedMessage);

			}
		} catch (HRMSException e) {
			e.printStackTrace();
			return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
		} catch (Exception unknown) {

			unknown.printStackTrace();
			return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
		}

	}

	private List<MasterMenu> getMenuByRole(LoginEntity entity, VOLoginEntity voLoginEntity) {
		List<Long> roleIds = new ArrayList<Long>();

		for (LoginEntityType loginEntityType : entity.getLoginEntityTypes()) {
			roleIds.add(loginEntityType.getId());
		}

		List<MasterMenu> menuList = menuDAO.getMenuByRoleIdInAndIsActive(roleIds.toArray(), IHRMSConstants.isActive);
//		List<MasterMenu> menuList = menuDAO.getMenuByRoleAndIsActive(roleId, IHRMSConstants.isActive);
		List<MasterMenu> newMenuList = new ArrayList<MasterMenu>();
		for (MasterMenu menu : menuList) {
			if (HRMSHelper.isNullOrEmpty(menu.getParent())
					&& (!HRMSHelper.isNullOrEmpty(containsId(menuList, menu.getId())))) {
				List<MasterMenu> childList = menuList.stream().filter(l -> l.getParent() == menu.getId())
						.collect(Collectors.toList());
				if (!HRMSHelper.isNullOrEmpty(childList))
					menu.setMenuItems(childList);
				newMenuList.add(menu);
			}
		}

		return newMenuList;

	}

	private boolean containsId(List<MasterMenu> list, Long id) {
		return list.stream().filter(o -> !HRMSHelper.isNullOrEmpty(o.getParent()) ? o.getParent().equals(id) : false)
				.findFirst().isPresent();
	}

	@RequestMapping(path = "/logout", method = { RequestMethod.GET }, produces = "application/json")
	public void logOut(HttpServletRequest httpServletRequest) throws IOException {
		String token = httpServletRequest.getHeader(IHRMSConstants.AUTH_KEY);
		String decodedToken = new String(Base64.decodeBase64(token.getBytes()));
		String[] arr = decodedToken.split("\\|");
		if (arr != null && arr.length == 2) {
			String uName = arr[1];
			LoginEntity entity = logDao.findByUsername(uName);
			entity.setApiKey(null);
			logDao.save(entity);
		}
	}

	@RequestMapping(path = "/updatePassword/{username}/{newPassword}", method = {
			RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String updatePassword(@PathVariable("username") String username,
			@PathVariable("newPassword") String newPassword) throws IOException {

		try {
			int count = 0;
			if (!HRMSHelper.isNullOrEmpty(newPassword) && newPassword.length() >= 6) {

				if (!HRMSHelper.isNullOrEmpty(username)) {
					// update given username password
					LoginEntity loginEntity = logDao.findByUsername(username);
					if (!HRMSHelper.isNullOrEmpty(loginEntity)) {
						loginEntity.setPassword(HRMSHelper.encryptToSHA256(newPassword));

						logDao.save(loginEntity);
						++count;
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}

				} else {
					// update all password
					throw new HRMSException(IHRMSConstants.InvalidActionCode, IHRMSConstants.InvalidActionMessage);
					/*
					 * count=0; List<LoginEntity> userLoginList = logDao.findAll(); for (LoginEntity
					 * loginEntity : userLoginList) {
					 * loginEntity.setPassword(HRMSHelper.encryptToSHA256(newPassword));
					 * logDao.save(loginEntity); ++count; }
					 */
				}
				return HRMSHelper.sendSuccessResponse(count + " Records updated", IHRMSConstants.successCode);
			} else {
				throw new HRMSException(IHRMSConstants.InValidDetailsCode, IHRMSConstants.InvalidDetailsMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
			return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
		} catch (Exception unknown) {

			unknown.printStackTrace();
			return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
		}

	}

	@RequestMapping(path = "/encryptExistingPasswords", method = { RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String encryptExistingPasswords() throws IOException {

		try {
			int count = 0;

			count = 0;
			List<LoginEntity> userLoginList = logDao.findAll();
			for (LoginEntity loginEntity : userLoginList) {

				if (!HRMSHelper.isNullOrEmpty(loginEntity.getPassword())) {
					loginEntity.setPassword(HRMSHelper.encryptToSHA256(loginEntity.getPassword()));
					logDao.save(loginEntity);
					++count;
				} else {
					logger.info("Null password login entity Id :::: " + loginEntity.getId());
				}

			}

			logger.info(" *** Total Records   : " + userLoginList.size());
			logger.info(" *** Updated Records : " + count);

			return HRMSHelper.sendSuccessResponse(
					"Toatal Records :: " + userLoginList.size() + "      Updated Records ::  " + count,
					IHRMSConstants.successCode);

		} catch (Exception unknown) {

			unknown.printStackTrace();
			return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
		}

	}

	@RequestMapping(path = "/changePassword", method = {
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String changePassword(@RequestBody HRMSLoginRequest request, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException {
		try {
			if (!HRMSHelper.isNullOrEmpty(request.getUsername()) && !HRMSHelper.isNullOrEmpty(request.getPassword())
					&& !HRMSHelper.isNullOrEmpty(request.getNewPassword())) {

				// Start
				String encryptedString = HRMSHelper.encryptToSHA256(request.getPassword());
				request.setPassword(encryptedString);
				// End

				// FIRST TIME LOGIN IS NOT IMPLEMENTED YET
				LoginEntity entity = logDao.findByCustomQuery(request.getUsername(), request.getPassword());

				if (!HRMSHelper.isNullOrEmpty(entity)) {
					String token = httpRequest.getHeader("Authorization");
					token = token.substring(7, token.length());
					Claims claims = JWTTokenHelper.getLoggedInEmpDetail(token);
					String logedInUser = claims.get("username").toString();
					// Long logedInUserEmpId=Long.valueOf(claims.get("employeeId").toString());

					if (!HRMSHelper.isNullOrEmpty(logedInUser) && logedInUser.equals(request.getUsername())) {

						if (entity != null && entity.getCandidate() != null
								&& !entity.getCandidate().getIsActive().equalsIgnoreCase(IHRMSConstants.isNotActive)) {
							// if (entity != null ) {

							if (request != null && !HRMSHelper.isNullOrEmpty(request.getNewPassword())) {

								entity.setPassword(HRMSHelper.encryptToSHA256(request.getNewPassword()));
								entity.setIsFirstLogin("N");
								// entity.setLastUpdatedPassword(new Date(System.currentTimeMillis()));

								logDao.save(entity);
								return HRMSHelper.sendSuccessResponse(IHRMSConstants.PasswordChangeSuccesfullyMessage,
										IHRMSConstants.successCode);
							}
						}
					} else {
						throw new HRMSException(IHRMSConstants.INVALID_USER, IHRMSConstants.INVALID_USER_LOGIN);
					}
				} else {
					throw new HRMSException(IHRMSConstants.AuthenticationFailedCode,
							IHRMSConstants.AuthenticationFailedMessage);
				}

			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} catch (HRMSException hre) {
			hre.printStackTrace();
			return HRMSHelper.sendErrorResponse(hre.getResponseMessage(), hre.getResponseCode());
		} catch (Exception e) {
			e.printStackTrace();
			return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
		}
		return null;
	}

//	private void findAndSendEmailToEmployeeForChangePassword(long divisionId, long orgId)
//			throws FileNotFoundException, IOException, HRMSException {
//		
//		try {
//			List<LoginEntity> loginEntityList = logDao.findtwentyOneDaysOldPassword(divisionId, orgId);
//			MasterOrganizationEmailConfig config = configDAO.findByorganizationAnddivision(orgId, divisionId);
//			String documentVerificationHeader = IHRMSEmailTemplateConstants.TEMPLATE_SERVICE_COMPLETION;
//			Map<String, String> placeHolderMapping = new HashMap<String, String>();
//
//			if (loginEntityList != null && !loginEntityList.isEmpty()) {
//				for (LoginEntity employee : loginEntityList) {
//
//					placeHolderMapping.put("{candidateFirstname}", employee.getCandidate().getFirstName());
//					placeHolderMapping.put("{candidateLastname}", employee.getCandidate().getLastName());
//					placeHolderMapping.put("{departmentName}", employee.getCandidate().getCandidateProfessionalDetail()
//							.getDepartment().getDepartmentName());
//					int diffrenceInYear = HRMSHelper
//							.calculateAge(employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining());
//					placeHolderMapping.put("{completedYears}", String.valueOf(diffrenceInYear));
//
//					String finalMailContent = HRMSHelper.replaceString(placeHolderMapping, documentVerificationHeader);
//					String emailSubject = "Work Anniversary - Year " + HRMSDateUtil.getCurrentYear();
//
//					// No Email to be send if diffrence in year is 0 or less
//					eventEmailSender.toSendEmailScheduler(employee.getUsername(), null, null, finalMailContent,
//							emailSubject, divisionId, orgId, null);
//
//				}
//			}
//		} catch (Exception ee) {
//			ee.printStackTrace();
//		}
//	}

	/*
	 * public boolean hasEmployeeResigned(LoginEntity[] employee) { boolean resigned
	 * = false; if (!HRMSHelper.isNullOrEmpty(employee)) { EmployeeSeparationDetails
	 * separationDetails =
	 * employeeSeparationDAO.findEmployeeIfResigned(employee.getId(),
	 * IHRMSConstants.isActive); if (!HRMSHelper.isNullOrEmpty(separationDetails)) {
	 * resigned = true; } } return resigned; }
	 */
}
