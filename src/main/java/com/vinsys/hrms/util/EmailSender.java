package com.vinsys.hrms.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmailSenderDAO;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterEmailSender;
import com.vinsys.hrms.entity.MasterOrganizationEmailConfig;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;

@Configuration
//@PropertySource(value="${HRMSCONFIG}")
public class EmailSender {

	@Value("${rootDirectory}")
	private String rootDirectory;
	@Autowired
	IHRMSOrganizationDAO orgDAO;
	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;
	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO configDAO;
	@Autowired
	IHRMSMasterEmailSenderDAO emailSenderDAO;
	@Autowired
	IHRMSCandidateDAO candidateDAO;

	private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

	/**
	 * To persist the email to be sent, scheduler will be configure to sent those
	 * email in bulk
	 */

	public void toPersistEmail(String recipientEmailId, String ccEmailId, String messageBody, String subject,
			long divisionId, long organizationId) {

		MasterDivision division = divisionDAO.findById(divisionId).get();
		Organization organization = orgDAO.findById(organizationId).get();
		MasterEmailSender emailSender = new MasterEmailSender();
		emailSender.setRecipient(recipientEmailId);
		emailSender.setBody(messageBody);
		emailSender.setCc(ccEmailId);
		emailSender.setDivision(division);
		emailSender.setOrgId(organization.getId());
		emailSender.setSubject(subject);
		emailSender.setCreatedDate(new Date());
		emailSender.setSent(false);
		emailSender.setIsActive(IHRMSConstants.isActive);
		emailSender.setReAttemptFrequency(IHRMSConstants.EMAIL_REATTEMPT);
		emailSender.setEmailStatus(IHRMSConstants.EMAIL_STATUS_PENDING);
		MasterOrganizationEmailConfig conf = configDAO.findByorganizationAnddivision(organization.getId(),
				division.getId());
		emailSender.setFromEmail(conf.getUsername());
		emailSenderDAO.save(emailSender);
	}

	/**
	 * This method will get the email details and its configuration and will send
	 * email.
	 * 
	 * @param
	 * @return boolean
	 * @author shome.nitin
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static boolean toSendEmail(String recipientEmailId, String ccEmailId, String messageBody, String subject)
			throws FileNotFoundException, IOException {

		/*
		 * Reading details from property file
		 */
		final String fromEmail = HRMSPropertyFileLoader.getPropertyFile(IHRMSConstants.Config_PropertyFile)
				.getProperty("From_email");
		final String password = HRMSPropertyFileLoader.getPropertyFile(IHRMSConstants.Config_PropertyFile)
				.getProperty("From_email_password");
		final String smtpHost = HRMSPropertyFileLoader.getPropertyFile(IHRMSConstants.Config_PropertyFile)
				.getProperty("Smtp_host");
		final String port = HRMSPropertyFileLoader.getPropertyFile(IHRMSConstants.Config_PropertyFile)
				.getProperty("Port");

		final String emailTitle = HRMSPropertyFileLoader.getPropertyFile(IHRMSConstants.Config_PropertyFile)
				.getProperty("Email_title");

