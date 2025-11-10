package com.vinsys.hrms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.vinsys.hrms.datamodel.VOHolidayResponse;
import com.vinsys.hrms.datamodel.VOMasterLeaveType;
import com.vinsys.hrms.employee.vo.AvailableLeavesVO;
import com.vinsys.hrms.employee.vo.EmployeeGetAllAppliedLeavesVO;
import com.vinsys.hrms.employee.vo.EmployeeLeaveDetailsVO;
import com.vinsys.hrms.employee.vo.EmployeeListVO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCompOffCreditLeaveHistory;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeGrantLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveApplied;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.OrganizationHoliday;


public class LeaveTransformUtil {

	public static AvailableLeavesVO ConvertToAvailableLeavesVO(EmployeeLeaveDetail a) {

		AvailableLeavesVO availableLeaves = new AvailableLeavesVO();
		VOMasterLeaveType voMasterLeaveType = new VOMasterLeaveType();

		voMasterLeaveType.setId(a.getMasterLeaveType().getId());

		if (a.getMasterLeaveType().getIsLop().equalsIgnoreCase("Y")) {
			availableLeaves.setLeaveAvailable(0);
		} else {
			availableLeaves.setLeaveAvailable(a.getLeaveAvailable());
		}

		availableLeaves.setClosingBalance(!HRMSHelper.isNullOrEmpty(a.getClosingBalance()) ? a.getClosingBalance() : 0);
		availableLeaves.setFyLeaveEncashment(
				!HRMSHelper.isNullOrEmpty(a.getFyLeaveEncashment()) ? a.getFyLeaveEncashment() : 0);
//		availableLeaves.setLeaveEarned(a.getLeaveEarned());
		availableLeaves.setLeaveEarned(a.getLeaveEarned() != null ? a.getLeaveEarned() : 0);
		availableLeaves.setMasterLeaveType(null);

//		availableLeaves.add(voEmployeeLeaveDetail);

		return availableLeaves;
	}

	public static EmployeeLeaveDetailsVO convertToEmployeeLeaveDetailsVO(List<EmployeeLeaveApplied> leaveApplied) {

		List<EmployeeGetAllAppliedLeavesVO> leaveDetails = new ArrayList<EmployeeGetAllAppliedLeavesVO>();
		EmployeeGetAllAppliedLeavesVO leaveDetail = null;
		EmployeeLeaveDetailsVO leaveDetailsVO = new EmployeeLeaveDetailsVO();

		for (EmployeeLeaveApplied applied : leaveApplied) {
			leaveDetail = new EmployeeGetAllAppliedLeavesVO();
			leaveDetail.setAppliedBy(applied.getAppliedBy());
			leaveDetail.setApproverActionDateWithdrawn(applied.getApproverActionDateWithdrawn());
			leaveDetail.setApproverCommentOnWithdrawn(applied.getApproverCommentOnWithdrawn());
			leaveDetail.setAttachment(applied.getAttachment());
			leaveDetail.setCc(applied.getCc());
			leaveDetail.setContactDetails(applied.getContactDetails());
			leaveDetail.setDateOfApplied(applied.getDateOfApplied());
			leaveDetail.setDateOfApproverAction(applied.getDateOfApproverAction());
			leaveDetail.setDateOfCancel(applied.getDateOfCancle());
			leaveDetail.setDateOfWithdrawn(applied.getDateOfWithdrawn());
			leaveDetail.setFromDate(applied.getFromDate());
			leaveDetail.setFromSession(applied.getFromSession());
			leaveDetail.setId(applied.getId());
			leaveDetail.setLeaveStatus(applied.getLeaveStatus());
			leaveDetail.setLeaveTypeId(applied.getMasterLeaveType().getId());
			leaveDetail.setNoOfDays(applied.getNoOfDays());
			leaveDetail.setReasonForApply(applied.getReasonForApply());
			leaveDetail.setReasonForCancel(applied.getReasonForCancel());
			leaveDetail.setReasonForReject(applied.getReasonForReject());
			leaveDetail.setReasonForWithdrawn(applied.getReasonForWithdrawn());
			leaveDetail.setToDate(applied.getToDate());
			leaveDetail.setToSession(applied.getToSession());
			leaveDetail.setLeaveTypeName(applied.getMasterLeaveType().getLeaveTypeName());
			leaveDetail.setLeaveTypeDescription(applied.getMasterLeaveType().getLeaveTypeDescription());
			leaveDetail.setEmployeeId(applied.getEmployee().getId());
			leaveDetail.setEmployeeName(applied.getEmployee().getCandidate().getFirstName() + " "
					+ applied.getEmployee().getCandidate().getLastName());

			leaveDetails.add(leaveDetail);
		}
		leaveDetailsVO.setLeaveDetails(leaveDetails);

		return leaveDetailsVO;
	}

	public static EmployeeLeaveDetailsVO convertToEmployeeGrantLeaveDetailsForEmployee(
			List<EmployeeGrantLeaveDetail> employeeGrantLeaveDetails, List<EmployeeCreditLeaveDetail> creditLeave) {

		EmployeeLeaveDetailsVO leaveDetailsVO = new EmployeeLeaveDetailsVO();

		List<EmployeeGetAllAppliedLeavesVO> leaveDetails = employeeGrantLeaveDetails.stream().map(applied -> {
			EmployeeGetAllAppliedLeavesVO leaveDetail = new EmployeeGetAllAppliedLeavesVO();
			grantLeaveDetails(applied, leaveDetail);

			Optional<Date> validTill = creditLeave.stream().filter(
					credit -> !HRMSHelper.isNullOrEmpty(credit.getFromDate()) && credit.getFromDate().equals(applied.getFromDate()))
					.map(EmployeeCreditLeaveDetail::getToDate).findFirst();
			leaveDetail.setValidTill(validTill.orElse(null)); 

			return leaveDetail;
		}).collect(Collectors.toList());

		leaveDetailsVO.setLeaveDetails(leaveDetails);

		return leaveDetailsVO;
	}

