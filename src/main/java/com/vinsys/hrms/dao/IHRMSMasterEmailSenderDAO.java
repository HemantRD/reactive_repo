package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterEmailSender;

public interface IHRMSMasterEmailSenderDAO extends JpaRepository<MasterEmailSender, Long> {

	@Query("SELECT emailSender FROM MasterEmailSender emailSender  where emailSender.emailStatus = 'PENDING'")
	public List<MasterEmailSender> findNotSendEmailsDetails();
	
	@Query("SELECT emailSender FROM MasterEmailSender emailSender  where emailSender.emailStatus = 'PENDING' "
			+ " AND emailSender.isEmailWithAttachment = ?1 ")
	public List<MasterEmailSender> findNotSendEmailsDetailsWithAttachment(String isEmailWithAttachment);

	public List<MasterEmailSender> findByRecipientAndIsActive(String recipient , String isActive);
}
