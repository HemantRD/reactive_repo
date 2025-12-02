package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MapOrgDivSeparationLetter;

public interface IHRMSOrgDivSeparationLetterMappingDAO extends JpaRepository<MapOrgDivSeparationLetter, Long> {

	@Query("SELECT sepLetter from MapOrgDivSeparationLetter sepLetter WHERE sepLetter.organization.id = ?1 "
			+ " AND sepLetter.division.id = ?2 AND sepLetter.isActive = ?3 ")
	public List<MapOrgDivSeparationLetter> getSeparationLetterByOrgDiv(long orgId, long divId, String isActive);
}
