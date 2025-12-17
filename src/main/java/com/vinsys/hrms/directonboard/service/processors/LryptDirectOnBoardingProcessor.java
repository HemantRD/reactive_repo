package com.vinsys.hrms.directonboard.service.processors;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.directonboard.vo.ProfileDetailsRequestVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCurrentDetail;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.LogConstants;
import com.vinsys.hrms.util.ResponseCode;

public class LryptDirectOnBoardingProcessor extends AbstractDirectOnBoardingProcessor {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private DirectOnBoardingDependencies directOnBoardingDependencies;

	protected LryptDirectOnBoardingProcessor(DirectOnBoardingDependencies directOnBoardingDependencies) {
		super(directOnBoardingDependencies);
		this.directOnBoardingDependencies = directOnBoardingDependencies;
	}

	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public HRMSBaseResponse<?> deleteProfile(Long candidateId) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "deleteProfile");
		}
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (directOnBoardingDependencies.authorizationServiceImpl.isAuthorizedFunctionName("deleteProfile", role)) {
			directOnBoardingDependencies.directOnboardAuthorityHelper.deleteProfileValidator(candidateId);
			Candidate candidate = directOnBoardingDependencies.candidateDAO.findByIdAndIsActiveAndorgId(candidateId,
					IHRMSConstants.isActive, orgId);

			Candidate inActiveCandidate=directOnBoardingDependencies.candidateDAO.findByIdAndIsActiveAndorgId(candidateId,
					IHRMSConstants.isNotActive, orgId);
			
			if(!HRMSHelper.isNullOrEmpty(inActiveCandidate)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1620));
			}
			if (!HRMSHelper.isNullOrEmpty(candidate)) {//it checks candidate from different org too
				setProfileInActive(candidate);
				response = getBaseResponse(ResponseCode.getResponseCodeMap().get(1619), 1200, null, 0);
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "deleteProfile");
		}
		return response;
	}

	private void setProfileInActive(Candidate candidate) {
		Employee employee = candidate.getEmployee();
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			employee.setIsActive(IHRMSConstants.isNotActive);
			directOnBoardingDependencies.employeeDAO.save(employee);
		}
		CandidatePersonalDetail personalDetail = candidate.getCandidatePersonalDetail();
		if (!HRMSHelper.isNullOrEmpty(personalDetail)) {
			personalDetail.setIsActive(IHRMSConstants.isNotActive);
			directOnBoardingDependencies.PersonalDetailDAO.save(personalDetail);
		}
		CandidateProfessionalDetail professional = candidate.getCandidateProfessionalDetail();
		if (!HRMSHelper.isNullOrEmpty(professional)) {
			professional.setIsActive(IHRMSConstants.isNotActive);
			directOnBoardingDependencies.professionalDAO.save(professional);
		}
		LoginEntity loginEntity = candidate.getLoginEntity();
		if (!HRMSHelper.isNullOrEmpty(loginEntity)) {
			loginEntity.setIsActive(IHRMSConstants.isNotActive);
			directOnBoardingDependencies.logDao.save(loginEntity);
		}
		if (!HRMSHelper.isNullOrEmpty(candidate.getEmployee())) {
			EmployeeCurrentDetail currentEmployeeDetail = candidate.getEmployee().getEmployeeCurrentDetail();
			if (!HRMSHelper.isNullOrEmpty(currentEmployeeDetail)) {
				currentEmployeeDetail.setIsActive(IHRMSConstants.isNotActive);
				directOnBoardingDependencies.currentEmploymentDAO.save(currentEmployeeDetail);
			}
		}
		candidate.setIsActive(IHRMSConstants.isNotActive);
		directOnBoardingDependencies.candidateDAO.save(candidate);
	}
}
