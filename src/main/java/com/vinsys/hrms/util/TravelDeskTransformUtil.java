package com.vinsys.hrms.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterDriverDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterVehicleDAO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.traveldesk.MasterDriver;
import com.vinsys.hrms.entity.traveldesk.MasterVehicle;
import com.vinsys.hrms.master.dao.IMasterTravellerTypeDAO;
import com.vinsys.hrms.master.entity.CurrencyMaster;
import com.vinsys.hrms.master.vo.CurrencyMasterVO;
import com.vinsys.hrms.master.vo.MasterDriverVO;
import com.vinsys.hrms.master.vo.MasterTravelTypeVO;
import com.vinsys.hrms.master.vo.MasterTravellerTypeVO;
import com.vinsys.hrms.master.vo.MasterVehicleVO;
import com.vinsys.hrms.master.vo.TripTypeVO;
import com.vinsys.hrms.traveldesk.dao.IAccommodationRequestDAO;
import com.vinsys.hrms.traveldesk.dao.ICabRequestDAO;
import com.vinsys.hrms.traveldesk.dao.ITicketRequestDAO;
import com.vinsys.hrms.traveldesk.dao.ITravelRequestDAO;
import com.vinsys.hrms.traveldesk.dao.ITravellerDetailDAO;
import com.vinsys.hrms.traveldesk.entity.AccommodationRequestV2;
import com.vinsys.hrms.traveldesk.entity.CabRequestV2;
import com.vinsys.hrms.traveldesk.entity.TicketRequestV2;
import com.vinsys.hrms.traveldesk.entity.TravelRequestV2;
import com.vinsys.hrms.traveldesk.entity.TravellerDetailsV2;
import com.vinsys.hrms.traveldesk.vo.AccommodationTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.AirTicketVO;
import com.vinsys.hrms.traveldesk.vo.AvailableTicketBooking;
import com.vinsys.hrms.traveldesk.vo.BusTicketVO;
import com.vinsys.hrms.traveldesk.vo.CabTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.GetTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.TicketTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.TrainTicketVO;
import com.vinsys.hrms.traveldesk.vo.TravelResponseVO;
import com.vinsys.hrms.traveldesk.vo.TravellerDetailsVO;

@Component
public class TravelDeskTransformUtil {

