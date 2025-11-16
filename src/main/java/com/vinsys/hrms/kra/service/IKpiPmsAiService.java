package com.vinsys.hrms.kra.service;

import com.vinsys.hrms.kra.vo.AiMsBackgroundAnalysisRequestInvokerVO;
import org.springframework.stereotype.Service;

@Service
public interface IKpiPmsAiService {
    AiMsBackgroundAnalysisRequestInvokerVO validateCeoRole() throws Exception;
    AiMsBackgroundAnalysisRequestInvokerVO validateCeoOrHodRole() throws Exception;
    void calculateDepartmentLevelAggregations(AiMsBackgroundAnalysisRequestInvokerVO invokerVO, Long deptId) throws Exception;
    void calculateOrganisationLevelAggregations(AiMsBackgroundAnalysisRequestInvokerVO invokerVO) throws Exception;
}
