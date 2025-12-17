package com.vinsys.hrms.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vinsys.hrms.dao.IHRMSCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSMasterCandidateChecklistActionDAO;
import com.vinsys.hrms.datamodel.HRMSFileUploadResponse;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.MasterCandidateChecklistAction;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/fileUpload")
//@PropertySource(value="${HRMSCONFIG}")
public class FileUploadService {

	@Value("${base.webdav.url}")
	private String baseURL;

	@Value("${MaxFileUploadSize}")
	private long MaxSize;

	@Value("${rootDirectory}")
	private String rootDirectory;

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSCandidateChecklistDAO candidateChecklistDAO;

	@Autowired
	IHRMSMasterCandidateChecklistActionDAO candidateChecklistActionDAO;

	HRMSFileUploadResponse fileUploadResponse = new HRMSFileUploadResponse();

	Logger logger = LoggerFactory.getLogger(FileUploadService.class);

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, produces = "application/json")
	@ResponseBody
	public String test(@RequestParam("file") MultipartFile[] request, long id, String uploadtype, String document_name,
			String submited, String mandatory, String createdBy, String updatedBy, String createdDate,
			String updatedDate, String remarks, String orgid, String branchId, String divisionId) throws HRMSException {
		try {
			if (HRMSHelper.isNullOrEmpty(request)) {
				throw new HRMSException(IHRMSConstants.FileUploadCode, IHRMSConstants.FileUploadErrorMessage);
			}
			if (id > 0 && !HRMSHelper.isNullOrEmpty(uploadtype) && !HRMSHelper.isNullOrEmpty(request)) {
				// Sardine sardine = null;
				// sardine = SardineFactory.begin();
				// String savePath = "http://localhost:8080/webdav/" + id;
				// String savePath =
				// HRMSPropertyFileLoader.getPropertyFile(IHRMSConstants.Config_PropertyFile).getProperty("base.url")+"/webdav/"
				// +orgid ;

				//String savePath = rootDirectory + orgid;
				String savePath = "D:"+ File.separator + "input" + File.separator+ orgid;

				logger.info("savepath : " + savePath);

				SimpleDateFormat df2 = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);

				boolean checkupdate = false;

				for (MultipartFile file : request) {
					// logger.info("Size " + file.getSize());
					logger.info("File  " + file.getName());
					logger.info("File name " + file.getOriginalFilename());
					logger.info("File size " + file.getSize());

					byte[] bytes = file.getBytes();

					String str = "";
					logger.info("str : " + str);

					if (file.getSize() > MaxSize) {
						throw new HRMSException(IHRMSConstants.FileUploadSizeLimitCode,
								IHRMSConstants.FileUploadSizeLimitErrorMessage);
					}

					Candidate candidateEntity = candidateDAO.findById(id).get();
					if (!HRMSHelper.isNullOrEmpty(candidateEntity)) // This is use to check candidate is exist or not.
					{
						str = savePath;// .concat("/gallery") + "/" + file.getOriginalFilename().replaceAll("\\s+",
										// "_");
						Path path = Paths.get(str);
						logger.info("str : " + str);
						logger.info("path : " + path);

						if (!Files.exists(path)) {
							Files.createDirectory(Paths.get(path.toUri()));
						}
						str = path + File.separator + divisionId;
						Path p = Paths.get(str);
						logger.info("str : " + str);
						logger.info("path : " + p);

						if (!Files.exists(p)) {
							// Files.createDirectory(p);
							Files.createDirectory(Paths.get(p.toUri()));
							// str= p + "/" + divisionId;
						} // else {
							// str = p + "/" + divisionId;
							// }

						str = str + File.separator + branchId;
						Path p1 = Paths.get(str);
						logger.info("str : " + str);
						logger.info("path : " + p1);

						if (!Files.exists(p1)) {
							// Files.createDirectory();

							Files.createDirectory(Paths.get(p1.toUri()));
							// str = p1 + "/" + branchId;
						} // else {
							// str = p1 + "/" + branchId;
							// }

						str = str + File.separator + id;
						Path p2 = Paths.get(str);
						logger.info("str : " + str);
						logger.info("path : " + p2);

						if (!Files.exists(p2)) {
							// Files.createDirectory();
							Files.createDirectory(Paths.get(p2.toUri()));
							// str = p2 + "/" + id;
						} // else {
							// str = p2 + "/" + id;
							// }

						if (uploadtype.equalsIgnoreCase("document")) // This is use to upload candidate's document
						{
//							str = str + "/" + file.getOriginalFilename().replaceAll("\\s+", "_");
							// sardine.put(str, bytes);
							Files.write(Paths.get(str, file.getOriginalFilename().replaceAll("\\s+", "_")),
									file.getBytes());
							logger.info("str : " + str);

							CandidateChecklist candidateCheckList = candidateChecklistDAO
									.findCandidateCheckListByProfessionalId(
											candidateEntity.getCandidateProfessionalDetail().getId(), document_name);

							// if(!HRMSHelper.isNullOrEmpty(candidateCheckList)) {

							/*
							 * to set master candidate checklist action : in IHRMSConstant, variable
							 * PendingDocument = "Pending" is present. Here, first get id of
							 * masterCandidateChecklistAction entity which is orgwise and name == "Pending"
							 * And then setting masterCandidateChecklistAction item in it.
							 * 
							 * 
							 */
							MasterCandidateChecklistAction pendingCandidateChecklistAction = null;
							if (!HRMSHelper.isLongZero(candidateEntity.getLoginEntity().getOrganization().getId())) {
								/*
								 * fetching master of candidate checklist action for type Pending
								 */
								pendingCandidateChecklistAction = candidateChecklistActionDAO
										.findAllCandidateChecklistActionOrgwiseAndActionNamewise(
												candidateEntity.getLoginEntity().getOrganization().getId(),
												IHRMSConstants.PendingDocument);
							} else {
								throw new HRMSException(IHRMSConstants.InsufficientDataCode,
										IHRMSConstants.InsufficientDataMessage);
							}

							if (!HRMSHelper.isNullOrEmpty(candidateCheckList)
									&& submited.equalsIgnoreCase(IHRMSConstants.SubmitDocument)) {
								candidateCheckList.setAttachment(file.getOriginalFilename().replaceAll("\\s+", "_"));
								candidateCheckList.setChecklistItem(document_name);
								candidateCheckList.setUpdatedDateTime(new Date());
								candidateCheckList.setSubmitted(IHRMSConstants.SubmitDocument);
								candidateCheckList.setMandatory(mandatory);
								candidateCheckList.setUpdatedBy(updatedBy);
								candidateCheckList.setUpdatedDate(new Date());
								candidateCheckList.setRemark(remarks);
								candidateCheckList.setIsActive(IHRMSConstants.isActive);
								candidateCheckList.setCandidateProfessionalDetail(
										candidateEntity.getCandidateProfessionalDetail());
								candidateCheckList.setHrValidationStatus(false);

								if (!HRMSHelper.isNullOrEmpty(pendingCandidateChecklistAction)) {
									candidateCheckList
											.setMasterCandidateChecklistAction(pendingCandidateChecklistAction);
									candidateChecklistDAO.save(candidateCheckList);
									// logger.info("submitted for : " +candidateEntity.getFirstName());

								} else {
									throw new HRMSException(IHRMSConstants.InsufficientDataCode,
											IHRMSConstants.InsufficientDataMessage);
								}

							} else { // This is use when candidate already upload document and uploaded next document
										// against that candidate
								CandidateChecklist checklistEntity = new CandidateChecklist();
								checklistEntity.setAttachment(file.getOriginalFilename().replaceAll("\\s+", "_"));
								checklistEntity.setChecklistItem(document_name);
								checklistEntity.setUpdatedDateTime(new Date());
								checklistEntity.setSubmitted(IHRMSConstants.SubmitDocument);
								checklistEntity.setMandatory(mandatory);
								checklistEntity.setCreatedBy(createdBy);
								checklistEntity.setRemark(remarks);
								checklistEntity.setCreatedDate(new Date());
								checklistEntity.setIsActive(IHRMSConstants.isActive);
								checklistEntity.setHrValidationStatus(false);
								checklistEntity.setCandidateProfessionalDetail(
										candidateEntity.getCandidateProfessionalDetail());
								// next added by SSW on 03Apr2019
								// for : saving document type
								if (document_name.equalsIgnoreCase("SALARY ANNEXURE")) {
									checklistEntity.setIsDocumentOnlyForCandidate(0);
								} else {
									checklistEntity.setIsDocumentOnlyForCandidate(1);
								}
								checklistEntity.setIsDocumentOnlyForEmployee(1);
								if (!HRMSHelper.isNullOrEmpty(pendingCandidateChecklistAction)) {
									checklistEntity.setMasterCandidateChecklistAction(pendingCandidateChecklistAction);
									candidateChecklistDAO.save(checklistEntity);
								} else {
									throw new HRMSException(IHRMSConstants.InsufficientDataCode,
											IHRMSConstants.InsufficientDataMessage);
								}
							}
							/*
							 * } else { //this is use to when there is no entry in tbl_candidate_checklist
							 * against candidate then it will create entry CandidateChecklist
							 * checklistEntity = new CandidateChecklist();
							 * checklistEntity.setAttachment(file.getOriginalFilename());
							 * checklistEntity.setChecklistItem(document_name);
							 * checklistEntity.setCandidateProfessionalDetail(candidateEntity.
							 * getCandidateProfessionalDetail());
							 * candidateChecklistDAO.save(checklistEntity); }
							 */
						} else // This is use to upload candidate photo
						{
							// str = savePath + "/" + file.getOriginalFilename();
							str = p2 + File.separator + "Photo";

							Path p3 = Paths.get(str);
							logger.info("str : " + str);
							logger.info("path : " + p3);

							if (!Files.exists(p3)) {
								Files.createDirectory(p3);
							}
							// str = savePath.concat("/Photo") + "/" +
							// file.getOriginalFilename().replaceAll("\\s+", "_");
							// sardine.put(str, bytes);
							Files.write(Paths.get(str, file.getOriginalFilename()), file.getBytes());
							logger.info("str : " + str);

							// candidateEntity.getCandidatePersonalDetail().setCandidatePhoto(file.getOriginalFilename().replaceAll("\\s+","_"));
							// candidateDAO.save(candidateEntity);

						}

					} else {
						throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
								IHRMSConstants.CandidateDoesnotExistMessage);
					}
					fileUploadResponse.setSubmitted(IHRMSConstants.SubmitDocument);
					fileUploadResponse.setFile_name(file.getOriginalFilename());
					fileUploadResponse.setPath(str);
					fileUploadResponse.setResponseCode(IHRMSConstants.successCode);
					fileUploadResponse.setResponseMessage(IHRMSConstants.addedsuccessMessage);
					Date date = new Date();

					String dateText = df2.format(date);
					fileUploadResponse.setUpdateDate(dateText);
					return HRMSHelper.createJsonString(fileUploadResponse);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		// return null;
		return null;
	}

}
