package com.vinsys.hrms.directonboard.service.processors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.vinsys.hrms.constants.EFileExtension;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.constants.EResponse;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.VOLoginEntityType;
import com.vinsys.hrms.directonboard.vo.AddressDetailsRequestVo;
import com.vinsys.hrms.directonboard.vo.CandidateAddressVo;
import com.vinsys.hrms.directonboard.vo.Counter;
import com.vinsys.hrms.directonboard.vo.CurrentEmploymentRequestVO;
import com.vinsys.hrms.directonboard.vo.CurrentEmploymentResponseVO;
import com.vinsys.hrms.directonboard.vo.DeleteFileRequestVO;
import com.vinsys.hrms.directonboard.vo.EmailCheckVo;
import com.vinsys.hrms.directonboard.vo.ExcelDataVo;
import com.vinsys.hrms.directonboard.vo.FileUploadRequestVO;
import com.vinsys.hrms.directonboard.vo.FileUploadResponseVo;
import com.vinsys.hrms.directonboard.vo.PreviousEmploymentRequestVO;
import com.vinsys.hrms.directonboard.vo.PreviousEmploymentResponseVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailListVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailsRequestVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailsResponseVO;
import com.vinsys.hrms.directonboard.vo.ValidationVO;
import com.vinsys.hrms.employee.vo.BankDetailsVO;
import com.vinsys.hrms.employee.vo.CertificationDetailsVO;
import com.vinsys.hrms.employee.vo.EducationalDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyDetailsVO;
import com.vinsys.hrms.employee.vo.HealthDetailsVO;
import com.vinsys.hrms.employee.vo.IdentificationDetailsVO;
import com.vinsys.hrms.entity.BankDetails;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateAddress;
import com.vinsys.hrms.entity.CandidateCertification;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.CandidateFamilyDetail;
import com.vinsys.hrms.entity.CandidateHealthReport;
import com.vinsys.hrms.entity.CandidateLetter;
import com.vinsys.hrms.entity.CandidatePassportDetail;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidatePreviousEmployment;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.CandidateQualification;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeBranch;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeCurrentDetail;
import com.vinsys.hrms.entity.EmployeeDepartment;
import com.vinsys.hrms.entity.EmployeeDesignation;
import com.vinsys.hrms.entity.EmployeeDivision;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MapLoginEntityType;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterCandidateChecklistAction;
import com.vinsys.hrms.entity.MasterCity;
import com.vinsys.hrms.entity.MasterCountry;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.entity.MasterTitle;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.entity.HodCycleFinalRating;
import com.vinsys.hrms.kra.entity.HrToHodMap;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraDetails;
import com.vinsys.hrms.kra.entity.KraWf;
import com.vinsys.hrms.kra.vo.EmpIdVo;
import com.vinsys.hrms.master.entity.GradeMaster;
import com.vinsys.hrms.master.entity.MasterGender;
import com.vinsys.hrms.master.entity.MasterMaritialStatus;
import com.vinsys.hrms.master.vo.BranchVO;
import com.vinsys.hrms.master.vo.DepartmentVO;
import com.vinsys.hrms.master.vo.DesignationVO;
import com.vinsys.hrms.master.vo.DivisionVO;
import com.vinsys.hrms.master.vo.EmploymentTypeVO;
import com.vinsys.hrms.master.vo.GenderMasterVO;
import com.vinsys.hrms.master.vo.LoginEntityTypeVO;
import com.vinsys.hrms.master.vo.MasterCityVO;
import com.vinsys.hrms.master.vo.MasterCountryVO;
import com.vinsys.hrms.master.vo.MasterMaritialStatusVo;
import com.vinsys.hrms.master.vo.MasterStateVO;
import com.vinsys.hrms.master.vo.MasterTitleVo;
import com.vinsys.hrms.master.vo.ReportingOfficerVO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.EChecklistItems;
import com.vinsys.hrms.util.EProfileStatus;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;
import com.vinsys.hrms.util.LogConstants;
import com.vinsys.hrms.util.ResponseCode;

public abstract class AbstractDirectOnBoardingProcessor implements IDirectOnboardProcessor {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private DirectOnBoardingDependencies processorDependencies;

