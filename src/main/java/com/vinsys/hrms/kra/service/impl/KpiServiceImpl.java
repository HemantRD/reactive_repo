package com.vinsys.hrms.kra.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
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
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.vinsys.hrms.constants.ELogo;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.email.utils.EventsConstants;
import com.vinsys.hrms.email.vo.EmailRequestVO;
import com.vinsys.hrms.email.vo.PlaceHolderVO;
import com.vinsys.hrms.email.vo.TemplateVO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterRole;
import com.vinsys.hrms.entity.OrgDivWiseHRMapping;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.dao.IAnalyticalDepartmentWiseDAO;
import com.vinsys.hrms.kra.dao.IDelegationMappingDAO;
import com.vinsys.hrms.kra.dao.IEmployeeReportDAO;
import com.vinsys.hrms.kra.dao.IFuncationHeadToDivisionFunctionDAO;
import com.vinsys.hrms.kra.dao.IGradeToStageMappingDAO;
import com.vinsys.hrms.kra.dao.IHodToDepartmentMap;
import com.vinsys.hrms.kra.dao.IKraCalenderDao;
import com.vinsys.hrms.kra.dao.IKraCycleDAO;
import com.vinsys.hrms.kra.dao.IKraDao;
import com.vinsys.hrms.kra.dao.IKraDetailsDao;
import com.vinsys.hrms.kra.dao.IKraWfDao;
import com.vinsys.hrms.kra.dao.IKraYearDao;
import com.vinsys.hrms.kra.dao.ISubCategoryStagesDAO;
import com.vinsys.hrms.kra.dao.KraStatusDao;
import com.vinsys.hrms.kra.entity.DelegationMapping;
import com.vinsys.hrms.kra.entity.EmployeeReport;
import com.vinsys.hrms.kra.entity.GradeToStageMapping;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraCycleCalender;
import com.vinsys.hrms.kra.entity.KraCycleStatus;
import com.vinsys.hrms.kra.entity.KraDetails;
import com.vinsys.hrms.kra.entity.KraWf;
import com.vinsys.hrms.kra.entity.KraYear;
import com.vinsys.hrms.kra.entity.SubCategoryStages;
import com.vinsys.hrms.kra.service.IKpiService;
import com.vinsys.hrms.kra.util.EKraStatus;
import com.vinsys.hrms.kra.util.KpiHelper;
import com.vinsys.hrms.kra.vo.AnalyticalDepartmentReportRequestVO;
import com.vinsys.hrms.kra.vo.AnalyticalDepartmentReportVO;
import com.vinsys.hrms.kra.vo.AnalyticalDepartmentSummaryResponseVO;
import com.vinsys.hrms.kra.vo.CategoryRequestVo;
import com.vinsys.hrms.kra.vo.CycleWiseResponseVO;
import com.vinsys.hrms.kra.vo.EmployeeReportVo;
import com.vinsys.hrms.kra.vo.KpiFormFinalResponseVo;
import com.vinsys.hrms.kra.vo.KpiFormStatusCycleVo;
import com.vinsys.hrms.kra.vo.KpiFormStatusRequestVo;
import com.vinsys.hrms.kra.vo.KpiFormStatusResponseVo;
import com.vinsys.hrms.kra.vo.KpiResponseVo;
import com.vinsys.hrms.kra.vo.MapAllGradeToStagesResponseVO;
import com.vinsys.hrms.kra.vo.MapGradeToStagesRequestVO;
import com.vinsys.hrms.kra.vo.MapGradeToStagesResponseVO;
import com.vinsys.hrms.kra.vo.MasterCategoryVO;
import com.vinsys.hrms.kra.vo.MasterSubCategoryVO;
import com.vinsys.hrms.kra.vo.NotesRequestVO;
import com.vinsys.hrms.kra.vo.NotesResponseVO;
import com.vinsys.hrms.kra.vo.NotificationListVo;
import com.vinsys.hrms.kra.vo.NotificationVo;
import com.vinsys.hrms.kra.vo.PmsDashboardRequestVo;
import com.vinsys.hrms.kra.vo.RejectRequestVo;
import com.vinsys.hrms.kra.vo.SubCategoryRequestVo;
import com.vinsys.hrms.kra.vo.SubCategoryVo;
import com.vinsys.hrms.logo.entity.Logo;
import com.vinsys.hrms.logo.service.LogoService;
import com.vinsys.hrms.master.dao.ICategoryDao;
import com.vinsys.hrms.master.dao.IGradeDAO;
import com.vinsys.hrms.master.dao.IMasterNotesDAO;
import com.vinsys.hrms.master.dao.IMasterRoleDao;
import com.vinsys.hrms.master.dao.IMasterScreenTypeDAO;
import com.vinsys.hrms.master.dao.IObjectivesDAO;
import com.vinsys.hrms.master.dao.ISubCategoryDao;
import com.vinsys.hrms.master.entity.Category;
import com.vinsys.hrms.master.entity.GradeMaster;
import com.vinsys.hrms.master.entity.MasterNotes;
import com.vinsys.hrms.master.entity.MasterScreenType;
import com.vinsys.hrms.master.entity.Objectives;
import com.vinsys.hrms.master.entity.Subcategory;
import com.vinsys.hrms.master.vo.CategoryResponseVo;
import com.vinsys.hrms.master.vo.GradeMasterVo;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