	private static void grantLeaveDetails(EmployeeGrantLeaveDetail applied, EmployeeGetAllAppliedLeavesVO leaveDetail) {
		leaveDetail.setAppliedBy(applied.getAppliedBy());
		leaveDetail.setApproverActionDateWithdrawn(applied.getApproverActionDateWithdrawn());
		leaveDetail.setApproverCommentOnWithdrawn(applied.getApproverCommentOnWithdrawn());
		leaveDetail.setAttachment(applied.getAttachment());
		leaveDetail.setCc(applied.getCc());
		leaveDetail.setContactDetails(applied.getContactDetails());
		leaveDetail.setDateOfApplied(applied.getDateOfApplied());
		leaveDetail.setDateOfApproverAction(applied.getDateOfApproverAction());
		leaveDetail.setDateOfCancel(applied.getDateOfCancel());
		leaveDetail.setDateOfWithdrawn(applied.getDateOfWithdrawn());
		leaveDetail.setFromDate(applied.getFromDate());
		leaveDetail.setFromSession(applied.getFromSession());
		leaveDetail.setId(applied.getId());
		leaveDetail.setLeaveStatus(applied.getLeaveStatus());
		leaveDetail.setLeaveTypeId(applied.getMasterLeaveType().getId());
		leaveDetail.setNoOfDays(applied.getNoOfDays());
		leaveDetail.setReasonForApply(applied.getReasonForApply());
		leaveDetail.setReasonForCancel(applied.getReasonForCancel());
		leaveDetail.setReasonForWithdrawn(applied.getReasonForWithdrawn());
		leaveDetail.setToDate(applied.getToDate());
		leaveDetail.setToSession(applied.getToSession());
		leaveDetail.setLeaveTypeName(applied.getMasterLeaveType().getLeaveTypeName());
		leaveDetail.setLeaveTypeDescription(applied.getMasterLeaveType().getLeaveTypeDescription());
		leaveDetail.setEmployeeId(applied.getEmployee().getId());
		leaveDetail.setEmployeeName(applied.getEmployee().getCandidate().getFirstName() + " "
				+ applied.getEmployee().getCandidate().getLastName());
	}

	
	public static EmployeeLeaveDetailsVO convertToEmployeeGrantLeaveDetailsVO(
			List<EmployeeGrantLeaveDetail> employeeGrantLeaveDetails) {
		List<EmployeeGetAllAppliedLeavesVO> leaveDetails = new ArrayList<EmployeeGetAllAppliedLeavesVO>();
		EmployeeGetAllAppliedLeavesVO leaveDetail = null;
		EmployeeLeaveDetailsVO leaveDetailsVO = new EmployeeLeaveDetailsVO();
		
		for (EmployeeGrantLeaveDetail applied : employeeGrantLeaveDetails) {
			leaveDetail = new EmployeeGetAllAppliedLeavesVO();
			grantLeaveDetails(applied, leaveDetail);
			leaveDetails.add(leaveDetail);
		}
		leaveDetailsVO.setLeaveDetails(leaveDetails);

		return leaveDetailsVO;
	}


	public static List<VOHolidayResponse> transalteToHolidayListVO(List<OrganizationHoliday> holidayEntityList) {
		List<VOHolidayResponse> voHolidayList = new ArrayList<VOHolidayResponse>();

		for (OrganizationHoliday organizationHolidayEntity : holidayEntityList) {
			VOHolidayResponse voHoliday = new VOHolidayResponse();
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

	public static EmployeeListVO convertToEmployeeModel(Employee employeeEntity) {
		EmployeeListVO voEmployee = null;
		if (employeeEntity != null) {
			voEmployee = new EmployeeListVO();

			voEmployee.setEmployeeId(employeeEntity.getId());
			voEmployee.setFirstName(employeeEntity.getCandidate().getFirstName());
			voEmployee.setLastName(employeeEntity.getCandidate().getLastName());
			voEmployee.setCandidateId(employeeEntity.getCandidate().getId());
			voEmployee.setOfficialEmailId(employeeEntity.getOfficialEmailId());

		}
		return voEmployee;
	}
	
	public static EmployeeCompOffCreditLeaveHistory transformToCompOffHistory(
			EmployeeCompOffCreditLeaveHistory compOffCreditLeaveHistory,EmployeeLeaveDetail empLeaveDetail
			)
			throws ParseException {
		
		
		compOffCreditLeaveHistory.setCreatedDate(new Date());
		compOffCreditLeaveHistory.setCreditedBy(empLeaveDetail.getCreatedBy());
		compOffCreditLeaveHistory.setIsActive(IHRMSConstants.isActive);
		compOffCreditLeaveHistory.setEmployee(empLeaveDetail.getEmployee());
	    compOffCreditLeaveHistory.setLeaveBalance(empLeaveDetail.getLeaveAvailable());
	    compOffCreditLeaveHistory.setMasterLeaveType(empLeaveDetail.getMasterLeaveType());

	
		return compOffCreditLeaveHistory;

	



	}

}
