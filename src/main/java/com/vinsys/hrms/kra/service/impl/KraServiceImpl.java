package com.vinsys.hrms.kra.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.ListUtils;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinsys.hrms.audit.service.IAuditLogService;
import com.vinsys.hrms.constants.ELogo;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IEmployeeDepartmentDAO;
import com.vinsys.hrms.dao.IEmployeeDesignationDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateProfessionalDetailDAO;
import com.vinsys.hrms.dao.IHRMSEmailTemplateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSLoginDAO;
import com.vinsys.hrms.dao.IHRMSLoginEntityTypeDAO;
import com.vinsys.hrms.dao.IHRMSMapOrgDivHrDAO;
import com.vinsys.hrms.dao.IHRMSMasterBranchDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.IMasterConfigDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.email.service.impl.EmailTransactionServiceImpl;
import com.vinsys.hrms.email.utils.EventsConstants;
import com.vinsys.hrms.email.vo.EmailRequestVO;
import com.vinsys.hrms.email.vo.PlaceHolderVO;
import com.vinsys.hrms.email.vo.TemplateVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.EmailTemplate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeDepartment;
import com.vinsys.hrms.entity.EmployeeDesignation;
import com.vinsys.hrms.entity.EmployeeDivision;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterConfig;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterRole;
import com.vinsys.hrms.entity.OrgDivWiseHRMapping;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.dao.IDashboardRatingDAO;
import com.vinsys.hrms.kra.dao.IDelegationMappingDAO;
import com.vinsys.hrms.kra.dao.IDeptwiseCountDAO;
import com.vinsys.hrms.kra.dao.IFuncationHeadToDivisionFunctionDAO;
import com.vinsys.hrms.kra.dao.IGradeToObjectiveMappingDao;
import com.vinsys.hrms.kra.dao.IHodCycleFinalRatingDao;
import com.vinsys.hrms.kra.dao.IHodToDepartmentMap;
import com.vinsys.hrms.kra.dao.IHrToDepartmentDAO;
import com.vinsys.hrms.kra.dao.IHrToHodMapDao;
import com.vinsys.hrms.kra.dao.IKpiFormToRoleDAO;
import com.vinsys.hrms.kra.dao.IKpiTemplateDAO;
import com.vinsys.hrms.kra.dao.IKpiTimelineDao;
import com.vinsys.hrms.kra.dao.IKraAggregateScoresDao;
import com.vinsys.hrms.kra.dao.IKraCalenderDao;
import com.vinsys.hrms.kra.dao.IKraCountForHRDAO;
import com.vinsys.hrms.kra.dao.IKraCycleDAO;
import com.vinsys.hrms.kra.dao.IKraCycleHistoryDAO;
import com.vinsys.hrms.kra.dao.IKraCycleTypeDAO;
import com.vinsys.hrms.kra.dao.IKraDao;
import com.vinsys.hrms.kra.dao.IKraDetailsDao;
import com.vinsys.hrms.kra.dao.IKraDetailsHistoryDAO;
import com.vinsys.hrms.kra.dao.IKraDetailsLiteDao;
import com.vinsys.hrms.kra.dao.IKraDisplayStatusDao;
import com.vinsys.hrms.kra.dao.IKraHistoryDAO;
import com.vinsys.hrms.kra.dao.IKraRatingRangeDao;
//import com.vinsys.hrms.kra.dao.IKraReportViewDao;
import com.vinsys.hrms.kra.dao.IKraWfDao;
import com.vinsys.hrms.kra.dao.IKraWfHistoryDAO;
import com.vinsys.hrms.kra.dao.IKraYearDao;
import com.vinsys.hrms.kra.dao.IMidYearRatingRangeDAO;
import com.vinsys.hrms.kra.dao.IPercentageRatingDAO;
import com.vinsys.hrms.kra.dao.IVisionKraViewDao;
import com.vinsys.hrms.kra.dao.KraStatusDao;
import com.vinsys.hrms.kra.dao.KraStatusMappingDao;
import com.vinsys.hrms.kra.entity.DashboardRatingView;
import com.vinsys.hrms.kra.entity.DelegationMapping;
import com.vinsys.hrms.kra.entity.DeptwiseCount;
import com.vinsys.hrms.kra.entity.GradeToObjectiveMapping;
import com.vinsys.hrms.kra.entity.HodCycleFinalRating;
import com.vinsys.hrms.kra.entity.HodToDepartmentMap;
import com.vinsys.hrms.kra.entity.HrToDepartment;
import com.vinsys.hrms.kra.entity.HrToHodMap;
import com.vinsys.hrms.kra.entity.KpiFormToRole;
import com.vinsys.hrms.kra.entity.KpiTimeline;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraAggregateScores;
import com.vinsys.hrms.kra.entity.KraCountForHR;
import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraCycleCalender;
import com.vinsys.hrms.kra.entity.KraCycleStatus;
import com.vinsys.hrms.kra.entity.KraCycleType;
import com.vinsys.hrms.kra.entity.KraDetails;
import com.vinsys.hrms.kra.entity.KraDetailsLite;
import com.vinsys.hrms.kra.entity.KraDetailsReport;
import com.vinsys.hrms.kra.entity.KraDisplayStatus;
import com.vinsys.hrms.kra.entity.KraRatingRange;
import com.vinsys.hrms.kra.entity.KraStatusMapping;
import com.vinsys.hrms.kra.entity.KraWf;
import com.vinsys.hrms.kra.entity.KraYear;
import com.vinsys.hrms.kra.entity.OrganizationalKpiListView;
import com.vinsys.hrms.kra.entity.PercentageRatingView;
import com.vinsys.hrms.kra.entity.ViKpiTemplate;
import com.vinsys.hrms.kra.entity.VisionKraDetailsView;
import com.vinsys.hrms.kra.service.IKraService;
import com.vinsys.hrms.kra.util.EKraStatus;
import com.vinsys.hrms.kra.util.KpiHelper;
import com.vinsys.hrms.kra.util.KraHelper;
import com.vinsys.hrms.kra.util.KraTransformUtil;
import com.vinsys.hrms.kra.vo.AiMsEmployeeFeedbackAnalysisResponseVO;
import com.vinsys.hrms.kra.vo.AutoSubmitResponseVo;
import com.vinsys.hrms.kra.vo.CalibrationRequestVo;
import com.vinsys.hrms.kra.vo.CategoryRequestVo;
import com.vinsys.hrms.kra.vo.CycleDefinationRequestVo;
import com.vinsys.hrms.kra.vo.CycleDefinationResponseVo;
import com.vinsys.hrms.kra.vo.CycleResponseVo;
import com.vinsys.hrms.kra.vo.DashboardCountResponse;
import com.vinsys.hrms.kra.vo.DashboardRatingResponseVO;
import com.vinsys.hrms.kra.vo.DashboardResponseVo;
import com.vinsys.hrms.kra.vo.DashboardTimelineVo;
import com.vinsys.hrms.kra.vo.DelegationMappingResponseVO;
import com.vinsys.hrms.kra.vo.DelegationMappingVO;
import com.vinsys.hrms.kra.vo.DelegationRequestVo;
import com.vinsys.hrms.kra.vo.DeleteDelegationMappingVO;
import com.vinsys.hrms.kra.vo.DeleteKraRequestVO;
import com.vinsys.hrms.kra.vo.DepartmentEmpListVo;
import com.vinsys.hrms.kra.vo.DeptwiseCountResponseVO;
import com.vinsys.hrms.kra.vo.DonutCountResponse;
import com.vinsys.hrms.kra.vo.DonutDashboardResponseVo;
import com.vinsys.hrms.kra.vo.DonutDatum;
import com.vinsys.hrms.kra.vo.EmpListVo;
import com.vinsys.hrms.kra.vo.GenerateKPITemplateRequestVo;
import com.vinsys.hrms.kra.vo.GroupedRowVO;
import com.vinsys.hrms.kra.vo.HcdCorrcetionRequest;
import com.vinsys.hrms.kra.vo.HcdCorrectionRequestVo;
import com.vinsys.hrms.kra.vo.HcdCorrectionResponseVo;
import com.vinsys.hrms.kra.vo.HeaderVO;
import com.vinsys.hrms.kra.vo.HodCycleFinalRatingResponseVO;
import com.vinsys.hrms.kra.vo.HodToDepartmentMapListResponseVO;
import com.vinsys.hrms.kra.vo.HodToDepartmentMapVO;
import com.vinsys.hrms.kra.vo.HrToDepartmentVO;
import com.vinsys.hrms.kra.vo.KraCategoryVo;
import com.vinsys.hrms.kra.vo.KraCeoDashboardResponseVO;
import com.vinsys.hrms.kra.vo.KraCycleListResponseVO;
import com.vinsys.hrms.kra.vo.KraCycleRequestVo;
import com.vinsys.hrms.kra.vo.KraCycleResponseVo;
import com.vinsys.hrms.kra.vo.KraCycleTypeVO;
import com.vinsys.hrms.kra.vo.KraDetailsRequestVO;
import com.vinsys.hrms.kra.vo.KraDetailsResponseVO;
import com.vinsys.hrms.kra.vo.KraDetailsVO;
import com.vinsys.hrms.kra.vo.KraListRequestVo;
import com.vinsys.hrms.kra.vo.KraObjectiveVo;
import com.vinsys.hrms.kra.vo.KraResponseVO;
import com.vinsys.hrms.kra.vo.KraSubcategoryVo;
import com.vinsys.hrms.kra.vo.KraYearVo;
import com.vinsys.hrms.kra.vo.ManagerRatingResponseVo;
import com.vinsys.hrms.kra.vo.NewKraResponse;
import com.vinsys.hrms.kra.vo.ObjectiveFeedbackDraftingAssistanceRequestVo;
import com.vinsys.hrms.kra.vo.ObjectiveFeedbackDraftingAssistanceRequestVo.EmployeeDetails;
import com.vinsys.hrms.kra.vo.ObjectiveFeedbackDraftingAssistanceResponseVo;
import com.vinsys.hrms.kra.vo.ObjectiveFeedbackResponseVo;
import com.vinsys.hrms.kra.vo.ObjectiveFeedbackVo;
import com.vinsys.hrms.kra.vo.ObjectiveVo;
import com.vinsys.hrms.kra.vo.OrgKpiDeleteRequestVo;
import com.vinsys.hrms.kra.vo.OrgKpiDeleteVo;
import com.vinsys.hrms.kra.vo.OrgKpiReportResponse;
import com.vinsys.hrms.kra.vo.OrgKpiRequest;
import com.vinsys.hrms.kra.vo.OrgKpiResponseVo;
import com.vinsys.hrms.kra.vo.OrgnizationalKpiVo;
import com.vinsys.hrms.kra.vo.PMSKraMetricVo;
import com.vinsys.hrms.kra.vo.PMSObjectiveVo;
import com.vinsys.hrms.kra.vo.PMSObjectivesRequestVo;
import com.vinsys.hrms.kra.vo.PercentageGroupedRowVo;
import com.vinsys.hrms.kra.vo.PercentageHeaderVO;
import com.vinsys.hrms.kra.vo.PercentageRowVO;
import com.vinsys.hrms.kra.vo.PmsDashboardRequestVo;
import com.vinsys.hrms.kra.vo.PmsDepartmentVo;
import com.vinsys.hrms.kra.vo.PmsKraVo;
import com.vinsys.hrms.kra.vo.PublishResponse;
import com.vinsys.hrms.kra.vo.PublishResponseVo;
import com.vinsys.hrms.kra.vo.RatingPercentageResponseVO;
import com.vinsys.hrms.kra.vo.RejectRequestVo;
import com.vinsys.hrms.kra.vo.RoleWiseCycleDefinationRequestVo;
import com.vinsys.hrms.kra.vo.RoleWiseCycleDefinationResponse;
import com.vinsys.hrms.kra.vo.RowVO;
import com.vinsys.hrms.kra.vo.SelfRatingResponseVo;
import com.vinsys.hrms.kra.vo.SubCategoryRequestVo;
import com.vinsys.hrms.kra.vo.SubCategoryVo;
import com.vinsys.hrms.kra.vo.TargetRequestVo;
import com.vinsys.hrms.kra.vo.TimeLineRequestVO;
import com.vinsys.hrms.kra.vo.TimeLineResponseVO;
import com.vinsys.hrms.kra.vo.TimeLineVo;
import com.vinsys.hrms.logo.entity.Logo;
import com.vinsys.hrms.logo.service.LogoService;
import com.vinsys.hrms.master.dao.ICategoryDao;
import com.vinsys.hrms.master.dao.IGradeDAO;
import com.vinsys.hrms.master.dao.IKpiMetricDAO;
import com.vinsys.hrms.master.dao.IKpiTypeDAO;
import com.vinsys.hrms.master.dao.IKraRatingDAO;
import com.vinsys.hrms.master.dao.IMasterRoleDao;
import com.vinsys.hrms.master.dao.ISubCategoryDao;
import com.vinsys.hrms.master.entity.Category;
import com.vinsys.hrms.master.entity.GradeMaster;
import com.vinsys.hrms.master.entity.KpiMetricMaster;
import com.vinsys.hrms.master.entity.KpiTypeMaster;
import com.vinsys.hrms.master.entity.KraRating;
import com.vinsys.hrms.master.entity.Subcategory;
import com.vinsys.hrms.master.vo.DepartmentVO;
import com.vinsys.hrms.master.vo.GradeMasterVo;
import com.vinsys.hrms.master.vo.HREmailDTO;
import com.vinsys.hrms.master.vo.SelfRatingVo;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.security.service.IAuthorizationService;
import com.vinsys.hrms.services.MasterConfigService;
import com.vinsys.hrms.traveldesk.vo.ExpenseSummaryXLSGenerator;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

/**
 * @author Onkar A.
 *
 */
@Service
public class KraServiceImpl implements IKraService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IAuthorizationService authorizationServiceImpl;
	@Autowired
	IKraRatingDAO ratingDao;

	@Autowired
	IKraYearDao kraYearDao;
	@Autowired
	IKraDao kraDao;
	@Autowired
	KraHelper kraHelper;
	@Autowired
	IKraDetailsDao kraDetailsDao;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IKraWfDao kraWfDao;
	@Autowired
	KraTransformUtil transformUtil;
	@Autowired
	IHRMSEmployeeReportingManager reportingManagerDao;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSEmailTemplateDAO emailTemplateDAO;
	@Autowired
	IHodToDepartmentMap hodToDepartmentMap;
	@Autowired
	IKraDetailsLiteDao kraDetailsLiteDao;

	@Autowired
	IKraCountForHRDAO kraCountDao;

	@Autowired
	IVisionKraViewDao visionKraDao;

	@Autowired
	IKraCycleDAO kraCycleDAO;

	@Autowired
	IKraRatingDAO kraRatingDAO;

	@Autowired
	IKraCalenderDao calenderDao;

	@Autowired
	KraStatusDao kraStatusDao;

	@Autowired
	IMasterRoleDao roleDao;

	@Autowired
	IHodCycleFinalRatingDao hodcycleDao;

	@Autowired
	IKraRatingRangeDao rangeDao;

	@Autowired
	KraStatusMappingDao statusMappingDao;

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	ExpenseSummaryXLSGenerator excelGenerator;

	@Autowired
	EmailTransactionServiceImpl emailService;

	@Autowired
	IHRMSMapOrgDivHrDAO hrmOrgDivHrDAO;

	@Autowired
	IHRMSOrganizationDAO hrIhrmsOrganizationDAO;

	@Autowired
	IDashboardRatingDAO dashboardRating;

	@Autowired
	IHRMSMasterDepartmentDAO masterDepartmentDao;

	@Autowired
	IPercentageRatingDAO percentageRatingDao;

	@Autowired
	IKpiTimelineDao kpiTimelinedao;

	@Autowired
	IHrToHodMapDao hrtohodMapDao;

	@Autowired
	IKraDisplayStatusDao kradisplaystatusDao;

	@Autowired
	IKraYearDao krayeardao;

	@Autowired
	ICategoryDao categoryDAO;

	@Autowired
	ISubCategoryDao subCategoryDao;

	@Autowired
	IKpiMetricDAO kpiMetricDao;

	@Autowired
	IGradeToObjectiveMappingDao gradeObjectiveMapDao;

	@Autowired
	IGradeDAO gradeDao;

	@Autowired
	IGradeToObjectiveMappingDao gradeToObjectiveMappingDao;

	@Autowired
	IKpiMetricDAO metricDao;

	@Autowired
	IKpiTypeDAO kpiTypeDao;

	@Autowired
	MasterConfigService masterConfigService;

	@Autowired
	IKpiTemplateDAO templateDAO;

	@Autowired
	IKraCalenderDao kraCalenderDao;

	@Autowired
	IHRMSCandidateProfessionalDetailDAO candidateprofDAO;

	@Autowired
	IHRMSEmployeeReportingManager reportingmanagerDAO;

	@Autowired
	IEmployeeDesignationDAO designationDAO;

	@Autowired
	KpiHelper kpiHelper;

	@Autowired
	IKraCycleDAO cycleDAO;

	@Autowired
	IDeptwiseCountDAO deptwiseCountDAO;

	@Autowired
	IFuncationHeadToDivisionFunctionDAO funcationHeadToDivisionFunctionDAO;

	@Autowired
	IHrToDepartmentDAO hrtodepartment;

    @Autowired
    IKraAggregateScoresDao kraAggregateScoresDao;
	
	KraServiceImpl kraserviceImpl;
	
	@Autowired
	IKpiFormToRoleDAO kpiFormToRoleDAO;
	 
	@PostConstruct
	void init() {
		kraserviceImpl = this;
	}

	@Value("${max_kra_count}")
	private int kraMaximumCount;
	@Value("${min_kra_count}")
	private int kraMinimumCount;
	@Value("${base.url}")
	private String baseURL;

	@Value("${kra.hr.emp.id}")
	private String kraHrEmpId;

	@Value("${rm.kra.action.enable}")
	private String rmKraActionEnable;

	@Value("${emp.kra.action.enable}")
	private String empKraActionEnable;

	@Value("${pmsaims.api.key:f3c4e83daf6fe67d21fcfc19f585027d}")
	private String pmsAiMsApiKey;
	
	@Value("${pmsaims.baseurl:http://192.168.33.51:8000}")
	private String pmsAiMsBaseUrl;

	@Value("${pmsaims.endpoint.employeedraftingassistance:/employee_feedback}")
	private String pmsAiMsEndPointEmployeeDraftingAssistance;

	@Value("${pmsaims.endpoint.employeefeedbackanalysis:/employee_feedback_analysis}")
	private String pmsAiMsEndPointEmployeeFeedbackAnalysis;

	@Value("${pmsaims.endpoint.managerdraftingassistance:/linemanager_feedback}")
	private String pmsAiMsEndPointManagerDraftingAssistance;

	@Value("${pmsaims.endpoint.managerfeedbackanalysis:/lineManager_feedback_analysis}")
	private String pmsAiMsEndPointManagerFeedbackAnalysis;

	@Value("${pmsaims.endpoint.divisionheadfeedbackanalysis:/functionalHead_feedback_analysis}")
	private String pmsAiMsEndPointDivisionHeadFeedbackAnalysis;

	@Value("${pmsaims.endpoint.hodfeedbackanalysis:/HOD_feedback_analysis}")
	private String pmsAiMsEndPointHodFeedbackAnalysis;

	@Value("${pmsaims.enabled:Y}")
	private String pmsAiMsEnabled;

	@Autowired
	IMasterConfigDAO masterConfigDAO;

	@Autowired
	IHRMSLoginEntityTypeDAO loginEntityTypeDAO;

	@Autowired
	EntityManager em;

	@Autowired
	IHodCycleFinalRatingDao hodCycleFinalRatingDao;

	@Autowired
	private IAuditLogService auditLogService;

	@Autowired
	IEmployeeDepartmentDAO employeeDepartmentDAO;

	@Autowired
	IDelegationMappingDAO delegationMappingDAO;

	@Autowired
	IHRMSLoginDAO loginDAO;

	@Autowired
	IEmployeeDepartmentDAO departmentDAO;

	@Autowired
	IKraCycleHistoryDAO kraCycleHistoryDAO;

	@Autowired
	IKraWfHistoryDAO kraWfHistoryDAO;

	@Autowired
	IKraDetailsHistoryDAO kraDetailsHistoryDAO;

	@Autowired
	IKraHistoryDAO kraHistoryDAO;

	@Autowired
	private IKraCycleTypeDAO kraCycleTypeDAO;

	@Autowired
	LogoService logoService;

	@Autowired
	IMidYearRatingRangeDAO midYearRatingDAO;

	@Autowired
	private IHRMSMasterDivisionDAO divisionDAO;

	@Autowired
	private IHRMSMasterBranchDAO branchDAO;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<KraDetailsResponseVO> saveKra(KraDetailsRequestVO request) throws HRMSException {
		log.info("Inside saveKra method");

		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<KraDetailsResponseVO>();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		// get KRA year
		KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(kraYear)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		MasterRole empRole = roleDao.findByRoleNameOrgIdIsActive(ERole.EMPLOYEE.name(), loggedInEmployee.getOrgId(),
				IHRMSConstants.isActive);
		Kra kra = null;
		if (authorizationServiceImpl.isAuthorizedFunctionName("saveKra", role)) {

			if (!HRMSHelper.isNullOrEmpty(empKraActionEnable)
					&& empKraActionEnable.equalsIgnoreCase(ERecordStatus.N.name())) {

				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));

			}

			// check KRA submitted or not
			kra = kraDao.findByEmployeeIdAndIsActiveAndKraYear(loggedInEmpId, ERecordStatus.Y.name(), kraYear);
			if (!HRMSHelper.isNullOrEmpty(kra)) {
				// check alraedy submitted or not
				if (!(kra.getKraWf().getStatus().equals(EKraStatus.INCOMPLETE.name())
						|| kra.getKraWf().getStatus().equals(EKraStatus.REJECTED.name()))) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1599));
				}
			} else {
				kra = new Kra();
				kra.setCreatedBy(String.valueOf(loggedInEmpId));
				kra.setCreatedDate(new Date());
				kra.setIsActive(ERecordStatus.Y.name());
				kra.setKraYear(kraYear);
				kra.setEmployee(loggedInEmployee);
				kra.setDivision(loggedInEmployee.getCandidate().getCandidateProfessionalDetail().getDivision());
				kra.setDepartment(loggedInEmployee.getCandidate().getCandidateProfessionalDetail().getDepartment());
				kra.setOrganization(loggedInEmployee.getCandidate().getLoginEntity().getOrganization());
				kra.setBranch(loggedInEmployee.getCandidate().getCandidateProfessionalDetail().getBranch());
				kra.setOrgId(loggedInEmployee.getOrgId());
				kra = kraDao.save(kra);
			}

			// input validation
			kraHelper.saveKraDetailsInputValidation(request);

			saveOrSubmitKraDetails(kra, request);
			// update WF status
			if (HRMSHelper.isNullOrEmpty(kra.getKraWf())) {
				addWFStatus(kra, loggedInEmpId, EKraStatus.INCOMPLETE.name(), ERole.EMPLOYEE.name());
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1216));
		log.info("Exit from  saveKra method");

		return response;
	}

	private void saveOrSubmitKraDetails(Kra kra, KraDetailsRequestVO request) throws HRMSException {
		log.info("Inside saveKraDetails method");
		if (!HRMSHelper.isNullOrEmpty(request.getKraDetailsVOList())) {
			List<KraDetails> kraDetailsList = new ArrayList<KraDetails>();
			for (KraDetailsVO kraDetailsVO : request.getKraDetailsVOList()) {

				KraDetails kraDetails = kraDetailsDao.findByIdAndIsActiveAndKra(kraDetailsVO.getId(),
						ERecordStatus.Y.name(), kra);
				if (!HRMSHelper.isNullOrEmpty(kraDetails)) {
					kraDetails.setUpdatedDate(new Date());
					kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));
				} else {
					kraDetails = new KraDetails();
					kraDetails.setCreatedBy(String.valueOf(kra.getEmployee().getId()));
					kraDetails.setCreatedDate(new Date());
					kraDetails.setIsActive(ERecordStatus.Y.name());
				}
				kraDetails.setAchievementPlan(kraDetailsVO.getAchievementPlan());
				kraDetails.setDescription(kraDetailsVO.getDescription());
				kraDetails.setKra(kra);
				kraDetails.setKraDetails(kraDetailsVO.getKraDetails());
				kraDetails.setMeasurementCriteria(kraDetailsVO.getMeasurementCriteria());
				kraDetails.setYear(kra.getKraYear().getYear());
				kraDetails.setWeightage(kraDetailsVO.getWeightage());
				kraDetails.setOrgId(kra.getEmployee().getOrgId());

				kraDetailsList.add(kraDetails);
			}
			// save all records
			kraDetailsDao.saveAll(kraDetailsList);
			long kradetailsCount = kraDetailsDao.countByKraAndIsActive(kra, ERecordStatus.Y.name());
			log.info("kradetailsCount::" + kradetailsCount);
			if (kradetailsCount > kraMaximumCount) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Maximum " + kraMaximumCount + " Kra Allowed");
			}
		}
		log.info("Exit from saveKraDetails method");
	}

	private void addWFStatus(Kra kra, Long loggnedEmpId, String status, String pendingWith) {
		log.info("Inside addWFStatus method");
		log.info("Adding KRA WF status for :", loggnedEmpId);
		KraWf kraWf = kraWfDao.findByKraAndIsActive(kra, IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kraWf)) {
			kraWf.setStatus(status);
			kraWf.setPendingWith(pendingWith);
			kraWf.setUpdatedDate(new Date());
			kraWf.setUpdatedBy(loggnedEmpId.toString());
		} else {
			kraWf = new KraWf();
			kraWf.setKra(kra);
			kraWf.setStatus(status);
			kraWf.setPendingWith(pendingWith);
			kraWf.setCreatedBy(loggnedEmpId.toString());
			kraWf.setCreatedDate(new Date());
			kraWf.setIsActive(IHRMSConstants.isActive);
			kraWf.setOrgId(kra.getEmployee().getOrgId());
		}
		kraWf = kraWfDao.save(kraWf);
		kra.setKraWf(kraWf);
		log.info("Exit from addWFStatus method");
	}

	@Override
	public HRMSBaseResponse<List<KraResponseVO>> getKraList(String roleName, Long deptid, Long empId, String year,
			Long countryId, String displaystatus, Long cycleId, Long gradeId, Pageable pageable) throws HRMSException {
		log.info("Inside getKraList method");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		List<KraResponseVO> kraResponseVOList = null;
		long totalRecord = 0;
		HRMSBaseResponse<List<KraResponseVO>> response = new HRMSBaseResponse<List<KraResponseVO>>();

		if (authorizationServiceImpl.isAuthorizedFunctionName("getKraList", role)) {
			List<Kra> kraList = null;
			if (roleName.equalsIgnoreCase(ERole.EMPLOYEE.name())) {

				if (!HRMSHelper.isNullOrEmpty(year)) {
					kraList = kraDao.findByEmployeeIdAndIsActiveAndYear(loggedInEmpId, ERecordStatus.Y.name(), year,
							pageable);
					totalRecord = kraDao.countByEmployeeIdAndIsActiveAndYear(loggedInEmpId, ERecordStatus.Y.name(),
							year);
				} else {
					kraList = kraDao.findByEmployeeIdAndIsActive(loggedInEmpId, ERecordStatus.Y.name(), pageable);
					totalRecord = kraDao.countByEmployeeIdAndIsActive(loggedInEmpId, ERecordStatus.Y.name());
				}
//			        }
				// convert to VO
				if (!HRMSHelper.isNullOrEmpty(kraList)) {
					kraResponseVOList = transformUtil.convertToKraResponseVO(kraList, roleName);
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}
			} else if (roleName.equalsIgnoreCase(ERole.MANAGER.name())) {
				// get all employee under the logged in manager
				List<Long> employeeIds = kraDao.findByEmployeeByManager(loggedInEmpId, ERecordStatus.Y.name());
				if (!HRMSHelper.isNullOrEmpty(year.trim()) && HRMSHelper.isNullOrEmpty(cycleId)) {
					if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(gradeId)) {
						GradeMaster grades = gradeDao.getById(gradeId);
						String grade = grades.getGradeDescription();
						kraList = kraDao.findByEmployeeByManagerAndYearAndDeptAndGrade(loggedInEmpId,
								ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), deptid, grade.trim(),
								pageable);
						totalRecord = kraDao.countByEmployeeByManagerAndYearAndDeptAndGrade(loggedInEmpId,
								ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), deptid,
								grade.trim());
					} else if (!HRMSHelper.isNullOrEmpty(deptid)) {
						kraList = kraDao.findByEmployeeByManagerAndYearAndDept(loggedInEmpId, ERecordStatus.Y.name(),
								EKraStatus.INCOMPLETE.name(), year.trim(), deptid, pageable);
						totalRecord = kraDao.countByEmployeeByManagerAndYearAndDept(loggedInEmpId,
								ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), deptid);
					} else if (!HRMSHelper.isNullOrEmpty(gradeId)) {
						GradeMaster grades = gradeDao.getById(gradeId);
						String grade = grades.getGradeDescription();
						kraList = kraDao.findByEmployeeByManagerAndYearAndGrade(loggedInEmpId, ERecordStatus.Y.name(),
								EKraStatus.INCOMPLETE.name(), year.trim(), grade.trim(), pageable);
						totalRecord = kraDao.countByEmployeeByManagerAndYearAndGrade(loggedInEmpId,
								ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), grade.trim());
					} else {
						kraList = kraDao.findByEmployeeByMangerAndYear(loggedInEmpId, ERecordStatus.Y.name(),
								EKraStatus.INCOMPLETE.name(), year.trim(), pageable);
						totalRecord = kraDao.countByEmployeeByMangerAndYear(loggedInEmpId, ERecordStatus.Y.name(),
								EKraStatus.INCOMPLETE.name(), year.trim());
					}

				} else if (!HRMSHelper.isNullOrEmpty(cycleId) && HRMSHelper.isNullOrEmpty(year.trim())) {
					KraCycle kraCycle = kraCycleDAO.getById(cycleId);
					String cycle = kraCycle.getCycleName();
					if (cycle.equalsIgnoreCase(IHRMSConstants.KPI_SUBMISSION)) {
						if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(gradeId)) {
							GradeMaster grades = gradeDao.getById(gradeId);
							String grade = grades.getGradeDescription();
							kraList = kraDao.findByEmployeeByManagerAndCycleAndDeptAndGradeByKpisubmission(
									loggedInEmpId, ERecordStatus.Y.name(), ERole.MANAGER.name(), cycle.trim(), deptid,
									grade.trim(), pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndCycleAndDeptAndGradeByKpisubmission(
									loggedInEmpId, ERecordStatus.Y.name(), ERole.MANAGER.name(), cycle.trim(), deptid,
									grade.trim());
						} else if (!HRMSHelper.isNullOrEmpty(deptid)) {
							kraList = kraDao.findByEmployeeByManagerAndCycleAndDeptByKpisubmission(loggedInEmpId,
									ERecordStatus.Y.name(), ERole.MANAGER.name(), cycle.trim(), deptid, pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndCycleAndDeptByKpisubmission(loggedInEmpId,
									ERecordStatus.Y.name(), ERole.MANAGER.name(), cycle.trim(), deptid);
						} else if (!HRMSHelper.isNullOrEmpty(gradeId)) {
							GradeMaster grades = gradeDao.getById(gradeId);
							String grade = grades.getGradeDescription();
							kraList = kraDao.findByEmployeeByManagerAndCycleAndGradeByKpisubmission(loggedInEmpId,
									ERecordStatus.Y.name(), ERole.MANAGER.name(), cycle.trim(), grade.trim(), pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndCycleAndGradeByKpisubmission(loggedInEmpId,
									ERecordStatus.Y.name(), ERole.MANAGER.name(), cycle.trim(), grade.trim());
						} else {

							kraList = kraDao.findByEmployeeByMangerAndCycleByKpisubmission(loggedInEmpId,
									ERecordStatus.Y.name(), ERole.MANAGER.name(), cycle.trim(), pageable);
							totalRecord = kraDao.countByEmployeeByMangerAndCycleByKpisubmission(loggedInEmpId,
									ERecordStatus.Y.name(), ERole.MANAGER.name(), cycle.trim());
						}
					} else {
						if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(gradeId)) {
							GradeMaster grades = gradeDao.getById(gradeId);
							String grade = grades.getGradeDescription();
							kraList = kraDao.findByEmployeeByManagerAndCycleAndDeptAndGrade(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), cycle.trim(), deptid,
									grade.trim(), pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndCycleAndDeptAndGrade(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), cycle.trim(), deptid,
									grade.trim());
						} else if (!HRMSHelper.isNullOrEmpty(deptid)) {
							kraList = kraDao.findByEmployeeByManagerAndCycleAndDept(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), cycle.trim(), deptid,
									pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndCycleAndDept(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), cycle.trim(), deptid);
						} else if (!HRMSHelper.isNullOrEmpty(gradeId)) {
							GradeMaster grades = gradeDao.getById(gradeId);
							String grade = grades.getGradeDescription();
							kraList = kraDao.findByEmployeeByManagerAndCycleAndGrade(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), cycle.trim(), grade.trim(),
									pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndCycleAndGrade(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), cycle.trim(), grade.trim());
						} else {

							kraList = kraDao.findByEmployeeByMangerAndCycle(loggedInEmpId, ERecordStatus.Y.name(),
									EKraStatus.INCOMPLETE.name(), cycle.trim(), pageable);
							totalRecord = kraDao.countByEmployeeByMangerAndCycle(loggedInEmpId, ERecordStatus.Y.name(),
									EKraStatus.INCOMPLETE.name(), cycle.trim());
						}
					}
				} else if (!HRMSHelper.isNullOrEmpty(year.trim()) && !HRMSHelper.isNullOrEmpty(cycleId)) {
					KraCycle kraCycle = kraCycleDAO.getById(cycleId);
					String cycle = kraCycle.getCycleName();
					if (cycle.equalsIgnoreCase(IHRMSConstants.KPI_SUBMISSION)) {
						if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(gradeId)) {
							GradeMaster grades = gradeDao.getById(gradeId);
							String grade = grades.getGradeDescription();
							kraList = kraDao.findByEmployeeByManagerAndYearAndCycleAndDeptAndGradeByKpisubmission(
									loggedInEmpId, ERecordStatus.Y.name(), ERole.MANAGER.name(), year.trim(),
									cycle.trim(), deptid, grade.trim(), pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndYearAndCycleAndDeptAndGradeByKpisubmission(
									loggedInEmpId, ERecordStatus.Y.name(), ERole.MANAGER.name(), year.trim(),
									cycle.trim(), deptid, grade.trim());
						} else if (!HRMSHelper.isNullOrEmpty(deptid)) {
							kraList = kraDao.findByEmployeeByManagerAndYearAndCycleAndDeptByKpisubmission(loggedInEmpId,
									ERecordStatus.Y.name(), ERole.MANAGER.name(), year.trim(), cycle.trim(), deptid,
									pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndYearAndCycleAndDeptByKpisubmission(
									loggedInEmpId, ERecordStatus.Y.name(), ERole.MANAGER.name(), year.trim(),
									cycle.trim(), deptid);
						} else if (!HRMSHelper.isNullOrEmpty(gradeId)) {
							GradeMaster grades = gradeDao.getById(gradeId);
							String grade = grades.getGradeDescription();
							kraList = kraDao.findByEmployeeByManagerAndYearAndCycleAndGradeByKpisubmission(
									loggedInEmpId, ERecordStatus.Y.name(), ERole.MANAGER.name(), year.trim(),
									cycle.trim(), grade.trim(), pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndYearAndCycleAndGradeByKpisubmission(
									loggedInEmpId, ERecordStatus.Y.name(), ERole.MANAGER.name(), year.trim(),
									cycle.trim(), grade.trim());
						} else {
							kraList = kraDao.findByEmployeeByMangerAndYearAndCycleByKpisubmission(loggedInEmpId,
									ERecordStatus.Y.name(), ERole.MANAGER.name(), year.trim(), cycle.trim(), pageable);
							totalRecord = kraDao.countByEmployeeByMangerAndYearAndCycleByKpisubmission(loggedInEmpId,
									ERecordStatus.Y.name(), ERole.MANAGER.name(), year.trim(), cycle.trim());
						}
					} else {
						if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(gradeId)) {
							GradeMaster grades = gradeDao.getById(gradeId);
							String grade = grades.getGradeDescription();
							kraList = kraDao.findByEmployeeByManagerAndYearAndCycleAndDeptAndGrade(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), cycle.trim(),
									deptid, grade.trim(), pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndYearAndCycleAndDeptAndGrade(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), cycle.trim(),
									deptid, grade.trim());
						} else if (!HRMSHelper.isNullOrEmpty(deptid)) {
							kraList = kraDao.findByEmployeeByManagerAndYearAndCycleAndDept(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), cycle.trim(),
									deptid, pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndYearAndCycleAndDept(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), cycle.trim(),
									deptid);
						} else if (!HRMSHelper.isNullOrEmpty(gradeId)) {
							GradeMaster grades = gradeDao.getById(gradeId);
							String grade = grades.getGradeDescription();
							kraList = kraDao.findByEmployeeByManagerAndYearAndCycleAndGrade(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), cycle.trim(),
									grade.trim(), pageable);
							totalRecord = kraDao.countByEmployeeByManagerAndYearAndCycleAndGrade(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), cycle.trim(),
									grade.trim());
						} else {
							kraList = kraDao.findByEmployeeByMangerAndYearAndCycle(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), cycle.trim(),
									pageable);
							totalRecord = kraDao.countByEmployeeByMangerAndYearAndCycle(loggedInEmpId,
									ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), year.trim(), cycle.trim());
						}
					}
				} else if (HRMSHelper.isNullOrEmpty(year.trim()) && HRMSHelper.isNullOrEmpty(cycleId)) {
					if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(gradeId)) {
						GradeMaster grades = gradeDao.getById(gradeId);
						String grade = grades.getGradeDescription();
						kraList = kraDao.findByEmployeeByManagerAndDeptAndGrade(loggedInEmpId, ERecordStatus.Y.name(),
								EKraStatus.INCOMPLETE.name(), deptid, grade.trim(), pageable);
						totalRecord = kraDao.countByEmployeeByManagerAndDeptAndGrade(loggedInEmpId,
								ERecordStatus.Y.name(), EKraStatus.INCOMPLETE.name(), deptid, grade.trim());
					} else if (!HRMSHelper.isNullOrEmpty(deptid)) {
						kraList = kraDao.findByEmployeeByManagerAndDept(loggedInEmpId, ERecordStatus.Y.name(),
								EKraStatus.INCOMPLETE.name(), deptid, pageable);
						totalRecord = kraDao.countByEmployeeByManagerAndDept(loggedInEmpId, ERecordStatus.Y.name(),
								EKraStatus.INCOMPLETE.name(), deptid);
					} else if (!HRMSHelper.isNullOrEmpty(gradeId)) {
						GradeMaster grades = gradeDao.getById(gradeId);
						String grade = grades.getGradeDescription();
						kraList = kraDao.findByEmployeeByManagerAndGrade(loggedInEmpId, ERecordStatus.Y.name(),
								EKraStatus.INCOMPLETE.name(), grade.trim(), pageable);
						totalRecord = kraDao.countByEmployeeByManagerAndGrade(loggedInEmpId, ERecordStatus.Y.name(),
								EKraStatus.INCOMPLETE.name(), grade.trim());
					} else {
						kraList = kraDao.findByEmployeeByManger(loggedInEmpId, ERecordStatus.Y.name(),
								EKraStatus.INCOMPLETE.name(), pageable);
						totalRecord = kraDao.countByEmployeeByManger(loggedInEmpId, ERecordStatus.Y.name(),
								EKraStatus.INCOMPLETE.name());
					}
				}
				// convert to VO
				if (!HRMSHelper.isNullOrEmpty(kraList)) {
					kraResponseVOList = transformUtil.convertToKraResponseVO(kraList, roleName);
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}

			} else if (roleName.equalsIgnoreCase(ERole.HR.name())
					|| roleName.equalsIgnoreCase(ERole.PERFORMANCE_MANAGER.name())) {

				List<KraDetailsLite> kraDetailsLiteList = null;

				if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(empId)
						&& !HRMSHelper.isNullOrEmpty(countryId)) {
					if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						if (!HRMSHelper.isNullOrEmpty(year.trim())) {
							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndEmpAndCountryIdAndYear(deptid,
									empId, countryId, status, year.trim(), pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndEmpAndCountryIdAndYear(deptid,
									empId, countryId, status, year.trim());
						} else {

							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndEmpAndCountryId(deptid, empId,
									countryId, status, pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndEmpAndCountryId(deptid, empId,
									countryId, status);
						}
					} else {
						kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndEmpAndCountryId(deptid, empId,
								countryId, pageable);
						totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndEmpAndCountryId(deptid, empId,
								countryId);
					}
				} else if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(countryId)) {
					if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						if (!HRMSHelper.isNullOrEmpty(year.trim())) {
							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndCountryIdAndYear(deptid,
									countryId, status, year.trim(), pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndCountryIdAndYear(deptid,
									countryId, status, year.trim());

						} else {

							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndCountryId(deptid, countryId,
									status, pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndCountryId(deptid, countryId,
									status);
						}
					} else {
						kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndCountryId(deptid, countryId,
								pageable);
						totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndCountryId(deptid, countryId);
					}
				} else if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(empId)) {
					if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						if (!HRMSHelper.isNullOrEmpty(year.trim())) {
							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndEmpAndStatusAndYear(deptid,
									empId, status, year.trim(), pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndEmpAndStatusAndYear(deptid,
									empId, status, year.trim());
						} else {
							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndEmpAndStatus(deptid, empId,
									status, pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndEmpAndStatus(deptid, empId,
									status);
						}
					} else {
						kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndEmp(deptid, empId, pageable);
						totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndEmp(deptid, empId);
					}
				} else if (!HRMSHelper.isNullOrEmpty(deptid)) {
					if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						if (!HRMSHelper.isNullOrEmpty(year.trim())) {
							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndStatusAndYear(deptid, status,
									year.trim(), pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndStatusAndYear(deptid, status,
									year.trim());

						} else {

							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndStatus(deptid, status,
									pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndStatus(deptid, status);
						}

					} else {
						kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDept(deptid, pageable);
						totalRecord = kraDetailsLiteDao.getKraDetailsCountByDept(deptid);
					}
				} else if (!HRMSHelper.isNullOrEmpty(empId)) {
					if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						if (!HRMSHelper.isNullOrEmpty(year.trim())) {
							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByEmpAndStatusAndYear(empId, status,
									year.trim(), pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByEmpAndStatusAndYear(empId, status,
									year.trim());
						} else {

							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByEmpAndStatus(empId, status, pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByEmpAndStatus(empId, status);
						}
					} else {
						kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByEmp(empId, pageable);
						totalRecord = kraDetailsLiteDao.getKraDetailsCountByEmp(empId);
					}
				} else if (!HRMSHelper.isNullOrEmpty(countryId)) {
					if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						if (!HRMSHelper.isNullOrEmpty(year.trim())) {
							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByCountryIdAndStatusAndYear(countryId,
									status, year.trim(), pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByCountryIdAndStatusAndYear(countryId,
									status, year.trim());
						} else {
							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByCountryIdAndStatus(countryId, status,
									pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByCountryIdAndStatus(countryId, status);
						}
					} else {
						kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByCountryId(countryId, pageable);
						totalRecord = kraDetailsLiteDao.getKraDetailsCountByCountryId(countryId);
					}
				} else if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
					KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
					String status = kradisplaystatus.getStatus();
					if (!HRMSHelper.isNullOrEmpty(year.trim())) {
						kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByStatusAndYear(status, year.trim(),
								pageable);
						totalRecord = kraDetailsLiteDao.getKraDetailsCountByStatusAndYear(status, year.trim());
					} else {
						kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByStatus(status, pageable);
						totalRecord = kraDetailsLiteDao.getKraDetailsCountByStatus(status);

					}
				}

				else if (!HRMSHelper.isNullOrEmpty(year)) {
					if (!HRMSHelper.isNullOrEmpty(countryId)) {
						if (!HRMSHelper.isNullOrEmpty(deptid)) {
							if (!HRMSHelper.isNullOrEmpty(empId)) {
								kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndEmpAndCountryIdAndYear(
										deptid, empId, countryId, year.trim(), pageable);
								totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndEmpAndCountryIdAndYear(
										deptid, empId, countryId, year.trim());

							} else {
								kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByDeptAndCountryIdAndYear(deptid,
										countryId, year.trim(), pageable);
								totalRecord = kraDetailsLiteDao.getKraDetailsCountByDeptAndCountryIdAndYear(deptid,
										countryId, year.trim());

							}
						}

						else {
							kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByCountryIdAndYear(countryId,
									year.trim(), pageable);
							totalRecord = kraDetailsLiteDao.getKraDetailsCountByCountryIdAndYear(countryId,
									year.trim());
						}
					} else {
						kraDetailsLiteList = kraDetailsLiteDao.getKraDetailsByYear(year.trim(), pageable);
						totalRecord = kraDetailsLiteDao.getKraDetailsCountByYear(year.trim());
					}

				}

				else if (HRMSHelper.isNullOrEmpty(deptid) && HRMSHelper.isNullOrEmpty(empId)
						&& HRMSHelper.isNullOrEmpty(countryId) && HRMSHelper.isNullOrEmpty(year.trim())) {
					kraDetailsLiteList = kraDetailsLiteDao.getKraDetails(pageable);
					totalRecord = kraDetailsLiteDao.getKraDetailsCount();
				}

				// convert to VO
				if (!HRMSHelper.isNullOrEmpty(kraDetailsLiteList)) {
					kraResponseVOList = transformUtil.convertToKraResponseVOforHR(kraDetailsLiteList, roleName,
							loggedInEmpId);
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}

			} else if (roleName.equalsIgnoreCase(ERole.HOD.name())) {

				// get mapped department for HOD
				List<HodToDepartmentMap> departmentMapList = hodToDepartmentMap
						.findByEmployeeIdAndIsActive(loggedInEmpId, ERecordStatus.Y.name());

				if (!HRMSHelper.isNullOrEmpty(departmentMapList)) {

					List<Long> divisions = hodToDepartmentMap.getDivisionByEmployeeAndIsActive(loggedInEmpId,
							ERecordStatus.Y.name());

					List<Long> departments = hodToDepartmentMap.getDepartmentByEmployeeAndIsActive(loggedInEmpId,
							ERecordStatus.Y.name());
					if (!HRMSHelper.isNullOrEmpty(year) && !HRMSHelper.isNullOrEmpty(cycleId)
							&& !HRMSHelper.isNullOrEmpty(gradeId) && !HRMSHelper.isNullOrEmpty(displaystatus)) {
						// All filters applied (Year, Cycle, Grade, Status)
						KraCycle kraCycle = kraCycleDAO.getById(cycleId);
						String cycle = kraCycle.getCycleName();
						GradeMaster grades = gradeDao.getById(gradeId);
						String grade = grades.getGradeDescription();
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						kraList = kraDao.findByDeptAndYearAndCycleAndGradeAndStatus(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								year.trim(), cycle.trim(), grade.trim(), status, pageable);
						totalRecord = kraDao.countByDeptAndYearAndCycleAndGradeAndStatus(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								year.trim(), cycle.trim(), grade.trim(), status);
					} else if (!HRMSHelper.isNullOrEmpty(year) && !HRMSHelper.isNullOrEmpty(cycleId)
							&& !HRMSHelper.isNullOrEmpty(gradeId)) {
						// Year, Cycle & Grade Filter
						KraCycle kraCycle = kraCycleDAO.getById(cycleId);
						String cycle = kraCycle.getCycleName();
						GradeMaster grades = gradeDao.getById(gradeId);
						String grade = grades.getGradeDescription();
						kraList = kraDao.findByDeptAndYearAndCycleAndGrade(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								year.trim(), cycle.trim(), grade.trim(), pageable);
						totalRecord = kraDao.countByDeptAndYearAndCycleAndGrade(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								year.trim(), cycle.trim(), grade.trim());
					} else if (!HRMSHelper.isNullOrEmpty(year) && !HRMSHelper.isNullOrEmpty(cycleId)
							&& !HRMSHelper.isNullOrEmpty(displaystatus)) {
						// Year, Cycle & Status Filter
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						KraCycle kraCycle = kraCycleDAO.getById(cycleId);
						String cycle = kraCycle.getCycleName();
						kraList = kraDao.findByDeptAndYearAndCycleAndStatus(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								year.trim(), cycle.trim(), status, pageable);
						totalRecord = kraDao.countByDeptAndYearAndCycleAndStatus(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								year.trim(), cycle.trim(), status);
					} else if (!HRMSHelper.isNullOrEmpty(year) && !HRMSHelper.isNullOrEmpty(gradeId)
							&& !HRMSHelper.isNullOrEmpty(displaystatus)) {
						// Year, Grade & Status Filter
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						GradeMaster grades = gradeDao.getById(gradeId);
						String grade = grades.getGradeDescription();
						kraList = kraDao.findByDeptAndYearAndGradeAndStatus(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								year.trim(), grade.trim(), status, pageable);
						totalRecord = kraDao.countByDeptAndYearAndGradeAndStatus(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								year.trim(), grade.trim(), status);
					} else if (!HRMSHelper.isNullOrEmpty(year) && !HRMSHelper.isNullOrEmpty(cycleId)) {
						// Year & Cycle Filter
						KraCycle kraCycle = kraCycleDAO.getById(cycleId);
						String cycle = kraCycle.getCycleName();
						kraList = kraDao.findByDeptAndYearAndCycle(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), year.trim(), cycle.trim(),
								pageable);
						totalRecord = kraDao.countByDeptAndYearAndCycle(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								year.trim(), cycle.trim());
					} else if (!HRMSHelper.isNullOrEmpty(year) && !HRMSHelper.isNullOrEmpty(gradeId)) {
						// Year & Grade Filter
						GradeMaster grades = gradeDao.getById(gradeId);
						String grade = grades.getGradeDescription();
						kraList = kraDao.findByDeptAndYearAndGrade(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), year.trim(), grade.trim(),
								pageable);
						totalRecord = kraDao.countByDeptAndYearAndGrade(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								year.trim(), grade.trim());
					} else if (!HRMSHelper.isNullOrEmpty(year) && !HRMSHelper.isNullOrEmpty(displaystatus)) {
						// Year & Status Filter
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						kraList = kraDao.findByDeptAndYearAndStatus(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), year.trim(), status, pageable);
						totalRecord = kraDao.countByDeptAndYearAndStatus(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								year.trim(), status);
					} else if (!HRMSHelper.isNullOrEmpty(cycleId) && !HRMSHelper.isNullOrEmpty(gradeId)) {
						// Cycle & Grade Filter
						KraCycle kraCycle = kraCycleDAO.getById(cycleId);
						String cycle = kraCycle.getCycleName();
						GradeMaster grades = gradeDao.getById(gradeId);
						String grade = grades.getGradeDescription();
						kraList = kraDao.findByDeptAndCycleAndGrade(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), cycle.trim(), grade.trim(),
								pageable);
						totalRecord = kraDao.countByDeptAndCycleAndGrade(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								cycle.trim(), grade.trim());
					} else if (!HRMSHelper.isNullOrEmpty(cycleId) && !HRMSHelper.isNullOrEmpty(displaystatus)) {
						// Cycle & Status Filter
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						KraCycle kraCycle = kraCycleDAO.getById(cycleId);
						String cycle = kraCycle.getCycleName();
						kraList = kraDao.findByDeptAndCycleAndStatus(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), cycle.trim(), status, pageable);
						totalRecord = kraDao.countByDeptAndCycleAndStatus(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								cycle.trim(), status);
					} else if (!HRMSHelper.isNullOrEmpty(gradeId) && !HRMSHelper.isNullOrEmpty(displaystatus)) {
						// Grade & Status Filter
						GradeMaster grades = gradeDao.getById(gradeId);
						String grade = grades.getGradeDescription();
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						kraList = kraDao.findByDeptAndGradeAndStatus(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), grade.trim(), status, pageable);
						totalRecord = kraDao.countByDeptAndGradeAndStatus(departments.toArray(),
								EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
								EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(),
								grade.trim(), status);
					} else if (!HRMSHelper.isNullOrEmpty(year)) {
						// Only Year Filter
						kraList = kraDao.findByDeptAndYear(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), year.trim(), pageable);
						totalRecord = kraDao.countByDeptAndYear(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), year.trim());
					} else if (!HRMSHelper.isNullOrEmpty(cycleId)) {
						// Only Cycle Filter
						KraCycle kraCycle = kraCycleDAO.getById(cycleId);
						String cycle = kraCycle.getCycleName();
						kraList = kraDao.findByDeptAndCycle(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), cycle.trim(), pageable);
						totalRecord = kraDao.countByDeptAndCycle(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), cycle.trim());
					} else if (!HRMSHelper.isNullOrEmpty(gradeId)) {
						// Only Grade Filter
						GradeMaster grades = gradeDao.getById(gradeId);
						String grade = grades.getGradeDescription();
						kraList = kraDao.findByDeptAndGrade(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), grade.trim(), pageable);
						totalRecord = kraDao.countByDeptAndGrade(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), grade.trim());
					} else if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
						// Only Status Filter
						KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
						String status = kradisplaystatus.getStatus();
						kraList = kraDao.findByDeptAndStatus(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), status, pageable);
						totalRecord = kraDao.countByDeptAndStatus(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), status);
					} else {
						kraList = kraDao.findByDeptIdInAndStatus(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), pageable);
						totalRecord = kraDao.countByDeptIdInAndStatus(departments.toArray(), EKraStatus.APPROVED.name(),
								EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(), EKraStatus.COMPLETED.name(),
								EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name());
					}

				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}

				// convert to VO
				if (!HRMSHelper.isNullOrEmpty(kraList)) {
					kraResponseVOList = transformUtil.convertToKraResponseVO(kraList, roleName);
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseBody(kraResponseVOList);
		response.setTotalRecord(totalRecord);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getKraList method");

		return response;
	}

	@Override
	public HRMSBaseResponse<KraDetailsResponseVO> getKraById(String kraid) throws HRMSException {
		log.info("Inside getKraById method");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<KraDetailsResponseVO>();
		KraDetailsResponseVO kraDetailsResponseVO = new KraDetailsResponseVO();
		if (authorizationServiceImpl.isAuthorizedFunctionName("getKraById", role)) {
			if (HRMSHelper.isNumber(kraid)) {
				Kra kra = kraDao.findByIdAndIsActive(Long.valueOf(kraid), ERecordStatus.Y.name());
				// if want see self Kra details
				if (!HRMSHelper.isNullOrEmpty(kra)) {
					List<KraDetails> kraDetailsList = null;
					if (loggedInEmpId.equals(kra.getEmployee().getId())) {
						kraDetailsList = kraDetailsDao.findByKraAndIsActiveOrderByIdAsc(kra, ERecordStatus.Y.name());
					} else if (HRMSHelper.isRolePresent(role, ERole.MANAGER.name())
							|| HRMSHelper.isRolePresent(role, ERole.HR.name())
							|| HRMSHelper.isRolePresent(role, ERole.HOD.name())
							|| HRMSHelper.isRolePresent(role, ERole.PERFORMANCE_MANAGER.name())) {
						EmployeeReportingManager reportingManager = reportingManagerDao
								.findByEmployeeAndIsActive(kra.getEmployee().getId(), ERecordStatus.Y.name());
						if (!HRMSHelper.isNullOrEmpty(reportingManager)
								&& reportingManager.getReporingManager().getId().equals(loggedInEmpId)) {
							kraDetailsList = kraDetailsDao.findByKraAndIsActiveOrderByIdAsc(kra,
									ERecordStatus.Y.name());
						} else {
							if (HRMSHelper.isRolePresent(role, ERole.HR.name())
									|| HRMSHelper.isRolePresent(role, ERole.PERFORMANCE_MANAGER.name())) {
								kraDetailsList = kraDetailsDao.findByKraAndIsActiveOrderByIdAsc(kra,
										ERecordStatus.Y.name());
							} else if (HRMSHelper.isRolePresent(role, ERole.HOD.name())) {
								List<Long> hodDepartments = hodToDepartmentMap
										.getDepartmentByEmployeeAndIsActive(loggedInEmpId, ERecordStatus.Y.name());

								if (hodDepartments.contains(kra.getDepartment().getId())) {
									kraDetailsList = kraDetailsDao.findByKraAndIsActiveOrderByIdAsc(kra,
											ERecordStatus.Y.name());
								} else {
									throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
								}

							} else {
								throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
							}
						}

					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
					}
					// convert to vo
					if (!HRMSHelper.isNullOrEmpty(kraDetailsList)) {
						kraDetailsResponseVO = transformUtil.convertToKraDetailsResponseVO(kraDetailsList);
						kraDetailsResponseVO.setPendingWith(kra.getKraWf().getPendingWith());
						kraDetailsResponseVO.setStatus(kra.getKraWf().getStatus());
						kraDetailsResponseVO.setEmployeeName(kra.getEmployee().getCandidate().getFirstName() + " "
								+ kra.getEmployee().getCandidate().getLastName());
						kraDetailsResponseVO.setKraId(kra.getId());

						// check employee form submitted or not
						if (kra.getKraWf().getStatus().equals(EKraStatus.INCOMPLETE.name())
								|| kra.getKraWf().getStatus().equals(EKraStatus.REJECTED.name())) {
							kraDetailsResponseVO.setIsEmployeeKraSubmitted(ERecordStatus.N.name());
							if (kra.getKraWf().getStatus().equals(EKraStatus.REJECTED.name())) {
								kraDetailsResponseVO.setActionByRM(ERecordStatus.Y.name());
							} else {
								kraDetailsResponseVO.setActionByRM(ERecordStatus.N.name());
							}
						} else {
							kraDetailsResponseVO.setIsEmployeeKraSubmitted(ERecordStatus.Y.name());
							if (kra.getKraWf().getStatus().equals(EKraStatus.INPROCESS.name())) {
								kraDetailsResponseVO.setActionByRM(ERecordStatus.N.name());
							} else {
								kraDetailsResponseVO.setActionByRM(ERecordStatus.Y.name());
							}
						}

						KraCycle activeCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
						if (!HRMSHelper.isNullOrEmpty(activeCycle)) {
							kraDetailsResponseVO.setActiveCycleName(activeCycle.getCycleName());
						}

						KraCycle formCycle = kra.getCycleId();
						if (!HRMSHelper.isNullOrEmpty(formCycle)) {
							KraCycle formCycleName = kraCycleDAO.findByKraCycle(formCycle.getId());
							kraDetailsResponseVO.setKpiFormCycle(formCycleName.getCycleName());
						}

					} else {
						throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
					}
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseBody(kraDetailsResponseVO);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		log.info("Exit from getKraById method");
		return response;
	}

	@Override
	public Kra getCurrentYearKra() throws HRMSException {
		log.info("Inside getCurrentYearKra method.");
		KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Kra kra = kraDao.findByEmployeeIdAndIsActiveAndKraYear(loggedInEmpId, ERecordStatus.Y.name(), kraYear);
		KraWf kraWf = kraWfDao.findByKraAndIsActive(kra, IHRMSConstants.isActive);
		kra.setKraWf(kraWf);
		log.info("Exit from  getCurrentYearKra method.");
		return kra;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<KraDetailsResponseVO> submitKra(KraDetailsRequestVO request) throws HRMSException {
		log.info("Inside saveKra method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<KraDetailsResponseVO>();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		// get KRA year
		KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(kraYear)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		Kra kra = null;
		if (authorizationServiceImpl.isAuthorizedFunctionName("submitKra", role)) {

			if (!HRMSHelper.isNullOrEmpty(rmKraActionEnable)
					&& rmKraActionEnable.equalsIgnoreCase(ERecordStatus.N.name())) {

				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));

			}

			// check KRA submitted or not
			kra = kraDao.findByEmployeeIdAndIsActiveAndKraYear(loggedInEmpId, ERecordStatus.Y.name(), kraYear);
			if (!HRMSHelper.isNullOrEmpty(kra)) {
				// check already submitted or not
				if (!(kra.getKraWf().getStatus().equals(EKraStatus.INCOMPLETE.name())
						|| kra.getKraWf().getStatus().equals(EKraStatus.REJECTED.name()))) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1599));
				}
			} else {
				kra = new Kra();
				kra.setCreatedBy(String.valueOf(loggedInEmpId));
				kra.setCreatedDate(new Date());
				kra.setIsActive(ERecordStatus.Y.name());
				kra.setKraYear(kraYear);
				kra.setEmployee(loggedInEmployee);
				kra.setDivision(loggedInEmployee.getCandidate().getCandidateProfessionalDetail().getDivision());
				kra.setDepartment(loggedInEmployee.getCandidate().getCandidateProfessionalDetail().getDepartment());
				kra.setOrganization(loggedInEmployee.getCandidate().getLoginEntity().getOrganization());
				kra.setBranch(loggedInEmployee.getCandidate().getCandidateProfessionalDetail().getBranch());
				kra = kraDao.save(kra);
			}
			// input validation
			kraHelper.submitKraDetailsInputValidation(request);
			saveOrSubmitKraDetails(kra, request);
			// update WF status
			addWFStatus(kra, loggedInEmpId, EKraStatus.INPROCESS.name(), ERole.MANAGER.name());

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee employee = employeeDAO.findActiveEmployeeById(loggedInEmpId, IHRMSConstants.isActive);
		Employee rm = employee.getCandidate().getEmployee().getEmployeeReportingManager().getReporingManager();
		String tdMailId = rm.getOfficialEmailId();
		EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
				IHRMSConstants.Employee_Submits_KRA, IHRMSConstants.isActive,
				employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
				employee.getCandidate().getLoginEntity().getOrganization());
		String ccMailId = null;
		mailForSubmitRequestByEmp(kra, loggedInEmpId, template, tdMailId, ccMailId);

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1217));
		log.info("Exit from  saveKra method");

		return response;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<KraDetailsResponseVO> deleteKraPoint(DeleteKraRequestVO request) throws HRMSException {
		log.info("Inside deleteKraPoint method");
		HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<KraDetailsResponseVO>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (authorizationServiceImpl.isAuthorizedFunctionName("deleteKraPoint", role)) {
			kraHelper.deleteKraPointInputValidation(request);
			// get KRA by KRA id
			Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(kra)) {
				if (!(kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.INCOMPLETE.name())
						|| kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.REJECTED.name()))) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

			if (loggedInEmpId.equals(kra.getEmployee().getId())) {
				KraDetails kraDetails = kraDetailsDao.findByIdAndIsActiveAndKra(request.getKraDetailsId(),
						ERecordStatus.Y.name(), kra);
				if (!HRMSHelper.isNullOrEmpty(kraDetails)) {
					kraDetails.setIsActive(ERecordStatus.N.name());
					kraDetails.setUpdatedBy(String.valueOf(loggedInEmpId));
					kraDetails.setUpdatedDate(new Date());
					kraDetailsDao.save(kraDetails);
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);

		log.info("Exit from deleteKraPoint method");
		return response;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<KraDetailsResponseVO> approveKra(KraDetailsRequestVO request) throws HRMSException {
		log.info("Inside approveKra method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<KraDetailsResponseVO>();

		if (authorizationServiceImpl.isAuthorizedFunctionName("approveKra", role)) {
			kraHelper.approveKraInputValidation(request);
			Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(kra)) {
				Long reportingmanagerId = reportingManagerDao.findReportingManagerIdByEmployeeId(kra.getEmployeeId());

				List<KraDetails> kraDetailsFromDb = kraDetailsDao.findByKraAndIsActiveOrderByIdAsc(kra,
						ERecordStatus.Y.name());

				if (kraDetailsFromDb.size() != request.getKraDetailsVOList().size()) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1603));
				}
				if (loggedInEmpId.equals(reportingmanagerId)) {
					if (kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.APPROVED.name())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1602));
					}
					if (kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.INPROCESS.name())) {
						// Approve KRA
						approveOrRejectKra(kra, request);
						addWFStatus(kra, reportingmanagerId, EKraStatus.APPROVED.name(), ERole.HR.name());
					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
					}

				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
			Kra empKra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
			Long empId = empKra.getEmployeeId();
			Employee employee = employeeDAO.findActiveEmployeeById(empId, IHRMSConstants.isActive);
			String tdMailId = employee.getOfficialEmailId();
			EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
					IHRMSConstants.KRA_Approved_By_RM, IHRMSConstants.isActive,
					employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
					employee.getCandidate().getLoginEntity().getOrganization());
			String ccMailId = null;
			mailForSubmitRequestByEmp(kra, loggedInEmpId, template, tdMailId, ccMailId);

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1606));
		response.setResponseCode(1200);
		log.info("Exit from approveKra method");
		return response;
	}

	private void approveOrRejectKra(Kra kra, KraDetailsRequestVO request) throws HRMSException {
		log.info("Inside approveOrRejectKra method");
		if (!HRMSHelper.isNullOrEmpty(request.getKraDetailsVOList())) {
			List<KraDetails> kraDetailsList = new ArrayList<KraDetails>();
			for (KraDetailsVO kraDetailsVO : request.getKraDetailsVOList()) {

				KraDetails kraDetails = kraDetailsDao.findByIdAndIsActiveAndKra(kraDetailsVO.getId(),
						ERecordStatus.Y.name(), kra);

				if (!HRMSHelper.isNullOrEmpty(kraDetails)) {
					kraDetails.setUpdatedDate(new Date());
					kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));
					kraDetails.setRmComment(kraDetailsVO.getRmComment());
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}
				kraDetailsList.add(kraDetails);
			}
			// save all records
			kraDetailsDao.saveAll(kraDetailsList);
			long kradetailsCount = kraDetailsDao.countByKraAndIsActive(kra, ERecordStatus.Y.name());
			log.info("kradetailsCount::" + kradetailsCount);
			if (kradetailsCount > kraMaximumCount) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Maximum " + kraMaximumCount + " Kra Allowed");
			}
		}
		log.info("Exit from approveOrRejectKra method");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<KraDetailsResponseVO> rejectKra(KraDetailsRequestVO request) throws HRMSException {
		log.info("Inside rejectKra method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<KraDetailsResponseVO>();

		if (authorizationServiceImpl.isAuthorizedFunctionName("rejectKra", role)) {
			kraHelper.approveKraInputValidation(request);
			Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(kra)) {
				List<KraDetails> kraDetailsFromDb = kraDetailsDao.findByKraAndIsActiveOrderByIdAsc(kra,
						ERecordStatus.Y.name());

				if (kraDetailsFromDb.size() != request.getKraDetailsVOList().size()) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1603));
				}

				Long reportingmanagerId = reportingManagerDao.findReportingManagerIdByEmployeeId(kra.getEmployeeId());
				if (loggedInEmpId.equals(reportingmanagerId)) {

					if (kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.REJECTED.name())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1605));
					}
					if (kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.INPROCESS.name())) {
						// Approve KRA
						approveOrRejectKra(kra, request);
						addWFStatus(kra, reportingmanagerId, EKraStatus.REJECTED.name(), ERole.EMPLOYEE.name());
					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
					}
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
			Kra empKra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
			Long empId = empKra.getEmployeeId();
			Employee employee = employeeDAO.findActiveEmployeeById(empId, IHRMSConstants.isActive);
			String tdMailId = employee.getOfficialEmailId();
			EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
					IHRMSConstants.KRA_Rejected_By_RM, IHRMSConstants.isActive,
					employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
					employee.getCandidate().getLoginEntity().getOrganization());
			String ccMailId = null;
			mailForSubmitRequestByEmp(kra, loggedInEmpId, template, tdMailId, ccMailId);
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1607));
		response.setResponseCode(1200);
		log.info("Exit from rejectKra method");
		return response;
	}

	public void mailForSubmitRequestByEmp(Kra kra, Long loggedInEmpId, EmailTemplate template, String mailIds,
			String ccMailId) {

		Employee employee = employeeDAO.findActiveEmployeeById(loggedInEmpId, IHRMSConstants.isActive);

		String employeeEmailID = mailIds;
		Map<String, String> placeHolderMap = new HashMap<String, String>();
		placeHolderMap.put("{websiteURL}", baseURL);

		if (EKraStatus.INPROCESS.name().equals(kra.getKraWf().getStatus())) {
			Employee rm = employee.getCandidate().getEmployee().getEmployeeReportingManager().getReporingManager();
			placeHolderMap.put("{rmName}",
					(rm.getCandidate().getFirstName()) + " " + (rm.getCandidate().getLastName()));
			placeHolderMap.put("{empName}",
					employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		} else if (EKraStatus.APPROVED.name().equals(kra.getKraWf().getStatus())
				|| EKraStatus.REJECTED.name().equals(kra.getKraWf().getStatus())) {
			Employee emp = employeeDAO.findActiveEmployeeById(kra.getEmployeeId(), IHRMSConstants.isActive);
			String empMail = emp.getOfficialEmailId();
			placeHolderMap.put("{empName}", emp.getCandidate().getFirstName() + " " + emp.getCandidate().getLastName());
		}

		String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

		if (!HRMSHelper.isNullOrEmpty(ccMailId)) {
			emailsender.toPersistEmail(mailIds, ccMailId, mailBody, template.getEmailSubject(),
					employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
					employee.getCandidate().getLoginEntity().getOrganization().getId());

		} else {
			emailsender.toPersistEmail(mailIds, null, mailBody, template.getEmailSubject(),
					employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
					employee.getCandidate().getLoginEntity().getOrganization().getId());
		}

	}

	public void sendEmailToAllEmp() {
		log.info("Inside send Mail To All Employee");

		// Find Employee IDs
		int count = 0;
		List<Employee> emp = employeeDAO.findByIsActive(IHRMSConstants.isActive);
		List<Long> empIds = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(emp)) {
			for (Employee employee : emp) {
				empIds.add(employee.getId());
			}
		}
		if (!HRMSHelper.isNullOrEmpty(empIds)) {
			for (Long empId : empIds) {
				sendFirstAprilEmailToAllEmp(empId);
				count++;
				log.info("total mail send count:::" + " " + count);
			}
		} else {
			log.info("No pending request.");
		}
	}

	public void sendFirstAprilEmailToAllEmp(Long empIds) {
		log.info("Sending mail to all employee for filling KRA 1st April:::");

		Employee employee = employeeDAO.findActiveEmployeeById(empIds, IHRMSConstants.isActive);
		// Fetch the Employee email ID
		String empMailId = employee.getOfficialEmailId();
		String empName = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName();

		// Fetch the appropriate email template for the reminder
		EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
				IHRMSConstants.Fill_KRA_Form, IHRMSConstants.isActive,
				employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
				employee.getCandidate().getLoginEntity().getOrganization());

		if (!HRMSHelper.isNullOrEmpty(template)) {

			Map<String, String> placeHolderMap = new HashMap<>();
			placeHolderMap.put("{empName}", empName);
			placeHolderMap.put("{URL}", baseURL);

			// Replace placeholders in the email template
			String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

			// Send the email reminder to the TD approver
			emailsender.toPersistEmail(empMailId, null, mailBody, template.getEmailSubject(),
					employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
					employee.getCandidate().getLoginEntity().getOrganization().getId());

			log.info("Email sent successfully to Employee :::");
		} else {
			// Handle the case where the email template is not found
			log.error("Email template not found for 1st April KRA Mail.");
		}
	}

	public void sendEmailToRM() {
		log.info("Inside send Reminder Mail To RM");

		List<Long> rm = reportingManagerDao.findReportingManagerByIsActive(IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(rm)) {
			for (Long rmId : rm) {
				if (!HRMSHelper.isNullOrEmpty(rmId)) {
					List<Kra> kra = kraDao.findByEmployeeByMangerAndIsActive(rmId, EKraStatus.INPROCESS.name(),
							IHRMSConstants.isActive);
					log.info("kra For RM" + " " + kra);
					if (!HRMSHelper.isNullOrEmpty(kra)) {
						sendReminderEmailToRM(rmId);
					} else {
						log.info("No Kra found for RM" + " " + rmId);
					}

				} else {
					log.info("No Reporting Manager ID found" + " " + rm);
				}

			}
		}

	}

	public void sendReminderEmailToRM(Long empIds) {
		log.info("Sending Reminder mail to RM for taking action on KRA:::");

		// Fetch the Employee email ID
		Employee employee = employeeDAO.findActiveEmployeeById(empIds, IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(employee)) {

			String empMailId = employee.getOfficialEmailId();
			String empName = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName();

			// Fetch the appropriate email template for the reminder
			EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
					IHRMSConstants.Action_On_KRA_Reminder_For_RM, IHRMSConstants.isActive,
					employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
					employee.getCandidate().getLoginEntity().getOrganization());

			if (!HRMSHelper.isNullOrEmpty(template)) {

				Map<String, String> placeHolderMap = new HashMap<>();
				placeHolderMap.put("{rmName}", empName);
				placeHolderMap.put("{websiteURL}", baseURL);

				// Replace placeholders in the email template
				String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

				// Send the email reminder to the TD approver
				emailsender.toPersistEmail(empMailId, null, mailBody, template.getEmailSubject(),
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());

				log.info("Email sent successfully to RM :::");
			} else {
				// Handle the case where the email template is not found
				log.error("Email template not found for Reminder mail to RM .");
			}
		}

	}

	public void sendReminderEmailToEmployee() {
		log.info("Inside send Reminder Mail To Employee");

		List<Employee> employee = employeeDAO.findByIsActive(IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			for (Employee emp : employee) {
				Kra kraEmp = kraDao.findByEmployeeId(emp.getId(), IHRMSConstants.isActive,
						EKraStatus.INCOMPLETE.name());
				// log.info("Inside KraEmp::::"+" "+kraEmp.getKraWf().getStatus());
				if (HRMSHelper.isNullOrEmpty(kraEmp)) {
					sendReminderEmailToEmp(emp.getId());
				} else if (!HRMSHelper.isNullOrEmpty(kraEmp)
						&& kraEmp.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.INCOMPLETE.name())) {
					sendReminderEmailToEmp(emp.getId());
				}

//				if(HRMSHelper.isNullOrEmpty(kraEmp) ||!HRMSHelper.isNullOrEmpty(kraEmp.getKraWf().getStatus())) {
//					if(HRMSHelper.isNullOrEmpty(kraEmp)|| kraEmp.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.INCOMPLETE.name())) {
//						sendReminderEmailToEmp(emp.getId());
//					}else {
//						log.info("Employee Already Fill KRA Form");
//					}
//				}

			}

		}

	}

	public void sendReminderEmailToEmp(Long empIds) {
		log.info("Sending Reminder mail to Employee for filling KRA:::");

		// Fetch the Employee email ID
		Employee employee = employeeDAO.findActiveEmployeeById(empIds, IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(employee)) {

			String empMailId = employee.getOfficialEmailId();
			String empName = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName();

			// Fetch the appropriate email template for the reminder
			EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
					IHRMSConstants.KRA_Reminder_For_Employee, IHRMSConstants.isActive,
					employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
					employee.getCandidate().getLoginEntity().getOrganization());

			if (!HRMSHelper.isNullOrEmpty(template)) {

				Map<String, String> placeHolderMap = new HashMap<>();
				placeHolderMap.put("{empName}", empName);
				placeHolderMap.put("{websiteURL}", baseURL);

				// Replace placeholders in the email template
				String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

				// Send the email reminder to the TD approver
				emailsender.toPersistEmail(empMailId, null, mailBody, template.getEmailSubject(),
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());

				log.info("Email sent successfully to Employee :::");
			} else {
				// Handle the case where the email template is not found
				log.error("Email template not found for Reminder mail to Employee .");
			}
		}

	}

	public void sendFirstAprilReminderToRM() {
		log.info("Inside send First April Reminder Mail To RM");

		List<Long> rm = reportingManagerDao.findReportingManagerByIsActive(IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(rm)) {
			for (Long rmId : rm) {
				if (!HRMSHelper.isNullOrEmpty(rmId)) {
					sendFirstAprilReminderEmailToRM(rmId);
				} else {
					log.info("No RM ID found for RM" + " " + rmId);
				}

			}
		}

	}

	public void sendFirstAprilReminderEmailToRM(Long empIds) {
		log.info("Sending First April Reminder mail to RM for taking action on KRA:::");

		// Fetch the Employee email ID
		Employee employee = employeeDAO.findActiveEmployeeById(empIds, IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(employee)) {

			String empMailId = employee.getOfficialEmailId();
			String empName = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName();

			// Fetch the appropriate email template for the reminder
			EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
					IHRMSConstants.Initial_Reminder_For_RM, IHRMSConstants.isActive,
					employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
					employee.getCandidate().getLoginEntity().getOrganization());

			if (!HRMSHelper.isNullOrEmpty(template)) {

				Map<String, String> placeHolderMap = new HashMap<>();
				placeHolderMap.put("{rmName}", empName);
				placeHolderMap.put("{websiteURL}", baseURL);

				// Replace placeholders in the email template
				String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

				// Send the email reminder to the TD approver
				emailsender.toPersistEmail(empMailId, null, mailBody, template.getEmailSubject(),
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());

				log.info("Email sent successfully to RM :::");
			} else {
				// Handle the case where the email template is not found
				log.error("Email template not found for First April Reminder mail to RM .");
			}
		}

	}

	@Override
	public void downloadKraSummary(Long deptid, Long empId, String year, Long countryId, String status,
			HttpServletResponse res) throws IOException, HRMSException {
		log.info("Inside downloadKRASummary method");

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (authorizationServiceImpl.isAuthorizedFunctionName("downloadKraSummaryForHR", role)
				|| authorizationServiceImpl.isAuthorizedFunctionName("downloadKraSummaryForPM", role)) {

			Pageable pageable = null;
			List<KraDetailsLite> kraDetailsLiteList = fetchKraDetails(deptid, empId, countryId, status, pageable);

			res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			res.setHeader("Content-Disposition", "attachment; filename=KRASummary.xlsx");

			try (Workbook workbook = excelGenerator.generateKRAXlS(kraDetailsLiteList);
					ServletOutputStream outputStream = res.getOutputStream()) {

				workbook.write(outputStream);
				outputStream.flush();

			} catch (IOException e) {
				log.error("Error while generating Excel file: ", e);
				throw e;
			}

			log.info("Exit from downloadKRASummary method");
		} else {

			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private List<KraDetailsLite> fetchKraDetails(Long deptid, Long empId, Long countryId, String displaystatus,
			Pageable pageable) {
		if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(empId)
				&& !HRMSHelper.isNullOrEmpty(countryId)) {
			if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
				KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
				String status = kradisplaystatus.getStatus();
				return kraDetailsLiteDao.getKraDetailsByDeptAndEmpAndCountryId(deptid, empId, countryId, status,
						pageable);
			} else {
				return kraDetailsLiteDao.getKraDetailsByDeptAndEmpAndCountryId(deptid, empId, countryId, pageable);
			}
		} else if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(countryId)) {
			if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
				KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
				String status = kradisplaystatus.getStatus();
				return kraDetailsLiteDao.getKraDetailsByDeptAndCountryId(deptid, countryId, status, pageable);
			} else {
				return kraDetailsLiteDao.getKraDetailsByDeptAndCountryId(deptid, countryId, pageable);
			}
		} else if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(empId)) {
			if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
				KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
				String status = kradisplaystatus.getStatus();
				return kraDetailsLiteDao.getKraDetailsByDeptAndEmpAndStatus(deptid, empId, status, pageable);
			} else {
				return kraDetailsLiteDao.getKraDetailsByDeptAndEmp(deptid, empId, pageable);
			}
		} else if (!HRMSHelper.isNullOrEmpty(deptid)) {
			if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
				KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
				String status = kradisplaystatus.getStatus();
				return kraDetailsLiteDao.getKraDetailsByDeptAndStatus(deptid, status, pageable);
			} else {
				return kraDetailsLiteDao.getKraDetailsByDept(deptid, pageable);
			}
		} else if (!HRMSHelper.isNullOrEmpty(empId)) {
			if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
				KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
				String status = kradisplaystatus.getStatus();
				return kraDetailsLiteDao.getKraDetailsByEmpAndStatus(empId, status, pageable);
			} else {
				return kraDetailsLiteDao.getKraDetailsByEmp(empId, pageable);
			}
		} else if (!HRMSHelper.isNullOrEmpty(countryId)) {
			if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
				KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
				String status = kradisplaystatus.getStatus();
				return kraDetailsLiteDao.getKraDetailsByCountryIdAndStatus(countryId, status, pageable);
			} else {
				return kraDetailsLiteDao.getKraDetailsByCountryId(countryId, pageable);
			}
		} else if (!HRMSHelper.isNullOrEmpty(displaystatus)) {
			KraDisplayStatus kradisplaystatus = kradisplaystatusDao.findByDisplayName(displaystatus);
			String status = kradisplaystatus.getStatus();
			return kraDetailsLiteDao.getKraDetailsByStatus(status, pageable);
		} else {
			return kraDetailsLiteDao.getKraDetails(pageable);
		}
	}

	@Override
	public void SendKraCountToHR() {
		log.info("Inside kraCountForHR Method");

		String[] empIds = kraHrEmpId.split(",");
		List<Long> empIdList = Arrays.stream(empIds).map(Long::parseLong).collect(Collectors.toList());

		List<Employee> empresult = employeeDAO.findEmpListByEmpIds(empIdList);
		List<KraCountForHR> kraDetail = kraCountDao.getKRADetails();
		sendKraReminderMail(empresult, kraDetail);
		log.info("Exit from kraCountForHR Method");

	}

	private void sendKraReminderMail(List<Employee> empresult, List<KraCountForHR> kraDetail) {
		log.info("Inside sendKraReminderMail Method");
		if (!HRMSHelper.isNullOrEmpty(empresult)) {
			for (Employee employee : empresult) {

				StringBuilder tableRowsBuilder = new StringBuilder();
				for (KraCountForHR kraLite : kraDetail) {
					tableRowsBuilder.append("<tr>").append("<td>").append(kraLite.getDepartmentName()).append("</td>")
							.append("<td>").append(kraLite.getTotalCount()).append("</td>").append("<td>")
							.append(kraLite.getNotSubmittedCount()).append("</td>").append("<td>")
							.append(kraLite.getInprocess()).append("</td>").append("<td>").append(kraLite.getApproved())
							.append("</td>").append("<td>").append(kraLite.getRejected()).append("</td>").append("<td>")
							.append(kraLite.getIncompleteCount()).append("</td>").append("</tr>");
				}
				String tableRows = tableRowsBuilder.toString();

				EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
						IHRMSConstants.Kra_Reminder_To_HR, IHRMSConstants.isActive,
						employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
						employee.getCandidate().getLoginEntity().getOrganization());
				String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
				String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
				String mailBody = template.getTemplate().replace("{time}", currentTime).replace("{date}", currentDate)
						.replace("{HrFirstName}", employee.getCandidate().getFirstName())
						.replace("{HrLastName}", employee.getCandidate().getLastName())
						.replace("{tableRows}", tableRows);
				log.info("Email Sender : Sending an email to HR for KRA reminder");

				emailsender.toPersistEmail(employee.getOfficialEmailId(), "", mailBody, template.getEmailSubject(),
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						employee.getCandidate().getLoginEntity().getOrganization().getId());

				log.info("Email Sender : Email sent to " + employee.getOfficialEmailId());
			}
		}
		log.info("Exit from sendKraReminderMail Method");
	}

	@Override
	public void downloadKraReport(HttpServletResponse res, Long empid, Long kraid) throws IOException, HRMSException {
		log.info("Inside downloadKraReport method  ");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		if (authorizationServiceImpl.isAuthorizedFunctionName("downloadKraReport", role)) {
			handleGenerateKraReportProcess(res, kraid, loggedInEmpId, stream);
		} else {
			stream.close();
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private void handleGenerateKraReportProcess(HttpServletResponse res, Long kraid, Long loggedInEmpId,
			ByteArrayOutputStream stream) throws IOException, HRMSException {
		// get kra
		Kra kra = kraDao.findByIdAndIsActive(kraid, ERecordStatus.Y.name());
		List<EmployeeReportingManager> employeesUnderManager = reportingManagerDao.findByreporingManager(loggedInEmpId);
		if (!HRMSHelper.isNullOrEmpty(kra)) {
			// check manger
			long matchCount = employeesUnderManager.stream()
					.filter(e -> e.getEmployee().getId().equals(kra.getEmployeeId())).count();
			if (matchCount > 0) {
//				List<VisionKraDetailsView> kraReport = kraReportViewDao.getKraDetailsByKraIdandEmpId(kra.getId(),
//						kra.getEmployeeId());
//				Workbook workbook = new KraReportXLSGenerator().generateXlS(kraReport);
				res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				res.setHeader("Content-Disposition", "attachment; filename=KraReport.xlsx");
//				workbook.write(res.getOutputStream());
//				workbook.write(stream);
//				workbook.close();
			} else {
				throw new HRMSException(1520, ResponseCode.getResponseCodeMap().get(1521));
			}
		}
		log.info("Exit from downloadKraReport method");
	}

	@Override
	public HRMSBaseResponse<NewKraResponse> getNewKraById(Long kraid) throws HRMSException {
		HRMSBaseResponse<NewKraResponse> baseResponse = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		List<VisionKraDetailsView> visionKraList = visionKraDao.findByKraIdAndIsActive(kraid, IHRMSConstants.isActive);
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		Kra empkra = kraDao.findByIdAndIsActive(kraid, IHRMSConstants.isActive);
		if (empkra == null) {
			throw new HRMSException("KRA not found or inactive for ID: " + kraid);
		}
		Employee empKpi = empkra.getEmployee();
		if (empKpi == null) {
			throw new HRMSException("Employee linked to KRA is null for KRA ID: " + kraid);
		}
		validateMcMember(role, loggedInEmpId, empkra);

		List<KraDetails> kraDetailsList = kraDetailsDao.findByIsActiveAndKra(IHRMSConstants.isActive, empkra);
		boolean isWeightageValid = KraTransformUtil.isOjectiveWeightageValid(kraDetailsList);
		// double totalWeight =
		// kraDetailsList.stream().mapToDouble(KraDetails::getObjectiveWeightage).sum();
		Float totalWeight = kraDetailsList.stream().map(KraDetails::getObjectiveWeightage).filter(Objects::nonNull)
				.reduce(0.0f, Float::sum);

		if (visionKraList != null && !visionKraList.isEmpty()) {

			List<KraCategoryVo> categories = visionKraList.stream()
					.collect(Collectors.groupingBy(VisionKraDetailsView::getCategory)).entrySet().stream()
					.map(entry -> {
						String categoryName = entry.getKey();
						List<VisionKraDetailsView> categoryDetails = entry.getValue();

						KraCategoryVo categoryResponseVo = new KraCategoryVo();
						categoryResponseVo.setCategoryId(categoryDetails.stream()
								.map(VisionKraDetailsView::getCategoryId).sorted().findFirst().orElse(null));
						categoryResponseVo.setName(categoryName);
						categoryResponseVo.setKraCycleId(categoryDetails.get(0).getKraCycleId());
						categoryResponseVo.setKraId(kraid);
						categoryResponseVo.setCategoryweight(categoryDetails.get(0).getCategoryWeight() + "%");

						// Process and sort subcategories by subCategoryId in ascending order
						List<KraSubcategoryVo> subcategoryList = categoryDetails.stream()
								.collect(Collectors.groupingBy(VisionKraDetailsView::getSubCategory)).entrySet()
								.stream().map(subcategoryEntry -> {
									String subcategoryName = subcategoryEntry.getKey();
									List<VisionKraDetailsView> subcategoryDetails = subcategoryEntry.getValue();

									// Sort subcategory details by subCategoryId in ascending order
									KraSubcategoryVo subcategoryResponseVo = new KraSubcategoryVo();
									subcategoryResponseVo.setSubcategoryId(
											subcategoryDetails.stream().map(VisionKraDetailsView::getSubCategoryId)
													.sorted().findFirst().orElse(null));
									subcategoryResponseVo.setName(subcategoryName);

									// Process and sort objectives by objective ID in ascending order
									List<KraObjectiveVo> kraObjectiveList = subcategoryDetails.stream()
											.sorted(Comparator.comparing(VisionKraDetailsView::getId)).map(detail -> {
												KraObjectiveVo kraObjectiveVo = new KraObjectiveVo();
												kraObjectiveVo.setId(detail.getId());
												kraObjectiveVo.setName(detail.getObjectives());
												kraObjectiveVo.setObjectiveweight(
														String.valueOf(detail.getObjectiveWeight()));
												kraObjectiveVo.setMetric(detail.getMetric());
												kraObjectiveVo.setIsEdit(detail.getIs_edit());
												kraObjectiveVo.setIsColor(detail.getIs_color());
												kraObjectiveVo.setHcdComment(detail.getHcdComment());
												if (!HRMSHelper.isNullOrEmpty(detail.getHcdComment())) {
													kraObjectiveVo.setViewComment(IHRMSConstants.isActive);
												} else {
													kraObjectiveVo.setViewComment(IHRMSConstants.isNotActive);
												}
												kraObjectiveVo.setRmAiComment(detail.getRmAiComment());

												// ===== Self Rating (with conditional translation) =====
												if (detail.getSelfRating() != null) {
													Optional<KraRating> rating = kraRatingDAO
															.findById(detail.getSelfRating());
													if (rating.isPresent()) {
														SelfRatingResponseVo responseVo = new SelfRatingResponseVo();
														responseVo.setId(rating.get().getId());
														String srLabel = rating.get().getLabel();

														// translate only for Half/Mid year cycles
														KraCycle activeCycleForTranslation = kraCycleDAO
																.findByKraId(detail.getKraCycleId());
														if (shouldTranslateForCycle(activeCycleForTranslation)) {
															srLabel = kpiHelper.translateRatingLabel(srLabel);
														}

														responseVo.setLabel(srLabel);
														responseVo.setValue(rating.get().getValue());
														responseVo.setCategoryId(rating.get().getCategory().getId());
														kraObjectiveVo.setSelfrating(responseVo);
													}
												}

												// (Manager rating left as-is per your requirement)
												if (detail.getManagerRating() != null) {
													Optional<KraRating> managerrating = kraRatingDAO
															.findById(detail.getManagerRating());

													if (managerrating.isPresent()) {
														ManagerRatingResponseVo managerRatingResponse = new ManagerRatingResponseVo();
														managerRatingResponse.setId(managerrating.get().getId());

														String mgrLabel = managerrating.get().getLabel();
														// translate only for Half/Mid year cycles
														KraCycle activeCycleForTranslation = kraCycleDAO
																.findByKraId(detail.getKraCycleId());
														if (shouldTranslateForCycle(activeCycleForTranslation)) {
															mgrLabel = kpiHelper.translateRatingLabel(mgrLabel);
														}

														managerRatingResponse.setLabel(mgrLabel);
														managerRatingResponse.setCategoryId(
																managerrating.get().getCategory().getId());
														managerRatingResponse.setValue(managerrating.get().getValue());
														kraObjectiveVo.setManagerrating(managerRatingResponse);
													}
												}

												kraObjectiveVo.setSelfqualitativeassessment(
														detail.getSelfQaulitativeAssisment());
												kraObjectiveVo.setManagerqaulitativeassisment(
														detail.getManagerQaulitativeAssisment());
												return kraObjectiveVo;
											}).collect(Collectors.toList());

									subcategoryResponseVo.setObjectives(kraObjectiveList);
									return subcategoryResponseVo;
								}).sorted(Comparator.comparing(KraSubcategoryVo::getSubcategoryId))
								.collect(Collectors.toList());

						categoryResponseVo.setSubcategory(subcategoryList);
						return categoryResponseVo;
					}).collect(Collectors.toList());

			// Set categories into the response body
			NewKraResponse response = new NewKraResponse();

			response.setRmAiComment(empkra.getRmAiComment());
			response.setFdhAiComment(empkra.getFdhAiComment());
			response.setHodAiComment(empkra.getHodAiComment());
			response.setHrAiComment(empkra.getHrAiComment());
			response.setCalibrateAiComment(empkra.getCalibrateAiComment());
		
			    String isEmployeeDownload = ERecordStatus.N.name();
			    String isManagerDownload = ERecordStatus.N.name();
			    String isFunctionHeadDownload = ERecordStatus.N.name();
			    String isHcdDownload = ERecordStatus.N.name();
			    String isHodDownload = ERecordStatus.N.name();

			    // EMPLOYEE
			    KpiFormToRole empRoleMapping = kpiFormToRoleDAO.findByRoleid(102L);
			    if (empRoleMapping != null && !HRMSHelper.isNullOrEmpty(empRoleMapping.getIsActive())) {
			        isEmployeeDownload = empRoleMapping.getIsActive();
			    }
			    response.setIsEmployeeDownload(isEmployeeDownload);

			    // LINE MANAGER
			    KpiFormToRole manRoleMapping = kpiFormToRoleDAO.findByRoleid(104L);
			    if (manRoleMapping != null && !HRMSHelper.isNullOrEmpty(manRoleMapping.getIsActive())) {
			        isManagerDownload = manRoleMapping.getIsActive();
			    }
			    response.setIsLineManagerDownload(isManagerDownload);

			    // FUNCTION HEAD
			    KpiFormToRole funcRoleMapping = kpiFormToRoleDAO.findByRoleid(108L);
			    if (funcRoleMapping != null && !HRMSHelper.isNullOrEmpty(funcRoleMapping.getIsActive())) {
			        isFunctionHeadDownload = funcRoleMapping.getIsActive();
			    }
			    response.setIsFunctionHeadDownload(isFunctionHeadDownload);

			    // HOD
			    KpiFormToRole hodRoleMapping = kpiFormToRoleDAO.findByRoleid(103L);
			    if (hodRoleMapping != null && !HRMSHelper.isNullOrEmpty(hodRoleMapping.getIsActive())) {
			        isHodDownload = hodRoleMapping.getIsActive();
			    }
			    response.setIsHodDownload(isHodDownload);

			    // HCD
			    KpiFormToRole hcdRoleMapping = kpiFormToRoleDAO.findByRoleid(106L);
			    if (hcdRoleMapping != null && !HRMSHelper.isNullOrEmpty(hcdRoleMapping.getIsActive())) {
			        isHcdDownload = hcdRoleMapping.getIsActive();
			    }
			    response.setIsHcdDownload(isHcdDownload);

		
			
			DelegationMapping delegatelist = delegationMappingDAO.findByDelegationForAndIsActive(empkra.getEmployeeId(),
					IHRMSConstants.isActive);
			DelegationMapping isDelegatedto = delegationMappingDAO.findByDelegationToAndDelegationForAndIsActive(
					loggedInEmpId, loggedInEmpId, ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(isDelegatedto)) {
				response.setIsDelegated(ERecordStatus.N.name());
			} else if (!HRMSHelper.isNullOrEmpty(delegatelist)) {
				response.setIsDelegated(ERecordStatus.Y.name());
			} else {
				response.setIsDelegated(ERecordStatus.N.name());
			}

			EmployeeReportingManager reportingManager = reportingManagerDao
					.findByReporingManagerAndEmployeeAndIsActive(loggedInEmployee, empKpi, ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(reportingManager)) {
				response.setIsReportingManager(ERecordStatus.Y.name());
			} else {
				response.setIsReportingManager(ERecordStatus.N.name());
			}

			Kra kra = kraDao.findByIdAndIsActive(kraid, ERecordStatus.Y.name());
			KraWf krafWf = kraWfDao.findByKraAndIsActive(kra, ERecordStatus.Y.name());
			List<KraDetails> details = kraDetailsDao.findByKraAndIsActiveAndIsColorOrderByIdAsc(kra,
					ERecordStatus.Y.name(), ERecordStatus.Y.name());
			if (HRMSHelper.isNullOrEmpty(details)) {
				response.setIsManagerObjectives(ERecordStatus.N.name());
			} else {
				response.setIsManagerObjectives(ERecordStatus.Y.name());
			}
			KraCycle cycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
			if (kra == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}
			if (cycle.getCycleTypeId().equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {
			    if (((krafWf.getStatus().equalsIgnoreCase(EKraStatus.INPROGRESS.name())
			            && krafWf.getPendingWith().equalsIgnoreCase(ERole.MANAGER.name()))
			        || (krafWf.getStatus().equalsIgnoreCase(EKraStatus.CORRECTION.name())
			            && krafWf.getPendingWith().equalsIgnoreCase(ERole.MANAGER.name())))
			        && isWeightageValid) {

			        response.setIsObjectiveSubmit(ERecordStatus.Y.name());

			    } else {
			        response.setIsObjectiveSubmit(ERecordStatus.N.name());
			    }

			} else if (krafWf.getPendingWith().equalsIgnoreCase(ERole.MANAGER.name())
			        && krafWf.getStatus().equalsIgnoreCase(EKraStatus.INPROCESS.name())) {

			    response.setIsObjectiveSubmit(ERecordStatus.Y.name());

			} else {
			    response.setIsObjectiveSubmit(ERecordStatus.N.name());
			}

			if (((krafWf.getStatus().equalsIgnoreCase(EKraStatus.SUBMITTED.name())
					&& krafWf.getPendingWith().equalsIgnoreCase(ERole.DIVISIONHEAD.name())))) {

				response.setIsFunctionHeadSubmit(ERecordStatus.Y.name());
			} else {
				response.setIsFunctionHeadSubmit(ERecordStatus.N.name());
			}

			response.setCycleName(cycle.getDescription());
			response.setCycleType(cycle.getCycleTypeId());

			if ((krafWf.getStatus().equalsIgnoreCase(EKraStatus.INPROGRESS.name())
					&& krafWf.getPendingWith().equalsIgnoreCase(ERole.HR.name()))) {

				response.setIsHcdApproved(ERecordStatus.Y.name());
			} else {
				response.setIsHcdApproved(ERecordStatus.N.name());
			}

			KraCycle activeCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(activeCycle)) {
				response.setActiveCycleName(activeCycle.getCycleName());
				response.setActiveCycleType(activeCycle.getCycleTypeId());
			}
			// final boolean translateForHalfOrMid = shouldTranslateForCycle(activeCycle);

			KraCycle formCycle = empkra.getCycleId();
			if (!HRMSHelper.isNullOrEmpty(formCycle)) {
				KraCycle formCycleName = kraCycleDAO.findByKraCycle(formCycle.getId());
				response.setFormCycleName(formCycleName.getCycleName());
				response.setFormCycleType(formCycleName.getCycleTypeId());
			}

			if ((krafWf.getStatus().equalsIgnoreCase(EKraStatus.APPROVED.name())
					&& krafWf.getPendingWith().equalsIgnoreCase(ERole.MANAGER.name()))) {

				response.setIsmanagerApproved(ERecordStatus.Y.name());
			} else {
				response.setIsmanagerApproved(ERecordStatus.N.name());
			}

			if ((krafWf.getStatus().equalsIgnoreCase(EKraStatus.INACTION.name())
					&& krafWf.getPendingWith().equalsIgnoreCase(ERole.EMPLOYEE.name()))) {

				response.setIsEmployeeAccept(ERecordStatus.Y.name());
			} else {
				response.setIsEmployeeAccept(ERecordStatus.N.name());
			}

			if ((krafWf.getStatus().equalsIgnoreCase(EKraStatus.CORRECTION.name())
					&& krafWf.getPendingWith().equalsIgnoreCase(ERole.MANAGER.name()))) {
				response.setIsHcdCorrect(ERecordStatus.Y.name());

			} else {
				response.setIsHcdCorrect(ERecordStatus.N.name());
			}

			String formattedWeight = String.format("%.2f", totalWeight);
			response.setTotalWeight(formattedWeight);

			if (EKraStatus.INCOMPLETE.name().equals(krafWf.getStatus())
					&& EKraStatus.EMPLOYEE.name().equals(krafWf.getPendingWith())) {
				response.setIsEmployeeSubmit(ERecordStatus.N.name());
			} else {
				response.setIsEmployeeSubmit(ERecordStatus.Y.name());
			}
			if ((EKraStatus.INPROCESS.name().equals(krafWf.getStatus())
					&& EKraStatus.MANAGER.name().equals(krafWf.getPendingWith()))
					|| (EKraStatus.INCOMPLETE.name().equals(krafWf.getStatus())
							&& EKraStatus.EMPLOYEE.name().equals(krafWf.getPendingWith()))
					|| (EKraStatus.REJECTED.name().equals(krafWf.getStatus())
							&& EKraStatus.MANAGER.name().equals(krafWf.getPendingWith()))) {

				response.setIsManagerSubmit(ERecordStatus.N.name());
			} else {
				response.setIsManagerSubmit(ERecordStatus.Y.name());
			}

			// ===== HOD Final Rating (with conditional translation) =====
			if (HRMSHelper.isRolePresent(role, ERole.EMPLOYEE.name())
					&& !EKraStatus.COMPLETED.name().equals(krafWf.getStatus())) {
				response.setFinalRating(null);
			} else {
				HodCycleFinalRating hodrating = hodcycleDao.findByIsActiveAndKra(ERecordStatus.Y.name(), kra);
				if (hodrating != null) {
					String finalRatingLabel = (hodrating.getScore() != null) ? hodrating.getScore().getLabel()
							: kra.getFinalRating();

					KraCycle currentCycle = kra.getCycleId();
					if (shouldTranslateForCycle(currentCycle) && finalRatingLabel != null) {
						finalRatingLabel = kpiHelper.translateRatingLabel(finalRatingLabel);
					}
					response.setFinalRating(finalRatingLabel);
				} else {
					String finalRatingLabel = kra.getFinalRating();
					KraCycle currentCycle = kra.getCycleId();
					if (shouldTranslateForCycle(currentCycle) && finalRatingLabel != null) {
						finalRatingLabel = kpiHelper.translateRatingLabel(finalRatingLabel);
					}
					response.setFinalRating(finalRatingLabel);
				}
			}

			// ===== Self Rating (with conditional translation if needed) =====
			if (HRMSHelper.isRolePresent(role, ERole.EMPLOYEE.name())) {
				Kra totalSelfRating = kraDao.findByIdAndIsActive(kra.getId(), ERecordStatus.Y.name());
				if (totalSelfRating != null) {
					String totalSelfRatingLabel = totalSelfRating.getTotalSelfRating();

					if (!HRMSHelper.isNullOrEmpty(totalSelfRatingLabel)) {
						KraCycle currentCycle = kra.getCycleId();
						if (shouldTranslateForCycle(currentCycle) && totalSelfRatingLabel != null) {
							totalSelfRatingLabel = kpiHelper.translateRatingLabel(totalSelfRatingLabel);
						}
						response.setTotalSelfRating(totalSelfRatingLabel);
					}
				} else {
					String totalSelfRatingLabel = kra.getTotalSelfRating();
					KraCycle currentCycle = kra.getCycleId();
					if (shouldTranslateForCycle(currentCycle) && totalSelfRatingLabel != null) {
						totalSelfRatingLabel = kpiHelper.translateRatingLabel(totalSelfRatingLabel);
					}
					response.setTotalSelfRating(totalSelfRatingLabel);
				}
			}

			// ===== Manager Rating (with conditional translation) =====
			if (HRMSHelper.isRolePresent(role, ERole.EMPLOYEE.name())) {
				Kra managerRating = kraDao.findByIdAndIsActive(kra.getId(), ERecordStatus.Y.name());
				if (managerRating != null) {
					String totalManagerRatingLabel = managerRating.getFinalRating();

					if (!HRMSHelper.isNullOrEmpty(totalManagerRatingLabel)) {
						KraCycle currentCycle = kra.getCycleId();
						if (shouldTranslateForCycle(currentCycle) && totalManagerRatingLabel != null) {
							totalManagerRatingLabel = kpiHelper.translateRatingLabel(totalManagerRatingLabel);
						}
						response.setTotalManagerRating(totalManagerRatingLabel);
					}
				} else {
					String totalManagerRatingLabel = kra.getFinalRating();
					KraCycle currentCycle = kra.getCycleId();
					if (shouldTranslateForCycle(currentCycle) && totalManagerRatingLabel != null) {
						totalManagerRatingLabel = kpiHelper.translateRatingLabel(totalManagerRatingLabel);
					}
					response.setTotalManagerRating(totalManagerRatingLabel);

				}
			}

			// ===== Last Year Rating (with conditional translation) =====
			Long lastCycleId = kraCycleDAO.findKraCycleById(kra.getCycleId().getId());
			if (lastCycleId != null) {
				KraCycle lastCycle = kraCycleDAO.findById(lastCycleId).orElse(null);
				if (lastCycle != null) {
					HodCycleFinalRating lastYearrating = hodcycleDao.findByEmployeeAndCycleId(kra.getEmployee(),
							lastCycle);
					if (lastYearrating != null && lastYearrating.getScore() != null) {
						String lastYearLabel = lastYearrating.getScore().getLabel();
						if (shouldTranslateForCycle(lastCycle) && lastYearLabel != null) {
							lastYearLabel = kpiHelper.translateRatingLabel(lastYearLabel);
						}
						response.setLastYearRating(lastYearLabel);
					} else {
						Kra lastKra = kraDao.findByEmpidAndCycleId(kra.getEmployee().getId(), lastCycleId);
						if (lastKra != null && lastKra.getFinalRating() != null) {
							String lastYearLabel = lastKra.getFinalRating();
							if (shouldTranslateForCycle(lastCycle) && lastYearLabel != null) {
								lastYearLabel = kpiHelper.translateRatingLabel(lastYearLabel);
							}
							response.setLastYearRating(lastYearLabel);
						}
					}
				}
			}

			response.setCategory(categories);

			baseResponse.setResponseCode(1200);
			baseResponse.setResponseMessage("Success");
			baseResponse.setResponseBody(response);
		} else {
			throw new HRMSException(1521, "KRA details not found for the given ID");
		}

		return baseResponse;
	}

	/**
	 * Translate labels only for Half Year / Mid Year cycles
	 */
	private boolean shouldTranslateForCycle(KraCycle cycle) {
		if (cycle == null || cycle.getCycleName() == null)
			return false;
		return IHRMSConstants.HALF_YEAR_CYCLE.equalsIgnoreCase(cycle.getCycleName())
				|| IHRMSConstants.MID_YEAR_CYCLE.equalsIgnoreCase(cycle.getCycleName());
	}

	/*	*//**
			 * DB label -> unified human-friendly label (with numeric prefix) e.g., "3", "3
			 * [Low]", "3 [Mid]", "3 [High]" -> "3: Performing"
			 *//*
				 * private String translateRatingLabel(String dbLabel) { if (dbLabel == null) {
				 * return null; } switch (dbLabel.trim()) { // Unsatisfactory case "1": case
				 * "1 [Low]": case "1 [High]": return "Unsatisfactory";
				 * 
				 * // Developing case "2": case "2 [Low]": case "2 [Mid]": case "2 [High]":
				 * return "Developing";
				 * 
				 * // Performing case "3": case "3 [Low]": case "3 [Mid]": case "3 [High]":
				 * return "Performing";
				 * 
				 * // Exceptional case "4": case "4 [Low]": case "4 [High]": return
				 * "Exceptional";
				 * 
				 * default: return dbLabel; // fallback if unknown label } }
				 */
    @Override
    public HRMSBaseResponse<ObjectiveFeedbackResponseVo> getSelfFeedbackDraft(ObjectiveFeedbackVo request) throws HRMSException, JsonProcessingException {

        log.info("Inside get Self Feedback Draft method");

        HRMSBaseResponse<ObjectiveFeedbackResponseVo> baseresponse = new HRMSBaseResponse<>();
        ObjectiveFeedbackResponseVo responseVo = new ObjectiveFeedbackResponseVo();
        responseVo.setId(request.getId());

		ObjectiveFeedbackDraftingAssistanceRequestVo aiRequestVo = new ObjectiveFeedbackDraftingAssistanceRequestVo();
		VisionKraDetailsView kraDetails = visionKraDao.findByIdAndIsActive(request.getId(), IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kraDetails)) {
			aiRequestVo.setCurrentCycle(kraDetails.getCycleName());
			aiRequestVo.setCategory(kraDetails.getCategory());
			aiRequestVo.setCategoryWeight(kraDetails.getCategoryWeight().toString());
			aiRequestVo.setSubCategory(kraDetails.getSubCategory());
			aiRequestVo.setObjectives(kraDetails.getObjectives());
			aiRequestVo.setObjectiveWeight(kraDetails.getObjectiveWeight().toString());
			aiRequestVo.setMetric(kraDetails.getMetric());
			aiRequestVo.setEmployeeRating(kraRatingDAO.getById(request.getSelfRating().getId()).getValue());
            aiRequestVo.setEmployeeRatingLabel(kraRatingDAO.getById(request.getSelfRating().getId()).getLabel());
			aiRequestVo.setEmployeeQualitativeAssessment(request.getSelfQualitativeAssessment());
		}

		// fetch employee details
		// Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long employeeId = kraDetails.getEmployeeId();
		Employee employee = employeeDAO.findActiveEmployeeById(employeeId, IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			EmployeeDetails employeeSelf = aiRequestVo.new EmployeeDetails();
			// Fetch department, devision, and designation of employee
			EmployeeDepartment department = departmentDAO.findByEmployee(employee);
			if (!HRMSHelper.isNullOrEmpty(department)) {
				employeeSelf.setDepartmentName(department.getDepartment().getDepartmentName());
			}

			EmployeeDesignation designation = designationDAO.findByEmployee(employee);
			if (!HRMSHelper.isNullOrEmpty(designation)) {
				employeeSelf.setDesignationName(designation.getDesignation().getDesignationName());
			}

			EmployeeDivision division = employee.getEmployeeDivision();
			if (!HRMSHelper.isNullOrEmpty(division)) {
				employeeSelf.setDivisionName(division.getDivision().getDivisionName());
			}

			EmployeeReportingManager employeeToReportingMapping = reportingmanagerDAO.findByEmployeeAndIsActive(employeeId, ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(employeeToReportingMapping)) {
				Employee reportingManager = employeeToReportingMapping.getReporingManager();
				if (!HRMSHelper.isNullOrEmpty(reportingManager)) {
					EmployeeDetails reportingManagerDetails = aiRequestVo.new EmployeeDetails();
					// Fetch department, designation, and division of reporting manager
					EmployeeDepartment rmDepartment = departmentDAO.findByEmployee(reportingManager);
					if (!HRMSHelper.isNullOrEmpty(rmDepartment)) {
						reportingManagerDetails.setDepartmentName(rmDepartment.getDepartment().getDepartmentName());
					}
					EmployeeDesignation rmDesignation = designationDAO.findByEmployee(reportingManager);
					if (!HRMSHelper.isNullOrEmpty(rmDesignation)) {
						reportingManagerDetails.setDesignationName(rmDesignation.getDesignation().getDesignationName());
					}
					EmployeeDivision rmDivision = reportingManager.getEmployeeDivision();
					if (!HRMSHelper.isNullOrEmpty(rmDivision)) {
						reportingManagerDetails.setDivisionName(rmDivision.getDivision().getDivisionName());
					}
					employeeSelf.setReportingManagerDetails(reportingManagerDetails);
				}
			}
			aiRequestVo.setEmployeeDetails(employeeSelf);
		}

		//Call pmsAiMS API and get response
		try {
			// ObjectMapper mapper = new ObjectMapper();
			// String aiRequestJson = mapper.writeValueAsString(aiRequestVo);
			// log.info("The Payload for PMS AIMS : \n\n" + aiRequestJson + "\n\n\n.");

			if (pmsAiMsEnabled.equalsIgnoreCase("N")) {
				log.info("PMS AIMS is disabled. Returning mock response.");
				responseVo.setSelfQualitativeAssessmentAI("PMS AIMS is disabled. Mock Response : \n\n " + request.getSelfQualitativeAssessment());
			} else {
				log.info("PMS AIMS is enabled. Proceeding to call the API.");

				URI uri = new URI(pmsAiMsBaseUrl + pmsAiMsEndPointEmployeeDraftingAssistance);
				RestTemplate restTemplate = new RestTemplate();

				// Create headers with API key
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("api-key", pmsAiMsApiKey);

				// Create HTTP entity with body and headers
				HttpEntity<ObjectiveFeedbackDraftingAssistanceRequestVo> requestEntity = new HttpEntity<>(aiRequestVo, headers);

				// Make POST request
				log.info("Calling PMS AIMS API : Request sent ...");
				ResponseEntity<ObjectiveFeedbackDraftingAssistanceResponseVo> result = restTemplate.postForEntity(uri, requestEntity, ObjectiveFeedbackDraftingAssistanceResponseVo.class);
				log.info("Calling PMS AIMS API : Response received ...");

				//check if request is successful
				if (result.getStatusCode() != HttpStatus.OK) {
					log.error("Error in getSelfFeedbackDraft():E1: HTTP Status Code = " + result.getStatusCode());
					throw new HRMSException(1500, "Error while calling AI service : " + result.getStatusCode());
				} else {
					ObjectiveFeedbackDraftingAssistanceResponseVo pmsAiMsResponse = result.getBody();
					if (!HRMSHelper.isNullOrEmpty(pmsAiMsResponse)) {
						responseVo.setSelfQualitativeAssessmentAI(pmsAiMsResponse.getAiFeedback());
					} else {
						log.error("Error in getSelfFeedbackDraft():E2: No Response body from AIMS : " + result.getStatusCode());
						throw new HRMSException(1500, "Error while calling AI service : No Response body : " + result.getStatusCode());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in getSelfFeedbackDraft():E1:", e);
			throw new HRMSException(1500, "Error while calling AI service: " + e.getMessage());
		}

        baseresponse.setResponseCode(1200);
        baseresponse.setResponseMessage("Success");
        baseresponse.setResponseBody(responseVo);
        return baseresponse;
    }

	@Override
	public HRMSBaseResponse<ObjectiveFeedbackResponseVo> getManagerFeedbackDraft(ObjectiveFeedbackVo request) throws HRMSException, JsonProcessingException {

		log.info("Inside get Manager Feedback Draft method");

		HRMSBaseResponse<ObjectiveFeedbackResponseVo> baseresponse = new HRMSBaseResponse<>();
		ObjectiveFeedbackResponseVo responseVo = new ObjectiveFeedbackResponseVo();
		responseVo.setId(request.getId());

		ObjectiveFeedbackDraftingAssistanceRequestVo aiRequestVo = new ObjectiveFeedbackDraftingAssistanceRequestVo();
		VisionKraDetailsView kraDetails = visionKraDao.findByIdAndIsActive(request.getId(), IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kraDetails)) {
			aiRequestVo.setCurrentCycle(kraDetails.getCycleName());
			aiRequestVo.setCategory(kraDetails.getCategory());
			aiRequestVo.setCategoryWeight(kraDetails.getCategoryWeight().toString());
			aiRequestVo.setSubCategory(kraDetails.getSubCategory());
			aiRequestVo.setObjectives(kraDetails.getObjectives());
			aiRequestVo.setObjectiveWeight(kraDetails.getObjectiveWeight().toString());
			aiRequestVo.setMetric(kraDetails.getMetric());
			aiRequestVo.setEmployeeRating(kraRatingDAO.getById(kraDetails.getSelfRating()).getValue());
            aiRequestVo.setEmployeeRatingLabel(kraRatingDAO.getById(kraDetails.getSelfRating()).getLabel());
			aiRequestVo.setEmployeeQualitativeAssessment(kraDetails.getSelfQaulitativeAssisment());
            aiRequestVo.setReportingManagerRating(kraRatingDAO.getById(request.getManagerRating().getId()).getValue());
            aiRequestVo.setReportingManagerRatingLabel(kraRatingDAO.getById(request.getManagerRating().getId()).getLabel());
			aiRequestVo.setReportingManagerQualitativeAssessment(request.getManagerQualitativeAssessment());
		}

		//fetch employee details
		//Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long employeeId = kraDetails.getEmployeeId();
		Employee employee = employeeDAO.findActiveEmployeeById(employeeId, IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			EmployeeDetails employeeSelf = aiRequestVo.new EmployeeDetails();
			// Fetch department, devision, and designation of employee
			EmployeeDepartment department = departmentDAO.findByEmployee(employee);
			if (!HRMSHelper.isNullOrEmpty(department)) {
				employeeSelf.setDepartmentName(department.getDepartment().getDepartmentName());
			}

			EmployeeDesignation designation = designationDAO.findByEmployee(employee);
			if (!HRMSHelper.isNullOrEmpty(designation)) {
				employeeSelf.setDesignationName(designation.getDesignation().getDesignationName());
			}

			EmployeeDivision division = employee.getEmployeeDivision();
			if (!HRMSHelper.isNullOrEmpty(division)) {
				employeeSelf.setDivisionName(division.getDivision().getDivisionName());
			}

			EmployeeReportingManager employeeToReportingMapping = reportingmanagerDAO.findByEmployeeAndIsActive(employeeId, ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(employeeToReportingMapping)) {
				Employee reportingManager = employeeToReportingMapping.getReporingManager();
				if (!HRMSHelper.isNullOrEmpty(reportingManager)) {
					EmployeeDetails reportingManagerDetails = aiRequestVo.new EmployeeDetails();
					// Fetch department, designation, and division of reporting manager
					EmployeeDepartment rmDepartment = departmentDAO.findByEmployee(reportingManager);
					if (!HRMSHelper.isNullOrEmpty(rmDepartment)) {
						reportingManagerDetails.setDepartmentName(rmDepartment.getDepartment().getDepartmentName());
					}
					EmployeeDesignation rmDesignation = designationDAO.findByEmployee(reportingManager);
					if (!HRMSHelper.isNullOrEmpty(rmDesignation)) {
						reportingManagerDetails.setDesignationName(rmDesignation.getDesignation().getDesignationName());
					}
					EmployeeDivision rmDivision = reportingManager.getEmployeeDivision();
					if (!HRMSHelper.isNullOrEmpty(rmDivision)) {
						reportingManagerDetails.setDivisionName(rmDivision.getDivision().getDivisionName());
					}
					employeeSelf.setReportingManagerDetails(reportingManagerDetails);
				}
			}
			aiRequestVo.setEmployeeDetails(employeeSelf);
		}

		//Call pmsAiMS API and get response
		try {
			// ObjectMapper mapper = new ObjectMapper();
			// String aiRequestJson = mapper.writeValueAsString(aiRequestVo);
			// log.info("The Payload for PMS AIMS : \n\n" + aiRequestJson + "\n\n\n.");

			if (pmsAiMsEnabled.equalsIgnoreCase("N")) {
				log.info("PMS AIMS is disabled. Returning mock response.");
				responseVo.setManagerQualitativeAssessmentAI("PMS AIMS is disabled. Mock Response : \n\n " + request.getManagerQualitativeAssessment());
			} else {
				log.info("PMS AIMS is enabled. Proceeding to call the API.");

				URI uri = new URI(pmsAiMsBaseUrl + pmsAiMsEndPointManagerDraftingAssistance);
				RestTemplate restTemplate = new RestTemplate();

				// Create headers with API key
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("api-key", pmsAiMsApiKey);

				// Create HTTP entity with body and headers
				HttpEntity<ObjectiveFeedbackDraftingAssistanceRequestVo> requestEntity = new HttpEntity<>(aiRequestVo, headers);

				// Make POST request
				log.info("Calling PMS AIMS API : Request sent ...");
				ResponseEntity<ObjectiveFeedbackDraftingAssistanceResponseVo> result = restTemplate.postForEntity(uri, requestEntity, ObjectiveFeedbackDraftingAssistanceResponseVo.class);
				log.info("Calling PMS AIMS API : Response received ...");

				//check if request is successful
				if (result.getStatusCode() != HttpStatus.OK) {
					log.error("Error in getManagerFeedbackDraft():E1: HTTP Status Code = " + result.getStatusCode());
					throw new HRMSException(1500, "Error while calling AI service : " + result.getStatusCode());
				} else {
					ObjectiveFeedbackDraftingAssistanceResponseVo pmsAiMsResponse = result.getBody();
					if (!HRMSHelper.isNullOrEmpty(pmsAiMsResponse)) {
						responseVo.setManagerQualitativeAssessmentAI(pmsAiMsResponse.getAiFeedback());
					} else {
						log.error("Error in getManagerFeedbackDraft():E2: No Response body from AIMS : " + result.getStatusCode());
						throw new HRMSException(1500, "Error while calling AI service : No Response body : " + result.getStatusCode());
					}
				}
			}
		} catch (Exception e) {
		    e.printStackTrace();
			log.error("Error in getManagerFeedbackDraft():E3:", e);
			throw new HRMSException(1500, "Error while calling AI service: " + e.getMessage());
		}

		baseresponse.setResponseCode(1200);
		baseresponse.setResponseMessage("Success");
		baseresponse.setResponseBody(responseVo);
		return baseresponse;
	}

	// ******************submit VIPMS kra****************

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<String> submitPmsKra(PmsKraVo request) throws Exception {
		log.info("Inside submit Kra method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		// get KRA year
		KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(kraYear)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		MasterRole empRole = roleDao.findByRoleNameOrgIdIsActive(ERole.EMPLOYEE.name(), loggedInEmployee.getOrgId(),
				IHRMSConstants.isActive);
		KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
		isCycleOpen(status, empRole, kraCycle.getId());

		Kra kra = null;
		if (authorizationServiceImpl.isAuthorizedFunctionName("submitKra", role)) {

			if (!HRMSHelper.isNullOrEmpty(rmKraActionEnable)
					&& rmKraActionEnable.equalsIgnoreCase(ERecordStatus.N.name())) {

				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));

			}

			KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getKraCycleId(), IHRMSConstants.isActive);
			kra = kraDao.findByEmployeeIdAndIsActiveAndKraYearAndCycleId(loggedInEmpId, ERecordStatus.Y.name(), kraYear,
					cycle);
			if (!HRMSHelper.isNullOrEmpty(kra)) {
				if (!(kra.getKraWf().getStatus().equals(EKraStatus.INCOMPLETE.name()))) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1599));
				} else {
					submitPmsKraDetails(kra, request);
					Double average = kraDetailsDao.calculateSelfRatingAverage(kra.getId(), IHRMSConstants.isActive,
							IHRMSConstants.ZERO_VALUE);

					if (!HRMSHelper.isNullOrEmpty(average)) {

						Double trimmedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN)
								.doubleValue();

						String finalRating = getFinalRatingByCycle(trimmedAverage, kraYear);
						if (!HRMSHelper.isNullOrEmpty(finalRating)) {
							kra.setTotalSelfRating(finalRating);
                            kra.setAvgSelfRating(trimmedAverage);
						}
					}
				}

			} else {

				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

			addWFStatus(kra, loggedInEmpId, EKraStatus.INPROCESS.name(), ERole.MANAGER.name());

			EmailRequestVO email = new EmailRequestVO();
			email.setEmailAddress(loggedInEmployee.getOfficialEmailId());
			email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
			TemplateVO template = new TemplateVO();
			template.setTemplateName(IHRMSConstants.EMPLOYEE_EMAIL);

			List<PlaceHolderVO> empPlaceHolders = new ArrayList<PlaceHolderVO>();
			PlaceHolderVO empPlaceHolderVo = new PlaceHolderVO();
			empPlaceHolderVo.setExpressionVariableName("empName");
			empPlaceHolderVo.setExpressionVariableValue(loggedInEmployee.getCandidate().getFirstName() + " "
					+ loggedInEmployee.getCandidate().getLastName());
			empPlaceHolders.add(empPlaceHolderVo);
			template.setPlaceHolders(empPlaceHolders);
			email.setTemplateVo(template);

			emailService.insertInEmailTxnTable(email);

			sendMailToManagerEmpAcceptKpi(loggedInEmployee);

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1217));
		log.info("Exit from  saveKra method");

		return response;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<AutoSubmitResponseVo> autoSubmitPmsKra(PmsKraVo request) throws Exception {
		log.info("Inside submit Kra method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		HRMSBaseResponse response = new HRMSBaseResponse<>();
		AutoSubmitResponseVo responseVo = new AutoSubmitResponseVo();

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());

		KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(kraYear)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		MasterRole empRole = roleDao.findByRoleNameOrgIdIsActive(ERole.EMPLOYEE.name(), loggedInEmployee.getOrgId(),
				IHRMSConstants.isActive);
		KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
		isCycleOpen(status, empRole, kraCycle.getId());

		Kra kra = null;

		if (authorizationServiceImpl.isAuthorizedFunctionName("submitKra", role)) {

			if (!HRMSHelper.isNullOrEmpty(rmKraActionEnable)
					&& rmKraActionEnable.equalsIgnoreCase(ERecordStatus.N.name())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

//			if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getCategory())
//					&& request.getCategory().get(0) != null && request.getCategory().get(0).getKraCycleId() != null) {
			if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getCategoryId())
					&& request.getCategoryId() != null && request.getKraCycleId() != null) {

				KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getKraCycleId(), IHRMSConstants.isActive);

				kra = kraDao.findByEmployeeIdAndIsActiveAndKraYearAndCycleId(loggedInEmpId, ERecordStatus.Y.name(),
						kraYear, cycle);

				if (!HRMSHelper.isNullOrEmpty(kra)) {
					if (!kra.getKraWf().getStatus().equals(EKraStatus.INCOMPLETE.name())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1599));
					} else {
						autoSubmitPmsKraDetails(kra, request);
					}
					Double average = kraDetailsDao.calculateSelfRatingAverage(kra.getId(), IHRMSConstants.isActive,
							IHRMSConstants.ZERO_VALUE);

					if (!HRMSHelper.isNullOrEmpty(average)) {
						Double trimmedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN)
								.doubleValue();
						String finalRating = getFinalRatingByCycle(trimmedAverage, kraYear);
						if (!HRMSHelper.isNullOrEmpty(finalRating)) {
							kra.setTotalSelfRating(finalRating);
                            kra.setAvgSelfRating(trimmedAverage);
						}
					}
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}
			} else {
				log.warn("Category or kraCycleId is missing. Skipping autoSubmitPmsKraDetails.");
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		// ===== ✅ Self Rating Logic =====
		if (HRMSHelper.isRolePresent(role, ERole.EMPLOYEE.name())) {
			String totalSelfRatingLabel = kra.getTotalSelfRating();
			KraCycle currentCycle = kra.getCycleId();
			if (shouldTranslateForCycle(currentCycle) && !HRMSHelper.isNullOrEmpty(totalSelfRatingLabel)) {
				totalSelfRatingLabel = kpiHelper.translateRatingLabel(totalSelfRatingLabel);
			}
			responseVo.setTotalSelfRating(totalSelfRatingLabel);
		}

		// ===== ✅ Manager Rating Logic =====
		if (HRMSHelper.isRolePresent(role, ERole.EMPLOYEE.name())) {
			String totalManagerRatingLabel = kra.getFinalRating();
			KraCycle currentCycle = kra.getCycleId();
			if (shouldTranslateForCycle(currentCycle) && !HRMSHelper.isNullOrEmpty(totalManagerRatingLabel)) {
				totalManagerRatingLabel = kpiHelper.translateRatingLabel(totalManagerRatingLabel);
			}
			responseVo.setTotalManagerRating(totalManagerRatingLabel);
		}

		// ✅ Final Response
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1885));
		response.setResponseBody(responseVo);

		log.info("Exit from autoSubmitPmsKra method");
		return response;
	}

	private void isCycleOpen(KraCycleStatus status, MasterRole empRole, Long cycleId) throws HRMSException {
		KraCycleCalender currentRoleCycle = calenderDao.findByIsActiveStatusAndRole(IHRMSConstants.isActive,
				status.getId(), empRole.getId(), cycleId);
		if (HRMSHelper.isNullOrEmpty(currentRoleCycle)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1625));
		}

		ZonedDateTime currentZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));

		Calendar today = Calendar.getInstance();

		java.util.Date utilEndDate = currentRoleCycle.getEndDate();
		Calendar cycleenddate = Calendar.getInstance();
		cycleenddate.setTime(utilEndDate);

		if (today.after(cycleenddate)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1626));
		}
	}

	/*
	 * private void submitPmsKraDetails(Kra kra, PmsKraVo request) throws
	 * HRMSException { log.info("Inside saveKraDetails method"); if
	 * (!HRMSHelper.isNullOrEmpty(request.getCategory())) { List<KraDetails>
	 * kraDetailsList = new ArrayList<KraDetails>(); for (CategoryVo kraDetailsVO :
	 * request.getCategory()) {
	 * 
	 * for (SubCategoryVo subCatVO : kraDetailsVO.getSubcategory()) {
	 * 
	 * for (ObjectiveVo objVo : subCatVO.getObjectives()) {
	 * 
	 * KraDetails kraDetails =
	 * kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId()); if
	 * (!HRMSHelper.isNullOrEmpty(kraDetails)) {
	 * kraHelper.submitPmsKraInputValidation(objVo); kraDetails.setUpdatedDate(new
	 * Date()); kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));
	 * if (!HRMSHelper.isNullOrEmpty(objVo.getSelfrating()) &&
	 * !HRMSHelper.isNullOrEmpty(objVo.getSelfrating().getId())) { KraRating rating
	 * = ratingDao.findById(objVo.getSelfrating().getId()).get();
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(rating)) { kraDetails.setSelfRating(rating); }
	 * }
	 * kraDetails.setSelfQaulitativeAssisment(objVo.getSelfqualitativeassessment());
	 * }
	 * 
	 * kraDetailsList.add(kraDetails);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } // save all records kraDetailsDao.saveAll(kraDetailsList);
	 * 
	 * } log.info("Exit from saveKraDetails method"); }
	 */

	/*
	 * private void autoSubmitPmsKraDetails(Kra kra, PmsKraVo request) throws
	 * HRMSException { log.info("Inside autoSubmitPmsKraDetails method");
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(request.getCategory())) { List<KraDetails>
	 * kraDetailsList = new ArrayList<>();
	 * 
	 * for (CategoryVo kraDetailsVO : request.getCategory()) { for (SubCategoryVo
	 * subCatVO : kraDetailsVO.getSubcategory()) { for (ObjectiveVo objVo :
	 * subCatVO.getObjectives()) {
	 * 
	 * KraDetails kraDetails =
	 * kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId());
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(kraDetails)) { kraDetails.setUpdatedDate(new
	 * Date()); kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));
	 * 
	 * KraRating rating = null; if (objVo.getSelfrating() != null &&
	 * objVo.getSelfrating().getId() != null) { Optional<KraRating> optionalRating =
	 * ratingDao.findById(objVo.getSelfrating().getId()); rating =
	 * optionalRating.orElse(null); } kraDetails.setSelfRating(rating);
	 * 
	 * kraDetails.setSelfQaulitativeAssisment(
	 * !HRMSHelper.isNullOrEmpty(objVo.getSelfqualitativeassessment()) ?
	 * objVo.getSelfqualitativeassessment() : null);
	 * 
	 * kraDetailsList.add(kraDetails); } } } }
	 * 
	 * // Save all updated kraDetails kraDetailsDao.saveAll(kraDetailsList); }
	 * 
	 * log.info("Exit from autoSubmitPmsKraDetails method"); }
	 */

	private void submitPmsKraDetails(Kra kra, PmsKraVo request) throws HRMSException {
		log.info("Inside submitPmsKraDetails method");

		// Validate input
		if (HRMSHelper.isNullOrEmpty(request) || HRMSHelper.isNullOrEmpty(request.getSubcategory())) {
			log.warn("Subcategory list is empty or request is invalid, skipping submission.");
			return;
		}

		List<KraDetails> kraDetailsList = new ArrayList<>();

		// Loop through subcategories
		for (SubCategoryVo subCatVO : request.getSubcategory()) {

			if (HRMSHelper.isNullOrEmpty(subCatVO.getObjectives())) {
				continue;
			}

			// Loop through each objective
			for (ObjectiveVo objVo : subCatVO.getObjectives()) {

				KraDetails kraDetails = kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId());

				if (!HRMSHelper.isNullOrEmpty(kraDetails)) {
					// Validation helper (as in your old code)
					kraHelper.submitPmsKraInputValidation(objVo);

					// Update metadata
					kraDetails.setUpdatedDate(new Date());
					kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));

					// Update self-rating if provided
					if (!HRMSHelper.isNullOrEmpty(objVo.getSelfrating())
							&& !HRMSHelper.isNullOrEmpty(objVo.getSelfrating().getId())) {

						Optional<KraRating> optionalRating = ratingDao.findById(objVo.getSelfrating().getId());
						optionalRating.ifPresent(kraDetails::setSelfRating);
					}

					// Update self qualitative assessment
					kraDetails.setSelfQaulitativeAssisment(objVo.getSelfqualitativeassessment());

					kraDetailsList.add(kraDetails);
				}
			}
		}

		// Save all updated KRA details
		if (!kraDetailsList.isEmpty()) {
			kraDetailsDao.saveAll(kraDetailsList);
		}

		log.info("Exit from submitPmsKraDetails method");
	}

	private void autoSubmitPmsKraDetails(Kra kra, PmsKraVo request) throws HRMSException {
		log.info("Inside autoSubmitPmsKraDetails method");

		if (HRMSHelper.isNullOrEmpty(request) || HRMSHelper.isNullOrEmpty(request.getSubcategory())) {
			log.warn("Subcategory or request is null, skipping auto submission.");
			return;
		}

		List<KraDetails> kraDetailsList = new ArrayList<>();

		for (SubCategoryVo subCatVO : request.getSubcategory()) {
			if (HRMSHelper.isNullOrEmpty(subCatVO.getObjectives())) {
				continue;
			}

			for (ObjectiveVo objVo : subCatVO.getObjectives()) {
				KraDetails kraDetails = kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId());

				if (!HRMSHelper.isNullOrEmpty(kraDetails)) {
					kraDetails.setUpdatedDate(new Date());
					kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));

					// Handle self-rating if available
					KraRating rating = null;
					if (objVo.getSelfrating() != null && objVo.getSelfrating().getId() != null) {
						Optional<KraRating> optionalRating = ratingDao.findById(objVo.getSelfrating().getId());
						rating = optionalRating.orElse(null);
					}
					kraDetails.setSelfRating(rating);

					// Handle self qualitative assessment
					kraDetails
							.setSelfQaulitativeAssisment(!HRMSHelper.isNullOrEmpty(objVo.getSelfqualitativeassessment())
									? objVo.getSelfqualitativeassessment()
									: null);

					kraDetailsList.add(kraDetails);
				}
			}
		}

		// Save all updated KRA details
		if (!kraDetailsList.isEmpty()) {
			kraDetailsDao.saveAll(kraDetailsList);
		}

		log.info("Exit from autoSubmitPmsKraDetails method");
	}

	// ***************save KRA****************************

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<String> savePmsKra(PmsKraVo request) throws HRMSException {
		log.info("Inside saveKra method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		// get KRA year
		KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(kraYear)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		Kra kra = null;
		if (authorizationServiceImpl.isAuthorizedFunctionName("submitKra", role)) {

			KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getKraCycleId(), IHRMSConstants.isActive);
			kra = kraDao.findByEmployeeIdAndIsActiveAndKraYearAndCycleId(loggedInEmpId, ERecordStatus.Y.name(), kraYear,
					cycle);
			if (!HRMSHelper.isNullOrEmpty(kra)) {

				if (!(kra.getKraWf().getStatus().equals(EKraStatus.INCOMPLETE.name())
						|| kra.getKraWf().getStatus().equals(EKraStatus.REJECTED.name()))) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1599));
				} else {
					saveOrSubmitPmsKraDetails(kra, request);
				}

			} else {

				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

			addWFStatus(kra, loggedInEmpId, EKraStatus.INCOMPLETE.name(), ERole.EMPLOYEE.name());

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1220));
		log.info("Exit from  saveKra method");

		return response;

	}

	/*
	 * private void saveOrSubmitPmsKraDetails(Kra kra, PmsKraVo request) throws
	 * HRMSException { log.info("Inside saveKraDetails method"); if
	 * (!HRMSHelper.isNullOrEmpty(request.getCategory())) { List<KraDetails>
	 * kraDetailsList = new ArrayList<KraDetails>(); for (CategoryVo kraDetailsVO :
	 * request.getCategory()) {
	 * 
	 * for (SubCategoryVo subCatVO : kraDetailsVO.getSubcategory()) {
	 * 
	 * for (ObjectiveVo objVo : subCatVO.getObjectives()) {
	 * 
	 * KraDetails kraDetails =
	 * kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId()); if
	 * (!HRMSHelper.isNullOrEmpty(kraDetails)) {
	 * kraHelper.savePmsKraInputValidation(objVo); kraDetails.setUpdatedDate(new
	 * Date()); kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));
	 * if (!HRMSHelper.isNullOrEmpty(objVo.getSelfrating()) &&
	 * !HRMSHelper.isNullOrEmpty(objVo.getSelfrating().getId())) { KraRating rating
	 * = ratingDao.findById(objVo.getSelfrating().getId()).get();
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(rating)) { kraDetails.setSelfRating(rating); }
	 * }
	 * kraDetails.setSelfQaulitativeAssisment(objVo.getSelfqualitativeassessment());
	 * 
	 * }
	 * 
	 * kraDetailsList.add(kraDetails);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } // save all records kraDetailsDao.saveAll(kraDetailsList);
	 * 
	 * } log.info("Exit from saveKraDetails method"); }
	 */

	private void saveOrSubmitPmsKraDetails(Kra kra, PmsKraVo request) throws HRMSException {
		log.info("Inside saveOrSubmitPmsKraDetails method");

		// Validate request and subcategory list
		if (HRMSHelper.isNullOrEmpty(request) || HRMSHelper.isNullOrEmpty(request.getSubcategory())) {
			log.warn("Subcategory list is empty or request is invalid, skipping save/submit operation.");
			return;
		}

		List<KraDetails> kraDetailsList = new ArrayList<>();

		// Loop through subcategories
		for (SubCategoryVo subCatVO : request.getSubcategory()) {

			if (HRMSHelper.isNullOrEmpty(subCatVO.getObjectives())) {
				continue;
			}

			// Loop through objectives
			for (ObjectiveVo objVo : subCatVO.getObjectives()) {

				KraDetails kraDetails = kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId());

				if (!HRMSHelper.isNullOrEmpty(kraDetails)) {

					// Perform validation for Save/Submit scenario
					kraHelper.savePmsKraInputValidation(objVo);

					kraDetails.setUpdatedDate(new Date());
					kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));

					// Set self-rating if provided
					if (!HRMSHelper.isNullOrEmpty(objVo.getSelfrating())
							&& !HRMSHelper.isNullOrEmpty(objVo.getSelfrating().getId())) {

						Optional<KraRating> optionalRating = ratingDao.findById(objVo.getSelfrating().getId());
						optionalRating.ifPresent(kraDetails::setSelfRating);
					}

					// Set self qualitative assessment
					kraDetails.setSelfQaulitativeAssisment(objVo.getSelfqualitativeassessment());

					kraDetailsList.add(kraDetails);
				}
			}
		}

		// Save all updated KRA details
		if (!kraDetailsList.isEmpty()) {
			kraDetailsDao.saveAll(kraDetailsList);
		}

		log.info("Exit from saveOrSubmitPmsKraDetails method");
	}

	@Override
	public HRMSBaseResponse<String> saveKraCycle(KraCycleRequestVo request) throws HRMSException {
		log.info("Inside saveKraCycle method");

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		try {
			KraHelper.validateSaveKraCycleRequest(request);
		} catch (ParseException e) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1763));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.SAVE_KRA_CYCLE, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		KraYear year = krayeardao.getById(request.getYearId());
		if (HRMSHelper.isNullOrEmpty(year)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		int currentYear = LocalDate.now().getYear();
		int requestYear;
		try {
			requestYear = Integer.parseInt(year.getYear());
		} catch (NumberFormatException e) {
			throw new HRMSException(1500, "Invalid year format in system: " + year.getYear());
		}

		if (requestYear < currentYear) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1914));
		}

		if (HRMSHelper.isNullOrEmpty(request.getCycleId())
				&& !HRMSHelper.isNullOrEmpty(kraCycleDAO.findByYearAndCycleName(year, request.getCycleName().trim()))) {
			throw new HRMSException(1630, ResponseCode.getResponseCodeMap().get(1630));
		}

		return createOrUpdateKraCycle(request, response, employeeId, orgId, year);
	}

	private HRMSBaseResponse<String> createOrUpdateKraCycle(KraCycleRequestVo request,
			HRMSBaseResponse<String> response, Long employeeId, Long orgId, KraYear year) throws HRMSException {

		KraCycle cycle;

		if (!HRMSHelper.isNullOrEmpty(request.getCycleId())) {
//			cycle = kraCycleDAO.findByIdAndIsActive(request.getCycleId(), ERecordStatus.Y.name());
//			if (cycle == null) {
//				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
//			}
			cycle = kraCycleDAO.findById(request.getCycleId())
					.orElseThrow(() -> new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201)));

			cycle.setUpdatedBy(employeeId.toString());
			cycle.setUpdatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1871));
		} else {
			cycle = new KraCycle();
			cycle.setCreatedBy(employeeId.toString());
			cycle.setCreatedDate(new Date());
			cycle.setIsActive(ERecordStatus.N.name());
			cycle.setIsLocked(ERecordStatus.N.name());
			cycle.setOrgId(orgId);

			KraCycleStatus closedStatus = kraStatusDao.findByStatus(IHRMSConstants.CLOSED);
			if (HRMSHelper.isNullOrEmpty(closedStatus)) {
				throw new HRMSException(1201, "Closed status not found in system.");
			}
			cycle.setStatus(closedStatus);

			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1629));
		}

		cycle.setCycleName(request.getCycleName());
		cycle.setDescription(request.getDescription());
		cycle.setStartDate(HRMSDateUtil.parse(request.getStartDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		cycle.setEndDate(HRMSDateUtil.parse(request.getEndDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		cycle.setYear(year);

		KraCycleType cycleType = kraCycleTypeDAO.findById(request.getCycleTypeId())
				.orElseThrow(() -> new HRMSException(1201, "Invalid Cycle Type ID: " + request.getCycleTypeId()));
		cycle.setCycleTypeId(cycleType.getId());

		kraCycleDAO.save(cycle);

		response.setResponseCode(1200);
		return response;
	}

	@Override
	public HRMSBaseResponse<String> rejectNewKra(RejectRequestVo request) throws Exception {
		log.info("Inside rejectNewKra method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		if (HRMSHelper.isRolePresent(role, ERole.HOD.name())) {
			if (request.getKraId() != null && request.getCycleId() != null) {
				Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
				if (kra == null) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getCycleId(), ERecordStatus.Y.name());
				if (cycle == null) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraWf kraWf = kraWfDao.findByKraAndCycleIdAndIsActive(kra, cycle, ERecordStatus.Y.name());
				kraWf.setStatus(EKraStatus.REJECTED.name());
				kraWf.setPendingWith(EKraStatus.MANAGER.name());
				kraWf.setComment(request.getComment());
				kraWfDao.save(kraWf);
				// send mail

				Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), kra.getEmployeeId());
				EmailRequestVO email = new EmailRequestVO();
				email.setEmailAddress(employee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId());

				email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
				TemplateVO template = new TemplateVO();
				template.setTemplateName(IHRMSConstants.MANAGER_EMAIL);

				List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
				PlaceHolderVO placeHolderVo = new PlaceHolderVO();
				placeHolderVo.setExpressionVariableName("empName");
				placeHolderVo.setExpressionVariableValue(employee.getEmployeeReportingManager().getReporingManager()
						.getCandidate().getFirstName() + " "
						+ employee.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());
				placeHolders.add(placeHolderVo);

				PlaceHolderVO mcplaceHolderVo = new PlaceHolderVO();
				mcplaceHolderVo.setExpressionVariableName("mcName");
				mcplaceHolderVo.setExpressionVariableValue(loggedInEmployee.getCandidate().getFirstName() + " "
						+ loggedInEmployee.getCandidate().getLastName());
				placeHolders.add(mcplaceHolderVo);

				PlaceHolderVO subplaceHolderVo = new PlaceHolderVO();
				subplaceHolderVo.setExpressionVariableName("subordinateName");
				subplaceHolderVo.setExpressionVariableValue(
						employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
				placeHolders.add(subplaceHolderVo);

				template.setPlaceHolders(placeHolders);

				email.setTemplateVo(template);

				emailService.insertInEmailTxnTable(email);

				EmailRequestVO hodEmail = new EmailRequestVO();
				hodEmail.setEmailAddress(loggedInEmployee.getOfficialEmailId());
				hodEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

				TemplateVO hodTemplate = new TemplateVO();
				hodTemplate.setTemplateName(IHRMSConstants.MC_MEMBER_EMAIL);

				List<PlaceHolderVO> hodplaceHolders = new ArrayList<PlaceHolderVO>();
				PlaceHolderVO hodplaceHolderVo = new PlaceHolderVO();
				hodplaceHolderVo.setExpressionVariableName("empName");
				hodplaceHolderVo.setExpressionVariableValue(loggedInEmployee.getCandidate().getFirstName() + " "
						+ loggedInEmployee.getCandidate().getLastName());
				hodplaceHolders.add(hodplaceHolderVo);

				PlaceHolderVO empplaceHolderVo = new PlaceHolderVO();
				empplaceHolderVo.setExpressionVariableName("subordinateName");
				empplaceHolderVo.setExpressionVariableValue(
						employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
				hodplaceHolders.add(empplaceHolderVo);

				hodTemplate.setPlaceHolders(hodplaceHolders);

				hodEmail.setTemplateVo(hodTemplate);

				emailService.insertInEmailTxnTable(hodEmail);

				response.setResponseBody(" Rejected Successfully");
				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1607));
				log.info("Exit from  rejectNewKra method");

				return response;
			} else {
				response.setResponseBody("Requet can't be null");
				response.setResponseCode(1501);
				return response;
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

	}

	// *******************submit manager rating**************

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<String> submitManagerRating(PmsKraVo request) throws Exception {
		log.info("Inside submit Manager Rating method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		// get KRA year
		KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(kraYear)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		MasterRole managerRole = roleDao.findByRoleNameOrgIdIsActive(ERole.MANAGER.name(), loggedInEmployee.getOrgId(),
				IHRMSConstants.isActive);
		KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
		isCycleOpen(status, managerRole, kraCycle.getId());

		Kra kra = null;
		if (authorizationServiceImpl.isAuthorizedFunctionName("submitRmRating", role)) {
			kra = kraDao.findByIdAndIsActiveAndKraYear(request.getKraId(), ERecordStatus.Y.name(), kraYear);
			if (!HRMSHelper.isNullOrEmpty(kra)) {

				Long reportingmanagerId = reportingManagerDao.findReportingManagerIdByEmployeeId(kra.getEmployeeId());

				if (loggedInEmpId.equals(reportingmanagerId)) {
					if (kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.INPROCESS.name())
							|| kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.REJECTED.name())) {
						// submit rating
						submitPmsManagerRating(kra, request);
						addWFStatus(kra, reportingmanagerId, EKraStatus.SUBMITTED.name(), ERole.DIVISIONHEAD.name());
						Double average = kraDetailsDao.calculateAverage(kra.getId(), IHRMSConstants.isActive,
								IHRMSConstants.ZERO_VALUE);

						if (!HRMSHelper.isNullOrEmpty(average)) {

							Double trimmedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN)
									.doubleValue();

							String finalRating = getFinalRatingByCycle(trimmedAverage, kraYear);
							if (!HRMSHelper.isNullOrEmpty(finalRating)) {
								kra.setFinalRating(finalRating);
                                kra.setAvgRmRating(trimmedAverage);
							}

						}
						kraDao.save(kra);
						sendMailsOnManagerSubmit(loggedInEmployee, kra);

					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
					}

				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}

			} else {

				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1217));
		log.info("Exit from  saveKra method");

		return response;

	}

	private String getFinalRatingByCycle(Double trimmedAverage, KraYear kraYear) {

		KraCycle activeCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());

		if (!HRMSHelper.isNullOrEmpty(activeCycle)) {
			String cycleName = activeCycle.getCycleName();

			if (cycleName.equalsIgnoreCase(IHRMSConstants.HALF_YEAR_CYCLE)
					|| cycleName.equalsIgnoreCase(IHRMSConstants.MID_YEAR_CYCLE)) {
				KraRatingRange range = rangeDao.findByAverage(trimmedAverage);
				if (!HRMSHelper.isNullOrEmpty(range)) {
					return range.getRating();
				}
			} else {
				KraRatingRange range = rangeDao.findByAverage(trimmedAverage);
				if (!HRMSHelper.isNullOrEmpty(range)) {
					return range.getRating();
				}
			}
		}

		return null;
	}

	private void sendMailsOnManagerSubmit(Employee loggedInEmployee, Kra kra) throws Exception {
		sendMailToEmployeeOnManagerSubmit(kra);
		// mail to manager
		sendMailToManagerOnSubmitRating(loggedInEmployee, kra);

		// mail to MC
		sendMailToMcOnManagerSubmit(kra);
	}

	private void sendMailToMcOnManagerSubmit(Kra kra) throws Exception {
		if (kra == null || kra.getEmployee() == null) {
			log.warn("KRA or its employee is null. Cannot proceed.");
			return;
		}

		Candidate kraCandidate = kra.getEmployee().getCandidate();
		if (kraCandidate == null || kraCandidate.getCandidateProfessionalDetail() == null
				|| kraCandidate.getCandidateProfessionalDetail().getDepartment() == null) {
			log.warn("KRA's candidate professional details or department are missing.");
			return;
		}

		Long departmentId = kraCandidate.getCandidateProfessionalDetail().getDepartment().getId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		HodToDepartmentMap hod = hodToDepartmentMap.findByDepartmentIdAndOrgIdAndIsActive(departmentId, orgId,
				IHRMSConstants.isActive);

		if (hod == null || hod.getEmployeeId() == null) {
			log.warn("No HOD mapping found for departmentId: {} and orgId: {}", departmentId, orgId);
			return;
		}

		Employee employee = employeeDAO.findActiveEmployeeById(hod.getEmployeeId(), ERecordStatus.Y.name());

		if (!ObjectUtils.allNotNull(employee, employee.getOfficialEmailId(), employee.getCandidate())) {
			log.warn("Employee or required fields are null for employeeId: {}", hod.getEmployeeId());
			return;
		}

		EmailRequestVO mcEmail = new EmailRequestVO();
		mcEmail.setEmailAddress(employee.getOfficialEmailId());
		mcEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

		TemplateVO mcTemplate = new TemplateVO();
		mcTemplate.setTemplateName(IHRMSConstants.KPI_PENDING_TO_MC);

		List<PlaceHolderVO> mcPlaceHolders = new ArrayList<>();

		PlaceHolderVO mcPlaceHolderVo = new PlaceHolderVO();
		mcPlaceHolderVo.setExpressionVariableName("empName");
		mcPlaceHolderVo.setExpressionVariableValue(
				employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		mcPlaceHolders.add(mcPlaceHolderVo);

		if (ObjectUtils.allNotNull(kraCandidate.getFirstName(), kraCandidate.getLastName())) {
			PlaceHolderVO subPlaceHolderVo = new PlaceHolderVO();
			subPlaceHolderVo.setExpressionVariableName("subordinateName");
			subPlaceHolderVo.setExpressionVariableValue(kraCandidate.getFirstName() + " " + kraCandidate.getLastName());
			mcPlaceHolders.add(subPlaceHolderVo);
		}

		mcTemplate.setPlaceHolders(mcPlaceHolders);
		mcEmail.setTemplateVo(mcTemplate);

		emailService.insertInEmailTxnTable(mcEmail);
	}

	private void sendMailToManagerOnSubmitRating(Employee loggedInEmployee, Kra kra) throws Exception {
		EmailRequestVO managerEmail = new EmailRequestVO();
		managerEmail.setEmailAddress(loggedInEmployee.getOfficialEmailId());
		managerEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
		TemplateVO managerTemplate = new TemplateVO();
		managerTemplate.setTemplateName(IHRMSConstants.MANAGER_SUBMIT_EMAIL_FOR_HIMSELF);

		List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName("empName");
		placeHolderVo.setExpressionVariableValue(
				loggedInEmployee.getCandidate().getFirstName() + " " + loggedInEmployee.getCandidate().getLastName());

		PlaceHolderVO subPlaceHolderVo = new PlaceHolderVO();
		subPlaceHolderVo.setExpressionVariableName("subordinateName");
		subPlaceHolderVo.setExpressionVariableValue(
				kra.getEmployee().getCandidate().getFirstName() + " " + kra.getEmployee().getCandidate().getLastName());

		placeHolders.add(placeHolderVo);
		placeHolders.add(subPlaceHolderVo);
		managerTemplate.setPlaceHolders(placeHolders);

		managerEmail.setTemplateVo(managerTemplate);
		emailService.insertInEmailTxnTable(managerEmail);
	}

	private void sendMailToEmployeeOnManagerSubmit(Kra kra) throws Exception {
		EmailRequestVO email = new EmailRequestVO();
		email.setEmailAddress(kra.getEmployee().getOfficialEmailId());
		email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

		TemplateVO template = new TemplateVO();
		template.setTemplateName(IHRMSConstants.MANAGER_SUBMIT_EMAIL);

		List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName("empName");
		placeHolderVo.setExpressionVariableValue(
				kra.getEmployee().getCandidate().getFirstName() + " " + kra.getEmployee().getCandidate().getLastName());
		placeHolders.add(placeHolderVo);
		template.setPlaceHolders(placeHolders);
		email.setTemplateVo(template);
		emailService.insertInEmailTxnTable(email);
	}

	/*
	 * private void submitPmsManagerRating(Kra kra, PmsKraVo request) throws
	 * HRMSException { log.info("Inside submit manager rating method"); if
	 * (!HRMSHelper.isNullOrEmpty(request.getCategory())) { List<KraDetails>
	 * kraDetailsList = new ArrayList<KraDetails>(); for (CategoryVo kraDetailsVO :
	 * request.getCategory()) {
	 * 
	 * for (SubCategoryVo subCatVO : kraDetailsVO.getSubcategory()) {
	 * 
	 * for (ObjectiveVo objVo : subCatVO.getObjectives()) {
	 * 
	 * KraDetails kraDetails =
	 * kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId()); if
	 * (!HRMSHelper.isNullOrEmpty(kraDetails)) {
	 * kraHelper.submitManagerRatingInputValidation(objVo);
	 * kraDetails.setUpdatedDate(new Date());
	 * kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(objVo.getManagerrating()) &&
	 * !HRMSHelper.isNullOrEmpty(objVo.getManagerrating().getId())) { KraRating
	 * rating = ratingDao.findById(objVo.getManagerrating().getId()).get();
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(rating)) { kraDetails.setManagerRating(rating);
	 * } }
	 * 
	 * kraDetails.setManagerQaulitativeAssisment(objVo.
	 * getManagerqaulitativeassisment()); }
	 * 
	 * kraDetailsList.add(kraDetails);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } // save all records kraDetailsDao.saveAll(kraDetailsList);
	 * 
	 * } log.info("Exit from saveKraDetails method"); }
	 */

	private void submitPmsManagerRating(Kra kra, PmsKraVo request) throws HRMSException {
		log.info("Inside submitPmsManagerRating method");

		// Validate request
		if (HRMSHelper.isNullOrEmpty(request) || HRMSHelper.isNullOrEmpty(request.getSubcategory())) {
			log.warn("Subcategory list is empty or request is invalid, skipping manager rating submission.");
			return;
		}

		List<KraDetails> kraDetailsList = new ArrayList<>();

		// Loop through subcategories
		for (SubCategoryVo subCatVO : request.getSubcategory()) {

			if (HRMSHelper.isNullOrEmpty(subCatVO.getObjectives())) {
				continue;
			}

			// Loop through objectives
			for (ObjectiveVo objVo : subCatVO.getObjectives()) {

				KraDetails kraDetails = kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId());

				if (!HRMSHelper.isNullOrEmpty(kraDetails)) {
					// Validate input fields before update
					kraHelper.submitManagerRatingInputValidation(objVo);

					kraDetails.setUpdatedDate(new Date());
					kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));

					// Set Manager Rating if available
					if (!HRMSHelper.isNullOrEmpty(objVo.getManagerrating())
							&& !HRMSHelper.isNullOrEmpty(objVo.getManagerrating().getId())) {

						Optional<KraRating> optionalRating = ratingDao.findById(objVo.getManagerrating().getId());
						optionalRating.ifPresent(kraDetails::setManagerRating);
					}

					// Set Manager qualitative assessment
					kraDetails.setManagerQaulitativeAssisment(objVo.getManagerqaulitativeassisment());

					kraDetailsList.add(kraDetails);
				}
			}
		}

		// Save all updated KRA details
		if (!kraDetailsList.isEmpty()) {
			kraDetailsDao.saveAll(kraDetailsList);
		}

		log.info("Exit from submitPmsManagerRating method");
	}

	/*
	 * @Override
	 * 
	 * @Transactional(rollbackFor = Exception.class) public
	 * HRMSBaseResponse<AutoSubmitResponseVo> autoSubmitManagerRating(PmsKraVo
	 * request) throws Exception {
	 * log.info("Inside Auto submit Manager Rating method"); List<String> role =
	 * SecurityFilter.TL_CLAIMS.get().getRoles(); Long loggedInEmpId =
	 * SecurityFilter.TL_CLAIMS.get().getEmployeeId();
	 * 
	 * HRMSBaseResponse<AutoSubmitResponseVo> response = new HRMSBaseResponse<>();
	 * AutoSubmitResponseVo responseVo = new AutoSubmitResponseVo();
	 * 
	 * // ===== ✅ Fetch Employee & KRA Year ===== Employee loggedInEmployee =
	 * employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
	 * KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
	 * 
	 * if (HRMSHelper.isNullOrEmpty(kraYear)) { throw new HRMSException(1500,
	 * ResponseCode.getResponseCodeMap().get(1201)); }
	 * 
	 * // ===== ✅ Validate Cycle Status ===== KraCycleStatus status =
	 * kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN); MasterRole managerRole =
	 * roleDao.findByRoleNameOrgIdIsActive(ERole.MANAGER.name(),
	 * loggedInEmployee.getOrgId(), IHRMSConstants.isActive); KraCycle kraCycle =
	 * kraCycleDAO.findByIsActive(ERecordStatus.Y.name()); isCycleOpen(status,
	 * managerRole, kraCycle.getId());
	 * 
	 * // ===== ✅ Validate Input ===== if (request.getKraId() == null) { throw new
	 * HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521)); }
	 * 
	 * Kra kra = null;
	 * 
	 * // ===== ✅ Authorization Check ===== if
	 * (authorizationServiceImpl.isAuthorizedFunctionName("submitRmRating", role)) {
	 * kra = kraDao.findByIdAndIsActiveAndKraYear(request.getKraId(),
	 * ERecordStatus.Y.name(), kraYear);
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(kra)) { Long reportingManagerId =
	 * reportingManagerDao.findReportingManagerIdByEmployeeId(kra.getEmployeeId());
	 * 
	 * if (loggedInEmpId.equals(reportingManagerId)) { if
	 * (EKraStatus.INPROCESS.name().equalsIgnoreCase(kra.getKraWf().getStatus()) ||
	 * EKraStatus.REJECTED.name().equalsIgnoreCase(kra.getKraWf().getStatus())) {
	 * 
	 * // ===== ✅ Validate Request Category Structure ===== if
	 * (!HRMSHelper.isNullOrEmpty(request.getCategory())) { boolean skipProcessing =
	 * false;
	 * 
	 * for (CategoryVo categoryVo : request.getCategory()) { if (categoryVo == null
	 * || categoryVo.getSubcategory() == null) { skipProcessing = true; break; } for
	 * (SubCategoryVo subCategoryVo : categoryVo.getSubcategory()) { if
	 * (subCategoryVo == null || subCategoryVo.getObjectives() == null) {
	 * skipProcessing = true; break; } } }
	 * 
	 * if (!skipProcessing) { autosubmitPmsManagerRating(kra, request); } else {
	 * log.
	 * warn("Skipping autosubmitPmsManagerRating due to null subcategory/objectives"
	 * ); }
	 * 
	 * // ===== ✅ Calculate Manager Rating ===== Double average =
	 * kraDetailsDao.calculateAverage(kra.getId(), IHRMSConstants.isActive,
	 * IHRMSConstants.ZERO_VALUE);
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(average)) { Double trimmedAverage =
	 * BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN) .doubleValue();
	 * 
	 * String finalRating = getFinalRatingByCycle(trimmedAverage, kraYear); if
	 * (!HRMSHelper.isNullOrEmpty(finalRating)) { kra.setFinalRating(finalRating); }
	 * }
	 * 
	 * } else {
	 * log.warn("Skipping autosubmitPmsManagerRating due to null category list"); }
	 * 
	 * kraDao.save(kra);
	 * 
	 * } else { throw new HRMSException(1500,
	 * ResponseCode.getResponseCodeMap().get(1521)); } } else { throw new
	 * HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521)); } } else {
	 * throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521)); }
	 * } else { throw new HRMSException(1500,
	 * ResponseCode.getResponseCodeMap().get(1521)); }
	 * 
	 * // ===== ✅ Self Rating Logic ===== if (HRMSHelper.isRolePresent(role,
	 * ERole.EMPLOYEE.name()) &&
	 * EKraStatus.INCOMPLETE.name().equals(kra.getKraWf().getStatus())) {
	 * responseVo.setTotalSelfRating(null); } else { String totalSelfRatingLabel =
	 * kra.getTotalSelfRating(); KraCycle currentCycle = kra.getCycleId(); if
	 * (shouldTranslateForCycle(currentCycle) &&
	 * !HRMSHelper.isNullOrEmpty(totalSelfRatingLabel)) { totalSelfRatingLabel =
	 * kpiHelper.translateRatingLabel(totalSelfRatingLabel); }
	 * responseVo.setTotalSelfRating(totalSelfRatingLabel); }
	 * 
	 * // ===== ✅ Manager Rating Logic ===== if (HRMSHelper.isRolePresent(role,
	 * ERole.EMPLOYEE.name()) &&
	 * EKraStatus.INCOMPLETE.name().equals(kra.getKraWf().getStatus())) {
	 * responseVo.setTotalManagerRating(null); } else { String
	 * totalManagerRatingLabel = kra.getFinalRating(); KraCycle currentCycle =
	 * kra.getCycleId(); if (shouldTranslateForCycle(currentCycle) &&
	 * !HRMSHelper.isNullOrEmpty(totalManagerRatingLabel)) { totalManagerRatingLabel
	 * = kpiHelper.translateRatingLabel(totalManagerRatingLabel); }
	 * responseVo.setTotalManagerRating(totalManagerRatingLabel); }
	 * 
	 * // ===== ✅ Final Response ===== response.setResponseCode(1200);
	 * response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1885));
	 * response.setResponseBody(responseVo);
	 * 
	 * log.info("Exit from auto submit manager method"); return response; }
	 */

	/*
	 * private void autosubmitPmsManagerRating(Kra kra, PmsKraVo request) {
	 * log.info("Inside autosubmit manager rating method");
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(request.getCategory())) { List<KraDetails>
	 * kraDetailsList = new ArrayList<>();
	 * 
	 * for (CategoryVo kraDetailsVO : request.getCategory()) { for (SubCategoryVo
	 * subCatVO : kraDetailsVO.getSubcategory()) { for (ObjectiveVo objVo :
	 * subCatVO.getObjectives()) {
	 * 
	 * KraDetails kraDetails =
	 * kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId());
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(kraDetails)) { kraDetails.setUpdatedDate(new
	 * Date()); kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));
	 * 
	 * KraRating rating = null; if (objVo.getManagerrating() != null &&
	 * objVo.getManagerrating().getId() != null) { Optional<KraRating>
	 * optionalRating = ratingDao .findById(objVo.getManagerrating().getId());
	 * rating = optionalRating.orElse(null); } kraDetails.setManagerRating(rating);
	 * 
	 * kraDetails.setManagerQaulitativeAssisment(
	 * !HRMSHelper.isNullOrEmpty(objVo.getManagerqaulitativeassisment()) ?
	 * objVo.getManagerqaulitativeassisment() : null); }
	 * 
	 * kraDetailsList.add(kraDetails); } } }
	 * 
	 * kraDetailsDao.saveAll(kraDetailsList); }
	 * 
	 * log.info("Exit from autosubmit manager rating method"); }
	 */

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<AutoSubmitResponseVo> autoSubmitManagerRating(PmsKraVo request) throws Exception {
		log.info("Inside autoSubmitManagerRating method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		HRMSBaseResponse<AutoSubmitResponseVo> response = new HRMSBaseResponse<>();
		AutoSubmitResponseVo responseVo = new AutoSubmitResponseVo();

		// ===== ✅ Fetch Employee & KRA Year =====
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
		if (kraYear == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		// ===== ✅ Validate Cycle Status =====
		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		MasterRole managerRole = roleDao.findByRoleNameOrgIdIsActive(ERole.MANAGER.name(), loggedInEmployee.getOrgId(),
				IHRMSConstants.isActive);
		KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
		isCycleOpen(status, managerRole, kraCycle.getId());

		// ===== ✅ Validate Input =====
		if (request.getKraId() == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		// ===== ✅ Authorization Check =====
		if (!authorizationServiceImpl.isAuthorizedFunctionName("submitRmRating", role)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		Kra kra = kraDao.findByIdAndIsActiveAndKraYear(request.getKraId(), ERecordStatus.Y.name(), kraYear);
		if (kra == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		Long reportingManagerId = reportingManagerDao.findReportingManagerIdByEmployeeId(kra.getEmployeeId());
		if (!loggedInEmpId.equals(reportingManagerId)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		// ===== ✅ Check Status before Auto Submit =====
		if (!(EKraStatus.INPROCESS.name().equalsIgnoreCase(kra.getKraWf().getStatus())
				|| EKraStatus.REJECTED.name().equalsIgnoreCase(kra.getKraWf().getStatus()))) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		// ===== ✅ Validate Request Structure (New Payload) =====
		if (HRMSHelper.isNullOrEmpty(request.getSubcategory())) {
			log.warn("Skipping autosubmitPmsManagerRating due to null subcategory list");
		} else {
			boolean hasObjectives = request.getSubcategory().stream()
					.anyMatch(sub -> sub != null && !HRMSHelper.isNullOrEmpty(sub.getObjectives()));

			if (hasObjectives) {
				autosubmitPmsManagerRating(kra, request);
			} else {
				log.warn("Skipping autosubmitPmsManagerRating due to null or empty objectives");
			}
		}

		// ===== ✅ Calculate Manager Rating =====
		Double average = kraDetailsDao.calculateAverage(kra.getId(), IHRMSConstants.isActive,
				IHRMSConstants.ZERO_VALUE);
		if (average != null) {
			Double trimmedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN).doubleValue();

			String finalRating = getFinalRatingByCycle(trimmedAverage, kraYear);
			if (!HRMSHelper.isNullOrEmpty(finalRating)) {
				kra.setFinalRating(finalRating);
                kra.setAvgRmRating(trimmedAverage);
			}
		}

		kraDao.save(kra);

		// ===== ✅ Self Rating Logic =====
		if (HRMSHelper.isRolePresent(role, ERole.EMPLOYEE.name())
				&& EKraStatus.INCOMPLETE.name().equals(kra.getKraWf().getStatus())) {
			responseVo.setTotalSelfRating(null);
		} else {
			String totalSelfRatingLabel = kra.getTotalSelfRating();
			KraCycle currentCycle = kra.getCycleId();
			if (shouldTranslateForCycle(currentCycle) && !HRMSHelper.isNullOrEmpty(totalSelfRatingLabel)) {
				totalSelfRatingLabel = kpiHelper.translateRatingLabel(totalSelfRatingLabel);
			}
			responseVo.setTotalSelfRating(totalSelfRatingLabel);
		}

		// ===== ✅ Manager Rating Logic =====
		if (HRMSHelper.isRolePresent(role, ERole.EMPLOYEE.name())
				&& EKraStatus.INCOMPLETE.name().equals(kra.getKraWf().getStatus())) {
			responseVo.setTotalManagerRating(null);
		} else {
			String totalManagerRatingLabel = kra.getFinalRating();
			KraCycle currentCycle = kra.getCycleId();
			if (shouldTranslateForCycle(currentCycle) && !HRMSHelper.isNullOrEmpty(totalManagerRatingLabel)) {
				totalManagerRatingLabel = kpiHelper.translateRatingLabel(totalManagerRatingLabel);
			}
			responseVo.setTotalManagerRating(totalManagerRatingLabel);
		}

		// ===== ✅ Final Response =====
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1885));
		response.setResponseBody(responseVo);

		log.info("Exit from autoSubmitManagerRating method");
		return response;
	}

	private void autosubmitPmsManagerRating(Kra kra, PmsKraVo request) {
		log.info("Inside autosubmitPmsManagerRating method");

		if (request == null || HRMSHelper.isNullOrEmpty(request.getSubcategory())) {
			log.warn("No subcategories found in request payload");
			return;
		}

		List<KraDetails> kraDetailsList = new ArrayList<>();

		for (SubCategoryVo subCatVO : request.getSubcategory()) {
			if (HRMSHelper.isNullOrEmpty(subCatVO.getObjectives())) {
				continue;
			}

			for (ObjectiveVo objVo : subCatVO.getObjectives()) {
				KraDetails kraDetails = kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId());

				if (kraDetails != null) {
					kraDetails.setUpdatedDate(new Date());
					kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));

					// Set category and subcategory for traceability (if these fields exist)
					if (request.getCategoryId() != null) {
						kraDetails.setCategoryId(request.getCategoryId());
					}
					if (subCatVO.getSubcategoryId() != null) {
						kraDetails.setSubcategoryId(subCatVO.getSubcategoryId());
					}

					// Set manager rating (if provided)
					KraRating rating = null;
					if (objVo.getManagerrating() != null && objVo.getManagerrating().getId() != null) {
						rating = ratingDao.findById(objVo.getManagerrating().getId()).orElse(null);
					}
					kraDetails.setManagerRating(rating);

					// Set manager qualitative assessment (if provided)
					kraDetails.setManagerQaulitativeAssisment(
							!HRMSHelper.isNullOrEmpty(objVo.getManagerqaulitativeassisment())
									? objVo.getManagerqaulitativeassisment()
									: null);

					kraDetailsList.add(kraDetails);
				}
			}
		}

		if (!kraDetailsList.isEmpty()) {
			kraDetailsDao.saveAll(kraDetailsList);
			log.info("Auto-submitted manager ratings saved for {} objectives", kraDetailsList.size());
		} else {
			log.warn("No KraDetails records found to update for autosubmit manager rating");
		}

		log.info("Exit from autosubmitPmsManagerRating method");
	}

	@Override
	public HRMSBaseResponse<String> saveCalibration(CalibrationRequestVo request) throws Exception {
		log.info("Inside submitCalibration method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HOD.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (request.getEmployeeId() == null || request.getComment() == null) {
			response.setResponseBody("Request can't be null");
			response.setResponseCode(1501);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1501));
			return response;
		}

		Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), request.getEmployeeId());
		if (employee == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getCycleId(), ERecordStatus.Y.name());
		if (cycle == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
		if (kra == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		HodCycleFinalRating finalRating = hodcycleDao.findByEmployeeAndMcMemberIdAndCycleIdAndIsActive(employee,
				loggedInEmployee, cycle, ERecordStatus.Y.name());

		if (finalRating == null) {
			return submitNewCalibration(request, employee, kra, cycle, loggedInEmployee);
		} else {
			return updateCalibration(request, employee, kra, cycle, loggedInEmployee, finalRating);
		}
	}

	private HRMSBaseResponse<String> submitNewCalibration(CalibrationRequestVo request, Employee employee, Kra kra,
			KraCycle cycle, Employee loggedInEmployee) throws Exception {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		if (request.getRating() == null || request.getRating().getId() == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		KraRating rating = ratingDao.findByIsActiveAndId(ERecordStatus.Y.name(), request.getRating().getId());
		if (rating == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		MasterRole empRole = roleDao.findByRoleNameOrgIdIsActive(ERole.HOD.name(), loggedInEmployee.getOrgId(),
				IHRMSConstants.isActive);
		KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
		isCycleOpen(status, empRole, kraCycle.getId());

		HodCycleFinalRating finalRating = new HodCycleFinalRating();
		finalRating.setComment(request.getComment());
		finalRating.setEmployee(employee);
		finalRating.setMcMemberId(loggedInEmployee);
		finalRating.setCycleId(cycle);
		finalRating.setKra(kra);
		finalRating.setScore(rating);
		finalRating.setOrgId(loggedInEmployee.getOrgId());
		finalRating.setIsActive(ERecordStatus.Y.name());

		hodcycleDao.save(finalRating);
		addWFStatus(kra, loggedInEmployee.getId(), EKraStatus.APPROVED.name(), ERole.HR.name());

		sendCalibrationMail(employee, loggedInEmployee);

		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1218));
		response.setResponseCode(1200);
		log.info("Exit from submitNewCalibration method");

		return response;
	}

	private HRMSBaseResponse<String> updateCalibration(CalibrationRequestVo request, Employee employee, Kra kra,
			KraCycle cycle, Employee loggedInEmployee, HodCycleFinalRating finalRating) throws Exception {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		finalRating.setComment(request.getComment());
		finalRating.setEmployee(employee);
		finalRating.setMcMemberId(loggedInEmployee);
		finalRating.setCycleId(cycle);
		finalRating.setKra(kra);
		finalRating.setOrgId(loggedInEmployee.getOrgId());
		finalRating.setIsActive(ERecordStatus.Y.name());

		if (request.getRating() != null && request.getRating().getId() != null) {
			KraRating rating = ratingDao.findByIsActiveAndId(ERecordStatus.Y.name(), request.getRating().getId());
			if (rating != null) {
				finalRating.setScore(rating);
			}

		}

		hodcycleDao.save(finalRating);
		addWFStatus(kra, loggedInEmployee.getId(), EKraStatus.APPROVED.name(), ERole.HR.name());

		sendCalibrationMail(employee, loggedInEmployee);

		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1218));
		response.setResponseCode(1200);
		log.info("Exit from updateCalibration method");

		return response;
	}

	private void sendCalibrationMail(Employee employee, Employee loggedInEmployee) throws Exception {
		EmailRequestVO empEmail = new EmailRequestVO();
		empEmail.setEmailAddress(employee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId());
		empEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

		TemplateVO empTemplate = new TemplateVO();
		empTemplate.setTemplateName(IHRMSConstants.HOD_CALIBRATION_EMAIL);

		List<PlaceHolderVO> placeHolders = new ArrayList<>();

		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName("empName");
		placeHolderVo.setExpressionVariableValue(
				loggedInEmployee.getCandidate().getFirstName() + " " + loggedInEmployee.getCandidate().getLastName());
		placeHolders.add(placeHolderVo);

		PlaceHolderVO subordinateVo = new PlaceHolderVO();
		subordinateVo.setExpressionVariableName("subordinateName");
		subordinateVo.setExpressionVariableValue(
				employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		placeHolders.add(subordinateVo);

		empTemplate.setPlaceHolders(placeHolders);
		empEmail.setTemplateVo(empTemplate);

		emailService.insertInEmailTxnTable(empEmail);
	}

	// -------save manager rating-----------

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<String> saveManagerRating(PmsKraVo request) throws HRMSException {
		log.info("Inside save Manager Rating method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		// get KRA year
		KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(kraYear)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		MasterRole managerRole = roleDao.findByRoleNameOrgIdIsActive(ERole.MANAGER.name(), loggedInEmployee.getOrgId(),
				IHRMSConstants.isActive);
		KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
		isCycleOpen(status, managerRole, kraCycle.getId());

		Kra kra = null;
		if (authorizationServiceImpl.isAuthorizedFunctionName("submitRmRating", role)) {
			kra = kraDao.findByIdAndIsActiveAndKraYear(request.getKraId(), ERecordStatus.Y.name(), kraYear);
			if (!HRMSHelper.isNullOrEmpty(kra)) {

				Long reportingmanagerId = reportingManagerDao.findReportingManagerIdByEmployeeId(kra.getEmployeeId());

				if (loggedInEmpId.equals(reportingmanagerId)) {
					if (kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.INPROCESS.name())
							|| kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.REJECTED.name())) {
						// submit rating
						savePmsManagerRating(kra, request);
						// addWFStatus(kra, reportingmanagerId, EKraStatus.APPROVED.name(),
						// ERole.HOD.name());

						kraDao.save(kra);

					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
					}

				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}

			} else {

				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1220));
		log.info("Exit from  saveKra method");

		return response;

	}

	/*
	 * private void savePmsManagerRating(Kra kra, PmsKraVo request) throws
	 * HRMSException { log.info("Inside save manager rating method"); if
	 * (!HRMSHelper.isNullOrEmpty(request.getCategory())) { List<KraDetails>
	 * kraDetailsList = new ArrayList<KraDetails>(); for (CategoryVo kraDetailsVO :
	 * request.getCategory()) {
	 * 
	 * for (SubCategoryVo subCatVO : kraDetailsVO.getSubcategory()) {
	 * 
	 * for (ObjectiveVo objVo : subCatVO.getObjectives()) {
	 * 
	 * KraDetails kraDetails =
	 * kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId()); if
	 * (!HRMSHelper.isNullOrEmpty(kraDetails)) {
	 * kraHelper.saveManagerRatingValidation(objVo); kraDetails.setUpdatedDate(new
	 * Date()); kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(objVo.getManagerrating()) &&
	 * !HRMSHelper.isNullOrEmpty(objVo.getManagerrating().getId())) { KraRating
	 * rating = ratingDao.findById(objVo.getManagerrating().getId()).get();
	 * 
	 * if (!HRMSHelper.isNullOrEmpty(rating)) { kraDetails.setManagerRating(rating);
	 * } }
	 * 
	 * kraDetails.setManagerQaulitativeAssisment(objVo.
	 * getManagerqaulitativeassisment()); }
	 * 
	 * kraDetailsList.add(kraDetails);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } // save all records kraDetailsDao.saveAll(kraDetailsList);
	 * 
	 * } log.info("Exit from saveKraDetails method"); }
	 */

	private void savePmsManagerRating(Kra kra, PmsKraVo request) throws HRMSException {
		log.info("Inside savePmsManagerRating method");

		// Check if subcategories exist in the request
		if (!HRMSHelper.isNullOrEmpty(request.getSubcategory())) {
			List<KraDetails> kraDetailsList = new ArrayList<>();

			for (SubCategoryVo subCatVO : request.getSubcategory()) {
				if (HRMSHelper.isNullOrEmpty(subCatVO.getObjectives())) {
					continue;
				}

				for (ObjectiveVo objVo : subCatVO.getObjectives()) {
					KraDetails kraDetails = kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId());

					if (!HRMSHelper.isNullOrEmpty(kraDetails)) {
						// Perform input validation for manager rating
						kraHelper.saveManagerRatingValidation(objVo);

						kraDetails.setUpdatedDate(new Date());
						kraDetails.setUpdatedBy(String.valueOf(kra.getEmployee().getId()));

						// Set manager rating if present
						if (objVo.getManagerrating() != null && objVo.getManagerrating().getId() != null) {
							Optional<KraRating> ratingOpt = ratingDao.findById(objVo.getManagerrating().getId());
							ratingOpt.ifPresent(kraDetails::setManagerRating);
						}

						// Set qualitative assessment (if any)
						kraDetails.setManagerQaulitativeAssisment(
								!HRMSHelper.isNullOrEmpty(objVo.getManagerqaulitativeassisment())
										? objVo.getManagerqaulitativeassisment()
										: null);

						kraDetailsList.add(kraDetails);
					}
				}
			}

			// Save all updated records
			if (!kraDetailsList.isEmpty()) {
				kraDetailsDao.saveAll(kraDetailsList);
			}
		}

		log.info("Exit from savePmsManagerRating method");
	}

	@Override
	public HRMSBaseResponse<DashboardCountResponse> getPmsDashboard(PmsDashboardRequestVo request)
			throws HRMSException {

		HRMSBaseResponse<DashboardCountResponse> baseResponse = new HRMSBaseResponse<>();
		DashboardCountResponse responseVo = new DashboardCountResponse();
		List<DashboardResponseVo> dashboardResponses = new ArrayList<>();

		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		try {
			Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
			KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());

			if (kraCycle == null) {
				baseResponse.setResponseCode(1627);
				baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1627));
				responseVo.setCurrentYear(Collections.emptyList());
				responseVo.setPreviousYear(Collections.emptyList());
				responseVo.setMessage(IHRMSConstants.cycleclosed);
				baseResponse.setResponseBody(responseVo);
				return baseResponse;
			}

			String year = request.getYear() != null ? request.getYear() : kraYear.getYear();
			Long cycleId = request.getCycleId() != null ? request.getCycleId() : kraCycle.getId();
//			boolean isKpiSubmission = IHRMSConstants.KPI_SUBMISSION.equalsIgnoreCase(kraCycle.getCycleName());
			boolean isKpiSubmission = Objects.equals(kraCycle.getCycleTypeId(), IHRMSConstants.KPI_SUBMISSION_TYPE_ID);

			String roleName = request.getRoleName();

			if (roleName == null || roleName.isBlank()) {
				if (roles != null && !roles.isEmpty()) {
					roleName = roles.get(0);
					log.info("RoleName from request is null. Using role from token: {}", roleName);
				} else {
					responseVo.setMessage("No Role Found for Logged-in User");
					baseResponse.setResponseCode(1500);
					baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1501));
					responseVo.setCurrentYear(Collections.emptyList());
					responseVo.setPreviousYear(Collections.emptyList());
					baseResponse.setResponseBody(responseVo);
					return baseResponse;
				}
			}

			List<KraCycleCalender> calendarList = calenderDao.findByIsActive(ERecordStatus.Y.name());

			List<KraCycleCalender> filteredCalendarList;
			if (calendarList.size() == 1) {
				filteredCalendarList = calendarList;
			} else {
				MasterRole masterRole = roleDao.findByRoleName(roleName);
				if (masterRole != null) {
					long roleId = masterRole.getId();
					filteredCalendarList = calendarList.stream()
							.filter(c -> c.getRoleId() != null && c.getRoleId().getId() == roleId)
							.collect(Collectors.toList());
				} else {
					filteredCalendarList = Collections.emptyList();
				}

				if (filteredCalendarList.isEmpty() && calendarList.size() == 1) {
					filteredCalendarList = calendarList;
				}
			}

			switch (roleName.toUpperCase()) {

			case IHRMSConstants.HR:
				String currentYear = year;
				int previousYearValue = Integer.parseInt(currentYear) - 1;

				KraYear previousYear = krayeardao.findByYear(String.valueOf(previousYearValue));

//				String cycleName = kraCycle.getCycleName();
				Long cycleTypeId = kraCycle.getCycleTypeId();
				KraCycle previousYearCycle = null;
				Long previousCycleId = null;

				if (previousYear != null) {
					previousYearCycle = kraCycleDAO.findByCycleTypeAndYear(cycleTypeId, previousYear.getId());
					previousCycleId = previousYearCycle != null ? previousYearCycle.getId() : null;
				}

				final Long finalPreviousCycleId = previousCycleId;

				List<DashboardResponseVo> currentYearDashboardResponses;
				List<DashboardResponseVo> previousYearDashboardResponses = Collections.emptyList();

				List<String> years = new ArrayList<>();
				if (previousYear != null && previousYear.getYear() != null) {
					years.add(previousYear.getYear());
				}
				years.add(currentYear);

				List<Long> cycleIds = new ArrayList<>();
				if (finalPreviousCycleId != null) {
					cycleIds.add(finalPreviousCycleId);
				}
				cycleIds.add(cycleId);

				List<CycleResult> combinedCycleResults = kraDao.countByHR(years, cycleIds);

				/*
				 * List<CycleResult> combinedCycleResults = kraDao.countByHR( previousYear !=
				 * null ? previousYear.getYear() : null, currentYear, cycleId,
				 * finalPreviousCycleId);
				 */
				
				log.info("==== Combined Results ====");
				for (CycleResult r : combinedCycleResults) {
				    log.info("year={}, cycleId={}, cycleName={}, total={}", 
				        r.getYear(), r.getCycleId(), r.getCycleName(), r.getTotalCount());
				}
				log.info("Requested current cycleId={}, previousCycleId={}", cycleId, finalPreviousCycleId);

				
				
				List<CycleResult> currentYearResults = combinedCycleResults.stream()
						.filter(c -> c.getCycleId() != null && c.getCycleId().longValue() == cycleId.longValue())
						.collect(Collectors.toList());

				List<CycleResult> previousYearResults = (finalPreviousCycleId != null) ? combinedCycleResults.stream()
						.filter(c -> c.getCycleId() != null
								&& c.getCycleId().longValue() == finalPreviousCycleId.longValue())
						.collect(Collectors.toList()) : Collections.emptyList();

				if (isKpiSubmission) {
					currentYearDashboardResponses = kpiHelper
							.populateDashboardResponseForKpiSubmission(currentYearResults, kraCycle);

					previousYearDashboardResponses = previousYearCycle != null
							? kpiHelper.populateDashboardResponseForKpiSubmission(previousYearResults,
									previousYearCycle)
							: Collections.emptyList();
				} else {
					Optional<KraCycleCalender> latestPhaseOpt = calendarList.stream()
							.filter(c -> c.getEndDate() != null)
							.max(Comparator.comparing(KraCycleCalender::getEndDate));

					List<KraCycleCalender> latestPhaseList = latestPhaseOpt.map(Collections::singletonList)
							.orElse(Collections.emptyList());

					currentYearDashboardResponses = kpiHelper.populateDashboardResponse(latestPhaseList,
							currentYearResults);
					previousYearDashboardResponses = kpiHelper.populateDashboardResponse(Collections.emptyList(),
							previousYearResults);

					LocalDate today = LocalDate.now();
					Optional<KraCycleCalender> currentPhaseOpt = latestPhaseList.stream()
							.filter(c -> c.getStartDate() != null && c.getEndDate() != null).filter(c -> {
								LocalDate start = c.getStartDate().toInstant().atZone(ZoneId.systemDefault())
										.toLocalDate();
								LocalDate end = c.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
								return !today.isBefore(start) && !today.isAfter(end);
							}).findFirst();

					if (currentPhaseOpt.isPresent()) {
						KraCycleCalender currentPhase = currentPhaseOpt.get();
						DashboardResponseVo phaseResponse = new DashboardResponseVo();
						phaseResponse.setCurrentPhase(currentPhase.getCurrentPhase());
						phaseResponse.setCycleName(
								currentPhase.getCycleId() != null ? currentPhase.getCycleId().getCycleName() : null);
						currentYearDashboardResponses.add(phaseResponse);
					}
				}

				responseVo.setCurrentYear(currentYearDashboardResponses);
				responseVo.setPreviousYear(previousYearDashboardResponses);
				break;

			case IHRMSConstants.HOD:
				List<Long> departments = hodToDepartmentMap.getDepartmentByEmployeeAndIsActive(loggedInEmpId,
						ERecordStatus.Y.name());
				if (departments == null || departments.isEmpty()) {
					responseVo.setMessage("No Departments Assigned to HOD");
					log.warn("No departments for HOD: {}", loggedInEmpId);
				} else {
					if (isKpiSubmission) {
						dashboardResponses.addAll(kpiHelper.populateDashboardResponseForKpiSubmission(
								kraDao.countByDepartmentIdIn(departments, year, cycleId), kraCycle));
					} else {
						dashboardResponses.addAll(kpiHelper.populateDashboardResponse(filteredCalendarList,
								kraDao.countByDepartmentIdIn(departments, year, cycleId)));
					}
				}
				responseVo.setCurrentYear(dashboardResponses);
				responseVo.setPreviousYear(Collections.emptyList());
				break;

			case IHRMSConstants.EMPLOYEE:
				if (isKpiSubmission) {
					dashboardResponses.addAll(kpiHelper.populateDashboardResponseForKpiSubmissionEmployee(
							kraDao.getEmployeeDetails(year, cycleId, loggedInEmpId), kraCycle));
				} else {
					dashboardResponses.addAll(kpiHelper.populateDashboardResponseForEmployee(filteredCalendarList,
							kraDao.getEmployeeDetails(year, cycleId, loggedInEmpId)));
				}
				responseVo.setCurrentYear(dashboardResponses);
				responseVo.setPreviousYear(Collections.emptyList());
				break;

			case IHRMSConstants.MANAGER:
				List<Long> employees = kraDao.findByEmployeeByManager(loggedInEmpId, ERecordStatus.Y.name());
				if (isKpiSubmission) {
					dashboardResponses.addAll(kpiHelper.populateDashboardResponseForKpiSubmission(
							kraDao.countByEmployeeIdIn(employees, year, cycleId), kraCycle));
				} else {
					dashboardResponses.addAll(kpiHelper.populateDashboardResponse(filteredCalendarList,
							kraDao.countByEmployeeIdIn(employees, year, cycleId)));
				}
				responseVo.setCurrentYear(dashboardResponses);
				responseVo.setPreviousYear(Collections.emptyList());
				break;

			default:
				responseVo.setMessage("Unsupported Role");
				log.warn("Unsupported roleName: {}", roleName);
				responseVo.setCurrentYear(Collections.emptyList());
				responseVo.setPreviousYear(Collections.emptyList());
				break;
			}

			int currentYearInt = Integer.parseInt(kraYear.getYear());
			int requestedYearInt = Integer.parseInt(year);

			boolean isFutureYearRequest = requestedYearInt > currentYearInt;
			boolean noDataFound = (responseVo.getCurrentYear() == null || responseVo.getCurrentYear().isEmpty())
					&& (responseVo.getPreviousYear() == null || responseVo.getPreviousYear().isEmpty());

			if (isFutureYearRequest && noDataFound) {
				baseResponse.setResponseCode(1201);
				baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1201));
				responseVo.setMessage("No data found for year: " + requestedYearInt);
				baseResponse.setResponseBody(responseVo);
				return baseResponse;
			}

			baseResponse.setResponseBody(responseVo);
			baseResponse.setResponseCode(1200);
			baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		} catch (Exception e) {
			log.error("Error in getPmsDashboard: ", e);
			baseResponse.setResponseCode(1500);
			baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		return baseResponse;
	}

	@Override
	public HRMSBaseResponse<PublishResponse> getPublished() throws HRMSException {
		HRMSBaseResponse<PublishResponse> hramsresponse = new HRMSBaseResponse<PublishResponse>();
		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		List<KraWf> krapendinglist = new ArrayList<>();
		List<KraWf> krasubmittedlist = new ArrayList<>();

		KraCycle cycle = kraCycleDAO.findByStatus(status);

		if (HRMSHelper.isNullOrEmpty(cycle)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
		}
		List<KraWf> kraWflist = kraWfDao.findByIsActiveAndCycleId(IHRMSConstants.isActive, cycle);

		for (KraWf kra : kraWflist) {
			if (kra.getStatus().equalsIgnoreCase(EKraStatus.INCOMPLETE.name())
					|| kra.getStatus().equalsIgnoreCase(EKraStatus.INPROCESS.name())) {

				krapendinglist.add(kra);
			} else if (kra.getStatus().equalsIgnoreCase(EKraStatus.APPROVED.name())
					|| kra.getStatus().equalsIgnoreCase(EKraStatus.COMPLETED.name())) {
				krasubmittedlist.add(kra);
			}

		}

		List<PublishResponseVo> responselist = new ArrayList<>();

		responselist = krapendinglist.stream().map(a -> {
			PublishResponseVo publishresponse = new PublishResponseVo();
			publishresponse.setEmployeeId(a.getKra().getEmployeeId());
			Employee employee = employeeDAO.findByIsActiveAndId(IHRMSConstants.isActive, a.getKra().getEmployeeId());
			publishresponse
					.setEmpName(employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
			return publishresponse;

		}).collect(Collectors.toList());

		PublishResponse publishResponse = new PublishResponse();

		publishResponse.setData(responselist);

		hramsresponse.setResponseBody(publishResponse);
		hramsresponse.setResponseCode(1200);
		hramsresponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		return hramsresponse;
	}

	@Override
	public HRMSBaseResponse<String> savePublished() throws Exception {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		validateRolePermission();

		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		KraCycle cycle = kraCycleDAO.findByStatus(status);
		if (HRMSHelper.isNullOrEmpty(cycle)) {
			String message = ResponseCode.getResponseCodeMap().get(1221);
			throw new HRMSException(1500, message != null ? message : "No active cycle found.");
		}

		List<KraWf> kraWflist = kraWfDao.findByIsActiveAndCycleId(IHRMSConstants.isActive, cycle);
		if (HRMSHelper.isNullOrEmpty(kraWflist)) {
			String message = ResponseCode.getResponseCodeMap().get(1201);
			throw new HRMSException(1500, message != null ? message : "No active Kra Workflow found.");
		}

		String cycleName = cycle.getCycleName();
		Long cycleType = cycle.getCycleTypeId();
		String employeeTemplateName;
		String hrTemplateName;

		if (IHRMSConstants.KPI_SUBMISSION_TYPE_ID.equals(cycleType)) {
			employeeTemplateName = IHRMSConstants.KPI_SUBMISSION_EMAIL;
			hrTemplateName = IHRMSConstants.HR_KPI_SUBMISSION_EMAIL;
		} else if (IHRMSConstants.HALF_YEAR_TYPE_ID.equals(cycleType)) {
			employeeTemplateName = IHRMSConstants.HALF_YEAR_CYCLE_EMAIL;
			hrTemplateName = IHRMSConstants.HR_HALF_YEAR_CYCLE_EMAIL;
		} else if (IHRMSConstants.YEAR_END_TYPE_ID.equals(cycleType)) {
			employeeTemplateName = IHRMSConstants.EMPLOYEE_PUBLISHED_EMAIL;
			hrTemplateName = IHRMSConstants.HR_PUBLISHED_EMAIL;
		} else {
			throw new HRMSException(1500, "Invalid cycle type: " + cycleType);
		}

		List<String> emailAddresses = new ArrayList<>();
		List<String> empNames = new ArrayList<>();

		for (KraWf kra : kraWflist) {
			kra.setStatus(EKraStatus.COMPLETED.name());
			kra.setPendingWith(ERole.HR.name());
			emailAddresses.add(kra.getKra().getEmployee().getOfficialEmailId());
			empNames.add(kra.getKra().getEmployee().getCandidate().getFirstName() + " "
					+ kra.getKra().getEmployee().getCandidate().getLastName());
			kraWfDao.save(kra);
		}
		setIsLocked(cycle);
		Iterator<String> emailIterator = emailAddresses.iterator();
		Iterator<String> nameIterator = empNames.iterator();

		while (emailIterator.hasNext() && nameIterator.hasNext()) {
			String emailAddress = emailIterator.next();
			String employeeName = nameIterator.next();

			EmailRequestVO empEmail = new EmailRequestVO();
			empEmail.setEmailAddress(emailAddress);
			empEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

			TemplateVO empTemplate = new TemplateVO();
			empTemplate.setTemplateName(employeeTemplateName);

			List<PlaceHolderVO> placeHolders = new ArrayList<>();
			PlaceHolderVO placeHolderVo = new PlaceHolderVO();
			placeHolderVo.setExpressionVariableName("empName");
			placeHolderVo.setExpressionVariableValue(employeeName);
			placeHolders.add(placeHolderVo);
			empTemplate.setPlaceHolders(placeHolders);
			empEmail.setTemplateVo(empTemplate);

			emailService.insertInEmailTxnTable(empEmail);
		}

		EmailRequestVO hrEmail = new EmailRequestVO();
		hrEmail.setEmailAddress(loggedInEmployee.getOfficialEmailId());
		hrEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

		TemplateVO hrTemplate = new TemplateVO();
		hrTemplate.setTemplateName(hrTemplateName);

		List<PlaceHolderVO> hrPlaceHolders = new ArrayList<>();
		PlaceHolderVO hrPlaceHolderVo = new PlaceHolderVO();
		hrPlaceHolderVo.setExpressionVariableName("empName");
		hrPlaceHolderVo.setExpressionVariableValue(
				loggedInEmployee.getCandidate().getFirstName() + " " + loggedInEmployee.getCandidate().getLastName());
		hrPlaceHolders.add(hrPlaceHolderVo);
		hrTemplate.setPlaceHolders(hrPlaceHolders);
		hrEmail.setTemplateVo(hrTemplate);

		emailService.insertInEmailTxnTable(hrEmail);

		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1222));
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1222));
		return response;
	}

	private void setIsLocked(KraCycle cycle) {
		cycle.setIsLocked(ERecordStatus.Y.name());
		kraCycleDAO.save(cycle);
	}

	@Override
	public HRMSBaseResponse<String> acceptKpi(RejectRequestVo request) throws Exception {
		log.info("Inside Accept KPI method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(request.getKraId()) && !HRMSHelper.isNullOrEmpty(request.getCycleId())) {

			if (HRMSHelper.isRolePresent(role, ERole.HOD.name())) {
				Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
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
				KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
				isCycleOpen(status, empRole, kraCycle.getId());
				KraWf kraWf = kraWfDao.findByKraAndCycleIdAndIsActive(kra, cycle, ERecordStatus.Y.name());

				if (!HRMSHelper.isNullOrEmpty(kraWf)) {
					if (HRMSHelper.isNullOrEmpty(kraWf.getKra().getFinalRating())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1792));
					}

					kraWf.setStatus(EKraStatus.APPROVED.name());
					kraWf.setPendingWith(ERole.HR.name());
					kraWfDao.save(kraWf);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}

				// send mail
				Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), kra.getEmployeeId());
				Organization org = hrIhrmsOrganizationDAO.findByIsActiveAndId(ERecordStatus.Y.name(),
						employee.getOrgId());
				OrgDivWiseHRMapping mapping = hrmOrgDivHrDAO.findByIsActiveAndOrganization(ERecordStatus.Y.name(), org);

				EmailRequestVO hrEmail = new EmailRequestVO();
				hrEmail.setEmailAddress(mapping.getEmployee().getOfficialEmailId());
				hrEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

				TemplateVO managerTemplate = new TemplateVO();
				managerTemplate.setTemplateName(IHRMSConstants.MC_ACCEPTANCE_TO_MC);

				List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
				PlaceHolderVO placeHolderVo = new PlaceHolderVO();
				placeHolderVo.setExpressionVariableName("empName");
				placeHolderVo.setExpressionVariableValue(mapping.getEmployee().getCandidate().getFirstName() + " "
						+ mapping.getEmployee().getCandidate().getLastName());
				placeHolders.add(placeHolderVo);

				PlaceHolderVO subplaceHolderVo = new PlaceHolderVO();
				subplaceHolderVo.setExpressionVariableName("mcName");
				subplaceHolderVo.setExpressionVariableValue(loggedInEmployee.getCandidate().getFirstName() + " "
						+ loggedInEmployee.getCandidate().getLastName());
				placeHolders.add(subplaceHolderVo);

				managerTemplate.setPlaceHolders(placeHolders);

				hrEmail.setTemplateVo(managerTemplate);

				emailService.insertInEmailTxnTable(hrEmail);

				sendmailToHcdOnAcceptance(loggedInEmployee, employee);

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1231));
		log.info("Exit from Accept KPI method");

		return response;

	}

	@Override
	public HRMSBaseResponse<String> saveHrCalibrate() throws Exception {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		validateRolePermission();
		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		KraCycle cycle = kraCycleDAO.findByStatus(status);

		if (HRMSHelper.isNullOrEmpty(cycle)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
		}
		List<Kra> kralist = kraDao.findByIsActiveAndCycleId(IHRMSConstants.isActive, cycle);
		if (HRMSHelper.isNullOrEmpty(kralist)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		for (Kra kra : kralist) {
			kra.setIshrcalibrate(IHRMSConstants.isActive);
			kraDao.save(kra);
			KraWf kraWf = kraWfDao.findByKraAndCycleIdAndIsActive(kra, cycle, IHRMSConstants.isActive);
			kraWf.setStatus(EKraStatus.APPROVED.name());
			kraWf.setPendingWith(ERole.HOD.name());
			kraWfDao.save(kraWf);

		}
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());

		List<HodToDepartmentMap> hodlist = hodToDepartmentMap.findByIsActiveAndOrgId(ERecordStatus.Y.name(),
				loggedInEmployee.getOrgId());
		EmailRequestVO hodEmail = new EmailRequestVO();

		List<String> emailAddresses = new ArrayList<>();
		List<String> hodNames = new ArrayList<>();

		for (HodToDepartmentMap hod : hodlist) {
			Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), hod.getEmployeeId());

			emailAddresses.add(employee.getOfficialEmailId());
			hodNames.add(employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		}
		Iterator<String> emailIterator = emailAddresses.iterator();
		Iterator<String> nameIterator = hodNames.iterator();

		while (emailIterator.hasNext() && nameIterator.hasNext()) {
			String emailAddress = emailIterator.next();
			String hodName = nameIterator.next();

			hodEmail.setEmailAddress(emailAddress);

			hodEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

			TemplateVO hodTemplate = new TemplateVO();
			hodTemplate.setTemplateName(IHRMSConstants.HOD_CALIBRATE_EMAIL);

			List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
			PlaceHolderVO placeHolderVo = new PlaceHolderVO();
			placeHolderVo.setExpressionVariableName("empName");
			placeHolderVo.setExpressionVariableValue(hodName);
			placeHolders.add(placeHolderVo);
			hodTemplate.setPlaceHolders(placeHolders);

			hodEmail.setTemplateVo(hodTemplate);

			emailService.insertInEmailTxnTable(hodEmail);
		}

		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		return response;
	}

	@Override
	public void downloadHodKraSummary(Long deptid, Long empId, String year, Long countryId, HttpServletResponse res)
			throws IOException, HRMSException {
		log.info("Inside downloadKRASummary for HOD method  ");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Pageable pageable = null;
		List<Kra> kraList = null;
		if (authorizationServiceImpl.isAuthorizedFunctionName("downloadKraSummaryForHod", role)) {

			List<HodToDepartmentMap> departmentMapList = hodToDepartmentMap.findByEmployeeIdAndIsActive(loggedInEmpId,
					ERecordStatus.Y.name());

			if (!HRMSHelper.isNullOrEmpty(departmentMapList)) {

				List<Long> divisions = hodToDepartmentMap.getDivisionByEmployeeAndIsActive(loggedInEmpId,
						ERecordStatus.Y.name());

				List<Long> departments = hodToDepartmentMap.getDepartmentByEmployeeAndIsActive(loggedInEmpId,
						ERecordStatus.Y.name());

				kraList = kraDao.findByDivIdInAndDeptIdInAndStatus(divisions.toArray(), departments.toArray(),
						EKraStatus.APPROVED.name(), EKraStatus.REJECTED.name(), EKraStatus.INPROCESS.name(),
						EKraStatus.COMPLETED.name(), EKraStatus.INCOMPLETE.name(), ERecordStatus.Y.name(), pageable);

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

			// convert to VO
			if (!HRMSHelper.isNullOrEmpty(kraList)) {
				Workbook workbook = excelGenerator.generateKRAXlSForHod(kraList);
				res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				res.setHeader("Content-Disposition", "attachment; filename=KRASummary.xlsx");
				workbook.write(res.getOutputStream());
				workbook.write(stream);
				workbook.close();
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

			log.info("Exit from downloadHodKRASummary method");
		} else {
			stream.close();
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	@Override
	public HRMSBaseResponse<DashboardRatingResponseVO> getDashboardRating() throws HRMSException {
		log.info("Inside getDashboardRating Method");
		HRMSBaseResponse<DashboardRatingResponseVO> baseresponse = new HRMSBaseResponse<>();
		List<DashboardRatingView> dashboardratinglist = dashboardRating.findAll();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<HrToHodMap> hr = hrtohodMapDao.findByHrIdAndIsActive(loggedInEmpId, IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(hr)) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		if (HRMSHelper.isNullOrEmpty(dashboardratinglist)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		for (DashboardRatingView viewList : dashboardratinglist) {
			List<KraRating> rating = kraRatingDAO.findByLabelStartingWith(viewList.getRating());
			Long target = rating.stream().findFirst().get().getTarget();
			if (target != null) {
				viewList.setTarget(target);
			} else {
				viewList.setTarget(null);
			}
		}
		DashboardRatingResponseVO responsevo = new DashboardRatingResponseVO();
		List<MasterDepartment> departments = masterDepartmentDao.findByIsActive(ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(departments)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<HeaderVO> headers = new ArrayList<>();
		headers.add(new HeaderVO("Rating", 1));
		headers.addAll(departments.stream().map(department -> new HeaderVO(department.getDepartmentDescription(), 1))
				.collect(Collectors.toList()));

		headers.add(new HeaderVO("Total (actual)", 1));
		headers.add(new HeaderVO("Total (actual) 1", 1));
		headers.add(new HeaderVO("Target", 1));
		responsevo.setHeaders(headers);

		List<String> colorCodes = List.of("high1", "high2", "high3", "high4", "high5");

		Map<String, List<Long>> departmentTop5Values = new HashMap<>();
		departments.forEach(department -> {
			String departmentName = department.getDepartmentDescription();
			List<Long> values = dashboardratinglist.stream().map(view -> {
				switch (departmentName) {
				case "LEG":
					return view.getLeg();
				case "HCD":
					return view.getHcd();
				case "PRT FIN":
					return view.getPrtFin();
				case "CDO":
					return view.getCdo();
				case "NBD":
					return view.getNbd();
				case "CSM":
					return view.getCsm();
				case "AUD":
					return view.getAud();
				default:
					return null;
				}
			}).filter(Objects::nonNull).distinct().sorted(Comparator.reverseOrder()).limit(5)
					.collect(Collectors.toList());
			departmentTop5Values.put(departmentName, values);
		});

		// Get top values for total, totalActual, and target
		List<Long> top5Total = dashboardratinglist.stream().map(DashboardRatingView::getTotal).filter(Objects::nonNull)
				.distinct().sorted(Comparator.reverseOrder()).limit(5).collect(Collectors.toList());

		List<Long> top3TotalActual = dashboardratinglist.stream().map(DashboardRatingView::getTotalActual)
				.filter(Objects::nonNull).distinct().sorted(Comparator.reverseOrder()).limit(4)
				.collect(Collectors.toList());

		List<Long> top3Target = dashboardratinglist.stream().map(DashboardRatingView::getTarget)
				.filter(Objects::nonNull).distinct().sorted(Comparator.reverseOrder()).limit(4)
				.collect(Collectors.toList());

		List<GroupedRowVO> groupedRows = new ArrayList<>();

		dashboardratinglist.stream().filter(view -> {
			String numericRating = view.getRating().replaceAll("\\D", "");
			if (numericRating.isEmpty()) {
				System.out.println("Skipping invalid rating: " + view.getRating());
				return false;
			}
			return true;
		}).collect(Collectors.groupingBy(view -> {

			String numericRating = view.getRating().replaceAll("\\D", "");
			return Integer.parseInt(numericRating);
		}))

				.entrySet().stream()
				.sorted(Map.Entry.<Integer, List<DashboardRatingView>>comparingByKey(Comparator.reverseOrder()))
				.forEach(entry -> {
					Integer numericRating = entry.getKey();
					List<DashboardRatingView> views = entry.getValue();
					List<RowVO> rows = views.stream().map(view -> {
						RowVO row = new RowVO();
						row.setLabel(view.getRating());

						departments.forEach(department -> {
							String departmentName = department.getDepartmentDescription();
							Long value = null;
							String color = "";
							switch (departmentName) {
							case "LEG":
								value = view.getLeg();
								break;
							case "HCD":
								value = view.getHcd();
								break;
							case "PRT FIN":
								value = view.getPrtFin();
								break;
							case "CDO":
								value = view.getCdo();
								break;
							case "NBD":
								value = view.getNbd();
								break;
							case "CSM":
								value = view.getCsm();
								break;
							case "AUD":
								value = view.getAud();
								break;
							}

							// Assign color if value is in top 5
							if (value != null && departmentTop5Values.get(departmentName).contains(value)) {
								int index = departmentTop5Values.get(departmentName).indexOf(value);
								color = colorCodes.get(index);
							}
							// Set value and color in the row
							switch (departmentName) {
							case "LEG":
								row.setLeg(value);
								if (value != 0)
									row.setLegColor(color);
								break;
							case "HCD":
								row.setHcd(value);
								if (value != 0)
									row.setHcdColor(color);
								break;
							case "PRT FIN":
								row.setPrtFin(value);
								if (value != 0)
									row.setPrtFinColor(color);
								break;
							case "CDO":
								row.setCdo(value);
								if (value != 0)
									row.setCdoColor(color);
								break;
							case "NBD":
								row.setNbd(value);
								if (value != 0)
									row.setNbdColor(color);
								break;
							case "CSM":
								row.setCsm(value);
								if (value != 0)
									row.setCsmColor(color);
								break;
							case "AUD":
								row.setAud(value);
								if (value != 0)
									row.setAudColor(color);
								break;
							}
						});
						// Assign colors for totals
						row.setTotal(view.getTotal());
						if (view.getTotal() != null && view.getTotal() != 0 && top5Total.contains(view.getTotal())) {
							int index = top5Total.indexOf(view.getTotal());
							row.setTotalColor(colorCodes.get(index));
						}

						row.setTotalActual(view.getTotalActual());
						if (view.getTotalActual() != null && view.getTotalActual() != 0
								&& top3TotalActual.contains(view.getTotalActual())) {
							int index = top3TotalActual.indexOf(view.getTotalActual());
							row.setTotalActualColor(colorCodes.get(index));
						}
						List<KraRating> rating = kraRatingDAO.findByLabelStartingWith(view.getRating());
						Long target = rating.stream().findFirst().get().getTarget();
						if (target != null) {
							row.setTarget(target);
							if (target != null && target != 0 && top3Target.contains(target)) {
								int index = top3Target.indexOf(target);
								row.setTargetColor(colorCodes.get(index));
							}
						} else {
							row.setTarget(null);
							row.setTargetColor(null);
						}
						return row;
					}).collect(Collectors.toList());
					groupedRows.add(new GroupedRowVO(numericRating, rows));
				});

		RowVO totalRow = new RowVO();
		totalRow.setLabel("Grand Total");
		long legSum = 0L, hcdSum = 0L, prtFinSum = 0L, cdoSum = 0L, nbdSum = 0L, csmSum = 0L, audSum = 0L;
		long totalSum = 0L, totalActualSum = 0L, targetSum = 0L;

		// iterate groupedRows → rows and sum department values
		for (GroupedRowVO groupedRow : groupedRows) {
			for (RowVO row : groupedRow.getRows()) {
				if (row.getLeg() != null)
					legSum += row.getLeg();
				if (row.getHcd() != null)
					hcdSum += row.getHcd();
				if (row.getPrtFin() != null)
					prtFinSum += row.getPrtFin();
				if (row.getCdo() != null)
					cdoSum += row.getCdo();
				if (row.getNbd() != null)
					nbdSum += row.getNbd();
				if (row.getCsm() != null)
					csmSum += row.getCsm();
				if (row.getAud() != null)
					audSum += row.getAud();

				if (row.getTotal() != null)
					totalSum += row.getTotal();
				if (row.getTotalActual() != null)
					totalActualSum += row.getTotalActual();
				if (row.getTarget() != null)
					targetSum += row.getTarget();
			}
		}

		// assign totals
		totalRow.setLeg(legSum);
		totalRow.setHcd(hcdSum);
		totalRow.setPrtFin(prtFinSum);
		totalRow.setCdo(cdoSum);
		totalRow.setNbd(nbdSum);
		totalRow.setCsm(csmSum);
		totalRow.setAud(audSum);

		totalRow.setTotal(totalSum);
		totalRow.setTotalActual(totalActualSum);
		totalRow.setTarget(targetSum);

		// add grand total row as a new grouped row (rating = 0)
		groupedRows.add(new GroupedRowVO(0, List.of(totalRow)));

		responsevo.setGroupedRows(groupedRows);
		baseresponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseresponse.setResponseBody(responsevo);
		baseresponse.setResponseCode(1200);
		log.info("Exist From getDashboardRating Method");
		return baseresponse;

	}

	// ************* API added to get dynamic timelines for dashboard

	@Override
	public HRMSBaseResponse<TimeLineVo> getKpiTimeline() throws HRMSException {
		HRMSBaseResponse<TimeLineVo> baseresponse = new HRMSBaseResponse<>();
		log.info("Inside getKraList method");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		TimeLineVo responsevo = new TimeLineVo();
		List<DashboardTimelineVo> timelineLists = null;

		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		KraCycle cycle = kraCycleDAO.findByStatus(status);
		KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());

		if (!HRMSHelper.isNullOrEmpty(status) && !HRMSHelper.isNullOrEmpty(cycle)
				&& !HRMSHelper.isNullOrEmpty(kraYear)) {

			List<KpiTimeline> timelineList = kpiTimelinedao.findByIsActiveAndOrgIdAndCycleIdAndYearIdOrderByIdAsc(
					IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId(), cycle.getId(), kraYear.getId());
			if (!HRMSHelper.isNullOrEmpty(timelineList)) {

				timelineLists = transformUtil.convertToTimelineVo(timelineList);
			}

			responsevo.setTimelines(timelineLists);

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		baseresponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseresponse.setResponseBody(responsevo);
		baseresponse.setResponseCode(1200);
		return baseresponse;
	}

	@Override
	public HRMSBaseResponse<String> saveTarget(TargetRequestVo request) throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();

		List<TargetRequestVo.LabelValuePair> labelValuePairs = request.getTargetLabelValuePairs();

		if (HRMSHelper.isNullOrEmpty(labelValuePairs)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		for (TargetRequestVo.LabelValuePair labelValuePair : labelValuePairs) {
			String label = labelValuePair.getTargetLabel();
			String targetValue = labelValuePair.getTargetValue();
			long targetLong = Long.parseLong(targetValue);
			List<KraRating> ratings = ratingDao.findByLabelStartingWith(label);

			if (HRMSHelper.isNullOrEmpty(ratings)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}

			for (KraRating rating : ratings) {
				rating.setTarget(targetLong);
				ratingDao.save(rating);
			}
		}
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		return response;
	}

	@Override
	public HRMSBaseResponse<List<SelfRatingVo>> getSelfRating(Long categoryId, Long kraId, Long objId)
			throws HRMSException {

		HRMSBaseResponse<List<SelfRatingVo>> response = new HRMSBaseResponse<>();
		List<SelfRatingVo> kraRatingResponse = new ArrayList<>();

		if (HRMSHelper.isNullOrEmpty(categoryId) || HRMSHelper.isNullOrEmpty(kraId)
				|| HRMSHelper.isNullOrEmpty(objId)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		Kra kra = kraDao.findByIdAndIsActive(kraId, IHRMSConstants.isActive);
		KraDetails details = kraDetailsDao.findByIdAndIsActiveAndKra(objId, IHRMSConstants.isActive, kra);

		if (HRMSHelper.isNullOrEmpty(details)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<KraRating> kraRatingList;

		String measurementCriteria = details.getMeasurementCriteria();

		if (IHRMSConstants.passfail.equalsIgnoreCase(measurementCriteria)) {
			kraRatingList = kraRatingDAO.findByCatId(categoryId, IHRMSConstants.isActive);
		} else if (IHRMSConstants.onOccurrence.equalsIgnoreCase(measurementCriteria)) {
			kraRatingList = kraRatingDAO.findByCategoryandMatrix(categoryId, IHRMSConstants.isActive);
//	    	kraRatingList = kraRatingDAO.findByCategoryandMatrix(categoryId, IHRMSConstants.isActive);

		} else {
			kraRatingList = kraRatingDAO.findByCatId(categoryId, IHRMSConstants.isNotActive);
		}

		if (HRMSHelper.isNullOrEmpty(kraRatingList)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		for (KraRating kraRating : kraRatingList) {
			SelfRatingVo selfRatingVo = new SelfRatingVo();
			selfRatingVo.setId(kraRating.getId());
			selfRatingVo.setValue(kraRating.getValue());
			selfRatingVo.setLabel(kraRating.getLabel());
			selfRatingVo.setCategoryId(kraRating.getCategory().getId());
			selfRatingVo.setIsPassFail(kraRating.getIsPassFail());
			selfRatingVo.setOnOccurrence(kraRating.getOnOccurrence());
			kraRatingResponse.add(selfRatingVo);
		}

		response.setResponseBody(kraRatingResponse);
		response.setResponseCode(1200);
		response.setTotalRecord(kraRatingResponse.size());
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from getSelfRating Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<RatingPercentageResponseVO> getRatingPercentage() throws HRMSException {
		HRMSBaseResponse<RatingPercentageResponseVO> baseresponse = new HRMSBaseResponse<>();
		List<PercentageRatingView> percenatgeratinglist = percentageRatingDao.findAll();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<HrToHodMap> hr = hrtohodMapDao.findByHrIdAndIsActive(loggedInEmpId, IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(hr)) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		if (HRMSHelper.isNullOrEmpty(percenatgeratinglist)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		for (PercentageRatingView viewList : percenatgeratinglist) {
			List<KraRating> rating = kraRatingDAO.findByLabelStartingWith(viewList.getRating());
			Double target = rating.stream().findFirst().get().getTargetPercentage();
			if (target != null) {
				String targetPer = target.toString().concat("%");
				viewList.setTarget(targetPer);
			} else {
				viewList.setTarget(null);
			}

		}

		RatingPercentageResponseVO responsevo = new RatingPercentageResponseVO();
		List<MasterDepartment> departments = masterDepartmentDao.findByIsActive(ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(departments)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<PercentageHeaderVO> headers = new ArrayList<>();
		headers.add(new PercentageHeaderVO("Rating", 1));
		headers.addAll(
				departments.stream().map(department -> new PercentageHeaderVO(department.getDepartmentDescription(), 1))
						.collect(Collectors.toList()));

		headers.add(new PercentageHeaderVO("Total (actual)", 1));
		headers.add(new PercentageHeaderVO("Total (actual) 1", 1));
		headers.add(new PercentageHeaderVO("Target", 1));
		responsevo.setHeaders(headers);

		List<String> colorCodes = List.of("high1", "high2", "high3", "high4", "high5");

		Map<String, List<String>> departmentTop5Values = new HashMap<>();
		departments.forEach(department -> {
			String departmentName = department.getDepartmentDescription();
			List<String> values = percenatgeratinglist.stream().map(view -> {
				switch (departmentName) {
				case "LEG":
					return view.getLeg();
				case "HCD":
					return view.getHcd();
				case "PRT FIN":
					return view.getPrtFin();
				case "CDO":
					return view.getCdo();
				case "NBD":
					return view.getNbd();
				case "CSM":
					return view.getCsm();
				case "AUD":
					return view.getAud();
				default:
					return null;
				}
			}).filter(Objects::nonNull).distinct().sorted((a, b) -> {
				Double numA = Double.parseDouble(a.replace("%", ""));
				Double numB = Double.parseDouble(b.replace("%", ""));
				return numB.compareTo(numA);
			}).limit(5).collect(Collectors.toList());
			departmentTop5Values.put(departmentName, values);
		});

		List<String> top5Total = percenatgeratinglist.stream().map(PercentageRatingView::getTotal)
				.filter(Objects::nonNull).distinct().sorted((a, b) -> {
					Double numA = Double.parseDouble(a.replace("%", ""));
					Double numB = Double.parseDouble(b.replace("%", ""));
					return numB.compareTo(numA);
				}).limit(5).collect(Collectors.toList());

		List<String> top3TotalActual = percenatgeratinglist.stream().map(PercentageRatingView::getTotalActual)
				.filter(Objects::nonNull).distinct().sorted((a, b) -> {
					Double numA = Double.parseDouble(a.replace("%", ""));
					Double numB = Double.parseDouble(b.replace("%", ""));
					return numB.compareTo(numA);
				}).limit(4).collect(Collectors.toList());

		List<String> top3Target = percenatgeratinglist.stream().map(PercentageRatingView::getTarget)
				.filter(Objects::nonNull).distinct().sorted((a, b) -> {

					Double numA = Double.parseDouble(a.replace("%", ""));
					Double numB = Double.parseDouble(b.replace("%", ""));
					return numB.compareTo(numA); // Descending order
				}).limit(4).collect(Collectors.toList());

		System.out.println(top3Target);
		// Grouped rows
		List<PercentageGroupedRowVo> groupedRows = new ArrayList<>();

		percenatgeratinglist.stream().filter(view -> {
			String numericRating = view.getRating().replaceAll("\\D", "");
			if (numericRating.isEmpty()) {
				System.out.println("Skipping invalid rating: " + view.getRating());
				return false;
			}
			return true;
		}).collect(Collectors.groupingBy(view -> {

			String numericRating = view.getRating().replaceAll("\\D", "");
			return Integer.parseInt(numericRating);
		}))

				.entrySet().stream()
				.sorted(Map.Entry.<Integer, List<PercentageRatingView>>comparingByKey(Comparator.reverseOrder()))
				.forEach(entry -> {
					Integer numericRating = entry.getKey();
					List<PercentageRatingView> views = entry.getValue();
					List<PercentageRowVO> rows = views.stream().map(view -> {
						PercentageRowVO row = new PercentageRowVO();
						row.setLabel(view.getRating());

						departments.forEach(department -> {
							String departmentName = department.getDepartmentDescription();
							String value = null;
							String color = "";
							switch (departmentName) {
							case "LEG":
								value = view.getLeg();
								break;
							case "HCD":
								value = view.getHcd();
								break;
							case "PRT FIN":
								value = view.getPrtFin();
								break;
							case "CDO":
								value = view.getCdo();
								break;
							case "NBD":
								value = view.getNbd();
								break;
							case "CSM":
								value = view.getCsm();
								break;
							case "AUD":
								value = view.getAud();
								break;
							}

							// Assign color if value is in top 5
							if (value != null && departmentTop5Values.get(departmentName).contains(value)) {
								int index = departmentTop5Values.get(departmentName).indexOf(value);
								color = colorCodes.get(index);
							}
							// Set value and color in the row
							switch (departmentName) {
							case "LEG":
								row.setLeg(value);
								if (!value.equals("0%"))
									row.setLegColor(color);
								break;
							case "HCD":
								row.setHcd(value);
								if (!value.equals("0%"))
									row.setHcdColor(color);
								break;
							case "PRT FIN":
								row.setPrtFin(value);
								if (!value.equals("0%"))
									row.setPrtFinColor(color);
								break;
							case "CDO":
								row.setCdo(value);
								if (!value.equals("0%"))
									row.setCdoColor(color);
								break;
							case "NBD":
								row.setNbd(value);
								if (!value.equals("0%"))
									row.setNbdColor(color);
								break;
							case "CSM":
								row.setCsm(value);
								if (!value.equals("0%"))
									row.setCsmColor(color);
								break;
							case "AUD":
								row.setAud(value);
								if (!value.equals("0%"))
									row.setAudColor(color);
								break;
							}
						});
						// Assign colors for totals
						row.setTotal(view.getTotal());
						if (view.getTotal() != null && !view.getTotal().equals("0%")
								&& top5Total.contains(view.getTotal())) {
							int index = top5Total.indexOf(view.getTotal());
							row.setTotalColor(colorCodes.get(index));
						}

						row.setTotalActual(view.getTotalActual());
						if (view.getTotalActual() != null && !view.getTotalActual().equals("0.0%")
								&& top3TotalActual.contains(view.getTotalActual())) {
							int index = top3TotalActual.indexOf(view.getTotalActual());
							row.setTotalActualColor(colorCodes.get(index));
						}
						List<KraRating> rating = kraRatingDAO.findByLabelStartingWith(view.getRating());
						KraRating kraRate = rating.stream().findFirst().get();
						if (kraRate.getTargetPercentage() != null) {
							String targetPer = Double.toString(kraRate.getTargetPercentage());
							String target = targetPer.concat("%");
							row.setTarget(target);
							if (target != null && !target.equals("0.0%") && top3Target.contains(target)) {
								int index = top3Target.indexOf(target);

								row.setTargetColor(colorCodes.get(index));
							}
						} else {
							row.setTarget(null);
							row.setTargetColor(null);
						}
						return row;
					}).collect(Collectors.toList());
					groupedRows.add(new PercentageGroupedRowVo(numericRating, rows));
				});

		// ---- Grand Total Row ----
		PercentageRowVO totalRow = new PercentageRowVO();
		totalRow.setLabel("Grand Total");

		double legSum = 0, hcdSum = 0, prtFinSum = 0, cdoSum = 0, nbdSum = 0, csmSum = 0, audSum = 0;
		double totalSum = 0, totalActualSum = 0, targetSum = 0;

		for (PercentageGroupedRowVo groupedRow : groupedRows) {
			for (PercentageRowVO row : groupedRow.getRows()) {
				legSum += HRMSHelper.parsePercentage(row.getLeg());
				hcdSum += HRMSHelper.parsePercentage(row.getHcd());
				prtFinSum += HRMSHelper.parsePercentage(row.getPrtFin());
				cdoSum += HRMSHelper.parsePercentage(row.getCdo());
				nbdSum += HRMSHelper.parsePercentage(row.getNbd());
				csmSum += HRMSHelper.parsePercentage(row.getCsm());
				audSum += HRMSHelper.parsePercentage(row.getAud());
				totalSum += HRMSHelper.parsePercentage(row.getTotal());
				totalActualSum += HRMSHelper.parsePercentage(row.getTotalActual());
				targetSum += HRMSHelper.parsePercentage(row.getTarget());
			}
		}

		totalRow.setLeg(HRMSHelper.formatPercentage(legSum));
		totalRow.setHcd(HRMSHelper.formatPercentage(hcdSum));
		totalRow.setPrtFin(HRMSHelper.formatPercentage(prtFinSum));
		totalRow.setCdo(HRMSHelper.formatPercentage(cdoSum));
		totalRow.setNbd(HRMSHelper.formatPercentage(nbdSum));
		totalRow.setCsm(HRMSHelper.formatPercentage(csmSum));
		totalRow.setAud(HRMSHelper.formatPercentage(audSum));

		totalRow.setTotal(HRMSHelper.formatPercentage(totalSum));
		totalRow.setTotalActual(HRMSHelper.formatPercentage(totalActualSum));
		totalRow.setTarget(HRMSHelper.formatPercentage(targetSum));

		// wrap into grouped row with key = 0 (or some special id)
		groupedRows.add(new PercentageGroupedRowVo(0, List.of(totalRow)));

		responsevo.setGroupedRows(groupedRows);
		baseresponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseresponse.setResponseBody(responsevo);
		baseresponse.setResponseCode(1200);
		return baseresponse;

	}
	/* **************Code for reminder mail schedular ******************/

	public void sendKpiReminderEmailToAllRM() throws Exception {
		log.info("Inside send KPI Reminder Mail To All RM");

		List<Long> rm = reportingManagerDao.findReportingManagerByIsActive(IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(rm)) {
			for (Long rmId : rm) {
				if (!HRMSHelper.isNullOrEmpty(rmId)) {
					List<Kra> kra = kraDao.findByEmployeeByMangerAndIsActive(rmId, EKraStatus.INPROCESS.name(),
							IHRMSConstants.isActive);
					Employee rmDetails = employeeDAO.findActiveEmployeeById(rmId, IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(rmDetails)) {
						KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
						MasterRole managerRole = roleDao.findByRoleNameOrgIdIsActive(ERole.MANAGER.name(),
								rmDetails.getOrgId(), IHRMSConstants.isActive);
						KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
						KraCycleCalender rmLastDate = calenderDao.findByIsActiveStatusAndRole(IHRMSConstants.isActive,
								status.getId(), managerRole.getId(), kraCycle.getId());

						log.info("kra For RM" + " " + kra);
						if (!HRMSHelper.isNullOrEmpty(kra)) {
							sendMailToAllRM(kra, rmDetails, rmLastDate);
						} else {
							log.info("No Kra found for RM" + " " + rmId);
						}
					}
				} else {
					log.info("No Reporting Manager ID found" + " " + rm);
				}

			}
		}

	}

	private void sendMailToAllRM(List<Kra> kraList, Employee emp, KraCycleCalender calender) throws Exception {
		EmailRequestVO email = new EmailRequestVO();
		email.setEmailAddress(emp.getOfficialEmailId());
		email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

		TemplateVO template = new TemplateVO();
		template.setTemplateName(IHRMSConstants.LINE_MANAGER_REMINDERS);

		List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName("empName");
		placeHolderVo
				.setExpressionVariableValue(emp.getCandidate().getFirstName() + " " + emp.getCandidate().getLastName());
		placeHolders.add(placeHolderVo);

		String lastDate = new SimpleDateFormat("MMMM dd, yyyy").format(calender.getEndDate());

		PlaceHolderVO datePlaceHolderVo = new PlaceHolderVO();
		datePlaceHolderVo.setExpressionVariableName("lastDate");
		datePlaceHolderVo.setExpressionVariableValue(lastDate);
		placeHolders.add(datePlaceHolderVo);

		PlaceHolderVO employeeList = new PlaceHolderVO();

		String empList = "";
		for (Kra kra : kraList) {
			empList += " " + kra.getEmployee().getCandidate().getFirstName() + " "
					+ kra.getEmployee().getCandidate().getLastName() + ",";
		}

		if (!empList.isEmpty()) {
			empList = empList.substring(0, empList.length() - 1);
		}

		employeeList.setExpressionVariableName("teamMembers");
		employeeList.setExpressionVariableValue(empList);
		placeHolders.add(employeeList);

		template.setPlaceHolders(placeHolders);
		email.setTemplateVo(template);
		emailService.insertInEmailTxnTable(email);
	}

	@Override
	public void downloadKraDashboardRatingReport(HttpServletResponse res) throws IOException, HRMSException {
		log.info("Inside downloadKraDashboardRatingReport method");

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (authorizationServiceImpl.isAuthorizedFunctionName("downloadKraSummaryForHR", role)
				|| authorizationServiceImpl.isAuthorizedFunctionName("downloadKraSummaryForPM", role)) {

			Pageable pageable = null;
			List<MasterDepartment> departmentsList = masterDepartmentDao.findByIsActive(ERecordStatus.Y.name());
			List<DashboardRatingView> dashboardratinglist = dashboardRating.findAll();
			// List<KraDetailsLite> kraDetailsLiteList = fetchKraDetails(pageable);

			res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			res.setHeader("Content-Disposition", "attachment; filename=KRADashoardRatingSummary.xlsx");

			try (Workbook workbook = excelGenerator.generateDashboardRatingXlS(departmentsList, dashboardratinglist);
					ServletOutputStream outputStream = res.getOutputStream()) {

				workbook.write(outputStream);
				outputStream.flush();

			} catch (IOException e) {
				log.error("Error while generating Excel file: ", e);
				throw e;
			}

			log.info("Exit from downloadKraDashboardRatingReport method");
		} else {

			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

	}

	@Override
	public void downloadKraPercentageRatingReport(HttpServletResponse res) throws HRMSException, IOException {
		log.info("Inside downloadKraPercentageRatingReport method");

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (authorizationServiceImpl.isAuthorizedFunctionName("downloadKraSummaryForHR", role)
				|| authorizationServiceImpl.isAuthorizedFunctionName("downloadKraSummaryForPM", role)) {

			Pageable pageable = null;
			List<MasterDepartment> departmentsList = masterDepartmentDao.findByIsActive(ERecordStatus.Y.name());
			List<PercentageRatingView> percenatgeratinglist = percentageRatingDao.findAll();

			res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			res.setHeader("Content-Disposition", "attachment; filename=KRAPercentageRatingSummary.xlsx");

			try (Workbook workbook = excelGenerator.generatePercentageRatingXlS(departmentsList, percenatgeratinglist);
					ServletOutputStream outputStream = res.getOutputStream()) {

				workbook.write(outputStream);
				outputStream.flush();

			} catch (IOException e) {
				log.error("Error while generating Excel file: ", e);
				throw e;
			}

			log.info("Exit from downloadKraPercentageRatingReport method");
		} else {

			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	@Override
	public HRMSBaseResponse<String> newSaveCalibration(CalibrationRequestVo request) throws HRMSException {
		log.info("Inside saveCalibration method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(role, ERole.HOD.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		validateRequestBasics(request);

		Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), request.getEmployeeId());
		if (employee == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getCycleId(), ERecordStatus.Y.name());
		if (cycle == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		MasterRole empRole = roleDao.findByRoleNameOrgIdIsActive(ERole.HOD.name(), loggedInEmployee.getOrgId(),
				IHRMSConstants.isActive);
		KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
		isCycleOpen(status, empRole, kraCycle.getId());

		Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
		if (kra == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		HodCycleFinalRating hodRating = hodcycleDao.findByEmployeeAndMcMemberIdAndCycleIdAndIsActive(employee,
				loggedInEmployee, cycle, ERecordStatus.Y.name());

		if (hodRating == null) {

			saveNewRating(request, loggedInEmployee, employee, kra, cycle);
		} else {

			updateExistingRating(request, hodRating);
		}

		addWFStatus(kra, loggedInEmployee.getId(), EKraStatus.APPROVED.name(), ERole.HOD.name());

		response.setResponseBody("Saved successfully");
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1628));
		response.setResponseCode(1200);
		log.info("Exit from saveCalibration method");

		return response;
	}

	private void validateRequestBasics(CalibrationRequestVo request) throws HRMSException {
		if (request.getEmployeeId() == null || request.getCycleId() == null || request.getKraId() == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
	}

	private void updateExistingRating(CalibrationRequestVo request, HodCycleFinalRating hodRating)
			throws HRMSException {
		if (request.getComment() != null) {
			hodRating.setComment(request.getComment());
		}

		if (request.getRating() != null && request.getRating().getId() != null) {
			KraRating rating = ratingDao.findByIsActiveAndId(ERecordStatus.Y.name(), request.getRating().getId());
			if (rating != null) {
				hodRating.setScore(rating);
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}
		}

		hodcycleDao.save(hodRating);
	}

	private void saveNewRating(CalibrationRequestVo request, Employee loggedInEmployee, Employee employee, Kra kra,
			KraCycle cycle) throws HRMSException {
		if (request.getRating() == null || request.getComment() == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}

		KraRating rating = ratingDao.findByIsActiveAndId(ERecordStatus.Y.name(), request.getRating().getId());
		if (rating == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		HodCycleFinalRating newRating = new HodCycleFinalRating();
		newRating.setComment(request.getComment());
		newRating.setEmployee(employee);
		newRating.setMcMemberId(loggedInEmployee);
		newRating.setScore(rating);
		newRating.setCycleId(cycle);
		newRating.setKra(kra);
		newRating.setOrgId(loggedInEmployee.getOrgId());
		newRating.setIsActive(ERecordStatus.Y.name());

		hodcycleDao.save(newRating);
	}

	@Override
	public HRMSBaseResponse<String> saveTargetPercentage(TargetRequestVo request) throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();

		List<TargetRequestVo.LabelValuePair> labelValuePairs = request.getTargetLabelValuePairs();

		if (HRMSHelper.isNullOrEmpty(labelValuePairs)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		for (TargetRequestVo.LabelValuePair labelValuePair : labelValuePairs) {
			String label = labelValuePair.getTargetLabel();
			String targetValue = labelValuePair.getTargetValue();
			Double targetLong = Double.parseDouble(targetValue);
			List<KraRating> ratings = ratingDao.findByLabelStartingWith(label);

			if (HRMSHelper.isNullOrEmpty(ratings)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}

			for (KraRating rating : ratings) {
				rating.setTargetPercentage(targetLong);
				ratingDao.save(rating);
			}
		}
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		return response;
	}

	@Override
	public HRMSBaseResponse<CycleDefinationResponseVo> getCycleDefinationList(Long yearId, Long cycleId)
			throws HRMSException {

		HRMSBaseResponse<CycleDefinationResponseVo> response = new HRMSBaseResponse<>();
		long totalRecord = 0;

		validateRolePermission();

		CycleDefinationResponseVo cycleDefinationResponse = new CycleDefinationResponseVo();

		if (!HRMSHelper.isNullOrEmpty(yearId) && !HRMSHelper.isNullOrEmpty(cycleId)) {

			KraYear year = validateYear(yearId);
			KraCycle existingCycle = validateCycle(cycleId, year);

			List<KraCycleCalender> kracycleCalenderList = calenderDao.findByCycleIdAndIsActive(existingCycle,
					ERecordStatus.Y.name());
			List<MasterRole> roles = validateRolesCyclewise(existingCycle);

			SimpleDateFormat cycleFormatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat roleFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			// Nullable values handled here
			String cycleStartDate = null;
			String cycleEndDate = null;

			if (existingCycle.getStartDate() != null) {
				cycleStartDate = cycleFormatter.format(existingCycle.getStartDate());
			}

			if (existingCycle.getEndDate() != null) {
				cycleEndDate = cycleFormatter.format(existingCycle.getEndDate());
			}

			KraCycleResponseVo kraCycleResponsevo = new KraCycleResponseVo();
			kraCycleResponsevo.setCycleId(existingCycle.getId());
			kraCycleResponsevo.setCycleName(existingCycle.getCycleName());
			kraCycleResponsevo.setCycleType(existingCycle.getCycleTypeId());
			kraCycleResponsevo.setStartDate(cycleStartDate); // could be null
			kraCycleResponsevo.setEndDate(cycleEndDate); // could be null
			cycleDefinationResponse.setCycle(kraCycleResponsevo);

			// Set cycle status
			if (HRMSHelper.isNullOrEmpty(existingCycle.getStatus())) {
				cycleDefinationResponse.setStatus(null);
			} else {
				Optional<KraCycleStatus> cycleStatus = kraStatusDao.findById(existingCycle.getStatus().getId());
				if (HRMSHelper.isNullOrEmpty(cycleStatus)) {
					throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
				}
				cycleDefinationResponse.setStatus(cycleStatus.get().getStatus());
			}

			// Handle role-wise response
			List<RoleWiseCycleDefinationResponse> responsevo = validateResponse(kracycleCalenderList, roles,
					roleFormatter);

			validateSortingLogicForCycle(existingCycle, responsevo);
			cycleDefinationResponse.setRoleWiseResponse(responsevo);

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1501));
		}

		response.setResponseBody(cycleDefinationResponse);
		response.setResponseCode(1200);
		response.setTotalRecord(totalRecord);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from getCycleDefinationList Method");

		return response;
	}

	public List<MasterRole> validateRolesCyclewise(KraCycle existingCycle) {
		if (existingCycle == null || HRMSHelper.isNullOrEmpty(existingCycle.getCycleName())) {
			return Collections.emptyList(); // or throw an exception if cycle must be present
		}

		if (existingCycle.getCycleTypeId().equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {
			return roleDao.findByRoleName(ERole.HR.name(), ERole.MANAGER.name(), ERole.EMPLOYEE.name());
		} else {
			return roleDao.findByRoleName(ERole.HOD.name(), ERole.MANAGER.name(), ERole.EMPLOYEE.name());
		}
	}

	private KraCycle validateCycle(Long cycleId, KraYear year) throws HRMSException {
		KraCycle existingCycle = kraCycleDAO.findByYearAndId(year, cycleId);
		if (HRMSHelper.isNullOrEmpty(existingCycle)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		return existingCycle;
	}

	private KraYear validateYear(Long yearId) throws HRMSException {
		KraYear year = krayeardao.getById(yearId);
		if (HRMSHelper.isNullOrEmpty(year)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		return year;
	}

	public List<RoleWiseCycleDefinationResponse> validateResponse(List<KraCycleCalender> kracycleCalenderList,
			List<MasterRole> roles, SimpleDateFormat formatter) {

		List<RoleWiseCycleDefinationResponse> responsevo = new ArrayList<>();

		if (HRMSHelper.isNullOrEmpty(roles)) {
			return responsevo; // No roles to process
		}

		for (MasterRole role : roles) {
			RoleWiseCycleDefinationResponse roleWiseResponse = new RoleWiseCycleDefinationResponse();
			roleWiseResponse.setRoleName(role.getRoleName());

			KraCycleCalender matchedCalender = null;

			if (!HRMSHelper.isNullOrEmpty(kracycleCalenderList)) {
				matchedCalender = kracycleCalenderList.stream()
						.filter(cal -> cal.getRoleId() != null && cal.getRoleId().getRoleName() != null
								&& cal.getRoleId().getRoleName().equals(role.getRoleName()))
						.findFirst().orElse(null);
			}

			if (matchedCalender != null) {
				String startDateStr = matchedCalender.getStartDate() != null
						? formatter.format(matchedCalender.getStartDate())
						: null;
				String endDateStr = matchedCalender.getEndDate() != null
						? formatter.format(matchedCalender.getEndDate())
						: null;

				roleWiseResponse.setStartDate(startDateStr);
				roleWiseResponse.setEndDate(endDateStr);
			} else {
				roleWiseResponse.setStartDate(null);
				roleWiseResponse.setEndDate(null);
			}

			responsevo.add(roleWiseResponse);
		}

		return responsevo;
	}

	private void validateSortingLogicForCycle(KraCycle existingCycle,
			List<RoleWiseCycleDefinationResponse> responsevo) {
		if (existingCycle.getCycleTypeId().equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {
			Map<String, Integer> kpiRolePriority = new HashMap<>();
			kpiRolePriority.put(ERole.MANAGER.name(), 1);
			kpiRolePriority.put(ERole.HR.name(), 2);
			kpiRolePriority.put(ERole.EMPLOYEE.name(), 3);

			responsevo.sort(
					Comparator.comparing(role -> kpiRolePriority.getOrDefault(role.getRoleName(), Integer.MAX_VALUE)));
		} else {
			Map<String, Integer> otherRolePriority = new HashMap<>();
			otherRolePriority.put(ERole.EMPLOYEE.name(), 1);
			otherRolePriority.put(ERole.MANAGER.name(), 2);
			otherRolePriority.put(ERole.HOD.name(), 3);

			responsevo.sort(Comparator
					.comparing(role -> otherRolePriority.getOrDefault(role.getRoleName(), Integer.MAX_VALUE)));
		}
	}

	@Override
	public HRMSBaseResponse<String> saveKpiYear(KraYearVo request) throws HRMSException {
		log.info("Inside save kPI year method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isNullOrEmpty(request.getYear())) {

			defineKpiYearValidations(request, role);
			saveKraYear(request, loggedInEmpId);
			populateKpiCycle(request, loggedInEmpId);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1632));
			log.info("Exit from save kPI year method");
			return response;
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}
	}

	private void saveKraYear(KraYearVo request, Long loggedInEmpId) throws HRMSException {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int requsetedYear = parseYear(request.getYear());
		KraYear kraYear = new KraYear();
		kraYear.setCreatedBy(loggedInEmpId.toString());
		kraYear.setCreatedDate(new Date());
		if (currentYear == requsetedYear) {
			kraYear.setIsActive(IHRMSConstants.isActive);
		} else {
			kraYear.setIsActive(IHRMSConstants.isNotActive);
		}

		kraYear.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		kraYear.setYear(request.getYear());
		kraYear.setLabel(request.getYear());
		krayeardao.save(kraYear);

	}

	private void populateKpiCycle(KraYearVo request, Long loggedInEmpId) throws HRMSException {

		// Define default cycle order and state
		Map<String, String> cycleData = new LinkedHashMap<>();
		cycleData.put(IHRMSConstants.KPI_SUBMISSION, IHRMSConstants.isActive);
		cycleData.put(IHRMSConstants.MID_YEAR_CYCLE, IHRMSConstants.isNotActive);
		cycleData.put(IHRMSConstants.YEAR_END_CYCLE, IHRMSConstants.isNotActive);

		// Fetch the requested year
		KraYear year = krayeardao.findByYear(request.getYear());
		if (year == null) {
			log.error("Year not found for: {}", request.getYear());
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

		// Fetch cycle statuses
		KraCycleStatus openStatus = kraStatusDao.findByName(IHRMSConstants.OPEN);
		KraCycleStatus closedStatus = kraStatusDao.findByName(IHRMSConstants.CLOSED);
		if (openStatus == null || closedStatus == null) {
			log.error("Cycle status values not found in tbl_mst_kra_cycle_status");
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

		// Check for any existing active/open cycle
		boolean hasActiveOpenEntries = kraCycleDAO.existsByStatusAndIsActive(openStatus, IHRMSConstants.isActive);

		// Fixed mapping of cycle name to cycle type ID
		Map<String, Long> cycleTypeIdMap = new HashMap<>();
		cycleTypeIdMap.put(IHRMSConstants.KPI_SUBMISSION, 1L);
		cycleTypeIdMap.put(IHRMSConstants.MID_YEAR_CYCLE, 2L);
		cycleTypeIdMap.put(IHRMSConstants.YEAR_END_CYCLE, 3L);

		List<KraCycle> cyclesToSave = new ArrayList<>();

		for (Map.Entry<String, String> entry : cycleData.entrySet()) {
			String cycleName = entry.getKey();

			KraCycle cycle = new KraCycle();
			cycle.setCreatedBy(String.valueOf(loggedInEmpId));
			cycle.setCreatedDate(new Date());
			cycle.setDescription(cycleName);
			cycle.setStartDate(null);
			cycle.setEndDate(null);
			cycle.setCycleName(cycleName);
			cycle.setCycleTypeId(cycleTypeIdMap.get(cycleName));
			cycle.setYear(year);
			cycle.setIsLocked(IHRMSConstants.isNotActive);
			cycle.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());

			// Determine cycle activation and status
			if (hasActiveOpenEntries) {
				// Existing open cycle → mark all new cycles as closed/inactive
				cycle.setStatus(closedStatus);
				cycle.setIsActive(IHRMSConstants.isNotActive);
			} else if (IHRMSConstants.KPI_SUBMISSION.equals(cycleName)) {
				// First setup → only KPI_SUBMISSION is open/active
				cycle.setStatus(openStatus);
				cycle.setIsActive(IHRMSConstants.isActive);
			} else {
				// Remaining cycles are closed/inactive
				cycle.setStatus(closedStatus);
				cycle.setIsActive(IHRMSConstants.isNotActive);
			}

			cyclesToSave.add(cycle);
		}

		kraCycleDAO.saveAll(cyclesToSave);
	}

	private void defineKpiYearValidations(KraYearVo request, List<String> role) throws HRMSException {
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int requsetedYear = parseYear(request.getYear());

		if (requsetedYear < currentYear || requsetedYear > currentYear + 20) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1647));
		}
		KraYear year = krayeardao.findByYear(request.getYear());
		if (!HRMSHelper.isNullOrEmpty(year)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1631));
		}

		kraHelper.submitKpiYearValidation(request);
	}

	private int parseYear(String year) throws HRMSException {
		try {
			return Integer.parseInt(year);
		} catch (NumberFormatException e) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1631));
		}

	}

	@Override
	public HRMSBaseResponse<String> saveCategory(CategoryRequestVo request) throws HRMSException {
		log.info("Inside save KPI Category method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		validationForCategory(request, role);

		KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(kraYear)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		Category category;

		if (!HRMSHelper.isNullOrEmpty(request.getId()) && !HRMSHelper.isLongZero(request.getId())) {
			category = categoryDAO.findByIdAndIsActive(request.getId(), ERecordStatus.Y.name());
			if (HRMSHelper.isNullOrEmpty(category)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1908));
			}

			Category duplicate = categoryDAO.findByCategoryNameAndIsActive(request.getName(), ERecordStatus.Y.name());
			if (duplicate != null && duplicate.getId() != request.getId()) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1907));
			}

			setCategoryEntity(request, loggedInEmployee, kraYear, category);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1893));
		}

		else {
			category = categoryDAO.findByCategoryNameAndIsActive(request.getName(), ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(category)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1907)); // "Category already exists"
			}

			category = new Category();
			setCategoryEntity(request, loggedInEmployee, kraYear, category);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1634)); // "Category created successfully"
		}

		response.setResponseCode(1200);
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from save category method");
		return response;
	}

	private void setCategoryEntity(CategoryRequestVo request, Employee loggedInEmployee, KraYear kraYear,
			Category category) {
		category.setCategoryName(request.getName());
		category.setIsActive(IHRMSConstants.isActive);
		category.setKraYear(kraYear);
		category.setWeightage(request.getWeightage());
		category.setOrgId(loggedInEmployee.getOrgId());
		categoryDAO.save(category);
	}

	private void validationForCategory(CategoryRequestVo request, List<String> role) throws HRMSException {
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		if (request.getWeightage() == 0) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

		String weight = String.valueOf(request.getWeightage());

		if (!HRMSHelper.regexMatcher(weight, "^(100|[1-9][0-9]?|0?[1-9])$")) {
			throw new HRMSException(1636, ResponseCode.getResponseCodeMap().get(1636) + " Category Weightage ");
		}

	}

	@Override
	public HRMSBaseResponse<String> updateKraCycle(KraCycleRequestVo request) throws HRMSException {
		log.info("Inside updateKraCycle method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		validateRolePermission();
		kraHelper.updateKpiCycleValidation(request);

		KraCycle existingCycle = kraCycleDAO.findById(request.getCycleId()).get();
		KraYear year = krayeardao.getById(request.getYearId());
		if (HRMSHelper.isNullOrEmpty(year)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		if (!HRMSHelper.isNullOrEmpty(existingCycle)) {

			existingCycle.setCycleName(request.getCycleName());
			existingCycle.setDescription(request.getDescription());
			existingCycle
					.setStartDate(HRMSDateUtil.parse(request.getStartDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			existingCycle.setEndDate(HRMSDateUtil.parse(request.getEndDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			existingCycle.setOrgId(loggedInEmployee.getOrgId());
			existingCycle.setIsActive(ERecordStatus.Y.name());
			existingCycle.setYear(year);
			kraCycleDAO.save(existingCycle);

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1635));
		log.info("Exit from updateKraCycle method");
		return response;

	}

	@Override
	@Transactional
	public HRMSBaseResponse<String> saveCycleDefination(CycleDefinationRequestVo request) throws Exception {

		log.info("Inside saveCycleDefination method");

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());

		if (ObjectUtils.isEmpty(loggedInEmployee) || ObjectUtils.isEmpty(loggedInEmployee.getCandidate())) {
			throw new Exception("Logged-in employee or candidate details not found.");
		}

		if (ObjectUtils.isEmpty(request) || ObjectUtils.isEmpty(request.getCycle())
				|| ObjectUtils.isEmpty(request.getRoleWiseResponse())) {
			throw new Exception("Request, cycle, or role-wise response is empty.");
		}

		log.debug("Received cycle request: {}", request);

		validateRolePermission();
		validateRequestDates(request);

		KraCycle cycle = validateActiveCycle(request);
		if (ObjectUtils.isEmpty(cycle)) {
			throw new Exception("Active cycle could not be validated.");
		}

		auditLogService.setActionHeader("SaveCycleDefination", loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cycleStartDate = resetTime(dateFormat.parse(request.getCycle().getStartDate()));
		Date cycleEndDate = resetTime(dateFormat.parse(request.getCycle().getEndDate()));

		validateCycleDates(cycleStartDate, cycleEndDate);

		cycle.setStartDate(cycleStartDate);
		cycle.setEndDate(cycleEndDate);
		kraCycleDAO.save(cycle);

		List<MasterRole> allowedRoles;

		if (IHRMSConstants.KPI_SUBMISSION.equalsIgnoreCase(cycle.getCycleName())) {
			allowedRoles = roleDao.findByRoleName(ERole.HR.name(), ERole.MANAGER.name(), ERole.EMPLOYEE.name());

			kraserviceImpl.sendMailToHrOncycleDefineBYHim(loggedInEmployee);

			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1637));

			log.info("KPI_SUBMISSION cycle processed. Exiting.");
			return response;
		} else {
			allowedRoles = roleDao.findByRoleName(ERole.HOD.name(), ERole.MANAGER.name(), ERole.EMPLOYEE.name());
		}

		Date currentDate = resetTime(new Date());
		Date employeeStartDate = null;
		Date managerEndDate = null;

		for (RoleWiseCycleDefinationRequestVo roleResponse : request.getRoleWiseResponse()) {
			if (ObjectUtils.isEmpty(roleResponse) || ObjectUtils.isEmpty(roleResponse.getRoleName())) {
				log.warn("Skipping invalid role entry: {}", roleResponse);
				continue;
			}

			log.debug("Processing role: {}", roleResponse.getRoleName());

			LocalDateTime startDate = roleResponse.getStartDate();
			LocalDateTime endDate = roleResponse.getEndDate();

			validateCycleDates(cycleStartDate, cycleEndDate, startDate, endDate);

			MasterRole roleName = roleDao.findByRoleName(roleResponse.getRoleName().toUpperCase().trim());

			if (ObjectUtils.isEmpty(roleName) || !allowedRoles.contains(roleName)) {
				log.warn("Skipping unauthorized or invalid role: {}", roleResponse.getRoleName());
				continue;
			}

			switch (roleResponse.getRoleName().toUpperCase()) {
			case "EMPLOYEE":
				validateEmployeeDates(currentDate, startDate, endDate, cycleStartDate, cycleEndDate);
				employeeStartDate = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
				break;
			case "MANAGER":
				validateManagerDates(employeeStartDate, startDate, cycleStartDate, cycleEndDate);
				managerEndDate = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
				break;
			case "HOD":
				validatehodDates(managerEndDate, startDate);
				break;
			}

			KraCycleCalender cycleCalender = saveCycleCalendar(cycle, roleName, startDate, endDate);

			if (!ObjectUtils.isEmpty(cycleCalender) && !ObjectUtils.isEmpty(cycleCalender.getRoleId())) {
				String role = cycleCalender.getRoleId().getRoleName();

				if (ERole.MANAGER.name().equalsIgnoreCase(role)) {
					kraserviceImpl.sendMailToLineMangerOncycleDefination(cycle, roleName, cycleCalender);
				} else if (ERole.HOD.name().equalsIgnoreCase(role)) {
					kraserviceImpl.sendMailToHodOnCycleDefination(cycle, roleName, cycleCalender, loggedInEmployee);
				} else if (ERole.EMPLOYEE.name().equalsIgnoreCase(role)) {
					kraserviceImpl.sendMailToEmployeeOnCycleDefination(cycle, roleName, cycleCalender);
				}
			}
		}

		kraserviceImpl.sendMailToHrOncycleDefineBYHim(loggedInEmployee);

		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1637));

		log.info("Exiting saveCycleDefination method");
		return response;
	}

	/**
	 * send mail to hod on cycledefination
	 */
	private void sendMailToHodOnCycleDefination(KraCycle cycle, MasterRole roleName, KraCycleCalender cycleCalender,
			Employee loggedInEmployee) throws Exception {

		List<HodToDepartmentMap> hodlist = hodToDepartmentMap.findByIsActiveAndOrgId(ERecordStatus.Y.name(),
				loggedInEmployee.getOrgId());

		if (hodlist == null || hodlist.isEmpty()) {
			return;
		}

		List<String> emailAddresses = new ArrayList<>();
		List<String> hodNames = new ArrayList<>();

		for (HodToDepartmentMap hod : hodlist) {
			if (hod.getEmployeeId() == null)
				continue;

			Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), hod.getEmployeeId());
			if (employee == null || employee.getOfficialEmailId() == null || employee.getCandidate() == null)
				continue;

			emailAddresses.add(employee.getOfficialEmailId());

			String firstName = employee.getCandidate().getFirstName() != null ? employee.getCandidate().getFirstName()
					: "";
			String lastName = employee.getCandidate().getLastName() != null ? employee.getCandidate().getLastName()
					: "";

			hodNames.add(firstName + " " + lastName);
		}

		Iterator<String> emailIterator = emailAddresses.iterator();
		Iterator<String> nameIterator = hodNames.iterator();

		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");

		while (emailIterator.hasNext() && nameIterator.hasNext()) {
			String emailAddress = emailIterator.next();
			String hodName = nameIterator.next();

			EmailRequestVO hodEmail = new EmailRequestVO();
			hodEmail.setEmailAddress(emailAddress);
			hodEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

			TemplateVO hodTemplate = new TemplateVO();
			hodTemplate.setTemplateName(IHRMSConstants.HOD_EMAIL_ON_DEFINE_CYCLE);

			List<PlaceHolderVO> placeHolders = new ArrayList<>();

			PlaceHolderVO namePH = new PlaceHolderVO();
			namePH.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
			namePH.setExpressionVariableValue(hodName);
			placeHolders.add(namePH);

			// Start Date
			PlaceHolderVO startDatePH = new PlaceHolderVO();
			startDatePH.setExpressionVariableName(IHRMSConstants.START_DATE);
			if (cycleCalender.getStartDate() != null) {
				startDatePH.setExpressionVariableValue(sdf.format(cycleCalender.getStartDate()));
			} else {
				startDatePH.setExpressionVariableValue("N/A");
			}
			placeHolders.add(startDatePH);

			// End Date
			PlaceHolderVO endDatePH = new PlaceHolderVO();
			endDatePH.setExpressionVariableName(IHRMSConstants.END_DATE);
			if (cycleCalender.getEndDate() != null) {
				endDatePH.setExpressionVariableValue(sdf.format(cycleCalender.getEndDate()));
			} else {
				endDatePH.setExpressionVariableValue("N/A");
			}
			placeHolders.add(endDatePH);

			hodTemplate.setPlaceHolders(placeHolders);
			hodEmail.setTemplateVo(hodTemplate);

			emailService.insertInEmailTxnTable(hodEmail);
		}
	}

	private void validatehodDates(Date managerEndDate, LocalDateTime startDate) throws HRMSException {

		if (managerEndDate == null || startDate == null) {
			return;
		}

		LocalDateTime managerEndDateTime = managerEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		if (!startDate.isAfter(managerEndDateTime)) {
			throw new HRMSException(1759, ResponseCode.getResponseCodeMap().get(1759));
		}
	}

	private void validateManagerDates(Date employeeStartDate, LocalDateTime managerStartDate, Date cycleStartDate,
			Date cycleEndDate) throws HRMSException {

		if (employeeStartDate == null) {
			throw new HRMSException(1786, ResponseCode.getResponseCodeMap().get(1786));
		}

		LocalDateTime employeeStartDateTime = employeeStartDate.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();

		if (!managerStartDate.isAfter(employeeStartDateTime)) {
			throw new HRMSException(1839, ResponseCode.getResponseCodeMap().get(1839));

		}

		if (cycleStartDate != null && cycleEndDate != null) {
			LocalDateTime cycleStartDateTime = cycleStartDate.toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			LocalDateTime cycleEndDateTime = cycleEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			if (managerStartDate.isBefore(cycleStartDateTime) || managerStartDate.isAfter(cycleEndDateTime)) {
				throw new HRMSException(1840, ResponseCode.getResponseCodeMap().get(1840));
			}
		}
	}

	private void validateCycleDates(Date cyclestartDate, Date cycleendDate) throws HRMSException {
		if (cycleendDate.before(cyclestartDate)) {
			throw new HRMSException(1779, ResponseCode.getResponseCodeMap().get(1779));
		}
	}

	private void sendMailToEmployeeOnCycleDefination(KraCycle cycle, MasterRole roleName,
			KraCycleCalender cycleCalender) throws Exception {

		List<Employee> loggedInEmployee = employeeDAO.findByIsActive(ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(loggedInEmployee)) {
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");

		for (Employee employee : loggedInEmployee) {
			// Skip if official email is null or blank
			if (employee.getOfficialEmailId() == null || employee.getOfficialEmailId().trim().isEmpty()) {
				continue;
			}

			// Skip if candidate is null
			if (employee.getCandidate() == null) {
				continue;
			}

			EmailRequestVO email = new EmailRequestVO();
			email.setEmailAddress(employee.getOfficialEmailId());
			email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

			TemplateVO hrTemplate = new TemplateVO();
			hrTemplate.setTemplateName(IHRMSConstants.EMPLOYEE_EMAIL_ON_DEFINE_CYCLE);

			List<PlaceHolderVO> hrPlaceHolders = new ArrayList<>();

			// Employee Name
			String fullName = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName();
			PlaceHolderVO empPlaceHolder = new PlaceHolderVO();
			empPlaceHolder.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
			empPlaceHolder.setExpressionVariableValue(fullName);
			hrPlaceHolders.add(empPlaceHolder);

			// Start Date
			PlaceHolderVO startdatePlaceHolderVo = new PlaceHolderVO();
			startdatePlaceHolderVo.setExpressionVariableName(IHRMSConstants.START_DATE);
			if (cycleCalender.getStartDate() != null) {
				startdatePlaceHolderVo.setExpressionVariableValue(sdf.format(cycleCalender.getStartDate()));
			} else {
				startdatePlaceHolderVo.setExpressionVariableValue("N/A");
			}
			hrPlaceHolders.add(startdatePlaceHolderVo);

			// End Date
			PlaceHolderVO enddatePlaceHolderVo = new PlaceHolderVO();
			enddatePlaceHolderVo.setExpressionVariableName(IHRMSConstants.END_DATE);
			if (cycleCalender.getEndDate() != null) {
				enddatePlaceHolderVo.setExpressionVariableValue(sdf.format(cycleCalender.getEndDate()));
			} else {
				enddatePlaceHolderVo.setExpressionVariableValue("N/A");
			}
			hrPlaceHolders.add(enddatePlaceHolderVo);

			hrTemplate.setPlaceHolders(hrPlaceHolders);
			email.setTemplateVo(hrTemplate);

			emailService.insertInEmailTxnTable(email);
		}
	}

	/**
	 * 
	 * @param cycle
	 * @param roleName
	 * @param cycleCalender send mail to hcd on cycle defination
	 */
	private void sendMailToHcdOnCycleDefination(KraCycle cycle, MasterRole roleName, KraCycleCalender cycleCalender,
			Employee employee) throws Exception {

		LoginEntityType loginRole = loginEntityTypeDAO.findByRoleName(ERole.HR.name());
		List<HREmailDTO> hrList = employeeDAO.findAllHREmailId(loginRole.getId(), IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(hrList)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		for (HREmailDTO hrEmail : hrList) {

			EmailRequestVO email = new EmailRequestVO();
			email.setEmailAddress(hrEmail.getOfficialEmailId());
			email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

			TemplateVO hrTemplate = new TemplateVO();
			hrTemplate.setTemplateName(IHRMSConstants.HR_EMAIL_ON_DEFINE_CYCLE);

			List<PlaceHolderVO> hrPlaceHolders = new ArrayList<>();

			PlaceHolderVO empPlaceHolder = new PlaceHolderVO();
			empPlaceHolder.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
			empPlaceHolder.setExpressionVariableValue(hrEmail.getFirstName() + " " + hrEmail.getLastName());
			hrPlaceHolders.add(empPlaceHolder);

			SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");

			PlaceHolderVO startdatePlaceHolderVo = new PlaceHolderVO();
			startdatePlaceHolderVo.setExpressionVariableName(IHRMSConstants.START_DATE);
			if (cycleCalender.getStartDate() != null) {
				startdatePlaceHolderVo.setExpressionVariableValue(sdf.format(cycleCalender.getStartDate()));
			} else {
				startdatePlaceHolderVo.setExpressionVariableValue("N/A");
			}
			hrPlaceHolders.add(startdatePlaceHolderVo);

			PlaceHolderVO enddatePlaceHolderVo = new PlaceHolderVO();
			enddatePlaceHolderVo.setExpressionVariableName(IHRMSConstants.END_DATE);
			if (cycleCalender.getEndDate() != null) {
				enddatePlaceHolderVo.setExpressionVariableValue(sdf.format(cycleCalender.getEndDate()));
			} else {
				enddatePlaceHolderVo.setExpressionVariableValue("N/A");
			}
			hrPlaceHolders.add(enddatePlaceHolderVo);

			hrTemplate.setPlaceHolders(hrPlaceHolders);
			email.setTemplateVo(hrTemplate);
			emailService.insertInEmailTxnTable(email);
		}
	}

	private void sendMailToLineMangerOncycleDefination(KraCycle cycle, MasterRole roleName,
			KraCycleCalender cycleCalender) throws Exception {

		List<EmployeeReportingManager> reportingManager = reportingManagerDao.findByIsActive(ERecordStatus.Y.name());

		for (EmployeeReportingManager manager : reportingManager) {
			if (manager.getReporingManager() == null) {
				continue;
			}

			Long loggedInEmpId = manager.getReporingManager().getId();
			Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());

			if (loggedInEmployee == null) {
				continue;
			}

			EmailRequestVO email = new EmailRequestVO();
			email.setEmailAddress(loggedInEmployee.getOfficialEmailId());
			email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

			TemplateVO hrTemplate = new TemplateVO();
			hrTemplate.setTemplateName(IHRMSConstants.MANAGER_EMAIL_ON_DEFINE_CYCLE);

			List<PlaceHolderVO> hrPlaceHolders = new ArrayList<>();

			PlaceHolderVO empPlaceHolder = new PlaceHolderVO();
			empPlaceHolder.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
			empPlaceHolder.setExpressionVariableValue(loggedInEmployee.getCandidate().getFirstName() + " "
					+ loggedInEmployee.getCandidate().getLastName());
			hrPlaceHolders.add(empPlaceHolder);

			String startDateStr = "";
			if (cycleCalender.getStartDate() != null) {
				startDateStr = new SimpleDateFormat("MMMM dd, yyyy").format(cycleCalender.getStartDate());
			}

			PlaceHolderVO startdatePlaceHolderVo = new PlaceHolderVO();
			startdatePlaceHolderVo.setExpressionVariableName(IHRMSConstants.START_DATE);
			startdatePlaceHolderVo.setExpressionVariableValue(startDateStr);
			hrPlaceHolders.add(startdatePlaceHolderVo);

			String endDateStr = "";
			if (cycleCalender.getEndDate() != null) {
				endDateStr = new SimpleDateFormat("MMMM dd, yyyy").format(cycleCalender.getEndDate());
			}

			PlaceHolderVO enddatePlaceHolderVo = new PlaceHolderVO();
			enddatePlaceHolderVo.setExpressionVariableName(IHRMSConstants.END_DATE);
			enddatePlaceHolderVo.setExpressionVariableValue(endDateStr);
			hrPlaceHolders.add(enddatePlaceHolderVo);

			hrTemplate.setPlaceHolders(hrPlaceHolders);
			email.setTemplateVo(hrTemplate);
			emailService.insertInEmailTxnTable(email);
		}
	}

	private void sendMailToHrOncycleDefineBYHim(Employee loggedInEmpId) throws Exception {
		EmailRequestVO email = new EmailRequestVO();
		email.setEmailAddress(loggedInEmpId.getOfficialEmailId());

		email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
		TemplateVO template = new TemplateVO();
		template.setTemplateName(IHRMSConstants.HR_EMAIL_ON_CYCLE_DEFINE_BY_HIM);

		List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
		placeHolderVo.setExpressionVariableValue(
				loggedInEmpId.getCandidate().getFirstName() + " " + loggedInEmpId.getCandidate().getLastName());
		placeHolders.add(placeHolderVo);
		template.setPlaceHolders(placeHolders);
		email.setTemplateVo(template);
		emailService.insertInEmailTxnTable(email);

	}

	private Date resetTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

//	private void validateMangerDateForCycle(Date employeeEndDate, Date startDate) throws HRMSException {
//		if (employeeEndDate == null || !startDate.after(employeeEndDate)) {
//			throw new HRMSException(1758, ResponseCode.getResponseCodeMap().get(1758));
//		}
//	}
//
//	private void validateManagerDatesForCycle(Date managerEndDate, Date startDate) throws HRMSException {
//		if (managerEndDate == null || !startDate.after(managerEndDate)) {
//			throw new HRMSException(1759, ResponseCode.getResponseCodeMap().get(1759));
//		}
//	}

	
	private void validateEmployeeDates(Date currentDate, LocalDateTime startDate, LocalDateTime endDate,
			Date cycleStartDate, Date cycleEndDate) throws HRMSException {

		LocalDateTime currentDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		if (startDate == null || endDate == null || cycleStartDate == null || cycleEndDate == null) {
			throw new HRMSException(1786, ResponseCode.getResponseCodeMap().get(1786));
		}

		LocalDateTime cycleStartDateTime = cycleStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime cycleEndDateTime = cycleEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		if (startDate.isAfter(endDate)) {
			throw new HRMSException(1841, ResponseCode.getResponseCodeMap().get(1841));
		}

		if (startDate.isBefore(cycleStartDateTime) || endDate.isAfter(cycleEndDateTime)) {
			throw new HRMSException(1842, ResponseCode.getResponseCodeMap().get(1842));
		}
	}

	
	private void validateCycleDates(Date cycleStartDate, Date cycleEndDate, LocalDateTime startDate,
			LocalDateTime endDate) throws HRMSException {

		if (startDate == null || endDate == null) {
			return;
		}

		if (!endDate.isAfter(startDate)) {
			throw new HRMSException(1755, ResponseCode.getResponseCodeMap().get(1755)); // End date must be after start
		}

		if (cycleStartDate == null || cycleEndDate == null) {
			throw new HRMSException(1756, "Cycle start or end date is not defined");
		}

		LocalDateTime cycleStartLdt = cycleStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime cycleEndLdt = cycleEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		if (startDate.isBefore(cycleStartLdt) || endDate.isAfter(cycleEndLdt)) {
			throw new HRMSException(1754, ResponseCode.getResponseCodeMap().get(1754));
		}
	}

	private void validateRequestDates(CycleDefinationRequestVo request) throws HRMSException {
		if (request == null || request.getCycle() == null || request.getRoleWiseResponse() == null) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

		if (HRMSHelper.isNullOrEmpty(request.getCycle().getStartDate())
				|| HRMSHelper.isNullOrEmpty(request.getCycle().getEndDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}
		/*
		 * if (!request.getCycle().getCycleName().equalsIgnoreCase(IHRMSConstants.
		 * KPI_SUBMISSION)) { for (RoleWiseCycleDefinationRequestVo role :
		 * request.getRoleWiseResponse()) { if
		 * (HRMSHelper.isNullOrEmpty(role.getStartDate()) ||
		 * HRMSHelper.isNullOrEmpty(role.getEndDate())) { throw new HRMSException(1501,
		 * ResponseCode.getResponseCodeMap().get(1501)); } } }
		 */
	}

	private KraCycle validateActiveCycle(CycleDefinationRequestVo request) throws HRMSException {
		KraCycle cycle = kraCycleDAO.getById(request.getCycle().getCycleId());
		if (HRMSHelper.isNullOrEmpty(cycle)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		return cycle;
	}

	public void validateRolePermission() throws HRMSException {
		validateRolePermissionforUnlocked();
	}

	private void validateEmployeeDates(Date hrEndDate, Date startDate) throws HRMSException {
		if (hrEndDate == null) {
			throw new HRMSException(1752, ResponseCode.getResponseCodeMap().get(1752));
		}
		if (!startDate.after(hrEndDate)) {
			throw new HRMSException(1753, ResponseCode.getResponseCodeMap().get(1753));
		}
	}

	private void validateHRDates(Date managerEndDate, Date startDate) throws HRMSException {
		if (managerEndDate == null) {
			throw new HRMSException(1750, ResponseCode.getResponseCodeMap().get(1750));
		}
		if (!startDate.after(managerEndDate)) {
			throw new HRMSException(1751, ResponseCode.getResponseCodeMap().get(1751));
		}
	}

	private void validateManagerDates(Date currentDate, Date startDate, Date endDate) throws HRMSException {
		if (startDate.before(currentDate) || endDate.before(currentDate)) {
			throw new HRMSException(1749, ResponseCode.getResponseCodeMap().get(1749));
		}
	}

	private KraCycleCalender saveCycleCalendar(KraCycle cycle, MasterRole roleName, LocalDateTime startDate,
			LocalDateTime endDate) {
		KraCycleCalender calender = calenderDao.findByCycleIdAndRoleIdAndIsActive(cycle, roleName,
				ERecordStatus.Y.name());

		if (HRMSHelper.isNullOrEmpty(calender)) {
			calender = new KraCycleCalender();
		}

		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);

		if (startDate != null) {
			calender.setStartDate(Timestamp.valueOf(startDate));
		} else {
			calender.setStartDate(null);
		}

		if (endDate != null) {
			calender.setEndDate(Timestamp.valueOf(endDate));
		} else {
			calender.setEndDate(null);
		}

		calender.setCycleId(cycle);
		calender.setIsActive(ERecordStatus.Y.name());
		calender.setStatus(status);
		calender.setRoleId(roleName);

		return calenderDao.save(calender);
	}

	@Override
	public HRMSBaseResponse<String> saveSubCategory(SubCategoryRequestVo request) throws HRMSException {
		log.info("Inside save SubCategory method");

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		validateSubCategoryRequest(request, role);

		Category category = categoryDAO.findByIdAndIsActive(request.getCategoryId(), ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(category)) {
			throw new HRMSException(1638, ResponseCode.getResponseCodeMap().get(1638));
		}

		Subcategory subcategory = (request.getId() != null)
				? subCategoryDao.findByIdAndIsActive(request.getId(), ERecordStatus.Y.name())
				: new Subcategory();

		if (request.getId() != null && HRMSHelper.isNullOrEmpty(subcategory)) {
			throw new HRMSException(1641, "Subcategory not found for update");
		}

		Subcategory duplicateCheck = subCategoryDao.findBySubCategoryNameAndCategoryAndIsActive(
				request.getSubCategoryname() + " " + request.getStageName().trim(), category, ERecordStatus.Y.name());
		if (duplicateCheck != null && (request.getId() == null || !duplicateCheck.getId().equals(request.getId()))) {
			throw new HRMSException(1639, ResponseCode.getResponseCodeMap().get(1639));
		}

		subcategory = KraTransformUtil.transformToSubcategory(request, loggedInEmployee, category, subcategory);

		subCategoryDao.save(subcategory);

		response.setResponseCode(1200);
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseMessage(request.getId() != null ? ResponseCode.getResponseCodeMap().get(1898)
				: ResponseCode.getResponseCodeMap().get(1640));

		log.info("Exit from save SubCategory method");
		return response;
	}

	private void validateSubCategoryRequest(SubCategoryRequestVo request, List<String> role) throws HRMSException {
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		if (request.getCategoryId() == null) {
			throw new HRMSException(1638, ResponseCode.getResponseCodeMap().get(1638));
		}
		if (request.getSubCategoryname() == null || request.getSubCategoryname().trim().isEmpty()) {
			throw new HRMSException(1641, ResponseCode.getResponseCodeMap().get(1641));
		}
	}

	@Override
	public HRMSBaseResponse<String> addOrgKpi(OrgnizationalKpiVo request) throws Exception {
		log.info("Inside Add Orgnizational kpi method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(loggedInEmployee)) {
			validateRolePermission();
			log.info("obj:" + request.getObjectiveWeight());
			auditLogService.setActionHeader("AddOrgKpi", loggedInEmployee.getCandidate().getEmployee(),
					loggedInEmployee.getCandidate().getEmployee());
			kraHelper.addOrgKpiValidations(request);
			setGreadToKpiEntity(request, loggedInEmpId, loggedInEmployee);
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1748));
		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		log.info("Exit from Add Orgnizational KPI method");
		return response;

	}

	private void setGreadToKpiEntity(OrgnizationalKpiVo request, Long loggedInEmpId, Employee loggedInEmployee)
			throws Exception {
		String isEdit = IHRMSConstants.isNotActive;
		GradeToObjectiveMapping objectiveMapping = new GradeToObjectiveMapping();
		validateObjectiveWeightAndSetOrgKpi(request, loggedInEmpId, loggedInEmployee, objectiveMapping, isEdit);
		sendMailToHROnAddOrgKpi(loggedInEmployee);
	}

	private void sendMailToHROnAddOrgKpi(Employee loggedInEmployee) throws Exception {
		EmailRequestVO email = new EmailRequestVO();
		email.setEmailAddress(loggedInEmployee.getOfficialEmailId());
		email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
		TemplateVO template = new TemplateVO();
		template.setTemplateName(IHRMSConstants.ORG_KPI_TO_HR);
		List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
		placeHolderVo.setExpressionVariableValue(
				loggedInEmployee.getCandidate().getFirstName() + " " + loggedInEmployee.getCandidate().getLastName());
		placeHolders.add(placeHolderVo);
		template.setPlaceHolders(placeHolders);
		email.setTemplateVo(template);
		emailService.insertInEmailTxnTable(email);
	}

	@Transactional
	@Override
	public HRMSBaseResponse<String> saveObejectives(PMSObjectivesRequestVo request) throws HRMSException {
		log.info("Inside save objectives  method");

		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		Kra kpi = kraDao.findByIdAndIsActive(request.getKraId(), IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kpi)) {
			Employee empKpi = kpi.getEmployee();
			auditLogService.setActionHeader(IHRMSConstants.SAVE_OBJECTIVES,
					loggedInEmployee.getCandidate().getEmployee(), empKpi.getCandidate().getEmployee());
		}
		validateManagerRole();
		validateRequest(request);
		List<DelegationMapping> reportingManager = delegationMappingDAO.findByDelegationToAndIsActive(loggedInEmpId,
				ERecordStatus.Y.name());
		KraWf kraWf = kraWfDao.findByKraAndIsActive(kpi, ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(reportingManager)) {
			kraWf.setDelegatedTo(loggedInEmployee);
			kraWfDao.save(kraWf);
		}
		KraCycle cycle = validateKraCycle(request);
		KraYear year = validateYear(cycle);
		Kra kra = validateKra(request);

		Category category = categoryDAO.findByCategoryNameAndIsActive(IHRMSConstants.FUNCTION_SPECIFIC_CATEGORY,
				ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(category)) {
			throw new HRMSException(1638, ResponseCode.getResponseCodeMap().get(1638));
		}

		Subcategory subCatVO = subCategoryDao.findByCategoryAndIsActive(category, ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(subCatVO)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		List<KraDetails> kraDetailsList = kraDetailsDao
				.findByIsActiveAndSubcategoryIdAndKraCycleAndKra(ERecordStatus.Y.name(), subCatVO.getId(), cycle, kra);

		Float categoryWeight = 0.0f;
		Float objectiveWeight = 0.0f;
		categoryWeight = Float.valueOf(category.getWeightage());
		if (!HRMSHelper.isNullOrEmpty(kraDetailsList)) {
			for (KraDetails details : kraDetailsList) {
				objectiveWeight += details.getObjectiveWeightage();
			}
		}
		if (objectiveWeight < categoryWeight) {
			float objWeight = categoryWeight - objectiveWeight;
			float remaningObjWeight = objWeight;
			double roundedObjWeight = Math.round(remaningObjWeight * 100.0) / 100.0;
			validateObjectiveWeightage(request);
			Double objectiveWeightage = Double.parseDouble(request.getObjectiveWeight());
//			float objectiveWeightage = Float.parseFloat(request.getObjectiveWeight());
			if (roundedObjWeight >= objectiveWeightage) {
				KraDetails details = new KraDetails();
				KpiMetricMaster metric = kpiMetricDao.getById(request.getMetric().getId());
				if (HRMSHelper.isNullOrEmpty(metric)) {
					throw new HRMSException(1646, ResponseCode.getResponseCodeMap().get(1646));
				}
				PMSKraMetricVo metricVo = new PMSKraMetricVo();
				metricVo.setId(metric.getId());
				metricVo.setMetricValue(metric.getMetric());
				details.setCategoryId(category.getId());
				details.setSubcategoryId(subCatVO.getId());
				details.setKra(kra);
				details.setKraCycle(cycle);
				details.setYear(year.getYear());
				details.setDescription(request.getName());
				details.setCreatedBy(String.valueOf(loggedInEmpId));
				details.setCreatedDate(new Date());
				details.setOrgId(loggedInEmployee.getOrgId());
				details.setIsActive(ERecordStatus.Y.name());
				details.setIsEdit(ERecordStatus.Y.name());
				details.setIsColor(ERecordStatus.Y.name());
				details.setMeasurementCriteria(metric.getMetric());
				details.setKraDetails(request.getName());
				float floatValue = (float) category.getWeightage();
				details.setWeightage(floatValue);
				details.setObjectiveWeightage(objectiveWeightage.floatValue());
				KraDetails savedDetails = kraDetailsDao.save(details);
				remaningObjWeight -= savedDetails.getObjectiveWeightage();
			} else {
				throw new HRMSException(1644, ResponseCode.getResponseCodeMap().get(1644));
			}
		} else {
			throw new HRMSException(1644, ResponseCode.getResponseCodeMap().get(1644));
		}

		response.setResponseCode(1200);
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1757));
		log.info("Exit from save Obejectives method");
		return response;

	}

	private Kra validateKra(PMSObjectivesRequestVo request) throws HRMSException {
		Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(kra)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		return kra;
	}

	private KraYear validateYear(KraCycle cycle) throws HRMSException {
		KraYear year = krayeardao.getById(cycle.getYear().getId());
		if (HRMSHelper.isNullOrEmpty(year)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		return year;
	}

	private KraCycle validateKraCycle(PMSObjectivesRequestVo request) throws HRMSException {
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
			return kraCycleDAO.findById(request.getKraCycleId())
					.orElseThrow(() -> new HRMSException(1221, ResponseCode.getResponseCodeMap().get(1221)));
		}

		KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getKraCycleId(), ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(cycle)) {
			throw new HRMSException(1221, ResponseCode.getResponseCodeMap().get(1221));
		}
		return cycle;
	}

	private void validateRequest(PMSObjectivesRequestVo request) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(request)) {

			if (HRMSHelper.isNullOrEmpty(request.getKraId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
			}

			if (HRMSHelper.isNullOrEmpty(request.getName()) || HRMSHelper.isNullOrEmpty(request.getObjectiveWeight())
					|| HRMSHelper.isNullOrEmpty(request.getMetric())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}
	}

	private void validateObjectiveWeightage(PMSObjectivesRequestVo objVo) throws HRMSException {
		if (!objVo.getObjectiveWeight().matches("^\\d{1,2}\\.\\d{2}$")) {
			throw new HRMSException(1761, ResponseCode.getResponseCodeMap().get(1761));
		}
		float objectiveWeightage = Float.parseFloat(objVo.getObjectiveWeight());
		if (objectiveWeightage <= 0.0f) {
			throw new HRMSException(1645, ResponseCode.getResponseCodeMap().get(1645));
		}
		MasterConfig minvalue = masterConfigService.getConfig(ERecordStatus.MIN_VALUE.name());
		MasterConfig maxvalue = masterConfigService.getConfig(ERecordStatus.MAX_VALUE.name());

		float minweight = Float.parseFloat(minvalue.getValue());
		float maxweight = Float.parseFloat(maxvalue.getValue());

		if (objectiveWeightage < minweight || objectiveWeightage > maxweight) {
			throw new HRMSException(1756, ResponseCode.getResponseCodeMap().get(1756));
		}
	}

	private void validateManagerRole() throws HRMSException {
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!(HRMSHelper.isRolePresent(role, ERole.MANAGER.name())
				|| HRMSHelper.isRolePresent(role, ERole.DELEGATOR.name()))) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	@Override
	public HRMSBaseResponse<List<CycleResponseVo>> getCycleList(Long yearId) throws HRMSException {
		HRMSBaseResponse<List<CycleResponseVo>> response = new HRMSBaseResponse<>();
		log.info("get getCycleList Method");
		validateRolePermission();
		KraYear year = krayeardao.findById(yearId).get();
		if (HRMSHelper.isNullOrEmpty(year)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		List<KraCycle> kraCycle = kraCycleDAO.findByYearOrderByIdAsc(year);
		if (HRMSHelper.isNullOrEmpty(kraCycle)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		List<CycleResponseVo> cycleList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(kraCycle)) {
			for (KraCycle cycle : kraCycle) {
				List<KraCycleCalender> calenderlist = kraCalenderDao.findByCycleIdAndIsActive(cycle,
						ERecordStatus.Y.name());
				List<MasterRole> roles = roleDao.findByRoleName(ERole.HR.name(), ERole.MANAGER.name(),
						ERole.EMPLOYEE.name());
				CycleResponseVo cycleVO = new CycleResponseVo();
				cycleVO.setId(cycle.getId());
				cycleVO.setCycle(cycle.getCycleName());
				cycleVO.setCycleType(cycle.getCycleTypeId());
				cycleVO.setStatus(cycle.getStatus().getStatus());
				Set<Long> roleIds = roles.stream().map(MasterRole::getId).collect(Collectors.toSet());
				Set<Long> calenderRoleIds = new HashSet<>();
				for (KraCycleCalender calender : calenderlist) {
					calenderRoleIds.add(calender.getRoleId().getId());
				}
				if (calenderRoleIds.containsAll(roleIds)) {
					cycleVO.setIsDateEntered("Y");
				} else {
					cycleVO.setIsDateEntered("N");
				}
				cycleList.add(cycleVO);
				cycleVO.setIslocked(cycle.getIsLocked());
			}

		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		response.setResponseBody(cycleList);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit getCycleList Method");
		return response;

	}

	@Override
	public HRMSBaseResponse<PMSObjectivesRequestVo> getObjectives(Long id) throws HRMSException {
		HRMSBaseResponse<PMSObjectivesRequestVo> response = new HRMSBaseResponse<>();
		log.info("get getObjectives Method");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		validateRolePermission();
		KraDetails kradetails = kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), id);
		if (HRMSHelper.isNullOrEmpty(kradetails)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		PMSObjectivesRequestVo obejctiveResponse = new PMSObjectivesRequestVo();
		obejctiveResponse.setId(id);
		obejctiveResponse.setKraId(kradetails.getKra().getId());
		obejctiveResponse.setKraCycleId(kradetails.getKraCycle().getId());
		obejctiveResponse.setName(kradetails.getKraDetails());
		obejctiveResponse.setComment(kradetails.getHcdComment());
		String objectiveWeightStr = String.valueOf(kradetails.getObjectiveWeightage());
		String Objstr = Float.toString(kradetails.getObjectiveWeightage());
		obejctiveResponse.setObjectiveWeight(Objstr);
		KpiMetricMaster metric = validateMetrics(kradetails);
		PMSKraMetricVo metricvo = new PMSKraMetricVo();
		metricvo.setId(metric.getId());
		metricvo.setMetricValue(metric.getMetric());
		obejctiveResponse.setMetric(metricvo);
		response.setResponseBody(obejctiveResponse);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit getObjectives Method");
		return response;
	}

	public KpiMetricMaster validateMetrics(KraDetails kradetails) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(kradetails.getMeasurementCriteria())) {
			throw new HRMSException(1646, ResponseCode.getResponseCodeMap().get(1646));
		}
		KpiMetricMaster metric = kpiMetricDao.findByMetric(kradetails.getMeasurementCriteria());
		if (HRMSHelper.isNullOrEmpty(metric)) {
			throw new HRMSException(1646, ResponseCode.getResponseCodeMap().get(1646));
		}
		return metric;
	}

	/*
	 * @Override // @Transactional(rollbackFor = Exception.class) public
	 * HRMSBaseResponse<String> generateKPITemplates(GenerateKPITemplateRequestVo
	 * request) throws HRMSException {
	 * log.info("Inside generateKPITemplates method"); HRMSBaseResponse<String>
	 * response = new HRMSBaseResponse<>(); try { Long loggedInEmpId =
	 * SecurityFilter.TL_CLAIMS.get().getEmployeeId(); Employee loggedInEmployee =
	 * employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
	 * validateRolePermissionforUnlocked();
	 * 
	 * String year = request.getYear(); KraYear kraYear =
	 * kraYearDao.findByYear(year); if (kraYear == null) {
	 * log.warn("KRA Year not found for year: {}", year);
	 * response.setResponseCode(1201); response.setResponseMessage("No Data Found");
	 * return response; }
	 * 
	 * List<KraCycle> kpiCycles = kraCycleDAO.findByYearAndOrgId(kraYear,
	 * loggedInEmployee.getOrgId()); // List<Employee> employees = //
	 * employeeDAO.findByIsActiveAndIsAllowedInCycle(ERecordStatus.Y.name(), //
	 * ERecordStatus.Y.name()); List<Employee> employees =
	 * employeeDAO.findByIsActiveAndIsAllowedInCycle(ERecordStatus.Y.name(),
	 * ERecordStatus.Y.name());
	 * 
	 * List<Long> skippedEmployeeIds = new ArrayList<>();
	 * 
	 * List<Employee> validEmployees = employees.stream().filter(emp -> { if
	 * (emp.getCycleAllowed() == null || emp.getCycleAllowed().trim().isEmpty()) {
	 * skippedEmployeeIds.add(emp.getId()); return false; } return
	 * ERecordStatus.Y.name().equals(emp.getCycleAllowed());
	 * }).collect(Collectors.toList());
	 * 
	 * if (!skippedEmployeeIds.isEmpty()) {
	 * System.out.println("Skipped employees due to null/empty 'isAllowedInCycle': "
	 * + skippedEmployeeIds); }
	 * 
	 * if (validEmployees.isEmpty() || kpiCycles.isEmpty()) {
	 * log.warn("No active employees or KPI cycles found for the year: {}", year);
	 * response.setResponseCode(1201); response.setResponseMessage("No Data Found");
	 * return response; }
	 * 
	 * Map<Long, Organization> orgMap =
	 * hrIhrmsOrganizationDAO.findByIsActive(IHRMSConstants.isActive).stream()
	 * .collect(Collectors.toMap(Organization::getId, org -> org));
	 * 
	 * Map<Long, List<ViKpiTemplate>> employeeKpiTemplateMap = templateDAO
	 * .findByEmpIdIn(validEmployees.stream().map(Employee::getId).collect(
	 * Collectors.toList())).stream() .filter(v -> v.getEmpId() !=
	 * null).collect(Collectors.groupingBy(ViKpiTemplate::getEmpId));
	 * 
	 * Map<Long, Subcategory> subcategoryMap = subCategoryDao.findAll().stream()
	 * .collect(Collectors.toMap(Subcategory::getId, s -> s));
	 * 
	 * Map<Long, Category> categoryMap = categoryDAO.findAll().stream()
	 * .collect(Collectors.toMap(Category::getId, s -> s));
	 * 
	 * List<Kra> kraList = new ArrayList<>(); List<KraWf> kraWfList = new
	 * ArrayList<>(); List<KraDetails> allKraDetailsList = new ArrayList<>(); //
	 * Global list to store all KraDetails AtomicInteger generatedCount = new
	 * AtomicInteger(0);
	 * 
	 * List<List<Employee>> employeeList = ListUtils.partition(validEmployees,
	 * getMaxSize(validEmployees.size()));
	 * 
	 * ExecutorService executorService =
	 * Executors.newFixedThreadPool(validEmployees.size() + 5);
	 * 
	 * for (List<Employee> list : employeeList) { executorService.execute(new
	 * Runnable() {
	 * 
	 * @Override public void run() { for (Employee employee : list) {
	 * generatedCount.set(processKPITemplates(loggedInEmployee, kraYear, kpiCycles,
	 * orgMap, employeeKpiTemplateMap, subcategoryMap, categoryMap, kraList,
	 * kraWfList, allKraDetailsList, generatedCount.get(), employee)); } } });
	 * 
	 * }
	 * 
	 * executorService.shutdown();
	 * executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.MINUTES);
	 * 
	 * if (!kraList.isEmpty()) kraDao.saveAll(kraList); if
	 * (!allKraDetailsList.isEmpty()) kraDetailsDao.saveAll(allKraDetailsList); if
	 * (!kraWfList.isEmpty()) kraWfDao.saveAll(kraWfList);
	 * 
	 * // sendEmailToHCDandManager(loggedInEmployee);
	 * 
	 * response.setResponseCode(1200); response.setResponseMessage(String.format(
	 * "KPI Templates generated successfully. Generated KPI For : %d Employees",
	 * generatedCount.get()));
	 * response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200)); }
	 * catch (Exception e) { response.setResponseCode(1500);
	 * log.error("Error generating KPI Templates", e); throw new
	 * HRMSException("Failed to generate KPI Templates", e); }
	 * log.info("Exit from generateKPITemplates method"); return response; }
	 */

	/*
	 * @Override public HRMSBaseResponse<String>
	 * generateKPITemplates(GenerateKPITemplateRequestVo request) throws
	 * HRMSException { log.info("Inside generateKPITemplates method");
	 * HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
	 * 
	 * try { Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
	 * Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId,
	 * ERecordStatus.Y.name()); validateRolePermissionforUnlocked();
	 * 
	 * String year = request.getYear(); KraYear kraYear =
	 * kraYearDao.findByYear(year); if (kraYear == null) {
	 * log.warn("KRA Year not found for year: {}", year);
	 * response.setResponseCode(1201); response.setResponseMessage("No Data Found");
	 * return response; }
	 * 
	 * 
	 * List<KraCycle> kpiCycles; if (request.getCycleIds() != null &&
	 * !request.getCycleIds().isEmpty()) {
	 * 
	 * kpiCycles = kraCycleDAO.findByIdInAndYearAndOrgId( request.getCycleIds(),
	 * kraYear, loggedInEmployee.getOrgId());
	 * 
	 * 
	 * boolean hasCycleType1 = kpiCycles.stream() .anyMatch(c ->
	 * IHRMSConstants.KPI_SUBMISSION_TYPE_ID.equals(c.getCycleTypeId()));
	 * 
	 * if (!hasCycleType1) { KraCycle cycleType1 =
	 * kraCycleDAO.findByIdAndYearAndOrgId( IHRMSConstants.KPI_SUBMISSION_TYPE_ID,
	 * kraYear, loggedInEmployee.getOrgId()); if (cycleType1 != null) {
	 * kpiCycles.add(cycleType1); } } } else { kpiCycles =
	 * kraCycleDAO.findByYearAndOrgId(kraYear, loggedInEmployee.getOrgId()); }
	 * 
	 * List<Employee> employees; if (request.getEmployeeIds() != null &&
	 * !request.getEmployeeIds().isEmpty()) { employees =
	 * employeeDAO.findByIdInAndIsActiveAndIsAllowedInCycle(
	 * request.getEmployeeIds(), ERecordStatus.Y.name(), ERecordStatus.Y.name()); }
	 * else { employees = employeeDAO.findByIsActiveAndIsAllowedInCycle(
	 * ERecordStatus.Y.name(), ERecordStatus.Y.name()); }
	 * 
	 * List<Long> skippedEmployeeIds = new ArrayList<>(); List<Employee>
	 * validEmployees = employees.stream().filter(emp -> { if (emp.getCycleAllowed()
	 * == null || emp.getCycleAllowed().trim().isEmpty()) {
	 * skippedEmployeeIds.add(emp.getId()); return false; } return
	 * ERecordStatus.Y.name().equals(emp.getCycleAllowed());
	 * }).collect(Collectors.toList());
	 * 
	 * if (!skippedEmployeeIds.isEmpty()) {
	 * log.warn("Skipped employees due to null/empty 'isAllowedInCycle': {}",
	 * skippedEmployeeIds); }
	 * 
	 * if (validEmployees.isEmpty() || kpiCycles.isEmpty()) {
	 * log.warn("No valid employees or KPI cycles found for the year: {}", year);
	 * response.setResponseCode(1201); response.setResponseMessage("No Data Found");
	 * return response; }
	 * 
	 * Map<Long, Organization> orgMap =
	 * hrIhrmsOrganizationDAO.findByIsActive(IHRMSConstants.isActive).stream()
	 * .collect(Collectors.toMap(Organization::getId, org -> org));
	 * 
	 * Map<Long, List<ViKpiTemplate>> employeeKpiTemplateMap = templateDAO
	 * .findByEmpIdIn(validEmployees.stream().map(Employee::getId).collect(
	 * Collectors.toList())).stream() .filter(v -> v.getEmpId() != null)
	 * .collect(Collectors.groupingBy(ViKpiTemplate::getEmpId));
	 * 
	 * Map<Long, Subcategory> subcategoryMap = subCategoryDao.findAll().stream()
	 * .collect(Collectors.toMap(Subcategory::getId, s -> s));
	 * 
	 * Map<Long, Category> categoryMap = categoryDAO.findAll().stream()
	 * .collect(Collectors.toMap(Category::getId, s -> s));
	 * 
	 * 
	 * List<Kra> kraList = new ArrayList<>(); List<KraWf> kraWfList = new
	 * ArrayList<>(); List<KraDetails> allKraDetailsList = new ArrayList<>();
	 * AtomicInteger generatedCount = new AtomicInteger(0);
	 * 
	 * List<List<Employee>> employeeList = ListUtils.partition(validEmployees,
	 * getMaxSize(validEmployees.size()));
	 * 
	 * ExecutorService executorService =
	 * Executors.newFixedThreadPool(validEmployees.size() + 5);
	 * 
	 * for (List<Employee> list : employeeList) { executorService.execute(() -> {
	 * for (Employee employee : list) {
	 * generatedCount.set(processKPITemplates(loggedInEmployee, kraYear, kpiCycles,
	 * orgMap, employeeKpiTemplateMap, subcategoryMap, categoryMap, kraList,
	 * kraWfList, allKraDetailsList, generatedCount.get(), employee)); } }); }
	 * 
	 * executorService.shutdown();
	 * executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.MINUTES);
	 * 
	 * 
	 * if (!kraList.isEmpty()) kraDao.saveAll(kraList); if
	 * (!allKraDetailsList.isEmpty()) kraDetailsDao.saveAll(allKraDetailsList); if
	 * (!kraWfList.isEmpty()) kraWfDao.saveAll(kraWfList);
	 * 
	 * response.setResponseCode(1200); response.setResponseMessage(String.format(
	 * "KPI Templates generated successfully. Generated KPI For : %d Employees",
	 * generatedCount.get()));
	 * response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
	 * 
	 * } catch (Exception e) { response.setResponseCode(1500);
	 * log.error("Error generating KPI Templates", e); throw new
	 * HRMSException("Failed to generate KPI Templates", e); }
	 * 
	 * log.info("Exit from generateKPITemplates method"); return response; }
	 */

	@Override
	public HRMSBaseResponse<String> generateKPITemplates(GenerateKPITemplateRequestVo request) throws HRMSException {
		log.info("Inside generateKPITemplates method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		try {
			Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
			validateRolePermissionforUnlocked();

			String year = request.getYear();
			KraYear kraYear = kraYearDao.findByYear(year);
			if (kraYear == null) {
				log.warn("KRA Year not found for year: {}", year);
				response.setResponseCode(1201);
				response.setResponseMessage("No Data Found");
				return response;
			}

			// --- Fetch KPI Cycles ---
			List<KraCycle> kpiCycles;
			if (request.getCycleIds() != null && !request.getCycleIds().isEmpty()) {
				kpiCycles = kraCycleDAO.findByIdInAndYearAndOrgId(request.getCycleIds(), kraYear,
						loggedInEmployee.getOrgId());

				boolean hasCycleType1 = kpiCycles.stream()
						.anyMatch(c -> IHRMSConstants.KPI_SUBMISSION_TYPE_ID.equals(c.getCycleTypeId()));

				if (!hasCycleType1) {
					KraCycle cycleType1 = kraCycleDAO.findByIdAndYearAndOrgId(IHRMSConstants.KPI_SUBMISSION_TYPE_ID,
							kraYear, loggedInEmployee.getOrgId());
					if (cycleType1 != null) {
						kpiCycles.add(cycleType1);
					}
				}
			} else {
				kpiCycles = kraCycleDAO.findByYearAndOrgId(kraYear, loggedInEmployee.getOrgId());
			}

			// --- Fetch Employees ---
			List<Employee> employees = new ArrayList<>();
			if (request.getEmployeeIds() != null && !request.getEmployeeIds().isEmpty()) {
				// Case 1: Generate for specific employees
				employees = employeeDAO.findByIdInAndIsActiveAndIsAllowedInCycle(request.getEmployeeIds(),
						ERecordStatus.Y.name(), ERecordStatus.Y.name());

				List<Long> foundIds = employees.stream().map(Employee::getId).collect(Collectors.toList());
				List<Long> missingIds = request.getEmployeeIds().stream().filter(id -> !foundIds.contains(id))
						.collect(Collectors.toList());
				if (!missingIds.isEmpty()) {
					log.warn("Requested Employee IDs not found or inactive: {}", missingIds);
				}
			} else {
				// Case 2: Generate for all active and allowed employees
				employees = employeeDAO.findByIsActiveAndIsAllowedInCycle(ERecordStatus.Y.name(),
						ERecordStatus.Y.name());
			}

			// --- Validate employees ---
			if (employees == null || employees.isEmpty()) {
				log.warn("No valid employees found for KPI generation for year: {}", year);
				response.setResponseCode(1201);
				response.setResponseMessage("No valid employees found");
				return response;
			}

			List<Long> skippedEmployeeIds = new ArrayList<>();
			List<Employee> validEmployees = employees.stream().filter(emp -> {
				if (emp.getCycleAllowed() == null || emp.getCycleAllowed().trim().isEmpty()) {
					skippedEmployeeIds.add(emp.getId());
					return false;
				}
				return ERecordStatus.Y.name().equals(emp.getCycleAllowed());
			}).collect(Collectors.toList());

			if (!skippedEmployeeIds.isEmpty()) {
				log.warn("Skipped employees due to null/empty 'cycleAllowed': {}", skippedEmployeeIds);
			}

			if (validEmployees.isEmpty() || kpiCycles.isEmpty()) {
				log.warn("No valid employees or KPI cycles found for year: {}", year);
				response.setResponseCode(1201);
				response.setResponseMessage("No Data Found");
				return response;
			}

			// --- Preload required data ---
			Map<Long, Organization> orgMap = hrIhrmsOrganizationDAO.findByIsActive(IHRMSConstants.isActive).stream()
					.collect(Collectors.toMap(Organization::getId, org -> org));

			Map<Long, List<ViKpiTemplate>> employeeKpiTemplateMap = templateDAO
					.findByEmpIdIn(validEmployees.stream().map(Employee::getId).collect(Collectors.toList())).stream()
					.filter(v -> v.getEmpId() != null).collect(Collectors.groupingBy(ViKpiTemplate::getEmpId));

			Map<Long, Subcategory> subcategoryMap = subCategoryDao.findAll().stream()
					.collect(Collectors.toMap(Subcategory::getId, s -> s));

			Map<Long, Category> categoryMap = categoryDAO.findAll().stream()
					.collect(Collectors.toMap(Category::getId, s -> s));

			// --- Initialize Lists ---
			List<Kra> kraList = new ArrayList<>();
			List<KraWf> kraWfList = new ArrayList<>();
			List<KraDetails> allKraDetailsList = new ArrayList<>();
			AtomicInteger generatedCount = new AtomicInteger(0);

			// --- Parallel Processing ---
			List<List<Employee>> employeeBatches = ListUtils.partition(validEmployees,
					getMaxSize(validEmployees.size()));
			ExecutorService executorService = Executors.newFixedThreadPool(Math.min(validEmployees.size() + 5, 20));

			for (List<Employee> batch : employeeBatches) {
				executorService.execute(() -> {
					for (Employee employee : batch) {
						generatedCount.set(processKPITemplates(loggedInEmployee, kraYear, kpiCycles, orgMap,
								employeeKpiTemplateMap, subcategoryMap, categoryMap, kraList, kraWfList,
								allKraDetailsList, generatedCount.get(), employee));
					}
				});
			}

			executorService.shutdown();
			executorService.awaitTermination(10, TimeUnit.MINUTES);

			// --- Save Results ---
			if (!kraList.isEmpty())
				kraDao.saveAll(kraList);
			if (!allKraDetailsList.isEmpty())
				kraDetailsDao.saveAll(allKraDetailsList);
			if (!kraWfList.isEmpty())
				kraWfDao.saveAll(kraWfList);

			response.setResponseCode(1200);
			response.setResponseMessage(
					String.format("KPI Templates generated successfully for %d employees", generatedCount.get()));
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));

		} catch (Exception e) {
			log.error("Error generating KPI Templates", e);
			response.setResponseCode(1500);
			response.setResponseMessage("Failed to generate KPI Templates");
			throw new HRMSException("Failed to generate KPI Templates", e);
		}

		log.info("Exit from generateKPITemplates method");
		return response;
	}

	@Transactional
	public int processKPITemplates(Employee loggedInEmployee, KraYear kraYear, List<KraCycle> kpiCycles,
			Map<Long, Organization> orgMap, Map<Long, List<ViKpiTemplate>> employeeKpiTemplateMap,
			Map<Long, Subcategory> subcategoryMap, Map<Long, Category> categoryMap, List<Kra> kraList,
			List<KraWf> kraWfList, List<KraDetails> allKraDetailsList, int generatedCount, Employee employee) {
		try {
			Organization org = orgMap.get(employee.getOrgId());
			if (org == null) {
				log.warn("Organization not found for Employee ID: {}", employee.getId());
				return generatedCount;
			}

			boolean isKraGenerated = false;

			List<KraDetails> kraDetailsList = new ArrayList<>();

			for (KraCycle cycle : kpiCycles) {
				if (kraDao.existsByEmployeeAndCycleIdAndKraYear(employee, cycle, kraYear)) {
					log.info("Skipping existing KRA for Employee ID: {}, Cycle ID: {}", employee.getId(),
							cycle.getId());
					continue;
				}

				Kra trnKra = populateTrnKra(employee, cycle, org, kraYear, loggedInEmployee);
				trnKra = kraDao.save(trnKra);
				kraList.add(trnKra);

				populateKraDetail(trnKra, kraDetailsList, loggedInEmployee, employeeKpiTemplateMap, subcategoryMap,
						categoryMap);
				populateKraWf(trnKra, cycle, loggedInEmployee, kraWfList);

				isKraGenerated = true;
			}

			allKraDetailsList.addAll(kraDetailsList);

			if (isKraGenerated) {
				generatedCount++;
			}
		} catch (Exception e) {
			log.error("Error generating KPI Templates for Employee ID: {}. Details: {}", employee.getId(),
					e.getMessage(), e);
		}
		return generatedCount;
	}

	public int getMaxSize(int max) {
		if (max >= 500 && max <= 1000) {
			return 200;
		}
		if (max >= 1000 && max <= 2000) {
			return 300;
		}

		if (max >= 3000 && max <= 5000) {
			return 400;
		}

		if (max >= 5000 && max <= 50000) {
			return 1000;
		}

		if (max > 50000) {
			return 2000;
		}

		return max;
	}

	private Kra populateTrnKra(Employee employee, KraCycle cycle, Organization org, KraYear kraYear,
			Employee loggedInEmployee) {
		Kra trnKra = new Kra();
		trnKra.setCreatedBy(String.valueOf(loggedInEmployee.getId()));
		trnKra.setCreatedDate(new Date());
		trnKra.setUpdatedBy(String.valueOf(loggedInEmployee.getId()));
		trnKra.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		trnKra.setBranch(validateDetails(() -> employee.getEmployeeBranch().getBranch()));
		trnKra.setDepartment(validateDetails(() -> employee.getEmployeeDepartment().getDepartment()));
		trnKra.setDivision(validateDetails(() -> employee.getEmployeeDivision().getDivision()));
		trnKra.setCycleId(cycle);
		trnKra.setEmployee(employee);
		trnKra.setIsActive(IHRMSConstants.isActive);
		trnKra.setIsfinalcalibrate(IHRMSConstants.isNotActive);
		trnKra.setIshrcalibrate(IHRMSConstants.isNotActive);
		trnKra.setRmRating(0.0f);
		trnKra.setHrRating(0.0f);
		trnKra.setHodRating(0.0f);
		trnKra.setSelfRating(0.0f);
		trnKra.setKraYear(kraYear);
		trnKra.setOrgId(employee.getOrgId());
		Long candidateId = employee.getCandidate().getId();
		if (candidateId != null) {
			CandidateProfessionalDetail details = candidateprofDAO.findById(candidateId).orElse(null);
			if (details != null && details.getGrade() != null) {
				trnKra.setGrade(details.getGrade());
			}
		}

		trnKra.setDepartmentName(
				(employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getDepartment() != null)
						? employee.getEmployeeDepartment().getDepartment().getDepartmentName()
						: null);

		trnKra.setDesignation((employee.getEmployeeDesignation() != null
				&& employee.getEmployeeDesignation().getDesignation() != null)
						? employee.getEmployeeDesignation().getDesignation().getDesignationName()
						: null);

		trnKra.setReportingManager((employee.getEmployeeReportingManager() != null)
				? employee.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName() + " "
						+ employee.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName()
				: null);

		return trnKra;
	}

	private <T> T validateDetails(Supplier<T> getter) {
		try {
			return Optional.ofNullable(getter).map(Supplier::get).orElse(null);
		} catch (NullPointerException e) {
			return null;
		}
	}

	private void populateKraDetail(Kra trnKra, List<KraDetails> kraDetailsList, Employee loggedInEmployee,
			Map<Long, List<ViKpiTemplate>> employeeKpiTemplateMap, Map<Long, Subcategory> subcategoryMap,
			Map<Long, Category> categoryMap) {

		populateFunctional(trnKra, kraDetailsList, loggedInEmployee, employeeKpiTemplateMap, subcategoryMap);
		populateCoreAndLeadership(trnKra, kraDetailsList, loggedInEmployee, employeeKpiTemplateMap, categoryMap);

	}

	private void populateCoreAndLeadership(Kra trnKra, List<KraDetails> kraDetailsList, Employee loggedInEmployee,
			Map<Long, List<ViKpiTemplate>> employeeKpiTemplateMap, Map<Long, Category> categoryMap) {

		List<ViKpiTemplate> kpiTemplates = employeeKpiTemplateMap.get(trnKra.getEmployee().getId());

		log.info("Populating populateCoreAndLeadership KRA Details for Employee ID: {}", trnKra.getEmployee().getId());

		if (kpiTemplates == null || kpiTemplates.isEmpty()) {
			log.warn("No KPI Template found for Employee ID: {}", trnKra.getEmployee().getId());
			return;
		}

		Map<Long, List<ViKpiTemplate>> categoryWiseObjectives = kpiTemplates.stream()
				.collect(Collectors.groupingBy(ViKpiTemplate::getCategoryId));

		for (Map.Entry<Long, List<ViKpiTemplate>> entry : categoryWiseObjectives.entrySet()) {
			Long categoryId = entry.getKey();
			List<ViKpiTemplate> objectives = entry.getValue();

			float categoryWeightage = Optional.ofNullable(categoryMap.get(categoryId)).map(Category::getWeightage)
					.orElse(0L).floatValue();

			categoryWeightage = (float) Math.round(categoryWeightage * 10000) / 10000f;

			float objectiveWeightage = calculateObjectiveWeightage(categoryWeightage, objectives.size());

			for (ViKpiTemplate temp : objectives) {
				KraDetails kraDetail = new KraDetails();

				kraDetail.setCreatedBy(String.valueOf(loggedInEmployee.getId()));
				kraDetail.setCreatedDate(new Date());
				kraDetail.setUpdatedBy(String.valueOf(loggedInEmployee.getId()));
				kraDetail.setUpdatedDate(new Date());
				kraDetail.setIsActive(IHRMSConstants.isActive);
				kraDetail.setYear(trnKra.getKraYear().getYear());
				kraDetail.setKraDetails(temp.getObjectives());
				kraDetail.setDescription(temp.getObjectives());

				kraDetail.setObjectiveWeightage(objectiveWeightage);
				kraDetail.setWeightage(categoryWeightage);

				kraDetail.setKra(trnKra);
				kraDetail.setOrgId(loggedInEmployee.getOrgId());
				kraDetail.setCategoryId(temp.getCategoryId());
				kraDetail.setSubcategoryId(temp.getSubCategoryId());
				kraDetail.setKraCycle(trnKra.getCycleId());
				kraDetail.setMeasurementCriteria(temp.getMetrics());

				log.info(
						"KRA Detail to be inserted: Employee ID: {}, Year: {}, Org ID: {}, Category ID: {}, Subcategory ID: {}, Weightage: {}, Objective: {}, Created By: {}",
						trnKra.getEmployee().getId(), kraDetail.getYear(), kraDetail.getOrgId(),
						kraDetail.getCategoryId(), kraDetail.getSubcategoryId(), kraDetail.getWeightage(),
						kraDetail.getKraDetails(), kraDetail.getObjectiveWeightage());

				kraDetailsList.add(kraDetail);
			}
		}
	}

	/**
	 * Populates Functional KRA Details.
	 */
	private void populateFunctional(Kra trnKra, List<KraDetails> kraDetailsList, Employee loggedInEmployee,
			Map<Long, List<ViKpiTemplate>> employeeKpiTemplateMap, Map<Long, Subcategory> subcategoryMap) {

		Long employeeId = trnKra.getEmployee().getId();
		List<ViKpiTemplate> kpiTemplates = employeeKpiTemplateMap.get(employeeId);

		log.info("Populating Functional KRA Details for Employee ID: {}", employeeId);

		if (kpiTemplates != null && !kpiTemplates.isEmpty()) {
			for (ViKpiTemplate kpiTemplate : kpiTemplates) {
				Long departmentId = kpiTemplate.getDepartmentId();

				String grade = kpiTemplate.getGrade();

				List<GradeMaster> gradeMasters = gradeDao.findByGradeDescription(grade);

				List<Long> gradeId = gradeMasters.stream().map(g -> g.getId()).collect(Collectors.toList());

				boolean isMappingPresent = gradeToObjectiveMappingDao
						.existsByDepartmentIdAndGradeIdInAndIsActive(departmentId, gradeId, IHRMSConstants.isActive);

				if (isMappingPresent) {

					createGradewiseFunctionalKpi(trnKra, kpiTemplate, kraDetailsList, loggedInEmployee, subcategoryMap);

				} else {

					createGenericFunctionalKpi(trnKra, kpiTemplate, kraDetailsList, loggedInEmployee, subcategoryMap);
				}
			}
		} else {
			log.warn("No KPI Templates found for Employee ID: {}", employeeId);
		}
	}

	private void createGradewiseFunctionalKpi(Kra trnKra, ViKpiTemplate kpiTemplate, List<KraDetails> kraDetailsList,
			Employee loggedInEmployee, Map<Long, Subcategory> subcategoryMap) {

		log.info("Populating Gradewise Functional KPI Details for Employee ID: {}", trnKra.getEmployee().getId());

		Long departmentId = kpiTemplate.getDepartmentId();
		String grade = kpiTemplate.getGrade();

		List<Long> gradeIds = gradeDao.findByGradeDescription(grade).stream().map(GradeMaster::getId)
				.collect(Collectors.toList());

		if (gradeIds.isEmpty()) {
			log.warn("No Grade ID found for Grade: {} for Employee ID: {}", grade, trnKra.getEmployee().getId());
			return;
		}

		List<GradeToObjectiveMapping> gradeMappings = gradeToObjectiveMappingDao
				.findByDepartmentIdAndGradeIdInAndKpiTypeAndIsActive(departmentId, gradeIds,
						kpiTypeDao.findById(2L).orElseThrow(() -> new RuntimeException("KPI Type not found for ID: 2")),
						IHRMSConstants.isActive);

		if (gradeMappings.isEmpty()) {
			log.warn("No Grade-To-Objective mapping found for Department ID: {} and Grade: {} for Employee ID: {}",
					departmentId, grade, trnKra.getEmployee().getId());
			return;
		}

		for (GradeToObjectiveMapping mapping : gradeMappings) {
			log.info("Processing Grade-To-Objective Mapping ID: {} for Employee ID: {}", mapping.getId(),
					trnKra.getEmployee().getId());

			boolean isDuplicate = kraDetailsList.stream()
					.anyMatch(kraDetail -> kraDetail.getKraDetails().equals(mapping.getObjective())
							&& kraDetail.getCategoryId().equals(mapping.getCategory().getId())
							&& kraDetail.getSubcategoryId()
									.equals(subCategoryDao.findByCategoryId(mapping.getCategory().getId()).getId())
							&& kraDetail.getKraCycle().equals(trnKra.getCycleId()));

			if (isDuplicate) {
				log.info("Duplicate entry found for Objective: {}, Category ID: {}, Cycle ID: {}. Skipping...",
						mapping.getObjective(), mapping.getCategory().getId(), trnKra.getCycleId());
				continue;
			}

			KraDetails kraDetail = new KraDetails();
			kraDetail.setCreatedBy(String.valueOf(loggedInEmployee.getId()));
			kraDetail.setCreatedDate(new Date());
			kraDetail.setUpdatedBy(String.valueOf(loggedInEmployee.getId()));
			kraDetail.setUpdatedDate(new Date());
			kraDetail.setIsActive(IHRMSConstants.isActive);
			kraDetail.setYear(trnKra.getKraYear().getYear());
			kraDetail.setKraDetails(mapping.getObjective());
			kraDetail.setDescription(mapping.getObjective());
			kraDetail.setKra(trnKra);
			kraDetail.setOrgId(loggedInEmployee.getOrgId());
			kraDetail.setKraCycle(trnKra.getCycleId());
			KpiMetricMaster measurement = kpiMetricDao.findById(mapping.getMetricType().getId()).orElseThrow(
					() -> new RuntimeException("KpiMetricMaster not found for ID: " + mapping.getMetricType().getId()));
			kraDetail.setMeasurementCriteria(measurement.getMetric());

			Subcategory subcat = subCategoryDao.findByCategoryId(mapping.getCategory().getId());
			if (subcat != null) {
				kraDetail.setSubcategoryId(subcat.getId());
				kraDetail.setCategoryId(mapping.getCategory().getId());
				kraDetail.setWeightage((float) mapping.getCategory().getWeightage());
				kraDetail.setObjectiveWeightage(mapping.getMetricWeight().floatValue());
			}

			kraDetailsList.add(kraDetail);

			log.info("Added Functional Gradewise KPI Detail for Employee ID: {}, Department: {}, Grade: {}",
					trnKra.getEmployee().getId(), departmentId, grade);
		}
	}

	private void createGenericFunctionalKpi(Kra trnKra, ViKpiTemplate kpiTemplate, List<KraDetails> kraDetailsList,
			Employee loggedInEmployee, Map<Long, Subcategory> subcategoryMap) {

		log.info("Populating Generic Functional Kpi Details for Employee ");

		List<GradeToObjectiveMapping> gradeMappings = gradeToObjectiveMappingDao.findByKpiTypeAndIsActive(
				kpiTypeDao.findById(1L).orElseThrow(() -> new RuntimeException("KPI Type not found for ID: 1")),
				IHRMSConstants.isActive);

		if (gradeMappings.isEmpty())
			return;

		int totalRecords = gradeMappings.size();
		for (GradeToObjectiveMapping mapping : gradeMappings) {
			log.info("Processing Grade-To-Objective Mapping ID: {}", mapping.getId());

			Subcategory subcat = subCategoryDao.findByCategoryId(mapping.getCategory().getId());

			boolean alreadyExists = kraDetailsList.stream().anyMatch(kraDetail -> kraDetail.getKra().equals(trnKra)
					&& kraDetail.getKraDetails().equals(mapping.getObjective()));

			if (alreadyExists)
				continue;

			KraDetails kraDetail = new KraDetails();
			kraDetail.setCreatedBy(String.valueOf(loggedInEmployee.getId()));
			kraDetail.setCreatedDate(new Date());
			kraDetail.setUpdatedBy(String.valueOf(loggedInEmployee.getId()));
			kraDetail.setUpdatedDate(new Date());
			kraDetail.setIsActive(IHRMSConstants.isActive);
			kraDetail.setYear(trnKra.getKraYear().getYear());
			kraDetail.setKraDetails(mapping.getObjective());
			kraDetail.setDescription(mapping.getObjective());
			kraDetail.setWeightage((float) mapping.getCategory().getWeightage());
			kraDetail.setSubcategoryId(subcat.getId());
			kraDetail.setCategoryId(mapping.getCategory().getId());
			kraDetail.setKra(trnKra);
			kraDetail.setOrgId(loggedInEmployee.getOrgId());
			kraDetail.setKraCycle(trnKra.getCycleId());
			kraDetail.setMeasurementCriteria("Pass/Fail");
			kraDetail.setObjectiveWeightage(mapping.getMetricWeight().floatValue());

			kraDetailsList.add(kraDetail);
		}
	}

	/**
	 * Calculates the weightage for each objective within a category.
	 *
	 * @param categoryWeightage The total weightage of the category.
	 * @param totalObjectives   The number of objectives in the category.
	 * @return The weightage for each objective.
	 */
	private float calculateObjectiveWeightage(float categoryWeightage, int totalObjectives) {
		if (totalObjectives == 0) {
			return 0.0f;
		}
		return categoryWeightage / totalObjectives;
	}

	private void populateKraWf(Kra trnKra, KraCycle cycle, Employee loggedInEmployee, List<KraWf> kraWfList) {
		if (kraWfDao.existsByKraAndCycleId(trnKra, cycle)) {
			log.info("Skipping existing KraWf for Employee ID: {}, Kra ID: {}", trnKra.getEmployee().getId(),
					trnKra.getId());
			return;
		}

		log.info("Creating workflow for Employee ID: {}, Kra ID: {}", trnKra.getEmployee().getId(), trnKra.getId());

		KraWf wf = new KraWf();
		wf.setCreatedBy(String.valueOf(loggedInEmployee.getId()));
		wf.setIsActive(IHRMSConstants.isActive);
		wf.setCreatedDate(new Date());
		wf.setKra(trnKra);
		wf.setOrgId(loggedInEmployee.getOrgId());
		wf.setCycleId(cycle);

		String cycleName = cycle.getCycleName();
		Long cycleType = cycle.getCycleTypeId();

		if (IHRMSConstants.KPI_SUBMISSION_TYPE_ID.equals(cycleType)) {
			wf.setStatus(IHRMSConstants.INPROGRESS);
			wf.setPendingWith(IHRMSConstants.MANAGER);
		} else if (IHRMSConstants.HALF_YEAR_TYPE_ID.equals(cycleType)
				|| IHRMSConstants.YEAR_END_TYPE_ID.equals(cycleType)) {
			wf.setStatus(IHRMSConstants.INCOMPLETE);
			wf.setPendingWith(IHRMSConstants.EMPLOYEE);
		}
		kraWfList.add(wf);
	}

	private void sendEmailToHCDandManager(Employee loggedInEmployee) throws Exception {
		log.info("Sending Email to HR and Line manager After generating KPI");

		try {
			EmailRequestVO emailToHCD = createEmailRequest(loggedInEmployee, IHRMSConstants.GENERATE_KPI_HR);
			emailToHCD.setEmailAddress(loggedInEmployee.getOfficialEmailId());
			emailService.insertInEmailTxnTable(emailToHCD);
		} catch (Exception e) {
			log.error("Failed to send email to HR for employee: " + loggedInEmployee.getOfficialEmailId(),
					loggedInEmployee.getId(), e);
			throw new Exception("Failed to send email to HR:  " + loggedInEmployee.getOfficialEmailId(), e);
		}

		try {
			List<Long> reportingManagerIds = reportingManagerDao.getAllReportingManagerIds(IHRMSConstants.isActive);
			List<Employee> lineManagers = employeeDAO.findAllById(reportingManagerIds);

			for (Employee lineManager : lineManagers) {
				try {
					EmailRequestVO emailToLineManager = createEmailRequest(loggedInEmployee,
							IHRMSConstants.GENERATE_KPI_MANAGER);
					emailToLineManager.setEmailAddress(lineManager.getOfficialEmailId());
					emailService.insertInEmailTxnTable(emailToLineManager);
				} catch (Exception e) {
					log.error("Failed to send email to line manager: " + lineManager.getOfficialEmailId()
							+ " for employee: " + lineManager.getId(), e);
				}
			}
		} catch (Exception e) {
			log.error("Failed to retrieve or process line managers for employee: " + loggedInEmployee.getId(), e);
			throw new Exception("Failed to retrieve or process line managers for employee: " + loggedInEmployee.getId(),
					e);
		}
	}

	private EmailRequestVO createEmailRequest(Employee loggedInEmployee, String templateName) throws HRMSException {

		if (loggedInEmployee == null) {
			throw new IllegalArgumentException("LoggedInEmployee cannot be null");
		}
		if (loggedInEmployee.getCandidate() == null) {
			throw new HRMSException("Candidate details are missing for the logged-in employee");
		}
		if (templateName == null || templateName.isEmpty()) {
			throw new IllegalArgumentException("Template name cannot be null or empty");
		}
		log.debug("Using template name: {}", templateName);

		EmailRequestVO email = new EmailRequestVO();
		email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

		TemplateVO template = new TemplateVO();
		template.setTemplateName(templateName);

		List<PlaceHolderVO> placeHolders = new ArrayList<>();
		PlaceHolderVO placeHolderVo = createPlaceHolder(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY,
				loggedInEmployee.getCandidate().getFirstName() + " " + loggedInEmployee.getCandidate().getLastName());
		placeHolders.add(placeHolderVo);

		template.setPlaceHolders(placeHolders);
		email.setTemplateVo(template);

		return email;
	}

	private PlaceHolderVO createPlaceHolder(String variableName, String variableValue) {
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName(variableName);
		placeHolderVo.setExpressionVariableValue(variableValue);
		return placeHolderVo;
	}

	@Override
	public HRMSBaseResponse<List<OrgKpiResponseVo>> getOrgKpiList(Long deptid, Long grade, Long yearId, Long kpiTypeId,
			Pageable pageable) throws HRMSException {
		HRMSBaseResponse<List<OrgKpiResponseVo>> response = new HRMSBaseResponse<>();
		log.info("get getCycleList Method");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		List<OrgKpiResponseVo> orgKpiResponseList = new ArrayList<>();
		List<GradeToObjectiveMapping> objectiveMappinglist = null;
		long totalRecord = 0;
		if (!HRMSHelper.isNullOrEmpty(yearId) && !HRMSHelper.isNullOrEmpty(deptid)
				&& !HRMSHelper.isNullOrEmpty(grade)) {
			KraYear year = krayeardao.getById(yearId);
			MasterDepartment department = masterDepartmentDao.getById(deptid);
			GradeMaster grades = gradeDao.getById(grade);

			if (!HRMSHelper.isNullOrEmpty(kpiTypeId)) {
				KpiTypeMaster typemaster = kpiTypeDao.getById(kpiTypeId);
				objectiveMappinglist = gradeToObjectiveMappingDao
						.findByYearAndMasterDepartmentAndGradeMasterAndKpiTypeMasterAndIsActive(year, department,
								grades, typemaster, ERecordStatus.Y.name(), pageable);
				totalRecord = gradeToObjectiveMappingDao
						.countByYearAndMasterDepartmentAndGradeMasterAndKpiTypeMasterAndIsActive(year, department,
								grades, typemaster, ERecordStatus.Y.name());
			} else {

				objectiveMappinglist = gradeToObjectiveMappingDao
						.findByYearAndMasterDepartmentAndGradeMasterAndIsActive(year, department, grades,
								ERecordStatus.Y.name(), pageable);
				totalRecord = gradeToObjectiveMappingDao.countByYearAndMasterDepartmentAndGradeMasterAndIsActive(year,
						department, grades, ERecordStatus.Y.name());
			}
		} else if (!HRMSHelper.isNullOrEmpty(yearId) && !HRMSHelper.isNullOrEmpty(deptid)
				&& HRMSHelper.isNullOrEmpty(grade)) {
			KraYear year = krayeardao.getById(yearId);
			MasterDepartment department = masterDepartmentDao.getById(deptid);

			if (!HRMSHelper.isNullOrEmpty(kpiTypeId)) {
				KpiTypeMaster typemaster = kpiTypeDao.getById(kpiTypeId);

				objectiveMappinglist = gradeToObjectiveMappingDao
						.findByYearAndMasterDepartmentAndKpiTypeMasterAndIsActive(year, department, typemaster,
								ERecordStatus.Y.name(), pageable);
				totalRecord = gradeToObjectiveMappingDao.countByYearAndMasterDepartmentAndKpiTypeMasterAndIsActive(year,
						department, typemaster, ERecordStatus.Y.name());
			}

			else {
				objectiveMappinglist = gradeToObjectiveMappingDao.findByYearAndMasterDepartmentAndIsActive(year,
						department, ERecordStatus.Y.name(), pageable);
				totalRecord = gradeToObjectiveMappingDao.countByYearAndMasterDepartmentAndIsActive(year, department,
						ERecordStatus.Y.name());
			}

		} else if (!HRMSHelper.isNullOrEmpty(yearId) && !HRMSHelper.isNullOrEmpty(grade)
				&& HRMSHelper.isNullOrEmpty(deptid)) {
			KraYear year = krayeardao.getById(yearId);
			GradeMaster grades = gradeDao.getById(grade);
			if (!HRMSHelper.isNullOrEmpty(kpiTypeId)) {
				KpiTypeMaster typemaster = kpiTypeDao.getById(kpiTypeId);
				objectiveMappinglist = gradeToObjectiveMappingDao.findByYearAndGradeMasterAndKpiTypeMasterAndIsActive(
						year, grades, kpiTypeId, ERecordStatus.Y.name(), pageable);
				totalRecord = gradeToObjectiveMappingDao.countByYearAndGradeMasterAndKpiTypeMasterAndIsActive(year,
						grades, kpiTypeId, ERecordStatus.Y.name());

			} else {
				objectiveMappinglist = gradeToObjectiveMappingDao.findByYearAndGradeMasterAndIsActive(year, grades,
						ERecordStatus.Y.name(), pageable);
				totalRecord = gradeToObjectiveMappingDao.countByYearAndGradeMasterAndIsActive(year, grades,
						ERecordStatus.Y.name());
			}
		} else if (!HRMSHelper.isNullOrEmpty(deptid) && !HRMSHelper.isNullOrEmpty(grade)
				&& HRMSHelper.isNullOrEmpty(yearId)) {
			MasterDepartment department = masterDepartmentDao.getById(deptid);
			GradeMaster grades = gradeDao.getById(grade);
			if (!HRMSHelper.isNullOrEmpty(kpiTypeId)) {
				KpiTypeMaster typemaster = kpiTypeDao.getById(kpiTypeId);
				objectiveMappinglist = gradeToObjectiveMappingDao
						.findByMasterDepartmentAndGradeMasterAndKpiTypeMasterAndIsActive(department, grades, typemaster,
								ERecordStatus.Y.name(), pageable);
				totalRecord = gradeToObjectiveMappingDao
						.countByMasterDepartmentAndGradeMasterAndKpiTypeMasterAndIsActive(department, grades,
								typemaster, ERecordStatus.Y.name());
			} else {
				objectiveMappinglist = gradeToObjectiveMappingDao.findByMasterDepartmentAndGradeMasterAndIsActive(
						department, grades, ERecordStatus.Y.name(), pageable);
				totalRecord = gradeToObjectiveMappingDao.countByMasterDepartmentAndGradeMasterAndIsActive(department,
						grades, ERecordStatus.Y.name());
			}
		} else if (HRMSHelper.isNullOrEmpty(deptid) && HRMSHelper.isNullOrEmpty(grade)
				&& !HRMSHelper.isNullOrEmpty(yearId) && !HRMSHelper.isNullOrEmpty(kpiTypeId)) {
			KraYear year = krayeardao.getById(yearId);
			KpiTypeMaster typemaster = kpiTypeDao.getById(kpiTypeId);
			objectiveMappinglist = gradeToObjectiveMappingDao.findByYearAndKpiTypeMasterAndIsActive(year, typemaster,
					ERecordStatus.Y.name(), pageable);
			totalRecord = gradeToObjectiveMappingDao.countByYearAndKpiTypeMasterAndIsActive(year, typemaster,
					ERecordStatus.Y.name());

		} else if (HRMSHelper.isNullOrEmpty(yearId) && HRMSHelper.isNullOrEmpty(deptid)
				&& HRMSHelper.isNullOrEmpty(grade) && HRMSHelper.isNullOrEmpty(kpiTypeId)) {
			objectiveMappinglist = gradeToObjectiveMappingDao.findByIsActiveOrderByIdDesc(ERecordStatus.Y.name(),
					pageable);
			totalRecord = gradeToObjectiveMappingDao.countByIsActive(ERecordStatus.Y.name());
		} else if (!HRMSHelper.isNullOrEmpty(deptid) && HRMSHelper.isNullOrEmpty(yearId)
				&& HRMSHelper.isNullOrEmpty(grade) && HRMSHelper.isNullOrEmpty(kpiTypeId)) {
			MasterDepartment department = masterDepartmentDao.getById(deptid);

			objectiveMappinglist = gradeToObjectiveMappingDao.findByMasterDepartmentAndIsActive(department,
					ERecordStatus.Y.name(), pageable);
			totalRecord = gradeToObjectiveMappingDao.countByMasterDepartmentAndIsActive(department,
					ERecordStatus.Y.name());
		} else if (!HRMSHelper.isNullOrEmpty(yearId) && HRMSHelper.isNullOrEmpty(deptid)
				&& HRMSHelper.isNullOrEmpty(grade) && HRMSHelper.isNullOrEmpty(kpiTypeId)) {
			KraYear year = krayeardao.getById(yearId);

			objectiveMappinglist = gradeToObjectiveMappingDao.findByYearAndIsActive(year, ERecordStatus.Y.name(),
					pageable);
			totalRecord = gradeToObjectiveMappingDao.countByYearAndIsActive(year, ERecordStatus.Y.name());
		} else if (!HRMSHelper.isNullOrEmpty(grade) && HRMSHelper.isNullOrEmpty(deptid)
				&& HRMSHelper.isNullOrEmpty(yearId) && HRMSHelper.isNullOrEmpty(kpiTypeId)) {
			GradeMaster grades = gradeDao.getById(grade);

			objectiveMappinglist = gradeToObjectiveMappingDao.findByGradeMasterAndIsActive(grades,
					ERecordStatus.Y.name(), pageable);
			totalRecord = gradeToObjectiveMappingDao.countByGradeMasterAndIsActive(grades, ERecordStatus.Y.name());
		} else if (!HRMSHelper.isNullOrEmpty(kpiTypeId) && HRMSHelper.isNullOrEmpty(grade)
				&& HRMSHelper.isNullOrEmpty(deptid) && HRMSHelper.isNullOrEmpty(yearId)) {
			KpiTypeMaster typemaster = kpiTypeDao.getById(kpiTypeId);
			objectiveMappinglist = gradeToObjectiveMappingDao.findByKpiTypeMasterAndIsActive(typemaster,
					ERecordStatus.Y.name(), pageable);
			totalRecord = gradeToObjectiveMappingDao.countByKpiTypeMasterAndIsActive(typemaster,
					ERecordStatus.Y.name());
		}
		if (HRMSHelper.isNullOrEmpty(objectiveMappinglist)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		orgKpiResponseList = objectiveMappinglist.stream().map(obj -> {
			OrgKpiResponseVo kpiResponse = new OrgKpiResponseVo();
			kpiResponse.setId(obj.getId());

			kpiResponse.setDeptId(
					(obj != null && obj.getDepartment() != null) ? String.valueOf(obj.getDepartment().getId()) : null);
			kpiResponse.setDepartmentName(
					(obj != null && obj.getDepartment() != null) ? obj.getDepartment().getDepartmentName() : "Generic");
			kpiResponse.setGradeId(
					(obj != null && obj.getGrade() != null) ? String.valueOf(obj.getGrade().getId()) : null);
			kpiResponse.setGradeDescription(
					(obj != null && obj.getGrade() != null) ? obj.getGrade().getGradeDescription() : "Generic");
			kpiResponse.setKpiTypeId(obj.getKpiType().getId());
			kpiResponse.setKpiType(obj.getKpiType().getKpiType());
			kpiResponse.setMetricId(obj.getMetricType().getId());
			kpiResponse.setMetric(obj.getMetricType().getMetric());
			kpiResponse.setObjective(obj.getObjective());
			kpiResponse.setObjectiveWeight(obj.getMetricWeight());
			kpiResponse.setYearId(obj.getYear().getId());
			kpiResponse.setYear(obj.getYear().getYear());

			return kpiResponse;
		}).collect(Collectors.toList());
		response.setResponseBody(orgKpiResponseList);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setTotalRecord(totalRecord);
		log.info("Exit getOrgKpiList Method");
		return response;

	}

	@Override
	public HRMSBaseResponse<String> updateOrgKpi(OrgnizationalKpiVo request) throws HRMSException {
		log.info("Inside Update Orgnizational kpi method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		validateRolePermission();
		auditLogService.setActionHeader(IHRMSConstants.UPDATE_ORGKPI, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());
		kraHelper.updateOrgKpiValidations(request);
		kraHelper.addOrgKpiValidations(request);
		updateGreadToKpiEntity(request, loggedInEmpId, loggedInEmployee);

		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1773));
		log.info("Exit from Update Orgnizational KPI method");
		return response;

	}

	private void updateGreadToKpiEntity(OrgnizationalKpiVo request, Long loggedInEmpId, Employee loggedInEmployee)
			throws HRMSException {

		String isEdit = IHRMSConstants.isActive;
		GradeToObjectiveMapping objectiveMapping = gradeObjectiveMapDao.findById(request.getId())
				.orElseThrow(() -> new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201)));
		if (!HRMSHelper.isNullOrEmpty(objectiveMapping)) {
			validateObjectiveWeightAndSetOrgKpi(request, loggedInEmpId, loggedInEmployee, objectiveMapping, isEdit);

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	@Transactional
	@Override
	public HRMSBaseResponse<String> updateObejectives(PMSObjectiveVo request) throws HRMSException {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		validateManagerRole();
		validateRequest(request);
		Kra kpi = kraDao.findByIdAndIsActive(request.getKraId(), IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kpi)) {
			Employee empKpi = kpi.getEmployee();
			auditLogService.setActionHeader(IHRMSConstants.SAVE_OBJECTIVES,
					loggedInEmployee.getCandidate().getEmployee(), empKpi.getCandidate().getEmployee());
		}
		KraCycle cycle = validateKraCycle(request);
		Kra kra = validateKra(request);

		Category category = categoryDAO.findByCategoryNameAndIsActive(IHRMSConstants.FUNCTION_SPECIFIC_CATEGORY,
				ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(category)) {
			throw new HRMSException(1638, ResponseCode.getResponseCodeMap().get(1638));
		}

		Subcategory subCatVO = subCategoryDao.findByCategoryAndIsActive(category, ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(subCatVO)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		List<KraDetails> kraDetailsList = kraDetailsDao
				.findByIsActiveAndSubcategoryIdAndKraCycleAndKra(ERecordStatus.Y.name(), subCatVO.getId(), cycle, kra);

		Float categoryWeight = 0.0f;
		Float objectiveWeight = 0.0f;
		categoryWeight = Float.valueOf(category.getWeightage());

		if (!HRMSHelper.isNullOrEmpty(kraDetailsList)) {
			for (KraDetails details : kraDetailsList) {
				if (!details.getId().equals(request.getId())) {
					objectiveWeight += details.getObjectiveWeightage();
				}
			}
		}
		if (objectiveWeight < categoryWeight) {
			float objWeight = categoryWeight - objectiveWeight;
			float remaningObjWeight = objWeight;
			double roundedObjWeight = Math.round(remaningObjWeight * 100.0) / 100.0;
			validateObjectiveWeightage(request);
			Double objectiveWeightage = Double.parseDouble(request.getObjectiveWeight());
			if (roundedObjWeight >= objectiveWeightage) {
				KraDetails details = kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), request.getId());

				details.setCategoryId(category.getId());
				details.setSubcategoryId(subCatVO.getId());
				details.setDescription(request.getName());
				details.setUpdatedBy(String.valueOf(loggedInEmpId));
				details.setUpdatedDate(new Date());
				details.setOrgId(loggedInEmployee.getOrgId());
				details.setIsActive(ERecordStatus.Y.name());
				details.setMeasurementCriteria(request.getMetric());
				details.setKraDetails(request.getName());
				float floatValue = (float) category.getWeightage();
				details.setWeightage(floatValue);
				details.setObjectiveWeightage(objectiveWeightage.floatValue());
				kraDetailsDao.save(details);
			} else {
				throw new HRMSException(1644, ResponseCode.getResponseCodeMap().get(1644));
			}
		} else {
			throw new HRMSException(1644, ResponseCode.getResponseCodeMap().get(1644));
		}
		response.setResponseCode(1200);
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1770));
		log.info("Exit from update Obejectives method");
		return response;
	}

	private Kra validateKra(PMSObjectiveVo request) throws HRMSException {
		Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(kra)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		return kra;
	}

	private KraCycle validateKraCycle(PMSObjectiveVo request) throws HRMSException {
		KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getKraCycleId(), ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(cycle)) {
			throw new HRMSException(1221, ResponseCode.getResponseCodeMap().get(1221));
		}
		return cycle;
	}

	private void validateRequest(PMSObjectiveVo request) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(request)) {
			if (HRMSHelper.isNullOrEmpty(request.getName()) || HRMSHelper.isNullOrEmpty(request.getObjectiveWeight())
					|| HRMSHelper.isNullOrEmpty(request.getMetric())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	private void validateObjectiveWeightage(PMSObjectiveVo objVo) throws HRMSException {
//		if (!objVo.getObjectiveWeight().matches("^\\d+$"))
		if (!objVo.getObjectiveWeight().matches("^\\d{1,2}\\.\\d{2}$")) {
			throw new HRMSException(1761, ResponseCode.getResponseCodeMap().get(1761));
		}
		float objectiveWeightage = Float.parseFloat(objVo.getObjectiveWeight());
		if (objectiveWeightage <= 0.0f) {
			throw new HRMSException(1645, ResponseCode.getResponseCodeMap().get(1645));
		}
		MasterConfig minvalue = masterConfigService.getConfig(ERecordStatus.MIN_VALUE.name());
		MasterConfig maxvalue = masterConfigService.getConfig(ERecordStatus.MAX_VALUE.name());

		float minweight = Float.parseFloat(minvalue.getValue());
		float maxweight = Float.parseFloat(maxvalue.getValue());

		if (objectiveWeightage < minweight || objectiveWeightage > maxweight) {
			throw new HRMSException(1756, ResponseCode.getResponseCodeMap().get(1756));
		}

	}

	private void validateObjectiveWeightAndSetOrgKpi(OrgnizationalKpiVo request, Long loggedInEmpId,
			Employee loggedInEmployee, GradeToObjectiveMapping objectiveMapping, String isEdit) throws HRMSException {

		KraYear year = krayeardao.findById(request.getYear().getId())
				.orElseThrow(() -> new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201)));
		KpiMetricMaster metric = metricDao.findById(request.getMetric().getId())
				.orElseThrow(() -> new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201)));
		KpiTypeMaster kpiType = kpiTypeDao.findById(request.getKpiType().getId())
				.orElseThrow(() -> new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201)));
		Category category = categoryDAO.findByCategoryNameAndIsActive(IHRMSConstants.FUNCTION_SPECIFIC_CATEGORY,
				IHRMSConstants.isActive);
		List<String> errorMessages = new ArrayList<>();
		List<String> successMessages = new ArrayList<>();
		List<GradeToObjectiveMapping> objectiveList = null;
		double objectiveWeight = 0.0;
		double categoryWeight = category.getWeightage();

		if (request.getKpiType().getKpiType().equalsIgnoreCase(IHRMSConstants.NON_GENERIC_KPI)) {

			setOrEditOrgKpiForNonGeneric(request, loggedInEmpId, loggedInEmployee, objectiveMapping, isEdit, year,
					metric, kpiType, category, errorMessages, successMessages, categoryWeight);
		} else {
			setOrEditOrgKpiForGeneric(request, loggedInEmpId, loggedInEmployee, objectiveMapping, year, metric, kpiType,
					category, objectiveWeight, categoryWeight, isEdit);
		}
	}

	private void setOrEditOrgKpiForGeneric(OrgnizationalKpiVo request, Long loggedInEmpId, Employee loggedInEmployee,
			GradeToObjectiveMapping objectiveMapping, KraYear year, KpiMetricMaster metric, KpiTypeMaster kpiType,
			Category category, double objectiveWeight, double categoryWeight, String isEdit) throws HRMSException {
		List<GradeToObjectiveMapping> objectiveList;
		objectiveList = gradeToObjectiveMappingDao.findByKpiTypeMasterAndIsActiveAndYear(request.getKpiType().getId(),
				IHRMSConstants.isActive, year);

		if (!HRMSHelper.isNullOrEmpty(objectiveList)) {
			for (GradeToObjectiveMapping details : objectiveList) {
				objectiveWeight += details.getMetricWeight();
			}
		}
		if (objectiveWeight < categoryWeight) {
			validateAndsetOrgKpis(request, loggedInEmpId, loggedInEmployee, year, metric, kpiType, category,
					objectiveWeight, categoryWeight, objectiveMapping, null, isEdit, null);
		} else {
			throw new HRMSException(1644, ResponseCode.getResponseCodeMap().get(1644));
		}
	}

	private void setOrEditOrgKpiForNonGeneric(OrgnizationalKpiVo request, Long loggedInEmpId, Employee loggedInEmployee,
			GradeToObjectiveMapping objectiveMapping, String isEdit, KraYear year, KpiMetricMaster metric,
			KpiTypeMaster kpiType, Category category, List<String> errorMessages, List<String> successMessages,
			double categoryWeight) throws HRMSException {
		List<GradeToObjectiveMapping> objectiveList;

		for (GradeMasterVo gradevo : request.getGrades()) {
			List<Long> deptList = setDeptList(request);

			for (long id : deptList) {

				MasterDepartment department = masterDepartmentDao.findById(id)
						.orElseThrow(() -> new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201)));
				try {
					double objectWeight = 0.0;
					GradeToObjectiveMapping objMapping = null;
					GradeMaster grade = gradeDao.findById(gradevo.getId())
							.orElseThrow(() -> new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201)));
					if (isEdit.equalsIgnoreCase(IHRMSConstants.isActive)) {
						objMapping = objectiveMapping;
					} else {
						objMapping = new GradeToObjectiveMapping();
						objMapping.setDepartment(department);
						objMapping.setGrade(grade);
					}
					objectiveList = gradeToObjectiveMappingDao.findByYearAndMasterDepartmentAndGradeMasterAndIsActive(
							year, department, grade, IHRMSConstants.isActive, null);
					if (!HRMSHelper.isNullOrEmpty(objectiveList)) {
						for (GradeToObjectiveMapping details : objectiveList) {
							objectWeight += details.getMetricWeight();
						}
					}
					if (objectWeight < categoryWeight) {
						validateAndsetOrgKpis(request, loggedInEmpId, loggedInEmployee, year, metric, kpiType, category,
								objectWeight, categoryWeight, objMapping, grade, isEdit, department);
						successMessages.add(ResponseCode.getResponseCodeMap().get(1200) + IHRMSConstants.FOR_GRADE
								+ grade.getGradeDescription() + " " + department.getDepartmentName());
					} else {
						throw new HRMSException(1782, ResponseCode.getResponseCodeMap().get(1782) + " "
								+ grade.getGradeDescription() + " " + department.getDepartmentName());
					}
				} catch (HRMSException e) {
					if (e.getMessage().contains(ResponseCode.getResponseCodeMap().get(1782))) {
						errorMessages.add(e.getMessage());
					} else {
						throw e;
					}
				}
			}
		}

		String finalMessage = (errorMessages.isEmpty() ? String.join(", ", ResponseCode.getResponseCodeMap().get(1200))
				: "") + (!errorMessages.isEmpty() ? String.join(", ", errorMessages) : "");

		if (!errorMessages.isEmpty()) {
			throw new HRMSException(finalMessage);
		}
	}

/////////////////////

	private void validateAndsetOrgKpis(OrgnizationalKpiVo request, Long loggedInEmpId, Employee loggedInEmployee,
			KraYear year, KpiMetricMaster metric, KpiTypeMaster kpiType, Category category, double objectiveWeight,
			double categoryWeight, GradeToObjectiveMapping objectiveMapping, GradeMaster grade, String isEdit,
			MasterDepartment department) throws HRMSException {
		double objWeight = categoryWeight - objectiveWeight;
		double remainingObjWeight = objWeight;
		validateOrgObjectiveWeightage(request, category);
		double objectiveWeightage = Double.parseDouble(request.getObjectiveWeight().toString());

		if (remainingObjWeight >= objectiveWeightage) {
			if (isEdit.equalsIgnoreCase(IHRMSConstants.isNotActive)) {
				objectiveMapping.setCreatedBy(loggedInEmpId.toString());
				objectiveMapping.setCreatedDate(new Date());
				objectiveMapping.setKpiType(kpiType);
				objectiveMapping.setCategory(category);
				objectiveMapping.setYear(year);
				objectiveMapping.setOrgId(loggedInEmployee.getOrgId());
				objectiveMapping.setIsActive(IHRMSConstants.isActive);
			}
			objectiveMapping.setMetricType(metric);
			objectiveMapping.setMetricWeight(request.getObjectiveWeight());
			objectiveMapping.setObjective(request.getObjective());

			gradeToObjectiveMappingDao.save(objectiveMapping);
		} else {
			throw new HRMSException(1782, ResponseCode.getResponseCodeMap().get(1782)
					+ (grade != null ? " " + grade.getGradeDescription() + " " + department.getDepartmentName() : ""));
		}
	}

	private void validateOrgObjectiveWeightage(OrgnizationalKpiVo objVo, Category category) throws HRMSException {
		double objectiveWeightage = objVo.getObjectiveWeight();

		if (objectiveWeightage <= 0.0f) {
			throw new HRMSException(1645, ResponseCode.getResponseCodeMap().get(1645));
		}
		MasterConfig minvalue = masterConfigDAO.findByKeyAndCategoryId(ERecordStatus.MIN.name(), category.getId());
		MasterConfig maxvalue = masterConfigDAO.findByKeyAndCategoryId(ERecordStatus.MAX.name(), category.getId());

		double minweight = Double.parseDouble(minvalue.getValue());
		double maxweight = Double.parseDouble(maxvalue.getValue());

		if (objectiveWeightage < minweight || objectiveWeightage > maxweight) {
			throw new HRMSException(1756, ResponseCode.getResponseCodeMap().get(1756));
		}
	}

	@Override
	public HRMSBaseResponse<String> acceptKpibyHcd(RejectRequestVo request) throws Exception {
		log.info("Inside Accept KPI method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		Kra kpi = kraDao.findByIdAndIsActive(request.getKraId(), IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kpi)) {
			Employee empKpi = kpi.getEmployee();
			auditLogService.setActionHeader(IHRMSConstants.ACCEPT_KPI_BY_HCD,
					loggedInEmployee.getCandidate().getEmployee(), empKpi.getCandidate().getEmployee());
		}
		validateMcMember(request, role, loggedInEmpId);
		if (!HRMSHelper.isNullOrEmpty(request.getKraId()) && !HRMSHelper.isNullOrEmpty(request.getCycleId())) {

			if (HRMSHelper.isRolePresent(role, ERole.HR.name())) {
				Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
				if (HRMSHelper.isNullOrEmpty(kra)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getCycleId(), ERecordStatus.Y.name());
				if (HRMSHelper.isNullOrEmpty(cycle)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraWf kraWf = kraWfDao.findByKraAndCycleIdAndIsActive(kra, cycle, ERecordStatus.Y.name());

				if (!HRMSHelper.isNullOrEmpty(kraWf)) {
					if (HRMSHelper.isNullOrEmpty(kraWf.getKra())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1792));
					}

					kraWf.setStatus(EKraStatus.APPROVED.name());
					kraWf.setPendingWith(ERole.MANAGER.name());
					kraWfDao.save(kraWf);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				// send mail LM
				Employee employee = sendMailToLineManagerOnHcdAcceptance(kra);

				// toHimself hcd
				sendmailToHcdOnAcceptance(loggedInEmployee, employee);

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1231));
		log.info("Exit from Accept KPI method");

		return response;
	}

	private void validateMcMember(RejectRequestVo request, List<String> role, Long loggedInEmpId) throws HRMSException {
		Kra empkra = kraDao.findByIdAndIsActive(request.getKraId(), IHRMSConstants.isActive);
		validateMcMember(role, loggedInEmpId, empkra);
	}

	private Employee sendMailToLineManagerOnHcdAcceptance(Kra kra) throws Exception {
		Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), kra.getEmployeeId());
		EmailRequestVO email = new EmailRequestVO();
		email.setEmailAddress(employee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId());

		email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
		TemplateVO template = new TemplateVO();
		template.setTemplateName(IHRMSConstants.LINE_MANAGER_EMAIL_ON_HCD_ACCEPTANCE);

		List<PlaceHolderVO> LMplaceHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
		placeHolderVo.setExpressionVariableValue(
				employee.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName() + " "
						+ employee.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());
		LMplaceHolders.add(placeHolderVo);

		PlaceHolderVO empplaceHolderVo = new PlaceHolderVO();
		empplaceHolderVo.setExpressionVariableName(IHRMSConstants.SUBORDINATE_KEY);
		empplaceHolderVo.setExpressionVariableValue(
				employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		LMplaceHolders.add(empplaceHolderVo);
		template.setPlaceHolders(LMplaceHolders);
		email.setTemplateVo(template);

		emailService.insertInEmailTxnTable(email);
		return employee;
	}

	private void sendmailToHcdOnAcceptance(Employee loggedInEmployee, Employee employee) throws Exception {
		EmailRequestVO hrEmail = new EmailRequestVO();
		hrEmail.setEmailAddress(loggedInEmployee.getOfficialEmailId());
		hrEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

		TemplateVO hrTemplate = new TemplateVO();
		hrTemplate.setTemplateName(IHRMSConstants.HCD_EMAIL_ON_HIS_ACCEPTANCE);

		List<PlaceHolderVO> hodplaceHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO empPlaceHolderVo = new PlaceHolderVO();
		empPlaceHolderVo.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
		empPlaceHolderVo.setExpressionVariableValue(
				loggedInEmployee.getCandidate().getFirstName() + " " + loggedInEmployee.getCandidate().getLastName());
		hodplaceHolders.add(empPlaceHolderVo);

		PlaceHolderVO emplaceHolderVo = new PlaceHolderVO();
		emplaceHolderVo.setExpressionVariableName(IHRMSConstants.SUBORDINATE_KEY);
		emplaceHolderVo.setExpressionVariableValue(
				employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		hodplaceHolders.add(emplaceHolderVo);

		hrTemplate.setPlaceHolders(hodplaceHolders);
		hrEmail.setTemplateVo(hrTemplate);
		emailService.insertInEmailTxnTable(hrEmail);
	}

	@Override
	public HRMSBaseResponse<String> rejectKpibyHcd(RejectRequestVo request) throws Exception {
		log.info("Inside rejectNewKra method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		Kra kpi = kraDao.findByIdAndIsActive(request.getKraId(), IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kpi)) {
			Employee empKpi = kpi.getEmployee();
			auditLogService.setActionHeader(IHRMSConstants.REJECT_KPI_BY_HCD,
					loggedInEmployee.getCandidate().getEmployee(), empKpi.getCandidate().getEmployee());
		}
		if (HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			if (request.getKraId() != null && request.getCycleId() != null) {
				Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
				if (kra == null) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getCycleId(), ERecordStatus.Y.name());
				if (cycle == null) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraWf kraWf = kraWfDao.findByKraAndCycleIdAndIsActive(kra, cycle, ERecordStatus.Y.name());
				kraWf.setStatus(EKraStatus.CORRECTION.name());
				kraWf.setPendingWith(EKraStatus.MANAGER.name());
				kraWf.setHcdCorrection(request.getComment());
				kraWfDao.save(kraWf);
				enableEditfunctinality(kra);

				// send mail to LM
				Employee employee = sendmailTolinemangerOnHcdcorrection(kra);

				Employee employees = sendmailToHcdOnCorrection(loggedInEmployee, kra);

				response.setResponseBody("Rejected Successfully");
				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1607));
				log.info("Exit from  rejectNewKra method");

				return response;
			} else {
				response.setResponseBody("Requet can't be null");
				response.setResponseCode(1501);
				return response;
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

	}

	private void enableEditfunctinality(Kra kra) {
		List<KraDetails> kraDetails = kraDetailsDao.findByKraAndIsActiveAndIsColor(kra, IHRMSConstants.isActive,
				IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kraDetails)) {
			for (KraDetails details : kraDetails) {
				details.setIsEdit(IHRMSConstants.isActive);
				kraDetailsDao.save(details);
			}
		}
	}

	private Employee sendmailToHcdOnCorrection(Employee loggedInEmployee, Kra kra) throws Exception {
		Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), kra.getEmployeeId());
		EmailRequestVO email = new EmailRequestVO();
		email.setEmailAddress(loggedInEmployee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId());

		email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
		TemplateVO template = new TemplateVO();
		template.setTemplateName(IHRMSConstants.HCD_EMAIL_ON_HIS_CORRECTION);

		List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
		placeHolderVo.setExpressionVariableValue(
				loggedInEmployee.getCandidate().getFirstName() + " " + loggedInEmployee.getCandidate().getLastName());
		placeHolders.add(placeHolderVo);

		PlaceHolderVO emplaceHolderVo = new PlaceHolderVO();
		emplaceHolderVo.setExpressionVariableName(IHRMSConstants.SUBORDINATE_KEY);
		emplaceHolderVo.setExpressionVariableValue(
				employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		placeHolders.add(emplaceHolderVo);

		template.setPlaceHolders(placeHolders);

		email.setTemplateVo(template);

		emailService.insertInEmailTxnTable(email);
		return employee;
	}

	private Employee sendmailTolinemangerOnHcdcorrection(Kra kra) throws Exception {
		Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), kra.getEmployeeId());
		EmailRequestVO email = new EmailRequestVO();
		email.setEmailAddress(employee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId());

		email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
		TemplateVO template = new TemplateVO();
		template.setTemplateName(IHRMSConstants.MANAGER_EMAIL_ON_HCD_CORRECTION);

		List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
		placeHolderVo.setExpressionVariableValue(
				employee.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName() + " "
						+ employee.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());
		placeHolders.add(placeHolderVo);

		PlaceHolderVO emplaceHolderVo = new PlaceHolderVO();
		emplaceHolderVo.setExpressionVariableName(IHRMSConstants.SUBORDINATE_KEY);
		emplaceHolderVo.setExpressionVariableValue(
				employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		placeHolders.add(emplaceHolderVo);

		template.setPlaceHolders(placeHolders);

		email.setTemplateVo(template);

		emailService.insertInEmailTxnTable(email);
		return employee;
	}

	@Override
	public HRMSBaseResponse<String> acceptKpiByLineManager(RejectRequestVo request) throws Exception {
		log.info("Inside Accept KPI method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		Kra kpi = kraDao.findByIdAndIsActive(request.getKraId(), IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kpi)) {
			Employee empKpi = kpi.getEmployee();
			auditLogService.setActionHeader(IHRMSConstants.ACCEPT_KPI_BY_LM,
					loggedInEmployee.getCandidate().getEmployee(), empKpi.getCandidate().getEmployee());
		}
		if (!HRMSHelper.isNullOrEmpty(request.getKraId()) && !HRMSHelper.isNullOrEmpty(request.getCycleId())) {

			if (HRMSHelper.isRolePresent(role, ERole.MANAGER.name())
					|| HRMSHelper.isRolePresent(role, ERole.DELEGATOR.name())) {
				Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
				if (HRMSHelper.isNullOrEmpty(kra)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getCycleId(), ERecordStatus.Y.name());
				if (HRMSHelper.isNullOrEmpty(cycle)) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
				KraWf kraWf = kraWfDao.findByKraAndCycleIdAndIsActive(kra, cycle, ERecordStatus.Y.name());

				if (!HRMSHelper.isNullOrEmpty(kraWf)) {
					if (HRMSHelper.isNullOrEmpty(kraWf.getKra())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1792));
					}

					kraWf.setStatus(EKraStatus.INACTION.name());
					kraWf.setPendingWith(ERole.EMPLOYEE.name());
					kraWfDao.save(kraWf);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}

				// toEmp
				sendMailToEmpOnLMAccept(loggedInEmployee, kra);

				// toHimself LM
				sendMailToManager(loggedInEmployee);

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1231));
		log.info("Exit from Accept KPI method");

		return response;
	}

	private void sendMailToEmpOnLMAccept(Employee loggedInEmployee, Kra kra) throws Exception {
		Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), kra.getEmployeeId());
		EmailRequestVO email = new EmailRequestVO();
		email.setEmailAddress(employee.getOfficialEmailId());

		email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
		TemplateVO template = new TemplateVO();
		template.setTemplateName(IHRMSConstants.EMPLOYEE_EMAILON_LM_ACCEPTANCE);

		List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
		placeHolderVo.setExpressionVariableValue(
				employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		placeHolders.add(placeHolderVo);

		template.setPlaceHolders(placeHolders);
		email.setTemplateVo(template);

		emailService.insertInEmailTxnTable(email);

	}

	private void sendMailToManager(Employee loggedInEmployee) throws Exception {
		EmailRequestVO mangerEmail = new EmailRequestVO();
		mangerEmail.setEmailAddress(loggedInEmployee.getOfficialEmailId());
		mangerEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

		TemplateVO hrTemplate = new TemplateVO();
		hrTemplate.setTemplateName(IHRMSConstants.MANAGER_EMAIL_ON_HIS_ACCEPTANCE);

		List<PlaceHolderVO> hodplaceHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO empPlaceHolderVo = new PlaceHolderVO();
		empPlaceHolderVo.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
		empPlaceHolderVo.setExpressionVariableValue(
				loggedInEmployee.getCandidate().getFirstName() + " " + loggedInEmployee.getCandidate().getLastName());
		hodplaceHolders.add(empPlaceHolderVo);

		hrTemplate.setPlaceHolders(hodplaceHolders);
		mangerEmail.setTemplateVo(hrTemplate);
		emailService.insertInEmailTxnTable(mangerEmail);
	}

	@Override
	public HRMSBaseResponse<String> acceptKpiByTeamMember(RejectRequestVo request) throws Exception {
		log.info("Inside Accept KPI method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		Kra kpi = kraDao.findByIdAndIsActive(request.getKraId(), IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kpi)) {
			Employee empKpi = kpi.getEmployee();
			auditLogService.setActionHeader(IHRMSConstants.ACCEPT_KPI_BY_TM,
					loggedInEmployee.getCandidate().getEmployee(), empKpi.getCandidate().getEmployee());
		}
		if (!HRMSHelper.isNullOrEmpty(request.getKraId()) && !HRMSHelper.isNullOrEmpty(request.getCycleId())) {
			kraHelper.acceptKpiByTeamMemberValidations(request);
			acceptKpiByTeamMember(request, role);
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1231));
		log.info("Exit from Accept KPI method");

		return response;
	}

	private void acceptKpiByTeamMember(RejectRequestVo request, List<String> role) throws HRMSException, Exception {
		if (HRMSHelper.isRolePresent(role, ERole.EMPLOYEE.name())) {
			Kra kra = kraDao.findByIdAndIsActive(request.getKraId(), ERecordStatus.Y.name());
			if (HRMSHelper.isNullOrEmpty(kra)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}
			KraCycle cycle = kraCycleDAO.findByIdAndIsActive(request.getCycleId(), ERecordStatus.Y.name());
			if (HRMSHelper.isNullOrEmpty(cycle)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}
			KraWf kraWf = kraWfDao.findByKraAndCycleIdAndIsActive(kra, cycle, ERecordStatus.Y.name());

			if (!HRMSHelper.isNullOrEmpty(kraWf)) {
				kraWf.setStatus(EKraStatus.COMPLETED.name());
				kraWf.setPendingWith(ERole.HR.name());
				kraWfDao.save(kraWf);
				kpiHelper.setFinalKpiObjectives(kra, cycle);
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}
			Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), kra.getEmployeeId());
			sendMailToEmpOnAcceptKpi(kra, employee);
			sendMailToManagerEmpAcceptKpi(employee);
			sendMailToAllHRs(employee);

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private void sendMailToAllHRs(Employee employee) throws HRMSException, Exception {
		LoginEntityType loginRole = loginEntityTypeDAO.findByRoleName(ERole.HR.name());

		if (loginRole == null) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		List<HREmailDTO> hrList = employeeDAO.findAllHREmailId(loginRole.getId(), IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(hrList)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		for (HREmailDTO hrEmail : hrList) {

			EmailRequestVO email = new EmailRequestVO();
			email.setEmailAddress(hrEmail.getOfficialEmailId());
			email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
			TemplateVO hrTemplate = new TemplateVO();
			hrTemplate.setTemplateName(IHRMSConstants.ACCEPTED_KPI_BY_EMP_TO_HR);
			hrTemplate.setTemplateName(IHRMSConstants.HR_EMAIL_ON_KPI_SUBMISSION);

			List<PlaceHolderVO> hrPlaceHolders = new ArrayList<>();
			PlaceHolderVO empPlaceHolder = new PlaceHolderVO();
			empPlaceHolder.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
			empPlaceHolder.setExpressionVariableValue(hrEmail.getFirstName() + " " + hrEmail.getLastName());
			hrPlaceHolders.add(empPlaceHolder);

			PlaceHolderVO subPlaceHolderVo = new PlaceHolderVO();
			subPlaceHolderVo.setExpressionVariableName("subordinateName");
			subPlaceHolderVo.setExpressionVariableValue(
					employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
			hrPlaceHolders.add(subPlaceHolderVo);
			hrTemplate.setPlaceHolders(hrPlaceHolders);
			email.setTemplateVo(hrTemplate);
			emailService.insertInEmailTxnTable(email);
		}
	}

	private void sendMailToManagerEmpAcceptKpi(Employee employee) throws Exception {
		EmailRequestVO managerEmail = new EmailRequestVO();
		managerEmail.setEmailAddress(employee.getEmployeeReportingManager().getReporingManager().getOfficialEmailId());
		managerEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

		TemplateVO managerTemplate = new TemplateVO();
		managerTemplate.setTemplateName(IHRMSConstants.ACCEPTED_KPI_BY_EMP_TO_MANGER);

		List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
		placeHolderVo.setExpressionVariableValue(
				employee.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName() + " "
						+ employee.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());
		PlaceHolderVO subPlaceHolderVo = new PlaceHolderVO();
		subPlaceHolderVo.setExpressionVariableName(IHRMSConstants.SUBORDINATE_KEY);
		subPlaceHolderVo.setExpressionVariableValue(
				employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());

		placeHolders.add(placeHolderVo);
		placeHolders.add(subPlaceHolderVo);
		managerTemplate.setPlaceHolders(placeHolders);
		managerEmail.setTemplateVo(managerTemplate);
		emailService.insertInEmailTxnTable(managerEmail);
	}

	private void sendMailToEmpOnAcceptKpi(Kra kra, Employee employee) throws Exception {

		EmailRequestVO email = new EmailRequestVO();
		email.setEmailAddress(employee.getOfficialEmailId());

		email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
		TemplateVO template = new TemplateVO();
		template.setTemplateName(IHRMSConstants.ACCEPTED_KPI_BY_EMP);

		List<PlaceHolderVO> placeHolders = new ArrayList<PlaceHolderVO>();
		PlaceHolderVO placeHolderVo = new PlaceHolderVO();
		placeHolderVo.setExpressionVariableName(IHRMSConstants.EMAIL_EMPLOYEE_NAME_KEY);
		placeHolderVo.setExpressionVariableValue(
				employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		placeHolders.add(placeHolderVo);
		template.setPlaceHolders(placeHolders);
		email.setTemplateVo(template);
		emailService.insertInEmailTxnTable(email);
	}

	@Override
	public HRMSBaseResponse<String> sendMailOnSubmitKra(Long kraid, String isObjectiveSubmit)
			throws HRMSException, Exception {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpIds = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpIds, ERecordStatus.Y.name());
		Kra kpi = kraDao.findByIdAndIsActive(kraid, IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kpi)) {
			Employee empKpi = kpi.getEmployee();
			auditLogService.setActionHeader(IHRMSConstants.SUBMIT_KPI_BY_LM,
					loggedInEmployee.getCandidate().getEmployee(), empKpi.getCandidate().getEmployee());
		}
		if (HRMSHelper.isNullOrEmpty(isObjectiveSubmit)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1772));
		}
		if (isObjectiveSubmit.equals(IHRMSConstants.isActive)) {

			Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

			Kra kra = kraDao.findByIdAndIsActive(kraid, IHRMSConstants.isActive);
			if (HRMSHelper.isNullOrEmpty(kra)) {
				throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
			}
			// addWFStatus(kra, loggedInEmpId, EKraStatus.INPROGRESS.name(),
			// ERole.HR.name());
			addWFStatus(kra, loggedInEmpId, EKraStatus.SUBMITTED.name(), ERole.DIVISIONHEAD.name());
			disableEdit(kra);
			Employee employee = kra.getEmployee();
			Employee lineManager = employeeDAO.findActiveEmployeeById(loggedInEmpId, IHRMSConstants.isActive);

			sendMailToAllHRs(employee);
			sendMailToLineManagerOnSubmitObjective(lineManager, employee);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1217));
			log.info("Exit from sendMailOnSumitKra method");

		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1771));
		}
		return response;
	}

	private void disableEdit(Kra kra) {
		List<KraDetails> kraDetails = kraDetailsDao.findByKraAndIsActiveAndIsColor(kra, IHRMSConstants.isActive,
				IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(kraDetails)) {
			for (KraDetails details : kraDetails) {
				details.setIsEdit(IHRMSConstants.isNotActive);
				kraDetailsDao.save(details);
			}
		}
	}

	private void sendMailToLineManagerOnSubmitObjective(Employee lineManager, Employee employee) throws Exception {
		EmailRequestVO lineManagerEmail = new EmailRequestVO();
		lineManagerEmail.setEmailAddress(lineManager.getOfficialEmailId());
		lineManagerEmail.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
		TemplateVO managerTemplateVo = new TemplateVO();
		managerTemplateVo.setTemplateName(IHRMSConstants.MANAGER_SUBMIT_EMAIL_FOR_HIMSELF_ON_SUBMIT);

		List<PlaceHolderVO> managerPlaceHolders = new ArrayList<>();
		PlaceHolderVO lineManagerPlaceHolderVo = new PlaceHolderVO();
		lineManagerPlaceHolderVo.setExpressionVariableName("empName");
		lineManagerPlaceHolderVo.setExpressionVariableValue(
				lineManager.getCandidate().getFirstName() + " " + lineManager.getCandidate().getLastName());
		managerPlaceHolders.add(lineManagerPlaceHolderVo);

		PlaceHolderVO subPlaceHolderVo = new PlaceHolderVO();
		subPlaceHolderVo.setExpressionVariableName("subordinateName");
		subPlaceHolderVo.setExpressionVariableValue(
				employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		managerPlaceHolders.add(subPlaceHolderVo);
		managerTemplateVo.setPlaceHolders(managerPlaceHolders);
		lineManagerEmail.setTemplateVo(managerTemplateVo);

		emailService.insertInEmailTxnTable(lineManagerEmail);
	}

	/*
	 * public void updateKpiCycleStatus() throws HRMSException {
	 * log.info("Entering updateKpiCycleStatus method");
	 * 
	 * KraCycleStatus openStatus = kraStatusDao.findByName(IHRMSConstants.OPEN);
	 * KraCycleStatus closedStatus = kraStatusDao.findByName(IHRMSConstants.CLOSED);
	 * 
	 * List<KraCycle> validCycles = kraCycleDAO.findValidCycleEntry();
	 * 
	 * if (validCycles == null || validCycles.isEmpty()) {
	 * log.info("No active cycle found"); return; }
	 * 
	 * List<KraCycle> allCycles = kraCycleDAO.findAll();
	 * 
	 * if (allCycles != null) { for (KraCycle kraCycle : allCycles) {
	 * updateCycle(kraCycle, validCycles, openStatus, closedStatus);
	 * updateCycleCalendar(kraCycle, validCycles, openStatus, closedStatus); } }
	 * 
	 * log.info("Exiting updateKpiCycleStatus method"); }
	 * 
	 * private void updateCycle(KraCycle kraCycle, List<KraCycle> validCycles,
	 * KraCycleStatus openStatus, KraCycleStatus closedStatus) { if
	 * (validCycles.contains(kraCycle)) {
	 * kraCycle.setIsActive(IHRMSConstants.isActive);
	 * kraCycle.setStatus(openStatus); } else {
	 * kraCycle.setIsActive(IHRMSConstants.isNotActive);
	 * kraCycle.setStatus(closedStatus); }
	 * 
	 * kraCycleDAO.save(kraCycle);
	 * 
	 * log.info("Updated Cycle : {} with status: {} and isActive: {} for Year: {}",
	 * kraCycle.getCycleName(), kraCycle.getStatus().getStatus(),
	 * kraCycle.getIsActive(), kraCycle.getYear().getYear()); }
	 * 
	 * private void updateCycleCalendar(KraCycle kraCycle, List<KraCycle>
	 * validCycles, KraCycleStatus openStatus, KraCycleStatus closedStatus) {
	 * List<KraCycleCalender> phaseToUpdate = calenderDao.findByCycleId(kraCycle);
	 * 
	 * if (phaseToUpdate == null || phaseToUpdate.isEmpty()) {
	 * log.warn("No calendar phases found for Cycle: {}", kraCycle.getCycleName());
	 * return; }
	 * 
	 * for (KraCycleCalender cycle : phaseToUpdate) {
	 * 
	 * if (validCycles.contains(cycle.getCycleId())) {
	 * cycle.setIsActive(IHRMSConstants.isActive); cycle.setStatus(openStatus); }
	 * else { cycle.setIsActive(IHRMSConstants.isNotActive);
	 * cycle.setStatus(closedStatus); }
	 * 
	 * calenderDao.save(cycle);
	 * 
	 * log.info("Updated Calendar Cycle Phase : {} with status: {} and isActive: {}"
	 * , cycle.getCurrentPhase(), cycle.getStatus().getStatus(),
	 * cycle.getIsActive()); }
	 * 
	 * log.info("Updating calendar for Cycle: {}", kraCycle.getCycleName()); }
	 */

	@Override
	public HRMSBaseResponse<String> deleteOrgKpi(OrgKpiDeleteRequestVo request) throws HRMSException {
		log.info("Inside delete Orgnizational kpi method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		validateRolePermission();
		auditLogService.setActionHeader(IHRMSConstants.DELETE_ORG_KPI, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());
		setOrgKpiEntity(request, loggedInEmployee);
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1774));
		log.info("Exit from delete Orgnizational KPI method");
		return response;

	}

	private void setOrgKpiEntity(OrgKpiDeleteRequestVo request, Employee loggedInEmployee) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
		for (Long kpiId : request.getId()) {
			GradeToObjectiveMapping orgKpi = gradeObjectiveMapDao.findById(kpiId)
					.orElseThrow(() -> new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201)));

			orgKpi.setIsActive(IHRMSConstants.isNotActive);
			orgKpi.setUpdatedBy(loggedInEmployee.getCandidate().getLoginEntity().getLoginEntityName());
			orgKpi.setUpdatedDate(new Date());

			gradeObjectiveMapDao.save(orgKpi);
		}
	}

    @Override
    public HRMSBaseResponse<KraCeoDashboardResponseVO> getOrgLevelDashboard() throws HRMSException {

        List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

        if (!HRMSHelper.isRolePresent(role, ERole.ADMIN.name())) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }

        //Fetch active cycle
        KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
        List<KraCycle> openCycles = kraCycleDAO.findAllByStatus(status);

        if (ObjectUtils.isEmpty(openCycles)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
        }

        if (openCycles.size() > 1) {
            throw new HRMSException(1900, ResponseCode.getResponseCodeMap().get(1900));
        }

        KraCycle cycle = openCycles.get(0);
        if (HRMSHelper.isNullOrEmpty(cycle)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
        }
        Long cycleId = cycle.getId();

        HRMSBaseResponse<KraCeoDashboardResponseVO> baseResponse = new HRMSBaseResponse<>();
        KraCeoDashboardResponseVO responseVo = new KraCeoDashboardResponseVO();

        List<KraAggregateScores> deptLevelData;
        List<KraAggregateScores> orgLevelData;
        deptLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregation(cycleId, "Department");
        orgLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregation(cycleId, "Organisation");

        List<KraCeoDashboardResponseVO.AggregatedScoreVO> departmentScores = new ArrayList<>();
        for (KraAggregateScores data : deptLevelData) {
            KraCeoDashboardResponseVO.AggregatedScoreVO deptScore = new KraCeoDashboardResponseVO.AggregatedScoreVO();
            deptScore.setDepartmentId(data.getDepartment().getId());
            deptScore.setDepartmentName(data.getDepartment().getDepartmentName());
            deptScore.setAggRating(data.getAggregateCalibratedRating());
            KraRatingRange range = rangeDao.findByAverage(data.getAggregateCalibratedRating());
            if (range != null) {
                deptScore.setAggRatingLabel1(range.getRemark());
                deptScore.setAggRatingLabel2(range.getRating());
            } else {
                deptScore.setAggRatingLabel1("N/A");
                deptScore.setAggRatingLabel2("N/A");
            }
            deptScore.setAiFeedback(data.getCeoAiComment());

            departmentScores.add(deptScore);
        }

        KraCeoDashboardResponseVO.AggregatedScoreVO orgScore = new KraCeoDashboardResponseVO.AggregatedScoreVO();
        for (KraAggregateScores data : orgLevelData) {
            orgScore.setAggRating(data.getAggregateCalibratedRating());
            KraRatingRange range = rangeDao.findByAverage(data.getAggregateCalibratedRating());
            if (range != null) {
                orgScore.setAggRatingLabel1(range.getRemark());
                orgScore.setAggRatingLabel2(range.getRating());
            } else {
                orgScore.setAggRatingLabel1("N/A");
                orgScore.setAggRatingLabel2("N/A");
            }
            orgScore.setAiFeedback(data.getCeoAiComment());
        }

        responseVo.setDepartmentScores(departmentScores);
        responseVo.setOrganizationScore(orgScore);
        baseResponse.setResponseBody(responseVo);
        baseResponse.setResponseCode(1200);
        baseResponse.setResponseMessage("Success");
        return baseResponse;
    }

    @Override
    public HRMSBaseResponse<KraCeoDashboardResponseVO> getDepartmentLevelDashboard(Long deptId) throws HRMSException {

        List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

        if (!(HRMSHelper.isRolePresent(role, ERole.ADMIN.name()) ||
                HRMSHelper.isRolePresent(role, ERole.HOD.name())
        )) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }

        //Fetch active cycle
        KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
        List<KraCycle> openCycles = kraCycleDAO.findAllByStatus(status);

        if (ObjectUtils.isEmpty(openCycles)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
        }

        if (openCycles.size() > 1) {
            throw new HRMSException(1900, ResponseCode.getResponseCodeMap().get(1900));
        }

        KraCycle cycle = openCycles.get(0);
        if (HRMSHelper.isNullOrEmpty(cycle)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
        }
        Long cycleId = cycle.getId();
        Long departmentId;

        //Fetch department details if HOD login
        if (HRMSHelper.isNullOrEmpty(deptId) ) {
            if (HRMSHelper.isRolePresent(role, ERole.HOD.name())) {
                Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
                Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
                EmployeeDepartment department = departmentDAO.findByEmployee(loggedInEmployee);

                if (HRMSHelper.isNullOrEmpty(department)) {
                    throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
                }
                departmentId = department.getDepartment().getId();
            } else {
                throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
            }
        } else {
            departmentId = deptId;
        }

        HRMSBaseResponse<KraCeoDashboardResponseVO> baseResponse = new HRMSBaseResponse<>();
        KraCeoDashboardResponseVO responseVo = new KraCeoDashboardResponseVO();

        List<KraAggregateScores> deptLevelData;
        deptLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregationAndDepartment_Id(cycleId, "Department", departmentId);

        KraCeoDashboardResponseVO.AggregatedScoreVO deptScore = new KraCeoDashboardResponseVO.AggregatedScoreVO();
        for (KraAggregateScores data : deptLevelData) {
            deptScore.setDepartmentId(data.getDepartment().getId());
            deptScore.setDepartmentName(data.getDepartment().getDepartmentName());
            deptScore.setAggRating(data.getAggregateCalibratedRating());
            KraRatingRange range = rangeDao.findByAverage(data.getAggregateCalibratedRating());
            if (range != null) {
                deptScore.setAggRatingLabel1(range.getRemark());
                deptScore.setAggRatingLabel2(range.getRating());
            } else {
                deptScore.setAggRatingLabel1("N/A");
                deptScore.setAggRatingLabel2("N/A");
            }
            deptScore.setAiFeedback(data.getCeoAiComment());
        }

        responseVo.setDepartmentScore(deptScore);
        baseResponse.setResponseBody(responseVo);
        baseResponse.setResponseCode(1200);
        baseResponse.setResponseMessage("Success");
        return baseResponse;
    }

	@Override
	public HRMSBaseResponse<List<KraResponseVO>> getKraListReport(KraListRequestVo request, Pageable pageable)
	        throws HRMSException {

	    HRMSBaseResponse<List<KraResponseVO>> response = new HRMSBaseResponse<>();
	    List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
	    Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
	    Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());

	    if (!HRMSHelper.isRolePresent(role, request.getRoleName())) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
	    }

	    validateRequest(request);

	    List<Integer> mangerIds = new ArrayList<>();
	    List<Long> departmentIds = new ArrayList<>();
	    List<Long> divIds = new ArrayList<>();

	    KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
	    List<KraCycle> openCycles = kraCycleDAO.findAllByStatus(status);

	    if (ObjectUtils.isEmpty(openCycles)) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
	    }

	    if (openCycles.size() > 1) {
	        throw new HRMSException(1900, ResponseCode.getResponseCodeMap().get(1900));
	    }

	    KraCycle cycle = openCycles.get(0);
	    String cycleName = cycle.getCycleName();

	    if (HRMSHelper.isNullOrEmpty(cycle)) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
	    }

	    validatedDataByManagerAndHodRolewise(request, loggedInEmpId, loggedInEmployee, mangerIds, departmentIds, divIds);

	    // ✅ Fetch department IDs for HR role
	    if (request.getRoleName().equalsIgnoreCase(ERole.HR.name())) {
	        List<Long> hrDepartments = hrtodepartment.findByEmployeeIds(loggedInEmpId);
	        if (!HRMSHelper.isNullOrEmpty(hrDepartments)) {
	            departmentIds.addAll(hrDepartments);
	        }
	    }

	    // ✅ Early return for Manager role if no direct reportees
	    if (request.getRoleName().equalsIgnoreCase(ERole.MANAGER.name()) && ObjectUtils.isEmpty(mangerIds)) {
	        response.setResponseBody(Collections.emptyList());
	        response.setTotalRecord(0);
	        response.setResponseCode(1200);
	        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
	        return response;
	    }

	    // ✅ Early return for HOD role if no departments mapped
	    if (request.getRoleName().equalsIgnoreCase(ERole.HOD.name()) && ObjectUtils.isEmpty(departmentIds)) {
	        response.setResponseBody(Collections.emptyList());
	        response.setTotalRecord(0);
	        response.setResponseCode(1200);
	        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
	        return response;
	    }

	    // ✅ Early return for HR role if no departments mapped
	    if (request.getRoleName().equalsIgnoreCase(ERole.HR.name()) && ObjectUtils.isEmpty(departmentIds)) {
	        response.setResponseBody(Collections.emptyList());
	        response.setTotalRecord(0);
	        response.setResponseCode(1200);
	        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
	        return response;
	    }

	    // ✅ Early return for DIV HEAD role if no divisions mapped
	    if (request.getRoleName().equalsIgnoreCase(ERole.DIVISIONHEAD.name()) && ObjectUtils.isEmpty(divIds)) {
	        response.setResponseBody(Collections.emptyList());
	        response.setTotalRecord(0);
	        response.setResponseCode(1200);
	        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
	        return response;
	    }

	    CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<KraDetailsReport> query = builder.createQuery(KraDetailsReport.class);
	    Root<KraDetailsReport> root = query.from(KraDetailsReport.class);

	    List<KraResponseVO> kradetailsResponseList = new ArrayList<>();
	    List<Predicate> predicates = new ArrayList<>();

	    // ✅ Cycle filtering
	    if (ObjectUtils.isNotEmpty(request.getCycleId())) {
	        predicates.add(builder.equal(root.get("cycle_id"), request.getCycleId()));
	    } else if (ObjectUtils.isNotEmpty(request.getYearId()) || ObjectUtils.isNotEmpty(request.getStatus())
	            || ObjectUtils.isNotEmpty(request.getDeptId()) || ObjectUtils.isNotEmpty(request.getEmpId())
	            || ObjectUtils.isNotEmpty(request.getCountryId()) || ObjectUtils.isNotEmpty(request.getGradeId())) {
	        List<KraCycle> kraCycle = kraCycleDAO.findAll();
	        List<Long> cycleIds = kraCycle.stream().map(KraCycle::getId).collect(Collectors.toList());
	        predicates.add(root.get("cycle_id").in(cycleIds));
	    } else {
	        predicates.add(builder.equal(root.get("cycle_id"), cycle.getId()));
	    }

	    validatedallfilters(request, builder, root, predicates, cycleName);
	    validateKeywordForSerach(request, builder, root, predicates);
	    validatedStatusByCyclewise(role, cycleName, builder, query, root, request);

	    // ✅ Role-wise predicate filters
	    if (ObjectUtils.isNotEmpty(mangerIds) && request.getRoleName().equalsIgnoreCase(ERole.MANAGER.name())) {
	        List<Predicate> empPredicates = mangerIds.stream()
	                .map(empId -> builder.equal(root.get("employeeId"), empId))
	                .collect(Collectors.toList());
	        predicates.add(builder.or(empPredicates.toArray(new Predicate[0])));
	    } else if (request.getRoleName().equalsIgnoreCase(ERole.EMPLOYEE.name())) {
	        predicates.add(builder.equal(root.get("employeeId"), loggedInEmpId));
	    } else if (request.getRoleName().equalsIgnoreCase(ERole.HOD.name())) {
	        if (ObjectUtils.isNotEmpty(departmentIds)) {
	            predicates.add(root.get("departmentId").in(departmentIds));
	        }
	    } else if (request.getRoleName().equalsIgnoreCase(ERole.DIVISIONHEAD.name())) {
	        if (ObjectUtils.isNotEmpty(divIds)) {
	            predicates.add(root.get("divId").in(divIds));
	        }
	    } else if (request.getRoleName().equalsIgnoreCase(ERole.HR.name())) {
	        if (ObjectUtils.isNotEmpty(departmentIds)) {
	            predicates.add(root.get("departmentId").in(departmentIds));
	        }
	    }

	    query.where(predicates.toArray(new Predicate[0]));

	    // ✅ Pagination and data fetch
	    int pageNumber = pageable.getPageNumber();
	    int pageSize = pageable.getPageSize();
	    int startRecord = pageNumber * pageSize;
	    int totalRecord = em.createQuery(query.select(root)).getResultList().size();

	    List<KraDetailsReport> filterkradetailsReport = em.createQuery(query.select(root))
	            .setFirstResult(startRecord)
	            .setMaxResults(pageSize)
	            .getResultList();

	    kradetailsResponseList = convertToKraReport(request, loggedInEmpId, filterkradetailsReport);

	    response.setResponseBody(kradetailsResponseList);
	    response.setTotalRecord(totalRecord);
	    response.setResponseCode(1200);
	    response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

	    return response;
	}


	private List<KraResponseVO> convertToKRAResponse(KraListRequestVo request, Long loggedInEmpId,
			List<KraDetailsReport> filterkradetailsReport) {

		KraYear activeYear = krayeardao.findByIsActive(ERecordStatus.Y.name());

		// ✅ Final set, assigned only once → effectively final
		final Set<Long> kpiSubmissionCycleIds;
		if (!HRMSHelper.isNullOrEmpty(activeYear)) {
			List<KraCycle> kpiRecords = cycleDAO.findByYearId(activeYear.getId());
			kpiSubmissionCycleIds = kpiRecords.stream()
					.filter(cycle -> IHRMSConstants.KPI_SUBMISSION_TYPE_ID.equals(cycle.getCycleTypeId()))
					.map(KraCycle::getId).collect(Collectors.toSet());
		} else {
			kpiSubmissionCycleIds = Collections.emptySet();
		}

		List<KraResponseVO> kradetailsResponseList = filterkradetailsReport.stream()
				.filter(kraReport -> kpiSubmissionCycleIds.contains(kraReport.getCycle_id())).map(kraReport -> {
					Kra kra = kraDao.findByIdAndIsActive(kraReport.getKraId(), ERecordStatus.Y.name());

					KraResponseVO kraResponseVO = new KraResponseVO();
					kraResponseVO.setId(kraReport.getKraId());
					kraResponseVO.setEmpId(kraReport.getEmployeeId());
					kraResponseVO.setEmployeeCode(kraReport.getEmployee_code());
					kraResponseVO.setKraYear(kraReport.getYear());
					kraResponseVO.setCycleId(kraReport.getCycle_id());

					Employee emp = employeeDAO.findActiveEmployeeById(kraReport.getEmployeeId(),
							ERecordStatus.Y.name());
					if (!HRMSHelper.isNullOrEmpty(emp)) {
						EmployeeDepartment department = departmentDAO.findByEmployee(emp);
						if (!HRMSHelper.isNullOrEmpty(department)) {
							kraResponseVO.setDepartment(department.getDepartment().getDepartmentName());
						} else {
							kraResponseVO
									.setDepartment(Optional.ofNullable(kraReport.getDepartmentName()).orElse(null));
						}
					} else {
						kraResponseVO.setDepartment(Optional.ofNullable(kraReport.getDepartmentName()).orElse(null));
					}

					kraResponseVO.setCycleName(kraReport.getCycleName());
					kraResponseVO.setCycleType(kraReport.getCycleType());
					kraResponseVO.setEmpName(kraReport.getFirstName() + " " + kraReport.getLastName());
					kraResponseVO.setGrade(kraReport.getGrade());
					// kraResponseVO.setHodRating(kraReport.getHodRating());
					// kraResponseVO.setHrRating(kraReport.getHrRating());
					// kraResponseVO.setRmRating(kraReport.getRmRating());
					kraResponseVO.setSelfRating(kraReport.getTotalSelfRating());
					kraResponseVO.setManagerRating(kraReport.getRmFinalRating());
					kraResponseVO.setIsHrCalibrated(kraReport.getIsHrCalibrated());

					if (!HRMSHelper.isNullOrEmpty(emp)) {
						EmployeeDesignation designation = designationDAO.findByEmployee(emp);
						if (!HRMSHelper.isNullOrEmpty(designation)) {
							kraResponseVO.setDesignation(designation.getDesignation().getDesignationName());
						} else {
							kraResponseVO.setDesignation(Optional.ofNullable(kraReport.getDesignation()).orElse(null));
						}
					} else {
						kraResponseVO.setDesignation(Optional.ofNullable(kraReport.getDesignation()).orElse(null));
					}

					EmployeeReportingManager reporting = reportingmanagerDAO
							.findByEmployeeAndIsActive(kraReport.getEmployeeId(), ERecordStatus.Y.name());
					if (HRMSHelper.isNullOrEmpty(reporting)) {
						kraResponseVO
								.setReportingManager(Optional.ofNullable(kraReport.getReportingManager()).orElse(null));
					} else {
						kraResponseVO.setReportingManager(reporting.getReporingManager().getCandidate().getFirstName()
								+ " " + reporting.getReporingManager().getCandidate().getLastName());
					}

					kraResponseVO.setRole(kraReport.getRole());

					DelegationMapping empId = delegationMappingDAO
							.findByDelegationForAndIsActive(kraReport.getEmployeeId(), ERecordStatus.Y.name());

					if (empId != null) {
						kraResponseVO.setDelegationId(empId.getId());
					} else {
						kraResponseVO.setDelegationId(null);
					}

					if (empId != null && !HRMSHelper.isNullOrEmpty(empId.getDelegationTo())) {
						Employee delegateEmp = employeeDAO.findActiveEmployeeById(empId.getDelegationTo(),
								ERecordStatus.Y.name());

						if (delegateEmp != null && delegateEmp.getCandidate() != null) {
							kraResponseVO.setDelegationTo(delegateEmp.getCandidate().getFirstName() + " "
									+ delegateEmp.getCandidate().getLastName());
						} else {
							kraResponseVO.setDelegationTo("");
						}
					} else {
						kraResponseVO.setDelegationTo("");
					}

					if (ERole.HOD.name().equalsIgnoreCase(request.getRoleName())
							|| ERole.HR.name().equalsIgnoreCase(request.getRoleName())
							|| ERole.MANAGER.name().equalsIgnoreCase(request.getRoleName())) {
						kraResponseVO.setPendingWith(kraReport.getPendingWith());
					}

					validateComment(request, kraReport, kra, kraResponseVO);
					validateLastYearRating(kra, kraResponseVO);
					validateAndSetFinalRating(request, kraReport, kra, kraResponseVO);
					validateStatusWithMapping(request, kra, kraResponseVO);
					validateHRResponse(request, loggedInEmpId, kraReport, kra, kraResponseVO);

					return kraResponseVO;
				}).collect(Collectors.toList());

		return kradetailsResponseList;
	}


	private List<KraResponseVO> convertToKraReport(KraListRequestVo request, Long loggedInEmpId,
			List<KraDetailsReport> filterkradetailsReport) {

		List<KraResponseVO> kradetailsResponseList = filterkradetailsReport.stream().map(kraReport -> {
			Kra kra = kraDao.findByIdAndIsActive(kraReport.getKraId(), ERecordStatus.Y.name());

			KraResponseVO kraResponseVO = new KraResponseVO();
			kraResponseVO.setId(kraReport.getKraId());
			kraResponseVO.setEmpId(kraReport.getEmployeeId());
			kraResponseVO.setEmployeeCode(kraReport.getEmployee_code());
			kraResponseVO.setKraYear(kraReport.getYear());
			kraResponseVO.setCycleId(kraReport.getCycle_id());

			Employee emp = employeeDAO.findActiveEmployeeById(kraReport.getEmployeeId(), ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(emp)) {
				EmployeeDepartment department = departmentDAO.findByEmployee(emp);
				if (!HRMSHelper.isNullOrEmpty(department)) {
					kraResponseVO.setDepartment(department.getDepartment().getDepartmentName());
				} else {
					kraResponseVO.setDepartment(Optional.ofNullable(kraReport.getDepartmentName()).orElse(null));
				}
			} else {
				kraResponseVO.setDepartment(Optional.ofNullable(kraReport.getDepartmentName()).orElse(null));
			}

			kraResponseVO.setCycleName(kraReport.getCycleName());
			kraResponseVO.setCycleType(kraReport.getCycleType());
			kraResponseVO.setEmpName(kraReport.getFirstName() + " " + kraReport.getLastName());
			kraResponseVO.setGrade(kraReport.getGrade());

			if (((kraReport.getStatus().equalsIgnoreCase(EKraStatus.SUBMITTED.name())
					&& kraReport.getPendingWith().equalsIgnoreCase(ERole.DIVISIONHEAD.name())))) {

				kraResponseVO.setIsFunctionHeadSubmit(ERecordStatus.Y.name());
			} else {
				kraResponseVO.setIsFunctionHeadSubmit(ERecordStatus.N.name());
			}
			if (IHRMSConstants.HALF_YEAR_TYPE_ID.equals(kraReport.getCycleType())) {

				kraResponseVO.setMcMemberRating(KpiHelper.translateRatingLabel(kraReport.getCalibratedRating()));

				kraResponseVO.setSelfRating(KpiHelper.translateRatingLabel(kraReport.getTotalSelfRating()));
				kraResponseVO.setManagerRating(KpiHelper.translateRatingLabel(kraReport.getRmFinalRating()));

			} else {
				kraResponseVO.setMcMemberRating(kraReport.getCalibratedRating());
				kraResponseVO.setSelfRating(kraReport.getTotalSelfRating());
				kraResponseVO.setManagerRating(kraReport.getRmFinalRating());
			}

			kraResponseVO.setIsHrCalibrated(kraReport.getIsHrCalibrated());

			if (!HRMSHelper.isNullOrEmpty(emp)) {
				EmployeeDesignation designation = designationDAO.findByEmployee(emp);
				if (!HRMSHelper.isNullOrEmpty(designation)) {
					kraResponseVO.setDesignation(designation.getDesignation().getDesignationName());
				} else {
					kraResponseVO.setDesignation(Optional.ofNullable(kraReport.getDesignation()).orElse(null));
				}
			} else {
				kraResponseVO.setDesignation(Optional.ofNullable(kraReport.getDesignation()).orElse(null));
			}

			EmployeeReportingManager reporting = reportingmanagerDAO
					.findByEmployeeAndIsActive(kraReport.getEmployeeId(), ERecordStatus.Y.name());
			if (HRMSHelper.isNullOrEmpty(reporting)) {
				kraResponseVO.setReportingManager(Optional.ofNullable(kraReport.getReportingManager()).orElse(null));
			} else {
				kraResponseVO.setReportingManager(reporting.getReporingManager().getCandidate().getFirstName() + " "
						+ reporting.getReporingManager().getCandidate().getLastName());
			}

			kraResponseVO.setRole(kraReport.getRole());

			DelegationMapping empId = delegationMappingDAO.findByDelegationForAndIsActive(kraReport.getEmployeeId(),
					ERecordStatus.Y.name());

			if (empId != null) {
				kraResponseVO.setDelegationId(empId.getId());
			} else {
				kraResponseVO.setDelegationId(null);
			}

			if (empId != null && !HRMSHelper.isNullOrEmpty(empId.getDelegationTo())) {
				Employee delegateEmp = employeeDAO.findActiveEmployeeById(empId.getDelegationTo(),
						ERecordStatus.Y.name());

				if (delegateEmp != null && delegateEmp.getCandidate() != null) {
					kraResponseVO.setDelegationTo(
							delegateEmp.getCandidate().getFirstName() + " " + delegateEmp.getCandidate().getLastName());
				} else {
					kraResponseVO.setDelegationTo("");
				}
			} else {
				kraResponseVO.setDelegationTo("");
			}

			if (ERole.HOD.name().equalsIgnoreCase(request.getRoleName())
					|| ERole.HR.name().equalsIgnoreCase(request.getRoleName())
					|| ERole.MANAGER.name().equalsIgnoreCase(request.getRoleName())
					|| ERole.DIVISIONHEAD.name().equalsIgnoreCase(request.getRoleName())
					|| ERole.EMPLOYEE.name().equalsIgnoreCase(request.getRoleName())) {
				kraResponseVO.setPendingWith(kraReport.getPendingWith());
			}

			validateComment(request, kraReport, kra, kraResponseVO);
			validateLastYearRating(kra, kraResponseVO);
			validateAndSetFinalRating(request, kraReport, kra, kraResponseVO);
			validateStatusWithMapping(request, kra, kraResponseVO);
			validateHRResponse(request, loggedInEmpId, kraReport, kra, kraResponseVO);

            if (ERole.MANAGER.name().equalsIgnoreCase(request.getRoleName())) {
                kraResponseVO.setRmAiComment(kraReport.getRmAiComment());
            } else if (ERole.DIVISIONHEAD.name().equalsIgnoreCase(request.getRoleName())) {
                kraResponseVO.setFdhAiComment(kraReport.getFdhAiComment());
            } else if (ERole.HOD.name().equalsIgnoreCase(request.getRoleName())) {
                kraResponseVO.setHodAiComment(kraReport.getHodAiComment());
            } else if (ERole.HR.name().equalsIgnoreCase(request.getRoleName())) {
                kraResponseVO.setHrAiComment(kraReport.getHrAiComment());
            }

			return kraResponseVO;
		}).collect(Collectors.toList());

		return kradetailsResponseList;
	}

	private void validatedStatusByCyclewise(List<String> role, String cycleName, CriteriaBuilder builder,
			CriteriaQuery<KraDetailsReport> query, Root<KraDetailsReport> root, KraListRequestVo request) {
		if (!HRMSHelper.isNullOrEmpty(request.getCycleId())) {
			KraCycle cycle = kraCycleDAO.getById(request.getCycleId());
			String cycleNames = cycle.getCycleName();
			Long cycleType = cycle.getCycleTypeId();
			if (cycleType.equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {

				if (HRMSHelper.isRolePresent(role, ERole.MANAGER.name())) {
					Expression<Object> statusOrder = builder.selectCase().when(
							builder.equal(root.get("pendingWith"), ERole.MANAGER.name()),
							builder.selectCase().when(builder.equal(root.get("status"), EKraStatus.APPROVED.name()), 1)
									.when(builder.equal(root.get("status"), EKraStatus.INPROGRESS.name()), 2)
									.when(builder.equal(root.get("status"), EKraStatus.CORRECTION.name()), 3)
									.otherwise(4))
							.otherwise(0);
					Order statusOrderAsc = builder.asc(statusOrder);
					query.orderBy(statusOrderAsc);

				} else if (HRMSHelper.isRolePresent(role, ERole.HOD.name())
						|| HRMSHelper.isRolePresent(role, ERole.HR.name())) {
					Expression<Integer> statusOrder = builder.<Integer>selectCase()
							.when(builder.equal(root.get("status"), EKraStatus.APPROVED.name()), 1)
							.when(builder.equal(root.get("status"), EKraStatus.CORRECTION.name()), 2)
							.when(builder.equal(root.get("status"), EKraStatus.INPROGRESS.name()), 3)
							.when(builder.equal(root.get("status"), EKraStatus.INACTION.name()), 4)
							.when(builder.equal(root.get("status"), EKraStatus.COMPLETED.name()), 5).otherwise(6);
					Order statusOrderAsc = builder.asc(statusOrder);
					query.orderBy(statusOrderAsc);
				}

			} else if (!cycleType.equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {
				if (HRMSHelper.isRolePresent(role, ERole.HOD.name())
						|| HRMSHelper.isRolePresent(role, ERole.HR.name())) {
					Expression<Integer> statusOrder = builder.<Integer>selectCase()
							.when(builder.equal(root.get("status"), EKraStatus.APPROVED.name()), 1)
							.when(builder.equal(root.get("status"), EKraStatus.REJECTED.name()), 2)
							.when(builder.equal(root.get("status"), EKraStatus.INPROCESS.name()), 3)
							.when(builder.equal(root.get("status"), EKraStatus.INCOMPLETE.name()), 4)
							.when(builder.equal(root.get("status"), EKraStatus.COMPLETED.name()), 5).otherwise(6);
					Order statusOrderAsc = builder.asc(statusOrder);
					query.orderBy(statusOrderAsc);
				} else if (HRMSHelper.isRolePresent(role, ERole.MANAGER.name())) {
					Expression<Object> statusOrder = builder.selectCase()
							.when(builder.equal(root.get("pendingWith"), ERole.MANAGER.name()),
									builder.selectCase()
											.when(builder.equal(root.get("status"), EKraStatus.INPROCESS.name()), 1)
											.when(builder.equal(root.get("status"), EKraStatus.APPROVED.name()), 2));
					Order statusOrderAsc = builder.asc(statusOrder);
					query.orderBy(statusOrderAsc);
				}
			}
		} else {
			if (HRMSHelper.isRolePresent(role, ERole.HOD.name()) || HRMSHelper.isRolePresent(role, ERole.HR.name())) {
				Expression<Integer> statusOrder = builder.<Integer>selectCase()
						.when(builder.equal(root.get("status"), EKraStatus.APPROVED.name()), 1)
						.when(builder.equal(root.get("status"), EKraStatus.REJECTED.name()), 2)
						.when(builder.equal(root.get("status"), EKraStatus.INPROCESS.name()), 3)
						.when(builder.equal(root.get("status"), EKraStatus.INPROGRESS.name()), 4)
						.when(builder.equal(root.get("status"), EKraStatus.INCOMPLETE.name()), 5)
						.when(builder.equal(root.get("status"), EKraStatus.CORRECTION.name()), 6)
						.when(builder.equal(root.get("status"), EKraStatus.COMPLETED.name()), 7)
						.when(builder.equal(root.get("status"), EKraStatus.INACTION.name()), 8).otherwise(9);
				Order statusOrderAsc = builder.asc(statusOrder);
				query.orderBy(statusOrderAsc);
			} else if (HRMSHelper.isRolePresent(role, ERole.MANAGER.name())) {
				Expression<Object> statusOrder = builder.selectCase().when(
						builder.equal(root.get("pendingWith"), ERole.MANAGER.name()),
						builder.selectCase().when(builder.equal(root.get("status"), EKraStatus.INPROCESS.name()), 1)
								.when(builder.equal(root.get("status"), EKraStatus.INPROGRESS.name()), 2)
								.when(builder.equal(root.get("status"), EKraStatus.APPROVED.name()), 3));
				Order statusOrderAsc = builder.asc(statusOrder);
				query.orderBy(statusOrderAsc);
			}
		}
	}

	private void validatedDataByManagerAndHodRolewise(KraListRequestVo request, Long loggedInEmpId,
			Employee loggedInEmployee, List<Integer> mangerIds, List<Long> departmentIds, List<Long> divIds) {
		if (request.getRoleName().equalsIgnoreCase(ERole.MANAGER.name())) {
			List<EmployeeReportingManager> reportingManager = reportingManagerDao
					.findByReporingManagerAndIsActive(loggedInEmployee, ERecordStatus.Y.name());
			for (EmployeeReportingManager manager : reportingManager) {
				mangerIds.add(manager.getEmployee().getId().intValue());
			}
		} else if (request.getRoleName().equalsIgnoreCase(ERole.HOD.name())) {
			List<HodToDepartmentMap> departmentList = hodToDepartmentMap.findByEmployeeIdAndIsActive(loggedInEmpId,
					ERecordStatus.Y.name());
			for (HodToDepartmentMap deptId : departmentList) {
				departmentIds.add(deptId.getDepartmentId());
			}
		} else if (request.getRoleName().equalsIgnoreCase(ERole.DIVISIONHEAD.name())) {
			List<Long> divisions = funcationHeadToDivisionFunctionDAO.getDivisionByEmployeeAndIsActive(loggedInEmpId,
					ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(divisions)) {
				for (Long divId : divisions) {
					divIds.add(divId);
				}
			}
		}else if (request.getRoleName().equalsIgnoreCase(ERole.HR.name())) {
		    List<Long> departments = hrtodepartment.findByEmployeeIds(loggedInEmpId);
		    if (!HRMSHelper.isNullOrEmpty(departments)) {
		    	for (Long deptId : departments ){
		    		departmentIds.add(deptId);
		    }
		}

	}
	}

	private void validateRequest(KraListRequestVo request) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request.getRoleName())) {
			throw new HRMSException(1775, ResponseCode.getResponseCodeMap().get(1775));
		}
	}

	private void validateStatusWithMapping(KraListRequestVo request, Kra kra, KraResponseVO kraResponseVO) {
		if (kra.getKraWf() == null) {
			return;
		}
		KraStatusMapping statuses = statusMappingDao.findByStatusAndPendingWithAndRoleNameAndOrgIdAndIsActive(
				kra.getKraWf().getStatus(), kra.getKraWf().getPendingWith(), request.getRoleName(), kra.getOrgId(),
				IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(statuses)) {
			kraResponseVO.setStatus(kra.getKraWf().getStatus());
		} else {
			kraResponseVO.setStatus(statuses.getLabel());
		}

	}

	private void validateAndSetFinalRating(KraListRequestVo request, KraDetailsReport kraReport, Kra kra,
			KraResponseVO kraResponseVO) {

		if (ERole.EMPLOYEE.name().equalsIgnoreCase(request.getRoleName())
				&& !EKraStatus.COMPLETED.name().equals(kraReport.getStatus())) {
			kraResponseVO.setFinalRating(null);
		} else {
			HodCycleFinalRating hodrating = hodcycleDao.findByIsActiveAndKra(ERecordStatus.Y.name(), kra);

			String finalRatingLabel = null;

			if (hodrating != null) {
				if (hodrating.getScore() != null) {
					finalRatingLabel = hodrating.getScore().getLabel();
				} else {
					finalRatingLabel = kra.getFinalRating();
				}
			} else {
				finalRatingLabel = kra.getFinalRating();
			}

			if (!HRMSHelper.isNullOrEmpty(finalRatingLabel)) {
				KraCycle kraCycle = kraCycleDAO.getById(kra.getCycleId().getId());

				if (kraCycle != null && (IHRMSConstants.HALF_YEAR_TYPE_ID.equals(kraCycle.getCycleTypeId()))) {
					kraResponseVO.setFinalRating(kpiHelper.translateRatingLabel(finalRatingLabel));
				} else {
					kraResponseVO.setFinalRating(finalRatingLabel);
				}
			} else {
				kraResponseVO.setFinalRating(null);
			}
		}
	}

	private void validateLastYearRating(Kra kra, KraResponseVO kraResponseVO) {
		KraCycle kraCycle = kraCycleDAO.getById(kra.getCycleId().getId());
		KraYear year = kraYearDao.getById(kraCycle.getYear().getId());
		long currentYear = Long.parseLong(year.getYear().trim());
		long lastYear = currentYear - 1;
		String yearname = Long.toString(lastYear);

		KraYear lastyear = kraYearDao.findByYearAndIsActive(yearname, ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(lastyear)) {
			kraResponseVO.setLastYearRating(null);
		} else {
			List<KraCycle> lastCyclelist = kraCycleDAO.findByYearAndIsActive(lastyear, ERecordStatus.Y.name());
			for (KraCycle lastCycle : lastCyclelist) {
				if (lastCycle.getCycleTypeId().equals(IHRMSConstants.YEAR_END_TYPE_ID)) {
					Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), kra.getEmployeeId());
					HodCycleFinalRating lastYearrating = hodcycleDao.findByEmployeeAndCycleId(employee, lastCycle);

					String lastYearRatingValue = null;

					if (lastYearrating != null && lastYearrating.getScore() != null) {
						lastYearRatingValue = lastYearrating.getScore().getLabel();
					} else {
						Kra lastKra = kraDao.findByEmpidAndCycleId(kra.getEmployeeId(), lastCycle.getId());
						if (lastKra != null && lastKra.getFinalRating() != null) {
							lastYearRatingValue = lastKra.getFinalRating();
						}
					}

					if (!HRMSHelper.isNullOrEmpty(lastYearRatingValue)) {
						if (kraCycle.getCycleTypeId().equals(IHRMSConstants.HALF_YEAR_TYPE_ID)) {
							kraResponseVO.setLastYearRating(KpiHelper.translateRatingLabel(lastYearRatingValue));
						} else {
							kraResponseVO.setLastYearRating(lastYearRatingValue);
						}
					}
				}
			}
		}
	}

	private void validateComment(KraListRequestVo request, KraDetailsReport kraReport, Kra kra,
			KraResponseVO kraResponseVO) {
		if ((EKraStatus.MANAGER.name().equalsIgnoreCase(request.getRoleName()))
				|| (ERole.HOD.name().equalsIgnoreCase(request.getRoleName()))) {

			HodCycleFinalRating hodCycle = hodCycleFinalRatingDao.findByIsActiveAndKra(ERecordStatus.Y.name(), kra);

			if (!HRMSHelper.isNullOrEmpty(hodCycle) && hodCycle.getComment() != null
					&& !hodCycle.getComment().isEmpty()) {
				kraResponseVO.setCalibrationComment(hodCycle.getComment());
			} else {
				kraResponseVO.setCalibrationComment(null);
			}

			kraResponseVO.setHcdCorrectionComment(kraReport.getComment());
		}

	}

	/*
	 * private void validateHRResponse(KraListRequestVo request, Long loggedInEmpId,
	 * KraDetailsReport kraReport, Kra kra, KraResponseVO kraResponseVO) {
	 * 
	 * if ((ERole.HR.name().equalsIgnoreCase(request.getRoleName()))) {
	 * List<HrToHodMap> emp =
	 * hrtohodMapDao.findByEmpIdAndIsActive(kra.getEmployeeId(),
	 * IHRMSConstants.isActive);
	 * 
	 * List<HrToHodMap> hr = hrtohodMapDao.findByHrIdAndIsActive(loggedInEmpId,
	 * IHRMSConstants.isActive); if (!HRMSHelper.isNullOrEmpty(emp) &&
	 * HRMSHelper.isNullOrEmpty(hr)) { kraResponseVO.setFinalRating(null);
	 * kraResponseVO.setManagerRating(null); kraResponseVO.setSelfRating(null);
	 * 
	 * } else { kraResponseVO.setManagerRating(kraReport.getRmFinalRating());
	 * kraResponseVO.setSelfRating(kraReport.getTotalSelfRating()); if
	 * (kraReport.getCalibratedRating() != null) {
	 * kraResponseVO.setFinalRating(kraReport.getCalibratedRating()); } else {
	 * kraResponseVO.setFinalRating(kraReport.getRmFinalRating()); } }
	 * kraResponseVO.setCountryName(kraReport.getCountryName()); }
	 * 
	 * }
	 */
	private void validateHRResponse(KraListRequestVo request, Long loggedInEmpId, KraDetailsReport kraReport, Kra kra,
			KraResponseVO kraResponseVO) {

		if ((ERole.HR.name().equalsIgnoreCase(request.getRoleName()))) {
			List<HrToHodMap> emp = hrtohodMapDao.findByEmpIdAndIsActive(kra.getEmployeeId(), IHRMSConstants.isActive);
			List<HrToHodMap> hr = hrtohodMapDao.findByHrIdAndIsActive(loggedInEmpId, IHRMSConstants.isActive);

			if (!HRMSHelper.isNullOrEmpty(emp) && HRMSHelper.isNullOrEmpty(hr)) {
				kraResponseVO.setFinalRating(null);
				kraResponseVO.setManagerRating(null);
				kraResponseVO.setSelfRating(null);

			} else {
				// ✅ Cycle check
				KraCycle currentCycle = kraCycleDAO.findById(kra.getCycleId().getId()).orElse(null);
				boolean translateForHalfOrMid = currentCycle != null
						&& (IHRMSConstants.HALF_YEAR_TYPE_ID.equals(currentCycle.getCycleTypeId()));

				// Manager Rating
				String managerRating = kraReport.getRmFinalRating();
				if (translateForHalfOrMid && managerRating != null) {
					managerRating = kpiHelper.translateRatingLabel(managerRating);
				}
				kraResponseVO.setManagerRating(managerRating);

				// Self Rating
				String selfRating = kraReport.getTotalSelfRating();
				if (translateForHalfOrMid && selfRating != null) {
					selfRating = kpiHelper.translateRatingLabel(selfRating);
				}
				kraResponseVO.setSelfRating(selfRating);

				// Final Rating
				String finalRating = kraReport.getCalibratedRating() != null ? kraReport.getCalibratedRating()
						: kraReport.getRmFinalRating();
				if (translateForHalfOrMid && finalRating != null) {
					finalRating = kpiHelper.translateRatingLabel(finalRating);
				}
				kraResponseVO.setFinalRating(finalRating);
			}
			kraResponseVO.setCountryName(kraReport.getCountryName());
		}
	}

	private void validateKeywordForSerach(KraListRequestVo request, CriteriaBuilder builder,
			Root<KraDetailsReport> root, List<Predicate> predicates) {

		if (ObjectUtils.isNotEmpty(request.getKeyword())) {
			String keywordPattern = "%" + request.getKeyword().toLowerCase() + "%";
			Predicate keywordCondition = builder.or(builder.like(builder.lower(root.get("firstName")), keywordPattern),
					builder.like(builder.lower(root.get("lastName")), keywordPattern),
					builder.like(builder.lower(root.get("status")), keywordPattern),
					builder.like(builder.lower(root.get("pendingWith")), keywordPattern),
					builder.like(builder.lower(root.get("departmentName")), keywordPattern),
					builder.like(builder.lower(root.get("empEmailId")), keywordPattern),
					builder.like(builder.lower(root.get("grade")), keywordPattern),
					builder.like(builder.lower(root.get("cycleName")), keywordPattern),
					builder.like(builder.lower(root.get("employee_code")), keywordPattern),
					builder.like(builder.lower(root.get("comment")), keywordPattern),
					builder.like(builder.lower(root.get("year")), keywordPattern),
					builder.like(builder.lower(root.get("countryName")), keywordPattern),
					builder.like(builder.lower(root.get("hcd_correction")), keywordPattern),
					builder.like(builder.lower(root.get("totalSelfRating")), keywordPattern),
					builder.like(builder.lower(root.get("rmFinalRating")), keywordPattern),
					builder.like(builder.lower(root.get("calibratedRating")), keywordPattern));
			predicates.add(keywordCondition);
		}

	}

	private void validatedallfilters(KraListRequestVo request, CriteriaBuilder builder, Root<KraDetailsReport> root,
			List<Predicate> predicates, String cycleName) {

		if (ObjectUtils.isNotEmpty(request.getYearId())) {
			Predicate selectYear = builder.equal(root.get("year"), request.getYearId());
			predicates.add(selectYear);
		}

		if (ObjectUtils.isNotEmpty(request.getStatus())) {

			if (ObjectUtils.isNotEmpty(request.getStatus())
					&& request.getStatus().equalsIgnoreCase(EKraStatus.PENDING.name())) {

				List<String> statusList = new ArrayList<>();
				statusList.add(EKraStatus.INPROCESS.name());
				statusList.add(EKraStatus.INPROGRESS.name());
				statusList.add(EKraStatus.REJECTED.name());
				statusList.add(EKraStatus.CORRECTION.name());
				statusList.add(EKraStatus.INCOMPLETE.name());
				statusList.add(EKraStatus.INACTION.name());
				statusList.add(EKraStatus.APPROVED.name());
				statusList.add(EKraStatus.SUBMITTED.name());
				Predicate selectStatus = root.get("status").in(statusList);
				predicates.add(selectStatus);
			} else if (ObjectUtils.isNotEmpty(request.getStatus())
					&& request.getStatus().equalsIgnoreCase(EKraStatus.COMPLETED.name())) {
				List<String> statusList = new ArrayList<>();
				statusList.add(EKraStatus.COMPLETED.name());
				Predicate selectStatus = builder.equal(root.get("status"), request.getStatus());
				predicates.add(selectStatus);
			} else if (ObjectUtils.isNotEmpty(request.getStatus())
					&& request.getStatus().equalsIgnoreCase(EKraStatus.SUBMITTED.name())) {
				Predicate selectStatus = builder.equal(root.get("status"), EKraStatus.INACTION.name());
				predicates.add(selectStatus);
			} else if (ObjectUtils.isNotEmpty(request.getStatus())
					&& request.getStatus().equalsIgnoreCase(EKraStatus.CALIBRATING.name())) {
				if (!HRMSHelper.isNullOrEmpty(request.getCycleId())) {
					KraCycle cycle = kraCycleDAO.getById(request.getCycleId());
					String cycleNames = cycle.getCycleName();
					Long cycleType = cycle.getCycleTypeId();
					if (!cycleType.equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {
						List<String> statusList = new ArrayList<>();
						statusList.add(EKraStatus.APPROVED.name());
						Predicate selectStatus = builder.equal(root.get("status"), EKraStatus.APPROVED.name());
						predicates.add(selectStatus);
					} else if (cycleType.equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {
						List<String> statusList = new ArrayList<>();
						statusList.add(EKraStatus.CALIBRATING.name());
						Predicate selectStatus = builder.equal(root.get("status"), request.getStatus());
						predicates.add(selectStatus);
					}
				} else {
					List<String> statusList = new ArrayList<>();
					statusList.add(EKraStatus.APPROVED.name());
					Predicate selectStatus = builder.equal(root.get("status"), EKraStatus.APPROVED.name());
					predicates.add(selectStatus);
				}
			}
		}

		if (ObjectUtils.isNotEmpty(request.getDeptId())) {
			Predicate selectDept = builder.equal(root.get("departmentId"), request.getDeptId());
			predicates.add(selectDept);
		}

		if (ObjectUtils.isNotEmpty(request.getEmpId())) {
			Predicate selectEmpId = builder.equal(root.get("employeeId"), request.getEmpId());
			predicates.add(selectEmpId);
		}

		if (ObjectUtils.isNotEmpty(request.getCountryId())) {
			Predicate selectCountryId = builder.equal(root.get("countryId"), request.getCountryId());
			predicates.add(selectCountryId);
		}

		if (ObjectUtils.isNotEmpty(request.getGradeId())) {
			Predicate selectGradeId = builder.equal(root.get("gradeId"), request.getGradeId());
			predicates.add(selectGradeId);
		}

		if (ObjectUtils.isNotEmpty(request.getDelegatedTo())) {
			Predicate selectDelegatedId = builder.equal(root.get("delegationTo"), request.getDelegatedTo());
			predicates.add(selectDelegatedId);
		}

	}

	private void validateRolePermissionforUnlocked() throws HRMSException {
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	@Override
	public HRMSBaseResponse<String> lockedcycle(Long cycleId) throws HRMSException {
		log.info("Inside lockedcycle method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		validateRolePermissionforUnlocked();
		KraCycle cycle = kraCycleDAO.findByIdAndIsActive(cycleId, ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(cycle)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1627));
		}
		if (cycle.getIsLocked().equalsIgnoreCase(ERecordStatus.Y.name())) {
			cycle.setIsLocked(ERecordStatus.N.name());

			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1776));
		} else {
			cycle.setIsLocked(ERecordStatus.Y.name());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1777));
		}

		kraCycleDAO.save(cycle);
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		log.info("Exit from lockedcycle method");
		return response;
	}

	@Override
	public HRMSBaseResponse<String> deleteLineManagerKpi(OrgKpiDeleteVo request) throws HRMSException {
		log.info("Inside delete LineManager kpi method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		validateManagerRole();
		Optional<KraDetails> kraDetails = kraDetailsDao.findById(request.getId());
		if (kraDetails.isPresent()) {
			Kra kpi = kraDao.findByIdAndIsActive(kraDetails.get().getKra().getId(), IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(kpi)) {
				Employee empKpi = kpi.getEmployee();
				auditLogService.setActionHeader(IHRMSConstants.DELETE_KPI_BY_LM,
						loggedInEmployee.getCandidate().getEmployee(), empKpi.getCandidate().getEmployee());
			}
		}
		removeLineManagerKPI(request, loggedInEmployee);
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1778));
		log.info("Exit from delete LineManager KPI method");
		return response;

	}

	private void removeLineManagerKPI(OrgKpiDeleteVo request, Employee loggedInEmployee) throws HRMSException {
		KraDetails kraDetails = kraDetailsDao.findById(request.getId())
				.orElseThrow(() -> new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201)));
		kraDetails.setIsActive(ERecordStatus.N.name());
		kraDetails.setUpdatedBy(loggedInEmployee.getCandidate().getLoginEntity().getLoginEntityName());
		kraDetails.setUpdatedDate(new Date());
		kraDetailsDao.save(kraDetails);
	}

	@Override
	public HRMSBaseResponse<List<OrgKpiReportResponse>> getOrgKpiListReport(OrgKpiRequest request, Pageable pageable)
			throws HRMSException {
		HRMSBaseResponse<List<OrgKpiReportResponse>> response = new HRMSBaseResponse<List<OrgKpiReportResponse>>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());

		validateRolePermission();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<OrganizationalKpiListView> query = builder.createQuery(OrganizationalKpiListView.class);
		Root<OrganizationalKpiListView> root = query.from(OrganizationalKpiListView.class);
		List<Predicate> predicates = new ArrayList<>();

		if (ObjectUtils.isNotEmpty(request.getYearId())) {
			Predicate selectYear = builder.equal(root.get("year"), request.getYearId());
			predicates.add(selectYear);
		}
		if (ObjectUtils.isNotEmpty(request.getDeptId())) {
			Predicate selectDept = builder.equal(root.get("departmentId"), request.getDeptId());
			predicates.add(selectDept);
		}
		if (ObjectUtils.isNotEmpty(request.getGradeId())) {
			Predicate selectGrade = builder.equal(root.get("gradeId"), request.getGradeId());
			predicates.add(selectGrade);
		}
		if (ObjectUtils.isNotEmpty(request.getKpiTypeId())) {
			Predicate selectkpitype = builder.equal(root.get("KpiTypeId"), request.getKpiTypeId());
			predicates.add(selectkpitype);
		}

		if (ObjectUtils.isNotEmpty(request.getKeyword())) {
			String keywordPattern = "%" + request.getKeyword().toLowerCase() + "%";
			Predicate keywordCondition = builder.or(
					builder.like(builder.lower(root.get("departmentName")), keywordPattern),
					builder.like(builder.lower(root.get("gradeDesc")), keywordPattern),
					builder.like(builder.lower(root.get("year")), keywordPattern),
					builder.like(builder.lower(root.get("kpiType")), keywordPattern),
					builder.like(builder.lower(root.get("metricType")), keywordPattern),
					builder.like(builder.lower(root.get("kpiObjective")), keywordPattern));
			predicates.add(keywordCondition);
		}

		query.where(predicates.toArray(new Predicate[0]));
		// pagination
		int pageNumber = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		int startRecord = pageNumber * pageSize;
		int totalRecord = em.createQuery(query.select(root)).getResultList().size();
		List<OrganizationalKpiListView> filterorgkpiReport = em.createQuery(query.select(root))
				.setFirstResult(startRecord).setMaxResults(pageSize).getResultList();

		List<OrgKpiReportResponse> orgkpiResponseList = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(filterorgkpiReport)) {
			orgkpiResponseList = filterorgkpiReport.stream().map(obj -> {
				OrgKpiReportResponse kpiResponse = new OrgKpiReportResponse();
				kpiResponse.setId(obj.getMapId());
				kpiResponse.setDeptId(obj.getDepartmentId());
				kpiResponse.setDepartmentName((obj != null && obj.getDepartmentName() != null) ? obj.getDepartmentName()
						: IHRMSConstants.GENERIC_KPI);
				kpiResponse.setGradeId(obj.getGradeId());
				kpiResponse.setGradeDescription(
						(obj != null && obj.getGradeDesc() != null) ? obj.getGradeDesc() : IHRMSConstants.GENERIC_KPI);
				kpiResponse.setKpiTypeId(obj.getKpiTypeId());
				kpiResponse.setKpiType(obj.getKpiType());
				kpiResponse.setMetricId(obj.getMetricTypeId());
				kpiResponse.setMetric(obj.getMetricType());
				kpiResponse.setObjective(obj.getKpiObjective());
				Double value = obj.getMetricWeight();
				Double roundedValue = BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
				kpiResponse.setObjectiveWeight(roundedValue);
				kpiResponse.setYearId(obj.getYearId());
				kpiResponse.setYear(obj.getYear());
				return kpiResponse;
			}).collect(Collectors.toList());
		}
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<OrganizationalKpiListView> countRoot = countQuery.from(OrganizationalKpiListView.class);
		countQuery.select(builder.count(countRoot));
		countQuery.where(predicates.toArray(new Predicate[] {}));
		response.setResponseBody(orgkpiResponseList);
		response.setTotalRecord(totalRecord);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		return response;
	}

	@Override
	public byte[] downloadOrgKpiReport(OrgKpiRequest request) throws HRMSException {

		int pageSize = (request.getTotalRecord() > Integer.MAX_VALUE) ? Integer.MAX_VALUE
				: request.getTotalRecord().intValue();

		Pageable pageable = PageRequest.of(0, pageSize);
		List<OrgKpiReportResponse> reportData = getOrgKpiListReport(request, pageable).getResponseBody();

		if (HRMSHelper.isNullOrEmpty(reportData)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Manage_Org_KPI's_Report");
			sheet.setDisplayGridlines(false);
			CreationHelper helper = workbook.getCreationHelper();

			String[] headers = { "Year", "Department Name", "Grade Description", "KPI Type", "Metric",
					"Objective Weight", "Objective" };
			int totalCols = headers.length;

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
			titleCell.setCellValue("Manage Org KPI's Report");

			CellStyle titleStyle = workbook.createCellStyle();
			Font titleFont = workbook.createFont();
			titleFont.setBold(true);
			titleFont.setFontHeightInPoints((short) 14);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.RIGHT);
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

			CellStyle borderStyle = workbook.createCellStyle();
			borderStyle.setBorderTop(BorderStyle.MEDIUM);
			borderStyle.setBorderBottom(BorderStyle.MEDIUM);
			borderStyle.setBorderLeft(BorderStyle.MEDIUM);
			borderStyle.setBorderRight(BorderStyle.MEDIUM);

			int rowNum = headerRowIndex + 1;

			for (OrgKpiReportResponse data : reportData) {
				Row row = sheet.createRow(rowNum++);

				Cell cell0 = row.createCell(0);
				cell0.setCellValue(data.getYear());
				cell0.setCellStyle(borderStyle);

				Cell cell1 = row.createCell(1);
				cell1.setCellValue(data.getDepartmentName());
				cell1.setCellStyle(borderStyle);

				Cell cell2 = row.createCell(2);
				cell2.setCellValue(data.getGradeDescription());
				cell2.setCellStyle(borderStyle);

				Cell cell3 = row.createCell(3);
				cell3.setCellValue(data.getKpiType());
				cell3.setCellStyle(borderStyle);

				Cell cell4 = row.createCell(4);
				cell4.setCellValue(data.getMetric());
				cell4.setCellStyle(borderStyle);

				Cell cell5 = row.createCell(5);
				if (data.getObjectiveWeight() != null) {
					cell5.setCellValue(data.getObjectiveWeight());
				} else {
					cell5.setCellValue("");
				}
				cell5.setCellStyle(borderStyle);

				Cell cell6 = row.createCell(6);
				cell6.setCellValue(data.getObjective());
				cell6.setCellStyle(borderStyle);
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
	public byte[] downloadKraReport(KraListRequestVo request) throws HRMSException {

		int pageSize = (request.getTotalRecord() > Integer.MAX_VALUE) ? Integer.MAX_VALUE
				: request.getTotalRecord().intValue();
		Pageable pageable = PageRequest.of(0, pageSize);
		List<KraResponseVO> reportData = getKraListReport(request, pageable).getResponseBody();

		if (HRMSHelper.isNullOrEmpty(reportData)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("KPI_List_Report");
			sheet.setDisplayGridlines(false);
			CreationHelper helper = workbook.getCreationHelper();

			String[] headers = { "Status", "Team Member Id", "Team Member Name", "Reporting Manager", "Designation",
					"Delegated To", "Pending With", "Department Name", "Grade Description", "Year", "Cycle",
					"Team Member Rating", "Line Manger Rating", "Current Rating" };
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
			titleCell.setCellValue("KPI List Report");

			CellStyle titleStyle = workbook.createCellStyle();
			Font titleFont = workbook.createFont();
			titleFont.setBold(true);
			titleFont.setFontHeightInPoints((short) 14);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.LEFT);
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
			for (KraResponseVO data : reportData) {
				Row row = sheet.createRow(rowNum++);
				String[] values = { data.getStatus(), data.getEmployeeCode(), data.getEmpName(),
						data.getReportingManager(), data.getDesignation(), data.getDelegationTo(),
						data.getPendingWith(), data.getDepartment(), data.getGrade(), data.getKraYear(),
						data.getCycleName(), data.getSelfRating(), data.getManagerRating(), data.getFinalRating() };

				for (int i = 0; i < values.length; i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(values[i] != null ? values[i] : "");
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
	public HRMSBaseResponse<List<HcdCorrectionResponseVo>> getHcdCorrection(Long kraId) throws HRMSException {
		HRMSBaseResponse<List<HcdCorrectionResponseVo>> response = new HRMSBaseResponse<>();
		log.info("get getHcdCorrection Method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		Kra empkra = kraDao.findByIdAndIsActive(kraId, IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(empkra)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		validateMcMember(role, loggedInEmpId, empkra);
		List<KraDetails> details = kraDetailsDao.findByKraAndIsActiveAndIsColorOrderByIdAsc(empkra,
				ERecordStatus.Y.name(), ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(details)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		List<HcdCorrectionResponseVo> hcdCorrcetionlist = details.stream().map(obj -> {
			HcdCorrectionResponseVo kpiResponse = new HcdCorrectionResponseVo();
			kpiResponse.setId(obj.getId());
			kpiResponse.setObjectiveName(obj.getKraDetails());
			kpiResponse.setComment(obj.getHcdComment());
			return kpiResponse;
		}).collect(Collectors.toList());
		response.setResponseBody(hcdCorrcetionlist);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit getHcdCorrection Method");
		return response;
	}

	private void validateMcMember(List<String> role, Long loggedInEmpId, Kra empkra) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(empkra)) {
			List<HrToHodMap> emp = hrtohodMapDao.findByEmpIdAndIsActive(empkra.getEmployeeId(),
					IHRMSConstants.isActive);

			List<HrToHodMap> hr = hrtohodMapDao.findByHrIdAndIsActive(loggedInEmpId, IHRMSConstants.isActive);

			if (!HRMSHelper.isNullOrEmpty(emp) && HRMSHelper.isNullOrEmpty(hr)
					&& HRMSHelper.isRolePresent(role, ERole.HR.name())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		}
	}

	@Transactional
	@Override
	public HRMSBaseResponse<String> saveHcdCorrection(HcdCorrcetionRequest request) throws HRMSException {
		log.info("Inside saveHcdCorrection method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		Optional<KraDetails> kraDetails = kraDetailsDao.findById(request.getHcdCorrection().get(0).getId());
		if (kraDetails.isPresent()) {
			Kra kpi = kraDao.findByIdAndIsActive(kraDetails.get().getKra().getId(), IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(kpi)) {
				Employee empKpi = kpi.getEmployee();
				auditLogService.setActionHeader(IHRMSConstants.SAVED_CORRECTION_BY_HCD,
						loggedInEmployee.getCandidate().getEmployee(), empKpi.getCandidate().getEmployee());
			}
		}
		kraHelper.validateHcdCorrectionRequest(request);
		for (HcdCorrectionRequestVo requestvo : request.getHcdCorrection()) {
			KraDetails details = kraDetailsDao.findByIdAndIsActive(requestvo.getId(), ERecordStatus.Y.name());
			details.setHcdComment(requestvo.getComment());
			kraDetailsDao.save(details);
		}
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1780));
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		log.info("Exit from saveHcdCorrection method");
		return response;
	}

	@Override
	public HRMSBaseResponse<String> submitHcdCorrection(Long kraId) throws HRMSException {
		log.info("Inside submitHcdCorrection method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		Kra kra = kraDao.findByIdAndIsActive(kraId, IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(kra)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		if (!HRMSHelper.isNullOrEmpty(kra)) {
			Employee empKpi = kra.getEmployee();
			auditLogService.setActionHeader(IHRMSConstants.SUBMIT_CORRECTION_BY_HCD,
					loggedInEmployee.getCandidate().getEmployee(), empKpi.getCandidate().getEmployee());
		}
		addWFStatus(kra, loggedInEmpId, EKraStatus.CORRECTION.name(), ERole.MANAGER.name());
		response.setResponseCode(1200);
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1781));
		log.info("Exit from submitHcdCorrection method");
		return response;
	}

	private List<Long> setDeptList(OrgnizationalKpiVo request) {
		List<Long> deptList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(request.getDepartment())) {
			for (DepartmentVO departmentVo : request.getDepartment()) {
				deptList.add(departmentVo.getId());
			}
		} else {
			List<Long> departmentList = masterDepartmentDao.findByisActive(IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(departmentList)) {
				deptList.addAll(departmentList);
			}
		}
		return deptList;
	}

	@Override
	public HRMSBaseResponse<String> saveKpiTimeLine(TimeLineRequestVO request) throws HRMSException {
		log.info("Inside saveKpiTimeLine method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.SAVE_KPI_TIME_LINE,
				loggedInEmployee.getCandidate().getEmployee(), loggedInEmployee.getCandidate().getEmployee());
		kraHelper.validationKpiTimeLine(request);
		KpiTimeline timeLine = null;

		if (!HRMSHelper.isNullOrEmpty(request.getId())) {
			timeLine = kpiTimelinedao.findByIdAndIsActive(request.getId(), IHRMSConstants.isActive);
			timeLine.setUpdatedBy(loggedInEmployee.getId().toString());
			timeLine.setUpdatedDate(new Date());

			response.setResponseCode(1200);
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1791));

//			log.info("Kpi TimeLine Update Successfully");

		} else {
			timeLine = new KpiTimeline();
			timeLine.setCreatedBy(loggedInEmployee.getId().toString());
			timeLine.setCreatedDate(new Date());

			response.setResponseCode(1200);
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1789));
		}

		KraTransformUtil.transformToKpiTimeline(request, loggedInEmployee, timeLine);

		kpiTimelinedao.save(timeLine);

		log.info("Kpi TimeLine Added Successfully");
		return response;
	}

	/*** API to get KPI timeline by id ***/

	@Override
	public HRMSBaseResponse<TimeLineRequestVO> getKpiTimelineById(Long id) throws HRMSException {
		HRMSBaseResponse<TimeLineRequestVO> baseresponse = new HRMSBaseResponse<>();
		log.info("Inside getKpiTimelineById method");
		TimeLineRequestVO responsevo = new TimeLineRequestVO();
		if (!HRMSHelper.isNullOrEmpty(id)) {
			KpiTimeline timelineList = kpiTimelinedao.findById(id)
					.orElseThrow(() -> new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201)));
			if (!HRMSHelper.isNullOrEmpty(timelineList)) {
				responsevo = transformUtil.convertDashboardTimelineVo(timelineList);
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
		baseresponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseresponse.setResponseBody(responsevo);
		baseresponse.setResponseCode(1200);
		return baseresponse;
	}

	@Override
	public HRMSBaseResponse<String> deleteKpiTimeline(TimeLineRequestVO request) throws HRMSException {
		log.info("Inside delete kpi Timeline method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		validateRolePermission();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_KPI_TIME_LINE,
				loggedInEmployee.getCandidate().getEmployee(), loggedInEmployee.getCandidate().getEmployee());
		deleteTimeline(request, loggedInEmpId);
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1790));
		log.info("Inside delete kpi Timeline method");
		return response;
	}

	private void deleteTimeline(TimeLineRequestVO request, Long loggedInEmpId) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(request.getId())) {
			KpiTimeline timelineList = kpiTimelinedao.findById(request.getId())
					.orElseThrow(() -> new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201)));
			timelineList.setIsActive(IHRMSConstants.isNotActive);
			timelineList.setUpdatedBy(loggedInEmpId.toString());
			timelineList.setUpdatedDate(new Date());
			kpiTimelinedao.save(timelineList);
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}
	}

	@Override
	public HRMSBaseResponse<TimeLineResponseVO> getKpiTimelineList(Long yearId, Long kraCycleId, Pageable pageable)
			throws HRMSException {
		HRMSBaseResponse<TimeLineResponseVO> baseresponse = new HRMSBaseResponse<>();
		log.info("Inside getKraList method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		List<KpiTimeline> timelineList;
		long totalRecords = 0;

		if (!HRMSHelper.isNullOrEmpty(yearId) && !HRMSHelper.isNullOrEmpty(kraCycleId)) {
			timelineList = kpiTimelinedao.findByIsActiveAndCycleIdAndYearIdOrderByIdDesc(IHRMSConstants.isActive,
					kraCycleId, yearId, pageable);
			totalRecords = kpiTimelinedao.countByIsActiveAndCycleIdAndYearIdOrderByIdDesc(IHRMSConstants.isActive,
					kraCycleId, yearId);
		} else if (!HRMSHelper.isNullOrEmpty(yearId)) {
			timelineList = kpiTimelinedao.findByIsActiveAndYearIdOrderByIdDesc(IHRMSConstants.isActive, yearId,
					pageable);
			totalRecords = kpiTimelinedao.countByIsActiveAndYearId(IHRMSConstants.isActive, yearId);
		} else if (!HRMSHelper.isNullOrEmpty(kraCycleId)) {
			timelineList = kpiTimelinedao.findByIsActiveAndCycleIdOrderByIdDesc(IHRMSConstants.isActive, kraCycleId,
					pageable);
			totalRecords = kpiTimelinedao.countByIsActiveAndCycleId(IHRMSConstants.isActive, kraCycleId);
		} else {
			timelineList = kpiTimelinedao.findByIsActiveOrderByIdDesc(IHRMSConstants.isActive, pageable);
			totalRecords = kpiTimelinedao.countByIsActive(IHRMSConstants.isActive);
		}

		List<TimeLineRequestVO> timelineRequests = new ArrayList<>();

		try {
			if (!HRMSHelper.isNullOrEmpty(timelineList)) {
				timelineRequests = transformUtil.convertToTimeline(timelineList);
			}
		} catch (Exception e) {
			log.error("Error while converting KPI timelines: ", e);
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1792));
		}

		TimeLineResponseVO responsevo = new TimeLineResponseVO();
		responsevo.setTimelines(timelineRequests);

		baseresponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseresponse.setResponseBody(responsevo);
		baseresponse.setTotalRecord(totalRecords);
		baseresponse.setResponseCode(1200);
		return baseresponse;
	}

	@Override
	public HRMSBaseResponse<DepartmentEmpListVo> getDeptpartmentEmpList() throws HRMSException {
		HRMSBaseResponse<DepartmentEmpListVo> baseResponse = new HRMSBaseResponse<>();
		log.info("Inside getDeptpartmentEmpList method");
		validateRolePermission();
		List<MasterDepartment> deptList = masterDepartmentDao.findByIsActive(IHRMSConstants.isActive);
		List<PmsDepartmentVo> departmentList = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(deptList)) {
			setDepartmentEmpListVo(deptList, departmentList);
		}

		DepartmentEmpListVo responseVo = new DepartmentEmpListVo();
		responseVo.setDepartments(departmentList);
		baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseResponse.setResponseBody(responseVo);
		baseResponse.setResponseCode(1200);
		return baseResponse;
	}

	private void setDepartmentEmpListVo(List<MasterDepartment> deptList, List<PmsDepartmentVo> departmentList) {
		for (MasterDepartment dept : deptList) {
			PmsDepartmentVo deptListVo = new PmsDepartmentVo();
			deptListVo.setDepartmentId(dept.getId());
			deptListVo.setDepartmentName(dept.getDepartmentName());
			List<EmployeeDepartment> empLists = employeeDepartmentDAO.findByDepartmentId(dept.getId(),
					IHRMSConstants.isActive);
			List<EmpListVo> employeeListVo = new ArrayList<>();
			if (!HRMSHelper.isNullOrEmpty(empLists)) {
				for (EmployeeDepartment empList : empLists) {
					if (!HRMSHelper.isNullOrEmpty(empList.getEmployee())) {
						EmpListVo empVo = new EmpListVo();
						empVo.setEmpId(empList.getEmployee().getId());
						empVo.setEmpName(empList.getEmployee().getCandidate().getFirstName() + " "
								+ empList.getEmployee().getCandidate().getLastName());
						employeeListVo.add(empVo);
					}
				}
			}
			deptListVo.setEmployees(employeeListVo);
			departmentList.add(deptListVo);
		}
	}

	@Override
	public HRMSBaseResponse<String> submitDelegation(DelegationRequestVo request) throws HRMSException {
		log.info("Inside submitDelegation method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		validateRolePermission();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.SUBMIT_DELEGATION, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());
		kraHelper.submitDelegationValidations(request);
		long empCount = setDelegationMapping(request, loggedInEmpId);
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1796) + " " + empCount);

		if (request.getSkippedEmpIds() != null && !request.getSkippedEmpIds().isEmpty()) {
			response.setResponseMessage(response.getResponseMessage()
					+ ". Skipped because existing manager is asignee for employee IDs: "
					+ request.getSkippedEmpIds().stream().map(String::valueOf).collect(Collectors.joining(", ")));
		}

		log.info("Exit from submitDelegation method");
		return response;
	}

	private long setDelegationMapping(DelegationRequestVo request, Long loggedInEmpId) throws HRMSException {
		KraCycle cycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
		long empCount = 0;

		if (HRMSHelper.isNullOrEmpty(cycle)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1627));
		}

		if (!IHRMSConstants.KPI_SUBMISSION_TYPE_ID.equals(cycle.getCycleTypeId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1627));
		}

		setDelegatorRole(request);
		List<String> skippedEmpIds = new ArrayList<>();

		for (EmpListVo empVo : request.getAssignFor()) {
			try {
				Employee employeeFor = employeeDAO.findActiveEmployeeById(empVo.getEmpId(), IHRMSConstants.isActive);

				if (employeeFor == null || employeeFor.getId() == null) {
					skippedEmpIds.add(employeeFor.getEmployeeCode());
					log.warn("Skipping delegation: No reporting manager found for empId {}", empVo.getEmpId());
					continue;
				}

				EmployeeReportingManager reportingManager = employeeFor.getEmployeeReportingManager();
				if (reportingManager == null || reportingManager.getEmployee() == null) {
					skippedEmpIds.add(employeeFor.getEmployeeCode());
					log.warn("Skipping delegation: Reporting manager or employee object is null for empId {}",
							empVo.getEmpId());
					continue;
				}

				Long managerId = reportingManager.getReporingManager().getId();
				if (Objects.equals(managerId, request.getAssigneeId())) {
					skippedEmpIds.add(employeeFor.getEmployeeCode());
					log.warn("Skipping delegation: Assignee ID {} is manager of employee ID {}",
							request.getAssigneeId(), empVo.getEmpId());
					continue;
				}

				DelegationMapping delegation = delegationMappingDAO.findByDelegationForAndIsActive(empVo.getEmpId(),
						IHRMSConstants.isActive);
				if (delegation == null) {
					delegation = new DelegationMapping();
				}

				delegation.setCreatedBy(loggedInEmpId.toString());
				delegation.setCreatedDate(new Date());
				delegation.setDelegationBy(loggedInEmpId);
				delegation.setDelegationFor(empVo.getEmpId());
				delegation.setDelegationTo(request.getAssigneeId());
				delegation.setIsActive(IHRMSConstants.isActive);
				delegation.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
				delegation.setCycleId(cycle.getId());

				delegationMappingDAO.save(delegation);
				empCount++;
			} catch (Exception ex) {
				skippedEmpIds.add(empVo.getEmployeeCode());
				log.error("Error while processing delegation for empId {}: {}", empVo.getEmpId(), ex.getMessage(), ex);
			}
		}

		if (!skippedEmpIds.isEmpty()) {
			request.setSkippedEmpIds(skippedEmpIds);
		}

		return empCount;
	}

	private void setDelegatorRole(DelegationRequestVo request) throws HRMSException {
		Employee delegatedEmployee = employeeDAO.findActiveEmployeeById(request.getAssigneeId(),
				IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(delegatedEmployee)) {
			Set<LoginEntityType> rolelist = delegatedEmployee.getCandidate().getLoginEntity().getLoginEntityTypes();
			List<String> roleNames = rolelist.stream().map(LoginEntityType::getLoginEntityTypeName)
					.collect(Collectors.toList());
			if (!HRMSHelper.isNullOrEmpty(rolelist)) {
				if (!HRMSHelper.isRolePresent(roleNames, ERole.DELEGATOR.name())) {
					LoginEntity delegatorLoginEntity = loginDAO
							.findById(delegatedEmployee.getCandidate().getLoginEntity().getId())
							.orElseThrow(() -> new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201)));
					LoginEntityType loginEntityType = loginEntityTypeDAO.findByRoleName(ERole.DELEGATOR.name());
					if (HRMSHelper.isNullOrEmpty(loginEntityType)) {
						throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
					}
					delegatorLoginEntity.getLoginEntityTypes().add(loginEntityType);
					delegatorLoginEntity.setLoginEntityTypes(delegatorLoginEntity.getLoginEntityTypes());
					loginDAO.save(delegatorLoginEntity);
				}
			}
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	@Override
	public HRMSBaseResponse<DelegationMappingResponseVO> getDelegationMappingList(Pageable pageable, String searchText)
			throws HRMSException {
		HRMSBaseResponse<DelegationMappingResponseVO> baseResponse = new HRMSBaseResponse<>();
		log.info("Inside getDelegationMappingList method");
		validateRolePermission();

		long totalRecords = 0;

		List<DelegationMapping> delMapList;

		if (HRMSHelper.isNullOrEmpty(searchText)) {
			delMapList = delegationMappingDAO.findByIsActive(IHRMSConstants.isActive, pageable);
			totalRecords = delegationMappingDAO.countByIsActive(IHRMSConstants.isActive);
		} else {
			delMapList = delegationMappingDAO.searchByIsActiveAndText(IHRMSConstants.isActive, searchText, pageable);
			totalRecords = delegationMappingDAO.countSearchByIsActiveAndText(IHRMSConstants.isActive, searchText);
		}

		List<DelegationMappingVO> delegationMappingVOList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(delMapList)) {
			setDelegationMappingListVo(delMapList, delegationMappingVOList);
		}

//		List<DelegationMapping> delMapList = delegationMappingDAO.findByIsActive(IHRMSConstants.isActive, pageable);
//		totalRecords = delegationMappingDAO.countByIsActive(IHRMSConstants.isActive);
//		List<DelegationMappingVO> delegationMappingVOList = new ArrayList<>();
//
//		if (!HRMSHelper.isNullOrEmpty(delMapList)) {
//			setDelegationMappingListVo(delMapList, delegationMappingVOList);
//		}

		DelegationMappingResponseVO responseVo = new DelegationMappingResponseVO();
		responseVo.setDelegationlist(delegationMappingVOList);

		baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseResponse.setResponseBody(responseVo);
		baseResponse.setTotalRecord(totalRecords);
		baseResponse.setResponseCode(1200);

		return baseResponse;
	}

	private void setDelegationMappingListVo(List<DelegationMapping> delMapList,
			List<DelegationMappingVO> delegationMappingVOList) {
		for (DelegationMapping entity : delMapList) {
			DelegationMappingVO vo = new DelegationMappingVO();
			vo.setId(entity.getId());
			vo.setDelegationForId(entity.getDelegationFor());
			vo.setDelegationToId(entity.getDelegationTo());

			String delegationForName = null;
			if (!HRMSHelper.isNullOrEmpty(entity.getDelegationFor())) {
//				Employee delegationForEmployee = employeeDAO.findActiveEmployeeById(entity.getDelegationFor(),
//						IHRMSConstants.isActive);
				Employee delegationForEmployee = employeeDAO.getById(entity.getDelegationFor());
				if (!HRMSHelper.isNullOrEmpty(delegationForEmployee)
						&& !HRMSHelper.isNullOrEmpty(delegationForEmployee.getCandidate())) {
					delegationForName = delegationForEmployee.getCandidate().getFirstName() + " "
							+ delegationForEmployee.getCandidate().getLastName();
				}
			}
			vo.setDelegationFor(delegationForName);

			String delegationToName = null;
			if (!HRMSHelper.isNullOrEmpty(entity.getDelegationTo())) {
//				Employee delegationToEmployee = employeeDAO.findActiveEmployeeById(entity.getDelegationTo(),
//						IHRMSConstants.isActive);

				Employee delegationToEmployee = employeeDAO.getById(entity.getDelegationTo());
				if (!HRMSHelper.isNullOrEmpty(delegationToEmployee)
						&& !HRMSHelper.isNullOrEmpty(delegationToEmployee.getCandidate())) {
					delegationToName = delegationToEmployee.getCandidate().getFirstName() + " "
							+ delegationToEmployee.getCandidate().getLastName();
				}
			}

			vo.setDelegationTo(delegationToName);

			String delegationByName = null;
			if (!HRMSHelper.isNullOrEmpty(entity.getDelegationBy())) {
				Employee delegationByEmployee = employeeDAO.getById(entity.getDelegationBy());
				if (!HRMSHelper.isNullOrEmpty(delegationByEmployee)
						&& !HRMSHelper.isNullOrEmpty(delegationByEmployee.getCandidate())) {
					delegationByName = delegationByEmployee.getCandidate().getFirstName() + " "
							+ delegationByEmployee.getCandidate().getLastName();
				}
			}
			vo.setDelegationBy(entity.getDelegationBy());
			vo.setDelegationByName(delegationByName);
			String cycleName = null;

			if (!HRMSHelper.isNullOrEmpty(entity.getCycleId())) {
				KraCycle cycle = kraCycleDAO.getById(entity.getCycleId());
				if (!HRMSHelper.isNullOrEmpty(cycle)) {
					cycleName = cycle.getCycleName();
				}
			}
			vo.setCycleId(entity.getCycleId());
			vo.setCycleName(cycleName);
			delegationMappingVOList.add(vo);
		}
	}

	@Override
	public HRMSBaseResponse<List<KraResponseVO>> getDelegateList(KraListRequestVo request, Pageable pageable)
			throws HRMSException {
		HRMSBaseResponse<List<KraResponseVO>> response = new HRMSBaseResponse<List<KraResponseVO>>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.DELEGATOR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		validateRequest(request);
		List<Integer> mangerIds = new ArrayList<>();
		validatedDataByManagerRolewise(request, loggedInEmpId, loggedInEmployee, mangerIds);

		KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
		KraCycle cycle = kraCycleDAO.findByStatus(status);
		String cycleNames = cycle.getCycleName();
		if (HRMSHelper.isNullOrEmpty(cycle)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
		}
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<KraDetailsReport> query = builder.createQuery(KraDetailsReport.class);
		Root<KraDetailsReport> root = query.from(KraDetailsReport.class);
		List<KraResponseVO> kradetailsResponseList = new ArrayList<>();
		List<Predicate> predicates = new ArrayList<>();

		if (cycleNames.equalsIgnoreCase(IHRMSConstants.KPI_SUBMISSION)) {
			predicates.add(builder.equal(root.get("cycleName"), IHRMSConstants.KPI_SUBMISSION));
		}

		validateKeywordForSerach(request, builder, root, predicates);

		if (ObjectUtils.isEmpty(mangerIds)) {
			response.setResponseBody(Collections.emptyList());
			response.setTotalRecord(0);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			return response;
		}

		// If manager IDs exist, add them to the query
		List<Predicate> locationPredicates = mangerIds.stream()
				.map(locationId -> builder.equal(root.get("employeeId"), locationId)).collect(Collectors.toList());

		Predicate locationPredicate = builder.or(locationPredicates.toArray(new Predicate[0]));
		predicates.add(locationPredicate);

		query.where(predicates.toArray(new Predicate[0]));

		// pagination
		int pageNumber = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		int startRecord = pageNumber * pageSize;
		int totalRecord = em.createQuery(query.select(root)).getResultList().size();

		List<KraDetailsReport> filterkradetailsReport = em.createQuery(query.select(root)).setFirstResult(startRecord)
				.setMaxResults(pageSize).getResultList();

		kradetailsResponseList = convertToKRAResponse(request, loggedInEmpId, filterkradetailsReport);
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<KraDetailsReport> countRoot = countQuery.from(KraDetailsReport.class);
		countQuery.select(builder.count(countRoot));
		countQuery.where(predicates.toArray(new Predicate[] {}));
		response.setResponseBody(kradetailsResponseList);
		response.setTotalRecord(totalRecord);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		return response;

	}

	private void validatedDataByManagerRolewise(KraListRequestVo request, Long loggedInEmpId, Employee loggedInEmployee,
			List<Integer> mangerIds) {
		if (request.getRoleName().equalsIgnoreCase(ERole.DELEGATOR.name())) {
			List<DelegationMapping> reportingManager = delegationMappingDAO.findByDelegationToAndIsActive(loggedInEmpId,
					ERecordStatus.Y.name());
			for (DelegationMapping manager : reportingManager) {
				mangerIds.add(manager.getDelegationFor().intValue());
			}
		}

	}

	@Override
	public HRMSBaseResponse<String> deleteDelegationMapping(DeleteDelegationMappingVO request) throws HRMSException {
		log.info("Inside delete kpi Timeline method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<String>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
//		validateRolePermission();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_DELEGATION_MAPPING,
				loggedInEmployee.getCandidate().getEmployee(), loggedInEmployee.getCandidate().getEmployee());
		deleteDelegation(request, loggedInEmpId);

		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1795));
		log.info("Delegation Mapping deleted successfully");
		return response;

	}

	private void deleteDelegation(DeleteDelegationMappingVO request, Long loggedInEmpId) throws HRMSException {

		List<Long> ids = request.getId();

		if (HRMSHelper.isNullOrEmpty(ids)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}

		for (Long id : ids) {
			DelegationMapping mapping = delegationMappingDAO.findByIdAndIsActive(id, IHRMSConstants.isActive);

			if (mapping == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}

			mapping.setIsActive(IHRMSConstants.isNotActive);
			mapping.setUpdatedBy(String.valueOf(loggedInEmpId));
			mapping.setUpdatedDate(new Date());

			delegationMappingDAO.save(mapping);
		}
	}

	/**
	 * Author Kailas B
	 * 
	 */

	@Override
	public HRMSBaseResponse<List<DeptwiseCountResponseVO>> getDeptwiseCountSummary() throws HRMSException {
		log.info("Fetching department-wise KPI summary...");

		HRMSBaseResponse<List<DeptwiseCountResponseVO>> response = new HRMSBaseResponse<>();

		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
			log.warn("Access denied: User does not have HR role.");
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		List<DeptwiseCount> resultList = deptwiseCountDAO.findAll();
		if (resultList == null || resultList.isEmpty()) {
			log.warn("No department-wise KPI data found.");
			response.setResponseCode(1201);
			response.setResponseBody(Collections.emptyList());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1201));
			return response;
		}

		List<DeptwiseCountResponseVO> voList = resultList.stream().map(entity -> {
			DeptwiseCountResponseVO vo = new DeptwiseCountResponseVO();
			vo.setDepartmentName(Optional.ofNullable(entity.getDepartmentName()).orElse(""));
			vo.setCycleName(Optional.ofNullable(entity.getCycleName()).orElse(""));
			vo.setTotalCount(Optional.ofNullable(entity.getTotalCount()).orElse(0));
			vo.setSubmittedCount(Optional.ofNullable(entity.getSubmittedCount()).orElse(0));
			vo.setSubmittedPercentage(Optional.ofNullable(entity.getSubmittedPercentage()).orElse(0.0));
			vo.setPendingCount(Optional.ofNullable(entity.getPendingCount()).orElse(0));
			vo.setPendingPercentage(Optional.ofNullable(entity.getPendingPercentage()).orElse(0.0));
			return vo;
		}).collect(Collectors.toList());

		response.setResponseCode(1200);
		response.setResponseBody(voList);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Department-wise KPI summary fetched successfully. Count: {}", voList.size());
		return response;
	}

	@Override
	public byte[] downloadDelegationMappingReport(String searchText) throws HRMSException {

		int pageSize = Integer.MAX_VALUE;
		Pageable pageable = PageRequest.of(0, pageSize);

		HRMSBaseResponse<DelegationMappingResponseVO> response = getDelegationMappingList(pageable, searchText);
		List<DelegationMappingVO> delegationList = response.getResponseBody().getDelegationlist();

		if (HRMSHelper.isNullOrEmpty(delegationList)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Delegation_Mapping_Report");
			sheet.setDisplayGridlines(false);
			CreationHelper helper = workbook.getCreationHelper();

			String[] headers = { "Delegation ID", "Delegation For", "Delegation To", "Delegation By", "Cycle Name" };
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
			titleCell.setCellValue("Delegation Mapping Report");

			CellStyle titleStyle = workbook.createCellStyle();
			Font titleFont = workbook.createFont();
			titleFont.setBold(true);
			titleFont.setFontHeightInPoints((short) 14);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.RIGHT);
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
			for (DelegationMappingVO data : delegationList) {
				Row row = sheet.createRow(rowNum++);
				String[] values = { String.valueOf(data.getId() != null ? data.getId() : 0L),
						data.getDelegationFor() != null ? data.getDelegationFor() : "",
						data.getDelegationTo() != null ? data.getDelegationTo() : "",
						data.getDelegationByName() != null ? data.getDelegationByName() : "",
						data.getCycleName() != null ? data.getCycleName() : "" };

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
	public byte[] downloadDelegateListReport(KraListRequestVo request) throws HRMSException {
		int maxPageSize = Integer.MAX_VALUE;
		Pageable pageable = PageRequest.of(0, maxPageSize);

		List<KraResponseVO> reportData = getDelegateList(request, pageable).getResponseBody();

		if (HRMSHelper.isNullOrEmpty(reportData)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Delegate_List_Report");
			sheet.setDisplayGridlines(false);
			CreationHelper helper = workbook.getCreationHelper();
			String[] headers = { "Status", "Employee Code", "Employee Name", "Department", "Grade", "KRA Year",
					"Cycle Name" };
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
			titleCell.setCellValue("Delegate List Report");

			CellStyle titleStyle = workbook.createCellStyle();
			Font titleFont = workbook.createFont();
			titleFont.setBold(true);
			titleFont.setFontHeightInPoints((short) 14);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			titleCell.setCellStyle(titleStyle);

			Cell dateCell = titleRow.createCell(totalCols - 1);
			dateCell.setCellValue("Date: " + LocalDate.now().toString());

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
			for (KraResponseVO data : reportData) {
				Row row = sheet.createRow(rowNum++);
				String[] values = { data.getStatus(), data.getEmployeeCode(), data.getEmpName(), data.getDepartment(),
						data.getGrade(), data.getKraYear(), data.getCycleName() };

				for (int i = 0; i < values.length; i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(values[i] != null ? values[i] : "");
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
	public HRMSBaseResponse<String> activateKraCycle(KraCycleRequestVo request) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		KraHelper.validateActivateCycleRequest(request);

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ACTIVATE_KRA_CYCLE,
				loggedInEmployee.getCandidate().getEmployee(), loggedInEmployee.getCandidate().getEmployee());

		processKraCycleActivation(request, orgId, employeeId, response);
//		response.setResponseCode(1200);
//		response.setResponseMessage((ResponseCode.getResponseCodeMap().get(1872)));
		return response;
	}

	private void processKraCycleActivation(KraCycleRequestVo request, Long orgId, Long employeeId,  HRMSBaseResponse<String> response)
			throws HRMSException {

		KraCycle targetCycle = kraCycleDAO.findById(request.getCycleId())
				.orElseThrow(() -> new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201)));

		KraYear selectedYear = krayeardao.getById(request.getYearId());
		if (HRMSHelper.isNullOrEmpty(selectedYear)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		if (!targetCycle.getYear().getId().equals(request.getYearId())) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		KraYear activeYear = krayeardao.findByIsActive(IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(activeYear) || !activeYear.getId().equals(request.getYearId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1880));
		}

		KraCycleStatus openStatus = kraStatusDao.findByStatus(IHRMSConstants.OPEN);
		KraCycleStatus closedStatus = kraStatusDao.findByStatus(IHRMSConstants.CLOSED);

		if (HRMSHelper.isNullOrEmpty(openStatus) || HRMSHelper.isNullOrEmpty(closedStatus)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1873));
		}

//		if (ERecordStatus.Y.name().equalsIgnoreCase(targetCycle.getIsActive()) && targetCycle.getStatus() != null
//				&& targetCycle.getStatus().getId().equals(openStatus.getId())) {
//			throw new HRMSException(1874, ResponseCode.getResponseCodeMap().get(1874));
//		}

//		List<KraCycle> yearCycles = kraCycleDAO.findByYearIdAndOrgId(request.getYearId(), orgId);
//
//		for (KraCycle cycle : yearCycles) {
//			if (cycle.getId().equals(request.getCycleId())) {
//				cycle.setIsActive(ERecordStatus.Y.name());
//				cycle.setStatus(openStatus);
//			} else {
//				cycle.setIsActive(ERecordStatus.N.name());
//				cycle.setStatus(closedStatus);
//			}
//			cycle.setUpdatedBy(employeeId.toString());
//			cycle.setUpdatedDate(new Date());
//			kraCycleDAO.save(cycle);
//		}
		
		List<KraCycle> allOrgCycles = kraCycleDAO.findByOrgId(orgId);

		boolean isCurrentlyActive = ERecordStatus.Y.name().equalsIgnoreCase(targetCycle.getIsActive())
				&& targetCycle.getStatus() != null && targetCycle.getStatus().getId().equals(openStatus.getId());

		if (isCurrentlyActive) {
			for (KraCycle cycle : allOrgCycles) {
				cycle.setIsActive(ERecordStatus.N.name());
				cycle.setStatus(closedStatus);
				cycle.setUpdatedBy(employeeId.toString());
				cycle.setUpdatedDate(new Date());
				kraCycleDAO.save(cycle);
			}
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1925));
		} else {
			for (KraCycle cycle : allOrgCycles) {
				if (cycle.getId().equals(request.getCycleId())) {
					cycle.setIsActive(ERecordStatus.Y.name());
					cycle.setStatus(openStatus);
				} else {
					cycle.setIsActive(ERecordStatus.N.name());
					cycle.setStatus(closedStatus);
				}
				cycle.setUpdatedBy(employeeId.toString());
				cycle.setUpdatedDate(new Date());
				kraCycleDAO.save(cycle);
			}
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1872));
		}
	}

	@Override
	public HRMSBaseResponse<String> preDeleteCheckForCycle(Long cycleId) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		if (HRMSHelper.isNullOrEmpty(cycleId) || cycleId <= 0) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1501));
		}

		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		KraCycle cycle = kraCycleDAO.findById(cycleId)
				.orElseThrow(() -> new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201)));

		boolean isMapped = false;

		if (!HRMSHelper.isNullOrEmpty(kraDao.findByCycleId(cycleId))) {
			isMapped = true;
		}

		if (!HRMSHelper.isNullOrEmpty(kraDetailsDao.findByCycleId(cycleId))) {
			isMapped = true;
		}

		if (!HRMSHelper.isNullOrEmpty(kraWfDao.findByCycleId(cycleId))) {
			isMapped = true;
		}

		if (isMapped) {
			response.setResponseCode(1501);
			response.setResponseMessage((ResponseCode.getResponseCodeMap().get(1879)));
		} else {
			response.setResponseCode(1200);
			response.setResponseMessage((ResponseCode.getResponseCodeMap().get(1876)));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<KraCycleListResponseVO> getKraCycleList(long yearId, String searchText, Pageable pageable)
			throws HRMSException {

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (HRMSHelper.isNullOrEmpty(yearId)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1877));
		}

		KraYear year = krayeardao.getById(yearId);
		if (HRMSHelper.isNullOrEmpty(year)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1877));
		}

		String keyword = HRMSHelper.isNullOrEmpty(searchText) ? "" : searchText.trim();

		List<KraCycle> cycleList = kraCycleDAO.searchByYearIdAndText(yearId, orgId, keyword, pageable);
		long totalRecords = kraCycleDAO.countByYearIdAndText(yearId, orgId, keyword);

		if (HRMSHelper.isNullOrEmpty(cycleList)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<KraCycleResponseVo> responseList = new ArrayList<>();
		for (KraCycle cycle : cycleList) {
			KraCycleResponseVo vo = new KraCycleResponseVo();
			vo.setCycleId(cycle.getId());
			vo.setCycleName(cycle.getCycleName());
			vo.setDescription(cycle.getDescription());
			vo.setStartDate(HRMSDateUtil.format(cycle.getStartDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			vo.setEndDate(HRMSDateUtil.format(cycle.getEndDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			vo.setCycleTypeId(cycle.getCycleTypeId());
			vo.setIsActive(cycle.getIsActive());
			vo.setYear(year.getLabel());
			vo.setYearId(year.getId());
			responseList.add(vo);
		}

		KraCycleListResponseVO responseVo = new KraCycleListResponseVO();
		responseVo.setKraCycleList(responseList);

		HRMSBaseResponse<KraCycleListResponseVO> baseResponse = new HRMSBaseResponse<>();
		baseResponse.setResponseCode(1200);
		baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		baseResponse.setResponseBody(responseVo);
		baseResponse.setTotalRecord(totalRecords);

		return baseResponse;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public HRMSBaseResponse<String> deleteCycleById(Long cycleId) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		if (HRMSHelper.isNullOrEmpty(cycleId) || cycleId <= 0) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1501));
		}

		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_KRA_CYCLE, loggedInEmployee.getCandidate().getEmployee(),
				loggedInEmployee.getCandidate().getEmployee());

		KraCycle cycle = kraCycleDAO.findById(cycleId)
				.orElseThrow(() -> new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201)));

		boolean isMapped = false;

		if (!HRMSHelper.isNullOrEmpty(kraDao.findByCycleId(cycleId))
				|| !HRMSHelper.isNullOrEmpty(kraDetailsDao.findByCycleId(cycleId))
				|| !HRMSHelper.isNullOrEmpty(kraWfDao.findByCycleId(cycleId))) {
			isMapped = true;
		}

		if (isMapped) {
			response.setResponseCode(1200);
			response.setResponseMessage((ResponseCode.getResponseCodeMap().get(1879)));
		} else {
			calenderDao.deleteByCycleId(cycleId);
			kraCycleDAO.deleteById(cycleId);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1878));
		}

		return response;
	}

	@Override
	public HRMSBaseResponse<DonutCountResponse> pmsDonutDashboard(PmsDashboardRequestVo request) throws HRMSException {
		HRMSBaseResponse<DonutCountResponse> baseResponse = new HRMSBaseResponse<>();
		DonutCountResponse responseVo = new DonutCountResponse();

		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		List<DonutDashboardResponseVo> donutDataList = new ArrayList<>();

		List<DonutDatum> donutList = new ArrayList<>();

		try {
			Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
			KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());

			if (kraCycle == null || kraYear == null) {
				baseResponse.setResponseCode(1627);
				baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1627));

				baseResponse.setResponseBody(responseVo);
				return baseResponse;
			}

			Long cycleId = request.getCycleId() != null ? request.getCycleId() : kraCycle.getId();
			String year = request.getYear() != null ? request.getYear() : kraYear.getYear();

			String roleName = request.getRoleName();

			List<KraCycleCalender> calendarList = calenderDao.findByIsActive(ERecordStatus.Y.name());

			List<KraCycleCalender> filteredCalendarList;
			if (calendarList.size() == 1) {
				filteredCalendarList = calendarList;
			} else {
				MasterRole masterRole = roleDao.findByRoleName(roleName);
				long roleId = masterRole != null ? masterRole.getId() : -1;

				filteredCalendarList = calendarList.stream()
						.filter(c -> c.getRoleId() != null && c.getRoleId().getId() == roleId)
						.collect(Collectors.toList());

				if (filteredCalendarList.isEmpty() && calendarList.size() == 1) {
					filteredCalendarList = calendarList;
				}
			}

			switch (roleName.toUpperCase()) {

			case IHRMSConstants.HR:
				List<CycleResult> hrResults = kraDao.countByHRForDonut(cycleId, year);
				donutDataList = kpiHelper.populateDonutData(hrResults, calendarList, kraCycle);

				donutList.addAll(kpiHelper.getDonutDataFromVo(donutDataList, cycleId, IHRMSConstants.HR));
				break;

			case IHRMSConstants.HOD:
				List<Long> deptIds = hodToDepartmentMap.getDepartmentByEmployeeAndIsActive(loggedInEmpId,
						ERecordStatus.Y.name());
				if (deptIds == null || deptIds.isEmpty()) {
					log.warn("No departments found for HOD: {}", loggedInEmpId);
				} else {
					List<CycleResult> hodResults = kraDao.countByDepartmentIdInForDonut(year, cycleId, deptIds);
					donutDataList = kpiHelper.populateDonutData(hodResults, filteredCalendarList, kraCycle);

					donutList.addAll(kpiHelper.getDonutDataFromVo(donutDataList, cycleId, IHRMSConstants.HOD));
				}
				break;

			case IHRMSConstants.MANAGER:
				List<Long> empIds = kraDao.findByEmployeeByManager(loggedInEmpId, ERecordStatus.Y.name());
				List<CycleResult> mgrResults = kraDao.countByEmployeeIdInForDonut(empIds, year, cycleId);
				donutDataList = kpiHelper.populateDonutData(mgrResults, filteredCalendarList, kraCycle);

				donutList.addAll(kpiHelper.getDonutDataFromVo(donutDataList, cycleId, IHRMSConstants.MANAGER));
				break;

			case IHRMSConstants.EMPLOYEE:
				List<CycleResult> empResults = kraDao.getEmployeeCycleResult(year, cycleId, loggedInEmpId);
				donutDataList = kpiHelper.populateDonutDataForEmployee(empResults, filteredCalendarList, kraCycle);

				donutList.addAll(kpiHelper.getDonutDataFromVo(donutDataList, cycleId, IHRMSConstants.EMPLOYEE));
				break;

			default:
				log.warn("Unsupported roleName: {}", roleName);
				break;
			}

			responseVo.setDonutData(donutList);
			baseResponse.setResponseBody(responseVo);
			baseResponse.setResponseCode(1200);
			baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		} catch (Exception e) {
			log.error("Error in PmsDonutDashboard: ", e);
			baseResponse.setResponseCode(1500);
			baseResponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		return baseResponse;
	}

	@Override
	public List<KraCycleTypeVO> getKraCycleTypeList() throws HRMSException {

		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		log.info("Inside getKraCycleTypeList Method");

		List<KraCycleType> kraCycleTypes = kraCycleTypeDAO.findAllKraCycleTypesByOrgIdAndIsActive(orgId,
				IHRMSConstants.isActive);
		List<KraCycleTypeVO> voList = new ArrayList<>();

		for (KraCycleType type : kraCycleTypes) {
			KraCycleTypeVO vo = new KraCycleTypeVO();
			vo.setId(type.getId());
			vo.setCycleTypeName(type.getCycleTypeName());
			vo.setCycleTypeDescription(type.getCycleTypeDescription());
			vo.setIsActive(type.getIsActive());
			vo.setOrgId(type.getOrgId());
			voList.add(vo);
		}

		log.info("Exit from getKraCycleTypeList Method");
		return voList;
	}

	@Override
	public HRMSBaseResponse<List<HodCycleFinalRatingResponseVO>> getLastYearFinalRating(Long employeeId)
			throws HRMSException {
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		int lastYear = Year.now().getValue() - 1;
		String yearStr = String.valueOf(lastYear);

		KraYear year = krayeardao.findByYear(yearStr);

		if (year == null || year.getId() == null) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1877));
		}

		List<HodCycleFinalRating> ratings = hodCycleFinalRatingDao.findLastCycleRatingForYear(loggedInEmpId,
				year.getId());

		if (HRMSHelper.isNullOrEmpty(ratings)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1884));
		}

		List<HodCycleFinalRatingResponseVO> responseList = new ArrayList<>();

		for (HodCycleFinalRating rating : ratings) {
			HodCycleFinalRatingResponseVO vo = new HodCycleFinalRatingResponseVO();

			vo.setId(rating.getId());

			if (!HRMSHelper.isNullOrEmpty(rating.getEmployee())
					&& !HRMSHelper.isNullOrEmpty(rating.getEmployee().getCandidate())) {
				var candidate = rating.getEmployee().getCandidate();
				vo.setEmployeeName(candidate.getFirstName() + " "
						+ (candidate.getLastName() != null ? candidate.getLastName() : ""));
			}

			if (!HRMSHelper.isNullOrEmpty(rating.getMcMemberId())
					&& !HRMSHelper.isNullOrEmpty(rating.getMcMemberId().getCandidate())) {
				var candidate = rating.getMcMemberId().getCandidate();
				vo.setMcMemberName(candidate.getFirstName() + " "
						+ (candidate.getLastName() != null ? candidate.getLastName() : ""));
			}

			if (!HRMSHelper.isNullOrEmpty(rating.getCycleId())) {
				vo.setCycleName(rating.getCycleId().getCycleName());
				if (rating.getCycleId().getYear() != null) {
					vo.setYear(String.valueOf(rating.getCycleId().getYear().getYear()));
				}
			}

			if (!HRMSHelper.isNullOrEmpty(rating.getScore())) {
				vo.setScore(rating.getScore().getLabel());
			}

			vo.setComment(rating.getComment());

			if (!HRMSHelper.isNullOrEmpty(rating.getKra())) {
				vo.setKraId(rating.getKra().getId());
			}

			responseList.add(vo);
		}

		HRMSBaseResponse<List<HodCycleFinalRatingResponseVO>> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(responseList);

		return response;
	}

	public static InputStream getInputStreamFromPath(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IOException("File not found at path: " + filePath);
		}
		return new FileInputStream(file);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public HRMSBaseResponse<String> deleteById(Long id) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		if (HRMSHelper.isNullOrEmpty(id) || id <= 0) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1501));
		}

		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Category deletedCategory = categoryDAO.findByIdAndIsActive(id, ERecordStatus.N.name());
		if (!HRMSHelper.isNullOrEmpty(deletedCategory)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1908));
		}
		Category category = categoryDAO.findByIdAndIsActive(id, ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(category)) {

			category.setIsActive(ERecordStatus.N.name());
			categoryDAO.save(category);
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1896));
		response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));

		return response;
	}

	@Override
	public HRMSBaseResponse<String> addHodToDepartmentMap(HodToDepartmentMapVO request) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee targetEmployee = employeeDAO.findActiveEmployeeById(request.getEmployeeId(), ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(targetEmployee)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1918));
		}

		KraHelper.validateHodToDepartmentMapRequest(request);

		if (HRMSHelper.isNullOrEmpty(request.getId())
				&& Boolean.TRUE.equals(hodToDepartmentMap.existsByEmployeeIdAndDepartmentIdAndIsActive(
						request.getEmployeeId(), request.getDepartmentId(), IHRMSConstants.isActive))) {
			throw new HRMSException(1546, ResponseCode.getResponseCodeMap().get(1917));
		}

//		HodToDepartmentMap employeeMapping = hodToDepartmentMap
//				.findSingleByEmployeeIdAndIsActive(request.getEmployeeId(), IHRMSConstants.isActive);
//
//		if (!HRMSHelper.isNullOrEmpty(employeeMapping)) {
//			if (HRMSHelper.isNullOrEmpty(request.getId())) {
//				throw new HRMSException(1548, ResponseCode.getResponseCodeMap().get(1921));
//			} else if (!employeeMapping.getId().equals(request.getId())) {
//				throw new HRMSException(1548, ResponseCode.getResponseCodeMap().get(1921));
//			}
//		}

		HodToDepartmentMap departmentMapping = hodToDepartmentMap
				.findByDepartmentIdAndIsActive(request.getDepartmentId(), IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(departmentMapping)) {
			if (HRMSHelper.isNullOrEmpty(request.getId())) {
				throw new HRMSException(1547, ResponseCode.getResponseCodeMap().get(1920));
			} else if (!departmentMapping.getId().equals(request.getId())) {
				throw new HRMSException(1547, ResponseCode.getResponseCodeMap().get(1920));
			}
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.ADD_HOD_TO_DEPARTMENT_MAPPING,
				loggedInEmployee.getCandidate().getEmployee(), loggedInEmployee.getCandidate().getEmployee());

		return createOrUpdateHodToDepartmentMap(request, response, employeeId, orgId);
	}

	private HRMSBaseResponse<String> createOrUpdateHodToDepartmentMap(HodToDepartmentMapVO request,
			HRMSBaseResponse<String> response, Long employeeId, Long orgId) throws HRMSException {

		HodToDepartmentMap entity;

		if (!HRMSHelper.isNullOrEmpty(request.getId())) {

			entity = hodToDepartmentMap.findByIdAndIsActive(request.getId(), IHRMSConstants.isActive);
			if (entity == null) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}

			entity.setUpdatedBy(employeeId.toString());
			entity.setUpdatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1915));

		} else {

			entity = new HodToDepartmentMap();
			entity.setCreatedBy(employeeId.toString());
			entity.setCreatedDate(new Date());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1916));
		}

		entity.setEmployeeId(request.getEmployeeId());
		entity.setDepartmentId(request.getDepartmentId());
		entity.setBranchId(request.getBranchId());
		entity.setDivisionId(request.getDivisionId());
		entity.setIsActive(IHRMSConstants.isActive);
		entity.setOrgId(orgId);

		hodToDepartmentMap.save(entity);

		response.setResponseCode(1200);
		return response;
	}

	@Override
	public HRMSBaseResponse<String> deleteHodToDepartmentMap(Long id) throws HRMSException {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		auditLogService.setActionHeader(IHRMSConstants.DELETE_HOD_TO_DEPARTMENT_MAPPING,
				loggedInEmployee.getCandidate().getEmployee(), loggedInEmployee.getCandidate().getEmployee());

		HodToDepartmentMap entity = hodToDepartmentMap.findByIdAndIsActive(id, IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(entity)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		entity.setIsActive(IHRMSConstants.isNotActive);
		entity.setUpdatedBy(employeeId.toString());
		entity.setUpdatedDate(new Date());

		hodToDepartmentMap.save(entity);

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1919));
		return response;
	}

	@Override
	public HRMSBaseResponse<HodToDepartmentMapListResponseVO> getAllHodToDepartmentMap(Pageable pageable)
			throws HRMSException {

		log.info("Inside getAllHodToDepartmentMap Method");

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		long totalRecords = hodToDepartmentMap.countActiveByOrgId(orgId, IHRMSConstants.isActive);

		List<HodToDepartmentMap> hodToDepartmentMapList = hodToDepartmentMap.findByActiveAndOrgId(orgId,
				IHRMSConstants.isActive, pageable);

		if (HRMSHelper.isNullOrEmpty(hodToDepartmentMapList)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		List<HodToDepartmentMapVO> voList = new ArrayList<>();
		for (HodToDepartmentMap entity : hodToDepartmentMapList) {
			HodToDepartmentMapVO vo = new HodToDepartmentMapVO();

			vo.setId(entity.getId());
			vo.setIsActive(entity.getIsActive());

			Employee emp = employeeDAO.findById(entity.getEmployeeId()).orElse(null);
			if (emp != null && emp.getCandidate() != null) {
				vo.setEmployeeName(Stream
						.of(emp.getCandidate().getFirstName(), emp.getCandidate().getMiddleName(),
								emp.getCandidate().getLastName())
						.filter(str -> !HRMSHelper.isNullOrEmpty(str)).collect(Collectors.joining(" ")));
			}
			vo.setEmployeeId(entity.getEmployeeId());

			MasterDepartment dept = masterDepartmentDao.findById(entity.getDepartmentId()).orElse(null);
			if (dept != null) {
				vo.setDepartmentName(dept.getDepartmentName());
			}
			vo.setDepartmentId(entity.getDepartmentId());

			MasterDivision division = divisionDAO.findById(entity.getDivisionId()).orElse(null);
			if (division != null) {
				vo.setDivisionName(division.getDivisionName());
			}
			vo.setDivisionId(entity.getDivisionId());

			MasterBranch branch = branchDAO.findById(entity.getBranchId()).orElse(null);
			if (branch != null) {
				vo.setBranchName(branch.getBranchName());
			}
			vo.setBranchId(entity.getBranchId());

			voList.add(vo);
		}

		HodToDepartmentMapListResponseVO responseVO = new HodToDepartmentMapListResponseVO();
		responseVO.setHodToDepartmentList(voList);

		HRMSBaseResponse<HodToDepartmentMapListResponseVO> response = new HRMSBaseResponse<>();
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(responseVO);
		response.setTotalRecord(totalRecords);

		log.info("Exit from getAllHodToDepartmentMap Method");
		return response;
	}

	@Override
	public HRMSBaseResponse<List<HrToDepartmentVO>> getHrToDepartment() throws HRMSException {

		log.info("Inside getHrToDepartment Method");

		HRMSBaseResponse<List<HrToDepartmentVO>> response = new HRMSBaseResponse<>();

		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		List<HrToDepartment> mappings = hrtodepartment.findByIsActive(ERecordStatus.Y.name());

		if (HRMSHelper.isNullOrEmpty(mappings)) {
			response.setResponseCode(1201);
			response.setResponseBody(Collections.emptyList());
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1201));
			return response;
		}

		List<HrToDepartmentVO> responseList = new ArrayList<>();

		for (HrToDepartment map : mappings) {
			HrToDepartmentVO vo = new HrToDepartmentVO();
			vo.setId(map.getId());

			// Department details
			MasterDepartment department = masterDepartmentDao.findByIdAndIsActive(
					map.getDepartmentId(), ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(department)) {
				vo.setDepartmentId(map.getDepartmentId());
				vo.setDepartmentName(department.getDepartmentName());
			} else {
				vo.setDepartmentId(map.getDepartmentId());
			}

			// Employee details with null check
			if (map.getEmployeeId() != null) {
				Employee emp = employeeDAO.findActiveEmployeeById(map.getEmployeeId(), ERecordStatus.Y.name());
				if (!HRMSHelper.isNullOrEmpty(emp)) {
					vo.setEmployeeId(map.getEmployeeId());
					vo.setEmployeeName(emp.getCandidate().getFirstName() + " " + emp.getCandidate().getLastName());
				} else {
					vo.setEmployeeId(map.getEmployeeId());
					vo.setEmployeeName(null);
				}
			} else {
				vo.setEmployeeId(null);
				vo.setEmployeeName(null);
			}

			vo.setIsActive(map.getIsActive());
			responseList.add(vo);
		}

		response.setResponseCode(1200);
		response.setResponseBody(responseList);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exiting getHrToDepartment Method");
		return response;
	}

	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<String> submitKpiForm(Long kraId) throws Exception {
	    log.info("Inside submitKpiForm (rolewise submit with auth check) method");

	    HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
	    Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
	    List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

	    Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
	    KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());
	    if (HRMSHelper.isNullOrEmpty(kraYear)) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
	    }

	    KraCycleStatus openStatus = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
	    KraCycle kraCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
	    MasterRole managerRole = roleDao.findByRoleNameOrgIdIsActive(
	            ERole.MANAGER.name(), loggedInEmployee.getOrgId(), IHRMSConstants.isActive);

	    // Validate cycle is open
	    isCycleOpen(openStatus, managerRole, kraCycle.getId());

	    Kra kra = kraDao.findByIdAndIsActiveAndKraYear(kraId, ERecordStatus.Y.name(), kraYear);
	    if (HRMSHelper.isNullOrEmpty(kra)) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
	    }

	    String pendingWith = kra.getKraWf().getPendingWith();
	    log.info("Workflow pending with: {}", pendingWith);

	    if (ERole.EMPLOYEE.name().equalsIgnoreCase(pendingWith)) {
	        handleEmployeeSubmit(kra, loggedInEmployee, roles);
	    } else if (ERole.MANAGER.name().equalsIgnoreCase(pendingWith)) {
	        handleManagerSubmit(kra, loggedInEmployee, roles);
	    } else {
	        throw new HRMSException(1500, "Invalid workflow state for submission.");
	    }

	    response.setResponseCode(1200);
	    response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1217));
	    log.info("Exit from submitKpiForm method");
	    return response;
	}

	@Async
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void sendKpiFeedbackForAiSummarization(Long kraId) throws Exception {
		log.info("Inside sendKpiFeedbackForAiSummarization (rolewise submit to AI) method");

	    KraYear kraYear = kraYearDao.findByIsActive(ERecordStatus.Y.name());

	    Kra kra = kraDao.findByIdAndIsActiveAndKraYear(kraId, ERecordStatus.Y.name(), kraYear);
	    if (HRMSHelper.isNullOrEmpty(kra)) {
	        log.error("Invalid Kra ID");
	    }

	    String pendingWith = kra.getKraWf().getPendingWith();
	    log.info("Workflow pending with: {}", pendingWith);

		if (ERole.MANAGER.name().equalsIgnoreCase(pendingWith) || 
				ERole.DIVISIONHEAD.name().equalsIgnoreCase(pendingWith) || 
				ERole.HOD.name().equalsIgnoreCase(pendingWith) || 
				ERole.HR.name().equalsIgnoreCase(pendingWith)) {
			sendKpiFeedbackForAiSummarizationLogic(kra.getId(), pendingWith);
		} else {
			log.error("Invalid workflow state for AI analysis submission. Pending with : " + pendingWith);
	    }

	    log.info("Exit from sendKpiFeedbackForAiSummarization method");
	}

	private void sendKpiFeedbackForAiSummarizationLogic(Long kraid, String pendingWith) {

		if (pmsAiMsEnabled.equalsIgnoreCase("N")) {
			log.info("PMS AIMS is disaled. Skipping call the AIMS API.");
			return;
		}

		log.info("PMS AIMS is enabled. Proceeding to call the API.");

		NewKraResponse kraDetails = new NewKraResponse();
		try {
			kraDetails = getKraByIdForAiAnalysis(kraid);
		} catch (Exception e) {
			log.error("Error while fetching KRA Details for submiting to AI");
			log.error(e.getMessage(), e);
			return;
		}

		// Call pmsAiMS API and get response
		try {
			ObjectMapper mapper = new ObjectMapper();
			String aiRequestJson = mapper.writeValueAsString(kraDetails);
			log.info("The Payload for PMS AIMS : \n\n" + aiRequestJson + "\n\n\n.");

			String uriString = pmsAiMsBaseUrl;

			if (ERole.MANAGER.name().equalsIgnoreCase(pendingWith)) {
				uriString += pmsAiMsEndPointEmployeeFeedbackAnalysis;
			} else if (ERole.DIVISIONHEAD.name().equalsIgnoreCase(pendingWith)) {
				uriString += pmsAiMsEndPointManagerFeedbackAnalysis;
			} else if (ERole.HOD.name().equalsIgnoreCase(pendingWith)) {
				uriString += pmsAiMsEndPointDivisionHeadFeedbackAnalysis;
			} else if (ERole.HR.name().equalsIgnoreCase(pendingWith)) {
				uriString += pmsAiMsEndPointHodFeedbackAnalysis;
			}

			URI uri = new URI(uriString);
			RestTemplate restTemplate = new RestTemplate();

			// Create headers with API key
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("api-key", pmsAiMsApiKey);
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

			// Create HTTP entity with body and headers
			HttpEntity<NewKraResponse> requestEntity = new HttpEntity<>(kraDetails, headers);

			// Make POST request
			log.info("Calling PMS AIMS API : Request sent ...");
			ResponseEntity<String> apiResult = restTemplate.postForEntity(uri, requestEntity, String.class);
			log.info("Calling PMS AIMS API : Response received ...");

			// check if request is successful
			if (apiResult.getStatusCode() != HttpStatus.OK) {
				log.error("Error in getSelfFeedbackDraft():E1: HTTP Status Code = " + apiResult.getStatusCode());
				throw new HRMSException(1500, "Error while calling AI service : " + apiResult.getStatusCode());
			} else {
				String apiResponseBody = apiResult.getBody();
				log.info("The response from PMS AIMS : \n\n" + apiResponseBody + "\n\n\n.");

				AiMsEmployeeFeedbackAnalysisResponseVO pmsAiMsResponse = mapper.readerFor(AiMsEmployeeFeedbackAnalysisResponseVO.class).readValue(apiResponseBody);
				if (!HRMSHelper.isNullOrEmpty(pmsAiMsResponse)) {
					// Update ai feedback in DB
					if(!HRMSHelper.isNullOrEmpty(pmsAiMsResponse.getObjectivesFeedbackList())) {
						List<KraDetails> kraDetailsList = new ArrayList<>();
						for (AiMsEmployeeFeedbackAnalysisResponseVO.ObjectiveFeedbackVo objVo : pmsAiMsResponse.getObjectivesFeedbackList()) {
							KraDetails kraDetail = kraDetailsDao.findByIdAndIsActive(ERecordStatus.Y.name(), objVo.getId());
							if (!HRMSHelper.isNullOrEmpty(kraDetail)) {
								objVo.setId(null); // to avoid redundancy
								String aiFeedbackJson = mapper.writeValueAsString(objVo);
								kraDetail.setRmAiComment(aiFeedbackJson);
								kraDetailsList.add(kraDetail);
							}
						}
						if (!kraDetailsList.isEmpty()) {
							kraDetailsDao.saveAll(kraDetailsList);
						}
						pmsAiMsResponse.setObjectivesFeedbackList(null); // to avoid redundancy
					}

					Kra kra = kraDao.findByIdAndIsActive(kraid, ERecordStatus.Y.name());
					pmsAiMsResponse.setKraId(null); // to avoid redundancy
					String aiOverallFeedbackJson = mapper.writeValueAsString(pmsAiMsResponse);
					if (ERole.MANAGER.name().equalsIgnoreCase(pendingWith)) {
						kra.setRmAiComment(aiOverallFeedbackJson);
					} else if (ERole.DIVISIONHEAD.name().equalsIgnoreCase(pendingWith)) {
						kra.setFdhAiComment(aiOverallFeedbackJson);
					} else if (ERole.HOD.name().equalsIgnoreCase(pendingWith)) {
						kra.setHodAiComment(aiOverallFeedbackJson);
					} else if (ERole.HR.name().equalsIgnoreCase(pendingWith)) {
						kra.setHrAiComment(aiOverallFeedbackJson);
					}
					kraDao.save(kra);

				} else {
					log.error("Error in getSelfFeedbackDraft():E2: No Response body from AIMS : "
							+ apiResult.getStatusCode());
					throw new HRMSException(1500,
							"Error while calling AI service : No Response body : " + apiResult.getStatusCode());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in getSelfFeedbackDraft():E1:", e);
			//TODO handle exception in background job
		}
	}
	
	private NewKraResponse getKraByIdForAiAnalysis(Long kraid) throws HRMSException {
		NewKraResponse response = new NewKraResponse();

		Kra empkra = kraDao.findByIdAndIsActive(kraid, IHRMSConstants.isActive);
		if (empkra == null) {
			throw new HRMSException("KRA not found or inactive for ID: " + kraid);
		}

		List<VisionKraDetailsView> visionKraList = visionKraDao.findByKraIdAndIsActive(kraid, IHRMSConstants.isActive);
		
		Employee empKpi = empkra.getEmployee();
		if (empKpi == null) {
			throw new HRMSException("Employee linked to KRA is null for KRA ID: " + kraid);
		}

		List<KraDetails> kraDetailsList = kraDetailsDao.findByIsActiveAndKra(IHRMSConstants.isActive, empkra);
		boolean isWeightageValid = KraTransformUtil.isOjectiveWeightageValid(kraDetailsList);
		Float totalWeight = kraDetailsList.stream().map(KraDetails::getObjectiveWeightage).filter(Objects::nonNull)
				.reduce(0.0f, Float::sum);

		if (visionKraList == null || visionKraList.isEmpty()) {
			throw new HRMSException(1521, "KRA details not found for the given ID");
		}

		List<KraCategoryVo> categories = visionKraList.stream()
				.collect(Collectors.groupingBy(VisionKraDetailsView::getCategory)).entrySet().stream()
				.map(entry -> {
					String categoryName = entry.getKey();
					List<VisionKraDetailsView> categoryDetails = entry.getValue();

					KraCategoryVo categoryResponseVo = new KraCategoryVo();
					categoryResponseVo.setCategoryId(categoryDetails.stream()
							.map(VisionKraDetailsView::getCategoryId).sorted().findFirst().orElse(null));
					categoryResponseVo.setName(categoryName);
					categoryResponseVo.setKraCycleId(categoryDetails.get(0).getKraCycleId());
					categoryResponseVo.setKraId(kraid);
					categoryResponseVo.setCategoryweight(categoryDetails.get(0).getCategoryWeight() + "%");

					// Process and sort subcategories by subCategoryId in ascending order
					List<KraSubcategoryVo> subcategoryList = categoryDetails.stream()
							.collect(Collectors.groupingBy(VisionKraDetailsView::getSubCategory)).entrySet()
							.stream().map(subcategoryEntry -> {
								String subcategoryName = subcategoryEntry.getKey();
								List<VisionKraDetailsView> subcategoryDetails = subcategoryEntry.getValue();

								// Sort subcategory details by subCategoryId in ascending order
								KraSubcategoryVo subcategoryResponseVo = new KraSubcategoryVo();
								subcategoryResponseVo.setSubcategoryId(
										subcategoryDetails.stream().map(VisionKraDetailsView::getSubCategoryId)
												.sorted().findFirst().orElse(null));
								subcategoryResponseVo.setName(subcategoryName);

								// Process and sort objectives by objective ID in ascending order
								List<KraObjectiveVo> kraObjectiveList = subcategoryDetails.stream()
										.sorted(Comparator.comparing(VisionKraDetailsView::getId)).map(detail -> {
											KraObjectiveVo kraObjectiveVo = new KraObjectiveVo();
											kraObjectiveVo.setId(detail.getId());
											kraObjectiveVo.setName(detail.getObjectives());
											kraObjectiveVo.setObjectiveweight(
													String.valueOf(detail.getObjectiveWeight()));
											kraObjectiveVo.setMetric(detail.getMetric());
											kraObjectiveVo.setIsEdit(detail.getIs_edit());
											kraObjectiveVo.setIsColor(detail.getIs_color());
											kraObjectiveVo.setHcdComment(detail.getHcdComment());
											if (!HRMSHelper.isNullOrEmpty(detail.getHcdComment())) {
												kraObjectiveVo.setViewComment(IHRMSConstants.isActive);
											} else {
												kraObjectiveVo.setViewComment(IHRMSConstants.isNotActive);
											}

											// ===== Self Rating (with conditional translation) =====
											if (detail.getSelfRating() != null) {
												Optional<KraRating> rating = kraRatingDAO
														.findById(detail.getSelfRating());
												if (rating.isPresent()) {
													SelfRatingResponseVo responseVo = new SelfRatingResponseVo();
													responseVo.setId(rating.get().getId());
													String srLabel = rating.get().getLabel();

													// translate only for Half/Mid year cycles
													KraCycle activeCycleForTranslation = kraCycleDAO
															.findByKraId(detail.getKraCycleId());
													if (shouldTranslateForCycle(activeCycleForTranslation)) {
														srLabel = kpiHelper.translateRatingLabel(srLabel);
													}

													responseVo.setLabel(srLabel);
													responseVo.setValue(rating.get().getValue());
													responseVo.setCategoryId(rating.get().getCategory().getId());
													kraObjectiveVo.setSelfrating(responseVo);
												}
											}

											// (Manager rating left as-is per your requirement)
											if (detail.getManagerRating() != null) {
												Optional<KraRating> managerrating = kraRatingDAO
														.findById(detail.getManagerRating());

												if (managerrating.isPresent()) {
													ManagerRatingResponseVo managerRatingResponse = new ManagerRatingResponseVo();
													managerRatingResponse.setId(managerrating.get().getId());

													String mgrLabel = managerrating.get().getLabel();
													// translate only for Half/Mid year cycles
													KraCycle activeCycleForTranslation = kraCycleDAO
															.findByKraId(detail.getKraCycleId());
													if (shouldTranslateForCycle(activeCycleForTranslation)) {
														mgrLabel = kpiHelper.translateRatingLabel(mgrLabel);
													}

													managerRatingResponse.setLabel(mgrLabel);
													managerRatingResponse.setCategoryId(
															managerrating.get().getCategory().getId());
													managerRatingResponse.setValue(managerrating.get().getValue());
													kraObjectiveVo.setManagerrating(managerRatingResponse);
												}
											}

											kraObjectiveVo.setSelfqualitativeassessment(
													detail.getSelfQaulitativeAssisment());
											kraObjectiveVo.setManagerqaulitativeassisment(
													detail.getManagerQaulitativeAssisment());
											return kraObjectiveVo;
										}).collect(Collectors.toList());

								subcategoryResponseVo.setObjectives(kraObjectiveList);
								return subcategoryResponseVo;
							}).sorted(Comparator.comparing(KraSubcategoryVo::getSubcategoryId))
							.collect(Collectors.toList());

					categoryResponseVo.setSubcategory(subcategoryList);
					return categoryResponseVo;
				}).collect(Collectors.toList());

		// Set categories into the response body

		DelegationMapping delegatelist = delegationMappingDAO.findByDelegationForAndIsActive(empkra.getEmployeeId(),
				IHRMSConstants.isActive);
		DelegationMapping isDelegatedto = delegationMappingDAO.findByDelegationToAndDelegationForAndIsActive(
				empkra.getEmployeeId(), empkra.getEmployeeId(), ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(isDelegatedto)) {
			response.setIsDelegated(ERecordStatus.N.name());
		} else if (!HRMSHelper.isNullOrEmpty(delegatelist)) {
			response.setIsDelegated(ERecordStatus.Y.name());
		} else {
			response.setIsDelegated(ERecordStatus.N.name());
		}

		Kra kra = kraDao.findByIdAndIsActive(kraid, ERecordStatus.Y.name());
		KraWf krafWf = kraWfDao.findByKraAndIsActive(kra, ERecordStatus.Y.name());
		List<KraDetails> details = kraDetailsDao.findByKraAndIsActiveAndIsColorOrderByIdAsc(kra,
				ERecordStatus.Y.name(), ERecordStatus.Y.name());
		if (HRMSHelper.isNullOrEmpty(details)) {
			response.setIsManagerObjectives(ERecordStatus.N.name());
		} else {
			response.setIsManagerObjectives(ERecordStatus.Y.name());
		}
		KraCycle cycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
		if (kra == null) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		if (cycle.getCycleTypeId().equals(IHRMSConstants.KPI_SUBMISSION_TYPE_ID)) {
			if (((krafWf.getStatus().equalsIgnoreCase(EKraStatus.INPROGRESS.name())
					&& krafWf.getPendingWith().equalsIgnoreCase(ERole.MANAGER.name()))
				|| (krafWf.getStatus().equalsIgnoreCase(EKraStatus.CORRECTION.name())
					&& krafWf.getPendingWith().equalsIgnoreCase(ERole.MANAGER.name())))
				&& isWeightageValid) {

				response.setIsObjectiveSubmit(ERecordStatus.Y.name());

			} else {
				response.setIsObjectiveSubmit(ERecordStatus.N.name());
			}

		} else if (krafWf.getPendingWith().equalsIgnoreCase(ERole.MANAGER.name())
				&& krafWf.getStatus().equalsIgnoreCase(EKraStatus.INPROCESS.name())) {

			response.setIsObjectiveSubmit(ERecordStatus.Y.name());

		} else {
			response.setIsObjectiveSubmit(ERecordStatus.N.name());
		}

		if (((krafWf.getStatus().equalsIgnoreCase(EKraStatus.SUBMITTED.name())
				&& krafWf.getPendingWith().equalsIgnoreCase(ERole.DIVISIONHEAD.name())))) {

			response.setIsFunctionHeadSubmit(ERecordStatus.Y.name());
		} else {
			response.setIsFunctionHeadSubmit(ERecordStatus.N.name());
		}

		response.setCycleName(cycle.getDescription());
		response.setCycleType(cycle.getCycleTypeId());

		if ((krafWf.getStatus().equalsIgnoreCase(EKraStatus.INPROGRESS.name())
				&& krafWf.getPendingWith().equalsIgnoreCase(ERole.HR.name()))) {

			response.setIsHcdApproved(ERecordStatus.Y.name());
		} else {
			response.setIsHcdApproved(ERecordStatus.N.name());
		}

		KraCycle activeCycle = kraCycleDAO.findByIsActive(ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(activeCycle)) {
			response.setActiveCycleName(activeCycle.getCycleName());
			response.setActiveCycleType(activeCycle.getCycleTypeId());
		}
		// final boolean translateForHalfOrMid = shouldTranslateForCycle(activeCycle);

		KraCycle formCycle = empkra.getCycleId();
		if (!HRMSHelper.isNullOrEmpty(formCycle)) {
			KraCycle formCycleName = kraCycleDAO.findByKraCycle(formCycle.getId());
			response.setFormCycleName(formCycleName.getCycleName());
			response.setFormCycleType(formCycleName.getCycleTypeId());
		}

		if ((krafWf.getStatus().equalsIgnoreCase(EKraStatus.APPROVED.name())
				&& krafWf.getPendingWith().equalsIgnoreCase(ERole.MANAGER.name()))) {

			response.setIsmanagerApproved(ERecordStatus.Y.name());
		} else {
			response.setIsmanagerApproved(ERecordStatus.N.name());
		}

		if ((krafWf.getStatus().equalsIgnoreCase(EKraStatus.INACTION.name())
				&& krafWf.getPendingWith().equalsIgnoreCase(ERole.EMPLOYEE.name()))) {

			response.setIsEmployeeAccept(ERecordStatus.Y.name());
		} else {
			response.setIsEmployeeAccept(ERecordStatus.N.name());
		}

		if ((krafWf.getStatus().equalsIgnoreCase(EKraStatus.CORRECTION.name())
				&& krafWf.getPendingWith().equalsIgnoreCase(ERole.MANAGER.name()))) {
			response.setIsHcdCorrect(ERecordStatus.Y.name());

		} else {
			response.setIsHcdCorrect(ERecordStatus.N.name());
		}

		String formattedWeight = String.format("%.2f", totalWeight);
		response.setTotalWeight(formattedWeight);

		if (EKraStatus.INCOMPLETE.name().equals(krafWf.getStatus())
				&& EKraStatus.EMPLOYEE.name().equals(krafWf.getPendingWith())) {
			response.setIsEmployeeSubmit(ERecordStatus.N.name());
		} else {
			response.setIsEmployeeSubmit(ERecordStatus.Y.name());
		}
		if ((EKraStatus.INPROCESS.name().equals(krafWf.getStatus())
				&& EKraStatus.MANAGER.name().equals(krafWf.getPendingWith()))
				|| (EKraStatus.INCOMPLETE.name().equals(krafWf.getStatus())
						&& EKraStatus.EMPLOYEE.name().equals(krafWf.getPendingWith()))
				|| (EKraStatus.REJECTED.name().equals(krafWf.getStatus())
						&& EKraStatus.MANAGER.name().equals(krafWf.getPendingWith()))) {

			response.setIsManagerSubmit(ERecordStatus.N.name());
		} else {
			response.setIsManagerSubmit(ERecordStatus.Y.name());
		}

		// ===== HOD Final Rating (with conditional translation) =====
		if (!EKraStatus.COMPLETED.name().equals(krafWf.getStatus())) {
			response.setFinalRating(null);
		} else {
			HodCycleFinalRating hodrating = hodcycleDao.findByIsActiveAndKra(ERecordStatus.Y.name(), kra);
			if (hodrating != null) {
				String finalRatingLabel = (hodrating.getScore() != null) ? hodrating.getScore().getLabel()
						: kra.getFinalRating();

				KraCycle currentCycle = kra.getCycleId();
				if (shouldTranslateForCycle(currentCycle) && finalRatingLabel != null) {
					finalRatingLabel = kpiHelper.translateRatingLabel(finalRatingLabel);
				}
				response.setFinalRating(finalRatingLabel);
			} else {
				String finalRatingLabel = kra.getFinalRating();
				KraCycle currentCycle = kra.getCycleId();
				if (shouldTranslateForCycle(currentCycle) && finalRatingLabel != null) {
					finalRatingLabel = kpiHelper.translateRatingLabel(finalRatingLabel);
				}
				response.setFinalRating(finalRatingLabel);
			}
		}

		// ===== Self Rating (with conditional translation if needed) =====
		//if (HRMSHelper.isRolePresent(role, ERole.EMPLOYEE.name())) {
			Kra totalSelfRating = kraDao.findByIdAndIsActive(kra.getId(), ERecordStatus.Y.name());
			if (totalSelfRating != null) {
				String totalSelfRatingLabel = totalSelfRating.getTotalSelfRating();

				if (!HRMSHelper.isNullOrEmpty(totalSelfRatingLabel)) {
					KraCycle currentCycle = kra.getCycleId();
					if (shouldTranslateForCycle(currentCycle) && totalSelfRatingLabel != null) {
						totalSelfRatingLabel = kpiHelper.translateRatingLabel(totalSelfRatingLabel);
					}
					response.setTotalSelfRating(totalSelfRatingLabel);
				}
			} else {
				String totalSelfRatingLabel = kra.getTotalSelfRating();
				KraCycle currentCycle = kra.getCycleId();
				if (shouldTranslateForCycle(currentCycle) && totalSelfRatingLabel != null) {
					totalSelfRatingLabel = kpiHelper.translateRatingLabel(totalSelfRatingLabel);
				}
				response.setTotalSelfRating(totalSelfRatingLabel);
			}
		//}

		// ===== Manager Rating (with conditional translation) =====
		//if (HRMSHelper.isRolePresent(role, ERole.EMPLOYEE.name())) {
			Kra managerRating = kraDao.findByIdAndIsActive(kra.getId(), ERecordStatus.Y.name());
			if (managerRating != null) {
				String totalManagerRatingLabel = managerRating.getFinalRating();

				if (!HRMSHelper.isNullOrEmpty(totalManagerRatingLabel)) {
					KraCycle currentCycle = kra.getCycleId();
					if (shouldTranslateForCycle(currentCycle) && totalManagerRatingLabel != null) {
						totalManagerRatingLabel = kpiHelper.translateRatingLabel(totalManagerRatingLabel);
					}
					response.setTotalManagerRating(totalManagerRatingLabel);
				}
			} else {
				String totalManagerRatingLabel = kra.getFinalRating();
				KraCycle currentCycle = kra.getCycleId();
				if (shouldTranslateForCycle(currentCycle) && totalManagerRatingLabel != null) {
					totalManagerRatingLabel = kpiHelper.translateRatingLabel(totalManagerRatingLabel);
				}
				response.setTotalManagerRating(totalManagerRatingLabel);

			}
		//}

		// ===== Last Year Rating (with conditional translation) =====
		// Long lastCycleId = kraCycleDAO.findKraCycleById(kra.getCycleId().getId());
		// if (lastCycleId != null) {
		// 	KraCycle lastCycle = kraCycleDAO.findById(lastCycleId).orElse(null);
		// 	if (lastCycle != null) {
		// 		HodCycleFinalRating lastYearrating = hodcycleDao.findByEmployeeAndCycleId(kra.getEmployee(),
		// 				lastCycle);
		// 		if (lastYearrating != null && lastYearrating.getScore() != null) {
		// 			String lastYearLabel = lastYearrating.getScore().getLabel();
		// 			if (shouldTranslateForCycle(lastCycle) && lastYearLabel != null) {
		// 				lastYearLabel = kpiHelper.translateRatingLabel(lastYearLabel);
		// 			}
		// 			response.setLastYearRating(lastYearLabel);
		// 		} else {
		// 			Kra lastKra = kraDao.findByEmpidAndCycleId(kra.getEmployee().getId(), lastCycleId);
		// 			if (lastKra != null && lastKra.getFinalRating() != null) {
		// 				String lastYearLabel = lastKra.getFinalRating();
		// 				if (shouldTranslateForCycle(lastCycle) && lastYearLabel != null) {
		// 					lastYearLabel = kpiHelper.translateRatingLabel(lastYearLabel);
		// 				}
		// 				response.setLastYearRating(lastYearLabel);
		// 			}
		// 		}
		// 	}
		// }

		response.setCategory(categories);

		return response;
	}

	private void handleEmployeeSubmit(Kra kra, Employee loggedInEmployee, List<String> roles) throws Exception {
	    Long loggedInEmpId = loggedInEmployee.getId();
	    log.info("Processing Employee submit for KRA ID: {}", kra.getId());

	    // ✅ Authorization check
	    if (!authorizationServiceImpl.isAuthorizedFunctionName("submitKra", roles)) {
	        throw new HRMSException(1500, "Unauthorized to submit KPI as Employee.");
	    }

	    // ✅ Validate owner of KRA
	    if (!loggedInEmpId.equals(kra.getEmployeeId())) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
	    }

	    String status = kra.getKraWf().getStatus();
	    if (EKraStatus.INCOMPLETE.name().equalsIgnoreCase(status)
	            || EKraStatus.REJECTED.name().equalsIgnoreCase(status)) {

	        // Update workflow status
	        addWFStatus(kra, loggedInEmpId, EKraStatus.INPROCESS.name(), ERole.MANAGER.name());

	        // Email alert to employee
	        EmailRequestVO email = new EmailRequestVO();
			email.setEmailAddress(loggedInEmployee.getOfficialEmailId());
			email.setEmailCategory(EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());
			TemplateVO template = new TemplateVO();
			template.setTemplateName(IHRMSConstants.EMPLOYEE_EMAIL);

			List<PlaceHolderVO> empPlaceHolders = new ArrayList<PlaceHolderVO>();
			PlaceHolderVO empPlaceHolderVo = new PlaceHolderVO();
			empPlaceHolderVo.setExpressionVariableName("empName");
			empPlaceHolderVo.setExpressionVariableValue(loggedInEmployee.getCandidate().getFirstName() + " "
					+ loggedInEmployee.getCandidate().getLastName());
			empPlaceHolders.add(empPlaceHolderVo);
			template.setPlaceHolders(empPlaceHolders);
			email.setTemplateVo(template);

			emailService.insertInEmailTxnTable(email);

	        log.info("Employee [{}] submitted KRA [{}] successfully.", loggedInEmpId, kra.getId());

	    } else {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
	    }
	}

	private void handleManagerSubmit(Kra kra, Employee loggedInEmployee, List<String> roles) throws Exception {
	    Long loggedInEmpId = loggedInEmployee.getId();
	    log.info("Processing Manager submit for KRA ID: {}", kra.getId());

	    // ✅ Authorization check
	    if (!authorizationServiceImpl.isAuthorizedFunctionName("submitRmRating", roles)) {
	        throw new HRMSException(1500, "Unauthorized to submit KPI as Manager.");
	    }

	    Long reportingManagerId = reportingManagerDao.findReportingManagerIdByEmployeeId(kra.getEmployeeId());
	    if (!loggedInEmpId.equals(reportingManagerId)) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
	    }

	    String status = kra.getKraWf().getStatus();
	    if (EKraStatus.INPROCESS.name().equalsIgnoreCase(status)
	            || EKraStatus.REJECTED.name().equalsIgnoreCase(status)) {

	        // Update workflow
	        addWFStatus(kra, reportingManagerId, EKraStatus.SUBMITTED.name(), ERole.DIVISIONHEAD.name());

	        // Calculate average & final rating
	        Double average = kraDetailsDao.calculateAverage(kra.getId(), IHRMSConstants.isActive,
	                IHRMSConstants.ZERO_VALUE);
	        if (!HRMSHelper.isNullOrEmpty(average)) {
	            Double trimmedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN).doubleValue();
	            String finalRating = getFinalRatingByCycle(trimmedAverage, kra.getKraYear());
	            if (!HRMSHelper.isNullOrEmpty(finalRating)) {
	                kra.setFinalRating(finalRating);
                    kra.setAvgRmRating(trimmedAverage);
	            }
	        }

	        kraDao.save(kra);
	        sendMailsOnManagerSubmit(loggedInEmployee, kra);

	        log.info("Manager [{}] submitted KRA [{}] successfully.", loggedInEmpId, kra.getId());

	    } else {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
	    }
	}

	@Override
	public HRMSBaseResponse<String> activateYear(KraYearVo request) throws HRMSException {

		log.info("Inside activateYear method");
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isRolePresent(roles, ERole.HR.name()) && !HRMSHelper.isRolePresent(roles, ERole.ADMIN.name())) {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (HRMSHelper.isNullOrEmpty(request) || HRMSHelper.isNullOrEmpty(request.getId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1788)); 
		}

		KraYear targetYear = krayeardao.findById(request.getId())
				.orElseThrow(() -> new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201)));
		
		if (IHRMSConstants.isActive.equalsIgnoreCase(targetYear.getIsActive())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1928));
		}

		KraCycleStatus openStatus = kraStatusDao.findByName(IHRMSConstants.OPEN);
		if (HRMSHelper.isNullOrEmpty(openStatus)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1873));
		}
		List<KraCycle> openCycles = kraCycleDAO.findByStatusAndOrgId(openStatus, orgId);
		if (!HRMSHelper.isNullOrEmpty(openCycles)) {

			boolean hasOpenOtherYear = openCycles.stream()
					.anyMatch(cycle -> !cycle.getYear().getId().equals(request.getId()));

			if (hasOpenOtherYear) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1927));

			}
		}

		List<KraYear> allYears = krayeardao.findAll();
		for (KraYear year : allYears) {
			year.setIsActive(IHRMSConstants.isNotActive);
			year.setUpdatedBy(String.valueOf(loggedInEmpId));
			year.setUpdatedDate(new Date());
			krayeardao.save(year);
		}

		targetYear.setIsActive(IHRMSConstants.isActive);
		targetYear.setUpdatedBy(String.valueOf(loggedInEmpId));
		targetYear.setUpdatedDate(new Date());
		krayeardao.save(targetYear);

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1926));
		log.info("Exit from activateYear method");
		return response;
	}


}
