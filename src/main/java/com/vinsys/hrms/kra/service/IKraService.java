package com.vinsys.hrms.kra.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.vo.AutoSubmitResponseVo;
import com.vinsys.hrms.kra.vo.CalibrationRequestVo;
import com.vinsys.hrms.kra.vo.CategoryRequestVo;
import com.vinsys.hrms.kra.vo.CycleDefinationRequestVo;
import com.vinsys.hrms.kra.vo.CycleDefinationResponseVo;
import com.vinsys.hrms.kra.vo.CycleResponseVo;
import com.vinsys.hrms.kra.vo.DashboardCountResponse;
import com.vinsys.hrms.kra.vo.DashboardRatingResponseVO;
import com.vinsys.hrms.kra.vo.DelegationMappingResponseVO;
import com.vinsys.hrms.kra.vo.DelegationRequestVo;
import com.vinsys.hrms.kra.vo.DeleteDelegationMappingVO;
import com.vinsys.hrms.kra.vo.DeleteKraRequestVO;
import com.vinsys.hrms.kra.vo.DepartmentEmpListVo;
import com.vinsys.hrms.kra.vo.DeptwiseCountResponseVO;
import com.vinsys.hrms.kra.vo.DonutCountResponse;
import com.vinsys.hrms.kra.vo.GenerateKPITemplateRequestVo;
import com.vinsys.hrms.kra.vo.HcdCorrcetionRequest;
import com.vinsys.hrms.kra.vo.HcdCorrectionResponseVo;
import com.vinsys.hrms.kra.vo.HodCycleFinalRatingResponseVO;
import com.vinsys.hrms.kra.vo.HodToDepartmentMapListResponseVO;
import com.vinsys.hrms.kra.vo.HodToDepartmentMapVO;
import com.vinsys.hrms.kra.vo.HrToDepartmentResponseVO;
import com.vinsys.hrms.kra.vo.HrToDepartmentVO;
import com.vinsys.hrms.kra.vo.KraCycleListResponseVO;
import com.vinsys.hrms.kra.vo.KraCycleRequestVo;
import com.vinsys.hrms.kra.vo.KraCycleTypeVO;
import com.vinsys.hrms.kra.vo.KraDetailsRequestVO;
import com.vinsys.hrms.kra.vo.KraDetailsResponseVO;
import com.vinsys.hrms.kra.vo.KraListRequestVo;
import com.vinsys.hrms.kra.vo.KraResponseVO;
import com.vinsys.hrms.kra.vo.KraYearVo;
import com.vinsys.hrms.kra.vo.NewKraResponse;
import com.vinsys.hrms.kra.vo.OrgKpiDeleteRequestVo;
import com.vinsys.hrms.kra.vo.OrgKpiDeleteVo;
import com.vinsys.hrms.kra.vo.OrgKpiReportResponse;
import com.vinsys.hrms.kra.vo.OrgKpiRequest;
import com.vinsys.hrms.kra.vo.OrgKpiResponseVo;
import com.vinsys.hrms.kra.vo.OrgnizationalKpiVo;
import com.vinsys.hrms.kra.vo.PMSObjectiveVo;
import com.vinsys.hrms.kra.vo.PMSObjectivesRequestVo;
import com.vinsys.hrms.kra.vo.PmsDashboardRequestVo;
import com.vinsys.hrms.kra.vo.PmsKraVo;
import com.vinsys.hrms.kra.vo.PublishResponse;
import com.vinsys.hrms.kra.vo.RatingPercentageResponseVO;
import com.vinsys.hrms.kra.vo.RejectRequestVo;
import com.vinsys.hrms.kra.vo.SubCategoryRequestVo;
import com.vinsys.hrms.kra.vo.TargetRequestVo;
import com.vinsys.hrms.kra.vo.TimeLineRequestVO;
import com.vinsys.hrms.kra.vo.TimeLineResponseVO;
import com.vinsys.hrms.kra.vo.TimeLineVo;
import com.vinsys.hrms.master.vo.SelfRatingVo;

/**
 * @author Onkar A.
 *
 */

@Service
public interface IKraService {

	HRMSBaseResponse<KraDetailsResponseVO> saveKra(KraDetailsRequestVO request) throws HRMSException;

