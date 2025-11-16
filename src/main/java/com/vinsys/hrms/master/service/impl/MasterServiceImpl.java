package com.vinsys.hrms.master.service.impl;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
/**
 * @author Onkar A.
 * 
 */
import org.springframework.stereotype.Service;

import com.vinsys.hrms.audit.service.IAuditLogService;
import com.vinsys.hrms.constants.ELeaveTypeCode;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IEmployeeBranchDAO;
import com.vinsys.hrms.dao.IEmployeeDepartmentDAO;
import com.vinsys.hrms.dao.IEmployeeDesignationDAO;
import com.vinsys.hrms.dao.IEmployeeDivisionDAO;
import com.vinsys.hrms.dao.IHRMSCandidateAddressDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateProfessionalDetailDAO;
import com.vinsys.hrms.dao.IHRMSEmployeTypeMapDAO;
import com.vinsys.hrms.dao.IHRMSEmployeTypeToLeaveMapDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveDetailsDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSLoginEntityTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterBranchDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSMasterDesignationDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterModeofSeparationDAO;
import com.vinsys.hrms.dao.IHRMSMasterModeofSeparationReasonDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.confirmation.IHRMSMasterParameterName;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterDriverDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterModeOfTravelDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterVehicleDAO;
import com.vinsys.hrms.dashboard.vo.MasterLeaveTypeVO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.VOMasterRole;
import com.vinsys.hrms.entity.EmployeTypeMap;
import com.vinsys.hrms.entity.EmployeTypeToLeaveMap;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterCity;
import com.vinsys.hrms.entity.MasterCountry;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterModeofSeparation;
import com.vinsys.hrms.entity.MasterModeofSeparationReason;
import com.vinsys.hrms.entity.MasterRole;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.entity.MasterTitle;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.entity.confirmation.MasterEvaluationParameter;
import com.vinsys.hrms.entity.traveldesk.MasterDriver;
import com.vinsys.hrms.entity.traveldesk.MasterModeOfTravel;
import com.vinsys.hrms.entity.traveldesk.MasterVehicle;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.dao.IKraCycleDAO;
import com.vinsys.hrms.kra.dao.IKraDetailsDao;
import com.vinsys.hrms.kra.dao.IKraDisplayStatusDao;
import com.vinsys.hrms.kra.dao.IKraYearDao;
import com.vinsys.hrms.kra.dao.ISubCategoryStagesDAO;
import com.vinsys.hrms.kra.dao.KraStatusDao;
import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraCycleStatus;
import com.vinsys.hrms.kra.entity.KraDisplayStatus;
import com.vinsys.hrms.kra.entity.KraYear;
import com.vinsys.hrms.kra.entity.SubCategoryStages;
import com.vinsys.hrms.kra.vo.KpiMetricVo;
import com.vinsys.hrms.kra.vo.KraCycleRequestVo;
import com.vinsys.hrms.kra.vo.KraYearResponseVo;
import com.vinsys.hrms.kra.vo.KraYearVo;
import com.vinsys.hrms.master.dao.ICategoryDao;
import com.vinsys.hrms.master.dao.ICurrencyMasterDAO;
import com.vinsys.hrms.master.dao.IGradeDAO;
import com.vinsys.hrms.master.dao.IKpiMetricDAO;
import com.vinsys.hrms.master.dao.IKpiTypeDAO;
import com.vinsys.hrms.master.dao.IKraRatingDAO;
import com.vinsys.hrms.master.dao.IMapTravelApproverDAO;
import com.vinsys.hrms.master.dao.IMasterAirTypeDAO;
import com.vinsys.hrms.master.dao.IMasterBrandingDAO;
import com.vinsys.hrms.master.dao.IMasterBusTypeDAO;
import com.vinsys.hrms.master.dao.IMasterClaimCategoryDAO;
import com.vinsys.hrms.master.dao.IMasterClaimTypeDAO;
import com.vinsys.hrms.master.dao.IMasterDegreeDAO;
import com.vinsys.hrms.master.dao.IMasterGenderDAO;
import com.vinsys.hrms.master.dao.IMasterLeavePolicyDAO;
import com.vinsys.hrms.master.dao.IMasterMaritalStatusDao;
import com.vinsys.hrms.master.dao.IMasterModeOfEducationDAO;
import com.vinsys.hrms.master.dao.IMasterRelationshipDAO;
import com.vinsys.hrms.master.dao.IMasterRoleDao;
import com.vinsys.hrms.master.dao.IMasterScreenTypeDAO;
import com.vinsys.hrms.master.dao.IMasterSelectionOptionDAO;
import com.vinsys.hrms.master.dao.IMasterStayTypeDAO;
import com.vinsys.hrms.master.dao.IMasterTitleDao;
import com.vinsys.hrms.master.dao.IMasterTravelApproverSlabDAO;
import com.vinsys.hrms.master.dao.IMasterTravelTypeDAO;
import com.vinsys.hrms.master.dao.IMasterTravellerTypeDAO;
import com.vinsys.hrms.master.dao.IMasterTripTypeDAO;
import com.vinsys.hrms.master.dao.IMasterVisaTypeDAO;
import com.vinsys.hrms.master.dao.IMasterYearDAO;
import com.vinsys.hrms.master.dao.IObjectivesDAO;
import com.vinsys.hrms.master.dao.IPrimaryRequesterTypeDAO;
import com.vinsys.hrms.master.dao.IReimbursementTravelTypeDAO;
import com.vinsys.hrms.master.dao.IReleaseTypeMasterDAO;
import com.vinsys.hrms.master.dao.ISubCategoryDao;
import com.vinsys.hrms.master.entity.Category;
import com.vinsys.hrms.master.entity.CurrencyMaster;
import com.vinsys.hrms.master.entity.DegreeMaster;
import com.vinsys.hrms.master.entity.GradeMaster;
import com.vinsys.hrms.master.entity.KpiMetricMaster;
import com.vinsys.hrms.master.entity.KpiTypeMaster;
import com.vinsys.hrms.master.entity.KraRating;
import com.vinsys.hrms.master.entity.MasterAirType;
import com.vinsys.hrms.master.entity.MasterBranding;
import com.vinsys.hrms.master.entity.MasterBusType;
import com.vinsys.hrms.master.entity.MasterClaimCategory;
import com.vinsys.hrms.master.entity.MasterClaimType;
import com.vinsys.hrms.master.entity.MasterGender;
import com.vinsys.hrms.master.entity.MasterLeavePolicy;
import com.vinsys.hrms.master.entity.MasterMapTravelApprover;
import com.vinsys.hrms.master.entity.MasterMaritialStatus;
import com.vinsys.hrms.master.entity.MasterReimbursementTravelType;
import com.vinsys.hrms.master.entity.MasterRelationship;
import com.vinsys.hrms.master.entity.MasterScreenType;
import com.vinsys.hrms.master.entity.MasterStayType;
import com.vinsys.hrms.master.entity.MasterTravelApproverSlab;
import com.vinsys.hrms.master.entity.MasterTravelType;
import com.vinsys.hrms.master.entity.MasterTravellerType;
import com.vinsys.hrms.master.entity.MasterTripType;
import com.vinsys.hrms.master.entity.MasterVisaType;
import com.vinsys.hrms.master.entity.ModeOfEducationMaster;
import com.vinsys.hrms.master.entity.Objectives;
import com.vinsys.hrms.master.entity.PrimaryRequester;
import com.vinsys.hrms.master.entity.ReleaseTypeMaster;
import com.vinsys.hrms.master.entity.SelectionOptionMaster;
import com.vinsys.hrms.master.entity.Subcategory;
import com.vinsys.hrms.master.entity.YearMaster;
import com.vinsys.hrms.master.service.IMasterService;
import com.vinsys.hrms.master.vo.BranchListResponseVO;
import com.vinsys.hrms.master.vo.BranchVO;
import com.vinsys.hrms.master.vo.BrandingVO;
import com.vinsys.hrms.master.vo.CategoryResponseVo;
import com.vinsys.hrms.master.vo.CityListResponseVO;
import com.vinsys.hrms.master.vo.CityMasterVO;
import com.vinsys.hrms.master.vo.ClaimCategoryVO;
import com.vinsys.hrms.master.vo.ClaimTypeVO;
import com.vinsys.hrms.master.vo.CountryListResponseVO;
import com.vinsys.hrms.master.vo.CountryMasterVO;
import com.vinsys.hrms.master.vo.CurrencyMasterVO;
import com.vinsys.hrms.master.vo.DegreeMasterVO;
import com.vinsys.hrms.master.vo.DepartmentListResponseVO;
import com.vinsys.hrms.master.vo.DepartmentVO;
import com.vinsys.hrms.master.vo.DesignationListResponseVO;
import com.vinsys.hrms.master.vo.DesignationVO;
import com.vinsys.hrms.master.vo.DisplayStatusVo;
import com.vinsys.hrms.master.vo.DivisionListResponseVO;
import com.vinsys.hrms.master.vo.DivisionVO;
import com.vinsys.hrms.master.vo.EmploymentTypeListResponseVO;
import com.vinsys.hrms.master.vo.EmploymentTypeVO;
import com.vinsys.hrms.master.vo.GenderMasterVO;
import com.vinsys.hrms.master.vo.GradeListResponseVO;
import com.vinsys.hrms.master.vo.GradeMasterVo;
import com.vinsys.hrms.master.vo.KpiMetricListResponseVO;
import com.vinsys.hrms.master.vo.KpiTypeMasterVo;
import com.vinsys.hrms.master.vo.KraCycleStatusVo;
import com.vinsys.hrms.master.vo.KraCycleVo;
import com.vinsys.hrms.master.vo.LoginEntityTypeVO;
import com.vinsys.hrms.master.vo.MasterAirTypeVO;
import com.vinsys.hrms.master.vo.MasterBusTypeVO;
import com.vinsys.hrms.master.vo.MasterCityVO;
import com.vinsys.hrms.master.vo.MasterCountryVO;
import com.vinsys.hrms.master.vo.MasterDriverVO;
import com.vinsys.hrms.master.vo.MasterLeavePolicyVO;
import com.vinsys.hrms.master.vo.MasterLeaveTypesDetailsVO;
import com.vinsys.hrms.master.vo.MasterMaritialStatusVo;
import com.vinsys.hrms.master.vo.MasterModeOfTravelVO;
import com.vinsys.hrms.master.vo.MasterScreenTypeResponseVo;
import com.vinsys.hrms.master.vo.MasterStateVO;
import com.vinsys.hrms.master.vo.MasterTitleListResponseVO;
import com.vinsys.hrms.master.vo.MasterTitleVo;
import com.vinsys.hrms.master.vo.MasterTravelTypeVO;
import com.vinsys.hrms.master.vo.MasterTravellerTypeVO;
import com.vinsys.hrms.master.vo.MasterVehicleVO;
import com.vinsys.hrms.master.vo.MasterVisaTypeVO;
import com.vinsys.hrms.master.vo.ModeOfEducationVO;
import com.vinsys.hrms.master.vo.ModeOfSeparationMasterVO;
import com.vinsys.hrms.master.vo.ModeofSeparationReasonVO;
import com.vinsys.hrms.master.vo.ObjectivesVO;
import com.vinsys.hrms.master.vo.OrganizationVO;
import com.vinsys.hrms.master.vo.PrimaryRequesterVO;
import com.vinsys.hrms.master.vo.ProbationFeedbackParameterVO;
import com.vinsys.hrms.master.vo.ReimbursementTravelTypeVO;
import com.vinsys.hrms.master.vo.RelationshipMasterVO;
import com.vinsys.hrms.master.vo.ReleaseTypeVO;
import com.vinsys.hrms.master.vo.ReportingOfficerVO;
import com.vinsys.hrms.master.vo.SelectionMasterVO;
import com.vinsys.hrms.master.vo.SelfRatingVo;
import com.vinsys.hrms.master.vo.SeparationReasonVO;
import com.vinsys.hrms.master.vo.StateListResponseVO;
import com.vinsys.hrms.master.vo.StateVO;
import com.vinsys.hrms.master.vo.StayTypeVO;
import com.vinsys.hrms.master.vo.SubCategoryMasterVo;
import com.vinsys.hrms.master.vo.SubCategoryStagesResponseVO;
import com.vinsys.hrms.master.vo.TravelApproverResponseVO;
import com.vinsys.hrms.master.vo.TravelDeskApproverRequestVO;
import com.vinsys.hrms.master.vo.TripTypeVO;
import com.vinsys.hrms.master.vo.YearMasterVO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.traveldesk.dao.IAccommodationRequestDAO;
import com.vinsys.hrms.traveldesk.dao.ICabRequestDAO;
import com.vinsys.hrms.traveldesk.dao.ITicketRequestDAO;
import com.vinsys.hrms.traveldesk.dao.ITravelRequestDAO;
import com.vinsys.hrms.traveldesk.dao.ITravellerDetailDAO;
import com.vinsys.hrms.traveldesk.entity.TravelRequestV2;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.JWTTokenHelper;
import com.vinsys.hrms.util.LogConstants;
import com.vinsys.hrms.util.MasterAuthorityHelper;
import com.vinsys.hrms.util.ResponseCode;
import com.vinsys.hrms.util.TravelDeskAuthorityHelper;
import com.vinsys.hrms.util.TravelDeskTransformUtil;

import io.jsonwebtoken.Claims;

