package com.vinsys.hrms.services.attendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendaceEmployeeACNDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceCsvDataDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.attendance.VOEmployeeAttendanceReport;
import com.vinsys.hrms.datamodel.attendance.VOMobileSwapDetailReq;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.attendance.AttendanceCSVData;
import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/attendance")


public class GetSwapDetails {

	private static final Logger logger = LoggerFactory.getLogger(GetSwapDetails.class);
	private Map<Long, Employee> empAcnMap = new HashMap<Long, Employee>();

	@Autowired
	IHRMSAttendanceCsvDataDAO attendanceCsvDataDAO;
	@Autowired
	IHRMSEmployeeAcnDAO employeeAcnDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSAttendaceEmployeeACNDAO employeeACNDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, value = "/swapdetails", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String empAttendanceReport(@RequestBody VOEmployeeAttendanceReport employeeAttendanceReportParams,
			HttpServletResponse res) {
		logger.info("GETTING SWAP DETAILS...");
		List<Object> colHeadersMapList = getHeaderRows();
		try {
			List<AttendanceCSVData> attendanceCSVData = new ArrayList<>();
			try {
				
				
				if (!HRMSHelper.isLongZero(employeeAttendanceReportParams.getAccessCardNo())) {
					
					if(HRMSHelper.isNullOrEmpty(employeeAttendanceReportParams.getFromDate())&& HRMSHelper.isNullOrEmpty(employeeAttendanceReportParams.getToDate())) {
						attendanceCSVData = attendanceCsvDataDAO
								.findBySwapDateBetweenAndCardNoAndOrgIdAndIsActiveOrderBySwapTimeDesc(new Date(),
										new Date(),
										employeeAttendanceReportParams.getAccessCardNo(),
										employeeAttendanceReportParams.getOrgId(), "Y");
					}else {
						attendanceCSVData = attendanceCsvDataDAO
								.findBySwapDateBetweenAndCardNoAndOrgIdAndIsActiveOrderBySwapTimeDesc(
										new SimpleDateFormat("yyyy-MM-dd")
												.parse(employeeAttendanceReportParams.getFromDate()),
										(new SimpleDateFormat("yyyy-MM-dd")
												.parse(employeeAttendanceReportParams.getToDate())),
										employeeAttendanceReportParams.getAccessCardNo(),
										employeeAttendanceReportParams.getOrgId(), "Y");
					}
					
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
			List<Object> voAttendanceSwapDataList = new ArrayList<>();
			if (!HRMSHelper.isNullOrEmpty(attendanceCSVData)) {
				voAttendanceSwapDataList = HRMSResponseTranslator.transalteToAttendanceSwapDataListVO(attendanceCSVData,
						voAttendanceSwapDataList);
				hrmsListResponseObject.setListResponse((List<Object>) voAttendanceSwapDataList);
				hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
				hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
				hrmsListResponseObject.setColHeaders(colHeadersMapList);
				return HRMSHelper.createJsonString(hrmsListResponseObject);
			} else {

				return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
						IHRMSConstants.DataNotFoundCode);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private List<Object> getHeaderRows() {

		Map<String, Object> colHeadersMap = new HashMap<String, Object>();
		List<Object> colHeadersMapList = new ArrayList<>();

		colHeadersMap.put("property", "srNo");
		colHeadersMap.put("header", "Sr. No.");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "accessCardNo");
		colHeadersMap.put("header", "Access Card No");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "swapDateTime");
		colHeadersMap.put("header", "Swap Date Time");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		return colHeadersMapList;
	}

	/**
	 * 
	 * @param voMobileSwapDetailReq
	 * @return string response
	 * @author SSW
	 */
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, value = "/postSwapFromMobile", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String postSwapFromMobile(@RequestBody VOMobileSwapDetailReq voMobileSwapDetailReq) {
		logger.info("GetSwapDetails ::: postSwapFromMobile() ::: ");

		try {
			if (!HRMSHelper.isNullOrEmpty(voMobileSwapDetailReq)
					&& !HRMSHelper.isLongZero(voMobileSwapDetailReq.getOrgId())
					&& !HRMSHelper.isNullOrEmpty(voMobileSwapDetailReq.getImei())) {
				// checking if correct imei is provided
				if (!HRMSHelper.isNullOrEmpty(voMobileSwapDetailReq.getEmployee())
						&& !HRMSHelper.isLongZero(voMobileSwapDetailReq.getEmployee().getId())) {
					Employee emp = employeeDAO.findActiveEmployeeById(voMobileSwapDetailReq.getEmployee().getId(),
							IHRMSConstants.isActive);
					// finding imei from database
					if (!HRMSHelper.isNullOrEmpty(emp) && !HRMSHelper.isNullOrEmpty(emp.getCandidate())
							&& !HRMSHelper.isNullOrEmpty(emp.getCandidate())
							&& !HRMSHelper.isNullOrEmpty(emp.getCandidate().getCandidatePersonalDetail()) && !HRMSHelper
									.isNullOrEmpty(emp.getCandidate().getCandidatePersonalDetail().getMappedIMEI())) {
						// comparing imei
						if (!emp.getCandidate().getCandidatePersonalDetail().getMappedIMEI()
								.equalsIgnoreCase(voMobileSwapDetailReq.getImei())) {
							throw new HRMSException(IHRMSConstants.MOBILE_REMOTE_LOCATION_ATTENDANCE_IMEI_NOT_MATCHED_CODE,
									IHRMSConstants.MOBILE_REMOTE_LOCATION_ATTENDANCE_IMEI_NOT_MATCHED_MSG);
						}
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
				} else {
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
				}
				// up to this for imei validation
				long empACNNo = 0;
				// if employee access card number is not available
				if (HRMSHelper.isLongZero(voMobileSwapDetailReq.getEmpACN())) {
					// if access card number not available, then empId is mandatory
					if (!HRMSHelper.isNullOrEmpty(voMobileSwapDetailReq.getEmployee())
							&& !HRMSHelper.isLongZero(voMobileSwapDetailReq.getEmployee().getId())) {
						EmployeeACN empAcn = employeeACNDAO.getEmployeeACNDetailsByEmpId(IHRMSConstants.isActive,
								voMobileSwapDetailReq.getEmployee().getId());
						empACNNo = empAcn.getEmpACN();
					} else {
						// no access card and no emp details provided..
						// any one is must
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
				} else { // emp access card number available
					empACNNo = voMobileSwapDetailReq.getEmpACN();
				}
				AttendanceCSVData attndCSVData = new AttendanceCSVData();
				attndCSVData.setCardNo(empACNNo);
				attndCSVData.setCreatedDate(new Date());
				attndCSVData.setDescription(IHRMSConstants.MOBILE_SUBMITTED_SWAP_MESSAGE);
				attndCSVData.setHashId(getCsvHashCode(empACNNo, voMobileSwapDetailReq.getOrgId()));
				attndCSVData.setIsActive(IHRMSConstants.isActive);
				attndCSVData.setOrgId(voMobileSwapDetailReq.getOrgId());
				attndCSVData.setProcessed(false);
				attndCSVData.setSwapDate(new Date());
				attndCSVData.setSwapTime(new Date());
				attendanceCsvDataDAO.save(attndCSVData);

				HRMSBaseResponse baseResponse = new HRMSBaseResponse();
				baseResponse.setResponseCode(IHRMSConstants.successCode);
				baseResponse.setResponseMessage(IHRMSConstants.MOBILE_ATTENDANCE_MARKED_SUCCESS_MESSAGE);
				return HRMSHelper.createJsonString(baseResponse);
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

	private long getCsvHashCode(long empACNNo, long orgId) {
		return (String.valueOf(empACNNo) + String.valueOf(orgId)
				+ HRMSDateUtil.format(new Date(), IHRMSConstants.FRONT_END_DATE_FORMAT_DDMMYY_HHMMSS)).hashCode();
	}
}
