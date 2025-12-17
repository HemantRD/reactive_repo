package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.EmployeeSeparationLetterMapping;

public interface IHRMSEmployeeSeparationLetterMappingDAO extends JpaRepository<EmployeeSeparationLetterMapping, Long> {

	@Query(" SELECT empSeparationLetterMap FROM EmployeeSeparationLetterMapping empSeparationLetterMap "
			+ " JOIN empSeparationLetterMap.candidateLetter candLetter "
			+ " WHERE empSeparationLetterMap.orgDivSeperationLetter.id = ?1 AND empSeparationLetterMap.isActive = ?2 AND candLetter.id = ?3 ")
	public EmployeeSeparationLetterMapping getEmployeeSeparationLetterMappingByOrgDivSeparationId(
			long orgDivSepLetterId, String isActive, long candLetterId);
}
