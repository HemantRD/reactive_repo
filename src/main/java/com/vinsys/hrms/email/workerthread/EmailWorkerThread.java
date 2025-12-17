package com.vinsys.hrms.email.workerthread;

import java.sql.Timestamp;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.email.dao.IEmailTransaction;
import com.vinsys.hrms.email.entity.EmailTransaction;
import com.vinsys.hrms.email.utils.EventsConstants;

@Component
@Scope("prototype")
public class EmailWorkerThread implements Runnable {

	private List<EmailTransaction> inputPayload;

	public List<EmailTransaction> getInputPayload() {
		return inputPayload;
	}

	public void setInputPayload(List<EmailTransaction> inputPayload) {
		this.inputPayload = inputPayload;
	}

	@Value("${spring.mail.username}")
	private String username;

	@Value("${email.aliasname}")
	private String aliasname;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private IEmailTransaction iEmailTransaction;
	
	@Value("${IS_TEST_MODE}")
	private String isTestMode;
	
	@Value("${TEST_MAIL_ID}")
	private String testEmailId; 

	@Override
	public void run() {
		for (EmailTransaction emailTransaction : inputPayload) {

			try {
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
				mimeMessageHelper.setFrom(username);
				
				if(isTestMode.equals(EventsConstants.IS_ACTIVE)) {
					mimeMessageHelper.setTo(testEmailId);
				}else {
					mimeMessageHelper.setTo(emailTransaction.getTargetEmailAddress());
				}
			
				mimeMessageHelper.setSubject(emailTransaction.getSubject());
				mimeMessageHelper.setText(emailTransaction.getMailBody(), true);
				mailSender.send(mimeMessage);
				emailTransaction.setStatus(EventsConstants.SUCCESS);
				emailTransaction.setSendDateTime(new Timestamp(System.currentTimeMillis()));
			} catch (Exception e) {
				e.printStackTrace();
				emailTransaction.setFailedReason(e.getLocalizedMessage());
				emailTransaction.setStatus(EventsConstants.FAILED);
			}
			iEmailTransaction.save(emailTransaction);

		}

	}

}
