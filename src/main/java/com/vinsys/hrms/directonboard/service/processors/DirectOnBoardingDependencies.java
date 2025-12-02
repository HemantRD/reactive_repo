package com.vinsys.hrms.directonboard.service.processors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vinsys.hrms.dao.IEmployeeBranchDAO;
import com.vinsys.hrms.dao.IEmployeeDepartmentDAO;
import com.vinsys.hrms.dao.IEmployeeDesignationDAO;
import com.vinsys.hrms.dao.IEmployeeDivisionDAO;
import com.vinsys.hrms.dao.IHRMSBankDetailsDAO;
import com.vinsys.hrms.dao.IHRMSCandidateAddressDAO;
import com.vinsys.hrms.dao.IHRMSCandidateCertificationDetalDAO;
import com.vinsys.hrms.dao.IHRMSCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateFamilyDetailsDAO;
import com.vinsys.hrms.dao.IHRMSCandidateHealthReportDAO;
import com.vinsys.hrms.dao.IHRMSCandidateLetterDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePassportDetailsDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePreviousEmploymentDAO;
import com.vinsys.hrms.dao.IHRMSCandidateQualificationDAO;
import com.vinsys.hrms.dao.IHRMSCreditCompOffHistoryDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeAcnDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditAppliedLeaveMappingDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCurrentOrganizationDetailDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeGrantLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveDetailsDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSHtmlTemplateDAO;
import com.vinsys.hrms.dao.IHRMSLoginDAO;
import com.vinsys.hrms.dao.IHRMSLoginEntityTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterBranchDAO;
import com.vinsys.hrms.dao.IHRMSMasterCandidateChecklistActionDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSMasterDesignationDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmailSenderDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveType;
import com.vinsys.hrms.dao.IHRMSMasterLeaveTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.directonboard.util.DirectOnboardCommonMethods;
import com.vinsys.hrms.kra.dao.IHodCycleFinalRatingDao;
import com.vinsys.hrms.kra.dao.IHodToDepartmentMap;
import com.vinsys.hrms.kra.dao.IHrToHodMapDao;
import com.vinsys.hrms.kra.dao.IKraDao;
import com.vinsys.hrms.kra.dao.IKraDetailsDao;
import com.vinsys.hrms.kra.dao.IKraWfDao;
import com.vinsys.hrms.master.dao.IGradeDAO;
import com.vinsys.hrms.master.dao.IMasterGenderDAO;
import com.vinsys.hrms.master.dao.IMasterMaritalStatusDao;
import com.vinsys.hrms.master.dao.IMasterTitleDao;
import com.vinsys.hrms.master.service.IMasterService;
import com.vinsys.hrms.security.service.IAuthorizationService;
import com.vinsys.hrms.services.EmployeeLeaveDetailsService;
import com.vinsys.hrms.util.DirectOnboardAuthorityHelper;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.EmployeeAuthorityHelper;

public class DirectOnBoardingDependencies {

	IAuthorizationService authorizationServiceImpl;
	IHRMSCandidatePersonalDetailDAO PersonalDetailDAO;
	IHRMSProfessionalDetailsDAO professionalDAO;
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
	IHRMSLoginDAO logDao;
	IHRMSCandidatePassportDetailsDAO passportDetailsDAO;
	DirectOnboardAuthorityHelper directOnboardAuthorityHelper;
	IHRMSBankDetailsDAO bankDAO;
	IHRMSMasterDesignationDAO designationDAO;
	IHRMSMasterCityDAO cityDAO;
	IHRMSMasterStateDAO stateDAO;
	IHRMSMasterCountryDAO countryDAO;
	IHRMSCandidatePreviousEmploymentDAO previousEmploymentDAO;
	IHRMSCandidateFamilyDetailsDAO familyDetailsDAO;
	IMasterTitleDao titleDAO;
	IMasterMaritalStatusDao maritalStatusDao;
	IMasterGenderDAO genderDAO;
	IHRMSMasterDepartmentDAO departmentDAO;
	IHRMSMasterBranchDAO branchDAO;
	IHRMSMasterDivisionDAO divisionDAO;
	IHRMSLoginEntityTypeDAO loginTypeDAO;
	IHRMSEmployeeAcnDAO acnDAO;
	IHRMSEmployeeReportingManager reportingMangerDAO;
	IHRMSCandidateQualificationDAO qualificationDao;
	IHRMSCandidateCertificationDetalDAO certificationDao;
	IHRMSCandidateHealthReportDAO healthDao;
	IHRMSEmployeeCurrentOrganizationDetailDAO currentEmploymentDAO;
	DirectOnboardCommonMethods commonMethod;
	IHRMSCandidateAddressDAO addressDao;
	Long maxFileSizeUpload;
	IHRMSCandidateChecklistDAO CandidateChecklistDAO;
	IHRMSMasterCandidateChecklistActionDAO candidateChecklistActionDAO;
	IHRMSCandidateLetterDAO candidateLetterDAO;
	String rootDirectory;
	IHRMSMasterEmailSenderDAO emailDao;
	IEmployeeDepartmentDAO employeeDepartmentDAO;
	IEmployeeDesignationDAO employeeDesignationDAO;
	IEmployeeBranchDAO employeeBranchDAO;
	IEmployeeDivisionDAO employeeDivisionDAO;
	IGradeDAO gradeDAO;
	IHrToHodMapDao hrtohodMapDao;
	IHodToDepartmentMap hodToDeptMapDao;
	IKraDao kraDao;
	IKraWfDao kraWfDao;
	IKraDetailsDao kradetailDao;
	IHodCycleFinalRatingDao hodFinalRatingDao;

