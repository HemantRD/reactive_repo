package com.vinsys.hrms.employee.service.impl.personaldetails.processors;

import com.vinsys.hrms.dao.IHRMSCandidateAddressDAO;
import com.vinsys.hrms.dao.IHRMSCandidateCertificationDetalDAO;
import com.vinsys.hrms.dao.IHRMSCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateFamilyDetailsDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.dao.IHRMSCandidateQualificationDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.util.EmployeeAuthorityHelper;

public class PersonalDetailsProcessorDependencies {
	IHRMSCandidateDAO candidateDao;
	IHRMSEmployeeDAO employeeDAO;
	IHRMSCandidateAddressDAO addressDAO;
	IHRMSCandidateChecklistDAO checklistDAO;
	IHRMSProfessionalDetailsDAO professionalDetailDao;
	IHRMSCandidateCertificationDetalDAO candidateCertificationDetalDAO;
	IHRMSCandidateQualificationDAO educationDao;
	IHRMSCandidateFamilyDetailsDAO familyDetailsDAO;
	IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO;
	EmployeeAuthorityHelper employeeAuthorityHelper;
	IHRMSMasterCountryDAO coutryDao;
	IHRMSMasterStateDAO stateDao;
	IHRMSMasterCityDAO cityDao;

	public PersonalDetailsProcessorDependencies(IHRMSCandidateDAO candidateDao, IHRMSEmployeeDAO employeeDAO,
			IHRMSCandidateAddressDAO addressDAO, IHRMSCandidateChecklistDAO checklistDAO,
			IHRMSProfessionalDetailsDAO professionalDetailDao,
			IHRMSCandidateCertificationDetalDAO candidateCertificationDetalDAO,
			IHRMSCandidateQualificationDAO educationDao, IHRMSCandidateFamilyDetailsDAO familyDetailsDAO,
			IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO, EmployeeAuthorityHelper employeeAuthorityHelper,
			IHRMSMasterCountryDAO coutryDao, IHRMSMasterStateDAO stateDao, IHRMSMasterCityDAO cityDao) {
		super();
		this.candidateDao = candidateDao;
		this.employeeDAO = employeeDAO;
		this.addressDAO = addressDAO;
		this.checklistDAO = checklistDAO;
		this.professionalDetailDao = professionalDetailDao;
		this.candidateCertificationDetalDAO = candidateCertificationDetalDAO;
		this.educationDao = educationDao;
		this.familyDetailsDAO = familyDetailsDAO;
		this.candidatePersonalDetailDAO = candidatePersonalDetailDAO;
		this.employeeAuthorityHelper = employeeAuthorityHelper;
		this.coutryDao = coutryDao;
		this.stateDao = stateDao;
		this.cityDao = cityDao;
	}

	public IHRMSCandidateDAO getCandidateDao() {
		return candidateDao;
	}

	public void setCandidateDao(IHRMSCandidateDAO candidateDao) {
		this.candidateDao = candidateDao;
	}

	public IHRMSEmployeeDAO getEmployeeDAO() {
		return employeeDAO;
	}

	public void setEmployeeDAO(IHRMSEmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}

	public IHRMSCandidateAddressDAO getAddressDAO() {
		return addressDAO;
	}

	public void setAddressDAO(IHRMSCandidateAddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}

	public IHRMSCandidateChecklistDAO getChecklistDAO() {
		return checklistDAO;
	}

	public void setChecklistDAO(IHRMSCandidateChecklistDAO checklistDAO) {
		this.checklistDAO = checklistDAO;
	}

	public IHRMSProfessionalDetailsDAO getProfessionalDetailDao() {
		return professionalDetailDao;
	}

	public void setProfessionalDetailDao(IHRMSProfessionalDetailsDAO professionalDetailDao) {
		this.professionalDetailDao = professionalDetailDao;
	}

	public IHRMSCandidateCertificationDetalDAO getCandidateCertificationDetalDAO() {
		return candidateCertificationDetalDAO;
	}

	public void setCandidateCertificationDetalDAO(IHRMSCandidateCertificationDetalDAO candidateCertificationDetalDAO) {
		this.candidateCertificationDetalDAO = candidateCertificationDetalDAO;
	}

	public IHRMSCandidateQualificationDAO getEducationDao() {
		return educationDao;
	}

	public void setEducationDao(IHRMSCandidateQualificationDAO educationDao) {
		this.educationDao = educationDao;
	}

	public IHRMSCandidateFamilyDetailsDAO getFamilyDetailsDAO() {
		return familyDetailsDAO;
	}

	public void setFamilyDetailsDAO(IHRMSCandidateFamilyDetailsDAO familyDetailsDAO) {
		this.familyDetailsDAO = familyDetailsDAO;
	}

	public IHRMSCandidatePersonalDetailDAO getCandidatePersonalDetailDAO() {
		return candidatePersonalDetailDAO;
	}

	public void setCandidatePersonalDetailDAO(IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO) {
		this.candidatePersonalDetailDAO = candidatePersonalDetailDAO;
	}

	public EmployeeAuthorityHelper getEmployeeAuthorityHelper() {
		return employeeAuthorityHelper;
	}

	public void setEmployeeAuthorityHelper(EmployeeAuthorityHelper employeeAuthorityHelper) {
		this.employeeAuthorityHelper = employeeAuthorityHelper;
	}

	public IHRMSMasterCountryDAO getCoutryDao() {
		return coutryDao;
	}

	public void setCoutryDao(IHRMSMasterCountryDAO coutryDao) {
		this.coutryDao = coutryDao;
	}

	public IHRMSMasterStateDAO getStateDao() {
		return stateDao;
	}

	public void setStateDao(IHRMSMasterStateDAO stateDao) {
		this.stateDao = stateDao;
	}

	public IHRMSMasterCityDAO getCityDao() {
		return cityDao;
	}

	public void setCityDao(IHRMSMasterCityDAO cityDao) {
		this.cityDao = cityDao;
	}

}
