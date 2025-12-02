package com.vinsys.hrms.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeGrantLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveDetailsDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveTypeDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOEmployeeGrantLeaveDetail;
import com.vinsys.hrms.datamodel.VOLeaveCalculationRequest;
import com.vinsys.hrms.datamodel.VOLeaveGrantRequest;
import com.vinsys.hrms.datamodel.VOMasterLeaveType;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeGrantLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/employeeGrantLeave")

//@PropertySource(value="${HRMSCONFIG}")
public class EmployeeGrantLeaveService {

	public static Logger logger = LoggerFactory.getLogger(EmployeeGrantLeaveService.class);
	@Value("${base.url}")
	private String baseURL;

	@Autowired
	IHRMSEmployeeGrantLeaveDAO employeeGrantLeaveDAO;
	@Autowired
	IHRMSMasterLeaveTypeDAO masterLeaveTypeDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailsDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO orgHolidayDAO;
	@Autowired
	IHRMSEmployeeCreditLeaveDAO creditLeaveDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, value = "/submitGrantLeave", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String submitEmployeeGrantLeaveDetails(@RequestBody VOEmployeeGrantLeaveDetail voEmployeeGrantLeaveDetail) {

		EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity;
		Employee employeeEntity;
		MasterLeaveType masterLeaveTypeEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voEmployeeGrantLeaveDetail)
					&& !HRMSHelper.isNullOrEmpty(voEmployeeGrantLeaveDetail.getEmployee().getId())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeGrantLeaveDetail.getMasterLeaveType().getId())) {

				employeeGrantLeaveDetailEntity = employeeGrantLeaveDAO.findById(voEmployeeGrantLeaveDetail.getId());
				masterLeaveTypeEntity = masterLeaveTypeDAO
						.findById(voEmployeeGrantLeaveDetail.getMasterLeaveType().getId()).get();
				if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity)
						&& !HRMSHelper.isNullOrEmpty(masterLeaveTypeEntity)) {
					/* update */
					employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntity);
					employeeGrantLeaveDetailEntity = HRMSRequestTranslator.translateToEmployeeGrantLeaveDetailEntity(
							employeeGrantLeaveDetailEntity, voEmployeeGrantLeaveDetail);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					employeeGrantLeaveDetailEntity = new EmployeeGrantLeaveDetail();
					employeeEntity = employeeDAO.findById(voEmployeeGrantLeaveDetail.getEmployee().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(employeeEntity) && !HRMSHelper.isNullOrEmpty(masterLeaveTypeEntity)) {
						employeeGrantLeaveDetailEntity.setEmployee(employeeEntity);
						employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntity);
						employeeGrantLeaveDetailEntity = HRMSRequestTranslator
								.translateToEmployeeGrantLeaveDetailEntity(employeeGrantLeaveDetailEntity,
										voEmployeeGrantLeaveDetail);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				employeeGrantLeaveDetailEntity = employeeGrantLeaveDAO.save(employeeGrantLeaveDetailEntity);

				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> listResponse = new ArrayList<Object>();
				listResponse
						.add(HRMSEntityToModelMapper.convertToCandidateLeaveGrantModel(employeeGrantLeaveDetailEntity));
				response.setListResponse(listResponse);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(resultMesage);

				return HRMSHelper.createJsonString(response);
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{loggedInEmployeeId}/{employeeId}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getEmployeeGrantLeaveDetails(@PathVariable("loggedInEmployeeId") long loggedInEmployeeId,
			@PathVariable("employeeId") long employeeId) {

		List<EmployeeGrantLeaveDetail> employeeGrantLeaveDetails = new ArrayList<EmployeeGrantLeaveDetail>();
		List<Object> voEmpGrantLeaveList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			// following modified by SSW on 06 Dec 2018 for : previous year leaves were
			// visible to requester
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			if (!HRMSHelper.isLongZero(employeeId)) {
				logger.info("in getEmployeeGrantLeaveDetails() : Find grant leave details of logged in employee");
				employeeGrantLeaveDetails = employeeGrantLeaveDAO.findAllGrantLeaveDetailsOfEmployeeOfYear(employeeId,
						IHRMSConstants.isActive, year);
			} else {
				logger.info(
						"in getCandidateActivityDetails() : Find grant leave details of subordinates in manager login");
				employeeGrantLeaveDetails = employeeGrantLeaveDAO
						.findAllGrantLeaveDetailsOfSubEmployeesOfRM(loggedInEmployeeId);
			}

			if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetails)) {
				voEmpGrantLeaveList = HRMSResponseTranslator
						.translateToEmployeeGrantLeaveDetailVO(employeeGrantLeaveDetails, voEmpGrantLeaveList);
				hrmsListResponseObject.setListResponse((List<Object>) voEmpGrantLeaveList);
				hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
				hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(hrmsListResponseObject);
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String leaveGrantAction(@RequestBody VOLeaveGrantRequest request) {

		EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity = null;
		MasterLeaveType masterLeaveTypeEntity = null;
		Employee employeeEntity = null;
		try {
			if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getActionPerformed())
					&& !HRMSHelper.isNullOrEmpty(request.getLeaveApplied())
					&& !HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getEmployee())
					&& !HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getMasterLeaveType())) {
				employeeGrantLeaveDetailEntity = employeeGrantLeaveDAO.findById(request.getLeaveApplied().getId());

				masterLeaveTypeEntity = masterLeaveTypeDAO
						.findById(request.getLeaveApplied().getMasterLeaveType().getId()).get();
				employeeEntity = employeeDAO.findById(request.getLeaveApplied().getEmployee().getId()).get();
				/*
				 * changed by ssw on 03jan2018 for :: grant leave action : apply
				 */
				boolean grantLeaveApplyAllowedStatus = false;
				if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_APPLY)) {
					/*
					 * finding candidate details after this commented on 03jan2018 morn
					 */
					Candidate candidate = candidateDAO.findById(employeeEntity.getCandidate().getId()).get();
					/*
					 * finding employee branch, division, org
					 */
					VOLeaveCalculationRequest leaveCalculation = new VOLeaveCalculationRequest();
					leaveCalculation.setBranchId(candidate.getCandidateProfessionalDetail().getBranch().getId());
					leaveCalculation.setDivisionId(candidate.getCandidateProfessionalDetail().getDivision().getId());
					leaveCalculation.setFromDate(HRMSDateUtil.parse(request.getLeaveApplied().getFromDate(),
							IHRMSConstants.FRONT_END_DATE_FORMAT));
					leaveCalculation.setFromSession(request.getLeaveApplied().getFromSession());
					leaveCalculation.setLeaveTypeId(request.getLeaveApplied().getMasterLeaveType().getId());
					leaveCalculation.setOrganizationId(candidate.getLoginEntity().getOrganization().getId());
					leaveCalculation.setToDate(HRMSDateUtil.parse(request.getLeaveApplied().getToDate(),
							IHRMSConstants.FRONT_END_DATE_FORMAT));
					leaveCalculation.setToSession(request.getLeaveApplied().getToSession());
					/*
					 * weekly off org, branch, div wise
					 */

					List<OrganizationWeekoff> weekoffList = organizationWeekoffDAO.getWeekoffByOrgBranchDivDeptId(
							candidate.getLoginEntity().getOrganization().getId(),
							candidate.getCandidateProfessionalDetail().getDivision().getId(),
							candidate.getCandidateProfessionalDetail().getBranch().getId(),
							candidate.getCandidateProfessionalDetail().getDepartment().getId());

					int workingdays = HRMSHelper.getWorkingDays(leaveCalculation, weekoffList);

					/*
					 * weekly off org, branch, div wise
					 */

					List<OrganizationHoliday> holidayList = orgHolidayDAO.getHolidayListByOrgBranchDivIdYear(
							candidate.getLoginEntity().getOrganization().getId(),
							candidate.getCandidateProfessionalDetail().getDivision().getId(),
							candidate.getCandidateProfessionalDetail().getBranch().getId(),
							new Long(HRMSDateUtil.getCurrentYear()));

					int holidayDays = HRMSHelper.getHolidays(leaveCalculation, holidayList, weekoffList);

					logger.info("holiday days " + holidayDays);
					logger.info("working days " + workingdays);

					if (workingdays == 0) {
						logger.info("grant leave apply allow : working days == 0 ");
						grantLeaveApplyAllowedStatus = true;
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						Date date1 = sdf.parse(request.getLeaveApplied().getFromDate());
						if (date1.compareTo(new Date()) > 0) {
							throw new HRMSException(IHRMSConstants.FUTURE_DATE_LEAVE_RESTRICT_CODE,
									IHRMSConstants.FUTURE_DATE_LEAVE_RESTRICT_MESSAGE);
						}
					} else if (workingdays > 0) {
						if (workingdays <= holidayDays) {
							logger.info("grant leave apply allow : workingdays <= holidayDays ");
							grantLeaveApplyAllowedStatus = true;
							SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
							Date date1 = sdf.parse(request.getLeaveApplied().getFromDate());
							if (date1.compareTo(new Date()) > 0) {
								throw new HRMSException(IHRMSConstants.FUTURE_DATE_LEAVE_RESTRICT_CODE,
										IHRMSConstants.FUTURE_DATE_LEAVE_RESTRICT_MESSAGE);
							}
						}
					}

					if (!grantLeaveApplyAllowedStatus) {
						throw new HRMSException(IHRMSConstants.GRANT_LEAVE_NOT_ALLOWED_CODE,
								IHRMSConstants.GRANT_LEAVE_NOT_ALLOWED_MESSAGE);
					}
				}
				/*
				 * upto this modified by ssw on 03jan2018
				 */

				/*
				 * condition : employee apply Leave grant
				 */
				if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_APPLY)
						&& grantLeaveApplyAllowedStatus) {
					employeeGrantLeaveDetailEntity = grantApplyLeaveAction(request, employeeGrantLeaveDetailEntity,
							masterLeaveTypeEntity, employeeEntity);
				}
				/*
				 * condition : employee cancel leave grant
				 */
				else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_CANCEL)) {
					employeeGrantLeaveDetailEntity = grantCancelLeaveAction(request, employeeGrantLeaveDetailEntity);
				}
				/*
				 * condition : employee withdraw leave grant
				 */
				else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_WITHDRAW)) {
					employeeGrantLeaveDetailEntity = grantWithdrawLeaveAction(request, employeeGrantLeaveDetailEntity);
				}
				/*
				 * condition : manager approve grant leave
				 */
				else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_APPROVE)) {
					employeeGrantLeaveDetailEntity = grantApproveLeaveAction(request, employeeGrantLeaveDetailEntity,
							employeeEntity);
				}
				/*
				 * condition : manager reject grant leave
				 */
				else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_REJECT)) {
					employeeGrantLeaveDetailEntity = grantRejectLeaveAction(request, employeeGrantLeaveDetailEntity);
				}
				/*
				 * condition : manager approve withdrawal of grant leave
				 */
				else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_WITHDRAW_APPROVE)) {
					employeeGrantLeaveDetailEntity = grantWDApproveLeaveAction(request, employeeGrantLeaveDetailEntity,
							employeeEntity);
				}
				/*
				 * condition : manager reject withdrawal of grant leave
				 */
				else if (request.getActionPerformed().equalsIgnoreCase(IHRMSConstants.LeaveAction_WITHDRAW_REJECT)) {
					employeeGrantLeaveDetailEntity = grantWDRejectLeaveAction(request, employeeGrantLeaveDetailEntity);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			HRMSListResponseObject response = new HRMSListResponseObject();
			List<Object> listResponse = new ArrayList<Object>();
			listResponse.add(HRMSEntityToModelMapper.convertToCandidateLeaveGrantModel(employeeGrantLeaveDetailEntity));
			response.setListResponse(listResponse);
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.LeaveActionSuccessMessage);

			return HRMSHelper.createJsonString(response);

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	private EmployeeGrantLeaveDetail grantWDRejectLeaveAction(VOLeaveGrantRequest request,
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity)
			throws HRMSException, FileNotFoundException, IOException {
		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity.getLeaveStatus()
				.equalsIgnoreCase(IHRMSConstants.LeaveStatus_WD_PENDING)) {
			employeeGrantLeaveDetailEntity.setApproverActionDateWithdrawn(new Date());
			employeeGrantLeaveDetailEntity
					.setApproverCommentOnWithdrawn(request.getLeaveApplied().getApproverCommentOnWithdrawn());
			employeeGrantLeaveDetailEntity.setUpdatedBy(request.getUpdatedBy());
			employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
			employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_WD_REJECTED);
			employeeGrantLeaveDetailEntity.setApproverActionDateWithdrawn(new Date());
			employeeGrantLeaveDetailEntity = employeeGrantLeaveDAO.save(employeeGrantLeaveDetailEntity);

			/**
			 * Email Sender
			 */
			String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
			String ccEmailId = employeeGrantLeaveDetailEntity.getCc();
			Map<String, String> placeHolderMapping = HRMSRequestTranslator
					.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
			placeHolderMapping.put("{rootIp}", baseURL);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_WithdrawLeaveRejected);

			/*
			 * EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContent,
			 * IHRMSConstants.MailSubject_GrantLeaveWithdrawRejected);
			 */
			emailsender.toPersistEmail(employeeEmailId, ccEmailId, mailContent,
					IHRMSConstants.MailSubject_GrantLeaveWithdrawRejected,
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
							.getDivision().getId(),
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization()
							.getId());
		} else {
			throw new HRMSException(IHRMSConstants.LeaveActionErrorCode, IHRMSConstants.InvalidLeaveActionMessage);
		}
		return employeeGrantLeaveDetailEntity;
	}

	private EmployeeGrantLeaveDetail grantWDApproveLeaveAction(VOLeaveGrantRequest request,
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity, Employee employeeEntity)
			throws HRMSException, FileNotFoundException, IOException {
		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity.getLeaveStatus()
				.equalsIgnoreCase(IHRMSConstants.LeaveStatus_WD_PENDING)) {
			Set<EmployeeLeaveDetail> empLeaveDetailsList = employeeEntity.getEmployeeLeaveDetails();
			for (EmployeeLeaveDetail employeeLeaveDetail : empLeaveDetailsList) {
				if (employeeLeaveDetail.getMasterLeaveType().getId() == employeeGrantLeaveDetailEntity
						.getMasterLeaveType().getId() && employeeLeaveDetail.getYear() == HRMSHelper.getCurrentYear()) {
					float leaveAvailable = employeeLeaveDetail.getLeaveAvailable();
					float grantLeaveApplied = employeeGrantLeaveDetailEntity.getNoOfDays();
					float updatedleaveAvailable = leaveAvailable - grantLeaveApplied;
					employeeLeaveDetail.setLeaveAvailable(updatedleaveAvailable);
					employeeLeaveDetailsDAO.save(employeeLeaveDetail);

					employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_WD_APPROVED);
					employeeGrantLeaveDetailEntity.setDateOfApproverAction(new Date());
					employeeGrantLeaveDetailEntity.setUpdatedBy(request.getUpdatedBy());
					employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
					employeeGrantLeaveDetailEntity.setApproverActionDateWithdrawn(new Date());
					employeeGrantLeaveDetailEntity = employeeGrantLeaveDAO.save(employeeGrantLeaveDetailEntity);

					/**
					 * Email Sender
					 */
					String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
					String ccEmailId = employeeGrantLeaveDetailEntity.getCc();
					Map<String, String> placeHolderMapping = HRMSRequestTranslator
							.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
					placeHolderMapping.put("{rootIp}", baseURL);
					placeHolderMapping.put("{websiteURL}", baseURL);
					String mailContent = HRMSHelper.replaceString(placeHolderMapping,
							IHRMSEmailTemplateConstants.Template_LeaveWithdrawApproved);

					/*
					 * EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContent,
					 * IHRMSConstants.MailSubject_GrantLeaveWithdrawApproved);
					 */

					emailsender.toPersistEmail(employeeEmailId, ccEmailId, mailContent,
							IHRMSConstants.MailSubject_GrantLeaveWithdrawApproved,
							employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
									.getDivision().getId(),
							employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity()
									.getOrganization().getId());
					break;
				}
			}
		} else {
			throw new HRMSException(IHRMSConstants.LeaveActionErrorCode, IHRMSConstants.InvalidLeaveActionMessage);
		}
		return employeeGrantLeaveDetailEntity;
	}

	private EmployeeGrantLeaveDetail grantRejectLeaveAction(VOLeaveGrantRequest request,
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity)
			throws HRMSException, FileNotFoundException, IOException {
		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity.getLeaveStatus()
				.equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
			employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_REJECT);
			employeeGrantLeaveDetailEntity.setDateOfApproverAction(new Date());
			employeeGrantLeaveDetailEntity.setUpdatedBy(request.getUpdatedBy());
			employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
			employeeGrantLeaveDetailEntity.setDateOfApproverAction(new Date());
			employeeGrantLeaveDetailEntity = employeeGrantLeaveDAO.save(employeeGrantLeaveDetailEntity);

			/**
			 * Email Sender
			 */
			String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
			String ccEmailId = employeeGrantLeaveDetailEntity.getCc();
			Map<String, String> placeHolderMapping = HRMSRequestTranslator
					.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
			placeHolderMapping.put("{rootIp}", baseURL);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_LeaveReject);

			// EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContent,
			// IHRMSConstants.MailSubject_LeaveRejected);
			emailsender.toPersistEmail(employeeEmailId, ccEmailId, mailContent,
					IHRMSConstants.MailSubject_LeaveRejected,
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
							.getDivision().getId(),
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization()
							.getId());
		} else {
			throw new HRMSException(IHRMSConstants.LeaveActionErrorCode, IHRMSConstants.LeaveNotInPendingErrorMessage);
		}
		return employeeGrantLeaveDetailEntity;
	}

	private EmployeeGrantLeaveDetail grantApproveLeaveAction(VOLeaveGrantRequest request,
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity, Employee employeeEntity)
			throws HRMSException, FileNotFoundException, IOException {
		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity.getLeaveStatus()
				.equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
			/*
			 * next variable is flag. It is used to check whether entry of leave type
			 * applied for comp off is present in EmployeeLeaveDetails. If not, then insert
			 * into it.
			 */
			boolean isCompOffAppliedLeaveTypePresent = false;
			// commented by ssw on 30Aug2018
			// to solve compoff issue

			// Set<EmployeeLeaveDetail> empLeaveDetailsList =
			// employeeEntity.getEmployeeLeaveDetails();
			// for (EmployeeLeaveDetail employeeLeaveDetail : empLeaveDetailsList) {
			// if (employeeLeaveDetail.getMasterLeaveType().getId() ==
			// employeeGrantLeaveDetailEntity
			// .getMasterLeaveType().getId() && employeeLeaveDetail.getYear() ==
			// HRMSHelper.getCurrentYear()) {
			// isCompOffAppliedLeaveTypePresent = true;
			// }
			// }
			if (!isCompOffAppliedLeaveTypePresent) {
				addToEmployeeLeaveDetailInGrant(employeeGrantLeaveDetailEntity.getEmployee().getId(),
						employeeGrantLeaveDetailEntity.getMasterLeaveType().getId(), HRMSHelper.getCurrentYear(),
						employeeGrantLeaveDetailEntity.getNoOfDays(),
						employeeGrantLeaveDetailEntity.getMasterLeaveType(),
						employeeGrantLeaveDetailEntity.getEmployee());
			}
			Employee employeeEntityUpdated = employeeDAO.findById(request.getLeaveApplied().getEmployee().getId())
					.get();
			// Set<EmployeeLeaveDetail> empLeaveDetailsUpdatedList =
			// employeeEntityUpdated.getEmployeeLeaveDetails();

			// next modified by SSW on 05 Dec 2018 for : fetching leave details yearwise
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			// upto this added and next DAO call modified, year parameter added
			List<EmployeeLeaveDetail> empLeaveDetailsUpdatedList = employeeLeaveDetailsDAO
					.findLeaveAvailableByEmployeeAndLeaveType(employeeEntity.getId(),
							employeeGrantLeaveDetailEntity.getMasterLeaveType().getId(), IHRMSConstants.isActive, year);
			for (EmployeeLeaveDetail employeeLeaveDetail : empLeaveDetailsUpdatedList) {
				if (employeeLeaveDetail.getMasterLeaveType().getId() == employeeGrantLeaveDetailEntity
						.getMasterLeaveType().getId() && employeeLeaveDetail.getYear() == HRMSHelper.getCurrentYear()) {
					float leaveAvailable = employeeLeaveDetail.getLeaveAvailable();
					float grantLeaveApplied = employeeGrantLeaveDetailEntity.getNoOfDays();
					float updatedleaveAvailable = leaveAvailable + grantLeaveApplied;
					// next 2 lines commented by SSW on 30Aug2018
					// issue : extra compoff added for new employee
					// employeeLeaveDetail.setLeaveAvailable(updatedleaveAvailable);
					// employeeLeaveDetailsDAO.save(employeeLeaveDetail);

					employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_APPROVED);
					employeeGrantLeaveDetailEntity.setDateOfApproverAction(new Date());
					employeeGrantLeaveDetailEntity.setUpdatedBy(request.getUpdatedBy());
					employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
					employeeGrantLeaveDetailEntity.setDateOfApproverAction(new Date());
					employeeGrantLeaveDetailEntity = employeeGrantLeaveDAO.save(employeeGrantLeaveDetailEntity);

					/**
					 * Email Sender
					 */
					String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
					String ccEmailId = employeeGrantLeaveDetailEntity.getCc();
					Map<String, String> placeHolderMapping = HRMSRequestTranslator
							.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
					placeHolderMapping.put("{rootIp}", baseURL);
					placeHolderMapping.put("{websiteURL}", baseURL);
					String mailContent = HRMSHelper.replaceString(placeHolderMapping,
							IHRMSEmailTemplateConstants.Template_LeaveApproved);
					/*
					 * EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContent,
					 * IHRMSConstants.MailSubject_GrantLeaveApproved);
					 */
					emailsender.toPersistEmail(employeeEmailId, ccEmailId, mailContent,
							IHRMSConstants.MailSubject_GrantLeaveApproved,
							employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
									.getDivision().getId(),
							employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity()
									.getOrganization().getId());
					/*
					 * add employee grant leave details in credit leave
					 */
					/*
					 * when comp off is getting approved, we need not to save to_date(expiry date)
					 * the to_date will be calculated from configuration data from org and div wise.
					 */
					EmployeeCreditLeaveDetail creditLeaveDetail = new EmployeeCreditLeaveDetail();
					creditLeaveDetail.setCreatedBy(employeeEntityUpdated.getId().toString());
					creditLeaveDetail.setCreatedDate(new Date());
					creditLeaveDetail.setCreditedBy(IHRMSConstants.GRANT_LEAVE_CREDITED_BY);
					creditLeaveDetail.setEmployee(employeeEntityUpdated);
					creditLeaveDetail.setFromDate(employeeGrantLeaveDetailEntity.getFromDate());
					creditLeaveDetail.setIsActive(IHRMSConstants.isActive);
					creditLeaveDetail.setMasterLeaveType(employeeGrantLeaveDetailEntity.getMasterLeaveType());
					creditLeaveDetail.setNoOfDays(employeeGrantLeaveDetailEntity.getNoOfDays());
					creditLeaveDetail.setPostedDate(new Date());
					creditLeaveDetail.setLeaveAvailable(employeeGrantLeaveDetailEntity.getNoOfDays());
					/** to_date set for credit comp off */
					calendar.setTime(employeeGrantLeaveDetailEntity.getFromDate());
					calendar.add(Calendar.DAY_OF_MONTH, 90);
					Date toDate = calendar.getTime();
					creditLeaveDetail.setToDate(toDate);
					creditLeaveDetail.setOrgId(employeeEntityUpdated.getOrgId());
					creditLeaveDAO.save(creditLeaveDetail);
					break;

				}
			}
		} else {
			throw new HRMSException(IHRMSConstants.LeaveActionErrorCode, IHRMSConstants.LeaveNotInPendingErrorMessage);
		}
		return employeeGrantLeaveDetailEntity;
	}

	private EmployeeGrantLeaveDetail grantWithdrawLeaveAction(VOLeaveGrantRequest request,
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity)
			throws HRMSException, FileNotFoundException, IOException {
		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity.getLeaveStatus()
				.equalsIgnoreCase(IHRMSConstants.LeaveStatus_APPROVED)) {
			employeeGrantLeaveDetailEntity.setUpdatedBy(request.getUpdatedBy());
			employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
			employeeGrantLeaveDetailEntity.setReasonForWithdrawn(request.getLeaveApplied().getReasonForWithdrawn());
			;
			employeeGrantLeaveDetailEntity.setDateOfWithdrawn(new Date());
			employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_WD_PENDING);
			employeeGrantLeaveDetailEntity.setDateOfWithdrawn(new Date());
			employeeGrantLeaveDetailEntity = employeeGrantLeaveDAO.save(employeeGrantLeaveDetailEntity);

			/**
			 * Email Sender
			 */
			String managerEmailId = employeeGrantLeaveDetailEntity.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getOfficialEmailId();
			String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
			String ccEmailId = employeeGrantLeaveDetailEntity.getCc();

			Map<String, String> placeHolderMapping = HRMSRequestTranslator
					.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
			placeHolderMapping.put("{rootIp}", baseURL);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String mailContentForManager = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_WithdrawGrantLeaveRequestToManager);
			String mailContentForEmployee = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_WithdrawLeaveConfirmationToEmployee);

			/*
			 * EmailSender.toSendEmail(managerEmailId, null, mailContentForManager,
			 * IHRMSConstants.MailSubject_GrantLeaveWithdrawRequest);
			 */
			emailsender.toPersistEmail(managerEmailId, null, mailContentForManager,
					IHRMSConstants.MailSubject_GrantLeaveWithdrawRequest,
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
							.getDivision().getId(),
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization()
							.getId());
			/*
			 * EmailSender.toSendEmail(employeeEmailId, ccEmailId, mailContentForEmployee,
			 * IHRMSConstants.MailSubject_GrantLeaveWithdrawRequest);
			 */
			emailsender.toPersistEmail(employeeEmailId, ccEmailId, mailContentForEmployee,
					IHRMSConstants.MailSubject_LeaveWithdrawRequest,
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
							.getDivision().getId(),
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization()
							.getId());
		} else {
			throw new HRMSException(IHRMSConstants.CannotWithdrawLeaveCode, IHRMSConstants.CannotWithdrawLeaveMessage);
		}
		return employeeGrantLeaveDetailEntity;
	}

	private EmployeeGrantLeaveDetail grantCancelLeaveAction(VOLeaveGrantRequest request,
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity)
			throws HRMSException, FileNotFoundException, IOException {
		/*
		 * employee grant leave cancel action checking if leave status is pending
		 */
		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity) && employeeGrantLeaveDetailEntity.getLeaveStatus()
				.equalsIgnoreCase(IHRMSConstants.LeaveStatus_PENDING)) {
			employeeGrantLeaveDetailEntity.setUpdatedBy(request.getUpdatedBy());
			employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
			employeeGrantLeaveDetailEntity.setReasonForCancel(request.getLeaveApplied().getReasonForCancel());
			employeeGrantLeaveDetailEntity.setDateOfCancel(new Date());
			employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_CANCELLED);
			employeeGrantLeaveDetailEntity.setDateOfCancel(new Date());
			employeeGrantLeaveDetailEntity = employeeGrantLeaveDAO.save(employeeGrantLeaveDetailEntity);

			/**
			 * Email Sender
			 */
			String employeeEmailId = employeeGrantLeaveDetailEntity.getEmployee().getOfficialEmailId();
			String managerEmailId = employeeGrantLeaveDetailEntity.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getOfficialEmailId();
			String ccEmailId = employeeGrantLeaveDetailEntity.getCc();

			Map<String, String> placeHolderMapping = HRMSRequestTranslator
					.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
			placeHolderMapping.put("{rootIp}", baseURL);
			placeHolderMapping.put("{websiteURL}", baseURL);
			String employeeMailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_CancleLeaveForEmployee);
			String managerMailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_CancleLeaveForManager);

			/*
			 * Sending email to manager,employee,and CC
			 */
			/*
			 * EmailSender.toSendEmail(employeeEmailId, null, employeeMailContent,
			 * IHRMSConstants.MailSubject_GrantLeaveCancelled);
			 */
			emailsender.toPersistEmail(employeeEmailId, null, employeeMailContent,
					IHRMSConstants.MailSubject_GrantLeaveCancelled,
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
							.getDivision().getId(),
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization()
							.getId());

			/*
			 * EmailSender.toSendEmail(managerEmailId, null, managerMailContent,
			 * IHRMSConstants.MailSubject_GrantLeaveCancelled);
			 */

			emailsender.toPersistEmail(managerEmailId, null, managerMailContent,
					IHRMSConstants.MailSubject_GrantLeaveCancelled,
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
							.getDivision().getId(),
					employeeGrantLeaveDetailEntity.getEmployee().getCandidate().getLoginEntity().getOrganization()
							.getId());
			/*
			 * the code for ccEmail sender have not been implemented as comp off has no cc
			 * field
			 */

		} else {
			throw new HRMSException(IHRMSConstants.ActivityProcessedCode, IHRMSConstants.ErrorInCancellation);
		}
		return employeeGrantLeaveDetailEntity;
	}

	private EmployeeGrantLeaveDetail grantApplyLeaveAction(VOLeaveGrantRequest request,
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity, MasterLeaveType masterLeaveTypeEntity,
			Employee employeeEntity) throws HRMSException, FileNotFoundException, IOException, ParseException {
		if (!HRMSHelper.isNullOrEmpty(employeeGrantLeaveDetailEntity)
				&& !HRMSHelper.isNullOrEmpty(masterLeaveTypeEntity)) {
			/* update */
			// employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntity);
			MasterLeaveType masterLeaveTypeEntityUpdate = masterLeaveTypeDAO.findById(masterLeaveTypeEntity.getId())
					.get();
			employeeGrantLeaveDetailEntity = HRMSRequestTranslator.translateToEmployeeGrantLeaveDetailEntity(
					employeeGrantLeaveDetailEntity, request.getLeaveApplied());
			employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntityUpdate);
			// resultMesage = IHRMSConstants.updatedsuccessMessage;
		} else {
			/* insert */
//			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//			Date fromDate = sdf.parse(request.getLeaveApplied().getFromDate());
//			Date toDate = sdf.parse(request.getLeaveApplied().getToDate());
//			 long dateDiff = (toDate.getTime() - fromDate.getTime())/ 1000 / 60 / 60 / 24;
			Date today = new Date();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date fromDate = dateFormat.parse(request.getLeaveApplied().getFromDate());

			today = removeTimeComponent(today);
			fromDate = removeTimeComponent(fromDate);
//
//	            // Calculate the difference in milliseconds
			long differenceInMilliseconds = today.getTime() - fromDate.getTime();
//
//	            // Convert the difference to days
			long differenceInDays = differenceInMilliseconds / (1000 * 60 * 60 * 24);

			if (differenceInDays > 30) {
				throw new HRMSException("Cannot Apply leave after 30 days after working day");
			}

			employeeGrantLeaveDetailEntity = new EmployeeGrantLeaveDetail();
			if (!HRMSHelper.isNullOrEmpty(employeeEntity) && !HRMSHelper.isNullOrEmpty(masterLeaveTypeEntity)) {
				employeeGrantLeaveDetailEntity.setEmployee(employeeEntity);
				MasterLeaveType masterLeaveTypeEntityUpdate = masterLeaveTypeDAO.findById(masterLeaveTypeEntity.getId())
						.get();
				// employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntity);
				employeeGrantLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntityUpdate);
				employeeGrantLeaveDetailEntity = HRMSRequestTranslator.translateToEmployeeGrantLeaveDetailEntity(
						employeeGrantLeaveDetailEntity, request.getLeaveApplied());
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			// resultMesage = IHRMSConstants.addedsuccessMessage;
		}
		employeeGrantLeaveDetailEntity.setLeaveStatus(IHRMSConstants.LeaveStatus_PENDING);
		employeeGrantLeaveDetailEntity.setDateOfApplied(new Date());
		employeeGrantLeaveDetailEntity = employeeGrantLeaveDAO.save(employeeGrantLeaveDetailEntity);
		/*
		 * Email sender
		 */
		String managerEmailId = employeeEntity.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
		String ccEmailId = request.getLeaveApplied().getCc();

		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapForLeaveGrant(employeeGrantLeaveDetailEntity);
		placeHolderMapping.put("{rootIp}", baseURL);
		placeHolderMapping.put("{websiteURL}", baseURL);

		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_GrantLeaveApply);

		/*
		 * Sending email to Recipient
		 */

		emailsender.toPersistEmail(managerEmailId, null, mailContent, IHRMSConstants.MailSubject_GrantLeaveApplication,
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());
		// EmailSender.toSendEmail(managerEmailId, null, mailContent,
		// IHRMSConstants.MailSubject_GrantLeaveApplication);

		/*
		 * Sending email to CC As CC is null, and is considered at parameter
		 * recepientMailID on EmailSender.toSendMail this results in null pointer method
		 * hence below is commented on 25 dec 2017
		 */
		/*
		 * String mailContent_cc = HRMSHelper.replaceString(placeHolderMapping,
		 * IHRMSEmailTemplateConstants.Template_LeaveApply_CC);
		 * emailsender.toPersistEmail(ccEmailId, null, mailContent_cc,
		 * IHRMSConstants.MailSubject_GrantLeaveApplication,
		 * employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().
		 * getId(),
		 * employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());
		 * EmailSender.toSendEmail(ccEmailId, null, mailContent_cc,
		 * IHRMSConstants.MailSubject_GrantLeaveApplication);
		 */

		return employeeGrantLeaveDetailEntity;
	}

	private static Date removeTimeComponent(Date date) {
		// Remove the time component from the date
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		try {
			return dateFormat.parse(dateFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}

	private void addToEmployeeLeaveDetailInGrant(long employeeId, long masterLeaveTypeId, int year, float leave,
			MasterLeaveType masterLeaveType, Employee employee) {
		EmployeeLeaveDetail employeeLeaveDetailEntity = employeeLeaveDetailsDAO.findEmployeeLeaveByEIDYEAR(employeeId,
				masterLeaveTypeId, year);

		if (!HRMSHelper.isNullOrEmpty(employeeLeaveDetailEntity)) {
			float leaveAvailable = employeeLeaveDetailEntity.getLeaveAvailable();
			float leaveEarned = employeeLeaveDetailEntity.getLeaveEarned();
			employeeLeaveDetailEntity.setLeaveAvailable(leaveAvailable + leave);
			employeeLeaveDetailEntity.setLeaveEarned(leaveEarned + leave);
			employeeLeaveDetailsDAO.save(employeeLeaveDetailEntity);
		} else {
			EmployeeLeaveDetail employeeLeaveDetail = new EmployeeLeaveDetail();
			employeeLeaveDetail.setLeaveAvailable(leave);
			employeeLeaveDetail.setLeaveEarned(leave);
			employeeLeaveDetail.setMasterLeaveType(masterLeaveType);
			employeeLeaveDetail.setEmployee(employee);
			employeeLeaveDetail.setYear(year);
			employeeLeaveDetailsDAO.save(employeeLeaveDetail);
		}
	}

	@RequestMapping(value = "leaveActionByMail/{leaveAction}/{leaveId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String grantLeaveActionByMail(@RequestBody @PathVariable("leaveAction") String leaveAction,
			@PathVariable("leaveId") String leaveId) throws IOException, URISyntaxException {
		try {
			if (!HRMSHelper.isNullOrEmpty(leaveAction) && !HRMSHelper.isNullOrEmpty(leaveId)) {
				long id = Long.parseLong(HRMSHelper.decodeString(leaveId));
				VOLeaveGrantRequest leaveGrantRequest = new VOLeaveGrantRequest();
				leaveGrantRequest.setActionPerformed(HRMSHelper.decodeString(leaveAction));
				VOEmployeeGrantLeaveDetail leaveApplied = new VOEmployeeGrantLeaveDetail();
				leaveApplied.setId(id);
				leaveApplied.setRemark(IHRMSConstants.ByMailerRemark);
				EmployeeGrantLeaveDetail empGrantLeaveDetailEntity = null;
				if (id > 0) {
					empGrantLeaveDetailEntity = employeeGrantLeaveDAO.findById(id);
				}
				leaveGrantRequest.setLeaveApplied(leaveApplied);
				leaveGrantRequest.getLeaveApplied().setEmployee(new VOEmployee());
				leaveGrantRequest.getLeaveApplied().getEmployee()
						.setId(empGrantLeaveDetailEntity.getEmployee().getId());
				leaveGrantRequest.getLeaveApplied().setMasterLeaveType(new VOMasterLeaveType());
				leaveGrantRequest.getLeaveApplied().getMasterLeaveType()
						.setId(empGrantLeaveDetailEntity.getMasterLeaveType().getId());
				String response = leaveGrantAction(leaveGrantRequest);
				HRMSListResponseObject res = HRMSHelper.getObjectMapper().readValue(response,
						HRMSListResponseObject.class);

				if (res.getResponseCode() == 0) {
					List<Object> listObject = res.getListResponse();
					switch (HRMSHelper.decodeString(leaveAction)) {
					case "APPROVE_LEAVE":
						response = IHRMSEmailTemplateConstants.GRANT_LEAVE_RESPONSE_VIA_EMAIL_APPROVED;
						break;
					case "REJECT_LEAVE":
						response = IHRMSEmailTemplateConstants.GRANT_LEAVE_RESPONSE_VIA_EMAIL_REJECTED;
						break;
					case "WD_APPROVE":
						response = IHRMSEmailTemplateConstants.GRANT_LEAVE_RESPONSE_VIA_EMAIL_WD_APPROVED;
						break;
					case "WD_REJECT":
						response = IHRMSEmailTemplateConstants.GRANT_LEAVE_RESPONSE_VIA_EMAIL_WD_REJECTED;
						break;
					}
				} else {
					response = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "<body onload=\"myFunction()\">\r\n" + "\r\n"
							+ "<script>\r\n" + "function myFunction() {\r\n" + "    alert('" + res.getResponseMessage()
							+ "');\r\n" + "}\r\n" + "</script>\r\n" + "\r\n" + "</body>\r\n" + "</html>";
				}
				return response;
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
}
