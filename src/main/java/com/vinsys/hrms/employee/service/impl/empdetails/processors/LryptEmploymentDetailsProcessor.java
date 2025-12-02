package com.vinsys.hrms.employee.service.impl.empdetails.processors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.vo.EmployeeCurrentDetailMainVO;
import com.vinsys.hrms.employee.vo.EmployeeCurrentDetailVO;
import com.vinsys.hrms.employee.vo.IdentificationDetailsVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.CandidateLetter;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.EChecklistItems;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.EmploymentDetailsTransformUtils;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

public class LryptEmploymentDetailsProcessor extends AbstractEmploymentDetailsProcessor {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private EmploymentDetailsProcessorDependencies processorDependencies;

	public LryptEmploymentDetailsProcessor(EmploymentDetailsProcessorDependencies detailsProcessorDependencies) {
		super(detailsProcessorDependencies);
		this.processorDependencies = detailsProcessorDependencies;
	}

	@Override
	public HRMSBaseResponse<EmployeeCurrentDetailMainVO> getEmployeeCurrentOrganizationDetails(Long candidateId) throws HRMSException {
		log.info("Inside getEmployeeCurrentOrganizationDetails method");
		HRMSBaseResponse<EmployeeCurrentDetailMainVO> response = new HRMSBaseResponse<>();
		Long actualCandidateId=validateAndGetCandidateId(candidateId);
		Long orgId=SecurityFilter.TL_CLAIMS.get().getOrgId();
		Employee employee = processorDependencies.getEmployeeDAO()
				.findByCandidateIdAndIsActiveAndOrgId(actualCandidateId, IHRMSConstants.isActive,SecurityFilter.TL_CLAIMS.get().getOrgId());
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		
		if(HRMSHelper.isNullOrEmpty(employee)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		if(!orgId.equals(employee.getOrgId())) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1521));
		}
		return getCurrentOrganizationResponse(response, employee, roles);
	}

	private HRMSBaseResponse<EmployeeCurrentDetailMainVO> getCurrentOrganizationResponse(
			HRMSBaseResponse<EmployeeCurrentDetailMainVO> response, Employee employee, List<String> roles)
			throws HRMSException {
		EmployeeCurrentDetailVO employeeCurrentDetailModel = null;
		if (!HRMSHelper.isLongZero(SecurityFilter.TL_CLAIMS.get().getEmployeeId())) {
			employeeCurrentDetailModel = EmploymentDetailsTransformUtils
					.convertToEmployeCurrentDetailModel(employee.getEmployeeCurrentDetail(), employee, roles);

			// String tabName="CURRENT_DETAILS"; getting SALARY ANNEXURE
			List<CandidateChecklist> checklists = processorDependencies.checklistDAO
					.getCandidateWithChecklistDetailsByItemIdAndOrgId(
							employee.getCandidate().getCandidateProfessionalDetail().getId(),
							employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							EChecklistItems.CURRENT_DETAILS.toString(),employee.getOrgId());

			List<IdentificationDetailsVO> identificationDetails = getCandidateLetters(employee, checklists);

			EmployeeCurrentDetailMainVO employeeCurrentDetailMainVO = new EmployeeCurrentDetailMainVO();
			employeeCurrentDetailMainVO.setEmployeeDetails(employeeCurrentDetailModel);
			employeeCurrentDetailMainVO.setDocuments(identificationDetails);

			response.setResponseBody(employeeCurrentDetailMainVO);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

			log.info("Exit from getEmployeeCurrentOrganizationDetails method");
			return response;
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}
	}

	private List<IdentificationDetailsVO> getCandidateLetters(Employee employee, List<CandidateChecklist> checklists) {

		List<IdentificationDetailsVO> identificationDetails = new ArrayList<>();
		IdentificationDetailsVO identificationDetail;
		List<CandidateLetter> letters = processorDependencies.candidateLetterDAO
				.getCandidateWithChecklistDetailsByItemId(employee.getCandidate().getId(),
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
		for (CandidateLetter letter : letters) {
			identificationDetail = new IdentificationDetailsVO();

			identificationDetail.setDocumentName(letter.getFileName());
			identificationDetail.setDocumentType(letter.getLetterType());
			identificationDetails.add(identificationDetail);
		}
		for (CandidateChecklist checklist : checklists) {
			identificationDetail = new IdentificationDetailsVO();
			identificationDetail.setDocumentName(checklist.getAttachment());
			identificationDetail.setDocumentType(checklist.getChecklistItem());
			identificationDetails.add(identificationDetail);
		}
		return identificationDetails;
	}
	
	private Long validateAndGetCandidateId(Long candidateId) throws HRMSException {
		if(!HRMSHelper.isNullOrEmpty(candidateId)) {
			List<String> role= SecurityFilter.TL_CLAIMS.get().getRoles();
			if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1521));
			}
			return candidateId;
		}else {
			return SecurityFilter.TL_CLAIMS.get().getCandidateId();
		}
		
	}
	
	@Override
	public ResponseEntity<String> previewAllDocuments(String documentName, Long candidateId)
			throws HRMSException, FileNotFoundException, IOException {

		log.info(" == >> Inside Previw Documents method<< == ");
		Long actualCandidateId = validateAndGetCandidateId(candidateId);
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
//		Long candId = SecurityFilter.TL_CLAIMS.get().getCandidateId();

		String fileName = null;
		String url = null;

		if (!HRMSHelper.isNullOrEmpty(actualCandidateId)) {
			Candidate candidate = processorDependencies.getCandidateDAO().findByIdAndIsActiveAndorgId(actualCandidateId,
					IHRMSConstants.isActive, orgId);

			if (HRMSHelper.isNullOrEmpty(candidate)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

			if (documentName.equals("Offer Letter") || documentName.equals("Appointment Letter")
					|| documentName.equals("Relieving Letter") || documentName.equals("Experience Letter")) {

				CandidateLetter candLetters = processorDependencies.getCandidateLetterDAO()
						.findLetterByCandidateIdAndLetterType(actualCandidateId, documentName);
				fileName = candLetters.getFileName();
				url = processorDependencies.getRootDirectory() + candLetters.getLetterUrl().replace("/input/", "")
						+ candLetters.getFileName();
				
//				url = processorDependencies.baseURL + candLetters.getLetterUrl()
//						+ candLetters.getFileName();
				log.info("letter URL:" + url);
			} else if (documentName.equals("PHOTO")) {

				CandidateChecklist checklist = processorDependencies.getChecklistDAO()
						.getCandidateWithChecklistDetailsByItem(documentName, actualCandidateId);

				if (!HRMSHelper.isNullOrEmpty(checklist)) {

					fileName = checklist.getAttachment();

					url = processorDependencies.getRootDirectory()
							+ candidate.getLoginEntity().getOrganization().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
							+ candidate.getId() + File.separator + "Photo" + File.separator + checklist.getAttachment();

				}
			} else {

				CandidateChecklist checklist = processorDependencies.getChecklistDAO()
						.getCandidateWithChecklistDetailsByItem(documentName, actualCandidateId);

				if (!HRMSHelper.isNullOrEmpty(checklist)) {

					fileName = checklist.getAttachment();
					
					url = processorDependencies.getRootDirectory()
							+ candidate.getLoginEntity().getOrganization().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
							+ candidate.getId() + File.separator + checklist.getAttachment();

				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}
			}
			
			

			File file = new File(url);

			byte[] fileContent = IOUtils.toByteArray(new FileInputStream(file));

			String base64Content = Base64.getEncoder().encodeToString(fileContent);

			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=" + fileName);
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");

			ResponseEntity<String> response = ResponseEntity.ok().headers(header).contentType(MediaType.TEXT_PLAIN)
					.body(base64Content);

			return response;

		} else {

			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

	}


}
