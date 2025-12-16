package com.vinsys.hrms.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.constants.ELeaveTypeCode;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dashboard.vo.AttendanceProcessedDataVO;
import com.vinsys.hrms.dashboard.vo.HolidayVO;
import com.vinsys.hrms.dashboard.vo.WeekOffVO;
import com.vinsys.hrms.employee.vo.AttendanceResponseVO;
import com.vinsys.hrms.employee.vo.AttendanceSwipeResponseVO;
import com.vinsys.hrms.entity.EmployeeLeaveApplied;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.entity.attendance.AttendanceCSVData;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;

@Service
public class AttendanceDetailsTransformUtils {

	@Autowired
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;

	public List<AttendanceProcessedDataVO> transalteToAttendanceProcessListVO(
			List<AttendanceProcessedData> attendanceProcessedDataList, List<OrganizationHoliday> holiday) {
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

			int i = isDateSame(holiday, attendanceProcessedData.getCompositePrimaryKey().getAttendanceDate());

//			GetLeaveTypeSessions leaveTypeAndSessions =   isLeaveApplied(attendanceProcessedData.getEmpId(),
//					attendanceProcessedData.getCompositePrimaryKey().getAttendanceDate());

			if (i != -1 && attendanceProcessedData.getStatus().equalsIgnoreCase(IHRMSConstants.Holiday)) {
				voAttendanceProcessedData
						.setStatus(attendanceProcessedData.getStatus() + "(" + holiday.get(i).getHolidayName() + ")");
			} else if (!attendanceProcessedData.getStatus().equalsIgnoreCase(IHRMSConstants.Holiday)
					&& !HRMSHelper.isNullOrEmpty(attendanceProcessedData.getLeaveType())) {
				if (!HRMSHelper.isNullOrEmpty(getLeaveAppliedSession(attendanceProcessedData))) {
					voAttendanceProcessedData.setStatus(getLeaveAppliedSession(attendanceProcessedData));
				} else {
					voAttendanceProcessedData.setStatus(
							attendanceProcessedData.getLeaveType());

				}

			} else {
				voAttendanceProcessedData.setStatus(attendanceProcessedData.getStatus());
			}

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
	


	private String getLeaveAppliedSession(AttendanceProcessedData attendanceProcessedData) {
		StringBuilder resultBuilder = new StringBuilder();
		Long empId = attendanceProcessedData.getCompositePrimaryKey().getEmpId();
		Date startDate = attendanceProcessedData.getCompositePrimaryKey().getAttendanceDate();
		List<EmployeeLeaveApplied> appliedLeave = employeeLeaveAppliedDAO
				.findAppliedLeaveByEmployeeAndFromDateAndLeaveStatus(empId, startDate,
						IHRMSConstants.LeaveStatus_APPROVED);

		for (EmployeeLeaveApplied leaveApplied : appliedLeave) {
			if (!HRMSHelper.isNullOrEmpty(appliedLeave)) {
				Float noOfDays = leaveApplied.getNoOfDays();
				if (!HRMSHelper.isNullOrEmpty(noOfDays) && noOfDays <= 1) {

					int sessionCount = 0;
					String fromSession = leaveApplied.getFromSession();
					String toSession = leaveApplied.getToSession();
					String leaveType = leaveApplied.getMasterLeaveType().getLeaveTypeDescription();

					if (!HRMSHelper.isNullOrEmpty(fromSession) && !HRMSHelper.isNullOrEmpty(toSession)) {
						sessionCount = calculateSessionDifference(fromSession, toSession);
						if (sessionCount > 0) {
							resultBuilder.append(leaveType).append("(").append(sessionCount).append(" session) ");
						}
					}

				} else if (!HRMSHelper.isNullOrEmpty(noOfDays) && noOfDays > 1) {
					String fromSession = leaveApplied.getFromSession();

					if (!HRMSHelper.isNullOrEmpty(fromSession)) {
						if (!HRMSHelper.isNullOrEmpty(leaveApplied.getMasterLeaveType())
								&& !HRMSHelper.isNullOrEmpty(leaveApplied.getMasterLeaveType().getLeaveTypeCode())) {
							if (leaveApplied.getMasterLeaveType().getLeaveTypeCode()
									.equals(ELeaveTypeCode.COMP.name())) {
								int sessionNumber = Integer
										.parseInt(fromSession.substring(fromSession.lastIndexOf(" ") + 1));
								if (sessionNumber == 2) {
									
									resultBuilder.append(leaveApplied.getMasterLeaveType().getLeaveTypeDescription())
											.append("(").append("1").append(" session) ");
								} else {
									resultBuilder.append(leaveApplied.getMasterLeaveType().getLeaveTypeDescription())
											.append("(").append("2").append(" session) ");
									
								}
							} else {

								int sessionNumber = Integer
										.parseInt(fromSession.substring(fromSession.lastIndexOf(" ") + 1));

								if (sessionNumber - 1 > 0) {
									resultBuilder.append(leaveApplied.getMasterLeaveType().getLeaveTypeDescription())
											.append("(").append(sessionNumber - 1).append(" session) ");

								}
							}
						}
					}
				}
			}
		}
		return resultBuilder.toString();
	}

	private int calculateSessionDifference(String fromSession, String toSession) {
		int from = Integer.parseInt(fromSession.substring(fromSession.lastIndexOf(" ") + 1));
		int to = Integer.parseInt(toSession.substring(toSession.lastIndexOf(" ") + 1));
		return to - from + 1;
	}

	private String getDayOfWeek(Date date) {
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
		return simpleDateformat.format(date);
	}

	public List<HolidayVO> transalteToHolidayListVO(List<OrganizationHoliday> holidayEntityList) {
		List<HolidayVO> voHolidayList = new ArrayList<>();
		for (OrganizationHoliday organizationHolidayEntity : holidayEntityList) {
			HolidayVO voHoliday = new HolidayVO();
			voHoliday.setDay(organizationHolidayEntity.getDay());
			SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
			voHoliday.setHolidayDate(df2.format(organizationHolidayEntity.getHolidayDate()));
			voHoliday.setHolidayName(organizationHolidayEntity.getHolidayName());
			voHoliday.setHolidayYear(organizationHolidayEntity.getHolidayYear());
			voHoliday.setId(organizationHolidayEntity.getId());
			voHoliday.setRestricted(organizationHolidayEntity.getRestricted());
			voHolidayList.add(voHoliday);
		}
		return voHolidayList;
	}

	public List<WeekOffVO> transalteToWeekOffVO(List<OrganizationWeekoff> weekOffEntityList) {
		List<WeekOffVO> WeekOffVO = new ArrayList<>();
		for (OrganizationWeekoff organizationHolidayEntity : weekOffEntityList) {
			WeekOffVO weekOffVo = new WeekOffVO();
			weekOffVo.setWeekNumber(organizationHolidayEntity.getWeekNumber());
			weekOffVo.setWeekOffDays(organizationHolidayEntity.getWeekoffDays());

			WeekOffVO.add(weekOffVo);
		}
		return WeekOffVO;
	}

	public AttendanceResponseVO transalteToAttendanceVO(List<AttendanceProcessedData> attendanceProcessedDataList,
			List<OrganizationHoliday> holidayEntityList, List<OrganizationWeekoff> weekOffEntityList,
			List<OrganizationHoliday> holiday) {

		List<AttendanceProcessedDataVO> voAttendanceProcessedDataList = transalteToAttendanceProcessListVO(
				attendanceProcessedDataList, holiday);
		List<HolidayVO> voHolidayList = transalteToHolidayListVO(holidayEntityList);
		List<WeekOffVO> weekOffVOList = transalteToWeekOffVO(weekOffEntityList);
		AttendanceResponseVO attendanceVO = new AttendanceResponseVO();

		attendanceVO.setAttendanceDataList(voAttendanceProcessedDataList);
		attendanceVO.setHolidayList(voHolidayList);
		attendanceVO.setWeekOffList(weekOffVOList);

		return attendanceVO;

	}

	public List<AttendanceSwipeResponseVO> transalteToAttendanceSwapDataListVO(
			List<AttendanceCSVData> attendanceCSVDataList) {
		List<AttendanceSwipeResponseVO> attendanceSwipeResponseVO = new ArrayList<>();
		for (AttendanceCSVData attendanceCSVData : attendanceCSVDataList) {
			AttendanceSwipeResponseVO voAttendanceCSVData = new AttendanceSwipeResponseVO();

			voAttendanceCSVData.setCardNo(!HRMSHelper.isNullOrEmpty(attendanceCSVData.getCardNo()) ? attendanceCSVData.getCardNo() : null);
			// voAttendanceCSVData.setSwapDate(attendanceCSVData.getSwapDate());
			voAttendanceCSVData.setSwapTime(attendanceCSVData.getSwapTime() != null
					? new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa").format(attendanceCSVData.getSwapTime())
					: null);
			voAttendanceCSVData.setSwapDate(attendanceCSVData.getSwapDate() != null
					? new SimpleDateFormat("dd-MM-yyyy").format(attendanceCSVData.getSwapDate())
					: null);
			voAttendanceCSVData.setEmpId(attendanceCSVData.getEmployeeId());
			attendanceSwipeResponseVO.add(voAttendanceCSVData);
		}
		return attendanceSwipeResponseVO;
	}

	public int isDateSame(List<OrganizationHoliday> dateList, Date targetDate) {
		int i = 0;
		for (OrganizationHoliday date : dateList) {
			if (date.getHolidayDate().equals(targetDate)) {
				return i;
			}
			i++;
		}
		return -1;
	}

}
