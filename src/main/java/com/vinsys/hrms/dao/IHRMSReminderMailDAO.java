package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MapReminderMailCCEmployee;

public interface IHRMSReminderMailDAO extends JpaRepository<MapReminderMailCCEmployee, Long> {

	@Query("SELECT remMail FROM MapReminderMailCCEmployee remMail WHERE remMail.reminderType = ?1 AND "
			+ " remMail.orgId = ?2 AND remMail.division.id = ?3 AND remMail.isActive = ?4 ")
	public List<MapReminderMailCCEmployee> findReminderMailCCByOrgDiv(String reminderType, long orgId, long divId, String isActive);
}
