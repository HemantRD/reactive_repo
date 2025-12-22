package com.vinsys.hrms.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmailSenderDAO;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.entity.MasterOrganizationEmailConfig;
import com.vinsys.hrms.exception.HRMSException;

@Configuration
//@PropertySource(value="${HRMSCONFIG}")
public class EventsEmailSender {

	@Autowired
	IHRMSOrganizationDAO orgDAO;
	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;
	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO configDAO;
	@Autowired
	IHRMSMasterEmailSenderDAO emailSenderDAO;

	private static final Logger logger = LoggerFactory.getLogger(EventsEmailSender.class);

	public boolean toSendEmailScheduler(String recipientEmailId, String ccEmailId, String bccEmailId,
			String messageBody, String subject, long divisionId, long organizationId, Map<String, String> map)
			throws FileNotFoundException, IOException, HRMSException, MessagingException {

		logger.info(" ==== >> Getting EMail Configuration << ==== ");

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
		logger.info("Sending EMail To :: " + recipientEmailId);
		return sendEmail(session, recipientEmailId, fromEmail, ccEmailId, bccEmailId, subject, messageBody, emailTitle,
				map);

	}

	public static boolean sendEmail(Session session, String recipientEmailId, String fromEamil, String ccEmailId,
			String bccEmail, String subject, String body, String title, Map<String, String> imageMap)
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

			if (!HRMSHelper.isNullOrEmpty(bccEmail)) {
				logger.info(" == >> BCC Recipient << == " + bccEmail);
				String[] bccArray = bccEmail.split(";");
				for (String bcc : bccArray) {
					logger.info("checking cc recepient -- >> " + bcc);
					if (!HRMSHelper.isNullOrEmpty(bcc)) {
						msg.addRecipient(RecipientType.BCC, new InternetAddress(bcc));
					}
				}
			}

			// creates message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body, "text/html");

			// creates multi-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// adds inline image attachments
			if (imageMap != null && imageMap.size() > 0) {
				Set<String> setImageID = imageMap.keySet();

				for (String contentId : setImageID) {
					MimeBodyPart imagePart = new MimeBodyPart();
					imagePart.setHeader("Content-ID", "<" + contentId + ">");
					imagePart.setDisposition(MimeBodyPart.INLINE);

					String imageFilePath = imageMap.get(contentId);
					try {
						imagePart.attachFile(imageFilePath);
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					multipart.addBodyPart(imagePart);
				}
			}

			msg.setContent(multipart);

			Transport.send(msg);
			logger.info(" ==== >> EMail Sent Succesfully << ==== ");
			response = true;
		} catch (Exception ee) {
			logger.info(" ==== >> EMail Sending Failed << ==== ");
			logger.info(" Exception occurd on " + new Date());
			logger.info(" Exception::" + ee.getMessage());
			ee.printStackTrace();
		}
		return response;
	}
}
