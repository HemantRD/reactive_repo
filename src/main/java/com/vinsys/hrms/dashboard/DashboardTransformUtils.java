package com.vinsys.hrms.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vinsys.hrms.dashboard.vo.AttendanceGraphVO;
import com.vinsys.hrms.dashboard.vo.AttendanceProcessedDataVO;
import com.vinsys.hrms.dashboard.vo.EmployeeLeaveSumarryDetailsVO;
import com.vinsys.hrms.dashboard.vo.HolidayVO;
import com.vinsys.hrms.dashboard.vo.MasterLeaveTypeVO;
import com.vinsys.hrms.dashboard.vo.WeekOffVO;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.util.HRMSHelper;

public class DashboardTransformUtils {

	public static List<AttendanceProcessedDataVO> transalteToAttendanceProcessListVO(
			List<AttendanceProcessedData> attendanceProcessedDataList) {
		List<AttendanceProcessedDataVO> voAttendanceProcessedDataList = new ArrayList<>();
		for (AttendanceProcessedData attendanceProcessedData : attendanceProcessedDataList) {
			AttendanceProcessedDataVO voAttendanceProcessedData = new AttendanceProcessedDataVO();

			voAttendanceProcessedData.setEmpId(attendanceProcessedData.getCompositePrimaryKey().getEmpId());
			voAttendanceProcessedData.setEmployeeACN(attendanceProcessedData.getCompositePrimaryKey().getEmployeeACN());
			voAttendanceProcessedData.setEmpName(attendanceProcessedData.getEmpName());
			voAttendanceProcessedData.setDepartment(attendanceProcessedData.getDepartmentName());
			voAttendanceProcessedData.setDesignation(attendanceProcessedData.getDesignationName());
			voAttendanceProcessedData
					.setAttendanceDate(
							attendanceProcessedData.getCompositePrimaryKey().getAttendanceDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(
											attendanceProcessedData.getCompositePrimaryKey().getAttendanceDate())
									: null);
			voAttendanceProcessedData
					.setAttendanceDay(attendanceProcessedData.getCompositePrimaryKey().getAttendanceDate() != null
							? getDayOfWeek(attendanceProcessedData.getCompositePrimaryKey().getAttendanceDate())
							: null);
			voAttendanceProcessedData.setStatus(attendanceProcessedData.getStatus());
			voAttendanceProcessedData.setStartTime(attendanceProcessedData.getStartTime() != null
					? new SimpleDateFormat("hh:mma").format(attendanceProcessedData.getStartTime())
					: null);
			voAttendanceProcessedData.setEndTime(attendanceProcessedData.getEndTime() != null
					? new SimpleDateFormat("hh:mma").format(attendanceProcessedData.getEndTime())
					: null);
			voAttendanceProcessedData.setManHours(attendanceProcessedData.getManHours());
			voAttendanceProcessedData.setUploadStatus(attendanceProcessedData.getUploadStatus());
			voAttendanceProcessedData.setLeaveType(attendanceProcessedData.getLeaveType());

			voAttendanceProcessedDataList.add(voAttendanceProcessedData);
		}
		return voAttendanceProcessedDataList;
	}

	private static String getDayOfWeek(Date date) {
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
		return simpleDateformat.format(date);
	}

	public static List<HolidayVO> transalteToHolidayListVO(List<OrganizationHoliday> holidayEntityList) {
		List<HolidayVO> voHolidayList = new ArrayList<>();
		for (OrganizationHoliday organizationHolidayEntity : holidayEntityList) {
			HolidayVO voHoliday = new HolidayVO();
			voHoliday.setDay(organizationHolidayEntity.getDay());
			SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
			voHoliday.setHolidayDate(df2.format(organizationHolidayEntity.getHolidayDate()));
			voHoliday.setHolidayName(organizationHolidayEntity.getHolidayName());
			voHoliday.setHolidayYear(organizationHolidayEntity.getHolidayYear());
			voHoliday.setId(organizationHolidayEntity.getId());
			voHoliday.setRestricted(organizationHolidayEntity.getRestricted());
			voHolidayList.add(voHoliday);
		}
		return voHolidayList;
	}

	public static List<WeekOffVO> transalteToWeekOffVO(List<OrganizationWeekoff> weekOffEntityList) {
		List<WeekOffVO> WeekOffVO = new ArrayList<>();
		for (OrganizationWeekoff organizationHolidayEntity : weekOffEntityList) {
			WeekOffVO weekOffVo = new WeekOffVO();
			weekOffVo.setWeekNumber(organizationHolidayEntity.getWeekNumber());
			weekOffVo.setWeekOffDays(organizationHolidayEntity.getWeekoffDays());

			WeekOffVO.add(weekOffVo);
		}
		return WeekOffVO;
	}

