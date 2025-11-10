package com.vinsys.hrms.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateActivityDAO;
import com.vinsys.hrms.dao.IHRMSCandidateActivityLetterMappingDAO;
import com.vinsys.hrms.dao.IHRMSCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateLetterDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationLetterMappingDAO;
import com.vinsys.hrms.dao.IHRMSHtmlTemplateDAO;
import com.vinsys.hrms.dao.IHRMSMapOrgDivHrDAO;
import com.vinsys.hrms.dao.IHRMSMasterCandidateActivityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmailSenderDAO;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.dao.IHRMSOrgDivSeparationLetterMappingDAO;
import com.vinsys.hrms.dao.IHRMSSeparationLetterTemplateDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateLetter;
import com.vinsys.hrms.datamodel.VOOrgDivSeparationLetter;
import com.vinsys.hrms.datamodel.VOSeparationLetterRequest;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateActivity;
import com.vinsys.hrms.entity.CandidateActivityLetterMapping;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.CandidateLetter;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeSeparationDetails;
import com.vinsys.hrms.entity.EmployeeSeparationLetterMapping;
import com.vinsys.hrms.entity.HtmlTemplate;
import com.vinsys.hrms.entity.MapOrgDivSeparationLetter;
import com.vinsys.hrms.entity.MasterCandidateActivity;
import com.vinsys.hrms.entity.MasterCandidateChecklist;
import com.vinsys.hrms.entity.MasterOrganizationEmailConfig;
import com.vinsys.hrms.entity.OrgDivWiseHRMapping;
import com.vinsys.hrms.entity.SeparationLetterTemplate;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;
import com.vinsys.hrms.util.PDFUtility;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/candidateLetter")

//@PropertySource(value="${HRMSCONFIG}")
public class CandidateLetterService {
	@Autowired
	IHRMSMapOrgDivHrDAO orgDivHRDAO;
	@Value("${base.url}")
	private String baseURL;
	@Value("${rootDirectory}")
	private String rootDirectory;
	@Autowired
	IHRMSEmployeeSeparationDAO employeeSeparationDAO;

	private static final Logger logger = LoggerFactory.getLogger(CandidateLetterService.class);

	@Autowired
	IHRMSCandidateLetterDAO candidateLetterDAO;

	@Autowired
	IHRMSCandidateDAO candidateDao;
	@Autowired
	IHRMSHtmlTemplateDAO emailTemplateDAO;
	@Autowired
	IHRMSMasterCandidateActivityDAO masterActivityDAO;
	@Autowired
	IHRMSMasterCandidateActivityDAO masterCandidateActitvityDAO;
	@Autowired
	IHRMSCandidateActivityDAO candidateActivityDAO;
	@Autowired
	IHRMSCandidateActivityLetterMappingDAO activityLetterMappingDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO configDAO;
	@Autowired
	IHRMSMasterEmailSenderDAO emailSenderDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSSeparationLetterTemplateDAO separationLetterTemplateDAO;
	@Autowired
	IHRMSEmployeeSeparationLetterMappingDAO separationLetterMappingDAO;
	@Autowired
	IHRMSOrgDivSeparationLetterMappingDAO orgDivSeparationLetterMappingDAO;
	@Autowired
	IHRMSMasterCandidateChecklistDAO masterCandidateChecklistDAO;
	@Autowired
	IHRMSCandidateChecklistDAO candidateChecklistDAO;

	@RequestMapping(value = "letterAction/{letterId}/{action}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String letterAction(@PathVariable("letterId") long letterId, @PathVariable("action") String action) {

		try {
			if (letterId > 0) {

				logger.info(" == >> Taking action on Letter << ==");

				/*
				 * Finding Candidate Letter,Candidate Activity and Candidate LetterToActivity
				 * Mapping
				 */
				CandidateLetter letter = candidateLetterDAO.findById(letterId).get();

				if (letter == null) {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.INVALID_LETTER);
				}
				/*
				 * Writing code for email while taking action on letter ,on 27 December 2017
				 */
				Candidate candidate = candidateDao.findById(letter.getCandidate().getId()).get();
				if (candidate == null) {
					throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
							IHRMSConstants.CandidateDoesnotExistMessage);
				}

				long divisionId = candidate.getCandidateProfessionalDetail().getDivision().getId();
				long organizationId = candidate.getLoginEntity().getOrganization().getId();
				MasterOrganizationEmailConfig conf = configDAO.findByorganizationAnddivision(organizationId,
						divisionId);

				if (conf == null) {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode,
							IHRMSConstants.EMAIL_CONFIGURATION_NOT_FOUND);
				}

