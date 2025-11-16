package com.vinsys.hrms.employee.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.constants.ELogo;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.constants.EResponse;
import com.vinsys.hrms.dao.IEmployeeMasterDetailsDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateProfessionalDetailDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSLoginEntityTypeDAO;
import com.vinsys.hrms.dao.IHRMSMapLoginEntityTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.dao.IHRMSMasterTitleDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.VOMasterRole;
import com.vinsys.hrms.employee.service.IOrganazationService;
import com.vinsys.hrms.employee.vo.EmployeeAddressVO;
import com.vinsys.hrms.employee.vo.EmployeeProfileVO;
import com.vinsys.hrms.employee.vo.EmployeeVO;
import com.vinsys.hrms.employee.vo.HRListResponseVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeBranch;
import com.vinsys.hrms.entity.EmployeeDepartment;
import com.vinsys.hrms.entity.EmployeeDesignation;
import com.vinsys.hrms.entity.EmployeeDivision;
import com.vinsys.hrms.entity.EmployeeMasterDetails;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterRole;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.dao.IHrToHodMapDao;
import com.vinsys.hrms.kra.entity.HrToHodMap;
import com.vinsys.hrms.logo.entity.Logo;
import com.vinsys.hrms.logo.service.LogoService;
import com.vinsys.hrms.master.dao.IGradeDAO;
import com.vinsys.hrms.master.dao.IMasterGenderDAO;
import com.vinsys.hrms.master.dao.IMasterMaritalStatusDao;
import com.vinsys.hrms.master.dao.IMasterRoleDao;
import com.vinsys.hrms.master.vo.BranchVO;
import com.vinsys.hrms.master.vo.CityMasterVO;
import com.vinsys.hrms.master.vo.CountryMasterVO;
import com.vinsys.hrms.master.vo.DepartmentVO;
import com.vinsys.hrms.master.vo.DesignationVO;
import com.vinsys.hrms.master.vo.DivisionVO;
import com.vinsys.hrms.master.vo.EmploymentTypeVO;
import com.vinsys.hrms.master.vo.GenderMasterVO;
import com.vinsys.hrms.master.vo.GradeMasterVo;
import com.vinsys.hrms.master.vo.MasterMaritialStatusVo;
import com.vinsys.hrms.master.vo.MasterTitleVo;
import com.vinsys.hrms.master.vo.ReportingOfficerVO;
import com.vinsys.hrms.master.vo.StateVO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

