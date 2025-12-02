package com.vinsys.hrms.audit.service.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.audit.dao.IAuditLogDao;
import com.vinsys.hrms.audit.entity.AuditLog;
import com.vinsys.hrms.audit.entity.AuditLogView;
import com.vinsys.hrms.audit.service.IAuditLogService;
import com.vinsys.hrms.audit.vo.AuditLogRequestVo;
import com.vinsys.hrms.audit.vo.AuditLogVO;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.entity.OrganizationalKpiListView;
import com.vinsys.hrms.kra.vo.AuditResponseVO;
import com.vinsys.hrms.security.Header;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

@Service
public class AuditLogServiceImpl implements IAuditLogService {
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	ThreadLocal<Header> threadLocal;
	
	@Autowired
	IAuditLogDao iAuditLogDao;
	
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	
	@Autowired
	EntityManager em;

	@Override
	public void setActionHeader(String actionName, Employee actionedBy, Employee actionedOn) {
		try {
			Header header = new Header();
			header.setActionName(actionName);
			if (!HRMSHelper.isNullOrEmpty(actionedBy)) {
				header.setActionBy(actionedBy.getId());
				header.setActionOn(actionedOn.getId());
			}
			if (!HRMSHelper.isNullOrEmpty(actionedOn)) {
				header.setActionBy(actionedBy.getId());
				header.setActionOn(actionedOn.getId());
			}
			threadLocal.set(header);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void setActionHeader(String actionName, Long... actionedOn) {
		try {
			Header header = new Header();
			header.setActionName(actionName);
			header.setActionBy(SecurityFilter.TL_CLAIMS.get().getEmployeeId());
			if (actionedOn != null && actionedOn.length > 0) {
				header.setActionOn(actionedOn[0]);
			}
			threadLocal.set(header);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public HRMSBaseResponse<List<AuditLogVO>> getAuditLogList(Pageable pageable) throws HRMSException{
		HRMSBaseResponse<List<AuditLogVO>> baseresponse = new HRMSBaseResponse<>();
		
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		
		long totalRecords=0;
		 List<AuditLog> auditLogs = iAuditLogDao.findByIsActiveOrderByIdDesc(IHRMSConstants.isActive,pageable);
		 totalRecords=iAuditLogDao.countByIsActive(IHRMSConstants.isActive);	 
		    List<AuditLogVO> auditLogVOList = new ArrayList<>();
		    	
		    try {
		        if (!HRMSHelper.isNullOrEmpty(auditLogs)) {
		            for (AuditLog auditLog : auditLogs) {
		                auditLogVOList.add(convertToAuditLogVO(auditLog));
		            }
		        }
		    } catch (Exception e) {
		    	log.info("Error while converting audit logs", e);
		    	throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1794));
		    }

		    baseresponse.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		    baseresponse.setResponseBody(auditLogVOList);
		    baseresponse.setTotalRecord(totalRecords);
		    baseresponse.setResponseCode(1200);

		    return baseresponse;
		}

	
	
	private AuditLogVO convertToAuditLogVO(AuditLog auditLog) {
	    AuditLogVO auditLogVO = new AuditLogVO();

	    String actionByName = null;

	    if (!HRMSHelper.isNullOrEmpty(auditLog.getActionBy())) {
	        Employee employee = employeeDAO.findActiveEmployeeById(auditLog.getActionBy(), IHRMSConstants.isActive);
	        if (!HRMSHelper.isNullOrEmpty(employee) && !HRMSHelper.isNullOrEmpty(employee.getCandidate())) {
	            actionByName = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName();
	        }
	    }
	    
	    
	    String actionOnName = null;
	    if (!HRMSHelper.isNullOrEmpty(auditLog.getActionOn())) {
	        Employee actionOnEmployee = employeeDAO.findActiveEmployeeById(auditLog.getActionOn(), IHRMSConstants.isActive);
	        if (!HRMSHelper.isNullOrEmpty(actionOnEmployee) && !HRMSHelper.isNullOrEmpty(actionOnEmployee.getCandidate())) {
	            actionOnName = actionOnEmployee.getCandidate().getFirstName() + " " + actionOnEmployee.getCandidate().getLastName();
	        }
	    }

	    auditLogVO.setId(auditLog.getId());
	    auditLogVO.setActionName(auditLog.getActionName());
	    auditLogVO.setActionBy(auditLog.getActionBy());
	    auditLogVO.setActionByName(actionByName);
	    auditLogVO.setActionOn(auditLog.getActionOn());
	    auditLogVO.setActionOnName(actionOnName);
	    auditLogVO.setRequestUrl(auditLog.getRequestUrl());
	    auditLogVO.setStatusCode(auditLog.getStatusCode());
	    auditLogVO.setResponseMessage(auditLog.getResponseMessage());
	    auditLogVO.setCreatedDate(HRMSDateUtil.format(auditLog.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
	    auditLogVO.setIsActive(auditLog.getIsActive());

	    return auditLogVO;
	}

	@Override
	public HRMSBaseResponse<List<AuditLogVO>> getAuditLogReportList(AuditLogRequestVo request, Pageable pageable) throws HRMSException {
		HRMSBaseResponse<List<AuditLogVO>> response = new HRMSBaseResponse<List<AuditLogVO>>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());

		validateRolePermission();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<AuditLogView> query = builder.createQuery(AuditLogView.class);
		Root<AuditLogView> root = query.from(AuditLogView.class);
		List<Predicate> predicates = new ArrayList<>();

		if (ObjectUtils.isNotEmpty(request.getKeyword())) {
			String keywordPattern = "%" + request.getKeyword().toLowerCase() + "%";
			Predicate keywordCondition = builder.or(
					builder.like(builder.lower(root.get("actionName")), keywordPattern),
					builder.like(builder.lower(root.get("requestUrl")), keywordPattern),
					builder.like(builder.lower(root.get("statusCode")), keywordPattern),
					builder.like(builder.lower(root.get("status")), keywordPattern),
					builder.like(builder.lower(root.get("responseMessage")), keywordPattern),
					builder.like(builder.lower(root.get("actionByName")), keywordPattern),
					builder.like(builder.lower(root.get("actionOnName")), keywordPattern));
			predicates.add(keywordCondition);
		}

		query.where(predicates.toArray(new Predicate[0]));
		// pagination
		int pageNumber = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		int startRecord = pageNumber * pageSize;
		int totalRecord = em.createQuery(query.select(root)).getResultList().size();
		List<AuditLogView> filterauditlogReport = em.createQuery(query.select(root))
				.setFirstResult(startRecord).setMaxResults(pageSize).getResultList();

		List<AuditLogVO> auditLogResponseList = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(filterauditlogReport)) {
			auditLogResponseList = filterauditlogReport.stream().map(auditLog -> {
				AuditLogVO auditLogVO = new AuditLogVO();
				 auditLogVO.setId(auditLog.getId());
				    auditLogVO.setActionName(auditLog.getActionName());
				    auditLogVO.setActionBy(auditLog.getActionBy());
				    auditLogVO.setActionByName(auditLog.getActionByName());
				    auditLogVO.setActionOn(auditLog.getActionOn());
				    auditLogVO.setActionOnName(auditLog.getActionOnName());
				    auditLogVO.setRequestUrl(auditLog.getRequestUrl());
				    auditLogVO.setStatusCode(auditLog.getStatusCode());
				    auditLogVO.setResponseMessage(auditLog.getResponseMessage());
				    auditLogVO.setCreatedDate(HRMSDateUtil.format(auditLog.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				    auditLogVO.setIsActive(auditLog.getIsActive());
				return auditLogVO;
			}).collect(Collectors.toList());
		}
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<AuditLogView> countRoot = countQuery.from(AuditLogView.class);
		countQuery.select(builder.count(countRoot));
		countQuery.where(predicates.toArray(new Predicate[] {}));
		response.setResponseBody(auditLogResponseList);
		response.setTotalRecord(totalRecord);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		return response;
	}
	
	
	/*
	 * Author 
	 * Kailas B
	 */
	
	@Override
	public byte[] downloadAuditLogReport(AuditLogRequestVo request) throws HRMSException {

	    List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
	    if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
	    }

	    int pageSize = Integer.MAX_VALUE;
	    Pageable pageable = PageRequest.of(0, pageSize);
	    List<AuditLogVO> reportData = getAuditLogReportList(request, pageable).getResponseBody();

	    ByteArrayOutputStream out = new ByteArrayOutputStream();

	    if (HRMSHelper.isNullOrEmpty(reportData)) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
	    }

	    try (Workbook workbook = new XSSFWorkbook()) {

	        Sheet sheet = workbook.createSheet("Audit_Log_Report");
	        Row headerRow = sheet.createRow(0);
	        String[] headers = { "Action By", "Action On", "Action Name", "Request URL", "Status Code",
	                "Response Message", "Created Date" };

	        CellStyle headerStyle = workbook.createCellStyle();
	        Font headerFont = workbook.createFont();
	        headerFont.setBold(true);
	        headerFont.setColor(IndexedColors.WHITE.getIndex());
	        headerStyle.setFont(headerFont);
	        headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerStyle.setAlignment(HorizontalAlignment.CENTER);
	        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(headerStyle);
	        }

	        int rowNum = 1;

	        for (AuditLogVO data : reportData) {
	            Row row = sheet.createRow(rowNum++);

	            
	            String actionBy = IHRMSConstants.NA;
	            if (data.getActionBy() != null) {
	                Employee employee = employeeDAO.findById(data.getActionBy()).orElse(null);
	                if (employee != null && employee.getCandidate() != null) {
	                	actionBy = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName();
	                }
	            }

	            String actionOn = IHRMSConstants.NA;
	            if (data.getActionOn() != null) {
	                Employee employee = employeeDAO.findById(data.getActionOn()).orElse(null);
	                if (employee != null && employee.getCandidate() != null) {
	                    actionOn = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName();
	                }
	            }


	            row.createCell(0).setCellValue(actionBy);
	            row.createCell(1).setCellValue(actionOn);
	            row.createCell(2).setCellValue(data.getActionName());
	            row.createCell(3).setCellValue(data.getRequestUrl());
	            row.createCell(4).setCellValue(data.getStatusCode());
	            row.createCell(5).setCellValue(data.getResponseMessage());
	            row.createCell(6).setCellValue(data.getCreatedDate().toString());
	        }

	        for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }

	        workbook.write(out);

	    } catch (IOException e) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1783));
	    }

	    HRMSBaseResponse<byte[]> response = new HRMSBaseResponse<>();
	    response.setResponseBody(out.toByteArray());
	    response.setResponseCode(1200);
	    response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1784));

	    return out.toByteArray();
	}
	
	public void validateRolePermission() throws HRMSException {
		validateRolePermissionforUnlocked();
	}

	private void validateRolePermissionforUnlocked() throws HRMSException {
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}


}
