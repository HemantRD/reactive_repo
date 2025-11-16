package com.vinsys.hrms.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateLetterDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMasterCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOEmployeeDocument;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.CandidateLetter;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.JWTTokenHelper;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/employeeDocument")

public class EmployeeDocumentService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeDocumentService.class);

	@Value("${base.url}")
	private String baseURL;

	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSCandidateChecklistDAO checklistDAO;
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSCandidateLetterDAO candidateLetterDAO;
	@Autowired
	IHRMSMasterCandidateChecklistDAO masterCandidateChecklistDAO;

	@Autowired
	IHRMSProfessionalDetailsDAO candidateProfessionalDtlDAO;
	@Value("${rootDirectory}")
	private String rootDirectory;

	@RequestMapping(value = "/findEmpNameIdForDocuments/{orgId}/{divId}/{isResigned}"
			+ "", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String findEmpNameIdForDocuments(@PathVariable("orgId") long orgId, @PathVariable("divId") long divId,
			@PathVariable("isResigned") boolean isResigned) {
		try {
			if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(divId)) {
				List<Employee> orgEmployees = new ArrayList<Employee>();
				Employee employee = new Employee();
				employee.setCandidate(new Candidate());
				employee.getCandidate().setLoginEntity(new LoginEntity());
				employee.getCandidate().getLoginEntity().setOrganization(new Organization());
				employee.getCandidate().getLoginEntity().getOrganization().setId(orgId);
				employee.getCandidate().setCandidateProfessionalDetail(new CandidateProfessionalDetail());
				if (!HRMSHelper.isLongZero(divId)) {
					employee.getCandidate().getCandidateProfessionalDetail().setDivision(new MasterDivision());
					employee.getCandidate().getCandidateProfessionalDetail().getDivision().setId(divId);
				}
				if (isResigned) {
					employee.setIsActive(IHRMSConstants.isNotActive);
					employee.getCandidate().setIsActive(IHRMSConstants.isNotActive);
				} else {
					employee.setIsActive(IHRMSConstants.isActive);
					employee.getCandidate().setIsActive(IHRMSConstants.isActive);
				}
				List<Employee> employees = employeeDAO.findAll(Example.of(employee));

				for (Employee emp : employees) {
					if (emp.getCandidate().getLoginEntity().getOrganization().getId().equals(orgId)) {
						orgEmployees.add(emp);
					}
				}

				List<Object> voEmployees = new ArrayList<>();
				if (!HRMSHelper.isNullOrEmpty(employees)) {
					voEmployees = HRMSResponseTranslator.translateEmployeeNameIdToVO(employees, voEmployees);
					HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
					hrmsListResponseObject.setListResponse(voEmployees);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else { // if org or div is zero
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} catch (HRMSException e) {
			e.printStackTrace();
			logger.info(e.getResponseMessage());
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			logger.info(unknown.getMessage());
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(value = "/getAllDocsForCandidate/{empId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getAllCandidateDocuments(@PathVariable("empId") long empId) {

		try {
			HRMSListResponseObject response = null;
//			Employee emp = employeeDAO.findOne(empId);
			Candidate cand = candidateDAO.findByIdAndIsActive(empId, "Y");
			Long candId;
			if (!HRMSHelper.isNullOrEmpty(cand)) {
				candId = cand.getId();
			} else {
				Employee emp = employeeDAO.findActiveEmployeeById(empId, "Y");
				candId = emp.getCandidate().getId();
			}
			response = new HRMSListResponseObject();
			List<Object> empAllDocuments = new ArrayList<Object>();
			// commented because id getting from candidate not employee
//			if (!HRMSHelper.isNullOrEmpty(emp) && !HRMSHelper.isNullOrEmpty(emp.getCandidate())
//					&& !HRMSHelper.isNullOrEmpty(emp.getCandidate().getCandidateProfessionalDetail())) {
			if (!HRMSHelper.isNullOrEmpty(candId)) {

//				Candidate candidate = checklistDAO.getCandidateWithChecklistDetailsForEmp(emp.getCandidate().getId(),
//						1);
				Candidate candidate = checklistDAO.getCandidateWithChecklistDetailsForEmp(candId, 1);
				if (!HRMSHelper.isNullOrEmpty(candidate)
						&& !HRMSHelper.isNullOrEmpty(candidate.getCandidateProfessionalDetail())) {
					Set<CandidateChecklist> checkListEntitySet = candidate.getCandidateProfessionalDetail()
							.getCandidateChecklists();
					for (CandidateChecklist checkListEntity : checkListEntitySet) {
						VOEmployeeDocument empDoc = new VOEmployeeDocument();
						empDoc.setDocumentName(checkListEntity.getChecklistItem());
						String path = rootDirectory + candidate.getLoginEntity().getOrganization().getId()
								+ File.separator + candidate.getCandidateProfessionalDetail().getDivision().getId()
								+ File.separator + candidate.getCandidateProfessionalDetail().getBranch().getId()
								+ File.separator + candidate.getId() + File.separator + checkListEntity.getAttachment();
						empDoc.setUrl(path);
						empDoc.setFileName(checkListEntity.getAttachment());
						empAllDocuments.add(empDoc);
					}
				}

//				Set<CandidateLetter> candLetters = candidateLetterDAO
//						.findLettersByCandidateId(emp.getCandidate().getId());
				Set<CandidateLetter> candLetters = candidateLetterDAO.findLettersByCandidateId(candId);
				for (CandidateLetter candidateLetter : candLetters) {
					VOEmployeeDocument empDoc = new VOEmployeeDocument();
					empDoc.setDocumentName(candidateLetter.getLetterType());
					empDoc.setUrl(baseURL + candidateLetter.getLetterUrl() + candidateLetter.getFileName());
					empDoc.setFileName(candidateLetter.getFileName());
					empAllDocuments.add(empDoc);
				}
				response.setListResponse(empAllDocuments);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			return HRMSHelper.createJsonString(response);
		} catch (HRMSException e) {
			e.printStackTrace();
			logger.info(e.getResponseMessage());
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			logger.info(unknown.getMessage());
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/***************************
	 * U download All Documents
	 *********************************/

	@RequestMapping(value = "/downloadDocuments", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadDocuments(@RequestParam(required = true) String letterUrl,
			HttpServletRequest servletRequest) throws HRMSException, MalformedURLException {

		String token = JWTTokenHelper.parseJwt(servletRequest);
		Claims loggedInEmpDetails;
		if (!HRMSHelper.isNullOrEmpty(token)) {
			loggedInEmpDetails = JWTTokenHelper.getLoggedInEmpDetail(token);
		} else {
			throw new HRMSException(IHRMSConstants.failedCode, IHRMSConstants.TOKEN_NOT_VALID);
		}

		if (!HRMSHelper.isNullOrEmpty(letterUrl)) {

			String[] urlSegments = letterUrl.split("/");
			String fileName = urlSegments[urlSegments.length - 1];

			String url = rootDirectory + letterUrl;
			File file = new File(url);
			logger.info("Document Path:" + url);
			logger.info("file exist: " + file.exists());
			logger.info("filename: " + fileName);
			if (file.exists()) {
				Resource resource = new UrlResource(file.toURI());
				HttpHeaders headers = new HttpHeaders();
				headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName.replace(" ", "") + " ");
				headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
				headers.add("Pragma", "no-cache");
				headers.add("Expires", "0");

				return ResponseEntity.ok().headers(headers).contentLength(file.length())
						.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
			}

		} else {

			new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		return ResponseEntity.badRequest().build();
	}

	@GetMapping(value = "/getdocumentpreview")
	public ResponseEntity<Resource> getDocumentPreview(@RequestParam(required = true) String letterUrlToSend,
			HttpServletRequest servletRequest) throws HRMSException {
		// ... (existing code)

		if (!HRMSHelper.isNullOrEmpty(letterUrlToSend)) {
			// String baseDirectory = "D:/";

			// Normalize the filePath to handle separators consistently (use backslashes on
			// Windows)
			// String normalizedFilePath = filePath.replace("/", File.separator);

			String absoluteFilePath = rootDirectory + letterUrlToSend;

			File file = new File(absoluteFilePath);
			if (file.exists()) {
				logger.info("Absolute file Path:" + absoluteFilePath);
				try {
					Resource resource = new UrlResource(file.toURI());
					// Determine the content type based on the file extension
					String contentType = determineContentType(file.getName());
					if (contentType == null) {
						// Handle the case when the content type is not supported or unknown
						throw new HRMSException(IHRMSConstants.failedCode, "Unsupported file format for preview.");
					}
					logger.info("File preview sent successfully.");
					return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, contentType)
							.header("Cache-Control", "no-cache, no-store, must-revalidate").header("Pragma", "no-cache")
							.header("Expires", "0").body(resource);
				} catch (MalformedURLException e) {
					logger.error("Error creating URL from file path: " + absoluteFilePath);
					throw new HRMSException(IHRMSConstants.failedCode, "Invalid file path provided.");
				}
			} else {
				logger.error("File not found at path: " + absoluteFilePath);
				throw new HRMSException(IHRMSConstants.failedCode, "File not found.");
			}
		} else {
			logger.error("Empty filePath parameter received.");
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}
	}

	// Helper method to determine content type based on file extension
	private String determineContentType(String filename) {
		String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
		switch (extension) {
		case "pdf":
			return "application/pdf";
		case "jpg":
		case "jpeg":
			return "image/jpeg";
		case "png":
			return "image/png";
		// Add more cases for other supported formats (e.g., docx, xlsx, etc.)
		// case "docx":
		// return
		// "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		// case "xlsx":
		// return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		default:
			return null; // Unsupported format
		}
	}

	/****************
	 *** added by akanksha to download all documents***
	 ***************/

	@RequestMapping(value = "/downloadAllDocuments", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadAllDocuments(@RequestParam(required = true) long candId,
			String documentName, HttpServletRequest servletRequest) throws HRMSException, MalformedURLException {

		String token = JWTTokenHelper.parseJwt(servletRequest);
		Claims loggedInEmpDetails;
		if (!HRMSHelper.isNullOrEmpty(token)) {
			loggedInEmpDetails = JWTTokenHelper.getLoggedInEmpDetail(token);
		} else {
			throw new HRMSException(IHRMSConstants.failedCode, IHRMSConstants.TOKEN_NOT_VALID);
		}

		String fileName = null;
		String url = null;

		if (!HRMSHelper.isNullOrEmpty(candId)) {
			if (documentName.equals("Offer Letter") || documentName.equals("Appointment Letter")
					|| documentName.equals("Relieving Letter") || documentName.equals("Experience Letter")) {

				CandidateLetter candLetters = candidateLetterDAO.findLetterByCandidateIdAndLetterType(candId,
						documentName);
				fileName = candLetters.getFileName();
				// url = "E:" + candLetters.getLetterUrl() + candLetters.getFileName();
				url = rootDirectory + candLetters.getLetterUrl().replace("/webdav/", "") + candLetters.getFileName();
			} else {

				Candidate candidate = candidateDAO.findById(candId).get();
				CandidateChecklist checklist = checklistDAO.getCandidateWithChecklistDetailsByItem(documentName,
						candId);

				if (!HRMSHelper.isNullOrEmpty(checklist)) {

					fileName = checklist.getAttachment();
					url = rootDirectory + candidate.getLoginEntity().getOrganization().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
							+ candidate.getId() + File.separator + checklist.getAttachment();

				}
			}

			// String url = rootDirectory;
			File file = new File(url);
			logger.info("Document Path:" + url);
			logger.info("file exist: " + file.exists());
			logger.info("filename: " + fileName);
			if (file.exists()) {
				Resource resource = new UrlResource(file.toURI());
				HttpHeaders headers = new HttpHeaders();
				headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName.replace(" ", "") + " ");
				headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
				headers.add("Pragma", "no-cache");
				headers.add("Expires", "0");

				return ResponseEntity.ok().headers(headers).contentLength(file.length())
						.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
			}

		} else {

			new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		return ResponseEntity.badRequest().build();
	}

	/*************
	 * All Document Preview added by akanksha
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 ****/

	@RequestMapping(value = "/previewAllDocuments", method = RequestMethod.GET)
	public ResponseEntity<String> previewAllDocuments(@RequestParam(required = true) long candId, String documentName,
			HttpServletRequest servletRequest) throws HRMSException, FileNotFoundException, IOException {

		String token = JWTTokenHelper.parseJwt(servletRequest);
		Claims loggedInEmpDetails;
		if (!HRMSHelper.isNullOrEmpty(token)) {
			loggedInEmpDetails = JWTTokenHelper.getLoggedInEmpDetail(token);
		} else {
			throw new HRMSException(IHRMSConstants.failedCode, IHRMSConstants.TOKEN_NOT_VALID);
		}

		String fileName = null;
		String url = null;

		if (!HRMSHelper.isNullOrEmpty(candId)) {
			if (documentName.equals("Offer Letter") || documentName.equals("Appointment Letter")
					|| documentName.equals("Relieving Letter") || documentName.equals("Experience Letter")) {

				CandidateLetter candLetters = candidateLetterDAO.findLetterByCandidateIdAndLetterType(candId,
						documentName);
				fileName = candLetters.getFileName();
				url = "E:" + candLetters.getLetterUrl() + candLetters.getFileName();

			} else if (documentName.equals("PHOTO")) {

				Candidate candidate = candidateDAO.findById(candId).get();
				CandidateChecklist checklist = checklistDAO.getCandidateWithChecklistDetailsByItem(documentName,
						candId);

				if (!HRMSHelper.isNullOrEmpty(checklist)) {

					fileName = checklist.getAttachment();
					url = rootDirectory + candidate.getLoginEntity().getOrganization().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
							+ candidate.getId() + File.separator + "Photo" + File.separator + checklist.getAttachment();

				}
			} else {

				Candidate candidate = candidateDAO.findById(candId).get();
				CandidateChecklist checklist = checklistDAO.getCandidateWithChecklistDetailsByItem(documentName,
						candId);

				if (!HRMSHelper.isNullOrEmpty(checklist)) {

					fileName = checklist.getAttachment();
					url = rootDirectory + candidate.getLoginEntity().getOrganization().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
							+ candidate.getId() + File.separator + checklist.getAttachment();

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

			ResponseEntity<String> res = ResponseEntity.ok().headers(header).contentType(MediaType.TEXT_PLAIN)
					.body(base64Content);

			return res;

		} else {

			new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		return ResponseEntity.badRequest().build();
	}

	@RequestMapping(value = "/deleteUploadedFile", method = RequestMethod.GET)
	public HRMSBaseResponse deleteUploadedFile(@RequestParam(required = true) long candId, String documentName,
			HttpServletRequest servletRequest) throws HRMSException, FileNotFoundException, IOException {
		HRMSBaseResponse response = new HRMSBaseResponse();

		String fileName = null;
		String url = null;

		if (!HRMSHelper.isNullOrEmpty(candId)) {
			if (documentName.equals("Offer Letter") || documentName.equals("Appointment Letter")
					|| documentName.equals("Relieving Letter") || documentName.equals("Experience Letter")) {

				CandidateLetter candLetters = candidateLetterDAO.findLetterByCandidateIdAndLetterType(candId,
						documentName);
				fileName = candLetters.getFileName();
				url = "E:" + candLetters.getLetterUrl() + candLetters.getFileName();

				Path path = Paths.get(url);
				if (Files.exists(path)) {
					Files.delete(path);
					candidateLetterDAO.deleteById(candLetters.getId());
					response.setResponseMessage(IHRMSConstants.successMessage);
				} else {
					response.setResponseMessage(IHRMSConstants.DataNotFoundMessage);
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);

				}

			} else if (documentName.equals("PHOTO")) {

				Candidate candidate = candidateDAO.findById(candId).get();
				CandidateChecklist checklist = checklistDAO.getCandidateWithChecklistDetailsByItem(documentName,
						candId);

				if (!HRMSHelper.isNullOrEmpty(checklist)) {

					fileName = checklist.getAttachment();
					url = rootDirectory + candidate.getLoginEntity().getOrganization().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
							+ candidate.getId() + File.separator + "Photo" + File.separator + checklist.getAttachment();
					Path path = Paths.get(url);
					if (Files.exists(path)) {
						Files.delete(path);
						checklistDAO.deleteById(checklist.getId());
						response.setResponseMessage(IHRMSConstants.successMessage);
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
				}
			} else {

				Candidate candidate = candidateDAO.findById(candId).get();
				CandidateChecklist checklist = checklistDAO.getCandidateWithChecklistDetailsByItem(documentName,
						candId);

				if (!HRMSHelper.isNullOrEmpty(checklist)) {

					fileName = checklist.getAttachment();
					url = rootDirectory + candidate.getLoginEntity().getOrganization().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
							+ candidate.getId() + File.separator + checklist.getAttachment();
					Path path = Paths.get(url);
					if (Files.exists(path)) {
						Files.delete(path);
						checklistDAO.deleteById(checklist.getId());
						response.setResponseMessage(IHRMSConstants.successMessage);
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}

				}
			}

		} else {

			new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		return response;
	}

}
