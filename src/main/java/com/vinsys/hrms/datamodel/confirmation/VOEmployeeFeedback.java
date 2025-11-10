package com.vinsys.hrms.datamodel.confirmation;

import java.util.List;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOEmployeeFeedback extends VOAuditBase{
	
		private long employeeId;
		private String confirmationDueDate;
		private String confirmationStatus;
		private String dateOfJoining;
		private String department;
		private String designation;
		private String reportingManager;
		private String location;
		private List<VOProbationFeedback> feedback;
		
		
		public long getEmployeeId() {
			return employeeId;
		}
		public void setEmployeeId(long employeeId) {
			this.employeeId = employeeId;
		}
		public String getConfirmationDueDate() {
			return confirmationDueDate;
		}
		public void setConfirmationDueDate(String confirmationDueDate) {
			this.confirmationDueDate = confirmationDueDate;
		}
		public String getConfirmationStatus() {
			return confirmationStatus;
		}
		public void setConfirmationStatus(String confirmationStatus) {
			this.confirmationStatus = confirmationStatus;
		}
		public String getDateOfJoining() {
			return dateOfJoining;
		}
		public void setDateOfJoining(String dateOfJoining) {
			this.dateOfJoining = dateOfJoining;
		}
		public String getDepartment() {
			return department;
		}
		public void setDepartment(String department) {
			this.department = department;
		}
		public String getDesignation() {
			return designation;
		}
		public void setDesignation(String designation) {
			this.designation = designation;
		}
		public String getReportingManager() {
			return reportingManager;
		}
		public void setReportingManager(String reportingManager) {
			this.reportingManager = reportingManager;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public List<VOProbationFeedback> getFeedback() {
			return feedback;
		}
		public void setFeedback(List<VOProbationFeedback> feedback) {
			this.feedback = feedback;
		}
}
