package com.vinsys.hrms.master.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.VOMasterRole;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.vo.KpiMetricVo;
import com.vinsys.hrms.kra.vo.KpiQuestionListResponseVO;
import com.vinsys.hrms.kra.vo.KpiQuestionRequestVO;
import com.vinsys.hrms.kra.vo.KpiQuestionsVO;
import com.vinsys.hrms.kra.vo.KraCycleRequestVo;
import com.vinsys.hrms.kra.vo.KraYearResponseVo;
import com.vinsys.hrms.kra.vo.KraYearVo;
import com.vinsys.hrms.kra.vo.QuestionAndAnswerRequestVO;
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

/**
 * @author Onkar A.
 * 
 */

public interface IMasterService {

	HRMSBaseResponse getLeaveTypes(HttpServletRequest request) throws HRMSException;

	MasterLeavePolicyVO getLeavePolicy(Long leaveType) throws HRMSException;

	HRMSBaseResponse getGrantLeaveTypes(HttpServletRequest request) throws HRMSException;

	List<DesignationVO> getDesignationList() throws HRMSException;

	List<DepartmentVO> getDepartmentList() throws HRMSException;

	List<DivisionVO> getDivisionList() throws HRMSException;

	List<BranchVO> getBranchList() throws HRMSException;

	List<MasterStateVO> getStateList(long countryId) throws HRMSException;

	List<SeparationReasonVO> getSeparationReasons(long modeOfSeparationId) throws HRMSException;

	HRMSBaseResponse<List<ModeOfEducationVO>> getModeOfEducation() throws HRMSException;

	HRMSBaseResponse<List<DegreeMasterVO>> getDegree() throws HRMSException;

	HRMSBaseResponse<List<YearMasterVO>> getYearList() throws HRMSException;

	HRMSBaseResponse<List<ProbationFeedbackParameterVO>> getProbationFeedbackParameter() throws HRMSException;;

	HRMSBaseResponse<List<RelationshipMasterVO>> getRelationshipList() throws HRMSException;

	HRMSBaseResponse<List<GenderMasterVO>> getGenderList() throws HRMSException;

	HRMSBaseResponse<List<SelectionMasterVO>> getOptionList() throws HRMSException;

	HRMSBaseResponse<?> addDesignation(DesignationVO designationVO) throws HRMSException;

	HRMSBaseResponse<?> addDepartment(DepartmentVO departmentDetails) throws HRMSException;

	HRMSBaseResponse<?> addDegree(DegreeMasterVO degreeDetails) throws HRMSException;

	HRMSBaseResponse<?> addRelationship(RelationshipMasterVO relationshipVO) throws HRMSException;

	HRMSBaseResponse<?> addGender(GenderMasterVO genderVO) throws HRMSException;

	HRMSBaseResponse<?> addModeOfEducation(ModeOfEducationVO modeOfEducationDetails) throws HRMSException;

	HRMSBaseResponse<?> addState(StateVO state) throws HRMSException;

	HRMSBaseResponse<?> addResignationReason(ModeofSeparationReasonVO resignationReason) throws HRMSException;

	HRMSBaseResponse<?> addDivision(DivisionVO divisionVO) throws HRMSException;

	HRMSBaseResponse<?> addBranch(BranchVO branch) throws HRMSException;

	HRMSBaseResponse<List<ReleaseTypeVO>> getReleaseTypes() throws HRMSException;;

	HRMSBaseResponse<List<ReportingOfficerVO>> getAllReportingManagerList() throws HRMSException;

	HRMSBaseResponse<List<OrganizationVO>> getOrganization() throws HRMSException;

	HRMSBaseResponse<List<ModeOfSeparationMasterVO>> getModeOfSeprationList() throws HRMSException;

	HRMSBaseResponse<List<MasterCountryVO>> getAllCountries() throws HRMSException;

	List<BrandingVO> getBranding(Long orgId) throws HRMSException;

