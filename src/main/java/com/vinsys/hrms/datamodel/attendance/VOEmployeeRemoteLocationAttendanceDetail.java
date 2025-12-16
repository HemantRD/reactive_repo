package com.vinsys.hrms.datamodel.attendance;

import java.util.List;

import com.vinsys.hrms.datamodel.VOAuditBase;
import com.vinsys.hrms.datamodel.VOEmployee;

public class VOEmployeeRemoteLocationAttendanceDetail extends VOAuditBase {

	private long id;

	private VOEmployee employee;

	private String isRemoteLocationAttendanceAllowed;

	private String currentMarkAttendanceStatus;

	private String imei;

	private List<VOGroupLocationDetail> groupLocationDetailList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}

	public String getIsRemoteLocationAttendanceAllowed() {
		return isRemoteLocationAttendanceAllowed;
	}

	public void setIsRemoteLocationAttendanceAllowed(String isRemoteLocationAttendanceAllowed) {
		this.isRemoteLocationAttendanceAllowed = isRemoteLocationAttendanceAllowed;
	}

	public String getCurrentMarkAttendanceStatus() {
		return currentMarkAttendanceStatus;
	}

	public void setCurrentMarkAttendanceStatus(String currentMarkAttendanceStatus) {
		this.currentMarkAttendanceStatus = currentMarkAttendanceStatus;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public List<VOGroupLocationDetail> getGroupLocationDetailList() {
		return groupLocationDetailList;
	}

	public void setGroupLocationDetailList(List<VOGroupLocationDetail> groupLocationDetailList) {
		this.groupLocationDetailList = groupLocationDetailList;
	}

}
