package com.vinsys.hrms.idp.reports.service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.reports.vo.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IIdpReportsService {

    HRMSBaseResponse<List<TopTrainingCourses>> getTopTrainingRequested(String keyword, Pageable pageable) throws HRMSException;

    byte[] getTopTrainingRequestedExcel(String viewType, String keyword, Pageable pageable) throws HRMSException;

    HRMSBaseResponse<List<MemberWiseCost>> getCostMemberWise(String keyword, Pageable pageable, String reportType) throws HRMSException;

    byte[] getCostMemberWiseExcel(String viewType, String keyword, Pageable pageable, String reportType) throws HRMSException;

    HRMSBaseResponse<List<TopicWiseCost>> getCostTrainingWise(String keyword, Pageable pageable, String reportType) throws HRMSException;

    byte[] getCostTrainingWiseExcel(String viewType, String keyword, Pageable pageable, String reportType) throws HRMSException;

    HRMSBaseResponse<DashboardVo> getDashboard(String keyword, Pageable pageable) throws HRMSException;

    HRMSBaseResponse<List<ParticipantsClustersVo>> getParticipantsClusters(ParticipantsClustersListReq request, Pageable pageable) throws HRMSException;

    byte[] getParticipantsClustersExcel(ParticipantsClustersListReq request) throws HRMSException;
}
