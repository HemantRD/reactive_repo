package com.vinsys.hrms.services.traveldesk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSAccommodationRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCancelTravelRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTicketRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTravelRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTraveldeskApproverDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTraveldeskCommentDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelCancelRequestModel;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOTraveldeskComment;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.traveldesk.AccommodationRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequest;
import com.vinsys.hrms.entity.traveldesk.CancelTravelRequest;
import com.vinsys.hrms.entity.traveldesk.MasterTraveldeskApprover;
import com.vinsys.hrms.entity.traveldesk.TicketRequest;
import com.vinsys.hrms.entity.traveldesk.TravelRequest;
import com.vinsys.hrms.entity.traveldesk.TraveldeskComment;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/travelRequestCancellation")
//@PropertySource(value="${HRMSCONFIG}")
public class TravelRequestCancellationService {

	public static final Logger logger = LoggerFactory.getLogger(TravelRequestCancellationService.class);

	@Autowired
	IHRMSTravelRequestDAO travelRequestDAO;
	@Autowired
	IHRMSCancelTravelRequestDAO cancelTravelRequestDAO;
	@Autowired
	IHRMSTraveldeskCommentDAO commentDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSTraveldeskApproverDAO travelDeskApproverDAO;
	@Autowired
	TravelRequestService travelRequestServiceObj ;
	@Autowired
	TravelRequestService travelRequestObj;
	@Autowired
	EmailSender emailSenderDAO;
	@Autowired
	IHRMSTicketRequestDAO ticketDAO;
	@Autowired
	IHRMSCabRequestDAO cabDAO;
	@Autowired
	IHRMSAccommodationRequestDAO accommodationDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String saveRequest(@RequestBody VOTravelCancelRequestModel cancellationRequest) {

		try {
			if (cancellationRequest != null) {

				TravelRequest travelRequest = travelRequestDAO.findByRequestId(cancellationRequest.getParentId(),
						IHRMSConstants.isActive);
				
				Employee requestorEmployee = null;
				long organizationId = 0;
				long divisionId = 0;
				if (travelRequest != null) {

					List<VOTraveldeskComment> commentModelList = null;

					if (!cancellationRequest.isCancelAccommodation() && !cancellationRequest.isCancelCab()
							&& !cancellationRequest.isCancelTicket()) {
						throw new HRMSException(IHRMSConstants.INVALID_REQ_CODE,
								IHRMSConstants.INVALID_CANCELLATION_REQUEST_MESSAGE);
					}

					

					if (travelRequest.getEmployeeId() != null) {
						requestorEmployee = employeeDAO.findActiveEmployeeById(travelRequest.getEmployeeId().getId(),
								IHRMSConstants.isActive);
						organizationId = requestorEmployee.getCandidate().getLoginEntity().getOrganization().getId();
						divisionId = requestorEmployee.getCandidate().getCandidateProfessionalDetail().getDivision()
								.getId();
					}

					Employee commentedByEmployee = null;
					List<TraveldeskComment> travelDeskComment = null;

					List<CancelTravelRequest> cancelTravelRequestList = new ArrayList<CancelTravelRequest>();
					if (!HRMSHelper.isNullOrEmpty(cancellationRequest.getCancelReqComment())) {
						if (!HRMSHelper.isNullOrEmpty(cancellationRequest.getCancelReqComment().getEmployee())) {

							commentedByEmployee = employeeDAO.findActiveEmployeeById(
									cancellationRequest.getCancelReqComment().getEmployee().getId(),
									IHRMSConstants.isActive);

							travelDeskComment = new ArrayList<TraveldeskComment>();
						} else {
							throw new HRMSException(IHRMSConstants.InsufficientDataCode,
									IHRMSConstants.InsufficientDataMessage);
						}
					}

					List<TicketRequest> ticketCancellationList = null;
					
					String getMailContentFor = "";
					
					if (cancellationRequest.isCancelTicket()) {
						
						if(!travelRequest.isBookTicket()) 
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,IHRMSConstants.TICKET_DETAILS_NOT_AVAILABLE_TO_CANCEL);
						
						cancelTicketRequest(cancellationRequest, travelRequest, commentedByEmployee, travelDeskComment,
								cancelTravelRequestList,ticketCancellationList);
						getMailContentFor = getMailContentFor + IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET + " ";
					}

					List<CabRequest> cabRequestList = null;
					if (cancellationRequest.isCancelCab()) {
						
						if(!travelRequest.isBookCab()) 
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,IHRMSConstants.CAB_DETAILS_NOT_AVAILABLE_TO_CANCEL);
						
						cancelCabRequest(cancellationRequest, travelRequest, commentedByEmployee, travelDeskComment,
								cancelTravelRequestList,cabRequestList);
						getMailContentFor = getMailContentFor +  IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB + " ";
					}   

					List<AccommodationRequest> accomodationList = null;
					if (cancellationRequest.isCancelAccommodation()) {
						
						if(!travelRequest.isBookAccommodation()) 
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,IHRMSConstants.ACCOMMODATION_DETAILS_NOT_AVAILABLE_TO_CANCEL);
						
						cancelAccommodationRequest(cancellationRequest, travelRequest, commentedByEmployee,
								travelDeskComment, cancelTravelRequestList,accomodationList);
						getMailContentFor = getMailContentFor +  IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION + " ";
					}
					
					
					if (travelRequest.getTravelStatus()
							.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING)) {
						logger.info(" :: Canceling The Entire Request If All Child Request Are CANCELLED::");

						int bookedCount = 0;
						int cancelledCount = 0;

						/**
						 * Counting Booking Count
						 */
						if (travelRequest.isBookCab()) {
							bookedCount++;
						}

						if (travelRequest.isBookAccommodation()) {
							bookedCount++;
						}

						if (travelRequest.isBookTicket()) {
							bookedCount++;
						}

						/**
						 * Counting Cancellation Count
						 */

						if (cancellationRequest.isCancelCab()) {
							cancelledCount++;
						}

						if (cancellationRequest.isCancelAccommodation()) {
							cancelledCount++;
						}

						if (cancellationRequest.isCancelTicket()) {
							cancelledCount++;
						}

						if (cancelledCount == bookedCount) {
							/*
							 * If Booked Count is equal To Cancellation Count then status to be changed qith
							 * InActive
							 */
							travelRequest.setIsActive(IHRMSConstants.isNotActive);
							travelRequest.setUpdatedBy(cancellationRequest.getCreatedBy());
							travelRequest.setUpdatedDate(new Date());
							travelRequestDAO.save(travelRequest);
						}
						}
					