	HRMSBaseResponse<List<MasterTravelTypeVO>> getTravelRequestType() throws HRMSException;

	HRMSBaseResponse<List<MasterModeOfTravelVO>> getTravelMode() throws HRMSException;

	HRMSBaseResponse<List<TripTypeVO>> getTravelTripType() throws HRMSException;

	HRMSBaseResponse<List<MasterBusTypeVO>> getBusType() throws HRMSException;

	HRMSBaseResponse<List<MasterTravellerTypeVO>> getTravellerType() throws HRMSException;

	HRMSBaseResponse<List<MasterAirTypeVO>> getAirType() throws HRMSException;

	HRMSBaseResponse<List<MasterVisaTypeVO>> getVisaType() throws HRMSException;

	HRMSBaseResponse<List<MasterDriverVO>> getDriverList() throws HRMSException;

	HRMSBaseResponse<List<MasterVehicleVO>> getVehicleList() throws HRMSException;

	TravelApproverResponseVO getApproverDetail(TravelDeskApproverRequestVO request) throws HRMSException;

	HRMSBaseResponse<List<CurrencyMasterVO>> getCurrencyList() throws HRMSException;

	HRMSBaseResponse<List<PrimaryRequesterVO>> getRequesterTypeList() throws HRMSException;

	HRMSBaseResponse<List<StayTypeVO>> getStayList() throws HRMSException;

	HRMSBaseResponse<List<ReimbursementTravelTypeVO>> getReimbursmentTravelTypeList() throws HRMSException;

	HRMSBaseResponse<List<ClaimTypeVO>> getClaimTypeList() throws HRMSException;

	HRMSBaseResponse<List<ClaimCategoryVO>> getClaimCategoryList() throws HRMSException;

	HRMSBaseResponse<List<MasterTitleVo>> getMasterTitle() throws HRMSException;

	HRMSBaseResponse<List<MasterMaritialStatusVo>> getMaritalStatus() throws HRMSException;

	List<MasterCityVO> getCityList(long stateId) throws HRMSException;

	HRMSBaseResponse<List<EmploymentTypeVO>> getEmploymentTypeList() throws HRMSException;

	HRMSBaseResponse<List<LoginEntityTypeVO>> getLoginEntityTypes() throws HRMSException;

	HRMSBaseResponse<List<SelfRatingVo>> getSelfRating() throws HRMSException;

	HRMSBaseResponse<List<KraYearResponseVo>> getNewYearList(Pageable pageable) throws HRMSException;

	HRMSBaseResponse<List<DisplayStatusVo>> getDisplayStatus() throws HRMSException;

	HRMSBaseResponse<List<KraCycleVo>> getKraCycleList(Long yearId) throws HRMSException;

	HRMSBaseResponse<List<KraCycleStatusVo>> getKraCycleStatusList() throws HRMSException;

	HRMSBaseResponse<List<KraYearVo>> getKraYears(String flag) throws HRMSException;

	HRMSBaseResponse<List<SubCategoryMasterVo>> getSubCategory(Long categoryId) throws HRMSException;

	HRMSBaseResponse<KraCycleRequestVo> getKraCycleById(Long cycleId) throws HRMSException;

	List<GradeMasterVo> getGradeList() throws HRMSException;

	List<KpiTypeMasterVo> getKpiTypeList() throws HRMSException;

	List<KpiMetricVo> getKpiMetricList() throws HRMSException;

	List<VOMasterRole> getRoleList() throws HRMSException;

	HRMSBaseResponse<List<KraYearVo>> getKraYearsPrevious() throws HRMSException;

	HRMSBaseResponse<String> deleteState(StateVO state) throws HRMSException;

	HRMSBaseResponse<String> addCountry(CountryMasterVO country) throws HRMSException;

	HRMSBaseResponse<String> deleteCountry(CountryMasterVO request) throws HRMSException;