	public DirectOnBoardingDependencies(IAuthorizationService authorizationServiceImpl,
			IHRMSCandidatePersonalDetailDAO PersonalDetailDAO, IHRMSProfessionalDetailsDAO professionalDAO,
			IHRMSEmployeeDAO employeeDAO, IHRMSEmployeeReportingManager reportingManagerDAO,
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
			IHRMSOrganizationHolidayDAO holidayDAO, IMasterService masterService, String applicationVersion,
			String baseURL, IHRMSOrganizationDAO organizationDAO, IHRMSEmployeeCreditLeaveDAO employeeCreditLeaveDAO,
			IHRMSLoginDAO logDao, IHRMSCandidatePassportDetailsDAO passportDetailsDAO,
			DirectOnboardAuthorityHelper directOnboardAuthorityHelper, IHRMSBankDetailsDAO bankDAO,
			IHRMSMasterDesignationDAO designationDAO, IHRMSMasterCityDAO cityDAO, IHRMSMasterStateDAO stateDAO,
			IHRMSMasterCountryDAO countryDAO, IHRMSCandidatePreviousEmploymentDAO previousEmploymentDAO,
			IHRMSCandidateFamilyDetailsDAO familyDetailsDAO, IMasterTitleDao titleDAO,
			IMasterMaritalStatusDao maritalStatusDao, IMasterGenderDAO genderDAO,
			IHRMSMasterDepartmentDAO departmentDAO, IHRMSMasterBranchDAO branchDAO, IHRMSMasterDivisionDAO divisionDAO,
			IHRMSLoginEntityTypeDAO loginTypeDAO, IHRMSEmployeeAcnDAO acnDAO,
			IHRMSEmployeeReportingManager reportingMangerDAO, IHRMSCandidateQualificationDAO qualificationDao,
			IHRMSCandidateCertificationDetalDAO certificationDao, IHRMSCandidateHealthReportDAO healthDao,
			IHRMSEmployeeCurrentOrganizationDetailDAO currentEmploymentDAO, DirectOnboardCommonMethods commonMethod,
			IHRMSCandidateAddressDAO addressDao, Long maxFileSizeUpload,
			IHRMSCandidateChecklistDAO CandidateChecklistDAO,
			IHRMSMasterCandidateChecklistActionDAO candidateChecklistActionDAO,
			IHRMSCandidateLetterDAO candidateLetterDAO, String rootDirectory, IHRMSMasterEmailSenderDAO emailDao,
			IEmployeeDepartmentDAO employeeDepartmentDAO, IEmployeeDesignationDAO employeeDesignationDAO,
			IEmployeeBranchDAO employeeBranchDAO, IEmployeeDivisionDAO employeeDivisionDAO, IGradeDAO gradeDAO,
			IHrToHodMapDao hrtohodMapDao, IHodToDepartmentMap hodToDeptMapDao, IKraDao kraDao,IKraWfDao kraWfDao,
	IKraDetailsDao kradetailDao,
	IHodCycleFinalRatingDao hodFinalRatingDao) {

		super();
		this.authorizationServiceImpl = authorizationServiceImpl;
		this.PersonalDetailDAO = PersonalDetailDAO;
		this.professionalDAO = professionalDAO;
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
		this.baseURL = baseURL;
		this.organizationDAO = organizationDAO;
		this.employeeCreditLeaveDAO = employeeCreditLeaveDAO;
		this.logDao = logDao;
		this.passportDetailsDAO = passportDetailsDAO;
		this.directOnboardAuthorityHelper = directOnboardAuthorityHelper;
		this.bankDAO = bankDAO;
		this.designationDAO = designationDAO;
		this.cityDAO = cityDAO;
		this.stateDAO = stateDAO;
		this.countryDAO = countryDAO;
		this.previousEmploymentDAO = previousEmploymentDAO;
		this.familyDetailsDAO = familyDetailsDAO;
		this.titleDAO = titleDAO;
		this.maritalStatusDao = maritalStatusDao;
		this.genderDAO = genderDAO;
		this.departmentDAO = departmentDAO;
		this.branchDAO = branchDAO;
		this.divisionDAO = divisionDAO;
		this.loginTypeDAO = loginTypeDAO;
		this.acnDAO = acnDAO;
		this.qualificationDao = qualificationDao;
		this.certificationDao = certificationDao;
		this.healthDao = healthDao;
		this.currentEmploymentDAO = currentEmploymentDAO;
		this.commonMethod = commonMethod;
		this.addressDao = addressDao;
		this.maxFileSizeUpload = maxFileSizeUpload;
		this.CandidateChecklistDAO = CandidateChecklistDAO;
		this.candidateChecklistActionDAO = candidateChecklistActionDAO;
		this.candidateLetterDAO = candidateLetterDAO;
		this.rootDirectory = rootDirectory;
		this.emailDao = emailDao;
		this.employeeDepartmentDAO = employeeDepartmentDAO;
		this.employeeDesignationDAO = employeeDesignationDAO;
		this.employeeBranchDAO = employeeBranchDAO;
		this.employeeDivisionDAO = employeeDivisionDAO;
		this.gradeDAO = gradeDAO;
		this.hrtohodMapDao = hrtohodMapDao;
		this.hodToDeptMapDao = hodToDeptMapDao;
		this.kraDao = kraDao;
		this.kraWfDao=kraWfDao;
		this.kradetailDao=kradetailDao;
		this.hodFinalRatingDao=hodFinalRatingDao;
	}

