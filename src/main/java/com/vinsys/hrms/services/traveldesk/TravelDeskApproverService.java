package com.vinsys.hrms.services.traveldesk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSAccommodationRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTicketRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTravelRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTraveldeskCommentDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.traveldesk.VOApproverActionWithComment;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOTraveldeskApprover;
import com.vinsys.hrms.datamodel.traveldesk.VOTraveldeskApproverListRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOTraveldeskComment;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.traveldesk.AccommodationGuest;
import com.vinsys.hrms.entity.traveldesk.AccommodationRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequestPassenger;
import com.vinsys.hrms.entity.traveldesk.MasterTraveldeskApprover;
import com.vinsys.hrms.entity.traveldesk.TicketRequest;
import com.vinsys.hrms.entity.traveldesk.TicketRequestPassenger;
import com.vinsys.hrms.entity.traveldesk.TravelRequest;
import com.vinsys.hrms.entity.traveldesk.TraveldeskComment;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;

import io.swagger.v3.oas.annotations.Hidden;
@Hidden @RestController
@RequestMapping(path = "/masterTravelApprover")

public class TravelDeskApproverService {

	private static final Logger logger = LoggerFactory.getLogger(TravelDeskApproverService.class);
	
	@Autowired
	com.vinsys.hrms.dao.traveldesk.IHRMSTraveldeskApproverDAO traveldeskApproverDAO;
	@Autowired
	IHRMSAccommodationRequestDAO accommodationRequestDAO;
	@Autowired
	IHRMSTravelRequestDAO travelRequestDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSTraveldeskCommentDAO traveldeskCommentDAO;
	@Autowired
	IHRMSCabRequestDAO cabRequestDAO;
	@Autowired
	IHRMSTicketRequestDAO ticketRequestDAO;
	@Autowired
	EmailSender emailSenderDAO;
	@Autowired
	TravelRequestService travelRequestHelper;

