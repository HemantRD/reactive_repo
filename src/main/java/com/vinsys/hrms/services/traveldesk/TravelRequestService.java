package com.vinsys.hrms.services.traveldesk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSAccommodationRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRecurringRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRequestPassengerDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCancelTravelRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSColHeaderDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMapCabDriverVehicleDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterDriverDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterModeOfTravelDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterVehicleDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTicketRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTicketRequestPassengerDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTravelDeskDocumentDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTravelRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTraveldeskApproverDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTraveldeskCommentDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.traveldesk.VOAccommodationRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOApproverAassignmentRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOCabRecurringRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOCabRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOCabRequestPassenger;
import com.vinsys.hrms.datamodel.traveldesk.VOMapCabDriverVehicle;
import com.vinsys.hrms.datamodel.traveldesk.VOMarkAsCompleteChildRequest;
import com.vinsys.hrms.datamodel.traveldesk.VORequestParamForTravelList;
import com.vinsys.hrms.datamodel.traveldesk.VOTicketRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelDeskDocument;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelRequestDispaly;
import com.vinsys.hrms.datamodel.traveldesk.VOTraveldeskComment;
import com.vinsys.hrms.datamodel.traveldesk.VOUpdateCabRequest;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.entity.traveldesk.AccommodationGuest;
import com.vinsys.hrms.entity.traveldesk.AccommodationRequest;
import com.vinsys.hrms.entity.traveldesk.BPMDetails;
import com.vinsys.hrms.entity.traveldesk.CabRecurringRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequestPassenger;
import com.vinsys.hrms.entity.traveldesk.CancelTravelRequest;
import com.vinsys.hrms.entity.traveldesk.MapCabDriverVehicle;
import com.vinsys.hrms.entity.traveldesk.MasterDriver;
import com.vinsys.hrms.entity.traveldesk.MasterTraveldeskApprover;
import com.vinsys.hrms.entity.traveldesk.MasterVehicle;
import com.vinsys.hrms.entity.traveldesk.TicketRequest;
import com.vinsys.hrms.entity.traveldesk.TicketRequestPassenger;
import com.vinsys.hrms.entity.traveldesk.TravelRequest;
import com.vinsys.hrms.entity.traveldesk.TraveldeskComment;
import com.vinsys.hrms.entity.traveldesk.TraveldeskDocument;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSFileuploadUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/travelRequest")

//@PropertySource(value="${HRMSCONFIG}")
public class TravelRequestService {

	private static final Logger logger = LoggerFactory.getLogger(TravelRequestService.class);

	@Value("${base.url}")
	private String baseURL;

	@Value("${rootDirectory}")
	private String rootDirectory;
	@Autowired
	IHRMSEmployeeReportingManager reportingManagerDAO;
	
	@Value("${TD_UPCOMING_REQ_NO_OF_DAYS}")
	private int upcomingReqNoOfDays;

	@Autowired
	EmailSender emailSenderDAO;
	@Autowired
	TravelDeskServiceHelper travelDeskServiceHelper;
	@Autowired
	IHRMSTravelRequestDAO travelRequestDAO;
	@Autowired
	IHRMSTravelDeskDocumentDAO travelDeskDocumentDAO;
	@Autowired
	IHRMSMasterDepartmentDAO masterDepartmentDAO;
	@Autowired
	IHRMSTraveldeskCommentDAO traveldeskCommentDAO;
	@Autowired
	IHRMSMasterModeOfTravelDAO modeOfTravelDAO;
	@Autowired
	IHRMSTicketRequestPassengerDAO ticketRequestPassengerDAO;
	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSTraveldeskApproverDAO travelDeskApproverDAO;
	@Autowired
	IHRMSTicketRequestDAO ticketRequestDAO;
	@Autowired
	IHRMSAccommodationRequestDAO accommodationRequestDAO;
	@Autowired
	IHRMSTraveldeskCommentDAO commentDAO;
	@Autowired
	IHRMSCabRequestDAO cabRequestDAO;
	@Autowired
	IHRMSCabRequestPassengerDAO cabRequestPassengerDAO;
	@Autowired
	IHRMSMasterDriverDAO driverDAO;
	@Autowired
	IHRMSMasterVehicleDAO vehicleDAO;
	@Autowired
	IHRMSCabRecurringRequestDAO cabRecurringRequestDAO;
	@Autowired
	IHRMSMapCabDriverVehicleDAO mapCabDriverVehicleDAO;
	@Autowired
	IHRMSCancelTravelRequestDAO cancelTravelRequestDAO;
	@Autowired
	IHRMSColHeaderDAO colHeaderDAO;
	@Autowired
	BPMRequestService bpm;
	@Autowired
	IHRMSTraveldeskApproverDAO masterApproverDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	// @Transactional(rollbackFor=Exception.class)
	public String saveRequest(@RequestBody VOTravelRequest travelRequest) {

		try {
			HRMSListResponseObject response = new HRMSListResponseObject();
			checkSufficientData(travelRequest);
			logger.info(" :: Inside Saving Travel Request :: ");
			if (travelRequest != null) {
				boolean isForUpdate = false;
				String responseMessage = "";

				if (travelRequest.getId() > 0) {
					isForUpdate = true;
					responseMessage = IHRMSConstants.TRAVEL_REQUEST_UPDATED_SUCCESFULLY;
				} else {
					responseMessage = IHRMSConstants.TRAVEL_REQUEST_SAVED_SUCCESFULLY;
				}

				if (!HRMSHelper.isLongZero(travelRequest.getWorkOrderNo())) {

					String bpmDetailsString = bpm.getBPMDetails(travelRequest.getWorkOrderNo());
					HRMSListResponseObject bpmDetails = HRMSHelper.getObjectMapper().readValue(bpmDetailsString,
							HRMSListResponseObject.class);
					if (!HRMSHelper.isNullOrEmpty(bpmDetails)) {
						List<Object> object = bpmDetails.getListResponse();
						if (!HRMSHelper.isNullOrEmpty(object)) {
							BPMDetails bpmInfo = HRMSHelper.getObjectMapper()
									.readValue(HRMSHelper.createJsonString(object.get(0)), BPMDetails.class);

							if (!HRMSHelper.isNullOrEmpty(bpmInfo)) {
								travelRequest.setClientName(bpmInfo.getClientName());
								travelRequest.setBdName(bpmInfo.getBdName());
								travelRequest.setBusinessUnit(bpmInfo.getBusinessUnit());
							}
						}
					}

				}

				long organizationId = 0;
				long divisionId = 0;
				Employee requestor = null;

				/*
				 * if(isForUpdate) { //for update TravelRequest newTravelRequest =
				 * travelRequestDAO.findById(travelRequest.getId()); requestor =
				 * newTravelRequest.getEmployeeId();
				 * 
				 * }
				 */

				if (!isForUpdate) {
					if (travelRequest.getEmployeeId() != null) {
						requestor = employeeDAO.findActiveEmployeeById(travelRequest.getEmployeeId().getId(),
								IHRMSConstants.isActive);
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode,
								IHRMSConstants.EmployeeDoesnotExistMessage);
					}

				} else {
					TravelRequest newTravelRequest = travelRequestDAO.findById(travelRequest.getId()).get();
					requestor = newTravelRequest.getEmployeeId();
				}

				if (requestor == null) {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode,
							IHRMSConstants.EmployeeDoesnotExistMessage);
				}

				if (!checkSufficientData(travelRequest)) {
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
				}

				TravelRequest travelRequestEntity = null;
				AccommodationRequest accommodationEntity = null;
				TicketRequest ticketRequestEntity = null;
				CabRequest cabEntity = null;

				/**
				 * This Section will persist the Actual Travel Request
				 */

				Organization organization = null;
				if (travelRequest.getOrganization() != null) {
					organization = organizationDAO.findById(travelRequest.getOrganization().getId()).get();
				}
				travelRequestEntity = travelDeskServiceHelper.toPersistTravelRequest(travelRequest, organization,
						requestor);

				VOAccommodationRequest accommodationRequestModel = travelRequest.getAccommodationRequest();
				VOTicketRequest ticketRequestModel = travelRequest.getTicketRequest();
				VOCabRequest cabRequestModel = travelRequest.getCabRequest();

				/**
				 * This Section will persist the Accommodation Request
				 */
				if (travelRequest.isBookAccommodation() && accommodationRequestModel != null) {
					accommodationEntity = travelDeskServiceHelper.toPersistAccommodationRequest(travelRequest,
							travelRequestEntity, accommodationRequestModel, organization);
				}

				/**
				 * This Section will persist the Ticket Request
				 */
				if (travelRequest.isBookTicket() && ticketRequestModel != null) {
					// ticketRequestModel.setCreatedBy(travelRequest.getCreatedBy());
					// ticketRequestModel.setUpdatedBy(travelRequest.getUpdatedBy());
					ticketRequestEntity = travelDeskServiceHelper.toPersistTicketRequest(travelRequestEntity,
							ticketRequestModel, organization);
				}

				/**
				 * This Section will persist the Cab Request
				 */

				if (travelRequest.isBookCab() && cabRequestModel != null) {
					cabEntity = travelDeskServiceHelper.toPersistCabRequest(travelRequest, travelRequestEntity,
							cabRequestModel, organization);
				}

				travelRequestEntity.setAccommodationRequest(accommodationEntity);
				travelRequestEntity.setTicketRequest(ticketRequestEntity);
				travelRequestEntity.setCabRequest(cabEntity);
				travelRequestEntity = travelRequestDAO.save(travelRequestEntity);

				if (requestor != null) {
					organizationId = organization.getId();
					divisionId = requestor.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
				}

				/**
				 * Persisting Comment For Travel Desk
				 */
				List<VOTraveldeskComment> travelDeskCommentModel = travelRequest.getTravelDeskComment();
				if (!HRMSHelper.isNullOrEmpty(travelDeskCommentModel)) {
					for (VOTraveldeskComment comment : travelDeskCommentModel) {
						if (!HRMSHelper.isNullOrEmpty(comment)) {
							if (!HRMSHelper.isNullOrEmpty(comment.getComment())) {
								TraveldeskComment commentEntity = new TraveldeskComment();
								commentEntity.setTravelRequest(travelRequestEntity);
								commentEntity.setChildType(IHRMSConstants.TD_COMMENT_CHILD_TYPE_ALL);
								commentEntity.setEmployee(requestor);
								commentEntity.setCommentator(IHRMSConstants.TRAVEL_REQUEST_COMMENTATOR_REQUESTER);
								commentEntity.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_TRAVEL_REQUEST);
								commentEntity.setComment(comment.getComment());
								commentEntity.setCreatedBy(travelRequest.getCreatedBy());
								commentEntity.setCreatedDate(new Date());
								commentEntity.setIsActive(IHRMSConstants.isActive);
								commentEntity = commentDAO.save(commentEntity);
							}
						}
					}
				}

				/**
				 * Email Sender Code
				 */

				if (!travelRequest.isTravelTypeTD()) {

					List<MasterTraveldeskApprover> masterTravelDeskApproverList = travelDeskApproverDAO
							.findTravelDeskEmployeeOrgWise(organizationId, divisionId,
									IHRMSConstants.APPROVER_TYPE_TRAVELDESK, IHRMSConstants.isActive);

					Employee travelDeskEmployeeObj = null;
					String email_traveldesk = "";

					if (masterTravelDeskApproverList != null) {

						for (MasterTraveldeskApprover mstTdApprovr : masterTravelDeskApproverList) {
							Employee emp = new Employee();
							emp = employeeDAO.findById(mstTdApprovr.getEmployee().getId()).get();
							email_traveldesk = email_traveldesk + emp.getOfficialEmailId() + ";";
						}
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.NO_TD_EMPLOYEE_FOUND);
					}

					Map<String, String> map = createMailContent(travelRequestEntity, travelDeskEmployeeObj,
							IHRMSConstants.STRING_ALL, travelDeskCommentModel);

					String mailContent = "";
					String subject = "";
					if (isForUpdate) {
						subject = IHRMSConstants.MAIL_SUBJECT_TRAVEL_REQUEST_UPDATED + " | Request ID : "
								+ travelRequestEntity.getSeqId();
						mailContent = HRMSHelper.replaceString(map,
								IHRMSEmailTemplateConstants.TEMPLATE_APPROVER_CONTENT_UPDATE);
					} else {
						subject = IHRMSConstants.MAIL_SUBJECT_TRAVEL_REQUEST + " | Request ID : "
								+ travelRequestEntity.getSeqId();
						mailContent = HRMSHelper.replaceString(map,
								IHRMSEmailTemplateConstants.TEMPLATE_APPROVER_CONTENT);
					}

