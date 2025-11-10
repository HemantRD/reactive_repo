package com.vinsys.hrms.audit.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.vinsys.hrms.audit.vo.AuditLogRequestVo;
import com.vinsys.hrms.audit.vo.AuditLogVO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.exception.HRMSException;

public interface IAuditLogService {
	public void setActionHeader(String actionName, Employee actionedBy, Employee actionedOn);
	
	 HRMSBaseResponse <List<AuditLogVO>> getAuditLogList(Pageable pageable) throws HRMSException;

	public HRMSBaseResponse<List<AuditLogVO>> getAuditLogReportList(AuditLogRequestVo request, Pageable pageable) throws HRMSException;

	byte[] downloadAuditLogReport(AuditLogRequestVo request) throws HRMSException;

}