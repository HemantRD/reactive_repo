package com.vinsys.hrms.services.attendance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendaceEmployeeACNDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceCsvDataDAO;
import com.vinsys.hrms.dao.attendance.IHRMSEmployeeRemoteLocationAttendanceDAO;
import com.vinsys.hrms.dao.attendance.IHRMSMapGroupToLocationDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.attendance.VOEmployeeImeiDetailRequest;
import com.vinsys.hrms.datamodel.attendance.VOEmployeeRemoteLocationAttendanceDetail;
import com.vinsys.hrms.datamodel.attendance.VOGroupLocationDetail;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.attendance.AttendanceCSVData;
import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.entity.attendance.EmployeeRemoteLocationAttendanceDetail;
import com.vinsys.hrms.entity.attendance.MapGroupToLocation;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/empRemoteAttendace")

public class EmployeeRemoteLocationAttendanceService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeRemoteLocationAttendanceService.class);

	@Autowired
	IHRMSEmployeeRemoteLocationAttendanceDAO empRemoteLocAttendanceDAO;

	@Autowired
	IHRMSAttendanceCsvDataDAO attendanceCSVDataDAO;

	@Autowired
	IHRMSAttendaceEmployeeACNDAO employeeACNDAO;

	@Autowired
	IHRMSEmployeeDAO employeeDAO;

	@Autowired
	IHRMSMapGroupToLocationDAO mapGroupToLocDAO;

	@Autowired
	IHRMSCandidatePersonalDetailDAO candPersonalDetailDAO;

	@RequestMapping(method = {
			RequestMethod.POST }, value = "/getEmpRemoteLocationAttendanceDetail", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getEmpRemoteLocationAttendanceDetail(
			@RequestBody VOEmployeeRemoteLocationAttendanceDetail voEmployeeRemoteLocationAttendanceDetail) {
		logger.info("EmployeeRemoteLocationAttendanceService ::: getEmpRemoteLocationAttendanceDetail() ::: ");

		try {
			if (!HRMSHelper.isNullOrEmpty(voEmployeeRemoteLocationAttendanceDetail)
					&& !HRMSHelper.isNullOrEmpty(voEmployeeRemoteLocationAttendanceDetail.getEmployee())
					&& !HRMSHelper.isLongZero(voEmployeeRemoteLocationAttendanceDetail.getEmployee().getId())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeRemoteLocationAttendanceDetail.getImei())) {
				// get employee remote loc attendance data
				EmployeeRemoteLocationAttendanceDetail empRemLocAttendaceDet = empRemoteLocAttendanceDAO
						.getEmployeeRemoteLocationDetailByEmpId(IHRMSConstants.isActive,
								voEmployeeRemoteLocationAttendanceDetail.getEmployee().getId());
				if (!HRMSHelper.isNullOrEmpty(empRemLocAttendaceDet)
						&& empRemLocAttendaceDet.getIsRemoteLocationAttendanceAllowed()
								.equals(IHRMSConstants.MOBILE_REMOTE_LOCATION_ATTENDANCE_ALLOWED_Y)) {
					Employee emp = employeeDAO.findActiveEmployeeById(
							voEmployeeRemoteLocationAttendanceDetail.getEmployee().getId(), IHRMSConstants.isActive);
					// finding imei from database
					if (!HRMSHelper.isNullOrEmpty(emp) && !HRMSHelper.isNullOrEmpty(emp.getCandidate())
							&& !HRMSHelper.isNullOrEmpty(emp.getCandidate())
							&& !HRMSHelper.isNullOrEmpty(emp.getCandidate().getCandidatePersonalDetail()) && !HRMSHelper
									.isNullOrEmpty(emp.getCandidate().getCandidatePersonalDetail().getMappedIMEI())) {
						// comparing imei
						if (!emp.getCandidate().getCandidatePersonalDetail().getMappedIMEI()
								.equalsIgnoreCase(voEmployeeRemoteLocationAttendanceDetail.getImei())) {
							throw new HRMSException(
									IHRMSConstants.MOBILE_REMOTE_LOCATION_ATTENDANCE_IMEI_NOT_MATCHED_CODE,
									IHRMSConstants.MOBILE_REMOTE_LOCATION_ATTENDANCE_IMEI_NOT_MATCHED_MSG);
						}
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					// get employee ACn details
					EmployeeACN empAcn = employeeACNDAO.getEmployeeACNDetailsByEmpId(IHRMSConstants.isActive,
							voEmployeeRemoteLocationAttendanceDetail.getEmployee().getId());
					if (!HRMSHelper.isNullOrEmpty(empAcn) && !HRMSHelper.isNullOrEmpty(empRemLocAttendaceDet)
							&& !HRMSHelper.isLongZero(empAcn.getEmpACN())) {
						// get existing mobile punches of today
						List<AttendanceCSVData> attendCSVDataList = attendanceCSVDataDAO
								.getCurrDateEmployeeMobileAttendanceRecords(IHRMSConstants.isActive, empAcn.getEmpACN(),
										IHRMSConstants.MOBILE_SUBMITTED_SWAP_MESSAGE);
						// getting status of IN or OUT of Attendance
						int reminder = attendCSVDataList.size() % 2;
						// current mark attendance status logic
						// if reminder is 0, means even records of mobile attendance are present, which
						// is employee's
						// last mark was for OUT, So current status to show on UI is 'IN'
						//
						// As opposite to above, if reminder is 1, means odd records of mobile
						// attendance are present, which is employee's
						// last mark was for IN, So current status to show on UI is 'OUT'
						String curMarkAttndStatus = "";
						if (reminder == 0) {
							curMarkAttndStatus = IHRMSConstants.MOBILE_CURRENT_MARKED_ATTENDANCE_STATUS_IN;
						} else if (reminder == 1) {
							curMarkAttndStatus = IHRMSConstants.MOBILE_CURRENT_MARKED_ATTENDANCE_STATUS_OUT;
						}

						// getting location details
						List<MapGroupToLocation> listGroupToLocation = mapGroupToLocDAO.getGroupToLocationByGroup(
								IHRMSConstants.isActive, empRemLocAttendaceDet.getMstOrgDivLocationGroup());
						List<VOGroupLocationDetail> groupLocationDetailList = new ArrayList<VOGroupLocationDetail>();
						for (MapGroupToLocation itr1 : listGroupToLocation) {
							if (!HRMSHelper.isNullOrEmpty(itr1)
									&& !HRMSHelper.isNullOrEmpty(itr1.getMstLocationDetail())) {
								VOGroupLocationDetail grpLocDet = new VOGroupLocationDetail();
								grpLocDet.setLatitude(itr1.getMstLocationDetail().getLatitude());
								grpLocDet.setLongitude(itr1.getMstLocationDetail().getLongitude());
								// radius in meter to be converted into Kilometers
								grpLocDet.setRadius((float) itr1.getMstLocationDetail().getRadius() / 1000);
								grpLocDet.setLocationName(itr1.getMstLocationDetail().getLocationName());
								groupLocationDetailList.add(grpLocDet);
							}
						}

						// end getting location details
						VOEmployeeRemoteLocationAttendanceDetail empRemLocAttndRespnse = new VOEmployeeRemoteLocationAttendanceDetail();
						empRemLocAttndRespnse.setCurrentMarkAttendanceStatus(curMarkAttndStatus);
						empRemLocAttndRespnse.setIsRemoteLocationAttendanceAllowed(
								empRemLocAttendaceDet.getIsRemoteLocationAttendanceAllowed());
						empRemLocAttndRespnse.setGroupLocationDetailList(groupLocationDetailList);

						HRMSListResponseObject response = new HRMSListResponseObject();
						List<Object> list = new ArrayList<Object>();
						list.add(empRemLocAttndRespnse);
						response.setListResponse(list);
						response.setResponseMessage(IHRMSConstants.successMessage);
						response.setResponseCode(IHRMSConstants.successCode);
						return HRMSHelper.createJsonString(response);
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
				} else {
					// throw user exception :: not allowed for remote attendance
					throw new HRMSException(IHRMSConstants.MOBILE_REMOTE_LOCATION_ATTENDANCE_NOT_ALLOWED_CODE,
							IHRMSConstants.MOBILE_REMOTE_LOCATION_ATTENDANCE_NOT_ALLOWED_MSG);
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

	@RequestMapping(method = {
			RequestMethod.POST }, value = "/saveEmployeeImei", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String saveEmployeeImei(@RequestBody VOEmployeeImeiDetailRequest voEmployeeImeiDetailRequest) {
		logger.info("EmployeeRemoteLocationAttendanceService ::: saveEmployeeImei() ::: ");

		try {
			if (!HRMSHelper.isNullOrEmpty(voEmployeeImeiDetailRequest)
					&& !HRMSHelper.isNullOrEmpty(voEmployeeImeiDetailRequest.getEmployee())
					&& !HRMSHelper.isLongZero(voEmployeeImeiDetailRequest.getEmployee().getId())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeImeiDetailRequest.getImei())) {
				// find candidate
				Employee emp = employeeDAO.findActiveEmployeeById(voEmployeeImeiDetailRequest.getEmployee().getId(),
						IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(emp) && !HRMSHelper.isNullOrEmpty(emp.getCandidate())
						&& !HRMSHelper.isLongZero(emp.getCandidate().getId())) {
					CandidatePersonalDetail cpd = candPersonalDetailDAO.findBycandidate(emp.getCandidate());
					cpd.setMappedIMEI(voEmployeeImeiDetailRequest.getImei());
					cpd.setUpdatedBy(String.valueOf(emp.getId()));
					cpd.setUpdatedDate(new Date());
					candPersonalDetailDAO.save(cpd);
					HRMSBaseResponse baseResp = new HRMSBaseResponse();
					baseResp.setResponseCode(IHRMSConstants.successCode);
					baseResp.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(baseResp);
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

}