@Service
public class MasterServiceImpl implements IMasterService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IMasterTitleDao masterTitleDao;
	@Autowired
	IHRMSEmployeTypeMapDAO employeTypeMapDAO;
	@Autowired
	IHRMSEmployeTypeToLeaveMapDAO employeTypeToLeaveMapDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IMasterLeavePolicyDAO masterLeavePolicyDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailsDAO;
	@Autowired
	IHRMSMasterDesignationDAO designationDAO;
	@Autowired
	IHRMSMasterDepartmentDAO departmentDAO;
	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;
	@Autowired
	IHRMSMasterBranchDAO branchDAO;
	@Autowired
	IHRMSMasterStateDAO stateDAO;
	@Autowired
	IHRMSMasterModeofSeparationReasonDAO separationReasonDAO;
	@Autowired
	IMasterModeOfEducationDAO modeOfEducationDAO;
	@Autowired
	IMasterDegreeDAO degreeDAO;
	@Autowired
	IMasterYearDAO yearDAO;
	@Autowired
	IHRMSMasterParameterName parameterDao;
	@Autowired
	IMasterRelationshipDAO relationshipDAO;
	@Autowired
	IMasterGenderDAO genderDAO;
	@Autowired
	IMasterSelectionOptionDAO optionsDAO;

	@Autowired
	MasterAuthorityHelper masterHelper;
	@Autowired
	IHRMSMasterModeofSeparationDAO separationDAO;
	@Autowired
	IHRMSMasterCountryDAO countryDAO;
	@Autowired
	IReleaseTypeMasterDAO releaseTypeMasterDAO;

	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@Autowired
	IHRMSEmployeeReportingManager reportingManagerDAO;

	@Autowired
	IHRMSLoginEntityTypeDAO loginEntityTypeDAO;

	@Autowired
	IMasterBrandingDAO brandingDAO;

	@Autowired
	IMasterTravelTypeDAO travelTypeDAO;

	@Autowired
	IHRMSMasterModeOfTravelDAO modeOfTravelDAO;

	@Autowired
	IMasterTripTypeDAO masterTripTypeDAO;

	@Autowired
	IMasterBusTypeDAO busTypeDAO;

	@Autowired
	IMasterTravellerTypeDAO travellerTypeDAO;

	@Autowired
	IMasterAirTypeDAO airTypeDAO;

	@Autowired
	IMasterVisaTypeDAO visaTypeDAO;

	@Autowired
	IHRMSMasterDriverDAO driverDAO;

	@Autowired
	IHRMSMasterVehicleDAO vehicleDAO;

	@Autowired
	IMasterTravelApproverSlabDAO slabDAO;

	@Autowired
	IMapTravelApproverDAO travelApproverDAO;

	@Autowired
	ITravelRequestDAO travelRequestDao;
	@Autowired
	ICabRequestDAO cabRequestDAO;
	@Autowired
	IAccommodationRequestDAO accommodationRequestDAO;
	@Autowired
	TravelDeskAuthorityHelper travelDeskAuthorityHelper;
	@Autowired
	ITravellerDetailDAO travellerDetailDAO;
	@Autowired
	ITicketRequestDAO ticketRequestDAO;
	@Autowired
	TravelDeskTransformUtil travelDeskTransformUtil;

	@Autowired
	ICurrencyMasterDAO currencyMasterDAO;

	@Autowired
	IPrimaryRequesterTypeDAO primaryRequesterTypeDAO;

	@Autowired
	IReimbursementTravelTypeDAO reimbursementTravelTypeDAO;

	@Autowired
	IMasterClaimTypeDAO masterClaimTypeDAO;

	@Autowired
	IMasterClaimCategoryDAO masterClaimCategoryDAO;

	@Autowired
	IMasterStayTypeDAO stayTypeDAO;

	@Autowired
	IMasterMaritalStatusDao masterMaritalStatusDao;

	@Autowired
	IHRMSMasterCityDAO cityDao;

	@Autowired
	IHRMSMasterEmployementTypeDAO employmentTypeDAO;

	@Autowired
	IKraRatingDAO kraRatingDAO;

	@Autowired
	ICategoryDao categoryDAO;

	@Autowired
	IKraYearDao krayeardao;

	@Autowired
	IKraCycleDAO kraCycleDAO;

	@Autowired
	IKraDisplayStatusDao kradisplaystatusDao;

	@Autowired
	KraStatusDao kraStatusDao;

	@Autowired
	IKraYearDao kraYearDao;

	@Autowired
	ISubCategoryDao subCategoryDao;

	@Autowired
	IGradeDAO gradeDao;

	@Autowired
	IKpiTypeDAO kpiTypeDao;

	@Autowired
	IKpiMetricDAO kpiMetricDao;

	@Autowired
	IMasterRoleDao roleDao;

	@Autowired
	private IAuditLogService auditLogService;

	@Autowired
	IHRMSCandidateAddressDAO candidateAddressDAO;

	@Autowired
	IKraDetailsDao kraDetailsDao;

	@Autowired
	IHRMSCandidateProfessionalDetailDAO candidateProfessionalDetailDAO;

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IEmployeeDivisionDAO employeeDivisionDAO;

	@Autowired
	IEmployeeDesignationDAO employeeDesignationDAO;

	@Autowired
	IEmployeeDepartmentDAO employeeDepartmentDAO;

	@Autowired
	IEmployeeBranchDAO employeeBranchDAO;

	@Autowired
	IObjectivesDAO objectivesDao;

	@Autowired
	IMasterScreenTypeDAO masterscreentypeDao;

	@Autowired
	ISubCategoryStagesDAO subCategoryStagesDAO;

	@Value("${app_version}")
	private String applicationVersion;
	@Value("${td_approver_for_mgmt_employee}")
	private Long tdApproverIdForMgmtEmployee;

	@Override
	public HRMSBaseResponse getLeaveTypes(HttpServletRequest request) {

		Claims claims = JWTTokenHelper.getLoggedInEmpDetail(JWTTokenHelper.parseJwt(request));
		String employeeType = claims.get("employeeId").toString();

		Employee employee = employeeDAO.findEmpCandByEmpId(Long.valueOf(employeeType));

		HRMSBaseResponse response = new HRMSBaseResponse();

		// get employee type
		EmployeTypeMap employeTypeMap = employeTypeMapDAO.getEmployeeType(
				employee.getCandidate().getLoginEntity().getOrganization().getId(),
				employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				employee.getCandidate().getCandidateProfessionalDetail().getBranch().getId(),
				employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getId());

		// get all leave for particular employee type
		List<EmployeTypeToLeaveMap> typeToLeaveMap = employeTypeToLeaveMapDAO
				.findByEmpTypeIdAndIsActive(employeTypeMap.getEntityId(), IHRMSConstants.isActive);

		// get all available leave
		List<EmployeeLeaveDetail> leaveDetails = employeeLeaveDetailsDAO
				.getAvailableLeaveByEmployee(Year.now().getValue(), employee.getId());

		List<MasterLeaveTypeVO> masterLeaveTypeList = new ArrayList<>();
		MasterLeaveTypeVO masterLeaveType = null;
		for (EmployeTypeToLeaveMap type : typeToLeaveMap) {

			long matchCount = leaveDetails.stream().filter(e -> e.getMasterLeaveType().getLeaveTypeCode()
					.equalsIgnoreCase(type.getLeaveType().getLeaveTypeCode())).count();

			log.info(type.getLeaveType().getLeaveTypeCode() + ":::" + matchCount);

			if (matchCount > 0 || (type.getLeaveType().getLeaveTypeCode().equalsIgnoreCase(ELeaveTypeCode.ONDT.name())
					|| type.getLeaveType().getLeaveTypeCode().equalsIgnoreCase(ELeaveTypeCode.LOPL.name())
					|| type.getLeaveType().getLeaveTypeCode().equalsIgnoreCase(ELeaveTypeCode.WRHM.name()))) {
				masterLeaveType = new MasterLeaveTypeVO();
				masterLeaveType.setId(type.getLeaveType().getId());
				masterLeaveType.setLeaveTypeName(type.getLeaveType().getLeaveTypeName());
				masterLeaveType.setLeaveTypeDescription(type.getLeaveType().getLeaveTypeDescription());
				masterLeaveType.setNumberOfSession(type.getLeaveType().getNumberOfSession());
				masterLeaveTypeList.add(masterLeaveType);
			}

		}
		MasterLeaveTypesDetailsVO masterLeaveTypesDetailsVO = new MasterLeaveTypesDetailsVO();
		masterLeaveTypesDetailsVO.setLeaveTypesDetails(masterLeaveTypeList);
		response.setResponseBody(masterLeaveTypesDetailsVO);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);

		return response;
	}

	@Override
	public MasterLeavePolicyVO getLeavePolicy(Long leaveTypeId) throws HRMSException {

		MasterLeavePolicy leavePolicy = null;
		MasterLeavePolicy leavePolicyByDepartment = getLeavePolicyByDepartment(leaveTypeId);

		if (!HRMSHelper.isNullOrEmpty(leavePolicyByDepartment)) {
			leavePolicy = leavePolicyByDepartment;
		}

		return convertToLeavePolicyVO(leavePolicy);
	}

	private MasterLeavePolicyVO convertToLeavePolicyVO(MasterLeavePolicy leavePolicy) {
		MasterLeavePolicyVO leavePolicyVO = null;
		if (!HRMSHelper.isNullOrEmpty(leavePolicy)) {
			leavePolicyVO = new MasterLeavePolicyVO();
			leavePolicyVO.setAllowedDays(leavePolicy.getAllowedDays());
			leavePolicyVO.setAllowedGender(leavePolicy.getAllowedGender());
			leavePolicyVO.setAllowedMaxDays(leavePolicy.getAllowedMaxDays());
			leavePolicyVO.setAllowedMinDays(leavePolicy.getAllowedMinDays());
			leavePolicyVO.setAllowedMonthsLaps(leavePolicy.getAllowedMonthsLaps());
			leavePolicyVO.setAllowedPreviousMonth(leavePolicy.getAllowedPreviousMonth());
			leavePolicyVO.setApprovalRequired(leavePolicy.getApprovalRequired());
			leavePolicyVO.setAutoApproveDays(leavePolicy.getAutoApproveDays());
			leavePolicyVO.setAutoApproveRequired(leavePolicy.getAutoApproveRequired());
			leavePolicyVO.setCarryForward(leavePolicy.getCarryForward());
			leavePolicyVO.setCommentRequired(leavePolicy.getCommentRequired());
			leavePolicyVO.setIsWeekendAllowed(leavePolicy.getIsWeekendAllowed());
			leavePolicyVO.setValidityDays(leavePolicy.getValidityDays());
			leavePolicyVO.setCcRequired(leavePolicy.getCcRequired());
		}
		return leavePolicyVO;
	}

	private MasterLeavePolicy getLeavePolicyByDepartment(Long leaveTypeId) {
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = employeeDAO.findEmpCandByEmpId(employeeId);
		Long orgId = employee.getCandidate().getLoginEntity().getOrganization().getId();
		Long divId = employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
		Long branchId = !HRMSHelper
				.isNullOrEmpty(employee.getCandidate().getCandidateProfessionalDetail().getWorkingLocation())
						? employee.getCandidate().getCandidateProfessionalDetail().getWorkingLocation().getId()
						: employee.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
		Long departmentId = employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();

		MasterLeavePolicy leavePolicyByDepartment = masterLeavePolicyDAO.findPolicyByDepartmentAndLeaveType(orgId,
				divId, branchId, departmentId, leaveTypeId);
		return leavePolicyByDepartment;
	}

	@Override
	public HRMSBaseResponse getGrantLeaveTypes(HttpServletRequest request) throws HRMSException {

		Claims claims = JWTTokenHelper.getLoggedInEmpDetail(JWTTokenHelper.parseJwt(request));
		String employeeType = claims.get("employeeId").toString();

		Employee employee = employeeDAO.findByEmpIdAndOrgId(Long.valueOf(employeeType),
				SecurityFilter.TL_CLAIMS.get().getOrgId());

		HRMSBaseResponse response = new HRMSBaseResponse();

		// get employee type
		EmployeTypeMap employeTypeMap = employeTypeMapDAO.getEmployeeType(
				employee.getCandidate().getLoginEntity().getOrganization().getId(),
				employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				employee.getCandidate().getCandidateProfessionalDetail().getBranch().getId(),
				employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getId());

		// get all leave for particular employee type
		List<EmployeTypeToLeaveMap> typeToLeaveMap = employeTypeToLeaveMapDAO.findByEmpTypeIdAndIsActiveAndOrgId(
				employeTypeMap.getEntityId(), IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());

		// get all available leave
		List<EmployeeLeaveDetail> leaveDetails = employeeLeaveDetailsDAO.getAvailableLeaveByEmployeeAndOrgId(
				Year.now().getValue(), employee.getId(), SecurityFilter.TL_CLAIMS.get().getOrgId());

		List<MasterLeaveTypeVO> masterLeaveTypeList = new ArrayList<>();
		MasterLeaveTypeVO masterLeaveType = null;
		for (EmployeTypeToLeaveMap type : typeToLeaveMap) {

			long matchCount = leaveDetails.stream().filter(e -> e.getMasterLeaveType().getLeaveTypeCode()
					.equalsIgnoreCase(type.getLeaveType().getLeaveTypeCode())).count();

			log.info(type.getLeaveType().getLeaveTypeCode() + ":::" + matchCount);
			response.setTotalRecord(matchCount);
			if (type.getLeaveType().getLeaveTypeCode().equalsIgnoreCase(ELeaveTypeCode.COMP.name())) {
				masterLeaveType = new MasterLeaveTypeVO();
				masterLeaveType.setId(type.getLeaveType().getId());
				masterLeaveType.setLeaveTypeName(type.getLeaveType().getLeaveTypeName());
				masterLeaveType.setLeaveTypeDescription(type.getLeaveType().getLeaveTypeDescription());
				masterLeaveType.setNumberOfSession(type.getLeaveType().getNumberOfSession());
				masterLeaveTypeList.add(masterLeaveType);
			}

		}
		MasterLeaveTypesDetailsVO masterLeaveTypesDetailsVO = new MasterLeaveTypesDetailsVO();
		masterLeaveTypesDetailsVO.setLeaveTypesDetails(masterLeaveTypeList);
		response.setResponseBody(masterLeaveTypesDetailsVO);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);

		return response;
	}

	@Override
	public List<DesignationVO> getDesignationList() throws HRMSException {

		log.info("Inside getDesignationList Method ");
		List<MasterDesignation> masterDesignation = designationDAO.findByIsActive(IHRMSConstants.isActive);
		List<DesignationVO> designationVo = new ArrayList<>();
		for (MasterDesignation designation : masterDesignation) {
			DesignationVO designations = new DesignationVO();
			designations.setId(designation.getId());
			designations.setDesignationName(designation.getDesignationName());
			designations.setDesignationDescription(designation.getDesignationDescription());
			designationVo.add(designations);
		}

		log.info("Exit from getDesignationList Method ");
		return designationVo;
	}

	@Override
	public List<SubCategoryStagesResponseVO> getSubCategoryStagesList() throws HRMSException {

		log.info("Inside getSubCategoryStagesList.. Method ");
		List<SubCategoryStages> subCategoryStages = subCategoryStagesDAO.findByIsActive(ERecordStatus.Y.name());
		List<SubCategoryStagesResponseVO> subCategoryStagesVO = new ArrayList<>();
		for (SubCategoryStages subcat : subCategoryStages) {
			SubCategoryStagesResponseVO stages = new SubCategoryStagesResponseVO();
			stages.setId(subcat.getId());
			stages.setLabel(subcat.getStageName());

			subCategoryStagesVO.add(stages);
		}

		log.info("Exit from getDesignationList Method ");
		return subCategoryStagesVO;
	}

	@Override
	public List<DepartmentVO> getDepartmentList() throws HRMSException {
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		log.info("Inside getDepartmentList Method ");
		List<MasterDepartment> masterDepartment = departmentDAO.findAllMasterDepartmentByOrgIdCustomQuery(orgId,
				IHRMSConstants.isActive);
		List<DepartmentVO> departmentVo = new ArrayList<>();
		for (MasterDepartment department : masterDepartment) {
			DepartmentVO departments = new DepartmentVO();
			departments.setId(department.getId());
			departments.setDepartmentName(department.getDepartmentName());
			departments.setDepartmentDescription(department.getDepartmentDescription());
			departments.setOrganizationId(department.getOrgId());
			departmentVo.add(departments);
		}

		log.info("Exit from getDepartmentList Method ");
		return departmentVo;
	}

	@Override
	public List<DivisionVO> getDivisionList() throws HRMSException {
		log.info("Inside getDivisionList Method ");
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<MasterDivision> masterDivision = divisionDAO.findByIsActiveAndOrgId(IHRMSConstants.isActive, orgId);
		List<DivisionVO> divisionVo = new ArrayList<>();
		for (MasterDivision division : masterDivision) {
			DivisionVO divisions = new DivisionVO();
			divisions.setId(division.getId());
			divisions.setDivisionName(division.getDivisionName());
			divisions.setDivisionDescription(division.getDivisionDescription());
			divisions.setOrganizationId(division.getOrgId());
			divisionVo.add(divisions);
		}

		log.info("Exit from getDivisionList Method ");
		return divisionVo;
	}

	@Override
	public List<BranchVO> getBranchList() throws HRMSException {
		log.info("Inside getBranchList Method ");
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<MasterBranch> masterBranch = branchDAO.findByIsActiveAndOrgId(IHRMSConstants.isActive, orgId);
		List<BranchVO> branchVO = new ArrayList<>();
		for (MasterBranch branch : masterBranch) {
			BranchVO branches = new BranchVO();
			branches.setId(branch.getId());
			branches.setBranchName(branch.getBranchName());
			branches.setBranchDescription(branch.getBranchDescription());
			branches.setOrganizationId(branch.getOrgId());
			branchVO.add(branches);
		}
		log.info("Exit from getBranchList Method ");
		return branchVO;
	}

	@Override
	public List<MasterStateVO> getStateList(long countryId) throws HRMSException {
		log.info("Inside getStateList Method ");

		List<MasterState> masterState = stateDAO.findAllMasterStateByCountryIdCustomQuery(countryId,
				IHRMSConstants.isActive);
		List<MasterStateVO> masterStateVO = new ArrayList<>();
		for (MasterState state : masterState) {
			MasterStateVO states = new MasterStateVO();
			states.setId(state.getId());
			states.setStateName(state.getStateName());
			states.setStateDescription(state.getStateDescription());
			masterStateVO.add(states);
		}

		log.info("Exit from getStateList Method ");
		return masterStateVO;
	}

	@Override
	public List<SeparationReasonVO> getSeparationReasons(long modeOfSeparationId) throws HRMSException {
		log.info("inside getSeparationReasons Method");
		HRMSBaseResponse<SeparationReasonVO> response = new HRMSBaseResponse<>();

		List<MasterModeofSeparationReason> reasonMaster = separationReasonDAO
				.findAllMasterModeofSeparationReasonCustomQuery(modeOfSeparationId);
		List<SeparationReasonVO> reasonVO = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(reasonMaster)) {
			for (MasterModeofSeparationReason reasons : reasonMaster) {
				SeparationReasonVO separation = new SeparationReasonVO();
				separation.setId(reasons.getId());
				separation.setReasonName(reasons.getReasonName());
				reasonVO.add(separation);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		log.info("inside getSeparationReasons Method");
		return reasonVO;
	}

	@Override
	public HRMSBaseResponse<List<ModeOfEducationVO>> getModeOfEducation() throws HRMSException {
		log.info("inside getModeOfEducation Method");
		HRMSBaseResponse<List<ModeOfEducationVO>> response = new HRMSBaseResponse<>();
		List<ModeOfEducationMaster> modeOfEducationMaster = modeOfEducationDAO.findByIsActive(IHRMSConstants.isActive);
		List<ModeOfEducationVO> modeofEducationList = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(modeOfEducationMaster)) {

			for (ModeOfEducationMaster educationMaster : modeOfEducationMaster) {
				ModeOfEducationVO educationModeVO = new ModeOfEducationVO();
				educationModeVO.setId(educationMaster.getId());
				educationModeVO.setModeOfEducation(educationMaster.getModeOfEducation());
				educationModeVO.setDescription(educationMaster.getDescription());
				modeofEducationList.add(educationModeVO);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseBody(modeofEducationList);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		log.info("Exit from getModeOfEducation Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<DegreeMasterVO>> getDegree() throws HRMSException {
		log.info("inside getDegree Method");
		HRMSBaseResponse<List<DegreeMasterVO>> response = new HRMSBaseResponse<>();
		List<DegreeMaster> degreeMaster = degreeDAO.findByIsActive(IHRMSConstants.isActive);
		List<DegreeMasterVO> degreeList = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(degreeMaster)) {

			for (DegreeMaster degree : degreeMaster) {
				DegreeMasterVO degrees = new DegreeMasterVO();
				degrees.setId(degree.getId());
				degrees.setDegreeName(degree.getDegreeName());
				degrees.setDescription(degree.getDescription());
				degreeList.add(degrees);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseBody(degreeList);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		log.info("Exit from getDegree Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<YearMasterVO>> getYearList() throws HRMSException {
		log.info("inside getYearList Method");
		HRMSBaseResponse<List<YearMasterVO>> response = new HRMSBaseResponse<>();
		// List<YearMaster> yearMaster =
		// yearDAO.findByIsActive(IHRMSConstants.isActive);
		Long currentYear = (long) Year.now().getValue();
		List<YearMaster> yearMaster = yearDAO.getLastTenYears(currentYear);
		List<YearMasterVO> yearList = new ArrayList<>();
		Long id = 1L;
		if (!HRMSHelper.isNullOrEmpty(yearMaster)) {
			for (YearMaster year : yearMaster) {
				YearMasterVO yearVO = new YearMasterVO();
				yearVO.setId(id++);
				yearVO.setYear(year.getYear());
				yearList.add(yearVO);
			}

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseBody(yearList);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		log.info("Exit getYearList Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<ProbationFeedbackParameterVO>> getProbationFeedbackParameter() throws HRMSException {

		log.info("Inside getProbationFeedbacParameter method");
		HRMSBaseResponse<List<ProbationFeedbackParameterVO>> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = null;

		if (!HRMSHelper.isNullOrEmpty(empId)) {
			employee = employeeDAO.findById(empId).get();
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		if (!HRMSHelper.isNullOrEmpty(employee.getCandidate().getLoginEntity().getOrganization().getId())) {
			List<ProbationFeedbackParameterVO> parameterResponseList = new ArrayList<>();

			List<MasterEvaluationParameter> parameterList = parameterDao.findAllParameterNames(IHRMSConstants.isActive,
					employee.getCandidate().getLoginEntity().getOrganization().getId());

			if (!HRMSHelper.isNullOrEmpty(parameterList)) {

				for (MasterEvaluationParameter masterEvaluationParameter : parameterList) {

					ProbationFeedbackParameterVO parameterVO = new ProbationFeedbackParameterVO();
					parameterVO.setId(masterEvaluationParameter.getId());
					parameterVO.setOrganizationId(masterEvaluationParameter.getOrganization().getId());
					parameterVO.setParameterName(masterEvaluationParameter.getParameterName());
					parameterVO.setMaxRating(masterEvaluationParameter.getMaxRating());
					parameterResponseList.add(parameterVO);
				}

				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setResponseBody(parameterResponseList);
				response.setApplicationVersion(applicationVersion);

			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		return response;

	}

	@Override
	public HRMSBaseResponse<List<RelationshipMasterVO>> getRelationshipList() throws HRMSException {
		log.info("Inside  getRelationshipList Method");
		HRMSBaseResponse<List<RelationshipMasterVO>> response = new HRMSBaseResponse<>();
		List<MasterRelationship> relationshipMaster = relationshipDAO.findByIsActive(IHRMSConstants.isActive);
		List<RelationshipMasterVO> relationshipList = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(relationshipMaster)) {
			for (MasterRelationship relation : relationshipMaster) {
				RelationshipMasterVO relationVO = new RelationshipMasterVO();
				relationVO.setId(relation.getId());
				relationVO.setRelation(relation.getRelation());
				relationshipList.add(relationVO);
			}

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseBody(relationshipList);
			response.setApplicationVersion(applicationVersion);
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<List<GenderMasterVO>> getGenderList() throws HRMSException {
		log.info("Inside  getGenderList Method");

		HRMSBaseResponse<List<GenderMasterVO>> response = new HRMSBaseResponse<>();
		List<MasterGender> genderMaster = genderDAO.findByIsActive(IHRMSConstants.isActive);
		List<GenderMasterVO> genderList = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(genderMaster)) {

			for (MasterGender gender : genderMaster) {
				GenderMasterVO genderVo = new GenderMasterVO();
				genderVo.setId(gender.getId());
				genderVo.setGender(gender.getGender());
				genderList.add(genderVo);
			}
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseBody(genderList);
			response.setApplicationVersion(applicationVersion);
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<List<SelectionMasterVO>> getOptionList() throws HRMSException {
		log.info("Inside  getGenderList Method");

		HRMSBaseResponse<List<SelectionMasterVO>> response = new HRMSBaseResponse<>();
		List<SelectionOptionMaster> optionMaster = optionsDAO.findByIsActive(IHRMSConstants.isActive);
		List<SelectionMasterVO> optionList = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(optionMaster)) {

			for (SelectionOptionMaster option : optionMaster) {
				SelectionMasterVO options = new SelectionMasterVO();
				options.setId(option.getId());
				options.setSelectionOptions(option.getSelectionOptions());
				optionList.add(options);
			}
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseBody(optionList);
			response.setApplicationVersion(applicationVersion);
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		return response;

	}

	@Override
	public HRMSBaseResponse<?> addDesignation(DesignationVO designationVO) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		masterHelper.validateDesignation(designationVO);

		if (HRMSHelper.isNullOrEmpty(designationVO.getId()) && Boolean.TRUE
				.equals(designationDAO.existsByDesignationNameIgnoreCase(designationVO.getDesignationName()))) {
			throw new HRMSException(IHRMSConstants.DesignationAlredyExist);
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_DESIGNATION, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		return createOrUpdateDesignation(designationVO, response, employeeId, orgId);
	}

	private HRMSBaseResponse<?> createOrUpdateDesignation(DesignationVO designationVO,
			HRMSBaseResponse<String> response, Long employeeId, Long orgId) throws HRMSException {

		MasterDesignation designation;

		if (!HRMSHelper.isNullOrEmpty(designationVO.getId())) {
			designation = designationDAO.findByIdAndIsActive(designationVO.getId(), IHRMSConstants.isActive);
			if (designation == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1835)); // Designation not found
			}

			designation.setUpdatedBy(employeeId.toString());
			designation.setUpdatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1836));

		} else {
			designation = new MasterDesignation();
			designation.setCreatedBy(employeeId.toString());
			designation.setCreatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1837));
		}

		designation.setDesignationName(designationVO.getDesignationName());
		designation.setDesignationDescription(designationVO.getDesignationDescription());
		designation.setIsActive(IHRMSConstants.isActive);
		designation.setOrgId(orgId);

		designationDAO.save(designation);

		response.setResponseCode(1200);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	@Override
	public HRMSBaseResponse<?> addDepartment(DepartmentVO departmentDetails) throws HRMSException {

		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		masterHelper.validateDepartment(departmentDetails);
		if (HRMSHelper.isNullOrEmpty(departmentDetails.getId())
				&& Boolean.TRUE.equals(departmentDAO.existsByDepartmentName(departmentDetails.getDepartmentName()))) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_DEPARTMENT, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		return createOrUpdateDepartment(departmentDetails, response, employeeId, orgId);
	}

	private HRMSBaseResponse<?> createOrUpdateDepartment(DepartmentVO departmentDetails, HRMSBaseResponse<?> response,
			Long employeeId, Long orgId) throws HRMSException {

		MasterDepartment departmentMaster;

		if (!HRMSHelper.isNullOrEmpty(departmentDetails.getId())) {

			departmentMaster = departmentDAO.findByIdAndIsActive(departmentDetails.getId(), IHRMSConstants.isActive);
			if (departmentMaster == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1825));
			}
			departmentMaster.setUpdatedBy(employeeId.toString());
			departmentMaster.setUpdatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1824));
		} else {
			departmentMaster = new MasterDepartment();
			departmentMaster.setCreatedBy(employeeId.toString());
			departmentMaster.setCreatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1823));
		}

		departmentMaster.setDepartmentName(departmentDetails.getDepartmentName());
		departmentMaster.setDepartmentDescription(departmentDetails.getDepartmentDescription());
		departmentMaster.setOrgId(orgId);
		departmentMaster.setIsActive(IHRMSConstants.isActive);
		departmentDAO.save(departmentMaster);

		response.setResponseCode(1200);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	@Override
	public HRMSBaseResponse<?> addDegree(DegreeMasterVO degreeDetails) throws HRMSException {
		log.info("Inside addDegree Method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();

		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		DegreeMaster degreeMaster = new DegreeMaster();
		Employee employee = employeeDAO.findEmpCandByEmpId(Long.valueOf(employeeId));
		if (HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
			masterHelper.validateDegree(degreeDetails);

			if (Boolean.TRUE.equals(degreeDAO.existsByDegreeName(degreeDetails.getDegreeName()))) {
				throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
			}
			degreeMaster.setDegreeName(degreeDetails.getDegreeName());
			degreeMaster.setDescription(degreeDetails.getDescription());
			degreeMaster.setIsActive(IHRMSConstants.isActive);
			degreeMaster.setCreatedDate(new Date());
			degreeMaster.setCreatedBy(employee.getId().toString());
			degreeDAO.save(degreeMaster);
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		log.info(" Exit from addDegree Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<?> addRelationship(RelationshipMasterVO relationshipVO) throws HRMSException {
		log.info("Inside addRelationship  Method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		Employee employee = employeeDAO.findEmpCandByEmpId(Long.valueOf(employeeId));
		if (HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
			masterHelper.validateRelationShipMaster(relationshipVO);

			if (Boolean.TRUE.equals(relationshipDAO.existsByRelation(relationshipVO.getRelation()))) {
				throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
			}
			MasterRelationship relationShipMaster = new MasterRelationship();

			relationShipMaster.setRelation(relationshipVO.getRelation());
			relationShipMaster.setIsActive(IHRMSConstants.isActive);
			relationShipMaster.setCreatedDate(new Date());
			relationShipMaster.setCreatedBy(employee.getId().toString());
			relationshipDAO.save(relationShipMaster);

		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);

		log.info("Exit from addRelationship Method");
		return response;

	}

	@Override
	public HRMSBaseResponse<?> addGender(GenderMasterVO genderVO) throws HRMSException {
		log.info("Inside  addGender Method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		Employee employee = employeeDAO.findEmpCandByEmpId(Long.valueOf(employeeId));
		if (HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
			masterHelper.validateGenderVo(genderVO);

			if (Boolean.TRUE.equals(genderDAO.existsByGender(genderVO.getGender()))) {
				throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
			}

			MasterGender genderMaster = new MasterGender();

			genderMaster.setGender(genderVO.getGender());
			genderMaster.setIsActive(IHRMSConstants.isActive);
			genderMaster.setCreatedBy(employee.getId().toString());
			genderMaster.setCreatedDate(new Date());

			genderDAO.save(genderMaster);
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);

		log.info("Exit from addGender Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<?> addModeOfEducation(ModeOfEducationVO modeOfEducationDetails) throws HRMSException {
		log.info("Inside  addModeOfEducation Method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		Employee employee = employeeDAO.findEmpCandByEmpId(Long.valueOf(employeeId));

		if (HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
			masterHelper.validatemodeOfEducationDetailsVo(modeOfEducationDetails);

			if (Boolean.TRUE
					.equals(modeOfEducationDAO.existsByModeOfEducation(modeOfEducationDetails.getModeOfEducation()))) {
				throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
			}
			ModeOfEducationMaster modeOfEducation = new ModeOfEducationMaster();

			modeOfEducation.setDescription(modeOfEducationDetails.getDescription());
			modeOfEducation.setModeOfEducation(modeOfEducationDetails.getModeOfEducation());
			modeOfEducation.setIsActive(IHRMSConstants.isActive);
			modeOfEducation.setCreatedDate(new Date());
			modeOfEducation.setCreatedBy(employeeId.toString());

			modeOfEducationDAO.save(modeOfEducation);
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);

		log.info("Exit from addModeOfEducation Method");
		return response;
	}

//	@Override
//	public HRMSBaseResponse<?> addState(StateVO state) throws HRMSException {
//		log.info("Inside  addstate Method");
//		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
//		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
//		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
//		Employee employee = employeeDAO.findEmpCandByEmpId(Long.valueOf(employeeId));
//		if (HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
//			masterHelper.validateStateVo(state);
//
//			if (Boolean.TRUE.equals(stateDAO.existsByStateName(state.getStateName()))) {
//				throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
//			}
//
//			MasterCountry country = countryDAO.findByCountryId(state.getCountryId());
//			if (country == null) {
//				throw new HRMSException("Country not found for id:  " + state.getCountryId());
//			}
//
//			MasterState stateMaster = new MasterState();
//
//			stateMaster.setStateName(state.getStateName());
//			stateMaster.setStateDescription(state.getStateDescription());
//			stateMaster.setMasterCountry(country);
//			stateMaster.setIsActive(IHRMSConstants.isActive);
//			stateMaster.setCreatedDate(new Date());
//			stateMaster.setCreatedBy(employeeId.toString());
//
//			stateDAO.save(stateMaster);
//		} else {
//			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
//		}
//		response.setResponseCode(1200);
//		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
//		response.setApplicationVersion(applicationVersion);
//
//		log.info("Exit from addstate Method");
//		return response;
//	}

	@Override
	public HRMSBaseResponse<?> addState(StateVO state) throws HRMSException {
		log.info("Inside addState Method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (HRMSHelper.isNullOrEmpty(state.getStateName())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1805));
		}
		masterHelper.validateStateVo(state);

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_STATE, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		if (HRMSHelper.isNullOrEmpty(state.getId())
				&& Boolean.TRUE.equals(stateDAO.existsByStateName(state.getStateName()))) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
		}

		MasterCountry country = countryDAO.findByCountryId(state.getCountryId());
		if (country == null) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1801));
		}

		return createOrUpdateState(state, response, employeeId, country, orgId);
	}

	private HRMSBaseResponse<?> createOrUpdateState(StateVO state, HRMSBaseResponse<?> response, Long employeeId,
			MasterCountry country, Long orgId) throws HRMSException {
		MasterState stateMaster;
		if (!HRMSHelper.isNullOrEmpty(state.getId())) {
			stateMaster = stateDAO.findByIdAndIsActive(state.getId(), IHRMSConstants.isActive);
			if (stateMaster == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1800));
			}

			stateMaster.setUpdatedBy(employeeId.toString());
			stateMaster.setUpdatedDate(new Date());

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1798)); // Update message

			log.info("State updated successfully with ID: {}", state.getId());

		} else {
			stateMaster = new MasterState();
			stateMaster.setCreatedBy(employeeId.toString());
			stateMaster.setCreatedDate(new Date());

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1797)); // Create message

			log.info("New state created");
		}

		stateMaster.setStateName(state.getStateName());
		stateMaster.setStateDescription(state.getStateDescription());
		stateMaster.setMasterCountry(country);
		stateMaster.setIsActive(IHRMSConstants.isActive);
		stateMaster.setOrgId(orgId);

		stateDAO.save(stateMaster);

		response.setApplicationVersion(applicationVersion);
		log.info("Exit from addState Method");

		return response;
	}

	@Override
	public HRMSBaseResponse<?> addResignationReason(ModeofSeparationReasonVO resignationReason) throws HRMSException {
		log.info("Inside  addSeparationReason Method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
			masterHelper.validateResignationReasonVo(resignationReason);

			if (Boolean.TRUE.equals(separationReasonDAO.existsByReasonName(resignationReason.getReasonName()))) {
				throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
			}

			MasterModeofSeparation masterModeofSeparation = separationDAO
					.findBySeprationId(resignationReason.getModeOfSeparation());
			if (masterModeofSeparation == null) {
				throw new HRMSException(
						"modeOfSeparation not found for id:  " + resignationReason.getModeOfSeparation());
			}
			Organization organization = organizationDAO.findByOrgId(resignationReason.getOrganizationId());

			if (organization == null) {
				throw new HRMSException("Organization not found for id:  " + resignationReason.getOrganizationId());
			}

			Employee employee = employeeDAO.findEmpCandByEmpId(Long.valueOf(employeeId));
			MasterModeofSeparation modeofSeparation = separationDAO
					.findByModeIdAndOrgId(resignationReason.getModeOfSeparation(), organization.getId());
			MasterModeofSeparationReason modeofSeparationReason = new MasterModeofSeparationReason();
			modeofSeparationReason.setReasonName(resignationReason.getReasonName());
			modeofSeparationReason.setOrgId(organization.getId());
			modeofSeparationReason.setMasterModeofSeparation(modeofSeparation);
			modeofSeparationReason.setResignActionType(resignationReason.getResignActionType());
			modeofSeparationReason.setIsActive(IHRMSConstants.isActive);
			modeofSeparationReason.setCreatedDate(new Date());
			modeofSeparationReason.setCreatedBy(employeeId.toString());
			separationReasonDAO.save(modeofSeparationReason);
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);

		log.info("Exit from addSeparationReason Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<?> addDivision(DivisionVO divisionVO) throws HRMSException {

		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		masterHelper.validateDivisionVo(divisionVO);

		if (HRMSHelper.isNullOrEmpty(divisionVO.getId())
				&& Boolean.TRUE.equals(divisionDAO.existsByDivisionName(divisionVO.getDivisionName()))) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_DIVISION, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		return createOrUpdateDivision(divisionVO, response, employeeId, orgId);
	}

	private HRMSBaseResponse<?> createOrUpdateDivision(DivisionVO divisionVO, HRMSBaseResponse<?> response,
			Long employeeId, Long orgId) throws HRMSException {

		MasterDivision division;

		if (!HRMSHelper.isNullOrEmpty(divisionVO.getId())) {
			division = divisionDAO.findByIdAndIsActive(divisionVO.getId(), IHRMSConstants.isActive);
			if (division == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1831));
			}
			division.setUpdatedBy(employeeId.toString());
			division.setUpdatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1832));
		} else {

			division = new MasterDivision();
			division.setCreatedBy(employeeId.toString());
			division.setCreatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1833));
		}

		division.setDivisionName(divisionVO.getDivisionName());
		division.setDivisionDescription(divisionVO.getDivisionDescription());
		division.setIsActive(IHRMSConstants.isActive);
		division.setOrgId(orgId);

		divisionDAO.save(division);
		response.setResponseCode(1200);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	@Override
	public HRMSBaseResponse<?> addBranch(BranchVO branch) throws HRMSException {

		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		masterHelper.validateBranchVo(branch);

		if (HRMSHelper.isNullOrEmpty(branch.getId())
				&& Boolean.TRUE.equals(branchDAO.existsByBranchName(branch.getBranchName()))) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_BRANCH, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		return createOrUpdateBranch(branch, response, employeeId, orgId);
	}

	private HRMSBaseResponse<?> createOrUpdateBranch(BranchVO branchVo, HRMSBaseResponse<?> response, Long employeeId,
			Long orgId) throws HRMSException {

		MasterBranch branch;

		if (!HRMSHelper.isNullOrEmpty(branchVo.getId())) {

			branch = branchDAO.findByIdAndIsActive(branchVo.getId(), IHRMSConstants.isActive);
			if (branch == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1820));
			}
			branch.setUpdatedBy(employeeId.toString());
			branch.setUpdatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1821));
		} else {

			branch = new MasterBranch();
			branch.setCreatedBy(employeeId.toString());
			branch.setCreatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1822));
		}

		branch.setBranchName(branchVo.getBranchName());
		branch.setBranchDescription(branchVo.getBranchDescription());
		branch.setIsActive(IHRMSConstants.isActive);
		branch.setOrgId(orgId);

		branchDAO.save(branch);

		response.setResponseCode(1200);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	@Override
	public HRMSBaseResponse<List<ReleaseTypeVO>> getReleaseTypes() throws HRMSException {

		log.info("Inside  getReleaseTypes Method");
		HRMSBaseResponse<List<ReleaseTypeVO>> response = new HRMSBaseResponse<>();
		List<ReleaseTypeMaster> releaseTypeMasters = releaseTypeMasterDAO.findByIsActive(ERecordStatus.Y.name());
		List<ReleaseTypeVO> releaseTypeVOs = new ArrayList<>();
		ReleaseTypeVO releaseTypeVO = new ReleaseTypeVO();
		if (!HRMSHelper.isNullOrEmpty(releaseTypeMasters)) {
			for (ReleaseTypeMaster type : releaseTypeMasters) {
				releaseTypeVO = new ReleaseTypeVO();
				releaseTypeVO.setId(type.getId());
				releaseTypeVO.setReleaseType(type.getReleaseType());
				releaseTypeVOs.add(releaseTypeVO);
			}

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(releaseTypeVOs);

		log.info("Exit from getReleaseTypes Method");
		return response;

	}

	@Override
	public HRMSBaseResponse<List<ReportingOfficerVO>> getAllReportingManagerList() throws HRMSException {
		log.info("inside getAllReportingManagerList method");
		HRMSBaseResponse<List<ReportingOfficerVO>> response = new HRMSBaseResponse<>();

		List<ReportingOfficerVO> reportingOfficerList = new ArrayList<>();

		LoginEntityType loginEntityType = loginEntityTypeDAO.findByRoleName(ERole.MANAGER.name());
		Long id = 1L;
		List<Employee> employeeList = employeeDAO.getEmployeeByLoginEntityType(loginEntityType.getId());
		if (!HRMSHelper.isNullOrEmpty(employeeList)) {

			for (Employee reportingManager : employeeList) {
				ReportingOfficerVO reportingOfficer = new ReportingOfficerVO();

				if (!HRMSHelper.isNullOrEmpty(reportingManager)) {

					reportingOfficer.setId(id++);
					reportingOfficer.setReportingOfficerEmployeeId(reportingManager.getId());
					reportingOfficer.setReportingOfficerName(reportingManager.getCandidate().getFirstName() + " "
							+ reportingManager.getCandidate().getLastName());
					reportingOfficer.setDepartmentId(
							reportingManager.getCandidate().getCandidateProfessionalDetail().getDepartment().getId());
					reportingOfficer.setDepartmentName(reportingManager.getCandidate().getCandidateProfessionalDetail()
							.getDepartment().getDepartmentName());
					// reportingOfficer.setRole(reportingManager.getCandidate().getLoginEntity().getLoginEntityTypes().);
				}
				reportingOfficerList.add(reportingOfficer);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(reportingOfficerList);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);

		log.info("exit getAllReportingManagerList method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<ModeOfSeparationMasterVO>> getModeOfSeprationList() throws HRMSException {
		HRMSBaseResponse<List<ModeOfSeparationMasterVO>> response = new HRMSBaseResponse<>();
		log.info("inside getModeOfSeprationList method");

		List<ModeOfSeparationMasterVO> listOfSeparationMaster = new ArrayList<>();
		List<MasterModeofSeparation> modeOfSeparation = separationDAO.findAll();

		for (MasterModeofSeparation separation : modeOfSeparation) {
			ModeOfSeparationMasterVO separationVO = new ModeOfSeparationMasterVO();
			separationVO.setId(separation.getId());
			separationVO.setModeOfSeparationName(separation.getModeOfSeparationName());
			separationVO.setModeOfSeparationCode(separation.getModeOfSeparationCode());
			listOfSeparationMaster.add(separationVO);
		}
		response.setResponseBody(listOfSeparationMaster);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);

		log.info("exit getModeOfSeprationList method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<OrganizationVO>> getOrganization() throws HRMSException {

		log.info("Inside  getOrgnization Method");
		HRMSBaseResponse<List<OrganizationVO>> response = new HRMSBaseResponse<>();
		List<Organization> organizationMasters = organizationDAO.findByIsActive(ERecordStatus.Y.name());
		List<OrganizationVO> organizationVOs = new ArrayList<>();
		OrganizationVO organizationTypeVO = new OrganizationVO();
		if (!HRMSHelper.isNullOrEmpty(organizationMasters)) {
			for (Organization org : organizationMasters) {
				organizationTypeVO = new OrganizationVO();
				organizationTypeVO.setId(org.getId());
				organizationTypeVO.setOrganizationName(org.getOrganizationName());
				organizationTypeVO.setOrganizationShortName(org.getOrganizationShortName());
				organizationVOs.add(organizationTypeVO);

			}

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(organizationVOs);

		log.info("Exit from getOrgnization Method");
		return response;

	}

	@Override
	public HRMSBaseResponse<List<MasterCountryVO>> getAllCountries() throws HRMSException {
		log.info("inside getAllCountries Method");
		HRMSBaseResponse<List<MasterCountryVO>> response = new HRMSBaseResponse<>();

		List<MasterCountry> countryMasterList = countryDAO.findAllMasterCountryCustomQuery(IHRMSConstants.isActive);

		List<MasterCountryVO> countryList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(countryMasterList)) {
			for (MasterCountry countryMaster : countryMasterList) {

				MasterCountryVO countryVO = new MasterCountryVO();
				countryVO.setId(countryMaster.getId());
				countryVO.setCountryName(countryMaster.getCountryName());
				countryVO.setCountryDescription(countryMaster.getCountryDescription());
				countryList.add(countryVO);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(countryList);
		log.info("exit from getAllCountries Method");
		return response;
	}

	@Override
	public List<BrandingVO> getBranding(Long orgId) throws HRMSException {

		log.info("Inside getBranding Method ");
		MasterBranding masterBranding = brandingDAO.findByIsActiveAndOrgId(IHRMSConstants.isActive, orgId);
		List<BrandingVO> brandingVo = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(masterBranding)) {
			BrandingVO brandings = new BrandingVO();
			brandings.setId(masterBranding.getId());
			brandings.setImagePath(masterBranding.getImagePath());
			brandings.setCompanyName(masterBranding.getCompanyName());
			brandings.setFavicon(masterBranding.getFavicon());
			brandings.setCopyRight(masterBranding.getCopyRight());
			brandings.setBgImagePath(masterBranding.getBgImagePath());
			brandings.setButtonColorCode(masterBranding.getButtonColorCode());
			brandings.setThemeColorCode(masterBranding.getThemeColorCode());
			brandingVo.add(brandings);
		}
		log.info("Exit from getBranding Method ");
		return brandingVo;
	}

	@Override
	public HRMSBaseResponse<List<MasterTravelTypeVO>> getTravelRequestType() throws HRMSException {
		HRMSBaseResponse<List<MasterTravelTypeVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getTravelType method");
		long travelRecord = travelTypeDAO.countMasterTravelType();
		List<MasterTravelType> travelTypeMaster = travelTypeDAO.findByIsActive(IHRMSConstants.isActive);
		List<MasterTravelTypeVO> travelTypeVoList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(travelTypeMaster)) {
			for (MasterTravelType travelType : travelTypeMaster) {
				MasterTravelTypeVO travelTypesVO = new MasterTravelTypeVO();
				travelTypesVO.setId(travelType.getId());
				travelTypesVO.setTravelType(travelType.getTravelType());
				travelTypesVO.setDescription(travelType.getDescription());
				travelTypeVoList.add(travelTypesVO);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(travelTypeVoList);
		response.setResponseCode(1200);
		response.setTotalRecord(travelRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getTravelType method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<MasterModeOfTravelVO>> getTravelMode() throws HRMSException {
		HRMSBaseResponse<List<MasterModeOfTravelVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getTravelMode method");
		long travelRecord = modeOfTravelDAO.countTravelMode();
		List<MasterModeOfTravel> modeOfTravelMaster = modeOfTravelDAO
				.getAllTravelModeByIsActive(IHRMSConstants.isActive);
		List<MasterModeOfTravelVO> modeOfTravelList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(modeOfTravelMaster)) {
			for (MasterModeOfTravel modeOfTravel : modeOfTravelMaster) {
				MasterModeOfTravelVO modeOfTravelVO = new MasterModeOfTravelVO();
				modeOfTravelVO.setId(modeOfTravel.getId());
				modeOfTravelVO.setModeOfTravel(modeOfTravel.getModeOfTravel());
				modeOfTravelVO.setModeOfTravelDescription(modeOfTravel.getModeOfTravelDescription());
				modeOfTravelList.add(modeOfTravelVO);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(modeOfTravelList);
		response.setResponseCode(1200);
		response.setTotalRecord(travelRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getTravelMode method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<TripTypeVO>> getTravelTripType() throws HRMSException {
		HRMSBaseResponse<List<TripTypeVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getTravelTripType method");
		long travelRecord = masterTripTypeDAO.countMasterTripType();
		List<MasterTripType> travelTripTypeMaster = masterTripTypeDAO.findByIsActive(IHRMSConstants.isActive);
		List<TripTypeVO> travelTripTypeVoList = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(travelTripTypeMaster)) {
			for (MasterTripType travelTripType : travelTripTypeMaster) {
				TripTypeVO travelTripTypesVO = new TripTypeVO();
				travelTripTypesVO.setId(travelTripType.getId());
				travelTripTypesVO.setDescription(travelTripType.getDescription());
				travelTripTypesVO.setTripType(travelTripType.getTripType());
				travelTripTypeVoList.add(travelTripTypesVO);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(travelTripTypeVoList);
		response.setResponseCode(1200);
		response.setTotalRecord(travelRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getTravelTripType method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<MasterBusTypeVO>> getBusType() throws HRMSException {
		HRMSBaseResponse<List<MasterBusTypeVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getBusType method");
		long totalRecord = busTypeDAO.countBusType();
		List<MasterBusTypeVO> busTypeList = new ArrayList<>();
		List<MasterBusType> masterBusType = busTypeDAO.findByIsActiveOrderById(IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(masterBusType)) {
			for (MasterBusType busType : masterBusType) {
				MasterBusTypeVO busTypeVO = new MasterBusTypeVO();
				busTypeVO.setId(busType.getId());
				busTypeVO.setBusType(busType.getBusType());
				busTypeVO.setDescription(busType.getDescription());
				busTypeList.add(busTypeVO);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(busTypeList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getBusType method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<MasterTravellerTypeVO>> getTravellerType() throws HRMSException {
		HRMSBaseResponse<List<MasterTravellerTypeVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getTravellerType method");
		long totalRecord = travellerTypeDAO.countTravellerType();
		List<MasterTravellerType> masterTravellerType = travellerTypeDAO.findByIsActive(IHRMSConstants.isActive);
		List<MasterTravellerTypeVO> travellerTypeList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(masterTravellerType)) {
			for (MasterTravellerType travellerType : masterTravellerType) {
				MasterTravellerTypeVO travellerTypeVO = new MasterTravellerTypeVO();
				travellerTypeVO.setId(travellerType.getId());
				travellerTypeVO.setTravellerType(travellerType.getTravellerType());
				travellerTypeVO.setDescription(travellerType.getDescription());
				travellerTypeList.add(travellerTypeVO);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(travellerTypeList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getTravellerType method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<MasterAirTypeVO>> getAirType() throws HRMSException {
		HRMSBaseResponse<List<MasterAirTypeVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getAirType method");
		long totalRecord = airTypeDAO.countAirType();
		List<MasterAirTypeVO> airTypeList = new ArrayList<>();
		List<MasterAirType> masterAirType = airTypeDAO.findByIsActive(IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(masterAirType)) {
			for (MasterAirType airType : masterAirType) {
				MasterAirTypeVO masterAirTypeVO = new MasterAirTypeVO();
				masterAirTypeVO.setId(airType.getId());
				masterAirTypeVO.setAirType(airType.getAirType());
				masterAirTypeVO.setDescription(airType.getDescription());
				airTypeList.add(masterAirTypeVO);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(airTypeList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getAirType method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<MasterVisaTypeVO>> getVisaType() throws HRMSException {
		log.info("Inside getVisaType Method");
		HRMSBaseResponse<List<MasterVisaTypeVO>> response = new HRMSBaseResponse<>();
		long totalRecord = visaTypeDAO.countVisaType();
		List<MasterVisaTypeVO> visaTypeList = new ArrayList<>();
		List<MasterVisaType> masterVisaType = visaTypeDAO.findByIsActiveOrderById(IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(masterVisaType)) {
			for (MasterVisaType visaType : masterVisaType) {
				MasterVisaTypeVO visaTypeVo = new MasterVisaTypeVO();
				visaTypeVo.setId(visaType.getId());
				visaTypeVo.setVisaType(visaType.getVisaType());
				visaTypeVo.setDescription(visaType.getDescription());
				visaTypeList.add(visaTypeVo);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(visaTypeList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from  getVisaType Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<MasterDriverVO>> getDriverList() throws HRMSException {
		HRMSBaseResponse<List<MasterDriverVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getDriverList Method");
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Employee employee = employeeDAO.findEmpCandByEmpId(Long.valueOf(employeeId));
		long divisionId = employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
		long totalRecord = driverDAO.countOfDriver(divisionId, IHRMSConstants.isActive);
		List<MasterDriver> masterDriver = driverDAO.findByDivIdAndIsActive(divisionId, IHRMSConstants.isActive);
		List<MasterDriverVO> driverList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(masterDriver)) {
			for (MasterDriver driver : masterDriver) {
				MasterDriverVO driverVO = new MasterDriverVO();
				driverVO.setId(driver.getId());
				driverVO.setDriverName(driver.getEmployee().getCandidate().getFirstName() + " "
						+ driver.getEmployee().getCandidate().getLastName());
				driverVO.setDriverEmployeeId(driver.getEmployee().getId());
				driverVO.setDivisionId(driver.getDivision().getId());
				driverList.add(driverVO);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(driverList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Inside getDriverList Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<MasterVehicleVO>> getVehicleList() throws HRMSException {
		HRMSBaseResponse<List<MasterVehicleVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getDriver Method");
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Employee employee = employeeDAO.findEmpCandByEmpId(Long.valueOf(employeeId));
		long divisionId = employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
		long totalRecord = vehicleDAO.countOfVehicle(divisionId, IHRMSConstants.isActive);
		List<MasterVehicle> masterVehicle = vehicleDAO.findAllVehicleByDivIdAndIsActive(divisionId,
				IHRMSConstants.isActive);
		List<MasterVehicleVO> vehicleList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(masterVehicle)) {
			for (MasterVehicle vehicle : masterVehicle) {
				MasterVehicleVO vehicleVO = new MasterVehicleVO();
				vehicleVO.setId(vehicle.getId());
				vehicleVO.setVehicleNumber(vehicle.getVehicleName());
				vehicleVO.setDivisionId(vehicle.getDivision().getId());
				vehicleList.add(vehicleVO);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(vehicleList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Inside getVehicle Method");
		return response;
	}

	@Override
	public TravelApproverResponseVO getApproverDetail(TravelDeskApproverRequestVO request) throws HRMSException {
		log.info("Inside getApproverName Method");
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		TravelApproverResponseVO approverResponse = new TravelApproverResponseVO();
		if (HRMSHelper.isRolePresent(roles, ERole.TRAVEL_DESK.name())) {
			TravelRequestV2 travelRequest = travelRequestDao.findByIdAndIsActive(request.getTravelRequestId(),
					IHRMSConstants.isActive);
			if (HRMSHelper.isNullOrEmpty(travelRequest)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1551));
			}

			MasterTravelApproverSlab travelApproverSlab = null;
			if (!HRMSHelper.isNullOrEmpty(travelRequest.getCurrency())
					&& !HRMSHelper.isNullOrEmpty(travelRequest.getCurrency().getEntityId())) {
				travelApproverSlab = slabDAO.findByAmountAndCurrencyAndIsActive(request.getAmount(),
						travelRequest.getCurrency().getEntityId(), ERecordStatus.Y.name());
			}

			// check is requet for mgmt employee
			if (travelDeskTransformUtil.isRequestForManagementEmployee(travelRequest)) {
				Employee employee = employeeDAO.findEmpCandByEmpId(tdApproverIdForMgmtEmployee);
				approverResponse.setId(employee.getId());
				approverResponse.setApproverName(
						employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
				approverResponse.setEmailId(employee.getOfficialEmailId());
			} else {
				Employee employee = employeeDAO.findEmpCandByEmpId(travelRequest.getRequesterId());
				Long divisionId = employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
				Long departmentId = employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
				Long branchId = employee.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
				if (!HRMSHelper.isNullOrEmpty(travelApproverSlab)) {
					MasterMapTravelApprover mapTravelApprover = travelApproverDAO
							.findByApproverSlabAndDivisionIdAndBranchIdAndDepartmentId(travelApproverSlab.getSlabId(),
									divisionId, branchId, departmentId);
					if (!HRMSHelper.isNullOrEmpty(mapTravelApprover)) {
						approverResponse.setId(mapTravelApprover.getApproverId().getId());
						approverResponse.setApproverName(mapTravelApprover.getApproverId().getCandidate().getFirstName()
								+ " " + mapTravelApprover.getApproverId().getCandidate().getLastName());
						approverResponse.setEmailId(mapTravelApprover.getApproverId().getOfficialEmailId());
					} else {
						throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
					}
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}
			}

		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		log.info("Exit from getApproverName Method");
		return approverResponse;
	}

	@Override
	public HRMSBaseResponse<List<CurrencyMasterVO>> getCurrencyList() throws HRMSException {
		HRMSBaseResponse<List<CurrencyMasterVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getCurrencyList Method");
		long totalRecord = currencyMasterDAO.currencyCount();
		List<CurrencyMaster> masterCurrency = currencyMasterDAO.findByIsActive(IHRMSConstants.isActive);
		List<CurrencyMasterVO> currencyList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(masterCurrency)) {
			for (CurrencyMaster currency : masterCurrency) {
				CurrencyMasterVO currencyMasterVO = new CurrencyMasterVO();
				currencyMasterVO.setEntityId(currency.getEntityId());
				currencyMasterVO.setCurrency(currency.getCurrency());
				currencyMasterVO.setCountryName(currency.getCountryName());
				currencyMasterVO.setSymbol(currency.getSymbol());
				currencyList.add(currencyMasterVO);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(currencyList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getCurrencyList Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<PrimaryRequesterVO>> getRequesterTypeList() throws HRMSException {
		HRMSBaseResponse<List<PrimaryRequesterVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getRequesterTypeList Method");
		long totalRecord = primaryRequesterTypeDAO.countTravellerType(IHRMSConstants.isActive);
		List<PrimaryRequester> requester = primaryRequesterTypeDAO.findByIsActive(IHRMSConstants.isActive);
		List<PrimaryRequesterVO> requesterList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(requester)) {
			for (PrimaryRequester requesters : requester) {
				PrimaryRequesterVO primaryRequesterVO = new PrimaryRequesterVO();
				primaryRequesterVO.setId(requesters.getId());
				primaryRequesterVO.setRequesterType(requesters.getRequesterType());

				requesterList.add(primaryRequesterVO);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(requesterList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getRequesterTypeList Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<StayTypeVO>> getStayList() throws HRMSException {
		HRMSBaseResponse<List<StayTypeVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getStayTypeList Method");
		long totalRecord = stayTypeDAO.countStayType(IHRMSConstants.isActive);
		List<MasterStayType> stayType = stayTypeDAO.findByIsActive(IHRMSConstants.isActive);
		List<StayTypeVO> typeList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(stayType)) {
			for (MasterStayType stay : stayType) {
				StayTypeVO stayTypeVO = new StayTypeVO();
				stayTypeVO.setId(stay.getId());
				stayTypeVO.setStayType(stay.getStayType());
				stayTypeVO.setDescription(stay.getDescription());

				typeList.add(stayTypeVO);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(typeList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getStayTypeList Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<ReimbursementTravelTypeVO>> getReimbursmentTravelTypeList() throws HRMSException {
		HRMSBaseResponse<List<ReimbursementTravelTypeVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getReimbursmentTravelTypeList Method");
		long totalRecord = reimbursementTravelTypeDAO.countTravelType();
		List<MasterReimbursementTravelType> travelType = reimbursementTravelTypeDAO
				.findByIsActiveOrderByIdAsc(IHRMSConstants.isActive);
		List<ReimbursementTravelTypeVO> typeList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(travelType)) {
			for (MasterReimbursementTravelType travel : travelType) {
				ReimbursementTravelTypeVO travelTypeVO = new ReimbursementTravelTypeVO();
				travelTypeVO.setId(travel.getId());
				travelTypeVO.setTravelType(travel.getTravelType());
				travelTypeVO.setDescription(travel.getDescription());

				typeList.add(travelTypeVO);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(typeList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getReimbursmentTravelTypeList Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<ClaimCategoryVO>> getClaimCategoryList() throws HRMSException {
		HRMSBaseResponse<List<ClaimCategoryVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getClaimCategoryList Method");
		long totalRecord = masterClaimCategoryDAO.countCategory();
		List<MasterClaimCategory> categoryType = masterClaimCategoryDAO.findByIsActive(IHRMSConstants.isActive);
		List<ClaimCategoryVO> typeList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(categoryType)) {
			for (MasterClaimCategory claim : categoryType) {
				ClaimCategoryVO typeVO = new ClaimCategoryVO();
				typeVO.setId(claim.getId());
				typeVO.setClaimCategory(claim.getClaimCategory());
				typeVO.setDescription(claim.getDescription());
				typeList.add(typeVO);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(typeList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getClaimCategoryList Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<ClaimTypeVO>> getClaimTypeList() throws HRMSException {
		HRMSBaseResponse<List<ClaimTypeVO>> response = new HRMSBaseResponse<>();
		log.info("Inside getClaimTypeList Method");
		long totalRecord = masterClaimTypeDAO.countofClaimTypeByIsActive(IHRMSConstants.isActive);
		List<MasterClaimType> claimtypeList = masterClaimTypeDAO.findByIsActiveOrderByIdAsc(IHRMSConstants.isActive);
		List<ClaimTypeVO> claimtypes = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(claimtypeList)) {
			for (MasterClaimType claims : claimtypeList) {
				ClaimTypeVO claimTypeVO = new ClaimTypeVO();
				claimTypeVO.setId(claims.getId());
				claimTypeVO.setClaimType(claims.getClaimType());
				claimTypeVO.setClaimCategory(claims.getClaimCategory());
				claimTypeVO.setDescription(claims.getDescription());
				claimtypes.add(claimTypeVO);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(claimtypes);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getClaimTypeList Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<MasterTitleVo>> getMasterTitle() throws HRMSException {
		HRMSBaseResponse<List<MasterTitleVo>> response = new HRMSBaseResponse<>();
		log.info("Inside getMasterTitle Method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		} else {
			long totalRecord = masterTitleDao.countOfRecord(IHRMSConstants.isActive);
			List<MasterTitle> masterTitleList = masterTitleDao.findByIsActive(IHRMSConstants.isActive);
			List<MasterTitleVo> masterTitles = new ArrayList<>();
			if (!HRMSHelper.isNullOrEmpty(masterTitleList)) {
				for (MasterTitle masterTitle : masterTitleList) {
					MasterTitleVo masterTitleVo = new MasterTitleVo();
					masterTitleVo.setId(masterTitle.getId());
					masterTitleVo.setTitle(masterTitle.getTitle());
					masterTitleVo.setTitleDescription(masterTitle.getTitleDescription());
					masterTitles.add(masterTitleVo);
				}
			} else {
				throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
			}
			response.setResponseBody(masterTitles);
			response.setResponseCode(1200);
			response.setTotalRecord(totalRecord);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			log.info("Exit from getMasterTitle Method");
			return response;
		}
	}

	@Override
	public HRMSBaseResponse<List<MasterMaritialStatusVo>> getMaritalStatus() throws HRMSException {
		HRMSBaseResponse<List<MasterMaritialStatusVo>> response = new HRMSBaseResponse<>();
		log.info("Inside getMasterTitle Method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		} else {
			long totalRecord = masterMaritalStatusDao.countOfRecord(IHRMSConstants.isActive);
			List<MasterMaritialStatus> masterMaritalStatusList = masterMaritalStatusDao
					.findByIsActive(IHRMSConstants.isActive);
			List<MasterMaritialStatusVo> masterMaritalStatuses = new ArrayList<>();
			if (!HRMSHelper.isNullOrEmpty(masterMaritalStatusList)) {
				for (MasterMaritialStatus masterMaritalStatus : masterMaritalStatusList) {
					MasterMaritialStatusVo masterMaritalStatusVo = new MasterMaritialStatusVo();
					masterMaritalStatusVo.setId(masterMaritalStatus.getId());
					masterMaritalStatusVo.setMaritalStatus(masterMaritalStatus.getMaritalStatus());
					masterMaritalStatusVo
							.setMaritalStatusDescription(masterMaritalStatus.getMaritalStatusDescription());
					masterMaritalStatuses.add(masterMaritalStatusVo);
				}
			} else {
				throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
			}

			response.setResponseBody(masterMaritalStatuses);
			response.setResponseCode(1200);
			response.setTotalRecord(totalRecord);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			log.info("Exit from getMasterTitle Method");
			return response;
		}
	}

	@Override
	public List<MasterCityVO> getCityList(long stateId) throws HRMSException {
		log.info("Inside getCityList Method ");

		List<MasterCity> masterCity = cityDao.findAllMasterCityByStateIdCustomQuery(stateId, IHRMSConstants.isActive);
		List<MasterCityVO> masterCityVO = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(masterCity)) {
			for (MasterCity cityEntity : masterCity) {
				MasterCityVO city = new MasterCityVO();
				city.setId(cityEntity.getId());
				city.setCityName(cityEntity.getCityName());
				city.setCityDescription(cityEntity.getCityDescription());
				masterCityVO.add(city);

			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}

		log.info("Exit from getCityList Method ");
		return masterCityVO;
	}

	@Override
	public HRMSBaseResponse<List<EmploymentTypeVO>> getEmploymentTypeList() throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "get EmploymentType List");
		}
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		HRMSBaseResponse<List<EmploymentTypeVO>> response = new HRMSBaseResponse<>();
		List<EmploymentTypeVO> typeList = new ArrayList<>();
		List<MasterEmploymentType> employmentTypes = employmentTypeDAO.findByIsActive(IHRMSConstants.isActive);
		long totalRecord = employmentTypeDAO.countOfTotalRecord(IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(employmentTypes)) {
			for (MasterEmploymentType list : employmentTypes) {
				EmploymentTypeVO typeVO = new EmploymentTypeVO();
				typeVO.setId(list.getId());
				typeVO.setEmploymentTypeName(list.getEmploymentTypeName());
				typeVO.setEmploymentTypeDescription(list.getEmploymentTypeDescription());
				typeList.add(typeVO);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(typeList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "get EmploymentType List");
		}
		return response;
	}

	@Override
	public HRMSBaseResponse<List<LoginEntityTypeVO>> getLoginEntityTypes() throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "get EmploymentType List");
		}
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		HRMSBaseResponse<List<LoginEntityTypeVO>> response = new HRMSBaseResponse<>();
		List<LoginEntityTypeVO> typeList = new ArrayList<>();
		List<LoginEntityType> loginEntityTypes = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(orgId)) {
			loginEntityTypes = loginEntityTypeDAO.findByOrgIdAndIsActive(orgId, IHRMSConstants.isActive);
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}

		long totalRecord = loginEntityTypes.size();
		if (!HRMSHelper.isNullOrEmpty(loginEntityTypes)) {
			for (LoginEntityType list : loginEntityTypes) {
				LoginEntityTypeVO typeVO = new LoginEntityTypeVO();
				typeVO.setId(list.getId());
				typeVO.setLoginEntityTypeName(list.getLoginEntityTypeName());
				typeVO.setDescription(list.getDescription());
				typeList.add(typeVO);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(typeList);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		if (log.isInfoEnabled()) {
			log.info(LogConstants.EXITED.template(), "get EmploymentType List");
		}
		return response;
	}

	@Override
	public HRMSBaseResponse<List<SelfRatingVo>> getSelfRating() throws HRMSException {
		HRMSBaseResponse<List<SelfRatingVo>> response = new HRMSBaseResponse<>();

		KraCycle activeCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(activeCycle)) {

			List<KraRating> kraRatingList = kraRatingDAO.findByIsActiveOrderById(ERecordStatus.Y.name());

			long totalRecord = 0;
			List<SelfRatingVo> kraRatingResponse = new ArrayList<>();

			if (!HRMSHelper.isNullOrEmpty(kraRatingList)) {

				Map<String, SelfRatingVo> uniqueRatings = new LinkedHashMap<>();

				for (KraRating kraRating : kraRatingList) {
					SelfRatingVo selfRatingVo = new SelfRatingVo();
					selfRatingVo.setId(kraRating.getId());
					selfRatingVo.setValue(kraRating.getValue());

					// Translate label only for Half/Mid cycles
					String translatedLabel = kraRating.getLabel();
					if (IHRMSConstants.HALF_YEAR_TYPE_ID.equals(activeCycle.getCycleTypeId())) {
						translatedLabel = translateRatingLabel(kraRating.getLabel());
					}

					selfRatingVo.setLabel(translatedLabel);
					selfRatingVo.setCategoryId(kraRating.getCategory().getId());
					selfRatingVo.setIsPassFail(kraRating.getIsPassFail());
					selfRatingVo.setOnOccurrence(kraRating.getOnOccurrence());

					String key;
					if (IHRMSConstants.PASS.equalsIgnoreCase(kraRating.getLabel())
							|| IHRMSConstants.FAIL.equalsIgnoreCase(kraRating.getLabel())) {

						key = kraRating.getLabel().trim() + "_" + selfRatingVo.getCategoryId();
					} else {

						key = translatedLabel + "_" + selfRatingVo.getCategoryId();
					}

					if (!uniqueRatings.containsKey(key) || selfRatingVo.getValue().doubleValue() > uniqueRatings
							.get(key).getValue().doubleValue()) {
						uniqueRatings.put(key, selfRatingVo);
					}
				}

				kraRatingResponse.addAll(uniqueRatings.values());

			} else {
				throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
			}

			response.setResponseBody(kraRatingResponse);
			response.setResponseCode(1200);
			response.setTotalRecord(totalRecord);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			log.info("Exit from getSelfRating Method");
			return response;
		} else {
			throw new HRMSException(1522, "No active cycle found");
		}
	}

	/*
	 * private String translateRatingLabel(String dbLabel) { if (dbLabel == null) {
	 * return null; }
	 * 
	 * switch (dbLabel.trim()) {
	 * 
	 * case "1": case "1 [Low]": case "1 [High]": return "Unsatisfactory";
	 * 
	 * 
	 * case "2": case "2 [Low]": case "2 [Mid]": case "2 [High]": return
	 * "Developing";
	 * 
	 * 
	 * case "3": case "3 [Low]": case "3 [Mid]": case "3 [High]": return
	 * "Performing";
	 * 
	 * 
	 * case "4": case "4 [Low]": case "4 [High]": return "Exceptional";
	 * 
	 * 
	 * default: return dbLabel; } }
	 */

	private String translateRatingLabel(String dbLabel) {
		if (dbLabel == null) {
			return null;
		}

		switch (dbLabel.trim()) {

		// Fail → Need Improvement
		case "Fail":
		case "1":
		case "1 [Low]":
		case "1 [High]":
		case "2":
		case "2 [Low]":
		case "2 [Mid]":
		case "2 [High]":
			return "Need Improvement";

		// Pass → On Track
		case "Pass":
		case "3":
		case "3 [Low]":
		case "3 [Mid]":
		case "3 [High]":
		case "4":
		case "4 [Low]":
		case "4 [High]":
			return "On Track";

		default:
			return dbLabel;
		}
	}

	@Override
	public HRMSBaseResponse<List<KraYearResponseVo>> getNewYearList(Pageable pageable) throws HRMSException {
		HRMSBaseResponse<List<KraYearResponseVo>> response = new HRMSBaseResponse<>();
		log.info("get getYearList Method");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.DESC, "id"));

//		List<KraYear> yearMaster = krayeardao.getByIsActive(ERecordStatus.Y.name());
		Page<KraYear> yearPage = krayeardao.findAll(sortedPageable);
		List<KraYearResponseVo> yearList = new ArrayList<>();
//		Long id = 1L;
//		if (!HRMSHelper.isNullOrEmpty(yearMaster)) {
//			for (KraYear year : yearMaster) {
//				KraYearVo yearVO = new KraYearVo();
//				yearVO.setId(id++);
//				yearVO.setYear(year.getYear());
//				yearList.add(yearVO);
//			}

		if (!HRMSHelper.isNullOrEmpty(yearPage.getContent())) {
			for (KraYear year : yearPage.getContent()) {
				KraYearResponseVo yearVO = new KraYearResponseVo();
				yearVO.setYearId(year.getId());
				yearVO.setYearName(year.getYear());
				yearVO.setIsActive(year.getIsActive());
				yearList.add(yearVO);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(yearList);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setTotalRecord((int) yearPage.getTotalElements());
		response.setApplicationVersion(applicationVersion);

		log.info("Exit getNewYearList Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<DisplayStatusVo>> getDisplayStatus() throws HRMSException {
		HRMSBaseResponse<List<DisplayStatusVo>> response = new HRMSBaseResponse<>();

		List<KraDisplayStatus> krastatusList = kradisplaystatusDao.findAll();
		long totalRecord = 0;

		List<DisplayStatusVo> kradisplaystatusResponse = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(krastatusList)) {
			for (KraDisplayStatus kradisplaystatus : krastatusList) {
				DisplayStatusVo displayStatusVo = new DisplayStatusVo();
				displayStatusVo.setId(kradisplaystatus.getId());
				displayStatusVo.setName(kradisplaystatus.getDisplayName());
				kradisplaystatusResponse.add(displayStatusVo);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(kradisplaystatusResponse);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getDisplayStatus Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<KraCycleVo>> getKraCycleList(Long yearId) throws HRMSException {
		HRMSBaseResponse<List<KraCycleVo>> response = new HRMSBaseResponse<>();
		log.info("get getKraCycleList Method");
		Optional<KraYear> year = krayeardao.findById(yearId);
		List<KraCycle> kraCycle = kraCycleDAO.findByYear(year);
		List<KraCycleVo> cycleList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(kraCycle)) {
			for (KraCycle cycle : kraCycle) {
				KraCycleVo cycleVO = new KraCycleVo();
				cycleVO.setId(cycle.getId());
				cycleVO.setLabel(cycle.getCycleName());
				cycleList.add(cycleVO);
			}

		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseBody(cycleList);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		log.info("Exit getKraCycleList Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<KraCycleStatusVo>> getKraCycleStatusList() throws HRMSException {
		HRMSBaseResponse<List<KraCycleStatusVo>> response = new HRMSBaseResponse<>();
		log.info("get getKraCycleStatusList Method");
		List<KraCycleStatus> cycleStatus = kraStatusDao.findByIsActive(ERecordStatus.Y.name());

		List<KraCycleStatusVo> cycleStatusList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(cycleStatus)) {
			for (KraCycleStatus status : cycleStatus) {
				KraCycleStatusVo cycleStatusVO = new KraCycleStatusVo();
				cycleStatusVO.setId(status.getId());
				cycleStatusVO.setLabel(status.getStatus());
				cycleStatusList.add(cycleStatusVO);
			}

		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseBody(cycleStatusList);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		log.info("Exit getKraCycleList Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<KraYearVo>> getKraYears(String flag) throws HRMSException {
		log.info("Enter into from getKraYears Method");
		HRMSBaseResponse<List<KraYearVo>> response = new HRMSBaseResponse<>();

		List<KraYear> kraYearList = kraYearDao.findAllYearsByAsc();
		List<KraYearVo> kraListVo = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(kraYearList)) {

			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			for (KraYear kraYear : kraYearList) {
				int year = parseYear(kraYear.getYear());

				if (flag.equalsIgnoreCase(IHRMSConstants.isActive) && year == currentYear) {
					KraYearVo kraYearVo = new KraYearVo();
					kraYearVo.setId(kraYear.getId());
					kraYearVo.setYear(kraYear.getYear());
					kraListVo.add(kraYearVo);
					break;
				} else if (flag.equalsIgnoreCase(IHRMSConstants.isNotActive) && year >= currentYear) {
					KraYearVo kraYearVo = new KraYearVo();
					kraYearVo.setId(kraYear.getId());
					kraYearVo.setYear(kraYear.getYear());
					kraListVo.add(kraYearVo);
				}
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(kraListVo);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getKraYears Method");
		return response;

	}

	private int parseYear(String year) throws HRMSException {
		try {
			return Integer.parseInt(year);
		} catch (NumberFormatException e) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1631));
		}
	}

	@Override
	public HRMSBaseResponse<List<CategoryResponseVo>> getCategory() throws HRMSException {
		log.info("Enter into from getCategory Method");

		HRMSBaseResponse<List<CategoryResponseVo>> response = new HRMSBaseResponse<>();

		List<Category> categoryList = categoryDAO.findByIsActive(ERecordStatus.Y.name());
		List<CategoryResponseVo> categoryListVo = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(categoryList)) {
			for (Category category : categoryList) {
				CategoryResponseVo categoryVo = new CategoryResponseVo();
				categoryVo.setId(category.getId());
				categoryVo.setLabel(category.getCategoryName());
				categoryVo.setWeight(category.getWeightage());
				categoryListVo.add(categoryVo);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(categoryListVo);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getCategory Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<SubCategoryMasterVo>> getSubCategory(Long categoryId) throws HRMSException {
		log.info("Enter into getSubCategory Method");
		HRMSBaseResponse<List<SubCategoryMasterVo>> response = new HRMSBaseResponse<>();
		List<SubCategoryMasterVo> subCategoryVoList = new ArrayList<>();
		List<Subcategory> subCategoryList = subCategoryDao.findBySubCategoryId(categoryId, IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(subCategoryList)) {
			setSubCategoryResponse(subCategoryVoList, subCategoryList);
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(subCategoryVoList);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getSubCategory Method");
		return response;
	}

	private void setSubCategoryResponse(List<SubCategoryMasterVo> subCategoryVoList,
			List<Subcategory> subCategoryList) {
		for (Subcategory subCategory : subCategoryList) {
			SubCategoryMasterVo subCategoryVo = new SubCategoryMasterVo();

			subCategoryVo.setCategoryId(subCategory.getCategory().getId());
			subCategoryVo.setSubCategoryId(subCategory.getId());
			subCategoryVo.setSubCategoryName(subCategory.getSubCategoryName());
			subCategoryVoList.add(subCategoryVo);
		}
	}

	@Override
	public HRMSBaseResponse<KraCycleRequestVo> getKraCycleById(Long cycleId) throws HRMSException {
		HRMSBaseResponse<KraCycleRequestVo> response = new HRMSBaseResponse<>();
		log.info("Inside getKraCycleById Method");
		if (!HRMSHelper.isNullOrEmpty(cycleId)) {
			KraCycle kraCycle = kraCycleDAO.findById(cycleId).get();
			KraCycleRequestVo cycleVO = new KraCycleRequestVo();
			if (!HRMSHelper.isNullOrEmpty(kraCycle)) {
				cycleVO.setCycleId(kraCycle.getId());
				cycleVO.setCycleName(kraCycle.getCycleName());
				cycleVO.setDescription(kraCycle.getDescription());
				cycleVO.setEndDate(HRMSDateUtil.format(kraCycle.getEndDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				cycleVO.setStartDate(
						HRMSDateUtil.format(kraCycle.getStartDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				cycleVO.setYearId(kraCycle.getYear().getId());
			} else {
				throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
			}
			response.setResponseBody(cycleVO);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
			log.info("Exit from getKraCycleById Method");
			return response;
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
	}

	@Override
	public List<GradeMasterVo> getGradeList() throws HRMSException {
		log.info("Inside getGradeList Method");

		List<GradeMaster> gradeMasters = gradeDao.findAllSortedGrades(IHRMSConstants.isActive);
		List<GradeMasterVo> gradeMasterVos = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(gradeMasters)) {
			for (GradeMaster gradeMaster : gradeMasters) {

				GradeMasterVo gradeMasterVo = new GradeMasterVo();
				gradeMasterVo.setId(gradeMaster.getId());
				gradeMasterVo.setGradeDescription(gradeMaster.getGradeDescription());
				gradeMasterVo.setCareerLevel(gradeMaster.getCareerLevel());
				gradeMasterVo.setIsActive(gradeMaster.getIsActive());
				gradeMasterVos.add(gradeMasterVo);
			}
		} else {

			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		log.info("Exit from getGradeList Method");
		return gradeMasterVos;
	}

	@Override
	public List<KpiTypeMasterVo> getKpiTypeList() throws HRMSException {
		log.info("Inside getKpiTypeList Method");

		List<KpiTypeMaster> kpiTypeMasters = kpiTypeDao.findByIsActive(IHRMSConstants.isActive);
		List<KpiTypeMasterVo> kpiTypeMasterVos = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(kpiTypeMasters)) {
			for (KpiTypeMaster kpiTypeMaster : kpiTypeMasters) {

				KpiTypeMasterVo kpiTypeMasterVo = new KpiTypeMasterVo();
				kpiTypeMasterVo.setId(kpiTypeMaster.getId());
				kpiTypeMasterVo.setDescription(kpiTypeMaster.getDescription());
				kpiTypeMasterVo.setKpiType(kpiTypeMaster.getKpiType());
				kpiTypeMasterVos.add(kpiTypeMasterVo);
			}
		} else {

			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		log.info("Exit from getGradeList Method");
		return kpiTypeMasterVos;
	}

	@Override
	public List<KpiMetricVo> getKpiMetricList() throws HRMSException {
		log.info("Inside getKpiTypeList Method");

		List<KpiMetricMaster> kpiMetricMasters = kpiMetricDao.findByIsActive(IHRMSConstants.isActive);
		List<KpiMetricVo> kpiMetricVos = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(kpiMetricMasters)) {
			for (KpiMetricMaster kpiMetricMaster : kpiMetricMasters) {
				KpiMetricVo kpiMetricVo = new KpiMetricVo();
				kpiMetricVo.setId(kpiMetricMaster.getId());
				kpiMetricVo.setDescription(kpiMetricMaster.getDescription());
				kpiMetricVo.setMetric(kpiMetricMaster.getMetric());
				kpiMetricVos.add(kpiMetricVo);
			}

			// Sort the list: 'Y' first, then 'N' (case-insensitive, nulls last)
			kpiMetricVos
					.sort(Comparator.comparing(vo -> ERecordStatus.Y.name().equalsIgnoreCase(vo.getMetric()) ? 0 : 1));

		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		log.info("Exit from getKpiTypeList Method");
		return kpiMetricVos;
	}

	@Override
	public List<VOMasterRole> getRoleList() throws HRMSException {
		log.info("Inside getRoleList Method");

		List<MasterRole> masterRoles = roleDao.findByIsActive(IHRMSConstants.isActive);
		List<VOMasterRole> voMaterRoles = new ArrayList();

		if (!HRMSHelper.isNullOrEmpty(masterRoles)) {
			for (MasterRole masterRole : masterRoles) {

				VOMasterRole voMasterRole = new VOMasterRole();
				voMasterRole.setId(masterRole.getId());
				voMasterRole.setRoleName(masterRole.getRoleName());
				voMasterRole.setRoleDescription(masterRole.getRoleDescription());
				voMaterRoles.add(voMasterRole);
			}
		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		log.info("Exit from getRoleList Method");
		return voMaterRoles;
	}

	@Override
	public HRMSBaseResponse<List<KraYearVo>> getKraYearsPrevious() throws HRMSException {
		log.info("Enter into from getKraYearsPervious Method");
		HRMSBaseResponse<List<KraYearVo>> response = new HRMSBaseResponse<>();

		List<KraYear> kraYearList = kraYearDao.findAllYearsByAsc();
		List<KraYearVo> kraListVo = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(kraYearList)) {

			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			int perviousYear = currentYear - 1;

			for (KraYear kraYear : kraYearList) {
				int year = parseYearPervious(kraYear.getYear());

				if (year >= perviousYear) {
					KraYearVo kraYearVo = new KraYearVo();
					kraYearVo.setId(kraYear.getId());
					kraYearVo.setYear(kraYear.getYear());
					kraListVo.add(kraYearVo);
				}
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(kraListVo);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getKraYearsPervious Method");
		return response;

	}

	private int parseYearPervious(String year) throws HRMSException {
		try {
			return Integer.parseInt(year);
		} catch (NumberFormatException e) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1631));
		}
	}

	@Override
	public HRMSBaseResponse<String> deleteState(StateVO request) throws HRMSException {
		log.info("Inside deleteState method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!(HRMSHelper.isRolePresent(roles, ERole.HR.name())
				|| HRMSHelper.isRolePresent(roles, ERole.ADMIN.name()))) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_STATE, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		String responseMessage = performDeleteState(request, loggedInEmpId);

		response.setResponseCode(1200);
		response.setResponseMessage(responseMessage);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	private String performDeleteState(StateVO request, Long loggedInEmpId) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(request.getId())) {

			if (HRMSHelper.isNullOrEmpty(request.getCountryId())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1801));
			}

			MasterState state = stateDAO.findById(request.getId()).orElse(null);

			if (state == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1800));
			}

			if (IHRMSConstants.isActive.equals(state.getIsActive())) {
				boolean isMapped = candidateAddressDAO.existsByState_IdAndState_IsActive(request.getId(),
						IHRMSConstants.isActive);
				if (isMapped) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1850));
				}
				state.setIsActive(IHRMSConstants.isNotActive);
				state.setUpdatedBy(loggedInEmpId.toString());
				state.setUpdatedDate(new Date());
				stateDAO.save(state);
				return ResponseCode.getResponseCodeMap().get(1799);
			} else {
				state.setIsActive(IHRMSConstants.isActive);
				state.setUpdatedBy(loggedInEmpId.toString());
				state.setUpdatedDate(new Date());
				stateDAO.save(state);
				return ResponseCode.getResponseCodeMap().get(1849);
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
	}

	@Override
	public HRMSBaseResponse<String> addCountry(CountryMasterVO country) throws HRMSException {
		log.info("Inside addCountry method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		masterHelper.validateCountryVo(country);

		if (HRMSHelper.isNullOrEmpty(country.getId())
				&& Boolean.TRUE.equals(countryDAO.existsByCountryName(country.getCountryName()))) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_COUNTRY, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		return createOrUpdateCountry(country, response, loggedInEmpId, orgId);
	}

	private HRMSBaseResponse<String> createOrUpdateCountry(CountryMasterVO country, HRMSBaseResponse<String> response,
			Long loggedInEmpId, Long orgId) throws HRMSException {

		MasterCountry mastercountry;

		if (!HRMSHelper.isNullOrEmpty(country.getId())) {

			mastercountry = countryDAO.findByIdAndIsActive(country.getId(), IHRMSConstants.isActive);
			if (mastercountry == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1801));
			}

			mastercountry.setUpdatedBy(loggedInEmpId.toString());
			mastercountry.setUpdatedDate(new Date());

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1802));
		} else {
			mastercountry = new MasterCountry();
			mastercountry.setCreatedBy(loggedInEmpId.toString());
			mastercountry.setCreatedDate(new Date());

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1803));
		}

		mastercountry.setCountryName(country.getCountryName());
		mastercountry.setCountryDescription(country.getCountryDescription());
		mastercountry.setIsActive(IHRMSConstants.isActive);
		mastercountry.setOrgId(orgId);

		countryDAO.save(mastercountry);
		response.setApplicationVersion(applicationVersion);

		return response;
	}

	@Override
	public HRMSBaseResponse<String> deleteCountry(CountryMasterVO request) throws HRMSException {
		log.info("Inside deleteCountry method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_COUNTRY, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		String responseMessage = performDeleteCountry(request, loggedInEmpId);

		response.setResponseCode(1200);
		response.setResponseMessage(responseMessage);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	private String performDeleteCountry(CountryMasterVO request, Long loggedInEmpId) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(request.getId())) {
			MasterCountry country = countryDAO.findById(request.getId()).orElse(null);
			if (country == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1801));
			}

			if (IHRMSConstants.isActive.equals(country.getIsActive())) {
				boolean isMapped = candidateAddressDAO.existsByCountry_IdAndCountry_IsActive(request.getId(),
						IHRMSConstants.isActive);
				if (isMapped) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1847));
				}

				country.setIsActive(IHRMSConstants.isNotActive);
				country.setUpdatedBy(loggedInEmpId.toString());
				country.setUpdatedDate(new Date());
				countryDAO.save(country);
				return ResponseCode.getResponseCodeMap().get(1811);
			} else {
				country.setIsActive(IHRMSConstants.isActive);
				country.setUpdatedBy(loggedInEmpId.toString());
				country.setUpdatedDate(new Date());
				countryDAO.save(country);
				return ResponseCode.getResponseCodeMap().get(1848);
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
	}

	@Override
	public HRMSBaseResponse<String> addCity(CityMasterVO city) throws HRMSException {
		log.info("Inside addCity method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		masterHelper.validateCityVo(city);

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_CITY, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		if (HRMSHelper.isNullOrEmpty(city.getId())
				&& Boolean.TRUE.equals(cityDao.existsByCityName(city.getCityName()))) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
		}

		MasterState state = stateDAO.findByIdAndIsActive(city.getStateId(), IHRMSConstants.isActive);
		if (state == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1801));
		}

		return createOrUpdateCity(city, response, loggedInEmpId, state, orgId);
	}

	private HRMSBaseResponse<String> createOrUpdateCity(CityMasterVO city, HRMSBaseResponse<String> response,
			Long loggedInEmpId, MasterState state, Long orgId) throws HRMSException {

		MasterCity cityMaster;

		if (!HRMSHelper.isNullOrEmpty(city.getId())) {
			cityMaster = cityDao.findByIdAndIsActive(city.getId(), IHRMSConstants.isActive);
			if (cityMaster == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1807));
			}

			cityMaster.setUpdatedBy(loggedInEmpId.toString());
			cityMaster.setUpdatedDate(new Date());

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1808));
		} else {
			cityMaster = new MasterCity();
			cityMaster.setCreatedBy(loggedInEmpId.toString());
			cityMaster.setCreatedDate(new Date());

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1809));
		}
		cityMaster.setCityName(city.getCityName());
		cityMaster.setCityDescription(city.getCityDescription());
		cityMaster.setMasterState(state);
		cityMaster.setIsActive(IHRMSConstants.isActive);
		cityMaster.setOrgId(orgId);

		cityDao.save(cityMaster);

		response.setApplicationVersion(applicationVersion);
		log.info("Exit from addCity Method");

		return response;
	}

	@Override
	public HRMSBaseResponse<String> deleteCity(CityMasterVO request) throws HRMSException {

		log.info("Inside deleteState method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_CITY, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		String responseMessage = performDeleteCity(request, loggedInEmpId);

		response.setResponseCode(1200);
		response.setResponseMessage(responseMessage);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	private String performDeleteCity(CityMasterVO request, Long loggedInEmpId) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}

		if (HRMSHelper.isNullOrEmpty(request.getStateId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1800));
		}

		MasterCity city = cityDao.findById(request.getId()).orElse(null);
		if (city == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1807));
		}

		if (IHRMSConstants.isActive.equals(city.getIsActive())) {
			boolean isMapped = candidateAddressDAO.existsByCity_IdAndCity_IsActive(request.getId(),
					IHRMSConstants.isActive);
			if (isMapped) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1851));
			}

			city.setIsActive(IHRMSConstants.isNotActive);
			city.setUpdatedBy(loggedInEmpId.toString());
			city.setUpdatedDate(new Date());
			cityDao.save(city);
			return ResponseCode.getResponseCodeMap().get(1810);
		} else {
			city.setIsActive(IHRMSConstants.isActive);
			city.setUpdatedBy(loggedInEmpId.toString());
			city.setUpdatedDate(new Date());
			cityDao.save(city);
			return ResponseCode.getResponseCodeMap().get(1852);
		}
	}

	@Override
	public HRMSBaseResponse<String> addMetric(KpiMetricVo metricVo) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		masterHelper.validateKpiMetricVO(metricVo);

		if (HRMSHelper.isNullOrEmpty(metricVo.getId())
				&& Boolean.TRUE.equals(kpiMetricDao.existsByMetric(metricVo.getMetric()))) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_KPI_METRIC, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		return createOrUpdateMetric(metricVo, response, loggedInEmpId, orgId);
	}

	private HRMSBaseResponse<String> createOrUpdateMetric(KpiMetricVo metricVo, HRMSBaseResponse<String> response,
			Long loggedInEmpId, Long orgId) throws HRMSException {

		KpiMetricMaster metric;

		if (!HRMSHelper.isNullOrEmpty(metricVo.getId())) {
			metric = kpiMetricDao.findByIdAndIsActive(metricVo.getId(), IHRMSConstants.isActive);
			if (metric == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1812));
			}
			metric.setUpdatedBy(loggedInEmpId.toString());
			metric.setUpdatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1813));
		} else {
			metric = new KpiMetricMaster();
			metric.setCreatedBy(loggedInEmpId.toString());
			metric.setCreatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1814));
		}

		metric.setMetric(metricVo.getMetric());
		metric.setDescription(metricVo.getDescription());
		metric.setIsActive(IHRMSConstants.isActive);
		metric.setOrgId(orgId);

		kpiMetricDao.save(metric);

		response.setResponseCode(1200);
		response.setApplicationVersion(applicationVersion);

		return response;
	}

	@Override
	public HRMSBaseResponse<String> deleteMetric(KpiMetricVo request) throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_KPI_METRIC, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		String resultMessage = performDeleteMetric(request, loggedInEmpId);

		response.setResponseCode(1200);
		response.setResponseMessage(resultMessage);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	private String performDeleteMetric(KpiMetricVo request, Long loggedInEmpId) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}

		KpiMetricMaster metric = kpiMetricDao.findById(request.getId()).orElse(null);
		if (metric == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1812));
		}

		if (IHRMSConstants.isActive.equals(metric.getIsActive())) {
			boolean isMapped = kraDetailsDao.existsByMeasurementCriteriaAndIsActive(metric.getMetric(),
					IHRMSConstants.isActive);

			if (isMapped) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1854));
			}
			metric.setIsActive(IHRMSConstants.isNotActive);
			metric.setUpdatedBy(loggedInEmpId.toString());
			metric.setUpdatedDate(new Date());
			kpiMetricDao.save(metric);

			return ResponseCode.getResponseCodeMap().get(1815);
		} else {

			metric.setIsActive(IHRMSConstants.isActive);
			metric.setUpdatedBy(loggedInEmpId.toString());
			metric.setUpdatedDate(new Date());
			kpiMetricDao.save(metric);

			return ResponseCode.getResponseCodeMap().get(1855);
		}
	}

	@Override
	public HRMSBaseResponse<String> addGrade(GradeMasterVo gradeVo) throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		masterHelper.validateGradeVO(gradeVo);

		if (HRMSHelper.isNullOrEmpty(gradeVo.getId())
				&& Boolean.TRUE.equals(gradeDao.existsByGradeDescription(gradeVo.getGradeDescription()))) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_GRADE, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		return createOrUpdateGrade(gradeVo, response, loggedInEmpId, orgId);
	}

	private HRMSBaseResponse<String> createOrUpdateGrade(GradeMasterVo gradeVo, HRMSBaseResponse<String> response,
			Long loggedInEmpId, Long orgId) throws HRMSException {

		GradeMaster grade;
		if (!HRMSHelper.isNullOrEmpty(gradeVo.getId())) {

			grade = gradeDao.findByIdAndIsActive(gradeVo.getId(), IHRMSConstants.isActive);
			if (grade == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1816));
			}

			boolean isMapped = candidateProfessionalDetailDAO.existsByGradeAndIsActive(grade.getGradeDescription(),
					IHRMSConstants.isActive);
			if (isMapped) {
				throw new HRMSException(1858, ResponseCode.getResponseCodeMap().get(1883));
			}

			grade.setUpdatedBy(loggedInEmpId.toString());
			grade.setUpdatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1817));
		} else {

			grade = new GradeMaster();

			grade.setCreatedBy(loggedInEmpId.toString());
			grade.setCreatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1818));
		}

		grade.setCareerLevel(gradeVo.getCareerLevel());
		grade.setGradeDescription(gradeVo.getGradeDescription());
		grade.setIsActive(IHRMSConstants.isActive);
		grade.setOrgId(orgId);

		gradeDao.save(grade);

		response.setResponseCode(1200);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	@Override
	public HRMSBaseResponse<String> deleteGrade(GradeMasterVo request) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_GRADE, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		String resultMessage = performDeleteGrade(request, loggedInEmpId);

		response.setResponseCode(1200);
		response.setResponseMessage(resultMessage);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	private String performDeleteGrade(GradeMasterVo request, Long loggedInEmpId) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}

		GradeMaster grade = gradeDao.findById(request.getId()).orElse(null);
		if (grade == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1816));
		}

		if (IHRMSConstants.isActive.equals(grade.getIsActive())) {
			boolean isMapped = candidateProfessionalDetailDAO.existsByGradeAndIsActive(grade.getGradeDescription(),
					IHRMSConstants.isActive);

			if (isMapped) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1856));
			}

			grade.setIsActive(IHRMSConstants.isNotActive);
			grade.setUpdatedBy(loggedInEmpId.toString());
			grade.setUpdatedDate(new Date());
			gradeDao.save(grade);
			return ResponseCode.getResponseCodeMap().get(1819);
		} else {
			grade.setIsActive(IHRMSConstants.isActive);
			grade.setUpdatedBy(loggedInEmpId.toString());
			grade.setUpdatedDate(new Date());
			gradeDao.save(grade);

			return ResponseCode.getResponseCodeMap().get(1857);
		}
	}

	@Override
	public HRMSBaseResponse<String> deleteBranch(BranchVO request) throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_BRANCH, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		String resultMessage = performDeleteBranch(request, loggedInEmpId);

		response.setResponseCode(1200);
		response.setResponseMessage(resultMessage);
		response.setApplicationVersion(applicationVersion);
		return response;

	}

	private String performDeleteBranch(BranchVO request, Long loggedInEmpId) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch ID");
		}

		MasterBranch branch = branchDAO.findById(request.getId()).orElse(null);
		if (branch == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1820));
		}

		boolean isMapped = candidateProfessionalDetailDAO.existsByBranchIdAndIsActive(branch.getId(),
				IHRMSConstants.isActive);
		if (isMapped) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1858));
		}

		boolean isMappedToEmployee = employeeBranchDAO.existsByBranchIdAndIsActive(branch.getId(),
				IHRMSConstants.isActive);
		if (isMappedToEmployee) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1858));
		}

		if (IHRMSConstants.isActive.equals(branch.getIsActive())) {
			branch.setIsActive(IHRMSConstants.isNotActive);
			branch.setUpdatedBy(loggedInEmpId.toString());
			branch.setUpdatedDate(new Date());
			branchDAO.save(branch);
			return ResponseCode.getResponseCodeMap().get(1860);
		} else {
			branch.setIsActive(IHRMSConstants.isActive);
			branch.setUpdatedBy(loggedInEmpId.toString());
			branch.setUpdatedDate(new Date());
			branchDAO.save(branch);
			return ResponseCode.getResponseCodeMap().get(1859);
		}
	}

	@Override
	public HRMSBaseResponse<String> deleteDepartment(DepartmentVO request) throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_DEPARTMENT, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		String resultMessage = performDeleteDepartment(request, loggedInEmpId);

		response.setResponseCode(1200);
		response.setResponseMessage(resultMessage);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	private String performDeleteDepartment(DepartmentVO request, Long loggedInEmpId) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Department ID");
		}

		MasterDepartment department = departmentDAO.findById(request.getId()).orElse(null);
		if (department == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1825));
		}

		boolean isMapped = candidateProfessionalDetailDAO.existsByDepartmentIdAndIsActive(department.getId(),
				IHRMSConstants.isActive);
		if (isMapped) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1862));
		}

		boolean isMappedToEmployee = employeeDepartmentDAO.existsByDepartmentIdAndIsActive(department.getId(),
				IHRMSConstants.isActive);
		if (isMappedToEmployee) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1862));
		}

		if (IHRMSConstants.isActive.equals(department.getIsActive())) {
			department.setIsActive(IHRMSConstants.isNotActive);
			department.setUpdatedBy(loggedInEmpId.toString());
			department.setUpdatedDate(new Date());
			departmentDAO.save(department);
			return ResponseCode.getResponseCodeMap().get(1826);
		} else {
			department.setIsActive(IHRMSConstants.isActive);
			department.setUpdatedBy(loggedInEmpId.toString());
			department.setUpdatedDate(new Date());
			departmentDAO.save(department);
			return ResponseCode.getResponseCodeMap().get(1861);
		}
	}

	@Override
	public HRMSBaseResponse<String> addEmploymentType(EmploymentTypeVO employmentTypeVO) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		masterHelper.validateEmploymentType(employmentTypeVO);

		if (HRMSHelper.isNullOrEmpty(employmentTypeVO.getId()) && Boolean.TRUE
				.equals(employmentTypeDAO.existsByEmploymentTypeName(employmentTypeVO.getEmploymentTypeName()))) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_EMPLOYMENT_TYPE,
				loggedInEmployee.getCandidate().getEmployee(), loggedInEmployee.getCandidate().getEmployee());

		return createOrUpdateEmploymentType(employmentTypeVO, response, employeeId, orgId);
	}

	private HRMSBaseResponse<String> createOrUpdateEmploymentType(EmploymentTypeVO employmentTypeVO,
			HRMSBaseResponse<String> response, Long employeeId, Long orgId) throws HRMSException {

		MasterEmploymentType employmentType;

		if (!HRMSHelper.isNullOrEmpty(employmentTypeVO.getId())) {

			employmentType = employmentTypeDAO.findByIdAndIsActive(employmentTypeVO.getId(), IHRMSConstants.isActive);
			if (employmentType == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1827));
			}
			employmentType.setUpdatedBy(employeeId.toString());
			employmentType.setUpdatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1828));
		} else {

			employmentType = new MasterEmploymentType();
			employmentType.setCreatedBy(employeeId.toString());
			employmentType.setCreatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1829));
		}

		employmentType.setEmploymentTypeName(employmentTypeVO.getEmploymentTypeName());
		employmentType.setEmploymentTypeDescription(employmentTypeVO.getEmploymentTypeDescription());
		employmentType.setIsActive(IHRMSConstants.isActive);
		employmentType.setOrgId(orgId);

		employmentTypeDAO.save(employmentType);

		response.setResponseCode(1200);
		response.setApplicationVersion(applicationVersion);

		return response;
	}

	@Override
	public HRMSBaseResponse<String> deleteEmploymentType(EmploymentTypeVO request) throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_EMPLOYMENT_TYPE,
				loggedInEmployee.getCandidate().getEmployee(), loggedInEmployee.getCandidate().getEmployee());

		String resultMessage = performDeleteEmploymentType(request, loggedInEmpId);

		response.setResponseCode(1200);
		response.setResponseMessage(resultMessage);
		response.setApplicationVersion(applicationVersion);
		return response;

	}

	private String performDeleteEmploymentType(EmploymentTypeVO request, Long loggedInEmpId) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employment Type ID");
		}

		MasterEmploymentType employmentType = employmentTypeDAO.findById(request.getId()).orElse(null);
		if (employmentType == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1827));
		}

		boolean isMapped = candidateDAO.existsByEmploymentTypeIdAndIsActive(employmentType.getId(),
				IHRMSConstants.isActive);
		if (isMapped) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1864));
		}

		if (IHRMSConstants.isActive.equals(employmentType.getIsActive())) {
			employmentType.setIsActive(IHRMSConstants.isNotActive);
			employmentType.setUpdatedBy(loggedInEmpId.toString());
			employmentType.setUpdatedDate(new Date());
			employmentTypeDAO.save(employmentType);
			return ResponseCode.getResponseCodeMap().get(1830);
		} else {
			employmentType.setIsActive(IHRMSConstants.isActive);
			employmentType.setUpdatedBy(loggedInEmpId.toString());
			employmentType.setUpdatedDate(new Date());
			employmentTypeDAO.save(employmentType);
			return ResponseCode.getResponseCodeMap().get(1863);
		}
	}

	@Override
	public HRMSBaseResponse<String> deleteDivision(DivisionVO request) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_DIVISION, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		String resultMessage = performDeleteDivision(request, employeeId);

		response.setResponseCode(1200);
		response.setResponseMessage(resultMessage);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	private String performDeleteDivision(DivisionVO request, Long employeeId) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Division ID");
		}

		MasterDivision division = divisionDAO.findById(request.getId()).orElse(null);
		if (division == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1833));
		}

		boolean isMappedToCandidate = candidateProfessionalDetailDAO.existsByDivisionIdAndIsActive(division.getId(),
				IHRMSConstants.isActive);
		if (isMappedToCandidate) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1865));
		}

		boolean isMappedToEmployee = employeeDivisionDAO.existsByDivisionIdAndIsActive(division.getId(),
				IHRMSConstants.isActive);
		if (isMappedToEmployee) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1865));
		}

		if (IHRMSConstants.isActive.equals(division.getIsActive())) {
			division.setIsActive(IHRMSConstants.isNotActive);
			division.setUpdatedBy(employeeId.toString());
			division.setUpdatedDate(new Date());
			divisionDAO.save(division);
			return ResponseCode.getResponseCodeMap().get(1834);
		} else {
			division.setIsActive(IHRMSConstants.isActive);
			division.setUpdatedBy(employeeId.toString());
			division.setUpdatedDate(new Date());
			divisionDAO.save(division);
			return ResponseCode.getResponseCodeMap().get(1866);
		}
	}

	@Override
	public HRMSBaseResponse<String> deleteDesignation(DesignationVO request) throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_DESIGNATION,
				loggedInEmployee.getCandidate().getEmployee(), loggedInEmployee.getCandidate().getEmployee());

		String resultMessage = performDeleteDesignation(request, employeeId);

		response.setResponseCode(1200);
		response.setResponseMessage(resultMessage);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	private String performDeleteDesignation(DesignationVO request, Long employeeId) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Designation ID");
		}

		MasterDesignation designation = designationDAO.findById(request.getId()).orElse(null);
		if (designation == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1835));
		}

		boolean isMappedToCandidate = candidateProfessionalDetailDAO
				.existsByDesignationIdAndIsActive(designation.getId(), IHRMSConstants.isActive);
		if (isMappedToCandidate) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1868));
		}

		boolean isMappedToEmployee = employeeDesignationDAO.existsByDesignationIdAndIsActive(designation.getId(),
				IHRMSConstants.isActive);
		if (isMappedToEmployee) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1868));
		}

		if (IHRMSConstants.isActive.equals(designation.getIsActive())) {
			designation.setIsActive(IHRMSConstants.isNotActive);
			designation.setUpdatedBy(employeeId.toString());
			designation.setUpdatedDate(new Date());
			designationDAO.save(designation);
			return ResponseCode.getResponseCodeMap().get(1838);
		} else {
			designation.setIsActive(IHRMSConstants.isActive);
			designation.setUpdatedBy(employeeId.toString());
			designation.setUpdatedDate(new Date());
			designationDAO.save(designation);
			return ResponseCode.getResponseCodeMap().get(1867);
		}
	}

	@Override
	public HRMSBaseResponse<String> addMasterTitle(MasterTitleVo masterTitleVO) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		masterHelper.validateMasterTitle(masterTitleVO);

		if (HRMSHelper.isNullOrEmpty(masterTitleVO.getId())
				&& Boolean.TRUE.equals(masterTitleDao.existsByTitle(masterTitleVO.getTitle()))) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1546));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_MASTER_TITLE, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		return createOrUpdateMasterTitle(masterTitleVO, response, employeeId, orgId);
	}

	private HRMSBaseResponse<String> createOrUpdateMasterTitle(MasterTitleVo masterTitleVO,
			HRMSBaseResponse<String> response, Long employeeId, Long orgId) throws HRMSException {

		MasterTitle title;

		if (!HRMSHelper.isNullOrEmpty(masterTitleVO.getId())) {
			title = masterTitleDao.findByIdAndIsActive(masterTitleVO.getId(), IHRMSConstants.isActive);
			if (title == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1846));
			}

			title.setUpdatedBy(employeeId.toString());
			title.setUpdatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1845));

		} else {
			title = new MasterTitle();
			title.setCreatedBy(employeeId.toString());
			title.setCreatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1844));
		}

		title.setTitle(masterTitleVO.getTitle());
		title.setTitleDescription(masterTitleVO.getTitleDescription());
		title.setIsActive(IHRMSConstants.isActive);
		title.setOrgId(orgId);
		masterTitleDao.save(title);

		response.setResponseCode(1200);
		response.setApplicationVersion(applicationVersion);

		return response;
	}

	@Override
	public HRMSBaseResponse<String> deleteMasterTitle(MasterTitleVo request) throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_SALUTATION, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		String resultMessage = performDeleteMasterTitle(request, employeeId);

		response.setResponseCode(1200);
		response.setResponseMessage(resultMessage);
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	private String performDeleteMasterTitle(MasterTitleVo request, Long employeeId) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Salutation Id");
		}

		MasterTitle title = masterTitleDao.findById(request.getId()).orElse(null);
		if (title == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1846));
		}

		boolean isMapped = candidateDAO.existsByTitleAndIsActive(title.getTitle(), IHRMSConstants.isActive);
		if (isMapped) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1869));
		}

		if (IHRMSConstants.isActive.equals(title.getIsActive())) {
			title.setIsActive(IHRMSConstants.isNotActive);
			title.setUpdatedBy(employeeId.toString());
			title.setUpdatedDate(new Date());
			masterTitleDao.save(title);
			return ResponseCode.getResponseCodeMap().get(1843);
		} else {
			title.setIsActive(IHRMSConstants.isActive);
			title.setUpdatedBy(employeeId.toString());
			title.setUpdatedDate(new Date());
			masterTitleDao.save(title);
			return ResponseCode.getResponseCodeMap().get(1870);
		}
	}

	@Override
	public HRMSBaseResponse<CountryListResponseVO> getAllCountry(String searchText, Pageable pageable)
			throws HRMSException {
		log.info("Inside getAllCountry Method");

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (searchText == null) {
			searchText = "";
		}

		List<MasterCountry> countryMasterList = countryDAO.searchCountryByOrgIdAndText(orgId, searchText.trim(),
				pageable);
		long totalRecords = countryDAO.countCountryByOrgIdAndText(orgId, searchText.trim());

		if (HRMSHelper.isNullOrEmpty(countryMasterList)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<MasterCountryVO> countryList = new ArrayList<>();
		for (MasterCountry countryMaster : countryMasterList) {
			MasterCountryVO countryVO = new MasterCountryVO();
			countryVO.setId(countryMaster.getId());
			countryVO.setCountryName(countryMaster.getCountryName());
			countryVO.setCountryDescription(countryMaster.getCountryDescription());
			countryVO.setIsActive(countryMaster.getIsActive());
			countryList.add(countryVO);
		}

		CountryListResponseVO responseVO = new CountryListResponseVO();
		responseVO.setCountryList(countryList);

		HRMSBaseResponse<CountryListResponseVO> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(responseVO);
		response.setTotalRecord(totalRecords);

		log.info("Exit from getAllCountry Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<StateListResponseVO> getAllStateList(long countryId, String searchText, Pageable pageable)
			throws HRMSException {
		log.info("Inside getAllStateList Method ");

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (HRMSHelper.isNullOrEmpty(countryId)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1800));
		}

		List<MasterState> stateList;
		long totalRecords;

		stateList = stateDAO.searchStateByCountryIdAndText(countryId, orgId,
				HRMSHelper.isNullOrEmpty(searchText) ? "" : searchText.trim(), pageable);

		totalRecords = stateDAO.countSearchStateByCountryIdAndText(countryId, orgId,
				HRMSHelper.isNullOrEmpty(searchText) ? "" : searchText.trim());

		if (HRMSHelper.isNullOrEmpty(stateList)) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<MasterStateVO> stateVOList = new ArrayList<>();
		for (MasterState stateEntity : stateList) {
			MasterStateVO state = new MasterStateVO();
			state.setId(stateEntity.getId());
			state.setStateName(stateEntity.getStateName());
			state.setStateDescription(stateEntity.getStateDescription());
			state.setIsActive(stateEntity.getIsActive());
			stateVOList.add(state);
		}

		StateListResponseVO responseVo = new StateListResponseVO();
		responseVo.setStateList(stateVOList);

		HRMSBaseResponse<StateListResponseVO> baseResponse = new HRMSBaseResponse<>();
		baseResponse.setResponseCode(1200);
		baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseResponse.setResponseBody(responseVo);
		baseResponse.setTotalRecord(totalRecords);

		return baseResponse;
	}

	@Override
	public HRMSBaseResponse<CityListResponseVO> getAllCityList(long stateId, String searchText, Pageable pageable)
			throws HRMSException {
		log.info("Inside getAllCityList Method");

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (HRMSHelper.isNullOrEmpty(stateId)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1800));
		}

		List<MasterCity> cityList = null;
		long totalRecords = 0;
		cityList = cityDao.searchCityByStateIdAndText(stateId, orgId, searchText.trim(), pageable);
		totalRecords = cityDao.countByStateIdAndText(stateId, orgId, searchText.trim());

		if (HRMSHelper.isNullOrEmpty(cityList)) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<MasterCityVO> cityVOList = new ArrayList<>();
		for (MasterCity cityEntity : cityList) {
			MasterCityVO city = new MasterCityVO();
			city.setId(cityEntity.getId());
			city.setCityName(cityEntity.getCityName());
			city.setCityDescription(cityEntity.getCityDescription());
			city.setIsActive(cityEntity.getIsActive());
			cityVOList.add(city);
		}

		CityListResponseVO responseVo = new CityListResponseVO();
		responseVo.setCityList(cityVOList);

		HRMSBaseResponse<CityListResponseVO> baseResponse = new HRMSBaseResponse<>();
		baseResponse.setResponseCode(1200);
		baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseResponse.setResponseBody(responseVo);
		baseResponse.setTotalRecord(totalRecords);

		return baseResponse;
	}

	@Override
	public HRMSBaseResponse<KpiMetricListResponseVO> getAllKpiMetric(String searchText, Pageable pageable)
			throws HRMSException {

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (searchText == null) {
			searchText = "";
		}

		List<KpiMetricMaster> kpiMetricMasters = kpiMetricDao.searchMetricByOrgIdAndText(orgId, searchText.trim(),
				pageable);
		long totalRecords = kpiMetricDao.countMetricByOrgIdAndText(orgId, searchText.trim());

		if (HRMSHelper.isNullOrEmpty(kpiMetricMasters)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<KpiMetricVo> metricVoList = new ArrayList<>();
		for (KpiMetricMaster master : kpiMetricMasters) {
			KpiMetricVo vo = new KpiMetricVo();
			vo.setId(master.getId());
			vo.setMetric(master.getMetric());
			vo.setDescription(master.getDescription());
			vo.setIsActive(master.getIsActive());
			metricVoList.add(vo);
		}

		// Sort: 'Y' first, then others like 'N' or null
		metricVoList
				.sort(Comparator.comparing(vo -> IHRMSConstants.isActive.equalsIgnoreCase(vo.getIsActive()) ? 0 : 1));

		KpiMetricListResponseVO responseVO = new KpiMetricListResponseVO();
		responseVO.setMetricList(metricVoList);

		HRMSBaseResponse<KpiMetricListResponseVO> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(responseVO);
		response.setTotalRecord(totalRecords);
		return response;
	}

	@Override
	public HRMSBaseResponse<GradeListResponseVO> getAllGrades(String searchText, Pageable pageable)
			throws HRMSException {

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (searchText == null) {
			searchText = "";
		}

		List<GradeMaster> gradeMasters = gradeDao.searchGradeByOrgIdAndText(orgId, searchText.trim(), pageable);

		long totalRecords = gradeDao.countGradeByOrgIdAndText(orgId, searchText.trim());

		if (HRMSHelper.isNullOrEmpty(gradeMasters)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<GradeMasterVo> gradeVoList = new ArrayList<>();
		for (GradeMaster grade : gradeMasters) {
			GradeMasterVo vo = new GradeMasterVo();
			vo.setId(grade.getId());
			vo.setGradeDescription(grade.getGradeDescription());
			vo.setCareerLevel(grade.getCareerLevel());
			vo.setIsActive(grade.getIsActive());
			gradeVoList.add(vo);
		}

		GradeListResponseVO responseVO = new GradeListResponseVO();
		responseVO.setGradeList(gradeVoList);

		HRMSBaseResponse<GradeListResponseVO> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(responseVO);
		response.setTotalRecord(totalRecords);

		return response;
	}

	@Override
	public HRMSBaseResponse<BranchListResponseVO> getAllBranches(String searchText, Pageable pageable)
			throws HRMSException {

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (searchText == null) {
			searchText = "";
		}

		List<MasterBranch> branches = branchDAO.searchBranchByOrgIdAndText(orgId, searchText.trim(), pageable);
		long totalRecords = branchDAO.countBranchByOrgIdAndText(orgId, searchText.trim());

		if (HRMSHelper.isNullOrEmpty(branches)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<BranchVO> branchVoList = new ArrayList<>();
		for (MasterBranch branch : branches) {
			BranchVO vo = new BranchVO();
			vo.setId(branch.getId());
			vo.setBranchName(branch.getBranchName());
			vo.setBranchDescription(branch.getBranchDescription());
			vo.setOrganizationId(branch.getOrgId());
			vo.setIsActive(branch.getIsActive());
			branchVoList.add(vo);
		}

		BranchListResponseVO responseVO = new BranchListResponseVO();
		responseVO.setBranchList(branchVoList);

		HRMSBaseResponse<BranchListResponseVO> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(responseVO);
		response.setTotalRecord(totalRecords);

		return response;
	}

	@Override
	public HRMSBaseResponse<DepartmentListResponseVO> getAllDepartments(String searchText, Pageable pageable)
			throws HRMSException {

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (searchText == null) {
			searchText = "";
		}
		List<MasterDepartment> departments = departmentDAO.searchDepartmentByOrgIdAndText(orgId, searchText.trim(),
				pageable);
		long totalRecords = departmentDAO.countDepartmentByOrgIdAndText(orgId, searchText.trim());

		if (HRMSHelper.isNullOrEmpty(departments)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<DepartmentVO> departmentVoList = new ArrayList<>();
		for (MasterDepartment department : departments) {
			DepartmentVO vo = new DepartmentVO();
			vo.setId(department.getId());
			vo.setDepartmentName(department.getDepartmentName());
			vo.setDepartmentDescription(department.getDepartmentDescription());
			vo.setOrganizationId(department.getOrgId());
			vo.setIsActive(department.getIsActive());
			departmentVoList.add(vo);
		}

		DepartmentListResponseVO responseVO = new DepartmentListResponseVO();
		responseVO.setDepartmentList(departmentVoList);

		HRMSBaseResponse<DepartmentListResponseVO> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(responseVO);
		response.setTotalRecord(totalRecords);

		return response;
	}

	@Override
	public HRMSBaseResponse<EmploymentTypeListResponseVO> getAllEmploymentTypes(String searchText, Pageable pageable)
			throws HRMSException {

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (searchText == null) {
			searchText = "";
		}

		List<MasterEmploymentType> employmentTypes = employmentTypeDAO.searchEmploymentTypeByOrgIdAndText(orgId,
				searchText.trim(), pageable);
		long totalRecords = employmentTypeDAO.countEmploymentTypeByOrgIdAndText(orgId, searchText.trim());

		if (HRMSHelper.isNullOrEmpty(employmentTypes)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<EmploymentTypeVO> typeVoList = new ArrayList<>();
		for (MasterEmploymentType type : employmentTypes) {
			EmploymentTypeVO vo = new EmploymentTypeVO();
			vo.setId(type.getId());
			vo.setEmploymentTypeName(type.getEmploymentTypeName());
			vo.setEmploymentTypeDescription(type.getEmploymentTypeDescription());
			vo.setIsActive(type.getIsActive());
			typeVoList.add(vo);
		}

		EmploymentTypeListResponseVO responseVO = new EmploymentTypeListResponseVO();
		responseVO.setEmploymentTypeList(typeVoList);

		HRMSBaseResponse<EmploymentTypeListResponseVO> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(responseVO);
		response.setTotalRecord(totalRecords);

		return response;
	}

	@Override
	public HRMSBaseResponse<DivisionListResponseVO> getAllDivisions(String searchText, Pageable pageable)
			throws HRMSException {

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (searchText == null) {
			searchText = "";
		}

		List<MasterDivision> divisions = divisionDAO.searchDivisionByOrgIdAndText(orgId, searchText.trim(), pageable);
		long totalRecords = divisionDAO.countDivisionByOrgIdAndText(orgId, searchText.trim());

		if (HRMSHelper.isNullOrEmpty(divisions)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<DivisionVO> divisionVoList = new ArrayList<>();
		for (MasterDivision division : divisions) {
			DivisionVO vo = new DivisionVO();
			vo.setId(division.getId());
			vo.setDivisionName(division.getDivisionName());
			vo.setDivisionDescription(division.getDivisionDescription());
			vo.setOrganizationId(division.getOrgId());
			vo.setIsActive(division.getIsActive());
			divisionVoList.add(vo);
		}

		DivisionListResponseVO responseVO = new DivisionListResponseVO();
		responseVO.setDivisionList(divisionVoList);

		HRMSBaseResponse<DivisionListResponseVO> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(responseVO);
		response.setTotalRecord(totalRecords);

		return response;
	}

	@Override
	public HRMSBaseResponse<DesignationListResponseVO> getAllDesignations(String searchText, Pageable pageable)
			throws HRMSException {

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (searchText == null) {
			searchText = "";
		}
		List<MasterDesignation> designations = designationDAO.searchDesignationByOrgIdAndText(orgId, searchText.trim(),
				pageable);
		long totalRecords = designationDAO.countDesignationByOrgIdAndText(orgId, searchText.trim());

		if (HRMSHelper.isNullOrEmpty(designations)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<DesignationVO> designationVoList = new ArrayList<>();
		for (MasterDesignation designation : designations) {
			DesignationVO vo = new DesignationVO();
			vo.setId(designation.getId());
			vo.setDesignationName(designation.getDesignationName());
			vo.setDesignationDescription(designation.getDesignationDescription());
			vo.setIsActive(designation.getIsActive());
			vo.setOrganizationId(designation.getOrgId());
			designationVoList.add(vo);
		}

		DesignationListResponseVO responseVO = new DesignationListResponseVO();
		responseVO.setDesignationList(designationVoList);

		HRMSBaseResponse<DesignationListResponseVO> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(responseVO);
		response.setTotalRecord(totalRecords);

		return response;
	}

	@Override
	public HRMSBaseResponse<MasterTitleListResponseVO> getAllMasterTitles(String searchText, Pageable pageable)
			throws HRMSException {

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (searchText == null) {
			searchText = "";
		}

		List<MasterTitle> titleList = masterTitleDao.searchMasterTitleByOrgIdAndText(orgId, searchText.trim(),
				pageable);
		long totalRecords = masterTitleDao.countMasterTitleByOrgIdAndText(orgId, searchText.trim());

		if (HRMSHelper.isNullOrEmpty(titleList)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<MasterTitleVo> titleVoList = new ArrayList<>();
		for (MasterTitle title : titleList) {
			MasterTitleVo vo = new MasterTitleVo();
			vo.setId(title.getId());
			vo.setTitle(title.getTitle());
			vo.setTitleDescription(title.getTitleDescription());
			vo.setIsActive(title.getIsActive());
			titleVoList.add(vo);
		}

		MasterTitleListResponseVO responseVO = new MasterTitleListResponseVO();
		responseVO.setTitleList(titleVoList);

		HRMSBaseResponse<MasterTitleListResponseVO> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(responseVO);
		response.setTotalRecord(totalRecords);

		return response;
	}

	@Override
	public HRMSBaseResponse<List<ObjectivesVO>> getObjectives(Long subCategoryId, Pageable pageable)
			throws HRMSException {
		log.info("Enter into getObjectivesBySubCategoryId Method");
		validateRolePermission();
		HRMSBaseResponse<List<ObjectivesVO>> response = new HRMSBaseResponse<>();
		List<ObjectivesVO> objectiveVoList = new ArrayList<>();
		List<Objectives> objectiveList = objectivesDao.findBySubCategoryId(subCategoryId, IHRMSConstants.isActive,
				pageable);

		if (!HRMSHelper.isNullOrEmpty(objectiveList)) {
			for (Objectives obj : objectiveList) {
				ObjectivesVO vo = new ObjectivesVO();
				vo.setId(obj.getId());
				vo.setDescription(obj.getDescription());
				vo.setCategory(obj.getCategory().getCategoryName());
				vo.setCategoryId(obj.getCategory().getId());
				vo.setSubCategory(obj.getSubCategory().getSubCategoryName());
				vo.setSubCategoryId(obj.getSubCategory().getId());
				vo.setObjWeightage(obj.getObjWeightage());
				vo.setMetrics(obj.getMetrics());
				vo.setTitle(obj.getTitle());
				objectiveVoList.add(vo);
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(objectiveVoList);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setTotalRecord(objectiveVoList.size());
		log.info("Exit from getObjectivesBySubCategoryId Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<String> addObjective(ObjectivesVO objectivesVO) throws HRMSException {

		log.info("Inside addObjective method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		masterHelper.validateObjectiveVO(objectivesVO);

		if (HRMSHelper.isNullOrEmpty(objectivesVO.getId())) {

			if (Boolean.TRUE.equals(objectivesDao.existsByDescription(objectivesVO.getDescription()))) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1546));
			}

			if (objectivesDao.existsByCategoryAndSubCategory(objectivesVO.getCategoryId(),
					objectivesVO.getSubCategoryId(), IHRMSConstants.isActive)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1897));
			}
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());

		auditLogService.setActionHeader(IHRMSConstants.ADD_OBJECTIVE, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		Category category = categoryDAO.findByIdAndIsActive(objectivesVO.getCategoryId(), IHRMSConstants.isActive);
		Subcategory subCategory = subCategoryDao.findByIdAndIsActive(objectivesVO.getSubCategoryId(),
				IHRMSConstants.isActive);

		if (category == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1638));
		}

		if (subCategory == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1641));
		}

		return createOrUpdateObjective(objectivesVO, response, loggedInEmpId, category, subCategory, orgId);
	}

	private HRMSBaseResponse<String> createOrUpdateObjective(ObjectivesVO vo, HRMSBaseResponse<String> response,
			Long loggedInEmpId, Category category, Subcategory subCategory, Long orgId) throws HRMSException {

		Objectives objective;

		if (!HRMSHelper.isNullOrEmpty(vo.getId())) {
			objective = objectivesDao.findByIdAndIsActive(vo.getId(), IHRMSConstants.isActive);
			if (objective == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1889));
			}

			objective.setUpdatedBy(loggedInEmpId.toString());
			objective.setUpdatedDate(new Date());

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1890));
		} else {
			objective = new Objectives();
			objective.setCreatedBy(loggedInEmpId.toString());
			objective.setCreatedDate(new Date());

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1891));
		}

		objective.setDescription(vo.getDescription());
		objective.setCategory(category);
		objective.setSubCategory(subCategory);
		objective.setObjWeightage(vo.getObjWeightage());
		objective.setMetrics(vo.getMetrics());
		objective.setTitle(vo.getTitle());
		objective.setIsActive(IHRMSConstants.isActive);
		objective.setOrgId(orgId);

		objectivesDao.save(objective);

		response.setApplicationVersion(applicationVersion);
		log.info("Exit from addObjective method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<MasterScreenTypeResponseVo>> getScreenType(Long roleId) throws HRMSException {
		log.info("Inside  getScreenType Method");

		HRMSBaseResponse<List<MasterScreenTypeResponseVo>> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		List<MasterScreenType> masterScreenTypes = masterscreentypeDao.findByRoleAndIsActive(roleId,
				IHRMSConstants.isActive);
		if (masterScreenTypes == null) {
			throw new HRMSException(1901, ResponseCode.getResponseCodeMap().get(1901));
		}
		List<MasterScreenTypeResponseVo> masterScreenTypeResponseList = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(masterScreenTypes)) {

			for (MasterScreenType screenType : masterScreenTypes) {
				MasterScreenTypeResponseVo screenTypeVo = new MasterScreenTypeResponseVo();
				screenTypeVo.setId(screenType.getId());
				screenTypeVo.setLabel(screenType.getScreenName());
				masterScreenTypeResponseList.add(screenTypeVo);
			}
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseBody(masterScreenTypeResponseList);
			response.setApplicationVersion(applicationVersion);
		} else {
			throw new HRMSException(1901, ResponseCode.getResponseCodeMap().get(1901));
		}

		return response;

	}

	public void validateRolePermission() throws HRMSException {
		validateRolePermissionforUnlocked();
	}

	private void validateRolePermissionforUnlocked() throws HRMSException {
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name()) && !HRMSHelper.isRolePresent(role, ERole.ADMIN.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}
	
	

}
