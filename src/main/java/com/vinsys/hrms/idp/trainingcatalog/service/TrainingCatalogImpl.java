package com.vinsys.hrms.idp.trainingcatalog.service;

import com.vinsys.hrms.dao.idp.CompetencySubTypesDAO;
import com.vinsys.hrms.dao.idp.CompetencyTypesDAO;
import com.vinsys.hrms.dao.idp.TrainingBudgetDAO;
import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.idp.TrainingBudget;
import com.vinsys.hrms.entity.idp.TrainingCatalog;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.reports.helper.ExcelHelper;
import com.vinsys.hrms.idp.trainingcatalog.util.TrainingCatalogHelper;
import com.vinsys.hrms.idp.trainingcatalog.validator.TrainingCatalogValidator;
import com.vinsys.hrms.idp.trainingcatalog.vo.*;
import com.vinsys.hrms.logo.service.LogoService;
import com.vinsys.hrms.spring.BackendProperties;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TrainingCatalogImpl implements ITrainingCatalogService {

    private final BackendProperties props;
    private final TrainingCatalogValidator validator;
    private final TrainingCatalogDAO trainingCatalogDAO;
    private final CompetencyTypesDAO competencyTypesDAO;
    private final CompetencySubTypesDAO competencySubTypesDAO;
    private final TrainingBudgetDAO trainingBudgetDAO;
    private final TrainingCatalogHelper helper;
    private final LogoService logoService;


    public TrainingCatalogImpl(final BackendProperties props, final TrainingCatalogValidator validator,
                               final TrainingCatalogDAO trainingCatalogDAO, final CompetencyTypesDAO competencyTypesDAO,
                               final CompetencySubTypesDAO competencySubTypesDAO, final TrainingBudgetDAO trainingBudgetDAO,
                               final TrainingCatalogHelper helper, final LogoService logoService) {
        this.props = props;
        this.validator = validator;
        this.trainingCatalogDAO = trainingCatalogDAO;
        this.competencyTypesDAO = competencyTypesDAO;
        this.competencySubTypesDAO = competencySubTypesDAO;
        this.trainingBudgetDAO = trainingBudgetDAO;
        this.helper = helper;
        this.logoService = logoService;
    }

    @Transactional
    public HRMSBaseResponse<String> addUpdateTrainingCatalog(TrainingCatalogVo trainingCatalogVo) throws HRMSException {
        validator.validateAddUpdate(trainingCatalogVo);
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        TrainingCatalog catalog;
        if (trainingCatalogVo.getId() == null) {
            catalog = trainingCatalogDAO.save(helper.populateTrainingCatalog(trainingCatalogVo, null));
        } else {
            TrainingCatalog dbCatalog = trainingCatalogDAO.getById(trainingCatalogVo.getId());
            catalog = trainingCatalogDAO.saveAndFlush(helper.populateTrainingCatalog(trainingCatalogVo, dbCatalog));
        }
        helper.saveKeywords(trainingCatalogVo.getKeywords(), catalog.getId());
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<TrainingCatalogVo> getById(Long id) throws HRMSException {
        validator.validateGetById(id);
        HRMSBaseResponse<TrainingCatalogVo> response = new HRMSBaseResponse<>();
        TrainingCatalog dbCatalog = trainingCatalogDAO.getById(id);
        response.setResponseBody(helper.populateTrainingCatalogVo(dbCatalog));
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<List<TrainingCatalogVo>> getAllTrainingCatalogs(TrainingCatalogListingReq request,
                                                                            Pageable pageable) throws HRMSException {
        validator.getAllTrainingCatalogs(request);
        Pageable pageableSortBy;
        if (request.getSortType().equals("asc"))
            pageableSortBy = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(request.getSortBy()).ascending());
        else
            pageableSortBy = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(request.getSortBy()).descending());
        String keywordsU;
        if (!HRMSHelper.isNullOrEmpty(request.getKeyword())) {
            keywordsU = request.getKeyword().replaceAll(" ", "%").toLowerCase();
        } else {
            keywordsU = null;
        }
        Page<TrainingCatalogVo> trainingCatalogList =
                trainingCatalogDAO.findTrainingCatalogsByPage(keywordsU, request.getIsInternal(),
                        request.getIsCertificationCourse(), request.getStatus() ? "Y" : "N", pageableSortBy);
        return helper.getResponse(trainingCatalogList.getContent(), trainingCatalogList.getTotalElements());
    }

    @Override
    public byte[] getAllTrainingCatalogsExcel(TrainingCatalogListingReq request) throws HRMSException {
        validator.getAllTrainingCatalogs(request);
        Sort sort;
        if (request.getSortType().equals("asc"))
            sort = Sort.by(request.getSortBy()).ascending();
        else
            sort = Sort.by(request.getSortBy()).descending();
        String keywordsU;
        if (!HRMSHelper.isNullOrEmpty(request.getKeyword())) {
            keywordsU = request.getKeyword().replaceAll(" ", "%").toLowerCase();
        } else {
            keywordsU = null;
        }
        List<TrainingCatalogVo> trainingCatalogList =
                trainingCatalogDAO.findTrainingCatalogsByPageExcel(keywordsU, request.getIsInternal(),
                        request.getIsCertificationCourse(), request.getStatus() ? "Y" : "N", sort);

        if (HRMSHelper.isNullOrEmpty(trainingCatalogList)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
        }
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            String[] headers = {"id", "trainingCode", "topicName",
                    "competencyTypeId", "competencySubTypeId", "trainingTypeId", "trainingClassificationId",
                    "isInternal", "costPerPersonIndividual", "costPerPersonGroup",
                    "costPerGroup", "minPersonInGroup", "maxPersonInGroup",
                    "isCertificationCourse", "priority", "durationInHours", "remark", "status"};
            String title = "TrainingCatalogList_Report";
            Sheet sheet = ExcelHelper.createInitialSheet(workbook, title, headers, logoService);
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.MEDIUM);
            borderStyle.setBorderBottom(BorderStyle.MEDIUM);
            borderStyle.setBorderLeft(BorderStyle.MEDIUM);
            borderStyle.setBorderRight(BorderStyle.MEDIUM);

            int rowNum = 4 + 1;
            for (TrainingCatalogVo data : trainingCatalogList) {
                Row row = sheet.createRow(rowNum++);
                ExcelHelper.createCell(row, 0, data.getId(), borderStyle);
                ExcelHelper.createCell(row, 1, data.getTrainingCode(), borderStyle);
                ExcelHelper.createCell(row, 2, data.getTopicName(), borderStyle);
                ExcelHelper.createCell(row, 3, data.getCompetencyTypeId(), borderStyle);
                ExcelHelper.createCell(row, 4, data.getCompetencySubTypeId(), borderStyle);
                ExcelHelper.createCell(row, 5, data.getTrainingTypeId(), borderStyle);
                ExcelHelper.createCell(row, 6, data.getTrainingClassificationId(), borderStyle);
                ExcelHelper.createCell(row, 7, data.getInternal(), borderStyle);
                ExcelHelper.createCell(row, 8, data.getCostPerPersonIndividual(), borderStyle);
                ExcelHelper.createCell(row, 9, data.getCostPerPersonGroup(), borderStyle);
                ExcelHelper.createCell(row, 10, data.getCostPerGroup(), borderStyle);
                ExcelHelper.createCell(row, 11, data.getMinPersonInGroup(), borderStyle);
                ExcelHelper.createCell(row, 12, data.getMaxPersonInGroup(), borderStyle);
                ExcelHelper.createCell(row, 13, data.getCertificationCourse(), borderStyle);
                ExcelHelper.createCell(row, 14, data.getPriority(), borderStyle);
                ExcelHelper.createCell(row, 15, data.getDurationInHours(), borderStyle);
                ExcelHelper.createCell(row, 16, data.getRemark(), borderStyle);
                ExcelHelper.createCell(row, 17, data.getStatus(), borderStyle);
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

    @Override
    public HRMSBaseResponse<List<CompetencyDDLVo>> getCompetencyTypes() {
        HRMSBaseResponse<List<CompetencyDDLVo>> response = new HRMSBaseResponse<>();
        response.setResponseBody(competencyTypesDAO.getCompetencyTypes());
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<List<CompetencyDDLVo>> getCompetencySubTypes(Integer id) {
        HRMSBaseResponse<List<CompetencyDDLVo>> response = new HRMSBaseResponse<>();
        response.setResponseBody(competencySubTypesDAO.getCompetencyTypes(id));
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @Override
    public HRMSBaseResponse<List<SearchTopicsVo>> searchTopics(SearchTopicReq request, String keyword, Pageable pageable) throws HRMSException {
        validator.searchTopics(request);
        List<String> allKeywords = trainingCatalogDAO.getDistinctKeywords();
        String linePayload = request.getLine().toLowerCase();
        List<String> dbWords = new ArrayList<>();
        for (String word : allKeywords) {
            if (linePayload.contains(word)) {
                dbWords.add(word);
            }
        }
        Page<SearchTopicsVo> trainingCatalogList = trainingCatalogDAO.getSearchTrainingTopics(keyword,
                pageable, dbWords);
        HRMSBaseResponse<List<SearchTopicsVo>> response = new HRMSBaseResponse<>();
        response.setResponseBody(trainingCatalogList.getContent().stream()
                .sorted(Comparator.comparingLong(SearchTopicsVo::getTotalCount).reversed()).collect(Collectors.toList()));
        response.setResponseCode(1200);
        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
        response.setApplicationVersion(props.getApp_version());
        response.setTotalRecord(trainingCatalogList.getTotalElements());
        return response;
    }

    @Transactional
    public HRMSBaseResponse<String> addUpdateTrainingBudget(TrainingBudgetVo trainingBudgetVo) throws HRMSException {
        validator.validateAddUpdateBudget(trainingBudgetVo);
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        if (trainingBudgetVo.getId() == null) {
            trainingBudgetDAO.save(helper.populateTrainingBudget(trainingBudgetVo, null));
        } else {
            TrainingBudget dbBudget = trainingBudgetDAO.getById(trainingBudgetVo.getId());
            trainingBudgetDAO.saveAndFlush(helper.populateTrainingBudget(trainingBudgetVo, dbBudget));
        }
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    public HRMSBaseResponse<String> updateTrainingBudgetStatus(TrainingBudgetStatus trainingBudgetStatus) throws HRMSException {
        validator.validateUpdateTrainingBudgetStatus(trainingBudgetStatus);
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        TrainingBudget budget = trainingBudgetDAO.getById(trainingBudgetStatus.getId());
        budget.setIsActive(trainingBudgetStatus.getStatus() ? "Y" : "N");
        trainingBudgetDAO.saveAndFlush(budget);
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    public HRMSBaseResponse<List<TrainingBudgetVo>> getAllTrainingBudgets(Integer year, Pageable pageable) {
        Page<TrainingBudgetVo> trainingCatalogList = trainingBudgetDAO.findTrainingBudgetsByPage(year, pageable);
        return helper.getResponse(trainingCatalogList.getContent(), trainingCatalogList.getTotalElements());
    }

    public byte[] getAllTrainingBudgetsExcel(Integer year) throws HRMSException {
        List<TrainingBudgetVo> trainingCatalogList = trainingBudgetDAO.findTrainingBudgetsExcel(year);
        if (HRMSHelper.isNullOrEmpty(trainingCatalogList)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
        }
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            String[] headers = {"id", "year", "budgetAmount",
                    "idpRequestAmount", "consumedAmount", "currencySymbol", "remark",
                    "createdDate", "updatedDate"};
            String title = "TrainingBudgetList_Report";
            Sheet sheet = ExcelHelper.createInitialSheet(workbook, title, headers, logoService);
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.MEDIUM);
            borderStyle.setBorderBottom(BorderStyle.MEDIUM);
            borderStyle.setBorderLeft(BorderStyle.MEDIUM);
            borderStyle.setBorderRight(BorderStyle.MEDIUM);

            int rowNum = 4 + 1;
            for (TrainingBudgetVo data : trainingCatalogList) {
                Row row = sheet.createRow(rowNum++);
                ExcelHelper.createCell(row, 0, data.getId(), borderStyle);
                ExcelHelper.createCell(row, 1, data.getYear(), borderStyle);
                ExcelHelper.createCell(row, 2, data.getBudgetAmount(), borderStyle);
                ExcelHelper.createCell(row, 3, data.getIdpRequestAmount(), borderStyle);
                ExcelHelper.createCell(row, 4, data.getConsumedAmount(), borderStyle);
                ExcelHelper.createCell(row, 5, data.getCurrencySymbol(), borderStyle);
                ExcelHelper.createCell(row, 6, data.getRemark(), borderStyle);
                ExcelHelper.createCell(row, 7, data.getCreatedDate(), borderStyle);
                ExcelHelper.createCell(row, 8, data.getUpdatedDate(), borderStyle);
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

    @Transactional
    public HRMSBaseResponse<String> updateTrainingStatus(TrainingCatalogStatus trainingCatalogStatus) throws HRMSException {
        validator.updateTrainingStatus(trainingCatalogStatus);
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        TrainingCatalog catalog = trainingCatalogDAO.getById(trainingCatalogStatus.getId());
        catalog.setIsActive(trainingCatalogStatus.getStatus() ? "Y" : "N");
        trainingCatalogDAO.saveAndFlush(catalog);
        response.setResponseCode(1200);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }
}
