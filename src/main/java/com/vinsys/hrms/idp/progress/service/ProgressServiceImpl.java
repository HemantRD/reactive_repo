package com.vinsys.hrms.idp.progress.service;

import com.vinsys.hrms.constants.EResponse;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.progress.validator.ProgressValidator;
import com.vinsys.hrms.util.ResponseCode;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProgressServiceImpl implements IProgressService {

    private final ProgressValidator validator;

    public ProgressServiceImpl(final ProgressValidator validator) {
        this.validator = validator;
    }

    public HRMSBaseResponse<String> uploadBulkProgressExcel(MultipartFile excelFile) throws HRMSException, Exception {
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        validator.validateExcelFileData(excelFile);
        Workbook workbook = excelUploadMethod(file);


        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
        response.setResponseCode(EResponse.SUCCESS.getCode());
        response.setResponseMessage(EResponse.SUCCESS.getMessage());
        workbook.close();
        return response;
    }
}
