package com.vinsys.hrms.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mst_email_sender")
public class MasterEmailSender extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_mst_email_sender", sequenceName = "seq_mst_email_sender", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_email_sender")
	private long id;

	@Column(name = "from_email")
	private String fromEmail;
	@Column(name = "recipient")
	private String recipient;
	@Column(name = "cc")
	private String cc;
	@Column(name = "bcc")
	private String bcc;
	@Column(name = "subject")
	private String subject;
	@Column(name = "body")
	private String body;
	@Column(name = "sent_On")
	private Date sentOn;
	@Column(name = "is_Sent")
	private boolean isSent;
	@Column(name = "re_attempt_frequency")
	private long reAttemptFrequency;
	@Column(name = "error_message")
	private String errorMessage;
	@Column(name = "email_status")
	private String emailStatus;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_id",insertable = false,updatable = false)
	private Organization organization;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "division_id")
	private MasterDivision division;
	@Column(name = "is_email_with_attachment")
	private String isEmailWithAttachment;
	@Column(name = "attachments")
	private String attachments;
	@Column(name = "file_path")
	private String filePath;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getSentOn() {
		return sentOn;
	}

	public void setSentOn(Date sentOn) {
		this.sentOn = sentOn;
	}

	public boolean isSent() {
		return isSent;
	}

	public void setSent(boolean isSent) {
		this.isSent = isSent;
	}

	public long getReAttemptFrequency() {
		return reAttemptFrequency;
	}

	public void setReAttemptFrequency(long reAttemptFrequency) {
		this.reAttemptFrequency = reAttemptFrequency;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public MasterDivision getDivision() {
		return division;
	}

	public void setDivision(MasterDivision division) {
		this.division = division;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	public String getIsEmailWithAttachment() {
		return isEmailWithAttachment;
	}

	public void setIsEmailWithAttachment(String isEmailWithAttachment) {
		this.isEmailWithAttachment = isEmailWithAttachment;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
