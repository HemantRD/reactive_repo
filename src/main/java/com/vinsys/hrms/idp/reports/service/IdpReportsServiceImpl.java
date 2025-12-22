package com.vinsys.hrms.idp.reports.service;

import com.vinsys.hrms.dao.idp.ApprovedTrainingsDAO;
import com.vinsys.hrms.dao.idp.IdpTrainingDetailsDAO;
import com.vinsys.hrms.dao.idp.TrainingBudgetDAO;
import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.dao.IIdpOrganizationEmployeeViewDAO;
import com.vinsys.hrms.idp.entity.IdpOrganizationEmployeeView;
import com.vinsys.hrms.idp.reports.helper.ExcelHelper;
import com.vinsys.hrms.idp.reports.validator.IdpReportsValidator;
import com.vinsys.hrms.idp.reports.vo.DashboardVo;
import com.vinsys.hrms.idp.reports.vo.GroupVsIndividualTrainingCountSummary;
import com.vinsys.hrms.idp.reports.vo.MemberWiseCost;
import com.vinsys.hrms.idp.reports.vo.ParticipantsClusterDetailsVo;
import com.vinsys.hrms.idp.reports.vo.ParticipantsClustersListReq;
import com.vinsys.hrms.idp.reports.vo.ParticipantsClustersVo;
import com.vinsys.hrms.idp.reports.vo.TopTrainingCourses;
import com.vinsys.hrms.idp.reports.vo.TopicWiseCost;
import com.vinsys.hrms.logo.service.LogoService;
import com.vinsys.hrms.master.dao.IMasterYearDAO;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.vinsys.hrms.idp.utils.IdpEnums.ReportType.DETAILED;

@Service
public class IdpReportsServiceImpl implements IIdpReportsService {

    private final BackendProperties props;
    private final IdpReportsValidator validator;
    private final LogoService logoService;
    private final TrainingCatalogDAO trainingCatalogDAO;
    private final IdpTrainingDetailsDAO idpTrainingDetailsDAO;
    private final TrainingBudgetDAO trainingBudgetDAO;
    private final ApprovedTrainingsDAO approvedTrainingsDAO;
    private final IMasterYearDAO iMasterYearDAO;

    @Autowired
    IIdpOrganizationEmployeeViewDAO idpOrganizationEmployeeViewDAO;

    public IdpReportsServiceImpl(final BackendProperties props, final IdpReportsValidator validator,
                                 final LogoService logoService, final TrainingCatalogDAO trainingCatalogDAO,
                                 final IdpTrainingDetailsDAO idpTrainingDetailsDAO, final TrainingBudgetDAO trainingBudgetDAO,
                                 final ApprovedTrainingsDAO approvedTrainingsDAO, final IMasterYearDAO iMasterYearDAO) {
        this.props = props;
        this.validator = validator;
        this.logoService = logoService;
        this.trainingCatalogDAO = trainingCatalogDAO;
        this.idpTrainingDetailsDAO = idpTrainingDetailsDAO;
        this.trainingBudgetDAO = trainingBudgetDAO;
        this.approvedTrainingsDAO = approvedTrainingsDAO;
        this.iMasterYearDAO = iMasterYearDAO;
    }