	@RequestMapping(method = RequestMethod.GET, value = "/{orgId}/{divisionId}/{approverType}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllMasterTravelApprover(@PathVariable("orgId") long orgId,
			@PathVariable("divisionId") long divisionId, @PathVariable("approverType") String approvertype) {

		// List<TraveldeskApprover> travelDeskApproverEntityList = new ArrayList<>();
		List<MasterTraveldeskApprover> traveldeskApproverEntity = new ArrayList<MasterTraveldeskApprover>();
		VOTraveldeskApprover voTraveldeskApprover = new VOTraveldeskApprover();
		List<Object> voTraveldeskApproverList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			if (!HRMSHelper.isNullOrEmpty(approvertype)) {
				// traveldeskApproverEntity =
				// traveldeskApproverDAO.findByapproverType(approvertype);
				traveldeskApproverEntity = traveldeskApproverDAO.findLoggedInApproverOrgDivWise(IHRMSConstants.isActive,
						orgId, divisionId, approvertype);

				// findAllMasterTravelApproverByOrg(orgId,IHRMSConstants.isActive);

				if (!HRMSHelper.isNullOrEmpty(traveldeskApproverEntity)) {

					for (MasterTraveldeskApprover approver : traveldeskApproverEntity) {
						voTraveldeskApprover = HRMSResponseTranslator.translateToTraveldeskApproverVo(approver);
						voTraveldeskApproverList.add(voTraveldeskApprover);
					}

					hrmsListResponseObject.setListResponse(voTraveldeskApproverList);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
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

	@RequestMapping(method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getApproverPendingRequests(@RequestBody VOTraveldeskApproverListRequest request) {

		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		List<VOTravelRequest> responseList = new ArrayList<>();
		try {

			if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getEmployeeId())) {

				// List<TicketRequest> ticketRequestList =
				// ticketRequestDAO.findTicketRequestForApprover(IHRMSConstants.isActive, true,
				// request.getEmployeeId(), IHRMSConstants.TD_APPROVER_STATUS_PENDING);
				List<TraveldeskComment> ticketCommentlist = null;
				List<TraveldeskComment> accomodationCommentlist = null;
				List<TraveldeskComment> cabCommentlist = null;

				List<TravelRequest> travelTicketRequestList = travelRequestDAO
						.getPendingTicketRequetsWithCommentsForApprover(IHRMSConstants.isActive, true,
								request.getEmployeeId(), IHRMSConstants.TD_APPROVER_STATUS_PENDING);

				for (TravelRequest travTicketEntity : travelTicketRequestList) {

					logger.info(travTicketEntity.isBookTicket() + " :: " + travTicketEntity.isBookAccommodation()
							+ " :: " + travTicketEntity.isBookCab() + " :: ");
					logger.info(travTicketEntity.getTicketRequest().getId()+"");

					ticketCommentlist = traveldeskCommentDAO.getCommentsOfRequest(
							travTicketEntity.getTicketRequest().getId(),
							IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
					travTicketEntity.setTraveldeskComment(ticketCommentlist);
					Employee empTicket = employeeDAO.findById(travTicketEntity.getEmployeeId().getId()).get();
					VOTravelRequest voTravelRequest = HRMSEntityToModelMapper.convertToTravelRequestModelForApprover(
							travTicketEntity, IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
					voTravelRequest.setRequestedBy(
							empTicket.getCandidate().getFirstName() + " " + empTicket.getCandidate().getLastName());
					responseList.add(voTravelRequest);
				}

				List<TravelRequest> travelAccommodationRequestList = travelRequestDAO
						.getPendingAccommodationRequetsWithCommentsForApprover(IHRMSConstants.isActive, true,
								request.getEmployeeId(), IHRMSConstants.TD_APPROVER_STATUS_PENDING);

				for (TravelRequest travAccoReqEntity : travelAccommodationRequestList) {

					logger.info(travAccoReqEntity.isBookTicket() + " :: " + travAccoReqEntity.isBookAccommodation()
									+ " :: " + travAccoReqEntity.isBookCab() + " :: ");

					accomodationCommentlist = traveldeskCommentDAO.getCommentsOfRequest(
							travAccoReqEntity.getAccommodationRequest().getId(),
							IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
					travAccoReqEntity.setTraveldeskComment(accomodationCommentlist);
					Employee empAcco = employeeDAO.findById(travAccoReqEntity.getEmployeeId().getId()).get();
					VOTravelRequest voTravelRequest = HRMSEntityToModelMapper.convertToTravelRequestModelForApprover(
							travAccoReqEntity, IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
					voTravelRequest.setRequestedBy(
							empAcco.getCandidate().getFirstName() + " " + empAcco.getCandidate().getLastName());
					responseList.add(voTravelRequest);
				}

				List<TravelRequest> travelCabRequestList = travelRequestDAO.getPendingCabRequetsWithCommentsForApprover(
						IHRMSConstants.isActive, true, request.getEmployeeId(),
						IHRMSConstants.TD_APPROVER_STATUS_PENDING);

				for (TravelRequest travCabReqEntity : travelCabRequestList) {

					logger.info(travCabReqEntity.isBookTicket() + " :: " + travCabReqEntity.isBookAccommodation()
							+ " :: " + travCabReqEntity.isBookCab() + " :: ");
					cabCommentlist = traveldeskCommentDAO.getCommentsOfRequest(travCabReqEntity.getCabRequest().getId(),
							IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB);
					travCabReqEntity.setTraveldeskComment(cabCommentlist);
					Employee empCab = employeeDAO.findById(travCabReqEntity.getEmployeeId().getId()).get();
					VOTravelRequest voTravelRequest = HRMSEntityToModelMapper.convertToTravelRequestModelForApprover(
							travCabReqEntity, IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB);
					voTravelRequest.setRequestedBy(
							empCab.getCandidate().getFirstName() + " " + empCab.getCandidate().getLastName());
					responseList.add(voTravelRequest);
				}
				responseList.sort(Comparator.comparing(VOTravelRequest::getCreatedDate));

				List<Object> objectList = new ArrayList<>();
				for (VOTravelRequest travelRequest : responseList) {
					logger.info(HRMSDateUtil.format(travelRequest.getCreatedDate(), "dd/MM/yyyy hh:mm:ss"));
					objectList.add((Object) travelRequest);
				}

				// List<Object> responseObjectlist = getPages(objectList, request.getPageSize(),
				// request.getPageNumber());

				hrmsListResponseObject.setListResponse(objectList);
				// hrmsListResponseObject.setListResponse(responseObjectlist);
				hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
				hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(hrmsListResponseObject);

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

	/**
	 * approver action with comment by TD SSW
	 */
	@RequestMapping(value = "approverActionWithComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String approverActionWithComment(@RequestBody VOApproverActionWithComment voApproverActionWithComment) {
		try {
			if (!HRMSHelper.isNullOrEmpty(voApproverActionWithComment)
					&& !HRMSHelper.isLongZero(voApproverActionWithComment.getTravelRequestId())
					&& !HRMSHelper.isNullOrEmpty(voApproverActionWithComment.getApproverAction())
					&& !HRMSHelper.isNullOrEmpty(voApproverActionWithComment.getComment())
					&& (!HRMSHelper.isLongZero(voApproverActionWithComment.getAccommodationRequest().getId())
							|| !HRMSHelper.isLongZero(voApproverActionWithComment.getCabRequest().getId())
							|| !HRMSHelper.isLongZero(voApproverActionWithComment.getTicketRequest().getId()))) {
				// finding travel request
				TravelRequest travelRequest = travelRequestDAO.findTravelRequestById(
						voApproverActionWithComment.getTravelRequestId(), IHRMSConstants.isActive);
				// finding approver employee
				Employee approverEmp = employeeDAO.findActiveEmployeeById(
						voApproverActionWithComment.getLoggedInEmployee().getId(), IHRMSConstants.isActive);
				StringBuffer travelRequestChild = null;
				if (!HRMSHelper.isNullOrEmpty(travelRequest) && !HRMSHelper.isNullOrEmpty(approverEmp)) {
					if (!HRMSHelper.isLongZero(voApproverActionWithComment.getAccommodationRequest().getId())) {
						AccommodationRequest accReq = accommodationRequestDAO.findAccommodationRequestById(
								voApproverActionWithComment.getAccommodationRequest().getId(), IHRMSConstants.isActive);
						if (!HRMSHelper.isNullOrEmpty(accReq) && !HRMSHelper.isNullOrEmpty(accReq.getApproverStatus())
								&& accReq.getApproverStatus()
										.equalsIgnoreCase(IHRMSConstants.TD_APPROVER_STATUS_PENDING)) {
							travelRequestChild = new StringBuffer(
									IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
							// saving comment
							TraveldeskComment traveldeskComment = new TraveldeskComment();
							traveldeskComment.setTravelRequest(travelRequest);
							traveldeskComment.setEmployee(approverEmp);
							traveldeskComment.setCommentator(IHRMSConstants.TRAVEL_REQUEST_COMMENTATOR_APPROVER);
							if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_APPROVED)) {
								traveldeskComment.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_APPROVED);
							} else if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_REJECTED)) {
								traveldeskComment.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_REJECTED);
							} else {
								throw new HRMSException(IHRMSConstants.INVALID_APPROVER_ACTION_CODE,
										IHRMSConstants.INVALID_APPROVER_ACTION_MESSAGE);
							}
							traveldeskComment.setComment(voApproverActionWithComment.getComment());
							traveldeskComment.setCreatedBy(String.valueOf(approverEmp.getId()));
							traveldeskComment.setCreatedDate(new Date());
							traveldeskComment.setIsActive(IHRMSConstants.isActive);
							traveldeskComment.setChildId(accReq.getId());
							traveldeskComment.setChildType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
							traveldeskCommentDAO.save(traveldeskComment);
							// saving accom request
							if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_APPROVED)) {
								accReq.setApproverStatus(IHRMSConstants.TD_APPROVER_STATUS_APPROVED);
							} else if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_REJECTED)) {
								accReq.setApproverStatus(IHRMSConstants.TD_APPROVER_STATUS_REJECTED);
							} else {
								throw new HRMSException(IHRMSConstants.INVALID_APPROVER_ACTION_CODE,
										IHRMSConstants.INVALID_APPROVER_ACTION_MESSAGE);
							}
							if (!HRMSHelper.isNullOrEmpty(voApproverActionWithComment.getLoggedInEmployee())
									&& !HRMSHelper
											.isLongZero(voApproverActionWithComment.getLoggedInEmployee().getId())) {
								accReq.setUpdatedBy(
										String.valueOf(voApproverActionWithComment.getLoggedInEmployee().getId()));
							}
							accReq.setUpdatedDate(new Date());
							accommodationRequestDAO.save(accReq);

						} else {
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.DataNotFoundMessage);
						}
					} else if (!HRMSHelper.isLongZero(voApproverActionWithComment.getCabRequest().getId())) {
						CabRequest cabReq = cabRequestDAO.findCabRequestById(
								voApproverActionWithComment.getCabRequest().getId(), IHRMSConstants.isActive);
						if (!HRMSHelper.isNullOrEmpty(cabReq)) {
							travelRequestChild = new StringBuffer(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB);
							// saving comment
							TraveldeskComment traveldeskComment = new TraveldeskComment();
							traveldeskComment.setTravelRequest(travelRequest);
							traveldeskComment.setEmployee(approverEmp);
							traveldeskComment.setCommentator(IHRMSConstants.TRAVEL_REQUEST_COMMENTATOR_APPROVER);
							if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_APPROVED)) {
								traveldeskComment.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_APPROVED);
							} else if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_REJECTED)) {
								traveldeskComment.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_REJECTED);
							} else {
								throw new HRMSException(IHRMSConstants.INVALID_APPROVER_ACTION_CODE,
										IHRMSConstants.INVALID_APPROVER_ACTION_MESSAGE);
							}
							traveldeskComment.setComment(voApproverActionWithComment.getComment());
							traveldeskComment.setCreatedBy(String.valueOf(approverEmp.getId()));
							traveldeskComment.setCreatedDate(new Date());
							traveldeskComment.setIsActive(IHRMSConstants.isActive);
							traveldeskComment.setChildId(cabReq.getId());
							traveldeskComment.setChildType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB);
							traveldeskCommentDAO.save(traveldeskComment);
							// saving cab request
							if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_APPROVED)) {
								cabReq.setApproverStatus(IHRMSConstants.TD_APPROVER_STATUS_APPROVED);
							} else if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_REJECTED)) {
								cabReq.setApproverStatus(IHRMSConstants.TD_APPROVER_STATUS_REJECTED);
							} else {
								throw new HRMSException(IHRMSConstants.INVALID_APPROVER_ACTION_CODE,
										IHRMSConstants.INVALID_APPROVER_ACTION_MESSAGE);
							}
							if (!HRMSHelper.isNullOrEmpty(voApproverActionWithComment.getLoggedInEmployee())
									&& !HRMSHelper
											.isLongZero(voApproverActionWithComment.getLoggedInEmployee().getId())) {
								cabReq.setUpdatedBy(
										String.valueOf(voApproverActionWithComment.getLoggedInEmployee().getId()));
							}
							cabReq.setUpdatedDate(new Date());
							cabRequestDAO.save(cabReq);

						} else {
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.DataNotFoundMessage);
						}
					} else if (!HRMSHelper.isLongZero(voApproverActionWithComment.getTicketRequest().getId())) {
						TicketRequest ticketReq = ticketRequestDAO.findTicketRequestById(
								voApproverActionWithComment.getTicketRequest().getId(), IHRMSConstants.isActive);
						if (!HRMSHelper.isNullOrEmpty(ticketReq)) {
							travelRequestChild = new StringBuffer(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
							// saving comment
							TraveldeskComment traveldeskComment = new TraveldeskComment();
							traveldeskComment.setTravelRequest(travelRequest);
							traveldeskComment.setEmployee(approverEmp);
							traveldeskComment.setCommentator(IHRMSConstants.TRAVEL_REQUEST_COMMENTATOR_APPROVER);
							if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_APPROVED)) {
								traveldeskComment.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_APPROVED);
							} else if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_REJECTED)) {
								traveldeskComment.setAction(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_REJECTED);
							} else {
								throw new HRMSException(IHRMSConstants.INVALID_APPROVER_ACTION_CODE,
										IHRMSConstants.INVALID_APPROVER_ACTION_MESSAGE);
							}
							traveldeskComment.setComment(voApproverActionWithComment.getComment());
							traveldeskComment.setCreatedBy(String.valueOf(approverEmp.getId()));
							traveldeskComment.setCreatedDate(new Date());
							traveldeskComment.setIsActive(IHRMSConstants.isActive);
							traveldeskComment.setChildId(ticketReq.getId());
							traveldeskComment.setChildType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
							traveldeskCommentDAO.save(traveldeskComment);
							// saving cab request
							if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_APPROVED)) {
								ticketReq.setApproverStatus(IHRMSConstants.TD_APPROVER_STATUS_APPROVED);
							} else if (voApproverActionWithComment.getApproverAction()
									.equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_REJECTED)) {
								ticketReq.setApproverStatus(IHRMSConstants.TD_APPROVER_STATUS_REJECTED);
							} else {
								throw new HRMSException(IHRMSConstants.INVALID_APPROVER_ACTION_CODE,
										IHRMSConstants.INVALID_APPROVER_ACTION_MESSAGE);
							}
							if (!HRMSHelper.isNullOrEmpty(voApproverActionWithComment.getLoggedInEmployee())
									&& !HRMSHelper
											.isLongZero(voApproverActionWithComment.getLoggedInEmployee().getId())) {
								ticketReq.setUpdatedBy(
										String.valueOf(voApproverActionWithComment.getLoggedInEmployee().getId()));
							}
							ticketReq.setUpdatedDate(new Date());
							ticketRequestDAO.save(ticketReq);

						} else {
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.DataNotFoundMessage);
						}
					}
