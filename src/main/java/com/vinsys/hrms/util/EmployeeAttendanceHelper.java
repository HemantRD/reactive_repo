package com.vinsys.hrms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.employee.vo.AttendanceRequestVO;
import com.vinsys.hrms.employee.vo.TeamAttendanceVO;
import com.vinsys.hrms.exception.HRMSException;

@Service
public class EmployeeAttendanceHelper {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void employeeAttendanceReportInputValidation(TeamAttendanceVO request)
			throws HRMSException, ParseException {

		if (HRMSHelper.isNullOrEmpty(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);

		Date fromDate = dateFormat.parse(request.getFromDate());
		Date toDate = dateFormat.parse(request.getToDate());

		if (fromDate.after(toDate)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1502));
		}
	}

	public void employeeGetSwipesInputValidation(AttendanceRequestVO request) throws HRMSException, ParseException {

		if (!HRMSHelper.validateDateFormate(request.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);

		Date fromDate = dateFormat.parse(request.getFromDate());
		Date toDate = dateFormat.parse(request.getToDate());

		if (fromDate.after(toDate)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1502));
		}
	}

	public void attendanceGraphInputValidation(String fromDate, String toDate) throws HRMSException {

		if (!HRMSHelper.validateDateFormate(fromDate)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (!HRMSHelper.validateDateFormate(toDate)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}
	}
	
	public void teamGetSwipesInputValidation(TeamAttendanceVO requestVO) throws HRMSException, ParseException {

		if (!HRMSHelper.validateDateFormate(requestVO.getFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Date");
		}

		if (!HRMSHelper.validateDateFormate(requestVO.getToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Date");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);

		Date fromDate = dateFormat.parse(requestVO.getFromDate());
		Date toDate = dateFormat.parse(requestVO.getToDate());

		if (fromDate.after(toDate)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1502));
		}
	}

}