	protected AbstractDirectOnBoardingProcessor(DirectOnBoardingDependencies personaleDetailsDependencies) {
		this.processorDependencies = personaleDetailsDependencies;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public HRMSBaseResponse<?> addProfileDetails(ProfileDetailsRequestVO request)
			throws HRMSException, ParseException, NoSuchAlgorithmException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "add Employee profile details");
		}
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("addProfileDetails", role)) {

			if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getOfficialEmailId())) {

				processorDependencies.directOnboardAuthorityHelper.profileDetailsInputValidation(request);
				checkCandidateEmailIfAlredyExist(request);
				addEmployeeDetails(request);

				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1766));
				response.setApplicationVersion(processorDependencies.applicationVersion);
				response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
				if (log.isInfoEnabled()) {
					log.info(LogConstants.EXITED.template(), "add profile details");
				}
				return response;

			} else
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private ProfileDetailsResponseVO setProfileResponseVO(Candidate candidate) {

		ProfileDetailsResponseVO responseVO = new ProfileDetailsResponseVO();
		CandidatePersonalDetail candp = processorDependencies.PersonalDetailDAO.findBycandidate(candidate);
		CandidateProfessionalDetail candprof = processorDependencies.professionalDAO.findBycandidate(candidate);
		CandidatePassportDetail passport = processorDependencies.passportDetailsDAO
				.findBycandidatePersonalDetail(candp);
		responseVO.setSalutation(convertToTitleMasterVO(candidate));
		responseVO.setId(candidate.getId());
		responseVO.setFirstName(candidate.getFirstName());
		responseVO.setMiddleName(candidate.getMiddleName());
		responseVO.setLastName(candidate.getLastName());
		responseVO.setGender(convertToGenderMasterVO(candidate));
		responseVO.setMaritalStatus(convertToMaritalStatusMasterVO(candp));
		responseVO
				.setDateOfBirth(HRMSDateUtil.format(candidate.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		responseVO.setEmailId(candidate.getEmailId());
		responseVO.setAdharCardNumber(candprof.getAadhaarCard());
		responseVO.setPanCardNumber(candprof.getAadhaarCard());
		responseVO.setDrivingLicenseNumber(candp.getDrivingLicense());
		responseVO.setPassportNumber(passport.getPassportNumber());
		responseVO.setSpouceName(candp.getSpouseName());
		responseVO.setOrgId(candidate.getOrgId());

		return responseVO;
	}

	public MasterTitleVo convertToTitleMasterVO(Candidate candidate) {
		MasterTitle titleMaster = processorDependencies.titleDAO.findByTitleAndIsActive(candidate.getTitle(),
				IHRMSConstants.isActive);
		MasterTitleVo titleVo = new MasterTitleVo();
		if (!HRMSHelper.isNullOrEmpty(titleMaster)) {
			titleVo.setId(titleMaster.getId());
			titleVo.setTitle(titleMaster.getTitle());
		}
		return titleVo;
	}

	public MasterCityVO convertToCityMasterVO(Long id) {
		MasterCityVO cityVo = new MasterCityVO();
		MasterCity city = processorDependencies.cityDAO.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(cityVo)) {
			cityVo.setId(city.getId());
			cityVo.setCityName(city.getCityName());
		}
		return cityVo;
	}

	public MasterStateVO convertToStateMasterVO(Long id) {
		MasterStateVO stateVo = new MasterStateVO();
		MasterState state = processorDependencies.stateDAO.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(stateVo)) {
			stateVo.setId(state.getId());
			stateVo.setStateName(state.getStateName());
		}
		return stateVo;
	}

	public DivisionVO convertToDivisionMasterVO(Long id) {
		DivisionVO divisionVo = new DivisionVO();
		MasterDivision division = processorDependencies.divisionDAO.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(divisionVo)) {
			divisionVo.setId(division.getId());
			divisionVo.setDivisionName(division.getDivisionName());
		}
		return divisionVo;
	}

	public static Set<VOLoginEntityType> convertToSetLoginEntityTypeModel(Set<LoginEntityType> entity) {

		Set<VOLoginEntityType> model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new HashSet<VOLoginEntityType>();
			for (LoginEntityType loginEntityType : entity) {
				if (!HRMSHelper.isNullOrEmpty(loginEntityType)) {
					VOLoginEntityType voLoginEntityType = new VOLoginEntityType();
					voLoginEntityType.setId(loginEntityType.getId());
					voLoginEntityType.setLoginEntityTypeName(loginEntityType.getLoginEntityTypeName());
					model.add(voLoginEntityType);
				}
			}
		}
		return model;
	}

	public BranchVO convertToBranchMasterVO(Long id) {
		BranchVO branchVO = new BranchVO();
		MasterBranch branch = processorDependencies.branchDAO.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(branchVO)) {
			branchVO.setId(branch.getId());
			branchVO.setBranchName(branch.getBranchName());
		}
		return branchVO;
	}

	public ReportingOfficerVO convertToreportingManagerVo(Long id) {
		ReportingOfficerVO reportVo = new ReportingOfficerVO();
		Employee emp = processorDependencies.employeeDAO.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(reportVo)) {
			reportVo.setId(emp.getId());
			reportVo.setReportingOfficerName(emp.getCandidate().getLoginEntity().getLoginEntityName());
		}
		return reportVo;
	}

	public DepartmentVO convertToDepartmentMasterVO(Long id) {
		DepartmentVO departmentVO = new DepartmentVO();
		MasterDepartment department = processorDependencies.departmentDAO.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(departmentVO)) {
			departmentVO.setId(department.getId());
			departmentVO.setDepartmentName(department.getDepartmentName());
		}
		return departmentVO;
	}

	public MasterCountryVO convertToCountryMasterVO(Long id) {
		MasterCountryVO countryVo = new MasterCountryVO();
		MasterCountry country = processorDependencies.countryDAO.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(countryVo)) {
			countryVo.setId(country.getId());
			countryVo.setCountryName(country.getCountryName());
		}
		return countryVo;
	}

	public EmploymentTypeVO convertToEmploymentTypeMasterVO(Long id) {
		EmploymentTypeVO employmentTypeVo = new EmploymentTypeVO();
		MasterEmploymentType employment = processorDependencies.masterEmploymentTypeDAO.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(employmentTypeVo)) {
			employmentTypeVo.setId(employment.getId());
			employmentTypeVo.setEmploymentTypeName(employment.getEmploymentTypeName());
		}
		return employmentTypeVo;
	}

	public DesignationVO convertToMasterDesignationVO(Long id) {
		DesignationVO designationVo = new DesignationVO();
		MasterDesignation designation = processorDependencies.designationDAO.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(designationVo)) {
			designationVo.setId(designation.getId());
			designationVo.setDesignationName(designation.getDesignationName());
		}
		return designationVo;
	}

	public GenderMasterVO convertToGenderMasterVO(Candidate candidate) {
		GenderMasterVO genderVo = new GenderMasterVO();
		MasterGender genderMaster = processorDependencies.genderDAO.findByGender(candidate.getGender());
		if (!HRMSHelper.isNullOrEmpty(genderMaster)) {
			genderVo.setId(genderMaster.getId());
			genderVo.setGender(genderMaster.getGender());
		}
		return genderVo;
	}

	public MasterMaritialStatusVo convertToMaritalStatusMasterVO(CandidatePersonalDetail candidate) {
		MasterMaritialStatusVo maritalVo = new MasterMaritialStatusVo();
		MasterMaritialStatus maritleStatus = processorDependencies.maritalStatusDao
				.findByMaritalStatusAndIsActive(candidate.getMaritalStatus(), IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(maritleStatus)) {
			maritalVo.setId(maritleStatus.getId());
			maritalVo.setMaritalStatus(maritleStatus.getMaritalStatus());
		}
		return maritalVo;
	}

	private Candidate addEmployeeDetails(ProfileDetailsRequestVO request)
			throws HRMSException, NoSuchAlgorithmException {
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		checkEmployeeEmailCodeIfAlredyExist(request);
		Candidate candidateNew = setCandidateAndLoginEntityDetails(request, empId);
		CandidateProfessionalDetail candProfDetails = setCandidateProfessionalDetails(request, candidateNew, empId);
		setCandidatePersonalDetails(request, candidateNew, empId);
		setCandidateAddress(request, empId, candidateNew, candProfDetails);

		Employee employee = addEmployeeAndCurrentDetails(request, candidateNew);
		setEmployeeDepartmentMpping(candProfDetails, employee);
		setEmployeeDesignationMapping(candProfDetails, employee);
		setEmployeeBranchMapping(candProfDetails, employee);
		setEmployeeDivisionMapping(candProfDetails, employee);
		addOrUpdateMcMemberKpiVisibility(request);
		return candidateNew;

	}

	private void setCandidateAddress(ProfileDetailsRequestVO request, Long empId, Candidate candidateNew,
			CandidateProfessionalDetail candProfDetails) {
		CandidateAddress address = new CandidateAddress();
		address.setCreatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		address.setCreatedDate(new Date());
		address.setIsActive(IHRMSConstants.isActive);

		address.setCandidate(candidateNew);
		address.setCity(!HRMSHelper.isNullOrEmpty(candProfDetails.getCity()) ? candProfDetails.getCity() : null);
		address.setState(!HRMSHelper.isNullOrEmpty(candProfDetails.getStatus()) ? candProfDetails.getState() : null);
		address.setCountry(
				!HRMSHelper.isNullOrEmpty(candProfDetails.getCountry()) ? candProfDetails.getCountry() : null);
		address.setSsnNumber((request.getSocialSecurityNo() != null && !request.getSocialSecurityNo().isEmpty())
				? Long.parseLong(request.getSocialSecurityNo())
				: null);
		processorDependencies.addressDao.save(address);
	}

	private void updateCandidateAddress(ProfileDetailsRequestVO request, Long empId, Candidate candidateNew,
			CandidateProfessionalDetail candProfDetails) {

		if (request == null || candidateNew == null || candidateNew.getId() == null || candProfDetails == null) {
			log.warn("Invalid input(s) for updating candidate address. Skipping update.");
			return;
		}

		// Fetch existing CandidateAddress for the candidate
		Optional<CandidateAddress> optionalAddress = processorDependencies.addressDao
				.findByCandidateId(candidateNew.getId());

		if (optionalAddress.isPresent()) {
			CandidateAddress address = optionalAddress.get();

			// Update fields only if input is valid
			address.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
			address.setUpdatedDate(new Date());
			address.setIsActive(IHRMSConstants.isActive);
			address.setCandidate(candidateNew);

			if (!HRMSHelper.isNullOrEmpty(candProfDetails.getCity())) {
				address.setCity(candProfDetails.getCity());
			}

			if (!HRMSHelper.isNullOrEmpty(candProfDetails.getState())) {
				address.setState(candProfDetails.getState());
			}

			if (!HRMSHelper.isNullOrEmpty(candProfDetails.getCountry())) {
				address.setCountry(candProfDetails.getCountry());
			}

			if (!HRMSHelper.isNullOrEmpty(request.getSocialSecurityNo())) {
				try {
					address.setSsnNumber(Long.parseLong(request.getSocialSecurityNo()));
				} catch (NumberFormatException e) {
					log.warn("Invalid social security number format: {}", request.getSocialSecurityNo());
					address.setSsnNumber(null);
				}
			}

			// Save updated address
			processorDependencies.addressDao.save(address);
			log.info("Candidate address updated for candidateId: {}", candidateNew.getId());
		} else {
			log.warn("No address record found for candidateId: {}. Skipping update.", candidateNew.getId());
		}
	}

	private Candidate setCandidateAndLoginEntityDetails(ProfileDetailsRequestVO request, Long empId)
			throws HRMSException {
		Candidate candidateNew = new Candidate();
		setCandidateDetails(request, candidateNew);
		LoginEntity loginEnity = setCandidateLoginDetails(request, empId);
		candidateNew.setCandidateStatus(IHRMSConstants.CandidateStatus_CREATED);
		candidateNew.setLoginEntity(loginEnity);
		Organization organization = processorDependencies.organizationDAO
				.findById(SecurityFilter.TL_CLAIMS.get().getOrgId()).get();
		candidateNew.getLoginEntity().setOrganization(organization);
		candidateNew.setOrgId(organization.getId());
		processorDependencies.candidateDAO.save(candidateNew);
		return candidateNew;
	}

	private LoginEntity setCandidateLoginDetails(ProfileDetailsRequestVO request, Long empId) throws HRMSException {
		LoginEntity loginEnity = new LoginEntity();
		loginEnity.setIsFirstLogin(IHRMSConstants.IsFirstLogin);

		String fullName = Stream.of(request.getFirstName(), request.getMiddleName(), request.getLastName())
				.filter(s -> s != null && !s.trim().isEmpty()).collect(Collectors.joining(" "));
		loginEnity.setLoginEntityName(fullName);

		loginEnity.setUsername(request.getOfficialEmailId());
		loginEnity.setPrimaryEmail(request.getEmailId());
		loginEnity.setPassword(HRMSHelper.randomString());
		loginEnity.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		loginEnity.setCreatedDate(new Date());
		loginEnity.setCreatedBy(empId.toString());
		loginEnity.setIsActive(IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(request.getRoles())) {
			Set<LoginEntityType> loginEntityTypes = new HashSet<>();
			for (LoginEntityTypeVO voLoginEntityType : request.getRoles()) {
				if (!HRMSHelper.isLongZero(voLoginEntityType.getId())
						&& !HRMSHelper.isNullOrEmpty(voLoginEntityType.getId())) {
					LoginEntityType loginEntityType = processorDependencies.loginTypeDAO
							.findById(voLoginEntityType.getId()).orElseThrow(
									() -> new HRMSException(1502, "Invalid Role ID: " + voLoginEntityType.getId()));
					loginEntityTypes.add(loginEntityType);
				} else {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Role Id");
				}
			}
			loginEnity.setLoginEntityTypes(loginEntityTypes);
		}

		processorDependencies.logDao.save(loginEnity);
		return loginEnity;
	}

	private void setEmployeeDivisionMapping(CandidateProfessionalDetail candProfDetails, Employee employee) {
		EmployeeDivision division = new EmployeeDivision();
		division.setCreatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		division.setCreatedDate(new Date());
		division.setDivision(
				!HRMSHelper.isNullOrEmpty(candProfDetails.getDivision()) ? candProfDetails.getDivision() : null);
		division.setEmployee(employee);
		division.setIsActive(IHRMSConstants.isActive);
		division.setOrgId(employee.getOrgId());
		processorDependencies.employeeDivisionDAO.save(division);
	}

	private void setEmployeeBranchMapping(CandidateProfessionalDetail candProfDetails, Employee employee) {
		EmployeeBranch branch = new EmployeeBranch();
		branch.setCreatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		branch.setCreatedDate(new Date());
		branch.setIsActive(IHRMSConstants.isActive);
		branch.setBranch(!HRMSHelper.isNullOrEmpty(candProfDetails.getBranch()) ? candProfDetails.getBranch() : null);
		branch.setEmployee(employee);
		branch.setOrgId(employee.getOrgId());
		processorDependencies.employeeBranchDAO.save(branch);
	}

	private void setEmployeeDesignationMapping(CandidateProfessionalDetail candProfDetails, Employee employee) {
		EmployeeDesignation deisgnation = new EmployeeDesignation();
		deisgnation.setCreatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		deisgnation.setCreatedDate(new Date());
		deisgnation.setDesignation(
				!HRMSHelper.isNullOrEmpty(candProfDetails.getDesignation()) ? candProfDetails.getDesignation() : null);
		deisgnation.setEmployee(employee);
		deisgnation.setIsActive(IHRMSConstants.isActive);
		deisgnation.setOrgId(employee.getOrgId());
		processorDependencies.employeeDesignationDAO.save(deisgnation);
	}

	private void setEmployeeDepartmentMpping(CandidateProfessionalDetail candProfDetails, Employee employee) {
		EmployeeDepartment department = new EmployeeDepartment();
		department.setCreatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		department.setCreatedDate(new Date());
		department.setIsActive(IHRMSConstants.isActive);
		department.setDepartment(
				!HRMSHelper.isNullOrEmpty(candProfDetails.getDepartment()) ? candProfDetails.getDepartment() : null);
		department.setEmployee(employee);
		department.setOrgId(employee.getOrgId());
		processorDependencies.employeeDepartmentDAO.save(department);
	}

	private void setCandidatePassportDetails(ProfileDetailsRequestVO request, Long empId,
			CandidatePersonalDetail candidatePersonalDetail) {
		CandidatePassportDetail passport = processorDependencies.passportDetailsDAO
				.findBycandidatePersonalDetail(candidatePersonalDetail);
		if (!HRMSHelper.isNullOrEmpty(passport)) {

			passport.setUpdatedBy(empId.toString());
			passport.setUpdatedDate(new Date());
			passport.setCandidatePersonalDetail(candidatePersonalDetail);
			passport.setOrgId(candidatePersonalDetail.getCandidate().getLoginEntity().getOrganization().getId());
		} else {
			passport = new CandidatePassportDetail();

			passport.setCreatedBy(empId.toString());
			passport.setCreatedDate(new Date());
			passport.setCandidatePersonalDetail(candidatePersonalDetail);
			passport.setOrgId(candidatePersonalDetail.getCandidate().getLoginEntity().getOrganization().getId());
		}
		processorDependencies.passportDetailsDAO.save(passport);
	}

	private CandidatePersonalDetail setCandidatePersonalDetails(ProfileDetailsRequestVO request, Candidate candidateNew,
			Long empId) {
		CandidatePersonalDetail candidatePersonalDetail = new CandidatePersonalDetail();

		if (!HRMSHelper.isNullOrEmpty(request.getMaritalStatus())
				&& !HRMSHelper.isNullOrEmpty(request.getMaritalStatus())) {
			candidatePersonalDetail.setMaritalStatus(request.getMaritalStatus().getMaritalStatus());
		}
		candidatePersonalDetail
				.setSpouseName(!HRMSHelper.isNullOrEmpty(request.getSpouceName()) ? request.getSpouceName() : null);
		candidatePersonalDetail.setCandidate(candidateNew);
		candidatePersonalDetail.setIsActive(IHRMSConstants.isActive);
		candidatePersonalDetail.setCreatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		candidatePersonalDetail.setCreatedDate(new Date());
		candidatePersonalDetail.setOrgId(candidateNew.getLoginEntity().getOrganization().getId());

		candidatePersonalDetail = processorDependencies.PersonalDetailDAO.save(candidatePersonalDetail);
		return candidatePersonalDetail;
	}

	private CandidatePersonalDetail updateCandidatePersonalDetails(ProfileDetailsRequestVO request,
			Candidate candidateNew, Long empId) {

		if (request == null || candidateNew == null || candidateNew.getId() == null) {
			log.warn("Invalid request or candidate data. Skipping personal details update.");
			return null;
		}

		// Fetch existing CandidatePersonalDetail by Candidate ID
		Optional<CandidatePersonalDetail> optionalDetail = processorDependencies.PersonalDetailDAO
				.findByCandidateId(candidateNew.getId());

		if (optionalDetail.isPresent()) {
			CandidatePersonalDetail candidatePersonalDetail = optionalDetail.get();

			// Update marital status if available
			if (request.getMaritalStatus() != null
					&& !HRMSHelper.isNullOrEmpty(request.getMaritalStatus().getMaritalStatus())) {
				candidatePersonalDetail.setMaritalStatus(request.getMaritalStatus().getMaritalStatus());
			}

			// Update spouse name only if not empty
			if (!HRMSHelper.isNullOrEmpty(request.getSpouceName())) {
				candidatePersonalDetail.setSpouseName(request.getSpouceName());
			}

			candidatePersonalDetail.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
			candidatePersonalDetail.setUpdatedDate(new Date());

			// Save and return updated entity
			return processorDependencies.PersonalDetailDAO.save(candidatePersonalDetail);

		} else {
			log.warn("No existing personal detail record found for candidateId: {}. Skipping update.",
					candidateNew.getId());
			return null;
		}
	}

	private CandidateProfessionalDetail setCandidateProfessionalDetails(ProfileDetailsRequestVO request,
			Candidate candidateNew, Long empId) throws HRMSException {

		GradeMaster grade = processorDependencies.gradeDAO.findById(request.getGrade().getId()).get();

		if (HRMSHelper.isNullOrEmpty(grade)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		CandidateProfessionalDetail professionalDetails = setCandidateProfDetails(request, candidateNew, empId, grade);

		professionalDetails = processorDependencies.professionalDAO.save(professionalDetails);
		return professionalDetails;
	}

	private CandidateProfessionalDetail setCandidateProfDetails(ProfileDetailsRequestVO request, Candidate candidateNew,
			Long empId, GradeMaster grade) {
		CandidateProfessionalDetail professionalDetails;
		professionalDetails = new CandidateProfessionalDetail();

		professionalDetails.setIsActive(IHRMSConstants.isActive);
		professionalDetails.setCreatedBy(empId.toString());
		professionalDetails.setCreatedDate(new Date());
		professionalDetails.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());

		MasterDesignation masterDesignation = processorDependencies.designationDAO
				.findById(request.getDesignation().getId()).get();
		if (!HRMSHelper.isNullOrEmpty(request.getCity()) && !HRMSHelper.isNullOrEmpty(request.getCity().getId())
				&& !HRMSHelper.isLongZero(request.getCity().getId())) {
			MasterCity masterCity = processorDependencies.cityDAO.findById(request.getCity().getId()).get();
			professionalDetails.setCity(!HRMSHelper.isNullOrEmpty(masterCity) ? masterCity : null);
		}
		MasterCountry masterCountry = processorDependencies.countryDAO.findById(request.getCountry().getId()).get();
		if (!HRMSHelper.isNullOrEmpty(request.getState()) && !HRMSHelper.isNullOrEmpty(request.getState().getId())
				&& !HRMSHelper.isLongZero(request.getState().getId())) {
			MasterState masterState = processorDependencies.stateDAO.findById(request.getState().getId()).get();
			professionalDetails.setState(!HRMSHelper.isNullOrEmpty(masterState) ? masterState : null);
		}
		MasterDepartment department = processorDependencies.departmentDAO.findById(request.getDepartment().getId())
				.get();
		MasterBranch branch = processorDependencies.branchDAO.findById(request.getBranch().getId()).get();
		MasterDivision division = processorDependencies.divisionDAO.findById(request.getDivision().getId()).get();

		professionalDetails.setDesignation(!HRMSHelper.isNullOrEmpty(masterDesignation) ? masterDesignation : null);
		professionalDetails.setDepartment(!HRMSHelper.isNullOrEmpty(department) ? department : null);
		professionalDetails.setBranch(!HRMSHelper.isNullOrEmpty(branch) ? branch : null);
		professionalDetails.setDivision(!HRMSHelper.isNullOrEmpty(division) ? division : null);

		professionalDetails.setCountry(!HRMSHelper.isNullOrEmpty(masterCountry) ? masterCountry : null);
		professionalDetails
				.setDateOfJoining(HRMSDateUtil.parse(request.getDateOfJoining(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		professionalDetails.setCandidate(candidateNew);
		professionalDetails.setGrade(grade.getGradeDescription());
		professionalDetails.setGradeId(grade);
		return professionalDetails;
	}

	private CandidateProfessionalDetail updateCandidatePorfDetails(ProfileDetailsRequestVO request,
			Candidate candidateNew, Long empId) {

		CandidateProfessionalDetail professionalDetails = candidateNew.getCandidateProfessionalDetail();

		professionalDetails.setIsActive(IHRMSConstants.isActive);
		professionalDetails.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		professionalDetails.setUpdatedDate(new Date());
		professionalDetails.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		professionalDetails.setCandidate(candidateNew);

		if (request.getDesignation() != null && request.getDesignation().getId() != null) {
			processorDependencies.designationDAO.findById(request.getDesignation().getId()).ifPresentOrElse(
					professionalDetails::setDesignation,
					() -> log.warn("Designation not found for ID: {}", request.getDesignation().getId()));
		} else {
			log.warn("Missing designation or designation ID");
			professionalDetails.setDesignation(null);
		}

		if (request.getCity() != null && request.getCity().getId() > 0) {
			processorDependencies.cityDAO.findById(request.getCity().getId()).ifPresentOrElse(
					professionalDetails::setCity,
					() -> log.warn("City not found for ID: {}", request.getCity().getId()));
		} else {
			log.warn("Missing city or city ID");
			professionalDetails.setCity(null);
		}

		MasterCountryVO countryVO = request.getCountry();

		if (countryVO != null && countryVO.getId() > 0) {
			processorDependencies.countryDAO.findById(countryVO.getId()).ifPresentOrElse(
					professionalDetails::setCountry, () -> log.warn("Country not found for ID: {}", countryVO.getId()));
		} else {
			log.warn("Missing country or country ID in request");
			professionalDetails.setCountry(null);
		}

		if (request.getState() != null && request.getState().getId() != null) {
			processorDependencies.stateDAO.findById(request.getState().getId()).ifPresentOrElse(
					professionalDetails::setState,
					() -> log.warn("State not found for ID: {}", request.getState().getId()));
		} else {
			log.warn("Missing state or state ID");
			professionalDetails.setState(null);
		}

		if (request.getDepartment() != null && request.getDepartment().getId() != null) {
			processorDependencies.departmentDAO.findById(request.getDepartment().getId()).ifPresentOrElse(
					professionalDetails::setDepartment,
					() -> log.warn("Department not found for ID: {}", request.getDepartment().getId()));
		} else {
			log.warn("Missing department or department ID");
			professionalDetails.setDepartment(null);
		}

		if (request.getBranch() != null && request.getBranch().getId() != null) {
			processorDependencies.branchDAO.findById(request.getBranch().getId()).ifPresentOrElse(
					professionalDetails::setBranch,
					() -> log.warn("Branch not found for ID: {}", request.getBranch().getId()));
		} else {
			log.warn("Missing branch or branch ID");
			professionalDetails.setBranch(null);
		}

		if (request.getDivision() != null && request.getDivision().getId() != null) {
			processorDependencies.divisionDAO.findById(request.getDivision().getId()).ifPresentOrElse(
					professionalDetails::setDivision,
					() -> log.warn("Division not found for ID: {}", request.getDivision().getId()));
		} else {
			log.warn("Missing division or division ID");
			professionalDetails.setDivision(null);
		}

		if (!HRMSHelper.isNullOrEmpty(request.getDateOfJoining())) {
			professionalDetails.setDateOfJoining(
					HRMSDateUtil.parse(request.getDateOfJoining(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		} else {
			log.warn("Missing date of joining");
			professionalDetails.setDateOfJoining(null);
		}

		if (!HRMSHelper.isNullOrEmpty(request.getGrade())) {
			professionalDetails.setGrade(request.getGrade().getGradeDescription());
		}

		if (!HRMSHelper.isNullOrEmpty(request.getGrade())) {
			GradeMaster gradeId = processorDependencies.gradeDAO.findByIdAndIsActive(request.getGrade().getId(),
					ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(gradeId)) {
				professionalDetails.setGradeId(gradeId);
			}
		}

		return professionalDetails;
	}

	/*
	 * private LoginEntity setCandidateLoginDetails(ProfileDetailsRequestVO request,
	 * Long empId) throws HRMSException { LoginEntity loginEnity = new
	 * LoginEntity(); loginEnity.setIsFirstLogin(IHRMSConstants.IsFirstLogin);
	 * loginEnity.setLoginEntityName( request.getFirstName() + " " +
	 * request.getMiddleName() + " " + request.getLastName());
	 * loginEnity.setUsername(request.getOfficialEmailId());
	 * loginEnity.setPrimaryEmail(request.getEmailId());
	 * loginEnity.setPassword(HRMSHelper.randomString());
	 * loginEnity.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
	 * loginEnity.setCreatedDate(new Date());
	 * loginEnity.setCreatedBy(empId.toString());
	 * loginEnity.setIsActive(IHRMSConstants.isActive);
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(request.getRoles())) { Set<LoginEntityType>
	 * loginEntityTypes = new HashSet<LoginEntityType>(); for (LoginEntityTypeVO
	 * voLoginEntityType : request.getRoles()) { if
	 * (!HRMSHelper.isLongZero(voLoginEntityType.getId()) &&
	 * !HRMSHelper.isNullOrEmpty(voLoginEntityType.getId())) { LoginEntityType
	 * loginEntityType = processorDependencies.loginTypeDAO
	 * .findById(voLoginEntityType.getId()).get();
	 * loginEntityTypes.add(loginEntityType); } else { throw new HRMSException(1501,
	 * ResponseCode.getResponseCodeMap().get(1501) + " Role Id"); } }
	 * 
	 * loginEnity.setLoginEntityTypes(loginEntityTypes); }
	 * 
	 * // } processorDependencies.logDao.save(loginEnity); return loginEnity; }
	 */

	private LoginEntity updateCandidateLoginDetails(ProfileDetailsRequestVO request, Long empId) throws HRMSException {
		if (empId == null) {
			throw new HRMSException(1500, "Employee ID is required for login update.");
		}

		Employee cand = processorDependencies.employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), empId);
		if (cand == null || cand.getCandidate() == null || cand.getCandidate().getLoginEntity() == null) {
			throw new HRMSException(1500, "Invalid candidate or login entity for employee ID: " + empId);
		}

		LoginEntity loginEntity = processorDependencies.logDao
				.findByLoginId(cand.getCandidate().getLoginEntity().getId());
		if (loginEntity == null) {
			throw new HRMSException(1500, "Login entity not found for employee ID: " + empId);
		}

		// Set full name
		String fullName = Stream.of(request.getFirstName(), request.getMiddleName(), request.getLastName())
				.filter(s -> s != null && !s.trim().isEmpty()).collect(Collectors.joining(" "));
		loginEntity.setLoginEntityName(fullName);

		loginEntity.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		loginEntity.setUpdatedDate(new Date());
		loginEntity.setUpdatedBy(empId.toString());

		if (!HRMSHelper.isNullOrEmpty(request.getRoles())) {
			Set<LoginEntityType> loginEntityTypes = new HashSet<>();
			for (LoginEntityTypeVO voLoginEntityType : request.getRoles()) {
				if (!HRMSHelper.isLongZero(voLoginEntityType.getId())) {
					LoginEntityType loginEntityType = processorDependencies.loginTypeDAO
							.findById(voLoginEntityType.getId()).orElseThrow(
									() -> new HRMSException(1502, "Invalid Role ID: " + voLoginEntityType.getId()));

					loginEntityType.getLoginEntities().add(loginEntity);
					loginEntityTypes.add(loginEntityType);
				} else {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Role Id");
				}
			}

			loginEntity.setLoginEntityTypes(loginEntityTypes);
		}

		processorDependencies.logDao.save(loginEntity);
		return loginEntity;
	}

	private void setCandidateDetails(ProfileDetailsRequestVO request, Candidate candidateNew) {

		String loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString();
		Date currentDate = new Date();

		candidateNew.setCreatedBy(loggedInEmpId);
		candidateNew.setCreatedDate(currentDate);
		candidateNew.setUpdatedBy(loggedInEmpId);
		candidateNew.setUpdatedDate(currentDate);
		candidateNew.setIsActive(IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(request.getDateOfBirth())) {
			candidateNew
					.setDateOfBirth(HRMSDateUtil.parse(request.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		}

		if (!HRMSHelper.isNullOrEmpty(request.getEmailId())) {
			candidateNew.setEmailId(request.getEmailId());
		}

		if (!HRMSHelper.isNullOrEmpty(request.getFirstName())) {
			candidateNew.setFirstName(request.getFirstName());
		}

		if (request.getGender() != null && !HRMSHelper.isNullOrEmpty(request.getGender().getGender())) {
			candidateNew.setGender(request.getGender().getGender());
		}

		if (!HRMSHelper.isNullOrEmpty(request.getLastName())) {
			candidateNew.setLastName(request.getLastName());
		}

		if (!HRMSHelper.isNullOrEmpty(request.getMiddleName())) {
			candidateNew.setMiddleName(request.getMiddleName());
		}

		if (!HRMSHelper.isNullOrEmpty(request.getMobileNumber())) {
			candidateNew.setMobileNumber(request.getMobileNumber());
		}

		if (request.getSalutation() != null && !HRMSHelper.isNullOrEmpty(request.getSalutation().getTitle())) {
			candidateNew.setTitle(request.getSalutation().getTitle());
		}

		Long salutationId = request.getSalutation().getId();
		if (!HRMSHelper.isNullOrEmpty(salutationId)) {
			MasterTitle salutation = processorDependencies.titleDAO.findByIdAndIsActive(salutationId,
					ERecordStatus.Y.name());
			candidateNew.setTitleId(salutation);
		}

		if (request.getEmploymentType() != null && request.getEmploymentType().getId() != null) {
			Optional<MasterEmploymentType> optionalEmploymentType = processorDependencies.masterEmploymentTypeDAO
					.findById(request.getEmploymentType().getId());

			optionalEmploymentType.ifPresent(candidateNew::setEmploymentType);
		}
	}

	private void updateCandidateDetails(ProfileDetailsRequestVO request, Candidate candidateNew) {

		String loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString();
		Date currentDate = new Date();

		candidateNew.setUpdatedBy(loggedInEmpId);
		candidateNew.setUpdatedDate(currentDate);
		candidateNew.setIsActive(IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(request.getDateOfBirth())) {
			candidateNew
					.setDateOfBirth(HRMSDateUtil.parse(request.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		}

		if (!HRMSHelper.isNullOrEmpty(request.getEmailId())) {
			candidateNew.setEmailId(request.getEmailId());
		}

		if (!HRMSHelper.isNullOrEmpty(request.getFirstName())) {
			candidateNew.setFirstName(request.getFirstName());
		}

		if (request.getGender() != null && !HRMSHelper.isNullOrEmpty(request.getGender().getGender())) {
			candidateNew.setGender(request.getGender().getGender());
		}

		if (!HRMSHelper.isNullOrEmpty(request.getLastName())) {
			candidateNew.setLastName(request.getLastName());
		}

		if (!HRMSHelper.isNullOrEmpty(request.getMiddleName())) {
			candidateNew.setMiddleName(request.getMiddleName());
		}

		if (!HRMSHelper.isNullOrEmpty(request.getMobileNumber())) {
			candidateNew.setMobileNumber(request.getMobileNumber());
		}

		if (request.getSalutation() != null && !HRMSHelper.isNullOrEmpty(request.getSalutation().getTitle())) {
			candidateNew.setTitle(request.getSalutation().getTitle());
		}

		if (request.getSalutation() != null && !HRMSHelper.isNullOrEmpty(request.getSalutation().getTitle())) {

			MasterTitle salutatonId = processorDependencies.titleDAO
					.findByIdAndIsActive(request.getSalutation().getId(), ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(salutatonId)) {
				candidateNew.setTitleId(salutatonId);
			}
		}

		if (request.getEmploymentType() != null && request.getEmploymentType().getId() != null) {
			Optional<MasterEmploymentType> optionalEmploymentType = processorDependencies.masterEmploymentTypeDAO
					.findById(request.getEmploymentType().getId());

			optionalEmploymentType.ifPresent(candidateNew::setEmploymentType);
		}
	}

	private Candidate addOrUpdateCandidate(ProfileDetailsRequestVO request) {
		Candidate candidateNew;

		candidateNew = new Candidate();

		return candidateNew;
	}

	private void checkCandidateEmailIfAlredyExist(ProfileDetailsRequestVO request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request.getEmailId())) {
			Candidate candidate = processorDependencies.candidateDAO.findByemailIdAndIsActive(request.getEmailId(),
					IHRMSConstants.isNotActive);
			if (!HRMSHelper.isNullOrEmpty(candidate)) {
				candidate.setEmailId(candidate.getEmailId() + "_old");
				processorDependencies.candidateDAO.save(candidate);
			}

			Candidate cand = processorDependencies.candidateDAO.findByemailIdAndIsActive(request.getEmailId(),
					IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(cand)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1615));
			}
		}

	}

	private void checkCandidateMailIfAlreadyExist(ProfileDetailsRequestVO request) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(request.getEmailId())) {

			Candidate inactiveCandidate = processorDependencies.candidateDAO
					.findByemailIdAndIsActive(request.getEmailId(), IHRMSConstants.isNotActive);

			if (!HRMSHelper.isNullOrEmpty(inactiveCandidate)) {
				inactiveCandidate.setEmailId(inactiveCandidate.getEmailId() + "_old");
				processorDependencies.candidateDAO.save(inactiveCandidate);
			}

			Candidate activeCandidate = processorDependencies.candidateDAO
					.findByemailIdAndIsActive(request.getEmailId(), IHRMSConstants.isActive);

			Long requestCandidateId = null;
			if (!HRMSHelper.isNullOrEmpty(request.getId())) {
				Employee emp = processorDependencies.employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(),
						request.getId());
				if (emp != null && emp.getCandidate() != null) {
					requestCandidateId = emp.getCandidate().getId();
				}
			}

			if (!HRMSHelper.isNullOrEmpty(activeCandidate)) {
				if (HRMSHelper.isNullOrEmpty(requestCandidateId)) {

					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1615));
				} else {

					if (!activeCandidate.getId().equals(requestCandidateId)) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1615));
					}
				}
			}
		}
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public HRMSBaseResponse<?> addPreviousEmployment(PreviousEmploymentRequestVO request)
			throws HRMSException, ParseException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "add previous employment details");
		}
		HRMSBaseResponse<PreviousEmploymentResponseVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("setPreviousEmploymentDetails",
				role)) {
			if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getCandidateId())) {
				processorDependencies.directOnboardAuthorityHelper.previousEmploymentInputValidation(request);
				CandidatePreviousEmployment previousEmployment = setPreviousEmploymentDetails(request);
				PreviousEmploymentResponseVO responseVo = setPreviousEmploymentResponseVO(previousEmployment, request);
				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setApplicationVersion(processorDependencies.applicationVersion);
				response.setResponseBody(responseVo);
				if (log.isInfoEnabled()) {
					log.info(LogConstants.EXITED.template(), "add previous employment details");
				}
				return response;

			} else
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private CandidatePreviousEmployment setPreviousEmploymentDetails(PreviousEmploymentRequestVO request)
			throws HRMSException {
		CandidatePreviousEmployment previousEmployment = null;
		previousEmployment = addOrUpdatePreviousEmployment(request);
		Candidate cand = processorDependencies.candidateDAO.findByIdAndIsActive(request.getCandidateId(),
				IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(cand)) {
			setCandidatePreviousEmployment(request, previousEmployment, cand);
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		return previousEmployment;

	}

	private void setCandidatePreviousEmployment(PreviousEmploymentRequestVO request,
			CandidatePreviousEmployment previousEmployment, Candidate cand) {
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		previousEmployment.setCreatedBy(empId.toString());
		previousEmployment.setCreatedDate(new Date());
		previousEmployment.setIsActive(IHRMSConstants.isActive);
		previousEmployment.setCandidateProfessionalDetail(cand.getCandidateProfessionalDetail());
		previousEmployment.setCompanyName(request.getCompanyName());
		previousEmployment.setCompanyAddress(request.getCompanyAddress());
		MasterDesignation masterDesignation = processorDependencies.designationDAO
				.findById(request.getDesignation().getId()).get();
		MasterCity masterCity = processorDependencies.cityDAO.findById(request.getCity().getId()).get();
		MasterCountry masterCountry = processorDependencies.countryDAO.findById(request.getCountry().getId()).get();
		MasterState masterState = processorDependencies.stateDAO.findById(request.getState().getId()).get();
		MasterEmploymentType employeeType = processorDependencies.masterEmploymentTypeDAO
				.findById(request.getEmployeeType().getId()).get();
		if (!HRMSHelper.isNullOrEmpty(employeeType)) {
			previousEmployment.setEmployeeType(employeeType.getEmploymentTypeName());
		}
		if (!HRMSHelper.isNullOrEmpty(masterCity)) {
			previousEmployment.setCity(masterCity);
		}
		if (!HRMSHelper.isNullOrEmpty(masterState)) {
			previousEmployment.setState(masterState);
		}
		if (!HRMSHelper.isNullOrEmpty(masterCountry)) {
			previousEmployment.setCountry(masterCountry);
		}
		if (!HRMSHelper.isNullOrEmpty(masterDesignation)) {
			previousEmployment.setDesignation(masterDesignation.getDesignationName());
		}
		previousEmployment.setExperience(request.getTotalExperience());
		previousEmployment
				.setFromDate(HRMSDateUtil.parse(request.getWorkFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		previousEmployment.setToDate(HRMSDateUtil.parse(request.getWorkToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		previousEmployment.setReasonForleaving(request.getReasonForleaving());
		previousEmployment.setOrgId(cand.getLoginEntity().getOrganization().getId());
		previousEmployment.setPreviousManager(request.getContactPersonName());
		previousEmployment.setPreviousManagerContactNumber(request.getContactPersonMobileNumber());
		previousEmployment.setPreviousManagerEmail(request.getContactPersonEmail());
		previousEmployment.setPincode(request.getPincode());
		previousEmployment.setContactPersonDesignation(request.getContactPersonDesignation());
		processorDependencies.previousEmploymentDAO.save(previousEmployment);
	}

	private CandidatePreviousEmployment addOrUpdatePreviousEmployment(PreviousEmploymentRequestVO request) {
		CandidatePreviousEmployment previousEmployment;
		if (!HRMSHelper.isNullOrEmpty(request.getId())) {
			previousEmployment = processorDependencies.previousEmploymentDAO.findByIdAndIsActive(request.getId(),
					IHRMSConstants.isActive);

			if (!HRMSHelper.isNullOrEmpty(previousEmployment)) {

				previousEmployment.setUpdatedDate(new Date());
			} else {
				previousEmployment = new CandidatePreviousEmployment();
			}

		} else {
			previousEmployment = new CandidatePreviousEmployment();
		}
		return previousEmployment;
	}

	private PreviousEmploymentResponseVO setPreviousEmploymentResponseVO(
			CandidatePreviousEmployment candidatePreviousEmployment, PreviousEmploymentRequestVO request) {

		PreviousEmploymentResponseVO responseVO = new PreviousEmploymentResponseVO();

		responseVO.setId(candidatePreviousEmployment.getId());
		responseVO.setCity(convertToCityMasterVO(candidatePreviousEmployment.getCity().getId()));
		responseVO.setState(convertToStateMasterVO(candidatePreviousEmployment.getState().getId()));
		responseVO.setCountry(convertToCountryMasterVO(candidatePreviousEmployment.getCountry().getId()));
		responseVO.setDesignation(convertToMasterDesignationVO(request.getDesignation().getId()));
		responseVO.setCompanyName(candidatePreviousEmployment.getCompanyName());
		responseVO.setCompanyAddress(candidatePreviousEmployment.getCompanyAddress());
		responseVO.setEmployeeType(convertToEmploymentTypeMasterVO(request.getEmployeeType().getId()));
		responseVO.setWorkFromDate(
				HRMSDateUtil.format(candidatePreviousEmployment.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		responseVO.setWorkToDate(
				HRMSDateUtil.format(candidatePreviousEmployment.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		responseVO.setContatPersonName(candidatePreviousEmployment.getPreviousManager());
		responseVO.setContactPersonEmail(candidatePreviousEmployment.getPreviousManagerEmail());
		responseVO.setContactPersonDesignation(candidatePreviousEmployment.getContactPersonDesignation());
		responseVO.setContactPersonMobileNumber(candidatePreviousEmployment.getPreviousManagerContactNumber());
		responseVO.setTotalExperience(candidatePreviousEmployment.getExperience());
		responseVO.setPincode(candidatePreviousEmployment.getPincode());
		responseVO.setReasonForleaving(candidatePreviousEmployment.getReasonForleaving());
		responseVO.setCandidateId(candidatePreviousEmployment.getCandidateProfessionalDetail().getCandidate().getId());
		responseVO.setIsOverseas(candidatePreviousEmployment.getOverseas());
		responseVO.setOrgId(candidatePreviousEmployment.getOrgId());

		return responseVO;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public HRMSBaseResponse<?> addCurrentEmployment(CurrentEmploymentRequestVO request)
			throws HRMSException, ParseException, NoSuchAlgorithmException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "add current details");
		}
		HRMSBaseResponse<CurrentEmploymentResponseVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("addCurrentDetails", role)) {
			if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getOfficialEmailId())) {

				processorDependencies.directOnboardAuthorityHelper.currentDetailsInputValidation(request);
				// updateEmployeeEmailIfAlredyExist(request);
				// Employee employee = addCurrentDetails(request);

				// CurrentEmploymentResponseVO responseVo =
				// setCurrentEmploymentResponseVO(employee, request);
				// response = getBaseResponse(ResponseCode.getResponseCodeMap().get(1200), 1200,
				// responseVo, 0);

				if (log.isInfoEnabled()) {
					log.info(LogConstants.EXITED.template(), "add current details");
				}
				return response;

			} else
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private void creditCasualLeaveToNewEmployee(CurrentEmploymentRequestVO request, Employee emp) {
		Candidate candidate = processorDependencies.candidateDAO.findByIdAndIsActive(request.getCandidateId(),
				IHRMSConstants.isActive);
		Organization org = processorDependencies.organizationDAO.findByOrgId(candidate.getOrgId());
		MasterLeaveType mstLeaveType = processorDependencies.mstLeaveTypeDAO
				.findByIsActiveAndOrganizationAndLeaveTypeCode(IHRMSConstants.isActive, org,
						IHRMSConstants.LEAVE_TYPE_CODE_CASUAL_lEAVE);
		EmployeeLeaveDetail currentYearLeaveDetail = processorDependencies.empLeaveDetailsDAO
				.findEmployeeLeaveByEIDYEARAndOrgId(emp.getId(), mstLeaveType.getId(), Year.now().getValue(),
						emp.getOrgId());

		if (HRMSHelper.isNullOrEmpty(currentYearLeaveDetail)) {
			Date dateOfJoining = candidate.getCandidateProfessionalDetail().getDateOfJoining();
			Calendar calToDate = Calendar.getInstance();
			calToDate.set(Year.now().getValue(), 11, 31);

			Float clCount = calculateInitialLeaves(dateOfJoining);

			currentYearLeaveDetail = new EmployeeLeaveDetail();
			currentYearLeaveDetail.setCreatedBy(ERole.HR.name());
			currentYearLeaveDetail.setCreatedDate(new Date());
			currentYearLeaveDetail.setLeaveAvailable(clCount);
			currentYearLeaveDetail.setLeaveEarned(clCount);
			currentYearLeaveDetail.setMasterLeaveType(mstLeaveType);
			currentYearLeaveDetail.setTotalEligibility(clCount);
			currentYearLeaveDetail.setYear(Year.now().getValue());
			currentYearLeaveDetail.setEmployee(emp);
			currentYearLeaveDetail.setOrgId(emp.getOrgId());
			currentYearLeaveDetail.setIsActive(IHRMSConstants.isActive);
			processorDependencies.employeeLeaveDetailDAO.save(currentYearLeaveDetail);

			EmployeeCreditLeaveDetail empCreditLeave = new EmployeeCreditLeaveDetail();
			empCreditLeave.setCreditedBy(ERole.HR.name());
			empCreditLeave.setFromDate(new Date());
			empCreditLeave.setNoOfDays(clCount);
			empCreditLeave.setToDate(calToDate.getTime());
			empCreditLeave.setPostedDate(new Date());
			empCreditLeave.setEmployee(emp);
			empCreditLeave.setMasterLeaveType(mstLeaveType);
			empCreditLeave.setCreatedDate(new Date());
			empCreditLeave.setOrgId(emp.getOrgId());
			empCreditLeave.setIsActive(IHRMSConstants.isActive);
			processorDependencies.employeeCreditLeaveDAO.save(empCreditLeave);
			log.info(clCount + "leaves creadited to the employee  ");
		}

		log.info("leaves already creadited to the employee  ");
	}

	private Float calculateInitialLeaves(Date dateOfJoining) {
		Calendar joiningDate = Calendar.getInstance();
		joiningDate.setTime(dateOfJoining);

		int dayOfMonth = joiningDate.get(Calendar.DAY_OF_MONTH);
		int month = joiningDate.get(Calendar.MONTH);

		Float initialLeaves;
		if (dayOfMonth <= 15) {
			initialLeaves = 6f - (month % 6);
		} else {
			initialLeaves = 5f - (month % 6);
		}
		return initialLeaves;
	}

	HRMSBaseResponse getBaseResponse(String responseMessage, int responseCode, Object responseBody, long totalRecord) {
		HRMSBaseResponse response = new HRMSBaseResponse<>();
		response.setResponseBody(responseBody);
		response.setResponseCode(responseCode);
		response.setResponseMessage(responseMessage);
		response.setTotalRecord(totalRecord);
		return response;
	}

	private void checkEmployeeEmailCodeIfAlredyExist(ProfileDetailsRequestVO request) throws HRMSException {
		Employee employee = processorDependencies.employeeDAO
				.findByofficialEmailIdAndIsActive(request.getOfficialEmailId(), IHRMSConstants.isNotActive);
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			employee.setOfficialEmailId(employee.getOfficialEmailId() + "_old");
			processorDependencies.employeeDAO.save(employee);
		}

		Employee emp = processorDependencies.employeeDAO.findByofficialEmailIdAndIsActive(request.getOfficialEmailId(),
				IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(emp)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1615));
		}

		List<Employee> empCode = processorDependencies.employeeDAO.findByEmpCodeLike(request.getEmployeeCode());

		if (!HRMSHelper.isNullOrEmpty(empCode)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1881));
		}

		if (!HRMSHelper.isNullOrEmpty(request.getMobileNumber()) && !HRMSHelper.isLongZero(request.getMobileNumber())) {
			List<Candidate> can = processorDependencies.candidateDAO
					.findByMobileNumberAndIsActive(request.getMobileNumber(), IHRMSConstants.isActive);

			System.out.println("Mobile Number: " + request.getMobileNumber());
			if (!HRMSHelper.isNullOrEmpty(can)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1616));
			}
		}

		LoginEntity loginDetail = processorDependencies.logDao.findByUsername(request.getOfficialEmailId());
		if (!HRMSHelper.isNullOrEmpty(loginDetail)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1615));
		}
	}

	private Employee addEmployeeAndCurrentDetails(ProfileDetailsRequestVO request, Candidate candidate)
			throws NoSuchAlgorithmException, HRMSException {
		Employee emp = new Employee();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Candidate cand = processorDependencies.candidateDAO.findByIdAndIsActive(candidate.getId(),
				IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(cand)) {
			if (orgId.equals(cand.getOrgId())) {
				Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
				emp.setCreatedBy(empId.toString());
				emp.setCreatedDate(new Date());
				emp.setIsActive(IHRMSConstants.isActive);
				emp.setOfficialEmailId(request.getOfficialEmailId());
				emp.setProbationPeriod(request.getProbationPeriod().toString());
				emp.setCandidate(cand);
				emp.setOrgId(cand.getLoginEntity().getOrganization().getId());
				emp.setOfficialMobileNumber(request.getOfficialMobileNumber());
				emp.setEmployeeCode(request.getEmployeeCode());
				emp.setCycleAllowed(request.getIsCycleAllowed());
				emp = processorDependencies.employeeDAO.save(emp);

				setEmployeeReportingManager(request, emp, empId);
				setEmployeeCurrentDetails(request, emp, cand, empId);
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		return emp;

	}

	private void sendEmailToEmployee(Candidate cand, CurrentEmploymentRequestVO request, String randomPwdStr) {

		Employee emp = processorDependencies.employeeDAO.findByCandidateIdAndIsActiveAndOrgId(cand.getId(),
				IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());

		if (HRMSHelper.isNullOrEmpty(emp)) {
			Map<String, String> mailContentMap = new HashMap<String, String>();
			mailContentMap.put("{employeeFirstName}", cand.getFirstName());
			mailContentMap.put("{employeeMiddleName}", cand.getMiddleName());
			mailContentMap.put("{employeeLastName}", cand.getLastName());
			mailContentMap.put("{employeeOfficialEmail}", request.getOfficialEmailId());
			mailContentMap.put("{employeePassword}", cand.getLoginEntity().getPassword());
			mailContentMap.put("{employeeId}", String.valueOf(request.getId()));
			mailContentMap.put("{employeeDesignation}",
					cand.getCandidateProfessionalDetail().getDesignation().getDesignationName());
			mailContentMap.put("{employeeDepartment}",
					cand.getCandidateProfessionalDetail().getDepartment().getDepartmentName());
			mailContentMap.put("{employeePassword}", randomPwdStr);

			String mailContent = HRMSHelper.replaceString(mailContentMap,
					IHRMSEmailTemplateConstants.Template_Employee_Creation_Lrypt);

			processorDependencies.emailsender.toPersistEmail(cand.getEmailId(), null, mailContent,
					IHRMSConstants.MailSubject_EmployeeCreation,
					cand.getCandidateProfessionalDetail().getDivision().getId(),
					cand.getLoginEntity().getOrganization().getId());
		}

	}

	private void setEmployeeCurrentDetails(ProfileDetailsRequestVO request, Employee emp, Candidate cand, Long empId) {
		EmployeeCurrentDetail current = new EmployeeCurrentDetail();
		current.setCreatedBy(empId.toString());
		current.setCreatedDate(new Date());
		current.setIsActive(IHRMSConstants.isActive);
		current.setNoticePeriod(!HRMSHelper.isNullOrEmpty(request.getNoticePeriod()) ? request.getNoticePeriod() : 0);
		current.setEmployee(emp);
		current.setRetirementDate(!HRMSHelper.isNullOrEmpty(request.getRetirementDate())
				? HRMSDateUtil.parse(request.getRetirementDate(), IHRMSConstants.FRONT_END_DATE_FORMAT)
				: null);
		current.setOrgId(cand.getLoginEntity().getOrganization().getId());

		processorDependencies.currentEmploymentDAO.save(current);
	}

	private void setEmployeeReportingManager(ProfileDetailsRequestVO request, Employee emp, Long empId) {
		EmployeeReportingManager employeeReportingMgr = new EmployeeReportingManager();
		Employee reportingManager = processorDependencies.employeeDAO.findById(request.getReportingManager().getId())
				.get();
		employeeReportingMgr.setReporingManager(!HRMSHelper.isNullOrEmpty(reportingManager) ? reportingManager : null);
		employeeReportingMgr.setEmployee(emp);
		employeeReportingMgr.setCreatedBy(empId.toString());
		employeeReportingMgr.setCreatedDate(new Date());
		employeeReportingMgr.setIsActive(IHRMSConstants.isActive);
		employeeReportingMgr.setOrgId(emp.getCandidate().getLoginEntity().getOrganization().getId());
		processorDependencies.reportingManagerDAO.save(employeeReportingMgr);
	}

	private void updateCandidateAndLoginentityForEmployee(ProfileDetailsRequestVO request, Candidate cand)
			throws NoSuchAlgorithmException, HRMSException {
		Set<LoginEntityType> loginEntityTypes = new HashSet<LoginEntityType>();
		for (LoginEntityTypeVO voLoginEntityType : request.getRoles()) {
			if (!HRMSHelper.isLongZero(voLoginEntityType.getId())
					&& !HRMSHelper.isNullOrEmpty(voLoginEntityType.getId())) {
				LoginEntityType loginEntityType = processorDependencies.loginTypeDAO.findById(voLoginEntityType.getId())
						.get();
				loginEntityTypes.add(loginEntityType);
			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Role Id");
			}
		}

		LoginEntity loginEntity = cand.getLoginEntity();
		Employee employee = processorDependencies.employeeDAO.findByCandidateIdAndIsActiveAndOrgId(cand.getId(),
				IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());

		String randomPwdStr = HRMSHelper.randomString();
		loginEntity.setLoginEntityTypes(loginEntityTypes);
		if (HRMSHelper.isNullOrEmpty(employee)) {
			String encryptedPwdStr = HRMSHelper.encryptToSHA256(randomPwdStr);
			loginEntity.setPassword(encryptedPwdStr);
			loginEntity.setIsFirstLogin(IHRMSConstants.IsFirstLogin);
			loginEntity.setUsername(request.getOfficialEmailId());
			cand.setCandidateStatus(IHRMSConstants.CandidateStatus_ISEMPLOYEE);
			cand.setLoginEntity(loginEntity);
			processorDependencies.candidateDAO.save(cand);

		}

//		MasterEmploymentType masterEmploymentType = processorDependencies.masterEmploymentTypeDAO
//				.findById(request.getEmploymentType().getId()).get();
//		cand.setEmploymentType(masterEmploymentType);
//		processorDependencies.candidateDAO.save(cand);

	}

	private void setProfessionalDetailsForEmployee(CurrentEmploymentRequestVO request,
			CandidateProfessionalDetail prof) {
		MasterDesignation masterDesignation = processorDependencies.designationDAO
				.findById(request.getDesignation().getId()).get();
		MasterCity masterCity = processorDependencies.cityDAO.findById(request.getCity().getId()).get();
		MasterCountry masterCountry = processorDependencies.countryDAO.findById(request.getCountry().getId()).get();
		MasterState masterState = processorDependencies.stateDAO.findById(request.getState().getId()).get();
		MasterDepartment department = processorDependencies.departmentDAO.findById(request.getDepartment().getId())
				.get();
		MasterBranch branch = processorDependencies.branchDAO.findById(request.getBranch().getId()).get();
		MasterDivision division = processorDependencies.divisionDAO.findById(request.getDivision().getId()).get();
		prof.setDesignation(masterDesignation);
		prof.setDepartment(department);
		prof.setBranch(branch);
		prof.setDivision(division);
		prof.setCity(masterCity);
		prof.setState(masterState);
		prof.setCountry(masterCountry);
		prof.setDateOfJoining(HRMSDateUtil.parse(request.getDateOfJoining(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		processorDependencies.professionalDAO.save(prof);
	}

	private CurrentEmploymentResponseVO setCurrentEmploymentResponseVO(Employee employee,
			CurrentEmploymentRequestVO request) {

		EmployeeCurrentDetail currentDetails = processorDependencies.currentEmploymentDAO
				.findEmployeeCurrentDetailEmployeeWise(employee.getId());
		EmployeeACN acn = processorDependencies.acnDAO.findByEmpId(employee.getId());
		CurrentEmploymentResponseVO responseVO = new CurrentEmploymentResponseVO();
		responseVO.setId(employee.getId());
		responseVO
				.setEmployeeType(convertToEmploymentTypeMasterVO(employee.getCandidate().getEmploymentType().getId()));
		responseVO.setDesignation(convertToMasterDesignationVO(
				employee.getCandidate().getCandidateProfessionalDetail().getDesignation().getId()));
		responseVO.setDateOfJoining(
				HRMSDateUtil.format(employee.getCandidate().getCandidateProfessionalDetail().getDateOfJoining(),
						IHRMSConstants.FRONT_END_DATE_FORMAT));
		responseVO.setDateOfRetirement(
				HRMSDateUtil.format(currentDetails.getRetirementDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		responseVO.setNoticePeriod(currentDetails.getNoticePeriod());
		responseVO.setProbationPeriod(Integer.valueOf(employee.getProbationPeriod()));
		responseVO.setDivision(convertToDivisionMasterVO(
				employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId()));
		responseVO.setBranch(
				convertToBranchMasterVO(employee.getCandidate().getCandidateProfessionalDetail().getBranch().getId()));
		responseVO.setCity(
				convertToCityMasterVO(employee.getCandidate().getCandidateProfessionalDetail().getCity().getId()));
		responseVO.setState(convertToStateMasterVO(employee.getCandidate().getCandidateProfessionalDetail().getId()));
		responseVO.setCountry(convertToCountryMasterVO(
				employee.getCandidate().getCandidateProfessionalDetail().getCountry().getId()));
		responseVO.setDepartment(convertToDepartmentMasterVO(
				employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getId()));
		responseVO.setReportingManager(convertToreportingManagerVo(request.getReportingManager().getId()));
		responseVO.setOfficialEmailId(employee.getOfficialEmailId());
		responseVO.setOfficialMobileNumber(employee.getOfficialMobileNumber());
		responseVO.setAcnNumber(acn.getEmpACN());
		responseVO.setCandidateId(employee.getCandidate().getId());
		responseVO.setRoles(
				convertToSetLoginEntityTypeModel(employee.getCandidate().getLoginEntity().getLoginEntityTypes()));

		return responseVO;
	}

	@Override
	public HRMSBaseResponse<List<ProfileDetailListVO>> getAllEmployeeList(String keyword, Pageable pageable)
			throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "getAllEmployeeList");
		}
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		List<Candidate> candidate = null;
		long totalRecord = 0;
		if (!HRMSHelper.isNullOrEmpty(keyword)) {
			totalRecord = processorDependencies.getCandidateDAO().countOfprofilesByKeyword(orgId,
					IHRMSConstants.isActive, keyword);
			candidate = processorDependencies.getCandidateDAO().findByOrgIdAndIsActiveAndKeyword(orgId,
					IHRMSConstants.isActive, keyword, pageable);
		} else {
			totalRecord = processorDependencies.getCandidateDAO().countOfprofiles(orgId, IHRMSConstants.isActive);
			candidate = processorDependencies.getCandidateDAO().findByOrgIdAndIsActive(orgId, IHRMSConstants.isActive,
					pageable);
		}
		List<ProfileDetailListVO> profileList = new ArrayList<>();
		getProfileList(candidate, profileList);

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "getAllEmployeeList");
		}
		HRMSBaseResponse<List<ProfileDetailListVO>> response = getBaseResponse(
				ResponseCode.getResponseCodeMap().get(1200), 1200, profileList, totalRecord);
		return response;
	}

	private void getProfileList(List<Candidate> candidate, List<ProfileDetailListVO> profileList) throws HRMSException {
		Long counter = 1L;
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			for (Candidate candidateDetail : candidate) {
				Employee employee = processorDependencies.employeeDAO.findByCandidateIdAndIsActiveAndOrgId(
						candidateDetail.getId(), IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());
				ProfileDetailListVO profileDetailList = new ProfileDetailListVO();
				profileDetailList.setSrNo(counter++);
				profileDetailList.setCandidateId(candidateDetail.getId());
				profileDetailList.setEmployeeName(candidateDetail.getFirstName() + " " + candidateDetail.getLastName());
				profileDetailList.setDepartment(
						!HRMSHelper.isNullOrEmpty(candidateDetail.getCandidateProfessionalDetail().getDepartment())
								? candidateDetail.getCandidateProfessionalDetail().getDepartment().getDepartmentName()
								: null);
				profileDetailList.setDesignation(
						!HRMSHelper.isNullOrEmpty(candidateDetail.getCandidateProfessionalDetail().getDesignation())
								? candidateDetail.getCandidateProfessionalDetail().getDesignation().getDesignationName()
								: null);
				profileDetailList.setEmployeeType(!HRMSHelper.isNullOrEmpty(candidateDetail.getEmploymentType())
						? candidateDetail.getEmploymentType().getEmploymentTypeName()
						: null);
				profileDetailList.setStatus(!HRMSHelper.isNullOrEmpty(employee) ? EProfileStatus.COMPLETED.name()
						: EProfileStatus.PROCESSING.name());
				profileDetailList.setEmployeeId(!HRMSHelper.isNullOrEmpty(employee) ? employee.getId() : null);
				profileList.add(profileDetailList);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	@Override
	public HRMSBaseResponse<ProfileDetailVO> getProfileDetail(Long candidateId) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "getProfileDetail");
		}
		if (HRMSHelper.isNullOrEmpty(candidateId)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		HRMSBaseResponse<ProfileDetailVO> response = null;
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Candidate candidate = processorDependencies.getCandidateDAO().findByIdAndIsActive(candidateId,
				IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(candidate)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		if (!orgId.equals(candidate.getOrgId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		ProfileDetailVO profileDetail = getProfileDetailById(candidate);
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "getProfileDetail");
		}
		response = getBaseResponse(ResponseCode.getResponseCodeMap().get(1200), 1200, profileDetail, 0);
		return response;
	}

	private ProfileDetailVO getProfileDetailById(Candidate candidate) {
		ProfileDetailVO profileDetail = new ProfileDetailVO();
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			profileDetail.setId(candidate.getId());
			profileDetail.setSalutation(convertToTitleMasterVO(candidate));
			profileDetail.setFirstName(candidate.getFirstName());
			profileDetail.setMiddleName(
					!HRMSHelper.isNullOrEmpty(candidate.getMiddleName()) ? candidate.getMiddleName() : null);
			profileDetail.setLastName(candidate.getLastName());
			profileDetail.setDateOfBirth(candidate.getDateOfBirth());
			profileDetail.setMobileNumber(candidate.getMobileNumber());
			profileDetail.setEmailId(candidate.getEmailId());
//			profileDetail.setMaritalStatus(!HRMSHelper.isNullOrEmpty(candidate.getCandidatePersonalDetail())
//					? candidate.getCandidatePersonalDetail().getMaritalStatus()
//					: null);
			profileDetail.setMaritalStatus(convertToMaritalStatusMasterVO(candidate.getCandidatePersonalDetail()));
			profileDetail.setGender(convertToGenderMasterVO(candidate));
			profileDetail.setSpouceName(!HRMSHelper.isNullOrEmpty(candidate.getCandidatePersonalDetail())
					? candidate.getCandidatePersonalDetail().getSpouseName()
					: null);
			profileDetail.setAdharCardNumber(!HRMSHelper.isNullOrEmpty(candidate.getCandidateProfessionalDetail())
					? candidate.getCandidateProfessionalDetail().getAadhaarCard()
					: null);
			profileDetail.setPanCardNumber(!HRMSHelper.isNullOrEmpty(candidate.getCandidateProfessionalDetail())
					? candidate.getCandidateProfessionalDetail().getPanCard()
					: null);
			profileDetail.setDrivingLicenseNumber(!HRMSHelper.isNullOrEmpty(candidate.getCandidatePersonalDetail())
					? candidate.getCandidatePersonalDetail().getDrivingLicense()
					: null);
			if (!HRMSHelper.isNullOrEmpty(candidate.getCandidatePersonalDetail())) {
				profileDetail.setPassportNumber(
						!HRMSHelper.isNullOrEmpty(candidate.getCandidatePersonalDetail().getCandidatePassportDetail())
								? candidate.getCandidatePersonalDetail().getCandidatePassportDetail()
										.getPassportNumber()
								: null);
			}
			profileDetail.setOrgId(candidate.getOrgId());

			List<IdentificationDetailsVO> identificationDetails = new ArrayList<IdentificationDetailsVO>();
			if (!HRMSHelper.isNullOrEmpty(candidate.getCandidateProfessionalDetail())) {
				List<CandidateChecklist> checklists = processorDependencies.CandidateChecklistDAO
						.getCandidateWithChecklistDetailsByCandidateIdAndItemId(
								candidate.getCandidateProfessionalDetail().getId(),
								EChecklistItems.PROFILE_DETAILS.toString());

				IdentificationDetailsVO identificationDetail = new IdentificationDetailsVO();

				if (!HRMSHelper.isNullOrEmpty(checklists)) {
					for (CandidateChecklist checklist : checklists) {
						identificationDetail = new IdentificationDetailsVO();
						identificationDetail.setDocumentName(checklist.getAttachment());
						identificationDetail.setDocumentType(checklist.getChecklistItem());
						identificationDetails.add(identificationDetail);
					}
				}

			}
			profileDetail.setDocuments(identificationDetails);
		}
		return profileDetail;
	}

	@Override
	public HRMSBaseResponse<?> saveBankDetail(BankDetailsVO bankDetail) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "saveBankDetail");
		}
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long loggedInUser = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("saveBankDetail", role)) {
			if (!HRMSHelper.isNullOrEmpty(bankDetail)) {
				processorDependencies.directOnboardAuthorityHelper.saveBankValidator(bankDetail);
				Candidate candidate = processorDependencies.candidateDAO
						.findByIdAndIsActiveAndorgId(bankDetail.getCandidateId(), IHRMSConstants.isActive, orgId);
				if (!HRMSHelper.isNullOrEmpty(candidate)) {
					BankDetails existingDetail = processorDependencies.bankDAO
							.findByCandidateIdAndOrgId(candidate.getId(), candidate.getOrgId());
					BankDetails bank = setBankDetail(bankDetail, loggedInUser, candidate, existingDetail);
					BankDetailsVO bankResponse = setBankDetailResponse(bankDetail, bank);
					response = getBaseResponse(ResponseCode.getResponseCodeMap().get(1200), 1200, bankResponse, 0);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " candidate Id");
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "saveBankDetail");
		}
		return response;
	}

	private BankDetailsVO setBankDetailResponse(BankDetailsVO bankDetail, BankDetails bank) {
		bankDetail.setBankId(bank.getId());
		bankDetail.setBankName(bank.getBankName());
		bankDetail.setBranchLocation(bank.getBranchLocation());
		bankDetail.setAccountNumber(bank.getAccountNumber());
		bankDetail.setFullName(bank.getNameAsPerBank());
		bankDetail.setIfscCode(bank.getIfscCode());
		bankDetail.setMobileNumber(bank.getPhoneNumber());
		return bankDetail;
	}

	private BankDetails setBankDetail(BankDetailsVO bankDetail, Long loggedInUser, Candidate candidate,
			BankDetails bank) {
		if (!HRMSHelper.isNullOrEmpty(bank)) {
			bank.setBankName(bankDetail.getBankName());
			bank.setAccountNumber(bankDetail.getAccountNumber());
			bank.setBranchLocation(bankDetail.getBranchLocation());
			bank.setUpdatedBy(loggedInUser.toString());
			bank.setUpdatedDate(new Date());
			bank.setIfscCode(bankDetail.getIfscCode());
			bank.setPhoneNumber(bankDetail.getMobileNumber());
			bank.setNameAsPerBank(bankDetail.getFullName());
			bank.setIsActive(IHRMSConstants.isActive);
			bank.setOrgId(candidate.getOrgId());
			bank.setCandidate(candidate);
		} else {
			bank = new BankDetails();
			bank.setBankName(bankDetail.getBankName());
			bank.setAccountNumber(bankDetail.getAccountNumber());
			bank.setBranchLocation(bankDetail.getBranchLocation());
			bank.setCreatedBy(loggedInUser.toString());
			bank.setCreatedDate(new Date());
			bank.setIfscCode(bankDetail.getIfscCode());
			bank.setPhoneNumber(bankDetail.getMobileNumber());
			bank.setNameAsPerBank(bankDetail.getFullName());
			bank.setIsActive(IHRMSConstants.isActive);
			bank.setOrgId(candidate.getOrgId());
			bank.setCandidate(candidate);
		}
		processorDependencies.bankDAO.save(bank);
		return bank;
	}

	@Override
	public HRMSBaseResponse<?> saveFamilyDetail(FamilyDetailsVO familyDetails) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "saveFamilyDetail");
		}
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Long loggedInUser = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("saveFamilyDetail", role)) {
			if (!HRMSHelper.isNullOrEmpty(familyDetails)) {
				processorDependencies.directOnboardAuthorityHelper.saveFamilyValidator(familyDetails);
				CandidatePersonalDetail personalDetails = processorDependencies.PersonalDetailDAO
						.findByCandidateAndIsActiveAndOrgId(familyDetails.getCandidateId(), IHRMSConstants.isActive,
								orgId);
				if (HRMSHelper.isNullOrEmpty(personalDetails)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " candidate Id ");
				}
				CandidateFamilyDetail existingDetail = processorDependencies.familyDetailsDAO
						.findByIdAndCandidatePersonalIdAndOrgIdAndIsActive(familyDetails.getId(),
								personalDetails.getId(), orgId, IHRMSConstants.isActive);
				CandidateFamilyDetail family = setFamilyDetail(familyDetails, loggedInUser, personalDetails,
						existingDetail);
				FamilyDetailsVO familyDetailResponse = setFamilyDetailResponse(familyDetails, family);
				response = getBaseResponse(ResponseCode.getResponseCodeMap().get(1200), 1200, familyDetailResponse, 0);
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "saveFamilyDetail");
		}
		return response;
	}

	@Override
	public HRMSBaseResponse<?> saveEducationDetails(EducationalDetailsVO educationalDetailsVo)
			throws HRMSException, ParseException {

		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "saveEducationalDetails");
		}
		HRMSBaseResponse<EducationalDetailsVO> response = null;
		if (!HRMSHelper.isNullOrEmpty(educationalDetailsVo)) {
			EducationalDetailsVO responseVO = new EducationalDetailsVO();
			List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
			Long loggedInUser = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("saveEducationDetails", role)) {
				if (!HRMSHelper.isNullOrEmpty(educationalDetailsVo)) {
					processorDependencies.directOnboardAuthorityHelper.saveQualificationValidator(educationalDetailsVo);
					Candidate candidate = processorDependencies.candidateDAO
							.findByIdAndIsActive(educationalDetailsVo.getCandidateId(), IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(candidate)) {
						responseVO = convertcandidateQualificationToEducationVo(educationalDetailsVo, response,
								loggedInUser, candidate);
					}

				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

			response = getBaseResponse(ResponseCode.getResponseCodeMap().get(1200), 1200, responseVO, 0);
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
		}

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "saveEducationalDetails");
		}
		return response;

	}

	private EducationalDetailsVO convertcandidateQualificationToEducationVo(EducationalDetailsVO educationalDetailsVo,
			HRMSBaseResponse<EducationalDetailsVO> response, Long loggedInUser, Candidate candidate) {
		CandidateQualification qualification = setCandidateEducationalDetailsAndSave(educationalDetailsVo, loggedInUser,
				candidate);
		EducationalDetailsVO detailsVo = new EducationalDetailsVO();
		detailsVo.setAcademicAchievements(qualification.getAcademicAchievements());
		detailsVo.setBoardUniversity(qualification.getBoardUniversity());
		detailsVo.setCandidateId(qualification.getCandidateProfessionalDetail().getCandidate().getId());
		detailsVo.setDegree(qualification.getDegree());
		detailsVo.setGradeDivisionPercentage(qualification.getGradeDivisionPercentage());
		detailsVo.setInstituteName(qualification.getInstituteName());
		detailsVo.setModeOfEducation(qualification.getModeOfEducation());
		detailsVo.setQualificationId(qualification.getId());
		detailsVo.setStateLocation(qualification.getStateLocation());
		detailsVo.setSubjectOfSpecialization(qualification.getSubjectOfSpecialization());
		detailsVo.setPassingYearMonth(
				HRMSDateUtil.format(qualification.getPassingYearMonth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		detailsVo.setOrgId(qualification.getOrgId());

		return detailsVo;
	}

	private CandidateQualification setCandidateEducationalDetailsAndSave(EducationalDetailsVO educationalDetailsVo,
			Long loggedInUser, Candidate candidate) {
		CandidateQualification qualification = addOrUpdateQualificationDetails(educationalDetailsVo, loggedInUser);
		qualification.setDegree(educationalDetailsVo.getDegree());
		qualification.setSubjectOfSpecialization(educationalDetailsVo.getSubjectOfSpecialization());
		qualification.setAcademicAchievements(educationalDetailsVo.getAcademicAchievements());
		qualification.setCreatedBy(loggedInUser.toString());
		qualification.setCreatedDate(new Date());
		qualification.setBoardUniversity(educationalDetailsVo.getBoardUniversity());
		qualification.setCandidateProfessionalDetail(candidate.getCandidateProfessionalDetail());
		qualification.setGradeDivisionPercentage(educationalDetailsVo.getGradeDivisionPercentage());
		qualification.setIsActive(IHRMSConstants.isActive);
		qualification.setOrgId(candidate.getOrgId());
		qualification.setInstituteName(educationalDetailsVo.getInstituteName());
		qualification.setId(educationalDetailsVo.getQualificationId());
		qualification.setModeOfEducation(educationalDetailsVo.getModeOfEducation());
		qualification.setStateLocation(educationalDetailsVo.getStateLocation());
		qualification.setPassingYearMonth(
				HRMSDateUtil.parse(educationalDetailsVo.getPassingYearMonth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		qualification.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());

		return processorDependencies.qualificationDao.save(qualification);

	}

	private CandidateQualification addOrUpdateQualificationDetails(EducationalDetailsVO educationalDetailsVo,
			Long loggedInUser) {
		CandidateQualification qualification;
		if (!HRMSHelper.isNullOrEmpty(educationalDetailsVo.getQualificationId())) {
			qualification = processorDependencies.qualificationDao.findById(educationalDetailsVo.getQualificationId())
					.orElse(null);

			if (!HRMSHelper.isNullOrEmpty(qualification)) {

				qualification.setUpdatedDate(new Date());
				qualification.setUpdatedBy(loggedInUser.toString());

			} else {
				qualification = new CandidateQualification();
			}

		} else {
			qualification = new CandidateQualification();
		}
		return qualification;
	}

	@Override
	public HRMSBaseResponse<?> saveCertificationDetails(CertificationDetailsVO certificationDetails)
			throws HRMSException {

		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "saveCertificationDetails");
		}
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long loggedInUser = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		CertificationDetailsVO certificationResponseVo = new CertificationDetailsVO();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("saveCertificationDetails", role)) {
			if (!HRMSHelper.isNullOrEmpty(certificationDetails)) {
				processorDependencies.directOnboardAuthorityHelper.saveCertificationValidator(certificationDetails);
				Candidate candidate = processorDependencies.candidateDAO
						.findByIdAndIsActive(certificationDetails.getCandidateId(), IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(candidate)) {
					CandidateCertification certification = setCertification(certificationDetails, loggedInUser,
							candidate);
					CandidateCertification savedCertification = processorDependencies.certificationDao
							.save(certification);
					certificationResponseVo = setCertificationResponse(savedCertification);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		response = getBaseResponse(ResponseCode.getResponseCodeMap().get(1200), 1200, certificationResponseVo, 0);
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "saveCertificationDetails");
		}
		return response;
	}

	private CertificationDetailsVO setCertificationResponse(CandidateCertification savedCertification) {
		CertificationDetailsVO certificationResponse = new CertificationDetailsVO();
		certificationResponse
				.setCandidateId(savedCertification.getCandidateProfessionalDetail().getCandidate().getId());
		certificationResponse.setCertificationDate(
				HRMSDateUtil.format(savedCertification.getCertificationDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		certificationResponse.setCertificationId(savedCertification.getId());
		certificationResponse.setCertificationName(savedCertification.getCertificationName());
		certificationResponse.setCertificationType(savedCertification.getCertificationType());
		certificationResponse.setCertificationValidityDate(HRMSDateUtil
				.format(savedCertification.getCertificationValidityDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		certificationResponse.setModeOfEducation(savedCertification.getModeOfEducation());
		certificationResponse.setNameOfInstitute(savedCertification.getNameOfInstitute());
		certificationResponse.setPercentageGrade(savedCertification.getPercentageGrade());
		certificationResponse.setOrgId(savedCertification.getOrgId());
		return certificationResponse;
	}

	private CandidateCertification setCertification(CertificationDetailsVO certificationDetails, Long loggedInUser,
			Candidate candidate) {
		CandidateCertification certification = addOrUpdateCertification(certificationDetails, loggedInUser);
		certification.setCandidateProfessionalDetail(candidate.getCandidateProfessionalDetail());
		certification.setCertificationDate(
				HRMSDateUtil.parse(certificationDetails.getCertificationDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		certification.setCertificationName(certificationDetails.getCertificationName());
		certification.setCreatedBy(loggedInUser.toString());
		certification.setCreatedDate(new Date());
		certification.setIsActive(IHRMSConstants.isActive);
		certification.setOrgId(candidate.getOrgId());
		certification.setNameOfInstitute(certificationDetails.getNameOfInstitute());
		certification.setId(certificationDetails.getCertificationId());
		certification.setModeOfEducation(certificationDetails.getModeOfEducation());
		certification.setCertificationType(certificationDetails.getCertificationType());
		certification.setCertificationValidityDate(HRMSDateUtil
				.parse(certificationDetails.getCertificationValidityDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		certification.setPercentageGrade(certificationDetails.getPercentageGrade());
		certification.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		return certification;
	}

	private CandidateCertification addOrUpdateCertification(CertificationDetailsVO certificationDetails,
			Long loggedInUser) {
		CandidateCertification certification;
		if (!HRMSHelper.isNullOrEmpty(certificationDetails.getCertificationId())) {
			certification = processorDependencies.certificationDao.findById(certificationDetails.getCertificationId())
					.orElse(null);

			if (!HRMSHelper.isNullOrEmpty(certification)) {

				certification.setUpdatedDate(new Date());
				certification.setUpdatedBy(loggedInUser.toString());

			} else {
				certification = new CandidateCertification();
			}

		} else {
			certification = new CandidateCertification();
		}
		return certification;
	}

	private FamilyDetailsVO setFamilyDetailResponse(FamilyDetailsVO familyDetails, CandidateFamilyDetail family) {
		familyDetails.setId(family.getId());
		familyDetails
				.setDateOfBirth(HRMSDateUtil.format(family.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		familyDetails.setContactNo1(family.getContactNo1());
		familyDetails.setDependent(family.getDependent());
		familyDetails.setFirstName(family.getFirst_name());
		familyDetails.setLastName(family.getLast_name());
		familyDetails.setGender(family.getGender());
		familyDetails.setIsEmergencyContact(
				!HRMSHelper.isNullOrEmpty(familyDetails.getIsEmergencyContact()) ? familyDetails.getIsEmergencyContact()
						: null);
		familyDetails.setOccupation(family.getOccupation());
		familyDetails.setRelationship(family.getRelationship());
		familyDetails
				.setDateOfBirth(HRMSDateUtil.format(family.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		return familyDetails;
	}

	private CandidateFamilyDetail setFamilyDetail(FamilyDetailsVO familyDetails, Long loggedInUser,
			CandidatePersonalDetail personalDetails, CandidateFamilyDetail family) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(family)) {
			family.setCandidatePersonalDetail(personalDetails);
			family.setFirst_name(familyDetails.getFirstName());
			family.setMiddle_name(familyDetails.getMiddleName());
			family.setLast_name(familyDetails.getLastName());
			family.setDateOfBirth(
					HRMSDateUtil.parse(familyDetails.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			family.setRelationship(familyDetails.getRelationship());
			family.setOccupation(familyDetails.getOccupation());
			family.setGender(familyDetails.getGender());
			family.setDependent(familyDetails.getDependent());
			family.setContactNo1(familyDetails.getContactNo1());
			checkIsMergencyContactAlreadyExists(familyDetails, personalDetails, family);
			family.setIsActive(IHRMSConstants.isActive);
			family.setUpdatedBy(loggedInUser.toString());
			family.setOrgId(personalDetails.getOrgId());
			family.setUpdatedDate(new Date());
		} else {
			family = new CandidateFamilyDetail();
			family.setCandidatePersonalDetail(personalDetails);
			family.setFirst_name(familyDetails.getFirstName());
			family.setMiddle_name(familyDetails.getMiddleName());
			family.setLast_name(familyDetails.getLastName());
			family.setDateOfBirth(
					HRMSDateUtil.parse(familyDetails.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			family.setRelationship(familyDetails.getRelationship());
			family.setOccupation(familyDetails.getOccupation());
			family.setGender(familyDetails.getGender());
			family.setDependent(familyDetails.getDependent());
			family.setContactNo1(familyDetails.getContactNo1());
			checkIsMergencyContactAlreadyExists(familyDetails, personalDetails, family);
			family.setIsActive(IHRMSConstants.isActive);
			family.setCreatedBy(loggedInUser.toString());
			family.setOrgId(personalDetails.getOrgId());
			family.setCreatedDate(new Date());
		}
		processorDependencies.familyDetailsDAO.save(family);
		return family;
	}

	private void checkIsMergencyContactAlreadyExists(FamilyDetailsVO familyDetails,
			CandidatePersonalDetail personalDetails, CandidateFamilyDetail family) throws HRMSException {
		List<CandidateFamilyDetail> candidateFamily = processorDependencies.familyDetailsDAO
				.findCandidateFamilyDetailsbyId(personalDetails.getId());
		boolean hasEmergencyContact = candidateFamily.stream()
				.anyMatch(detail -> !HRMSHelper.isNullOrEmpty(detail.getIsEmergencyContact()));

		if (!HRMSHelper.isNullOrEmpty(familyDetails.getIsEmergencyContact())
				&& familyDetails.getIsEmergencyContact().equalsIgnoreCase(IHRMSConstants.isEmergencyContact)) {
			if (hasEmergencyContact) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1614));
			} else {
				family.setIsEmergencyContact(familyDetails.getIsEmergencyContact());
			}
		}
	}

	@Override
	public HRMSBaseResponse<?> saveHealthDetail(HealthDetailsVO healthDetail) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "saveHealthDetail");
		}
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Long loggedInUser = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("saveHealthDetail", role)) {
			if (!HRMSHelper.isNullOrEmpty(healthDetail)) {
				processorDependencies.directOnboardAuthorityHelper.saveHealthValidator(healthDetail);
				CandidatePersonalDetail personalDetails = processorDependencies.PersonalDetailDAO
						.findByCandidateAndIsActiveAndOrgId(healthDetail.getCandidateId(), IHRMSConstants.isActive,
								orgId);
				CandidateHealthReport health = processorDependencies.healthDao
						.findBycandidatePersonalDetail(personalDetails);
				if (HRMSHelper.isNullOrEmpty(personalDetails)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " candidate Id ");
				}
				setHEalthDetail(healthDetail, loggedInUser, personalDetails, health);

				HealthDetailsVO healthResponse = new HealthDetailsVO();
				healthResponse = setHealthRespnse(personalDetails, health, healthResponse);
				response = getBaseResponse(ResponseCode.getResponseCodeMap().get(1200), 1200, healthResponse, 0);

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "saveHealthDetail");
		}
		return response;
	}

	private void setHEalthDetail(HealthDetailsVO healthDetail, Long loggedInUser,
			CandidatePersonalDetail personalDetails, CandidateHealthReport health) {
		if (!HRMSHelper.isNullOrEmpty(health)) {
			health.setOrgId(personalDetails.getOrgId());
			health.setBloodGroup(healthDetail.getBloodGroup());
			health.setPhysicallyHandicapped(healthDetail.getPhysicallyHandicapped());
			health.setPhysicallyHandicapDescription(healthDetail.getPhysicallyHandicapDiscription());
			health.setSurgery(healthDetail.getSurgery());
			health.setSurgeryDescription(healthDetail.getSurgeryDiscription());
			health.setAllergy(healthDetail.getAllergy());
			health.setAllergyDescription(healthDetail.getAllergyDiscription());
			health.setHealthHistory(healthDetail.getHealthHistory());
			health.setHealthHistoryDescription(healthDetail.getHealthHistoryDiscription());
			health.setCandidatePersonalDetail(personalDetails);
			health.setIsActive(IHRMSConstants.isActive);
			health.setUpdatedBy(loggedInUser.toString());
			health.setUpdatedDate(new Date());
			personalDetails.setEsiNo(healthDetail.getEsic());// saving ESIC in CandidatePersonalDetail
			processorDependencies.PersonalDetailDAO.save(personalDetails);
		} else {
			health = new CandidateHealthReport();
			health.setOrgId(personalDetails.getOrgId());
			health.setBloodGroup(healthDetail.getBloodGroup());
			health.setPhysicallyHandicapped(healthDetail.getPhysicallyHandicapped());
			health.setPhysicallyHandicapDescription(healthDetail.getPhysicallyHandicapDiscription());
			health.setSurgery(healthDetail.getSurgery());
			health.setSurgeryDescription(healthDetail.getSurgeryDiscription());
			health.setAllergy(healthDetail.getAllergy());
			health.setAllergyDescription(healthDetail.getAllergyDiscription());
			health.setHealthHistory(healthDetail.getHealthHistory());
			health.setHealthHistoryDescription(healthDetail.getHealthHistoryDiscription());
			health.setCandidatePersonalDetail(personalDetails);
			health.setIsActive(IHRMSConstants.isActive);
			health.setCreatedBy(loggedInUser.toString());
			health.setCreatedDate(new Date());
			personalDetails.setEsiNo(healthDetail.getEsic());// saving ESIC in CandidatePersonalDetail
			processorDependencies.PersonalDetailDAO.save(personalDetails);
		}
		processorDependencies.healthDao.save(health);
	}

	private HealthDetailsVO setHealthRespnse(CandidatePersonalDetail personalDetails, CandidateHealthReport health,
			HealthDetailsVO healthResponse) {
		if (!HRMSHelper.isNullOrEmpty(health)) {
			healthResponse.setAllergy(!HRMSHelper.isNullOrEmpty(health.getAllergy()) ? health.getAllergy() : null);
			healthResponse.setAllergyDiscription(
					!HRMSHelper.isNullOrEmpty(health.getAllergyDescription()) ? health.getAllergyDescription() : null);
			healthResponse
					.setBloodGroup(!HRMSHelper.isNullOrEmpty(health.getBloodGroup()) ? health.getBloodGroup() : null);
			healthResponse.setHealthHistory(
					!HRMSHelper.isNullOrEmpty(health.getHealthHistory()) ? health.getHealthHistory() : null);
			healthResponse.setHealthHistoryDiscription(!HRMSHelper.isNullOrEmpty(health.getHealthHistoryDescription())
					? health.getHealthHistoryDescription()
					: null);
			healthResponse.setPhysicallyHandicapDiscription(
					!HRMSHelper.isNullOrEmpty(health.getPhysicallyHandicapDescription())
							? health.getPhysicallyHandicapDescription()
							: null);
			healthResponse.setPhysicallyHandicapped(
					!HRMSHelper.isNullOrEmpty(health.getPhysicallyHandicapped()) ? health.getPhysicallyHandicapped()
							: null);
			healthResponse.setSurgery(!HRMSHelper.isNullOrEmpty(health.getSurgery()) ? health.getSurgery() : null);
			healthResponse.setSurgeryDiscription(
					!HRMSHelper.isNullOrEmpty(health.getSurgeryDescription()) ? health.getSurgeryDescription() : null);
			healthResponse.setEsic(!HRMSHelper.isNullOrEmpty(personalDetails) ? personalDetails.getEsiNo() : null);
		}
		return healthResponse;
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
		if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("deleteProfile", role)) {
			processorDependencies.directOnboardAuthorityHelper.deleteProfileValidator(candidateId);
			Candidate candidate = processorDependencies.candidateDAO.findByIdAndIsActiveAndorgId(candidateId,
					IHRMSConstants.isActive, orgId);

			Candidate inActiveCandidate = processorDependencies.candidateDAO.findByIdAndIsActiveAndorgId(candidateId,
					IHRMSConstants.isNotActive, orgId);

			if (!HRMSHelper.isNullOrEmpty(inActiveCandidate)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1620));
			}
			if (!HRMSHelper.isNullOrEmpty(candidate)) {// it checks candidate from different org too
				if (!HRMSHelper.isNullOrEmpty(candidate.getEmployee())) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1621));
				}
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
		CandidatePersonalDetail personalDetail = candidate.getCandidatePersonalDetail();
		if (!HRMSHelper.isNullOrEmpty(personalDetail)) {
			personalDetail.setIsActive(IHRMSConstants.isNotActive);
			processorDependencies.PersonalDetailDAO.save(personalDetail);
		}
		CandidateProfessionalDetail professional = candidate.getCandidateProfessionalDetail();
		if (!HRMSHelper.isNullOrEmpty(professional)) {
			professional.setIsActive(IHRMSConstants.isNotActive);
			processorDependencies.professionalDAO.save(professional);
		}
		LoginEntity loginEntity = candidate.getLoginEntity();
		if (!HRMSHelper.isNullOrEmpty(loginEntity)) {
			loginEntity.setIsActive(IHRMSConstants.isNotActive);
			processorDependencies.logDao.save(loginEntity);
		}
		if (!HRMSHelper.isNullOrEmpty(candidate.getEmployee())) {
			EmployeeCurrentDetail currentEmployeeDetail = candidate.getEmployee().getEmployeeCurrentDetail();
			if (!HRMSHelper.isNullOrEmpty(currentEmployeeDetail)) {
				currentEmployeeDetail.setIsActive(IHRMSConstants.isNotActive);
				processorDependencies.currentEmploymentDAO.save(currentEmployeeDetail);
			}
		}
		candidate.setIsActive(IHRMSConstants.isNotActive);
		processorDependencies.candidateDAO.save(candidate);
	}

	@Override
	public HRMSBaseResponse<ValidationVO> getDirectOnboardingValidationFlags(Long candidateId) throws HRMSException {
		log.info(LogConstants.ENTERED.template(), "getDirectOnboardingValidationFlags");
		HRMSBaseResponse<ValidationVO> response = new HRMSBaseResponse<>();
		// check candidateId is 0
		if (HRMSHelper.isNullOrEmpty(candidateId) || HRMSHelper.isLongZero(candidateId)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		ValidationVO validationVO = handleGetDirectOnboardingValidationFlags(candidateId);
		response = processorDependencies.getCommonMethod().getBaseResponse(ResponseCode.getResponseCodeMap().get(1200),
				1200, validationVO, 0);
		log.info(LogConstants.EXITED.template(), "getDirectOnboardingValidationFlags");
		return response;
	}

	private ValidationVO handleGetDirectOnboardingValidationFlags(Long candidateId) throws HRMSException {
		log.info(LogConstants.ENTERED.template(), "handleGetDirectOnboardingValidationFlags");
		ValidationVO validationVO = new ValidationVO();
		validationVO.setIsCandidateIdCreated(ERecordStatus.N.name());
		validationVO.setIsEmployeeIdCreated(ERecordStatus.N.name());
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		processorDependencies.getCommonMethod().validateRoleToFunction("getDirectOnboardingValidationFlags", role);
		Candidate candidate = processorDependencies.candidateDAO.findByIdAndIsActiveAndorgId(candidateId,
				ERecordStatus.Y.name(), SecurityFilter.TL_CLAIMS.get().getOrgId());
		Employee employee = processorDependencies.employeeDAO.findByCandidateIdAndIsActiveAndOrgId(candidateId,
				ERecordStatus.Y.name(), SecurityFilter.TL_CLAIMS.get().getOrgId());
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			validationVO.setIsCandidateIdCreated(ERecordStatus.Y.name());
		}
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			validationVO.setIsEmployeeIdCreated(ERecordStatus.Y.name());
		}
		log.info(LogConstants.EXITED.template(), "handleGetDirectOnboardingValidationFlags");
		return validationVO;
	}

	@Override
	public HRMSBaseResponse<?> saveAddressDetails(AddressDetailsRequestVo addressVo) throws HRMSException {

		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "saveAddressDetails");
		}
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long loggedInUser = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		AddressDetailsRequestVo addresDetailsReqVo = new AddressDetailsRequestVo();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("saveAddressDetails", role)) {
			if (!HRMSHelper.isNullOrEmpty(addressVo)) {
				processorDependencies.directOnboardAuthorityHelper.saveAddressValidator(addressVo);
				Candidate candidate = processorDependencies.candidateDAO
						.findByIdAndIsActiveAndorgId(addressVo.getCandidateId(), IHRMSConstants.isActive, orgId);
				if (!HRMSHelper.isNullOrEmpty(candidate)) {
					CandidateAddress currentAddress = setCurrentAddress(addressVo, loggedInUser, candidate);
					CandidateAddress permenantAddress = setPermenantAddress(addressVo, loggedInUser, candidate);
					CandidateAddress currentAddressResponse = processorDependencies.addressDao.save(currentAddress);
					CandidateAddress permanantAddressResponse = processorDependencies.addressDao.save(permenantAddress);
					addresDetailsReqVo = setAddressResponse(currentAddressResponse, permanantAddressResponse);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		response = getBaseResponse(ResponseCode.getResponseCodeMap().get(1200), 1200, addresDetailsReqVo, 0);
		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "saveCertificationDetails");
		}
		return response;
	}

	private AddressDetailsRequestVo setAddressResponse(CandidateAddress currentAddressResponse,
			CandidateAddress permanantAddressResponse) throws HRMSException {
		AddressDetailsRequestVo response = new AddressDetailsRequestVo();
		response.setNationality(currentAddressResponse.getNationality());
		response.setSSNumber(currentAddressResponse.getSsnNumber());
		response.setCandidateId(currentAddressResponse.getCandidate().getId());
		response.setSSNumber(currentAddressResponse.getSsnNumber());
		Candidate candidate = currentAddressResponse.getCandidate();
		CandidatePersonalDetail personalDetails = processorDependencies.PersonalDetailDAO.findBycandidate(candidate);
		response.setCitizenship(personalDetails.getCitizenship());
		response.setPresentAddress(setCurrentAddressVo(currentAddressResponse, personalDetails));
		response.setPermanentAddress(setPermanentAddressVo(permanantAddressResponse, personalDetails));
		return response;
	}

	private CandidateAddressVo setCurrentAddressVo(CandidateAddress currentAddressResponse,
			CandidatePersonalDetail personalDetails) {
		CandidateAddressVo currentAddress = new CandidateAddressVo();
		currentAddress.setId(currentAddressResponse.getId());
		currentAddress.setAddressType(currentAddressResponse.getAddressType());
		currentAddress.setCity(convertToCityMasterVO(currentAddressResponse.getCity().getId()));
		currentAddress.setCountry(convertToCountryMasterVO(currentAddressResponse.getCountry().getId()));
		currentAddress.setAddressLine1(currentAddressResponse.getAddressLine1());
		currentAddress.setAddressType(currentAddressResponse.getAddressType());
		currentAddress.setState(convertToStateMasterVO(currentAddressResponse.getState().getId()));
		currentAddress.setIsRental(currentAddressResponse.getIsRental());
		currentAddress.setLandMark(currentAddressResponse.getLandMark());
		currentAddress.setPincode(currentAddressResponse.getPincode());
		currentAddress.setIsRental(currentAddressResponse.getIsRental());
		currentAddress.setOwnerName(currentAddressResponse.getOwnerName());
		currentAddress.setOwnerContact(currentAddressResponse.getOwnerContact());
		currentAddress.setOwnerAadhar(currentAddressResponse.getOwnerAdhar());
		currentAddress.setStreet(currentAddressResponse.getStreet());
		currentAddress.setNationality(currentAddressResponse.getNationality());
		currentAddress.setCitizenship(personalDetails.getCitizenship());
		Candidate candidate = currentAddressResponse.getCandidate();
		currentAddress.setCandidateId(candidate.getId());
		return currentAddress;
	}

	private CandidateAddressVo setPermanentAddressVo(CandidateAddress permanentAddressResponse,
			CandidatePersonalDetail personalDetails) {
		CandidateAddressVo permanentAddress = new CandidateAddressVo();
		permanentAddress.setId(permanentAddressResponse.getId());
		permanentAddress.setAddressType(permanentAddressResponse.getAddressType());
		permanentAddress.setCity(convertToCityMasterVO(permanentAddressResponse.getCity().getId()));
		permanentAddress.setCountry(convertToCountryMasterVO(permanentAddressResponse.getCountry().getId()));
		permanentAddress.setAddressLine1(permanentAddressResponse.getAddressLine1());
		permanentAddress.setAddressType(permanentAddressResponse.getAddressType());
		permanentAddress.setState(convertToStateMasterVO(permanentAddressResponse.getState().getId()));
		permanentAddress.setIsRental(permanentAddressResponse.getIsRental());
		permanentAddress.setLandMark(permanentAddressResponse.getLandMark());
		permanentAddress.setPincode(permanentAddressResponse.getPincode());
		permanentAddress.setIsRental(permanentAddressResponse.getIsRental());
		permanentAddress.setOwnerName(permanentAddressResponse.getOwnerName());
		permanentAddress.setOwnerContact(permanentAddressResponse.getOwnerContact());
		permanentAddress.setOwnerAadhar(permanentAddressResponse.getOwnerAdhar());
		permanentAddress.setStreet(permanentAddressResponse.getStreet());
		permanentAddress.setNationality(permanentAddressResponse.getNationality());
		permanentAddress.setCitizenship(personalDetails.getCitizenship());
		Candidate candidate = permanentAddressResponse.getCandidate();
		permanentAddress.setCandidateId(candidate.getId());
		return permanentAddress;
	}

	private CandidateAddress setCurrentAddress(AddressDetailsRequestVo addressVo, Long loggedInUserId,
			Candidate candidate) throws HRMSException {
		CandidateAddress address = processorDependencies.addressDao
				.findByAddressTypeAndCandidateId(IHRMSConstants.PRESENT_ADDRESS_TYPE, candidate.getId());
		if (!HRMSHelper.isNullOrEmpty(address)) {
			updateCurrentAddress(addressVo, loggedInUserId, candidate, address);
		} else {
			address = createCurrentAddress(addressVo, loggedInUserId, candidate);
		}

		return address;
	}

	private CandidateAddress createCurrentAddress(AddressDetailsRequestVo addressVo, Long loggedInUserId,
			Candidate candidate) throws HRMSException {
		CandidateAddress address;
		address = new CandidateAddress();
		address.setNationality(addressVo.getNationality());
		address.setSsnNumber(addressVo.getSSNumber());
		address.setCandidate(candidate);
		MasterCity masterCity = processorDependencies.cityDAO.findById(addressVo.getPresentAddress().getCity().getId())
				.get();
		MasterCountry masterCountry = processorDependencies.countryDAO
				.findById(addressVo.getPresentAddress().getCountry().getId()).get();
		MasterState masterState = processorDependencies.stateDAO
				.findById(addressVo.getPresentAddress().getState().getId()).get();

		if (!HRMSHelper.isNullOrEmpty(masterCity)) {
			address.setCity(masterCity);
		}
		if (!HRMSHelper.isNullOrEmpty(masterState)) {
			address.setState(masterState);
		}
		if (!HRMSHelper.isNullOrEmpty(masterCountry)) {
			address.setCountry(masterCountry);
		}
		address.setAddressType(addressVo.getPresentAddress().getAddressType());
		address.setAddressLine1(addressVo.getPresentAddress().getAddressLine1());
		address.setAddressType(addressVo.getPresentAddress().getAddressType());
		address.setIsRental(addressVo.getPresentAddress().getIsRental());
		address.setLandMark(addressVo.getPresentAddress().getLandMark());
		address.setOrgId(candidate.getOrgId());
		address.setCreatedBy(loggedInUserId.toString());
		address.setIsRental(addressVo.getPresentAddress().getIsRental());
		address.setStreet(addressVo.getPresentAddress().getStreet());
		address.setPincode((addressVo.getPresentAddress().getPincode()));
		address.setIsActive(IHRMSConstants.isActive);

		address.setCreatedDate(new Date());
		if (!HRMSHelper.isNullOrEmpty(addressVo.getPresentAddress().getIsRental())) {
			if (addressVo.getPresentAddress().getIsRental().equals(IHRMSConstants.isActive)) {
				address.setOwnerName(addressVo.getPresentAddress().getOwnerName());
				address.setOwnerContact(addressVo.getPresentAddress().getOwnerContact());
				address.setOwnerAdhar(addressVo.getPresentAddress().getOwnerAadhar());
			}
		}

		CandidatePersonalDetail personalDetails = processorDependencies.PersonalDetailDAO.findBycandidate(candidate);
		if (!HRMSHelper.isNullOrEmpty(personalDetails)) {
			personalDetails.setCitizenship(addressVo.getCitizenship());
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		return address;
	}

	private void updateCurrentAddress(AddressDetailsRequestVo addressVo, Long loggedInUserId, Candidate candidate,
			CandidateAddress address) throws HRMSException {
		address.setNationality(addressVo.getNationality());
		address.setSsnNumber(addressVo.getSSNumber());
		address.setCandidate(candidate);
		MasterCity masterCity = processorDependencies.cityDAO.findById(addressVo.getPresentAddress().getCity().getId())
				.get();
		MasterCountry masterCountry = processorDependencies.countryDAO
				.findById(addressVo.getPresentAddress().getCountry().getId()).get();
		MasterState masterState = processorDependencies.stateDAO
				.findById(addressVo.getPresentAddress().getState().getId()).get();

		if (!HRMSHelper.isNullOrEmpty(masterCity)) {
			address.setCity(masterCity);
		}
		if (!HRMSHelper.isNullOrEmpty(masterState)) {
			address.setState(masterState);
		}
		if (!HRMSHelper.isNullOrEmpty(masterCountry)) {
			address.setCountry(masterCountry);
		}
		address.setAddressType(addressVo.getPresentAddress().getAddressType());
		address.setAddressLine1(addressVo.getPresentAddress().getAddressLine1());
		address.setAddressType(addressVo.getPresentAddress().getAddressType());
		address.setIsRental(addressVo.getPresentAddress().getIsRental());
		address.setLandMark(addressVo.getPresentAddress().getLandMark());
		address.setOrgId(candidate.getOrgId());
		address.setUpdatedBy(loggedInUserId.toString());
		address.setIsRental(addressVo.getPresentAddress().getIsRental());
		address.setStreet(addressVo.getPresentAddress().getStreet());
		address.setPincode((addressVo.getPresentAddress().getPincode()));
		address.setUpdatedDate(new Date());
		if (!HRMSHelper.isNullOrEmpty(addressVo.getPresentAddress().getIsRental())) {
			if (addressVo.getPresentAddress().getIsRental().equals(IHRMSConstants.isActive)) {
				address.setOwnerName(addressVo.getPresentAddress().getOwnerName());
				address.setOwnerContact(addressVo.getPresentAddress().getOwnerContact());
				address.setOwnerAdhar(addressVo.getPresentAddress().getOwnerAadhar());
			}
		}

		CandidatePersonalDetail personalDetails = processorDependencies.PersonalDetailDAO.findBycandidate(candidate);
		if (!HRMSHelper.isNullOrEmpty(personalDetails)) {
			personalDetails.setCitizenship(addressVo.getCitizenship());
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	private CandidateAddress setPermenantAddress(AddressDetailsRequestVo addressVo, Long loggedInUserId,
			Candidate candidate) throws HRMSException {
		CandidateAddress address = processorDependencies.addressDao
				.findByAddressTypeAndCandidateId(IHRMSConstants.PERMANANT_ADDRESS_TYPE, candidate.getId());
		if (!HRMSHelper.isNullOrEmpty(address)) {
			updatePermanentAddress(addressVo, loggedInUserId, candidate, address);
		} else {
			address = createPermanentAddress(addressVo, loggedInUserId, candidate);

		}

		return address;
	}

	private CandidateAddress createPermanentAddress(AddressDetailsRequestVo addressVo, Long loggedInUserId,
			Candidate candidate) throws HRMSException {
		CandidateAddress address;
		address = new CandidateAddress();
		address.setNationality(addressVo.getNationality());
		address.setSsnNumber(addressVo.getSSNumber());
		address.setCandidate(candidate);
		address.setIsActive(IHRMSConstants.isActive);
		address.setAddressType(addressVo.getPermanentAddress().getAddressType());
		MasterCity masterCity = processorDependencies.cityDAO
				.findById(addressVo.getPermanentAddress().getCity().getId()).get();
		MasterCountry masterCountry = processorDependencies.countryDAO
				.findById(addressVo.getPermanentAddress().getCountry().getId()).get();
		MasterState masterState = processorDependencies.stateDAO
				.findById(addressVo.getPermanentAddress().getState().getId()).get();

		if (!HRMSHelper.isNullOrEmpty(masterCity)) {
			address.setCity(masterCity);
		}
		if (!HRMSHelper.isNullOrEmpty(masterState)) {
			address.setState(masterState);
		}
		if (!HRMSHelper.isNullOrEmpty(masterCountry)) {
			address.setCountry(masterCountry);
		}
		address.setAddressLine1(addressVo.getPermanentAddress().getAddressLine1());
		address.setStreet(addressVo.getPermanentAddress().getStreet());
		address.setAddressType(addressVo.getPermanentAddress().getAddressType());
		address.setIsRental(addressVo.getPermanentAddress().getIsRental());
		address.setLandMark(addressVo.getPermanentAddress().getLandMark());
		address.setOrgId(candidate.getOrgId());
		address.setCreatedBy(loggedInUserId.toString());
		address.setPincode(addressVo.getPermanentAddress().getPincode());
		address.setIsRental(addressVo.getPermanentAddress().getIsRental());
		if (!HRMSHelper.isNullOrEmpty(addressVo.getPermanentAddress().getIsRental())) {
			if (addressVo.getPermanentAddress().getIsRental().equals(IHRMSConstants.isActive)) {
				address.setOwnerName(addressVo.getPermanentAddress().getOwnerName());
				address.setOwnerContact(addressVo.getPermanentAddress().getOwnerContact());
				address.setOwnerAdhar(addressVo.getPermanentAddress().getOwnerAadhar());
			}
		}
		CandidatePersonalDetail personalDetails = processorDependencies.PersonalDetailDAO.findBycandidate(candidate);
		if (!HRMSHelper.isNullOrEmpty(personalDetails)) {
			personalDetails.setCitizenship(addressVo.getCitizenship());
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		return address;
	}

	private void updatePermanentAddress(AddressDetailsRequestVo addressVo, Long loggedInUserId, Candidate candidate,
			CandidateAddress address) throws HRMSException {
		address.setNationality(addressVo.getNationality());
		address.setSsnNumber(addressVo.getSSNumber());
		address.setCandidate(candidate);
		address.setAddressType(addressVo.getPermanentAddress().getAddressType());
		MasterCity masterCity = processorDependencies.cityDAO
				.findById(addressVo.getPermanentAddress().getCity().getId()).get();
		MasterCountry masterCountry = processorDependencies.countryDAO
				.findById(addressVo.getPermanentAddress().getCountry().getId()).get();
		MasterState masterState = processorDependencies.stateDAO
				.findById(addressVo.getPermanentAddress().getState().getId()).get();

		if (!HRMSHelper.isNullOrEmpty(masterCity)) {
			address.setCity(masterCity);
		}
		if (!HRMSHelper.isNullOrEmpty(masterState)) {
			address.setState(masterState);
		}
		if (!HRMSHelper.isNullOrEmpty(masterCountry)) {
			address.setCountry(masterCountry);
		}

		address.setAddressLine1(addressVo.getPermanentAddress().getAddressLine1());
		address.setStreet(addressVo.getPermanentAddress().getStreet());
		address.setAddressType(addressVo.getPermanentAddress().getAddressType());
		address.setIsRental(addressVo.getPermanentAddress().getIsRental());
		address.setLandMark(addressVo.getPermanentAddress().getLandMark());
		address.setOrgId(candidate.getOrgId());
		address.setUpdatedBy(loggedInUserId.toString());
		address.setIsRental(addressVo.getPermanentAddress().getIsRental());
		address.setPincode(addressVo.getPermanentAddress().getPincode());
		if (!HRMSHelper.isNullOrEmpty(addressVo.getPermanentAddress().getIsRental())) {
			if (addressVo.getPermanentAddress().getIsRental().equals(IHRMSConstants.isActive)) {
				address.setOwnerName(addressVo.getPermanentAddress().getOwnerName());
				address.setOwnerContact(addressVo.getPermanentAddress().getOwnerContact());
				address.setOwnerAdhar(addressVo.getPermanentAddress().getOwnerAadhar());
			}
		}
		CandidatePersonalDetail personalDetails = processorDependencies.PersonalDetailDAO.findBycandidate(candidate);
		if (!HRMSHelper.isNullOrEmpty(personalDetails)) {
			personalDetails.setCitizenship(addressVo.getCitizenship());
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	@Override
	public HRMSBaseResponse<?> uploadFile(MultipartFile[] request, FileUploadRequestVO requestVO)
			throws HRMSException, IOException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "File upload");
		}
		HRMSBaseResponse<CurrentEmploymentResponseVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Candidate candidate = processorDependencies.candidateDAO.findByIdAndIsActive(requestVO.getCandidateId(),
				IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			if (orgId == candidate.getOrgId()) {
				if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("uploadFileMethod", role)) {
					if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(requestVO)) {
						ValidationVO validationVO = handleGetDirectOnboardingValidationFlags(
								requestVO.getCandidateId());
						if (validationVO.getIsEmployeeIdCreated().equals(IHRMSConstants.isActive)) {

							processorDependencies.directOnboardAuthorityHelper.uploadFileValidator(requestVO);

							if (HRMSHelper.isNullOrEmpty(request)) {
								throw new HRMSException(IHRMSConstants.FileUploadCode,
										IHRMSConstants.FileUploadErrorMessage);
							}
							if (requestVO.getCandidateId() > 0 && !HRMSHelper.isNullOrEmpty(requestVO.getUploadtype())
									&& !HRMSHelper.isNullOrEmpty(request)) {
								response = uploadFileMethod(request, requestVO, response, loggedInEmpId);
							} else {
								throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
							}

							if (log.isInfoEnabled()) {
								log.info(LogConstants.EXITED.template(), "File upload");
							}
							return response;

						} else {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1622));
						}

					} else
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	private HRMSBaseResponse<CurrentEmploymentResponseVO> uploadFileMethod(MultipartFile[] request,
			FileUploadRequestVO requestVO, HRMSBaseResponse<CurrentEmploymentResponseVO> response, Long loggedInEmpId)
			throws IOException, HRMSException {
		String savePath = processorDependencies.rootDirectory + SecurityFilter.TL_CLAIMS.get().getOrgId();
		// String savePath = "E:" + File.separator + "input" + File.separator +
		// SecurityFilter.TL_CLAIMS.get().getOrgId();
		log.info("savepath : " + savePath);

		SimpleDateFormat df2 = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);
		boolean checkupdate = false;

		for (MultipartFile file : request) {
			log.info("File  " + file.getName());
			log.info("File name " + file.getOriginalFilename());
			log.info("File size " + file.getSize());

			byte[] bytes = file.getBytes();

			String str = "";
			log.info("str : " + str);

			if (file.getSize() > processorDependencies.maxFileSizeUpload) {
				throw new HRMSException(IHRMSConstants.FileUploadSizeLimitCode,
						IHRMSConstants.FileUploadSizeLimitErrorMessage);
			}

			Candidate candidateEntity = processorDependencies.candidateDAO.findById(requestVO.getCandidateId()).get();
			if (!HRMSHelper.isNullOrEmpty(candidateEntity)) {
				str = savePath;

				Path path = Paths.get(str);
				log.info("str : " + str);
				log.info("path : " + path);

				if (!Files.exists(path)) {
					Files.createDirectory(Paths.get(path.toUri()));
				}
				str = path + File.separator + candidateEntity.getCandidateProfessionalDetail().getDivision().getId();
				Path p = Paths.get(str);
				log.info("str : " + str);
				log.info("path : " + p);

				if (!Files.exists(p)) {
					Files.createDirectory(Paths.get(p.toUri()));
				}

				str = str + File.separator + candidateEntity.getCandidateProfessionalDetail().getBranch().getId();
				Path p1 = Paths.get(str);
				log.info("str : " + str);
				log.info("path : " + p1);

				if (!Files.exists(p1)) {
					Files.createDirectory(Paths.get(p1.toUri()));

				}

				str = str + File.separator + requestVO.getCandidateId();
				Path p2 = Paths.get(str);
				log.info("str : " + str);
				log.info("path : " + p2);

				if (!Files.exists(p2)) {
					Files.createDirectory(Paths.get(p2.toUri()));
				}

				if (requestVO.getUploadtype().equalsIgnoreCase("document")) {

					CandidateChecklist candidateCheckList = processorDependencies.CandidateChecklistDAO
							.findCandidateCheckListByProfessionalIdAndIsActive(
									candidateEntity.getCandidateProfessionalDetail().getId(),
									requestVO.getDocumentName(), IHRMSConstants.isActive);

					if (!HRMSHelper.isNullOrEmpty(candidateCheckList)) {

						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1623));

					} else {
						String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();

						if (!EFileExtension.PDF.name().equalsIgnoreCase(fileExtension)
								&& !EFileExtension.JPEG.name().equalsIgnoreCase(fileExtension)
								&& !EFileExtension.JPG.name().equalsIgnoreCase(fileExtension)
								&& !EFileExtension.PNG.name().equalsIgnoreCase(fileExtension)) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1571));
						}
						Files.write(Paths.get(str, file.getOriginalFilename().replaceAll("\\s+", "_")),
								file.getBytes());
						log.info("str : " + str);
						updateChecklistForFileUpload(requestVO, loggedInEmpId, file, candidateEntity);
					}

				} else if (requestVO.getUploadtype().equals("Offer_Letter")
						|| requestVO.getUploadtype().equals("Appointment_Letter")) {
					String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();

					if (!EFileExtension.PDF.name().equalsIgnoreCase(fileExtension)
							&& !EFileExtension.JPEG.name().equalsIgnoreCase(fileExtension)
							&& !EFileExtension.JPG.name().equalsIgnoreCase(fileExtension)
							&& !EFileExtension.PNG.name().equalsIgnoreCase(fileExtension)) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1571));
					}
					uploadOfferAppointmentLetters(requestVO, loggedInEmpId, file, candidateEntity, p2);
				} else {
					String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();

					if (!EFileExtension.JPEG.name().equalsIgnoreCase(fileExtension)
							&& !EFileExtension.JPG.name().equalsIgnoreCase(fileExtension)
							&& !EFileExtension.PNG.name().equalsIgnoreCase(fileExtension)) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1571));
					}
					str = uploadPhoto(file, p2);

				}

			} else {
				throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
						IHRMSConstants.CandidateDoesnotExistMessage);
			}

			FileUploadResponseVo responseVo = setFileUploadResponseVO(file.getOriginalFilename(), str, requestVO);
			response = getBaseResponse(ResponseCode.getResponseCodeMap().get(1200), 1200, responseVo, 0);
		}
		return response;
	}

	private String uploadPhoto(MultipartFile file, Path p2) throws IOException {
		String str;
		str = p2 + File.separator + "Photo";
		Path p3 = Paths.get(str);
		log.info("str : " + str);
		log.info("path : " + p3);

		if (!Files.exists(p3)) {
			Files.createDirectory(p3);
		}

		Files.write(Paths.get(str, file.getOriginalFilename()), file.getBytes());
		log.info("str : " + str);
		return str;
	}

	private void uploadOfferAppointmentLetters(FileUploadRequestVO requestVO, Long loggedInEmpId, MultipartFile file,
			Candidate candidateEntity, Path p2) throws IOException, HRMSException {

		CandidateLetter candletter = processorDependencies.candidateLetterDAO
				.findLetterByCandidateIdAndLetterTypeAndIsActive(requestVO.getCandidateId(),
						requestVO.getDocumentName(), IHRMSConstants.isActive);

		if (HRMSHelper.isNullOrEmpty(candletter)) {
			String letterURL = p2 + File.separator + "letters";

			Path letterPath = Paths.get(letterURL);
			log.info("URL : " + letterURL);
			log.info("path : " + letterPath);

			if (!Files.exists(letterPath)) {
				Files.createDirectory(letterPath);
			}

			String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
			String fileName = "CAND_" + candidateEntity.getId() + "_" + requestVO.getUploadtype() + "." + fileExtension;

			Files.write(Paths.get(letterURL, fileName), file.getBytes());

//			Files.write(Paths.get(letterURL, fileName), file.getBytes());
//			log.info("URL : " + letterURL);

			String URL = File.separator + "input" + File.separator
					+ candidateEntity.getLoginEntity().getOrganization().getId() + File.separator
					+ candidateEntity.getCandidateProfessionalDetail().getDivision().getId() + File.separator
					+ candidateEntity.getCandidateProfessionalDetail().getBranch().getId() + File.separator
					+ candidateEntity.getId() + File.separator + "letters" + File.separator;

			CandidateLetter letter = new CandidateLetter();
			letter.setCreatedBy(loggedInEmpId.toString());
			letter.setCreatedDate(new Date());
			letter.setCandidate(candidateEntity);
			letter.setFileName(fileName);
			letter.setIsActive(IHRMSConstants.isActive);
			letter.setLetterType(requestVO.getDocumentName());
			letter.setLetterUrl(URL);
			letter.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
			processorDependencies.candidateLetterDAO.save(letter);
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1623));
		}
	}

	private void updateChecklistForFileUpload(FileUploadRequestVO requestVO, Long loggedInEmpId, MultipartFile file,
			Candidate candidateEntity) throws HRMSException {
		CandidateChecklist checklistEntity = new CandidateChecklist();
		checklistEntity.setAttachment(file.getOriginalFilename().replaceAll("\\s+", "_"));
		checklistEntity.setChecklistItem(requestVO.getDocumentName());
		checklistEntity.setUpdatedDateTime(new Date());
		checklistEntity.setSubmitted(IHRMSConstants.SubmitDocument);
		checklistEntity.setMandatory(requestVO.getMandatory());
		checklistEntity.setCreatedBy(loggedInEmpId.toString());
		checklistEntity.setCreatedDate(new Date());
		checklistEntity.setIsActive(IHRMSConstants.isActive);
		checklistEntity.setHrValidationStatus(false);
		checklistEntity.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		checklistEntity.setCandidateProfessionalDetail(candidateEntity.getCandidateProfessionalDetail());

		if (requestVO.getDocumentName().equalsIgnoreCase("SALARY ANNEXURE")) {
			checklistEntity.setIsDocumentOnlyForCandidate(0);
		} else {
			checklistEntity.setIsDocumentOnlyForCandidate(1);
		}
		checklistEntity.setIsDocumentOnlyForEmployee(1);

		processorDependencies.CandidateChecklistDAO.save(checklistEntity);

	}

	private void setChecklistForFileUpload(FileUploadRequestVO requestVO, Long loggedInEmpId, MultipartFile file,
			Candidate candidateEntity, CandidateChecklist candidateCheckList,
			MasterCandidateChecklistAction pendingCandidateChecklistAction) throws HRMSException {
		candidateCheckList.setAttachment(file.getOriginalFilename().replaceAll("\\s+", "_"));
		candidateCheckList.setChecklistItem(requestVO.getDocumentName());
		candidateCheckList.setUpdatedDateTime(new Date());
		candidateCheckList.setSubmitted(IHRMSConstants.SubmitDocument);
		candidateCheckList.setMandatory(requestVO.getMandatory());
		candidateCheckList.setUpdatedBy(loggedInEmpId.toString());
		candidateCheckList.setUpdatedDate(new Date());
		candidateCheckList.setIsActive(IHRMSConstants.isActive);
		candidateCheckList.setCandidateProfessionalDetail(candidateEntity.getCandidateProfessionalDetail());
		candidateCheckList.setHrValidationStatus(false);
		candidateCheckList.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());

		if (!HRMSHelper.isNullOrEmpty(pendingCandidateChecklistAction)) {
			candidateCheckList.setMasterCandidateChecklistAction(pendingCandidateChecklistAction);
			processorDependencies.CandidateChecklistDAO.save(candidateCheckList);

		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}
	}

	private FileUploadResponseVo setFileUploadResponseVO(String fileName, String path, FileUploadRequestVO requestVo) {
		FileUploadResponseVo responseVO = new FileUploadResponseVo();
		responseVO.setDocument_name(requestVo.getDocumentName());
		responseVO.setFileName(fileName);
		responseVO.setSubmited(IHRMSConstants.SubmitDocument);
		responseVO.setPath(path);
		responseVO.setCandidateId(requestVo.getCandidateId());
		return responseVO;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public HRMSBaseResponse<?> deleteFile(DeleteFileRequestVO request) throws HRMSException, IOException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "delete File");
		}
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		String fileName = null;
		String url = null;

		if (processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("deleteFile", role)) {
			if (!HRMSHelper.isNullOrEmpty(request)) {
				processorDependencies.directOnboardAuthorityHelper.deleteFileValidator(request);
				Candidate candidate = processorDependencies.candidateDAO
						.findByIdAndIsActiveAndorgId(request.getCandidateId(), IHRMSConstants.isActive, orgId);
				if (HRMSHelper.isNullOrEmpty(candidate)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}
				CandidateLetter candidateLetter = processorDependencies.candidateLetterDAO
						.findLetterByCandidateIdAndLetterType(request.getCandidateId(), request.getDocumentType());
				if (!HRMSHelper.isNullOrEmpty(candidateLetter)) {
					if (candidateLetter.getLetterType().equals(IHRMSConstants.Offer_Letter)
							|| candidateLetter.getLetterType().equals(IHRMSConstants.Appointment_Letter)
							|| candidateLetter.getLetterType().equals(IHRMSConstants.Experience_Letter)
							|| candidateLetter.getLetterType().equals(IHRMSConstants.Relieving_Letter)
									&& candidateLetter.getIsActive().equals(IHRMSConstants.isActive)) {

						processorDependencies.candidateLetterDAO.delete(candidateLetter);

						fileName = candidateLetter.getFileName();
//						url = processorDependencies.baseURL + candidateLetter.getLetterUrl()
//								+ candidateLetter.getFileName();

						url = processorDependencies.getRootDirectory()
								+ candidateLetter.getLetterUrl().replace("/input/", "") + candidateLetter.getFileName();

						Path path = Paths.get(url);
						log.info(" file path:" + url);
						Files.delete(path);

					}
				} else if (!HRMSHelper.isNullOrEmpty(candidate)
						&& !HRMSHelper.isNullOrEmpty(candidate.getCandidateProfessionalDetail())) {
					CandidateChecklist checklist = processorDependencies.CandidateChecklistDAO
							.findCandidateCheckListByProfessionalId(candidate.getCandidateProfessionalDetail().getId(),
									request.getDocumentType());

					if (!HRMSHelper.isNullOrEmpty(checklist)
							&& checklist.getIsActive().equals(IHRMSConstants.isActive)) {
						processorDependencies.CandidateChecklistDAO.delete(checklist);

						fileName = checklist.getAttachment();
						url = processorDependencies.rootDirectory + candidate.getLoginEntity().getOrganization().getId()
								+ File.separator + candidate.getCandidateProfessionalDetail().getDivision().getId()
								+ File.separator + candidate.getCandidateProfessionalDetail().getBranch().getId()
								+ File.separator + candidate.getId() + File.separator + checklist.getAttachment();

						Path path = Paths.get(url);
						log.info(" file path:" + url);
						Files.delete(path);
					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1624));
					}
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}
			if (log.isInfoEnabled()) {
				log.info(LogConstants.EXITED.template(), "delete File");
			}
			response = getBaseResponse(ResponseCode.getResponseCodeMap().get(1619), 1200, null, 0);
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		return response;
	}

	// *******************excel upload**********************

	@Transactional(rollbackOn = { Exception.class })
	@Override
	public HRMSBaseResponse<String> uploadExcelFile(MultipartFile file)
			throws HRMSException, IOException, NoSuchAlgorithmException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "Excel File upload");
		}
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		Workbook workbook = excelUploadMethod(file);
		response.setResponseBody("Data uploaded successfully");
		response.setResponseCode(EResponse.SUCCESS.getCode());
		response.setResponseMessage(EResponse.SUCCESS.getMessage());

		workbook.close();

		return response;

	}

	private Workbook excelUploadMethod(MultipartFile file) throws IOException, HRMSException, NoSuchAlgorithmException {
		Workbook workbook;
		String fileName = file.getOriginalFilename().toLowerCase();
		if (fileName.contains(".xlsx") || fileName.contains(".xlsm")) {
			workbook = new XSSFWorkbook(file.getInputStream());
		} else if (fileName.contains(".xls")) {
			workbook = new HSSFWorkbook(file.getInputStream());
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1571));
		}

		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.iterator();
		Counter counter = new Counter();
		List<ExcelDataVo> excelDataListVO = new ArrayList<>();
		int rowCount = 0;

		rows.forEachRemaining((row) -> {

			if (counter.counterValue > 0) {
				setExcelDataToVo(excelDataListVO, row);
			}

			counter.counterValue++;
		});

		System.out.println("Data:::" + excelDataListVO.toString());
		LoginEntity login = null;
		for (ExcelDataVo excelData : excelDataListVO) {
			System.out.println("excelData::" + excelData.toString());
			if (!HRMSHelper.isNullOrEmpty(excelData.getEmail())) {
				log.info("inserting  entry");

				login = setLoginEntityDetails(excelData);
				Candidate candidate = setCandidateDetails(login, excelData);
				setCandidatePersonalDetails(excelData, candidate);

				MasterDesignation masterDesignation = processorDependencies.designationDAO
						.findDesignationNameAndIsActive(excelData.getDesignationId().trim(), IHRMSConstants.isActive);
				MasterCity masterCity = processorDependencies.cityDAO.findByCityName(excelData.getCityId(),
						IHRMSConstants.isActive);
				MasterCountry masterCountry = processorDependencies.countryDAO.findByName(excelData.getContryId(),
						IHRMSConstants.isActive);
				MasterState masterState = processorDependencies.stateDAO.findByName(excelData.getStateId(),
						IHRMSConstants.isActive);
				MasterDepartment department = processorDependencies.departmentDAO
						.findByName(excelData.getDepartmentId(), IHRMSConstants.isActive);
				MasterBranch branch = processorDependencies.branchDAO.findByName(excelData.getBranchId(),
						IHRMSConstants.isActive);

				MasterDivision division = processorDependencies.divisionDAO.findByName(excelData.getDivisionId(),
						IHRMSConstants.isActive);

				// proffessional details
				setCandidateProfessionalDetails(excelData, candidate, masterDesignation, masterCity, masterCountry,
						masterState, department, branch, division);
				// employee
				Employee emp1 = setEmployeeDetails(excelData, candidate);
				// employeecurrentDetails
				setCurrentDetails(emp1);

			}

		}
		return workbook;
	}

	private LoginEntity setLoginEntityDetails(ExcelDataVo excelData) throws NoSuchAlgorithmException {
		LoginEntity login;
		LoginEntityType employeeRole = processorDependencies.loginTypeDAO.findByRoleName(ERole.EMPLOYEE.name(),
				SecurityFilter.TL_CLAIMS.get().getOrgId());
		LoginEntityType role = processorDependencies.loginTypeDAO.findByRoleName(excelData.getRole(),
				SecurityFilter.TL_CLAIMS.get().getOrgId());
		Set<LoginEntityType> loginEntityTypes = new HashSet<LoginEntityType>();

		if (excelData.getRole().equalsIgnoreCase(ERole.EMPLOYEE.name())) {
			loginEntityTypes.add(employeeRole);
		} else {
			loginEntityTypes.add(employeeRole);
			loginEntityTypes.add(role);
		}
		String randomPassword = HRMSHelper.randomString();
		String encryptedString = HRMSHelper.encryptToSHA256(randomPassword);
		login = new LoginEntity();
		login.setCreatedBy("System");
		login.setCreatedDate(new Date());
		login.setIsActive(IHRMSConstants.isActive);
		login.setIsFirstLogin(IHRMSConstants.isNotActive);
		login.setLoginEntityName(excelData.getFirstName() + " " + excelData.getLastName());
		login.setFirstPassword(randomPassword);
		login.setPassword(encryptedString);
		login.setPrimaryEmail(excelData.getEmail());
		login.setUsername(excelData.getEmail());
		login.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		login.setLoginEntityTypes(loginEntityTypes);

		processorDependencies.logDao.save(login);
		return login;
	}

	private void setCurrentDetails(Employee emp1) {
		EmployeeCurrentDetail empdetails = new EmployeeCurrentDetail();
		empdetails.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		empdetails.setCreatedBy("System");
		empdetails.setCreatedDate(new Date());
		empdetails.setIsActive(IHRMSConstants.isActive);
		empdetails.setRetirementDate(null);
		empdetails.setNoticePeriod(60);
		empdetails.setEmployee(emp1);
		processorDependencies.currentEmploymentDAO.save(empdetails);
	}

	private Employee setEmployeeDetails(ExcelDataVo excelData, Candidate candidate) {
		Employee emp = new Employee();
		Employee emp1 = new Employee();
		emp.setCreatedBy("System");
		emp.setCreatedDate(new Date());
		emp.setIsActive(IHRMSConstants.isActive);
		emp.setOfficialEmailId(excelData.getEmail());
		emp.setCandidate(candidate);
		// emp.setProbationPeriod(excelData.getProbationPeriod());
		emp.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		emp.setOfficialMobileNumber(excelData.getMobileNo());
		// emp.setEmployeeCode(excelData.getEmployeeCode());
		emp.setEmployeeCode(String.valueOf(excelData.getEmployeeCode()));
		emp.setId(excelData.getEmployeeCode());
		emp1 = processorDependencies.employeeDAO.save(emp);
		return emp1;
	}

	private void setCandidateProfessionalDetails(ExcelDataVo excelData, Candidate candidate,
			MasterDesignation masterDesignation, MasterCity masterCity, MasterCountry masterCountry,
			MasterState masterState, MasterDepartment department, MasterBranch branch, MasterDivision division) {
		CandidateProfessionalDetail profDetails = new CandidateProfessionalDetail();
		profDetails.setCreatedBy("System");
		profDetails.setCreatedDate(new Date());
		profDetails.setIsActive(IHRMSConstants.isActive);
		profDetails.setDateOfJoining(excelData.getDateOfJoining());
		profDetails.setBranch(branch);
		profDetails.setCandidate(candidate);
		profDetails.setCity(masterCity);
		profDetails.setCountry(masterCountry);
		profDetails.setDepartment(department);
		profDetails.setDesignation(masterDesignation);
		profDetails.setDivision(division);
		profDetails.setState(masterState);
		profDetails.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		processorDependencies.professionalDAO.save(profDetails);
	}

	private void setCandidatePersonalDetails(ExcelDataVo excelData, Candidate candidate) {
		CandidatePersonalDetail personal = new CandidatePersonalDetail();
		personal.setCreatedBy("System");
		personal.setCreatedDate(new Date());
		personal.setIsActive(IHRMSConstants.isActive);
		personal.setMaritalStatus(excelData.getMarritalStatus());
		personal.setOfficialMobileNumber(excelData.getMobileNo().toString());
		personal.setOrgId((SecurityFilter.TL_CLAIMS.get().getOrgId()));
		personal.setCandidate(candidate);
		processorDependencies.PersonalDetailDAO.save(personal);
	}

	private Candidate setCandidateDetails(LoginEntity login, ExcelDataVo excelData) {
		Candidate candidate = new Candidate();
		candidate.setCreatedBy("System");
		candidate.setCreatedDate(new Date());
		candidate.setIsActive(IHRMSConstants.isActive);
		candidate.setCandidateStatus("CAND_ISEMP");
		candidate.setDateOfBirth(excelData.getDob());
		candidate.setEmailId(excelData.getEmail());
		candidate.setFirstName(excelData.getFirstName());
		candidate.setMiddleName(excelData.getMiddleName());
		candidate.setLastName(excelData.getLastName());
		candidate.setGender(excelData.getGender());
		candidate.setMobileNumber(excelData.getMobileNo());
		candidate.setTitle(excelData.getSalutation());
		candidate.setLoginEntity(login);
		candidate.setNoticePeriod(excelData.getNoticePeriod());
		candidate.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		processorDependencies.candidateDAO.save(candidate);
		return candidate;
	}

	private void setExcelDataToVo(List<ExcelDataVo> excelDataListVO, Row row) {
		ExcelDataVo vo = new ExcelDataVo();

		// vo.setSrNo(row);
		vo.setSalutation(processorDependencies.getCommonMethod().mapSalutation(row));
		vo.setFirstName(processorDependencies.getCommonMethod().mapFirstName(row));
		vo.setMiddleName(processorDependencies.getCommonMethod().mapMiddleName(row));
		vo.setLastName(processorDependencies.getCommonMethod().mapLastName(row));
		vo.setDob(processorDependencies.getCommonMethod().mapDob(row));
		vo.setGender(processorDependencies.getCommonMethod().mapGender(row));
		vo.setMarritalStatus(processorDependencies.getCommonMethod().mapMarritalStatus(row));
		vo.setEmploymentTypeId(processorDependencies.getCommonMethod().mapEmploymentType(row));
		vo.setEmployeeCode(processorDependencies.getCommonMethod().mapEmploymeeCode(row));
		vo.setDesignationId(processorDependencies.getCommonMethod().mapDesignation(row));
		vo.setRole(processorDependencies.getCommonMethod().mapRole(row));
		vo.setDateOfJoining(processorDependencies.getCommonMethod().mapDoj(row));
		vo.setNoticePeriod(processorDependencies.getCommonMethod().mapNoticePeriod(row));
		vo.setProbationPeriod(processorDependencies.getCommonMethod().mapProbPeriod(row));
		vo.setBranchId(processorDependencies.getCommonMethod().mapBranch(row));
		vo.setContryId(processorDependencies.getCommonMethod().mapCountry(row));
		vo.setStateId(processorDependencies.getCommonMethod().mapState(row));
		vo.setCityId(processorDependencies.getCommonMethod().mapCity(row));
		vo.setDepartmentId(processorDependencies.getCommonMethod().mapDept(row));
		vo.setDivisionId(processorDependencies.getCommonMethod().mapDiv(row));
		vo.setReportingManager(processorDependencies.getCommonMethod().mapRm(row));
		vo.setEmail(processorDependencies.getCommonMethod().mapEmail(row));
		vo.setMobileNo(processorDependencies.getCommonMethod().mapMobile(row));

		System.out.println(vo.toString());
		excelDataListVO.add(vo);
	}

	@Override
	public HRMSBaseResponse<?> candidateEmailIdCheck(EmailCheckVo request) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "email Check method");
		}
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (HRMSHelper.isRolePresent(role, ERole.HR.name())) {

			if (!HRMSHelper.isNullOrEmpty(request)) {

				checkIfCandidateEmailAlredyExist(request);

				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setApplicationVersion(processorDependencies.applicationVersion);

				if (log.isInfoEnabled()) {
					log.info(LogConstants.EXITED.template(), "email Check method");
				}
				return response;

			} else
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private void checkIfCandidateEmailAlredyExist(EmailCheckVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request.getEmailId())) {
			List<Candidate> cand = processorDependencies.candidateDAO.findByEmailLike(request.getEmailId());

			if (!HRMSHelper.validateEmailFormat(request.getEmailId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Email Id");
			}

			if (!HRMSHelper.isNullOrEmpty(cand)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1615));
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Email Id");
		}

	}

	@Override
	public HRMSBaseResponse<?> employeeEmailIdCheck(EmailCheckVo request) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "email Check method");
		}
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (HRMSHelper.isRolePresent(role, ERole.HR.name())) {

			if (!HRMSHelper.isNullOrEmpty(request)) {

				checkIfEmployeeEmailAlredyExist(request);

				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setApplicationVersion(processorDependencies.applicationVersion);

				if (log.isInfoEnabled()) {
					log.info(LogConstants.EXITED.template(), "email Check method");
				}
				return response;

			} else
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private void checkIfEmployeeEmailAlredyExist(EmailCheckVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request.getEmailId())) {
			List<Employee> employee = processorDependencies.employeeDAO.findByEmailLike(request.getEmailId());

			if (!HRMSHelper.validateEmailFormat(request.getEmailId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Official Email Id");
			}

			if (!HRMSHelper.isNullOrEmpty(employee)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1615));
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Official Email Id");
		}

	}

	@Override
	public HRMSBaseResponse<?> employeeCodeCheck(EmailCheckVo request) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "validate employee code method");
		}
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (HRMSHelper.isRolePresent(role, ERole.HR.name())) {

			if (!HRMSHelper.isNullOrEmpty(request)) {

				checkIfEmployeeCodeAlredyExist(request);

				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setApplicationVersion(processorDependencies.applicationVersion);

				if (log.isInfoEnabled()) {
					log.info(LogConstants.EXITED.template(), "validate employee code method");
				}
				return response;

			} else
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private void checkIfEmployeeCodeAlredyExist(EmailCheckVo request) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(request.getEmpCode())) {
			List<Employee> employee = processorDependencies.employeeDAO.findByEmpCodeLike(request.getEmpCode());

			if (!HRMSHelper.regexMatcher(request.getEmpCode(), IHRMSConstants.EMP_CODE_REGEX)) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee Code ");
			}

			if (!HRMSHelper.isNullOrEmpty(employee)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1769));
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee Code");
		}

	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public HRMSBaseResponse<?> updateProfileDetails(ProfileDetailsRequestVO request)
			throws HRMSException, ParseException, NoSuchAlgorithmException {

		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "update Employee profile details");
		}

		Long loggedInEmployee = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<ProfileDetailsResponseVO> response = new HRMSBaseResponse<>();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!processorDependencies.authorizationServiceImpl.isAuthorizedFunctionName("updateProfileDetails", roles)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (HRMSHelper.isNullOrEmpty(request) || HRMSHelper.isNullOrEmpty(request.getOfficialEmailId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
		}

		processorDependencies.directOnboardAuthorityHelper.profileDetailsInputValidationForUpdate(request);
		checkCandidateMailIfAlreadyExist(request);

		Employee emp = processorDependencies.employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), request.getId());
		if (emp == null || emp.getCandidate() == null || emp.getCandidate().getLoginEntity() == null) {
			throw new HRMSException(1500, "Team Member Details  Not found or Inactive.");
		}

		LoginEntity loginEntity = processorDependencies.logDao.findByLoginEntityId(ERecordStatus.Y.name(),
				emp.getCandidate().getLoginEntity().getId());

		if (loginEntity == null) {
			throw new HRMSException(1500, "Team Member Details  Not found or Inactive.");
		}

		String roleUpdatedFlag = "N";
		if (!HRMSHelper.isNullOrEmpty(request.getRoles())) {
			Set<Long> requestedRoleIds = request.getRoles().stream().map(LoginEntityTypeVO::getId)
					.filter(id -> !HRMSHelper.isLongZero(id)).collect(Collectors.toSet());

			Set<Long> existingRoleIds = Optional.ofNullable(loginEntity.getLoginEntityTypes())
					.orElse(Collections.emptySet()).stream().map(LoginEntityType::getId).filter(Objects::nonNull)
					.collect(Collectors.toSet());

			if (!existingRoleIds.equals(requestedRoleIds) && loggedInEmployee.equals(request.getId())) {
				roleUpdatedFlag = "Y";
			}
		}

		updateEmployeeDetails(request);

		ProfileDetailsResponseVO responseVO = new ProfileDetailsResponseVO();
		responseVO.setId(request.getId());
		responseVO.setRoleUpdated(roleUpdatedFlag);

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1853));
		response.setApplicationVersion(processorDependencies.applicationVersion);
		response.setResponseBody(responseVO);

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "update profile details");
		}

		return response;
	}

	private Candidate updateEmployeeDetails(ProfileDetailsRequestVO request)
			throws HRMSException, NoSuchAlgorithmException {
		Long empId = request.getId();
		checkEmployeeEmailCodeIfAlreadyExistForUpdate(request);
		Candidate candidateNew = updateCandidateAndLoginEntityDetails(request, empId);
		CandidateProfessionalDetail candProfDetails = updateCandidatePorfDetails(request, candidateNew, empId);
		updateCandidatePersonalDetails(request, candidateNew, empId);
		updateCandidateAddress(request, empId, candidateNew, candProfDetails);

		Employee employee = updateEmployeeAndCurrentDetails(request, candidateNew);
		updateEmployeeDepartmentMpping(candProfDetails, employee);
		updateEmployeeDesignationMapping(candProfDetails, employee);
		updateEmployeeBranchMapping(candProfDetails, employee);
		updateEmployeeDivisionMapping(candProfDetails, employee);

/////
		addOrUpdateMcMemberKpiVisibility(request);

		return candidateNew;

	}

	private void addOrUpdateMcMemberKpiVisibility(ProfileDetailsRequestVO request) throws HRMSException {
		Employee empCode = processorDependencies.employeeDAO.findByEmpCode(request.getEmployeeCode());
		if (!HRMSHelper.isNullOrEmpty(empCode)) {
			if (request.getIsKpiVisible().equals(IHRMSConstants.isActive)) {

				List<HrToHodMap> hr = processorDependencies.hrtohodMapDao.findByHrIdAndIsActive(empCode.getId(),
						IHRMSConstants.isActive);
				if (HRMSHelper.isNullOrEmpty(hr)) {
					List<Long> hodList = processorDependencies.hodToDeptMapDao
							.findAllHodByIsActive(IHRMSConstants.isActive);

					if (!HRMSHelper.isNullOrEmpty(hodList)) {
						for (Long empId : hodList) {

							HrToHodMap hrToHod = new HrToHodMap();
							hrToHod.setHodEmployeeId(empId);
							hrToHod.setHrEmployeeId(empCode.getId());
							hrToHod.setIsActive(IHRMSConstants.isActive);
							hrToHod.setCreatedDate(new Date());
							hrToHod.setOrgId(empCode.getOrgId());
							processorDependencies.hrtohodMapDao.save(hrToHod);
						}

					} else {
						throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
					}
				}
			} else { // if flag is N

				List<HrToHodMap> hrList = processorDependencies.hrtohodMapDao.findByHrIdAndIsActive(empCode.getId(),
						IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(hrList)) {

					for (HrToHodMap map : hrList) {
						map.setIsActive(IHRMSConstants.isNotActive);
						processorDependencies.hrtohodMapDao.save(map);
					}

				}

			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	private void checkEmployeeEmailCodeIfAlreadyExistForUpdate(ProfileDetailsRequestVO request) throws HRMSException {
		Long employeeId = request.getId();
		if (HRMSHelper.isNullOrEmpty(employeeId)) {
			throw new HRMSException(1500, "Team Member ID is required for update.");
		}

		// Fetch the current employee and candidate
		Employee currentEmployee = processorDependencies.employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(),
				employeeId);
		if (currentEmployee == null) {
			throw new HRMSException(1500, "Team Member not found for ID: " + employeeId);
		}

		Long currentCandidateId = currentEmployee.getCandidate() != null ? currentEmployee.getCandidate().getId()
				: null;

		// 1. Check official email conflict (active only)
		if (!HRMSHelper.isNullOrEmpty(request.getOfficialEmailId())) {
			Employee existingEmp = processorDependencies.employeeDAO
					.findByofficialEmailIdAndIsActive(request.getOfficialEmailId(), IHRMSConstants.isActive);
			if (existingEmp != null && !existingEmp.getId().equals(employeeId)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1615));
			}

			// Also check if login username is used by someone else
			LoginEntity loginDetail = processorDependencies.logDao
					.findByUsername(currentEmployee.getCandidate().getLoginEntity().getUsername());
			if (loginDetail != null && !loginDetail.getUsername()
					.equals(currentEmployee.getCandidate().getLoginEntity().getUsername())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1615));
			}
		}

		// 2. Check if employee code is already used by someone else
		if (!HRMSHelper.isNullOrEmpty(request.getEmployeeCode())) {
			List<Employee> empCodeList = processorDependencies.employeeDAO.findByEmpCodeLike(request.getEmployeeCode());
			for (Employee e : empCodeList) {
				if (!e.getId().equals(employeeId)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1769));
				}
			}
		}

		// 3. Check if mobile number is used by another candidate
		if (!HRMSHelper.isNullOrEmpty(request.getMobileNumber()) && !HRMSHelper.isLongZero(request.getMobileNumber())) {
			List<Candidate> candidates = processorDependencies.candidateDAO
					.findByMobileNumberAndIsActive(request.getMobileNumber(), IHRMSConstants.isActive);
			for (Candidate c : candidates) {
				if (!c.getId().equals(currentCandidateId)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1616));
				}
			}
		}
	}

	private Candidate updateCandidateAndLoginEntityDetails(ProfileDetailsRequestVO request, Long empId)
			throws HRMSException {

		if (empId == null) {
			throw new HRMSException(1500, "Team Member ID is required for candidate update.");
		}

		// Fetch the employee
		Employee employee = processorDependencies.employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), empId);
		if (employee == null) {
			throw new HRMSException(1500, "Team Member not found for ID: " + empId);
		}

		// Fetch the existing candidate from the employee
		Candidate candidate = employee.getCandidate();
		if (candidate == null) {
			throw new HRMSException(1500, "Candidate details not found for this Team Member.");
		}

		// Update candidate fields from request
		updateCandidateDetails(request, candidate);

		// Fetch and update the login entity
		LoginEntity loginEntity = candidate.getLoginEntity();
		if (loginEntity == null) {
			throw new HRMSException(1500, "Login details not found for Team Member.");
		}

		loginEntity = updateCandidateLoginDetails(request, empId);
		candidate.setLoginEntity(loginEntity);

		Organization organization = processorDependencies.organizationDAO
				.findById(SecurityFilter.TL_CLAIMS.get().getOrgId())
				.orElseThrow(() -> new HRMSException(1500, "Organization not found."));

		loginEntity.setOrganization(organization);
		candidate.setOrgId(organization.getId());

		processorDependencies.candidateDAO.save(candidate);

		return candidate;
	}

	private Employee updateEmployeeAndCurrentDetails(ProfileDetailsRequestVO request, Candidate candidate)
			throws NoSuchAlgorithmException, HRMSException {

		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (request.getId() == null || request.getId() <= 0) {
			log.error("Team Member ID is missing or invalid in the request");
			throw new HRMSException(1500, "Team Member ID is required for update.");
		}

		Long empId = request.getId();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		log.info("Updating employee details for Team Member: {}", empId);

		Candidate cand = processorDependencies.candidateDAO.findByIdAndIsActive(candidate.getId(),
				IHRMSConstants.isActive);

		if (HRMSHelper.isNullOrEmpty(cand)) {
			log.error("Candidate not found or inactive for ID: {}", candidate.getId());
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		if (!orgId.equals(cand.getOrgId())) {
			log.error("Organization ID mismatch. Request Org: {}, Candidate Org: {}", orgId, cand.getOrgId());
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee emp = processorDependencies.employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), empId);

		if (emp == null) {
			log.error("Team Member not found for ID: {}", empId);
			throw new HRMSException(1500, "Employee not found for ID: " + empId);
		}

		emp.setUpdatedBy(loggedInEmpId.toString());
		emp.setUpdatedDate(new Date());
		emp.setIsActive(IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(request.getOfficialEmailId())) {
			emp.setOfficialEmailId(request.getOfficialEmailId());
		}

		if (!HRMSHelper.isNullOrEmpty(request.getOfficialMobileNumber())) {
			emp.setOfficialMobileNumber(request.getOfficialMobileNumber());
		}

		if (!HRMSHelper.isNullOrEmpty(request.getEmployeeCode())) {
			emp.setEmployeeCode(request.getEmployeeCode());
		}

		if (request.getIsCycleAllowed() != null) {
			emp.setCycleAllowed(request.getIsCycleAllowed());
		}

		if (cand.getLoginEntity() != null && cand.getLoginEntity().getOrganization() != null) {
			emp.setOrgId(cand.getLoginEntity().getOrganization().getId());
		}

		if (request.getProbationPeriod() != null) {
			emp.setProbationPeriod(request.getProbationPeriod().toString());
		}

		emp.setCandidate(cand);

		emp = processorDependencies.employeeDAO.save(emp);
		log.info("Team Member record updated for Team Member Id: {}", empId);

		updateEmployeeReportingManager(request, emp, loggedInEmpId);
		updateEmployeeCurrentDetails(request, emp, cand, loggedInEmpId);

		return emp;
	}

	private void updateEmployeeReportingManager(ProfileDetailsRequestVO request, Employee emp, Long loggedInEmpId) {
		if (request.getReportingManager() == null || request.getReportingManager().getId() == null) {
			log.warn("No reporting manager provided for Team Member Id: {}", emp.getId());
			return;
		}

		Long reportingManagerId = request.getReportingManager().getId();

		Employee reportingManager = processorDependencies.employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(),
				reportingManagerId);

		if (reportingManager == null) {
			log.error("Reporting Manager not found or inactive for ID: {}", reportingManagerId);
			return;
		}

		EmployeeReportingManager employeeReportingMgr = processorDependencies.reportingManagerDAO
				.findByEmployeeAndIsActive(emp.getId(), ERecordStatus.Y.name());

		if (HRMSHelper.isNullOrEmpty(employeeReportingMgr)) {
			log.warn("No existing reporting manager mapping found for Team Member Id: {}. Skipping update.",
					emp.getId());
			return;
		}

		employeeReportingMgr.setReporingManager(reportingManager);
		employeeReportingMgr.setUpdatedBy(loggedInEmpId.toString());
		employeeReportingMgr.setUpdatedDate(new Date());
		employeeReportingMgr.setIsActive(ERecordStatus.Y.name());

		processorDependencies.reportingManagerDAO.save(employeeReportingMgr);
		log.info("Updated reporting manager for Team Member Id: {} to managerId: {}", emp.getId(), reportingManagerId);
	}

	private void updateEmployeeCurrentDetails(ProfileDetailsRequestVO request, Employee emp, Candidate cand,
			Long empId) {
		// Fetch existing current detail by employee ID
		EmployeeCurrentDetail current = processorDependencies.currentEmploymentDAO
				.findByEmpIdAndIsActive(request.getId(), ERecordStatus.Y.name());

		if (HRMSHelper.isNullOrEmpty(current)) {
			log.warn("No existing EmployeeCurrentDetail found for Team Member Id: {}. Skipping update.",
					request.getId());
			return;
		}

		current.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		current.setUpdatedDate(new Date());
		current.setIsActive(IHRMSConstants.isActive);

		current.setNoticePeriod(!HRMSHelper.isNullOrEmpty(request.getNoticePeriod()) ? request.getNoticePeriod() : 0);

		current.setRetirementDate(!HRMSHelper.isNullOrEmpty(request.getRetirementDate())
				? HRMSDateUtil.parse(request.getRetirementDate(), IHRMSConstants.FRONT_END_DATE_FORMAT)
				: null);

		current.setOrgId(cand.getLoginEntity().getOrganization().getId());
		current.setEmployee(emp);

		processorDependencies.currentEmploymentDAO.save(current);
		log.info("Updated Team Member CurrentDetail for Team Member Id: {}", emp.getId());
	}

	private void updateEmployeeDepartmentMpping(CandidateProfessionalDetail candProfDetails, Employee employee) {
		if (HRMSHelper.isNullOrEmpty(employee) || HRMSHelper.isNullOrEmpty(employee.getId())) {
			log.error("Team Member or Team Member ID is null. Cannot update department mapping.");
			return;
		}

		if (HRMSHelper.isNullOrEmpty(candProfDetails)) {
			log.error("CandidateProfessionalDetail is null. Cannot update department mapping for Team Member Id: {}",
					employee.getId());
			return;
		}

		EmployeeDepartment department = processorDependencies.employeeDepartmentDAO.findByEmployee(employee);

		if (HRMSHelper.isNullOrEmpty(department)) {
			log.warn("No existing EmployeeDepartment mapping found for employeeId: {}. Skipping update.",
					employee.getId());
			return;
		}

		department.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		department.setUpdatedDate(new Date());
		department.setIsActive(IHRMSConstants.isActive);
		department.setOrgId(employee.getOrgId());
		department.setEmployee(employee);

		if (!HRMSHelper.isNullOrEmpty(candProfDetails.getDepartment())
				&& !HRMSHelper.isNullOrEmpty(candProfDetails.getDepartment().getId())) {

			department.setDepartment(candProfDetails.getDepartment());
			log.info("Updated department to ID: {} for employeeId: {}", candProfDetails.getDepartment().getId(),
					employee.getId());
		} else {
			log.warn("Department or department ID is missing in CandidateProfessionalDetail for employeeId: {}",
					employee.getId());
			department.setDepartment(null);
		}

		processorDependencies.employeeDepartmentDAO.save(department);
		log.info("EmployeeDepartment mapping successfully updated for employeeId: {}", employee.getId());
	}

	private void updateEmployeeDesignationMapping(CandidateProfessionalDetail candProfDetails, Employee employee) {
		if (HRMSHelper.isNullOrEmpty(employee) || HRMSHelper.isNullOrEmpty(employee.getId())) {
			log.error("Employee or employee ID is null. Cannot update designation mapping.");
			return;
		}

		if (HRMSHelper.isNullOrEmpty(candProfDetails)) {
			log.error("CandidateProfessionalDetail is null. Cannot update designation mapping for employeeId: {}",
					employee.getId());
			return;
		}

		// Fetch existing active designation mapping for the employee
		EmployeeDesignation designation = processorDependencies.employeeDesignationDAO.findByEmployee(employee);

		if (HRMSHelper.isNullOrEmpty(designation)) {
			log.warn("No existing EmployeeDesignation mapping found for employeeId: {}. Skipping update.",
					employee.getId());
			return;
		}

		designation.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		designation.setUpdatedDate(new Date());
		designation.setIsActive(IHRMSConstants.isActive);
		designation.setOrgId(employee.getOrgId());
		designation.setEmployee(employee);

		if (!HRMSHelper.isNullOrEmpty(candProfDetails.getDesignation())
				&& !HRMSHelper.isNullOrEmpty(candProfDetails.getDesignation().getId())) {

			designation.setDesignation(candProfDetails.getDesignation());
			log.info("Updated designation to ID: {} for employeeId: {}", candProfDetails.getDesignation().getId(),
					employee.getId());
		} else {
			log.warn("Designation or designation ID is missing in CandidateProfessionalDetail for employeeId: {}",
					employee.getId());
			designation.setDesignation(null);
		}

		processorDependencies.employeeDesignationDAO.save(designation);
		log.info("EmployeeDesignation mapping successfully updated for employeeId: {}", employee.getId());
	}

	private void updateEmployeeBranchMapping(CandidateProfessionalDetail candProfDetails, Employee employee) {
		if (HRMSHelper.isNullOrEmpty(employee) || HRMSHelper.isNullOrEmpty(employee.getId())) {
			log.error("Employee or employee ID is null. Cannot update branch mapping.");
			return;
		}

		if (HRMSHelper.isNullOrEmpty(candProfDetails)) {
			log.error("CandidateProfessionalDetail is null. Cannot update branch mapping for employeeId: {}",
					employee.getId());
			return;
		}

		// Correct: Fetch by employee ID and isActive
		EmployeeBranch branch = processorDependencies.employeeBranchDAO.findByEmployeeAndIsActive(employee,
				ERecordStatus.Y.name());

		if (HRMSHelper.isNullOrEmpty(branch)) {
			log.warn("No existing EmployeeBranch mapping found for employeeId: {}. Skipping update.", employee.getId());
			return;
		}

		branch.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		branch.setUpdatedDate(new Date());
		branch.setIsActive(IHRMSConstants.isActive);
		branch.setOrgId(employee.getOrgId());
		branch.setEmployee(employee);

		if (!HRMSHelper.isNullOrEmpty(candProfDetails.getBranch())
				&& !HRMSHelper.isNullOrEmpty(candProfDetails.getBranch().getId())) {

			branch.setBranch(candProfDetails.getBranch());
			log.info("Updated branch to ID: {} for employeeId: {}", candProfDetails.getBranch().getId(),
					employee.getId());
		} else {
			log.warn("Branch or branch ID is missing in CandidateProfessionalDetail for employeeId: {}",
					employee.getId());
			branch.setBranch(null);
		}

		processorDependencies.employeeBranchDAO.save(branch);
		log.info("EmployeeBranch mapping successfully updated for employeeId: {}", employee.getId());
	}

	private void updateEmployeeDivisionMapping(CandidateProfessionalDetail candProfDetails, Employee employee) {
		if (HRMSHelper.isNullOrEmpty(employee) || HRMSHelper.isNullOrEmpty(employee.getId())) {
			log.error("Employee or employee ID is null. Cannot update division mapping.");
			return;
		}

		if (HRMSHelper.isNullOrEmpty(candProfDetails)) {
			log.error("CandidateProfessionalDetail is null. Cannot update division mapping for employeeId: {}",
					employee.getId());
			return;
		}

		EmployeeDivision division = processorDependencies.employeeDivisionDAO.findByEmployeeAndIsActive(employee,
				ERecordStatus.Y.name());

		if (HRMSHelper.isNullOrEmpty(division)) {
			log.warn("No existing EmployeeDivision mapping found for employeeId: {}. Skipping update.",
					employee.getId());
			return;
		}

		division.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
		division.setUpdatedDate(new Date());
		division.setIsActive(IHRMSConstants.isActive);
		division.setOrgId(employee.getOrgId());
		division.setEmployee(employee);

		if (!HRMSHelper.isNullOrEmpty(candProfDetails.getDivision())
				&& !HRMSHelper.isNullOrEmpty(candProfDetails.getDivision().getId())) {

			division.setDivision(candProfDetails.getDivision());
			log.info("Updated division to ID: {} for employeeId: {}", candProfDetails.getDivision().getId(),
					employee.getId());
		} else {
			log.warn("Division or division ID is missing in CandidateProfessionalDetail for employeeId: {}",
					employee.getId());
			division.setDivision(null);
		}

		processorDependencies.employeeDivisionDAO.save(division);
		log.info("EmployeeDivision mapping successfully updated for employeeId: {}", employee.getId());
	}

	//////// delete employee/////////
	@Override
	@Transactional(rollbackOn = Exception.class)
	public HRMSBaseResponse<?> deleteEmployee(EmpIdVo request) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "deleteEmployee");
		}
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isNullOrEmpty(request)) {
			if (HRMSHelper.isRolePresent(role, ERole.HR.name())) {

				for (Long empId : request.getEmpId()) {

					Employee emp = processorDependencies.employeeDAO.findActiveEmployeeById(empId,
							IHRMSConstants.isActive);

					if (!HRMSHelper.isNullOrEmpty(emp)) {
						emp.setIsActive(ERecordStatus.N.name());
						emp.setUpdatedDate(new Date());
						processorDependencies.employeeDAO.save(emp);

						if (!HRMSHelper.isNullOrEmpty(emp.getCandidate())) {
							emp.getCandidate().setIsActive(ERecordStatus.N.name());
							emp.getCandidate().setUpdatedDate(new Date());
							processorDependencies.candidateDAO.save(emp.getCandidate());

							if (!HRMSHelper.isNullOrEmpty(emp.getCandidate().getCandidatePersonalDetail())) {
								emp.getCandidate().getCandidatePersonalDetail().setIsActive(ERecordStatus.N.name());
								emp.getCandidate().getCandidatePersonalDetail().setUpdatedDate(new Date());
								processorDependencies.PersonalDetailDAO
										.save(emp.getCandidate().getCandidatePersonalDetail());

							}
							if (!HRMSHelper.isNullOrEmpty(emp.getCandidate().getCandidateProfessionalDetail())) {
								emp.getCandidate().getCandidateProfessionalDetail().setIsActive(ERecordStatus.N.name());
								emp.getCandidate().getCandidateProfessionalDetail().setUpdatedDate(new Date());
								processorDependencies.professionalDAO
										.save(emp.getCandidate().getCandidateProfessionalDetail());

							}

							if (!HRMSHelper.isNullOrEmpty(emp.getCandidate().getCandidateAddress())) {

								for (CandidateAddress address : emp.getCandidate().getCandidateAddress()) {

									address.setIsActive(ERecordStatus.N.name());
									address.setUpdatedDate(new Date());
									processorDependencies.addressDao.save(address);

								}

							}

							if (!HRMSHelper.isNullOrEmpty(emp.getEmployeeBranch())) {

								emp.getEmployeeBranch().setIsActive(ERecordStatus.N.name());
								emp.getEmployeeBranch().setUpdatedDate(new Date());
								processorDependencies.employeeBranchDAO.save(emp.getEmployeeBranch());

							}

							if (!HRMSHelper.isNullOrEmpty(emp.getEmployeeCurrentDetail())) {

								emp.getEmployeeCurrentDetail().setIsActive(ERecordStatus.N.name());
								emp.getEmployeeCurrentDetail().setUpdatedDate(new Date());
								processorDependencies.currentEmploymentDAO.save(emp.getEmployeeCurrentDetail());

							}

							if (!HRMSHelper.isNullOrEmpty(emp.getEmployeeDepartment())) {

								emp.getEmployeeDepartment().setIsActive(ERecordStatus.N.name());
								emp.getEmployeeDepartment().setUpdatedDate(new Date());
								processorDependencies.employeeDepartmentDAO.save(emp.getEmployeeDepartment());

							}

							if (!HRMSHelper.isNullOrEmpty(emp.getEmployeeDesignation())) {

								emp.getEmployeeDesignation().setIsActive(ERecordStatus.N.name());
								emp.getEmployeeDesignation().setUpdatedDate(new Date());
								processorDependencies.employeeDesignationDAO.save(emp.getEmployeeDesignation());

							}
							if (!HRMSHelper.isNullOrEmpty(emp.getEmployeeDivision())) {

								emp.getEmployeeDivision().setIsActive(ERecordStatus.N.name());
								emp.getEmployeeDivision().setUpdatedDate(new Date());
								processorDependencies.employeeDivisionDAO.save(emp.getEmployeeDivision());

							}

							if (!HRMSHelper.isNullOrEmpty(emp.getEmployeeReportingManager())) {

								emp.getEmployeeReportingManager().setIsActive(ERecordStatus.N.name());
								emp.getEmployeeReportingManager().setUpdatedDate(new Date());
								processorDependencies.reportingManagerDAO.save(emp.getEmployeeReportingManager());

							}

							if (!HRMSHelper.isNullOrEmpty(emp.getCandidate().getLoginEntity())) {

								emp.getCandidate().getLoginEntity().setIsActive(ERecordStatus.N.name());
								processorDependencies.logDao.save(emp.getCandidate().getLoginEntity());

							}

							List<HrToHodMap> hrList = processorDependencies.hrtohodMapDao
									.findByHrIdAndIsActive(emp.getId(), IHRMSConstants.isActive);
							if (!HRMSHelper.isNullOrEmpty(hrList)) {

								for (HrToHodMap hr : hrList) {

									hr.setIsActive(ERecordStatus.N.name());
									hr.setUpdatedDate(new Date());
									processorDependencies.hrtohodMapDao.save(hr);
								}

							}

							List<Kra> kraList = processorDependencies.kraDao.findByEmployeeIdAndIsActive(empId,
									ERecordStatus.Y.name(), null);
							if (!HRMSHelper.isNullOrEmpty(kraList)) {
								for (Kra kra : kraList) {

									kra.setIsActive(ERecordStatus.N.name());
									kra.setUpdatedDate(new Date());
									processorDependencies.kraDao.save(kra);

									if (!HRMSHelper.isNullOrEmpty(kra.getKraWf())) {
										kra.getKraWf().setIsActive(ERecordStatus.N.name());
										kra.getKraWf().setUpdatedDate(new Date());
										processorDependencies.kraWfDao.save(kra.getKraWf());

									}

									HodCycleFinalRating hodRating = processorDependencies.hodFinalRatingDao
											.findByIsActiveAndKra(ERecordStatus.Y.name(), kra);

									if (!HRMSHelper.isNullOrEmpty(hodRating)) {
										hodRating.setIsActive(ERecordStatus.N.name());
										hodRating.setUpdatedDate(new Date());
										processorDependencies.hodFinalRatingDao.save(hodRating);

									}

									List<KraDetails> detailsList = processorDependencies.kradetailDao
											.findByIsActiveAndKra(ERecordStatus.Y.name(), kra);

									if (!HRMSHelper.isNullOrEmpty(detailsList)) {
										for (KraDetails details : detailsList) {

											details.setIsActive(ERecordStatus.N.name());
											details.setUpdatedDate(new Date());
											processorDependencies.kradetailDao.save(details);
										}
									}

								}

							}

						}

					}
				} // cloding for loop
				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1888));
				response.setApplicationVersion(processorDependencies.applicationVersion);
				response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "deleteEmployee");
		}
		return response;
	}
}
