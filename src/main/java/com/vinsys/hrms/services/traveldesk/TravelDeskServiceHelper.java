package com.vinsys.hrms.services.traveldesk;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSAccommodationGuestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSAccommodationRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRecurringRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRequestPassengerDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterModeOfTravelDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTicketRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTicketRequestPassengerDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTravelRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTraveldeskApproverDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSTraveldeskCommentDAO;
import com.vinsys.hrms.datamodel.traveldesk.VOAccommodationGuest;
import com.vinsys.hrms.datamodel.traveldesk.VOAccommodationRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOCabRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOCabRequestPassenger;
import com.vinsys.hrms.datamodel.traveldesk.VOTicketRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOTicketRequestPassenger;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelRequest;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.entity.traveldesk.AccommodationGuest;
import com.vinsys.hrms.entity.traveldesk.AccommodationRequest;
import com.vinsys.hrms.entity.traveldesk.CabRecurringRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequestPassenger;
import com.vinsys.hrms.entity.traveldesk.MasterDriver;
import com.vinsys.hrms.entity.traveldesk.MasterModeOfTravel;
import com.vinsys.hrms.entity.traveldesk.MasterTraveldeskApprover;
import com.vinsys.hrms.entity.traveldesk.MasterVehicle;
import com.vinsys.hrms.entity.traveldesk.TicketRequest;
import com.vinsys.hrms.entity.traveldesk.TicketRequestPassenger;
import com.vinsys.hrms.entity.traveldesk.TravelRequest;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
//@PropertySource(value="${HRMSCONFIG}")
public class TravelDeskServiceHelper {

	public static final Logger logger = LoggerFactory.getLogger(TravelDeskServiceHelper.class);

	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSAccommodationRequestDAO accommodationRequestDAO;
	@Autowired
	IHRMSAccommodationGuestDAO accommodationGuestDAO;
	@Autowired
	IHRMSTravelRequestDAO travelRequestDAO;
	@Autowired
	IHRMSTicketRequestDAO ticketRequestDAO;
	@Autowired
	IHRMSMasterModeOfTravelDAO modeOfTravelDAO;
	@Autowired
	IHRMSTicketRequestPassengerDAO ticketRequestPassengerDAO;
	@Autowired
	IHRMSMasterDepartmentDAO masterDepartmentDAO;
	@Autowired
	IHRMSTraveldeskCommentDAO traveldeskCommentDAO;
	@Autowired
	IHRMSCabRequestDAO cabRequestDAO;
	@Autowired
	IHRMSCabRecurringRequestDAO cabRecurringRequestDAO;
	@Autowired
	IHRMSCabRequestPassengerDAO cabRequestPassengerDAO;
	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	@Autowired
	IHRMSTraveldeskApproverDAO masterApproverDAO;

	@Value("${base.url}")
	private String baseURL;