@Service
public class KpiServiceImpl implements IKpiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IKraDao kraDAO;

	@Autowired
	private IKraCycleDAO cycleDAO;

	@Autowired
	private IMasterRoleDao roleDao;

	@Autowired
	private IKraDetailsDao KraDetailsDAO;

	@Autowired
	private IKraWfDao KrawfDAO;

	@Autowired
	private ISubCategoryDao subCategoryDAO;

	@Autowired
	private ICategoryDao categoryDAO;

	@Autowired
	private IKraYearDao yearDAO;

	@Autowired
	private IGradeToStageMappingDAO gradeToStageMappingDAO;

	@Autowired
	private IGradeDAO gradeDAO;

	@Autowired
	private IObjectivesDAO objectiveDAO;

	@Autowired
	private ISubCategoryStagesDAO stageDAO;

	@Autowired
	private IMasterNotesDAO notesDAO;

	@Autowired
	private IMasterScreenTypeDAO screenDAO;

	@Autowired
	private IEmployeeReportDAO empReportDAO;

	@Autowired
	private IAnalyticalDepartmentWiseDAO analyticalDepartmentDao;

	@Autowired
	IKraYearDao kraYearDao;

	@Autowired
	private IKraCycleDAO kraCycleDAO;

	@Autowired
	private LogoService logoService;

	@Autowired
	private IDelegationMappingDAO delegationMappingDAO;

	@Autowired
	private IHodToDepartmentMap hodToDepartmentMapDao;

	@Autowired
	private IHRMSEmployeeDAO employeeDAO;

	@Autowired
	KraStatusDao kraStatusDao;

	@Autowired
	IKraCalenderDao calenderDao;
	
	@Autowired
	IFuncationHeadToDivisionFunctionDAO funcationHeadToDivisionFunctionDAO;

	@Override
	public KpiFormFinalResponseVo getKpiFormStatus(Long yearId, Long empId) throws HRMSException {

		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (roles.stream().noneMatch(role -> role.equalsIgnoreCase(ERole.HR.name()))) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		List<Kra> kraData = kraDAO.findByEmployeeIdAndYear(empId, yearId);

		if (ObjectUtils.isEmpty(kraData)) {
			throw new HRMSException(1887, ResponseCode.getResponseCodeMap().get(1887));

		}

		List<KpiFormStatusResponseVo> cycleList = new ArrayList<>();
		KpiFormFinalResponseVo finalResponse = new KpiFormFinalResponseVo();

		if (ObjectUtils.isNotEmpty(kraData)) {
			for (Kra kra : kraData) {
				if (ObjectUtils.isEmpty(kra) || ObjectUtils.isEmpty(kra.getCycleId())) {
					continue;
				}

				KpiFormStatusResponseVo vo = new KpiFormStatusResponseVo();

				vo.setId(kra.getCycleId().getId());
				vo.setLabel(kra.getCycleId().getCycleName());
				vo.setIsActive(kra.getIsActive());

				cycleList.add(vo);
			}

			if (ObjectUtils.isNotEmpty(kraData.get(0).getKraYear())) {
				finalResponse.setYearId(kraData.get(0).getKraYear().getId());
			}

			finalResponse.setCycles(cycleList);
		}

		return finalResponse;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public HRMSBaseResponse<KpiFormFinalResponseVo> setKpiFormStatus(KpiFormStatusRequestVo request)
			throws HRMSException {
		HRMSBaseResponse<KpiFormFinalResponseVo> baseResponse = new HRMSBaseResponse<>();

		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (roles.stream().noneMatch(role -> role.equalsIgnoreCase(ERole.HR.name()))) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		KraYear year = yearDAO.findByIdAndIsActive(request.getYearId(), ERecordStatus.Y.name());

		List<Kra> kraData = kraDAO.findByEmployeeIdAndKraYear(request.getId(), year);

		if (HRMSHelper.isNullOrEmpty(kraData)) {
			throw new HRMSException(1887, ResponseCode.getResponseCodeMap().get(1887));
		}

		KpiFormFinalResponseVo finalResponse = new KpiFormFinalResponseVo();
		finalResponse.setYearId(request.getYearId());

		List<KpiFormStatusResponseVo> cycleList = new ArrayList<>();

		try {
			if (!CollectionUtils.isEmpty(request.getCycles())) {
				for (KpiFormStatusCycleVo cycle : request.getCycles()) {

					Kra kra = saveOrUpdateKra(request.getId(), request.getYearId(), cycle);

					updateKraDetailsByKraId(kra.getId(), cycle.getIsActive());
					updateKraWorkflowByKraId(kra.getId(), cycle.getIsActive());

					KpiFormStatusResponseVo vo = new KpiFormStatusResponseVo();
					vo.setId(cycle.getId());
					vo.setIsActive(cycle.getIsActive());

					KraCycle cycleEntity = cycleDAO.findById(cycle.getId()).orElse(null);
					if (cycleEntity != null) {
						vo.setLabel(cycleEntity.getCycleName());
						vo.setId(cycleEntity.getId());
					}

					cycleList.add(vo);
				}
			}

			finalResponse.setCycles(cycleList);
			baseResponse.setResponseBody(finalResponse);
			baseResponse.setResponseCode(1200);
			baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1886));

		} catch (Exception e) {
			baseResponse.setResponseCode(1500);
			baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		return baseResponse;
	}

	private void updateKraWorkflowByKraId(Long kraId, String isActive) {
		KraWf wfList = KrawfDAO.findByKraId(kraId);
		if (!HRMSHelper.isNullOrEmpty(wfList)) {
			wfList.setIsActive(isActive);
			wfList.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
			wfList.setUpdatedDate(new Date());
		}
		KrawfDAO.save(wfList);
	}

	private void updateKraDetailsByKraId(Long kraId, String isActive) {
		List<KraDetails> detailsList = KraDetailsDAO.findByKraId(kraId);
		if (!HRMSHelper.isNullOrEmpty(detailsList)) {
			for (KraDetails detail : detailsList) {
				detail.setIsActive(isActive);
				detail.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
				detail.setUpdatedDate(new Date());
			}
		}
		KraDetailsDAO.saveAll(detailsList);
	}

	private Kra saveOrUpdateKra(Long empId, Long yearId, KpiFormStatusCycleVo cycle) throws HRMSException {
		Kra kra = kraDAO.findByEmployeeIdAndYearIdAndCycleId(empId, yearId, cycle.getId());
		if (kra == null) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		kra.setIsActive(cycle.getIsActive());
		kra.setUpdatedDate(new Date());
		kra.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());

		return kraDAO.save(kra);
	}

	@Override
	public List<KpiResponseVo> getKpiSubCategoryList(Long categoryId) throws HRMSException {

		validateRolePermission();

		Category category = categoryDAO.findByIdAndIsActive(categoryId, ERecordStatus.Y.name());
		if (category == null) {
			throw new HRMSException(1638, ResponseCode.getResponseCodeMap().get(1638));
		}

		List<Subcategory> subCategoryList = subCategoryDAO.findBySubCategoryId(category.getId(),
				ERecordStatus.Y.name());
		if (CollectionUtils.isEmpty(subCategoryList)) {
			throw new HRMSException(1641, ResponseCode.getResponseCodeMap().get(1641));
		}

		return subCategoryList.stream().map(sub -> {
			KpiResponseVo vo = new KpiResponseVo();
			vo.setId(sub.getId());
			vo.setLabel(sub.getSubCategoryName());
			return vo;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public HRMSBaseResponse<String> setGradeToStageMapping(MapGradeToStagesRequestVO request) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		validateRolePermission();
		ValidateRequest(request);

		try {
			Long gradeId = request.getGrade().getId();
			Long categoryId = request.getCategory().getId();
			Long subCategoryId = request.getSubCategory().getId();
			Long mappingId = request.getId();

			Subcategory subCategory = subCategoryDAO.findByIdAndIsActive(subCategoryId, ERecordStatus.Y.name());
			if (subCategory == null) {
				throw new HRMSException(1641, ResponseCode.getResponseCodeMap().get(1641));
			}

			Objectives objective = objectiveDAO.findByCategoryIdAndSubCategoryIdAndIsActive(categoryId, subCategoryId,
					ERecordStatus.Y.name());

			GradeToStageMapping entity;
			boolean isUpdate = !HRMSHelper.isNullOrEmpty(mappingId);

			if (isUpdate) {
				entity = gradeToStageMappingDAO.findByIdAndIsActive(mappingId, ERecordStatus.Y.name());
				if (entity == null) {
					throw new HRMSException(1896, ResponseCode.getResponseCodeMap().get(1896));
				}
				validateDuplicateMappingForUpdate(gradeId, categoryId, subCategoryId, mappingId);
			} else {
				validateDuplicateMappingForAdd(gradeId, categoryId, subCategoryId);
				entity = new GradeToStageMapping();
				entity.setIsActive(ERecordStatus.Y.name());
				entity.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
				entity.setCreatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
				entity.setCreatedDate(new Date());
			}

			entity.setGradeId(gradeId);
			entity.setCategoryId(categoryId);
			entity.setSubCategoryId(subCategoryId);
			entity.setSubCategoryName(subCategory.getSubCategoryName());

			entity.setObjectiveId(objective != null ? objective.getId() : null);

			if (isUpdate) {
				entity.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
				entity.setUpdatedDate(new Date());
				response.setResponseBody(ResponseCode.getResponseCodeMap().get(1895));
			} else {
				response.setResponseBody(ResponseCode.getResponseCodeMap().get(1895));
			}

			gradeToStageMappingDAO.save(entity);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		} catch (HRMSException he) {
			throw he;
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	/**
	 * Validate duplicate mapping on ADD
	 */
	private void validateDuplicateMappingForAdd(Long gradeId, Long categoryId, Long subCategoryId)
			throws HRMSException {
		boolean mappingExists = gradeToStageMappingDAO.existsByGradeIdAndCategoryIdAndSubCategoryIdAndIsActive(gradeId,
				categoryId, subCategoryId, ERecordStatus.Y.name());
		if (mappingExists) {
			throw new HRMSException(1894, ResponseCode.getResponseCodeMap().get(1894));
		}
	}

	/**
	 * Validate duplicate mapping on UPDATE (excluding same ID)
	 */
	private void validateDuplicateMappingForUpdate(Long gradeId, Long categoryId, Long subCategoryId, Long id)
			throws HRMSException {
		boolean mappingExists = gradeToStageMappingDAO.existsByGradeIdAndCategoryIdAndSubCategoryIdAndIsActiveAndIdNot(
				gradeId, categoryId, subCategoryId, ERecordStatus.Y.name(), id);
		if (mappingExists) {
			throw new HRMSException(1894, ResponseCode.getResponseCodeMap().get(1894));
		}
	}

	private void ValidateRequest(MapGradeToStagesRequestVO request) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request) || HRMSHelper.isNullOrEmpty(request.getGrade())
				|| HRMSHelper.isNullOrEmpty(request.getGrade().getId())
				|| HRMSHelper.isNullOrEmpty(request.getCategory())
				|| HRMSHelper.isNullOrEmpty(request.getCategory().getId())
				|| HRMSHelper.isNullOrEmpty(request.getSubCategory())
				|| HRMSHelper.isNullOrEmpty(request.getSubCategory().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

		if (request.getCategory().getId() == (157L)) {
			throw new HRMSException(1913, ResponseCode.getResponseCodeMap().get(1913));
		}
	}

	@Override
	public HRMSBaseResponse<MapGradeToStagesResponseVO> getGradeToStageMappingById(Long mappingId)
			throws HRMSException {

		HRMSBaseResponse<MapGradeToStagesResponseVO> response = new HRMSBaseResponse<>();

		validateRolePermission();

		try {

			if (HRMSHelper.isNullOrEmpty(mappingId)) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
			}

			GradeToStageMapping entity = gradeToStageMappingDAO.findByIdAndIsActive(mappingId, ERecordStatus.Y.name());
			if (entity == null) {
				throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
			}

			MapGradeToStagesResponseVO vo = new MapGradeToStagesResponseVO();
			vo.setId(entity.getId());

			GradeMaster grade = gradeDAO.findByIdAndIsActive(entity.getGradeId(), ERecordStatus.Y.name());
			if (grade != null) {
				GradeMasterVo gradeVo = new GradeMasterVo();
				gradeVo.setId(grade.getId());
				gradeVo.setLabel(grade.getGradeDescription());
				vo.setGrade(gradeVo);
			}

			Category category = categoryDAO.findByIdAndIsActive(entity.getCategoryId(), ERecordStatus.Y.name());
			if (category != null) {
				MasterCategoryVO categoryVo = new MasterCategoryVO();
				categoryVo.setId(category.getId());
				categoryVo.setLabel(category.getCategoryName());
				categoryVo.setWeight(category.getWeightage());
				vo.setCategory(categoryVo);
			}

			Subcategory subCategory = subCategoryDAO.findByIdAndIsActive(entity.getSubCategoryId(),
					ERecordStatus.Y.name());
			if (subCategory != null) {
				MasterSubCategoryVO subCategoryVo = new MasterSubCategoryVO();
				subCategoryVo.setId(subCategory.getId());
				subCategoryVo.setLabel(subCategory.getSubCategoryName());
				vo.setSubCategory(subCategoryVo);
			}

			response.setResponseBody(vo);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		} catch (HRMSException he) {
			throw he;
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<List<MapAllGradeToStagesResponseVO>> getGradeToStageMappings(
			MapGradeToStagesRequestVO requestVO, Pageable pageable) throws HRMSException {

		HRMSBaseResponse<List<MapAllGradeToStagesResponseVO>> response = new HRMSBaseResponse<>();

		validateRolePermission();

		try {
			Long gradeId = Optional.ofNullable(requestVO).map(MapGradeToStagesRequestVO::getGrade)
					.map(GradeMasterVo::getId).orElse(null);

			Long categoryId = Optional.ofNullable(requestVO).map(MapGradeToStagesRequestVO::getCategory)
					.map(MasterCategoryVO::getId).orElse(null);

			Long subCategoryId = Optional.ofNullable(requestVO).map(MapGradeToStagesRequestVO::getSubCategory)
					.map(MasterSubCategoryVO::getId).orElse(null);

			List<GradeToStageMapping> allRecords = (gradeId == null && categoryId == null && subCategoryId == null)
					? gradeToStageMappingDAO.findAllByIsActive(ERecordStatus.Y.name())
					: gradeToStageMappingDAO.findAllMappingsWithFilters(ERecordStatus.Y.name(), gradeId, categoryId,
							subCategoryId);

			int totalRecords = allRecords.size();
			int start = (int) pageable.getOffset();
			int end = Math.min(start + pageable.getPageSize(), totalRecords);

			List<GradeToStageMapping> pagedList = (start < totalRecords) ? allRecords.subList(start, end)
					: Collections.emptyList();

			List<MapAllGradeToStagesResponseVO> voList = pagedList.stream().map(entity -> {
				MapAllGradeToStagesResponseVO vo = new MapAllGradeToStagesResponseVO();
				vo.setId(entity.getId());

				GradeMaster grade = gradeDAO.findByIdAndIsActive(entity.getGradeId(), ERecordStatus.Y.name());
				vo.setGrade(grade != null ? grade.getGradeDescription() : null);

				Category category = categoryDAO.findByIdAndIsActive(entity.getCategoryId(), ERecordStatus.Y.name());
				vo.setCategoryName(category != null ? category.getCategoryName() : null);

				vo.setSubCategoryName(entity.getSubCategoryName());
				return vo;
			}).collect(Collectors.toList());

			response.setResponseBody(voList);
			response.setTotalRecord(totalRecords);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		} catch (Exception e) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<List<CategoryResponseVo>> getCategoryList(Pageable pageable, String searchText)
			throws HRMSException {
		HRMSBaseResponse<List<CategoryResponseVo>> baseResponse = new HRMSBaseResponse<>();

		validateRolePermission();

		long totalRecords = 0;

		List<Category> categoryList;

		if (HRMSHelper.isNullOrEmpty(searchText)) {
			categoryList = categoryDAO.findByIsActive(IHRMSConstants.isActive, pageable);
			totalRecords = categoryDAO.countByIsActive(IHRMSConstants.isActive);
		} else {
			categoryList = categoryDAO.searchByIsActiveAndText(IHRMSConstants.isActive, searchText, pageable);
			totalRecords = categoryDAO.countSearchByIsActiveAndText(IHRMSConstants.isActive, searchText);
		}

		List<CategoryResponseVo> categoryResponseVo = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(categoryList)) {

			for (Category category : categoryList) {
				CategoryResponseVo vo = new CategoryResponseVo();
				vo.setId(category.getId());
				vo.setLabel(category.getCategoryName());
				vo.setWeight(category.getWeightage());
				categoryResponseVo.add(vo);
			}

		}

		baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseResponse.setResponseBody(categoryResponseVo);
		baseResponse.setTotalRecord(totalRecords);
		baseResponse.setResponseCode(1200);

		return baseResponse;
	}

	public void validateRolePermission() throws HRMSException {
		validateRolePermissionforUnlocked();
	}

	private void validateRolePermissionforUnlocked() throws HRMSException {
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	@Override
	public HRMSBaseResponse<CategoryRequestVo> getCategoryById(Long id) throws HRMSException {
		HRMSBaseResponse<CategoryRequestVo> baseResponse = new HRMSBaseResponse<>();
		CategoryRequestVo vo = new CategoryRequestVo();
		validateRolePermission();
		if (!HRMSHelper.isNullOrEmpty(id)) {
			Category category = categoryDAO.findByIdAndIsActive(id, ERecordStatus.Y.name());

			if (!HRMSHelper.isNullOrEmpty(category)) {

				vo.setId(category.getId());
				vo.setName(category.getCategoryName());
				vo.setWeightage(category.getWeightage());

			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}

		baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseResponse.setResponseBody(vo);
		baseResponse.setResponseCode(1200);

		return baseResponse;
	}

	@Override
	public HRMSBaseResponse<List<SubCategoryVo>> getAllSubcategoryList(SubCategoryRequestVo requestVO,
			Pageable pageable) throws HRMSException {

		validateRolePermission();
		HRMSBaseResponse<List<SubCategoryVo>> response = new HRMSBaseResponse<>();

		try {

			Long categoryId = requestVO.getCategoryId();
			if (HRMSHelper.isNullOrEmpty(categoryId)) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
			}

			Category category = categoryDAO.findByIdAndIsActive(categoryId, ERecordStatus.Y.name());
			if (category == null) {
				throw new HRMSException(1638, ResponseCode.getResponseCodeMap().get(1638));
			}

			List<Subcategory> allRecords = subCategoryDAO.findAllByCategory_IdAndIsActive(category.getId(),
					ERecordStatus.Y.name());
			if (allRecords == null) {
				allRecords = Collections.emptyList();
			}

			if (!HRMSHelper.isNullOrEmpty(requestVO.getKeyword())) {
				String searchKeyword = requestVO.getKeyword().trim().toLowerCase();
				allRecords = allRecords.stream().filter(s -> {
					String name = s != null ? s.getSubCategoryName() : null;
					return name != null && name.toLowerCase().contains(searchKeyword);
				}).collect(Collectors.toList());
			}

			int totalRecords = allRecords.size();
			int start = (int) pageable.getOffset();
			int end = Math.min(start + pageable.getPageSize(), totalRecords);

			List<Subcategory> pagedList = (start < totalRecords) ? allRecords.subList(start, end)
					: Collections.emptyList();

			List<SubCategoryVo> voList = pagedList.stream().filter(Objects::nonNull).map(entity -> {
				SubCategoryVo vo = new SubCategoryVo();
				vo.setSubcategoryId(entity.getId());
				vo.setSubcategoryname(entity.getSubCategoryName());
				vo.setCategoryName(entity.getCategory() != null ? entity.getCategory().getCategoryName() : null);
				vo.setCategoryId(entity.getCategory() != null ? entity.getCategory().getId() : null);
				return vo;
			}).collect(Collectors.toList());

			response.setResponseBody(voList);
			response.setTotalRecord(totalRecords);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		} catch (HRMSException e) {
			throw e;
		} catch (Exception e) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<SubCategoryVo> getSubCategoryById(Long subcategoryId) throws HRMSException {
		log.info("Inside getSubcategoryById method");

		HRMSBaseResponse<SubCategoryVo> response = new HRMSBaseResponse<>();
		validateRolePermission();

		if (subcategoryId == null) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

		Subcategory subcategory = subCategoryDAO.findByIdAndIsActive(subcategoryId, ERecordStatus.Y.name());
		if (subcategory == null) {
			throw new HRMSException(1641, ResponseCode.getResponseCodeMap().get(1641));
		}

		SubCategoryVo vo = new SubCategoryVo();
		vo.setSubcategoryId(subcategory.getId());
		vo.setCategoryId(subcategory.getCategory() != null ? subcategory.getCategory().getId() : null);
		vo.setCategoryName(subcategory.getCategory() != null ? subcategory.getCategory().getCategoryName() : null);
		vo.setSubcategoryname(subcategory.getSubCategoryName());
		vo.setStageId(subcategory.getStageId());
		SubCategoryStages stages = stageDAO.findByIsActiveAndId(ERecordStatus.Y.name(), subcategory.getStageId());
		if (!HRMSHelper.isNullOrEmpty(stages)) {
			vo.setStageName(stages.getStageName());
		}

		vo.setIsActive(subcategory.getIsActive());

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(vo);

		log.info("Exit from getSubcategoryById method");
		return response;
	}

	@Override
	public HRMSBaseResponse<SubCategoryVo> deleteSubCategoryById(Long subcategoryId) throws HRMSException {
		log.info("Inside deleteSubCategoryById method");

		HRMSBaseResponse<SubCategoryVo> response = new HRMSBaseResponse<>();
		validateRolePermission();

		if (subcategoryId == null) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

		Subcategory subcategory = subCategoryDAO.findByIdAndIsActive(subcategoryId, ERecordStatus.Y.name());
		if (subcategory == null) {
			throw new HRMSException(1641, ResponseCode.getResponseCodeMap().get(1641));
		}

		subcategory.setIsActive(ERecordStatus.N.name());
		subCategoryDAO.save(subcategory);

		List<Objectives> objectives = objectiveDAO.findBySubCategory(subcategoryId, ERecordStatus.Y.name());
		if (objectives != null && !objectives.isEmpty()) {
			for (Objectives objective : objectives) {
				objective.setIsActive(ERecordStatus.N.name());
			}
			objectiveDAO.saveAll(objectives);
		}

		SubCategoryVo vo = new SubCategoryVo();
		vo.setSubcategoryId(subcategory.getId());
		vo.setCategoryId(subcategory.getCategory() != null ? subcategory.getCategory().getId() : null);
		vo.setCategoryName(subcategory.getCategory() != null ? subcategory.getCategory().getCategoryName() : null);
		vo.setSubcategoryname(subcategory.getSubCategoryName());
		vo.setStageId(subcategory.getStageId());
		vo.setIsActive(subcategory.getIsActive());

		SubCategoryStages stages = stageDAO.findByIsActiveAndId(ERecordStatus.Y.name(), subcategory.getStageId());
		if (stages != null) {
			vo.setStageName(stages.getStageName());
		}
		vo.setIsActive(subcategory.getIsActive());

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1899));
		response.setResponseBody(vo);

		log.info("Exit from deleteSubCategoryById method");
		return response;
	}

	@Override
	public HRMSBaseResponse<String> saveNotes(NotesRequestVO request) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		validateRolePermission();
		KpiHelper.validateRequest(request);

		try {
			Long noteId = request.getId();
			boolean isUpdate = noteId != null;

			MasterNotes entity;

			if (isUpdate) {

				entity = notesDAO.findById(noteId)
						.orElseThrow(() -> new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201)));

				entity.setUpdatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
				entity.setUpdatedDate(new Date());

				if (request.getRoleId() != null) {
					entity.setRole(request.getRoleId());
				}
				if (request.getDescription() != null) {
					entity.setDescription(request.getDescription());
				}
				if (request.getTitle() != null) {
					entity.setTitle(request.getTitle());
				}
				if (request.getScreenId() != null) {
					entity.setScreenId(request.getScreenId());

					MasterScreenType screen = screenDAO.findByIdAndIsActive(request.getScreenId(),
							ERecordStatus.Y.name());
					if (!HRMSHelper.isNullOrEmpty(screen)) {
						entity.setScreenName(screen.getScreenName());
					}
				}
				if (request.getIsActive() != null) {
					entity.setIsActive(request.getIsActive());
				}

				notesDAO.save(entity);

				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setResponseBody(ResponseCode.getResponseCodeMap().get(1903));

			} else {

				List<MasterNotes> existingNote = notesDAO.findByScreenId(request.getScreenId());

				if (!HRMSHelper.isNullOrEmpty(existingNote)) {
					throw new HRMSException(1906, ResponseCode.getResponseCodeMap().get(1906));

				}

				entity = new MasterNotes();
				entity.setIsActive(ERecordStatus.Y.name());
				entity.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
				entity.setCreatedBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId().toString());
				entity.setCreatedDate(new Date());

				MasterScreenType screen = screenDAO.findByIdAndIsActive(request.getScreenId(), ERecordStatus.Y.name());
				if (!HRMSHelper.isNullOrEmpty(screen)) {
					entity.setScreenName(screen.getScreenName());
				}

				entity.setRole(request.getRoleId());
				entity.setDescription(request.getDescription());
				entity.setTitle(request.getTitle());
				entity.setScreenId(request.getScreenId());

				notesDAO.save(entity);

				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setResponseBody(ResponseCode.getResponseCodeMap().get(1902));
			}

		} catch (HRMSException he) {
			throw he;
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<List<NotesResponseVO>> getAllNotes(String keyword, Pageable pageable) throws HRMSException {
		validateRolePermission();
		HRMSBaseResponse<List<NotesResponseVO>> response = new HRMSBaseResponse<>();

		try {
			List<MasterNotes> allRecords = notesDAO.findAll();
			if (allRecords == null) {
				allRecords = Collections.emptyList();
			}

			if (!HRMSHelper.isNullOrEmpty(keyword)) {
				String searchKeyword = keyword.trim().toLowerCase();
				allRecords = allRecords.stream().filter(note -> {
					String title = note != null ? note.getTitle() : null;
					String description = note != null ? note.getDescription() : null;
					String screenName = note != null ? note.getScreenName() : null;

					return (title != null && title.toLowerCase().contains(searchKeyword))
							|| (description != null && description.toLowerCase().contains(searchKeyword))
							|| (screenName != null && screenName.toLowerCase().contains(searchKeyword));
				}).collect(Collectors.toList());
			}

			allRecords.sort(Comparator.comparing((MasterNotes note) -> !"Y".equalsIgnoreCase(note.getIsActive())));

			int totalRecords = allRecords.size();
			int start = (int) pageable.getOffset();
			int end = Math.min(start + pageable.getPageSize(), totalRecords);

			List<MasterNotes> pagedList = (start < totalRecords) ? allRecords.subList(start, end)
					: Collections.emptyList();

			List<NotesResponseVO> voList = pagedList.stream().filter(Objects::nonNull).map(note -> {
				NotesResponseVO vo = new NotesResponseVO();
				vo.setId(note.getId());
				vo.setTitle(note.getTitle());
				vo.setDescription(note.getDescription());
				vo.setRoleId(note.getRole());
				vo.setScreenId(note.getScreenId());
				vo.setIsActive(note.getIsActive());
				vo.setScreeName(note.getScreenName());
				return vo;
			}).collect(Collectors.toList());

			response.setResponseBody(voList);
			response.setTotalRecord(totalRecords);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		} catch (Exception e) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<NotesResponseVO> getNoteById(Long id) throws HRMSException {

		validateRolePermission();
		HRMSBaseResponse<NotesResponseVO> response = new HRMSBaseResponse<>();

		try {
			if (HRMSHelper.isNullOrEmpty(id)) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
			}

			MasterNotes entity = notesDAO.findByIdAndIsActive(id, ERecordStatus.Y.name());
			if (entity == null) {
				throw new HRMSException(1904, ResponseCode.getResponseCodeMap().get(1904));
			}

			NotesResponseVO vo = new NotesResponseVO();
			vo.setId(entity.getId());
			vo.setTitle(entity.getTitle());
			vo.setDescription(entity.getDescription());
			vo.setRoleId(entity.getRole());
			vo.setScreenId(entity.getScreenId());
			vo.setScreeName(entity.getScreenName());

			response.setResponseBody(vo);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		} catch (HRMSException e) {
			throw e;
		} catch (Exception e) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<String> deleteNoteById(Long id) throws HRMSException {

		validateRolePermission();
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		try {
			if (HRMSHelper.isNullOrEmpty(id)) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
			}

			if (!notesDAO.existsById(id)) {
				throw new HRMSException(1904, ResponseCode.getResponseCodeMap().get(1904));
			}

			notesDAO.deleteById(id);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1905));

		} catch (HRMSException e) {
			throw e;
		} catch (Exception e) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<List<NotesResponseVO>> getNoteByScreeId(Long id) throws HRMSException {

		validateRolePermission();
		HRMSBaseResponse<List<NotesResponseVO>> response = new HRMSBaseResponse<>();

		try {
			if (id == null) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
			}

			List<MasterNotes> notesList = notesDAO.findByScreenIdAndIsActive(id, ERecordStatus.Y.name());

			List<NotesResponseVO> voList = new ArrayList<>();
			if (notesList != null && !notesList.isEmpty()) {
				for (MasterNotes entity : notesList) {
					NotesResponseVO vo = new NotesResponseVO();
					vo.setTitle(entity.getTitle());
					vo.setDescription(entity.getDescription());
					vo.setScreenId(entity.getScreenId());
					vo.setScreeName(entity.getScreenName());
					voList.add(vo);
				}
			}

			response.setResponseBody(voList);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		} catch (HRMSException e) {
			throw e;
		} catch (Exception e) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<List<CycleWiseResponseVO>> analyticalCycleWiseDashboard(PmsDashboardRequestVo request)
			throws HRMSException {

		HRMSBaseResponse<List<CycleWiseResponseVO>> baseResponse = new HRMSBaseResponse<>();

		try {
			validateRolePermission();
			String year = request.getYear();
			KraYear activeYear;

			if (HRMSHelper.isNullOrEmpty(year)) {
				activeYear = yearDAO.findByIsActive(ERecordStatus.Y.name());

				if (HRMSHelper.isNullOrEmpty(activeYear)) {
					baseResponse.setResponseCode(1877);
					baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1877));
					baseResponse.setResponseBody(Collections.emptyList());
					baseResponse.setTotalRecord(0L);
					return baseResponse;
				}
			} else {
				activeYear = yearDAO.findByYear(year);

				if (HRMSHelper.isNullOrEmpty(activeYear)) {
					baseResponse.setResponseCode(1877);
					baseResponse.setResponseMessage("Provided year not found");
					baseResponse.setResponseBody(Collections.emptyList());
					baseResponse.setTotalRecord(0L);
					return baseResponse;
				}
			}

			List<KraCycle> cycles = cycleDAO.findByYear(Optional.of(activeYear));
			if (cycles == null || cycles.isEmpty()) {
				baseResponse.setResponseCode(1910);
				baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1910));
				baseResponse.setResponseBody(Collections.emptyList());
				baseResponse.setTotalRecord(0L);
				return baseResponse;
			}

			List<Long> cycleIds = cycles.stream().map(KraCycle::getId).collect(Collectors.toList());

			List<CycleCountResult> results = kraDAO.cycleWiseCount(activeYear.getYear(), cycleIds);
			if (results == null || results.isEmpty()) {
				baseResponse.setResponseCode(1201);
				baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1201));
				baseResponse.setResponseBody(Collections.emptyList());
				baseResponse.setTotalRecord(0L);
				return baseResponse;
			}

			Map<Long, KraCycle> cycleMap = cycles.stream()
					.collect(Collectors.toMap(KraCycle::getId, Function.identity()));

			List<CycleWiseResponseVO> responseList = results.stream().sorted(Comparator.comparingInt(result -> {
				KraCycle cycle = cycleMap.get(result.getCycleId() != null ? result.getCycleId().longValue() : null);
				return cycle != null && "Y".equalsIgnoreCase(cycle.getIsActive()) ? 0 : 1;
			})).map(result -> {
				KraCycle cycle = cycleMap.get(result.getCycleId() != null ? result.getCycleId().longValue() : null);

				if (cycle == null) {

					return new CycleWiseResponseVO("Unknown Cycle", 0L);
				}

				Long count = "Y".equalsIgnoreCase(cycle.getIsActive())
						? (result.getSubmittedCount() != null ? result.getSubmittedCount().longValue() : 0L)
						: (result.getCompletedCount() != null ? result.getCompletedCount().longValue() : 0L);

				return new CycleWiseResponseVO(cycle.getCycleName(), count);
			}).collect(Collectors.toList());

			baseResponse.setResponseBody(responseList);
			baseResponse.setResponseCode(1200);
			baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			baseResponse.setTotalRecord((long) responseList.size());

		} catch (Exception e) {
			log.error("Error in analyticalDashboardCycleWise for request: {}", request, e);
			baseResponse.setResponseCode(1500);
			baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			baseResponse.setResponseBody(Collections.emptyList());
			baseResponse.setTotalRecord(0L);
		}

		return baseResponse;
	}

	@Override
	public HRMSBaseResponse<List<AnalyticalDepartmentReportVO>> getAnalyticalDepartmentReport(
			AnalyticalDepartmentReportRequestVO request) throws HRMSException {

		HRMSBaseResponse<List<AnalyticalDepartmentReportVO>> response = new HRMSBaseResponse<>();
		List<AnalyticalDepartmentReportVO> reportList = new ArrayList<>();

		try {
			validateRolePermission();

			Long yearId = request.getYearId();
			Long cycleId = request.getCycleId();
			Long departmentId = request.getDepartmentId();

			KraCycle activeCycle = null;

			if (!HRMSHelper.isNullOrEmpty(cycleId)) {
				activeCycle = kraCycleDAO.findByKraCycle(cycleId);
				if (HRMSHelper.isNullOrEmpty(activeCycle)) {
					throw new HRMSException(1910, ResponseCode.getResponseCodeMap().get(1910));
				}
			} else {
				activeCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
				if (HRMSHelper.isNullOrEmpty(activeCycle)) {
					throw new HRMSException(1910, ResponseCode.getResponseCodeMap().get(1910));
				}
				cycleId = activeCycle.getId();
			}

			if (HRMSHelper.isNullOrEmpty(yearId)) {
				KraYear activeYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
				if (HRMSHelper.isNullOrEmpty(activeYear)) {
					throw new HRMSException(1877, ResponseCode.getResponseCodeMap().get(1877));
				}
				yearId = activeYear.getId();
			}

			final KraCycle finalActiveCycle = activeCycle;

			List<AnalyticalDepartmentSummaryResponseVO> summaryList;

			if (departmentId == null) {
				summaryList = analyticalDepartmentDao.getDepartmentSummary(yearId, cycleId).stream().map(row -> {
					AnalyticalDepartmentSummaryResponseVO vo = new AnalyticalDepartmentSummaryResponseVO();
					String deptName = row[0] != null ? row[0].toString() : null;
					String deptDesc = row[1] != null ? row[1].toString() : null;
					vo.setDepartmentName(deptName);

					// ===== Translate rating if Half Year / Mid Year cycle =====
					if (shouldTranslateForCycle(finalActiveCycle)) {
						vo.setDepartment(KpiHelper.translateRatingLabel(deptDesc));
					} else {
						vo.setDepartment(deptDesc);
					}

					vo.setCount(row[2] != null ? ((Number) row[2]).longValue() : 0L);
					return vo;
				}).collect(Collectors.toList());
			} else {
				summaryList = analyticalDepartmentDao.getRatingSummary(yearId, cycleId, departmentId).stream()
						.map(row -> {
							AnalyticalDepartmentSummaryResponseVO vo = new AnalyticalDepartmentSummaryResponseVO();
							String deptName = row[0] != null ? row[0].toString() : null;
							String deptDesc = row[1] != null ? row[1].toString() : null;
							vo.setDepartmentName(deptName);

							// ===== Translate rating if Half Year / Mid Year cycle =====
							if (shouldTranslateForCycle(finalActiveCycle)) {
								vo.setDepartment(KpiHelper.translateRatingLabel(deptDesc));
							} else {
								vo.setDepartment(deptDesc);
							}

							vo.setCount(row[2] != null ? ((Number) row[2]).longValue() : 0L);
							return vo;
						}).collect(Collectors.toList());
			}

			if (summaryList.isEmpty()) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1500));
			}

			AnalyticalDepartmentReportVO reportVO = new AnalyticalDepartmentReportVO();
			reportVO.setDepartmentSummary(summaryList);
			reportList.add(reportVO);

			response.setResponseBody(reportList);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		} catch (HRMSException e) {
			throw e;
		} catch (Exception e) {
			throw new HRMSException(1500,
					ResponseCode.getResponseCodeMap().get(1500) + " while fetching Analytical Department Report");
		}

		return response;
	}

	/**
	 * Returns true if the cycle is Half Year or Mid Year, for which translation is
	 * required
	 */
	private boolean shouldTranslateForCycle(KraCycle cycle) {
		if (HRMSHelper.isNullOrEmpty(cycle))
			return false;
		String name = cycle.getCycleName();
		Long cycleType = cycle.getCycleTypeId();
		return IHRMSConstants.HALF_YEAR_TYPE_ID.equals(cycleType);
	}

	@Override
	public byte[] downloadAnalyticalDepartmentReport(AnalyticalDepartmentReportRequestVO request) throws HRMSException {
		try {
			HRMSBaseResponse<List<AnalyticalDepartmentReportVO>> reportResponse = getAnalyticalDepartmentReport(
					request);
			List<AnalyticalDepartmentReportVO> reportList = reportResponse.getResponseBody();

			if (HRMSHelper.isNullOrEmpty(reportList)) {
				throw new HRMSException(1904, ResponseCode.getResponseCodeMap().get(1904));
			}

			try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				Sheet sheet = workbook.createSheet("Analytical_Department_Report");
				sheet.setDisplayGridlines(false);
				CreationHelper helper = workbook.getCreationHelper();

				;

				String[] headers = { "Department Name", request.getDepartmentId() == null ? "Department" : "Rating",
						"Count" };
				int totalCols = headers.length;

				CellStyle borderStyle = workbook.createCellStyle();
				borderStyle.setBorderTop(BorderStyle.THIN);
				borderStyle.setBorderBottom(BorderStyle.THIN);
				borderStyle.setBorderLeft(BorderStyle.THIN);
				borderStyle.setBorderRight(BorderStyle.THIN);

				for (int i = 0; i < 5; i++) {
					sheet.createRow(i);
				}

				Logo logo = logoService.getConfig(ELogo.LOGO.name());
				if (ObjectUtils.isEmpty(logo)) {
					throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
				}

				InputStream logoStream = getInputStreamFromPath(logo.getValue());
				if (logoStream != null) {
					byte[] logoBytes = IOUtils.toByteArray(logoStream);
					int pictureIdx = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG);
					logoStream.close();

					Drawing<?> drawing = sheet.createDrawingPatriarch();
					ClientAnchor anchor = helper.createClientAnchor();
					anchor.setCol1(0);
					anchor.setRow1(0);
					anchor.setCol2(1);
					anchor.setRow2(4);

					Picture pict = drawing.createPicture(anchor, pictureIdx);
					pict.resize(1.0, 1.0);
				}

				int titleRowIndex = 1;
				Row titleRow = sheet.getRow(titleRowIndex);
//				Cell titleCell = titleRow.createCell(2);
//				titleCell.setCellValue("Analytical Department Report");
				if (titleRow == null)
					titleRow = sheet.createRow(titleRowIndex);

				Cell titleCell = titleRow.createCell(1); // Column B
				titleCell.setCellValue("Analytical Department Report");

				CellStyle titleStyle = workbook.createCellStyle();
				Font titleFont = workbook.createFont();
				titleFont.setBold(true);
				titleFont.setFontHeightInPoints((short) 11);
				titleStyle.setFont(titleFont);
				titleStyle.setAlignment(HorizontalAlignment.CENTER);
				titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				titleCell.setCellStyle(titleStyle);

//				sheet.addMergedRegion(new CellRangeAddress(titleRowIndex, titleRowIndex, 2, totalCols + 1));

//				  int lastColIndex = headers.length;
//				Cell dateCell = titleRow.createCell(lastColIndex);
//				dateCell.setCellValue("Date: " + LocalDate.now());
//				CellStyle dateStyle = workbook.createCellStyle();
//				dateStyle.setAlignment(HorizontalAlignment.LEFT);
				Cell dateCell = titleRow.createCell(2); // Change this line to set the date in column C
				dateCell.setCellValue("Date: " + LocalDate.now());
				CellStyle dateStyle = workbook.createCellStyle();
				dateStyle.setAlignment(HorizontalAlignment.RIGHT);
				dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				dateCell.setCellStyle(dateStyle);

				int headerRowIndex = 5;
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

				int rowNum = headerRowIndex + 1;
				for (AnalyticalDepartmentReportVO reportVO : reportList) {
					List<AnalyticalDepartmentSummaryResponseVO> summaryList = reportVO.getDepartmentSummary();
					for (AnalyticalDepartmentSummaryResponseVO summary : summaryList) {
						Row row = sheet.createRow(rowNum++);
						String[] values = { summary.getDepartmentName(), summary.getDepartment(),
								summary.getCount() != null ? summary.getCount().toString() : "0" };

						for (int i = 0; i < values.length; i++) {
							Cell cell = row.createCell(i);
							cell.setCellValue(values[i]);
							cell.setCellStyle(borderStyle);
						}
					}
				}

				for (int i = 0; i < headers.length; i++) {
					sheet.autoSizeColumn(i);
				}

				workbook.write(out);
				return out.toByteArray();

			} catch (IOException e) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1783));
			}

		} catch (HRMSException e) {
			throw e;
		} catch (Exception e) {
			throw new HRMSException(1500,
					ResponseCode.getResponseCodeMap().get(1500) + " while downloading Analytical Department Report");
		}
	}

	public static InputStream getInputStreamFromPath(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IOException("File not found at path: " + filePath);
		}
		return new FileInputStream(file);
	}

	/*
	 * @Override public HRMSBaseResponse<List<EmployeeReportVo>>
	 * getAllEmployeeReport(String keyword) throws HRMSException {
	 * 
	 * log.info("Inside getAllEmployeeReport");
	 * HRMSBaseResponse<List<EmployeeReportVo>> response = new HRMSBaseResponse<>();
	 * 
	 * try { validateRolePermission(); List<EmployeeReport> allRecords =
	 * empReportDAO.findAll(); if (allRecords == null) { allRecords =
	 * Collections.emptyList(); }
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(keyword)) { String searchKeyword =
	 * keyword.trim().toLowerCase(); allRecords = allRecords.stream().filter(entity
	 * -> { String empName = entity != null ? entity.getEmployeeName() : null;
	 * String empCode = entity != null ? entity.getEmpCode() : null; String dept =
	 * entity != null ? entity.getDepartmentName() : null; String designation =
	 * entity != null ? entity.getDesignationName() : null;
	 * 
	 * return (empName != null && empName.toLowerCase().contains(searchKeyword)) ||
	 * (empCode != null && empCode.toLowerCase().contains(searchKeyword)) || (dept
	 * != null && dept.toLowerCase().contains(searchKeyword)) || (designation !=
	 * null && designation.toLowerCase().contains(searchKeyword));
	 * }).collect(Collectors.toList()); }
	 * 
	 * List<EmployeeReportVo> voList = allRecords.stream().map(entity -> {
	 * EmployeeReportVo vo = new EmployeeReportVo(); vo.setId(entity.getId());
	 * vo.setTmId(entity.getEmpCode());
	 * vo.setEmployeeName(entity.getEmployeeName()); vo.setGrade(entity.getGrade());
	 * vo.setDesignation(entity.getDesignationName());
	 * vo.setDepartment(entity.getDepartmentName());
	 * vo.setFunction(entity.getDivisionName());
	 * vo.setLinemanager(entity.getReportingManagerName()); vo.setJoiningDate(
	 * entity.getDateOfJoining() != null ? new
	 * java.sql.Date(entity.getDateOfJoining().getTime()) : null);
	 * 
	 * KraYear year = yearDAO.findByIsActive(ERecordStatus.Y.name()); if (year !=
	 * null) { List<KraCycle> cycles = cycleDAO.findByYearId(year.getId()); if
	 * (cycles != null && !cycles.isEmpty()) { List<Kra> kraData =
	 * kraDAO.findByEmployeeIdAndKraYear(entity.getId(), year);
	 * 
	 * if (kraData != null && !kraData.isEmpty()) { for (KraCycle cycle : cycles) {
	 * List<Kra> cycleKras = kraData.stream() .filter(kra -> kra.getCycleId() !=
	 * null && kra.getCycleId().getId().equals(cycle.getId()))
	 * .collect(Collectors.toList());
	 * 
	 * if (!cycleKras.isEmpty()) { String status = cycleKras.stream().anyMatch( kra
	 * -> IHRMSConstants.isActive.equalsIgnoreCase(kra.getIsActive())) ? "YES" :
	 * "NO";
	 * 
	 * switch (cycle.getCycleName()) { case IHRMSConstants.KPI_SUBMISSION:
	 * vo.setKpiSubmission(status); break; case IHRMSConstants.HALF_YEAR_CYCLE:
	 * vo.setHalfYear(status); break; case IHRMSConstants.YEAR_END_CYCLE:
	 * vo.setYearEnd(status); break; default: break; } } else { switch
	 * (cycle.getCycleName()) { case IHRMSConstants.KPI_SUBMISSION:
	 * vo.setKpiSubmission("NO"); break; case IHRMSConstants.HALF_YEAR_CYCLE:
	 * vo.setHalfYear("NO"); break; case IHRMSConstants.YEAR_END_CYCLE:
	 * vo.setYearEnd("NO"); break; default: break; } } } } } }
	 * 
	 * return vo; }).collect(Collectors.toList());
	 * 
	 * response.setResponseBody(voList); response.setTotalRecord(voList.size());
	 * response.setResponseCode(1200);
	 * response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
	 * 
	 * } catch (Exception e) { log.error("Error in getAllEmployeeReport: {}",
	 * e.getMessage(), e); throw new HRMSException(1500,
	 * ResponseCode.getResponseCodeMap().get(1500)); }
	 * 
	 * return response; }
	 */

	@Override
	public HRMSBaseResponse<List<EmployeeReportVo>> getAllEmployeeReport(String keyword) throws HRMSException {

		log.info("Inside getAllEmployeeReport");
		HRMSBaseResponse<List<EmployeeReportVo>> response = new HRMSBaseResponse<>();

		try {
			validateRolePermission();
			List<EmployeeReport> allRecords = empReportDAO.findAll();
			if (allRecords == null) {
				allRecords = Collections.emptyList();
			}

			if (!HRMSHelper.isNullOrEmpty(keyword)) {
				String searchKeyword = keyword.trim().toLowerCase();
				allRecords = allRecords.stream().filter(entity -> {
					String empName = entity != null ? entity.getEmployeeName() : null;
					String empCode = entity != null ? entity.getEmpCode() : null;
					String dept = entity != null ? entity.getDepartmentName() : null;
					String designation = entity != null ? entity.getDesignationName() : null;

					return (empName != null && empName.toLowerCase().contains(searchKeyword))
							|| (empCode != null && empCode.toLowerCase().contains(searchKeyword))
							|| (dept != null && dept.toLowerCase().contains(searchKeyword))
							|| (designation != null && designation.toLowerCase().contains(searchKeyword));
				}).collect(Collectors.toList());
			}

			List<EmployeeReportVo> voList = allRecords.stream().map(entity -> {
				EmployeeReportVo vo = new EmployeeReportVo();
				vo.setId(entity.getId());
				vo.setTmId(entity.getEmpCode());
				vo.setEmployeeName(entity.getEmployeeName());
				vo.setGrade(entity.getGrade());
				vo.setDesignation(entity.getDesignationName());
				vo.setDepartment(entity.getDepartmentName());
				vo.setFunction(entity.getDivisionName());
				vo.setLinemanager(entity.getReportingManagerName());
				vo.setJoiningDate(
						entity.getDateOfJoining() != null ? new java.sql.Date(entity.getDateOfJoining().getTime())
								: null);

				KraYear year = yearDAO.findByIsActive(ERecordStatus.Y.name());
				if (year != null) {
					List<KraCycle> cycles = cycleDAO.findByYearId(year.getId());
					if (cycles != null && !cycles.isEmpty()) {
						List<Kra> kraData = kraDAO.findByEmployeeIdAndKraYear(entity.getId(), year);

						if (kraData != null && !kraData.isEmpty()) {
							for (KraCycle cycle : cycles) {
								List<Kra> cycleKras = kraData.stream()
										.filter(kra -> kra.getCycleId() != null
												&& kra.getCycleId().getId().equals(cycle.getId()))
										.collect(Collectors.toList());

								String status = (!cycleKras.isEmpty() && cycleKras.stream()
										.anyMatch(kra -> IHRMSConstants.isActive.equalsIgnoreCase(kra.getIsActive())))
												? "YES"
												: "NO";

								Long cycleTypeId = cycle.getCycleTypeId();
								if (IHRMSConstants.KPI_SUBMISSION_TYPE_ID.equals(cycleTypeId)) {
									vo.setKpiSubmission(status);
								} else if (IHRMSConstants.HALF_YEAR_TYPE_ID.equals(cycleTypeId)) {
									vo.setHalfYear(status);
								} else if (IHRMSConstants.YEAR_END_TYPE_ID.equals(cycleTypeId)) {
									vo.setYearEnd(status);
								}

							}
						}
					}
				}

				return vo;
			}).collect(Collectors.toList());

			response.setResponseBody(voList);
			response.setTotalRecord(voList.size());
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		} catch (Exception e) {
			log.error("Error in getAllEmployeeReport: {}", e.getMessage(), e);
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<String> toggleNotesStatus(NotesRequestVO request) throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		validateRolePermission();

		String resultMessage = performToggleNotesStatus(request, loggedInEmpId);

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(resultMessage);

		return response;
	}

	private String performToggleNotesStatus(NotesRequestVO request, Long loggedInEmpId) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}

		MasterNotes note = notesDAO.findById(request.getId())
				.orElseThrow(() -> new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201)));

		if (IHRMSConstants.isActive.equals(note.getIsActive())) {

			note.setIsActive(IHRMSConstants.isNotActive);
			note.setUpdatedBy(loggedInEmpId.toString());
			note.setUpdatedDate(new Date());
			notesDAO.save(note);

			return ResponseCode.getResponseCodeMap().get(1912);
		} else {

			note.setIsActive(IHRMSConstants.isActive);
			note.setUpdatedBy(loggedInEmpId.toString());
			note.setUpdatedDate(new Date());
			notesDAO.save(note);

			return ResponseCode.getResponseCodeMap().get(1911);
		}
	}

	///////// get notifications////////////

	@Override
	public HRMSBaseResponse<NotificationVo> getNotifications() throws HRMSException {
		HRMSBaseResponse<NotificationVo> baseResponse = new HRMSBaseResponse<>();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		// ✅ Use LinkedHashSet to preserve order and remove duplicates
		Set<String> messageSet = new LinkedHashSet<>();

		if (!HRMSHelper.isNullOrEmpty(roles)) {
			KraCycle kraCycle = cycleDAO.findByIsActive(ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(kraCycle)) {
				messageSet.add(kraCycle.getCycleName() + " is open");

				for (String role : roles) {
					KraYear kraYear = yearDAO.findByIsActive(ERecordStatus.Y.name());
					if (HRMSHelper.isNullOrEmpty(kraYear) || HRMSHelper.isNullOrEmpty(kraCycle)) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
					}

					if (role.equalsIgnoreCase(ERole.EMPLOYEE.name())) {
						Kra kra = kraDAO.findByEmployeeIdAndIsActiveAndKraYearAndCycleId(loggedInEmpId,
								ERecordStatus.Y.name(), kraYear, kraCycle);
						KraWf workflow = KrawfDAO.findByKraAndPendingWithAndIsActive(kra, ERole.EMPLOYEE.name(),
								ERecordStatus.Y.name());

						if (!HRMSHelper.isNullOrEmpty(workflow)) {
							if (kraCycle.getCycleName().equalsIgnoreCase(IHRMSConstants.KPI_SUBMISSION)) {
								messageSet.add("Your KPI submission is Pending.");
							} else if (kraCycle.getCycleName().equalsIgnoreCase(IHRMSConstants.HALF_YEAR_CYCLE)) {
								messageSet.add("Your Half Year Rating is Pending.");
							} else if (kraCycle.getCycleName().equalsIgnoreCase(IHRMSConstants.YEAR_END_CYCLE)) {
								messageSet.add("Your Year End Rating is Pending.");
							}
						}

					} else if (role.equalsIgnoreCase(ERole.MANAGER.name())) {
						List<Kra> kraList = kraDAO.findEmployeesByManger(loggedInEmpId, ERecordStatus.Y.name(),
								ERole.MANAGER.name());

						if (!HRMSHelper.isNullOrEmpty(kraList)) {
							setCycleWiseMessagesForLineManager(messageSet, kraCycle, kraList);
						}

					} else if (role.equalsIgnoreCase(ERole.HOD.name())) {
						List<Long> departments = hodToDepartmentMapDao.getDepartmentByEmployeeAndIsActive(loggedInEmpId,
								ERecordStatus.Y.name());
						if (!HRMSHelper.isNullOrEmpty(departments)) {
							List<Kra> kraList = kraDAO.findByDeptIdInAndPendingWith(departments.toArray(),
									ERole.HOD.name(), ERecordStatus.Y.name());
							if (!HRMSHelper.isNullOrEmpty(kraList)) {
								setCycleWiseMessages(messageSet, kraCycle, kraList);
							}
						}

					} else if (role.equalsIgnoreCase(ERole.HR.name())) {
						List<Kra> kraList = kraDAO.findByPendingWithStatusAndIsActive(ERole.HR.name(),
								EKraStatus.COMPLETED.name(), ERecordStatus.Y.name());
						if (!HRMSHelper.isNullOrEmpty(kraList)) {
							setCycleWiseMessages(messageSet, kraCycle, kraList);
						}

					} else if (role.equalsIgnoreCase(ERole.DELEGATOR.name())) {
						List<DelegationMapping> empList = delegationMappingDAO
								.findByDelegationToAndIsActive(loggedInEmpId, ERecordStatus.Y.name());
						if (!HRMSHelper.isNullOrEmpty(empList)) {
							// KraCycle kpiCycle = cycleDAO.findByYearAndCycleName(kraYear,
							// IHRMSConstants.KPI_SUBMISSION);
							KraCycle kpiCycle = cycleDAO.findByYearAndCycleType(kraYear,
									IHRMSConstants.KPI_SUBMISSION_TYPE_ID);
							for (DelegationMapping emp : empList) {
								Kra kra = kraDAO.findByEmployeeIdAndIsActiveAndKraYearAndCycleId(emp.getDelegationFor(),
										ERecordStatus.Y.name(), kraYear, kpiCycle);
								KraWf workflow = KrawfDAO.findByKraAndPendingWithAndIsActive(kra, ERole.MANAGER.name(),
										ERecordStatus.Y.name());

								if (!HRMSHelper.isNullOrEmpty(workflow)) {
									if (kraCycle.getCycleTypeId().equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {
										messageSet.add(kra.getEmployee().getCandidate().getFirstName() + " "
												+ kra.getEmployee().getCandidate().getLastName()
												+ " is delegated to you. "
												+ IHRMSConstants.KPI_SUBMISSION_PENDING_MESSAGE + " "
												+ kra.getEmployee().getCandidate().getFirstName() + " "
												+ kra.getEmployee().getCandidate().getLastName());
									}
								}
							}
						}
					} else if (role.equalsIgnoreCase(ERole.DIVISIONHEAD.name())) {
						List<Long> divisions = funcationHeadToDivisionFunctionDAO.getDivisionByEmployeeAndIsActive(loggedInEmpId,
								ERecordStatus.Y.name());
						if (!HRMSHelper.isNullOrEmpty(divisions)) {
							List<Kra> kraList = kraDAO.findByDivIdnAndPendingWith(divisions.toArray(),
									ERole.DIVISIONHEAD.name(), ERecordStatus.Y.name());
							if (!HRMSHelper.isNullOrEmpty(kraList)) {
								setCycleWiseMessages(messageSet, kraCycle, kraList);
							}
						}
					}
				}
			}
		}

		// ✅ Build final response
		NotificationVo mainVo = new NotificationVo();
		List<NotificationListVo> listVo = new ArrayList<>();
		for (String text : messageSet) {
			NotificationListVo vo = new NotificationListVo();
			vo.setMessage(text);
			listVo.add(vo);
		}
		mainVo.setNotificationList(listVo);

		// ✅ Use unique count
		mainVo.setCount(Long.valueOf(messageSet.size()));
		baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseResponse.setResponseBody(mainVo);
		baseResponse.setTotalRecord(messageSet.size());
		baseResponse.setResponseCode(1200);
		return baseResponse;
	}

	private void setCycleWiseMessages(Set<String> messageSet, KraCycle kraCycle, List<Kra> kraList) {
		for (Kra empKra : kraList) {
			if (kraCycle.getCycleTypeId().equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {
				messageSet.add(IHRMSConstants.KPI_SUBMISSION_PENDING_MESSAGE + " "
						+ empKra.getEmployee().getCandidate().getFirstName() + " "
						+ empKra.getEmployee().getCandidate().getLastName());
			} else if (kraCycle.getCycleTypeId().equals(IHRMSConstants.HALF_YEAR_TYPE_ID)) {
				messageSet.add(IHRMSConstants.HALF_YEAR_PENDING_MESSAGE + " "
						+ empKra.getEmployee().getCandidate().getFirstName() + " "
						+ empKra.getEmployee().getCandidate().getLastName());
			} else if (kraCycle.getCycleTypeId().equals(IHRMSConstants.YEAR_END_TYPE_ID)) {
				messageSet.add(IHRMSConstants.YEAR_END_PENDING_MESSAGE + " "
						+ empKra.getEmployee().getCandidate().getFirstName() + " "
						+ empKra.getEmployee().getCandidate().getLastName());
			}
		}
	}

	private void setCycleWiseMessagesForLineManager(Set<String> messageSet, KraCycle kraCycle, List<Kra> kraList) {
		for (Kra empKra : kraList) {
			if (kraCycle.getCycleTypeId().equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {
				messageSet.add(IHRMSConstants.KPI_SUBMISSION_PENDING_MESSAGE_FOR_TEAM + " "
						+ empKra.getEmployee().getCandidate().getFirstName() + " "
						+ empKra.getEmployee().getCandidate().getLastName());
			} else if (kraCycle.getCycleTypeId().equals(IHRMSConstants.HALF_YEAR_TYPE_ID)) {
				messageSet.add(IHRMSConstants.HALF_YEAR_PENDING_MESSAGE_FOR_TEAM + " "
						+ empKra.getEmployee().getCandidate().getFirstName() + " "
						+ empKra.getEmployee().getCandidate().getLastName());
			} else if (kraCycle.getCycleTypeId().equals(IHRMSConstants.YEAR_END_TYPE_ID)) {
				messageSet.add(IHRMSConstants.YEAR_END_PENDING_MESSAGE_FOR_TEAM + " "
						+ empKra.getEmployee().getCandidate().getFirstName() + " "
						+ empKra.getEmployee().getCandidate().getLastName());
			}
		}
	}

	@Override
	public byte[] downloadEmployeeReport(String searchText) throws HRMSException {
		HRMSBaseResponse<List<EmployeeReportVo>> response = getAllEmployeeReport(searchText);
		List<EmployeeReportVo> employeeList = response.getResponseBody();

		if (HRMSHelper.isNullOrEmpty(employeeList)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Employee_Report");
			sheet.setDisplayGridlines(false);
			CreationHelper helper = workbook.getCreationHelper();

			String[] headers = { "TM ID", "Employee Name", "Grade", "Designation", "Department", "Function",
					"Line Manager", "Joining Date", "KPI Submission", "Half Year", "Year End" };
			int totalCols = headers.length;

			CellStyle borderStyle = workbook.createCellStyle();
			borderStyle.setBorderTop(BorderStyle.MEDIUM);
			borderStyle.setBorderBottom(BorderStyle.MEDIUM);
			borderStyle.setBorderLeft(BorderStyle.MEDIUM);
			borderStyle.setBorderRight(BorderStyle.MEDIUM);

			for (int i = 0; i < 4; i++) {
				Row row = sheet.createRow(i);
				for (int j = 0; j < totalCols; j++) {
					row.createCell(j);
				}
			}

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
				anchor.setCol2(3);
				anchor.setRow2(4);
				anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE);

				drawing.createPicture(anchor, pictureIdx);
			}

			int titleRowIndex = 1;
			int titleStartCol = totalCols / 2 - 1;
			int titleEndCol = Math.min(totalCols - 1, titleStartCol + 3);
			sheet.addMergedRegion(new CellRangeAddress(titleRowIndex, titleRowIndex, titleStartCol, titleEndCol));

			Row titleRow = sheet.getRow(titleRowIndex);
			Cell titleCell = titleRow.createCell(titleStartCol);
			titleCell.setCellValue("Employee Report");

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
			Font dateFont = workbook.createFont();
			dateFont.setBold(false);
			dateStyle.setFont(dateFont);
			dateStyle.setAlignment(HorizontalAlignment.RIGHT);
			dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			dateCell.setCellStyle(dateStyle);

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

			int rowNum = headerRowIndex + 1;
			for (EmployeeReportVo data : employeeList) {
				Row row = sheet.createRow(rowNum++);
				String[] values = { data.getTmId() != null ? data.getTmId() : "",
						data.getEmployeeName() != null ? data.getEmployeeName() : "",
						data.getGrade() != null ? data.getGrade() : "",
						data.getDesignation() != null ? data.getDesignation() : "",
						data.getDepartment() != null ? data.getDepartment() : "",
						data.getFunction() != null ? data.getFunction() : "",
						data.getLinemanager() != null ? data.getLinemanager() : "",
						data.getJoiningDate() != null ? data.getJoiningDate().toString() : "",
						data.getKpiSubmission() != null ? data.getKpiSubmission() : "NO",
						data.getHalfYear() != null ? data.getHalfYear() : "NO",
						data.getYearEnd() != null ? data.getYearEnd() : "NO" };

				for (int i = 0; i < values.length; i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(values[i]);
					cell.setCellStyle(borderStyle);
				}
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

	@Override
	public byte[] downloadAnalyticalCycleWiseReport(PmsDashboardRequestVo request) throws HRMSException {
		try {
			HRMSBaseResponse<List<CycleWiseResponseVO>> reportResponse = analyticalCycleWiseDashboard(request);
			List<CycleWiseResponseVO> reportList = reportResponse.getResponseBody();

			if (HRMSHelper.isNullOrEmpty(reportList)) {
				throw new HRMSException(1904, ResponseCode.getResponseCodeMap().get(1904));
			}

			try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				Sheet sheet = workbook.createSheet("Cycle_Wise_Report");
				sheet.setDisplayGridlines(false);
				CreationHelper helper = workbook.getCreationHelper();

				String[] headers = { "Cycle Name", "Count" };

				CellStyle borderStyle = workbook.createCellStyle();
				borderStyle.setBorderTop(BorderStyle.THIN);
				borderStyle.setBorderBottom(BorderStyle.THIN);
				borderStyle.setBorderLeft(BorderStyle.THIN);
				borderStyle.setBorderRight(BorderStyle.THIN);

				for (int i = 0; i < 5; i++) {
					sheet.createRow(i);
				}

				Logo logo = logoService.getConfig(ELogo.LOGO.name());
				if (ObjectUtils.isEmpty(logo)) {
					throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
				}

				InputStream logoStream = getInputStreamFromPath(logo.getValue());
				if (logoStream != null) {
					byte[] logoBytes = IOUtils.toByteArray(logoStream);
					int pictureIdx = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG);
					logoStream.close();

					Drawing<?> drawing = sheet.createDrawingPatriarch();
					ClientAnchor anchor = helper.createClientAnchor();
					anchor.setCol1(0);
					anchor.setRow1(0);
					anchor.setCol2(1);
					anchor.setRow2(4);

					Picture pict = drawing.createPicture(anchor, pictureIdx);
					pict.resize(1.0, 1.0);
				}

				int titleRowIndex = 1;
				Row titleRow = sheet.getRow(titleRowIndex);
				if (titleRow == null)
					titleRow = sheet.createRow(titleRowIndex);

				Cell titleCell = titleRow.createCell(1);
				titleCell.setCellValue("Analytical Cycle Wise Report");

				CellStyle titleStyle = workbook.createCellStyle();
				Font titleFont = workbook.createFont();
				titleFont.setBold(true);
				titleFont.setFontHeightInPoints((short) 11);
				titleStyle.setFont(titleFont);
				titleStyle.setAlignment(HorizontalAlignment.CENTER);
				titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				titleCell.setCellStyle(titleStyle);

				Cell dateCell = titleRow.createCell(2);
				dateCell.setCellValue("Date: " + LocalDate.now());
				CellStyle dateStyle = workbook.createCellStyle();
				dateStyle.setAlignment(HorizontalAlignment.RIGHT);
				dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				dateCell.setCellStyle(dateStyle);

				int headerRowIndex = 5;
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

				int rowNum = headerRowIndex + 1;
				for (CycleWiseResponseVO reportVO : reportList) {
					Row row = sheet.createRow(rowNum++);
					String[] values = { reportVO.getCycleName(),
							reportVO.getCount() != null ? reportVO.getCount().toString() : "0" };

					for (int i = 0; i < values.length; i++) {
						Cell cell = row.createCell(i);
						cell.setCellValue(values[i]);
						cell.setCellStyle(borderStyle);
					}
				}

				for (int i = 0; i < headers.length; i++) {
					sheet.autoSizeColumn(i);
				}

				workbook.write(out);
				return out.toByteArray();

			} catch (IOException e) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1783));
			}

		} catch (HRMSException e) {
			throw e;
		} catch (Exception e) {
			throw new HRMSException(1500,
					ResponseCode.getResponseCodeMap().get(1500) + " while downloading Analytical Cycle Wise Report");
		}
	}

	@Override
	public HRMSBaseResponse<String> acceptKpiFunctionHead(RejectRequestVo request) throws Exception {
		log.info("Inside Accept KPI by function head method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(request.getKraId()) && !HRMSHelper.isNullOrEmpty(request.getCycleId())) {

			if (HRMSHelper.isRolePresent(role, ERole.DIVISIONHEAD.name())) {
				Kra kra = kraDAO.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
				if (HRMSHelper.isNullOrEmpty(kra)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getCycleId(), ERecordStatus.Y.name());
				if (HRMSHelper.isNullOrEmpty(cycle)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
				MasterRole empRole = roleDao.findByRoleNameOrgIdIsActive(ERole.HOD.name(), loggedInEmployee.getOrgId(),
						IHRMSConstants.isActive);
				// isCycleOpen(status, empRole);
				KraWf kraWf = KrawfDAO.findByKraAndCycleIdAndIsActive(kra, cycle, ERecordStatus.Y.name());

				if (!HRMSHelper.isNullOrEmpty(kraWf)) {

					if (cycle.getCycleName().equalsIgnoreCase(IHRMSConstants.KPI_SUBMISSION)) {
						kraWf.setStatus(EKraStatus.INPROGRESS.name());
						kraWf.setPendingWith(ERole.HR.name());
					} else {
						kraWf.setStatus(EKraStatus.APPROVED.name());
						kraWf.setPendingWith(ERole.HOD.name());

					}
					KrawfDAO.save(kraWf);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1231));
		log.info("Exit from Accept KPI by Function head method");

		return response;

	}

	private void isCycleOpen(KraCycleStatus status, MasterRole empRole, Long cycleId) throws HRMSException {
		KraCycleCalender currentRoleCycle = calenderDao.findByIsActiveStatusAndRole(IHRMSConstants.isActive,
				status.getId(), empRole.getId(), cycleId );
		if (HRMSHelper.isNullOrEmpty(currentRoleCycle)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1625));
		}

		ZonedDateTime currentZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));

		Calendar today = Calendar.getInstance();

		Date utilEndDate = currentRoleCycle.getEndDate();
		Calendar cycleenddate = Calendar.getInstance();
		cycleenddate.setTime(utilEndDate);

		if (today.after(cycleenddate)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1626));
		}
	}

	@Override
	public HRMSBaseResponse<String> rejectKpibyFunctionHead(RejectRequestVo request) throws Exception {
		log.info("Inside Reject KPI by function head method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(request.getKraId()) && !HRMSHelper.isNullOrEmpty(request.getCycleId())) {

			if (HRMSHelper.isRolePresent(role, ERole.DIVISIONHEAD.name())) {
				Kra kra = kraDAO.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
				if (HRMSHelper.isNullOrEmpty(kra)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getCycleId(), ERecordStatus.Y.name());
				if (HRMSHelper.isNullOrEmpty(cycle)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
				KraWf kraWf = KrawfDAO.findByKraAndCycleIdAndIsActive(kra, cycle, ERecordStatus.Y.name());

				if (!HRMSHelper.isNullOrEmpty(kraWf)) {

					if (cycle.getCycleTypeId().equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {
						kraWf.setStatus(EKraStatus.INPROGRESS.name());
						kraWf.setPendingWith(ERole.MANAGER.name());
					} else {
						kraWf.setStatus(EKraStatus.INPROCESS.name());
						kraWf.setPendingWith(ERole.MANAGER.name());

					}
					
				/*	if (!HRMSHelper.isNullOrEmpty(request.getComment())) {
						kraWf.setComment(request.getComment());
					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
					}    */

					KrawfDAO.save(kraWf);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1923));
		log.info("Exit from Reject KPI by Function head method");

		return response;

	}
	
	
	@Override
	public HRMSBaseResponse<String> migrateData() throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		validateRolePermission();

		String resultMessage = performMigration(loggedInEmpId);

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(resultMessage);

		return response;
	}

	private String performMigration(Long loggedInEmpId) throws HRMSException {
		Long sourceCycleId = 321L;
		Long targetCycleId = 323L;

		List<Kra> sourceKraList = kraDAO.findByCycleId(sourceCycleId);

		if (!HRMSHelper.isNullOrEmpty(sourceKraList)) {
			for (Kra sourceKra : sourceKraList) {
				Long employeeId = sourceKra.getEmployeeId();

				Kra targetKra = kraDAO.findByEmployeeIdAndCycleId(employeeId, targetCycleId);
				KraCycle targetcycle = kraCycleDAO.findByKraCycle(targetCycleId);
				if (targetKra == null) {
					continue;
				}

				List<KraDetails> detailsList = KraDetailsDAO.findByKraId(sourceKra.getId());

				if (!HRMSHelper.isNullOrEmpty(detailsList)) {
					for (KraDetails sourceDetail : detailsList) {
						KraDetails cloned = new KraDetails();

						cloned.setYear(sourceDetail.getYear());
						cloned.setWeightage(sourceDetail.getWeightage());
						cloned.setKraDetails(sourceDetail.getKraDetails());
						cloned.setDescription(sourceDetail.getDescription());
						cloned.setIsActive(ERecordStatus.Y.name());
						cloned.setMeasurementCriteria(sourceDetail.getMeasurementCriteria());
						cloned.setAchievementPlan(sourceDetail.getAchievementPlan());
						cloned.setRmComment(sourceDetail.getRmComment());
						cloned.setCategoryId(sourceDetail.getCategoryId());
						cloned.setSubcategoryId(sourceDetail.getSubcategoryId());
						cloned.setObjectiveWeightage(sourceDetail.getObjectiveWeightage());
						cloned.setSelfRating(sourceDetail.getSelfRating());
						cloned.setManagerRating(sourceDetail.getManagerRating());
						cloned.setSelfQaulitativeAssisment(sourceDetail.getSelfQaulitativeAssisment());
						cloned.setManagerQaulitativeAssisment(sourceDetail.getManagerQaulitativeAssisment());
						cloned.setIsEdit(sourceDetail.getIsEdit());
						cloned.setIsColor(sourceDetail.getIsColor());
						cloned.setHcdComment(sourceDetail.getHcdComment());

						cloned.setKra(targetKra);
						cloned.setKraCycle(targetcycle);

						cloned.setCreatedBy(loggedInEmpId != null ? loggedInEmpId.toString() : "ETL");
						cloned.setCreatedDate(new Date());

						KraDetailsDAO.save(cloned);
					}
				}
			}
		}

		return ResponseCode.getResponseCodeMap().get(1922);
	}

}