					emailSenderDAO.toPersistEmail(email_traveldesk, requestor.getOfficialEmailId(), mailContent,
							subject, divisionId, organizationId, IHRMSConstants.IS_MAIL_WITH_ATTACHMENT_N, null);
				}
				travelRequestEntity = travelRequestDAO.findById(travelRequestEntity.getId()).get();

				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(responseMessage);

				List<Object> list = new ArrayList<Object>();
				VOTravelRequest travelRequestModel = HRMSEntityToModelMapper
						.convertToTravelRequestModel(travelRequestEntity);
				travelRequestModel.setTravelDeskComment(travelDeskCommentModel);
				String name = "";

				if (travelRequestModel != null && travelRequestModel.getEmployeeId() != null
						&& travelRequestModel.getEmployeeId().getCandidate() != null) {
					name = HRMSHelper
							.convertNullToEmpty(travelRequestModel.getEmployeeId().getCandidate().getFirstName())
							+ " "
							+ HRMSHelper
									.convertNullToEmpty(travelRequestModel.getEmployeeId().getCandidate().getLastName()
											+ " " + travelRequestModel.getEmployeeId().getId());
				}
				travelRequestModel.setCreatedBy(name);
				list.add(travelRequestModel);
				response.setListResponse(list);
			}

			return HRMSHelper.createJsonString(response);
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (org.hibernate.TransactionException trn) {
			trn.printStackTrace();
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

	/**
	 * to ACCEPT OR REJECT Travel request by TD SSW
	 */
	@RequestMapping(value = "acceptRejectTravelReqByTD", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String acceptRejectTravelRequestByTD(@RequestBody VOTravelRequest travelRequest) {
		TravelRequest travelRequestEntity = null;
		try {
			if (!HRMSHelper.isNullOrEmpty(travelRequest) && !HRMSHelper.isLongZero(travelRequest.getId())
					&& !HRMSHelper.isNullOrEmpty(travelRequest.getTravelStatus())) {
				travelRequestEntity = travelRequestDAO.findTravelRequestById(travelRequest.getId(),
						IHRMSConstants.isActive);

				String responseMessage = "";
				if (!HRMSHelper.isNullOrEmpty(travelRequestEntity)) {
					if (travelRequestEntity.getTravelStatus().equals(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING)
							|| travelRequestEntity.getTravelStatus().equals(IHRMSConstants.TRAVEL_REQUEST_STATUS_WIP)) {
						if (travelRequest.getTravelStatus().equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_ACCEPT)) {
							// travel request
							responseMessage = IHRMSConstants.TRAVEL_REQUEST_ACCEPTED_MESSAGE;

							travelRequestEntity.setTravelStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_WIP);
							travelRequestEntity
									.setUpdatedBy(String.valueOf(travelRequest.getLoggedInEmployee().getId()));
							travelRequestEntity.setUpdatedDate(new Date());
							travelRequestDAO.save(travelRequestEntity);
							return HRMSHelper.sendSuccessResponse(IHRMSConstants.successMessage,
									IHRMSConstants.successCode);
						} else if (travelRequest.getTravelStatus()
								.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_REJECT)) {
							responseMessage = IHRMSConstants.TRAVEL_REQUEST_REJECTED_MESSAGE;
							if (!HRMSHelper.isNullOrEmpty(travelRequest.getLoggedInEmployee())
									&& !HRMSHelper.isLongZero(travelRequest.getLoggedInEmployee().getId())
									&& !HRMSHelper.isNullOrEmpty(travelRequest.getEmployeeId())
									&& !HRMSHelper.isLongZero(travelRequest.getEmployeeId().getId())) {
								Employee employeeCommenter = employeeDAO
										.findById(travelRequest.getLoggedInEmployee().getId()).get();
								Employee employeeRequester = employeeDAO.findById(travelRequest.getEmployeeId().getId()).get();
								if (!HRMSHelper.isNullOrEmpty(employeeCommenter)
										&& !HRMSHelper.isNullOrEmpty(employeeRequester)) {
									TraveldeskComment traveldeskComment = new TraveldeskComment();
									traveldeskComment.setTravelRequest(travelRequestEntity);
									traveldeskComment.setEmployee(employeeCommenter);
									traveldeskComment
											.setCommentator(IHRMSConstants.TRAVEL_REQUEST_COMMENTATOR_TRAVELDESK);
									traveldeskComment.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_REJECT);
									traveldeskComment.setComment(travelRequest.getComment());
									traveldeskComment.setCreatedBy(String.valueOf(employeeCommenter.getId()));
									traveldeskComment.setCreatedDate(new Date());
									traveldeskComment.setIsActive(IHRMSConstants.isActive);
									traveldeskCommentDAO.save(traveldeskComment);
									travelRequestEntity.setTravelStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_REJECTED);
									travelRequestEntity
											.setUpdatedBy(String.valueOf(travelRequest.getLoggedInEmployee().getId()));
									travelRequestEntity.setUpdatedDate(new Date());
									travelRequestDAO.save(travelRequestEntity);
									// send mail

									// checking which child status is pending
									String pendingChildRequests = travelDeskServiceHelper
											.getPendingChildRequests(travelRequestEntity);

									VOTraveldeskComment voTraveldeskComment = new VOTraveldeskComment();
									voTraveldeskComment.setComment(travelRequest.getComment());

									List<VOTraveldeskComment> commentObj = null;
									if (!HRMSHelper.isNullOrEmpty(travelRequest.getComment())) {
										commentObj = new ArrayList<VOTraveldeskComment>();
										commentObj.add(voTraveldeskComment);
									}

									Map<String, String> mailBodyMap = createMailContent(travelRequestEntity,
											employeeRequester, pendingChildRequests, commentObj);
									String mailBody = HRMSHelper.replaceString(mailBodyMap,
											IHRMSEmailTemplateConstants.TEMPLATE_TD_TO_REQUESTER_ON_REJECT);

									long division = employeeRequester.getCandidate().getCandidateProfessionalDetail()
											.getDivision().getId();
									long organization = employeeRequester.getCandidate().getLoginEntity()
											.getOrganization().getId();

									emailSenderDAO.toPersistEmail(employeeRequester.getOfficialEmailId(), "", mailBody,
											IHRMSConstants.TRAVEL_REQUEST_REJECT_EMAIL_SUBJECT, division, organization,
											IHRMSConstants.IS_MAIL_WITH_ATTACHMENT_N, null);
									return HRMSHelper.sendSuccessResponse(responseMessage, IHRMSConstants.successCode);
								} else {
									throw new HRMSException(IHRMSConstants.InsufficientDataCode,
											IHRMSConstants.InsufficientDataMessage);
								}
							}
						} else {
							throw new HRMSException(IHRMSConstants.InvalidActionCode,
									IHRMSConstants.InvalidActionMessage);
						}
					} else {
						throw new HRMSException(IHRMSConstants.TRAVEL_REQUEST_ALREADY_PROCESSED_CODE,
								IHRMSConstants.TRAVEL_REQUEST_ALREADY_PROCESSED);
					}
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
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

	private boolean checkSufficientData(VOTravelRequest requestForm) throws HRMSException {
		boolean result = true;
		if (requestForm.isBookCab() && requestForm.getCabRequest() == null) {
			result = false;
		}

		if (requestForm.isBookAccommodation() && requestForm.getAccommodationRequest() == null) {
			result = false;
		}
		if (requestForm.isBookTicket() && requestForm.getTicketRequest() == null) {
			result = false;
		}

		if (requestForm.isBookCab() && requestForm.getCabRequest() != null) {
			if (requestForm.getCabRequest() == null) {
				throw new HRMSException(IHRMSConstants.PASSENGER_ERROR_CODE,
						IHRMSConstants.PASSENGER_NOT_FOUND_MESSAGE);
			}
		}

		if (requestForm.isBookCab() && requestForm.getCabRequest() != null) {
			if (requestForm.getCabRequest().getCabRequestPassengers() == null) {
				result = false;
				throw new HRMSException(IHRMSConstants.PASSENGER_ERROR_CODE,
						IHRMSConstants.PASSENGER_NOT_FOUND_MESSAGE);
			}

			if (requestForm.getCabRequest().getCabRequestPassengers() != null
					&& requestForm.getCabRequest().getCabRequestPassengers().isEmpty()) {
				result = false;
				throw new HRMSException(IHRMSConstants.PASSENGER_ERROR_CODE,
						IHRMSConstants.PASSENGER_NOT_FOUND_MESSAGE);
			}
		}

		if (requestForm.isBookAccommodation() && requestForm.getAccommodationRequest() != null) {
			if (requestForm.getAccommodationRequest().getAccommodationGuests() == null) {
				result = false;
				throw new HRMSException(IHRMSConstants.PASSENGER_ERROR_CODE,
						IHRMSConstants.PASSENGER_NOT_FOUND_MESSAGE);
			}

			if (requestForm.getAccommodationRequest().getAccommodationGuests() != null
					&& requestForm.getAccommodationRequest().getAccommodationGuests().isEmpty()) {
				result = false;
				throw new HRMSException(IHRMSConstants.PASSENGER_ERROR_CODE,
						IHRMSConstants.PASSENGER_NOT_FOUND_MESSAGE);
			}
		}

		if (requestForm.isBookTicket() && requestForm.getTicketRequest() != null) {
			if (requestForm.getTicketRequest().getTicketRequestPassengers() == null) {
				result = false;
				throw new HRMSException(IHRMSConstants.PASSENGER_ERROR_CODE,
						IHRMSConstants.PASSENGER_NOT_FOUND_MESSAGE);
			}

			if (requestForm.getTicketRequest().getTicketRequestPassengers() != null
					&& requestForm.getTicketRequest().getTicketRequestPassengers().isEmpty()) {
				result = false;
				throw new HRMSException(IHRMSConstants.PASSENGER_ERROR_CODE,
						IHRMSConstants.PASSENGER_NOT_FOUND_MESSAGE);
			}
		}

		return result;
	}

	/**
	 * 
	 * This service will return all the request done by logged in employee
	 * Pagination is applied Default page size is 10,if size is less than 0
	 */
	@RequestMapping(value = "getAllRequestByEmployee/{employeeId}/{organizationId}/{page}/{size}", method = {
			RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String getAllRequestByEmployeeId(@PathVariable("employeeId") long employeeId, @PathVariable("page") int page,
			@PathVariable("size") int size, @PathVariable("organizationId") long organizationId) {

		try {
			if (employeeId != 0) {

				if (size <= 0) {
					size = IHRMSConstants.DefaultPageSize;
				}

				List<TravelRequest> travelRequestList = travelRequestDAO.findTravelRequestByEmployeeId(employeeId,
						IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING, IHRMSConstants.TRAVEL_REQUEST_STATUS_WIP,
						IHRMSConstants.TRAVEL_REQUEST_STATUS_REJECTED, IHRMSConstants.isActive, organizationId/*,
						new PageRequest(page, size)*/);

				if (travelRequestList != null && !travelRequestList.isEmpty()) {

					HRMSListResponseObject response = new HRMSListResponseObject();
					List<Object> list = new ArrayList<Object>();
					for (TravelRequest travelRequest : travelRequestList) {

						if (travelRequest != null) {
							VOTravelRequest travelRequestModel = HRMSEntityToModelMapper
									.convertToTravelRequestModel(travelRequest);

							String name = "";
							Employee employee = employeeDAO.findById(travelRequest.getEmployeeId().getId()).get();

							if (travelRequestModel != null && travelRequestModel.getTicketRequest() != null) {

								// Getting TravelRequest Headers
								// Map<String,List<Object>> header = getTravelRequestHeader(travelRequestModel);
								// travelRequestModel.setHeaders(header);

								List<TraveldeskDocument> ticketDocumentList = travelDeskDocumentDAO
										.getDocumentsUsingparentId(travelRequest.getId(),
												travelRequestModel.getTicketRequest().getId(),
												IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET,
												IHRMSConstants.isActive);
								List<VOTravelDeskDocument> ticketDocument = getDocumentModelForChild(ticketDocumentList,
										travelRequest, employee, IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
								travelRequestModel.getTicketRequest().setTicketDocument(ticketDocument);
							}

							/**
							 * Getting Document For Accommodation Request if available
							 */
							if (travelRequestModel != null && travelRequestModel.getAccommodationRequest() != null) {

								List<TraveldeskDocument> accommodationDocuments = travelDeskDocumentDAO
										.getDocumentsUsingparentId(travelRequest.getId(),
												travelRequestModel.getAccommodationRequest().getId(),
												IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION,
												IHRMSConstants.isActive);
								List<VOTravelDeskDocument> accommodationDocument = getDocumentModelForChild(
										accommodationDocuments, travelRequest, employee,
										IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
								travelRequestModel.getAccommodationRequest()
										.setAccommodationDocument(accommodationDocument);
							}

							if (employee != null && employee.getCandidate() != null) {

								name = HRMSHelper.convertNullToEmpty(employee.getCandidate().getFirstName()) + " "
								// + HRMSHelper.convertNullToEmpty(employee.getCandidate().getMiddleName()) + "
								// "
										+ HRMSHelper.convertNullToEmpty(
												employee.getCandidate().getLastName() + " - " + employee.getId());
								logger.info("Name :: " + name);
							}
							travelRequestModel.setCreatedBy(name);

							/*
							 * finding cab details
							 */

							if (!HRMSHelper.isNullOrEmpty(travelRequestModel)
									&& !HRMSHelper.isNullOrEmpty(travelRequestModel.getCabRequest())
									&& !HRMSHelper.isNullOrEmpty(
											travelRequestModel.getCabRequest().getCabRequestPassengers())) {
								for (Iterator<VOCabRequestPassenger> iterator = travelRequestModel.getCabRequest()
										.getCabRequestPassengers().iterator(); iterator.hasNext();) {
									VOCabRequestPassenger cabReqPassengerItr = iterator.next();
									if (!HRMSHelper.isNullOrEmpty(cabReqPassengerItr)) {
										if (cabReqPassengerItr.isRecurring()) {
											// recurring request
											if (!HRMSHelper
													.isNullOrEmpty(cabReqPassengerItr.getCabRecurringRequests())) {
												for (Iterator<VOCabRecurringRequest> iteratorRecur = cabReqPassengerItr
														.getCabRecurringRequests().iterator(); iteratorRecur
																.hasNext();) {
													VOCabRecurringRequest cabRecRecItr = iteratorRecur.next();
													if (!HRMSHelper.isNullOrEmpty(cabRecRecItr)) {
														List<MapCabDriverVehicle> mapCabDriverVehList = mapCabDriverVehicleDAO
																.findMapCabDriverByCabReqId(cabRecRecItr.getId(),
																		cabReqPassengerItr.isRecurring(),
																		IHRMSConstants.isActive);
														if (!HRMSHelper.isNullOrEmpty(mapCabDriverVehList)) {
															cabRecRecItr.setMapCabDriverVehicles(HRMSResponseTranslator
																	.translateToVOMapCabDriverVehicle(
																			mapCabDriverVehList));
														}
													}
												}
											}
										} else {
											// non recurring single request
											List<MapCabDriverVehicle> mapCabDriverVehList = mapCabDriverVehicleDAO
													.findMapCabDriverByCabReqId(cabReqPassengerItr.getId(),
															cabReqPassengerItr.isRecurring(), IHRMSConstants.isActive);
											if (!HRMSHelper.isNullOrEmpty(mapCabDriverVehList)) {
												cabReqPassengerItr.setMapCabDriverVehicles(HRMSResponseTranslator
														.translateToVOMapCabDriverVehicle(mapCabDriverVehList));
											}
										}
									}
								}
							}

							if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB, travelRequest,
									travelRequestModel))
								travelRequestModel.setDisableCab(true);

							if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION,
									travelRequest, travelRequestModel))
								travelRequestModel.setDisableAccommodation(true);

							if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, travelRequest,
									travelRequestModel))
								travelRequestModel.setDisableTicket(true);

							list.add(travelRequestModel);
						}
					}
					// response.setColHeaders(getHeaderRows("TICKET_PASSENGER"));
					response.setListResponse(list);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(response);

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
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

	@RequestMapping(value = "/ticketupload", method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = "application/json")
	@ResponseBody
	public String submitTravelDocuemnt(@RequestParam("file") MultipartFile[] request, String travellerId,
			String childId, String travellerType) throws HRMSException {
		try {
			Sardine sardine = null;
			sardine = SardineFactory.begin();
			HRMSListResponseObject response = new HRMSListResponseObject();
			List<Object> list = new ArrayList<Object>();

			if (!HRMSHelper.isNullOrEmpty(travellerId) && !HRMSHelper.isNullOrEmpty(travellerType)
					&& !HRMSHelper.isNullOrEmpty(childId) && !HRMSHelper.isNullOrEmpty(request)) {

				TravelRequest travelRequestForm = travelRequestDAO.findById(Long.parseLong(travellerId)).get();

				if (!HRMSHelper.isNullOrEmpty(travelRequestForm)) {

					Employee employeeTravelRequestEntity = employeeDAO
							.findById(travelRequestForm.getEmployeeId().getId()).get();
					for (MultipartFile file : request) {

						String savePath = HRMSFileuploadUtil.directorycreationforTravelusingEmployeeId(baseURL,
								employeeTravelRequestEntity, travellerId, travellerType);

						List<TraveldeskDocument> documentList = travelDeskDocumentDAO.getDocumentsUsingparentId(
								Long.parseLong(travellerId), Long.parseLong(childId), travellerType,
								IHRMSConstants.isActive);
						String filename = file.getOriginalFilename().replaceAll("\\s+", "_");
						int countmatch = 0;
						for (TraveldeskDocument docuemnt : documentList) {
							if (docuemnt.getDocumentName().equals(filename)) {
								filename.concat("_" + countmatch);
								countmatch = countmatch + 1;

							}
						}

						logger.info("====  path of Travel  ====" + savePath);

						String str = savePath + "/" + filename;
						byte[] bytes = file.getBytes();
						sardine.put(str, bytes);

						TraveldeskDocument tdDocument = new TraveldeskDocument();

						tdDocument.setIsActive(IHRMSConstants.isActive);
						tdDocument.setChildType(travellerType);
						tdDocument.setChildId(Long.parseLong(childId));
						tdDocument.setDocumentName(filename);
						tdDocument.setTravelRequest(travelRequestForm);
						tdDocument.setCreatedDate(new Date());

						travelDeskDocumentDAO.save(tdDocument);

					}
					travelRequestForm = travelRequestDAO.findById(travelRequestForm.getId()).get();
					VOTravelRequest travelRequestModel = HRMSEntityToModelMapper
							.convertToTravelRequestModel(travelRequestForm);

					if (travelRequestModel != null && travelRequestModel.getTicketRequest() != null) {

						List<TraveldeskDocument> ticketDocumentList = travelDeskDocumentDAO.getDocumentsUsingparentId(
								travelRequestForm.getId(), travelRequestModel.getTicketRequest().getId(),
								IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, IHRMSConstants.isActive);
						List<VOTravelDeskDocument> ticketDocument = getDocumentModelForChild(ticketDocumentList,
								travelRequestForm, employeeTravelRequestEntity,
								IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
						travelRequestModel.getTicketRequest().setTicketDocument(ticketDocument);
					}

					/**
					 * Getting Document For Accommodation Request if available
					 */
					if (travelRequestModel != null && travelRequestModel.getAccommodationRequest() != null) {

						if (travelRequestModel != null && travelRequestModel.getAccommodationRequest() != null) {

							List<TraveldeskDocument> accommodationDocuments = travelDeskDocumentDAO
									.getDocumentsUsingparentId(travelRequestForm.getId(),
											travelRequestModel.getAccommodationRequest().getId(),
											IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION,
											IHRMSConstants.isActive);
							List<VOTravelDeskDocument> accommodationDocument = getDocumentModelForChild(
									accommodationDocuments, travelRequestForm, employeeTravelRequestEntity,
									IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
							travelRequestModel.getAccommodationRequest()
									.setAccommodationDocument(accommodationDocument);
						}
					}

					list.add(travelRequestModel);

					response.setListResponse(list);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.FILEUPLOADSUCCESS);
					return HRMSHelper.createJsonString(response);

					/*
					 * fileUploadResponse.setFile_name(request.getOriginalFilename());
					 * fileUploadResponse.setPath(str);
					 * fileUploadResponse.setResponseCode(IHRMSConstants.successCode);
					 * fileUploadResponse.setResponseMessage(IHRMSConstants.addedsuccessMessage);
					 */

					// return HRMSHelper.sendSuccessResponse(IHRMSConstants.FILEUPLOADSUCCESS,
					// IHRMSConstants.successCode);
				} else
					throw new HRMSException(IHRMSConstants.TRAVEL_REQUEST_NOT_FOUND_CODE,
							IHRMSConstants.TRAVEL_REQUEST_NOT_FOUND);
			} else
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
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

	@RequestMapping(value = "/deleteFile/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String deleteFile(@PathVariable("id") long fileid) {
		try {
			if (!HRMSHelper.isLongZero(fileid)) {

				TraveldeskDocument tdDocument = travelDeskDocumentDAO.findById(fileid).get();
				tdDocument.setIsActive(IHRMSConstants.isNotActive);
				travelDeskDocumentDAO.save(tdDocument);
				return HRMSHelper.sendSuccessResponse(IHRMSConstants.FILEDELETEDSUCCESS, IHRMSConstants.successCode);

			} else
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
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

	@RequestMapping(value = "/getdocumentdetails/{empId}/{travelId}/{childId}/{childType}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getBPMDetails(@PathVariable("empId") long loggedinEmpId, @PathVariable("travelId") long travelId,
			@PathVariable("childId") long childId, @PathVariable("childType") String childType) {
		try {
			if (!HRMSHelper.isNullOrEmpty(loggedinEmpId) && !HRMSHelper.isLongZero(travelId)
					&& !HRMSHelper.isNullOrEmpty(childId) && !HRMSHelper.isNullOrEmpty(childType)) {

				List<TraveldeskDocument> tdDocumentList = travelDeskDocumentDAO.getDocumentsUsingparentId(travelId,
						childId, childType, IHRMSConstants.isActive);
				List<Object> vodocumentList = new ArrayList<>();
				Employee loggedinEmpEntity = employeeDAO.findById(loggedinEmpId).get();

				// vodocumentList =
				// HRMSResponseTranslator.translatetoVODocuemntList(tdDocumentList,
				// vodocumentList);
				if (!HRMSHelper.isNullOrEmpty(tdDocumentList)) {

					for (TraveldeskDocument traveldeskEntity : tdDocumentList) {
						VOTravelDeskDocument votravelDeskDocuemt = new VOTravelDeskDocument();
						votravelDeskDocuemt.setChildId(traveldeskEntity.getChildId());
						votravelDeskDocuemt.setChildType(traveldeskEntity.getChildType());
						votravelDeskDocuemt.setDocumentName(traveldeskEntity.getDocumentName());
						votravelDeskDocuemt.setId(traveldeskEntity.getId());
						votravelDeskDocuemt.setIsActive(traveldeskEntity.getIsActive());

						String path = rootDirectory
								+ loggedinEmpEntity.getCandidate().getLoginEntity().getOrganization().getId() + "/"
								+ IHRMSConstants.TRAVELFOLDERNAME + "/" + travelId + "/" + childType + "/"
								+ votravelDeskDocuemt.getDocumentName();

						votravelDeskDocuemt.setLocation(path);

						votravelDeskDocuemt.setTravelRequestId(HRMSEntityToModelMapper
								.convertToTravelRequestModel(traveldeskEntity.getTravelRequest()));
						vodocumentList.add(votravelDeskDocuemt);
					}

					HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
					hrmsListResponseObject.setListResponse(vodocumentList);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
				} else
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			} else
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
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

	public void create() {

	}

	@RequestMapping(value = "/idTest", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void idTest() {
		try {
			long orgId = 1L;
			Organization org = organizationDAO.findById(orgId).get();
			long currentTravelReqSeq = 0;
			long countTravelReqByOrg = travelRequestDAO.getCountTravelRequestOrgwise(orgId);
			if (countTravelReqByOrg == 0) {
				currentTravelReqSeq = 0;
			} else {
				currentTravelReqSeq = travelRequestDAO.getTravelRequestSeqOrgwise(org.getId());
			}
			TravelRequest travelRequest = new TravelRequest();
			travelRequest.setSeqId(currentTravelReqSeq + 1l);
			travelRequest.setOrganization(org);
			travelRequestDAO.save(travelRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * to mark as complete by TD SSW
	 */
	@RequestMapping(value = "markAsComplete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String markAsComplete(@RequestBody VOMarkAsCompleteChildRequest macChildReq) {
		try {
			if (!HRMSHelper.isNullOrEmpty(macChildReq) && !HRMSHelper.isLongZero(macChildReq.getChildRequestId())
					&& !HRMSHelper.isNullOrEmpty(macChildReq.getChildType())
					&& !HRMSHelper.isLongZero(macChildReq.getTravelRequestId())) {
				TravelRequest travelRequest = travelRequestDAO.findTravelRequestById(macChildReq.getTravelRequestId(),
						IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(travelRequest)) {
					Employee employeeTD = employeeDAO.findById(macChildReq.getLoggedInEmployeeId()).get();
					if (macChildReq.getChildType().equals(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET)) {
						TicketRequest ticketRequest = ticketRequestDAO
								.findTicketRequestById(macChildReq.getChildRequestId(), IHRMSConstants.isActive);
						if (!HRMSHelper.isNullOrEmpty(ticketRequest)) {
							if (!HRMSHelper.isNullOrEmpty(macChildReq.getComment())) {
								TraveldeskComment traveldeskComment = new TraveldeskComment();
								traveldeskComment.setTravelRequest(travelRequest);
								traveldeskComment.setEmployee(employeeTD);
								traveldeskComment.setCommentator(IHRMSConstants.TRAVEL_REQUEST_COMMENTATOR_TRAVELDESK);
								traveldeskComment.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_MARK_AS_COMPLETE);
								traveldeskComment.setComment(macChildReq.getComment());
								traveldeskComment.setCreatedBy(String.valueOf(employeeTD.getId()));
								traveldeskComment.setCreatedDate(new Date());
								traveldeskComment.setIsActive(IHRMSConstants.isActive);
								traveldeskComment.setChildId(macChildReq.getChildRequestId());
								traveldeskComment.setChildType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
								traveldeskCommentDAO.save(traveldeskComment);
							}
							ticketRequest.setTicketRequestStatus(IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_CLOSED);
							if (!HRMSHelper.isLongZero(macChildReq.getLoggedInEmployeeId())) {
								ticketRequest.setUpdatedBy(String.valueOf(macChildReq.getLoggedInEmployeeId()));
							}
							ticketRequest.setTotalTicketFare(macChildReq.getAmount());
							ticketRequest.setUpdatedDate(new Date());
							ticketRequest.setTotalRefundAmount(macChildReq.getTotalRefundAmount());
							ticketRequestDAO.save(ticketRequest);
						}

					} else if (macChildReq.getChildType()
							.equals(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION)) {
						AccommodationRequest accomReq = accommodationRequestDAO
								.findAccommodationRequestById(macChildReq.getChildRequestId(), IHRMSConstants.isActive);
						if (!HRMSHelper.isNullOrEmpty(accomReq)) {
							if (!HRMSHelper.isNullOrEmpty(macChildReq.getComment())) {
								TraveldeskComment traveldeskComment = new TraveldeskComment();
								traveldeskComment.setTravelRequest(travelRequest);
								traveldeskComment.setEmployee(employeeTD);
								traveldeskComment.setCommentator(IHRMSConstants.TRAVEL_REQUEST_COMMENTATOR_TRAVELDESK);
								traveldeskComment.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_MARK_AS_COMPLETE);
								traveldeskComment.setComment(macChildReq.getComment());
								traveldeskComment.setCreatedBy(String.valueOf(employeeTD.getId()));
								traveldeskComment.setCreatedDate(new Date());
								traveldeskComment.setIsActive(IHRMSConstants.isActive);
								traveldeskComment.setChildId(macChildReq.getChildRequestId());
								traveldeskComment.setChildType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
								traveldeskCommentDAO.save(traveldeskComment);
							}
							accomReq.setAccommodationRequestStatus(IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_CLOSED);
							if (!HRMSHelper.isLongZero(macChildReq.getLoggedInEmployeeId())) {
								accomReq.setUpdatedBy(String.valueOf(macChildReq.getLoggedInEmployeeId()));
							}
							accomReq.setTotalAccommodationCost(macChildReq.getAmount());
							accomReq.setUpdatedDate(new Date());
							accomReq.setTotalRefundAmount(macChildReq.getTotalRefundAmount());
							accommodationRequestDAO.save(accomReq);
						}
					} else if (macChildReq.getChildType().equals(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB)) {
						// cabRequestDAO
						CabRequest cabReq = cabRequestDAO.findCabRequestById(macChildReq.getChildRequestId(),
								IHRMSConstants.isActive);
						if (!HRMSHelper.isNullOrEmpty(cabReq)) {
							if (!HRMSHelper.isNullOrEmpty(macChildReq.getComment())) {
								TraveldeskComment traveldeskComment = new TraveldeskComment();
								traveldeskComment.setTravelRequest(travelRequest);
								traveldeskComment.setEmployee(employeeTD);
								traveldeskComment.setCommentator(IHRMSConstants.TRAVEL_REQUEST_COMMENTATOR_TRAVELDESK);
								traveldeskComment.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_MARK_AS_COMPLETE);
								traveldeskComment.setComment(macChildReq.getComment());
								traveldeskComment.setCreatedBy(String.valueOf(employeeTD.getId()));
								traveldeskComment.setCreatedDate(new Date());
								traveldeskComment.setIsActive(IHRMSConstants.isActive);
								traveldeskComment.setChildId(macChildReq.getChildRequestId());
								traveldeskComment.setChildType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB);
								traveldeskCommentDAO.save(traveldeskComment);
							}
							cabReq.setCabRequestStatus(IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_CLOSED);
							if (!HRMSHelper.isLongZero(macChildReq.getLoggedInEmployeeId())) {
								cabReq.setUpdatedBy(String.valueOf(macChildReq.getLoggedInEmployeeId()));
							}
							cabReq.setTotalCabCost(macChildReq.getAmount());
							cabReq.setUpdatedDate(new Date());
							cabReq.setTotalRefundAmount(macChildReq.getTotalRefundAmount());
							cabRequestDAO.save(cabReq);
						}
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
				}

			} else if (!HRMSHelper.isLongZero(macChildReq.getTravelRequestId())) {
				TravelRequest travelRequest = travelRequestDAO.findTravelRequestById(macChildReq.getTravelRequestId(),
						IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(travelRequest)) {
					int status = 1;
					if (travelRequest.isBookTicket()) {
						if (travelRequest.getTicketRequest().getTicketRequestStatus()
								.equals(IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_CLOSED)) {
							status = status * 1;
						} else {
							status = status * 0;
						}
					}

					if (travelRequest.isBookAccommodation()) {
						if (travelRequest.getAccommodationRequest().getAccommodationRequestStatus()
								.equals(IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_CLOSED)) {
							status = status * 1;
						} else {
							status = status * 0;
						}
					}

					if (travelRequest.isBookCab()) {
						if (travelRequest.getCabRequest().getCabRequestStatus()
								.equals(IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_CLOSED)) {
							status = status * 1;
						} else {
							status = status * 0;
						}
					}

					if (status == 1) {
						Employee employeeTD = employeeDAO.findById(macChildReq.getLoggedInEmployeeId()).get();
						if (!HRMSHelper.isNullOrEmpty(macChildReq.getComment())) {
							TraveldeskComment traveldeskComment = new TraveldeskComment();
							traveldeskComment.setTravelRequest(travelRequest);
							traveldeskComment.setEmployee(employeeTD);
							traveldeskComment.setCommentator(IHRMSConstants.TRAVEL_REQUEST_COMMENTATOR_TRAVELDESK);
							traveldeskComment.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_MARK_AS_COMPLETE);
							traveldeskComment.setComment(macChildReq.getComment());
							traveldeskComment.setCreatedBy(String.valueOf(employeeTD.getId()));
							traveldeskComment.setCreatedDate(new Date());
							traveldeskComment.setIsActive(IHRMSConstants.isActive);
							traveldeskCommentDAO.save(traveldeskComment);
						}
						travelRequest.setTravelStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_COMPLETED);
						if (!HRMSHelper.isLongZero(macChildReq.getLoggedInEmployeeId())) {
							travelRequest.setUpdatedBy(String.valueOf(macChildReq.getLoggedInEmployeeId()));
						}
						travelRequest.setUpdatedDate(new Date());
						travelRequestDAO.save(travelRequest);
					} else {
						throw new HRMSException(IHRMSConstants.CHILD_REQUEST_NOT_CLOSED_CODE,
								IHRMSConstants.CHILD_REQUEST_NOT_CLOSED_MESSAGE);
					}
				} else {
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			return HRMSHelper.sendSuccessResponse(IHRMSConstants.APPROVER_ACTION_SUCCESS_MSG,
					IHRMSConstants.successCode);
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

	/**
	 * To Get All Travel Request For travel desk employee Pagination is applied Sort
	 * Order : 1: Pending 2: WIP 3: Rejected
	 */
	@RequestMapping(value = "getAllRequest/{organizationId}/{page}/{size}", method = {
			RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String getAllRequest(@PathVariable("page") int page, @PathVariable("size") int size,
			@PathVariable("organizationId") long organizationId) {

		try {

			if (size <= 0) {
				size = IHRMSConstants.DefaultPageSize;
			}

			List<TravelRequest> travelRequestList = travelRequestDAO.findAllTravelRequest(IHRMSConstants.isActive,
					IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING, IHRMSConstants.TRAVEL_REQUEST_STATUS_WIP,
					IHRMSConstants.TRAVEL_REQUEST_STATUS_REJECTED, organizationId /* , new PageRequest(page, size) */);

			if (!HRMSHelper.isNullOrEmpty(travelRequestList)) {

				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> list = new ArrayList<Object>();
				for (TravelRequest travelRequest : travelRequestList) {
					if (travelRequest != null) {

						Employee employee = employeeDAO.findActiveEmployeeById(travelRequest.getEmployeeId().getId(),
								IHRMSConstants.isActive);
						VOTravelRequest travelRequestModel = HRMSEntityToModelMapper
								.convertToTravelRequestModel(travelRequest);

						/**
						 * Getting Document For Ticket Request if available
						 */
						if (travelRequestModel != null && travelRequestModel.getTicketRequest() != null) {

							List<TraveldeskDocument> ticketDocumentList = travelDeskDocumentDAO
									.getDocumentsUsingparentId(travelRequest.getId(),
											travelRequestModel.getTicketRequest().getId(),
											IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, IHRMSConstants.isActive);
							List<VOTravelDeskDocument> ticketDocument = getDocumentModelForChild(ticketDocumentList,
									travelRequest, employee, IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
							travelRequestModel.getTicketRequest().setTicketDocument(ticketDocument);
						}

						/**
						 * Getting Document For Accommodation Request if available
						 */
						if (travelRequestModel != null && travelRequestModel.getAccommodationRequest() != null) {

							if (travelRequestModel != null && travelRequestModel.getAccommodationRequest() != null) {

								List<TraveldeskDocument> accommodationDocuments = travelDeskDocumentDAO
										.getDocumentsUsingparentId(travelRequest.getId(),
												travelRequestModel.getAccommodationRequest().getId(),
												IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION,
												IHRMSConstants.isActive);
								List<VOTravelDeskDocument> accommodationDocument = getDocumentModelForChild(
										accommodationDocuments, travelRequest, employee,
										IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
								travelRequestModel.getAccommodationRequest()
										.setAccommodationDocument(accommodationDocument);
							}
						}

						/*
						 * finding cab details
						 */

						if (!HRMSHelper.isNullOrEmpty(travelRequestModel)
								&& !HRMSHelper.isNullOrEmpty(travelRequestModel.getCabRequest()) && !HRMSHelper
										.isNullOrEmpty(travelRequestModel.getCabRequest().getCabRequestPassengers())) {
							for (Iterator<VOCabRequestPassenger> iterator = travelRequestModel.getCabRequest()
									.getCabRequestPassengers().iterator(); iterator.hasNext();) {
								VOCabRequestPassenger cabReqPassengerItr = iterator.next();
								if (!HRMSHelper.isNullOrEmpty(cabReqPassengerItr)) {
									if (cabReqPassengerItr.isRecurring()) {
										// recurring request

										Comparator<VOCabRecurringRequest> mycomparator = Collections.reverseOrder();
										Collections.sort(cabReqPassengerItr.getCabRecurringRequests(), mycomparator);

										if (!HRMSHelper.isNullOrEmpty(cabReqPassengerItr.getCabRecurringRequests())) {
											for (Iterator<VOCabRecurringRequest> iteratorRecur = cabReqPassengerItr
													.getCabRecurringRequests().iterator(); iteratorRecur.hasNext();) {
												VOCabRecurringRequest cabRecRecItr = iteratorRecur.next();
												if (!HRMSHelper.isNullOrEmpty(cabRecRecItr)) {
													List<MapCabDriverVehicle> mapCabDriverVehList = mapCabDriverVehicleDAO
															.findMapCabDriverByCabReqId(cabRecRecItr.getId(),
																	cabReqPassengerItr.isRecurring(),
																	IHRMSConstants.isActive);
													if (!HRMSHelper.isNullOrEmpty(mapCabDriverVehList)) {
														cabRecRecItr.setMapCabDriverVehicles(HRMSResponseTranslator
																.translateToVOMapCabDriverVehicle(mapCabDriverVehList));
													}
												}
											}
										}
									} else {
										// non recurring single request
										List<MapCabDriverVehicle> mapCabDriverVehList = mapCabDriverVehicleDAO
												.findMapCabDriverByCabReqId(cabReqPassengerItr.getId(),
														cabReqPassengerItr.isRecurring(), IHRMSConstants.isActive);
										if (!HRMSHelper.isNullOrEmpty(mapCabDriverVehList)) {
											cabReqPassengerItr.setMapCabDriverVehicles(HRMSResponseTranslator
													.translateToVOMapCabDriverVehicle(mapCabDriverVehList));
										}
									}
								}
							}
						}

						/**
						 * Added - 21-08-2018 :: Start
						 **/
						/*
						 * if (travelRequest.getCabRequest() != null) { if
						 * (travelRequest.getCabRequest().getIsActive().equalsIgnoreCase(IHRMSConstants.
						 * isActive)) { travelRequestModel.setBookCab(true); }
						 * 
						 * }
						 * 
						 * if (travelRequest.getAccommodationRequest() != null) { if
						 * (travelRequest.getAccommodationRequest().getIsActive()
						 * .equalsIgnoreCase(IHRMSConstants.isActive)) {
						 * travelRequestModel.setBookAccommodation(true); } }
						 * 
						 * if (travelRequest.getTicketRequest() != null) { if
						 * (travelRequest.getTicketRequest().getIsActive()
						 * .equalsIgnoreCase(IHRMSConstants.isActive)) {
						 * travelRequestModel.setBookTicket(true); }
						 * 
						 * }
						 */

						/**
						 * End
						 */

						int cancelledCount = 0;
						int bookedCount = 0;

						if (travelRequest.isBookCab()) {
							bookedCount++;
						}

						if (travelRequest.isBookAccommodation()) {
							bookedCount++;
						}

						if (travelRequest.isBookTicket()) {
							bookedCount++;
						}

						if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB, travelRequest,
								travelRequestModel)) {
							cancelledCount++;
							travelRequestModel.setDisableCab(true);
						}

						if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION,
								travelRequest, travelRequestModel)) {
							cancelledCount++;
							travelRequestModel.setDisableAccommodation(true);
						}

						if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, travelRequest,
								travelRequestModel)) {
							cancelledCount++;
							travelRequestModel.setDisableTicket(true);
						}

						int activeCount = bookedCount - cancelledCount;
						if (activeCount < 0)
							activeCount = 0;

						String completeStatus = "Active : " + activeCount + " | " + "Cancelled :" + cancelledCount;
						travelRequestModel.setRequestSummaryCount(completeStatus.replace("-", ""));

						list.add(travelRequestModel);
					}

				}
				response.setListResponse(list);
				response.setPageNo(page);
				response.setPageSize(size);
				response.setTotalCount(travelRequestList.size());
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(response);

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

	@RequestMapping(value = "assignApprover", method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String assignApprover(@RequestBody VOApproverAassignmentRequest approverRequest)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info(" ::  Assigning Approver !!! Please Wait ::  ");

		try {

			if (approverRequest != null) {

				String mailBody = "";
				String emailSubject = "";
				long divisionId = 0;
				long organizationId = 0;
				String email_CC_traveldesk = "";

				TravelRequest travelRequest = travelRequestDAO.findByRequestId(approverRequest.getParentId(),
						IHRMSConstants.isActive);

				if (travelRequest != null) {

					VOTraveldeskComment comment = approverRequest.getComment();

					Employee commenter = null;
					TraveldeskComment commentEntity = null;

					/**
					 * Persisting Commenter Details
					 */
					if (approverRequest.getComment() != null) {

						if (approverRequest.getComment().getEmployee() != null) {
							commentEntity = new TraveldeskComment();
							commentEntity.setComment(comment.getComment());
							commentEntity.setTravelRequest(travelRequest);
							commenter = employeeDAO.findActiveEmployeeById(
									approverRequest.getComment().getEmployee().getId(), IHRMSConstants.isActive);
							commentEntity.setEmployee(commenter);
							commentEntity.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_PENDING);
							commentEntity.setCreatedDate(new Date());
						} else {
							throw new HRMSException(IHRMSConstants.InsufficientDataCode,
									IHRMSConstants.COMMENTER_DETAILS_INSUFFICIENT);
						}
					}
					/**
					 * Finding Approver Details
					 */
					/*
					 * Employee approver =
					 * employeeDAO.findActiveEmployeeById(approverRequest.getApprover().getId(),
					 * IHRMSConstants.isActive);
					 */
					Employee approver = null;
					MasterTraveldeskApprover masterApprover = masterApproverDAO
							.findById(approverRequest.getApprover().getId()).get();
					if (masterApprover != null) {

						approver = masterApprover.getEmployee();
						if (approver != null) {
							divisionId = approver.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
							organizationId = approver.getCandidate().getLoginEntity().getOrganization().getId();
						} else {
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.APPROVER_NOT_FOUND_MSG);
						}
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.APPROVER_NOT_FOUND_MSG);
					}

					/**
					 * Finding Travel Desk Employee Details,For Sending Email in CC
					 */
					List<MasterTraveldeskApprover> travelDeskEmployeeList = travelDeskApproverDAO
							.findTravelDeskEmployeeOrgWise(organizationId, divisionId,
									IHRMSConstants.APPROVER_TYPE_TRAVELDESK, IHRMSConstants.isActive);

					if (!HRMSHelper.isNullOrEmpty(travelDeskEmployeeList)) {
						for (MasterTraveldeskApprover approvar : travelDeskEmployeeList) {
							email_CC_traveldesk = email_CC_traveldesk + approvar.getEmployee().getOfficialEmailId()
									+ ";";
						}
					}

					/**
					 * Assigning Approver Based On The Request
					 */
					if (approverRequest.getApprovalType()
							.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB)) {
						CabRequest cabRequest = travelRequest.getCabRequest();
						if (cabRequest != null) {

							cabRequest.setApprovalRequired(true);
							// cabRequest.setApprover(approver);
							cabRequest.setMasterApprover(masterApprover);

							cabRequest.setApproverStatus(IHRMSConstants.TD_APPROVER_STATUS_PENDING);

							if (commentEntity != null) {
								commentEntity.setChildId(cabRequest.getId());
								commentEntity.setChildType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB);
							}
							List<VOTraveldeskComment> tdCommentList = null;
							if (!HRMSHelper.isNullOrEmpty(approverRequest.getComment())) {
								tdCommentList = new ArrayList<VOTraveldeskComment>();
								tdCommentList.add(approverRequest.getComment());
							}
							emailSubject = IHRMSConstants.CAB_REQUEST_EMAIL_SUBJECT + " | Pending : "
									+ travelRequest.getSeqId();
							Map<String, String> map = createMailContent(travelRequest, approver,
									IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB, tdCommentList);
							mailBody = HRMSHelper.replaceString(map,
									IHRMSEmailTemplateConstants.TEMPLATE_APPROVER_CONTENT);
							travelRequest.setCabRequest(cabRequest);
						}
					} else if (approverRequest.getApprovalType()
							.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET)) {
						TicketRequest ticket = travelRequest.getTicketRequest();
						if (ticket != null) {
							ticket.setApprovalRequired(true);
							// ticket.setApproverId(approver);
							ticket.setMasterApprover(masterApprover);
							ticket.setApproverStatus(IHRMSConstants.TD_APPROVER_STATUS_PENDING);
							travelRequest.setTicketRequest(ticket);

							if (commentEntity != null) {
								logger.info("Ticket Commented");
								commentEntity.setChildId(ticket.getId());
								commentEntity.setChildType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
							}

							List<VOTraveldeskComment> tdCommentList = null;
							if (!HRMSHelper.isNullOrEmpty(approverRequest.getComment())) {
								tdCommentList = new ArrayList<VOTraveldeskComment>();
								tdCommentList.add(approverRequest.getComment());
							}

							Map<String, String> map = createMailContent(travelRequest, approver,
									IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, tdCommentList);
							mailBody = HRMSHelper.replaceString(map,
									IHRMSEmailTemplateConstants.TEMPLATE_APPROVER_CONTENT);
							emailSubject = IHRMSConstants.TICKET_REQUEST_EMAIL_SUBJECT + " | Pending : "
									+ travelRequest.getSeqId();
						}

					} else if (approverRequest.getApprovalType()
							.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION)) {
						AccommodationRequest accommodation = travelRequest.getAccommodationRequest();
						if (accommodation != null) {
							accommodation.setApprovalRequired(true);
							// accommodation.setApproverId(approver);
							accommodation.setMasterApprover(masterApprover);
							accommodation.setApproverStatus(IHRMSConstants.TD_APPROVER_STATUS_PENDING);
							travelRequest.setAccommodationRequest(accommodation);

							if (commentEntity != null) {
								commentEntity.setChildId(accommodation.getId());
								commentEntity.setChildType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
							}

							List<VOTraveldeskComment> tdCommentList = null;
							if (!HRMSHelper.isNullOrEmpty(approverRequest.getComment())) {
								tdCommentList = new ArrayList<VOTraveldeskComment>();
								tdCommentList.add(approverRequest.getComment());
							}

							Map<String, String> map = createMailContent(travelRequest, approver,
									IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION, tdCommentList);
							emailSubject = IHRMSConstants.ACCOMMODATION_REQUEST_EMAIL_SUBJECT;
							mailBody = HRMSHelper.replaceString(map,
									IHRMSEmailTemplateConstants.TEMPLATE_APPROVER_CONTENT);
						}
					}

					if (commentEntity != null)
						commentDAO.save(commentEntity);
					travelRequestDAO.save(travelRequest);

					/**
					 * Persisting Email To Be Sent To Approver
					 */
					emailSenderDAO.toPersistEmail(approver.getOfficialEmailId(), email_CC_traveldesk, mailBody,
							emailSubject, divisionId, organizationId, IHRMSConstants.IS_MAIL_WITH_ATTACHMENT_N, null);
				} else {
					throw new HRMSException(IHRMSConstants.TRAVEL_REQUEST_NOT_FOUND_CODE,
							IHRMSConstants.TRAVEL_REQUEST_NOT_FOUND);
				}
				return HRMSHelper.sendSuccessResponse(IHRMSConstants.APPROVER_ASSIGNMENT_SUCCESS_MSG,
						IHRMSConstants.successCode);
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

	/**
	 * This method is to create Mail Content for Travel Request
	 * 
	 * @param Travel Request Employee ( To whom the email needs to send ) String for
	 *               which the email content to be created
	 *               i.e.Cab,Ticket,Accommodation
	 */
	public Map<String, String> createMailContent(TravelRequest travelRequestForm, Employee recipientEmployee,
			String toCreateMailContentFor, List<VOTraveldeskComment> comment)
			throws JsonGenerationException, JsonMappingException, IOException {

		Map<String, String> placeHolder = new HashMap<String, String>();
		placeHolder.put("{websiteURL}", baseURL);
		Employee employeeRequester = employeeDAO.findById(travelRequestForm.getEmployeeId().getId()).get();
		;

		if (!HRMSHelper.isNullOrEmpty(employeeRequester)) {
			placeHolder.put("{requestBy}", employeeRequester.getCandidate().getFirstName() + " "
					+ employeeRequester.getCandidate().getLastName());
		} else {
			placeHolder.put("{requestBy}", travelRequestForm.getCreatedBy());
		}

		String recipientEmployeeName = "";
		if (!HRMSHelper.isNullOrEmpty(recipientEmployee)) {
			recipientEmployeeName = recipientEmployee.getCandidate().getFirstName() + " "
					+ recipientEmployee.getCandidate().getLastName();
		} else {
			recipientEmployeeName = "Traveldesk";
		}

		placeHolder.put("{recipientEmployeeName}", recipientEmployeeName);
		placeHolder.put("{ticket}", HRMSHelper.toConvertBooleanToHumanReadable(travelRequestForm.isBookTicket()));
		placeHolder.put("{cab}", HRMSHelper.toConvertBooleanToHumanReadable(travelRequestForm.isBookCab()));
		placeHolder.put("{accomodation}",
				HRMSHelper.toConvertBooleanToHumanReadable(travelRequestForm.isBookAccommodation()));

		placeHolder.put("{requestId}", String.valueOf(travelRequestForm.getSeqId()));

		String clientName = IHRMSConstants.NA;
		String bdName = IHRMSConstants.NA;
		String workOrderNo = IHRMSConstants.NA;
		String buName = IHRMSConstants.NA;

		if (!HRMSHelper.isNullOrEmpty(travelRequestForm.getClientName())) {
			clientName = travelRequestForm.getClientName();
		}

		if (!HRMSHelper.isNullOrEmpty(travelRequestForm.getBdName())) {
			bdName = travelRequestForm.getBdName();
		}

		if (travelRequestForm.getWorkOrderNo() != 0) {
			workOrderNo = String.valueOf(travelRequestForm.getWorkOrderNo());
		}

		if (!HRMSHelper.isNullOrEmpty(travelRequestForm.getBusinessUnit())) {
			buName = String.valueOf(travelRequestForm.getBusinessUnit());
		}

		placeHolder.put("{client}", clientName);
		placeHolder.put("{bdName}", bdName);
		placeHolder.put("{buName}", buName);
		placeHolder.put("{won}", workOrderNo);

		/*
		 * // next added by ssw on 11July2018 if
		 * (!HRMSHelper.isNullOrEmpty(travelRequestForm.getRejectCommentByTd())) {
		 * placeHolder.put("{travelRequestRejectReason}",
		 * String.valueOf(travelRequestForm.getRejectCommentByTd())); }
		 */

		if (travelRequestForm.getTicketRequest() != null
				&& (toCreateMailContentFor.contains(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET)
						|| toCreateMailContentFor.equalsIgnoreCase("ALL"))) {

			Set<TicketRequestPassenger> ticketTravellers = travelRequestForm.getTicketRequest()
					.getTicketRequestPassengers();
			String passenger = "";

			if (toCreateMailContentFor.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET)) {

				// + "{ticketRequest}" + "{cabRequestBase}" + "{accomodationRequest}"
				placeHolder.put("{accomodationRequest}", "");
				placeHolder.put("{cabRequestBase}", "");
			}

			if (!HRMSHelper.isNullOrEmpty(ticketTravellers)) {

				for (TicketRequestPassenger travellers : ticketTravellers) {

					if (!HRMSHelper.isNullOrEmpty(travellers)) {
						passenger = passenger + "<tr><td>" + travellers.getPassengerName() + "</td>";
						if (!HRMSHelper.isNullOrEmpty(travellers.getDateOfBirth())) {
							passenger = passenger + "<td>" + HRMSDateUtil.format(travellers.getDateOfBirth(),
									IHRMSConstants.FRONT_END_DATE_FORMAT) + "</td>";
						} else {
							passenger = passenger + "<td></td>";
						}
						if (!HRMSHelper.isNullOrEmpty(travellers.getEmployee())) {
							passenger = passenger + "<td>" + travellers.getEmployee().getId() + "</td>";
						} else {
							passenger = passenger + "<td></td>";
						}
						passenger = passenger + "<td>" + travellers.getContactNumber() + "</td>" + "<td>"
								+ travellers.getEmailId() + "</td></tr>";
					}
				}
			}

			String ticket = "<p><span style=\"text-decoration: underline; font-family:verdana; font-size:11px;\">"
					+ "<strong>Ticket Details</strong></span></p> "
					+ "<p style=\"font-family:verdana;  font-size: 11px; color: Maroon; font-weight: bold;\">Source: "
					+ travelRequestForm.getTicketRequest().getFromLocation() + " | Destination: "
					+ travelRequestForm.getTicketRequest().getToLocation() + " | Date Of Journey: "
					+ HRMSDateUtil.format(travelRequestForm.getTicketRequest().getPreferredDate(),
							IHRMSConstants.FRONT_END_DATE_FORMAT);

			if (travelRequestForm.getTicketRequest().isRoundTrip() == true) {

				ticket = ticket + " | Return: "
						+ HRMSHelper.toConvertBooleanToHumanReadable(travelRequestForm.getTicketRequest().isRoundTrip())
						+ " | Return Date: "
						+ HRMSDateUtil.format(travelRequestForm.getTicketRequest().getReturnPreferredDate(),
								IHRMSConstants.FRONT_END_DATE_FORMAT);

			}

			if (!HRMSHelper.isNullOrEmpty(travelRequestForm.getTicketRequest().getApproverStatus())) {

				ticket = ticket + " | Ticket Approval Status:  |  "
						+ travelRequestForm.getTicketRequest().getApproverStatus();
			}

			ticket = ticket + "</p><p style=\"font-family:verdana;  font-size: 11px;\">Passenger details:</p>"
					+ "<table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"4\"><tbody>";

			ticket = ticket + "<tr style=\"color:white;\" bgcolor=\"#e98700\">" + "<td>Name</td>"
					+ "<td>Date of Birth</td>" + "<td>Employee Id</td>" + "<td>Contact No</td>"
					+ "<td>Email Id</td></tr>" + passenger + "</tbody>" + "</table>";
			placeHolder.put("{ticketRequest}", ticket);
		} else {
			placeHolder.put("{ticketRequest}", "");
		}

		if (travelRequestForm.getCabRequest() != null
				&& (toCreateMailContentFor.contains(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB)
						|| toCreateMailContentFor.equalsIgnoreCase("ALL"))) {

			CabRequest cabRequest = travelRequestForm.getCabRequest();

			if (toCreateMailContentFor.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB)) {
				placeHolder.put("{accomodationRequest}", "");
				placeHolder.put("{ticketRequest}", "");
			}

			Set<CabRequestPassenger> cabPassenger = cabRequest.getCabRequestPassengers();
			String cabTraveller = "";

			if (!HRMSHelper.isNullOrEmpty(cabPassenger)) {

				for (CabRequestPassenger cabPassengers : cabPassenger) {

					if (!HRMSHelper.isNullOrEmpty(cabPassengers)) {
						cabTraveller = cabTraveller + "<tr><td>" + cabPassengers.getPassengerName();

						if (!HRMSHelper.isNullOrEmpty(cabPassengers.getContactNumber())) {
							cabTraveller = cabTraveller + "<td>" + cabPassengers.getContactNumber() + "</td>";
						} else {
							cabTraveller = cabTraveller + "<td></td>";
						}

						if (!HRMSHelper.isNullOrEmpty(cabPassengers.getEmployee())) {
							cabTraveller = cabTraveller + "<td>" + cabPassengers.getEmployee().getId() + "</td>";
						} else {
							cabTraveller = cabTraveller + "<td></td>";
						}

						String pickupDate = "";
						if (cabPassengers.getPickupDate() != null) {
							pickupDate = HRMSDateUtil.format(cabPassengers.getPickupDate(),
									IHRMSConstants.FRONT_END_DATE_FORMAT);
						} else {
							pickupDate = IHRMSConstants.NA;
						}

						String pickupTime = "";
						if (cabPassengers.getPickupTime() != null) {
							pickupTime = cabPassengers.getPickupTime();
						} else {
							pickupTime = IHRMSConstants.NA;
						}

						String returnPickupDate = "";
						if (cabPassengers.getReturnDate() != null) {
							returnPickupDate = HRMSDateUtil.format(cabPassengers.getReturnDate(),
									IHRMSConstants.FRONT_END_DATE_FORMAT);
						} else {
							returnPickupDate = IHRMSConstants.NA;
						}

						String returnPickupTime = "";
						if (cabPassengers.getReturnTime() != null) {
							returnPickupTime = cabPassengers.getReturnTime();
						} else {
							returnPickupTime = IHRMSConstants.NA;
						}

						cabTraveller = cabTraveller + "<td>" + cabPassengers.getEmailId() + "</td>" + "<td>"
								+ pickupDate + "</td>" + "<td>" + pickupTime + "</td>" + "<td>"
								+ cabPassengers.getPickupAt() + "</td>" + "<td>" + cabPassengers.getDropLocation()
								+ "</td>" + "<td>" + returnPickupDate + "</td>" + "<td>" + returnPickupTime + "</td>"
								+ "</tr>";
					}
				}
			}

			String cabRequestString = "<p><span style=\"text-decoration: underline; font-family:calibri; font-size:15px;\"><strong>Passenger details</strong></span></p>"
					+ "<table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"4\"><tbody>"
					+ "<tr style=\"color:white;\" bgcolor=\"#e98700\">" + "<td>Name</td>" + "<td>Contact No</td>"
					+ "<td>Employee Id</td>" + "<td>Email Id</td>" + "<td>Pickup Date</td>" + "<td>Pickup Time</td>"
					+ "<td>Pickup At</td>" + "<td>Drop Location</td>" + "<td>Return Date</td>" + "<td>Return Time</td>"
					+ "</tr>" + cabTraveller + "</tbody>" + "</table>";
			placeHolder.put("{cabRequestBase}", cabRequestString);

		} else {
			placeHolder.put("{cabRequestBase}", "");
		}

		if (travelRequestForm.getAccommodationRequest() != null
				&& (toCreateMailContentFor.contains(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION)
						|| toCreateMailContentFor.equalsIgnoreCase("ALL"))) {

			if (toCreateMailContentFor.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION)) {
				placeHolder.put("{ticketRequest}", "");
				placeHolder.put("{cabRequestBase}", "");
			}

			Set<AccommodationGuest> guestDetails = travelRequestForm.getAccommodationRequest().getAccommodationGuests();
			String guests = "";

			if (!HRMSHelper.isNullOrEmpty(guestDetails)) {
				for (AccommodationGuest guestObject : guestDetails) {
					if (!HRMSHelper.isNullOrEmpty(guestObject)) {

						guests = guests + "<tr><td>" + guestObject.getPassengerName() + "</td>";
						if (!HRMSHelper.isNullOrEmpty(guestObject.getDateOfBirth())) {
							guests = guests + "<td>" + HRMSDateUtil.format(guestObject.getDateOfBirth(),
									IHRMSConstants.FRONT_END_DATE_FORMAT) + "</td>";
						} else {
							guests = guests + "<td></td>";
						}
						if (!HRMSHelper.isNullOrEmpty(guestObject.getEmployee())) {
							guests = guests + "<td>" + guestObject.getEmployee().getId() + "</td>";
						} else {
							guests = guests + "<td></td>";
						}
						guests = guests + "<td>" + guestObject.getContactNumber() + "</td>" + "<td>"
								+ guestObject.getEmailId() + "</td></tr>";

					}

				}
			}

			String accomodationRequest = "<p><span style=\"text-decoration: underline; font-family:calibri; font-size:15px;\"><strong>Accomodation Details</strong></span></p>"
					+ "<p style=\"font-family:verdana;  font-size: 11px; font-weight: bold; color: Maroon; \">From Date: "
					+ String.valueOf(HRMSDateUtil.format(travelRequestForm.getAccommodationRequest().getFromDate(),
							IHRMSConstants.FRONT_END_DATE_FORMAT))
					+ " | To Date: "
					+ String.valueOf(HRMSDateUtil.format(travelRequestForm.getAccommodationRequest().getToDate(),
							IHRMSConstants.FRONT_END_DATE_FORMAT))
					+ "</p><p style=\"font-family:verdana;  font-size: 11px;\">Guest details:</p>"
					+ "<table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"4\"><tbody>"
					+ "<tr style=\"color:white;\" bgcolor=\"#e98700\">" + "<td>Name</td>" + "<td>Date of Birth</td>"
					+ "<td>Employee Id</td>" + "<td>Contact No</td>" + "<td>Email Id</td>" + "</tr>" + guests
					+ "</tbody>" + "</table>";

			placeHolder.put("{accomodationRequest}", accomodationRequest);

		} else {
			placeHolder.put("{accomodationRequest}", "");
		}

		if (!HRMSHelper.isNullOrEmpty(comment)) {
			for (VOTraveldeskComment tdComment : comment) {
				if (tdComment != null) {
					placeHolder.put("{comment}", tdComment.getComment());
				} else {
					placeHolder.put("{comment}", "");
				}

			}
		} else {
			placeHolder.put("{comment}", "");
		}
		return placeHolder;
	}

	/**
	 * assign cab details by TD SSW
	 */
	@RequestMapping(value = "assignCabDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String assignCabDetails(@RequestBody VOUpdateCabRequest voUpdateCabReq) {
		try {
			if (!HRMSHelper.isNullOrEmpty(voUpdateCabReq) && !HRMSHelper.isLongZero(voUpdateCabReq.getCabRequestId())
					&& !HRMSHelper.isNullOrEmpty(voUpdateCabReq.isRecurring())
					&& !HRMSHelper.isNullOrEmpty(voUpdateCabReq.getMapCabDriverVehList())) {
				// get cab request details
				CabRequest cabRequest = cabRequestDAO.findCabRequestById(voUpdateCabReq.getCabRequestId(),
						IHRMSConstants.isActive);
				CabRequestPassenger cabReqPassenger = null;
				CabRecurringRequest cabRecReq = null;
				MasterDriver driver = null;
				MasterVehicle vehicle = null;
				MasterDriver returnDriver = null;
				MasterVehicle returnVehicle = null;
				if (!HRMSHelper.isNullOrEmpty(cabRequest)) {
					
					
					
					for (VOMapCabDriverVehicle voMapCabDriverVehicle : voUpdateCabReq.getMapCabDriverVehList()) {

						if (!HRMSHelper.isNullOrEmpty(voMapCabDriverVehicle)) {
							driver = driverDAO.findDriverById(voMapCabDriverVehicle.getDriver().getId(),
									IHRMSConstants.isActive);
							vehicle = vehicleDAO.findVehicleById(voMapCabDriverVehicle.getVehicle().getId(),
									IHRMSConstants.isActive);
							long cabRequestPassengerId = 0;
							if (voMapCabDriverVehicle.isRecurring()) {
								cabRecReq = cabRecurringRequestDAO.findCabRecurringRequestById(
										voMapCabDriverVehicle.getCabRequestPassenger(), IHRMSConstants.isActive);
								if (!HRMSHelper.isNullOrEmpty(cabRecReq)) {
									cabRequestPassengerId = cabRecReq.getId();
									if (!HRMSHelper.isNullOrEmpty(cabRecReq.getCabRequestPassenger())) {
										cabReqPassenger = cabRecReq.getCabRequestPassenger();
										cabRecReq.setTdSelfManaged(voUpdateCabReq.isSelfManaged());
										cabRecReq.setTdSelfManagedComment(voUpdateCabReq.getSelfManagedComment());
										cabRecReq.setDriverPickupTime(voUpdateCabReq.getDriverpickupTime());
										cabRecReq.setReturnDriverPickupTime(voUpdateCabReq.getDriverreturnPickupTime());
										cabRecReq.setDriverComment(voUpdateCabReq.getDriverComment());
										cabRecurringRequestDAO.save(cabRecReq);
									}
								} else {
									throw new HRMSException(IHRMSConstants.DataNotFoundCode,
											IHRMSConstants.DataNotFoundMessage);
								}
								// cabRecReq.get
							} else {
								cabReqPassenger = cabRequestPassengerDAO.findCabPassengerRequestById(
										voMapCabDriverVehicle.getCabRequestPassenger(), IHRMSConstants.isActive);
								if (!HRMSHelper.isNullOrEmpty(cabReqPassenger)) {
									cabRequestPassengerId = cabReqPassenger.getId();
									
									cabReqPassenger.setTdSelfManaged(voUpdateCabReq.isSelfManaged());
									cabReqPassenger.setTdSelfManagedComment(voUpdateCabReq.getSelfManagedComment());
									cabReqPassenger.setDriverPickupTime(voUpdateCabReq.getDriverpickupTime());
									cabReqPassenger.setReturnDriverPickupTime(voUpdateCabReq.getDriverreturnPickupTime());
									cabReqPassenger.setDriverComment(voUpdateCabReq.getDriverComment());
									cabRequestPassengerDAO.save(cabReqPassenger);
									
									
								} else {
									throw new HRMSException(IHRMSConstants.DataNotFoundCode,
											IHRMSConstants.DataNotFoundMessage);
								}
							}
							
							if(!voUpdateCabReq.isSelfManaged()) {
		
									// one-way mapCabDriverVehicle request
									if (!HRMSHelper.isNullOrEmpty(driver) && !HRMSHelper.isNullOrEmpty(vehicle)) {
										// find existing map cab driver and modifying if updated
										MapCabDriverVehicle mapCabDriverVehiclOneWay = mapCabDriverVehicleDAO
												.findMapCabDriverByCabReqIdTripWay(cabRequestPassengerId,
														voMapCabDriverVehicle.isRecurring(),
														IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_ONE_WAY,
														IHRMSConstants.isActive);
										if (HRMSHelper.isNullOrEmpty(mapCabDriverVehiclOneWay)) {
											// adding new mapping of one way
											MapCabDriverVehicle mapCabDriverVehicle = new MapCabDriverVehicle();
											mapCabDriverVehicle.setCabRequestPassengerId(cabRequestPassengerId);
											mapCabDriverVehicle.setDriverId(driver);
											mapCabDriverVehicle.setVehicleId(vehicle);
											mapCabDriverVehicle.setTripWay(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_ONE_WAY);
											mapCabDriverVehicle.setCreatedDate(new Date());
											mapCabDriverVehicle.setIsActive(IHRMSConstants.isActive);
											mapCabDriverVehicle.setRecurring(voMapCabDriverVehicle.isRecurring());
											mapCabDriverVehicle.setCreatedBy(voMapCabDriverVehicle.getDoneBy());
											mapCabDriverVehicleDAO.save(mapCabDriverVehicle);
										} else {
											if (!(!HRMSHelper.isNullOrEmpty(mapCabDriverVehiclOneWay.getDriverId())
													&& !HRMSHelper.isNullOrEmpty(mapCabDriverVehiclOneWay.getVehicleId())
													&& mapCabDriverVehiclOneWay.getDriverId().getId() == driver.getId()
													&& mapCabDriverVehiclOneWay.getVehicleId().getId() == vehicle.getId())) {
												// request modified
												mapCabDriverVehiclOneWay.setIsActive(IHRMSConstants.isNotActive);
												mapCabDriverVehiclOneWay.setUpdatedBy(voMapCabDriverVehicle.getDoneBy());
												mapCabDriverVehiclOneWay.setUpdatedDate(new Date());
												mapCabDriverVehicleDAO.save(mapCabDriverVehiclOneWay);
		
												MapCabDriverVehicle mapCabDriverVehicle = new MapCabDriverVehicle();
												mapCabDriverVehicle.setCabRequestPassengerId(cabRequestPassengerId);
												mapCabDriverVehicle.setDriverId(driver);
												mapCabDriverVehicle.setVehicleId(vehicle);
												mapCabDriverVehicle.setTripWay(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_ONE_WAY);
												mapCabDriverVehicle.setCreatedDate(new Date());
												mapCabDriverVehicle.setIsActive(IHRMSConstants.isActive);
												mapCabDriverVehicle.setRecurring(voMapCabDriverVehicle.isRecurring());
												mapCabDriverVehicle.setCreatedBy(voMapCabDriverVehicle.getDoneBy());
												mapCabDriverVehicleDAO.save(mapCabDriverVehicle);
											} else {
												// no modification in request
											}
										}
									} else {
										throw new HRMSException(
												IHRMSConstants.TRAVEL_REQUEST_DRIVER_VEHICLE_NOT_FOUND_ERROR_CODE,
												IHRMSConstants.TRAVEL_REQUEST_DRIVER_VEHICLE_NOT_FOUND_ERROR_MESSAGE);
									}
									// return mapCabDriverVehicle request
									if (!voMapCabDriverVehicle.isDropOnly()) {
										if (!HRMSHelper.isNullOrEmpty(voMapCabDriverVehicle.getReturnDriver())
												&& !HRMSHelper.isNullOrEmpty(voMapCabDriverVehicle.getReturnVehicle())) {
											returnDriver = driverDAO.findDriverById(
													voMapCabDriverVehicle.getReturnDriver().getId(), IHRMSConstants.isActive);
											returnVehicle = vehicleDAO.findVehicleById(
													voMapCabDriverVehicle.getReturnVehicle().getId(), IHRMSConstants.isActive);
											if (!HRMSHelper.isNullOrEmpty(returnDriver)
													&& !HRMSHelper.isNullOrEmpty(returnVehicle)) {
												// find existing map cab driver and modifying if updated
												MapCabDriverVehicle mapCabDriverVehiclReturn = mapCabDriverVehicleDAO
														.findMapCabDriverByCabReqIdTripWay(cabRequestPassengerId,
																voMapCabDriverVehicle.isRecurring(),
																IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_RETURN,
																IHRMSConstants.isActive);
												if (HRMSHelper.isNullOrEmpty(mapCabDriverVehiclReturn)) {
													// adding new mapping of one way
													MapCabDriverVehicle mapCabDriverVehicle = new MapCabDriverVehicle();
													mapCabDriverVehicle.setCabRequestPassengerId(cabRequestPassengerId);
													mapCabDriverVehicle.setDriverId(returnDriver);
													mapCabDriverVehicle.setVehicleId(returnVehicle);
													mapCabDriverVehicle
															.setTripWay(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_RETURN);
													mapCabDriverVehicle.setCreatedDate(new Date());
													mapCabDriverVehicle.setIsActive(IHRMSConstants.isActive);
													mapCabDriverVehicle.setRecurring(voMapCabDriverVehicle.isRecurring());
													mapCabDriverVehicle.setCreatedBy(voMapCabDriverVehicle.getDoneBy());
													mapCabDriverVehicleDAO.save(mapCabDriverVehicle);
												} else {
													if (!(!HRMSHelper.isNullOrEmpty(mapCabDriverVehiclReturn.getDriverId())
															&& !HRMSHelper
																	.isNullOrEmpty(mapCabDriverVehiclReturn.getVehicleId())
															&& mapCabDriverVehiclReturn.getDriverId().getId() == returnDriver
																	.getId()
															&& mapCabDriverVehiclReturn.getVehicleId().getId() == returnVehicle
																	.getId())) {
														// request modified
														mapCabDriverVehiclReturn.setIsActive(IHRMSConstants.isNotActive);
														mapCabDriverVehiclReturn
																.setUpdatedBy(voMapCabDriverVehicle.getDoneBy());
														mapCabDriverVehiclReturn.setUpdatedDate(new Date());
														mapCabDriverVehicleDAO.save(mapCabDriverVehiclReturn);
		
														MapCabDriverVehicle mapCabDriverVehicle = new MapCabDriverVehicle();
														mapCabDriverVehicle.setCabRequestPassengerId(cabRequestPassengerId);
														mapCabDriverVehicle.setDriverId(returnDriver);
														mapCabDriverVehicle.setVehicleId(returnVehicle);
														mapCabDriverVehicle
																.setTripWay(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_RETURN);
														mapCabDriverVehicle.setCreatedDate(new Date());
														mapCabDriverVehicle.setIsActive(IHRMSConstants.isActive);
														mapCabDriverVehicle.setRecurring(voMapCabDriverVehicle.isRecurring());
														mapCabDriverVehicle.setCreatedBy(voMapCabDriverVehicle.getDoneBy());
														mapCabDriverVehicleDAO.save(mapCabDriverVehicle);
													} else {
														// no modification in request
													}
												}
											} /*
												 * else { throw new HRMSException(
												 * IHRMSConstants.TRAVEL_REQUEST_DRIVER_VEHICLE_NOT_FOUND_ERROR_CODE,
												 * IHRMSConstants.TRAVEL_REQUEST_DRIVER_VEHICLE_NOT_FOUND_ERROR_MESSAGE); }
												 */
										} /*
											 * else { throw new HRMSException(
											 * IHRMSConstants.TRAVEL_REQUEST_DRIVER_VEHICLE_NOT_FOUND_ERROR_CODE,
											 * IHRMSConstants.TRAVEL_REQUEST_DRIVER_VEHICLE_NOT_FOUND_ERROR_MESSAGE); }
											 */
									}
							
							}
							// send mail to requester
							// requesterid = cabRequest.getTravelRequest().getEmployeeId().getId();
							Employee employeeRequester = employeeDAO
									.findById(cabRequest.getTravelRequest().getEmployeeId().getId()).get();
							if (!HRMSHelper.isNullOrEmpty(employeeRequester)) {
								String travelDeskEmployeesEmailIDs = "";
								List<MasterTraveldeskApprover> travelDeskEmployee = travelDeskApproverDAO
										.findTravelDeskEmployeeOrgWise(
												employeeRequester.getCandidate().getLoginEntity().getOrganization()
														.getId(),
												employeeRequester.getCandidate().getCandidateProfessionalDetail()
														.getDivision().getId(),
												IHRMSConstants.APPROVER_TYPE_TRAVELDESK, IHRMSConstants.isActive);

								if (!HRMSHelper.isNullOrEmpty(travelDeskEmployee)) {
									for (MasterTraveldeskApprover approver : travelDeskEmployee) {
										travelDeskEmployeesEmailIDs = travelDeskEmployeesEmailIDs
												+ approver.getEmployee().getOfficialEmailId() + ";";
									}
								} else {
									throw new HRMSException(IHRMSConstants.DataNotFoundCode,
											IHRMSConstants.TRAVEL_DESK_EMPLOYEE_NOT_FOUND);
								}

								TravelRequest travelRequestForm = travelRequestDAO.findByRequestId(
										cabRequest.getTravelRequest().getId(), IHRMSConstants.isActive);
								String messageBody = HRMSHelper.replaceString(
										travelDeskServiceHelper.createMailContentForCabDetails(travelRequestForm,
												cabReqPassenger, cabRecReq, voMapCabDriverVehicle.isRecurring(), driver,
												vehicle, returnDriver, returnVehicle, employeeRequester,
												IHRMSConstants.CAB_DETAIL_MAIL_TYPE_BOOK_CAB),
										IHRMSEmailTemplateConstants.TEMPLATE_TD_TO_REQUESTER_ASSIGNED_CAB_DETAILS);

								String subject = IHRMSConstants.ASSIGN_CAB_DETAILS_MAIL_SUBJECT;
								String isMailWithAttachment = IHRMSConstants.isNotActive;
								emailSenderDAO.toPersistEmail(employeeRequester.getOfficialEmailId(),
										travelDeskEmployeesEmailIDs.trim(), messageBody, subject,
										employeeRequester.getCandidate().getCandidateProfessionalDetail().getDivision()
												.getId(),
										employeeRequester.getCandidate().getLoginEntity().getOrganization().getId(),
										isMailWithAttachment, "", "");
							} else {
								throw new HRMSException(IHRMSConstants.DataNotFoundCode,
										IHRMSConstants.DataNotFoundMessage);
							}
						} else {
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.APPROVER_NOT_FOUND_MSG);
						}
					}

					// Changed by SSW on 29Aug2018
					// Response changed. Returns parent request **** START
					/*
					 * TravelRequest travelReq = null; if
					 * (!HRMSHelper.isNullOrEmpty(cabRequest.getTravelRequest())) { travelReq =
					 * travelRequestDAO.findByRequestId(cabRequest.getTravelRequest().getId(),
					 * IHRMSConstants.isActive); if (!HRMSHelper.isNullOrEmpty(travelReq)) {
					 * VOTravelRequest travelRequestModel = HRMSEntityToModelMapper
					 * .convertToTravelRequestModel(travelReq); String name = ""; Employee employee
					 * = employeeDAO.findById(travelReq.getEmployeeId().getId());
					 * 
					 * if (travelRequestModel != null && travelRequestModel.getTicketRequest() !=
					 * null) {
					 * 
					 * // Getting TravelRequest Headers // Map<String,List<Object>> header =
					 * getTravelRequestHeader(travelRequestModel); //
					 * travelRequestModel.setHeaders(header);
					 * 
					 * List<TraveldeskDocument> ticketDocumentList = travelDeskDocumentDAO
					 * .getDocumentsUsingparentId(travelReq.getId(),
					 * travelRequestModel.getTicketRequest().getId(),
					 * IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, IHRMSConstants.isActive);
					 * List<VOTravelDeskDocument> ticketDocument =
					 * getDocumentModelForChild(ticketDocumentList, travelReq, employee,
					 * IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
					 * travelRequestModel.getTicketRequest().setTicketDocument(ticketDocument); }
					 * 
					 * 
					 * // Getting Document For Accommodation Request if available
					 * 
					 * if (travelRequestModel != null &&
					 * travelRequestModel.getAccommodationRequest() != null) {
					 * 
					 * List<TraveldeskDocument> accommodationDocuments = travelDeskDocumentDAO
					 * .getDocumentsUsingparentId(travelReq.getId(),
					 * travelRequestModel.getAccommodationRequest().getId(),
					 * IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION,
					 * IHRMSConstants.isActive); List<VOTravelDeskDocument> accommodationDocument =
					 * getDocumentModelForChild( accommodationDocuments, travelReq, employee,
					 * IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
					 * travelRequestModel.getAccommodationRequest()
					 * .setAccommodationDocument(accommodationDocument); }
					 * 
					 * if (employee != null && employee.getCandidate() != null) {
					 * 
					 * name = HRMSHelper.convertNullToEmpty(employee.getCandidate().getFirstName())
					 * + " " // +
					 * HRMSHelper.convertNullToEmpty(employee.getCandidate().getMiddleName()) + " //
					 * " + HRMSHelper.convertNullToEmpty( employee.getCandidate().getLastName() +
					 * " - " + employee.getId()); logger.info("Name :: " + name); }
					 * travelRequestModel.setCreatedBy(name);
					 * 
					 * if (!HRMSHelper.isNullOrEmpty(travelRequestModel) &&
					 * !HRMSHelper.isNullOrEmpty(travelRequestModel.getCabRequest()) &&
					 * !HRMSHelper.isNullOrEmpty(
					 * travelRequestModel.getCabRequest().getCabRequestPassengers())) { for
					 * (Iterator<VOCabRequestPassenger> iterator =
					 * travelRequestModel.getCabRequest() .getCabRequestPassengers().iterator();
					 * iterator.hasNext();) { VOCabRequestPassenger cabReqPassengerItr =
					 * iterator.next(); if (!HRMSHelper.isNullOrEmpty(cabReqPassengerItr)) { if
					 * (cabReqPassengerItr.isRecurring()) { // recurring request if (!HRMSHelper
					 * .isNullOrEmpty(cabReqPassengerItr.getCabRecurringRequests())) { for
					 * (Iterator<VOCabRecurringRequest> iteratorRecur = cabReqPassengerItr
					 * .getCabRecurringRequests().iterator(); iteratorRecur .hasNext();) {
					 * VOCabRecurringRequest cabRecRecItr = iteratorRecur.next(); if
					 * (!HRMSHelper.isNullOrEmpty(cabRecRecItr)) { List<MapCabDriverVehicle>
					 * mapCabDriverVehList = mapCabDriverVehicleDAO
					 * .findMapCabDriverByCabReqId(cabRecRecItr.getId(),
					 * cabReqPassengerItr.isRecurring(), IHRMSConstants.isActive); if
					 * (!HRMSHelper.isNullOrEmpty(mapCabDriverVehList)) {
					 * cabRecRecItr.setMapCabDriverVehicles(HRMSResponseTranslator
					 * .translateToVOMapCabDriverVehicle( mapCabDriverVehList)); } } } } } else { //
					 * non recurring single request List<MapCabDriverVehicle> mapCabDriverVehList =
					 * mapCabDriverVehicleDAO
					 * .findMapCabDriverByCabReqId(cabReqPassengerItr.getId(),
					 * cabReqPassengerItr.isRecurring(), IHRMSConstants.isActive); if
					 * (!HRMSHelper.isNullOrEmpty(mapCabDriverVehList)) {
					 * cabReqPassengerItr.setMapCabDriverVehicles(HRMSResponseTranslator
					 * .translateToVOMapCabDriverVehicle(mapCabDriverVehList)); } } } } }
					 * 
					 * if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB,
					 * travelReq, travelRequestModel)) travelRequestModel.setDisableCab(true);
					 * 
					 * if (checkIfChildIsCancelled(IHRMSConstants.
					 * TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION, travelReq, travelRequestModel))
					 * travelRequestModel.setDisableAccommodation(true);
					 * 
					 * if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET,
					 * travelReq, travelRequestModel)) travelRequestModel.setDisableTicket(true);
					 * HRMSListResponseObject response = new HRMSListResponseObject(); List<Object>
					 * list = new ArrayList<Object>(); list.add(travelRequestModel);
					 * response.setListResponse(list);
					 * response.setResponseCode(IHRMSConstants.successCode);
					 * response.setResponseMessage(IHRMSConstants.successMessage); return
					 * HRMSHelper.createJsonString(response); } }
					 */
					// Returns parent request **** END
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			return HRMSHelper.sendSuccessResponse(IHRMSConstants.CAB_DETAILS_ASSIGNED_SUCCESSFULLY,
					IHRMSConstants.successCode);
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

	/**
	 * This method returns true if child is available for cancellation
	 */
	public boolean checkIfChildIsCancelled(String childType, TravelRequest travelRequest, VOTravelRequest model) {

		if (childType.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB)) {

			if (travelRequest != null && travelRequest.getCabRequest() != null) {
				CancelTravelRequest childCabCheck = cancelTravelRequestDAO.findCancelRequestByChildReq(
						travelRequest.getCabRequest().getId(), travelRequest.getId(),
						IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB, IHRMSConstants.isActive);

				if (childCabCheck == null
						&& travelRequest.getCabRequest().getIsActive().equalsIgnoreCase(IHRMSConstants.isNotActive)) {
					model.setCabRequest(null);
					logger.info("Inside isActive check ticket");
				}

				if (childCabCheck != null) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		if (childType.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION)) {

			if (travelRequest != null && travelRequest.getAccommodationRequest() != null) {
				CancelTravelRequest childCabCheck = cancelTravelRequestDAO.findCancelRequestByChildReq(
						travelRequest.getAccommodationRequest().getId(), travelRequest.getId(),
						IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION, IHRMSConstants.isActive);

				if (childCabCheck == null && travelRequest.getAccommodationRequest().getIsActive()
						.equalsIgnoreCase(IHRMSConstants.isNotActive)) {
					model.setAccommodationRequest(null);
					logger.info("Inside isActive check ticket");
				}

				if (childCabCheck != null) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		if (childType.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET)) {

			if (travelRequest != null && travelRequest.getTicketRequest() != null) {
				CancelTravelRequest childCabCheck = cancelTravelRequestDAO.findCancelRequestByChildReq(
						travelRequest.getTicketRequest().getId(), travelRequest.getId(),
						IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, IHRMSConstants.isActive);

				if (childCabCheck == null && travelRequest.getTicketRequest().getIsActive()
						.equalsIgnoreCase(IHRMSConstants.isNotActive)) {
					model.setTicketRequest(null);
					logger.info("Inside isActive check ticket");
				}

				if (childCabCheck != null) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * This method will return the list of documents model available for child
	 * request will return null if not available any document
	 */
	public List<VOTravelDeskDocument> getDocumentModelForChild(List<TraveldeskDocument> travelDocumentList,
			TravelRequest travelRequest, Employee employee, String childType) {

		List<VOTravelDeskDocument> travelDocumentModelList = null;
		if (!HRMSHelper.isNullOrEmpty(travelDocumentList != null)) {
			travelDocumentModelList = new ArrayList<VOTravelDeskDocument>();
			for (TraveldeskDocument accommodationDoc : travelDocumentList) {

				VOTravelDeskDocument travelDeskDocumentModel = HRMSEntityToModelMapper
						.convertToTraveldeskDocumentModel(accommodationDoc);
				String path = "";

				if (employee != null) {

					path = rootDirectory + employee.getCandidate().getLoginEntity().getOrganization().getId()
							+ "/" + IHRMSConstants.TRAVELFOLDERNAME + "/" + travelRequest.getId() + "/" + childType
							+ "/" + travelDeskDocumentModel.getDocumentName();
				}
				travelDeskDocumentModel.setLocation(path);
				travelDocumentModelList.add(travelDeskDocumentModel);
			}
		}
		return travelDocumentModelList;
	}

	/**
	 * This Service will send email notification to Requester with attachment of
	 * ticket or file uploaded
	 */
	@RequestMapping(value = "sendEmailNotification/{requestId}/{childType}", method = {
			RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String sendEmailNotificationToRequestor(@PathVariable("requestId") long requestId,
			@PathVariable("childType") String childType) {

		try {
			TravelRequest travelRequest = travelRequestDAO.findByRequestId(requestId, IHRMSConstants.isActive);
			if (travelRequest != null) {

				Map<String, String> contentMap = new HashMap<String, String>();
				long divisionId = 0;
				long organizationId = 0;
				Employee employee = null;
				String travelDeskEmployeesEmailIDs = "";
				String recipientEmailId = "";
				String subject = "";

				if (travelRequest.getEmployeeId() != null) {
					employee = employeeDAO.findActiveEmployeeById(travelRequest.getEmployeeId().getId(),
							IHRMSConstants.isActive);
				} else {
					logger.info(" Requestor Employee Not Provided ");
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
				}

				if (employee != null) {
					String name = HRMSHelper.convertNullToEmpty(employee.getCandidate().getFirstName()) + " "
							+ HRMSHelper.convertNullToEmpty(employee.getCandidate().getMiddleName()) + " "
							+ HRMSHelper.convertNullToEmpty(employee.getCandidate().getLastName());
					contentMap.put("{employeeName}", name);
					recipientEmailId = employee.getOfficialEmailId();
					divisionId = employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
					organizationId = employee.getCandidate().getLoginEntity().getOrganization().getId();
				} else {
					logger.info(" Requestor Employee Not Found In System ");
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
				}

				List<MasterTraveldeskApprover> travelDeskEmployee = travelDeskApproverDAO.findTravelDeskEmployeeOrgWise(
						organizationId, divisionId, IHRMSConstants.APPROVER_TYPE_TRAVELDESK, IHRMSConstants.isActive);

				if (!HRMSHelper.isNullOrEmpty(travelDeskEmployee)) {
					for (MasterTraveldeskApprover approver : travelDeskEmployee) {
						travelDeskEmployeesEmailIDs = travelDeskEmployeesEmailIDs
								+ approver.getEmployee().getOfficialEmailId() + ";";
					}
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode,
							IHRMSConstants.TRAVEL_DESK_EMPLOYEE_NOT_FOUND);
				}

				if (childType.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET)) {

					TicketRequest ticket = travelRequest.getTicketRequest();

					if (ticket != null) {

						String messageBody = HRMSHelper.replaceString(contentMap,
								IHRMSEmailTemplateConstants.TEMPLATE_NOTIFICATION_TO_REQUESTOR);

						subject = IHRMSConstants.NOTIFICATION_MAIL_SUBJECT_TICKET;// "Travel Request :: Ticket Details
						String isMailWithAttachment = IHRMSConstants.isActive;

						List<TraveldeskDocument> documentList = travelDeskDocumentDAO.getDocumentsUsingparentId(
								travelRequest.getId(), ticket.getId(), IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET,
								IHRMSConstants.isActive);

						String attachment = "";
						if (!HRMSHelper.isNullOrEmpty(documentList)) {
							for (TraveldeskDocument doc : documentList) {
								attachment = attachment + doc.getDocumentName() + ";";
							}
						} else {
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.TICKET_ATTACHMENT_DETAILS_NOT_AVAILABLE);
						}

						String path = rootDirectory + organizationId + "\\" + IHRMSConstants.TRAVELFOLDERNAME + "\\"
								+ travelRequest.getId() + "\\" + IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET;

						emailSenderDAO.toPersistEmail(recipientEmailId, travelDeskEmployeesEmailIDs.trim(), messageBody,
								subject, divisionId, organizationId, isMailWithAttachment, attachment, path);

					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode,
								IHRMSConstants.TICKET_DETAILS_NOT_AVAILABLE);
					}
				} else if (childType.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION)) {
					AccommodationRequest accommodation = travelRequest.getAccommodationRequest();
					if (accommodation != null) {

						String messageBody = HRMSHelper.replaceString(contentMap,
								IHRMSEmailTemplateConstants.TEMPLATE_NOTIFICATION_TO_REQUESTOR);
						subject = IHRMSConstants.NOTIFICATION_MAIL_SUBJECT_ACCOMMODATION;// "Travel Request :: Ticket
																							// Details
						// ";
						String isMailWithAttachment = IHRMSConstants.isActive;

						List<TraveldeskDocument> documentList = travelDeskDocumentDAO.getDocumentsUsingparentId(
								travelRequest.getId(), accommodation.getId(),
								IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION, IHRMSConstants.isActive);

						String attachment = "";
						if (!HRMSHelper.isNullOrEmpty(documentList)) {
							for (TraveldeskDocument doc : documentList) {
								attachment = attachment + doc.getDocumentName() + ";";
							}
						} else {
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.ACCOMMODATION_ATTACHMENT_DETAILS_NOT_AVAILABLE);
						}
						String path = rootDirectory + organizationId + "\\" + IHRMSConstants.TRAVELFOLDERNAME + "\\"
								+ travelRequest.getId() + "\\" + IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION;

						emailSenderDAO.toPersistEmail(recipientEmailId, travelDeskEmployeesEmailIDs.trim(), messageBody,
								subject, divisionId, organizationId, isMailWithAttachment, attachment, path);
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode,
								IHRMSConstants.ACCOMMODATION_DETAILS_NOT_AVAILABLE);
					}
				} else {
					throw new HRMSException(IHRMSConstants.InvalidActionCode, IHRMSConstants.INVALID_CHILD_TYPE);
				}
			}
			return HRMSHelper.sendSuccessResponse(IHRMSConstants.TD_EMAIL_NOTIFICATION_SENT_SUCCESFULLY,
					IHRMSConstants.successCode);
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

	/*
	 * public List<Object> getHeaderRows(String screen) {
	 * 
	 * List<ColHeader> colHeaderList = colHeaderDAO.findByScreenName(screen,
	 * IHRMSConstants.isActive); List<Object> colHeaderMapList = new
	 * ArrayList<Object>(); if (!HRMSHelper.isNullOrEmpty(colHeaderList)) {
	 * 
	 * for (ColHeader header : colHeaderList) { Map<String, Object> colHeadersMap =
	 * new HashMap<String, Object>(); colHeadersMap.put("property",
	 * header.getProperty()); colHeadersMap.put("header", header.getHeader());
	 * colHeadersMap.put("sortable", header.isSortable());
	 * colHeadersMap.put("resizable", header.isResiazable());
	 * colHeaderMapList.add(colHeadersMap); }
	 * 
	 * } return colHeaderMapList; }
	 */

	/*
	 * public Map<String, List<Object>> getTravelRequestHeader(VOTravelRequest
	 * travelRequestModel) throws JsonGenerationException, JsonMappingException,
	 * IOException {
	 *//**
		 * Setting up Col Headers for all the Date Tables Date -: 09-08-2018
		 */

	/*
	
	*//**
		 * Either Setting up once variable in travel request model class and set all the
		 * headers required in the map or setting up specific header object in each
		 * cab,accommodation and ticket request form
		 *//*
			 * 
			 * Map<String, List<Object>> travelrequestHeader = new HashMap<String,
			 * List<Object>>();
			 * 
			 * if (travelRequestModel != null) {
			 * 
			 * if (travelRequestModel.getCabRequest() != null) { if
			 * (!HRMSHelper.isNullOrEmpty(travelRequestModel.getCabRequest().
			 * getCabRequestPassengers())) {
			 * travelRequestModel.getCabRequest().setCabPassengerColHeaders(getHeaderRows(
			 * "SCR-CAB-PASSENGER")); travelrequestHeader.put("SCR-CAB-PASSENGER",
			 * getHeaderRows("SCR-CAB-PASSENGER")); } }
			 * 
			 * if (travelRequestModel.getAccommodationRequest() != null) {
			 * 
			 * travelrequestHeader.put("ACCOMMODATION-DOC-HEADERS",
			 * getHeaderRows("ACCOMMODATION-DOC-HEADERS"));
			 * 
			 * if (!HRMSHelper.isNullOrEmpty(travelRequestModel.getAccommodationRequest().
			 * getAccommodationGuests())) { travelRequestModel.getAccommodationRequest()
			 * .setAccommodationGuestColHeaders(getHeaderRows("SCR-ACCOMMODATION-PASSENGER")
			 * ); travelrequestHeader.put("SCR-ACCOMMODATION-PASSENGER",
			 * getHeaderRows("SCR-ACCOMMODATION-PASSENGER")); } }
			 * 
			 * if (travelRequestModel.getTicketRequest() != null) { if
			 * (!HRMSHelper.isNullOrEmpty(travelRequestModel.getTicketRequest().
			 * getTicketRequestPassengers())) { travelRequestModel.getTicketRequest()
			 * .setTicketPassengerColHeaders(getHeaderRows("SCR-TICKET-PASSENGER"));
			 * travelrequestHeader.put("SCR-TICKET-PASSENGER",
			 * getHeaderRows("SCR-TICKET-PASSENGER")); } } } return travelrequestHeader; }
			 */

	@RequestMapping(value = "journeycomplete/{passengerId}/{isRecurring}/{typeofTravel}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllDriverDetails(@PathVariable("passengerId") long passengerId,
			@PathVariable("isRecurring") String isrecurring, @PathVariable("typeofTravel") String typeofTravel) {

		try {
			logger.info(" *** Inside Complete Journey of Passenger  *** ");

			if (!HRMSHelper.isLongZero(passengerId) && !HRMSHelper.isNullOrEmpty(isrecurring)) {
				Employee employeeRequester = null;
				TravelRequest travelRequest = null;
				CabRecurringRequest cabReqReq = null;
				CabRequestPassenger cabReqPassanger = null;
				if (Boolean.parseBoolean(isrecurring)) {
					CabRecurringRequest cabRecurringRequest = cabRecurringRequestDAO.findById(passengerId).get();

					if (!HRMSHelper.isNullOrEmpty(cabRecurringRequest)) {

						if (typeofTravel.equalsIgnoreCase(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_ONE_WAY))
							cabRecurringRequest.setOneWayTripStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_COMPLETED);
						else
							cabRecurringRequest.setReturnTripStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_COMPLETED);
						cabRecurringRequestDAO.save(cabRecurringRequest);
						employeeRequester = cabRecurringRequest.getCabRequestPassenger().getCabRequest()
								.getTravelRequest().getEmployeeId();
						travelRequest = travelRequestDAO.findById(cabRecurringRequest.getCabRequestPassenger()
								.getCabRequest().getTravelRequest().getId()).get();
						cabReqReq = cabRecurringRequest;
						cabReqPassanger = cabRecurringRequest.getCabRequestPassenger();
					} else {
						throw new HRMSException(IHRMSConstants.PASSENGER_ERROR_CODE,
								IHRMSConstants.TRAVEL_REQUEST_PASSENGER_NOT_FOUND);
					}
				} else {
					CabRequestPassenger cabRequestPassenger = cabRequestPassengerDAO.findById(passengerId).get();
					if (!HRMSHelper.isNullOrEmpty(cabRequestPassenger)) {

						if (typeofTravel.equalsIgnoreCase(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_ONE_WAY))
							cabRequestPassenger.setOneWayTripStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_COMPLETED);
						else
							cabRequestPassenger.setReturnTripStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_COMPLETED);

						cabRequestPassengerDAO.save(cabRequestPassenger);
						employeeRequester = cabRequestPassenger.getCabRequest().getTravelRequest().getEmployeeId();
						travelRequest = travelRequestDAO
								.findById(cabRequestPassenger.getCabRequest().getTravelRequest().getId()).get();
						cabReqPassanger = cabRequestPassenger;
					} else {
						throw new HRMSException(IHRMSConstants.PASSENGER_ERROR_CODE,
								IHRMSConstants.TRAVEL_REQUEST_PASSENGER_NOT_FOUND);
					}
				}
				// send mail to requester
				// requesterid = cabRequest.getTravelRequest().getEmployeeId().getId();
				if (!HRMSHelper.isNullOrEmpty(employeeRequester)) {
					String travelDeskEmployeesEmailIDs = "";
					List<MasterTraveldeskApprover> travelDeskEmployee = travelDeskApproverDAO
							.findTravelDeskEmployeeOrgWise(
									employeeRequester.getCandidate().getLoginEntity().getOrganization().getId(),
									employeeRequester.getCandidate().getCandidateProfessionalDetail().getDivision()
											.getId(),
									IHRMSConstants.APPROVER_TYPE_TRAVELDESK, IHRMSConstants.isActive);

					if (!HRMSHelper.isNullOrEmpty(travelDeskEmployee)) {
						for (MasterTraveldeskApprover approver : travelDeskEmployee) {
							travelDeskEmployeesEmailIDs = travelDeskEmployeesEmailIDs
									+ approver.getEmployee().getOfficialEmailId() + ";";
						}
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode,
								IHRMSConstants.TRAVEL_DESK_EMPLOYEE_NOT_FOUND);
					}

					String messageBody = HRMSHelper.replaceString(
							travelDeskServiceHelper.createMailContentForCabDetails(travelRequest, cabReqPassanger,
									cabReqReq, Boolean.parseBoolean(isrecurring), null, null, null, null,
									employeeRequester, IHRMSConstants.CAB_DETAIL_MAIL_TYPE_JOURNEY_COMPLETE),
							IHRMSEmailTemplateConstants.TEMPLATE_TO_TD_JOURNEY_COMPLETE_CAB_DETAILS);

					String subject = IHRMSConstants.CAB_DETAIL_MAIL_SUBJECT_JOURNEY_COMPLETE;
					String isMailWithAttachment = IHRMSConstants.isNotActive;
					emailSenderDAO.toPersistEmail(employeeRequester.getOfficialEmailId(),
							travelDeskEmployeesEmailIDs.trim(), messageBody, subject,
							employeeRequester.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							employeeRequester.getCandidate().getLoginEntity().getOrganization().getId(),
							isMailWithAttachment, "", "");
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
				return HRMSHelper.sendSuccessResponse(IHRMSConstants.TRAVEL_REQUEST_JOURNEY_COMPLETED_MESSAGE,
						IHRMSConstants.successCode);

			} else {
				throw new HRMSException(IHRMSConstants.InValidDetailsCode, IHRMSConstants.InvalidDetailsMessage);
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

	@RequestMapping(value = "getAllRequestPost", method = {
			RequestMethod.POST }, produces = "application/json")
	@ResponseBody
	public String getAllRequestPost(@RequestBody VORequestParamForTravelList voReqParamTravelList) {

		long organizationId = voReqParamTravelList.getOrganizationId();
		int page = voReqParamTravelList.getPage();
		int size = voReqParamTravelList.getSize();
		try {

			if (size <= 0) {
				size = IHRMSConstants.DefaultPageSize;
			}

			List<TravelRequest> travelRequestList = travelRequestDAO.findAllTravelRequest(IHRMSConstants.isActive,
					IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING, IHRMSConstants.TRAVEL_REQUEST_STATUS_WIP,
					IHRMSConstants.TRAVEL_REQUEST_STATUS_REJECTED, organizationId /*, new PageRequest(page, size) */);

			if (!HRMSHelper.isNullOrEmpty(travelRequestList)) {

				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> list = new ArrayList<Object>();
				for (TravelRequest travelRequest : travelRequestList) {
					if (travelRequest != null) {

						Employee employee = employeeDAO.findActiveEmployeeById(travelRequest.getEmployeeId().getId(),
								IHRMSConstants.isActive);
						VOTravelRequest travelRequestModel = HRMSEntityToModelMapper
								.convertToTravelRequestModel(travelRequest);

						/**
						 * Getting Document For Ticket Request if available
						 */
						if (travelRequestModel != null && travelRequestModel.getTicketRequest() != null) {

							List<TraveldeskDocument> ticketDocumentList = travelDeskDocumentDAO
									.getDocumentsUsingparentId(travelRequest.getId(),
											travelRequestModel.getTicketRequest().getId(),
											IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, IHRMSConstants.isActive);
							List<VOTravelDeskDocument> ticketDocument = getDocumentModelForChild(ticketDocumentList,
									travelRequest, employee, IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
							travelRequestModel.getTicketRequest().setTicketDocument(ticketDocument);
						}

						/**
						 * Getting Document For Accommodation Request if available
						 */
						if (travelRequestModel != null && travelRequestModel.getAccommodationRequest() != null) {

							if (travelRequestModel != null && travelRequestModel.getAccommodationRequest() != null) {

								List<TraveldeskDocument> accommodationDocuments = travelDeskDocumentDAO
										.getDocumentsUsingparentId(travelRequest.getId(),
												travelRequestModel.getAccommodationRequest().getId(),
												IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION,
												IHRMSConstants.isActive);
								List<VOTravelDeskDocument> accommodationDocument = getDocumentModelForChild(
										accommodationDocuments, travelRequest, employee,
										IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
								travelRequestModel.getAccommodationRequest()
										.setAccommodationDocument(accommodationDocument);
							}
						}

						/*
						 * finding cab details
						 */

						if (!HRMSHelper.isNullOrEmpty(travelRequestModel)
								&& !HRMSHelper.isNullOrEmpty(travelRequestModel.getCabRequest()) && !HRMSHelper
										.isNullOrEmpty(travelRequestModel.getCabRequest().getCabRequestPassengers())) {
							for (Iterator<VOCabRequestPassenger> iterator = travelRequestModel.getCabRequest()
									.getCabRequestPassengers().iterator(); iterator.hasNext();) {
								VOCabRequestPassenger cabReqPassengerItr = iterator.next();
								if (!HRMSHelper.isNullOrEmpty(cabReqPassengerItr)) {
									if (cabReqPassengerItr.isRecurring()) {
										// recurring request

										Comparator<VOCabRecurringRequest> mycomparator = Collections.reverseOrder();
										Collections.sort(cabReqPassengerItr.getCabRecurringRequests(), mycomparator);

										if (!HRMSHelper.isNullOrEmpty(cabReqPassengerItr.getCabRecurringRequests())) {
											for (Iterator<VOCabRecurringRequest> iteratorRecur = cabReqPassengerItr
													.getCabRecurringRequests().iterator(); iteratorRecur.hasNext();) {
												VOCabRecurringRequest cabRecRecItr = iteratorRecur.next();
												if (!HRMSHelper.isNullOrEmpty(cabRecRecItr)) {
													List<MapCabDriverVehicle> mapCabDriverVehList = mapCabDriverVehicleDAO
															.findMapCabDriverByCabReqId(cabRecRecItr.getId(),
																	cabReqPassengerItr.isRecurring(),
																	IHRMSConstants.isActive);
													if (!HRMSHelper.isNullOrEmpty(mapCabDriverVehList)) {
														cabRecRecItr.setMapCabDriverVehicles(HRMSResponseTranslator
																.translateToVOMapCabDriverVehicle(mapCabDriverVehList));
													}
												}
											}
										}
									} else {
										// non recurring single request
										List<MapCabDriverVehicle> mapCabDriverVehList = mapCabDriverVehicleDAO
												.findMapCabDriverByCabReqId(cabReqPassengerItr.getId(),
														cabReqPassengerItr.isRecurring(), IHRMSConstants.isActive);
										if (!HRMSHelper.isNullOrEmpty(mapCabDriverVehList)) {
											cabReqPassengerItr.setMapCabDriverVehicles(HRMSResponseTranslator
													.translateToVOMapCabDriverVehicle(mapCabDriverVehList));
										}
									}
								}
							}
						}

						/**
						 * Added - 21-08-2018 :: Start
						 **/
						/*
						 * if (travelRequest.getCabRequest() != null) { if
						 * (travelRequest.getCabRequest().getIsActive().equalsIgnoreCase(IHRMSConstants.
						 * isActive)) { travelRequestModel.setBookCab(true); }
						 * 
						 * }
						 * 
						 * if (travelRequest.getAccommodationRequest() != null) { if
						 * (travelRequest.getAccommodationRequest().getIsActive()
						 * .equalsIgnoreCase(IHRMSConstants.isActive)) {
						 * travelRequestModel.setBookAccommodation(true); } }
						 * 
						 * if (travelRequest.getTicketRequest() != null) { if
						 * (travelRequest.getTicketRequest().getIsActive()
						 * .equalsIgnoreCase(IHRMSConstants.isActive)) {
						 * travelRequestModel.setBookTicket(true); }
						 * 
						 * }
						 */

						/**
						 * End
						 */

						int cancelledCount = 0;
						int bookedCount = 0;

						if (travelRequest.isBookCab()) {
							bookedCount++;
						}

						if (travelRequest.isBookAccommodation()) {
							bookedCount++;
						}

						if (travelRequest.isBookTicket()) {
							bookedCount++;
						}

						if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB, travelRequest,
								travelRequestModel)) {
							cancelledCount++;
							travelRequestModel.setDisableCab(true);
						}

						if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION,
								travelRequest, travelRequestModel)) {
							cancelledCount++;
							travelRequestModel.setDisableAccommodation(true);
						}

						if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, travelRequest,
								travelRequestModel)) {
							cancelledCount++;
							travelRequestModel.setDisableTicket(true);
						}

						int activeCount = bookedCount - cancelledCount;
						if (activeCount < 0)
							activeCount = 0;

						String completeStatus = "Active : " + activeCount + " | " + "Cancelled :" + cancelledCount;
						travelRequestModel.setRequestSummaryCount(completeStatus.replace("-", ""));

						list.add(travelRequestModel);
					}

				}
				response.setListResponse(list);
				response.setPageNo(page);
				response.setPageSize(size);
				response.setTotalCount(travelRequestList.size());
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(response);

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

	
	@RequestMapping(value = "getUpcomingRequest/{organizationId}/{page}/{size}", method = {
			RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String getUpcomingRequest(@PathVariable("page") int page, @PathVariable("size") int size,
			@PathVariable("organizationId") long organizationId) throws JsonGenerationException, JsonMappingException, IOException, HRMSException {
		try {

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, upcomingReqNoOfDays);

			HRMSListResponseObject response = new HRMSListResponseObject();
			List<Object> list = new ArrayList<Object>();

			List<TravelRequest> upcomingTravelRequestForTicket = travelRequestDAO.getUpcomingTicketRequest(
					IHRMSConstants.isActive, calendar.getTime(), IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_PENDING,
					IHRMSConstants.TRAVEL_REQUEST_STATUS_WIP, organizationId);
			List<TravelRequest> upcomingTravelRequestForCab = travelRequestDAO.getUpcomingCabRequest(
					IHRMSConstants.isActive, calendar.getTime(), IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_PENDING,
					IHRMSConstants.TRAVEL_REQUEST_STATUS_WIP, organizationId);
			List<TravelRequest> upcomingTravelRequestForAccommodation = travelRequestDAO.getUpcomingAccommodation(
					IHRMSConstants.isActive, calendar.getTime(), IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_PENDING,
					IHRMSConstants.TRAVEL_REQUEST_STATUS_WIP, organizationId);

			for (TravelRequest e : upcomingTravelRequestForTicket) {
				VOTravelRequest model = HRMSEntityToModelMapper.convertToTravelRequestModelForApprover(e,
						IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
				String name = "";
				if (model != null && model.getEmployeeId() != null && model.getEmployeeId().getCandidate() != null) {
					name = HRMSHelper.convertNullToEmpty(model.getEmployeeId().getCandidate().getFirstName()) + " "
							+ HRMSHelper.convertNullToEmpty(model.getEmployeeId().getCandidate().getLastName() + " "
									+ model.getEmployeeId().getId());
				}
				//model.setCreatedBy(name);
				model.setRequestedBy(name);
				list.add(model);
			}

			List<Long> idChecklist = new ArrayList<Long>();
			for (TravelRequest entity : upcomingTravelRequestForCab) {

				if (!idChecklist.contains(entity.getId())) {
					VOTravelRequest model = HRMSEntityToModelMapper.convertToTravelRequestModelForApprover(entity,
							IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB);
					idChecklist.add(model.getId());

					String name = "";
					if (model != null && model.getEmployeeId() != null
							&& model.getEmployeeId().getCandidate() != null) {
						name = HRMSHelper.convertNullToEmpty(model.getEmployeeId().getCandidate().getFirstName()) + " "
								+ HRMSHelper.convertNullToEmpty(model.getEmployeeId().getCandidate().getLastName() + " "
										+ model.getEmployeeId().getId());
					}
					
					//model.setCreatedBy(name);
					model.setRequestedBy(name);
					list.add(model);
				}
			}

			for (TravelRequest e : upcomingTravelRequestForAccommodation) {
				VOTravelRequest model = HRMSEntityToModelMapper.convertToTravelRequestModelForApprover(e,
						IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);

				String name = "";
				if (model != null && model.getEmployeeId() != null && model.getEmployeeId().getCandidate() != null) {
					name = HRMSHelper.convertNullToEmpty(model.getEmployeeId().getCandidate().getFirstName()) + " "
							+ HRMSHelper.convertNullToEmpty(model.getEmployeeId().getCandidate().getLastName() + " "
									+ model.getEmployeeId().getId());
				}
				//model.setCreatedBy(name);
				model.setRequestedBy(name);
				list.add(model);
			}

			if (HRMSHelper.isNullOrEmpty(list)) {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}

			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			response.setListResponse(list);
			return HRMSHelper.createJsonString(response);
			
		}catch (HRMSException e) {
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
	
	
	
	/**
	 * Added By RK for sending only limited parameters in response
	 */
	
	@RequestMapping(value = "getAllRequestforTD/{organizationId}/{page}/{size}", method = {
			RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String getAllRequestNew(@PathVariable("page") int page, @PathVariable("size") int size,
			@PathVariable("organizationId") long organizationId) {

		try {

			if (size <= 0) {
				size = IHRMSConstants.DefaultPageSize;
			}

			List<TravelRequest> travelRequestList = travelRequestDAO.findAllTravelRequest(IHRMSConstants.isActive,
					IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING, IHRMSConstants.TRAVEL_REQUEST_STATUS_WIP,
					IHRMSConstants.TRAVEL_REQUEST_STATUS_REJECTED, organizationId /* , new PageRequest(page, size) */);

			if (!HRMSHelper.isNullOrEmpty(travelRequestList)) {

				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> list = new ArrayList<Object>();
				for (TravelRequest travelRequest : travelRequestList) {
					if (travelRequest != null) {

					/*	Employee employee = employeeDAO.findActiveEmployeeById(travelRequest.getEmployeeId().getId(),
								IHRMSConstants.isActive);*/
						
						Employee employee = employeeDAO.findById(travelRequest.getEmployeeId().getId()).get();
						
						
						VOTravelRequestDispaly voTravelRequestModel= HRMSEntityToModelMapper
								.convertoToVOTravelRequest(travelRequest);
						
						String requestedBy=employee.getCandidate().getFirstName()+" "+employee.getCandidate().getLastName()+" -"+employee.getId();
						voTravelRequestModel.setRequestedBy(requestedBy);
						
						
						VOTravelRequest travelRequestModel = HRMSEntityToModelMapper
								.convertToTravelRequestModel(travelRequest);

						int cancelledCount = 0;
						int bookedCount = 0;

						if (travelRequest.isBookCab()) {
							bookedCount++;
						}

						if (travelRequest.isBookAccommodation()) {
							bookedCount++;
						}

						if (travelRequest.isBookTicket()) {
							bookedCount++;
						}

						if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB, travelRequest,
								travelRequestModel)) {
							cancelledCount++;
							travelRequestModel.setDisableCab(true);
						}

						if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION,
								travelRequest, travelRequestModel)) {
							cancelledCount++;
							travelRequestModel.setDisableAccommodation(true);
						}

						if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, travelRequest,
								travelRequestModel)) {
							cancelledCount++;
							travelRequestModel.setDisableTicket(true);
						}

						int activeCount = bookedCount - cancelledCount;
						if (activeCount < 0)
							activeCount = 0;

						String completeStatus = "Active : " + activeCount + " | " + "Cancelled :" + cancelledCount;
						travelRequestModel.setRequestSummaryCount(completeStatus.replace("-", ""));
						
						voTravelRequestModel.setRequestSummaryCount(completeStatus.replace("-", ""));

						list.add(voTravelRequestModel);
					}

				}
				response.setListResponse(list);
				response.setPageNo(page);
				response.setPageSize(size);
				response.setTotalCount(travelRequestList.size());
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(response);

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

	
	
	@RequestMapping(value = "getFormDetails/{travelRequestId}", method = {
			RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String getFormDetails(@PathVariable("travelRequestId") long travelRequId) {

		try {
			
			if(!HRMSHelper.isLongZero(travelRequId))
			{
				TravelRequest travelRequest=travelRequestDAO.findById(travelRequId).get();
				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> list = new ArrayList<Object>();
				Employee employee = employeeDAO.findActiveEmployeeById(travelRequest.getEmployeeId().getId(),
						IHRMSConstants.isActive);
				VOTravelRequest travelRequestModel = HRMSEntityToModelMapper
						.convertToTravelRequestModel(travelRequest);
			
				
				
				/**
				 * Getting Document For Ticket Request if available
				 */
				if (travelRequestModel != null && travelRequestModel.getTicketRequest() != null) {

					List<TraveldeskDocument> ticketDocumentList = travelDeskDocumentDAO
							.getDocumentsUsingparentId(travelRequest.getId(),
									travelRequestModel.getTicketRequest().getId(),
									IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, IHRMSConstants.isActive);
					List<VOTravelDeskDocument> ticketDocument = getDocumentModelForChild(ticketDocumentList,
							travelRequest, employee, IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
					travelRequestModel.getTicketRequest().setTicketDocument(ticketDocument);
				}

				/**
				 * Getting Document For Accommodation Request if available
				 */
				if (travelRequestModel != null && travelRequestModel.getAccommodationRequest() != null) {

					if (travelRequestModel != null && travelRequestModel.getAccommodationRequest() != null) {

						List<TraveldeskDocument> accommodationDocuments = travelDeskDocumentDAO
								.getDocumentsUsingparentId(travelRequest.getId(),
										travelRequestModel.getAccommodationRequest().getId(),
										IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION,
										IHRMSConstants.isActive);
						List<VOTravelDeskDocument> accommodationDocument = getDocumentModelForChild(
								accommodationDocuments, travelRequest, employee,
								IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
						travelRequestModel.getAccommodationRequest()
								.setAccommodationDocument(accommodationDocument);
					}
				}

				/*
				 * finding cab details
				 */

				if (!HRMSHelper.isNullOrEmpty(travelRequestModel)
						&& !HRMSHelper.isNullOrEmpty(travelRequestModel.getCabRequest()) && !HRMSHelper
								.isNullOrEmpty(travelRequestModel.getCabRequest().getCabRequestPassengers())) {
					for (Iterator<VOCabRequestPassenger> iterator = travelRequestModel.getCabRequest()
							.getCabRequestPassengers().iterator(); iterator.hasNext();) {
						VOCabRequestPassenger cabReqPassengerItr = iterator.next();
						if (!HRMSHelper.isNullOrEmpty(cabReqPassengerItr)) {
							if (cabReqPassengerItr.isRecurring()) {
								// recurring request

								Comparator<VOCabRecurringRequest> mycomparator = Collections.reverseOrder();
								Collections.sort(cabReqPassengerItr.getCabRecurringRequests(), mycomparator);

								if (!HRMSHelper.isNullOrEmpty(cabReqPassengerItr.getCabRecurringRequests())) {
									for (Iterator<VOCabRecurringRequest> iteratorRecur = cabReqPassengerItr
											.getCabRecurringRequests().iterator(); iteratorRecur.hasNext();) {
										VOCabRecurringRequest cabRecRecItr = iteratorRecur.next();
										if (!HRMSHelper.isNullOrEmpty(cabRecRecItr)) {
											List<MapCabDriverVehicle> mapCabDriverVehList = mapCabDriverVehicleDAO
													.findMapCabDriverByCabReqId(cabRecRecItr.getId(),
															cabReqPassengerItr.isRecurring(),
															IHRMSConstants.isActive);
											if (!HRMSHelper.isNullOrEmpty(mapCabDriverVehList)) {
												cabRecRecItr.setMapCabDriverVehicles(HRMSResponseTranslator
														.translateToVOMapCabDriverVehicle(mapCabDriverVehList));
											}
										}
									}
								}
							} else {
								// non recurring single request
								List<MapCabDriverVehicle> mapCabDriverVehList = mapCabDriverVehicleDAO
										.findMapCabDriverByCabReqId(cabReqPassengerItr.getId(),
												cabReqPassengerItr.isRecurring(), IHRMSConstants.isActive);
								if (!HRMSHelper.isNullOrEmpty(mapCabDriverVehList)) {
									cabReqPassengerItr.setMapCabDriverVehicles(HRMSResponseTranslator
											.translateToVOMapCabDriverVehicle(mapCabDriverVehList));
								}
							}
						}
					}
				}

				int cancelledCount = 0;
				int bookedCount = 0;

				if (travelRequest.isBookCab()) {
					bookedCount++;
				}

				if (travelRequest.isBookAccommodation()) {
					bookedCount++;
				}

				if (travelRequest.isBookTicket()) {
					bookedCount++;
				}

				if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB, travelRequest,
						travelRequestModel)) {
					cancelledCount++;
					travelRequestModel.setDisableCab(true);
				}

				if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION,
						travelRequest, travelRequestModel)) {
					cancelledCount++;
					travelRequestModel.setDisableAccommodation(true);
				}

				if (checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, travelRequest,
						travelRequestModel)) {
					cancelledCount++;
					travelRequestModel.setDisableTicket(true);
				}

				int activeCount = bookedCount - cancelledCount;
				if (activeCount < 0)
					activeCount = 0;

				String completeStatus = "Active : " + activeCount + " | " + "Cancelled :" + cancelledCount;
				travelRequestModel.setRequestSummaryCount(completeStatus.replace("-", ""));

				list.add(travelRequestModel);
				
				
				
				response.setListResponse(list);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(response);
			}
	       else {
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
	
	/**
	 * This service will provide the manager subordinate brief details under the
	 * provided manager id provided,if manager id is 0 then it will search all the
	 * employee under the provided organization
	 * 
	 * This service will by utilized for HR and Manager
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/findSubordinate/{orgId}/{managerId}", produces = "application/json")
	public String findEmployeeIdAndNameForManager(@PathVariable("orgId") long orgId,
			@PathVariable("managerId") long managerId)
			throws JsonGenerationException, JsonMappingException, IOException, HRMSException {
		try {
			HRMSListResponseObject hrmsListResponseObject = null;
			List<Object> voEmployees = new ArrayList<Object>();
			if (!HRMSHelper.isLongZero(orgId)) {
				if (managerId > 0) {
					logger.info("Finding Subordinate For Manager : ");
					List<EmployeeReportingManager> reportingManager = reportingManagerDAO
							.findByreporingManager(managerId);
					for (EmployeeReportingManager rptmgr : reportingManager) {
						if (rptmgr.getEmployee().getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {

							VOEmployee employeeModel = HRMSEntityToModelMapper
									.convertToEmployeeModel(rptmgr.getEmployee());
							voEmployees.add(employeeModel);
						}
					}
				} else {
					logger.info("Finding All , Based On Organization  ... ");
					List<Employee> employees = employeeDAO.getAllEmpNameIdByOrg(orgId);
					if (!HRMSHelper.isNullOrEmpty(employees)) {
						for (Employee employeeEntity : employees) {
							VOEmployee employeeModel = HRMSEntityToModelMapper.convertToEmployeeModel(employeeEntity);
							voEmployees.add(employeeModel);
						}

					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
				}
				
				/**
				 * Below code is used to get Employees from another organization
				 */
				
				
				
				
				if (voEmployees.isEmpty()) {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			hrmsListResponseObject = new HRMSListResponseObject();
			hrmsListResponseObject.setListResponse(voEmployees);
			hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
			hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
			return HRMSHelper.createJsonString(hrmsListResponseObject);
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