	@Autowired
	ITravellerDetailDAO travellerDetailDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IMasterTravellerTypeDAO travellerTypeDAO;
	@Autowired
	IHRMSMasterVehicleDAO vehicleDAO;
	@Autowired
	IHRMSMasterDriverDAO driverDAO;
	@Autowired
	ITravelRequestDAO travelRequestDao;
	@Autowired
	ICabRequestDAO cabRequestDAO;
	@Autowired
	IAccommodationRequestDAO accommodationRequestDAO;
	@Autowired
	TravelDeskAuthorityHelper travelDeskAuthorityHelper;
	@Autowired
	ITicketRequestDAO ticketRequestDAO;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public List<TravelResponseVO> convertToTravelRequestVO(List<TravelRequestV2> travelRequestV2) {
		List<TravelResponseVO> travelRequestVO = new ArrayList<TravelResponseVO>();

		for (TravelRequestV2 request : travelRequestV2) {
			TravelResponseVO responseVO = new TravelResponseVO();
			responseVO.setId(request.getId());
			responseVO.setName(request.getName());
			responseVO.setTravelReason(request.getTravelReason());
			responseVO.setBpmNumber(request.getBpmNumber());
			responseVO.setBookTicket(request.getBookTicket());
			responseVO.setCreatedDate(
					HRMSDateUtil.format(request.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			responseVO.setBookCab(request.getBookCab());
			responseVO.setBookAccommodation(request.getBookAccommodation());
			responseVO.setStatus(request.getRequestWF().getStatus());
			if (!HRMSHelper.isNullOrEmpty(request.getDepartment().getDepartmentName())) {
				responseVO.setDepartmentName(request.getDepartment().getDepartmentName());
			}
			responseVO.setRequesterId(request.getRequesterId());
			responseVO.setInvoiceNumber(request.getInvoiceNumber());
			responseVO.setBdName(request.getBdName());
			responseVO.setCurrency(convertToCurrencyMasterVO(request.getCurrency()));
			//responseVO.setPreference(!HRMSHelper.isNullOrEmpty(request.getTravelComment()) ? request.getTravelComment() : null);
			travelRequestVO.add(responseVO);
		}
		return travelRequestVO;
	}
	
	public  CurrencyMasterVO convertToCurrencyMasterVO(CurrencyMaster currency) {
		CurrencyMasterVO currencyMasterVO = new CurrencyMasterVO();
		if (!HRMSHelper.isNullOrEmpty(currency)) {
			currencyMasterVO.setEntityId(currency.getEntityId());
			currencyMasterVO.setCountryName(currency.getCountryName());
			currencyMasterVO.setCurrency(currency.getCurrency());
			currencyMasterVO.setSymbol(currency.getSymbol());
		}
		return currencyMasterVO;
	}


	public GetTravelRequestVO convertToTravelRequestVO(TravelRequestV2 travelRequestV2) {

		GetTravelRequestVO travelRequestVO = new GetTravelRequestVO();
		List<AvailableTicketBooking> availableTicketBookings = new ArrayList<>();
		travelRequestVO.setAvailableTicketBooking(availableTicketBookings);

		if (!HRMSHelper.isNullOrEmpty(travelRequestV2.getCabRequestV2())) {
			getCabRequestDetails(travelRequestV2, travelRequestVO);
		}

		if (!HRMSHelper.isNullOrEmpty(travelRequestV2.getAccommodationRequest())) {
			getAccommodationDetails(travelRequestV2, travelRequestVO);
		}

		if (!HRMSHelper.isNullOrEmpty(travelRequestV2.getTicketBooking())) {
			getTicketRequestDetails(travelRequestV2, travelRequestVO);
		}

		travelRequestVO.setApproverComment(travelRequestV2.getApproverComment());

		if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getTicketDetails())
				&& travelRequestVO.getTicketDetails().isAirDetailsSubmitted()) {
			AvailableTicketBooking availableTicketBooking = new AvailableTicketBooking();
			availableTicketBooking.setModeOfTravel(EModeOfTravel.Air.name());
			availableTicketBooking.setValue(true);
			availableTicketBookings.add(availableTicketBooking);
			travelRequestVO.setAvailableTicketBooking(availableTicketBookings);
		} else {
			AvailableTicketBooking availableTicketBooking = new AvailableTicketBooking();
			availableTicketBooking.setModeOfTravel(EModeOfTravel.Air.name());
			availableTicketBooking.setValue(false);
			availableTicketBookings.add(availableTicketBooking);
			travelRequestVO.setAvailableTicketBooking(availableTicketBookings);
		}
		if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getTicketDetails())
				&& travelRequestVO.getTicketDetails().isBusDetailsSubmitted()) {
			AvailableTicketBooking availableTicketBooking = new AvailableTicketBooking();
			availableTicketBooking.setModeOfTravel(EModeOfTravel.Bus.name());
			availableTicketBooking.setValue(true);
			availableTicketBookings.add(availableTicketBooking);
			travelRequestVO.setAvailableTicketBooking(availableTicketBookings);
		} else {
			AvailableTicketBooking availableTicketBooking = new AvailableTicketBooking();
			availableTicketBooking.setModeOfTravel(EModeOfTravel.Bus.name());
			availableTicketBooking.setValue(false);
			availableTicketBookings.add(availableTicketBooking);
			travelRequestVO.setAvailableTicketBooking(availableTicketBookings);
		}
		if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getTicketDetails())
				&& travelRequestVO.getTicketDetails().isTrainDetailsSubmitted()) {
			AvailableTicketBooking availableTicketBooking = new AvailableTicketBooking();
			availableTicketBooking.setModeOfTravel(EModeOfTravel.Train.name());
			availableTicketBooking.setValue(true);
			availableTicketBookings.add(availableTicketBooking);
			travelRequestVO.setAvailableTicketBooking(availableTicketBookings);
		} else {
			AvailableTicketBooking availableTicketBooking = new AvailableTicketBooking();
			availableTicketBooking.setModeOfTravel(EModeOfTravel.Train.name());
			availableTicketBooking.setValue(false);
			availableTicketBookings.add(availableTicketBooking);
			travelRequestVO.setAvailableTicketBooking(availableTicketBookings);
		}

		return travelRequestVO;
	}

	private void getTicketRequestDetails(TravelRequestV2 travelRequestV2, GetTravelRequestVO travelRequestVO) {
		TicketTravelRequestVO ticketRequest = new TicketTravelRequestVO();

		AirTicketVO airTicket = new AirTicketVO();
		BusTicketVO busTicket = new BusTicketVO();
		TrainTicketVO trainTicket = null;

		List<AvailableTicketBooking> availableTicketBookings = new ArrayList<AvailableTicketBooking>();

		List<TicketRequestV2> ticketBooking = travelRequestV2.getTicketBooking();

		for (TicketRequestV2 ticketBook : ticketBooking) {

			List<TravellerDetailsV2> travellerDetails = null;
			List<TravellerDetailsVO> travellerDetailVO = null;

			trainTicket = new TrainTicketVO();
			if (ticketBook.getModeOfTravel().getModeOfTravel()
					.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_AIR)) {

				airTicket.setAirType(ticketBook.getAirType());
				airTicket.setChargableClient(ticketBook.getChargeableToClient());
				airTicket.setDateOfjourney(
						HRMSDateUtil.format(ticketBook.getDateofJourney(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				airTicket.setReturnDateOfjourney(
						HRMSDateUtil.format(ticketBook.getReturnDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				airTicket.setId(ticketBook.getId());
				airTicket.setFromLocation(ticketBook.getFromLocation());
				airTicket.setToLocation(ticketBook.getToLocation());
				airTicket.setNoOfTravellers(ticketBook.getNoOfTravellers());
				airTicket.setPreferedTime(ticketBook.getPreferredTime());
				airTicket.setReturnPreferedTime(ticketBook.getPreferredReturnTime());
				airTicket.setRoundTrip(ticketBook.getRoundTrip());
				airTicket.setTravelRequestId(ticketBook.getTravelRequestId());
				airTicket.setApproximateCost(ticketBook.getApproximateCost());
				airTicket.setApproximateComment(ticketBook.getApproximateCostComment());
				airTicket.setFinalCost(ticketBook.getFinalCost());
				airTicket.setFinalCostcomment(ticketBook.getFinalCostComment());
//				airTicket.setApprover(travelApprover);
				if (!HRMSHelper.isNullOrEmpty(ticketBook.getId())) {
					travellerDetails = travellerDetailDAO.findByTicketRequestAndIsActive(ticketBook.getId(),
							IHRMSConstants.isActive);
					travellerDetailVO = getTravellerDetails(travellerDetails);

				}
				airTicket.setTravellerDetails(travellerDetailVO);
				ticketRequest.setAirDetails(airTicket);
				ticketRequest.setAirDetailsSubmitted(true);

			} else if (ticketBook.getModeOfTravel().getModeOfTravel()
					.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_BUS)) {
				busTicket.setBusType(ticketBook.getBusType());
				busTicket.setChargableClient(ticketBook.getChargeableToClient());
				busTicket.setDateOfjourney(
						HRMSDateUtil.format(ticketBook.getDateofJourney(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				busTicket.setReturnDateOfjourney(
						HRMSDateUtil.format(ticketBook.getReturnDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				busTicket.setId(ticketBook.getId());
				busTicket.setFromLocation(ticketBook.getFromLocation());
				busTicket.setToLocation(ticketBook.getToLocation());
				busTicket.setNoOfTravellers(ticketBook.getNoOfTravellers());
				busTicket.setPreferedTime(ticketBook.getPreferredTime());
				busTicket.setReturnPreferedTime(ticketBook.getPreferredReturnTime());
				busTicket.setRoundTrip(ticketBook.getRoundTrip());
				busTicket.setTravelRequestId(ticketBook.getTravelRequestId());
				busTicket.setApproximateCost(ticketBook.getApproximateCost());
				busTicket.setApproximateComment(ticketBook.getApproximateCostComment());
				busTicket.setFinalCost(ticketBook.getFinalCost());
				busTicket.setFinalCostcomment(ticketBook.getFinalCostComment());
//				busTicket.setApprover(travelApprover);
				if (!HRMSHelper.isNullOrEmpty(ticketBook.getId())) {
					travellerDetails = travellerDetailDAO.findByTicketRequestAndIsActive(ticketBook.getId(),
							IHRMSConstants.isActive);
					travellerDetailVO = getTravellerDetails(travellerDetails);

				}
				busTicket.setTravellerDetails(travellerDetailVO);
				ticketRequest.setBusDetails(busTicket);

				ticketRequest.setBusDetailsSubmitted(true);
			} else if (ticketBook.getModeOfTravel().getModeOfTravel()
					.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TRAIN)) {
				trainTicket.setTrainType(ticketBook.getTrainType());
				trainTicket.setChargableClient(ticketBook.getChargeableToClient());
				trainTicket.setDateOfjourney(
						HRMSDateUtil.format(ticketBook.getDateofJourney(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				trainTicket.setReturnDateOfjourney(
						HRMSDateUtil.format(ticketBook.getReturnDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				trainTicket.setId(ticketBook.getId());
				trainTicket.setFromLocation(ticketBook.getFromLocation());
				trainTicket.setToLocation(ticketBook.getToLocation());
				trainTicket.setNoOfTravellers(ticketBook.getNoOfTravellers());
				trainTicket.setPreferedTime(ticketBook.getPreferredTime());
				trainTicket.setReturnPreferedTime(ticketBook.getPreferredReturnTime());
				trainTicket.setRoundTrip(ticketBook.getRoundTrip());
				trainTicket.setTravelRequestId(ticketBook.getTravelRequestId());
				trainTicket.setApproximateCost(ticketBook.getApproximateCost());
				trainTicket.setApproximateComment(ticketBook.getApproximateCostComment());
				trainTicket.setFinalCost(ticketBook.getFinalCost());
				trainTicket.setFinalCostcomment(ticketBook.getFinalCostComment());
//				trainTicket.setApprover(travelApprover);
				if (!HRMSHelper.isNullOrEmpty(ticketBook.getId())) {
					travellerDetails = travellerDetailDAO.findByTicketRequestAndIsActive(ticketBook.getId(),
							IHRMSConstants.isActive);
					travellerDetailVO = getTravellerDetails(travellerDetails);

				}
				trainTicket.setTravellerDetails(travellerDetailVO);
				ticketRequest.setTrainDetails(trainTicket);

				ticketRequest.setTrainDetailsSubmitted(true);
			}

		}

		travelRequestVO.setTicketDetails(ticketRequest);
	}

	private void getAccommodationDetails(TravelRequestV2 travelRequestV2, GetTravelRequestVO travelRequestVO) {
		AccommodationTravelRequestVO accommodation = new AccommodationTravelRequestVO();
		List<TravellerDetailsV2> travellerDetails = null;
		List<TravellerDetailsVO> travellerDetailVO = null;
		accommodation.setChargableClient(travelRequestV2.getAccommodationRequest().getChargeableToClient());
		accommodation.setCheckInTime(travelRequestV2.getAccommodationRequest().getCheckInTime());
		accommodation.setCheckOutTime(travelRequestV2.getAccommodationRequest().getCheckOutTime());
		accommodation.setFromDate(HRMSDateUtil.format(travelRequestV2.getAccommodationRequest().getFromDate(),
				IHRMSConstants.FRONT_END_DATE_FORMAT));
		accommodation.setToDate(HRMSDateUtil.format(travelRequestV2.getAccommodationRequest().getToDate(),
				IHRMSConstants.FRONT_END_DATE_FORMAT));
		accommodation.setId(travelRequestV2.getAccommodationRequest().getId());
		accommodation.setLocation(travelRequestV2.getAccommodationRequest().getLocation());
		accommodation.setNoOfRooms(travelRequestV2.getAccommodationRequest().getNumberOfRooms());
		accommodation.setNoOfTravellers(travelRequestV2.getAccommodationRequest().getNumberOfTravellers());
		accommodation.setTravelRequestId(travelRequestV2.getAccommodationRequest().getTravelRequestId());
		accommodation.setApproximateCost(travelRequestV2.getAccommodationRequest().getApproximateCost());
		accommodation.setApproximateComment(travelRequestV2.getAccommodationRequest().getApproximateCostComment());
		accommodation.setFinalCost(travelRequestV2.getAccommodationRequest().getFinalCost());
		accommodation.setFinalCostcomment(travelRequestV2.getAccommodationRequest().getFinalCostComment());
		if (!HRMSHelper.isNullOrEmpty(travelRequestV2.getAccommodationRequest().getId())) {
			travellerDetails = travellerDetailDAO.findByAccommodationRequestAndIsActive(
					travelRequestV2.getAccommodationRequest().getId(), IHRMSConstants.isActive);
			travellerDetailVO = getTravellerDetails(travellerDetails);

		}
		accommodation.setTravellerDetails(travellerDetailVO);
		travelRequestVO.setAccommodationDetails(accommodation);
	}

	private void getCabRequestDetails(TravelRequestV2 travelRequestV2, GetTravelRequestVO travelRequestVO) {
		CabTravelRequestVO cabRequestVO = new CabTravelRequestVO();
		List<TravellerDetailsV2> travellerDetails = null;

		if (!HRMSHelper.isNullOrEmpty(travelRequestV2.getCabRequestV2().getCabType())) {
			MasterTravellerTypeVO travellerType = new MasterTravellerTypeVO();
			travellerType.setId(travelRequestV2.getCabRequestV2().getCabType().getId());
			travellerType
					.setTravellerType(travelRequestV2.getCabRequestV2().getCabType().getTravellerType());
			cabRequestVO.setCabType(travellerType);
		}

		if (!HRMSHelper.isNullOrEmpty(travelRequestV2.getCabRequestV2().getDriverId())) {
			MasterDriver masterDriver = driverDAO.findByIdAndIsActive(travelRequestV2.getCabRequestV2().getDriverId(),
					ERecordStatus.Y.name());

			Employee driver = employeeDAO.findActiveEmployeeById(masterDriver.getEmployee().getId(),
					IHRMSConstants.isActive);

			MasterDriverVO driverVO = new MasterDriverVO();
			driverVO.setId(travelRequestV2.getCabRequestV2().getDriverId());
			driverVO.setDivisionId(driver.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
			driverVO.setDriverEmployeeId(driver.getId());
			driverVO.setDriverName(driver.getCandidate().getFirstName() + " " + driver.getCandidate().getLastName());
			cabRequestVO.setDriverName(driverVO);
		}

		if (!HRMSHelper.isNullOrEmpty(travelRequestV2.getCabRequestV2().getVehicleId())) {
			MasterVehicleVO vehicleVo = new MasterVehicleVO();
			MasterVehicle vehicle = vehicleDAO.findIdAndIsActive(travelRequestV2.getCabRequestV2().getVehicleId(),
					IHRMSConstants.isActive);
			vehicleVo.setId(travelRequestV2.getCabRequestV2().getVehicleId());
			vehicleVo.setDivisionId(vehicle.getDivision().getId());
			vehicleVo.setVehicleNumber(vehicle.getVehicleName());
			cabRequestVO.setVehicleName(vehicleVo);
		}

		if (!HRMSHelper.isNullOrEmpty(travelRequestV2.getCabRequestV2().getTripType().getTripType())) {
			TripTypeVO tripTypeVO = new TripTypeVO();
			tripTypeVO.setTripType(travelRequestV2.getCabRequestV2().getTripType().getTripType());
			tripTypeVO.setId(travelRequestV2.getCabRequestV2().getTripType().getId());
			cabRequestVO.setTripType(tripTypeVO);
		}

		if (!HRMSHelper.isNullOrEmpty(travelRequestV2.getCabRequestV2().getTravelType().getBusType())) {
			MasterTravelTypeVO travelTypeVO = new MasterTravelTypeVO();
			travelTypeVO.setTravelType(travelRequestV2.getCabRequestV2().getTravelType().getBusType());
			travelTypeVO.setId(travelRequestV2.getCabRequestV2().getTravelType().getId());
			cabRequestVO.setTravelType(travelTypeVO);
		}

		cabRequestVO.setId(travelRequestV2.getCabRequestV2().getId());
		cabRequestVO.setChargableClient(travelRequestV2.getCabRequestV2().getChargeableToClient());
		cabRequestVO.setDateOfjourney(HRMSDateUtil.format(travelRequestV2.getCabRequestV2().getDateOfJourney(),
				IHRMSConstants.FRONT_END_DATE_FORMAT));
		cabRequestVO.setFromLocation(travelRequestV2.getCabRequestV2().getFromLocation());
		cabRequestVO.setToLocation(travelRequestV2.getCabRequestV2().getToLocation());
		cabRequestVO.setReturnDateOfjourney(HRMSDateUtil.format(travelRequestV2.getCabRequestV2().getReturnDate(),
				IHRMSConstants.FRONT_END_DATE_FORMAT));
		cabRequestVO.setNoOfTravellers(travelRequestV2.getCabRequestV2().getNumberOfTravellers());

		cabRequestVO.setPreferedTime(travelRequestV2.getCabRequestV2().getPreferredTime());
		cabRequestVO.setReturnPreferedTime(travelRequestV2.getCabRequestV2().getPreferredReturnTime());
		cabRequestVO.setTravelRequestId(travelRequestV2.getCabRequestV2().getTravelRequestId());

		cabRequestVO.setApproximateDistance(travelRequestV2.getCabRequestV2().getApproximateDistance());
		cabRequestVO.setApproximateCost(travelRequestV2.getCabRequestV2().getApproximateCost());
		cabRequestVO.setApproximateComment(travelRequestV2.getCabRequestV2().getApproximateCostComment());
		cabRequestVO.setFinalCost(travelRequestV2.getCabRequestV2().getFinalCost());
		cabRequestVO.setFinalCostcomment(travelRequestV2.getCabRequestV2().getFinalCostComment());

		if (!HRMSHelper.isNullOrEmpty(travelRequestV2.getCabRequestV2().getId())) {
			travellerDetails = travellerDetailDAO.findByCabRequestAndIsActive(travelRequestV2.getCabRequestV2().getId(),
					IHRMSConstants.isActive);
		}

		List<TravellerDetailsVO> travellerDetailVO = getTravellerDetails(travellerDetails);

		cabRequestVO.setTravellerDetails(travellerDetailVO);
		travelRequestVO.setCabDetails(cabRequestVO);
	}

	private List<TravellerDetailsVO> getTravellerDetails(List<TravellerDetailsV2> travellerDetails) {
		List<TravellerDetailsVO> travellerDetailVO = new ArrayList<>();

		for (TravellerDetailsV2 travellerDetail : travellerDetails) {
			if (travellerDetail.getIsActive().equalsIgnoreCase(ERecordStatus.Y.name())) {
				TravellerDetailsVO travellerDetailsVO = new TravellerDetailsVO();
				travellerDetailsVO.setCabId(travellerDetail.getCabRequestId());
				travellerDetailsVO.setTicketId(travellerDetail.getTicketRequestId());
				travellerDetailsVO.setAccommodationId(travellerDetail.getAccommodationId());
				travellerDetailsVO.setContactNo(travellerDetail.getContactNumber());
				travellerDetailsVO.setDateOfBirth(
						HRMSDateUtil.format(travellerDetail.getDob(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				travellerDetailsVO.setEmailId(travellerDetail.getEmailId());
				travellerDetailsVO.setIsPrimaryTreveller(travellerDetail.isPrimaryTraveller());
				travellerDetailsVO.setName(travellerDetail.getName());
				travellerDetailsVO.setPickUpLocation(travellerDetail.getPickupLocation());
				travellerDetailsVO.setDropLocation(travellerDetail.getDropLocation());
				travellerDetailsVO.setPassportNo(travellerDetail.getPassportNumber());
				travellerDetailsVO.setPassportDateOfExpiry(HRMSDateUtil.format(travellerDetail.getPassportDateExpiry(),
						IHRMSConstants.FRONT_END_DATE_FORMAT));
				travellerDetailsVO.setPassportNo(travellerDetail.getPassportNumber());
				travellerDetailsVO.setId(travellerDetail.getId());
				travellerDetailsVO.setPickUpTime(travellerDetail.getPickupTime());
				travellerDetailsVO.setVisaCountry(travellerDetail.getVisaCountry());
				travellerDetailsVO.setVisaDateOfExpiry(
						HRMSDateUtil.format(travellerDetail.getVisaDateExpiry(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				travellerDetailsVO.setVisaNo(travellerDetail.getVisaNumber());
				travellerDetailsVO.setVisaType(travellerDetail.getVisaType());

				if (!HRMSHelper.isNullOrEmpty(travellerDetail.getMasterTravellerType().getTravellerType())) {
					MasterTravellerTypeVO travellerType = new MasterTravellerTypeVO();
					travellerType.setId(travellerDetail.getMasterTravellerType().getId());
					travellerType.setTravellerType(travellerDetail.getMasterTravellerType().getTravellerType());
					travellerDetailsVO.setTravellerType(travellerType);
				}
				travellerDetailsVO.setManagementEmployee(travellerDetail.isManagementEmployee());

				travellerDetailVO.add(travellerDetailsVO);

			}
		}
		return travellerDetailVO;
	}
	
	public boolean isRequestForManagementEmployee(TravelRequestV2 travelRequest) {
		log.info("Inside isRequestManagementEmployee Method");
		boolean isRequestForMgmtEmp = false;

		GetTravelRequestVO travelRequestVO = new GetTravelRequestVO();
		CabRequestV2 cabRequest = cabRequestDAO.findByTravelRequestIdAndIsActive(travelRequest.getId(),
				ERecordStatus.Y.name());
		AccommodationRequestV2 accommodationRequest = accommodationRequestDAO
				.findByTravelRequestIdAndIsActive(travelRequest.getId(), ERecordStatus.Y.name());
		List<TicketRequestV2> ticketRequest = ticketRequestDAO.findByTravelRequestIdAndIsActive(travelRequest.getId(),
				ERecordStatus.Y.name());
		travelRequest.setCabRequestV2(cabRequest);
		travelRequest.setAccommodationRequest(accommodationRequest);
		travelRequest.setTicketBooking(ticketRequest);
		travelRequestVO = convertToTravelRequestVO(travelRequest);

		if (!HRMSHelper.isNullOrEmpty(travelRequestVO)) {
			if (travelRequest.getBookAccommodation()) {
				if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getAccommodationDetails())) {
					if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getAccommodationDetails().getTravellerDetails())) {
						for (TravellerDetailsVO travellerDetailsVO : travelRequestVO.getAccommodationDetails()
								.getTravellerDetails()) {
							if (travellerDetailsVO.isManagementEmployee()) {
								return true;
							}
						}
					}
				}
			}

			if (travelRequest.getBookCab()) {
				if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getCabDetails())) {
					if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getCabDetails().getTravellerDetails())) {
						for (TravellerDetailsVO travellerDetailsVO : travelRequestVO.getCabDetails()
								.getTravellerDetails()) {
							if (travellerDetailsVO.isManagementEmployee()) {
								return true;
							}
						}
					}
				}
			}

			if (travelRequest.getBookTicket()) {
				if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getTicketDetails())) {
					// check for Air
					if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getTicketDetails().getAirDetails())) {
						for (TravellerDetailsVO travellerDetailsVO : travelRequestVO.getTicketDetails().getAirDetails()
								.getTravellerDetails()) {
							if (travellerDetailsVO.isManagementEmployee()) {
								return true;
							}
						}
					}
					// check for Bus
					if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getTicketDetails().getBusDetails())) {
						for (TravellerDetailsVO travellerDetailsVO : travelRequestVO.getTicketDetails().getBusDetails()
								.getTravellerDetails()) {
							if (travellerDetailsVO.isManagementEmployee()) {
								return true;
							}
						}
					}
					// check for Train
					if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getTicketDetails().getTrainDetails())) {
						for (TravellerDetailsVO travellerDetailsVO : travelRequestVO.getTicketDetails()
								.getTrainDetails().getTravellerDetails()) {
							if (travellerDetailsVO.isManagementEmployee()) {
								return true;
							}
						}
					}

				}
			}

		}

		log.info("Exit from isRequestManagementEmployee Method");
		return isRequestForMgmtEmp;

	}

}
