package com.vinsys.hrms.directonboard.service.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.directonboard.service.IDirectOnBoardService;
import com.vinsys.hrms.directonboard.service.processors.DirectOnBoardProcessorFactory;
import com.vinsys.hrms.directonboard.service.processors.DirectOnBoardingDependencies;
import com.vinsys.hrms.directonboard.service.processors.IDirectOnboardProcessor;
import com.vinsys.hrms.directonboard.util.DirectOnboardCommonMethods;
import com.vinsys.hrms.directonboard.vo.AddressDetailsRequestVo;
import com.vinsys.hrms.directonboard.vo.CurrentEmploymentRequestVO;
import com.vinsys.hrms.directonboard.vo.DeleteFileRequestVO;
import com.vinsys.hrms.directonboard.vo.EmailCheckVo;
import com.vinsys.hrms.directonboard.vo.FileUploadRequestVO;
import com.vinsys.hrms.directonboard.vo.PreviousEmploymentRequestVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailListVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailsRequestVO;
import com.vinsys.hrms.directonboard.vo.ValidationVO;
import com.vinsys.hrms.employee.vo.BankDetailsVO;
import com.vinsys.hrms.employee.vo.CertificationDetailsVO;
import com.vinsys.hrms.employee.vo.EducationalDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyDetailsVO;
import com.vinsys.hrms.employee.vo.HealthDetailsVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.dao.IHodCycleFinalRatingDao;
import com.vinsys.hrms.kra.dao.IHodToDepartmentMap;
import com.vinsys.hrms.kra.dao.IHrToHodMapDao;
import com.vinsys.hrms.kra.dao.IKraDao;
import com.vinsys.hrms.kra.dao.IKraDetailsDao;
import com.vinsys.hrms.kra.dao.IKraWfDao;
import com.vinsys.hrms.kra.vo.EmpIdVo;
import com.vinsys.hrms.master.dao.IGradeDAO;
import com.vinsys.hrms.master.dao.IMasterGenderDAO;
import com.vinsys.hrms.master.dao.IMasterMaritalStatusDao;
import com.vinsys.hrms.master.dao.IMasterTitleDao;
import com.vinsys.hrms.master.service.IMasterService;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.security.service.IAuthorizationService;
import com.vinsys.hrms.services.EmployeeLeaveDetailsService;
import com.vinsys.hrms.util.DirectOnboardAuthorityHelper;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.EmployeeAuthorityHelper;