	HRMSBaseResponse<String> addCity(CityMasterVO city) throws HRMSException;

	HRMSBaseResponse<String> deleteCity(CityMasterVO request) throws HRMSException;

	HRMSBaseResponse<String> addMetric(KpiMetricVo metric) throws HRMSException;

	HRMSBaseResponse<String> deleteMetric(KpiMetricVo request) throws HRMSException;

	HRMSBaseResponse<String> addGrade(GradeMasterVo gradeVo) throws HRMSException;

	HRMSBaseResponse<String> deleteGrade(GradeMasterVo request) throws HRMSException;

	HRMSBaseResponse<String> deleteBranch(BranchVO request) throws HRMSException;

	HRMSBaseResponse<String> deleteDepartment(DepartmentVO request) throws HRMSException;

	HRMSBaseResponse<String> addEmploymentType(EmploymentTypeVO employmentTypeVO) throws HRMSException;

	HRMSBaseResponse<String> deleteEmploymentType(EmploymentTypeVO request) throws HRMSException;

	HRMSBaseResponse<String> deleteDivision(DivisionVO request) throws HRMSException;

	HRMSBaseResponse<String> deleteDesignation(DesignationVO request) throws HRMSException;

	HRMSBaseResponse<String> addMasterTitle(MasterTitleVo masterTitleVO) throws HRMSException;

	HRMSBaseResponse<String> deleteMasterTitle(MasterTitleVo request) throws HRMSException;

	HRMSBaseResponse<CountryListResponseVO> getAllCountry(String searchText, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<StateListResponseVO> getAllStateList(long countryId, String searchText, Pageable pageable)
			throws HRMSException;

	HRMSBaseResponse<CityListResponseVO> getAllCityList(long stateId, String searchText, Pageable pageable)
			throws HRMSException;

	HRMSBaseResponse<KpiMetricListResponseVO> getAllKpiMetric(String searchText, Pageable pageable)
			throws HRMSException;

	HRMSBaseResponse<GradeListResponseVO> getAllGrades(String searchText, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<BranchListResponseVO> getAllBranches(String searchText, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<DepartmentListResponseVO> getAllDepartments(String searchText, Pageable pageable)
			throws HRMSException;

	HRMSBaseResponse<EmploymentTypeListResponseVO> getAllEmploymentTypes(String searchText, Pageable pageable)
			throws HRMSException;

	HRMSBaseResponse<DivisionListResponseVO> getAllDivisions(String searchText, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<DesignationListResponseVO> getAllDesignations(String searchText, Pageable pageable)
			throws HRMSException;

	HRMSBaseResponse<MasterTitleListResponseVO> getAllMasterTitles(String searchText, Pageable pageable)
			throws HRMSException;

	HRMSBaseResponse<List<ObjectivesVO>> getObjectives(Long subCategoryId, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<String> addObjective(ObjectivesVO objectivesVO) throws HRMSException;

	HRMSBaseResponse<List<CategoryResponseVo>> getCategory() throws HRMSException;

	List<SubCategoryStagesResponseVO> getSubCategoryStagesList() throws HRMSException;


	HRMSBaseResponse<List<MasterScreenTypeResponseVo>> getScreenType(Long roleId) throws HRMSException;
	
	HRMSBaseResponse<String> addOrUpdateKpiQuestion(KpiQuestionRequestVO vo) throws HRMSException;
	
	public HRMSBaseResponse<KpiQuestionListResponseVO> getAllKpiQuestions(long categoryId, long subcategoryId,
			String searchText, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<KpiQuestionsVO> getKpiQuestionById(Long id) throws HRMSException;

	HRMSBaseResponse<String> deleteKpiQuestion(Long id) throws HRMSException;

	HRMSBaseResponse<KpiQuestionListResponseVO> getAllKpiQuestionsByCategoryAndSubcategory(
	        long categoryId, long subcategoryId) throws HRMSException;
}
