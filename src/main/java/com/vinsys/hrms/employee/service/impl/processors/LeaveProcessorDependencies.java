package com.vinsys.hrms.employee.service.impl.processors;

import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCreditCompOffHistoryDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditAppliedLeaveMappingDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeGrantLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveDetailsDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSHtmlTemplateDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveType;
import com.vinsys.hrms.dao.IHRMSMasterLeaveTypeDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.employee.vo.ApplyLeaveRequestVO;
import com.vinsys.hrms.master.service.IMasterService;
import com.vinsys.hrms.services.EmployeeLeaveDetailsService;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.EmployeeAuthorityHelper;

public class LeaveProcessorDependencies {
	private ApplyLeaveRequestVO request;
	IHRMSEmployeeDAO employeeDAO;
	IHRMSEmployeeReportingManager reportingManagerDAO;
	IHRMSMasterLeaveType leaveTypeDAO;
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;
	IHRMSEmployeeLeaveDetailsDAO empLeaveDetailsDAO;
	IHRMSMasterLeaveType masterLeaveTypeDAO;
	IHRMSOrganizationHolidayDAO organizationHolidayDAO;
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;
	IHRMSHtmlTemplateDAO emailTemplateDAO;
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailsDAO;
	EmailSender emailsender;
	IHRMSEmployeeCreditLeaveDAO creditLeaveDAO;
	IHRMSEmployeeCreditAppliedLeaveMappingDAO creditAppliedLeaveMappingDAO;
	IHRMSMasterEmployementTypeDAO masterEmploymentTypeDAO;
	EmployeeLeaveDetailsService employeeLeaveDetailsService;
	IHRMSMasterLeaveTypeDAO mstLeaveTypeDAO;
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailDAO;
	IHRMSEmployeeGrantLeaveDAO employeeGrantLeaveDAO;
	IHRMSCandidateDAO candidateDAO;
	IHRMSOrganizationHolidayDAO orgHolidayDAO;
	IHRMSCreditCompOffHistoryDAO creditCompOffHistoryDAO;
	EmployeeAuthorityHelper employeeAuthorityHelper;
	IHRMSOrganizationHolidayDAO holidayDAO;
	IMasterService masterService;
	String applicationVersion;
	String baseURL;
	IHRMSOrganizationDAO organizationDAO;
	IHRMSEmployeeCreditLeaveDAO employeeCreditLeaveDAO;
	public LeaveProcessorDependencies(IHRMSEmployeeDAO employeeDAO, IHRMSEmployeeReportingManager reportingManagerDAO,
			IHRMSMasterLeaveType leaveTypeDAO, IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO,
			IHRMSEmployeeLeaveDetailsDAO empLeaveDetailsDAO, IHRMSMasterLeaveType masterLeaveTypeDAO,
			IHRMSOrganizationHolidayDAO organizationHolidayDAO, IHRMSOrganizationWeekoffDAO organizationWeekoffDAO,
			IHRMSHtmlTemplateDAO emailTemplateDAO, IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailsDAO,
			EmailSender emailsender, IHRMSEmployeeCreditLeaveDAO creditLeaveDAO,
			IHRMSEmployeeCreditAppliedLeaveMappingDAO creditAppliedLeaveMappingDAO,
			IHRMSMasterEmployementTypeDAO masterEmploymentTypeDAO,
			EmployeeLeaveDetailsService employeeLeaveDetailsService, IHRMSMasterLeaveTypeDAO mstLeaveTypeDAO,
			IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailDAO, IHRMSEmployeeGrantLeaveDAO employeeGrantLeaveDAO,
			IHRMSCandidateDAO candidateDAO, IHRMSOrganizationHolidayDAO orgHolidayDAO,
			IHRMSCreditCompOffHistoryDAO creditCompOffHistoryDAO, EmployeeAuthorityHelper employeeAuthorityHelper,
			IHRMSOrganizationHolidayDAO holidayDAO, IMasterService masterService, String applicationVersion, String baseURL,IHRMSOrganizationDAO organizationDAO,IHRMSEmployeeCreditLeaveDAO employeeCreditLeaveDAO) {
		super();
		this.employeeDAO = employeeDAO;
		this.reportingManagerDAO = reportingManagerDAO;
		this.leaveTypeDAO = leaveTypeDAO;
		this.employeeLeaveAppliedDAO = employeeLeaveAppliedDAO;
		this.empLeaveDetailsDAO = empLeaveDetailsDAO;
		this.masterLeaveTypeDAO = masterLeaveTypeDAO;
		this.organizationHolidayDAO = organizationHolidayDAO;
		this.organizationWeekoffDAO = organizationWeekoffDAO;
		this.emailTemplateDAO = emailTemplateDAO;
		this.employeeLeaveDetailsDAO = employeeLeaveDetailsDAO;
		this.emailsender = emailsender;
		this.creditLeaveDAO = creditLeaveDAO;
		this.creditAppliedLeaveMappingDAO = creditAppliedLeaveMappingDAO;
		this.masterEmploymentTypeDAO = masterEmploymentTypeDAO;
		this.employeeLeaveDetailsService = employeeLeaveDetailsService;
		this.mstLeaveTypeDAO = mstLeaveTypeDAO;
		this.employeeLeaveDetailDAO = employeeLeaveDetailDAO;
		this.employeeGrantLeaveDAO = employeeGrantLeaveDAO;
		this.candidateDAO = candidateDAO;
		this.orgHolidayDAO = orgHolidayDAO;
		this.creditCompOffHistoryDAO = creditCompOffHistoryDAO;
		this.employeeAuthorityHelper = employeeAuthorityHelper;
		this.holidayDAO = holidayDAO;
		this.masterService = masterService;
		this.applicationVersion = applicationVersion;
		this.baseURL=baseURL;
		this.organizationDAO=organizationDAO;
		this.employeeCreditLeaveDAO=employeeCreditLeaveDAO;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public IHRMSEmployeeDAO getEmployeeDAO() {
		return employeeDAO;
	}

	public void setEmployeeDAO(IHRMSEmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}

	public IHRMSEmployeeReportingManager getReportingManagerDAO() {
		return reportingManagerDAO;
	}

	public void setReportingManagerDAO(IHRMSEmployeeReportingManager reportingManagerDAO) {
		this.reportingManagerDAO = reportingManagerDAO;
	}

	public IHRMSMasterLeaveType getLeaveTypeDAO() {
		return leaveTypeDAO;
	}

	public void setLeaveTypeDAO(IHRMSMasterLeaveType leaveTypeDAO) {
		this.leaveTypeDAO = leaveTypeDAO;
	}

	public IHRMSEmployeeLeaveAppliedDAO getEmployeeLeaveAppliedDAO() {
		return employeeLeaveAppliedDAO;
	}

	public void setEmployeeLeaveAppliedDAO(IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO) {
		this.employeeLeaveAppliedDAO = employeeLeaveAppliedDAO;
	}

	public IHRMSEmployeeLeaveDetailsDAO getEmpLeaveDetailsDAO() {
		return empLeaveDetailsDAO;
	}

	public void setEmpLeaveDetailsDAO(IHRMSEmployeeLeaveDetailsDAO empLeaveDetailsDAO) {
		this.empLeaveDetailsDAO = empLeaveDetailsDAO;
	}

	public IHRMSMasterLeaveType getMasterLeaveTypeDAO() {
		return masterLeaveTypeDAO;
	}

	public void setMasterLeaveTypeDAO(IHRMSMasterLeaveType masterLeaveTypeDAO) {
		this.masterLeaveTypeDAO = masterLeaveTypeDAO;
	}

	public IHRMSOrganizationHolidayDAO getOrganizationHolidayDAO() {
		return organizationHolidayDAO;
	}

	public void setOrganizationHolidayDAO(IHRMSOrganizationHolidayDAO organizationHolidayDAO) {
		this.organizationHolidayDAO = organizationHolidayDAO;
	}

	public IHRMSOrganizationWeekoffDAO getOrganizationWeekoffDAO() {
		return organizationWeekoffDAO;
	}

	public void setOrganizationWeekoffDAO(IHRMSOrganizationWeekoffDAO organizationWeekoffDAO) {
		this.organizationWeekoffDAO = organizationWeekoffDAO;
	}

	public IHRMSHtmlTemplateDAO getEmailTemplateDAO() {
		return emailTemplateDAO;
	}

	public void setEmailTemplateDAO(IHRMSHtmlTemplateDAO emailTemplateDAO) {
		this.emailTemplateDAO = emailTemplateDAO;
	}

	public IHRMSEmployeeLeaveDetailsDAO getEmployeeLeaveDetailsDAO() {
		return employeeLeaveDetailsDAO;
	}

	public void setEmployeeLeaveDetailsDAO(IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailsDAO) {
		this.employeeLeaveDetailsDAO = employeeLeaveDetailsDAO;
	}

	public EmailSender getEmailsender() {
		return emailsender;
	}

	public void setEmailsender(EmailSender emailsender) {
		this.emailsender = emailsender;
	}

	public IHRMSEmployeeCreditLeaveDAO getCreditLeaveDAO() {
		return creditLeaveDAO;
	}

	public void setCreditLeaveDAO(IHRMSEmployeeCreditLeaveDAO creditLeaveDAO) {
		this.creditLeaveDAO = creditLeaveDAO;
	}

	public IHRMSEmployeeCreditAppliedLeaveMappingDAO getCreditAppliedLeaveMappingDAO() {
		return creditAppliedLeaveMappingDAO;
	}

	public void setCreditAppliedLeaveMappingDAO(
			IHRMSEmployeeCreditAppliedLeaveMappingDAO creditAppliedLeaveMappingDAO) {
		this.creditAppliedLeaveMappingDAO = creditAppliedLeaveMappingDAO;
	}

	public IHRMSMasterEmployementTypeDAO getMasterEmploymentTypeDAO() {
		return masterEmploymentTypeDAO;
	}

	public void setMasterEmploymentTypeDAO(IHRMSMasterEmployementTypeDAO masterEmploymentTypeDAO) {
		this.masterEmploymentTypeDAO = masterEmploymentTypeDAO;
	}

	public EmployeeLeaveDetailsService getEmployeeLeaveDetailsService() {
		return employeeLeaveDetailsService;
	}

	public void setEmployeeLeaveDetailsService(EmployeeLeaveDetailsService employeeLeaveDetailsService) {
		this.employeeLeaveDetailsService = employeeLeaveDetailsService;
	}

	public IHRMSMasterLeaveTypeDAO getMstLeaveTypeDAO() {
		return mstLeaveTypeDAO;
	}

	public void setMstLeaveTypeDAO(IHRMSMasterLeaveTypeDAO mstLeaveTypeDAO) {
		this.mstLeaveTypeDAO = mstLeaveTypeDAO;
	}

	public IHRMSEmployeeLeaveDetailsDAO getEmployeeLeaveDetailDAO() {
		return employeeLeaveDetailDAO;
	}

	public void setEmployeeLeaveDetailDAO(IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailDAO) {
		this.employeeLeaveDetailDAO = employeeLeaveDetailDAO;
	}

	public IHRMSEmployeeGrantLeaveDAO getEmployeeGrantLeaveDAO() {
		return employeeGrantLeaveDAO;
	}

	public void setEmployeeGrantLeaveDAO(IHRMSEmployeeGrantLeaveDAO employeeGrantLeaveDAO) {
		this.employeeGrantLeaveDAO = employeeGrantLeaveDAO;
	}

	public IHRMSCandidateDAO getCandidateDAO() {
		return candidateDAO;
	}

	public void setCandidateDAO(IHRMSCandidateDAO candidateDAO) {
		this.candidateDAO = candidateDAO;
	}

	public IHRMSOrganizationHolidayDAO getOrgHolidayDAO() {
		return orgHolidayDAO;
	}

	public void setOrgHolidayDAO(IHRMSOrganizationHolidayDAO orgHolidayDAO) {
		this.orgHolidayDAO = orgHolidayDAO;
	}

	public IHRMSCreditCompOffHistoryDAO getCreditCompOffHistoryDAO() {
		return creditCompOffHistoryDAO;
	}

	public void setCreditCompOffHistoryDAO(IHRMSCreditCompOffHistoryDAO creditCompOffHistoryDAO) {
		this.creditCompOffHistoryDAO = creditCompOffHistoryDAO;
	}

	public EmployeeAuthorityHelper getEmployeeAuthorityHelper() {
		return employeeAuthorityHelper;
	}

	public void setEmployeeAuthorityHelper(EmployeeAuthorityHelper employeeAuthorityHelper) {
		this.employeeAuthorityHelper = employeeAuthorityHelper;
	}

	public IHRMSOrganizationHolidayDAO getHolidayDAO() {
		return holidayDAO;
	}

	public void setHolidayDAO(IHRMSOrganizationHolidayDAO holidayDAO) {
		this.holidayDAO = holidayDAO;
	}

	public IMasterService getMasterService() {
		return masterService;
	}

	public void setMasterService(IMasterService masterService) {
		this.masterService = masterService;
	}

	public IHRMSOrganizationDAO getOrganizationDAO() {
		return organizationDAO;
	}

	public void setOrganizationDAO(IHRMSOrganizationDAO organizationDAO) {
		this.organizationDAO = organizationDAO;
	}

	public IHRMSEmployeeCreditLeaveDAO getEmployeeCreditLeaveDAO() {
		return employeeCreditLeaveDAO;
	}

	public void setEmployeeCreditLeaveDAO(IHRMSEmployeeCreditLeaveDAO employeeCreditLeaveDAO) {
		this.employeeCreditLeaveDAO = employeeCreditLeaveDAO;
	}
	
	

}