				Map<String, String> templateCreationMap = new HashMap<String, String>();
				templateCreationMap.put("{hrName}", conf.getHrName());
				templateCreationMap.put("{candidateFirstName}", candidate.getFirstName());
				templateCreationMap.put("{candidateMiddleName}", candidate.getMiddleName());
				templateCreationMap.put("{candidateLastName}", candidate.getLastName());
				templateCreationMap.put("{candidateUserName}", candidate.getLoginEntity().getUsername());
				templateCreationMap.put("{candidatePassword}", candidate.getLoginEntity().getPassword());
				templateCreationMap.put("{candidateDesignation}",
						candidate.getCandidateProfessionalDetail().getDesignation().getDesignationName());
				templateCreationMap.put("{city}",
						candidate.getCandidateProfessionalDetail().getBranch().getBranchName());
				templateCreationMap.put("{currentDate}", String.valueOf(new Date()));
				templateCreationMap.put("{dateOfJoin}",
						String.valueOf(candidate.getCandidateProfessionalDetail().getDateOfJoining()));
				templateCreationMap.put("{dateOfIssue}",
						HRMSDateUtil.format(letter.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));

				CandidateActivityLetterMapping letterMapping = activityLetterMappingDAO.findBycandidateLetter(letter);
				CandidateActivity activity = candidateActivityDAO.findById(letterMapping.getCandidateActivity().getId())
						.get();

				if (action.equalsIgnoreCase(IHRMSConstants.OfferLetterAction_ACCEPT)) {

					if (letter.getStatus().equalsIgnoreCase(IHRMSConstants.LetterStatus_PENDING)) {

						letter.setStatus(IHRMSConstants.LetterStatus_ACCEPT);
						activity.setActivityResponseDate(new Date());
						templateCreationMap.put("{letterType}", letter.getLetterType());
						templateCreationMap.put("{action}", IHRMSConstants.LetterStatus_ACCEPT);
						candidate.setCandidateActivityStatus(IHRMSConstants.CANDIDATE_ACTIVITY_OFFER_LETTER_ACCEPTED);
						logger.info(" == >> Offer Letter Accepted << ==");

					} else {
						throw new HRMSException(IHRMSConstants.OfferLetterNotInPendingStateCode,
								IHRMSConstants.OfferLetterNotInPendingStateMessage);
					}
				} else if (action.equalsIgnoreCase(IHRMSConstants.OfferLetterAction_REJECT)) {

					if (letter.getStatus().equalsIgnoreCase(IHRMSConstants.LetterStatus_PENDING)) {

						letter.setStatus(IHRMSConstants.LetterStatus_REJECT);
						activity.setActivityResponseDate(new Date());
						templateCreationMap.put("{letterType}", letter.getLetterType());
						templateCreationMap.put("{action}", IHRMSConstants.LetterStatus_REJECT);
						candidate.setCandidateActivityStatus(IHRMSConstants.CANDIDATE_ACTIVITY_OFFER_LETTER_REJECTED);
						logger.info(" == >> Offer Letter REJECTED << ==");

					} else {
						throw new HRMSException(IHRMSConstants.OfferLetterNotInPendingStateCode,
								IHRMSConstants.OfferLetterNotInPendingStateMessage);
					}
				} else if (action.equalsIgnoreCase(IHRMSConstants.AppointmentLetterAction_ACCEPT)) {

					if (letter.getStatus().equalsIgnoreCase(IHRMSConstants.LetterStatus_PENDING)) {

						letter.setStatus(IHRMSConstants.LetterStatus_ACCEPT);
						activity.setActivityResponseDate(new Date());
						templateCreationMap.put("{letterType}", letter.getLetterType());
						templateCreationMap.put("{action}", IHRMSConstants.LetterStatus_ACCEPT);
						candidate.setCandidateActivityStatus(
								IHRMSConstants.CANDIDATE_ACTIVITY_APPOINTMENT_LETTER_ACCEPTED);
						logger.info(" == >> Appointment Letter Accepted << ==");

					} else {
						throw new HRMSException(IHRMSConstants.OfferLetterNotInPendingStateCode,
								IHRMSConstants.OfferLetterNotInPendingStateMessage);
					}
				} else if (action.equalsIgnoreCase(IHRMSConstants.AppointmentLetterAction_REJECT)) {

					if (letter.getStatus().equalsIgnoreCase(IHRMSConstants.LetterStatus_PENDING)) {

						letter.setStatus(IHRMSConstants.LetterStatus_REJECT);
						activity.setActivityResponseDate(new Date());
						templateCreationMap.put("{letterType}", letter.getLetterType());
						templateCreationMap.put("{action}", IHRMSConstants.LetterStatus_REJECT);
						candidate.setCandidateActivityStatus(
								IHRMSConstants.CANDIDATE_ACTIVITY_APPOINTMENT_LETTER_REJECTED);
						logger.info(" == >> Appointment Letter REJECTED << ==");

					} else {
						throw new HRMSException(IHRMSConstants.OfferLetterNotInPendingStateCode,
								IHRMSConstants.OfferLetterNotInPendingStateMessage);
					}
				} else {
					throw new HRMSException(IHRMSConstants.InvalidActionCode, IHRMSConstants.InvalidActionMessage);
				}

