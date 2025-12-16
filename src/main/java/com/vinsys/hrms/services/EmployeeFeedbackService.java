package com.vinsys.hrms.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeFeedbackDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationDAO;
import com.vinsys.hrms.dao.IHRMSFeedbackOptionDAO;
import com.vinsys.hrms.dao.IHRMSFeedbackQuestionDAO;
import com.vinsys.hrms.dao.IHRMSMapOrgDivHrDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOEmployeeFeedback;
import com.vinsys.hrms.datamodel.VOFeedbackOption;
import com.vinsys.hrms.datamodel.VOFeedbackQuestion;
import com.vinsys.hrms.datamodel.VOSubmittedEmployeeFeedback;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeFeedback;
import com.vinsys.hrms.entity.EmployeeSeparationDetails;
import com.vinsys.hrms.entity.FeedbackOption;
import com.vinsys.hrms.entity.FeedbackQuestion;
import com.vinsys.hrms.entity.OrgDivWiseHRMapping;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;
import com.vinsys.hrms.util.PDFUtility;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/employeeFeedback")

public class EmployeeFeedbackService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeFeedback.class);
	@Value("${base.url}")
	private String baseURL;
	@Autowired
	IHRMSEmployeeFeedbackDAO employeeFeedbackDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSFeedbackQuestionDAO questionDAO;
	@Autowired
	IHRMSMapOrgDivHrDAO orgDivHRDAO;
	@Autowired
	IHRMSFeedbackOptionDAO optionDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSEmployeeSeparationDAO employeeSeparationDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createEmployeeFeedback(@RequestBody VOSubmittedEmployeeFeedback empFb) {

		String resultMesage = "";
		List<VOEmployeeFeedback> employeeFeedbackVoList = new ArrayList<VOEmployeeFeedback>();
		try {
			if (!HRMSHelper.isNullOrEmpty(empFb) && !HRMSHelper.isNullOrEmpty(empFb.getEmpFeedbackList())
					&& !HRMSHelper.isNullOrEmpty(empFb.getEmployee())
					&& !HRMSHelper.isNullOrEmpty(empFb.getEmployee().getId())) {

				List<EmployeeFeedback> employeeFeedbackEntityList = employeeFeedbackDAO
						.findByEmployeeCustomQuery(empFb.getEmployee().getId());
				if (employeeFeedbackEntityList.size() > 0) {
					return HRMSHelper.sendSuccessResponse(IHRMSConstants.EXIT_FEEDBACK_ALREADY_SUBMITTED_MESSAGE,
							IHRMSConstants.EXIT_FEEDBACK_ALREADY_SUBMITTED_CODE);
				}

				employeeFeedbackVoList = HRMSRequestTranslator.translateToEmployeeFeedbackVoList(empFb);
				Employee employeeEntity = employeeDAO.findById(empFb.getEmployee().getId()).get();
				for (VOEmployeeFeedback voEmployeeFeedback : employeeFeedbackVoList) {
					EmployeeFeedback employeeFeedbackEntity;
					FeedbackQuestion feedbackQuestionEntity;
					FeedbackOption feedbackOptionEntity;
					// VOEmployeeFeedback voEmployeeFeedback = new VOEmployeeFeedback();

					employeeFeedbackEntity = employeeFeedbackDAO.findById(voEmployeeFeedback.getId()).get();;
					feedbackQuestionEntity = questionDAO.findById(voEmployeeFeedback.getFeedbackQuestion().getId()).get();;
					feedbackOptionEntity = optionDAO.findById(voEmployeeFeedback.getFeedbackOption().getId()).get();;

					if (!HRMSHelper.isNullOrEmpty(employeeFeedbackEntity)) {
						/* update */
						employeeFeedbackEntity.setFeedbackQuestion(feedbackQuestionEntity);
						employeeFeedbackEntity.setFeedbackOption(feedbackOptionEntity);
						employeeFeedbackEntity.setEmployee(employeeEntity);
						employeeFeedbackEntity = HRMSRequestTranslator
								.translateToEmployeeFeedbackEntity(employeeFeedbackEntity, voEmployeeFeedback);
						// resultMesage = IHRMSConstants.updatedsuccessMessage;
						resultMesage = IHRMSConstants.successMessage;
					} else {
						/* insert */
						employeeFeedbackEntity = new EmployeeFeedback();
						employeeFeedbackEntity.setFeedbackQuestion(feedbackQuestionEntity);
						employeeFeedbackEntity.setFeedbackOption(feedbackOptionEntity);
						employeeFeedbackEntity.setEmployee(employeeEntity);
						employeeFeedbackEntity = HRMSRequestTranslator
								.translateToEmployeeFeedbackEntity(employeeFeedbackEntity, voEmployeeFeedback);
						// resultMesage = IHRMSConstants.addedsuccessMessage;
						resultMesage = IHRMSConstants.successMessage;
					}

					employeeFeedbackDAO.save(employeeFeedbackEntity);

				}
				SendEmailofFeedbackTOHRAndRO(employeeEntity);
				return HRMSHelper.sendSuccessResponse(resultMesage, IHRMSConstants.successCode);
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

	/**
	 * This Method is for when employee Submit Exit feedback form,Then mail goes to
	 * HR and in CC is RO
	 * 
	 * @param employee
	 */

	private void SendEmailofFeedbackTOHRAndRO(Employee employeeEntity) {

		String hrEmailIds = "";
		List<OrgDivWiseHRMapping> orgDivHRList = orgDivHRDAO.findOrgDivWiseHrMapping(
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				IHRMSConstants.isActive);
		for (OrgDivWiseHRMapping orgDivHRMApEntity : orgDivHRList) {
			hrEmailIds = hrEmailIds + orgDivHRMApEntity.getEmployee().getOfficialEmailId() + ";";
		}
		String roEmailId = employeeEntity.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapAfterExitFeedbackFormSubmitted(employeeEntity);
		placeHolderMapping.put("{websiteURL}", baseURL);

		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_Empployee_SUBMIT_EXIT_FEEDBACK_FORM);

		String mailSubject = IHRMSConstants.MailSubject_Exit_Feedback_Form_Submit;

		emailsender.toPersistEmail(hrEmailIds, roEmailId, mailContent, mailSubject,
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getEmployeeFeedBack(@PathVariable("id") long empId) {

		List<EmployeeFeedback> employeeFeedbackEntityList = new ArrayList<>();
		List<Object> voEmployeeFeedbackList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			if (!HRMSHelper.isLongZero(empId)) {

				// masterBranchEntityList = masterBranchDAO.findAll();
				Employee employeeEntity = employeeDAO.findById(empId).get();
				if (!HRMSHelper.isNullOrEmpty(employeeEntity)) {

					// employeeFeedbackEntityList =
					// employeeFeedbackDAO.findByEmployee(employeeEntity);
					employeeFeedbackEntityList = employeeFeedbackDAO.findByEmployeeCustomQuery(employeeEntity.getId());
					if (!HRMSHelper.isNullOrEmpty(employeeFeedbackEntityList)) {
						voEmployeeFeedbackList = HRMSResponseTranslator
								.transalteToEmployeeFeedbackVOList(employeeFeedbackEntityList, voEmployeeFeedbackList);
						hrmsListResponseObject.setListResponse((List<Object>) voEmployeeFeedbackList);
						hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
						hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
						// return HRMSHelper.createJsonString(voMasterBranchList);
						return HRMSHelper.createJsonString(hrmsListResponseObject);
					} else {
						return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
								IHRMSConstants.DataNotFoundCode);
					}

				} else {
					return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
							IHRMSConstants.DataNotFoundCode);
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

	@RequestMapping(method = RequestMethod.GET, value = "pdf/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void createEmployeeFeedBackPdf(@PathVariable("id") long empId, HttpServletResponse response) {

		List<EmployeeFeedback> employeeFeedbackEntityList = new ArrayList<>();
		List<VOSubmittedEmployeeFeedback> voEmployeeFeedbackList = new ArrayList<>();
		String relievingDate = "";
		// String dateOfJoining = "";
		// HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			if (!HRMSHelper.isLongZero(empId)) {
				String htmlCode = "";
				Employee employeeEntity = employeeDAO.findById(empId).get();

				if (!HRMSHelper.isNullOrEmpty(employeeEntity)) {

					// employeeFeedbackEntityList =
					// employeeFeedbackDAO.findByEmployee(employeeEntity);
					employeeFeedbackEntityList = employeeFeedbackDAO.findByEmployeeCustomQuery(employeeEntity.getId());
					List<EmployeeSeparationDetails> empSeplist = employeeSeparationDAO
							.findSeparationDetailsByEmpId(empId, IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(empSeplist) && !empSeplist.isEmpty()) {
						relievingDate = HRMSDateUtil.format(empSeplist.get(0).getActualRelievingDate(),
								IHRMSConstants.FRONT_END_DATE_FORMAT);
					}

					if (!HRMSHelper.isNullOrEmpty(employeeFeedbackEntityList)) {
						voEmployeeFeedbackList = HRMSResponseTranslator.transalteToEmployeeFeedbackVOListForPdf(
								employeeFeedbackEntityList, voEmployeeFeedbackList);

						for (VOSubmittedEmployeeFeedback voSubmittedEmployeeFeedback : voEmployeeFeedbackList) {
							List<VOFeedbackQuestion> feedbackList = voSubmittedEmployeeFeedback.getEmpFeedbackList();

							htmlCode += "<html><table style=\"height: 66px;font-size:14px font-family:calibri,verdana,geneva,sans-serif\" border=\"black\" width=\"100%;\">"
									+ "<tbody>";
							htmlCode += "<tr>";

							/*********** START OF UPPER TABLE ************/
							htmlCode += "<td>" + "<table>"

									+ "<tr>" + "<td><h3> Exit Feedback - "
									+ employeeEntity.getCandidate().getFirstName() + " "
									+ employeeEntity.getCandidate().getLastName() + " (" + employeeEntity.getId() + ")"
									+ "</h3>" + "</td>" + "<td>" + "<h4> Location : "
									+ employeeEntity.getCandidate().getCandidateProfessionalDetail().getBranch()
											.getBranchName()
									+ "</h4>" + "</td>" + "</tr>"

									+ "<tr>" + "<td><h4> Department : "
									+ employeeEntity.getCandidate().getCandidateProfessionalDetail().getDepartment()
											.getDepartmentName()
									+ "</h4></td>" + "<td><h4> Designation : "
									+ employeeEntity.getCandidate().getCandidateProfessionalDetail().getDesignation()
											.getDesignationName()
									+ "</h4></td>" + "</tr>"

									+ "<tr>" + "<td><h4> Date Of Joining : "
									+ HRMSDateUtil.format(employeeEntity.getCandidate().getCandidateProfessionalDetail()
											.getDateOfJoining(), IHRMSConstants.FRONT_END_DATE_FORMAT)
									+ "</h4></td>" + "<td><h4> Last Working Date : " + relievingDate + "</h4></td>"
									+ "</tr>"

									+ "</table>" + "</td>";

							/*********** END OF UPPER TABLE ************/
							htmlCode += "</tr>" + "<tr><td>&nbsp;</td></tr>";
							int questionNumber = 0;
							for (VOFeedbackQuestion voFeedbackQuestion : feedbackList) {

								String cbOrRadioChecked = "";
								String cbOrRadioUnChecked = "";
								String cbOrRadioUnCheckedClosed = "</img>";
								if (voFeedbackQuestion.getChoice().trim()
										.equalsIgnoreCase(IHRMSConstants.Question_type_MULTIPLE)) {
									cbOrRadioChecked = "<img src=\"" + IHRMSConstants.CHECKBOX_CHECKED_IMG_PATH
											+ "\"  height=\"15\" width=\"15\">";
									cbOrRadioUnChecked = "<img src=\"" + IHRMSConstants.CHECKBOX_UNCHECKED_IMG_PATH
											+ "\"  height=\"15\" width=\"15\">";
								} else {
									cbOrRadioChecked = "<img src=\"" + IHRMSConstants.RADIO_CHECKED_IMG_PATH
											+ "\"  height=\"15\" width=\"15\">";
									cbOrRadioUnChecked = "<img src=\"" + IHRMSConstants.RADIO_UNCHECKED_IMG_PATH
											+ "\"  height=\"15\" width=\"15\">";
								}

								htmlCode += "<tr>";
								htmlCode += "<td style=\"width: 100%;\">";
								htmlCode += ++questionNumber + ")  " + voFeedbackQuestion.getQuestionName();
								htmlCode += "</td>" + "</tr>";
								// Set<VOFeedbackOption> optionList = voFeedbackQuestion.getFeedbackOptions();
								List<VOFeedbackOption> optionList = voFeedbackQuestion.getFeedbackOptions();
								if (!HRMSHelper.isNullOrEmpty(optionList) && !optionList.isEmpty()) {

									htmlCode += "<tr>";
									htmlCode += "<td style=\"width: 100%;\">";
									htmlCode += "<ol>";
									for (VOFeedbackOption option : optionList) {
										htmlCode += "<li>";

										if (voFeedbackQuestion.getChoice()
												.equalsIgnoreCase(IHRMSConstants.Question_type_MULTIPLE)) {
											if (option.getIsSelected().trim().equalsIgnoreCase("true")) {
												htmlCode += cbOrRadioChecked;
												htmlCode += cbOrRadioUnCheckedClosed;
											} else {
												htmlCode += cbOrRadioUnChecked;
												htmlCode += cbOrRadioUnCheckedClosed;
											}
										} else {
											if (!option.getIsSelected().trim().equalsIgnoreCase("NOT_SELECTED")) {
												htmlCode += cbOrRadioChecked;
												htmlCode += cbOrRadioUnCheckedClosed;
											} else {
												htmlCode += cbOrRadioUnChecked;
												htmlCode += cbOrRadioUnCheckedClosed;
											}
										}

										htmlCode += "&nbsp;&nbsp;" + option.getOptionName();
										htmlCode += "</li>";
									}
									htmlCode += "</ol>";
									htmlCode += "</td>";
									htmlCode += "</tr>";
								} else {
									htmlCode += "<tr> <td> &nbsp; </td> </tr>";
									htmlCode += "<tr>";
									htmlCode += "<td style=\"width: 100%;\">";
									// htmlCode +="<textarea rows=\"4\" cols=\"50\">";
									htmlCode += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ voFeedbackQuestion.getUserFeedback().getComment();
									// htmlCode +="</textarea>";
									htmlCode += "</td>";
									htmlCode += "</tr>";
								}

								htmlCode += "<tr> <td> &nbsp; </td> </tr>";
								htmlCode += "<tr> <td> &nbsp; </td> </tr>";
							}
							htmlCode += "</tbody>" + "</table> </html>";
						}
						logger.info(htmlCode);
						// String path = "C:\\Users\\admin\\Desktop\\New_Folder";
						// String fileName = "Feedback_Report";
						// boolean pdfResult = PDFUtility.createPdf(htmlCode, path, fileName);

						String pdfFileName = "Feedback_Report_" + employeeEntity.getCandidate().getFirstName() + "_"
								+ employeeEntity.getCandidate().getLastName() + "_" + employeeEntity.getId() + ".pdf";
						response.setContentType("application/pdf");
						response.addHeader("Content-Disposition", "attachment; filename=" + pdfFileName);
						ByteArrayOutputStream baos = PDFUtility.createPdfWithoutSave(htmlCode);

						OutputStream os = response.getOutputStream();
						baos.writeTo(os);
						os.flush();
						os.close();

						// return HRMSHelper.sendSuccessResponse(IHRMSConstants.successMessage,
						// IHRMSConstants.successCode);

					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}

				} else {

					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				// return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage,
				// IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		// return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "demoPdf/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void dempPdf(@PathVariable("id") long empId, HttpServletResponse res) {

		try {
			String pdfFileName = "Feedback_Report.pdf";
			res.setContentType("application/pdf");
			res.addHeader("Content-Disposition", "attachment; filename=" + pdfFileName);
			String template = "<html><h1>Let's Rock....!</h1></html>";
			ByteArrayOutputStream baos = PDFUtility.createPdfWithoutSave(template);

			OutputStream os = res.getOutputStream();
			baos.writeTo(os);
			os.flush();
			os.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