@Service
public class DirectOnBoardServiceImpl implements IDirectOnBoardService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app_version}")
	String applicationVersion;

	@Value("${rootDirectory}")
	private String rootDirectory;

	@Autowired
	IHRMSEmployeeDAO employeeDAO;

	@Autowired
	IHRMSEmployeeCurrentOrganizationDetailDAO currentDAO;

	@Autowired
	IHRMSMasterDesignationDAO designationDAO;
	@Autowired
	IHRMSMasterCityDAO cityDAO;
	@Autowired
	IHRMSMasterStateDAO stateDAO;
	@Autowired
	IHRMSMasterCountryDAO counryDAO;
	@Autowired
	IAuthorizationService authorizationServiceImpl;
	@Autowired
	IHRMSEmployeeReportingManager reportingManagerDAO;
	@Autowired
	IHRMSMasterLeaveType leaveTypeDAO;
	@Autowired
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO empLeaveDetailsDAO;
	@Autowired
	IHRMSMasterLeaveType masterLeaveTypeDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO organizationHolidayDAO;
	@Autowired
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;
	@Autowired
	IHRMSHtmlTemplateDAO emailTemplateDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailsDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSEmployeeCreditLeaveDAO creditLeaveDAO;
	@Autowired
	IHRMSEmployeeCreditAppliedLeaveMappingDAO creditAppliedLeaveMappingDAO;
	@Autowired
	IHRMSMasterEmployementTypeDAO masterEmploymentTypeDAO;
	@Autowired
	EmployeeLeaveDetailsService employeeLeaveDetailsService;
	@Autowired
	IHRMSMasterLeaveTypeDAO mstLeaveTypeDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailDAO;
	@Autowired
	IHRMSEmployeeGrantLeaveDAO employeeGrantLeaveDAO;
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO orgHolidayDAO;
	@Autowired
	IHRMSCreditCompOffHistoryDAO creditCompOffHistoryDAO;
	@Autowired
	EmployeeAuthorityHelper employeeAuthorityHelper;
	@Autowired
	IHRMSProfessionalDetailsDAO professionalDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO holidayDAO;
	@Autowired
	IMasterService masterService;
	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	@Autowired
	IHRMSEmployeeCreditLeaveDAO employeeCreditLeaveDAO;
	@Autowired
	IHRMSCandidatePersonalDetailDAO PersonalDetailDAO;
	@Autowired
	IHRMSLoginDAO logDao;
	@Autowired
	IHRMSCandidatePassportDetailsDAO passportDetailsDAO;
	@Autowired
	DirectOnboardAuthorityHelper directOnboardAuthorityHelper;
	@Autowired
	IHRMSBankDetailsDAO bankDAO;
	@Autowired
	IHRMSCandidatePreviousEmploymentDAO previousEmploymentDAO;
	@Autowired
	IHRMSCandidateFamilyDetailsDAO familyDetailsDAO;
	@Autowired
	IMasterGenderDAO genderDAO;
	@Autowired
	IMasterMaritalStatusDao maritalStatusDao;
	@Autowired
	IMasterTitleDao titleDAO;
	@Autowired
	IHRMSCandidateHealthReportDAO healthDao;
	@Autowired
	IHRMSMasterDepartmentDAO departmentDAO;
	@Autowired
	IHRMSMasterBranchDAO branchDAO;
	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;
	@Autowired
	IHRMSLoginEntityTypeDAO loginTypeDAO;
	@Autowired
	IHRMSEmployeeAcnDAO acnDAO;
	@Autowired
	IHRMSEmployeeReportingManager reportingMangerDAO;
	@Autowired
	IHRMSCandidateQualificationDAO qualificationDao;
	@Autowired
	IHRMSCandidateCertificationDetalDAO certificationDao;
	@Autowired
	DirectOnboardCommonMethods commonMethod;

	@Autowired
	IHRMSCandidateAddressDAO addressDao;

	@Autowired
	IHRMSCandidateChecklistDAO CandidateChecklistDAO;

	@Autowired
	IHRMSMasterCandidateChecklistActionDAO candidateChecklistActionDAO;

	@Autowired
	IHRMSCandidateLetterDAO candidateLetterDAO;

	@Autowired
	IHRMSMasterEmailSenderDAO emailDao;

	@Autowired
	IEmployeeDepartmentDAO employeeDepartmentDAO;

	@Autowired
	IEmployeeDesignationDAO employeeDesignationDAO;

	@Autowired
	IEmployeeBranchDAO employeeBranchDAO;

	@Autowired
	IEmployeeDivisionDAO employeeDivisionDAO;

	@Autowired
	IGradeDAO gradeDAO;

	@Autowired
	IHrToHodMapDao hrtohodMapDao;

	@Autowired
	IHodToDepartmentMap hodToDeptMapDao;

	@Autowired
	IKraDao kraDao;

	@Autowired
	IKraWfDao kraWfDao;

	@Autowired
	IKraDetailsDao kradetailDao;

	@Autowired
	IHodCycleFinalRatingDao hodFinalRatingDao;

	@Value("${base.url}")
	private String baseURL;

	@Value("${MaxFileUploadSize}")
	private Long maxFileSizeUpload;

	@Override
	public HRMSBaseResponse<?> addProfileDetails(ProfileDetailsRequestVO request)
			throws HRMSException, ParseException, NoSuchAlgorithmException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		if (request.getId() == null || request.getId() == 0) {
			return processor.addProfileDetails(request);
		} else {
			return processor.updateProfileDetails(request);
		}
	}

	protected DirectOnBoardingDependencies getDependencies() {
		DirectOnBoardingDependencies dependencies = new DirectOnBoardingDependencies(authorizationServiceImpl,
				PersonalDetailDAO, professionalDAO, employeeDAO, reportingManagerDAO, leaveTypeDAO,
				employeeLeaveAppliedDAO, empLeaveDetailsDAO, masterLeaveTypeDAO, organizationHolidayDAO,
				organizationWeekoffDAO, emailTemplateDAO, employeeLeaveDetailsDAO, emailsender, creditLeaveDAO,
				creditAppliedLeaveMappingDAO, masterEmploymentTypeDAO, employeeLeaveDetailsService, mstLeaveTypeDAO,
				employeeLeaveDetailDAO, employeeGrantLeaveDAO, candidateDAO, orgHolidayDAO, creditCompOffHistoryDAO,
				employeeAuthorityHelper, holidayDAO, masterService, applicationVersion, baseURL, organizationDAO,
				employeeCreditLeaveDAO, logDao, passportDetailsDAO, directOnboardAuthorityHelper, bankDAO,
				designationDAO, cityDAO, stateDAO, counryDAO, previousEmploymentDAO, familyDetailsDAO, titleDAO,
				maritalStatusDao, genderDAO, departmentDAO, branchDAO, divisionDAO, loginTypeDAO, acnDAO,
				reportingMangerDAO, qualificationDao, certificationDao, healthDao, currentDAO, commonMethod, addressDao,
				maxFileSizeUpload, CandidateChecklistDAO, candidateChecklistActionDAO, candidateLetterDAO,
				rootDirectory, emailDao, employeeDepartmentDAO, employeeDesignationDAO, employeeBranchDAO,
				employeeDivisionDAO, gradeDAO, hrtohodMapDao, hodToDeptMapDao, kraDao, kraWfDao, kradetailDao,
				hodFinalRatingDao);

		return dependencies;
	}

	@Override
	public HRMSBaseResponse<List<ProfileDetailListVO>> getAllEmployeeList(String keyword, Pageable pageable)
			throws HRMSException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.getAllEmployeeList(keyword, pageable);
	}

	@Override
	public HRMSBaseResponse<ProfileDetailVO> getProfileDetail(Long candidateId) throws HRMSException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.getProfileDetail(candidateId);
	}

	@Override
	public HRMSBaseResponse<?> saveBankDetail(BankDetailsVO bankDetails) throws HRMSException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.saveBankDetail(bankDetails);
	}

	@Override
	public HRMSBaseResponse<?> addPreviousEmployment(PreviousEmploymentRequestVO request)
			throws HRMSException, ParseException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.addPreviousEmployment(request);

	}

	@Override
	public HRMSBaseResponse<?> saveFamilyDetail(FamilyDetailsVO familyDetails) throws HRMSException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.saveFamilyDetail(familyDetails);
	}

	@Override
	public HRMSBaseResponse<?> addCurrentEmployment(CurrentEmploymentRequestVO request)
			throws HRMSException, ParseException, NoSuchAlgorithmException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.addCurrentEmployment(request);
	}

	@Override
	public HRMSBaseResponse<?> addEducationDetails(EducationalDetailsVO educationDetailsVo)
			throws HRMSException, ParseException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