				letter.setUpdatedDate(new Date());
				letter.setActionTakenOn(new Date());
				letter = candidateLetterDAO.save(letter);
				candidateActivityDAO.save(activity);
				candidateDao.save(candidate);
				VOCandidateLetter letterModel = HRMSEntityToModelMapper.convertToCandidateLetterModel(letter);
				String letterURL = baseURL + letter.getLetterUrl() + letter.getFileName();
				letterModel.setLetterUrl(letterURL);
				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> list = new ArrayList<Object>();
				list.add(letterModel);
				response.setListResponse(list);
				response.setResponseMessage(IHRMSConstants.successMessage);
				response.setResponseCode(IHRMSConstants.successCode);
				String mailContent = HRMSHelper.replaceString(templateCreationMap,
						IHRMSEmailTemplateConstants.TEMPLATE_LETTER_STATUS_EMAIL);

				String ccId = candidate.getEmailId() + ";"
						+ candidate.getCandidateProfessionalDetail().getMarkLetterTo();

				String subject = candidate.getFirstName() + "_" + candidate.getLastName() + "_"
						+ IHRMSConstants.LETTER_STATUS_SUBJECT + "_" + letter.getStatus();
				emailsender.toPersistEmail(conf.getHrEmailId(), ccId, mailContent, subject, divisionId, organizationId);
				// EmailSender.toSendEmail(conf.getHrEmailId(), ccId, mailContent, subject);

				// emailsender.toPersistEmail(candidate.getEmailId(),
				// candidate.getCandidateProfessionalDetail().getMarkLetterTo(), mailContent,
				// subject, divisionId, organizationId);
				// EmailSender.toSendEmail(candidate.getEmailId(),
				// candidate.getCandidateProfessionalDetail().getMarkLetterTo(), mailContent,
				// subject);