@Service
public class OrganazationDetailsServiceImpl implements IOrganazationService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IHRMSEmployeeDAO employeeDAO;

	@Autowired
	IHRMSMasterTitleDAO titleDAO;

	@Autowired
	IMasterGenderDAO genderDAO;

	@Autowired
	IMasterMaritalStatusDao maritialStatusDAO;

	@Autowired
	IHRMSMasterCountryDAO countryDAO;

	@Autowired
	IHRMSMasterStateDAO stateDAO;

	@Autowired
	IHRMSMasterCityDAO cityDAO;

	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;

	@Autowired
	IHRMSEmployeeReportingManager reportingDAO;

	@Autowired
	IHRMSCandidateProfessionalDetailDAO candidateProfessionalDAO;

	@Autowired
	IGradeDAO gradeDAO;

	@Autowired
	IHRMSMapLoginEntityTypeDAO maploginEntityDAO;

	@Autowired
	IHRMSLoginEntityTypeDAO loginEntityTypeDAO;

	@Autowired
	IEmployeeMasterDetailsDAO empmasterDAO;

	@Autowired
	IHrToHodMapDao hrtohodMapDao;
	
	@Autowired
	LogoService logoService;
	
	@Autowired
	IMasterRoleDao masterRoleDAO;
	
	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Value("${app_version}")
	private String applicationVersion;

	@Override
	public HRMSBaseResponse<List<EmployeeAddressVO>> findAllEmployeeAddressBook(Long branchId, String branch,
			String keyword,Long gradeId, Long deptId, Pageable pageable) throws HRMSException {

		log.info("Inside findAllEmployeeAddressBook method");
		HRMSBaseResponse<List<EmployeeAddressVO>> response = new HRMSBaseResponse<>();

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (!HRMSHelper.isLongZero(empId)) {

			Employee emp = employeeDAO.findActiveEmployeeById(Long.valueOf(empId), "Y");
			long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
			long totalRecord = 0;
			List<Employee> employeeAddressBook;

			if (!HRMSHelper.isNullOrEmpty(branchId)) {
				employeeAddressBook = employeeDAO.findEmployeeOrgWiseBranchWiseInfo(orgId, IHRMSConstants.isActive,
						branchId, pageable);
				totalRecord = employeeDAO.countEmployeeOrgWiseBranchWiseInfo(orgId, IHRMSConstants.isActive, branchId);

			} else if (!HRMSHelper.isNullOrEmpty(branch)) {
				employeeAddressBook = employeeDAO.findEmployeeOrgWiseBranchNameWiseInfo(orgId, IHRMSConstants.isActive,
						branch, pageable);
				totalRecord = employeeDAO.countEmployeeOrgWiseBranchNameWiseInfo(orgId, IHRMSConstants.isActive,
						branch);

			} else if (!HRMSHelper.isNullOrEmpty(keyword)) {
				employeeAddressBook = employeeDAO.findEmployeeOrgWiseInfoByKeyword(orgId, IHRMSConstants.isActive,
						keyword, pageable);
				totalRecord = employeeDAO.countEmployeeOrgWiseInfoByKeyword(orgId, IHRMSConstants.isActive, keyword);

			} else if (!HRMSHelper.isNullOrEmpty(gradeId)) {
				employeeAddressBook = employeeDAO.findEmployeeOrgWiseGradeWiseInfo(orgId, IHRMSConstants.isActive,
						gradeId, pageable);
				totalRecord = employeeDAO.countEmployeeOrgWiseGradeWiseInfo(orgId, IHRMSConstants.isActive, gradeId);

			} else if (!HRMSHelper.isNullOrEmpty(deptId)) {
				employeeAddressBook = employeeDAO.findEmployeeOrgWiseDeptWiseInfo(orgId, IHRMSConstants.isActive,
						deptId, pageable);
				totalRecord = employeeDAO.countEmployeeOrgWiseDeptWiseInfo(orgId, IHRMSConstants.isActive, deptId);

			} else {
				employeeAddressBook = employeeDAO.findEmployeeOrgWiseInfo(orgId, IHRMSConstants.isActive, pageable);
				totalRecord = employeeDAO.countEmployeeOrgWiseInfo(orgId, IHRMSConstants.isActive);
			}

			List<EmployeeAddressVO> list = new ArrayList<>();

			if (!HRMSHelper.isNullOrEmpty(employeeAddressBook)) {
				for (Employee employee : employeeAddressBook) {
					EmployeeAddressVO employeeAddressVO = new EmployeeAddressVO();
					employeeAddressVO.setId(employee.getId());
					employeeAddressVO.setOfficialEmailId(employee.getOfficialEmailId());
					employeeAddressVO.setFirstName(employee.getCandidate().getFirstName());
					employeeAddressVO.setLastName(employee.getCandidate().getLastName());
					employeeAddressVO.setMiddleName(employee.getCandidate().getMiddleName());
					
					employeeAddressVO.setBranch(Optional.ofNullable(employee.getEmployeeBranch())
							.map(EmployeeBranch::getBranch).map(MasterBranch::getBranchName)
							.filter(name -> !HRMSHelper.isNullOrEmpty(name)).orElse(null));
					employeeAddressVO.setBranchId(
							Optional.ofNullable(employee.getEmployeeBranch()).map(EmployeeBranch::getBranch)
									.map(MasterBranch::getId).orElse(null));

					employeeAddressVO.setDepartment(Optional.ofNullable(employee.getEmployeeDepartment())
							.map(EmployeeDepartment::getDepartment).map(MasterDepartment::getDepartmentName)
							.filter(name -> !HRMSHelper.isNullOrEmpty(name)).orElse(null));
					employeeAddressVO.setDepartmentId(Optional.ofNullable(employee.getEmployeeDepartment())
							.map(EmployeeDepartment::getDepartment).map(MasterDepartment::getId).orElse(null));
					
					employeeAddressVO.setDivision(Optional.ofNullable(employee.getEmployeeDivision())
							.map(EmployeeDivision::getDivision).map(MasterDivision::getDivisionName)
							.filter(name -> !HRMSHelper.isNullOrEmpty(name)).orElse(null));
					employeeAddressVO.setDivisionId(Optional.ofNullable(employee.getEmployeeDivision())
							.map(EmployeeDivision::getDivision).map(MasterDivision::getId).orElse(null));
					
					employeeAddressVO.setDesignation(Optional.ofNullable(employee.getEmployeeDesignation())
							.map(EmployeeDesignation::getDesignation).map(MasterDesignation::getDesignationName)
							.filter(name -> !HRMSHelper.isNullOrEmpty(name)).orElse(null));

					employeeAddressVO
							.setContactNo(Optional.ofNullable(employee.getCandidate().getMobileNumber()).orElse(0L));

					employeeAddressVO.setOfficialContactNo(
							Optional.ofNullable(employee.getOfficialMobileNumber()).map(Object::toString).orElse(null));

					employeeAddressVO.setDateOfBirth(HRMSDateUtil.format(employee.getCandidate().getDateOfBirth(),
							IHRMSConstants.FRONT_END_DATE_FORMAT));

					employeeAddressVO.setEmployeeCode(employee.getEmployeeCode());
					employeeAddressVO.setName(
							employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());

					Long employeeId = employee.getId();

					EmployeeReportingManager rmId = reportingDAO.findByEmployeeAndIsActive(employeeId,
							ERecordStatus.Y.name());

					if (rmId != null && rmId.getReporingManager() != null
							&& rmId.getReporingManager().getId() != null) {
						employeeAddressVO.setReportingManager(rmId.getReporingManager().getCandidate().getFirstName()
								+ " " + rmId.getReporingManager().getCandidate().getLastName());
					} else {
						employeeAddressVO.setReportingManager(null);
						log.warn("Reporting Manager or its ID is null for employeeId: {}", employeeId);
					}

					String grade = Optional.ofNullable(employee.getCandidate())
							.map(c -> c.getCandidateProfessionalDetail()).map(p -> p.getGrade()).orElse(null);
					employeeAddressVO.setGrade(grade);

					list.add(employeeAddressVO);
				}

				if (HRMSHelper.isNullOrEmpty(list)) {
					throw new HRMSException(EResponse.ERROR_INSUFFICIENT_DATA.getCode(),
							EResponse.ERROR_INSUFFICIENT_DATA.getMessage());
				}

				response.setResponseBody(list);
				response.setApplicationVersion(applicationVersion);
				response.setTotalRecord(totalRecord);
				response.setResponseCode(EResponse.SUCCESS.getCode());
				response.setResponseMessage(EResponse.SUCCESS.getMessage());
				log.info("Exit findAllEmployeeAddressBook method");
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}
			return response;
		} else {
			throw new HRMSException(EResponse.INAVALID_DATA.getCode(), EResponse.INAVALID_DATA.getMessage());
		}
	}

	@Override
	public HRMSBaseResponse<List<EmployeeAddressVO>> findAllEmployeeAddressBookByDepartment() throws HRMSException {

		log.info("Inside findAllEmployeeAddressBookByDepartment method");
		HRMSBaseResponse<List<EmployeeAddressVO>> response = new HRMSBaseResponse();

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (!HRMSHelper.isLongZero(empId)) {

			Employee emp = employeeDAO.findActiveEmployeeById(Long.valueOf(empId), "Y");
			long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
			long deptId = emp.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
			long totalRecord = 0;
			List<Employee> employeeAddressBook = null;

			employeeAddressBook = employeeDAO.findEmployeeDepartmentWiseInfo(orgId, IHRMSConstants.isActive, deptId);
			totalRecord = employeeDAO.countEmployeeDepartmentWiseInfo(orgId, IHRMSConstants.isActive, deptId);

			List<EmployeeAddressVO> list = new ArrayList<>();

			if (!HRMSHelper.isNullOrEmpty(employeeAddressBook)) {
				for (Employee employee : employeeAddressBook) {
					EmployeeAddressVO employeeAddressVO = new EmployeeAddressVO();
					employeeAddressVO.setId(employee.getId());
					employeeAddressVO.setOfficialEmailId(employee.getOfficialEmailId());
					employeeAddressVO.setFirstName(employee.getCandidate().getFirstName());
					employeeAddressVO.setLastName(employee.getCandidate().getLastName());
					employeeAddressVO.setMiddleName(employee.getCandidate().getMiddleName());
					employeeAddressVO.setBranch(Optional.ofNullable(employee.getEmployeeBranch())
							.map(EmployeeBranch::getBranch).map(MasterBranch::getBranchName)
							.filter(name -> !HRMSHelper.isNullOrEmpty(name)).orElse(null));

					employeeAddressVO.setDepartment(Optional.ofNullable(employee.getEmployeeDepartment())
							.map(EmployeeDepartment::getDepartment).map(MasterDepartment::getDepartmentName)
							.filter(name -> !HRMSHelper.isNullOrEmpty(name)).orElse(null));

					employeeAddressVO.setDivision((Optional.ofNullable(employee.getEmployeeDivision())
							.map(EmployeeDivision::getDivision).map(MasterDivision::getDivisionName)
							.filter(name -> !HRMSHelper.isNullOrEmpty(name))).orElse(null));

					employeeAddressVO.setDesignation(Optional.ofNullable(employee.getEmployeeDesignation())
							.map(EmployeeDesignation::getDesignation).map(MasterDesignation::getDesignationName)
							.filter(name -> !HRMSHelper.isNullOrEmpty(name)).orElse(null));
					list.add(employeeAddressVO);
				}
				if (HRMSHelper.isNullOrEmpty(list)) {
					throw new HRMSException(EResponse.ERROR_INSUFFICIENT_DATA.getCode(),
							EResponse.ERROR_INSUFFICIENT_DATA.getMessage());
				}

				response.setResponseBody(list);
				response.setApplicationVersion(applicationVersion);
				response.setTotalRecord(totalRecord);
				response.setResponseCode(EResponse.SUCCESS.getCode());
				response.setResponseMessage(EResponse.SUCCESS.getMessage());
				log.info("Exist findAllEmployeeAddressBookByDepartment method");
			} else {
				throw new HRMSException(EResponse.ERROR_INSUFFICIENT_DATA.getCode(),
						EResponse.ERROR_INSUFFICIENT_DATA.getMessage());
			}
			return response;
		} else {
			// Handle the case where empId is zero
			throw new HRMSException(EResponse.INAVALID_DATA.getCode(), EResponse.INAVALID_DATA.getMessage());
		}

	}

	@Override
	public HRMSBaseResponse<List<EmployeeVO>> getActiveEmployeeList() throws HRMSException {

		log.info("Inside getActiveEmployeeList method");

		HRMSBaseResponse<List<EmployeeVO>> response = new HRMSBaseResponse<>();
		List<EmployeeVO> activeEmployeeList = new ArrayList<>();

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (HRMSHelper.isLongZero(empId)) {
			throw new HRMSException(EResponse.INAVALID_DATA.getCode(), EResponse.INAVALID_DATA.getMessage());
		}

		Employee emp = employeeDAO.findActiveEmployeeById(empId, IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(emp) || HRMSHelper.isNullOrEmpty(emp.getCandidate())) {
			throw new HRMSException(EResponse.INAVALID_DATA.getCode(), EResponse.INAVALID_DATA.getMessage());
		}

		Long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
		List<Employee> employees = employeeDAO.findAllByOrgIdAndIsActive(orgId, IHRMSConstants.isActive);

		for (Employee employee : employees) {
			if (HRMSHelper.isNullOrEmpty(employee.getCandidate()))
				continue;

			EmployeeVO vo = new EmployeeVO();
			vo.setId(employee.getId());
			vo.setOfficialEmailId(employee.getOfficialEmailId());
			vo.setFullName(employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
			vo.setFirstName(employee.getCandidate().getFirstName());
			vo.setMiddleName(employee.getCandidate().getMiddleName());
			vo.setLastName(employee.getCandidate().getLastName());
			vo.setDepartment(Optional.ofNullable(employee.getEmployeeDepartment()).map(ed -> ed.getDepartment())
					.map(md -> md.getDepartmentName()).orElse(null));
			vo.setBranch(Optional.ofNullable(employee.getEmployeeBranch()).map(eb -> eb.getBranch())
					.map(mb -> mb.getBranchName()).orElse(null));
			vo.setEmployeeCode(employee.getEmployeeCode());
			vo.setGrade(Optional.ofNullable(employee.getCandidate().getCandidateProfessionalDetail())
					.map(cpd -> cpd.getGrade()).orElse(null));

			EmployeeReportingManager rm = reportingDAO.findByEmployeeAndIsActive(employee.getId(),
					ERecordStatus.Y.name());
			if (rm != null && rm.getReporingManager() != null && rm.getReporingManager().getCandidate() != null) {
				String rmName = rm.getReporingManager().getCandidate().getFirstName();
				if (rm.getReporingManager().getCandidate().getLastName() != null) {
					rmName += " " + rm.getReporingManager().getCandidate().getLastName();
				}
				vo.setReportingManager(rmName);
				vo.setReportingManagerId(rm.getReporingManager().getId());
			}

			activeEmployeeList.add(vo);
		}

		response.setResponseBody(activeEmployeeList);
		response.setResponseCode(EResponse.SUCCESS.getCode());
		response.setResponseMessage(EResponse.SUCCESS.getMessage());
		response.setApplicationVersion(applicationVersion);
		response.setTotalRecord(activeEmployeeList.size());

		return response;
	}

	@Override
	public HRMSBaseResponse<EmployeeProfileVO> getEmployeeProfileDetails(Long employeeId) throws HRMSException {

		log.info("Inside getEmployeeProfileDetails for employeeId: {}", employeeId);
		HRMSBaseResponse<EmployeeProfileVO> response = new HRMSBaseResponse<>();
		
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		 if (!HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
		        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		    }

		EmployeeMasterDetails entity = empmasterDAO.findById(employeeId).orElseThrow(
				() -> new HRMSException(EResponse.ERROR_INSUFFICIENT_DATA.getCode(), "Employee not found"));

		EmployeeProfileVO vo = new EmployeeProfileVO();

		// Basic fields
		vo.setId(entity.getId());
		vo.setFirstName(entity.getFirstName());
		vo.setLastName(entity.getLastName());
		vo.setMiddleName(entity.getMiddleName());
		vo.setOfficialEmailId(entity.getOfficialEmailId());
		vo.setEmailId(entity.getPersonalEmail());
		vo.setOfficialMobileNumber(entity.getOfficialMobileNumber());
		vo.setEmployeeCode(entity.getEmployeeCode());
		vo.setSocialSecurityNo(entity.getSsnNumber());
		vo.setOrgId(entity.getOrgId());
		vo.setSpouceName(entity.getSpouseName());
		vo.setProbationPeriod(entity.getProbationPeriod());
		vo.setNoticePeriod(entity.getNoticePeriod());
		vo.setCycleAllowed(entity.getCycleAllowed());
		vo.setMobileNumber(entity.getMobileNumber());

		vo.setDateOfBirth(entity.getDateOfBirth() != null
				? HRMSDateUtil.format(entity.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT)
				: null);

		vo.setDateOfJoining(entity.getDateOfJoining() != null
				? HRMSDateUtil.format(entity.getDateOfJoining(), IHRMSConstants.FRONT_END_DATE_FORMAT)
				: null);

		vo.setRetirementDate(entity.getRetirementDate() != null
				? HRMSDateUtil.format(entity.getRetirementDate(), IHRMSConstants.FRONT_END_DATE_FORMAT)
				: null);

		MasterTitleVo salutation = null;
		if (entity.getSalutationId() != null || entity.getTitle() != null) {
			salutation = new MasterTitleVo();
			salutation.setId(entity.getSalutationId());
			salutation.setTitle(entity.getTitle());
		}
		vo.setSalutation(salutation);

		GenderMasterVO gender = null;
		if (entity.getGenderId() != null || entity.getGenderDescription() != null) {
			gender = new GenderMasterVO();
			gender.setId(entity.getGenderId());
			gender.setGender(entity.getGenderDescription());
		}
		vo.setGender(gender);

		MasterMaritialStatusVo maritalStatus = null;
		if (entity.getMaritalStatusId() != null || entity.getMaritalStatus() != null) {
			maritalStatus = new MasterMaritialStatusVo();
			maritalStatus.setId(entity.getMaritalStatusId());
			maritalStatus.setMaritalStatus(entity.getMaritalStatus());
		}
		vo.setMaritalStatus(maritalStatus);

		EmploymentTypeVO employmentType = null;
		if (entity.getEmployementTypeId() != null || entity.getEmploymentTypeName() != null) {
			employmentType = new EmploymentTypeVO();
			employmentType.setId(entity.getEmployementTypeId());
			employmentType.setEmploymentTypeName(entity.getEmploymentTypeName());
		}
		vo.setEmploymentType(employmentType);

		ReportingOfficerVO reportingManager = null;
		if (entity.getReportingManagerId() != null || entity.getReportingManagerName() != null) {
			reportingManager = new ReportingOfficerVO();
			reportingManager.setId(entity.getReportingManagerId());
			reportingManager.setReportingOfficerName(entity.getReportingManagerName());
		}
		vo.setReportingManager(reportingManager);

		BranchVO branch = null;
		if (entity.getBranchId() != null || entity.getBranchName() != null) {
			branch = new BranchVO();
			branch.setId(entity.getBranchId());
			branch.setBranchName(entity.getBranchName());
		}
		vo.setBranch(branch);

		DepartmentVO department = null;
		if (entity.getDepartmentId() != null || entity.getDepartmentName() != null) {
			department = new DepartmentVO();
			department.setId(entity.getDepartmentId());
			department.setDepartmentName(entity.getDepartmentName());
		}
		vo.setDepartment(department);

		DivisionVO division = null;
		if (entity.getFunctionId() != null || entity.getFunctionName() != null) {
			division = new DivisionVO();
			division.setId(entity.getFunctionId());
			division.setDivisionName(entity.getFunctionName());
		}
		vo.setDivision(division);

		DesignationVO designation = null;
		if (entity.getDesignationId() != null || entity.getDesignation() != null) {
			designation = new DesignationVO();
			designation.setId(entity.getDesignationId());
			designation.setDesignationName(entity.getDesignation());
		}
		vo.setDesignation(designation);

		GradeMasterVo grade = null;
		if (entity.getGradeId() != null || entity.getGradeDescription() != null) {
			grade = new GradeMasterVo();
			grade.setId(entity.getGradeId());
			grade.setGradeDescription(entity.getGradeDescription());
		}
		vo.setGrade(grade);

		CountryMasterVO country = null;
		if (entity.getCountryId() != null || entity.getCountryName() != null) {
			country = new CountryMasterVO();
			country.setId(entity.getCountryId());
			country.setCountryName(entity.getCountryName());
		}
		vo.setCountry(country);

		StateVO state = null;
		if (entity.getStateId() != null || entity.getStateName() != null) {
			state = new StateVO();
			state.setId(entity.getStateId());
			state.setStateName(entity.getStateName());
		}
		vo.setState(state);

		CityMasterVO city = null;
		if (entity.getCityId() != null || entity.getCityName() != null) {
			city = new CityMasterVO();
			city.setId(entity.getCityId());
			city.setCityName(entity.getCityName());
		}
		vo.setCity(city);

		List<VOMasterRole> roleList = new ArrayList<>();

		// Fetch active employee with given ID
		Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), employeeId);

		if (employee != null && employee.getCandidate() != null && employee.getCandidate().getLoginEntity() != null) {

			Long loginEntityId = employee.getCandidate().getLoginEntity().getId();

			// Get all login_entity_type_ids
			List<Long> loginEntityTypeIds = maploginEntityDAO.findLoginEntityTypeIdsByLoginEntityId(loginEntityId);

			if (!loginEntityTypeIds.isEmpty()) {
				// Fetch all LoginEntityTypes in a single DB call
				List<LoginEntityType> loginEntityTypes = loginEntityTypeDAO.findAllById(loginEntityTypeIds);

				// Track added login entity type IDs to avoid duplicates
				Set<Long> addedLoginEntityTypeIds = new HashSet<>();

				roleList = loginEntityTypes.stream()
						.filter(let -> let != null && let.getRole() != null && addedLoginEntityTypeIds.add(let.getId()))
						.map(let -> {
							VOMasterRole masterVo = new VOMasterRole();
							masterVo.setId(let.getId()); 
							masterVo.setRoleName(let.getLoginEntityTypeName());
							return masterVo;
						}).collect(Collectors.toList());
			}
		}

		vo.setRoles(roleList);

		List<HrToHodMap> hr = hrtohodMapDao.findByHrIdAndIsActive(employeeId, IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(hr)) {
			vo.setIsKpiVisible(IHRMSConstants.isActive);
		} else {
			vo.setIsKpiVisible(IHRMSConstants.isNotActive);
		}

		response.setResponseBody(vo);
		response.setResponseCode(EResponse.SUCCESS.getCode());
		response.setResponseMessage(EResponse.SUCCESS.getMessage());
		response.setApplicationVersion(applicationVersion);

		return response;
	}

	
	@Override
	public byte[] downloadEmployeeDetailsReport(String keyword) throws HRMSException {

	    List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
	    if (!HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
	    }

	    List<EmployeeMasterDetails> employees;
	    if (!HRMSHelper.isNullOrEmpty(keyword)) {
	        employees = empmasterDAO.searchAllColumns(keyword.trim());
	    } else {
	        employees = empmasterDAO.findAll();
	    }

	    if (HRMSHelper.isNullOrEmpty(employees)) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
	    }

	    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

	        Sheet sheet = workbook.createSheet("Employee_Address_Book");
	        sheet.setDisplayGridlines(false);
	        CreationHelper helper = workbook.getCreationHelper();

	        String[] headers = {
	            "Employee ID", "Full Name", "Official Email", "Mobile", "Department",
	            "Designation", "Branch", "Reporting Manager", "Grade", "Function"
	        };

	        int totalCols = headers.length;

	        for (int i = 0; i < 4; i++) {
	            Row row = sheet.createRow(i);
	            for (int j = 0; j < totalCols; j++) {
	                row.createCell(j);
	            }
	        }

	        // --- Embed Logo ---
	        Logo logo = logoService.getConfig(ELogo.LOGO.name());
	        if (ObjectUtils.isEmpty(logo)) {
	            throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
	        }

	        InputStream logoStream = getInputStreamFromPath(logo.getValue());
	        if (logoStream != null) {
	            byte[] logoBytes = IOUtils.toByteArray(logoStream);
	            int pictureIdx = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_JPEG);
	            logoStream.close();

	            Drawing<?> drawing = sheet.createDrawingPatriarch();
	            ClientAnchor anchor = helper.createClientAnchor();
	            anchor.setCol1(0);
	            anchor.setRow1(0);
	            anchor.setCol2(2);
	            anchor.setRow2(4);
	            anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
	            drawing.createPicture(anchor, pictureIdx);
	        }

	        int titleRowIndex = 1;
	        int titleStartCol = totalCols / 2 - 1;
	        int titleEndCol = Math.min(totalCols - 1, titleStartCol + 2);

	        sheet.addMergedRegion(new CellRangeAddress(titleRowIndex, titleRowIndex, titleStartCol, titleEndCol));

	        Row titleRow = sheet.getRow(titleRowIndex);
	        Cell titleCell = titleRow.createCell(titleStartCol);
	        titleCell.setCellValue("Employee Address Book");

	        CellStyle titleStyle = workbook.createCellStyle();
	        Font titleFont = workbook.createFont();
	        titleFont.setBold(true);
	        titleFont.setFontHeightInPoints((short) 14);
	        titleStyle.setFont(titleFont);
	        titleStyle.setAlignment(HorizontalAlignment.CENTER);
	        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	        titleCell.setCellStyle(titleStyle);

	        Cell dateCell = titleRow.createCell(totalCols - 1);
	        dateCell.setCellValue("Date: " + LocalDate.now());

	        CellStyle dateStyle = workbook.createCellStyle();
	        dateStyle.setAlignment(HorizontalAlignment.RIGHT);
	        Font dateFont = workbook.createFont();
	        dateFont.setBold(false);
	        dateStyle.setFont(dateFont);
	        dateCell.setCellStyle(dateStyle);

	        // --- Header Row ---
	        int headerRowIndex = 4;
	        Row headerRow = sheet.createRow(headerRowIndex);

	        CellStyle headerStyle = workbook.createCellStyle();
	        Font headerFont = workbook.createFont();
	        headerFont.setBold(true);
	        headerFont.setColor(IndexedColors.WHITE.getIndex());
	        headerStyle.setFont(headerFont);
	        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerStyle.setAlignment(HorizontalAlignment.CENTER);
	        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	        headerStyle.setBorderTop(BorderStyle.MEDIUM);
	        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
	        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
	        headerStyle.setBorderRight(BorderStyle.MEDIUM);

	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(headerStyle);
	        }

	        CellStyle dataStyle = workbook.createCellStyle();
	        dataStyle.setBorderTop(BorderStyle.MEDIUM);
	        dataStyle.setBorderBottom(BorderStyle.MEDIUM);
	        dataStyle.setBorderLeft(BorderStyle.MEDIUM);
	        dataStyle.setBorderRight(BorderStyle.MEDIUM);

	        int rowNum = headerRowIndex + 1;
	        for (EmployeeMasterDetails emp : employees) {
	            Row row = sheet.createRow(rowNum++);

	            Cell cell0 = row.createCell(0);
	            cell0.setCellValue(HRMSHelper.safeString(emp.getEmployeeCode()));
	            cell0.setCellStyle(dataStyle);

	            Cell cell1 = row.createCell(1);
	            cell1.setCellValue(HRMSHelper.safeString(emp.getFirstName()) + " " + HRMSHelper.safeString(emp.getLastName()));
	            cell1.setCellStyle(dataStyle);

	            Cell cell2 = row.createCell(2);
	            cell2.setCellValue(HRMSHelper.safeString(emp.getOfficialEmailId()));
	            cell2.setCellStyle(dataStyle);

	            Cell cell3 = row.createCell(3);
	            cell3.setCellValue(HRMSHelper.safeString(emp.getOfficialMobileNumber()));
	            cell3.setCellStyle(dataStyle);

	            Cell cell4 = row.createCell(4);
	            cell4.setCellValue(HRMSHelper.safeString(emp.getDepartmentName()));
	            cell4.setCellStyle(dataStyle);

	            Cell cell5 = row.createCell(5);
	            cell5.setCellValue(HRMSHelper.safeString(emp.getDesignation()));
	            cell5.setCellStyle(dataStyle);

	            Cell cell6 = row.createCell(6);
	            cell6.setCellValue(HRMSHelper.safeString(emp.getBranchName()));
	            cell6.setCellStyle(dataStyle);

	            Cell cell7 = row.createCell(7);
	            cell7.setCellValue(HRMSHelper.safeString(emp.getReportingManagerName()));
	            cell7.setCellStyle(dataStyle);

	            Cell cell8 = row.createCell(8);
	            cell8.setCellValue(HRMSHelper.safeString(emp.getGradeDescription()));
	            cell8.setCellStyle(dataStyle);

	            Cell cell9 = row.createCell(9);
	            cell9.setCellValue(HRMSHelper.safeString(emp.getFunctionName()));
	            cell9.setCellStyle(dataStyle);
	        }

	        for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }

	        workbook.write(out);
	        return out.toByteArray();

	    } catch (IOException e) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1783));
	    }
	}
	
	 public static InputStream getInputStreamFromPath(String filePath) throws IOException {
	        File file = new File(filePath);
	        if (!file.exists()) {
	            throw new IOException("File not found at path: " + filePath);
	        }
	        return new FileInputStream(file);
	    }

	 
	 @Override
	 public HRMSBaseResponse<List<HRListResponseVO>> getActiveHRList() throws HRMSException {

	     log.info("Inside getActiveHRList method");

	     HRMSBaseResponse<List<HRListResponseVO>> response = new HRMSBaseResponse<>();
	     List<HRListResponseVO> hrEmployeeList = new ArrayList<>();

	     List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

	     
	     if (!HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
	         throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
	     }

	  
	     MasterRole hrRole = masterRoleDAO.findByRoleName(ERole.HR.name());
	     if (hrRole == null) {
	         throw new HRMSException(1500, "HR role not configured in MasterRole table");
	     }

	     Long hrId = hrRole.getId();

	     
	     LoginEntityType loginEntityTypeHr = loginEntityTypeDAO.findByRoleIdAndIsActive(hrId, ERecordStatus.Y.name());
	     if (loginEntityTypeHr == null) {
	         log.warn("No active LoginEntityType found for HR role.");
	         response.setResponseBody(Collections.emptyList());
	         response.setResponseCode(EResponse.SUCCESS.getCode());
	         response.setResponseMessage("No HR login entity type found");
	         response.setApplicationVersion(applicationVersion);
	         return response;
	     }

	     
	     List<Long> hrLoginEntityIds = maploginEntityDAO.findLoginEntityIdsByLoginEntityTypeId(loginEntityTypeHr.getId());

	     if (HRMSHelper.isNullOrEmpty(hrLoginEntityIds)) {
	         log.warn("No login entities mapped to HR role found.");
	         response.setResponseCode(EResponse.SUCCESS.getCode());
	         response.setResponseMessage("No active HR employees found");
	         response.setApplicationVersion(applicationVersion);
	         response.setResponseBody(Collections.emptyList());
	         return response;
	     }


	     List<Candidate> candidates = candidateDAO.findByLoginEntityIdIn(hrLoginEntityIds);

	     if (HRMSHelper.isNullOrEmpty(candidates)) {
	         log.warn("No candidates found for HR login entities.");
	         response.setResponseBody(Collections.emptyList());
	         response.setResponseCode(EResponse.SUCCESS.getCode());
	         response.setResponseMessage("No HR employees found");
	         response.setApplicationVersion(applicationVersion);
	         return response;
	     }

	     
	     List<Long> candidateIds = candidates.stream()
	             .map(Candidate::getId)
	             .collect(Collectors.toList());

	     List<Employee> employees = employeeDAO.findByCandidateIdInAndIsActive(candidateIds, IHRMSConstants.isActive);

	     if (!HRMSHelper.isNullOrEmpty(employees)) {
	         for (Employee employee : employees) {
	             HRListResponseVO vo = new HRListResponseVO();
	             vo.setId(employee.getId());
	             vo.setOfficialEmailId(employee.getOfficialEmailId());
	             vo.setName(employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
	             vo.setDepartment(Optional.ofNullable(employee.getEmployeeDepartment())
	                     .map(ed -> ed.getDepartment())
	                     .map(md -> md.getDepartmentName())
	                     .orElse(null));
	             vo.setEmployeeCode(employee.getEmployeeCode());
	             vo.setGrade(Optional.ofNullable(employee.getCandidate().getCandidateProfessionalDetail())
	                     .map(cpd -> cpd.getGrade())
	                     .orElse(null));

	             hrEmployeeList.add(vo);
	         }
	     }

	     response.setResponseBody(hrEmployeeList);
	     response.setResponseCode(EResponse.SUCCESS.getCode());
	     response.setResponseMessage(EResponse.SUCCESS.getMessage());
	     response.setApplicationVersion(applicationVersion);
	     response.setTotalRecord(hrEmployeeList.size());

	     return response;
	 }

}