					/**
					 * Saving Cancellation Details
					 */

					/**
					 * Needs  Discussion for boolean values changes,if cancelled then do i need to change iscabbooked,isTicketbooked,isAccommodation booked,
					 * logic to display data in cancellation screen is failing due to changing the booleanvalue
					 * */
					
					/*if (cancellationRequest.isCancelAccommodation() && travelRequest.isBookAccommodation()) {
						travelRequest.setBookAccommodation(false);
					}

					if (cancellationRequest.isCancelCab() && travelRequest.isBookCab()) {
						travelRequest.setBookCab(false);
					}

					if (cancellationRequest.isCancelTicket() && travelRequest.isBookTicket()) {
						travelRequest.setBookTicket(false);
					}*/
					
					travelRequest.setUpdatedBy(cancellationRequest.getCreatedBy());
					travelRequest.setUpdatedDate(new Date());
					travelRequestDAO.save(travelRequest);
					
					/**
					 * Saving Cancellation Details
					 */
					
					if (!HRMSHelper.isNullOrEmpty(accomodationList)) {
						accommodationDAO.saveAll(accomodationList);
					}

					if (!HRMSHelper.isNullOrEmpty(cabRequestList)) {
						cabDAO.saveAll(cabRequestList);
					}

					if (!HRMSHelper.isNullOrEmpty(ticketCancellationList)) {
						ticketDAO.saveAll(ticketCancellationList);
					}
					
					if (!HRMSHelper.isNullOrEmpty(travelDeskComment))
						commentDAO.saveAll(travelDeskComment);
					
					cancelTravelRequestDAO.saveAll(cancelTravelRequestList);
					
					if (!HRMSHelper.isNullOrEmpty(travelDeskComment)) {
						for (TraveldeskComment commentEntity : travelDeskComment) {
							commentModelList = new ArrayList<VOTraveldeskComment>();
							VOTraveldeskComment commentModel = new VOTraveldeskComment();
							commentModel.setComment(commentEntity.getComment());
							commentModelList.add(commentModel);
						}
					}

