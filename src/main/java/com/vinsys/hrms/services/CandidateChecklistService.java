package com.vinsys.hrms.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.IHRMSCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSMasterCandidateChecklistActionDAO;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateChecklist;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.MasterCandidateChecklistAction;
import com.vinsys.hrms.entity.MasterOrganizationEmailConfig;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping("/candidateChecklist")

//@PropertySource(value="${HRMSCONFIG}")
public class CandidateChecklistService {
	@Value("${base.url}")
	private String baseURL;
	@Value("${hr.email.id}")
	private String hrEmailID;
	@Value("${HR_Name}")
	private String HR_Name;
	
	@Autowired
	IHRMSProfessionalDetailsDAO professionalDetailsDAO;
	@Autowired
	IHRMSCandidateChecklistDAO checklistDAO;
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSMasterCandidateChecklistActionDAO masterCandidateChecklistActionDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO configDAO;
	
	@Value("${rootDirectory}")
	private String rootDirectory;

	
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public String addCandidateChecklist(@RequestBody List<VOCandidateChecklist> ListVOCandidateChecklist)
			throws JsonGenerationException, JsonMappingException, IOException {
		try {
			Map<String, String> mailContentMap = new HashMap<String, String>();

			boolean toSendEmail = false;
			int count = 1;
			StringBuilder documentVerificationHeader = new StringBuilder(IHRMSEmailTemplateConstants.Document_Verification_Mail_Header);
			String documentVerificationFooter = IHRMSEmailTemplateConstants.Document_Verification_Mail_Footer;
			
			Candidate candidateEntity = null;
			for (VOCandidateChecklist voCandidateChecklist : ListVOCandidateChecklist) {

				if (!HRMSHelper.isNullOrEmpty(voCandidateChecklist) && !HRMSHelper
						.isNullOrEmpty(voCandidateChecklist.getMasterCandidateChecklistAction().getId())) {

					if (voCandidateChecklist.getId() > 0) {
						CandidateChecklist checklist = checklistDAO.findById(voCandidateChecklist.getId()).get();
						MasterCandidateChecklistAction candidateChecklistEntity = masterCandidateChecklistActionDAO
								.findById(voCandidateChecklist.getMasterCandidateChecklistAction().getId()).get();
						if (checklist != null) {
							candidateEntity = checklist.getCandidateProfessionalDetail().getCandidate();
							checklist = HRMSRequestTranslator.translateToCandidateChecklistEntity(checklist,
									voCandidateChecklist);
							checklist.setMasterCandidateChecklistAction(candidateChecklistEntity);
							if (candidateChecklistEntity.getCandidateChecklistActionName()
									.equalsIgnoreCase(IHRMSConstants.VerifiedDocument)) {
								checklist.setHrValidationStatus(true);
							} else {
								toSendEmail = true;
								checklist.setHrValidationStatus(false);
								String documentName = "{document" + count + "}";
								String documentType = "{documentType" + count + "}";
								mailContentMap.put(documentName, checklist.getAttachment());
								mailContentMap.put(documentType, checklist.getChecklistItem());
								mailContentMap.put("{reason}",
										candidateChecklistEntity.getCandidateChecklistActionName());
								mailContentMap.put("{candidateFirstName}", candidateEntity.getFirstName());
								mailContentMap.put("{candidateMiddleName}", candidateEntity.getMiddleName());
								mailContentMap.put("{candidateLastName}", candidateEntity.getLastName());

								documentVerificationHeader.append("<tr>\r\n" + "<td style=\"width: 44px;\">" + count + "</td>\r\n"
										+ "<td style=\"width: 150px;\">" + documentType +"</td>\r\n"
										+ "<td style=\"width: 150px;\">" + documentName + "</td>\r\n"
										+ "<td style=\"width: 879px;\">{reason}</td>\r\n" + "</tr>");
								count++;
							}
							checklistDAO.save(checklist);
						} else {
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.DataNotFoundMessage);
						}

					} else {
						
						/*
						if (!HRMSHelper.isNullOrEmpty(voCandidateChecklist)
								&& !HRMSHelper.isNullOrEmpty(voCandidateChecklist.getCandidateProfessionalDetail())
								&& !HRMSHelper.isNullOrEmpty(
										voCandidateChecklist.getCandidateProfessionalDetail().getCandidate())) {

							Candidate candidate = candidateDAO.findById(
									voCandidateChecklist.getCandidateProfessionalDetail().getCandidate().getId());
							if (candidate != null) {
								CandidateChecklist checklistEntity = new CandidateChecklist();
								checklistEntity = HRMSRequestTranslator
										.translateToCandidateChecklistEntity(checklistEntity, voCandidateChecklist);

								CandidateProfessionalDetail professionalDetailsEntity = professionalDetailsDAO
										.findBycandidate(
												checklistEntity.getCandidateProfessionalDetail().getCandidate());
								checklistEntity.setCandidateProfessionalDetail(professionalDetailsEntity);
								checklistDAO.save(checklistEntity);

								// return
								// HRMSHelper.sendSuccessResponse(IHRMSConstants.successMessage,IHRMSConstants.successCode);
							} else {
								throw new HRMSException(IHRMSConstants.DataNotFoundCode,
										IHRMSConstants.CandidateDoesnotExistMessage);
							}
						} else {
							throw new HRMSException(IHRMSConstants.InsufficientDataCode,
									IHRMSConstants.InsufficientDataMessage);
						}
					*/
					}
				} else {
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage); 
				}
			}
			if(toSendEmail) {
				documentVerificationHeader.append(documentVerificationFooter);
				String mailContent = HRMSHelper.replaceString(mailContentMap, documentVerificationHeader.toString());
				//EmailSender.toSendEmail(candidateEntity.getEmailId(), null, mailContent, IHRMSConstants.DOCUMENT_VERIFICATION_FAILED_SUBJECT);
				emailsender.toPersistEmail(candidateEntity.getEmailId(), null, mailContent, IHRMSConstants.DOCUMENT_VERIFICATION_FAILED_SUBJECT,
						candidateEntity.getCandidateProfessionalDetail().getDivision().getId(), candidateEntity.getLoginEntity().getOrganization().getId());
			}
			