		logger.info("Sending EMail To :: " + recipientEmailId);
		logger.info(" ==== >> Getting EMail Configuration << ==== ");

		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);

		logger.info(" ==== >> Sending EMail << ==== ");
		return sendEmail(session, recipientEmailId, fromEmail, ccEmailId, subject, messageBody, emailTitle);

	}

	/**
	 * To send email via configuration from dataBase Based on division id and
	 * organization id,TO be utilized while sending email via Scheduler
	 * 
	 * @throws MessagingException
	 */
	public boolean toSendEmailScheduler(String recipientEmailId, String ccEmailId, String bccEmailId,
			String messageBody, String subject, long divisionId, long organizationId)
			throws FileNotFoundException, IOException, HRMSException, MessagingException {

		logger.info(" ==== >> Getting EMail Configuration << ==== ");

		MasterOrganizationEmailConfig conf = configDAO.findByorganizationAnddivision(organizationId, divisionId);

		if (conf == null) {
			logger.info("EMail configuration not found");
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EMAIL_CONFIGURATION_NOT_FOUND);
		}
		logger.info("fromEmail" + conf.getUsername() + " " + "password :" + conf.getPassword() + " " + "smtpHost :"
				+ conf.getSmtpMailServer());
		logger.info("port :" + conf.getSmtpPort() + " " + "smtpAuth :" + conf.isSmtpAuth() + " " + "emailTitle"
				+ conf.getEmailTitle());
		final String fromEmail = conf.getUsername();
		final String password = conf.getPassword();
		final String smtpHost = conf.getSmtpMailServer();
		final String port = conf.getSmtpPort();
		final String smtpAuth = conf.isSmtpAuth();
		final String emailTitle = conf.getEmailTitle();

		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", smtpAuth);
		props.put("mail.smtp.starttls.enable", "true");

		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);

		logger.info(" ==== >> Sending EMail << ==== ");
		logger.info("Sending EMail To :: " + recipientEmailId + " " + ccEmailId);
		return sendEmail(session, recipientEmailId, fromEmail, ccEmailId, bccEmailId, subject, messageBody, emailTitle);

	}

	/**
	 * This method will send email based on the configuration provided It Dosen't
	 * include BCC
	 * 
	 * @param
	 * @author shome.nitin
	 */
	public static boolean sendEmail(Session session, String recipientEmailId, String fromEamil, String ccEmailId,
			String subject, String body, String title) {
		boolean response = false;
		try {

			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress(fromEamil, title));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "utf-8", "html");
			msg.setSentDate(new Date());

			String[] recipient = recipientEmailId.split(";");
			for (String to : recipient) {
				logger.info("checking to -- >> " + to);
				if (!HRMSHelper.isNullOrEmpty(to)) {
					// msg.setReplyTo(InternetAddress.parse(to, false));
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				}
			}

			if (!HRMSHelper.isNullOrEmpty(ccEmailId)) {
				logger.info(" == >> CC Recipient << == " + ccEmailId);
				String[] ccArray = ccEmailId.split(";");
				for (String cc : ccArray) {
					logger.info("checking cc recepient -- >> " + cc);
					if (!HRMSHelper.isNullOrEmpty(cc)) {
						msg.addRecipient(RecipientType.CC, new InternetAddress(cc));
					}
				}
			}
			Transport.send(msg);
			response = true;
			logger.info(" ==== >> EMail Sent Succesfully << ==== ");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * To Send Email Including BCC
	 * 
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean sendEmail(Session session, String recipientEmailId, String fromEamil, String ccEmailId,
			String bccEmail, String subject, String body, String title)
			throws MessagingException, UnsupportedEncodingException {
		boolean response = false;
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress(fromEamil, title));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "utf-8", "html");
			msg.setSentDate(new Date());

			String[] recipient = recipientEmailId.split(";");
			for (String to : recipient) {
				logger.info("checking to -- >> " + to);
				if (to != null) {
					// msg.setReplyTo(InternetAddress.parse(to, false));
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				}
			}

			if (!HRMSHelper.isNullOrEmpty(ccEmailId)) {
				logger.info(" == >> CC Recipient << == " + ccEmailId);
				String[] ccArray = ccEmailId.split(";");
				for (String cc : ccArray) {
					logger.info("checking cc recepient -- >> " + cc);
					// monika
					if (cc != null && !cc.isEmpty()) {
						msg.addRecipient(RecipientType.CC, new InternetAddress(cc));
					}

				}
			}

			if (!HRMSHelper.isNullOrEmpty(bccEmail)) {
				logger.info(" == >> BCC Recipient << == " + bccEmail);
				String[] bccArray = bccEmail.split(";");
				for (String bcc : bccArray) {
					logger.info("checking cc recepient -- >> " + bcc);
					if (bcc != null) {
						msg.addRecipient(RecipientType.BCC, new InternetAddress(bcc));
					}
				}
			}
			Transport.send(msg);
			response = true;
			logger.info(" ==== >> EMail Sent Succesfully << ==== ");
		} catch (Exception ee) {
			logger.info(" ==== >> EMail Sending Failed << ==== ");
			logger.info(" Exception occurd on " + new Date());
			ee.printStackTrace();
		}
		return response;
	}

	public void toSendBulkEmail() {

		List<MasterEmailSender> masterEmailsListWithAttachment = emailSenderDAO
				.findNotSendEmailsDetailsWithAttachment(IHRMSConstants.IS_MAIL_WITH_ATTACHMENT_Y);
		if (masterEmailsListWithAttachment != null && !masterEmailsListWithAttachment.isEmpty()) {
			for (MasterEmailSender emailSender : masterEmailsListWithAttachment) {
				try {
					sendEmailWithAttachment(emailSender.getRecipient(), emailSender.getCc(), emailSender.getBcc(),
							emailSender.getSubject(), emailSender.getBody(), emailSender.getDivision().getId(),
							emailSender.getOrganization().getId(), emailSender.getAttachments(),
							emailSender.getFilePath());
					emailSender.setSent(true);
					emailSender.setSentOn(new Date());
					emailSender.setEmailStatus(IHRMSConstants.EMAIL_STATUS_SENT);
					emailSenderDAO.save(emailSender);
				} catch (Exception ee) {
					emailSender.setErrorMessage(ee.getMessage());
					emailSender.setSent(false);
					emailSender.setSentOn(new Date());
					emailSender.setEmailStatus(IHRMSConstants.EMAIL_STATUS_ERROR);
					emailSenderDAO.save(emailSender);
				}
			}
		} else {
			logger.info("No Email To Sent");
		}

		List<MasterEmailSender> masterEmailsList = emailSenderDAO.findNotSendEmailsDetails();
		if (masterEmailsList != null && !masterEmailsList.isEmpty()) {
			for (MasterEmailSender emailSender : masterEmailsList) {
				try {
					toSendEmailScheduler(emailSender.getRecipient(), emailSender.getCc(), emailSender.getBcc(),
							emailSender.getBody(), emailSender.getSubject(), emailSender.getDivision().getId(),
							emailSender.getOrganization().getId());
					emailSender.setSent(true);
					emailSender.setSentOn(new Date());
					emailSender.setEmailStatus(IHRMSConstants.EMAIL_STATUS_SENT);
					emailSenderDAO.save(emailSender);
				} catch (Exception ee) {
					emailSender.setErrorMessage(ee.getMessage());
					emailSender.setSent(false);
					emailSender.setSentOn(new Date());
					emailSender.setEmailStatus(IHRMSConstants.EMAIL_STATUS_ERROR);
					emailSenderDAO.save(emailSender);
				}
			}
		} else {
			logger.info("No Email To Sent");
		}

	}

	/**
	 * @param recipientEmailId
	 * @param ccEmailId
	 * @param messageBody
	 * @param subject
	 * @param divisionId
	 * @param organizationId
	 * @param isMailWithAttachment
	 * @author ssw
	 * 
	 *         this method is overloaded with parameter isMailWithAttachment to
	 *         handle scenario of attachment in mail
	 */

	public void toPersistEmail(String recipientEmailId, String ccEmailId, String messageBody, String subject,
			long divisionId, long organizationId, String isMailWithAttachment, String attachments) {

		MasterDivision division = divisionDAO.findById(divisionId).get();
		Organization organization = orgDAO.findById(organizationId).get();
		MasterEmailSender emailSender = new MasterEmailSender();
		emailSender.setRecipient(recipientEmailId);
		emailSender.setBody(messageBody);
		emailSender.setCc(ccEmailId);
		emailSender.setDivision(division);
		emailSender.setOrgId(organization.getId());
		emailSender.setSubject(subject);
		emailSender.setCreatedDate(new Date());
		emailSender.setSent(false);
		emailSender.setIsActive(IHRMSConstants.isActive);
		emailSender.setReAttemptFrequency(IHRMSConstants.EMAIL_REATTEMPT);
		emailSender.setEmailStatus(IHRMSConstants.EMAIL_STATUS_PENDING);
		MasterOrganizationEmailConfig conf = configDAO.findByorganizationAnddivision(organization.getId(),
				division.getId());
		emailSender.setFromEmail(conf.getUsername());
		emailSender.setIsEmailWithAttachment(isMailWithAttachment);
		emailSender.setAttachments(attachments);
		emailSenderDAO.save(emailSender);
	}

	/**
	 * @param divisionId
	 * @param organizationId
	 * @return
	 * @throws HRMSException
	 * @author SSW
	 * 
	 *         this method is used to send email with attachment configuration
	 *         method
	 */
	public JavaMailSender getJavaMailSender(long organizationId, long divisionId) throws HRMSException {
		// get email configuration
		MasterOrganizationEmailConfig conf = configDAO.findByorganizationAnddivision(organizationId, divisionId);
		if (conf == null) {
			logger.info("EMail configuration not found");
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EMAIL_CONFIGURATION_NOT_FOUND);
		}

		final String fromEmail = conf.getUsername();
		final String password = conf.getPassword();
		final String smtpHost = conf.getSmtpMailServer();
		final String port = conf.getSmtpPort();
		final String smtpAuth = conf.isSmtpAuth();
		final String emailTitle = conf.getEmailTitle();

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(smtpHost);
		mailSender.setPort(Integer.parseInt(port));
		mailSender.setUsername(fromEmail);
		mailSender.setPassword(password);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", smtpAuth);
		props.put("mail.smtp.starttls.enable", "true");
		// props.put("mail.debug", "true");

		return mailSender;
	}

	/**
	 * @param session
	 * @param recipientEmailId
	 * @param fromEamil
	 * @param ccEmailId
	 * @param bccEmail
	 * @param subject
	 * @param body
	 * @param title
	 * @param organizationId
	 * @param divisionId
	 * @return
	 * @author SSW
	 * 
	 */
	public boolean sendEmailWithAttachment(String recipientEmailId, String ccEmailId, String bccEmail, String subject,
			String body, long divisionId, long organizationId, String attachments, String filePath) {
		boolean response = false;
		try {
			JavaMailSender emailSender = getJavaMailSender(organizationId, divisionId);
			MasterOrganizationEmailConfig conf = configDAO.findByorganizationAnddivision(organizationId, divisionId);
			/*
			 * Candidate candidate = candidateDAO.findCandidateByEmailId(recipientEmailId,
			 * IHRMSConstants.isActive);
			 * 
			 * String path = rootDirectory +
			 * candidate.getLoginEntity().getOrganization().getId() + "\\" +
			 * candidate.getCandidateProfessionalDetail().getDivision().getId() + "\\" +
			 * candidate.getCandidateProfessionalDetail().getBranch().getId() + "\\" +
			 * candidate.getId() + "\\"+IHRMSConstants.SEPARATIONFOLDERNAME;
			 */
			String path = "";
			if (!HRMSHelper.isNullOrEmpty(filePath)) {
				path = filePath;
			}
			logger.info(" Path of the file is  :: " + path);

			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(new InternetAddress(conf.getUsername(), conf.getEmailTitle()));
			String[] recipient = recipientEmailId.split(";");
			for (String to : recipient) {
				logger.info("Sending mail with attachment recipient : " + to);
				if (to != null) {
					helper.setTo(new InternetAddress(to));
				}
			}

			String[] ccmails = ccEmailId.split(";");
			InternetAddress[] arrayofCcMailIds = new InternetAddress[ccmails.length];
			int counter = 0;
			for (String cc : ccmails) {
				logger.info("Sending mail with attachment cc : " + cc);
				if (cc != null) {
					arrayofCcMailIds[counter] = new InternetAddress(cc);
					counter++;
					// helper.setCc(cc);
				}
			}
			helper.setCc(arrayofCcMailIds);

			helper.setSubject(subject);
			helper.setText(body, true);
			String[] attachmentArray = attachments.split(";");
			for (String attach : attachmentArray) {
				logger.info("attachments are " + attach);
				if (!HRMSHelper.isNullOrEmpty(attach)) {
					// FileSystemResource file = new FileSystemResource(path + "\\" +attach +
					// ".pdf");
					FileSystemResource file = new FileSystemResource(path + "\\" + attach);
					helper.addAttachment(file.getFilename(), file);
				}
			}
			emailSender.send(message);
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				// return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage,
				// IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * @param recipientEmailId
	 * @param ccEmailId
	 * @param messageBody
	 * @param subject
	 * @param divisionId
	 * @param organizationId
	 * @param isMailWithAttachment
	 * @param filePath
	 * @author ssw
	 * 
	 *         this method is overloaded with parameter isMailWithAttachment &&
	 *         filePath to handle scenario of attachment in mail
	 */

	public void toPersistEmail(String recipientEmailId, String ccEmailId, String messageBody, String subject,
			long divisionId, long organizationId, String isMailWithAttachment, String attachments, String filePath) {

		MasterDivision division = divisionDAO.findById(divisionId).get();
		Organization organization = orgDAO.findById(organizationId).get();
		MasterEmailSender emailSender = new MasterEmailSender();
		emailSender.setRecipient(recipientEmailId);
		emailSender.setBody(messageBody);
		emailSender.setCc(ccEmailId);
		emailSender.setDivision(division);
		emailSender.setOrgId(organization.getId());
		emailSender.setSubject(subject);
		emailSender.setCreatedDate(new Date());
		emailSender.setSent(false);
		emailSender.setIsActive(IHRMSConstants.isActive);
		emailSender.setReAttemptFrequency(IHRMSConstants.EMAIL_REATTEMPT);
		emailSender.setEmailStatus(IHRMSConstants.EMAIL_STATUS_PENDING);
		MasterOrganizationEmailConfig conf = configDAO.findByorganizationAnddivision(organization.getId(),
				division.getId());
		emailSender.setFromEmail(conf.getUsername());
		emailSender.setIsEmailWithAttachment(isMailWithAttachment);
		emailSender.setAttachments(attachments);
		emailSender.setFilePath(filePath);
		emailSenderDAO.save(emailSender);
	}
}
