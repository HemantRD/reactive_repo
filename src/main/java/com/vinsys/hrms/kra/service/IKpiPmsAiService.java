package com.vinsys.hrms.kra.service;

import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.vo.AiMsBackgroundAnalysisRequestInvokerVO;
import org.springframework.stereotype.Service;

@Service
public interface IKpiPmsAiService {
    void fetchActiveCycle(AiMsBackgroundAnalysisRequestInvokerVO invokerVO) throws HRMSException;
    AiMsBackgroundAnalysisRequestInvokerVO validateCeoRole(String role) throws Exception;
    AiMsBackgroundAnalysisRequestInvokerVO validateCeoOrHodRole(String role) throws Exception;
    void validateIfProcessIsAlreadyInProgress(AiMsBackgroundAnalysisRequestInvokerVO invokerVO, Long deptId) throws HRMSException;
    void calculateDepartmentLevelAggregations(AiMsBackgroundAnalysisRequestInvokerVO invokerVO, Long deptId) throws Exception;
    void doDepartmentLevelAnalysis(AiMsBackgroundAnalysisRequestInvokerVO invokerVO, Long deptId) throws Exception;
    void calculateOrganisationLevelAggregations(AiMsBackgroundAnalysisRequestInvokerVO invokerVO) throws Exception;
    void doOrganisationLevelAnalysis(AiMsBackgroundAnalysisRequestInvokerVO invokerVO) throws Exception;
}
