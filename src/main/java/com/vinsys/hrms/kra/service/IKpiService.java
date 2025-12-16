package com.vinsys.hrms.kra.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.vo.AnalyticalDepartmentReportRequestVO;
import com.vinsys.hrms.kra.vo.AnalyticalDepartmentReportVO;
import com.vinsys.hrms.kra.vo.CategoryRequestVo;
import com.vinsys.hrms.kra.vo.CycleWiseResponseVO;
import com.vinsys.hrms.kra.vo.DeleteTemplateRequestVo;
import com.vinsys.hrms.kra.vo.EmployeeReportVo;
import com.vinsys.hrms.kra.vo.FunctionheadsToFunctionVO;
import com.vinsys.hrms.kra.vo.HrToDepartmentBulkVO;
import com.vinsys.hrms.kra.vo.HrToDepartmentRequestVo;
import com.vinsys.hrms.kra.vo.KpiFormFinalResponseVo;
import com.vinsys.hrms.kra.vo.KpiFormStatusRequestVo;
import com.vinsys.hrms.kra.vo.KpiFormToRoleRequestVO;
import com.vinsys.hrms.kra.vo.KpiFormToRoleResponseVO;
import com.vinsys.hrms.kra.vo.KpiResponseVo;
import com.vinsys.hrms.kra.vo.MapAllGradeToStagesResponseVO;
import com.vinsys.hrms.kra.vo.MapGradeToStagesRequestVO;
import com.vinsys.hrms.kra.vo.MapGradeToStagesResponseVO;
import com.vinsys.hrms.kra.vo.NotesRequestVO;
import com.vinsys.hrms.kra.vo.NotesResponseVO;
import com.vinsys.hrms.kra.vo.NotificationVo;
import com.vinsys.hrms.kra.vo.PmsDashboardRequestVo;
import com.vinsys.hrms.kra.vo.QuestionAndAnswerByKraIdRequestVO;
import com.vinsys.hrms.kra.vo.QuestionAnswerBulkRequestVO;
import com.vinsys.hrms.kra.vo.QuestionAnswerResponseVO;
import com.vinsys.hrms.kra.vo.RejectRequestVo;
import com.vinsys.hrms.kra.vo.SubCategoryRequestVo;
import com.vinsys.hrms.kra.vo.SubCategoryVo;
import com.vinsys.hrms.kra.vo.submitQuestionAnswerResponseVo;
import com.vinsys.hrms.master.vo.CategoryResponseVo;

@Service
public interface IKpiService {

	KpiFormFinalResponseVo getKpiFormStatus(Long yearId, Long empId) throws HRMSException;

	HRMSBaseResponse<KpiFormFinalResponseVo> setKpiFormStatus(KpiFormStatusRequestVo request) throws HRMSException;

	List<KpiResponseVo> getKpiSubCategoryList(Long categoryId) throws HRMSException;

	HRMSBaseResponse<String> setGradeToStageMapping(MapGradeToStagesRequestVO request) throws HRMSException;

	HRMSBaseResponse<MapGradeToStagesResponseVO> getGradeToStageMappingById(Long mappingId) throws HRMSException;

	HRMSBaseResponse<List<CategoryResponseVo>> getCategoryList(Pageable pageable, String searchText) throws HRMSException;

	HRMSBaseResponse<List<MapAllGradeToStagesResponseVO>> getGradeToStageMappings(MapGradeToStagesRequestVO requestVO,
			Pageable pageable)throws HRMSException;

	HRMSBaseResponse<CategoryRequestVo> getCategoryById(Long id) throws HRMSException;

	HRMSBaseResponse<List<SubCategoryVo>> getAllSubcategoryList(SubCategoryRequestVo requestVO, Pageable pageable)
			throws HRMSException;

	HRMSBaseResponse<SubCategoryVo> getSubCategoryById(Long subcategoryId) throws HRMSException;

	HRMSBaseResponse<SubCategoryVo> deleteSubCategoryById(Long subcategoryId) throws HRMSException;

	HRMSBaseResponse<String> saveNotes(NotesRequestVO request) throws HRMSException;

	HRMSBaseResponse<List<NotesResponseVO>> getAllNotes(String keyword, Pageable pageable)
			throws HRMSException;

	HRMSBaseResponse<NotesResponseVO> getNoteById(Long id) throws HRMSException;

	HRMSBaseResponse<String> deleteNoteById(Long id) throws HRMSException;

	HRMSBaseResponse<List<NotesResponseVO>> getNoteByScreeId(Long id) throws HRMSException;

	HRMSBaseResponse<List<AnalyticalDepartmentReportVO>> getAnalyticalDepartmentReport(AnalyticalDepartmentReportRequestVO request) throws HRMSException;
	
	HRMSBaseResponse<List<CycleWiseResponseVO>> analyticalCycleWiseDashboard(PmsDashboardRequestVo request)
			throws HRMSException;
	
	public byte[] downloadAnalyticalDepartmentReport(AnalyticalDepartmentReportRequestVO request) throws HRMSException;

	HRMSBaseResponse<List<EmployeeReportVo>> getAllEmployeeReport(String keyword) throws HRMSException;

	HRMSBaseResponse<String> toggleNotesStatus(NotesRequestVO request)throws HRMSException;
	
	public byte[] downloadEmployeeReport(String keyword) throws HRMSException;

	HRMSBaseResponse<NotificationVo> getNotifications() throws HRMSException;	
	
	public byte[] downloadAnalyticalCycleWiseReport(PmsDashboardRequestVo request) throws HRMSException ;

	HRMSBaseResponse<String> acceptKpiFunctionHead(RejectRequestVo request) throws Exception;

	HRMSBaseResponse<String> rejectKpibyFunctionHead(RejectRequestVo request) throws Exception;

	HRMSBaseResponse<String> migrateData() throws HRMSException;

	HRMSBaseResponse<String> deleteTemplate(DeleteTemplateRequestVo request) throws HRMSException;

	HRMSBaseResponse<String> saveHrToDepartment(HrToDepartmentBulkVO request) throws HRMSException;

	HRMSBaseResponse<List<KpiFormToRoleResponseVO>> kpiFormRole() throws HRMSException;

	HRMSBaseResponse<String> saveKpiFormRole(KpiFormToRoleRequestVO requestVO) throws HRMSException;

	HRMSBaseResponse<String> functionheadtofunction(FunctionheadsToFunctionVO request) throws HRMSException;
	
	HRMSBaseResponse<submitQuestionAnswerResponseVo> addOrUpdateQuestionAnswer(QuestionAnswerBulkRequestVO requestVO) throws HRMSException;
	
	HRMSBaseResponse<QuestionAnswerResponseVO> getQuestionAnswersByKraId(
			QuestionAndAnswerByKraIdRequestVO filterRequest) throws HRMSException;

	HRMSBaseResponse<String> deleteHrToDepartment(HrToDepartmentRequestVo request) throws HRMSException;

}
