package com.vinsys.hrms.idp.t_classifications.service;

import com.vinsys.hrms.dao.idp.TClassificationsDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.idp.TClassifications;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.reports.helper.ExcelHelper;
import com.vinsys.hrms.idp.t_classifications.validator.TClassificationsValidator;
import com.vinsys.hrms.idp.t_classifications.vo.TClassificationsStatus;
import com.vinsys.hrms.idp.t_classifications.vo.TClassificationsVo;
import com.vinsys.hrms.idp.trainingcatalog.util.TrainingCatalogHelper;
import com.vinsys.hrms.logo.service.LogoService;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TClassificationsImpl implements TClassificationsService {

    private final BackendProperties props;
    private final TClassificationsValidator validator;
    private final TClassificationsDAO tClassificationsDAO;
    private final LogoService logoService;
    private final TrainingCatalogHelper trainingCatalogHelper;


    public TClassificationsImpl(final BackendProperties props, final TClassificationsValidator validator,
                                final TClassificationsDAO tClassificationsDAO, final LogoService logoService,
                                final TrainingCatalogHelper trainingCatalogHelper) {
        this.props = props;
        this.validator = validator;
        this.tClassificationsDAO = tClassificationsDAO;
        this.logoService = logoService;
        this.trainingCatalogHelper = trainingCatalogHelper;
    }

    @Transactional
    public HRMSBaseResponse<String> addUpdateTrainingClassification(TClassificationsVo tClassificationsVo) throws HRMSException {
        validator.validateAddUpdate(tClassificationsVo);
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        if (tClassificationsVo.getId() == null) {
            tClassificationsDAO.save(populateTrainingClassifications(tClassificationsVo, null));
        } else {
            TClassifications dbClassifications = tClassificationsDAO.getById(tClassificationsVo.getId());
            tClassificationsDAO.saveAndFlush(populateTrainingClassifications(tClassificationsVo, dbClassifications));
        }
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Transactional
    public HRMSBaseResponse<String> updateTrainingClassificationStatus(TClassificationsStatus tClassificationsStatus) throws HRMSException {
        validator.updateTrainingClassificationStatus(tClassificationsStatus);
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        TClassifications dbClassifications = tClassificationsDAO.getById(tClassificationsStatus.getId());
        dbClassifications.setIsActive(tClassificationsStatus.getStatus() ? "Y" : "N");
        tClassificationsDAO.saveAndFlush(dbClassifications);
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<TClassificationsVo> getById(Long id) throws HRMSException {
        validator.validateGetById(id);
        HRMSBaseResponse<TClassificationsVo> response = new HRMSBaseResponse<>();
        TClassifications dbClassifications = tClassificationsDAO.getById(id);
        response.setResponseBody(populateTrainingClassificationsVo(dbClassifications));
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<List<TClassificationsVo>> getAllTrainingClassifications(String keyword, Pageable pageable) throws HRMSException {
        keyword = keyword == null ? keyword : keyword.toLowerCase();
        Page<TClassificationsVo> classificationList =
                tClassificationsDAO.findTrainingClassificationsByPage(keyword, pageable);
        return trainingCatalogHelper.getResponse(classificationList.getContent(), classificationList.getTotalElements());
    }

    @Override
    public byte[] getAllTrainingClassificationsExcel(String keyword) throws HRMSException {
        keyword = keyword == null ? keyword : keyword.toLowerCase();
        List<TClassificationsVo> classificationList = tClassificationsDAO.findTrainingClassificationsExcel(keyword);
        if (HRMSHelper.isNullOrEmpty(classificationList)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
        }
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            String[] headers = {"id", "name", "remark", "status"};
            String title = "TrainingClassificationsReport";
            Sheet sheet = ExcelHelper.createInitialSheet(workbook, title, headers, logoService);
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.MEDIUM);
            borderStyle.setBorderBottom(BorderStyle.MEDIUM);
            borderStyle.setBorderLeft(BorderStyle.MEDIUM);
            borderStyle.setBorderRight(BorderStyle.MEDIUM);

            int rowNum = 4 + 1;
            for (TClassificationsVo data : classificationList) {
                Row row = sheet.createRow(rowNum++);
                ExcelHelper.createCell(row, 0, data.getId(), borderStyle);
                ExcelHelper.createCell(row, 1, data.getName(), borderStyle);
                ExcelHelper.createCell(row, 2, data.getRemark(), borderStyle);
                ExcelHelper.createCell(row, 3, data.getStatus(), borderStyle);
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error(ResponseCode.getResponseCodeMap().get(1783), e);
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1783));
        }
    }

    public TClassifications populateTrainingClassifications(TClassificationsVo tClassificationsVo,
                                                            TClassifications dbClassifications) {
        TClassifications classification = dbClassifications;
        Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
        if (classification == null) {
            classification = new TClassifications();
            classification.setCreatedBy(employeeId.toString());
            classification.setCreatedDate(new Date());
        }
        classification.setName(tClassificationsVo.getName());
        classification.setIsActive("Y");
        classification.setUpdatedBy(employeeId.toString());
        classification.setUpdatedDate(new Date());
        classification.setRemark(tClassificationsVo.getRemark());
        classification.setOrgId(orgId);
        return classification;
    }

    public TClassificationsVo populateTrainingClassificationsVo(TClassifications dbClassifications) {
        TClassificationsVo tClassificationsVo = new TClassificationsVo();
        tClassificationsVo.setId(dbClassifications.getId());
        tClassificationsVo.setName(dbClassifications.getName());
        tClassificationsVo.setStatus(dbClassifications.getIsActive().equalsIgnoreCase("Y"));
        tClassificationsVo.setRemark(dbClassifications.getRemark());
        return tClassificationsVo;
    }
}