	HRMSBaseResponse<List<KraResponseVO>> getKraList(String roleName, Long deptid, Long empId, String year,
			Long countryId, String status, Long cycleId, Long gradeId, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<KraDetailsResponseVO> getKraById(String kraid) throws HRMSException;

	Kra getCurrentYearKra() throws HRMSException;

	HRMSBaseResponse<KraDetailsResponseVO> submitKra(KraDetailsRequestVO request) throws HRMSException;

	HRMSBaseResponse<KraDetailsResponseVO> deleteKraPoint(DeleteKraRequestVO request) throws HRMSException;

	HRMSBaseResponse<KraDetailsResponseVO> approveKra(KraDetailsRequestVO request) throws HRMSException;

	HRMSBaseResponse<KraDetailsResponseVO> rejectKra(KraDetailsRequestVO request) throws HRMSException;

	// void downloadKraSummary(HttpServletResponse res) throws IOException,
	// HRMSException;

	public void SendKraCountToHR();

	void downloadKraReport(HttpServletResponse res, Long empid, Long kraid) throws IOException, HRMSException;

	HRMSBaseResponse<NewKraResponse> getNewKraById(Long kraid) throws HRMSException;

	HRMSBaseResponse<String> submitPmsKra(PmsKraVo request) throws HRMSException, Exception;

	HRMSBaseResponse<String> savePmsKra(PmsKraVo request) throws HRMSException;

	HRMSBaseResponse<String> saveKraCycle(KraCycleRequestVo request) throws HRMSException;

	HRMSBaseResponse<String> rejectNewKra(RejectRequestVo request) throws HRMSException, Exception;

	HRMSBaseResponse<String> submitManagerRating(PmsKraVo request) throws HRMSException, Exception;

	HRMSBaseResponse<String> saveCalibration(CalibrationRequestVo request) throws HRMSException, Exception;

	HRMSBaseResponse<String> saveManagerRating(PmsKraVo request) throws HRMSException;

	void downloadKraSummary(Long deptid, Long empid, String year, Long countryId, String status,
			HttpServletResponse res) throws IOException, HRMSException;

	HRMSBaseResponse<DashboardCountResponse> getPmsDashboard(PmsDashboardRequestVo request) throws HRMSException;

	HRMSBaseResponse<PublishResponse> getPublished() throws HRMSException;

	HRMSBaseResponse<String> savePublished() throws HRMSException, Exception;

	HRMSBaseResponse<String> acceptKpi(RejectRequestVo request) throws HRMSException, Exception;

	HRMSBaseResponse<String> saveHrCalibrate() throws HRMSException, Exception;

	void downloadHodKraSummary(Long deptid, Long empid, String year, Long countryId, HttpServletResponse res)
			throws IOException, HRMSException;

	HRMSBaseResponse<DashboardRatingResponseVO> getDashboardRating() throws HRMSException;

	HRMSBaseResponse<TimeLineVo> getKpiTimeline() throws HRMSException;

	HRMSBaseResponse<String> saveTarget(TargetRequestVo request) throws HRMSException;

	HRMSBaseResponse<RatingPercentageResponseVO> getRatingPercentage() throws HRMSException;

	HRMSBaseResponse<List<SelfRatingVo>> getSelfRating(Long categoryId, Long kraId, Long objId) throws HRMSException;

	void downloadKraDashboardRatingReport(HttpServletResponse res) throws IOException, HRMSException;

	void downloadKraPercentageRatingReport(HttpServletResponse res) throws HRMSException, IOException;

	HRMSBaseResponse<String> newSaveCalibration(CalibrationRequestVo request) throws HRMSException;

	HRMSBaseResponse<String> saveTargetPercentage(TargetRequestVo request) throws HRMSException;

	HRMSBaseResponse<CycleDefinationResponseVo> getCycleDefinationList(Long yearId, Long cycleId) throws HRMSException;

	HRMSBaseResponse<String> saveKpiYear(KraYearVo request) throws HRMSException;

	HRMSBaseResponse<String> saveCategory(CategoryRequestVo request) throws HRMSException;

	HRMSBaseResponse<String> updateKraCycle(KraCycleRequestVo request) throws HRMSException;

	HRMSBaseResponse<String> saveCycleDefination(CycleDefinationRequestVo request) throws HRMSException, ParseException, Exception;

	HRMSBaseResponse<String> addOrgKpi(OrgnizationalKpiVo request) throws HRMSException, Exception;

	HRMSBaseResponse<String> saveSubCategory(SubCategoryRequestVo request) throws HRMSException;

	HRMSBaseResponse<String> saveObejectives(PMSObjectivesRequestVo request) throws HRMSException;

	HRMSBaseResponse<List<CycleResponseVo>> getCycleList(Long yearId) throws HRMSException;

	HRMSBaseResponse<PMSObjectivesRequestVo> getObjectives(Long id) throws HRMSException;

	HRMSBaseResponse<String> generateKPITemplates(GenerateKPITemplateRequestVo request) throws HRMSException;

	HRMSBaseResponse<List<OrgKpiResponseVo>> getOrgKpiList(Long deptid, Long grade, Long yearId, Long kpiTypeId, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<String> updateOrgKpi(OrgnizationalKpiVo request) throws HRMSException;

	HRMSBaseResponse<String> updateObejectives(PMSObjectiveVo request) throws HRMSException;
	
	HRMSBaseResponse<String> sendMailOnSubmitKra(Long kraid, String isObjectiveSubmit) throws HRMSException, Exception;

	HRMSBaseResponse<String> acceptKpibyHcd(RejectRequestVo request) throws HRMSException, Exception;

	HRMSBaseResponse<String> rejectKpibyHcd(RejectRequestVo request) throws HRMSException, Exception;

	 
	HRMSBaseResponse<String> acceptKpiByLineManager(RejectRequestVo request) throws HRMSException, Exception;

	HRMSBaseResponse<String> acceptKpiByTeamMember(RejectRequestVo request) throws Exception;

	HRMSBaseResponse<String> deleteOrgKpi(OrgKpiDeleteRequestVo request) throws HRMSException;

	HRMSBaseResponse<List<KraResponseVO>> getKraListReport(KraListRequestVo request, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<String> lockedcycle(Long cycleId) throws HRMSException;

	HRMSBaseResponse<String> deleteLineManagerKpi(OrgKpiDeleteVo request) throws HRMSException;
	
	HRMSBaseResponse<List<OrgKpiReportResponse>> getOrgKpiListReport(OrgKpiRequest request, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<List<HcdCorrectionResponseVo>> getHcdCorrection(Long kraId) throws HRMSException;

	byte[] downloadOrgKpiReport(OrgKpiRequest request) throws HRMSException;
	
	byte[] downloadKraReport(KraListRequestVo  request) throws HRMSException;
	HRMSBaseResponse<String> saveHcdCorrection(HcdCorrcetionRequest request) throws HRMSException;

	HRMSBaseResponse<String> submitHcdCorrection(Long kraId) throws HRMSException;
	
	HRMSBaseResponse<String> saveKpiTimeLine(TimeLineRequestVO request) throws HRMSException;

	HRMSBaseResponse<TimeLineRequestVO> getKpiTimelineById(Long id) throws HRMSException;

	HRMSBaseResponse<String> deleteKpiTimeline(TimeLineRequestVO request) throws HRMSException;

	HRMSBaseResponse<TimeLineResponseVO> getKpiTimelineList(Long yearId, Long kraCycleId, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<DepartmentEmpListVo> getDeptpartmentEmpList() throws HRMSException;
	
	HRMSBaseResponse<DelegationMappingResponseVO> getDelegationMappingList(Pageable pageable, String searchText) throws HRMSException;
	
	HRMSBaseResponse<String> deleteDelegationMapping(DeleteDelegationMappingVO request) throws HRMSException;

	HRMSBaseResponse<List<KraResponseVO>> getDelegateList(KraListRequestVo request, Pageable pageable) throws HRMSException;
	HRMSBaseResponse<String> submitDelegation(DelegationRequestVo request) throws HRMSException;

	HRMSBaseResponse<List<DeptwiseCountResponseVO>> getDeptwiseCountSummary() throws HRMSException;
	
	byte[] downloadDelegationMappingReport(String searchText) throws HRMSException;

	byte[] downloadDelegateListReport(KraListRequestVo request) throws HRMSException;

	HRMSBaseResponse<AutoSubmitResponseVo> autoSubmitPmsKra(PmsKraVo request) throws Exception;

	HRMSBaseResponse<AutoSubmitResponseVo> autoSubmitManagerRating(PmsKraVo request) throws Exception;
	
	HRMSBaseResponse<String> activateKraCycle(KraCycleRequestVo request) throws HRMSException;
	
	HRMSBaseResponse<String> preDeleteCheckForCycle(Long cycleId) throws HRMSException;
	
	HRMSBaseResponse<KraCycleListResponseVO> getKraCycleList(long yearId, String searchText, Pageable pageable) throws HRMSException;
	
	HRMSBaseResponse<String> deleteCycleById(Long cycleId) throws HRMSException;

	HRMSBaseResponse<DonutCountResponse> pmsDonutDashboard(PmsDashboardRequestVo request) throws HRMSException;
	
	List<KraCycleTypeVO> getKraCycleTypeList() throws HRMSException;
	
	HRMSBaseResponse<List<HodCycleFinalRatingResponseVO>> getLastYearFinalRating(Long employeeId) throws HRMSException;

	HRMSBaseResponse<String> deleteById(Long id) throws HRMSException;
	
	HRMSBaseResponse<String> addHodToDepartmentMap(HodToDepartmentMapVO hodToDepartmentMapVo) throws HRMSException;
	
	HRMSBaseResponse<String> deleteHodToDepartmentMap(Long id) throws HRMSException;
	
	 HRMSBaseResponse<HodToDepartmentMapListResponseVO> getAllHodToDepartmentMap(Pageable pageable)throws HRMSException ;

	HRMSBaseResponse<List<HrToDepartmentVO>> getHrToDepartment() throws HRMSException;

	HRMSBaseResponse<String> submitKpiForm(Long kraId) throws Exception;


	
}