				return HRMSHelper.createJsonString(response);

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
		// return null
		return null;
	}

	@RequestMapping(value = "/{candidateId}/{activityId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String toGenerateAndSendLetter(@PathVariable("candidateId") long candidateId,
			@PathVariable("activityId") long activityId) throws Exception {

		try {
			Candidate candidate = candidateDao.findById(candidateId).get();

			if (candidate != null) {

				String letterContentPDF = "";
				String fileName = "";
				String mailContent = "";
				String emailStatus = IHRMSConstants.EmailSentSuccess;
				String ip = baseURL;
				/*
				 * String webdavPathToView = ip + "/webdav/" +
				 * candidate.getLoginEntity().getOrganization().getId() + "/" +
				 * candidate.getCandidateProfessionalDetail().getDivision().getId() + "/" +
				 * candidate.getCandidateProfessionalDetail().getBranch().getId() + "/" +
				 * candidate.getId() + "/letters";
				 */

				String letterURL = File.separator + "input" + File.separator
						+ candidate.getLoginEntity().getOrganization().getId() + File.separator
						+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
						+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
						+ candidate.getId() + File.separator + "letters" + File.separator;

				logger.info("letterURL : " + letterURL);
				/*
				 * Finding the templates for the provided activity
				 */
				MasterCandidateActivity masterActivity = masterActivityDAO.findById(activityId).get();
				// Setting activity status for letter sent
				String activityName = masterActivity.getName() + "Sent";
				HtmlTemplate emailTemplate = emailTemplateDAO.findByActivityIdAndType(activityId, "EMAIL");
				HtmlTemplate pdfTemplate = emailTemplateDAO.findByActivityIdAndType(activityId, "PDF");
				String randomPwdStr = HRMSHelper.randomString();
				String encryptPwdStr = HRMSHelper.encryptToSHA256(randomPwdStr);
				/*
				 * Setting up map for place holder to create mail/PDF content
				 */
				Map<String, String> templateCreationMap = new HashMap<String, String>();
				templateCreationMap.put("{candidateFirstName}", candidate.getFirstName());
				templateCreationMap.put("{candidateMiddleName}", candidate.getMiddleName());
				templateCreationMap.put("{candidateLastName}", candidate.getLastName());
				templateCreationMap.put("{candidateUserName}", candidate.getLoginEntity().getUsername());
				// templateCreationMap.put("{candidatePassword}",
				// candidate.getLoginEntity().getPassword());
				templateCreationMap.put("{candidatePassword}", randomPwdStr);
				templateCreationMap.put("{candidateDesignation}",
						candidate.getCandidateProfessionalDetail().getDesignation().getDesignationName());
				templateCreationMap.put("{city}",
						candidate.getCandidateProfessionalDetail().getBranch().getBranchName());
				templateCreationMap.put("{currentDate}",
						HRMSDateUtil.format(new Date(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				templateCreationMap.put("{dateOfJoin}",
						HRMSDateUtil.format(candidate.getCandidateProfessionalDetail().getDateOfJoining(),
								IHRMSConstants.FRONT_END_DATE_FORMAT));
				templateCreationMap.put("{websiteURL}", baseURL);
				templateCreationMap.put("{noticePeriod}", String.valueOf(candidate.getNoticePeriod()));

				String path = rootDirectory + candidate.getLoginEntity().getOrganization().getId() + File.separator
						+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
						+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
						+ candidate.getId() + File.separator + "letters";

				logger.info("path : " + path);

				CandidateLetter letter = new CandidateLetter();
				Set<CandidateLetter> candidateLetterSet = candidate.getLetters();
				CandidateActivity candidateActivity = new CandidateActivity();
				CandidateActivityLetterMapping letterToActivityMapping = new CandidateActivityLetterMapping();

				if (candidateLetterSet != null) {
					/*
					 * This block will check if the letter is already present or not,if yes then
					 * there wont be any new insert in the database
					 */
					for (CandidateLetter letterEntity : candidateLetterSet) {
						// CandidateLetter candidateLetterEntity =
						// candidateLetterDAO.findById(letterId);
						CandidateActivityLetterMapping letterMapping = activityLetterMappingDAO
								.findBycandidateLetter(letterEntity);
						CandidateActivity activity = candidateActivityDAO
								.findById(letterMapping.getCandidateActivity().getId()).get();
						if (activity.getMasterCandidateActivity().getId() == activityId) {
							letter = letterEntity;
							letterToActivityMapping = letterMapping;
							candidateActivity = activity;
							activityName = candidate.getCandidateActivityStatus();
							/*
							 * To Set updated date when updating
							 */
							candidateActivity.setUpdatedDate(new Date());
							letterToActivityMapping.setUpdatedDate(new Date());
							letter.setUpdatedDate(new Date());
							break;
						}
					}
				}

				if (pdfTemplate != null) {
					/*
					 * Creating PDF,have to check if the pdf template is available or not
					 */
					fileName = "CAND_" + candidate.getId() + "_" + pdfTemplate.getFileName();
					templateCreationMap.put("{logoPath}", pdfTemplate.getLogoPath());
					templateCreationMap.put("{signaturePath}", pdfTemplate.getSignaturePath());
					letterContentPDF = HRMSHelper.replaceString(templateCreationMap, pdfTemplate.getTemplate());
					boolean pdfResult = PDFUtility.createPdf(letterContentPDF, path, fileName);
					if (!pdfResult) {
						logger.info(" == >> PDF Creation Failed & EMail Not Sent  << == ");
						throw new HRMSException(IHRMSConstants.PDFGenerationAndEmailSendingFailedCode,
								IHRMSConstants.PDFGenerationAndEmailSendingFailedMessage);
					}

					candidate.getCandidateProfessionalDetail().setShortlistDate(new Date());
					candidate.setCandidateActivityStatus(activityName);
					candidate.getLoginEntity().setPassword(encryptPwdStr);
					candidate.getLoginEntity().setIsFirstLogin(IHRMSConstants.isActive);
					candidateDao.save(candidate);
					letter.setCandidate(candidate);
					letter.setStatus(IHRMSConstants.LetterStatus_PENDING);

					if (letter.getCreatedDate() == null) {
						/*
						 * if creating for the first time then only date will get set,other wise updated
						 * date in above line of code
						 */
						letter.setCreatedDate(new Date());
						letterToActivityMapping.setCreatedDate(new Date());
						candidateActivity.setCreatedDate(new Date());
					}

					letter.setSentOn(new Date());
					letter.setLetterType(masterActivity.getName());
					letter.setFileName(fileName + ".pdf");
					letter.setLetterUrl(letterURL);
					// letter.setServerIp(ip);
					letter = candidateLetterDAO.save(letter);

					letterToActivityMapping.setCandidateLetter(letter);
					letterToActivityMapping.setCandidateActivity(candidateActivity);
					activityLetterMappingDAO.save(letterToActivityMapping);

				}

				candidateActivity.setActivityTriggredDate(new Date());
				candidateActivity.setCandidate(candidate);
				candidateActivity.setEmailStatus(emailStatus);
				candidateActivity.setMasterCandidateActivity(masterCandidateActitvityDAO.getOne(activityId));

				/*
				 * Persist candidate letter details,and activities status
				 */
				candidateActivity = candidateActivityDAO.save(candidateActivity);

				/*
				 * Sending EMail
				 */
				if (emailTemplate != null) {
					mailContent = HRMSHelper.replaceString(templateCreationMap, emailTemplate.getTemplate());
					/*
					 * boolean emailResponse = EmailSender.toSendEmail(candidate.getEmailId(),
					 * candidate.getCandidateProfessionalDetail().getMarkLetterTo(), mailContent,
					 * emailTemplate.getEmailSubject());
					 */
					// EmailSender sender = new EmailSender();
					emailsender.toPersistEmail(candidate.getEmailId(),
							candidate.getCandidateProfessionalDetail().getMarkLetterTo(), mailContent,
							emailTemplate.getEmailSubject(),
							candidate.getCandidateProfessionalDetail().getDivision().getId(),
							candidate.getLoginEntity().getOrganization().getId());

					/*
					 * if (!emailResponse) {
					 * logger.info(" == >> PDF Created ,EMail not send succesfully << == ");
					 * emailStatus = IHRMSConstants.EmailSentFailed; throw new
					 * HRMSException(IHRMSConstants.EmailSendingFailedCode,
					 * IHRMSConstants.EmailSendingFailedButPDFCreatedMessage); }
					 */
				}
				return HRMSHelper.sendSuccessResponse(IHRMSConstants.successMessage, IHRMSConstants.successCode);
			} else {
				throw new HRMSException(IHRMSConstants.CandidateDoesnotExistCode,
						IHRMSConstants.CandidateDoesnotExistMessage);
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

	@RequestMapping(value = "/{candidateId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String toGetLetter(@PathVariable("candidateId") long candidateId) throws Exception {
		Candidate candidate = candidateDao.findById(candidateId).get();

		try {
			if (candidate != null) {
				logger.info("BASE URL FROM SERVICE" + baseURL);
				Set<CandidateLetter> candidateLetter = candidate.getLetters();
				List<Object> letterModelList = new ArrayList<Object>();
				for (CandidateLetter letter : candidateLetter) {
					String letterURL = baseURL + letter.getLetterUrl() + letter.getFileName();
					VOCandidateLetter letterModel = HRMSEntityToModelMapper.convertToCandidateLetterModel(letter);
					letterModel.setLetterUrl(letterURL);
					letterModelList.add(letterModel);
					logger.info("letterURL" + letterURL);
				}
				// next added by SSW on 29sept2018 for salary annexure
				MasterCandidateChecklist mstCandChecklistSalAnx = masterCandidateChecklistDAO
						.getMasterCandidateChecklistByOrgIdAndCode(candidate.getLoginEntity().getOrganization().getId(),
								IHRMSConstants.MASTER_CANDIDATE_CHECKLIST_SALARY_ANNEXURE_CODE, IHRMSConstants.isActive,
								candidate.getCandidateProfessionalDetail().getDivision().getId());

				CandidateChecklist cc = candidateChecklistDAO.getCandidateWithChecklistDetailsByItem(
						mstCandChecklistSalAnx.getChecklistItem(), candidate.getId());
				if (!HRMSHelper.isNullOrEmpty(cc)) {
					VOCandidateLetter letterModel = new VOCandidateLetter();
					letterModel.setLetterType(cc.getChecklistItem());
					String url = rootDirectory + candidate.getLoginEntity().getOrganization().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
							+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
							+ candidate.getId() + File.separator + cc.getAttachment();
					letterModel.setLetterUrl(url);
					letterModel.setFileName(cc.getAttachment());
					letterModelList.add(letterModel);
					logger.info("checklistUrl" + url);
				}
				// up to this by SSW on 29sept2018 for salary annexure
				HRMSListResponseObject response = new HRMSListResponseObject();
				response.setListResponse(letterModelList);
				response.setResponseMessage(IHRMSConstants.successMessage);
				response.setResponseCode(IHRMSConstants.successCode);
				return HRMSHelper.createJsonString(response);
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

	@RequestMapping(value = "/sendSeparationLetters", method = {
			RequestMethod.POST }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String sendSeparationLetters(@RequestBody VOSeparationLetterRequest voSeperationLetterRequest) {

		try {
			if (!HRMSHelper.isNullOrEmpty(voSeperationLetterRequest)
					&& !HRMSHelper.isNullOrEmpty(voSeperationLetterRequest.getEmployee())) {
				Employee employee = employeeDAO.findById(voSeperationLetterRequest.getEmployee().getId()).get();
				Candidate candidate = candidateDao.findById(employee.getCandidate().getId()).get();
				String letterContentPDF = "";
				EmployeeSeparationDetails employeeSeparationDetailsEntity;
				String fileName = "";
				String mailContent = "";
				String emailStatus = IHRMSConstants.EmailSentSuccess;
				String ip = baseURL;
				String letterURL = File.separator + "input" + File.separator
						+ candidate.getLoginEntity().getOrganization().getId() + File.separator
						+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
						+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
						+ candidate.getId() + File.separator + IHRMSConstants.SEPARATIONFOLDERNAME + File.separator;
				logger.info("seperationletterURL" + letterURL);
				StringBuilder attachments = new StringBuilder();

				employeeSeparationDetailsEntity = employeeSeparationDAO
						.findSeparationDetailsUsingEmpIdAndStatusNoReject(
								IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED, employee.getId(),
								IHRMSConstants.isActive);

				// Setting up map for place holder to create mail/PDF content
				Map<String, String> templateCreationMap = new HashMap<String, String>();
				templateCreationMap.put("{candidateFirstName}", candidate.getFirstName());
				templateCreationMap.put("{candidateMiddleName}",
						HRMSHelper.isNullOrEmpty(candidate.getMiddleName()) ? "" : candidate.getMiddleName());
				templateCreationMap.put("{candidateLastName}", candidate.getLastName());
				templateCreationMap.put("{candidateUserName}", candidate.getLoginEntity().getUsername());
				templateCreationMap.put("{candidatePassword}", candidate.getLoginEntity().getPassword());
				templateCreationMap.put("{candidateDesignation}",
						candidate.getCandidateProfessionalDetail().getDesignation().getDesignationName());
				templateCreationMap.put("{candidateDivision}",
						candidate.getCandidateProfessionalDetail().getDivision().getDivisionName());
				templateCreationMap.put("{candidateDepartment}",
						candidate.getCandidateProfessionalDetail().getDepartment().getDepartmentName());
				templateCreationMap.put("{city}",
						candidate.getCandidateProfessionalDetail().getBranch().getBranchName());
				templateCreationMap.put("{currentDate}",
						HRMSDateUtil.format(new Date(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				templateCreationMap.put("{actualRelievingDate}",
						HRMSDateUtil.format(employeeSeparationDetailsEntity.getActualRelievingDate(),
								IHRMSConstants.FRONT_END_DATE_FORMAT));
				templateCreationMap.put("{dateOfJoin}",
						HRMSDateUtil.format(candidate.getCandidateProfessionalDetail().getDateOfJoining(),
								IHRMSConstants.FRONT_END_DATE_FORMAT));
				templateCreationMap.put("{websiteURL}", baseURL);

				String path = rootDirectory + candidate.getLoginEntity().getOrganization().getId() + File.separator
						+ candidate.getCandidateProfessionalDetail().getDivision().getId() + File.separator
						+ candidate.getCandidateProfessionalDetail().getBranch().getId() + File.separator
						+ candidate.getId() + File.separator + IHRMSConstants.SEPARATIONFOLDERNAME;
				logger.info("separationLetterPath" + path);
				// String path =
				// HRMSFileuploadUtil.directoryCreationForSeparationusingEmployeeId(baseURL,
				// employee);

				// find template of seperation letter of selected
				SeparationLetterTemplate separationLetterTemplateForEmail = null;

				List<SeparationLetterTemplate> separationLetterTemplates = new ArrayList<SeparationLetterTemplate>();
				for (VOOrgDivSeparationLetter voOrgDivSeperationLetter : voSeperationLetterRequest
						.getVoOrgDivSeparationLetters()) {
					fileName = "";
					MapOrgDivSeparationLetter orgDivSeparationLetterEntity = orgDivSeparationLetterMappingDAO
							.findById(voOrgDivSeperationLetter.getId()).get();
					SeparationLetterTemplate separationLetterTemplate = separationLetterTemplateDAO
							.getSeperationLetterTemplateById(voOrgDivSeperationLetter.getId(), "PDF",
									IHRMSConstants.isActive);
					separationLetterTemplateForEmail = separationLetterTemplateDAO.getSeperationLetterTemplateById(
							voOrgDivSeperationLetter.getId(), "EMAIL", IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(separationLetterTemplate)) {
						separationLetterTemplates.add(separationLetterTemplate);
						// checking if letter already present
						CandidateLetter letter = new CandidateLetter();
						Set<CandidateLetter> candidateLetterSet = candidateLetterDAO
								.findLettersByCandidateId(candidate.getId());
						EmployeeSeparationLetterMapping separationLetterMapping = new EmployeeSeparationLetterMapping();
						if (!HRMSHelper.isNullOrEmpty(candidateLetterSet) && candidateLetterSet.size() > 0) {
							// This block will check if the letter is already present or not,if yes then
							// there wont be any new insert in the database
							for (CandidateLetter letterEntity : candidateLetterSet) {
								// CandidateLetter candidateLetterEntity =
								// candidateLetterDAO.findById(letterId);
								EmployeeSeparationLetterMapping separationLetterMappingLoop = separationLetterMappingDAO
										.getEmployeeSeparationLetterMappingByOrgDivSeparationId(
												orgDivSeparationLetterEntity.getId(), IHRMSConstants.isActive,
												letterEntity.getId());
								// separationLetterMappingLoop will be null if sending emp letter for the
								// first time
								if (!HRMSHelper.isNullOrEmpty(separationLetterMappingLoop)) {
									MapOrgDivSeparationLetter orgDivSeparationLetter = orgDivSeparationLetterMappingDAO
											.findById(separationLetterMappingLoop.getOrgDivSeperationLetter().getId())
											.get();
									if (orgDivSeparationLetter.getId() == voOrgDivSeperationLetter.getId()) {
										letter = letterEntity;
										letterEntity.setUpdatedDate(new Date());

										separationLetterMapping = separationLetterMappingLoop;
										separationLetterMapping.setUpdatedDate(new Date());
										break;
									}
								}
							}
						}
						// pdf creation
						fileName = "CAND_" + candidate.getId() + "_" + separationLetterTemplate.getFileName();
						templateCreationMap.put("{logoPath}", separationLetterTemplate.getLogoPath());
						templateCreationMap.put("{signaturePath}", separationLetterTemplate.getSignaturePath());
						letterContentPDF = HRMSHelper.replaceString(templateCreationMap,
								separationLetterTemplate.getTemplate());
						boolean pdfResult = PDFUtility.createPdf(letterContentPDF, path, fileName);
						if (!pdfResult) {
							logger.info(" PDF Generation Of Employee separation letter ");
							throw new HRMSException(IHRMSConstants.PDFGenerationAndEmailSendingFailedCode,
									IHRMSConstants.PDFGenerationAndEmailSendingFailedMessage);
						}
						letter.setCandidate(candidate);
						letter.setStatus(IHRMSConstants.LetterStatus_PENDING);
						if (letter.getCreatedDate() == null) {
							// if creating for the first time then only date will get set,other wise updated
							// date in above line of code
							letter.setCreatedDate(new Date());
							separationLetterMapping.setCreatedDate(new Date());
							separationLetterMapping.setIsActive(IHRMSConstants.isActive);
						}
						letter.setSentOn(new Date());
						letter.setLetterType(orgDivSeparationLetterEntity.getLetterName());
						letter.setFileName(fileName + ".pdf");
						letter.setLetterUrl(letterURL);
						// letter.setServerIp(ip);
						letter = candidateLetterDAO.save(letter);

						attachments.append(fileName + ".pdf" + ";");

						separationLetterMapping.setCandidateLetter(letter);
						separationLetterMapping.setOrgDivSeperationLetter(orgDivSeparationLetterEntity);
						separationLetterMappingDAO.save(separationLetterMapping);

					}
				}
				String hrEmailIds = " ";
				List<OrgDivWiseHRMapping> orgDivHRList = orgDivHRDAO.findOrgDivWiseHrMapping(
						employee.getCandidate().getLoginEntity().getOrganization().getId(),
						employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						IHRMSConstants.isActive);
				for (OrgDivWiseHRMapping orgDivHRMApEntity : orgDivHRList) {
					hrEmailIds = hrEmailIds + orgDivHRMApEntity.getEmployee().getOfficialEmailId() + ";";
				}
				logger.info("hrEmailIds " + hrEmailIds);
				// sending email
				if (separationLetterTemplateForEmail != null) {
					mailContent = HRMSHelper.replaceString(templateCreationMap,
							separationLetterTemplateForEmail.getTemplate());
					// EmailSender sender = new EmailSender();
					emailsender.toPersistEmail(candidate.getEmailId(), hrEmailIds, mailContent,
							separationLetterTemplateForEmail.getEmailSubject(),
							candidate.getCandidateProfessionalDetail().getDivision().getId(),
							candidate.getLoginEntity().getOrganization().getId(),
							IHRMSConstants.IS_MAIL_WITH_ATTACHMENT_Y, attachments.toString(), path);
					return HRMSHelper.sendSuccessResponse(IHRMSConstants.successMessage, IHRMSConstants.successCode);
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
		return null;
	}
}
