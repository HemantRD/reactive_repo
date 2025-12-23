package com.vinsys.hrms.idp.t_types.service;

import com.vinsys.hrms.dao.idp.TTypesDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.idp.TTypes;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.reports.helper.ExcelHelper;
import com.vinsys.hrms.idp.t_types.validator.TTypesValidator;
import com.vinsys.hrms.idp.t_types.vo.TTypesStatus;
import com.vinsys.hrms.idp.t_types.vo.TTypesVo;
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
public class TTypesImpl implements TTypesService {

    private final BackendProperties props;
    private final TTypesValidator validator;
    private final TTypesDAO tTypesDAO;
    private final LogoService logoService;
    private final TrainingCatalogHelper trainingCatalogHelper;


    public TTypesImpl(final BackendProperties props, final TTypesValidator validator,
                      final TTypesDAO tTypesDAO, final LogoService logoService,
                      final TrainingCatalogHelper trainingCatalogHelper) {
        this.props = props;
        this.validator = validator;
        this.tTypesDAO = tTypesDAO;
        this.logoService = logoService;
        this.trainingCatalogHelper = trainingCatalogHelper;
    }

    @Transactional
    public HRMSBaseResponse<String> addUpdateTrainingType(TTypesVo tTypesVo) throws HRMSException {
        validator.validateAddUpdate(tTypesVo);
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        if (tTypesVo.getId() == null) {
            tTypesDAO.save(populateTrainingType(tTypesVo, null));
        } else {
            TTypes dbTType = tTypesDAO.getById(tTypesVo.getId());
            tTypesDAO.saveAndFlush(populateTrainingType(tTypesVo, dbTType));
        }
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Transactional
    public HRMSBaseResponse<String> updateTrainingTypeStatus(TTypesStatus tTypesStatus) throws HRMSException {
        validator.updateTrainingTypeStatus(tTypesStatus);
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        TTypes dbTType = tTypesDAO.getById(tTypesStatus.getId());
        dbTType.setIsActive(tTypesStatus.getStatus() ? "Y" : "N");
        tTypesDAO.saveAndFlush(dbTType);
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<TTypesVo> getById(Long id) throws HRMSException {
        validator.validateGetById(id);
        HRMSBaseResponse<TTypesVo> response = new HRMSBaseResponse<>();
        TTypes dbTType = tTypesDAO.getById(id);
        response.setResponseBody(populateTrainingTypeVo(dbTType));
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<List<TTypesVo>> getAllTrainingTypes(String keyword, Pageable pageable) throws HRMSException {
        keyword = keyword == null ? keyword : keyword.toLowerCase();
        Page<TTypesVo> classificationList =
                tTypesDAO.findTrainingTypesByPage(keyword, pageable);
        return trainingCatalogHelper.getResponse(classificationList.getContent(), classificationList.getTotalElements());
    }

    @Override
    public byte[] getAllTrainingTypesExcel(String keyword) throws HRMSException {
        keyword = keyword == null ? keyword : keyword.toLowerCase();
        List<TTypesVo> classificationList = tTypesDAO.findTrainingTypesExcel(keyword);
        if (HRMSHelper.isNullOrEmpty(classificationList)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
        }
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            String[] headers = {"id", "name", "remark", "status"};
            String title = "TrainingTypesReport";
            Sheet sheet = ExcelHelper.createInitialSheet(workbook, title, headers, logoService);
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.MEDIUM);
            borderStyle.setBorderBottom(BorderStyle.MEDIUM);
            borderStyle.setBorderLeft(BorderStyle.MEDIUM);
            borderStyle.setBorderRight(BorderStyle.MEDIUM);

            int rowNum = 4 + 1;
            for (TTypesVo data : classificationList) {
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

    public TTypes populateTrainingType(TTypesVo tTypesVo, TTypes dbTType) {
        TTypes tTypes = dbTType;
        Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
        if (tTypes == null) {
            tTypes = new TTypes();
            tTypes.setCreatedBy(employeeId.toString());
            tTypes.setCreatedDate(new Date());
        }
        tTypes.setName(tTypesVo.getName());
        tTypes.setIsActive("Y");
        tTypes.setUpdatedBy(employeeId.toString());
        tTypes.setUpdatedDate(new Date());
        tTypes.setRemark(tTypesVo.getRemark());
        tTypes.setOrgId(orgId);
        return tTypes;
    }

    public TTypesVo populateTrainingTypeVo(TTypes dbTType) {
        TTypesVo tTypesVo = new TTypesVo();
        tTypesVo.setId(dbTType.getId());
        tTypesVo.setName(dbTType.getName());
        tTypesVo.setStatus(dbTType.getIsActive().equalsIgnoreCase("Y"));
        tTypesVo.setRemark(dbTType.getRemark());
        return tTypesVo;
    }
}
