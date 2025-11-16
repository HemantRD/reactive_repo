package com.vinsys.hrms.employee.vo;

import java.util.List;

import com.vinsys.hrms.dashboard.vo.AttendanceProcessedDataVO;
import com.vinsys.hrms.dashboard.vo.HolidayVO;
import com.vinsys.hrms.dashboard.vo.WeekOffVO;

public class AttendanceResponseVO {

	
private List<AttendanceProcessedDataVO> attendanceDataList;
private List<HolidayVO> holidayList;
private List<WeekOffVO> weekOffList;
private List<String> weekDays; 




public List<AttendanceProcessedDataVO> getAttendanceDataList() {
	return attendanceDataList;
}
public void setAttendanceDataList(List<AttendanceProcessedDataVO> attendanceDataList) {
	this.attendanceDataList = attendanceDataList;
}
public List<HolidayVO> getHolidayList() {
	return holidayList;
}
public void setHolidayList(List<HolidayVO> holidayList) {
	this.holidayList = holidayList;
}
public List<WeekOffVO> getWeekOffList() {
	return weekOffList;
}
public void setWeekOffList(List<WeekOffVO> weekOffList) {
	this.weekOffList = weekOffList;
}
public List<String> getWeekDays() {
	return weekDays;
}
public void setWeekDays(List<String> weekDays) {
	this.weekDays = weekDays;
}




}
