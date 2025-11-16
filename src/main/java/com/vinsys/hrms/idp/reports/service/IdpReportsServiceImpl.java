package com.vinsys.hrms.idp.reports.service;

import com.vinsys.hrms.dao.idp.IdpTrainingDetailsDAO;
import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.reports.helper.ExcelHelper;
import com.vinsys.hrms.idp.reports.validator.IdpReportsValidator;
import com.vinsys.hrms.idp.reports.vo.MemberWiseCost;
import com.vinsys.hrms.idp.reports.vo.TopTrainingCourses;
import com.vinsys.hrms.idp.reports.vo.TopicWiseCost;
import com.vinsys.hrms.logo.service.LogoService;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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


    public IdpReportsServiceImpl(final BackendProperties props, final IdpReportsValidator validator,
                                 final LogoService logoService, final TrainingCatalogDAO trainingCatalogDAO,
                                 final IdpTrainingDetailsDAO idpTrainingDetailsDAO) {
        this.props = props;
        this.validator = validator;
        this.logoService = logoService;
        this.trainingCatalogDAO = trainingCatalogDAO;
        this.idpTrainingDetailsDAO = idpTrainingDetailsDAO;
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

    public byte[] getTopTrainingRequestedExcel(String viewType, String keyword, Pageable pageable) throws HRMSException {
        validator.validateTopTrainingCoursesExcel(viewType);
        Page<TopTrainingCourses> reportData = getTopTrainingCourses(keyword, pageable);
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
            for (TopTrainingCourses data : reportData.getContent()) {
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
        validator.validateTopTrainingCoursesExcel(viewType);
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
        validator.validateTopTrainingCoursesExcel(viewType);
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

    private Page<TopTrainingCourses> getTopTrainingCourses(String keyword, Pageable pageable) {
        return trainingCatalogDAO.getTopTrainingCourses(keyword, pageable);
    }

    private Page<TopicWiseCost> getTopicWiseCostList(String keyword, Pageable pageable, String reportType) {
        if (reportType.equals(DETAILED.getKey())) {
            return idpTrainingDetailsDAO.getTopicWiseCostDetailed(keyword, pageable);
        }
        return idpTrainingDetailsDAO.getTopicWiseCostSummary(keyword, pageable);
    }

    private Page<MemberWiseCost> getMemberWiseCostList(String keyword, Pageable pageable, String reportType) {
        if (reportType.equals(DETAILED.getKey())) {
            return idpTrainingDetailsDAO.getMemberWiseCostDetailed(keyword, pageable);
        }
        return idpTrainingDetailsDAO.getMemberWiseCostSummary(keyword, pageable);

    }
}