//
					// send mail to td
					VOTraveldeskComment tdCommentObj = new VOTraveldeskComment();
					tdCommentObj.setComment(voApproverActionWithComment.getComment());
					
					
					
					/*Map<String, String> mailBodyMap = createMailContentApprover(travelRequest, null,
							travelRequestChild.toString(), tdCommentObj,
							voApproverActionWithComment.getApproverAction());*/
					String approverAction = "";
					if(voApproverActionWithComment.getApproverAction().equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_APPROVED)) {
						approverAction = IHRMSConstants.TD_APPROVER_STATUS_APPROVED;
					} else if (voApproverActionWithComment.getApproverAction().equals(IHRMSConstants.TRAVEL_REQUEST_ACTION_APPROVER_REJECTED)) {
						approverAction = IHRMSConstants.TD_APPROVER_STATUS_REJECTED;
					}
					List<VOTraveldeskComment> commentList = new ArrayList<VOTraveldeskComment>();
					commentList.add(tdCommentObj);
					
					Map<String, String> mailBodyMap = travelRequestHelper.createMailContent(travelRequest, null,travelRequestChild.toString(), commentList);
					
					StringBuffer mainDetails = new StringBuffer();
					// mainDetails.append("<br/>");
					mainDetails
							.append("<table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" "
									+ "cellpadding=\"4\"><tbody><tr bgcolor=\"yellow\">");
					mainDetails.append(
							"<td style=\"width: 15%;\">Request Id</td><td style=\"width: 15%;\">Child Type</td>"
							+ "<td style=\"width: 20%;\">Approver Action</td><td style=\"width: 50%;\">Approver Comment</td><td style=\\\"width: 50%;\\\">WON No</td></tr>");
					mainDetails.append(
							"<tr><td style=\"width: 15%;\">" + String.valueOf(travelRequest.getSeqId()) + "</td>"
							+ "<td style=\"width: 15%;\">"+ travelRequestChild.toString() + "</td>"
							+ "<td style=\"width: 20%;\">" + approverAction 
							+"</td><td style=\"width: 50%;\">" + tdCommentObj.getComment() + "</td>"
							+"</td><td style=\"width: 50%;\">" + travelRequest.getWorkOrderNo() + "</td>"
									+ "</tr>");
					mainDetails.append("</tbody></table>");
					mailBodyMap.put("{mainDetails}", mainDetails.toString());
					mailBodyMap.put("{approverName}", approverEmp.getCandidate().getFirstName() + " " + approverEmp.getCandidate().getLastName());
					//travelRequestHelper.createMailContent(travelRequestForm, recipientEmployee, toCreateMailContentFor, comment)
					
					String mailBody = HRMSHelper.replaceString(mailBodyMap,
							IHRMSEmailTemplateConstants.TEMPLATE_TD_APPROVER_ACTION_MAIL);

					long division = approverEmp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
					long organization = approverEmp.getCandidate().getLoginEntity().getOrganization().getId();

					List<MasterTraveldeskApprover> traveldeskApproverEntity = traveldeskApproverDAO
							.findTravelDeskEmployeeOrgWise(organization, division,
									IHRMSConstants.APPROVER_TYPE_TRAVELDESK, IHRMSConstants.isActive);
					StringBuffer tdEmailIds = null;
					for (MasterTraveldeskApprover masterTraveldeskApprover : traveldeskApproverEntity) {
						if (!HRMSHelper.isNullOrEmpty(tdEmailIds)) {
							tdEmailIds.append(";" + masterTraveldeskApprover.getEmployee().getOfficialEmailId());
						} else {
							tdEmailIds = new StringBuffer();
							tdEmailIds.append(masterTraveldeskApprover.getEmployee().getOfficialEmailId());
						}
					}

					String subject = IHRMSConstants.APPROVER_ACTION_MAIL_SUBJECT + "| Request ID " + travelRequest.getSeqId();
					if (!HRMSHelper.isNullOrEmpty(tdEmailIds)) {
						emailSenderDAO.toPersistEmail(tdEmailIds.toString(), "", mailBody,
								subject , division, organization,
								IHRMSConstants.IS_MAIL_WITH_ATTACHMENT_N, null);
						
						
						return HRMSHelper.sendSuccessResponse(IHRMSConstants.APPROVER_ACTION_SUCCESS_MSG,
								IHRMSConstants.successCode);
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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

	public List<Object> getPages(Collection<Object> c, Integer pageSize, int pageNum) {
		if (c == null)
			return Collections.emptyList();
		List<Object> list = new ArrayList<Object>(c);
		if (pageSize == null || pageSize <= 0 || pageSize > list.size()) {
			if (pageSize < 10) {
				pageSize = list.size();
			} else {
				pageSize = 10;
			}
		}

		int numPages = (int) Math.ceil((double) list.size() / (double) pageSize);
		if (pageNum < numPages)
			return list = list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size()));
		else
			return list = new ArrayList<>();
	}

	/**
	 * This method is to create Mail Content for Travel Request
	 * 
	 * @param Travel
	 *            Request Employee ( To whom the email needs to send ) String for
	 *            which the email content to be created i.e.Cab,Ticket,Accommodation
	 */
	private Map<String, String> createMailContentApprover(TravelRequest travelRequestForm, Employee recipientEmployee,
			String toCreateMailContentFor, VOTraveldeskComment comment, String approverAction)
			throws JsonGenerationException, JsonMappingException, IOException {

		Map<String, String> placeHolder = new HashMap<String, String>();
		// placeHolder.put("{websiteURL}", baseURL);
		Employee employeeRequester = employeeDAO.findById(travelRequestForm.getEmployeeId().getId()).get();

		if (!HRMSHelper.isNullOrEmpty(employeeRequester)) {
			placeHolder.put("{requestBy}", employeeRequester.getCandidate().getFirstName() + " "
					+ employeeRequester.getCandidate().getLastName());
		} else {
			placeHolder.put("{requestBy}", travelRequestForm.getCreatedBy());
		}

		// String recipientEmployeeName =
		// recipientEmployee.getCandidate().getFirstName() + " "
		// + recipientEmployee.getCandidate().getLastName();

		placeHolder.put("{recipientEmployeeName}", "Traveldesk");
		placeHolder.put("{ticket}", HRMSHelper.toConvertBooleanToHumanReadable(travelRequestForm.isBookTicket()));
		placeHolder.put("{cab}", HRMSHelper.toConvertBooleanToHumanReadable(travelRequestForm.isBookCab()));
		placeHolder.put("{accomodation}",
				HRMSHelper.toConvertBooleanToHumanReadable(travelRequestForm.isBookAccommodation()));

		/*
		 * // next added by ssw on 11July2018 if
		 * (!HRMSHelper.isNullOrEmpty(travelRequestForm.getRejectCommentByTd())) {
		 * placeHolder.put("{travelRequestRejectReason}",
		 * String.valueOf(travelRequestForm.getRejectCommentByTd())); }
		 */

		//
		// placeHolder.put("{travelRequestSeqNo}",
		// String.valueOf(travelRequestForm.getSeqId()));

		StringBuffer mainDetails = new StringBuffer();
		// mainDetails.append("<br/>");
		mainDetails
				.append("<table style=\"height: 40%; border-collapse: collapse;\" border=\"1\" width=\"100%\"><tbody>");
		mainDetails.append(
				"<tr><td style=\"width:20%;\" colspan=\"1\">Travel request No</td><td style=\"width:80%;\" colspan=\"1\">");
		mainDetails.append(String.valueOf(travelRequestForm.getSeqId()) + "</td></tr>");
		mainDetails.append(
				"<tr><td style=\"width:20%;\" colspan=\"1\">Child Type</td><td style=\"width:80%;\" colspan=\"1\">");
		mainDetails.append(toCreateMailContentFor + "</td></tr>");
		mainDetails.append(
				"<tr><td style=\"width:20%;\" colspan=\"1\">Approver Action</td><td style=\"width:80%;\" colspan=\"1\">");
		mainDetails.append(approverAction + "</td></tr>");
		mainDetails.append(
				"<tr><td style=\"width:20%;\" colspan=\"1\">Approver Comment</td><td style=\"width:80%;\" colspan=\"1\">");
		mainDetails.append(comment.getComment() + "</td></tr>");
		mainDetails.append("</tbody></table>");

		placeHolder.put("{mainDetails}", mainDetails.toString());

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
						passenger = passenger + "<tr><td style=\"width: 20%;\">" + travellers.getPassengerName()
								+ "</td>";
						if (!HRMSHelper.isNullOrEmpty(travellers.getDateOfBirth())) {
							passenger = passenger + "<td style=\"width: 20%;\">" + HRMSDateUtil.format(
									travellers.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT) + "</td>";
						} else {
							passenger = passenger + "<td style=\"width: 20%;\"></td>";
						}
						if (!HRMSHelper.isNullOrEmpty(travellers.getEmployee())) {
							passenger = passenger + "<td style=\"width: 20%;\">" + travellers.getEmployee().getId()
									+ "</td>";
						} else {
							passenger = passenger + "<td style=\"width: 20%;\"></td>";
						}
						passenger = passenger + "<td style=\"width: 20%;\">" + travellers.getContactNumber() + "</td>"
								+ "<td style=\"width: 20%;\">" + travellers.getEmailId() + "</td></tr>";
					}

				}

			}

			String ticket = "<p><span style=\"text-decoration: underline; font-size:18px;\">"
					+ "<strong>Ticket Details</strong></span></p> <table style=\"height: 40%; border-collapse: collapse;\" border=\"1\" width=\"100%\"><tbody>"
					+ "<tr><td style=\"width:40%;\" colspan=\"2\">From</td>" + "<td style=\"width:60%;\" colspan=\"3\">"
					+ travelRequestForm.getTicketRequest().getFromLocation() + "</td></tr>"
					+ "<tr><td style=\"width:40%;\" colspan=\"2\">To</td>" + "<td style=\"width:60%;\" colspan=\"3\">"
					+ travelRequestForm.getTicketRequest().getToLocation() + "</td></tr>"
					+ "<tr><td style=\"width:40%;\" colspan=\"2\">Date of Journey</td>"
					+ "<td style=\"width:60%;\" colspan=\"3\">"
					+ HRMSDateUtil.format(travelRequestForm.getTicketRequest().getPreferredDate(),
							IHRMSConstants.FRONT_END_DATE_FORMAT)
					+ "</td></tr>";
			if (travelRequestForm.getTicketRequest().isRoundTrip() == true) {
				ticket = ticket + "<tr><td style=\"width:40%;\" colspan=\"2\">Round Trip</td>"
						+ "<td style=\"width:60%;\" colspan=\"3\">"
						+ String.valueOf(travelRequestForm.getTicketRequest().isRoundTrip()) + "</td></tr>"
						+ "<tr><td style=\"width:40%;\" colspan=\"2\">Return Date</td>"
						+ "<td style=\"width:60%;\" colspan=\"3\">"
						+ HRMSDateUtil.format(travelRequestForm.getTicketRequest().getReturnPreferredDate(),
								IHRMSConstants.FRONT_END_DATE_FORMAT)
						+ "</td></tr>";
			}
			if (!HRMSHelper.isNullOrEmpty(travelRequestForm.getTicketRequest().getApproverStatus())) {
				ticket = ticket + "<tr><td style=\"width:40%;\" colspan=\"2\">Ticket Approval Status</td>"
						+ "<td style=\"width:60%;\" colspan=\"3\">"
						+ travelRequestForm.getTicketRequest().getApproverStatus() + "</td></tr>";
			}
			ticket = ticket + "<tr><td style=\"width:100%;\" colspan=\"5\">" + "</td></tr>"
					+ "<tr><td style=\"width:100%; text-align:center; \" colspan=\"5\"> <b>Passenger Details </b>"
					+ "</td></tr>" + "<tr><td style=\"width:20%;\">Name</td><td style=\"width:20%;\">Date of Birth</td>"
					+ "<td style=\"width:20%;\">Employee Id</td>"
					+ "<td style=\"width: 20%;\">Contact No</td><td style=\"width: 20%;\">Email Id</td></tr>"
					+ passenger + "</tbody>" + "</table>";
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
						cabTraveller = cabTraveller + "<tr><td style=\"width: 16.66%;\">"
								+ cabPassengers.getPassengerName();

						if (!HRMSHelper.isNullOrEmpty(cabPassengers.getContactNumber())) {
							cabTraveller = cabTraveller + "<td style=\"width: 16.66%;\">"
									+ cabPassengers.getContactNumber() + "</td>";
						} else {
							cabTraveller = cabTraveller + "<td style=\"width: 16.66%;\"></td>";
						}
						if (!HRMSHelper.isNullOrEmpty(cabPassengers.getEmployee())) {
							cabTraveller = cabTraveller + "<td style=\"width: 16.66%;\">"
									+ cabPassengers.getEmployee().getId() + "</td>";
						} else {
							cabTraveller = cabTraveller + "<td style=\"width: 16.66%;\"></td>";
						}

						String pickupDate = "";
						if (cabPassengers.getPickupDate() != null) {
							pickupDate = HRMSDateUtil.format(cabPassengers.getPickupDate(),
									IHRMSConstants.FRONT_END_DATE_FORMAT);
						}

						cabTraveller = cabTraveller + "<td style=\"width: 16.66%;\">" + cabPassengers.getEmailId()
								+ "</td>" + "<td style=\"width: 16.66%;\">" + pickupDate + "</td>"
								+ "<td style=\"width: 16.66%;\">" + cabPassengers.getPickupAt() + "</td></tr>";
					}
				}

			}

			String cabRequestString = "<p><span style=\"text-decoration: underline; font-size:18px;\"><strong>Cab Details</strong></span></p>"
					+ "<table style=\"height: 40%; border-collapse: collapse;\" border=\"1\" width=\"100%\"><tbody>"
					+ "<tr><td style=\"width:40%;\" colspan=\"2\">From</td>" + "<td style=\"width:60%;\" colspan=\"4\">"
					+ "NA" + "</td></tr>" + "<tr><td style=\"width:40%;\" colspan=\"2\">To</td>"
					+ "<td style=\"width:60%;\" colspan=\"4\">" + "NA" + "</td></tr>"
					+ "<tr><td style=\"width:40%;\" colspan=\"2\">Date Of Journey</td>"
					+ "<td style=\"width:60%;\" colspan=\"4\">" + "NA" + "</td></tr>"
					+ "<tr><td style=\"width:100%;\" colspan=\"6\">" + "</td></tr>"
					+ "<tr><td style=\"width:100%; text-align:center; \" colspan=\"6\"> <b>Passenger Details </b>"
					+ "</td></tr>" + "<tr><td style=\"width:16.66%;\">Name</td>"
					+ "<td style=\"width:16.66%;\">Contact No</td>" + "<td style=\"width:16.66%;\">Employee Id</td>"
					+ "<td style=\"width: 16.66%;\">Email Id</td>" + "<td style=\"width: 16.66%;\">Pickup Date</td>"
					+ "<td style=\"width: 16.66%;\">Pick up At</td></tr>" + cabTraveller + "</tbody>" + "</table>";

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

						guests = guests + "<tr><td style=\"width: 20%;\">" + guestObject.getPassengerName() + "</td>";
						if (!HRMSHelper.isNullOrEmpty(guestObject.getDateOfBirth())) {
							guests = guests + "<td style=\"width: 20%;\">" + HRMSDateUtil.format(
									guestObject.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT) + "</td>";
						} else {
							guests = guests + "<td style=\"width: 20%;\"></td>";
						}
						if (!HRMSHelper.isNullOrEmpty(guestObject.getEmployee())) {
							guests = guests + "<td style=\"width: 20%;\">" + guestObject.getEmployee().getId()
									+ "</td>";
						} else {
							guests = guests + "<td style=\"width: 20%;\"></td>";
						}
						guests = guests + "<td style=\"width: 20%;\">" + guestObject.getContactNumber() + "</td>"
								+ "<td style=\"width: 20%;\">" + guestObject.getEmailId() + "</td></tr>";
					}
				}
			}

			String accomodationRequest = "<p><span style=\"text-decoration: underline; font-size:18px;\"><strong>Accomodation Details</strong></span></p>"
					+ "<table style=\"height: 40%; border-collapse: collapse;\" border=\"1\" width=\"100%\"><tbody>"
					+ "<tr><td style=\"width:40%;\" colspan=\"2\">From Date</td>"
					+ "<td style=\"width:60%;\" colspan=\"3\">"
					+ String.valueOf(HRMSDateUtil.format(travelRequestForm.getAccommodationRequest().getFromDate(),
							IHRMSConstants.FRONT_END_DATE_FORMAT))
					+ "</td></tr>" + "<tr><td style=\"width:40%;\" colspan=\"2\">To Date</td>"
					+ "<td style=\"width:60%;\" colspan=\"3\">"
					+ String.valueOf(HRMSDateUtil.format(travelRequestForm.getAccommodationRequest().getToDate(),
							IHRMSConstants.FRONT_END_DATE_FORMAT))
					+ "</td></tr>" + "<tr><td style=\"width:100%;\" colspan=\"5\">" + "</td></tr>"
					+ "<tr><td style=\"width:100%; text-align:center; \" colspan=\"5\"> <b>Passenger Details </b> </td></tr>"

					+ "<tr><td style=\"width:20%;\">Name</td><td style=\"width:20%;\">Date of Birth</td>"
					+ "<td style=\"width: 20%;\">Employee Id</td>"
					+ "<td style=\"width: 20%;\">Contact No</td><td style=\"width: 20%;\">Email Id</td></tr>" + guests
					+ "</tbody>" + "</table>";

			placeHolder.put("{accomodationRequest}", accomodationRequest);

		} else {
			placeHolder.put("{accomodationRequest}", "");
		}

		placeHolder.put("{comment}", comment.getComment());
		return placeHolder;
	}
}
