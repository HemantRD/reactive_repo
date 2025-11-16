package com.vinsys.hrms.services.traveldesk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRecurringRequestDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSCabRequestPassengerDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMapCabDriverVehicleDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterDriverDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.traveldesk.VODriverAssignmentModel;
import com.vinsys.hrms.entity.traveldesk.CabRecurringRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequestPassenger;
import com.vinsys.hrms.entity.traveldesk.MapCabDriverVehicle;
import com.vinsys.hrms.entity.traveldesk.MasterDriver;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.MarathiHelper;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/masterDriver")

public class DriverService {

	private static final Logger logger = LoggerFactory.getLogger(DriverService.class);

	@Autowired
	IHRMSMasterDriverDAO driverDAO;

	@Autowired
	IHRMSMapCabDriverVehicleDAO mapCabDriverVehicleDAO;
	@Autowired
	IHRMSCabRecurringRequestDAO cabRecurringDAO;
	@Autowired
	IHRMSCabRequestPassengerDAO cabPassengerDAO;

	MarathiHelper marathiHelper = new MarathiHelper();

	@RequestMapping(value = "/{orgId}/{divId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllDriverDetails(@PathVariable("orgId") long orgId, @PathVariable("divId") long divId) {
		try {
			logger.info(" *** Inside getAllDriverDetails()  *** ");
			List<MasterDriver> driverList = null;
			// MasterDriver driver = null;

			if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(divId)) {

				driverList = driverDAO.findAllDriverByOrgIdandDivId(orgId, divId, IHRMSConstants.isActive);

				if (!HRMSHelper.isNullOrEmpty(driverList)) {

					HRMSListResponseObject response = new HRMSListResponseObject();

					List<Object> listResponse = new ArrayList<Object>();

					for (MasterDriver driverEntity : driverList) {

						listResponse.add((Object) HRMSResponseTranslator.translateToMasterDriverResponse(driverEntity));

					}
					logger.info("list Count ==== >> " + listResponse.size());
					response.setListResponse(listResponse);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					response.setTotalCount(driverList.size());
					// response.setPageNo(page);
					// response.setPageSize(size);
					return HRMSHelper.createJsonString(response);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}

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

	@RequestMapping(value = "/getDriverAssignment/{driverId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getLoggedInDriverAssignment(@PathVariable("driverId") long employeeId)
			throws JsonGenerationException, JsonMappingException, IOException {
		try {

			MasterDriver masterDriver = driverDAO.findDriverByEmployeeId(employeeId, IHRMSConstants.isActive);

			if (masterDriver == null) {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode,
						IHRMSConstants.TRAVEL_REQUEST_DRIVER_NOT_FOUND);
			}

			List<MapCabDriverVehicle> cabDriverVehicleMappingList = mapCabDriverVehicleDAO
					.findMapCabDriverVehicleByDriverId(masterDriver.getId(), IHRMSConstants.isActive);

			List<Object> list = new ArrayList<Object>();

			if (!HRMSHelper.isNullOrEmpty(cabDriverVehicleMappingList)) {

				for (MapCabDriverVehicle cabDriverVehicleMapping : cabDriverVehicleMappingList) {

					if (!HRMSHelper.isNullOrEmpty(cabDriverVehicleMapping)) {

						long cabRequestPassengerID = cabDriverVehicleMapping.getCabRequestPassengerId();

						if (cabDriverVehicleMapping.isRecurring()) {

							/**
							 * Finding Request for return trip completion status is pending
							 */

							List<CabRecurringRequest> cabRecurringRequestReturnList = new ArrayList<CabRecurringRequest>();
							;
							List<CabRecurringRequest> cabRecurringOnewayRequestList = new ArrayList<CabRecurringRequest>();
							;

							if (cabDriverVehicleMapping.getTripWay()
									.equalsIgnoreCase(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_ONE_WAY)) {

								cabRecurringOnewayRequestList = cabRecurringDAO.findCabRcrReqByOneWayRowId(
										cabRequestPassengerID, IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING,
										IHRMSConstants.isActive);
							} else if (cabDriverVehicleMapping.getTripWay()
									.equalsIgnoreCase(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_RETURN)) {
								cabRecurringRequestReturnList = cabRecurringDAO.findCabRcrReqByReturnTripStatusRowId(
										cabRequestPassengerID, IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING,
										IHRMSConstants.isActive);
							}

							/**
							 * Finding Request for one way completion status is pending, can have the same
							 * request in above query
							 */

							cabRecurringRequestReturnList.addAll(cabRecurringOnewayRequestList);

							// cabRecurringRequestReturnList.addAll(cabRecurringOnewayRequestList);

							for (CabRecurringRequest cabRecurringEntity : cabRecurringRequestReturnList) {
								VODriverAssignmentModel model = new VODriverAssignmentModel();

								if (cabDriverVehicleMapping.getTripWay()
										.equalsIgnoreCase(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_RETURN)) {
									String pickupLocation = "";
									String dropLocation = "";
									String pickupTime = "";
									pickupLocation = (marathiHelper
											.latinToDevanagari(cabRecurringEntity.getDropLocation())).concat("( ")
											.concat(cabRecurringEntity.getDropLocation()).concat(" )");
									dropLocation = (marathiHelper.latinToDevanagari(cabRecurringEntity.getPickupAt())
											.concat("( ").concat(cabRecurringEntity.getPickupAt()).concat(" )"));
									pickupTime = (marathiHelper.latinToDevanagari(cabRecurringEntity.getReturnTime())
											.concat("( ").concat(cabRecurringEntity.getReturnTime()).concat(" )"));

									model.setPickupAt(pickupLocation);
									model.setPickupTime(pickupTime);
									model.setDropLocation(dropLocation);
								} else {

									String pickupLocation = "";
									String dropLocation = "";
									String pickupTime = "";
									pickupLocation = (marathiHelper.latinToDevanagari(cabRecurringEntity.getPickupAt())
											.concat("( ").concat(cabRecurringEntity.getPickupAt()).concat(" )"));
									dropLocation = (marathiHelper
											.latinToDevanagari(cabRecurringEntity.getDropLocation()).concat("( ")
											.concat(cabRecurringEntity.getDropLocation()).concat(" )"));
									pickupTime = (marathiHelper.latinToDevanagari(cabRecurringEntity.getPickupTime())
											.concat("( ").concat(cabRecurringEntity.getPickupTime()).concat(" )"));

									model.setPickupAt(pickupLocation);
									model.setDropLocation(dropLocation);
									model.setPickupTime(pickupTime);
								}

								String passengerName = (marathiHelper
										.latinToDevanagari(
												cabRecurringEntity.getCabRequestPassenger().getPassengerName())
										.concat("( ")
										.concat(cabRecurringEntity.getCabRequestPassenger().getPassengerName())
										.concat(" )"));
								String contactNo = (marathiHelper
										.latinToDevanagari(
												cabRecurringEntity.getCabRequestPassenger().getContactNumber())
										.concat("( ")
										.concat(cabRecurringEntity.getCabRequestPassenger().getContactNumber())
										.concat(" )"));
								// String
								// id=(marathiHelper.latinToDevanagari(String.valueOf(cabRecurringEntity.getId())).concat("(
								// ").concat(String.valueOf(cabRecurringEntity.getId())).concat(" )"));
								model.setPassengerName(passengerName);
								model.setEmailId(cabRecurringEntity.getCabRequestPassenger().getEmailId());
								model.setDateOfBirth(HRMSDateUtil.format(
										cabRecurringEntity.getCabRequestPassenger().getDateOfBirth(),
										IHRMSConstants.FRONT_END_DATE_FORMAT));
								model.setContactNumber(contactNo);
								model.setId(cabRecurringEntity.getId());

								// model.setPickupDate(HRMSDateUtil.format(cabRecurringEntity.getPickupDate(),
								// IHRMSConstants.FRONT_END_DATE_FORMAT));

								String pickupdate = marathiHelper
										.latinToDevanagari(HRMSDateUtil.format(cabRecurringEntity.getPickupDate(),
												IHRMSConstants.FRONT_END_DATE_FORMAT))
										.concat("( ").concat(HRMSDateUtil.format(cabRecurringEntity.getPickupDate(),
												IHRMSConstants.FRONT_END_DATE_FORMAT))
										.concat(" )");

								model.setPickupDate(pickupdate);
								String drivercomment = "";
								if (!HRMSHelper.isNullOrEmpty(cabRecurringEntity.getDriverComment()))
									drivercomment = (marathiHelper
											.latinToDevanagari(cabRecurringEntity.getDriverComment()).concat("( ")
											.concat(cabRecurringEntity.getDriverComment()).concat(" )"));

								model.setDriverComment(drivercomment);

								// model.setPickupTime(cabRecurringEntity.getPickupTime());
								// model.setPickupAt(cabRecurringEntity.getPickupAt());
								model.setReturnDate(HRMSDateUtil.format(cabRecurringEntity.getReturnDate(),
										IHRMSConstants.FRONT_END_DATE_FORMAT));
								model.setReturnTime(cabRecurringEntity.getReturnTime());
								// model.setDropLocation(cabRecurringEntity.getDropLocation());
								model.setChargeableToClient(cabRecurringEntity.isChargeableToClient());
								model.setTripType(cabDriverVehicleMapping.getTripWay());
								model.setTravelRequestId(cabRecurringEntity.getCabRequestPassenger().getCabRequest()
										.getTravelRequest().getId());
								// has set as true as this block is for recurring only
								model.setRecurring(true);
								list.add(model);

							}
						} else {

							List<CabRequestPassenger> cabRequestPassengerOneWayList = new ArrayList<CabRequestPassenger>();
							List<CabRequestPassenger> cabRequestPassengerReturnList = new ArrayList<CabRequestPassenger>();
							;

							if (cabDriverVehicleMapping.getTripWay()
									.equalsIgnoreCase(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_ONE_WAY)) {

								cabRequestPassengerOneWayList = cabPassengerDAO.findCabPassengerListOneWayAndRowId(
										cabRequestPassengerID, IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING,
										IHRMSConstants.isActive);

							} else if (cabDriverVehicleMapping.getTripWay()
									.equalsIgnoreCase(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_RETURN)) {
								cabRequestPassengerReturnList = cabPassengerDAO.findCabPassengerListReturnByRowId(
										cabRequestPassengerID, IHRMSConstants.TRAVEL_REQUEST_STATUS_PENDING,
										IHRMSConstants.isActive);
							}

							cabRequestPassengerOneWayList.addAll(cabRequestPassengerReturnList);

							if (!HRMSHelper.isNullOrEmpty(cabRequestPassengerOneWayList)) {

								for (CabRequestPassenger passengerEntity : cabRequestPassengerOneWayList) {

									VODriverAssignmentModel model = new VODriverAssignmentModel();
									String passengerName = (marathiHelper
											.latinToDevanagari(passengerEntity.getPassengerName()).concat("( ")
											.concat(passengerEntity.getPassengerName()).concat(" )"));
									// model.setPassengerName(passengerEntity.getPassengerName());

									model.setPassengerName(passengerName);

									String drivercomment = "";
									if (!HRMSHelper.isNullOrEmpty(passengerEntity.getDriverComment()))
										drivercomment = (marathiHelper
												.latinToDevanagari(passengerEntity.getDriverComment()).concat("( ")
												.concat(passengerEntity.getDriverComment()).concat(" )"));

									model.setDriverComment(drivercomment);

									model.setEmailId(passengerEntity.getEmailId());
									model.setDateOfBirth(HRMSDateUtil.format(passengerEntity.getDateOfBirth(),
											IHRMSConstants.FRONT_END_DATE_FORMAT));
									String contactNo = (marathiHelper
											.latinToDevanagari(passengerEntity.getContactNumber()).concat("( ")
											.concat(passengerEntity.getContactNumber()).concat(" )"));

									// model.setContactNumber(passengerEntity.getContactNumber());
									model.setContactNumber(contactNo);
									model.setId(passengerEntity.getId());

									if (cabDriverVehicleMapping.getTripWay()
											.equalsIgnoreCase(IHRMSConstants.CAB_REQUEST_JOURNEY_WAY_RETURN)) {
										// model.setPickupTime(passengerEntity.getPickupTime());

										// model.setPickupAt(passengerEntity.getDropLocation());
										// model.setPickupTime(passengerEntity.getReturnTime());
										// model.setDropLocation(passengerEntity.getPickupAt());

										String pickupLocation = "";
										String dropLocation = "";
										String pickupTime = "";
										pickupLocation = (marathiHelper
												.latinToDevanagari(passengerEntity.getDropLocation())).concat("( ")
												.concat(passengerEntity.getDropLocation()).concat(" )");
										dropLocation = (marathiHelper.latinToDevanagari(passengerEntity.getPickupAt())
												.concat("( ").concat(passengerEntity.getPickupAt()).concat(" )"));
										// pickupTime=(marathiHelper.latinToDevanagari(passengerEntity.getReturnTime()).concat("(
										// ").concat(passengerEntity.getReturnTime()).concat(" )"));
										if (!HRMSHelper.isNullOrEmpty(passengerEntity.getReturnDriverPickupTime()))
											pickupTime = (marathiHelper
													.latinToDevanagari(passengerEntity.getReturnDriverPickupTime())
													.concat("( ").concat(passengerEntity.getReturnDriverPickupTime())
													.concat(" )"));
										else
											pickupTime = (marathiHelper
													.latinToDevanagari(passengerEntity.getReturnTime()).concat("( ")
													.concat(passengerEntity.getReturnTime()).concat(" )"));

										model.setPickupAt(pickupLocation);
										model.setPickupTime(pickupTime);
										model.setDropLocation(dropLocation);

									} else {

										// model.setPickupAt(passengerEntity.getPickupAt());
										// model.setPickupTime(passengerEntity.getPickupTime());
										// model.setDropLocation(passengerEntity.getDropLocation());

										String pickupLocation = "";
										String dropLocation = "";
										String pickupTime = "";
										pickupLocation = (marathiHelper
												.latinToDevanagari(passengerEntity.getPickupAt())).concat("( ")
												.concat(passengerEntity.getPickupAt()).concat(" )");
										dropLocation = (marathiHelper
												.latinToDevanagari(passengerEntity.getDropLocation()).concat("( ")
												.concat(passengerEntity.getDropLocation()).concat(" )"));
										// pickupTime=(marathiHelper.latinToDevanagari(passengerEntity.getPickupTime()).concat("(
										// ").concat(passengerEntity.getPickupTime()).concat(" )"));

										if (!HRMSHelper.isNullOrEmpty(passengerEntity.getDriverPickupTime()))
											pickupTime = (marathiHelper
													.latinToDevanagari(passengerEntity.getDriverPickupTime())
													.concat("( ").concat(passengerEntity.getDriverPickupTime())
													.concat(" )"));
										else
											pickupTime = (marathiHelper
													.latinToDevanagari(passengerEntity.getPickupTime()).concat("( ")
													.concat(passengerEntity.getPickupTime()).concat(" )"));

										model.setPickupAt(pickupLocation);
										model.setPickupTime(pickupTime);
										model.setDropLocation(dropLocation);

									}

									model.setPickupDate(HRMSDateUtil.format(passengerEntity.getPickupDate(),
											IHRMSConstants.FRONT_END_DATE_FORMAT));

									model.setReturnDate(HRMSDateUtil.format(passengerEntity.getReturnDate(),
											IHRMSConstants.FRONT_END_DATE_FORMAT));
									model.setReturnTime(passengerEntity.getReturnTime());
									// model.setDropLocation(passengerEntity.getDropLocation());
									model.setChargeableToClient(passengerEntity.isChargeableToClient());
									model.setRecurring(passengerEntity.isRecurring());
									model.setTripType(cabDriverVehicleMapping.getTripWay());
									// model.setRecurring(true);
									model.setTravelRequestId(
											passengerEntity.getCabRequest().getTravelRequest().getId());
									list.add(model);
								}
							}
						}
					}
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.NO_ASSIGNMENT_FOUND_MSG);
			}

			if (!HRMSHelper.isNullOrEmpty(list)) {
				HRMSListResponseObject response = new HRMSListResponseObject();
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				response.setListResponse(list);
				return HRMSHelper.createJsonString(response);
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.NO_ASSIGNMENT_FOUND_MSG);
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

}
