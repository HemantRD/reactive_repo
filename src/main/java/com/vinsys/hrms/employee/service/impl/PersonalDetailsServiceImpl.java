package com.vinsys.hrms.employee.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.directonboard.vo.AddressDetailsRequestVo;
import com.vinsys.hrms.employee.service.IPersonalDetailsService;
import com.vinsys.hrms.employee.service.impl.personaldetails.processors.IPersonalDetailsProcessor;
import com.vinsys.hrms.employee.service.impl.personaldetails.processors.PersonalDetailsProcessorDependencies;
import com.vinsys.hrms.employee.service.impl.personaldetails.processors.PersonalDetailsProcessorFactory;
import com.vinsys.hrms.employee.vo.CertificationDetailsVO;
import com.vinsys.hrms.employee.vo.CertificationVO;
import com.vinsys.hrms.employee.vo.ContactNoVo;
import com.vinsys.hrms.employee.vo.EducationVO;
import com.vinsys.hrms.employee.vo.EducationalDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyVO;
import com.vinsys.hrms.employee.vo.HealthDetailsVO;
import com.vinsys.hrms.employee.vo.ProfileVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.EmployeeAuthorityHelper;

@Service
public class PersonalDetailsServiceImpl implements IPersonalDetailsService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IHRMSCandidateDAO candidateDao;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSCandidateAddressDAO addressDAO;
	@Autowired
	IHRMSCandidateChecklistDAO checklistDAO;

	@Autowired
	IHRMSProfessionalDetailsDAO professionalDetailDao;

	@Autowired
	IHRMSCandidateCertificationDetalDAO candidateCertificationDetalDAO;

	@Autowired
	IHRMSCandidateQualificationDAO educationDao;

	@Autowired
	IHRMSCandidateFamilyDetailsDAO familyDetailsDAO;
	@Autowired
	IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO;

	@Autowired
	EmployeeAuthorityHelper employeeAuthorityHelper;
	
	@Autowired
	IHRMSMasterCountryDAO coutryDao;
	
	@Autowired
	IHRMSMasterStateDAO stateDao;
	
	@Autowired
	IHRMSMasterCityDAO cityDao;

	@Value("${app_version}")
	private String applicationVersion;

	PersonalDetailsProcessorDependencies getDepedencies() {
		return new PersonalDetailsProcessorDependencies(candidateDao, employeeDAO, addressDAO, checklistDAO,
				professionalDetailDao, candidateCertificationDetalDAO, educationDao, familyDetailsDAO,
				candidatePersonalDetailDAO, employeeAuthorityHelper,coutryDao,stateDao,cityDao);
	}

	@Override
	public HRMSBaseResponse<ProfileVO> getProfileDetails() throws HRMSException {
		PersonalDetailsProcessorDependencies dependencies = getDepedencies();
		PersonalDetailsProcessorFactory factory = new PersonalDetailsProcessorFactory();
		IPersonalDetailsProcessor processor = factory.getPersonalProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.getProfileDetails();

	}

	@Override
	public HRMSBaseResponse<AddressDetailsRequestVo> getAddress(Long candidateId) throws HRMSException {

		PersonalDetailsProcessorDependencies dependencies = getDepedencies();
		PersonalDetailsProcessorFactory factory = new PersonalDetailsProcessorFactory();
		IPersonalDetailsProcessor processor = factory.getPersonalProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.getAddress(candidateId);

	}

	@Override
	public HRMSBaseResponse<EducationVO> getEducationalDetails(Long candidateId) throws HRMSException {

		PersonalDetailsProcessorDependencies dependencies = getDepedencies();
		PersonalDetailsProcessorFactory factory = new PersonalDetailsProcessorFactory();
		IPersonalDetailsProcessor processor = factory.getPersonalProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.getEducationalDetails(candidateId);

	}

	@Override
	public HRMSBaseResponse<CertificationVO> getCertificationDetails(Long candidateId) throws HRMSException {

		PersonalDetailsProcessorDependencies dependencies = getDepedencies();
		PersonalDetailsProcessorFactory factory = new PersonalDetailsProcessorFactory();
		IPersonalDetailsProcessor processor = factory.getPersonalProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.getCertificationDetails(candidateId);

	}

	@Override
	public HRMSBaseResponse<HealthDetailsVO> getHealthDetails(Long candidateId) throws HRMSException {

		PersonalDetailsProcessorDependencies dependencies = getDepedencies();
		PersonalDetailsProcessorFactory factory = new PersonalDetailsProcessorFactory();
		IPersonalDetailsProcessor processor = factory.getPersonalProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.getHealthDetails(candidateId);

	}

	@Override
	public HRMSBaseResponse<FamilyVO> getFamilyDetails(Long candidateId) throws HRMSException {

		PersonalDetailsProcessorDependencies dependencies = getDepedencies();
		PersonalDetailsProcessorFactory factory = new PersonalDetailsProcessorFactory();
		IPersonalDetailsProcessor processor = factory.getPersonalProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.getFamilyDetails(candidateId);

	}

	@Override
	public HRMSBaseResponse<?> addCertification(CertificationDetailsVO request) throws HRMSException {
		PersonalDetailsProcessorDependencies dependencies = getDepedencies();
		PersonalDetailsProcessorFactory factory = new PersonalDetailsProcessorFactory();
		IPersonalDetailsProcessor processor = factory.getPersonalProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.addCertification(request);
	}

	@Override
	public HRMSBaseResponse<?> addEducationDetails(EducationalDetailsVO educationDetails) throws HRMSException {

		PersonalDetailsProcessorDependencies dependencies = getDepedencies();
		PersonalDetailsProcessorFactory factory = new PersonalDetailsProcessorFactory();
		IPersonalDetailsProcessor processor = factory.getPersonalProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.addEducationDetails(educationDetails);

	}

	@Override
	public HRMSBaseResponse<?> addfamilyDetails(FamilyDetailsVO familyDetails) throws HRMSException {
		PersonalDetailsProcessorDependencies dependencies = getDepedencies();
		PersonalDetailsProcessorFactory factory = new PersonalDetailsProcessorFactory();
		IPersonalDetailsProcessor processor = factory.getPersonalProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.addfamilyDetails(familyDetails);

	}
	
	@Override
	public HRMSBaseResponse<?> updateContact(ContactNoVo request) throws HRMSException {
		PersonalDetailsProcessorDependencies dependencies = getDepedencies();
		PersonalDetailsProcessorFactory factory = new PersonalDetailsProcessorFactory();
		IPersonalDetailsProcessor processor = factory.getPersonalProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.updateContact(request);
	}

}