	// @Transactional(rollbackFor=Exception.class )
	public CabRequest toPersistCabRequest(VOTravelRequest travelRequest, TravelRequest travelRequestEntity,
			VOCabRequest cabRequest, Organization organization) throws TransactionException {
		CabRequest cabRequestEntity = null;

		if (cabRequest.getId() > 0) {
			cabRequestEntity = cabRequestDAO.findById(cabRequest.getId()).get();
			if (cabRequestEntity != null) {
				cabRequestEntity.setUpdatedBy(travelRequest.getUpdatedBy());
				cabRequestEntity.setUpdatedDate(new Date());
				cabRequestEntity.setIsActive(IHRMSConstants.isActive);
			}
		} else {
			cabRequestEntity = new CabRequest();
			cabRequestEntity.setCabRequestStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
			cabRequestEntity.setCreatedBy(travelRequest.getCreatedBy());
			cabRequestEntity.setCreatedDate(new Date());
			cabRequestEntity.setIsActive(IHRMSConstants.isActive);
		}

		/**
		 * Actual CabRequest Will have to save here the cab request
		 */
		cabRequestEntity = HRMSRequestTranslator.createCabRequestEntity(cabRequestEntity, cabRequest);
		cabRequestEntity.setTravelRequest(travelRequestEntity);

		cabRequestEntity = cabRequestDAO.save(cabRequestEntity);

		/**
		 * Cab Request Passenger Details
		 */
		Set<VOCabRequestPassenger> cabReqPassenger = cabRequest.getCabRequestPassengers();
		Set<CabRequestPassenger> cabReqPassengerSet = new HashSet<CabRequestPassenger>();

		if (!HRMSHelper.isNullOrEmpty(cabReqPassenger)) {

			List<CabRequestPassenger> dbGuestList = cabRequestPassengerDAO
					.findCabPassengerListByRequestId(cabRequestEntity.getId(), IHRMSConstants.isActive);

			// .findAccommodationGuestByAccId(accommodationRequestEntity.getId(),
			// IHRMSConstants.isActive);

			List<Long> dbGuestIds = new ArrayList<Long>();
			if (!HRMSHelper.isNullOrEmpty(dbGuestList)) {
				for (CabRequestPassenger existingGuest : dbGuestList) {
					dbGuestIds.add(existingGuest.getId());
				}
			}

			for (VOCabRequestPassenger cabPassenger : cabReqPassenger) {
				CabRequestPassenger cabPassengerEntity = null;

				if (dbGuestIds.contains(cabPassenger.getId())) {

					cabPassengerEntity = cabRequestPassengerDAO.findById(cabPassenger.getId()).get();
					cabPassengerEntity.setUpdatedBy(travelRequest.getUpdatedBy());
					cabPassengerEntity.setUpdatedDate(new Date());
					cabPassengerEntity = HRMSRequestTranslator.createCabPassengerEntity(cabPassengerEntity,
							cabPassenger);

					if (cabPassenger.getEmployee() != null) {
						Employee cabPassengerEmployee = employeeDAO.findById(cabPassenger.getEmployee().getId()).get();
						cabPassengerEntity.setEmployee(cabPassengerEmployee);
					}

					cabPassengerEntity.setCabRequest(cabRequestEntity);
					cabPassengerEntity = cabRequestPassengerDAO.save(cabPassengerEntity);

					cabReqPassengerSet.add(cabPassengerEntity);
					/**
					 * Recurring cab request ,if the passenger has a recurring request
					 */
					if (cabPassenger.isRecurring()) {

						toPersistRecurringCabRequest(cabPassenger, cabPassengerEntity);

					} else if (!cabPassenger.isRecurring()) {
						List<CabRecurringRequest> cabRecurringPassengerEntityList = cabRecurringRequestDAO
								.findCabRecurringRequestByPassenger(cabPassengerEntity.getId(),
										IHRMSConstants.isActive);

						if (!HRMSHelper.isNullOrEmpty(cabRecurringPassengerEntityList)) {
							cabRecurringRequestDAO.deleteAll(cabRecurringPassengerEntityList);
						}
					}

					dbGuestIds.remove(cabPassenger.getId());

				} else {

					cabPassengerEntity = new CabRequestPassenger();
					cabPassengerEntity.setCreatedBy(travelRequest.getCreatedBy());
					cabPassengerEntity.setCreatedDate(new Date());
					cabPassengerEntity.setIsActive(IHRMSConstants.isActive);

					/**
					 * Added on 24 August for storing return and one way status
					 */
					if (cabPassenger.isDropOnly()) {
						cabPassengerEntity.setOneWayTripStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
					} else {
						cabPassengerEntity.setOneWayTripStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
						cabPassengerEntity.setReturnTripStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
					}

					/**
					 * End
					 */
					// cabPassengerEntity.setStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);

					cabPassengerEntity = HRMSRequestTranslator.createCabPassengerEntity(cabPassengerEntity,
							cabPassenger);

					if (cabPassenger.getEmployee() != null) {
						Employee cabPassengerEmployee = employeeDAO.findById(cabPassenger.getEmployee().getId()).get();
						cabPassengerEntity.setEmployee(cabPassengerEmployee);
					}

					cabPassengerEntity.setCabRequest(cabRequestEntity);
					cabPassengerEntity = cabRequestPassengerDAO.save(cabPassengerEntity);

					cabReqPassengerSet.add(cabPassengerEntity);
					/**
					 * Recurring cab request ,if the passenger has a recurring request
					 */
					if (cabPassenger.isRecurring()) {

						toPersistRecurringCabRequest(cabPassenger, cabPassengerEntity);

					} else if (!cabPassenger.isRecurring()) {
						List<CabRecurringRequest> cabRecurringPassengerEntityList = cabRecurringRequestDAO
								.findCabRecurringRequestByPassenger(cabPassengerEntity.getId(),
										IHRMSConstants.isActive);

						if (!HRMSHelper.isNullOrEmpty(cabRecurringPassengerEntityList)) {
							cabRecurringRequestDAO.deleteAll(cabRecurringPassengerEntityList);
						}
					}
				}
			}

			if (!HRMSHelper.isNullOrEmpty(dbGuestIds))
				cabRequestPassengerDAO.updateGuestIds(dbGuestIds);

			cabRequestEntity.setCabRequestPassengers(cabReqPassengerSet);
		}

		return cabRequestEntity;
	}