//		IDirectOnboardProcessor processor = getProcessor();
		return processor.saveEducationDetails(educationDetailsVo);
	}

	@Override
	public HRMSBaseResponse<?> addCertificationDetails(CertificationDetailsVO certificationDetails)
			throws HRMSException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
//		IDirectOnboardProcessor processor = getProcessor();
		return processor.saveCertificationDetails(certificationDetails);
	}

	@Override
	public HRMSBaseResponse<?> saveHealthDetail(HealthDetailsVO healthDetail) throws HRMSException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.saveHealthDetail(healthDetail);
	}

	@Override
	public HRMSBaseResponse<?> deleteProfile(Long candidateId) throws HRMSException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.deleteProfile(candidateId);
	}

	@Override
	public HRMSBaseResponse<ValidationVO> getDirectOnboardingValidationFlags(Long candidateId) throws HRMSException {
		IDirectOnboardProcessor processor = getProcessor();
		return processor.getDirectOnboardingValidationFlags(candidateId);
	}

	public IDirectOnboardProcessor getProcessor() {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		return factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
	}

	@Override
	public HRMSBaseResponse<?> addAddressDetails(AddressDetailsRequestVo addressVo) throws HRMSException {
		IDirectOnboardProcessor processor = getProcessor();
		return processor.saveAddressDetails(addressVo);
	}

	@Override
	public HRMSBaseResponse<?> uploadFile(MultipartFile[] request, FileUploadRequestVO requestVO)
			throws HRMSException, IOException {

		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.uploadFile(request, requestVO);
	}

	@Override
	public HRMSBaseResponse<?> deleteFile(DeleteFileRequestVO request) throws HRMSException, IOException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.deleteFile(request);
	}

	@Override
	public HRMSBaseResponse<?> uploadExcelFile(MultipartFile file)
			throws HRMSException, IOException, NoSuchAlgorithmException {

		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.uploadExcelFile(file);
	}

	@Override
	public HRMSBaseResponse<?> candidateEmailIdCheck(EmailCheckVo request) throws HRMSException, IOException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.candidateEmailIdCheck(request);
	}

	@Override
	public HRMSBaseResponse<?> employeeEmailIdCheck(EmailCheckVo request) throws HRMSException, IOException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.employeeEmailIdCheck(request);
	}

	@Override
	public HRMSBaseResponse<?> employeeCodeCheck(EmailCheckVo request) throws HRMSException, IOException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.employeeCodeCheck(request);
	}

	@Override
	public HRMSBaseResponse<?> deleteEmployee(EmpIdVo request) throws HRMSException, IOException {
		DirectOnBoardingDependencies dependencies = getDependencies();
		DirectOnBoardProcessorFactory factory = new DirectOnBoardProcessorFactory();
		IDirectOnboardProcessor processor = factory.getDirectOnBoardProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(),
				dependencies);
		return processor.deleteEmployee(request);
	}
}
