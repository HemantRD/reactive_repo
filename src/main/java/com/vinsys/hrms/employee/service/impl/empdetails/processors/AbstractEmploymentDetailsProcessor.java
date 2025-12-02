package com.vinsys.hrms.employee.service.impl.empdetails.processors;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.vinsys.hrms.constants.EResponse;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.vo.BankDetailsVO;
import com.vinsys.hrms.employee.vo.EmployeeCurrentDetailMainVO;
import com.vinsys.hrms.employee.vo.EmployeeCurrentDetailVO;
import com.vinsys.hrms.employee.vo.EmployeeDocumentVO;
import com.vinsys.hrms.employee.vo.IdentificationDetailsVO;
import com.vinsys.hrms.employee.vo.PreviousEmploymentDetailsVO;
import com.vinsys.hrms.employee.vo.PreviousEmploymentVO;
import com.vinsys.hrms.entity.BankDetails;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.CandidateLetter;
import com.vinsys.hrms.entity.CandidatePreviousEmployment;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.vo.EmploymentTypeVO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.EChecklistItems;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.EmploymentDetailsTransformUtils;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

public abstract class AbstractEmploymentDetailsProcessor implements IEmploymentDetailsProcessor {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private EmploymentDetailsProcessorDependencies processorDependencies;

	protected AbstractEmploymentDetailsProcessor(EmploymentDetailsProcessorDependencies detailsProcessorDependencies) {
		this.processorDependencies = detailsProcessorDependencies;
	}

