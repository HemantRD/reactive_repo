package com.vinsys.hrms.idp.progress.service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import org.springframework.web.multipart.MultipartFile;

public interface IProgressService {

    HRMSBaseResponse<?> uploadBulkProgressExcel(MultipartFile requestFile) throws HRMSException, Exception;
}
