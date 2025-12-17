package com.vinsys.hrms.employee.service.impl.empdetails.processors;

import com.vinsys.hrms.dao.IHRMSBankDetailsDAO;
import com.vinsys.hrms.dao.IHRMSCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateLetterDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;

public class EmploymentDetailsProcessorDependencies {
	IHRMSEmployeeDAO employeeDAO;
	IHRMSBankDetailsDAO bankDAO;
	IHRMSCandidateDAO candidateDAO;
	IHRMSCandidateChecklistDAO checklistDAO;
	IHRMSProfessionalDetailsDAO candidateProfessionalDtlDAO;
	String rootDirectory;
	IHRMSCandidateLetterDAO candidateLetterDAO;
	String baseURL;
	IHRMSMasterEmployementTypeDAO masterEmploymentTypeDAO;

	public EmploymentDetailsProcessorDependencies(IHRMSEmployeeDAO employeeDAO, IHRMSBankDetailsDAO bankDAO,
			IHRMSCandidateDAO candidateDAO, IHRMSCandidateChecklistDAO checklistDAO,
			IHRMSProfessionalDetailsDAO candidateProfessionalDtlDAO, String rootDirectory,
			IHRMSCandidateLetterDAO candidateLetterDAO, String baseURL,
			IHRMSMasterEmployementTypeDAO masterEmploymentTypeDAO) {
		super();
		this.employeeDAO = employeeDAO;
		this.bankDAO = bankDAO;
		this.candidateDAO = candidateDAO;
		this.checklistDAO = checklistDAO;
		this.candidateProfessionalDtlDAO = candidateProfessionalDtlDAO;
		this.rootDirectory = rootDirectory;
		this.candidateLetterDAO = candidateLetterDAO;
		this.baseURL = baseURL;
		this.masterEmploymentTypeDAO = masterEmploymentTypeDAO;
	}

	public IHRMSCandidateLetterDAO getCandidateLetterDAO() {
		return candidateLetterDAO;
	}

	public void setCandidateLetterDAO(IHRMSCandidateLetterDAO candidateLetterDAO) {
		this.candidateLetterDAO = candidateLetterDAO;
	}

	public IHRMSEmployeeDAO getEmployeeDAO() {
		return employeeDAO;
	}

	public void setEmployeeDAO(IHRMSEmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}

	public IHRMSBankDetailsDAO getBankDAO() {
		return bankDAO;
	}

	public void setBankDAO(IHRMSBankDetailsDAO bankDAO) {
		this.bankDAO = bankDAO;
	}

	public IHRMSCandidateDAO getCandidateDAO() {
		return candidateDAO;
	}

	public void setCandidateDAO(IHRMSCandidateDAO candidateDAO) {
		this.candidateDAO = candidateDAO;
	}

	public IHRMSCandidateChecklistDAO getChecklistDAO() {
		return checklistDAO;
	}

	public void setChecklistDAO(IHRMSCandidateChecklistDAO checklistDAO) {
		this.checklistDAO = checklistDAO;
	}

	public IHRMSProfessionalDetailsDAO getCandidateProfessionalDtlDAO() {
		return candidateProfessionalDtlDAO;
	}

	public void setCandidateProfessionalDtlDAO(IHRMSProfessionalDetailsDAO candidateProfessionalDtlDAO) {
		this.candidateProfessionalDtlDAO = candidateProfessionalDtlDAO;
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public IHRMSMasterEmployementTypeDAO getMasterEmploymentTypeDAO() {
		return masterEmploymentTypeDAO;
	}

	public void setMasterEmploymentTypeDAO(IHRMSMasterEmployementTypeDAO masterEmploymentTypeDAO) {
		this.masterEmploymentTypeDAO = masterEmploymentTypeDAO;
	}

}
