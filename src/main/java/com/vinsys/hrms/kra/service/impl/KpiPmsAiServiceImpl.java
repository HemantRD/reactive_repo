package com.vinsys.hrms.kra.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IEmployeeDesignationDAO;
import com.vinsys.hrms.dao.IEmployeeDivisionDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSMasterDesignationDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.dao.IHodCycleFinalRatingDao;
import com.vinsys.hrms.kra.dao.IHodToDepartmentMap;
import com.vinsys.hrms.kra.dao.IKraAggregateScoresDao;
import com.vinsys.hrms.kra.dao.IKraCycleDAO;
import com.vinsys.hrms.kra.dao.IKraDao;
import com.vinsys.hrms.kra.dao.IKraDetailsDao;
import com.vinsys.hrms.kra.dao.IKraDetailsReportDAO;
import com.vinsys.hrms.kra.dao.IKraRatingRangeDao;
import com.vinsys.hrms.kra.dao.KraStatusDao;
import com.vinsys.hrms.kra.entity.HodCycleFinalRating;
import com.vinsys.hrms.kra.entity.HodToDepartmentMap;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraAggregateScores;
import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraCycleStatus;
import com.vinsys.hrms.kra.entity.KraDetails;
import com.vinsys.hrms.kra.entity.KraDetailsReport;
import com.vinsys.hrms.kra.entity.KraRatingRange;
import com.vinsys.hrms.kra.service.IKpiPmsAiService;
import com.vinsys.hrms.kra.vo.AggregateCalcVo;
import com.vinsys.hrms.kra.vo.AiMsBackgroundAnalysisRequestInvokerVO;
import com.vinsys.hrms.kra.vo.AiMsDepartmentLevelAnalysisRequestVO;
import com.vinsys.hrms.kra.vo.AiMsDepartmentLevelAnalysisResponseVO;
import com.vinsys.hrms.master.dao.ICategoryDao;
import com.vinsys.hrms.master.dao.IKraRatingDAO;
import com.vinsys.hrms.master.dao.ISubCategoryDao;
import com.vinsys.hrms.master.entity.KraRating;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KpiPmsAiServiceImpl implements IKpiPmsAiService {

    public enum LevelOfAggregation {
        Organisation,
        Department,
        Division
    }

    public enum LevelOfAnalysis {
        ORGANISATION,
        DEPARTMENT
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IHodToDepartmentMap hodToDepartmentMap;

    @Autowired
    IKraDetailsReportDAO kraDetailsReportDAO;

    @Autowired
    IKraDetailsDao kraDetailsDao;

    @Autowired
    IKraAggregateScoresDao kraAggregateScoresDao;

    @Autowired
    IHRMSMasterDepartmentDAO departmentDAO;

    @Autowired
    IHRMSMasterDivisionDAO divisionDAO;

    @Autowired
    IKraDao kraDAO;

    @Autowired
    IHodCycleFinalRatingDao hodCycleFinalRatingDao;

    @Autowired
    KraStatusDao kraStatusDao;

    @Autowired
    IKraCycleDAO kraCycleDAO;

    @Autowired
    IKraRatingRangeDao rangeDao;

    @Autowired
    ICategoryDao categoryDao;

    @Autowired
    ISubCategoryDao subCategoryDao;

    @Autowired
    IEmployeeDesignationDAO employeeDesignationDAO;

    @Autowired
    IHRMSMasterDesignationDAO masterDesignationDAO;

    @Autowired
    IEmployeeDivisionDAO employeeDivisionDAO;

    @Autowired
    IHRMSMasterDivisionDAO masterDivisionDAO;

    @Autowired
    IHRMSEmployeeDAO employeeDAO;

    @Autowired
    IKraRatingDAO ratingDao;

    @Value("${pmsaims.api.key:f3c4e83daf6fe67d21fcfc19f585027d}")
    String pmsAiMsApiKey;

    @Value("${pmsaims.baseurl:http://192.168.33.51:8000}")
    String pmsAiMsBaseUrl;

    @Value("${pmsaims.endpoint.departmentalanalysis:/departmental_Analysis}")
    String pmsAiMsEndPointDepartmentalAnalysis;

    @Override
    public void fetchActiveCycle(AiMsBackgroundAnalysisRequestInvokerVO invokerVO) throws HRMSException {
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

        invokerVO.setCycle(cycle);
    }

    private List<Long> fetchDepartmentsForHod(AiMsBackgroundAnalysisRequestInvokerVO invokerVO) throws Exception {
        List<Long> departmentIds = new ArrayList<>();
        //Fetch department details if HOD login
        if (HRMSHelper.isRolePresent(invokerVO.getRoles(), ERole.HOD.name())) {
            List<HodToDepartmentMap> departmentList = hodToDepartmentMap.findByEmployeeIdAndIsActive(invokerVO.getLoggedInEmployeeId(), ERecordStatus.Y.name());
            for (HodToDepartmentMap depId : departmentList) {
                departmentIds.add(depId.getDepartmentId());
            }

            if (HRMSHelper.isNullOrEmpty(departmentIds)) {
                throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
            }
        } else {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
        }
        return departmentIds;
    }

    //Fetch all departments details if CEO login
    private List<MasterDepartment> fetchAllDepartmentsForCeo(AiMsBackgroundAnalysisRequestInvokerVO invokerVO) throws Exception {
        List<MasterDepartment> departmentList;
        if (HRMSHelper.isRolePresent(invokerVO.getRoles(), ERole.ADMIN.name())) {
            departmentList = departmentDAO.findByIsActive(ERecordStatus.Y.name());
            if (HRMSHelper.isNullOrEmpty(departmentList)) {
                throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
            }
        } else {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
        }
        return departmentList;
    }

    @Override
    public AiMsBackgroundAnalysisRequestInvokerVO validateCeoRole(String role) throws Exception {
        AiMsBackgroundAnalysisRequestInvokerVO invokerVO = new AiMsBackgroundAnalysisRequestInvokerVO();
        invokerVO.setRoles(SecurityFilter.TL_CLAIMS.get().getRoles());
        invokerVO.setLoggedInEmployeeId(SecurityFilter.TL_CLAIMS.get().getEmployeeId());

        if (!HRMSHelper.isRolePresent(invokerVO.getRoles(), role)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }

        if (role.equalsIgnoreCase(ERole.ADMIN.name())) {
            invokerVO.setRole(role);
        } else {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }

        fetchActiveCycle(invokerVO);

        return invokerVO;
    }

    @Override
    public AiMsBackgroundAnalysisRequestInvokerVO validateCeoOrHodRole(String role) throws Exception {
        AiMsBackgroundAnalysisRequestInvokerVO invokerVO = new AiMsBackgroundAnalysisRequestInvokerVO();
        invokerVO.setRoles(SecurityFilter.TL_CLAIMS.get().getRoles());
        invokerVO.setLoggedInEmployeeId(SecurityFilter.TL_CLAIMS.get().getEmployeeId());

        if (!HRMSHelper.isRolePresent(invokerVO.getRoles(), role)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }

        if (role.equalsIgnoreCase(ERole.ADMIN.name()) || role.equalsIgnoreCase(ERole.HOD.name())) {
            invokerVO.setRole(role);
        } else {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }

        fetchActiveCycle(invokerVO);

        return invokerVO;
    }

    @Override
    public void validateIfProcessIsAlreadyInProgress(AiMsBackgroundAnalysisRequestInvokerVO invokerVO, Long deptId) throws HRMSException {
        KraCycle cycle = invokerVO.getCycle();
        List<KraAggregateScores> aggScores;

        if (invokerVO.getLevelOfAnalysis().equalsIgnoreCase(LevelOfAnalysis.DEPARTMENT.name())) {
            aggScores = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregationAndDepartment_Id(cycle.getId(), LevelOfAggregation.Department.name(), deptId);
        } else if (invokerVO.getLevelOfAnalysis().equalsIgnoreCase(LevelOfAnalysis.ORGANISATION.name())) {
            aggScores = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregation(cycle.getId(), LevelOfAggregation.Organisation.name());
        } else {
            aggScores = null;
        }

        if (!HRMSHelper.isNullOrEmpty(aggScores) && aggScores.size() > 0) {
            KraAggregateScores aggScore = aggScores.get(0);
            String processingProgressStatus;

            if (invokerVO.getPurpose().equalsIgnoreCase("CALIBRATION_SUGGESTION")) {
                processingProgressStatus = aggScore.getCalibrationProcessingProgressStatus();
            } else if (invokerVO.getPurpose().equalsIgnoreCase("REVIEW_ASSISTANCE")) {
                processingProgressStatus = aggScore.getReviewProcessingProgressStatus();
            } else {
                processingProgressStatus = null;
            }

            AiMsBackgroundAnalysisRequestInvokerVO.AiMsBackgroundAnalysisProgressStatus statusObj;
            if (!HRMSHelper.isNullOrEmpty(processingProgressStatus)) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    statusObj = mapper.readerFor(AiMsBackgroundAnalysisRequestInvokerVO.AiMsBackgroundAnalysisProgressStatus.class).readValue(processingProgressStatus);
                    if (HRMSHelper.isNullOrEmpty(statusObj)) {
                        log.error("Error in validateIfProcessIsAlreadyInProgress : " + processingProgressStatus);
                        throw new HRMSException(1500, "Error while parsing AI Analysis progress status.");
                    }
                } catch (JsonProcessingException e) {
                    log.error("Error in validateIfProcessIsAlreadyInProgress: ", e);
                    throw new HRMSException(1500, "Error while parsing AI Analysis progress status.");
                }
            } else {
                statusObj = null;
            }

            if (!HRMSHelper.isNullOrEmpty(statusObj) && !statusObj.getStatus().equalsIgnoreCase("Completed")) {
                log.info("AI Analysis process is already in progress for deptId: " + deptId);
                throw new HRMSException(1500, "AI Analysis process is already in progress for the selected " + invokerVO.getLevelOfAnalysis() + ". Please try again later.");
            }
        }
    }

    //@Async
    @Override
    public void calculateDepartmentLevelAggregations(AiMsBackgroundAnalysisRequestInvokerVO invokerVO, Long deptId) throws Exception {
        try {
            log.info("Starting calculateDepartmentLevelAggregations for deptId: " + deptId);
            KraCycle cycle = invokerVO.getCycle();
            log.info("Active Cycle ID: " + cycle.getId());
            List<Long> departmentIds;
            if (HRMSHelper.isNullOrEmpty(deptId)) { // deptId == null
                //Fetch department details if HOD login
                departmentIds = fetchDepartmentsForHod(invokerVO);
            } else {
                departmentIds = new ArrayList<>();
                departmentIds.add(deptId);
            }

            List<AggregateCalcVo> aggregateCalcVoList = new ArrayList<>();

            log.info("Departments to process: " + departmentIds);
            for (Long dptId : departmentIds) {
                log.info("Processing departmentId: " + dptId);
                List<KraDetailsReport> kraList = kraDetailsReportDAO.findByCycleIdAndDepartmentId(cycle.getId(), dptId);
                log.info("Total KRAs for departmentId '" + dptId + "': " + (kraList != null ? kraList.size() : 0));
                if (HRMSHelper.isNullOrEmpty(kraList)) {
                    throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1502));
                }

                fetchAllKraData(kraList, aggregateCalcVoList, cycle);

                log.info("Completed processing for departmentId: " + dptId);
            }

            log.info("calculateDepartmentLevelAggregations :: Storing data.");
            populateDepartmentData(aggregateCalcVoList, cycle);
            populateDivisionData(aggregateCalcVoList, cycle);
            log.info("calculateDepartmentLevelAggregations :: Data stored.");

            log.info("Completed calculateDepartmentLevelAggregations");

        } catch (Exception e) {
            log.error("Error in calculateDepartmentLevelAggregations: ", e);
            throw e;
        }
    }

    @Async
    @Override
    public void doDepartmentLevelAnalysis(AiMsBackgroundAnalysisRequestInvokerVO invokerVO, Long deptId) throws Exception {
        try {
            // Run calculateDepartmentLevelAggregations first to populate department level aggregate scores
            // calculateDepartmentLevelAggregations(invokerVO, deptId);

            log.info("Starting doDepartmentLevelAnalysis");
            log.info("Purpose: " + invokerVO.getPurpose() + ", Role: " + invokerVO.getRole() + ", deptId: " + deptId);
            KraCycle cycle = invokerVO.getCycle();
            log.info("Active Cycle ID: " + cycle.getId());

            List<Long> departmentIds;
            if (HRMSHelper.isNullOrEmpty(deptId)) { // deptId == null
                //Fetch department details if HOD login
                departmentIds = fetchDepartmentsForHod(invokerVO);
            } else {
                departmentIds = new ArrayList<>();
                departmentIds.add(deptId);
            }
            log.info("Departments to process: " + departmentIds.size());

            AiMsDepartmentLevelAnalysisRequestVO pmsAiRequestVo = fetchDataForDepartmentLevelAnalysis(invokerVO, cycle, departmentIds);

            AiMsDepartmentLevelAnalysisResponseVO pmsAiMsResponse = callPmsAiMsDepartmentAnalysisApi(pmsAiRequestVo);

            saveDepartmentAnalysisData(invokerVO, cycle, pmsAiMsResponse, LevelOfAnalysis.DEPARTMENT.name());

            log.info("Completed doDepartmentLevelAnalysis");

        } catch (Exception e) {
            log.error("Error in doDepartmentLevelAnalysis: " + e.getMessage() + "::" + e.getLocalizedMessage() + "\n\n\n", e);
            throw e;
        }
    }

    private AiMsDepartmentLevelAnalysisRequestVO fetchDataForDepartmentLevelAnalysis(AiMsBackgroundAnalysisRequestInvokerVO invokerVO, KraCycle cycle, List<Long> departmentIds) throws Exception {
        boolean isAllowBlankValues = true;
        AiMsDepartmentLevelAnalysisRequestVO pmsAiRequestVo = new AiMsDepartmentLevelAnalysisRequestVO();
        pmsAiRequestVo.setIsDryRun("N");
        pmsAiRequestVo.setPurpose(invokerVO.getPurpose());
        pmsAiRequestVo.setForRole(invokerVO.getRole());
        pmsAiRequestVo.setCycleId(cycle.getId());
        pmsAiRequestVo.setCycleName(cycle.getCycleName());
        List<AiMsDepartmentLevelAnalysisRequestVO.DepartmentDataVo> departmentDataVoList = new ArrayList<>();

        for (Long dptId : departmentIds) {
            MasterDepartment department = departmentDAO.findByIdAndIsActive(dptId, ERecordStatus.Y.name());
            log.info("Processing department: " + department.getDepartmentName());
            AiMsDepartmentLevelAnalysisRequestVO.DepartmentDataVo departmentDataVo = new AiMsDepartmentLevelAnalysisRequestVO.DepartmentDataVo();
            departmentDataVo.setId(dptId);
            departmentDataVo.setDepartmentName(department.getDepartmentName());

            List<KraAggregateScores> deptLevelData;
            deptLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregationAndDepartment_Id(cycle.getId(), LevelOfAggregation.Department.name(), dptId);
            if (!HRMSHelper.isNullOrEmpty(deptLevelData) && deptLevelData.size() > 0) {
                KraAggregateScores deptScore = deptLevelData.get(0);
                if (!HRMSHelper.isNullOrEmpty(deptScore.getAggregateCalibratedRating())) {
                    departmentDataVo.setDepartmentAvgRating(deptScore.getAggregateCalibratedRating());
                    KraRatingRange range = rangeDao.findByAverage(deptScore.getAggregateCalibratedRating());
                    if (range != null) {
                        departmentDataVo.setDepartmentAvgRatingLabel(range.getRemark());
                    } else {
                        departmentDataVo.setDepartmentAvgRatingLabel("" + deptScore.getAggregateCalibratedRating());
                    }
                }
            }

            if (invokerVO.getPurpose().equalsIgnoreCase("CALIBRATION_SUGGESTION")) {
                KraRating targetRating = ratingDao.findByIsActiveAndId(IHRMSConstants.isActive, invokerVO.getRatingId());
                if (HRMSHelper.isNullOrEmpty(targetRating)) {
                    throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1502));
                }
                departmentDataVo.setDepartmentAvgRatingTarget(targetRating.getValue());
                departmentDataVo.setDepartmentAvgRatingTargetLabel(targetRating.getLabel());
            }

            List<KraDetailsReport> kraList = kraDetailsReportDAO.findByCycleIdAndDepartmentId(cycle.getId(), department.getId());
            log.info("Total KRAs for department '" + department.getDepartmentName() + "': " + (kraList != null ? kraList.size() : 0));
            if (HRMSHelper.isNullOrEmpty(kraList)) {
                throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1502));
            }

            List<AiMsDepartmentLevelAnalysisRequestVO.EmployeeDataVo> employees = new ArrayList<>();

            for (KraDetailsReport kraDetailsReport : kraList) {
                log.info("Processing KRA ID: " + kraDetailsReport.getKraId());
                AiMsDepartmentLevelAnalysisRequestVO.EmployeeDataVo employeeDataVo = new AiMsDepartmentLevelAnalysisRequestVO.EmployeeDataVo();

                Kra kra = kraDAO.findByIdAndIsActive(kraDetailsReport.getKraId(), ERecordStatus.Y.name());
                if (!HRMSHelper.isNullOrEmpty(kra)) {
                    employeeDataVo.setKraId(kra.getId());
                    employeeDataVo.setEmployeeId(kra.getEmployeeId());
                    AiMsDepartmentLevelAnalysisRequestVO.EmployeeDetailsVo employeeDetailsVo = new AiMsDepartmentLevelAnalysisRequestVO.EmployeeDetailsVo();
                    Employee employee = kra.getEmployee();
                    employeeDetailsVo.setId(employee.getId());
                    employeeDetailsVo.setName(employee.getCandidate().getFirstName());
                    Long designationId = employeeDesignationDAO.findDesignationIdByEmployeeId(employee.getId());
                    MasterDesignation designation = masterDesignationDAO.findByIdAndIsActive(designationId, ERecordStatus.Y.name());
                    if (!HRMSHelper.isNullOrEmpty(designation)) {
                        employeeDetailsVo.setDesignation(designation.getDesignationName());
                    }
                    Long divisionId = employeeDivisionDAO.findDivisionIdByEmployeeId(employee.getId());
                    MasterDivision division = masterDivisionDAO.findByIdAndIsActive(divisionId, ERecordStatus.Y.name());
                    employeeDetailsVo.setDivision(division.getDivisionName());

                    AiMsDepartmentLevelAnalysisRequestVO.EmployeeDetailsVo reportingManagerDetailsVo = new AiMsDepartmentLevelAnalysisRequestVO.EmployeeDetailsVo();
                    Employee reportingManager = employeeDAO.findById(kraDetailsReport.getRmId()).get();
                    reportingManagerDetailsVo.setId(reportingManager.getId());
                    reportingManagerDetailsVo.setName(reportingManager.getCandidate().getFirstName());
                    Long rmDesignationId = employeeDesignationDAO.findDesignationIdByEmployeeId(reportingManager.getId());
                    MasterDesignation rmDesignation = masterDesignationDAO.findByIdAndIsActive(rmDesignationId, ERecordStatus.Y.name());
                    if (!HRMSHelper.isNullOrEmpty(rmDesignation)) {
                        reportingManagerDetailsVo.setDesignation(rmDesignation.getDesignationName());
                    }
                    Long rmDivisionId = employeeDivisionDAO.findDivisionIdByEmployeeId(reportingManager.getId());
                    MasterDivision rmDivision = masterDivisionDAO.findByIdAndIsActive(rmDivisionId, ERecordStatus.Y.name());
                    reportingManagerDetailsVo.setDivision(rmDivision.getDivisionName());
                    employeeDetailsVo.setReportingTo(reportingManagerDetailsVo);

                    employeeDataVo.setEmployeeDetails(employeeDetailsVo);

                    if (!HRMSHelper.isNullOrEmpty(kra.getAvgSelfRating())) {
                        employeeDataVo.setAvgSelfRating(kra.getAvgSelfRating());
                        KraRatingRange range = rangeDao.findByAverage(kra.getAvgSelfRating());
                        employeeDataVo.setAvgSelfRatingLabel(range.getRating());
                    } /*else {
                        if (isAllowBlankValues) {
                            log.warn("Avg Self Rating is missing for KRA ID: " + kra.getId() + ". Setting default value.");
                            employeeDataVo.setAvgSelfRating(3.0);
                            employeeDataVo.setAvgSelfRatingLabel("3 [Mid]");
                        } else {
                            throw new HRMSException(1500, "Avg Self Rating is missing for KRA ID: " + kra.getId());
                        }
                    }*/
                    if (!HRMSHelper.isNullOrEmpty(kra.getAvgRmRating())) {
                        employeeDataVo.setAvgManagerRating(kra.getAvgRmRating());
                        KraRatingRange range = rangeDao.findByAverage(kra.getAvgRmRating());
                        employeeDataVo.setAvgManagerRatingLabel(range.getRating());
                    } /*else {
                        if (isAllowBlankValues) {
                            log.warn("Avg Manager Rating is missing for KRA ID: " + kra.getId() + ". Setting default value.");
                            employeeDataVo.setAvgManagerRating(3.0);
                            employeeDataVo.setAvgManagerRatingLabel("3 [Mid]");
                        } else {
                            throw new HRMSException(1500, "Avg Manager Rating is missing for KRA ID: " + kra.getId());
                        }
                    }*/
                    if (!HRMSHelper.isNullOrEmpty(kra.getHodAiComment())) {
                        employeeDataVo.setHodAiComments(kra.getHodAiComment());
                    } /*else {
                        if (isAllowBlankValues) {
                            log.warn("HOD AI Comments are missing for KRA ID: " + kra.getId() + ". Setting default value.");
                            employeeDataVo.setHodAiComments(" ");
                        } else {
                            throw new HRMSException(1500, "HOD AI Comments are missing for KRA ID: " + kra.getId());
                        }
                    }*/
                    List<AiMsDepartmentLevelAnalysisRequestVO.ObjectiveDataVo> objectives = new ArrayList<>();
                    List<KraDetails> kraDetailsList = kraDetailsDao.findByIsActiveAndKra(ERecordStatus.Y.name(), kra);
                    for (KraDetails kraDetails : kraDetailsList) {
                        AiMsDepartmentLevelAnalysisRequestVO.ObjectiveDataVo objectiveDataVo = new AiMsDepartmentLevelAnalysisRequestVO.ObjectiveDataVo();
                        objectiveDataVo.setId(kraDetails.getId());
                        objectiveDataVo.setCategory(categoryDao.findByIdAndIsActive(kraDetails.getCategoryId(), ERecordStatus.Y.name()).getCategoryName());
                        objectiveDataVo.setCategoryWeight("" + kraDetails.getWeightage());
                        objectiveDataVo.setSubcategory(subCategoryDao.findByIdAndIsActive(kraDetails.getSubcategoryId(), ERecordStatus.Y.name()).getSubCategoryName());
                        objectiveDataVo.setObjective(kraDetails.getDescription());
                        objectiveDataVo.setObjectiveWeight("" + kraDetails.getObjectiveWeightage());
                        objectiveDataVo.setMetric(kraDetails.getMeasurementCriteria());

                        if (!HRMSHelper.isNullOrEmpty(kraDetails.getSelfRating())) {
                            objectiveDataVo.setSelfRating(kraDetails.getSelfRating().getValue());
                            objectiveDataVo.setSelfRatingLabel(kraDetails.getSelfRating().getLabel());
                        } /*else {
                            if (isAllowBlankValues) {
                                log.warn("Self Rating is missing for KRA Details ID: " + kraDetails.getId() + ". Setting default value.");
                                objectiveDataVo.setSelfRating(3.0);
                                objectiveDataVo.setSelfRatingLabel("3 [Mid]");
                            } else {
                                throw new HRMSException(1500, "Self Rating is missing for KRA Details ID: " + kraDetails.getId());
                            }
                        }*/
                        if (!HRMSHelper.isNullOrEmpty(kraDetails.getSelfQaulitativeAssisment())) {
                            objectiveDataVo.setSelfQualitativeAssessment(kraDetails.getSelfQaulitativeAssisment());
                        } /*else {
                            if (isAllowBlankValues) {
                                log.warn("Self Qualitative Assessment is missing for KRA Details ID: " + kraDetails.getId() + ". Setting default value.");
                                objectiveDataVo.setSelfQualitativeAssessment("No input received yet.");
                            } else {
                                throw new HRMSException(1500, "Self Qualitative Assessment is missing for KRA Details ID: " + kraDetails.getId());
                            }
                        }*/

                        if (!HRMSHelper.isNullOrEmpty(kraDetails.getManagerRating())) {
                            objectiveDataVo.setManagerRating(kraDetails.getManagerRating().getValue());
                            objectiveDataVo.setManagerRatingLabel(kraDetails.getManagerRating().getLabel());
                        } /*else {
                            if (isAllowBlankValues) {
                                log.warn("Manager Rating is missing for KRA Details ID: " + kraDetails.getId() + ". Setting default value.");
                                objectiveDataVo.setManagerRating(3.0);
                                objectiveDataVo.setManagerRatingLabel("3 [Mid]");
                            } else {
                                throw new HRMSException(1500, "Manager Rating is missing for KRA Details ID: " + kraDetails.getId());
                            }
                        }*/

                        if (!HRMSHelper.isNullOrEmpty(kraDetails.getManagerQaulitativeAssisment())) {
                            objectiveDataVo.setManagerQualitativeAssessment(kraDetails.getManagerQaulitativeAssisment());
                        } /*else {
                            if (isAllowBlankValues) {
                                log.warn("Manager Qualitative Assessment is missing for KRA Details ID: " + kraDetails.getId() + ". Setting default value.");
                                objectiveDataVo.setManagerQualitativeAssessment("No input received yet.");
                            } else {
                                throw new HRMSException(1500, "Manager Qualitative Assessment is missing for KRA Details ID: " + kraDetails.getId());
                            }
                        }*/

                        if (!HRMSHelper.isNullOrEmpty(kraDetails.getRmAiComment())) {
                            objectiveDataVo.setAiFeedbackForManager(kraDetails.getRmAiComment());
                        } /*else {
                            // PMS AI is not using this value for now, so not throwing error
                            log.warn("AI Feedback for Manager is missing for KRA Details ID: " + kraDetails.getId() + ". Setting default value.");
                            objectiveDataVo.setAiFeedbackForManager("");
                            // throw new HRMSException(1500, "AI Feedback for Manager is missing for KRA Details ID: " + kraDetails.getId());
                        }*/
                        objectives.add(objectiveDataVo);
                    }
                    employeeDataVo.setObjectives(objectives);
                }
                employees.add(employeeDataVo);
            }
            departmentDataVo.setDepartmentEmployees(employees);

            departmentDataVoList.add(departmentDataVo);
            log.info("Completed data fetching for department : " + department.getDepartmentName());
        }

        pmsAiRequestVo.setDepartments(departmentDataVoList);

        return pmsAiRequestVo;
    }

    private AiMsDepartmentLevelAnalysisResponseVO callPmsAiMsDepartmentAnalysisApi(AiMsDepartmentLevelAnalysisRequestVO pmsAiRequestVo) throws Exception {
        // Call pmsAiMS API and get response
        try {
            ObjectMapper mapper = new ObjectMapper();
            String aiRequestJson = mapper.writeValueAsString(pmsAiRequestVo);
            log.debug("The Payload for PMS AIMS : \n\n" + aiRequestJson + "\n\n\n.");

            String uriString = pmsAiMsBaseUrl;
            uriString += pmsAiMsEndPointDepartmentalAnalysis;

            URI uri = new URI(uriString);
            RestTemplate restTemplate = new RestTemplate();

            // Create headers with API key
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", pmsAiMsApiKey);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create HTTP entity with body and headers
            HttpEntity<AiMsDepartmentLevelAnalysisRequestVO> requestEntity = new HttpEntity<>(pmsAiRequestVo, headers);

            // Make POST request
            log.info("Calling PMS AIMS API : " + pmsAiMsEndPointDepartmentalAnalysis + " : Request sent ...");
            ResponseEntity<String> apiResult = restTemplate.postForEntity(uri, requestEntity, String.class);
            log.info("Calling PMS AIMS API : " + pmsAiMsEndPointDepartmentalAnalysis + " : Response received ...");

            // check if request is successful
            if (apiResult.getStatusCode() != HttpStatus.OK) {
                log.error("Error in callPmsAiMsDepartmentAnalysisApi():E1: HTTP Status Code = " + apiResult.getStatusCode());
                throw new HRMSException(1500, "Error while calling AI service : " + apiResult.getStatusCode());
            } else {
                String apiResponseBody = apiResult.getBody();
                log.debug("The response from PMS AIMS : \n\n" + apiResponseBody + "\n\n\n.");
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                AiMsDepartmentLevelAnalysisResponseVO pmsAiMsResponse = mapper.readerFor(AiMsDepartmentLevelAnalysisResponseVO.class).readValue(apiResponseBody);
                if (HRMSHelper.isNullOrEmpty(pmsAiMsResponse)) {
                    log.error("Error in callPmsAiMsDepartmentAnalysisApi():E2: No Response body from AIMS : " + apiResult.getStatusCode());
                    throw new HRMSException(1500, "Error while calling AI service : No Response body : " + apiResult.getStatusCode());
                }
                return pmsAiMsResponse;
            }

        } catch (Exception e) {
            log.error("Error in callPmsAiMsDepartmentAnalysisApi():E3: " + e.getMessage(), e);
            log.error(e.getLocalizedMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void saveDepartmentAnalysisData(AiMsBackgroundAnalysisRequestInvokerVO invokerVO, KraCycle cycle, AiMsDepartmentLevelAnalysisResponseVO pmsAiMsResponse, String levelOfAnalysis) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        if (!HRMSHelper.isNullOrEmpty(pmsAiMsResponse.getDepartments())) {

            for (AiMsDepartmentLevelAnalysisResponseVO.DepartmentDataVo deptVo : pmsAiMsResponse.getDepartments()) {
                List<KraAggregateScores> deptLevelData;
                deptLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregationAndDepartment_Id(cycle.getId(), LevelOfAggregation.Department.name(), deptVo.getId());
                KraAggregateScores deptScore;
                if (HRMSHelper.isNullOrEmpty(deptLevelData)) {
                    //Insert new record here
                    deptScore = new KraAggregateScores();
                    deptScore.setOrgId(cycle.getOrgId());
                    deptScore.setYear(cycle.getYear().getId());
                    deptScore.setKraCycleId(cycle.getId());
                    deptScore.setLevelOfAggregation(LevelOfAggregation.Department.name());
                    deptScore.setDepartment(departmentDAO.findByIdAndIsActive(deptVo.getId(), ERecordStatus.Y.name()));
                } else {
                    //Update existing record here
                    deptScore = deptLevelData.get(0);
                }
                if (invokerVO.getPurpose().equalsIgnoreCase("CALIBRATION_SUGGESTION")) {
                    if (HRMSHelper.isNullOrEmpty(deptVo.getAiFeedbackObj())) {
                        log.warn("AI Feedback Object is null for Department ID: " + deptVo.getId() + " in CALIBRATION_SUGGESTION.");
                    }
                    deptScore.setCalibrateAiComment(mapper.writeValueAsString(deptVo.getAiFeedbackObj()));
                } else if (invokerVO.getPurpose().equalsIgnoreCase("REVIEW_ASSISTANCE")) {
                    if (HRMSHelper.isNullOrEmpty(deptVo.getAiFeedbackObj())) {
                        log.warn("AI Feedback Object is null for Department ID: " + deptVo.getId() + " in REVIEW_ASSISTANCE for '" + invokerVO.getRole() + "' role.");
                    }
                    deptScore.setCeoAiComment(mapper.writeValueAsString(deptVo.getAiFeedbackObj()));
                    deptScore.setHodAiComment(mapper.writeValueAsString(deptVo.getAiFeedbackObj()));
//                    if (invokerVO.getRole().equalsIgnoreCase(ERole.ADMIN.name())) {
//                        if (HRMSHelper.isNullOrEmpty(deptVo.getAiFeedbackObj())) {
//                            log.warn("AI Feedback Object is null for Department ID: " + deptVo.getId() + " in REVIEW_ASSISTANCE for ADMIN/CEO role.");
//                        }
//                        deptScore.setCeoAiComment(mapper.writeValueAsString(deptVo.getAiFeedbackObj()));
//                    } else if (invokerVO.getRole().equalsIgnoreCase(ERole.HOD.name())) {
//                        if (HRMSHelper.isNullOrEmpty(deptVo.getAiFeedbackObj())) {
//                            log.warn("AI Feedback Object is null for Department ID: " + deptVo.getId() + " in REVIEW_ASSISTANCE for HOD role.");
//                        }
//                        deptScore.setHodAiComment(mapper.writeValueAsString(deptVo.getAiFeedbackObj()));
//                    }
                }
                deptScore.setModifiedDate(new Date());
                kraAggregateScoresDao.save(deptScore);
                //commented data saving as AIMS is now saving data directly to db
//                if (invokerVO.getPurpose().equalsIgnoreCase("CALIBRATION_SUGGESTION")) {
//                    if (!HRMSHelper.isNullOrEmpty(deptVo.getDepartmentEmployees())) {
//                        for (AiMsDepartmentLevelAnalysisResponseVO.EmployeeDataVo empVo : deptVo.getDepartmentEmployees()) {
//                            if (!HRMSHelper.isNullOrEmpty(empVo.getAiFeedbackObj())) {
//                                Kra kra = kraDAO.findByIdAndIsActive(empVo.getKraId(), ERecordStatus.Y.name());
//                                kra.setCalibrateAiComment(mapper.writeValueAsString(empVo.getAiFeedbackObj()));
//                                kraDAO.save(kra);
//                            } else {
//                                log.warn("AI Feedback Object is null for Employee ID: " + empVo.getEmployeeId() + " and KRA ID: " + empVo.getKraId() + " in CALIBRATION_SUGGESTION.");
//                            }
//                        }
//                    }
//                }
            }
        }

        if (levelOfAnalysis.equalsIgnoreCase(LevelOfAnalysis.ORGANISATION.name())) {
            if (!HRMSHelper.isNullOrEmpty(pmsAiMsResponse.getOverallSummary())) {
                AiMsDepartmentLevelAnalysisResponseVO.OverallSummaryVO orgVo = pmsAiMsResponse.getOverallSummary();
                List<KraAggregateScores> orgLevelData;
                orgLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregation(cycle.getId(), LevelOfAggregation.Organisation.name());
                KraAggregateScores orgScore;
                if (HRMSHelper.isNullOrEmpty(orgLevelData)) {
                    //Insert new record here
                    orgScore = new KraAggregateScores();
                    orgScore.setOrgId(cycle.getOrgId());
                    orgScore.setYear(cycle.getYear().getId());
                    orgScore.setKraCycleId(cycle.getId());
                    orgScore.setLevelOfAggregation(LevelOfAggregation.Organisation.name());
                } else {
                    //Update existing record here
                    orgScore = orgLevelData.get(0);
                }

                if (invokerVO.getPurpose().equalsIgnoreCase("CALIBRATION_SUGGESTION")) {
                    if (HRMSHelper.isNullOrEmpty(orgVo.getAiFeedbackObj())) {
                        log.warn("AI Feedback Object is null for OverallSummary in CALIBRATION_SUGGESTION.");
                    }
                    orgScore.setCalibrateAiComment(mapper.writeValueAsString(orgVo.getAiFeedbackObj()));
                } else if (invokerVO.getPurpose().equalsIgnoreCase("REVIEW_ASSISTANCE")) {
                    if (invokerVO.getRole().equalsIgnoreCase(ERole.ADMIN.name())) {
                        if (HRMSHelper.isNullOrEmpty(orgVo.getAiFeedbackObj())) {
                            log.warn("AI Feedback Object is null for OverallSummary in REVIEW_ASSISTANCE for ADMIN/CEO role.");
                        }
                        orgScore.setCeoAiComment(mapper.writeValueAsString(orgVo.getAiFeedbackObj()));
                    }
                }

                orgScore.setModifiedDate(new Date());
                kraAggregateScoresDao.save(orgScore);
            } else {
                log.warn("OverallSummary is null in ORGANISATION level analysis.");
            }
        }
    }

    //@Async
    @Override
    public void calculateOrganisationLevelAggregations(AiMsBackgroundAnalysisRequestInvokerVO invokerVO) throws Exception {
        try {
            log.info("Starting calculateOrganisationLevelAggregations");
            KraCycle cycle = invokerVO.getCycle();
            log.info("Active Cycle ID: " + cycle.getId());
            List<MasterDepartment> departmentList = fetchAllDepartmentsForCeo(invokerVO);

            List<AggregateCalcVo> aggregateCalcVoList = new ArrayList<>();

            log.info("Total Departments to process: " + departmentList.size());
            for (MasterDepartment department : departmentList) {
                log.info("Processing department: " + department.getDepartmentName());
                List<KraDetailsReport> kraList = kraDetailsReportDAO.findByCycleIdAndDepartmentId(cycle.getId(), department.getId());
                log.info("Total KRAs for department '" + department.getDepartmentName() + "': " + (kraList != null ? kraList.size() : 0));
                if (HRMSHelper.isNullOrEmpty(kraList)) {
                    throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1502));
                }

                fetchAllKraData(kraList, aggregateCalcVoList, cycle);

                log.info("Completed fetching data for department: " + department.getDepartmentName());
            }

            log.info("calculateOrganisationLevelAggregations :: Storing data.");
            populateOrganisationData(aggregateCalcVoList, cycle);
            populateDepartmentData(aggregateCalcVoList, cycle);
            populateDivisionData(aggregateCalcVoList, cycle);
            log.info("calculateOrganisationLevelAggregations :: Data stored.");

            log.info("Completed calculateOrganisationLevelAggregations");

        } catch (Exception e) {
            log.error("Error in calculateOrganisationLevelAggregations: ", e);
            throw e;
        }
    }

    private void fetchAllKraData(List<KraDetailsReport> kraList, List<AggregateCalcVo> aggregateCalcVoList, KraCycle cycle) {
        for (KraDetailsReport kraDetailsReport : kraList) {
            log.info("Processing KRA ID: " + kraDetailsReport.getKraId());
            fetchSingleKraData(kraDetailsReport, aggregateCalcVoList, cycle);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void fetchSingleKraData(KraDetailsReport kraDetailsReport, List<AggregateCalcVo> aggregateCalcVoList, KraCycle cycle) {
        boolean isReCalculateRatingIfNull = false;
        AggregateCalcVo calculationVo = new AggregateCalcVo();
        Kra kra = kraDAO.findByIdAndIsActive(kraDetailsReport.getKraId(), ERecordStatus.Y.name());
        if (!HRMSHelper.isNullOrEmpty(kra)) {
            calculationVo.setDeptId(kra.getDepartment().getId());
            calculationVo.setDivId(kra.getDivision().getId());
            calculationVo.setOrganizationId(kra.getOrgId());
            if (isReCalculateRatingIfNull && HRMSHelper.isNullOrEmpty(kra.getAvgSelfRating())) {
                Double average = kraDetailsDao.calculateSelfRatingAverage(kra.getId(), IHRMSConstants.isActive, IHRMSConstants.ZERO_VALUE);
                if (!HRMSHelper.isNullOrEmpty(average)) {
                    Double trimmedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN).doubleValue();
                    kra.setAvgSelfRating(trimmedAverage);
                    kraDAO.save(kra);
                }
            }
            if (!HRMSHelper.isNullOrEmpty(kra.getAvgSelfRating())) {
                calculationVo.setSelfRating(kra.getAvgSelfRating());
            } else {
                log.warn("Avg Self Rating is missing for KRA ID: " + kra.getId() + ". Skipping this KRA for aggregation.");
            }

            if (isReCalculateRatingIfNull && HRMSHelper.isNullOrEmpty(kra.getAvgRmRating())) {
                Double average = kraDetailsDao.calculateAverage(kra.getId(), IHRMSConstants.isActive, IHRMSConstants.ZERO_VALUE);
                if (!HRMSHelper.isNullOrEmpty(average)) {
                    Double trimmedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN).doubleValue();
                    kra.setAvgRmRating(trimmedAverage);
                    kraDAO.save(kra);
                }
            }
            if (!HRMSHelper.isNullOrEmpty(kra.getAvgRmRating())) {
                calculationVo.setManagerRating(kra.getAvgRmRating());
            } else {
                log.warn("Avg Manager Rating is missing for KRA ID: " + kra.getId() + ". Skipping this KRA for aggregation.");
            }

            HodCycleFinalRating hodCycle = hodCycleFinalRatingDao.findByIsActiveAndKra(ERecordStatus.Y.name(), kra);
            if (!HRMSHelper.isNullOrEmpty(hodCycle)) {
                // Hod rating record found
                if (!HRMSHelper.isNullOrEmpty(hodCycle.getScore())) {
                    // HOD rating is present
                    calculationVo.setCalibratedRating(hodCycle.getScore().getValue());
                }
            }

            if (HRMSHelper.isNullOrEmpty(calculationVo.getCalibratedRating())) {
                if (!HRMSHelper.isNullOrEmpty(calculationVo.getManagerRating())) {
                    calculationVo.setCalibratedRating(calculationVo.getManagerRating()); // If HOD rating not present, set calibrated rating as manager rating
                } else if (!HRMSHelper.isNullOrEmpty(calculationVo.getSelfRating())) {
                    calculationVo.setCalibratedRating(calculationVo.getSelfRating()); // If manager rating not present, set calibrated rating as self rating
                }
            }

            if (HRMSHelper.isNullOrEmpty(calculationVo.getCalibratedRating())) {
                log.warn("Calibrated Rating is missing for KRA ID: " + kra.getId() + ". Skipping this KRA for aggregation.");
            }

            aggregateCalcVoList.add(calculationVo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void populateOrganisationData(List<AggregateCalcVo> aggregateCalcVoList, KraCycle cycle) {
        log.info("Populating Organisation Level Data, start");
        List<KraAggregateScores> orgLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregation(cycle.getId(), LevelOfAggregation.Organisation.name());
        KraAggregateScores orgScore;
        if (HRMSHelper.isNullOrEmpty(orgLevelData)) {
            orgScore = new KraAggregateScores();
            orgScore.setOrgId(cycle.getOrgId());
            orgScore.setYear(cycle.getYear().getId());
            orgScore.setKraCycleId(cycle.getId());
            orgScore.setLevelOfAggregation(LevelOfAggregation.Organisation.name());
        } else {
            orgScore = orgLevelData.get(0);
        }
        Long countOfSelfRating = aggregateCalcVoList.stream().filter(r -> r.getSelfRating() != null && r.getSelfRating() > 0).count();
        Double sumRatingSelfRating = null;
        if (countOfSelfRating > 0) {
            sumRatingSelfRating = aggregateCalcVoList.stream().filter(r -> r.getSelfRating() != null && r.getSelfRating() > 0).mapToDouble(r -> r.getSelfRating()).sum();
            orgScore.setAggregateSelfRating(sumRatingSelfRating / countOfSelfRating);
        }
        Long countOfManagerRating = aggregateCalcVoList.stream().filter(r -> r.getManagerRating() != null && r.getManagerRating() > 0).count();
        Double sumRatingManagerRating = null;
        if (countOfManagerRating > 0) {
            sumRatingManagerRating = aggregateCalcVoList.stream().filter(r -> r.getManagerRating() != null && r.getManagerRating() > 0).mapToDouble(r -> r.getManagerRating()).sum();
            orgScore.setAggregateRmRating(sumRatingManagerRating / countOfManagerRating);
        }
        Long countOfCalibrateRating = aggregateCalcVoList.stream().filter(r -> r.getCalibratedRating() != null && r.getCalibratedRating() > 0).count();
        Double sumRatingCalibrateRating = null;
        if (countOfCalibrateRating > 0) {
            sumRatingCalibrateRating = aggregateCalcVoList.stream().filter(r -> r.getCalibratedRating() != null && r.getCalibratedRating() > 0).mapToDouble(r -> r.getCalibratedRating()).sum();
            orgScore.setAggregateCalibratedRating(sumRatingCalibrateRating / countOfCalibrateRating);
        }
        orgScore.setHrAiComment("{\"selfRating\": { \"sum\":" + sumRatingSelfRating + ", \"count\": " + countOfSelfRating + "}, \"managerRating\": { \"sum\": " + sumRatingManagerRating + ", \"count\": " + countOfManagerRating + "}, \"calibratedRating\": { \"sum\": " + sumRatingCalibrateRating + ", \"count\": " + countOfCalibrateRating + "} }");
        orgScore.setModifiedDate(new Date());
        kraAggregateScoresDao.save(orgScore);
        log.info("Populating Organisation Level Data, end");
    }

    @Transactional(rollbackFor = Exception.class)
    private void populateDepartmentData(List<AggregateCalcVo> aggregateCalcVoList, KraCycle cycle) {
        log.info("Populating Department Level Data, start");
        List<Long> departmentList = aggregateCalcVoList.stream().map(AggregateCalcVo::getDeptId).distinct().collect(Collectors.toList());
        for (Long departmentId : departmentList) {
            List<KraAggregateScores> deptLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregationAndDepartment_Id(cycle.getId(), LevelOfAggregation.Department.name(), departmentId);
            KraAggregateScores deptScore;
            if (HRMSHelper.isNullOrEmpty(deptLevelData)) {
                //Insert new record here
                deptScore = new KraAggregateScores();
                deptScore.setOrgId(cycle.getOrgId());
                deptScore.setYear(cycle.getYear().getId());
                deptScore.setKraCycleId(cycle.getId());
                deptScore.setLevelOfAggregation(LevelOfAggregation.Department.name());
                deptScore.setDepartment(departmentDAO.findByIdAndIsActive(departmentId, ERecordStatus.Y.name()));
            } else {
                //Update existing record here
                deptScore = deptLevelData.get(0);
            }
            Long countOfSelfRating = aggregateCalcVoList.stream().filter(r -> r.getSelfRating() != null && r.getSelfRating() > 0 && r.getDeptId().equals(departmentId)).count();
            Double sumRatingSelfRating = null;
            if (countOfSelfRating > 0) {
                sumRatingSelfRating = aggregateCalcVoList.stream().filter(r -> r.getSelfRating() != null && r.getSelfRating() > 0 && r.getDeptId().equals(departmentId)).mapToDouble(r -> r.getSelfRating()).sum();
                deptScore.setAggregateSelfRating(sumRatingSelfRating / countOfSelfRating);
            }
            Long countOfManagerRating = aggregateCalcVoList.stream().filter(r -> r.getManagerRating() != null && r.getManagerRating() > 0 && r.getDeptId().equals(departmentId)).count();
            Double sumRatingManagerRating = null;
            if (countOfManagerRating > 0) {
                sumRatingManagerRating = aggregateCalcVoList.stream().filter(r -> r.getManagerRating() != null && r.getManagerRating() > 0 && r.getDeptId().equals(departmentId)).mapToDouble(r -> r.getManagerRating()).sum();
                deptScore.setAggregateRmRating(sumRatingManagerRating / countOfManagerRating);
            }
            Long countOfCalibrateRating = aggregateCalcVoList.stream().filter(r -> r.getCalibratedRating() != null && r.getCalibratedRating() > 0 && r.getDeptId().equals(departmentId)).count();
            Double sumRatingCalibrateRating = null;
            if (countOfCalibrateRating > 0) {
                sumRatingCalibrateRating = aggregateCalcVoList.stream().filter(r -> r.getCalibratedRating() != null && r.getCalibratedRating() > 0 && r.getDeptId().equals(departmentId)).mapToDouble(r -> r.getCalibratedRating()).sum();
                deptScore.setAggregateCalibratedRating(sumRatingCalibrateRating / countOfCalibrateRating);
            }
            deptScore.setHrAiComment("{\"selfRating\": { \"sum\":" + sumRatingSelfRating + ", \"count\": " + countOfSelfRating + "}, \"managerRating\": { \"sum\": " + sumRatingManagerRating + ", \"count\": " + countOfManagerRating + "}, \"calibratedRating\": { \"sum\": " + sumRatingCalibrateRating + ", \"count\": " + countOfCalibrateRating + "} }");
            deptScore.setModifiedDate(new Date());
            kraAggregateScoresDao.save(deptScore);
        }
        log.info("Populating Department Level Data, end");
    }

    @Transactional(rollbackFor = Exception.class)
    private void populateDivisionData(List<AggregateCalcVo> aggregateCalcVoList, KraCycle cycle) {
        log.info("Populating Division Level Data, start");
        List<Long> departmentList = aggregateCalcVoList.stream().map(AggregateCalcVo::getDeptId).distinct().collect(Collectors.toList());
        for (Long departmentId : departmentList) {
            List<Long> divisionList = aggregateCalcVoList.stream().filter(r -> r.getDeptId().equals(departmentId)).map(AggregateCalcVo::getDivId).distinct().collect(Collectors.toList());
            for (Long divisionId : divisionList) {
                List<KraAggregateScores> deptLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregationAndDepartment_IdAndDivision_Id(cycle.getId(), LevelOfAggregation.Division.name(), departmentId, divisionId);
                KraAggregateScores divisionScore;
                if (HRMSHelper.isNullOrEmpty(deptLevelData)) {
                    //Insert new record here
                    divisionScore = new KraAggregateScores();
                    divisionScore.setOrgId(cycle.getOrgId());
                    divisionScore.setYear(cycle.getYear().getId());
                    divisionScore.setKraCycleId(cycle.getId());
                    divisionScore.setLevelOfAggregation(LevelOfAggregation.Division.name());
                    divisionScore.setDepartment(departmentDAO.findByIdAndIsActive(departmentId, ERecordStatus.Y.name()));
                    divisionScore.setDivision(divisionDAO.findByIdAndIsActive(divisionId, ERecordStatus.Y.name()));
                } else {
                    //Update existing record here
                    divisionScore = deptLevelData.get(0);
                }
                Long countOfSelfRating = aggregateCalcVoList.stream().filter(r -> r.getSelfRating() != null && r.getSelfRating() > 0 && r.getDeptId().equals(departmentId) && r.getDivId().equals(divisionId)).count();
                Double sumRatingSelfRating = null;
                if (countOfSelfRating > 0) {
                    sumRatingSelfRating = aggregateCalcVoList.stream().filter(r -> r.getSelfRating() != null && r.getSelfRating() > 0 && r.getDeptId().equals(departmentId) && r.getDivId().equals(divisionId)).mapToDouble(r -> r.getSelfRating()).sum();
                    divisionScore.setAggregateSelfRating(sumRatingSelfRating / countOfSelfRating);
                }
                Long countOfManagerRating = aggregateCalcVoList.stream().filter(r -> r.getManagerRating() != null && r.getManagerRating() > 0 && r.getDeptId().equals(departmentId) && r.getDivId().equals(divisionId)).count();
                Double sumRatingManagerRating = null;
                if (countOfManagerRating > 0) {
                    sumRatingManagerRating = aggregateCalcVoList.stream().filter(r -> r.getManagerRating() != null && r.getManagerRating() > 0 && r.getDeptId().equals(departmentId) && r.getDivId().equals(divisionId)).mapToDouble(r -> r.getManagerRating()).sum();
                    divisionScore.setAggregateRmRating(sumRatingManagerRating / countOfManagerRating);
                }
                Long countOfCalibrateRating = aggregateCalcVoList.stream().filter(r -> r.getCalibratedRating() != null && r.getCalibratedRating() > 0 && r.getDeptId().equals(departmentId) && r.getDivId().equals(divisionId)).count();
                Double sumRatingCalibrateRating = null;
                if (countOfCalibrateRating > 0) {
                    sumRatingCalibrateRating = aggregateCalcVoList.stream().filter(r -> r.getCalibratedRating() != null && r.getCalibratedRating() > 0 && r.getDeptId().equals(departmentId) && r.getDivId().equals(divisionId)).mapToDouble(r -> r.getCalibratedRating()).sum();
                    divisionScore.setAggregateCalibratedRating(sumRatingCalibrateRating / countOfCalibrateRating);
                }
                divisionScore.setHrAiComment("{\"selfRating\": { \"sum\":" + sumRatingSelfRating + ", \"count\": " + countOfSelfRating + "}, \"managerRating\": { \"sum\": " + sumRatingManagerRating + ", \"count\": " + countOfManagerRating + "}, \"calibratedRating\": { \"sum\": " + sumRatingCalibrateRating + ", \"count\": " + countOfCalibrateRating + "} }");
                divisionScore.setModifiedDate(new Date());
                kraAggregateScoresDao.save(divisionScore);
            }
        }
        log.info("Populating Division Level Data, end");
    }

    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doOrganisationLevelAnalysis(AiMsBackgroundAnalysisRequestInvokerVO invokerVO) throws Exception {
        try {
            boolean isAllowBlankValues = true;
            //Run calculateOrganisationLevelAggregations first to populate organisation level aggregate scores
            //calculateOrganisationLevelAggregations(invokerVO);

            log.info("Starting doOrganisationLevelAnalysis");
            log.info("Purpose: " + invokerVO.getPurpose() + ", Role: " + invokerVO.getRole());
            KraCycle cycle = invokerVO.getCycle();
            log.info("Active Cycle ID: " + cycle.getId());

            List<MasterDepartment> departmentList = fetchAllDepartmentsForCeo(invokerVO);

            List<Long> departmentIds = new ArrayList<>();
            for (MasterDepartment dept : departmentList) {
                departmentIds.add(dept.getId());
            }

            log.info("Total Departments to process: " + departmentList.size());

            AiMsDepartmentLevelAnalysisRequestVO pmsAiRequestVo = fetchDataForDepartmentLevelAnalysis(invokerVO, cycle, departmentIds);

            AiMsDepartmentLevelAnalysisResponseVO pmsAiMsResponse = callPmsAiMsDepartmentAnalysisApi(pmsAiRequestVo);

            saveDepartmentAnalysisData(invokerVO, cycle, pmsAiMsResponse, LevelOfAnalysis.ORGANISATION.name());

            log.info("Completed doOrganisationLevelAnalysis");

        } catch (Exception e) {
            log.error("Error in doOrganisationLevelAnalysis: " + e.getMessage() + "::" + e.getLocalizedMessage(), e);
            throw e;
        }
    }
}
