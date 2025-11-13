package com.vinsys.hrms.idp.progress.validator;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.idp.IdpEntityDAO;
import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.directonboard.vo.Counter;
import com.vinsys.hrms.directonboard.vo.ExcelDataVo;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.progress.util.ExcelFileIndexEnum;
import com.vinsys.hrms.idp.progress.vo.BulkExcelProgressVo;
import com.vinsys.hrms.idp.reports.helper.ExcelHelper;
import com.vinsys.hrms.idp.utils.IdpEnums;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class ProgressValidator {

    private final IHRMSEmployeeDAO employeeDAO;
    private final IdpEntityDAO idpEntityDAO;


    public ProgressValidator(final IHRMSEmployeeDAO employeeDAO, final IdpEntityDAO idpEntityDAO) {
        this.employeeDAO = employeeDAO;
        this.idpEntityDAO = idpEntityDAO;
    }

    public List<BulkExcelProgressVo> validateExcelFileData(MultipartFile excelFile) throws HRMSException, Exception {
        String fileName = excelFile.getOriginalFilename().toLowerCase();
        if (!fileName.contains(".xlsx") && !fileName.contains(".xlsm")
                && !fileName.contains(".xls")) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1571));
        }
        Workbook workbook;
        if (fileName.contains(".xlsx") || fileName.contains(".xlsm")) {
            workbook = new XSSFWorkbook(excelFile.getInputStream());
        } else {
            workbook = new HSSFWorkbook(excelFile.getInputStream());
        }
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        int rowCounter = 0;
        List<BulkExcelProgressVo> bulkExcelProgressVoList = new ArrayList<>();
        for (Iterator<Row> it = rows; it.hasNext(); ) {
            rowCounter += 1;
            Row row = it.next();
            BulkExcelProgressVo bulkExcelProgressVo = new BulkExcelProgressVo();
            bulkExcelProgressVo.setMemberEmail(ExcelHelper.getString(row, rowCounter, ExcelFileIndexEnum.MEMBER_EMAIL));
            bulkExcelProgressVo.setTrainingCode(ExcelHelper.getString(row, rowCounter, ExcelFileIndexEnum.TRAINING_CODE));
            bulkExcelProgressVo.setProgressDate(ExcelHelper.getDate(row, rowCounter, ExcelFileIndexEnum.PROGRESS_DATE));
            bulkExcelProgressVo.setProgressValue(ExcelHelper.getNumber(row, rowCounter, ExcelFileIndexEnum.PROGRESS_VALUE));
            bulkExcelProgressVo.setProgressUnit(ExcelHelper.getString(row, rowCounter, ExcelFileIndexEnum.PROGRESS_UNIT));
            bulkExcelProgressVo.setRemark(ExcelHelper.getString(row, rowCounter, ExcelFileIndexEnum.REMARK));
            bulkExcelProgressVo.setStatus(ExcelHelper.getString(row, rowCounter, ExcelFileIndexEnum.STATUS));
            bulkExcelProgressVoList.add(bulkExcelProgressVo);
            Employee employee = employeeDAO.findByofficialEmailId(bulkExcelProgressVo.getMemberEmail());
            if (employee == null) {
                closeSheetAndThrow(workbook, rowCounter, "MemberEmail");
            }
            if (!idpEntityDAO.existsByEmployeeIdAndRecordStatus(employee.getId(), "Y")) {
                closeSheetAndThrow(workbook, rowCounter, "MemberEmail");
            }
        }
        workbook.close();
        return bulkExcelProgressVoList;
    }

    private void closeSheetAndThrow(Workbook workbook, int rowCounter, String message) throws HRMSException, Exception {
        workbook.close();
        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + message + " At Row" + rowCounter);
    }
}