	public static AttendanceGraphVO transalteToAttendanceGraphVO(
			List<AttendanceProcessedData> attendanceProcessedDataList, List<OrganizationHoliday> holidayEntityList,
			List<OrganizationWeekoff> weekOffEntityList) {

		List<AttendanceProcessedDataVO> voAttendanceProcessedDataList = transalteToAttendanceProcessListVO(
				attendanceProcessedDataList);
		List<HolidayVO> voHolidayList = transalteToHolidayListVO(holidayEntityList);
		List<WeekOffVO> weekOffVOList = transalteToWeekOffVO(weekOffEntityList);
		AttendanceGraphVO attendanceGraphVO = new AttendanceGraphVO();

		attendanceGraphVO.setAttendanceDataList(voAttendanceProcessedDataList);
		attendanceGraphVO.setHolidayList(voHolidayList);
		attendanceGraphVO.setWeekOffList(weekOffVOList);

		return attendanceGraphVO;

	}

	public static EmployeeLeaveSumarryDetailsVO convertToEmployeeLeaveSumarryVo(
			EmployeeLeaveDetail employeeLeaveDetailsEntity) {
		EmployeeLeaveSumarryDetailsVO model = null;
		if (employeeLeaveDetailsEntity != null) {

			if ((!HRMSHelper.isNullOrEmpty(employeeLeaveDetailsEntity.getNumberOfDaysAvailed())
					&& employeeLeaveDetailsEntity.getNumberOfDaysAvailed() > 0)
					|| (!HRMSHelper.isNullOrEmpty(employeeLeaveDetailsEntity.getLeaveAvailable())
							&& employeeLeaveDetailsEntity.getLeaveAvailable() > 0)) {

				if ((employeeLeaveDetailsEntity.getEmployee().getCandidate().getGender().equalsIgnoreCase("Female")
						&& !employeeLeaveDetailsEntity.getMasterLeaveType().getLeaveTypeCode().equalsIgnoreCase("PATR"))
						|| (employeeLeaveDetailsEntity.getEmployee().getCandidate().getGender().equalsIgnoreCase("Male")
								&& !employeeLeaveDetailsEntity.getMasterLeaveType().getLeaveTypeCode()
										.equalsIgnoreCase("MATR"))
						|| (employeeLeaveDetailsEntity.getEmployee().getCandidate().getGender()
								.equalsIgnoreCase("Other"))) {

					model = new EmployeeLeaveSumarryDetailsVO();
					model.setMasterLeaveType(
							convertToMasterLeaveTypeModel(employeeLeaveDetailsEntity.getMasterLeaveType()));
					model.setClosingBalance(HRMSHelper.isNullOrEmpty(employeeLeaveDetailsEntity.getClosingBalance()) ? 0
							: employeeLeaveDetailsEntity.getClosingBalance());
					model.setPyLeaveEncashment(
							HRMSHelper.isNullOrEmpty(employeeLeaveDetailsEntity.getPyLeaveEncashment()) ? 0
									: employeeLeaveDetailsEntity.getPyLeaveEncashment());
					model.setLeaveCarriedOver(
							HRMSHelper.isNullOrEmpty(employeeLeaveDetailsEntity.getLeaveCarriedOver()) ? 0
									: employeeLeaveDetailsEntity.getLeaveCarriedOver());
					model.setLeaveEarned(HRMSHelper.isNullOrEmpty(employeeLeaveDetailsEntity.getLeaveEarned()) ? 0
							: employeeLeaveDetailsEntity.getLeaveEarned());
					model.setFyLeaveEncashment(
							HRMSHelper.isNullOrEmpty(employeeLeaveDetailsEntity.getFyLeaveEncashment()) ? 0
									: employeeLeaveDetailsEntity.getFyLeaveEncashment());
					model.setTotalEligibility(
							HRMSHelper.isNullOrEmpty(employeeLeaveDetailsEntity.getTotalEligibility()) ? 0
									: employeeLeaveDetailsEntity.getTotalEligibility());
					model.setNumberOfDaysAvailed(
							HRMSHelper.isNullOrEmpty(employeeLeaveDetailsEntity.getNumberOfDaysAvailed()) ? 0
									: employeeLeaveDetailsEntity.getNumberOfDaysAvailed());
					model.setLeaveAvailable(HRMSHelper.isNullOrEmpty(employeeLeaveDetailsEntity.getLeaveAvailable()) ? 0
							: employeeLeaveDetailsEntity.getLeaveAvailable());
				}
			}
		}
		return model;
	}

	public static MasterLeaveTypeVO convertToMasterLeaveTypeModel(MasterLeaveType entity) {

		MasterLeaveTypeVO model = null;
		if (entity != null) {
			model = new MasterLeaveTypeVO();
			model.setId(entity.getId());
			model.setLeaveTypeDescription(entity.getLeaveTypeDescription());
			model.setLeaveTypeName(entity.getLeaveTypeName());
		}
		return model;
	}

}
