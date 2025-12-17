package com.vinsys.hrms.email.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author amey.gangakhedkar
 *
 */
@Entity
@Table(name = "tbl_txn_email")
public class EmailTransaction {

	
	@Id
	@SequenceGenerator(name = "seq_email_txn", sequenceName = "seq_email_txn", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_email_txn")
	@Column(name = "id")
	private Long id;

	@Column(name = "subject")
	private String subject;

	@Column(name = "mail_body")
	private String mailBody;

	@Column(name = "status")
	private String status;

	@Column(name = "emailid")
	private String targetEmailAddress;

	@Column(name = "category")
	private String category;

	

	@Column(name = "email_senddatetime")
	private Timestamp sendDateTime;

	@Column(name = "created_datetime")
	private Timestamp createdDateTime;

	@Column(name = "failed_reason")
	private String failedReason;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTargetEmailAddress() {
		return targetEmailAddress;
	}

	public void setTargetEmailAddress(String targetEmailAddress) {
		this.targetEmailAddress = targetEmailAddress;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	

	public Timestamp getSendDateTime() {
		return sendDateTime;
	}

	public void setSendDateTime(Timestamp sendDateTime) {
		this.sendDateTime = sendDateTime;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	@Override
	public String toString() {
		return "EmailTransaction [id=" + id + ", subject=" + subject + ", mailBody=" + mailBody + ", status=" + status
				+ ", targetEmailAddress=" + targetEmailAddress + ", category=" + category + ", sendDateTime="
				+ sendDateTime + ", createdDateTime=" + createdDateTime + ", failedReason=" + failedReason + "]";
	}
	
	

}