    public HRMSBaseResponse<List<TopTrainingCourses>> getTopTrainingRequested(String keyword, Pageable pageable) throws HRMSException {
        HRMSBaseResponse<List<TopTrainingCourses>> response = new HRMSBaseResponse<>();
        Page<TopTrainingCourses> topTrainingCourses = getTopTrainingCourses(keyword, pageable);
        response.setResponseBody(topTrainingCourses.getContent());
        response.setResponseCode(1200);
        response.setTotalRecord(topTrainingCourses.getTotalElements());
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    public byte[] getTopTrainingRequestedExcel(String keyword) throws HRMSException {
        List<TopTrainingCourses> reportData = getTopTrainingCourses(keyword);
        if (HRMSHelper.isNullOrEmpty(reportData)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
        }
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            String[] headers = {"Training Name", "Training Code", "Competency Type Name",
                    "Competency SubType Name", "number Of Times Requested"};
            String title = "TopTrainingRequestedExcel_Report";
            Sheet sheet = ExcelHelper.createInitialSheet(workbook, title, headers, logoService);

            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.MEDIUM);
            borderStyle.setBorderBottom(BorderStyle.MEDIUM);
            borderStyle.setBorderLeft(BorderStyle.MEDIUM);
            borderStyle.setBorderRight(BorderStyle.MEDIUM);

            int rowNum = 4 + 1;
            for (TopTrainingCourses data : reportData) {
                Row row = sheet.createRow(rowNum++);
                ExcelHelper.createCell(row, 0, data.getTrainingName(), borderStyle);
                ExcelHelper.createCell(row, 1, data.getTrainingCode(), borderStyle);
                ExcelHelper.createCell(row, 2, data.getCompetencyTypeName(), borderStyle);
                ExcelHelper.createCell(row, 3, data.getCompetencySubTypeName(), borderStyle);
                ExcelHelper.createCell(row, 4, data.getNumberOfTimesRequested(), borderStyle);
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
    public HRMSBaseResponse<List<MemberWiseCost>> getCostMemberWise(String keyword, Pageable pageable, String reportType) throws HRMSException {
        validator.validateCostReport(null, reportType);
        HRMSBaseResponse<List<MemberWiseCost>> response = new HRMSBaseResponse<>();
        Page<MemberWiseCost> memberWiseCostList = getMemberWiseCostList(keyword, pageable, reportType);
        response.setResponseBody(memberWiseCostList.getContent());
        response.setResponseCode(1200);
        response.setTotalRecord(memberWiseCostList.getTotalElements());
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public byte[] getCostMemberWiseExcel(String viewType, String keyword, Pageable pageable, String reportType) throws HRMSException {
        validator.validateCostReport(viewType, reportType);
        Page<MemberWiseCost> memberWiseCostList = getMemberWiseCostList(keyword, pageable, reportType);
        if (HRMSHelper.isNullOrEmpty(memberWiseCostList)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
        }
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            String[] headers = {"Member Name", "Training Name", "Training Code", "Competency Type Name",
                    "Competency SubType Name", "Courses Count", "Total Cost"};
            String title = "MemberWiseCost_Report_" + reportType.toUpperCase();
            Sheet sheet = ExcelHelper.createInitialSheet(workbook, title, headers, logoService);

            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.MEDIUM);
            borderStyle.setBorderBottom(BorderStyle.MEDIUM);
            borderStyle.setBorderLeft(BorderStyle.MEDIUM);
            borderStyle.setBorderRight(BorderStyle.MEDIUM);

            int rowNum = 4 + 1;
            for (MemberWiseCost data : memberWiseCostList.getContent()) {
                Row row = sheet.createRow(rowNum++);
                ExcelHelper.createCell(row, 0, data.getMemberName(), borderStyle);
                ExcelHelper.createCell(row, 1, data.getTrainingName(), borderStyle);
                ExcelHelper.createCell(row, 2, data.getTrainingCode(), borderStyle);
                ExcelHelper.createCell(row, 3, data.getCompetencyTypeName(), borderStyle);
                ExcelHelper.createCell(row, 4, data.getCompetencySubTypeName(), borderStyle);
                ExcelHelper.createCell(row, 5, data.getCoursesCount(), borderStyle);
                ExcelHelper.createCell(row, 6, data.getTotalCost(), borderStyle);
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
    public HRMSBaseResponse<List<TopicWiseCost>> getCostTrainingWise(String keyword, Pageable pageable, String reportType) throws HRMSException {
        validator.validateCostReport(null, reportType);
        HRMSBaseResponse<List<TopicWiseCost>> response = new HRMSBaseResponse<>();
        Page<TopicWiseCost> topicWiseCostList = getTopicWiseCostList(keyword, pageable, reportType);
        response.setResponseBody(topicWiseCostList.getContent());
        response.setResponseCode(1200);
        response.setTotalRecord(topicWiseCostList.getTotalElements());
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public byte[] getCostTrainingWiseExcel(String viewType, String keyword, Pageable pageable, String reportType) throws HRMSException {
        validator.validateCostReport(viewType, reportType);
        Page<TopicWiseCost> topicWiseCostList = getTopicWiseCostList(keyword, pageable, reportType);
        if (HRMSHelper.isNullOrEmpty(topicWiseCostList)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
        }
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            String[] headers = {"Training Name", "Training Code", "Competency Type Name",
                    "Competency SubType Name", "Member Name", "Member Count", "Total Cost"};
            String title = "TrainingWiseCost_Report_" + reportType.toUpperCase();
            Sheet sheet = ExcelHelper.createInitialSheet(workbook, title, headers, logoService);
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.MEDIUM);
            borderStyle.setBorderBottom(BorderStyle.MEDIUM);
            borderStyle.setBorderLeft(BorderStyle.MEDIUM);
            borderStyle.setBorderRight(BorderStyle.MEDIUM);

            int rowNum = 4 + 1;
            for (TopicWiseCost data : topicWiseCostList.getContent()) {
                Row row = sheet.createRow(rowNum++);
                ExcelHelper.createCell(row, 0, data.getTrainingName(), borderStyle);
                ExcelHelper.createCell(row, 1, data.getTrainingCode(), borderStyle);
                ExcelHelper.createCell(row, 2, data.getCompetencyTypeName(), borderStyle);
                ExcelHelper.createCell(row, 3, data.getCompetencySubTypeName(), borderStyle);
                ExcelHelper.createCell(row, 4, data.getMemberName(), borderStyle);
                ExcelHelper.createCell(row, 5, data.getMemberCount(), borderStyle);
                ExcelHelper.createCell(row, 6, data.getTotalCost(), borderStyle);
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
    public HRMSBaseResponse<DashboardVo> getDashboard(String keyword, Pageable pageable) throws HRMSException {
        DashboardVo responseVo = new DashboardVo();

        Page<TopTrainingCourses> topTrainingCourses = getTopTrainingCourses(keyword, pageable);
        responseVo.setTopRequestedTopics(topTrainingCourses.getContent());

        Long yearId = iMasterYearDAO.getYearIdRunningYear();
        responseVo.setGroupVsIndividualCostSummary(approvedTrainingsDAO.groupIndividualCostSummary(yearId));

        responseVo.setBudgetUtilization(trainingBudgetDAO.getBudgetUtilization(yearId));
        if (responseVo.getBudgetUtilization() != null &&
                responseVo.getBudgetUtilization().getCurrencySymbol() != null) {
            responseVo.setCurrencySymbol(responseVo.getBudgetUtilization().getCurrencySymbol());
            responseVo.getBudgetUtilization().setCurrencySymbol(null);
        }
        var trainingCountSummary = new GroupVsIndividualTrainingCountSummary();
        trainingCountSummary.setTotalTrainingCount(approvedTrainingsDAO.getTotalTraining(yearId));
        trainingCountSummary.setGroupTrainingCount(approvedTrainingsDAO.getTotalGroupTraining(yearId));
        trainingCountSummary.setIndividualTrainingCount(approvedTrainingsDAO.getTotalIndividualTraining(yearId));
        responseVo.setGroupVsIndividualTrainingCountSummary(trainingCountSummary);

        HRMSBaseResponse<DashboardVo> response = new HRMSBaseResponse<>();
        response.setResponseBody(responseVo);
        response.setResponseCode(1200);
        response.setTotalRecord(topTrainingCourses.getTotalElements());
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<List<ParticipantsClustersVo>> getParticipantsClusters(ParticipantsClustersListReq request,
                                                                                  Pageable pageable) throws HRMSException {
        validator.validateGetParticipantsClusters(request);
        HRMSBaseResponse<List<ParticipantsClustersVo>> response = new HRMSBaseResponse<>();
        Long yearId = iMasterYearDAO.getYearIdRunningYear();

        String keywordU;
        if (!HRMSHelper.isNullOrEmpty(request.getKeyword())) {
            keywordU = request.getKeyword().replaceAll(" ", "%").toLowerCase();
        } else {
            keywordU = null;
        }
        Page<ParticipantsClustersVo> participantClusters;
        if (request.getSortType().equals("asc")) {
            if (request.getSortBy().equals("memberCount")) {
                participantClusters = approvedTrainingsDAO
                        .getParticipantClustersCountAsc(keywordU, request.getIsInternal(), request.getPriority(),
                                request.getTrainingType(), pageable, yearId);
            } else {
                participantClusters = approvedTrainingsDAO
                        .getParticipantClustersCostAsc(keywordU, request.getIsInternal(), request.getPriority(),
                                request.getTrainingType(), pageable, yearId);
            }
        } else {
            if (request.getSortBy().equals("memberCount")) {
                participantClusters = approvedTrainingsDAO
                        .getParticipantClustersCountDesc(keywordU, request.getIsInternal(), request.getPriority(),
                                request.getTrainingType(), pageable, yearId);
            } else {
                participantClusters = approvedTrainingsDAO
                        .getParticipantClustersCostDesc(keywordU, request.getIsInternal(), request.getPriority(),
                                request.getTrainingType(), pageable, yearId);
            }
        }

        List<ParticipantsClustersVo> particilantClusterlist = participantClusters.getContent();

        for (ParticipantsClustersVo clusterVo : particilantClusterlist) {
            List<Long> empIdList = approvedTrainingsDAO.getAllEmployeeIdsByGroupCode(clusterVo.getTrainingGroupCode(), yearId);
            List<IdpOrganizationEmployeeView> employeesList = idpOrganizationEmployeeViewDAO.findByEmployeeIdIn(empIdList);

            for (IdpOrganizationEmployeeView emp : employeesList) {
                ParticipantsClusterDetailsVo detailsVo = getParticipantsClusterDetailsVo(emp);
                clusterVo.getParticipantDetails().add(detailsVo);
            }

        }

        response.setResponseBody(particilantClusterlist);
        response.setResponseCode(1200);
        response.setTotalRecord(participantClusters.getTotalElements());
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public byte[] getParticipantsClustersExcel(ParticipantsClustersListReq request) throws HRMSException {
        validator.validateGetParticipantsClusters(request);
        Long yearId = iMasterYearDAO.getYearIdRunningYear();
        String keywordU;
        if (!HRMSHelper.isNullOrEmpty(request.getKeyword())) {
            keywordU = request.getKeyword().replaceAll(" ", "%").toLowerCase();
        } else {
            keywordU = null;
        }
        List<ParticipantsClustersVo> participantClusters = approvedTrainingsDAO
                .getParticipantClustersExcel(keywordU, request.getIsInternal(), request.getPriority(),
                        request.getTrainingType(), yearId);

        for (ParticipantsClustersVo clusterVo : participantClusters) {
            List<Long> empIdList = approvedTrainingsDAO.getAllEmployeeIdsByGroupCode(clusterVo.getTrainingGroupCode(), yearId);
            List<IdpOrganizationEmployeeView> employeesList = idpOrganizationEmployeeViewDAO.findByEmployeeIdIn(empIdList);
            for (IdpOrganizationEmployeeView emp : employeesList) {
                ParticipantsClusterDetailsVo detailsVo = getParticipantsClusterDetailsVo(emp);
                clusterVo.getParticipantDetails().add(detailsVo);
            }
        }

        if (HRMSHelper.isNullOrEmpty(participantClusters)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
        }
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            String[] headers = {"id", "trainingCode", "trainingName",
                    "isInternal", "priority", "trainingType", "trainingGroupCode",
                    "trainingCost", "memberCount", "totalCost", "employeeId",
                    "employeeCode", "employeeName", "designationName", "departmentName",
                    "divisionName", "grade"};
            String title = "ParticipantClusters_Report";
            Sheet sheet = ExcelHelper.createInitialSheet(workbook, title, headers, logoService);
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.MEDIUM);
            borderStyle.setBorderBottom(BorderStyle.MEDIUM);
            borderStyle.setBorderLeft(BorderStyle.MEDIUM);
            borderStyle.setBorderRight(BorderStyle.MEDIUM);

            int rowNum = 4 + 1;
            for (ParticipantsClustersVo topic : participantClusters) {
                for (ParticipantsClusterDetailsVo participant : topic.getParticipantDetails()) {
                    Row row = sheet.createRow(rowNum++);
                    ExcelHelper.createCell(row, 0, topic.getId(), borderStyle);
                    ExcelHelper.createCell(row, 1, topic.getTrainingCode(), borderStyle);
                    ExcelHelper.createCell(row, 2, topic.getTrainingName(), borderStyle);
                    ExcelHelper.createCell(row, 3, topic.getIsInternal(), borderStyle);
                    ExcelHelper.createCell(row, 4, topic.getPriority(), borderStyle);
                    ExcelHelper.createCell(row, 5, topic.getTrainingType(), borderStyle);
                    ExcelHelper.createCell(row, 6, topic.getTrainingGroupCode(), borderStyle);
                    ExcelHelper.createCell(row, 7, topic.getTrainingCost(), borderStyle);
                    ExcelHelper.createCell(row, 8, topic.getMemberCount(), borderStyle);
                    ExcelHelper.createCell(row, 9, topic.getTotalCost(), borderStyle);
                    ExcelHelper.createCell(row, 10, participant.getEmployeeId(), borderStyle);
                    ExcelHelper.createCell(row, 11, participant.getEmployeeCode(), borderStyle);
                    ExcelHelper.createCell(row, 12, participant.getEmployeeName(), borderStyle);
                    ExcelHelper.createCell(row, 13, participant.getDesignationName(), borderStyle);
                    ExcelHelper.createCell(row, 14, participant.getDepartmentName(), borderStyle);
                    ExcelHelper.createCell(row, 15, participant.getDivisionName(), borderStyle);
                    ExcelHelper.createCell(row, 16, participant.getGrade(), borderStyle);
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

    private static ParticipantsClusterDetailsVo getParticipantsClusterDetailsVo(IdpOrganizationEmployeeView emp) {
        ParticipantsClusterDetailsVo detailsVo = new ParticipantsClusterDetailsVo();
        detailsVo.setEmployeeId(emp.getEmployeeId());
        detailsVo.setEmployeeCode(emp.getEmployeeCode());
        detailsVo.setEmployeeName(emp.getEmpFirstName() + " " +
                (emp.getEmpMiddleName() != null ? emp.getEmpMiddleName() + " " : "") +
                emp.getEmpLastName());
        detailsVo.setDesignationName(emp.getDesignationName());
        detailsVo.setDepartmentName(emp.getDepartmentName());
        detailsVo.setDivisionName(emp.getDivisionName());
        detailsVo.setGrade(emp.getGrade());
        return detailsVo;
    }

    private Page<TopTrainingCourses> getTopTrainingCourses(String keyword, Pageable pageable) {
        String keywordU;
        if (!HRMSHelper.isNullOrEmpty(keyword)) {
            keywordU = keyword.replaceAll(" ", "%").toLowerCase();
        } else {
            keywordU = null;
        }
        return trainingCatalogDAO.getTopTrainingCourses(keywordU, pageable);
    }

    private List<TopTrainingCourses> getTopTrainingCourses(String keyword) {
        String keywordU;
        if (!HRMSHelper.isNullOrEmpty(keyword)) {
            keywordU = keyword.replaceAll(" ", "%").toLowerCase();
        } else {
            keywordU = null;
        }
        return trainingCatalogDAO.getTopTrainingCoursesExcel(keywordU);
    }

    private Page<TopicWiseCost> getTopicWiseCostList(String keyword, Pageable pageable, String reportType) {
        String keywordU;
        if (!HRMSHelper.isNullOrEmpty(keyword)) {
            keywordU = keyword.replaceAll(" ", "%").toLowerCase();
        } else {
            keywordU = null;
        }
        if (reportType.equals(DETAILED.getKey())) {
            return idpTrainingDetailsDAO.getTopicWiseCostDetailed(keywordU, pageable);
        }
        return idpTrainingDetailsDAO.getTopicWiseCostSummary(keywordU, pageable);
    }

    private Page<MemberWiseCost> getMemberWiseCostList(String keyword, Pageable pageable, String reportType) {
        String keywordU;
        if (!HRMSHelper.isNullOrEmpty(keyword)) {
            keywordU = keyword.replaceAll(" ", "%").toLowerCase();
        } else {
            keywordU = null;
        }
        if (reportType.equals(DETAILED.getKey())) {
            return idpTrainingDetailsDAO.getMemberWiseCostDetailed(keywordU, pageable);
        }
        return idpTrainingDetailsDAO.getMemberWiseCostSummary(keywordU, pageable);

    }
}
