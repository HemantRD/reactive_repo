package com.vinsys.hrms.services.attendance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.attendance.IHRMSAttendanceCsvDataDAO;
import com.vinsys.hrms.datamodel.attendance.VOAttendanceMDBSwapData;
import com.vinsys.hrms.entity.attendance.AttendanceCSVData;
import com.vinsys.hrms.util.HRMSHelper;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/attendance")

public class MDBDataUploaderService {

	@Autowired
	IHRMSAttendanceCsvDataDAO attendanceMDBDataDAO;

	@RequestMapping(value = "/mdbuploader", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String uploadExcel(@RequestBody List<VOAttendanceMDBSwapData> swapDataList) {
		try {
			for (VOAttendanceMDBSwapData voAttendanceMDBSwapData : swapDataList) {
				AttendanceCSVData swapData = new AttendanceCSVData();

				swapData.setCardNo(voAttendanceMDBSwapData.getCardNo());
				swapData.setCreatedBy("");
				swapData.setCreatedDate(new Date());
				swapData.setIsActive("Y");
				swapData.setDescription(voAttendanceMDBSwapData.getDirection());
				swapData.setOrgId(voAttendanceMDBSwapData.getOrgId());
				swapData.setProcessed(false);
				swapData.setRemark("");
				swapData.setSwapDate(
						null != voAttendanceMDBSwapData.getSwipeDateTime() ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.parse(voAttendanceMDBSwapData.getSwipeDateTime().trim()) : null);
				swapData.setSwapTime(
						null != voAttendanceMDBSwapData.getSwipeDateTime() ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.parse(voAttendanceMDBSwapData.getSwipeDateTime().trim()) : null);
				swapData.setHashId(getHashForMDBData(swapData));

				AttendanceCSVData existingSwapData = attendanceMDBDataDAO.findById(swapData.getHashId()).get();

				if (HRMSHelper.isNullOrEmpty(existingSwapData)) {
					attendanceMDBDataDAO.save(swapData);
				}

			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	private long getHashForMDBData(AttendanceCSVData attendanceCsvData) {
		if (!HRMSHelper.isNullOrEmpty(attendanceCsvData)) {
			try {
				StringBuffer strBuff = new StringBuffer();

				strBuff.append(attendanceCsvData.getSwapDate() != null ? attendanceCsvData.getSwapDate() : "");
				strBuff.append(attendanceCsvData.getSwapTime() != null ? attendanceCsvData.getSwapTime() : "");
				strBuff.append(!HRMSHelper.isLongZero(attendanceCsvData.getCardNo())
						? String.valueOf(attendanceCsvData.getCardNo())
						: "");
				strBuff.append(!HRMSHelper.isLongZero(attendanceCsvData.getOrgId())
						? String.valueOf(attendanceCsvData.getOrgId())
						: "");
				return strBuff.toString().hashCode();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return 0;

	}

}