	public IHRMSCandidateAddressDAO getAddressDao() {
		return addressDao;
	}

	public void setAddressDao(IHRMSCandidateAddressDAO addressDao) {
		this.addressDao = addressDao;
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

	public IAuthorizationService getAuthorizationServiceImpl() {
		return authorizationServiceImpl;
	}

	public void setAuthorizationServiceImpl(IAuthorizationService authorizationServiceImpl) {
		this.authorizationServiceImpl = authorizationServiceImpl;
	}

	public IHRMSCandidatePersonalDetailDAO getPersonalDetailDAO() {
		return PersonalDetailDAO;
	}

	public void setPersonalDetailDAO(IHRMSCandidatePersonalDetailDAO personalDetailDAO) {
		PersonalDetailDAO = personalDetailDAO;
	}

	public IHRMSProfessionalDetailsDAO getProfessionalDAO() {
		return professionalDAO;
	}

	public void setProfessionalDAO(IHRMSProfessionalDetailsDAO professionalDAO) {
		this.professionalDAO = professionalDAO;
	}

	public IHRMSLoginDAO getLogDao() {
		return logDao;
	}

	public void setLogDao(IHRMSLoginDAO logDao) {
		this.logDao = logDao;
	}

	public IHRMSCandidatePassportDetailsDAO getPassportDetailsDAO() {
		return passportDetailsDAO;
	}

	public void setPassportDetailsDAO(IHRMSCandidatePassportDetailsDAO passportDetailsDAO) {
		this.passportDetailsDAO = passportDetailsDAO;
	}

	public DirectOnboardAuthorityHelper getDirectOnboardAuthorityHelper() {
		return directOnboardAuthorityHelper;
	}

	public void setDirectOnboardAuthorityHelper(DirectOnboardAuthorityHelper directOnboardAuthorityHelper) {
		this.directOnboardAuthorityHelper = directOnboardAuthorityHelper;
	}

	public IHRMSBankDetailsDAO getBankDAO() {
		return bankDAO;
	}

	public void setBankDAO(IHRMSBankDetailsDAO bankDAO) {
		this.bankDAO = bankDAO;
	}

	public IHRMSCandidateQualificationDAO getQualificationDao() {
		return qualificationDao;
	}

	public void setQualificationDao(IHRMSCandidateQualificationDAO qualificationDao) {
		this.qualificationDao = qualificationDao;
	}

	public IHRMSCandidateCertificationDetalDAO getCertificationDao() {
		return certificationDao;
	}

	public void setCertificationDao(IHRMSCandidateCertificationDetalDAO certificationDao) {
		this.certificationDao = certificationDao;
	}

	public IHRMSMasterDesignationDAO getDesignationDAO() {
		return designationDAO;
	}

	public void setDesignationDAO(IHRMSMasterDesignationDAO designationDAO) {
		this.designationDAO = designationDAO;
	}

	public IHRMSMasterCityDAO getCityDAO() {
		return cityDAO;
	}

	public void setCityDAO(IHRMSMasterCityDAO cityDAO) {
		this.cityDAO = cityDAO;
	}

	public IHRMSMasterStateDAO getStateDAO() {
		return stateDAO;
	}

	public void setStateDAO(IHRMSMasterStateDAO stateDAO) {
		this.stateDAO = stateDAO;
	}

	public IHRMSMasterCountryDAO getCountryDAO() {
		return countryDAO;
	}

	public void setCountryDAO(IHRMSMasterCountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}

	public IHRMSCandidatePreviousEmploymentDAO getPreviousEmploymentDAO() {
		return previousEmploymentDAO;
	}

	public void setPreviousEmploymentDAO(IHRMSCandidatePreviousEmploymentDAO previousEmploymentDAO) {
		this.previousEmploymentDAO = previousEmploymentDAO;
	}

	public IHRMSCandidateFamilyDetailsDAO getFamilyDetailsDAO() {
		return familyDetailsDAO;
	}

	public void setFamilyDetailsDAO(IHRMSCandidateFamilyDetailsDAO familyDetailsDAO) {
		this.familyDetailsDAO = familyDetailsDAO;
	}

	public IMasterTitleDao getTitleDAO() {
		return titleDAO;
	}

	public void setTitleDAO(IMasterTitleDao titleDAO) {
		this.titleDAO = titleDAO;
	}

	public IMasterMaritalStatusDao getMaritalStatusDao() {
		return maritalStatusDao;
	}

	public void setMaritalStatusDao(IMasterMaritalStatusDao maritalStatusDao) {
		this.maritalStatusDao = maritalStatusDao;
	}

	public IMasterGenderDAO getGenderDAO() {
		return genderDAO;
	}

	public void setGenderDAO(IMasterGenderDAO genderDAO) {
		this.genderDAO = genderDAO;
	}

	public IHRMSMasterDepartmentDAO getDepartmentDAO() {
		return departmentDAO;
	}

	public void setDepartmentDAO(IHRMSMasterDepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
	}

	public IHRMSMasterBranchDAO getBranchDAO() {
		return branchDAO;
	}

	public void setBranchDAO(IHRMSMasterBranchDAO branchDAO) {
		this.branchDAO = branchDAO;
	}

	public IHRMSMasterDivisionDAO getDivisionDAO() {
		return divisionDAO;
	}

	public void setDivisionDAO(IHRMSMasterDivisionDAO divisionDAO) {
		this.divisionDAO = divisionDAO;
	}

	public IHRMSLoginEntityTypeDAO getLoginTypeDAO() {
		return loginTypeDAO;
	}

	public void setLoginTypeDAO(IHRMSLoginEntityTypeDAO loginTypeDAO) {
		this.loginTypeDAO = loginTypeDAO;
	}

	public IHRMSEmployeeAcnDAO getAcnDAO() {
		return acnDAO;
	}

	public void setAcnDAO(IHRMSEmployeeAcnDAO acnDAO) {
		this.acnDAO = acnDAO;
	}

	public IHRMSEmployeeReportingManager getReportingMangerDAO() {
		return reportingMangerDAO;
	}

	public void setReportingMangerDAO(IHRMSEmployeeReportingManager reportingMangerDAO) {
		this.reportingMangerDAO = reportingMangerDAO;
	}

	public IHRMSCandidateHealthReportDAO getHealthDao() {
		return healthDao;
	}

	public void setHealthDao(IHRMSCandidateHealthReportDAO healthDao) {
		this.healthDao = healthDao;
	}

	public IHRMSEmployeeCurrentOrganizationDetailDAO getCurrentEmploymentDAO() {
		return currentEmploymentDAO;
	}

	public void setCurrentEmploymentDAO(IHRMSEmployeeCurrentOrganizationDetailDAO currentEmploymentDAO) {
		this.currentEmploymentDAO = currentEmploymentDAO;
	}

	public DirectOnboardCommonMethods getCommonMethod() {
		return commonMethod;
	}

	public void setCommonMethod(DirectOnboardCommonMethods commonMethod) {
		this.commonMethod = commonMethod;
	}

	public Long getMaxFileSizeUpload() {
		return maxFileSizeUpload;
	}

	public void setMaxFileSizeUpload(Long maxFileSizeUpload) {
		this.maxFileSizeUpload = maxFileSizeUpload;
	}

	public IHRMSCandidateChecklistDAO getCandidateChecklistDAO() {
		return CandidateChecklistDAO;
	}

	public void setCandidateChecklistDAO(IHRMSCandidateChecklistDAO candidateChecklistDAO) {
		CandidateChecklistDAO = candidateChecklistDAO;
	}

	public IHRMSMasterCandidateChecklistActionDAO getCandidateChecklistActionDAO() {
		return candidateChecklistActionDAO;
	}

	public void setCandidateChecklistActionDAO(IHRMSMasterCandidateChecklistActionDAO candidateChecklistActionDAO) {
		this.candidateChecklistActionDAO = candidateChecklistActionDAO;
	}

	public IHRMSCandidateLetterDAO getCandidateLetterDAO() {
		return candidateLetterDAO;
	}

	public void setCandidateLetterDAO(IHRMSCandidateLetterDAO candidateLetterDAO) {
		this.candidateLetterDAO = candidateLetterDAO;
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public IHRMSMasterEmailSenderDAO getEmailDao() {
		return emailDao;
	}

	public void setEmailDao(IHRMSMasterEmailSenderDAO emailDao) {
		this.emailDao = emailDao;
	}

	public IEmployeeDepartmentDAO getEmployeeDepartmentDAO() {
		return employeeDepartmentDAO;
	}

	public void setEmployeeDepartmentDAO(IEmployeeDepartmentDAO employeeDepartmentDAO) {
		this.employeeDepartmentDAO = employeeDepartmentDAO;
	}

	public IEmployeeDesignationDAO getEmployeeDesignationDAO() {
		return employeeDesignationDAO;
	}

	public void setEmployeeDesignationDAO(IEmployeeDesignationDAO employeeDesignationDAO) {
		this.employeeDesignationDAO = employeeDesignationDAO;
	}

	public IEmployeeBranchDAO getEmployeeBranchDAO() {
		return employeeBranchDAO;
	}

	public void setEmployeeBranchDAO(IEmployeeBranchDAO employeeBranchDAO) {
		this.employeeBranchDAO = employeeBranchDAO;
	}

	public IEmployeeDivisionDAO getEmployeeDivisionDAO() {
		return employeeDivisionDAO;
	}

	public void setEmployeeDivisionDAO(IEmployeeDivisionDAO employeeDivisionDAO) {
		this.employeeDivisionDAO = employeeDivisionDAO;
	}

	public IGradeDAO getGradeDAO() {
		return gradeDAO;
	}

	public void setGradeDAO(IGradeDAO gradeDAO) {
		this.gradeDAO = gradeDAO;
	}

	public IHrToHodMapDao getHrtohodMapDao() {
		return hrtohodMapDao;
	}

	public void setHrtohodMapDao(IHrToHodMapDao hrtohodMapDao) {
		this.hrtohodMapDao = hrtohodMapDao;
	}

	public IHodToDepartmentMap getHodToDeptMapDao() {
		return hodToDeptMapDao;
	}

	public void setHodToDeptMapDao(IHodToDepartmentMap hodToDeptMapDao) {
		this.hodToDeptMapDao = hodToDeptMapDao;
	}

	public IKraDao getKraDao() {
		return kraDao;
	}

	public void setKraDao(IKraDao kraDao) {
		this.kraDao = kraDao;
	}

	public IKraWfDao getKraWfDao() {
		return kraWfDao;
	}

	public void setKraWfDao(IKraWfDao kraWfDao) {
		this.kraWfDao = kraWfDao;
	}

	public IKraDetailsDao getKradetailDao() {
		return kradetailDao;
	}

	public void setKradetailDao(IKraDetailsDao kradetailDao) {
		this.kradetailDao = kradetailDao;
	}

	public IHodCycleFinalRatingDao getHodFinalRatingDao() {
		return hodFinalRatingDao;
	}

	public void setHodFinalRatingDao(IHodCycleFinalRatingDao hodFinalRatingDao) {
		this.hodFinalRatingDao = hodFinalRatingDao;
	}
	

}
