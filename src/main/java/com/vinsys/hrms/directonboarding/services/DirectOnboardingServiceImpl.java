package com.vinsys.hrms.directonboarding.services;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.impl.IllegalIcuArgumentException;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSBankDetailsDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.directonboarding.validators.OnboardingRequestValidator;
import com.vinsys.hrms.directonboarding.vo.DirectOnboardingRequest;
import com.vinsys.hrms.directonboarding.vo.DirectOnboardingResponse;
import com.vinsys.hrms.entity.BankDetails;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.security.AuthInfo;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

@Service
public class DirectOnboardingServiceImpl implements IDirectOnboardingService {

	private IHRMSEmployeeDAO employeeDAO;
	private OnboardingRequestValidator validator;
	private IHRMSCandidateDAO candidateDAO;
	private IHRMSBankDetailsDAO bankDetailsDAO;

	public DirectOnboardingServiceImpl(@Autowired IHRMSEmployeeDAO employeeDAO,  OnboardingRequestValidator validator,
			@Autowired IHRMSCandidateDAO candidateDAO, @Autowired IHRMSBankDetailsDAO bankDetailsDAO) {
		this.employeeDAO = employeeDAO;
		this.validator = validator;
		this.candidateDAO = candidateDAO;
		this.bankDetailsDAO = bankDetailsDAO;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public DirectOnboardingResponse onboard(DirectOnboardingRequest request) throws IllegalIcuArgumentException {
		if (HRMSHelper.isNullOrEmpty(request)) {
			throw new IllegalArgumentException("Input Cannot Be Null");
		}
		DirectOnboardingResponse response = new DirectOnboardingResponse();
		validator.validateOnboardingRequest(request);
		Employee employee = createAndInjectEmployeeDetails(request);
		bankDetailsDAO.save(employee.getCandidate().getBank());
		candidateDAO.save(employee.getCandidate());
		employee.setId(employee.getCandidate().getId());
		employeeDAO.save(employee);
		return response;
	}

	private Employee createAndInjectEmployeeDetails(DirectOnboardingRequest request) {
		AuthInfo info = SecurityFilter.TL_CLAIMS.get();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeWithCandidateByEmpIdAndOrgId(info.getEmployeeId(),
				ERecordStatus.Y.name(),SecurityFilter.TL_CLAIMS.get().getOrgId());
		Employee newEmployee = new Employee();
		newEmployee.setCreatedBy(loggedInEmployee.getCandidate().getFirstName());
		newEmployee.setEmployeeACN(null);
		newEmployee.setCreatedDate(new Date());
		populateCandidateDetails(newEmployee, request);
		populateACN(newEmployee, request);
		populateDepartment(newEmployee, request);
		populateExteInfos(newEmployee, request);
		return newEmployee;
	}

	private void populateCandidateDetails(Employee newEmployee, DirectOnboardingRequest request) {
		Candidate candidate = new Candidate();
		candidate.setFirstName(request.getFirstName());
		candidate.setLastName(request.getLastName());
		candidate.setMiddleName(request.getMiddleName());
		BankDetails bankDetails = new BankDetails();
		bankDetails.setAccountNumber(request.getBankAccountNumber());
		bankDetails.setBankName(request.getBankName());
		bankDetails.setIfscCode(request.getIfscCode());
		candidate.setBank(bankDetails);
		candidate.setCandidateStatus(IHRMSConstants.EMPLOYMENT_TYPE_CONFIRMED);
		newEmployee.setCandidate(candidate);
	}

	private void populateExteInfos(Employee newEmployee, DirectOnboardingRequest request) {
		// TODO Auto-generated method stub

	}

	private void populateDepartment(Employee newEmployee, DirectOnboardingRequest request) {
		MasterDepartment department = new MasterDepartment();
	}

	private void populateACN(Employee newEmployee, DirectOnboardingRequest request) {

	}

}
