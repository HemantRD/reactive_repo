package com.vinsys.hrms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.traveldesk.vo.AccommodationTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.CabTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.DownloadExpenseSummaryReqVO;
import com.vinsys.hrms.traveldesk.vo.ExpenseSummeryRequest;
import com.vinsys.hrms.traveldesk.vo.TicketTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.TravelDetailsCancelVO;
import com.vinsys.hrms.traveldesk.vo.TravelDetailsDeleteVO;
import com.vinsys.hrms.traveldesk.vo.TravelRequestApprovalVO;
import com.vinsys.hrms.traveldesk.vo.TravelRequestRejectionVO;
import com.vinsys.hrms.traveldesk.vo.TravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.TravellerDetailsVO;

@Service
public class TravelDeskAuthorityHelper {

	@Value("${bpm.default.bpmnumber}")
	private Long defauldBpmNumber;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void addTravelRequestInputValidation(TravelRequestVO request) throws HRMSException {
		// null or empty
		if (HRMSHelper.isNullOrEmpty(request.isBookAccommodation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Book Accommodation");
		}
		if (HRMSHelper.isNullOrEmpty(request.isBookCab())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Book Cab");
		}
		if (HRMSHelper.isNullOrEmpty(request.isBookTicket())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Book Ticket");
		}
		if (!HRMSHelper.isNullOrEmpty(request.getBpmNumber()) && HRMSHelper.isLongZero(request.getBpmNumber())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " BPM Number is Mandatory");
		}
		if (HRMSHelper.isNullOrEmpty(request.getTravelReason())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Reason");
		}
		// regex
		if (!HRMSHelper.regexMatcher(String.valueOf(request.getBpmNumber()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " BPM Number is Mandatory");
		}

		if (!HRMSHelper.regexMatcher(request.getTravelReason(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Travel Reason ");
		}

		if (!HRMSHelper.regexMatcher(String.valueOf(request.isBookAccommodation()), "^(true|false)$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Book Accommodation");
		}

		if (!HRMSHelper.regexMatcher(String.valueOf(request.isBookCab()), "^(true|false)$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Book Cab");
		}
		if (!HRMSHelper.regexMatcher(String.valueOf(request.isBookTicket()), "^(true|false)$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Book Ticket");
		}
		if (HRMSHelper.isNullOrEmpty(request.getInvoiceNumber()) && !request.getBpmNumber().equals(defauldBpmNumber)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Invoice number");
		}
		if (HRMSHelper.isNullOrEmpty(request.getBdName()) && !request.getBpmNumber().equals(defauldBpmNumber)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " BD Name");
		}
		
		//travel comment 
//		if (!HRMSHelper.isNullOrEmpty(request.getPreference())
//				&& !HRMSHelper.regexMatcher(request.getPreference(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
//			throw new HRMSException(1501,ResponseCode.getResponseCodeMap().get(1501) + " Travel Comment");
//		}

		// currency input validation
		if (!HRMSHelper.isNullOrEmpty(request.getCurrency())) {
			if (!HRMSHelper.isNullOrEmpty(request.getCurrency().getCurrency())
					&& !HRMSHelper.isNullOrEmpty(request.getCurrency().getEntityId())) {
				// check with master data
				long count = MasterDataLoad.currencies.stream()
						.filter(e -> (e.getCurrency().equalsIgnoreCase(request.getCurrency().getCurrency())
								&& e.getEntityId().equals(request.getCurrency().getEntityId())))
						.count();
				if (count == 0) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Currency");
				}
			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Currency");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Currency");
		}
	}

	public void deleteTravelRequestInputValidation(TravelDetailsDeleteVO deleteVO) throws HRMSException {

		log.info("Inside deleteTravelRequestInputValidation method");

		if (!HRMSHelper.regexMatcher(String.valueOf(deleteVO.getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(deleteVO.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (!HRMSHelper.regexMatcher(String.valueOf(deleteVO.getTravelRequestId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}

		if (HRMSHelper.isLongZero(deleteVO.getTravelRequestId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}

//		if (!HRMSHelper.regexMatcher(String.valueOf(deleteVO.getTicketRequestId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Ticket Request Id ");
//		}
//
//		if (HRMSHelper.isLongZero(deleteVO.getTicketRequestId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Ticket Request Id ");
//		}
//		
//		if (!HRMSHelper.regexMatcher(String.valueOf(deleteVO.getCabRequestId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Request Id ");
//		}
//
//		if (HRMSHelper.isLongZero(deleteVO.getCabRequestId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Request Id ");
//		}
//		
//		if (!HRMSHelper.regexMatcher(String.valueOf(deleteVO.getAccomadationRequestId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accomadation Request Id ");
//		}
//
//		if (HRMSHelper.isLongZero(deleteVO.getAccomadationRequestId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accomadation Request Id ");
//		}

		log.info("Exist deleteTravelRequestInputValidation method");
	}

	// **************save accommodation***********************/

	public void saveAccommodationInputValidation(AccommodationTravelRequestVO request)
			throws HRMSException, ParseException {

		if (HRMSHelper.isNullOrEmpty(request.getNoOfRooms())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation No of rooms");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(request.getNoOfRooms()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation No of rooms");
		}

		if (HRMSHelper.isNullOrEmpty(request.getNoOfTravellers())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Accommodation No of travellers");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(request.getNoOfTravellers()))) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Accommodation No of travellers");
		}

		if (HRMSHelper.isNullOrEmpty(request.getChargableClient())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " To Accommodation Chargable to client");
		}

		if (!HRMSHelper.regexMatcher(request.getChargableClient(), "^(Y|N)$")) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " To Accommodation Chargable to client");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Location");
		}

		if (HRMSHelper.isNumber(request.getLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Location");
		}

		if (!HRMSHelper.regexMatcher(request.getLocation(), "^[a-zA-Z\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Location");
		}

		if (HRMSHelper.isNullOrEmpty(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation From date");
		}

		if (!HRMSHelper.validateDateFormate(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation From date");
		}
		if (HRMSHelper.isNullOrEmpty(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation To date");
		}

		if (!HRMSHelper.validateDateFormate(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation To date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getCheckInTime())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Check in time");
		}

		if (!HRMSHelper.regexMatcher(request.getCheckInTime(), "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Check in time");
		}

		if (HRMSHelper.isNullOrEmpty(request.getCheckOutTime())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Check out time");
		}

		if (!HRMSHelper.regexMatcher(request.getCheckOutTime(), "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Check out time");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);

		Date fromDate = dateFormat.parse(request.getFromDate());
		Date toDate = dateFormat.parse(request.getToDate());

		if (fromDate.after(toDate)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1502));
		}

	}

	/********************
	 * save cab
	 * 
	 * @throws ParseException
	 ***************************/

	public void saveCabInputValidation(CabTravelRequestVO request) throws HRMSException, ParseException {

		if (HRMSHelper.isLongZero(request.getTravelType().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Travel Type Id");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(request.getTravelType().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Travel Type Id");
		}

		if (HRMSHelper.isLongZero(request.getNoOfTravellers())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab No of travellers");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(request.getNoOfTravellers()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab No of travellers");
		}
		if (HRMSHelper.isLongZero(request.getTripType().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Trip Type Id");
		}

		if (!HRMSHelper.validateNumber(String.valueOf(request.getTripType().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Trip Type Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getTripType().getTripType())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Trip Type");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getTripType().getTripType())
				&& !(request.getTripType().getTripType().equals(IHRMSConstants.CAB_REQUEST_JOURNEY_ROUND)
						|| request.getTripType().getTripType().equals(IHRMSConstants.CAB_REQUEST_JOURNEY_DROP_ONLY))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Trip Type");
		}

		if (HRMSHelper.isNullOrEmpty(request.getChargableClient())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Cab Chargable to client");
		}

		if (!HRMSHelper.regexMatcher(request.getChargableClient(), "^(Y|N)$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Cab Chargable to client");
		}

		if (HRMSHelper.isNullOrEmpty(request.getFromLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab From Location");
		}

		if (HRMSHelper.isNumber(request.getFromLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab From Location");
		}

		if (!HRMSHelper.regexMatcher(request.getFromLocation(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab From Location");
		}

		if (HRMSHelper.isNullOrEmpty(request.getToLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab To Location");
		}

		if (HRMSHelper.isNumber(request.getToLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab To Location");
		}

		if (!HRMSHelper.regexMatcher(request.getToLocation(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab To Location");
		}

		if (HRMSHelper.isNullOrEmpty(request.getDateOfjourney())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Date of journey");
		}
		if (!HRMSHelper.validateDateFormate(request.getDateOfjourney())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Date of journey");
		}

		if (HRMSHelper.isNullOrEmpty(request.getPreferedTime())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Cab Prefered Time of journey");
		}

		if (!HRMSHelper.regexMatcher(request.getPreferedTime(), "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Cab Prefered Time of journey");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getTripType())
				&& request.getTripType().getTripType().equals(IHRMSConstants.CAB_REQUEST_JOURNEY_ROUND)) {
			if (HRMSHelper.isNullOrEmpty(request.getReturnDateOfjourney())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Return date of journey");
			}

			if (!HRMSHelper.validateDateFormate(request.getReturnDateOfjourney())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Return date of journey");
			}

			if (HRMSHelper.isNullOrEmpty(request.getReturnPreferedTime())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Return preferred time of journey");
			}

			if (!HRMSHelper.regexMatcher(request.getReturnPreferedTime(), "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Return preferred time of journey");
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);

			Date fromDate = dateFormat.parse(request.getDateOfjourney());
			Date toDate = dateFormat.parse(request.getReturnDateOfjourney());

			if (fromDate.after(toDate)) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1570));
			}
		}

	}

	/*******************
	 * save ticket
	 * 
	 * @throws ParseException
	 ***********************************************/

	public void saveTicketInputValidation(TicketTravelRequestVO request) throws HRMSException, ParseException {

		if (!HRMSHelper.isNullOrEmpty(request.getAirDetails())) {

			if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getAirType())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Type");
			} else {
				// check with master data
				long count = MasterDataLoad.airTypes.stream()
						.filter(e -> e.getAirType().equalsIgnoreCase(request.getAirDetails().getAirType())).count();
				if (count == 0) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Air Type");
				}
			}

			if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getNoOfTravellers())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air No of travellers");
			}

			if (!HRMSHelper.validateNumber(String.valueOf(request.getAirDetails().getNoOfTravellers()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air No of travellers");
			}

			if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getChargableClient())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " To Air Chargable to client");
			}

			if (!HRMSHelper.regexMatcher(request.getAirDetails().getChargableClient(), "^(Y|N)$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " To Air Chargable to client");
			}

			if (!HRMSHelper.regexMatcher(request.getAirDetails().getRoundTrip(), "^(Y|N)$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Air Round trip");
			}

			if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getFromLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air From Location");
			}

			if (HRMSHelper.isNumber(request.getAirDetails().getFromLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air From Location");
			}

			if (!HRMSHelper.regexMatcher(request.getAirDetails().getFromLocation(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air From Location");
			}

			if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getToLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air To Location");
			}

			if (HRMSHelper.isNumber(request.getAirDetails().getToLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air To Location");
			}

			if (!HRMSHelper.regexMatcher(request.getAirDetails().getToLocation(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air To Location");
			}

			if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getDateOfjourney())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Date of journey");
			}

			if (!HRMSHelper.validateDateFormate(request.getAirDetails().getDateOfjourney())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Date of journey");
			}

			if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getPreferedTime())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Air Prefered Time of journey");
			}

			if (!HRMSHelper.regexMatcher(request.getAirDetails().getPreferedTime(),
					"^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Air Prefered Time of journey");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getAirDetails().getRoundTrip())
					&& request.getAirDetails().getRoundTrip().equals(IHRMSConstants.isActive)) {
				if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getReturnDateOfjourney())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return date of journey");
				}

				if (!HRMSHelper.validateDateFormate(request.getAirDetails().getReturnDateOfjourney())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return date of journey");
				}

				if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getReturnPreferedTime())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return prefered time of journey");
				}

				if (!HRMSHelper.regexMatcher(request.getAirDetails().getReturnPreferedTime(),
						"^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return prefered time of journey");
				}

				SimpleDateFormat dateFormat = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);

				Date fromDate = dateFormat.parse(request.getAirDetails().getDateOfjourney());
				Date toDate = dateFormat.parse(request.getAirDetails().getReturnDateOfjourney());

				if (fromDate.after(toDate)) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1570));
				}
			}
		}
		if (!HRMSHelper.isNullOrEmpty(request.getBusDetails())) {

			if (HRMSHelper.isNullOrEmpty(request.getBusDetails().getBusType())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Type");
			} else {

				// check with master data
				long count = MasterDataLoad.busTypes.stream()
						.filter(e -> e.getBusType().equalsIgnoreCase(request.getBusDetails().getBusType())).count();
				if (count == 0) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Type");
				}
			}

			if (HRMSHelper.isNullOrEmpty(request.getBusDetails().getNoOfTravellers())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus No of travellers");
			}

			if (!HRMSHelper.validateNumber(String.valueOf(request.getBusDetails().getNoOfTravellers()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus No of travellers");
			}

			if (HRMSHelper.isNullOrEmpty(request.getBusDetails().getChargableClient())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Chargable to client");
			}

			if (!HRMSHelper.regexMatcher(request.getBusDetails().getChargableClient(), "^(Y|N)$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Chargable to client");
			}

			if (!HRMSHelper.regexMatcher(request.getBusDetails().getRoundTrip(), "^(Y|N)$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Bus Round trip");
			}

			if (HRMSHelper.isNullOrEmpty(request.getBusDetails().getFromLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus From Location");
			}

			if (HRMSHelper.isNumber(request.getBusDetails().getFromLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus From Location");
			}

			if (!HRMSHelper.regexMatcher(request.getBusDetails().getFromLocation(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus From Location");
			}

			if (HRMSHelper.isNullOrEmpty(request.getBusDetails().getToLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus To Location");
			}

			if (HRMSHelper.isNumber(request.getBusDetails().getToLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus To Location");
			}

			if (!HRMSHelper.regexMatcher(request.getBusDetails().getToLocation(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus To Location");
			}

			if (HRMSHelper.isNullOrEmpty(request.getBusDetails().getDateOfjourney())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Date of journey");
			}

			if (!HRMSHelper.validateDateFormate(request.getBusDetails().getDateOfjourney())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Date of journey");
			}

			if (HRMSHelper.isNullOrEmpty(request.getBusDetails().getPreferedTime())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Bus Prefered time of journey");
			}

			if (!HRMSHelper.regexMatcher(request.getBusDetails().getPreferedTime(),
					"^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Bus Prefered time of journey");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getBusDetails().getRoundTrip())
					&& request.getBusDetails().getRoundTrip().equals(IHRMSConstants.isActive)) {
				if (HRMSHelper.isNullOrEmpty(request.getBusDetails().getReturnDateOfjourney())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return date of journey");
				}

				if (!HRMSHelper.validateDateFormate(request.getBusDetails().getDateOfjourney())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return date of journey");
				}

				if (HRMSHelper.isNullOrEmpty(request.getBusDetails().getReturnPreferedTime())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return preferred time of journey");
				}

				if (!HRMSHelper.regexMatcher(request.getBusDetails().getReturnPreferedTime(),
						"^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return preferred time of journey");
				}

				SimpleDateFormat dateFormat = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);

				Date fromDate = dateFormat.parse(request.getBusDetails().getDateOfjourney());
				Date toDate = dateFormat.parse(request.getBusDetails().getReturnDateOfjourney());

				if (fromDate.after(toDate)) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1570));
				}
			}
		}
		if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails())) {

			if (HRMSHelper.isNullOrEmpty(request.getTrainDetails().getTrainType())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Type");
			} else {

				// check with master data
				long count = MasterDataLoad.busTypes.stream()
						.filter(e -> e.getBusType().equalsIgnoreCase(request.getTrainDetails().getTrainType())).count();
				if (count == 0) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Type");
				}
			}

			if (HRMSHelper.isNullOrEmpty(request.getTrainDetails().getNoOfTravellers())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train No of travellers");
			}

			if (!HRMSHelper.validateNumber(String.valueOf(request.getTrainDetails().getNoOfTravellers()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train No of travellers");
			}

			if (HRMSHelper.isNullOrEmpty(request.getTrainDetails().getChargableClient())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Train Chargable to client");
			}

			if (!HRMSHelper.regexMatcher(request.getTrainDetails().getChargableClient(), "^(Y|N)$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Train Chargable to client");
			}

			if (!HRMSHelper.regexMatcher(request.getTrainDetails().getRoundTrip(), "^(Y|N)$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Round trip");
			}

			if (HRMSHelper.isNullOrEmpty(request.getTrainDetails().getFromLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train From Location");
			}

			if (HRMSHelper.isNumber(request.getTrainDetails().getFromLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train From Location");
			}

			if (!HRMSHelper.regexMatcher(request.getTrainDetails().getFromLocation(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train From Location");
			}

			if (HRMSHelper.isNullOrEmpty(request.getTrainDetails().getToLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train To Location");
			}

			if (HRMSHelper.isNumber(request.getTrainDetails().getToLocation())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train To Location");
			}

			if (!HRMSHelper.regexMatcher(request.getTrainDetails().getToLocation(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train To Location");
			}

			if (HRMSHelper.isNullOrEmpty(request.getTrainDetails().getDateOfjourney())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Date of journey");
			}

			if (!HRMSHelper.validateDateFormate(request.getTrainDetails().getDateOfjourney())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Date of journey");
			}

			if (HRMSHelper.isNullOrEmpty(request.getTrainDetails().getPreferedTime())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Train Prefered Time of journey");
			}
			if (!HRMSHelper.regexMatcher(request.getTrainDetails().getPreferedTime(),
					"^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Train Prefered Time of journey");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails().getRoundTrip())
					&& request.getTrainDetails().getRoundTrip().equals(IHRMSConstants.isActive)) {
				if (HRMSHelper.isNullOrEmpty(request.getTrainDetails().getReturnDateOfjourney())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return date of journey");
				}

				if (!HRMSHelper.validateDateFormate(request.getTrainDetails().getReturnDateOfjourney())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return date of journey");
				}

				if (HRMSHelper.isNullOrEmpty(request.getTrainDetails().getReturnPreferedTime())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return preferred time of journey");
				}
				if (!HRMSHelper.regexMatcher(request.getTrainDetails().getReturnPreferedTime(),
						"^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Return preferred time of journey");
				}

				SimpleDateFormat dateFormat = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);

				Date fromDate = dateFormat.parse(request.getTrainDetails().getDateOfjourney());
				Date toDate = dateFormat.parse(request.getTrainDetails().getReturnDateOfjourney());

				if (fromDate.after(toDate)) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1570));
				}
			}
		}
	}

	public void addTravellerInputValidation(TravellerDetailsVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getTravellerType().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Type Id ");
		}

		if (HRMSHelper.isLongZero(request.getTravellerType().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Type Id ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Name");
		}
		boolean name = HRMSHelper.regexMatcher(request.getName(), "^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z\\s -]+(?<!\\s)$");
		if (!request.getTravellerType().getTravellerType().equalsIgnoreCase(IHRMSConstants.Internal_Employee) && !HRMSHelper.isNullOrEmpty(request.getName())) {
			if (!name) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Name of Traveller");
			}
		}

		if (HRMSHelper.isLongZero(request.getContactNo())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Contact Number ");
		}

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getContactNo()), "^\\d{9,15}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Contact Number ");
		}
		if (HRMSHelper.isNullOrEmpty(request.getEmailId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Email Id");
		}
		boolean email = HRMSHelper.regexMatcher(request.getEmailId(), "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
		if (!HRMSHelper.isNullOrEmpty(request.getName())) {
			if (!email) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Email Id");
			}
		}

		if (HRMSHelper.isNullOrEmpty(request.getDateOfBirth())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Date of Birth");
		}

		if (!HRMSHelper.validateDateFormate(request.getDateOfBirth())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Date of birth");
		}

	}

	public void addTravellerValidationForCabRquest(TravellerDetailsVO request) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getPickUpLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Pick up Location");
		}
		if (HRMSHelper.isNullOrEmpty(request.getDropLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Drop Location");
		}
		if (HRMSHelper.isNullOrEmpty(request.getPickUpTime())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveler pickup time");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getPickUpTime())
				&& !HRMSHelper.regexMatcher(request.getPickUpTime(), "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveler pickup time ");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getPickUpLocation())
				&& HRMSHelper.isNumber(request.getPickUpLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Pick up Location");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getPickUpLocation())
				&& !HRMSHelper.regexMatcher(request.getPickUpLocation(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Pick up Location");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getDropLocation()) && HRMSHelper.isNumber(request.getDropLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Drop Location");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getDropLocation())
				&& !HRMSHelper.regexMatcher(request.getDropLocation(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Drop Location");
		}

	}

	public void addInternationalTravellerValidation(TravellerDetailsVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getPassportNo()), "^[A-Za-z0-9]{6,15}$")) {
			// if (!HRMSHelper.regexMatcher(String.valueOf(request.getPassportNo()),
			// "^[A-Za-z0-9]{12,15}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Passport Number  ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getPassportDateOfExpiry())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Traveller passport expiry date");
		}

		if (!HRMSHelper.validateDateFormate(request.getPassportDateOfExpiry())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Traveller passport expiry date");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getVisaDateOfExpiry())
				&& !HRMSHelper.validateDateFormate(request.getVisaDateOfExpiry())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Visa expiry date");
		}

		/*
		 * if (HRMSHelper.isNullOrEmpty(request.getVisaDateOfExpiry())) { throw new
		 * HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) +
		 * "Traveller Visa expiry date"); }
		 */

		/*
		 * if (HRMSHelper.isNullOrEmpty(request.getVisaNo())) { throw new
		 * HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) +
		 * "Traveller Visa number"); }
		 */

		// if (!HRMSHelper.isNullOrEmpty(request.getVisaNo())
		// && !HRMSHelper.regexMatcher(String.valueOf(request.getVisaNo()),
		// "^4[0-9]{11,14}(?:[0-9]{3})?$")) {
		if (!HRMSHelper.isNullOrEmpty(request.getVisaNo())
				&& !HRMSHelper.regexMatcher(String.valueOf(request.getVisaNo()), "[A-Za-z0-9]{1,12}")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Traveller Visa number");
		}

		/*
		 * if (HRMSHelper.isNullOrEmpty(request.getVisaCountry())) { throw new
		 * HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) +
		 * "Traveller Visa country"); }
		 * 
		 * if (HRMSHelper.isNullOrEmpty(request.getVisaType())) { throw new
		 * HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) +
		 * "Traveller Visa Type"); }
		 */
	}

	public void cancelTravelRequestInputValidation(TravelDetailsCancelVO cancelVO) throws HRMSException {

		log.info("Inside cancelTravelRequestInputValidation method");

		if (!HRMSHelper.regexMatcher(String.valueOf(cancelVO.getTravelRequestId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}

		if (HRMSHelper.isLongZero(cancelVO.getTravelRequestId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}
		if (!HRMSHelper.regexMatcher(String.valueOf(cancelVO.getRequesterId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Requester Id ");
		}
		log.info("Exist cancelTravelRequestInputValidation method");
	}

	public void saveCabInputValidationTd(CabTravelRequestVO request) throws HRMSException {

		log.info("Inside saveCabInputValidationTd method");

		if (!HRMSHelper.isNullOrEmpty(request.getCabType())) {
			if (!HRMSHelper.regexMatcher(String.valueOf(request.getCabType().getId()), "[0-9]+")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Type Id ");
			}
			if (HRMSHelper.isLongZero(request.getCabType().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Type Id ");
			}
		}
		/*
		 * else { throw new HRMSException(1501,
		 * ResponseCode.getResponseCodeMap().get(1501) + " Cab Type "); }
		 */
		if (!HRMSHelper.isNullOrEmpty(request.getDriverName())) {
			if (!HRMSHelper.regexMatcher(String.valueOf(request.getDriverName().getId()), "[0-9]+")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Driver Id ");
			}

			if (HRMSHelper.isLongZero(request.getDriverName().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Driver Id ");
			}
		}
		/*
		 * else { throw new HRMSException(1501,
		 * ResponseCode.getResponseCodeMap().get(1501) + " Cab Driver Name "); }
		 */

		if (!HRMSHelper.isNullOrEmpty(request.getVehicleName())) {
			if (!HRMSHelper.regexMatcher(String.valueOf(request.getVehicleName().getId()), "[0-9]+")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Vehicle Id ");
			}

			if (HRMSHelper.isLongZero(request.getVehicleName().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Vehicle Id ");
			}
		}
		/*
		 * else { throw new HRMSException(1501,
		 * ResponseCode.getResponseCodeMap().get(1501) + " Vehicle Name "); }
		 */
		if (!HRMSHelper.isNullOrEmpty(request.getApproximateDistance())) {
			if (!HRMSHelper.regexMatcher(request.getApproximateDistance(), "[a-zA-Z0-9]{1,250}$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Cab Approximate Distance");
			}
		}

//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getApprover().getId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approver Id ");
//		}
//
//		if (HRMSHelper.isLongZero(request.getApprover().getId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approver Id ");
//		}
//
//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getApprover().getId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approver Id ");
//		}

//		if (HRMSHelper.isLongZero(request.getApprover().getId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approver Id ");
//		}

		if (!HRMSHelper.isNullOrEmpty(request.getApproximateCost())) {
			if (HRMSHelper.isFloatZero(request.getApproximateCost())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approximate Cost");
			}

			if (request.getApproximateCost() <= 1
					|| !HRMSHelper.regexMatcher(String.valueOf(request.getApproximateCost()), "[0-9.]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approximate Cost");
			}
		}
//		else {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "  Cab Approximate Cost");
//		}

		if (!HRMSHelper.isNullOrEmpty(request.getApproximateComment())) {
			if (!HRMSHelper.regexMatcher(request.getApproximateComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Comment ");
			}
		}

		log.info("Exist saveCabInputValidationTd method");
	}

	public void saveAccommodationInputValidationTd(AccommodationTravelRequestVO request) throws HRMSException {

		log.info("Inside saveAccommodationInputValidationTd method");

//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getApprover().getId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Approver Id ");
//		}
//
//		if (HRMSHelper.isLongZero(request.getApprover().getId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Approver Id ");
//		}
		if (!HRMSHelper.isNullOrEmpty(request.getApproximateComment())) {
			if (!HRMSHelper.regexMatcher(request.getApproximateComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Comment ");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getApproximateCost())) {
			if (HRMSHelper.isFloatZero(request.getApproximateCost())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Approximate Cost");
			}

			if (request.getApproximateCost() <= 1
					|| !HRMSHelper.regexMatcher(String.valueOf(request.getApproximateCost()), "[0-9.]{1,250}$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Approximate Cost");
			}
		}
		log.info("Exist saveAccommodationInputValidationTd method");
	}

	public void saveTicketInputValidationTd(TicketTravelRequestVO request) throws HRMSException {
		log.info("Inside saveTicketInputValidationTd method");
		if (!HRMSHelper.isNullOrEmpty(request.getAirDetails())) {

//			if (!HRMSHelper.regexMatcher(String.valueOf(request.getAirDetails().getApprover().getId()), "[0-9]+")) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Approver Id ");
//			}

//			if (HRMSHelper.isLongZero(request.getAirDetails().getApprover().getId())) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Approver Id ");
//			}
//			if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getApproximateComment())) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Comment ");
//			}
			if (!HRMSHelper.isNullOrEmpty(request.getAirDetails().getApproximateComment())) {
				if (!HRMSHelper.regexMatcher(request.getAirDetails().getApproximateComment(),
						"[a-zA-Z0-9.-;, ]{1,250}$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Air Approximate Comment ");
				}
			}

			if (!HRMSHelper.isNullOrEmpty(request.getAirDetails().getApproximateCost())) {
				if (HRMSHelper.isFloatZero(request.getAirDetails().getApproximateCost())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Air Approximate Cost");
				}

				if (request.getAirDetails().getApproximateCost() <= 1 || !HRMSHelper
						.regexMatcher(String.valueOf(request.getAirDetails().getApproximateCost()), "[0-9.]{1,250}$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Air Approximate Cost");
				}
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getBusDetails())) {

//			if (!HRMSHelper.regexMatcher(String.valueOf(request.getBusDetails().getApprover().getId()), "[0-9]+")) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Approver Id ");
//			}

//			if (HRMSHelper.isLongZero(request.getBusDetails().getApprover().getId())) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Approver Id ");
//			}

			if (!HRMSHelper.isNullOrEmpty(request.getBusDetails().getApproximateComment())) {

				if (!HRMSHelper.regexMatcher(request.getBusDetails().getApproximateComment(),
						"[a-zA-Z0-9.-;, ]{1,250}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Comment ");
				}
			}

			if (!HRMSHelper.isNullOrEmpty(request.getBusDetails().getApproximateCost())) {
				if (HRMSHelper.isFloatZero(request.getBusDetails().getApproximateCost())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Bus Approximate Cost");
				}

				if (request.getBusDetails().getApproximateCost() <= 1 || !HRMSHelper
						.regexMatcher(String.valueOf(request.getBusDetails().getApproximateCost()), "[0-9.]{1,250}$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Bus Approximate Cost");
				}
			}

		}

		if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails())) {

//			if (!HRMSHelper.regexMatcher(String.valueOf(request.getTrainDetails().getApprover().getId()), "[0-9]+")) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Approver Id ");
//			}

//			if (HRMSHelper.isLongZero(request.getTrainDetails().getApprover().getId())) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Approver Id ");
//			}
			if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails().getApproximateComment())) {
				if (!HRMSHelper.regexMatcher(request.getTrainDetails().getApproximateComment(),
						"[a-zA-Z0-9.-;, ]{1,250}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Comment ");
				}
			}

			if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails().getApproximateCost())) {
				if (HRMSHelper.isFloatZero(request.getTrainDetails().getApproximateCost())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Train Approximate Cost");
				}

				if (request.getTrainDetails().getApproximateCost() <= 1
						|| !HRMSHelper.regexMatcher(String.valueOf(request.getTrainDetails().getApproximateCost()),
								"[0-9.]{1,250}$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Train Approximate Cost");
				}
			}

		}

		log.info("Exist saveTicketInputValidationTd method");
	}

	// ***********submit ticket****************

	public void submitTicketInputValidationTd(TicketTravelRequestVO request) throws HRMSException {
		log.info("Inside saveTicketInputValidationTd method");
		if (!HRMSHelper.isNullOrEmpty(request.getAirDetails())) {

//			if (!HRMSHelper.regexMatcher(String.valueOf(request.getAirDetails().getApprover().getId()), "[0-9]+")) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Approver Id ");
//			}

//			if (HRMSHelper.isLongZero(request.getAirDetails().getApprover().getId())) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Approver Id ");
//			}
//			if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getApproximateComment())) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Comment ");
//			}

			if (!HRMSHelper.regexMatcher(request.getAirDetails().getApproximateComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Air Approximate Comment ");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getAirDetails().getApproximateCost())) {
				if (HRMSHelper.isFloatZero(request.getAirDetails().getApproximateCost())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Air Approximate Cost");
				}

				if (request.getAirDetails().getApproximateCost() <= 1 || !HRMSHelper
						.regexMatcher(String.valueOf(request.getAirDetails().getApproximateCost()), "[0-9.]{1,250}$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Air Approximate Cost");
				}
			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Approximate Cost");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getBusDetails())) {

//			if (!HRMSHelper.regexMatcher(String.valueOf(request.getBusDetails().getApprover().getId()), "[0-9]+")) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Approver Id ");
//			}

//			if (HRMSHelper.isLongZero(request.getBusDetails().getApprover().getId())) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Approver Id ");
//			}

			if (HRMSHelper.isNullOrEmpty(request.getBusDetails().getApproximateComment())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Comment ");
			}

			if (!HRMSHelper.regexMatcher(request.getBusDetails().getApproximateComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Comment ");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getBusDetails().getApproximateCost())) {
				if (HRMSHelper.isFloatZero(request.getBusDetails().getApproximateCost())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Bus Approximate Cost");
				}

				if (request.getBusDetails().getApproximateCost() <= 1 || !HRMSHelper
						.regexMatcher(String.valueOf(request.getBusDetails().getApproximateCost()), "[0-9.]{1,250}$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Bus Approximate Cost");
				}
			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "  Bus Approximate Cost");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails())) {

//			if (!HRMSHelper.regexMatcher(String.valueOf(request.getTrainDetails().getApprover().getId()), "[0-9]+")) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Approver Id ");
//			}

//			if (HRMSHelper.isLongZero(request.getTrainDetails().getApprover().getId())) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Approver Id ");
//			}
			if (HRMSHelper.isNullOrEmpty(request.getTrainDetails().getApproximateComment())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Comment ");
			}

			if (!HRMSHelper.regexMatcher(request.getTrainDetails().getApproximateComment(),
					"[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Comment ");
			}
			if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails().getApproximateCost())) {
				if (HRMSHelper.isFloatZero(request.getTrainDetails().getApproximateCost())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Train Approximate Cost");
				}

				if (request.getTrainDetails().getApproximateCost() < 1) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Train Approximate Cost");
				}
			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "  Train Approximate Cost");
			}
		}

		log.info("Exist saveTicketInputValidationTd method");
	}

	/************** for final cost TD ************************/

	public void saveCabInputValidationTdFinal(CabTravelRequestVO request) throws HRMSException {

		log.info("Inside saveCabInputValidationTd method");

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Details Id ");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getFinalCost())) {
			if (HRMSHelper.isFloatZero(request.getFinalCost())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Final Cost");
			}

			if (!HRMSHelper.regexMatcher(String.valueOf(request.getFinalCost()), "[0-9.]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Final Cost");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getFinalCostcomment())) {
			if (!HRMSHelper.regexMatcher(request.getFinalCostcomment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Final Cost Comment ");
			}
		}

		log.info("Exist saveCabInputValidationTd Final method");
	}

	public void submitCabInputValidationTdFinal(CabTravelRequestVO request) throws HRMSException {

		log.info("Inside submitCabInputValidationTd method");

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Details Id ");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getFinalCost()) && HRMSHelper.isFloatZero(request.getFinalCost())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Final Cost");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getFinalCost())
				&& !HRMSHelper.regexMatcher(String.valueOf(request.getFinalCost()), "[0-9.]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Final Cost");
		}

//		if (HRMSHelper.isNullOrEmpty(request.getFinalCostcomment())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab  Final Cost Comment ");
//		}

		if (!HRMSHelper.isNullOrEmpty(request.getFinalCostcomment())
				&& !HRMSHelper.regexMatcher(request.getFinalCostcomment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Final Cost Comment ");
		}
		log.info("Exist submitCabInputValidationTd Final method");
	}

	public void saveAccommodationInputValidationTdFinal(AccommodationTravelRequestVO request) throws HRMSException {

		log.info("Inside saveAccommodationInputValidationTd method");

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accomodation Details Id ");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getFinalCostcomment())) {
			if (!HRMSHelper.regexMatcher(request.getFinalCostcomment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Final cost Comment ");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getFinalCost())) {
			if (HRMSHelper.isFloatZero(request.getFinalCost())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Final Cost");
			}

			if (!HRMSHelper.regexMatcher(String.valueOf(request.getFinalCost()), "[0-9.]{1,250}$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Final Cost");
			}
		}
		log.info("Exist saveAccommodationInputValidationTd method");
	}

	public void submitAccommodationInputValidationTdFinal(AccommodationTravelRequestVO request) throws HRMSException {

		log.info("Inside submitAccommodationInputValidationTd method");

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accomodation Details Id ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getFinalCostcomment())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Final cost Comment ");
		}

		if (!HRMSHelper.regexMatcher(request.getFinalCostcomment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Final cost Comment ");
		}
		if (!HRMSHelper.isNullOrEmpty(request.getFinalCost())) {
			if (HRMSHelper.isFloatZero(request.getFinalCost())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Final Cost");
			}

			if (!HRMSHelper.regexMatcher(String.valueOf(request.getFinalCost()), "[0-9.]{1,250}$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Final Cost");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Final Cost");
		}
		log.info("Exist submitAccommodationInputValidationTd method");
	}

	public void saveTicketInputValidationTdFinal(TicketTravelRequestVO request) throws HRMSException {
		log.info("Inside saveTicketInputValidationTd Final method");
		if (!HRMSHelper.isNullOrEmpty(request.getAirDetails())) {

			if (HRMSHelper.isLongZero(request.getAirDetails().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Details Id ");
			}
			if (!HRMSHelper.isNullOrEmpty(request.getAirDetails().getFinalCostcomment())) {
				if (!HRMSHelper.regexMatcher(request.getAirDetails().getFinalCostcomment(),
						"[a-zA-Z0-9.-;, ]{1,250}$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Air Final cost Comment ");
				}
			}

			if (!HRMSHelper.isNullOrEmpty(request.getAirDetails().getFinalCost())) {
				if (HRMSHelper.isFloatZero(request.getAirDetails().getFinalCost())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Final Cost");
				}

				if (!HRMSHelper.regexMatcher(String.valueOf(request.getAirDetails().getFinalCost()),
						"[0-9.]{1,250}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Final Cost");
				}
			}
//			else {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Final Cost");
//			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getBusDetails())) {

			if (HRMSHelper.isLongZero(request.getBusDetails().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Details Id ");
			}
			if (!HRMSHelper.isNullOrEmpty(request.getBusDetails().getFinalCostcomment())) {
				if (!HRMSHelper.regexMatcher(request.getBusDetails().getFinalCostcomment(),
						"[a-zA-Z0-9.-;, ]{1,250}$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Bus Final Cost Comment ");
				}
			}

			if (!HRMSHelper.isNullOrEmpty(request.getBusDetails().getFinalCost())) {
				if (HRMSHelper.isFloatZero(request.getBusDetails().getFinalCost())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Final Cost");
				}

				if (!HRMSHelper.regexMatcher(String.valueOf(request.getBusDetails().getFinalCost()),
						"[0-9.]{1,250}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Final Cost");
				}
			}

		}

		if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails())) {

			if (HRMSHelper.isLongZero(request.getTrainDetails().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Details Id ");
			}
			if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails().getFinalCostcomment())) {
				if (!HRMSHelper.regexMatcher(request.getTrainDetails().getFinalCostcomment(),
						"[a-zA-Z0-9.-;, ]{1,250}$")) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Train Final Cost Comment ");
				}
			}

			if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails().getFinalCost())) {
				if (HRMSHelper.isFloatZero(request.getTrainDetails().getFinalCost())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Final Cost");
				}

				if (!HRMSHelper.regexMatcher(String.valueOf(request.getTrainDetails().getFinalCost()),
						"[0-9.]{1,250}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Final Cost");
				}
			}
		}

		log.info("Exist saveTicketInputValidationTd Final method");
	}

	public void submitTicketInputValidationTdFinal(TicketTravelRequestVO request) throws HRMSException {
		log.info("Inside submitTicketInputValidationTd Final method");
		if (!HRMSHelper.isNullOrEmpty(request.getAirDetails())) {

			if (HRMSHelper.isLongZero(request.getAirDetails().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Details Id ");
			}
			if (HRMSHelper.isNullOrEmpty(request.getAirDetails().getFinalCostcomment())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Final cost Comment ");
			}

			if (!HRMSHelper.regexMatcher(request.getAirDetails().getFinalCostcomment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Final cost Comment ");
			}
			if (!HRMSHelper.isNullOrEmpty(request.getAirDetails().getFinalCost())) {
				if (HRMSHelper.isFloatZero(request.getAirDetails().getFinalCost())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Final Cost");
				}

				if (!HRMSHelper.regexMatcher(String.valueOf(request.getAirDetails().getFinalCost()),
						"[0-9.]{1,250}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Final Cost");
				}
			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Air Final Cost");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getBusDetails())) {

			if (HRMSHelper.isLongZero(request.getBusDetails().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Details Id ");
			}
			if (HRMSHelper.isNullOrEmpty(request.getBusDetails().getFinalCostcomment())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Final Cost Comment ");
			}

			if (!HRMSHelper.regexMatcher(request.getBusDetails().getFinalCostcomment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Final Cost Comment ");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getBusDetails().getFinalCost())) {
				if (HRMSHelper.isFloatZero(request.getBusDetails().getFinalCost())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Final Cost");
				}

				if (!HRMSHelper.regexMatcher(String.valueOf(request.getBusDetails().getFinalCost()),
						"[0-9.]{1,250}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Final Cost");
				}
			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bus Final Cost");
			}

		}

		if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails())) {

			if (HRMSHelper.isLongZero(request.getTrainDetails().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Details Id ");
			}
			if (HRMSHelper.isNullOrEmpty(request.getTrainDetails().getFinalCostcomment())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Train  Final Cost Comment ");
			}

			if (!HRMSHelper.regexMatcher(request.getTrainDetails().getFinalCostcomment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Train Final Cost Comment ");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails().getFinalCost())) {
				if (HRMSHelper.isFloatZero(request.getTrainDetails().getFinalCost())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Final Cost");
				}

				if (!HRMSHelper.regexMatcher(String.valueOf(request.getTrainDetails().getFinalCost()),
						"[0-9.]{1,250}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Final Cost");
				}
			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Train Final Cost");
			}
		}

		log.info("Exist saveTicketInputValidationTd Final method");
	}

	public void approveTravelRequestInputValidation(TravelRequestApprovalVO approvalVO) throws HRMSException {

		log.info("Inside Travel Request Approval Input Validation method");
		if (!HRMSHelper.regexMatcher(String.valueOf(approvalVO.getTravelRequestId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}

		if (HRMSHelper.isLongZero(approvalVO.getTravelRequestId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}

		if (HRMSHelper.isNullOrEmpty(approvalVO.getApproverComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Approved Comment ");
		}

		if (!HRMSHelper.regexMatcher(approvalVO.getApproverComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Approved Comment ");
		}

		log.info("Exist Travel Request Approval Input Validation method");
	}

	public void rejectTravelRequestRequestInputValidation(TravelRequestRejectionVO rejectionVO) throws HRMSException {

		log.info("Inside Travel Request Reject Input Validation method");

		if (!HRMSHelper.regexMatcher(String.valueOf(rejectionVO.getTravelRequestId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}

		if (HRMSHelper.isLongZero(rejectionVO.getTravelRequestId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}

		if (HRMSHelper.isNullOrEmpty(rejectionVO.getApproverComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Reject Comment ");
		}

		if (!HRMSHelper.regexMatcher(rejectionVO.getApproverComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Reject Comment ");
		}

		log.info("Exist Travel Request Reject Input Validation method");
	}

	public void expenseSummaryRequestInputValidation(ExpenseSummeryRequest expenseSummaryRequest) throws HRMSException {

		log.info("Inside Expense Summery Request Input Validation method");
		if (!HRMSHelper.regexMatcher(String.valueOf(expenseSummaryRequest.getTravelRequestId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}

		if (HRMSHelper.isLongZero(expenseSummaryRequest.getTravelRequestId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}

//	    // Validate airExpenseVO
//	    validateExpenseVO(expenseSummaryRequest.getTicketExpense().getAirExpenseVO(), "Air");
//
//	    // Validate busExpenseVO
//	    validateExpenseVO(expenseSummaryRequest.getTicketExpense().getBusExpenseVO(), "Bus");
//
//	    // Validate trainExpenseVO
//	    validateExpenseVO(expenseSummaryRequest.getTicketExpense().getTrainExpenseVO(), "Train");
//
//	    // Validate cabExpenseVO
//	    validateExpenseVO(expenseSummaryRequest.getCabExpenseVO(), "Cab");
//
//	    // Validate accommodationExpenseVO
//	    validateExpenseVO(expenseSummaryRequest.getAccommodationExpenseVO(), "Accommodation");

		log.info("Exist Expense Summery Request Input Validation method");
	}

//	private void validateExpenseVO(ExpenseVO expenseVO, String expenseType) throws HRMSException {
//	    if (!HRMSHelper.isNullOrEmpty(expenseVO)) {
//	        // Validate refundCost
//	        if (!HRMSHelper.isFloatZero(expenseVO.getRefundCost().floatValue())) {
//	            throw new HRMSException(1501, "Invalid " + expenseType + " Refund Cost: " + expenseVO.getRefundCost());
//	        }
//
//	        if (!HRMSHelper.regexMatcher(String.valueOf(expenseVO.getRefundCost()), "^\\d*\\.?\\d+$")) {
//	            throw new HRMSException(1501, "Invalid " + expenseType + " Refund Cost: " + expenseVO.getRefundCost() + ". Must be a number.");
//	        }
//
//	        // Validate settledCost
//	        if (!HRMSHelper.isNullOrEmpty(expenseVO.getSetteledCost())) {
//	            throw new HRMSException(1501, "Invalid " + expenseType + " Settled Cost: null");
//	        }
//
//	        if (!HRMSHelper.isFloatZero(expenseVO.getSetteledCost().floatValue())) {
//	            throw new HRMSException(1501, "Invalid " + expenseType + " Settled Cost: " + expenseVO.getSetteledCost());
//	        }
//
//	        if (!HRMSHelper.regexMatcher(String.valueOf(expenseVO.getSetteledCost()), "^\\d*\\.?\\d+$")) {
//	            throw new HRMSException(1501, "Invalid " + expenseType + " Settled Cost: " + expenseVO.getSetteledCost() + ". Must be a number.");
//	        }
//	    }
//	}

	public void downloadExpenseSummaryInputValidation(DownloadExpenseSummaryReqVO summeryReqVO) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(summeryReqVO.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From date.");
		} else {
			if (!HRMSHelper.validateDateFormate(summeryReqVO.getFromDate())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From date.");
			}
		}
		if (HRMSHelper.isNullOrEmpty(summeryReqVO.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To date.");
		} else {
			if (!HRMSHelper.validateDateFormate(summeryReqVO.getToDate())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To date.");
			}
		}

	}

	public void getTravelSummaryRequestInputValidation(Long travelRequestId) throws HRMSException {

		log.info("Inside get Travel Summary method input validation");

		if (!HRMSHelper.regexMatcher(String.valueOf(travelRequestId), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}

		if (HRMSHelper.isLongZero(travelRequestId)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request Id ");
		}

		log.info("Exist get Travel Summary method validation");
	}

	public void submitCabInputValidationTd(CabTravelRequestVO request) throws HRMSException {

		log.info("Inside submitCabInputValidationTd method");

		if (!HRMSHelper.isNullOrEmpty(request.getCabType())) {
			if (!HRMSHelper.regexMatcher(String.valueOf(request.getCabType().getId()), "[0-9]+")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Type Id ");
			}
			if (HRMSHelper.isLongZero(request.getCabType().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Type Id ");
			}
		}
		/*
		 * else { throw new HRMSException(1501,
		 * ResponseCode.getResponseCodeMap().get(1501) + " Cab Type "); }
		 */
//		if(!HRMSHelper.isNullOrEmpty(request.getDriverName())) {		
		if (!HRMSHelper.isNullOrEmpty(request.getDriverName())
				&& !HRMSHelper.regexMatcher(String.valueOf(request.getDriverName().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Driver Id ");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getDriverName())
				&& HRMSHelper.isLongZero(request.getDriverName().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Driver Id ");
		}
//		} 
		/*
		 * else { throw new HRMSException(1501,
		 * ResponseCode.getResponseCodeMap().get(1501) + " Cab Driver Name "); }
		 */

		// if(!HRMSHelper.isNullOrEmpty(request.getVehicleName())) {
		if (!HRMSHelper.isNullOrEmpty(request.getVehicleName())
				&& !HRMSHelper.regexMatcher(String.valueOf(request.getVehicleName().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Vehicle Id ");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getVehicleName())
				&& HRMSHelper.isLongZero(request.getVehicleName().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Vehicle Id ");
		}
		// }
		/*
		 * else { throw new HRMSException(1501,
		 * ResponseCode.getResponseCodeMap().get(1501) + " Vehicle Name "); }
		 */
		if (HRMSHelper.isNullOrEmpty(request.getApproximateDistance())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approximate Distance");
		}

		if (!HRMSHelper.regexMatcher(request.getApproximateDistance(), "[a-zA-Z0-9]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approximate Distance");
		}

//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getApprover().getId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approver Id ");
//		}
//
//		if (HRMSHelper.isLongZero(request.getApprover().getId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approver Id ");
//		}
//
//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getApprover().getId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approver Id ");
//		}

//		if (HRMSHelper.isLongZero(request.getApprover().getId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approver Id ");
//		}

		if (!HRMSHelper.isNullOrEmpty(request.getApproximateCost())) {
			if (HRMSHelper.isFloatZero(request.getApproximateCost())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approximate Cost");
			}

			if (request.getApproximateCost() <= 1
					|| !HRMSHelper.regexMatcher(String.valueOf(request.getApproximateCost()), "[0-9.]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Approximate Cost");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "  Cab Approximate Cost");
		}

		if (HRMSHelper.isNullOrEmpty(request.getApproximateComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Comment ");
		}

		if (!HRMSHelper.regexMatcher(request.getApproximateComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Comment ");
		}
		log.info("Exist submitCabInputValidationTd method");
	}

	public void submitAccommodationInputValidationTd(AccommodationTravelRequestVO request) throws HRMSException {

		log.info("Inside saveAccommodationInputValidationTd method");

//		if (!HRMSHelper.regexMatcher(String.valueOf(request.getApprover().getId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Approver Id ");
//		}
//
//		if (HRMSHelper.isLongZero(request.getApprover().getId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Approver Id ");
//		}
		if (HRMSHelper.isNullOrEmpty(request.getApproximateComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Comment ");
		}

		if (!HRMSHelper.regexMatcher(request.getApproximateComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Comment ");
		}
		if (!HRMSHelper.isNullOrEmpty(request.getApproximateCost())) {
			if (HRMSHelper.isFloatZero(request.getApproximateCost())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Approximate Cost");
			}

			if (request.getApproximateCost() <= 1
					|| !HRMSHelper.regexMatcher(String.valueOf(request.getApproximateCost()), "[0-9.]{1,250}$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Approximate Cost");
			}
		} else {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + "  Accommodation Approximate Cost");
		}
		log.info("Exist submitAccommodationInputValidationTd method");
	}

}