	@Override
	public HRMSBaseResponse<BankDetailsVO> getEmployeeBankDetails(Long candidateId) throws HRMSException {
		log.info(" == >> Inside Get Employee bank details method<< == ");
		HRMSBaseResponse<BankDetailsVO> response = new HRMSBaseResponse<>();
		Long actualCandidateId = validateAndGetCandidateId(candidateId);
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Candidate candidate = processorDependencies.getCandidateDAO().findByIdAndIsActive(actualCandidateId,
				IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(candidate)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		if (!orgId.equals(candidate.getOrgId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		BankDetails bank = processorDependencies.getBankDAO().findByCandidateId(actualCandidateId);

		List<CandidateChecklist> checklist = processorDependencies.getChecklistDAO()
				.getCandidateWithChecklistDetailsByItemId(candidate.getCandidateProfessionalDetail().getId(),
						candidate.getCandidateProfessionalDetail().getDivision().getId(),
						EChecklistItems.BANK_DETAILS.toString());

		if (!HRMSHelper.isNullOrEmpty(bank)) {

			BankDetailsVO model = EmploymentDetailsTransformUtils.convertToBankDetailsToModel(bank, checklist);

			response.setResponseBody(model);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		} else {

			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		return response;

	}

	@Override
	public HRMSBaseResponse<PreviousEmploymentVO> getPreviousEmploymentDetails(Long candidateId) throws HRMSException {

		log.info(" == >> Inside Get Previous employement details method<< == ");

		HRMSBaseResponse<PreviousEmploymentVO> response = new HRMSBaseResponse<>();
		Long actualCandidateId = validateAndGetCandidateId(candidateId);
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		Candidate candidate = processorDependencies.getCandidateDAO().findByIdAndIsActive(actualCandidateId,
				IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(candidate)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		if (!orgId.equals(candidate.getOrgId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		if (!HRMSHelper.isNullOrEmpty(candidate)
				&& !HRMSHelper.isNullOrEmpty(candidate.getCandidateProfessionalDetail())) {

			List<CandidateChecklist> checklistDetails = processorDependencies.getChecklistDAO()
					.getCandidateWithChecklistDetailsByItemId(candidate.getCandidateProfessionalDetail().getId(),
							candidate.getCandidateProfessionalDetail().getDivision().getId(),
							EChecklistItems.PREVIOUS_EMPLOYMENT_DETAILS.toString());
			Set<CandidatePreviousEmployment> prevEmploymentEntitySet = candidate.getCandidateProfessionalDetail()
					.getCandidatePreviousEmployments();
			List<PreviousEmploymentDetailsVO> prevEmploymentModelList = new ArrayList<PreviousEmploymentDetailsVO>();

			for (CandidatePreviousEmployment prevEmploymentEntity : prevEmploymentEntitySet) {
				PreviousEmploymentDetailsVO model = EmploymentDetailsTransformUtils
						.convertToCandidatePreviousEmploymentModel(prevEmploymentEntity);
				model.setEmployeeType(converEmploymentEntityModel(prevEmploymentEntity.getEmployeeType()));
				prevEmploymentModelList.add(model);

			}
			List<IdentificationDetailsVO> identificationDetails = new ArrayList<IdentificationDetailsVO>();

			IdentificationDetailsVO identificationDetail = new IdentificationDetailsVO();

			for (CandidateChecklist checklist : checklistDetails) {
				identificationDetail = new IdentificationDetailsVO();
				identificationDetail.setDocumentName(checklist.getAttachment());
				identificationDetail.setDocumentType(checklist.getChecklistItem());
				identificationDetails.add(identificationDetail);
			}

			PreviousEmploymentVO previousVO = new PreviousEmploymentVO();
			previousVO.setPreviousEmployment(prevEmploymentModelList);
			previousVO.setDocuments(identificationDetails);

			int totalCount = prevEmploymentEntitySet.size();
			response.setResponseBody(previousVO);
			response.setTotalRecord(totalCount);
			response.setResponseCode(EResponse.SUCCESS.getCode());
			response.setResponseMessage(EResponse.SUCCESS.getMessage());
			log.info(" == >> Exit from Get Previous employement details method<< == ");
		} else {

			throw new HRMSException(EResponse.DATA_NOT_FOUND.getCode(), EResponse.DATA_NOT_FOUND.getMessage());
		}

		return response;
	}
	private  EmploymentTypeVO converEmploymentEntityModel(String employemntType) {
		MasterEmploymentType employmentType = processorDependencies.masterEmploymentTypeDAO.findByIsActiveAndEmploymentTypeName(IHRMSConstants.isActive, employemntType);
		EmploymentTypeVO model = null;
		if (!HRMSHelper.isNullOrEmpty(employmentType)) {
			model = new EmploymentTypeVO();
			model.setId(employmentType.getId());
			model.setEmploymentTypeName(employmentType.getEmploymentTypeName());
			model.setEmploymentTypeDescription(employmentType.getEmploymentTypeDescription());
		}
		return model;
	}

	private Long validateAndGetCandidateId(Long candidateId) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(candidateId)) {
			List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
			if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}
			return candidateId;
		} else {
			return SecurityFilter.TL_CLAIMS.get().getCandidateId();
		}

	}

	public HRMSBaseResponse<List<EmployeeDocumentVO>> getAllCandidateDocuments() throws HRMSException {

		log.info(" == >> Inside getAllCandidateDocuments method<< == ");

		HRMSBaseResponse<List<EmployeeDocumentVO>> response = new HRMSBaseResponse();

		Long candId = SecurityFilter.TL_CLAIMS.get().getCandidateId();

		Candidate cand = processorDependencies.getCandidateDAO().findByIdAndIsActive(candId, "Y");
		Long candidateId;
		if (!HRMSHelper.isNullOrEmpty(cand)) {
			candidateId = cand.getId();
		} else {
			Employee emp = processorDependencies.getEmployeeDAO().findActiveEmployeeById(candId, "Y");
			candidateId = emp.getCandidate().getId();
		}

		List<EmployeeDocumentVO> empAllDocuments = new ArrayList<EmployeeDocumentVO>();

		if (!HRMSHelper.isNullOrEmpty(candId)) {

			Candidate candidate = processorDependencies.getChecklistDAO()
					.getCandidateWithChecklistDetailsForEmp(candidateId, 1);
			if (!HRMSHelper.isNullOrEmpty(candidate)
					&& !HRMSHelper.isNullOrEmpty(candidate.getCandidateProfessionalDetail())) {
				Set<CandidateChecklist> checkListEntitySet = candidate.getCandidateProfessionalDetail()
						.getCandidateChecklists();
				for (CandidateChecklist checkListEntity : checkListEntitySet) {
					EmployeeDocumentVO empDoc = new EmployeeDocumentVO();
					empDoc.setDocumentName(checkListEntity.getChecklistItem());
					String path = processorDependencies.getRootDirectory()
							+ candidate.getLoginEntity().getOrganization().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
							+ candidate.getId() + File.separator + checkListEntity.getAttachment();
					empDoc.setUrl(path);
					empDoc.setFileName(checkListEntity.getAttachment());
					empAllDocuments.add(empDoc);

				}
			}

			Set<CandidateLetter> candLetters = processorDependencies.candidateLetterDAO
					.findLettersByCandidateId(candidateId);
			for (CandidateLetter candidateLetter : candLetters) {
				EmployeeDocumentVO empDoc = new EmployeeDocumentVO();
				empDoc.setDocumentName(candidateLetter.getLetterType());
				empDoc.setUrl(candidateLetter.getLetterUrl() + candidateLetter.getFileName());
				empDoc.setFileName(candidateLetter.getFileName());
				empAllDocuments.add(empDoc);
			}
			response.setResponseBody(empAllDocuments);
			response.setResponseCode(EResponse.SUCCESS.getCode());
			response.setResponseMessage(EResponse.SUCCESS.getMessage());
			log.info(" == >> Exit from getAllCandidateDocuments method<< == ");
		} else {
			throw new HRMSException(EResponse.DATA_NOT_FOUND.getCode(), EResponse.DATA_NOT_FOUND.getMessage());
		}

		return response;
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
				url = processorDependencies.getRootDirectory() + candLetters.getLetterUrl().replace("/webdav/", "")
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

	@Override
	public HRMSBaseResponse<EmployeeCurrentDetailMainVO> getEmployeeCurrentOrganizationDetails(Long candidateId)
			throws HRMSException {

		log.info("Inside getEmployeeCurrentOrganizationDetails method");

		HRMSBaseResponse<EmployeeCurrentDetailMainVO> response = new HRMSBaseResponse<>();

		Long actualCandidateId = validateAndGetCandidateId(candidateId);
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		// Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		Employee employee = processorDependencies.getEmployeeDAO().findByCandidateIdAndIsActiveAndOrgId(
				actualCandidateId, IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());

		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (HRMSHelper.isNullOrEmpty(employee)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		if (!orgId.equals(employee.getOrgId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		EmployeeCurrentDetailVO employeeCurrentDetailModel = null;
		if (!HRMSHelper.isLongZero(employee.getId())) {
			employeeCurrentDetailModel = EmploymentDetailsTransformUtils
					.convertToEmployeCurrentDetailModel(employee.getEmployeeCurrentDetail(), employee, roles);
			// ------
			List<CandidateLetter> letters = processorDependencies.getCandidateLetterDAO()
					.getCandidateWithChecklistDetailsByItemId(employee.getCandidate().getId(),
							employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId());

			// String tabName="CURRENT_DETAILS"; getting SALARY ANNEXURE
			List<CandidateChecklist> checklists = processorDependencies.getChecklistDAO()
					.getCandidateWithChecklistDetailsByItemId(
							employee.getCandidate().getCandidateProfessionalDetail().getId(),
							employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							EChecklistItems.CURRENT_DETAILS.toString());

			List<IdentificationDetailsVO> identificationDetails = new ArrayList<IdentificationDetailsVO>();

			IdentificationDetailsVO identificationDetail = new IdentificationDetailsVO();

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

}
