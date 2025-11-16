package com.vinsys.hrms.employee.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.vo.BankDetailsVO;
import com.vinsys.hrms.employee.vo.EmployeeCurrentDetailMainVO;
import com.vinsys.hrms.employee.vo.EmployeeDocumentVO;
import com.vinsys.hrms.employee.vo.PreviousEmploymentVO;
import com.vinsys.hrms.exception.HRMSException;

public interface IEmploymentDetailsService {

	HRMSBaseResponse<BankDetailsVO> getEmployeeBankDetails(Long candidateId) throws HRMSException;

	HRMSBaseResponse<PreviousEmploymentVO> getPreviousEmploymentDetails(Long candidateId) throws HRMSException;

	HRMSBaseResponse<EmployeeCurrentDetailMainVO> getEmployeeCurrentOrganizationDetails(Long candidateId) throws HRMSException;

	HRMSBaseResponse<List<EmployeeDocumentVO>> getAllCandidateDocuments() throws HRMSException;

	ResponseEntity<String> previewAllDocuments(String documentName , Long candidateId)
			throws HRMSException, FileNotFoundException, IOException;


}
