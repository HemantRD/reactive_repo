package com.vinsys.hrms.idp.reports.service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.reports.vo.TopTrainingCourses;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IIdpReportsService {

    HRMSBaseResponse<List<TopTrainingCourses>> getTopTrainingRequested(String keyword, Pageable pageable) throws HRMSException;

    byte[] getTopTrainingRequestedExcel(String viewType, String keyword, Pageable pageable) throws HRMSException;

    HRMSBaseResponse<List<TopTrainingCourses>> getCostMemberWise(String keyword, Pageable pageable) throws HRMSException;

    byte[] getCostMemberWiseExcel(String viewType, String keyword, Pageable pageable) throws HRMSException;

    HRMSBaseResponse<List<TopTrainingCourses>> getCostTrainingWise(String keyword, Pageable pageable) throws HRMSException;

    byte[] getCostTrainingWiseExcel(String viewType, String keyword, Pageable pageable) throws HRMSException;
}
