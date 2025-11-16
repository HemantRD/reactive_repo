package com.vinsys.hrms.employee.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.dao.IHRMSBankDetailsDAO;
import com.vinsys.hrms.dao.IHRMSCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateLetterDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.service.IEmploymentDetailsService;
import com.vinsys.hrms.employee.service.impl.empdetails.processors.EmploymentDetailsProcessorDependencies;
import com.vinsys.hrms.employee.service.impl.empdetails.processors.EmploymentDetailsProcessorFactory;
import com.vinsys.hrms.employee.service.impl.empdetails.processors.IEmploymentDetailsProcessor;
import com.vinsys.hrms.employee.vo.BankDetailsVO;
import com.vinsys.hrms.employee.vo.EmployeeCurrentDetailMainVO;
import com.vinsys.hrms.employee.vo.EmployeeDocumentVO;
import com.vinsys.hrms.employee.vo.PreviousEmploymentVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;

@Service
public class EmploymentDetailsServiceImpl implements IEmploymentDetailsService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app_version}")
	private String applicationVersion;

	@Autowired
	IHRMSEmployeeDAO employeeDAO;

	@Autowired
	IHRMSBankDetailsDAO bankDAO;

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSCandidateChecklistDAO checklistDAO;
	@Autowired
	IHRMSProfessionalDetailsDAO candidateProfessionalDtlDAO;
	@Value("${rootDirectory}")
	private String rootDirectory;
	
	@Value("${base.url}")
	private String baseURL;

	@Autowired
	IHRMSCandidateLetterDAO candidateLetterDAO;
	
	@Autowired
	IHRMSMasterEmployementTypeDAO masterEmploymentTypeDAO;

	@Override
	public HRMSBaseResponse<BankDetailsVO> getEmployeeBankDetails(Long candidateId) throws HRMSException {
		EmploymentDetailsProcessorDependencies dependencies = getDepedencies();
		EmploymentDetailsProcessorFactory factory = new EmploymentDetailsProcessorFactory();
		IEmploymentDetailsProcessor processor = factory
				.getEmploymentDetailsProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.getEmployeeBankDetails(candidateId);
	}

	EmploymentDetailsProcessorDependencies getDepedencies() {
		return new EmploymentDetailsProcessorDependencies(employeeDAO, bankDAO, candidateDAO, checklistDAO,
				candidateProfessionalDtlDAO, rootDirectory, candidateLetterDAO,baseURL,masterEmploymentTypeDAO);
	}

	@Override
	public HRMSBaseResponse<PreviousEmploymentVO> getPreviousEmploymentDetails(Long candidateId) throws HRMSException {
		EmploymentDetailsProcessorDependencies dependencies = getDepedencies();
		EmploymentDetailsProcessorFactory factory = new EmploymentDetailsProcessorFactory();
		IEmploymentDetailsProcessor processor = factory
				.getEmploymentDetailsProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.getPreviousEmploymentDetails(candidateId);
	}

	public HRMSBaseResponse<List<EmployeeDocumentVO>> getAllCandidateDocuments() throws HRMSException {
		EmploymentDetailsProcessorDependencies dependencies = getDepedencies();
		EmploymentDetailsProcessorFactory factory = new EmploymentDetailsProcessorFactory();
		IEmploymentDetailsProcessor processor = factory
				.getEmploymentDetailsProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.getAllCandidateDocuments();
	}

	@Override
	public ResponseEntity<String> previewAllDocuments(String documentName, Long candidateId)
			throws HRMSException, FileNotFoundException, IOException {

		EmploymentDetailsProcessorDependencies dependencies = getDepedencies();
		EmploymentDetailsProcessorFactory factory = new EmploymentDetailsProcessorFactory();
		IEmploymentDetailsProcessor processor = factory
				.getEmploymentDetailsProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.previewAllDocuments(documentName,candidateId);

	}

	@Override
	public HRMSBaseResponse<EmployeeCurrentDetailMainVO> getEmployeeCurrentOrganizationDetails(Long candidateId) throws HRMSException {

		EmploymentDetailsProcessorDependencies dependencies = getDepedencies();
		EmploymentDetailsProcessorFactory factory = new EmploymentDetailsProcessorFactory();
		IEmploymentDetailsProcessor processor = factory
				.getEmploymentDetailsProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.getEmployeeCurrentOrganizationDetails(candidateId);

	}

}
