package com.vinsys.hrms.master.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * @author Onkar A.
 * 
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.vo.KpiMetricVo;
import com.vinsys.hrms.kra.vo.KraCycleRequestVo;
import com.vinsys.hrms.kra.vo.KraYearResponseVo;
import com.vinsys.hrms.kra.vo.KraYearVo;
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
import com.vinsys.hrms.master.vo.KraCycleStatusVo;
import com.vinsys.hrms.master.vo.KraCycleVo;
import com.vinsys.hrms.master.vo.LoginEntityTypeVO;
import com.vinsys.hrms.master.vo.MasterAirTypeVO;
import com.vinsys.hrms.master.vo.MasterBusTypeVO;
import com.vinsys.hrms.master.vo.MasterCityVO;
import com.vinsys.hrms.master.vo.MasterCountryVO;
import com.vinsys.hrms.master.vo.MasterDriverVO;
import com.vinsys.hrms.master.vo.MasterLeavePolicyVO;
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
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("master")
public class MasterController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	IMasterService masterService;

	@Value("${app_version}")
	private String applicationVersion;

	@GetMapping("/leavetypes")
	public HRMSBaseResponse getLeaveTypes(HttpServletRequest request) {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			response = masterService.getLeaveTypes(request);

		} catch (HRMSException e) {

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setResponseBody(e.getMessage());
		} catch (Exception e) {

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setResponseBody(e.getMessage());
		}
		return response;
	}

	@GetMapping("/leavepolicy")
	public HRMSBaseResponse getLeavePolicy(@RequestParam(required = true) Long leaveTypeId) {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			MasterLeavePolicyVO leavePolicyVO = masterService.getLeavePolicy(leaveTypeId);
			response.setResponseBody(leavePolicyVO);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());

		} catch (Exception e) {

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));

		}
		return response;
	}

	@GetMapping("/grantleavetypes")
	public HRMSBaseResponse getGrantLeaveTypes(HttpServletRequest request) {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			response = masterService.getGrantLeaveTypes(request);

		} catch (HRMSException e) {

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setResponseBody(e.getMessage());
		} catch (Exception e) {

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setResponseBody(e.getMessage());
		}
		return response;
	}

	@GetMapping("/designations")
	public HRMSBaseResponse getDesignations() {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			List<DesignationVO> designatioVo = masterService.getDesignationList();
			response.setResponseBody(designatioVo);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());

		} catch (Exception e) {

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));

		}
		return response;
	}

	@GetMapping("/departments")
	public HRMSBaseResponse getDepartment() {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			List<DepartmentVO> departmentVo = masterService.getDepartmentList();
			response.setResponseBody(departmentVo);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());

		} catch (Exception e) {

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));

		}
		return response;
	}

	@GetMapping("/divisions")
	public HRMSBaseResponse getDivision() {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			List<DivisionVO> divisionVo = masterService.getDivisionList();
			response.setResponseBody(divisionVo);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());

		} catch (Exception e) {

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));

		}
		return response;

	}

	@GetMapping("/branch")
	public HRMSBaseResponse getBranch() {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			List<BranchVO> branchVo = masterService.getBranchList();
			response.setResponseBody(branchVo);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());

		} catch (Exception e) {

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));

		}
		return response;
	}

	@GetMapping("/states")
	public HRMSBaseResponse getState(@RequestParam(required = true) long countryId) {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			List<MasterStateVO> stateVO = masterService.getStateList(countryId);
			response.setResponseBody(stateVO);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());

		} catch (Exception e) {

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));

		}
		return response;
	}

	@GetMapping("/separationreasons")
	public HRMSBaseResponse getSeprationReason(@RequestParam(required = true) long modeOfSeparationId) {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			List<SeparationReasonVO> reasonVO = masterService.getSeparationReasons(modeOfSeparationId);
			response.setResponseBody(reasonVO);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/getmodeofeducation")
	HRMSBaseResponse<List<ModeOfEducationVO>> modeOfEducation() {
		HRMSBaseResponse<List<ModeOfEducationVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getModeOfEducation();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/getdegreemaster")
	HRMSBaseResponse<List<DegreeMasterVO>> getdegreeMaster() {
		HRMSBaseResponse<List<DegreeMasterVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getDegree();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/yearmaster")
	HRMSBaseResponse<List<YearMasterVO>> getYearMaster() {
		HRMSBaseResponse<List<YearMasterVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getYearList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("probation/feedbackparameter")
	HRMSBaseResponse<List<ProbationFeedbackParameterVO>> getProbationFeedbackParameter() {
		HRMSBaseResponse<List<ProbationFeedbackParameterVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getProbationFeedbackParameter();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/relationship")
	HRMSBaseResponse<List<RelationshipMasterVO>> getRelations() {
		HRMSBaseResponse<List<RelationshipMasterVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getRelationshipList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/gender")
	HRMSBaseResponse<List<GenderMasterVO>> getGenderMaster() {
		HRMSBaseResponse<List<GenderMasterVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getGenderList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/selectoptions")
	HRMSBaseResponse<List<SelectionMasterVO>> getOptionList() {
		HRMSBaseResponse<List<SelectionMasterVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getOptionList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "you can add new designation details using this API", description = " Add designation API")
	@PostMapping(value = "adddesignation")
	HRMSBaseResponse<?> addDesignationDetails(@RequestBody DesignationVO designationDetails) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addDesignation(designationDetails);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "you can add new department details using this API", description = " Add department API")
	@PostMapping(value = "adddepartment")
	HRMSBaseResponse<?> addDepartmentDetails(@RequestBody DepartmentVO departmentDetails) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = masterService.addDepartment(departmentDetails);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@PostMapping(value = "adddegree")
	HRMSBaseResponse<?> addDegreeDetail(@RequestBody DegreeMasterVO degreeVO) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = masterService.addDegree(degreeVO);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@PostMapping(value = "addrelation")
	HRMSBaseResponse<?> addRelation(@RequestBody RelationshipMasterVO relationshipVo) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = masterService.addRelationship(relationshipVo);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@PostMapping(value = "addgender")
	HRMSBaseResponse<?> addGender(@RequestBody GenderMasterVO genderVO) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = masterService.addGender(genderVO);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "you can add new mode of education details using this API", description = " Add Mode of Education API")
	@PostMapping(value = "addmodeofeducation")
	HRMSBaseResponse<?> addModeOFEducationDetails(@RequestBody ModeOfEducationVO modeOfEducationDetails) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addModeOfEducation(modeOfEducationDetails);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "you can add new state details using this API", description = " Add state API")
	@PostMapping(value = "addstate")
	HRMSBaseResponse<?> addStateDetails(@RequestBody StateVO state) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addState(state);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "you can add new Resignation Reason details using this API", description = " Add Resignation Reason API")
	@PostMapping(value = "addresignationreason")
	HRMSBaseResponse<?> addResignationReason(@RequestBody ModeofSeparationReasonVO resignationReason) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addResignationReason(resignationReason);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "you can add new Division details using this API", description = " Add Division API")
	@PostMapping(value = "adddivision")
	HRMSBaseResponse<?> addDivision(@RequestBody DivisionVO division) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addDivision(division);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "you can add new branch details using this API", description = " Add branch API")
	@PostMapping(value = "addbranch")
	HRMSBaseResponse<?> addBranch(@RequestBody BranchVO branch) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addBranch(branch);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "This API will use for get release type ", description = "")
	@GetMapping("releasetypes")
	HRMSBaseResponse<List<ReleaseTypeVO>> getReleaseTypes() {
		HRMSBaseResponse<List<ReleaseTypeVO>> response = new HRMSBaseResponse();
		try {
			response = masterService.getReleaseTypes();
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/getallreportingmanager")
	HRMSBaseResponse<List<ReportingOfficerVO>> getAllReportingOfficer() {
		HRMSBaseResponse<List<ReportingOfficerVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getAllReportingManagerList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API will use for get release type ", description = "")
	@GetMapping("/getorganization")
	HRMSBaseResponse<List<OrganizationVO>> getOrganization() {
		HRMSBaseResponse<List<OrganizationVO>> response = new HRMSBaseResponse();
		try {
			response = masterService.getOrganization();
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API is to get mode of separation list ", description = "")
	@GetMapping("/getmodeofseparationlist")
	HRMSBaseResponse<List<ModeOfSeparationMasterVO>> getAllModeOfSeparation() {
		HRMSBaseResponse<List<ModeOfSeparationMasterVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getModeOfSeprationList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API is to get country list ", description = "")
	@GetMapping("/country")
	HRMSBaseResponse<List<MasterCountryVO>> getAllCountries() {
		HRMSBaseResponse<List<MasterCountryVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getAllCountries();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/branding")
	public HRMSBaseResponse<List<BrandingVO>> getBranding(@RequestParam Long orgId) {
		HRMSBaseResponse<List<BrandingVO>> response = new HRMSBaseResponse<List<BrandingVO>>();
		try {
			List<BrandingVO> brandingVo = masterService.getBranding(orgId);
			response.setResponseBody(brandingVo);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());

		} catch (Exception e) {

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));

		}
		return response;
	}

	@Operation(summary = "This API is to get travel type list ", description = "")
	@GetMapping("traveltype")
	HRMSBaseResponse<List<MasterTravelTypeVO>> getAllTravelType() {
		HRMSBaseResponse<List<MasterTravelTypeVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getTravelRequestType();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API is to get mode of travel  ", description = "")
	@GetMapping("traveltypemode")
	HRMSBaseResponse<List<MasterModeOfTravelVO>> getAllModeOfTravel() {
		HRMSBaseResponse<List<MasterModeOfTravelVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getTravelMode();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API use to get travel trip type ", description = "Used trip type dropdown in cab booking tab")
	@GetMapping("traveltriptype")
	HRMSBaseResponse<List<TripTypeVO>> getTravelTripType() {
		HRMSBaseResponse<List<TripTypeVO>> response = new HRMSBaseResponse<List<TripTypeVO>>();
		try {
			response = masterService.getTravelTripType();
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API is to get bus type  ", description = "This Api is used for Train and Cab also")
	@GetMapping("bustype")
	HRMSBaseResponse<List<MasterBusTypeVO>> getBusType() {
		HRMSBaseResponse<List<MasterBusTypeVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getBusType();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API is to get traveller type  ", description = "")
	@GetMapping("travellertype")
	HRMSBaseResponse<List<MasterTravellerTypeVO>> getTravellerType() {
		HRMSBaseResponse<List<MasterTravellerTypeVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getTravellerType();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API is to get air type  ", description = "")
	@GetMapping("airtype")
	HRMSBaseResponse<List<MasterAirTypeVO>> getAirType() {
		HRMSBaseResponse<List<MasterAirTypeVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getAirType();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API is to get visa type  ", description = "")
	@GetMapping("visatype")
	HRMSBaseResponse<List<MasterVisaTypeVO>> getVisaType() {
		HRMSBaseResponse<List<MasterVisaTypeVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getVisaType();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API is to get driver list", description = "")
	@GetMapping("driver")
	HRMSBaseResponse<List<MasterDriverVO>> getDriver() {
		HRMSBaseResponse<List<MasterDriverVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getDriverList();
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API is to get vehicle list", description = "")
	@GetMapping("vehicle")
	HRMSBaseResponse<List<MasterVehicleVO>> getVehicles() {
		HRMSBaseResponse<List<MasterVehicleVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getVehicleList();
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "This API is to get approver detail", description = "")
	@PostMapping("approverdetail")
	HRMSBaseResponse getApproverDetails(@RequestBody TravelDeskApproverRequestVO request) {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			TravelApproverResponseVO approverResponse = masterService.getApproverDetail(request);
			response.setResponseBody(approverResponse);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/currency")
	HRMSBaseResponse<List<CurrencyMasterVO>> getCurrencyMasterList() {
		HRMSBaseResponse<List<CurrencyMasterVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getCurrencyList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/requester")
	HRMSBaseResponse<List<PrimaryRequesterVO>> getPrimaryRequesterList() {
		HRMSBaseResponse<List<PrimaryRequesterVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getRequesterTypeList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/staytype")
	HRMSBaseResponse<List<StayTypeVO>> getStayTypeList() {
		HRMSBaseResponse<List<StayTypeVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getStayList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/reimbursementtraveltype")
	HRMSBaseResponse<List<ReimbursementTravelTypeVO>> getTravelTypeList() {
		HRMSBaseResponse<List<ReimbursementTravelTypeVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getReimbursmentTravelTypeList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/claimtype")
	HRMSBaseResponse<List<ClaimTypeVO>> getClaimTypeList() {
		HRMSBaseResponse<List<ClaimTypeVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getClaimTypeList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/categorytype")
	HRMSBaseResponse<List<ClaimCategoryVO>> getCategoryList() {
		HRMSBaseResponse<List<ClaimCategoryVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getClaimCategoryList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/mastertitles")
	HRMSBaseResponse<List<MasterTitleVo>> getMasterTitleList() {
		HRMSBaseResponse<List<MasterTitleVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getMasterTitle();
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/maritalstatus")
	HRMSBaseResponse<List<MasterMaritialStatusVo>> getMasterMaritalStatusList() {
		HRMSBaseResponse<List<MasterMaritialStatusVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getMaritalStatus();
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/cities")
	public HRMSBaseResponse getCity(@RequestParam(required = true) long stateId) {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			List<MasterCityVO> cityVO = masterService.getCityList(stateId);
			response.setResponseBody(cityVO);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());

		} catch (Exception e) {

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));

		}
		return response;
	}

	@GetMapping("/employementtype")
	HRMSBaseResponse<List<EmploymentTypeVO>> getEmploymentTypeList() {
		HRMSBaseResponse<List<EmploymentTypeVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getEmploymentTypeList();
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/loginentitytype")
	HRMSBaseResponse<List<LoginEntityTypeVO>> getLoginEntityType() {
		HRMSBaseResponse<List<LoginEntityTypeVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getLoginEntityTypes();
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	/**
	 * @author madhuri.wakchaure
	 * @return
	 */

	@GetMapping("/selfrating")
	HRMSBaseResponse<List<SelfRatingVo>> getSelfRating() {
		HRMSBaseResponse<List<SelfRatingVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getSelfRating();
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "list of KRA Year", description = "Fetches paginated and sorted (by ID descending) list of KRA Year details.")
	@GetMapping("/yearmasternew")
	HRMSBaseResponse<List<KraYearResponseVo>> getNewYearMaster(Pageable pageable) {
		HRMSBaseResponse<List<KraYearResponseVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getNewYearList(pageable);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/kpistatuslist")
	HRMSBaseResponse<List<DisplayStatusVo>> getDisplayStatus() {
		HRMSBaseResponse<List<DisplayStatusVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getDisplayStatus();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/kpicycle")
	HRMSBaseResponse<KraCycleRequestVo> getKraCycleById(@RequestParam(required = true) Long cycleId) {
		HRMSBaseResponse<KraCycleRequestVo> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getKraCycleById(cycleId);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/kracyclestatus")
	HRMSBaseResponse<List<KraCycleStatusVo>> getKraCycleStatusMaster() {
		HRMSBaseResponse<List<KraCycleStatusVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getKraCycleStatusList();

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "Get list of kra years", description = "This api is used to get list of kra years")
	@GetMapping("/krayears")
	public HRMSBaseResponse<List<KraYearVo>> getKraYears(
			@RequestParam(name = "flag", required = false, defaultValue = "N") String flag) {
		HRMSBaseResponse<List<KraYearVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getKraYears(flag);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "Get list of category", description = "This api is used to get list of kra category")
	@GetMapping("/category")
	HRMSBaseResponse<List<CategoryResponseVo>> getCategory() {
		HRMSBaseResponse<List<CategoryResponseVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getCategory();
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "Get list of KPI Sub Categories", description = "This api is used to get list of kpi sub categories")
	@GetMapping("/subcategory")
	HRMSBaseResponse<List<SubCategoryMasterVo>> getSubCategory(@RequestParam(required = true) Long categoryId) {
		HRMSBaseResponse<List<SubCategoryMasterVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getSubCategory(categoryId);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/kracycle")
	HRMSBaseResponse<List<KraCycleVo>> getKraCycleMaster(@RequestParam(required = true) Long yearId) {
		HRMSBaseResponse<List<KraCycleVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getKraCycleList(yearId);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/grades")
	public HRMSBaseResponse getGradeList() {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			response.setResponseBody(masterService.getGradeList());
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		response.setApplicationVersion(applicationVersion);
		return response;
	}

	@GetMapping("/kpitype")
	public HRMSBaseResponse getKpiTypeList() {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			response.setResponseBody(masterService.getKpiTypeList());
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		response.setApplicationVersion(applicationVersion);
		return response;
	}

	@GetMapping("/kpimetric")
	public HRMSBaseResponse getKpiMetricList() {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			response.setResponseBody(masterService.getKpiMetricList());
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		response.setApplicationVersion(applicationVersion);
		return response;
	}

	@GetMapping("/roles")
	public HRMSBaseResponse getRoleList() {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			response.setResponseBody(masterService.getRoleList());
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		response.setApplicationVersion(applicationVersion);
		return response;
	}

	@Operation(summary = "Get list of kra years with pervious year", description = "This api is used to get list of kra years")
	@GetMapping("/krayearspervious")
	HRMSBaseResponse<List<KraYearVo>> getKraYearsPrevious() {
		HRMSBaseResponse<List<KraYearVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getKraYearsPrevious();
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "Delete state", description = "This API is use to delete state")
	@PostMapping("delete/state")
	public HRMSBaseResponse<String> deleteState(@RequestBody StateVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = masterService.deleteState(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "you can add new Country using this API", description = " Add Country API")
	@PostMapping(value = "/addcountry")
	HRMSBaseResponse<?> addCountry(@RequestBody CountryMasterVO addCountry) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addCountry(addCountry);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Delete country", description = "This API is use to delete country")
	@PostMapping("delete/country")
	public HRMSBaseResponse<String> deleteCountry(@RequestBody CountryMasterVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = masterService.deleteCountry(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "you can add new city details using this API", description = " Add city API")
	@PostMapping(value = "/addcity")
	HRMSBaseResponse<?> addStateDetails(@RequestBody CityMasterVO city) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addCity(city);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Delete city", description = "This API is use to delete city")
	@PostMapping("delete/city")
	public HRMSBaseResponse<String> deleteCity(@RequestBody CityMasterVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = masterService.deleteCity(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "you can add Metric using this API", description = " Add Metric API")
	@PostMapping(value = "/addmetric")
	HRMSBaseResponse<?> addMetric(@RequestBody KpiMetricVo metricVo) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addMetric(metricVo);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Delete Metric", description = "This API is use to delete Metric")
	@PostMapping("delete/metric")
	public HRMSBaseResponse<String> deleteMetric(@RequestBody KpiMetricVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = masterService.deleteMetric(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "you can add Grade using this API", description = " Add Grade API")
	@PostMapping(value = "/addgrade")
	HRMSBaseResponse<?> addGrade(@RequestBody GradeMasterVo gradeVo) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addGrade(gradeVo);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Delete Grade", description = "This API is use to delete Grade")
	@PostMapping("delete/grade")
	public HRMSBaseResponse<String> deleteGrade(@RequestBody GradeMasterVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = masterService.deleteGrade(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Delete Branch", description = "This API is use to delete Branch")
	@PostMapping("delete/branch")
	public HRMSBaseResponse<String> deleteBranch(@RequestBody BranchVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = masterService.deleteBranch(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Delete Department", description = "This API is use to delete Department")
	@PostMapping("delete/department")
	public HRMSBaseResponse<String> deleteDepartment(@RequestBody DepartmentVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = masterService.deleteDepartment(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "you can add Employment Type using this API", description = " Add Employment Type API")
	@PostMapping(value = "/addemploymenttype")
	HRMSBaseResponse<?> addEmploymentType(@RequestBody EmploymentTypeVO employmentTypeVO) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addEmploymentType(employmentTypeVO);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Delete Department", description = "This API is use to delete Department")
	@PostMapping("delete/employmenttype")
	public HRMSBaseResponse<String> deleteEmploymentType(@RequestBody EmploymentTypeVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = masterService.deleteEmploymentType(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Delete Division", description = "This API is use to delete Division")
	@PostMapping("delete/division")
	public HRMSBaseResponse<String> deleteDivision(@RequestBody DivisionVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = masterService.deleteDivision(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Delete Designation", description = "This API is use to delete Designation")
	@PostMapping("delete/designation")
	public HRMSBaseResponse<String> deleteDesignation(@RequestBody DesignationVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = masterService.deleteDesignation(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "you can add Salutation Type using this API", description = " Add Salutation API")
	@PostMapping(value = "/addmastertile")
	HRMSBaseResponse<?> addMasterTitle(@RequestBody MasterTitleVo masterTitleVO) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addMasterTitle(masterTitleVO);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Delete Salutation", description = "This API is use to delete Salutation")
	@PostMapping("delete/mastertile")
	public HRMSBaseResponse<String> deleteMasterTitle(@RequestBody MasterTitleVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = masterService.deleteMasterTitle(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@GetMapping("/countrylist")
	public HRMSBaseResponse<CountryListResponseVO> getAllCountry(@RequestParam(required = false) String searchText,
			Pageable pageable) {

		HRMSBaseResponse<CountryListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = masterService.getAllCountry(searchText, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@GetMapping("/allstates")
	public HRMSBaseResponse<StateListResponseVO> getAllStateList(@RequestParam long countryId,
			@RequestParam(required = false) String searchText, Pageable pageable) {

		HRMSBaseResponse<StateListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = masterService.getAllStateList(countryId, searchText, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@GetMapping("/allcities")
	public HRMSBaseResponse<CityListResponseVO> getCity(@RequestParam(required = true) long stateId,
			@RequestParam(required = false) String searchText, Pageable pageable) {
		HRMSBaseResponse<CityListResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getAllCityList(stateId, searchText, pageable);
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@GetMapping("/kpimetriclist")
	public HRMSBaseResponse<KpiMetricListResponseVO> getAllKpiMetric(@RequestParam(required = false) String searchText,
			Pageable pageable) {

		HRMSBaseResponse<KpiMetricListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = masterService.getAllKpiMetric(searchText, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@GetMapping("/allgradelist")
	public HRMSBaseResponse<GradeListResponseVO> getAllGrades(@RequestParam(required = false) String searchText,
			Pageable pageable) {

		HRMSBaseResponse<GradeListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = masterService.getAllGrades(searchText, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@GetMapping("/allbranchlist")
	public HRMSBaseResponse<BranchListResponseVO> getAllBranches(@RequestParam(required = false) String searchText,
			Pageable pageable) {

		HRMSBaseResponse<BranchListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = masterService.getAllBranches(searchText, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@GetMapping("/alldepartmentist")
	public HRMSBaseResponse<DepartmentListResponseVO> getAllDepartments(
			@RequestParam(required = false) String searchText, Pageable pageable) {

		HRMSBaseResponse<DepartmentListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = masterService.getAllDepartments(searchText, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@GetMapping("/allemploymentist")
	public HRMSBaseResponse<EmploymentTypeListResponseVO> getAllEmploymentTypes(
			@RequestParam(required = false) String searchText, Pageable pageable) {

		HRMSBaseResponse<EmploymentTypeListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = masterService.getAllEmploymentTypes(searchText, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@GetMapping("/alldivisionlist")
	public HRMSBaseResponse<DivisionListResponseVO> getAllDivisions(@RequestParam(required = false) String searchText,
			Pageable pageable) {

		HRMSBaseResponse<DivisionListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = masterService.getAllDivisions(searchText, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@GetMapping("/alldesginationlist")
	public HRMSBaseResponse<DesignationListResponseVO> getAllDesignations(
			@RequestParam(required = false) String searchText, Pageable pageable) {

		HRMSBaseResponse<DesignationListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = masterService.getAllDesignations(searchText, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@GetMapping("/alltitleslist")
	public HRMSBaseResponse<MasterTitleListResponseVO> getAllMasterTitles(
			@RequestParam(required = false) String searchText, Pageable pageable) {

		HRMSBaseResponse<MasterTitleListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = masterService.getAllMasterTitles(searchText, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Get list of Objectives by SubCategory", description = "This API is used to get list of objectives based on SubCategory ID")
	@GetMapping("/objective")
	public HRMSBaseResponse<List<ObjectivesVO>> getObjective(@RequestParam(required = true) Long subCategoryId,
			Pageable pageable) {
		HRMSBaseResponse<List<ObjectivesVO>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getObjectives(subCategoryId, pageable);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);

		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "you can add new Objective using this API", description = " Add Ojective API")
	@PostMapping(value = "/addojective")
	HRMSBaseResponse<?> addObjective(@RequestBody ObjectivesVO objectivesVO) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse();
		try {
			response = masterService.addObjective(objectivesVO);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new HRMSBaseResponse<>();
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@GetMapping("/subcategory-stages")
	public HRMSBaseResponse getSubCategoryStagesList() {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			List<SubCategoryStagesResponseVO> subCategoryStagesVO = masterService.getSubCategoryStagesList();
			response.setResponseBody(subCategoryStagesVO);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());

		} catch (Exception e) {

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));

		}
		return response;
	}

	@Operation(summary = "This API is to get screen type list ", description = "Fetches a list of screen types based on the role and active status")
	@GetMapping("/screen-type")
	HRMSBaseResponse<List<MasterScreenTypeResponseVo>> getScreenType(@RequestParam Long role) throws HRMSException {
		HRMSBaseResponse<List<MasterScreenTypeResponseVo>> response = new HRMSBaseResponse<>();
		try {
			response = masterService.getScreenType(role);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

}