			return HRMSHelper.sendSuccessResponse(IHRMSConstants.updateSuccesMessage, IHRMSConstants.successCode);
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
		return null;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getCandidateChecklist(@PathVariable("id") long id) {

		try {
			HRMSListResponseObject response = null;
			// Candidate candidate = candidateDAO.findById(id);
			Candidate candidate = checklistDAO.getCandidateWithChecklistDetailsForCand(id, 1);
			if (candidate != null && candidate.getCandidateProfessionalDetail() != null) {
				response = new HRMSListResponseObject();
				Set<CandidateChecklist> checkListEntitySet = candidate.getCandidateProfessionalDetail()
						.getCandidateChecklists();
				List<Object> checkListModel = new ArrayList<Object>();
				for (CandidateChecklist checkListEntity : checkListEntitySet) {
					VOCandidateChecklist model = HRMSEntityToModelMapper
							.convertToCandidateChecklistDetailsModel(checkListEntity);
//					String path = HRMSPropertyFileLoader.getPropertyFile(IHRMSConstants.Config_PropertyFile)
//							.getProperty("base.url") +"/webdav/"+ candidate.getLoginEntity().getOrganization().getId() + "/"
//							+ candidate.getCandidateProfessionalDetail().getDivision().getId() + "/"
//							+ candidate.getCandidateProfessionalDetail().getBranch().getId() + "/" + candidate.getId()+"/"
//							+ model.getAttachment();
					String path = rootDirectory + candidate.getLoginEntity().getOrganization().getId() + "/"
							+ candidate.getCandidateProfessionalDetail().getDivision().getId() + "/"
							+ candidate.getCandidateProfessionalDetail().getBranch().getId() + "/" + candidate.getId()+"/"
							+ model.getAttachment();
					model.setUrl(path);
					checkListModel.add(model);

				}
				response.setListResponse(checkListModel);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			return HRMSHelper.createJsonString(response);
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
		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
	@ResponseBody
	public String deleteCandidateChecklist(@PathVariable("id") long checkListId) {

		try {
			if (checkListId > 0) {
				CandidateChecklist checklistEntity = checklistDAO.findById(checkListId).get();
				if (checklistEntity != null) {
					checklistDAO.delete(checklistEntity);
					return HRMSHelper.sendSuccessResponse(IHRMSConstants.deletedsuccessMessage,
							IHRMSConstants.successCode);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
		return null;
	}

	@RequestMapping(value = "/sendDocNotification/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String sendNotificationForDocumentUpload(@PathVariable("id") long candidateId) {

		try {

			Candidate candidate = candidateDAO.findById(candidateId).get();
			if (candidateId > 0 && candidate != null) {

				Set<CandidateChecklist> candidateCheckList = candidate.getCandidateProfessionalDetail()
						.getCandidateChecklists();
				if (candidateCheckList != null) {
					StringBuilder documentVerificationHeader = new StringBuilder(
							IHRMSEmailTemplateConstants.Document_Sending_Mail_Header);
					String documentVerificationFooter = IHRMSEmailTemplateConstants.Document_Verification_Mail_Footer;
					int count = 1;
					
					Map<String, String> mailContentMap = new HashMap<String, String>();
					mailContentMap.put("{candidateFirstName}", candidate.getFirstName());
					mailContentMap.put("{candidateMiddleName}", candidate.getMiddleName());
					mailContentMap.put("{candidateLastName}", candidate.getLastName());
					mailContentMap.put("{candidateId}", String.valueOf(candidate.getId()));
					mailContentMap.put("{toName}", HR_Name);
					
					for (CandidateChecklist checkList : candidateCheckList) {
						if (checkList.getSubmitted().equalsIgnoreCase(IHRMSConstants.SubmitDocument) && !checkList.getHrValidationStatus()) {
							String documentName = "{document"+ count + "}";
							String documentType = "{documentType"+ count + "}";
							mailContentMap.put(documentName, checkList.getAttachment());
							mailContentMap.put(documentType, checkList.getChecklistItem());
							documentVerificationHeader.append("<tr>" + "<td>" + count + "</td>"+
							"<td>" + documentType + "</td>"
									+"<td>" + documentName + "</td>" + "</tr>");
							count++;
						}

					}
					documentVerificationHeader.append(documentVerificationFooter);
					String mailContent = HRMSHelper.replaceString(mailContentMap,
							documentVerificationHeader.toString());
					String subject = candidate.getId() + "_" + candidate.getFirstName() + "_" + candidate.getLastName()
							+ " Document_Upload";
					
					MasterOrganizationEmailConfig emailConfig = configDAO.findByorganizationAnddivision(candidate.getLoginEntity().getOrganization().getId(), candidate.getCandidateProfessionalDetail().getDivision().getId());
					if(emailConfig != null) {
						
						//EmailSender.toSendEmail(emailConfig.getHrEmailId(), null, mailContent, subject);
						emailsender.toPersistEmail(emailConfig.getHrEmailId(), null, mailContent, subject, candidate.getCandidateProfessionalDetail().getDivision().getId(),
								candidate.getLoginEntity().getOrganization().getId());
					}
					
					return HRMSHelper.sendSuccessResponse(IHRMSConstants.DOCUMENT_UPLOAD_SUCCESS_MESSAGE, IHRMSConstants.successCode);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.NoDocumentFound);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.CandidateDoesnotExistMessage);
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
		return null;
	}
	
}