					/**
					 * Email Sender Code
					 */
					List<MasterTraveldeskApprover> masterTravelDeskApproverList = travelDeskApproverDAO
							.findTravelDeskEmployeeOrgWise(organizationId, divisionId,
									IHRMSConstants.APPROVER_TYPE_TRAVELDESK, IHRMSConstants.isActive);

					String email_traveldesk = "";

					if (!HRMSHelper.isNullOrEmpty(masterTravelDeskApproverList)) {
						for (MasterTraveldeskApprover mstTdApprovr : masterTravelDeskApproverList) {
							Employee travelDeskEmployeeObj = employeeDAO.findById(mstTdApprovr.getEmployee().getId()).get();
							email_traveldesk = email_traveldesk + travelDeskEmployeeObj.getOfficialEmailId() + ";";
						}
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.NO_TD_EMPLOYEE_FOUND);
					}

					Map<String, String> map = travelRequestServiceObj.createMailContent(travelRequest, null,
							getMailContentFor, commentModelList);

					map.put("{ticket}", HRMSHelper.toConvertBooleanToHumanReadable(cancellationRequest.isCancelTicket()));
					map.put("{cab}", HRMSHelper.toConvertBooleanToHumanReadable(cancellationRequest.isCancelCab()));
					map.put("{accomodation}",
							HRMSHelper.toConvertBooleanToHumanReadable(cancellationRequest.isCancelAccommodation()));
					
					String mailContent = HRMSHelper.replaceString(map,
							IHRMSEmailTemplateConstants.TEMPLATE_REQUEST_CANCELLATION);

					String subject = IHRMSConstants.TRAVEL_REQUEST_CANCEL_SUBJECT+" | Request ID : "+travelRequest.getSeqId();
					emailSenderDAO.toPersistEmail(email_traveldesk, requestorEmployee.getOfficialEmailId(), mailContent,
							subject, divisionId, organizationId,
							IHRMSConstants.IS_MAIL_WITH_ATTACHMENT_N, null);

					
					
					travelRequest = travelRequestDAO.findById(travelRequest.getId()).get();
					HRMSListResponseObject response = new HRMSListResponseObject();
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.TRAVEL_REQ_CANCELLATION_SUCCESS);
					
					List<Object> list = new ArrayList<Object>();
					VOTravelRequest  travelRequestModel = HRMSEntityToModelMapper.convertToTravelRequestModel(travelRequest);
					String name = "";
					
					if (travelRequestModel != null && travelRequestModel.getEmployeeId() != null
							&& travelRequestModel.getEmployeeId().getCandidate() != null)
						name = HRMSHelper
								.convertNullToEmpty(travelRequestModel.getEmployeeId().getCandidate().getFirstName()) + " "
					+ HRMSHelper.convertNullToEmpty(travelRequestModel.getEmployeeId().getCandidate().getLastName()) + " "+travelRequestModel.getEmployeeId().getId();
					
					travelRequestModel.setCreatedBy(name);
					
					list.add(travelRequestModel);
					response.setListResponse(list);
					
					return HRMSHelper.createJsonString(response);
				} else {
					logger.info(" :: Travel Details Not Found :: ");
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.TRAVEL_REQUEST_NOT_FOUND);
				}
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

	private void cancelAccommodationRequest(VOTravelCancelRequestModel cancellationRequest, TravelRequest travelReuqest,
			Employee commentedByEmployee, List<TraveldeskComment> travelDeskComment,
			List<CancelTravelRequest> cancelTravelRequestList, List<AccommodationRequest> accommodationRequestList)
			throws HRMSException {

		CancelTravelRequest cancelTravelEntity;
		logger.info(" :: Cancelling Accommodation Request :: ");
		AccommodationRequest accommodation = travelReuqest.getAccommodationRequest();

		if (travelReuqest.getTravelStatus().equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING)) {
			if (accommodation != null) {
				accommodationRequestList = new ArrayList<AccommodationRequest>();
				accommodation.setIsActive(IHRMSConstants.isNotActive);
				accommodation.setUpdatedDate(new Date());
				accommodation.setUpdatedBy(cancellationRequest.getCreatedBy());
				accommodationRequestList.add(accommodation);
				// accommodationDAO.save(accommodation);
			}
		} else {
			if (accommodation != null) {

				if (!accommodation.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {
					throw new HRMSException(IHRMSConstants.DataAlreadyExist,
							IHRMSConstants.ACCOMMODATION_ALREADY_CANCELLED);
				}

				CancelTravelRequest dataCheck = cancelTravelRequestDAO.findCancelRequestByChildReq(
						accommodation.getId(), travelReuqest.getId(),
						IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION, IHRMSConstants.isActive);

				if (dataCheck != null) {
					throw new HRMSException(IHRMSConstants.DataAlreadyExist,
							IHRMSConstants.ACCOMMODATION_ALREADY_CANCELLED);
				}

				cancelTravelEntity = createCancellationEntity(travelReuqest, accommodation.getId(),
						IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION, cancellationRequest);

				cancelTravelRequestList.add(cancelTravelEntity);
			} else {
				logger.info(" :: No Ticket Details Found For The Provided Travel Details ::");
				throw new HRMSException(IHRMSConstants.DataNotFoundCode,
						IHRMSConstants.ACCOMMODATION_DETAILS_NOT_AVAILABLE_TO_CANCEL);
			}
		}

		TraveldeskComment accommodationComment = createTravelDeskCommentEntity(travelReuqest, cancellationRequest,
				commentedByEmployee, IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION, accommodation.getId());

		if (!HRMSHelper.isNullOrEmpty(cancellationRequest.getCancelReqComment()))
			travelDeskComment.add(accommodationComment);

	}

	private void cancelCabRequest(VOTravelCancelRequestModel cancellationRequest, TravelRequest travelReuqest,
			Employee commentedByEmployee, List<TraveldeskComment> travelDeskComment,
			List<CancelTravelRequest> cancelTravelRequestList, List<CabRequest> cabRequestList) throws HRMSException {
		CancelTravelRequest cancelTravelEntity;
		logger.info(" :: Cancelling Cab Request :: ");
		CabRequest cabRequest = travelReuqest.getCabRequest();
		/**
		 * Added For Setting InActive To Child Request
		 */
		if (travelReuqest.getTravelStatus().equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING)) {
			if (cabRequest != null) {
				cabRequestList = new ArrayList<CabRequest>();
				cabRequest.setIsActive(IHRMSConstants.isNotActive);
				cabRequest.setUpdatedDate(new Date());
				cabRequest.setUpdatedBy(cancellationRequest.getCreatedBy());
				cabRequestList.add(cabRequest);
				// cabDAO.save(cabRequest);
			}
		} else {
			if (cabRequest != null) {

				if (!cabRequest.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {
					throw new HRMSException(IHRMSConstants.DataAlreadyExist, IHRMSConstants.CAB_ALREADY_CANCELLED);
				}

				CancelTravelRequest dataCheck = cancelTravelRequestDAO.findCancelRequestByChildReq(cabRequest.getId(),
						travelReuqest.getId(), IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB, IHRMSConstants.isActive);

				if (dataCheck != null) {
					throw new HRMSException(IHRMSConstants.DataAlreadyExist, IHRMSConstants.CAB_ALREADY_CANCELLED);
				}

				cancelTravelEntity = createCancellationEntity(travelReuqest, cabRequest.getId(),
						IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB, cancellationRequest);

				TraveldeskComment cabComment = createTravelDeskCommentEntity(travelReuqest, cancellationRequest,
						commentedByEmployee, IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB, cabRequest.getId());

				if (!HRMSHelper.isNullOrEmpty(cancellationRequest.getCancelReqComment()))
					travelDeskComment.add(cabComment);

				cancelTravelRequestList.add(cancelTravelEntity);
			} else {
				logger.info(" :: No Ticket Details Found For The Provided Travel Details ::");
				throw new HRMSException(IHRMSConstants.DataNotFoundCode,
						IHRMSConstants.CAB_DETAILS_NOT_AVAILABLE_TO_CANCEL);
			}

		}
		TraveldeskComment cabComment = createTravelDeskCommentEntity(travelReuqest, cancellationRequest,
				commentedByEmployee, IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB, cabRequest.getId());

		if (!HRMSHelper.isNullOrEmpty(cancellationRequest.getCancelReqComment()))
			travelDeskComment.add(cabComment);
	}

	private void cancelTicketRequest(VOTravelCancelRequestModel cancellationRequest, TravelRequest travelReuqest,
			Employee commentedByEmployee, List<TraveldeskComment> travelDeskComment,
			List<CancelTravelRequest> cancelTravelRequestList, List<TicketRequest> ticketRequestList)
			throws HRMSException {

		CancelTravelRequest cancelTravelEntity;
		logger.info(" :: Cancelling Ticket Request :: ");
		TicketRequest ticketRequest = travelReuqest.getTicketRequest();

		/**
		 * Added For Setting InActive To Child Request
		 */
		if (travelReuqest.getTravelStatus().equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING)) {
			if (ticketRequest != null) {
				ticketRequestList = new ArrayList<TicketRequest>();
				ticketRequest.setIsActive(IHRMSConstants.isNotActive);
				ticketRequest.setUpdatedDate(new Date());
				ticketRequest.setUpdatedBy(cancellationRequest.getCreatedBy());
				ticketRequestList.add(ticketRequest);
				// ticketDAO.save(ticketRequest);
			}

		} else {

			if (ticketRequest != null) {

				if (!ticketRequest.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {
					throw new HRMSException(IHRMSConstants.DataAlreadyExist, IHRMSConstants.TICKET_ALREADY_CANCELLED);
				}
				CancelTravelRequest dataCheck = cancelTravelRequestDAO.findCancelRequestByChildReq(
						ticketRequest.getId(), travelReuqest.getId(), IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET,
						IHRMSConstants.isActive);

				if (dataCheck != null) {
					throw new HRMSException(IHRMSConstants.DataAlreadyExist, IHRMSConstants.TICKET_ALREADY_CANCELLED);
				}

				cancelTravelEntity = createCancellationEntity(travelReuqest, ticketRequest.getId(),
						IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, cancellationRequest);

				cancelTravelRequestList.add(cancelTravelEntity);
			} else {
				logger.info(" :: No Ticket Details Found For The Provided Travel Details ::");
				throw new HRMSException(IHRMSConstants.DataNotFoundCode,
						IHRMSConstants.TICKET_DETAILS_NOT_AVAILABLE_TO_CANCEL);
			}
		}

		TraveldeskComment ticketComment = createTravelDeskCommentEntity(travelReuqest, cancellationRequest,
				commentedByEmployee, IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, ticketRequest.getId());

		if (!HRMSHelper.isNullOrEmpty(cancellationRequest.getCancelReqComment()))
			travelDeskComment.add(ticketComment);
	}

	private CancelTravelRequest createCancellationEntity(TravelRequest travelRequest, long childId, String childType,
			VOTravelCancelRequestModel requestModel) {

		CancelTravelRequest cancelTravelEntity = new CancelTravelRequest();
		cancelTravelEntity.setCreatedBy(requestModel.getCreatedBy());
		cancelTravelEntity.setCreatedDate(new Date());
		cancelTravelEntity.setTravelRequest(travelRequest);
		cancelTravelEntity.setChildId(childId);
		cancelTravelEntity.setChildType(childType);
		cancelTravelEntity.setIsActive(IHRMSConstants.isActive);
		return cancelTravelEntity;
	}

	private TraveldeskComment createTravelDeskCommentEntity(TravelRequest requestEntity,
			VOTravelCancelRequestModel requestModel, Employee requestor, String childType, long childId) {

		TraveldeskComment commentEntity = new TraveldeskComment();
		commentEntity.setTravelRequest(requestEntity);
		commentEntity.setChildType(childType);
		commentEntity.setChildId(childId);
		commentEntity.setEmployee(requestor);
		commentEntity.setCommentator(IHRMSConstants.TRAVEL_REQUEST_COMMENTATOR_REQUESTER);
		commentEntity.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_TRAVEL_CANCEL);
		commentEntity.setComment(requestModel.getCancelReqComment().getComment());
		commentEntity.setCreatedBy(requestModel.getCreatedBy());
		commentEntity.setCreatedDate(new Date());
		commentEntity.setIsActive(IHRMSConstants.isActive);
		return commentEntity;
	}
	
	@RequestMapping(value = "getAllCancelledRequest/{orgId}/{page}/{size}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllCancelledListForTravelDesk(@PathVariable("orgId") long orgId, @PathVariable("page") int page,
			@PathVariable("size") int size) {

		try {

			HRMSListResponseObject listResponse = new HRMSListResponseObject();
			List<Object> listObj = new ArrayList<Object>();

			List<TravelRequest> cancelledTravelRequestList = travelRequestDAO.findAllCancelledTravelRequest(orgId,
					IHRMSConstants.isActive, PageRequest.of(page, size));

			if (cancelledTravelRequestList != null && !cancelledTravelRequestList.isEmpty()) {

				for (TravelRequest travelRequestEntity : cancelledTravelRequestList) {

					if (travelRequestEntity != null) {
						VOTravelRequest travelRequestModel = HRMSEntityToModelMapper
								.convertToTravelRequestModel(travelRequestEntity);

						if (travelRequestModel != null) {
							if (travelRequestObj.checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB,
									travelRequestEntity,travelRequestModel))
								travelRequestModel.setDisableCab(false);
							else
								travelRequestModel.setDisableCab(true);

							if (travelRequestObj.checkIfChildIsCancelled(
									IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION, travelRequestEntity,travelRequestModel))
								travelRequestModel.setDisableAccommodation(false);
							else
								travelRequestModel.setDisableAccommodation(true);

							if (travelRequestObj.checkIfChildIsCancelled(
									IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, travelRequestEntity,travelRequestModel))
								travelRequestModel.setDisableTicket(false);
							else
								travelRequestModel.setDisableTicket(true);

							listObj.add(travelRequestModel);
						}

						listResponse.setListResponse(listObj);
						listResponse.setResponseCode(IHRMSConstants.successCode);
						listResponse.setResponseMessage(IHRMSConstants.successMessage);
					}
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
			return HRMSHelper.createJsonString(listResponse);
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
	
	//employee cancellation
	
	@RequestMapping(value = "getMyCancelledRequest/{orgId}/{employeeId}/{page}/{size}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getMyCancelledRequest(@PathVariable("orgId") long orgId,@PathVariable("employeeId") long employeeId, @PathVariable("page") int page,
			@PathVariable("size") int size) {

		try {

			Employee employee = employeeDAO.findActiveEmployeeById(employeeId, IHRMSConstants.isActive);
			if (employee == null) {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode,
						IHRMSConstants.EmployeeLeaveDetailsNotFoundMessage);
			}

			HRMSListResponseObject listResponse = new HRMSListResponseObject();
			List<Object> listObj = new ArrayList<Object>();

			List<TravelRequest> cancelledTravelRequestList = travelRequestDAO.findMyCancelledTravelRequest(employeeId,
					orgId, IHRMSConstants.isActive, PageRequest.of(page, size));

			if (cancelledTravelRequestList != null && !cancelledTravelRequestList.isEmpty()) {

				for (TravelRequest travelRequestEntity : cancelledTravelRequestList) {

					if (travelRequestEntity != null) {
						VOTravelRequest travelRequestModel = HRMSEntityToModelMapper
								.convertToTravelRequestModel(travelRequestEntity);

						if (travelRequestModel != null) {
							if (travelRequestObj.checkIfChildIsCancelled(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB,
									travelRequestEntity,travelRequestModel))
								travelRequestModel.setDisableCab(false);
							else
								travelRequestModel.setDisableCab(true);

							if (travelRequestObj.checkIfChildIsCancelled(
									IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION, travelRequestEntity,travelRequestModel))
								travelRequestModel.setDisableAccommodation(false);
							else
								travelRequestModel.setDisableCab(true);

							if (travelRequestObj.checkIfChildIsCancelled(
									IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET, travelRequestEntity,travelRequestModel))
								travelRequestModel.setDisableTicket(false);
							else
								travelRequestModel.setDisableCab(true);

							listObj.add(travelRequestModel);
						}

						listResponse.setListResponse(listObj);
						listResponse.setResponseCode(IHRMSConstants.successCode);
						listResponse.setResponseMessage(IHRMSConstants.successMessage);
					}
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
			return HRMSHelper.createJsonString(listResponse);
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
	
}