	/**
	 * This method will persist Recurring Cab Request
	 */
	// @Transactional(rollbackFor=Exception.class)
	private void toPersistRecurringCabRequest(VOCabRequestPassenger cabPassenger,
			CabRequestPassenger cabPassengerEntity) {

		List<CabRecurringRequest> cabRecurringPassengerEntityList = cabRecurringRequestDAO
				.findCabRecurringRequestByPassenger(cabPassengerEntity.getId(), IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(cabRecurringPassengerEntityList)) {
			cabRecurringRequestDAO.deleteAll(cabRecurringPassengerEntityList);
		}

		logger.info(" :: Persisting Recurring Cab Request For Passenger :: ");
		LocalDate startDate = HRMSDateUtil.convertDateToLocalDate(
				HRMSDateUtil.parse(cabPassenger.getPickupDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		LocalDate endDate = HRMSDateUtil.convertDateToLocalDate(
				HRMSDateUtil.parse(cabPassenger.getReturnDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));

		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {
			logger.info(" : Persisting Cab Recurring Request : ");
			CabRecurringRequest cabRecurringRequest = new CabRecurringRequest();
			cabPassenger.setPickupDate(HRMSDateUtil.format((HRMSDateUtil.convertLocalDateToDate(date)),
					IHRMSConstants.FRONT_END_DATE_FORMAT));
			cabPassenger.setReturnDate(HRMSDateUtil.format((HRMSDateUtil.convertLocalDateToDate(date)),
					IHRMSConstants.FRONT_END_DATE_FORMAT));
			cabRecurringRequest = HRMSRequestTranslator.createCabRecurringRequestEntity(cabRecurringRequest,
					cabPassenger);
			cabRecurringRequest.setCabRequestRecurringStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
			cabRecurringRequest.setCabRequestPassenger(cabPassengerEntity);
			cabRecurringRequest.setCreatedBy(cabPassengerEntity.getCreatedBy());
			cabRecurringRequest.setCreatedDate(new Date());
			cabRecurringRequest.setIsActive(IHRMSConstants.isActive);

			/**
			 * Added on 24 August for storing return and one way status
			 */
			if (cabPassenger.isDropOnly()) {
				cabRecurringRequest.setOneWayTripStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
			} else {
				cabRecurringRequest.setOneWayTripStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
				cabRecurringRequest.setReturnTripStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
			}
			/**
			 * End
			 */
			// cabRecurringRequest.setStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
			cabRecurringRequestDAO.save(cabRecurringRequest);

			// entity.setTravelRequestId(VOTravelRequest travelRequestId);
			// entity.setDriverId(VOMasterDriver driverId);
			// entity.setVehicleId(VOMasterVehicle vehicleId);
			// entity.setCabRecurringRequests(Set<VOCabRecurringRequest>
			// cabRecurringRequests);
			// entity.setCabRequestPassengers(Set<VOCabRequestPassenger>
			// cabRequestPassengers);

		}
	}

	// @Transactional(rollbackFor=Exception.class)
	public TicketRequest toPersistTicketRequest(TravelRequest travelRequestEntity, VOTicketRequest ticketRequestModel,
			Organization organization) {

		logger.info(" :: Persisting Ticket Request :: ");
		TicketRequest ticketRequestEntity = null;

		if (ticketRequestModel.getId() > 0) {
			ticketRequestEntity = ticketRequestDAO.findById(ticketRequestModel.getId()).get();
			if (ticketRequestEntity != null) {
				ticketRequestEntity.setUpdatedBy(ticketRequestModel.getUpdatedBy());
				ticketRequestEntity.setUpdatedDate(new Date());
				ticketRequestEntity.setIsActive(IHRMSConstants.isActive);
			}

		} else {
			ticketRequestEntity = new TicketRequest();
			ticketRequestEntity.setTicketRequestStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
			ticketRequestEntity.setCreatedBy(ticketRequestModel.getCreatedBy());
			ticketRequestEntity.setCreatedDate(new Date());
			ticketRequestEntity.setIsActive(IHRMSConstants.isActive);
		}
		ticketRequestEntity = HRMSRequestTranslator.createTicketRequestEntity(ticketRequestEntity, ticketRequestModel);

		// if(organization!=null) {
		// ticketRequestEntity.setOrganization(organization);
		// }

		ticketRequestEntity.setTravelRequest(travelRequestEntity);
		MasterModeOfTravel masterModeOfTravel = modeOfTravelDAO
				.findById(ticketRequestModel.getMasterModeOfTravel().getId()).get();
		ticketRequestEntity.setMasterModeOfTravel(masterModeOfTravel);

		if (ticketRequestModel.getMasterApprover() != null) {

			// This will be done by travel desk employe
			// Employee ticketApprover =
			// employeeDAO.findById(ticketRequestModel.getApproverId().getId());
			// ticketRequestEntity.setApproverId(ticketApprover)
			MasterTraveldeskApprover masterTicketApprover = masterApproverDAO
					.findById(ticketRequestModel.getMasterApprover().getId()).get();
			ticketRequestEntity.setMasterApprover(masterTicketApprover);
		}

		Set<VOTicketRequestPassenger> ticketRequestPassengerModel = ticketRequestModel.getTicketRequestPassengers();
		Set<TicketRequestPassenger> ticketRequestPassengerEntity = new HashSet<TicketRequestPassenger>();
		ticketRequestEntity = ticketRequestDAO.save(ticketRequestEntity);
		if (ticketRequestPassengerModel != null) {

			List<TicketRequestPassenger> dbGuestList = ticketRequestPassengerDAO
					.findTicketPassengerByTicketId(ticketRequestEntity.getId(), IHRMSConstants.isActive);

			// .findAccommodationGuestByAccId(accommodationRequestEntity.getId(),
			// IHRMSConstants.isActive);

			List<Long> dbGuestIds = new ArrayList<Long>();
			if (!HRMSHelper.isNullOrEmpty(dbGuestList)) {
				for (TicketRequestPassenger existingGuest : dbGuestList) {
					dbGuestIds.add(existingGuest.getId());
				}
			}

			for (VOTicketRequestPassenger ticketPassengerModel : ticketRequestPassengerModel) {
				TicketRequestPassenger ticketPassengeEntity = null;

				if (dbGuestIds.contains(ticketPassengerModel.getId())) {

					ticketPassengeEntity = ticketRequestPassengerDAO.findById(ticketPassengerModel.getId()).get();
					if (ticketRequestEntity != null) {
						ticketPassengeEntity.setUpdatedBy(ticketRequestModel.getUpdatedBy());
						ticketPassengeEntity.setUpdatedDate(new Date());
						ticketPassengeEntity.setIsActive(IHRMSConstants.isActive);
					}

					ticketPassengeEntity = HRMSRequestTranslator.createTicketPassengerEntity(ticketPassengeEntity,
							ticketPassengerModel);

					if (ticketPassengerModel.getEmployee() != null) {
						Employee guestEmployee = employeeDAO.findById(ticketPassengerModel.getEmployee().getId()).get();
						ticketPassengeEntity.setEmployee(guestEmployee);

					}
					ticketPassengeEntity.setTicketRequest(ticketRequestEntity);
					ticketPassengeEntity = ticketRequestPassengerDAO.save(ticketPassengeEntity);
					ticketRequestPassengerEntity.add(ticketPassengeEntity);
					dbGuestIds.remove(ticketPassengerModel.getId());

				} else {

					ticketPassengeEntity = new TicketRequestPassenger();
					ticketPassengeEntity.setCreatedBy(ticketRequestModel.getCreatedBy());
					ticketPassengeEntity.setCreatedDate(new Date());
					ticketPassengeEntity = HRMSRequestTranslator.createTicketPassengerEntity(ticketPassengeEntity,
							ticketPassengerModel);

					if (ticketPassengerModel.getEmployee() != null) {
						Employee guestEmployee = employeeDAO.findById(ticketPassengerModel.getEmployee().getId()).get();
						ticketPassengeEntity.setEmployee(guestEmployee);

					}

					ticketPassengeEntity.setTicketRequest(ticketRequestEntity);
					ticketPassengeEntity = ticketRequestPassengerDAO.save(ticketPassengeEntity);
					ticketRequestPassengerEntity.add(ticketPassengeEntity);

				}
			}

			if (!HRMSHelper.isNullOrEmpty(dbGuestIds))
				ticketRequestPassengerDAO.updatePassengerIdsToInActive(dbGuestIds);

			ticketRequestEntity.setTicketRequestPassengers(ticketRequestPassengerEntity);
		}

		ticketRequestEntity = ticketRequestDAO.save(ticketRequestEntity);
		return ticketRequestEntity;
	}

	// @Transactional(rollbackFor=Exception.class)
	public AccommodationRequest toPersistAccommodationRequest(VOTravelRequest travelRequest,
			TravelRequest travelRequestEntity, VOAccommodationRequest accommodationRequestModel,
			Organization organization) {
		AccommodationRequest accommodationRequestEntity = null;

		if (accommodationRequestModel.getId() > 0) {
			accommodationRequestEntity = accommodationRequestDAO.findById(accommodationRequestModel.getId()).get();
			if (accommodationRequestEntity != null) {
				accommodationRequestEntity.setUpdatedBy(travelRequest.getUpdatedBy());
				accommodationRequestEntity.setUpdatedDate(new Date());
				accommodationRequestEntity.setIsActive(IHRMSConstants.isActive);
			}
		} else {
			accommodationRequestEntity = new AccommodationRequest();
			accommodationRequestEntity.setAccommodationRequestStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
			accommodationRequestEntity.setCreatedBy(travelRequest.getCreatedBy());
			accommodationRequestEntity.setCreatedDate(new Date());
			accommodationRequestEntity.setIsActive(IHRMSConstants.isActive);
		}

		accommodationRequestEntity = HRMSRequestTranslator.createAccomodationRequestEntity(accommodationRequestEntity,
				accommodationRequestModel);

		accommodationRequestEntity.setTravelRequest(travelRequestEntity);

		if (accommodationRequestModel.getMasterApprover() != null) {
			// Employee approver =
			// employeeDAO.findById(accommodationRequestModel.getApproverId().getId());
			// accommodationRequestEntity.setApproverId(approver);
			MasterTraveldeskApprover masterTicketApprover = masterApproverDAO
					.findById(accommodationRequestModel.getMasterApprover().getId()).get();
			accommodationRequestEntity.setMasterApprover(masterTicketApprover);
		}
		// if(organization!=null) {
		// Organization organization =
		// organizationDAO.findById(accommodationRequestModel.getOrganization().getId());
		// accommodationRequestEntity.setOrganization(organization);
		// }

		accommodationRequestEntity = accommodationRequestDAO.save(accommodationRequestEntity);
		Set<VOAccommodationGuest> newGuestList = accommodationRequestModel.getAccommodationGuests();
		Set<AccommodationGuest> accomodationGuestEntitySet = new HashSet<AccommodationGuest>();

		if (newGuestList != null) {

			/*
			 * Finding Existing Guest List for Accommodation
			 */
			List<AccommodationGuest> dbGuestList = accommodationGuestDAO
					.findAccommodationGuestByAccId(accommodationRequestEntity.getId(), IHRMSConstants.isActive);

			List<Long> dbGuestIds = new ArrayList<Long>();
			if (!HRMSHelper.isNullOrEmpty(dbGuestList)) {
				for (AccommodationGuest existingGuest : dbGuestList) {
					dbGuestIds.add(existingGuest.getId());
				}
			}

			for (VOAccommodationGuest newGuest : newGuestList) {

				if (dbGuestIds.contains(newGuest.getId())) {

					AccommodationGuest accomodationGuest = null;

					accomodationGuest = accommodationGuestDAO.findById(newGuest.getId()).get();
					accomodationGuest.setUpdatedBy(travelRequest.getUpdatedBy());
					accomodationGuest.setUpdatedDate(new Date());

					accomodationGuest = HRMSRequestTranslator.createAccommodationGuestEntity(accomodationGuest,
							newGuest);
					accomodationGuest.setAccomodationReq(accommodationRequestEntity);
					if (newGuest.getEmployee() != null) {
						Employee guestEmployee = employeeDAO.findById(newGuest.getEmployee().getId()).get();
						accomodationGuest.setEmployee(guestEmployee);
					}
					accomodationGuestEntitySet.add(accomodationGuest);
					dbGuestIds.remove(newGuest.getId());
				} else {

					AccommodationGuest accomodationGuest = null;

					accomodationGuest = new AccommodationGuest();
					accomodationGuest.setCreatedBy(travelRequest.getCreatedBy());
					accomodationGuest.setCreatedDate(new Date());

					accomodationGuest = HRMSRequestTranslator.createAccommodationGuestEntity(accomodationGuest,
							newGuest);
					accomodationGuest.setAccomodationReq(accommodationRequestEntity);
					if (newGuest.getEmployee() != null) {
						Employee guestEmployee = employeeDAO.findById(newGuest.getEmployee().getId()).get();
						accomodationGuest.setEmployee(guestEmployee);
					}
					accomodationGuestEntitySet.add(accomodationGuest);

				}

			}
			if (!HRMSHelper.isNullOrEmpty(dbGuestIds))
				accommodationGuestDAO.updateGuestIds(dbGuestIds);

			accommodationRequestEntity.setAccommodationGuests(accomodationGuestEntitySet);

			accommodationRequestEntity = accommodationRequestDAO.save(accommodationRequestEntity);
		}
		return accommodationRequestEntity;
	}

	// @Transactional(rollbackFor=Exception.class)
	public TravelRequest toPersistTravelRequest(VOTravelRequest travelRequest, Organization organization,
			Employee requestor) throws HRMSException {

		logger.info(" :: Persisting Travel Request :: ");
		TravelRequest travelRequestEntity = null;

		if (travelRequest.getId() > 0) {
			travelRequestEntity = travelRequestDAO.findById(travelRequest.getId()).get();
			if (travelRequestEntity != null) {
				if (travelRequestEntity.getTravelStatus().equalsIgnoreCase(
						IHRMSConstants.TRAVEL_REQUEST_STATUS_REJECTED) && !travelRequest.isTravelTypeTD()) {
					travelRequestEntity.setTravelStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_WIP);
				}
				travelRequestEntity.setUpdatedBy(travelRequest.getUpdatedBy());
				travelRequestEntity.setUpdatedDate(new Date());
				travelRequestEntity.setIsActive(IHRMSConstants.isActive);
			}

		} else {
			logger.info(" Inside New Travel Request ");
			travelRequestEntity = new TravelRequest();
			travelRequestEntity.setTravelStatus(IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING);
			travelRequestEntity.setSeqId(generateSequenceId(organization));
			travelRequestEntity.setCreatedBy(travelRequest.getCreatedBy());
			travelRequestEntity.setCreatedDate(new Date());
			travelRequestEntity.setRequestDate(new Date());
			travelRequestEntity.setIsActive(IHRMSConstants.isActive);
			if (organization != null) {
				travelRequestEntity.setOrganization(organization);
			}
		}
		travelRequestEntity = HRMSRequestTranslator.createTravelRequestEntity(travelRequestEntity, travelRequest);

		if (travelRequest.getDepartment() != null) {
			MasterDepartment department = masterDepartmentDAO.findById(travelRequest.getDepartment().getId()).get();
			travelRequestEntity.setDepartment(department);
		}

		if (requestor != null) {
			// Employee requestor =
			// employeeDAO.findById(travelRequest.getEmployeeId().getId());
			travelRequestEntity.setEmployeeId(requestor);
		}

		travelRequestEntity = travelRequestDAO.save(travelRequestEntity);

		return travelRequestEntity;
	}

	/**
	 * This method will create sequence for travel request
	 * 
	 */
	private synchronized long generateSequenceId(Organization organization) {
		try {

			logger.info(" :: Creating Sequence For Travel Request");
			if (!HRMSHelper.isNullOrEmpty(organization)) {

				long currentTravelReqSeq = 0;
				long countTravelReqByOrg = travelRequestDAO.getCountTravelRequestOrgwise(organization.getId());
				if (countTravelReqByOrg == 0) {
					currentTravelReqSeq = 0;
				} else {
					currentTravelReqSeq = travelRequestDAO.getTravelRequestSeqOrgwise(organization.getId());
				}

				return currentTravelReqSeq + 1;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public String getPendingChildRequests(TravelRequest travelRequest) {
		String pendingChild = "";
		if (!HRMSHelper.isNullOrEmpty(travelRequest)) {
			if (!HRMSHelper.isNullOrEmpty(travelRequest.getAccommodationRequest())) {
				if (!HRMSHelper
						.isNullOrEmpty(travelRequest.getAccommodationRequest().getAccommodationRequestStatus())) {
					if (travelRequest.getAccommodationRequest().getAccommodationRequestStatus()
							.equals(IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_PENDING)) {
						pendingChild = IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION;
					}
				}
			}
			if (!HRMSHelper.isNullOrEmpty(travelRequest.getCabRequest())) {
				if (!HRMSHelper.isNullOrEmpty(travelRequest.getCabRequest().getCabRequestStatus())) {
					if (travelRequest.getCabRequest().getCabRequestStatus()
							.equals(IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_PENDING)) {
						if (!HRMSHelper.isNullOrEmpty(pendingChild)) {
							pendingChild = pendingChild + ", " + IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB;
						} else {
							pendingChild = IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB;
						}
					}
				}
			}
			if (!HRMSHelper.isNullOrEmpty(travelRequest.getTicketRequest())) {
				if (!HRMSHelper.isNullOrEmpty(travelRequest.getTicketRequest().getTicketRequestStatus())) {
					if (travelRequest.getTicketRequest().getTicketRequestStatus()
							.equals(IHRMSConstants.TRAVEL_REQUEST_CHILD_STATUS_PENDING)) {
						if (!HRMSHelper.isNullOrEmpty(pendingChild)) {
							pendingChild = pendingChild + ", " + IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET;
						} else {
							pendingChild = IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET;
						}
					}
				}
			}
			return pendingChild;
		} else {
			return pendingChild;
		}
	}

	/**
	 * This method is to create Mail Content for Cab Details assignment
	 * 
	 * @param Travel
	 * 
	 */
	public Map<String, String> createMailContentForCabDetails(TravelRequest travelRequestForm,
			CabRequestPassenger cabReqPassenger, CabRecurringRequest cabRecReq, boolean isRecurring,
			MasterDriver driver, MasterVehicle vehicle, MasterDriver returnDriver, MasterVehicle returnVehicle,
			Employee requester, String typeOfMail) {

		try {
			Map<String, String> placeHolder = new HashMap<String, String>();
			placeHolder.put("{websiteURL}", baseURL);

			if (!HRMSHelper.isNullOrEmpty(requester)) {
				placeHolder.put("{requestBy}",
						requester.getCandidate().getFirstName() + " " + requester.getCandidate().getLastName());
			} else {
				placeHolder.put("{requestBy}", travelRequestForm.getCreatedBy());
			}

			placeHolder.put("{ticket}", HRMSHelper.toConvertBooleanToHumanReadable(travelRequestForm.isBookTicket()));
			placeHolder.put("{cab}", HRMSHelper.toConvertBooleanToHumanReadable(travelRequestForm.isBookCab()));
			placeHolder.put("{accomodation}",
					HRMSHelper.toConvertBooleanToHumanReadable(travelRequestForm.isBookAccommodation()));
			placeHolder.put("{requestId}", String.valueOf(travelRequestForm.getId()));
			placeHolder.put("{buName}", String.valueOf(travelRequestForm.getBdName()));

			String clientName = IHRMSConstants.NA;
			String bdName = IHRMSConstants.NA;
			String workOrderNo = IHRMSConstants.NA;

			if (!HRMSHelper.isNullOrEmpty(travelRequestForm.getClientName())) {
				clientName = travelRequestForm.getClientName();
			}

			if (!HRMSHelper.isNullOrEmpty(travelRequestForm.getBdName())) {
				bdName = travelRequestForm.getBdName();
			}

			if (travelRequestForm.getWorkOrderNo() != 0) {
				workOrderNo = String.valueOf(travelRequestForm.getWorkOrderNo());
			}

			placeHolder.put("{client}", clientName);
			placeHolder.put("{bdName}", bdName);
			placeHolder.put("{won}", workOrderNo);

			if (!HRMSHelper.isNullOrEmpty(cabReqPassenger) && !HRMSHelper.isNullOrEmpty(travelRequestForm)) {

				String passengerName = "";
				String contactNo = "";
				String employeeId = "";
				String emailId = "";
				String pickupDate = "";
				String pickupTime = "";
				String pickupAt = "";
				String dropLocation = "";
				String returnDate = "";
				String returnTime = "";
				String tdSelfManagedComment = "";
				String returndriverPickUpTime = "";
				String driverPickupTime = "";

				if (isRecurring) {
					if (!HRMSHelper.isNullOrEmpty(cabRecReq)) {
						passengerName = cabReqPassenger.getPassengerName();
						contactNo = cabReqPassenger.getContactNumber();
						tdSelfManagedComment = cabRecReq.getTdSelfManagedComment();
						if (tdSelfManagedComment.trim().length() > 0)
							tdSelfManagedComment = tdSelfManagedComment.concat(" ," + cabRecReq.getDriverComment());
						else
							tdSelfManagedComment = cabRecReq.getDriverComment();
						if (!HRMSHelper.isNullOrEmpty(cabReqPassenger.getEmployee())) {
							employeeId = String.valueOf(cabReqPassenger.getEmployee().getId());
						} else {
							employeeId = "";
						}
						emailId = cabReqPassenger.getEmailId();
						pickupDate = HRMSDateUtil.format(cabRecReq.getPickupDate(),
								IHRMSConstants.FRONT_END_DATE_FORMAT);
						pickupTime = cabRecReq.getPickupTime();
						pickupAt = cabRecReq.getPickupAt();
						dropLocation = cabRecReq.getDropLocation();
						returnDate = HRMSDateUtil.format(cabRecReq.getReturnDate(),
								IHRMSConstants.FRONT_END_DATE_FORMAT);
						returnTime = cabRecReq.getReturnTime();
						driverPickupTime = cabRecReq.getDriverPickupTime();
						returndriverPickUpTime = cabRecReq.getReturnDriverPickupTime();
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
				} else {
					passengerName = cabReqPassenger.getPassengerName();
					contactNo = cabReqPassenger.getContactNumber();
					tdSelfManagedComment = cabReqPassenger.getTdSelfManagedComment();

					if (tdSelfManagedComment.trim().length() > 0)
						tdSelfManagedComment = tdSelfManagedComment.concat(" ," + cabReqPassenger.getDriverComment());
					else
						tdSelfManagedComment = cabReqPassenger.getDriverComment();

					// tdSelfManagedComment.concat(", "+cabReqPassenger.getDriverComment());
					if (!HRMSHelper.isNullOrEmpty(cabReqPassenger.getEmployee())) {
						employeeId = String.valueOf(cabReqPassenger.getEmployee().getId());
					} else {
						employeeId = "";
					}
					emailId = cabReqPassenger.getEmailId();
					pickupDate = HRMSDateUtil.format(cabReqPassenger.getPickupDate(),
							IHRMSConstants.FRONT_END_DATE_FORMAT);
					pickupTime = cabReqPassenger.getPickupTime();
					pickupAt = cabReqPassenger.getPickupAt();
					dropLocation = cabReqPassenger.getDropLocation();
					returnDate = HRMSDateUtil.format(cabReqPassenger.getReturnDate(),
							IHRMSConstants.FRONT_END_DATE_FORMAT);
					returnTime = cabReqPassenger.getReturnTime();

					driverPickupTime = cabReqPassenger.getDriverPickupTime();
					returndriverPickUpTime = cabReqPassenger.getReturnDriverPickupTime();
				}

				StringBuffer cabReqTravelDetailDataSB = new StringBuffer();
				cabReqTravelDetailDataSB.append("<tr><td style=\"width:14.28%;\">" + passengerName
						+ "</td><td style=\"width:14.28%;\">" + contactNo + "</td><td style=\"width:14.28%;\">"
						+ employeeId + "</td><td style=\"width:14.28%;\">" + emailId
						+ "</td><td style=\"width:14.28%;\">" + pickupDate + "</td><td style=\"width:14.28%;\">"
						+ pickupAt + "</td><td style=\"width:14.28%;\">" + dropLocation + "</td>");

				StringBuffer cabRequestTravelDetailsSB = null;
				if (!HRMSHelper.isNullOrEmpty(typeOfMail)
						&& typeOfMail.equalsIgnoreCase(IHRMSConstants.CAB_DETAIL_MAIL_TYPE_BOOK_CAB)) {
					cabRequestTravelDetailsSB = new StringBuffer(
							"<p><span style=\"text-decoration: underline; font-family:calibri; font-size:15px;\"><strong>Passenger details</strong></span></p>\r\n");
				} else if (!HRMSHelper.isNullOrEmpty(typeOfMail)
						&& typeOfMail.equalsIgnoreCase(IHRMSConstants.CAB_DETAIL_MAIL_TYPE_JOURNEY_COMPLETE)) {
					cabRequestTravelDetailsSB = new StringBuffer(
							"<p><span style=\"text-decoration: underline; font-family:calibri; font-size:15px;\"><strong>Journey completion Details</strong></span></p>\r\n");
				}

				cabRequestTravelDetailsSB.append(
						"<table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"4\"><tbody>"
								+ "<tr style=\"color:white;\" bgcolor=\"#e98700\"><td style=\"width:14.28%;\">Name</td>"
								+ "<td style=\"width:14.28%;\">Contact No</td>"
								+ "<td style=\"width:14.28%;\">Employee Id</td>"
								+ "<td style=\"width:14.28%;\">Email Id</td>"
								+ "<td style=\"width:14.28%;\">Pickup Date</td>"
								+ "<td style=\"width:14.28%;\">Pick up At</td>"
								+ "<td style=\"width:14.28%;\">Drop Location</td>" + "</tr>"
								+ cabReqTravelDetailDataSB.toString() + "</tbody></table>");

				placeHolder.put("{cabRequestTravelDetailsSB}", cabRequestTravelDetailsSB.toString());

				if (!HRMSHelper.isNullOrEmpty(typeOfMail)
						&& typeOfMail.equalsIgnoreCase(IHRMSConstants.CAB_DETAIL_MAIL_TYPE_BOOK_CAB)) {
					StringBuffer cabAssignDetailsDataSB = new StringBuffer();
					if (!HRMSHelper.isNullOrEmpty(driver) && !HRMSHelper.isNullOrEmpty(vehicle)) {
						cabAssignDetailsDataSB.append(
								"<tr><td style=\"width: 16.66%;\">" + driver.getEmployee().getCandidate().getFirstName()
										+ " " + driver.getEmployee().getCandidate().getLastName()
										+ "</td><td style=\"width: 16.66%;\">"
										+ driver.getEmployee().getCandidate().getMobileNumber()
										+ "</td><td style=\"width: 16.66%;\">" + vehicle.getVehicleName() + " - "
										+ (HRMSHelper.isNullOrEmpty(vehicle.getVehicleDescription()) ? "NA"
												: vehicle.getVehicleDescription())
										+ "</td><td style=\"width: 16.66%;\">" + driverPickupTime);

					} else {
						cabAssignDetailsDataSB.append("</td><td style=\"width: 16.66%;\">NA</td>"
								+ "<td style=\"width: 16.66%;\">NA</td><td style=\"width: 16.66%;\">NA</td><td style=\\\"width: 16.66%;\\\">NA</td>");
					}
					if (!HRMSHelper.isNullOrEmpty(returnDriver) && !HRMSHelper.isNullOrEmpty(returnVehicle)) {
						cabAssignDetailsDataSB.append("</td><td style=\"width: 16.66%;\">"
								+ returnDriver.getEmployee().getCandidate().getFirstName() + " "
								+ returnDriver.getEmployee().getCandidate().getLastName()
								+ "</td><td style=\"width: 16.66%;\">"
								+ returnDriver.getEmployee().getCandidate().getMobileNumber()
								+ "</td><td style=\"width: 16.66%;\">" + returnVehicle.getVehicleName() + " - "
								+ (HRMSHelper.isNullOrEmpty(returnVehicle.getVehicleDescription()) ? "NA"
										: returnVehicle.getVehicleDescription())
								+ "</td><td style=\"width: 16.66%;\">" + returndriverPickUpTime + "</td>");
					} else {
						cabAssignDetailsDataSB.append("</td><td style=\"width: 16.66%;\">NA</td>"
								+ "<td style=\"width: 16.66%;\">NA</td><td style=\"width: 16.66%;\">NA</td><td style=\\\"width: 16.66%;\\\">NA</td>");
					}

					StringBuffer cabAssignDetailsSB = new StringBuffer(
							"<p><span style=\"text-decoration: underline; font-family:calibri; font-size:15px;\">"
									+ "<strong>Driver And Vehicle Details</strong></span></p>\r\n"
									+ "<table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"4\"><tbody>"
									+ "<tr style=\"color:white;\" bgcolor=\"#e98700\"><td style=\"width: 16.66%;\">Driver</td><td style=\"width: 16.66%;\">Driver Contact Number</td>"
									+ "<td style=\"width: 16.66%;\">Vehicle</td><td style=\\\"width: 16.66%;\\\">Driver Pickup Time</td><td style=\"width: 16.66%;\">Return Driver</td>"
									+ "<td style=\"width: 16.66%;\">Return Driver Contact Number</td><td style=\"width: 16.66%;\">Return vehicle</td>"
									+ "<td style=\"width: 16.66%;\">Return Driver Pickup Time</td>" + "</tr>"
									+ cabAssignDetailsDataSB.toString() + "</tbody></table>");

					placeHolder.put("{comment}", tdSelfManagedComment);

					placeHolder.put("{cabAssignDetailsSB}", cabAssignDetailsSB.toString());
				}
			}
			return placeHolder;
		} catch (HRMSException e) {
			e.printStackTrace();
		} catch (Exception unknown) {
			unknown.printStackTrace();
		}
		return null;
	}
}
