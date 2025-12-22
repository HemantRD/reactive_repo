package com.vinsys.hrms.services.attendance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceProcessedDataDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceRawDataDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.attendance.AttendanceStatusDetails;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedDataKey;
import com.vinsys.hrms.entity.attendance.AttendanceRawData;
import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSAttendanceHelper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/attendance")

public class ProcessAttendanceService {

	private Map<Long, Employee> empAcnMap = new HashMap<Long, Employee>();

	@Autowired
	IHRMSAttendanceRawDataDAO attendanceRawDataDAO;
	@Autowired
	IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO;
	@Autowired
	IHRMSEmployeeAcnDAO employeeAcnDAO;
	@Autowired
	HRMSAttendanceHelper attendanceHelper;

	@RequestMapping(method = RequestMethod.GET, value = "/processExcel/{orgId}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String processAttendanceRawData(@PathVariable("orgId") long orgId) {

		List<AttendanceRawData> attendanceRawDataList = new ArrayList<>();
		List<Object> voAttendanceProcessedDataList = new ArrayList<>();
		List<AttendanceProcessedData> attendanceProcessedDataList = new ArrayList<>();
		List<Object> colHeadersMapList = getHeaderRows();
		List<EmployeeACN> employeeACNList = new ArrayList<>();

		employeeACNList = employeeAcnDAO.findAll();
		for (EmployeeACN employeeACN : employeeACNList) {
			empAcnMap.put(employeeACN.getEmpACN(), employeeACN.getEmployee());
		}
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			attendanceRawDataList = attendanceRawDataDAO.findByorgId(orgId);

			for (AttendanceRawData attendanceRawData : attendanceRawDataList) {

				AttendanceProcessedDataKey compositePrimaryKey = new AttendanceProcessedDataKey();
				compositePrimaryKey.setEmployeeACN(attendanceRawData.getEmployeeACN());
				compositePrimaryKey.setAttendanceDate(attendanceRawData.getAttendanceDate());
//				compositePrimaryKey.setOrgId(orgId);

				try {
					AttendanceProcessedData attendanceProcessedData = attendanceProcessedDataDAO
							.findBycompositePrimaryKey(compositePrimaryKey);
					if (attendanceProcessedData == null) {
						attendanceProcessedData = new AttendanceProcessedData();

						attendanceProcessedData.setCompositePrimaryKey(compositePrimaryKey);
						attendanceProcessedData.setStartTime(attendanceRawData.getStartTime());
						attendanceProcessedData.setEndTime(attendanceRawData.getEndTime());
						attendanceProcessedData.setManHours(attendanceRawData.getManHours());

						attendanceProcessedData.setCreatedBy("System Upload");
						attendanceProcessedData.setCreatedDate(new Date());
						attendanceProcessedData.setIsActive("Y");

						attendanceProcessedData.setOverTime(
								attendanceProcessedData.getManHours() > 9 ? attendanceProcessedData.getManHours() - 9
										: 0);

						Employee empObj = employeeAcnDAO.findByACNEmpObj(attendanceRawData.getEmployeeACN(), orgId);

						if (!HRMSHelper.isNullOrEmpty(empObj)) {
							attendanceProcessedData.getCompositePrimaryKey().setEmpId(empObj.getId());
							attendanceProcessedData.setEmpName(
									empObj.getCandidate().getFirstName() + " " + empObj.getCandidate().getLastName());

							attendanceProcessedData.setBranchId(
									empObj.getCandidate().getCandidateProfessionalDetail().getBranch().getId());
							attendanceProcessedData.setDivisionId(
									empObj.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
							attendanceProcessedData.setDepartmentId(
									empObj.getCandidate().getCandidateProfessionalDetail().getDepartment().getId());
							attendanceProcessedData.setDesignationId(
									empObj.getCandidate().getCandidateProfessionalDetail().getDesignation().getId());

							attendanceProcessedData.setBranchName(
									empObj.getCandidate().getCandidateProfessionalDetail().getBranch().getBranchName());
							attendanceProcessedData.setDivisionName(empObj.getCandidate()
									.getCandidateProfessionalDetail().getDivision().getDivisionName());
							attendanceProcessedData.setDepartmentName(empObj.getCandidate()
									.getCandidateProfessionalDetail().getDepartment().getDepartmentName());
							attendanceProcessedData.setDesignationName(empObj.getCandidate()
									.getCandidateProfessionalDetail().getDesignation().getDesignationName());

							attendanceProcessedData.setRemark("System Auto Processed");
						} else
							attendanceProcessedData.setRemark("Card Not Assigned");

						AttendanceStatusDetails statusDetails = new AttendanceStatusDetails();
						statusDetails = attendanceHelper.getAttendanceStatus(attendanceProcessedData, true);

						attendanceProcessedData.setStatus(statusDetails.getStatus());
						attendanceProcessedData.setLeaveType(statusDetails.getLeaveType());
						attendanceProcessedData.setLop(statusDetails.getLopCount());

						if (attendanceProcessedData.getStatus().equals("H")
								|| attendanceProcessedData.getStatus().equals("WO"))
							attendanceProcessedData.setOverTime(attendanceProcessedData.getManHours());

						if (attendanceRawData.getUploadStatus().equals("Success")) {
							attendanceProcessedDataList.add(attendanceProcessedData);
							attendanceProcessedDataDAO.save(attendanceProcessedData);
						}

					} else {
						attendanceProcessedDataList.add(attendanceProcessedData);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				attendanceRawDataDAO.delete(attendanceRawData);
			}

			if (!HRMSHelper.isNullOrEmpty(attendanceProcessedDataList)) {

				voAttendanceProcessedDataList = HRMSResponseTranslator
						.transalteToAttendanceProcessListVO(attendanceProcessedDataList, voAttendanceProcessedDataList);
				hrmsListResponseObject.setListResponse((List<Object>) voAttendanceProcessedDataList);
				hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
				hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
				hrmsListResponseObject.setColHeaders(colHeadersMapList);
				return HRMSHelper.createJsonString(hrmsListResponseObject);

			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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

	private List<Object> getHeaderRows() {
		Map<String, Object> colHeadersMap = new HashMap<String, Object>();
		List<Object> colHeadersMapList = new ArrayList<>();

		colHeadersMap.put("property", "empId");
		colHeadersMap.put("header", "Employee Id");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "employeeACN");
		colHeadersMap.put("header", "Employee ACN");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "empName");
		colHeadersMap.put("header", "Employee Name");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "department");
		colHeadersMap.put("header", "Department");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "designation");
		colHeadersMap.put("header", "Designation");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "attendanceDate");
		colHeadersMap.put("header", "Date");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "status");
		colHeadersMap.put("header", "Status");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "startTime");
		colHeadersMap.put("header", "Start Time");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "endTime");
		colHeadersMap.put("header", "End Time");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);
		colHeadersMap = new HashMap<String, Object>();

		colHeadersMap.put("property", "manHours");
		colHeadersMap.put("header", "Man Hours");
		colHeadersMap.put("sortable", true);
		colHeadersMap.put("resizable", true);
		colHeadersMapList.add(colHeadersMap);

		return colHeadersMapList;
	}
}
