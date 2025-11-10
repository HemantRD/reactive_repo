package com.vinsys.hrms.idp.reports.service;

import com.vinsys.hrms.constants.ELogo;
import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.reports.helper.ExcelHelper;
import com.vinsys.hrms.idp.reports.validator.IdpReportsValidator;
import com.vinsys.hrms.idp.reports.vo.TopTrainingCourses;
import com.vinsys.hrms.logo.entity.Logo;
import com.vinsys.hrms.logo.service.LogoService;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class IdpReportsServiceImpl implements IIdpReportsService {

    private final BackendProperties props;
    private final IdpReportsValidator validator;
    private final LogoService logoService;
    private final TrainingCatalogDAO trainingCatalogDAO;

    public IdpReportsServiceImpl(final BackendProperties props, final IdpReportsValidator validator,
                                 final LogoService logoService, final TrainingCatalogDAO trainingCatalogDAO) {
        this.props = props;
        this.validator = validator;
        this.logoService = logoService;
        this.trainingCatalogDAO = trainingCatalogDAO;
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
    public HRMSBaseResponse<List<TopTrainingCourses>> getCostMemberWise(String keyword, Pageable pageable) throws HRMSException {
        return null;
    }

    @Override
    public byte[] getCostMemberWiseExcel(String viewType, String keyword, Pageable pageable) throws HRMSException {
        return new byte[0];
    }

    @Override
    public HRMSBaseResponse<List<TopTrainingCourses>> getCostTrainingWise(String keyword, Pageable pageable) throws HRMSException {
        return null;
    }

    @Override
    public byte[] getCostTrainingWiseExcel(String viewType, String keyword, Pageable pageable) throws HRMSException {
        return new byte[0];
    }

    private Page<TopTrainingCourses> getTopTrainingCourses(String keyword, Pageable pageable) {
        return trainingCatalogDAO.getTopTrainingCourses(keyword, pageable);
    }
}
