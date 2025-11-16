package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidateFamilyDetail;

public interface IHRMSCandidateAddFamilyDetailsDAO extends JpaRepository<CandidateFamilyDetail, Long> {

	@Query("select resp from CandidateFamilyDetail resp where resp.isActive=?1")
	public List<CandidateFamilyDetail> findallCandidateFamilyDetails(String isActive);

}
