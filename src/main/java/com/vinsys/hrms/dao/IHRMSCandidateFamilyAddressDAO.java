package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.CandidateFamilyAddress;
import com.vinsys.hrms.entity.CandidateFamilyDetail;

public interface IHRMSCandidateFamilyAddressDAO extends JpaRepository<CandidateFamilyAddress, Long> {

	public CandidateFamilyAddress findBycandidateFamilyDetail(CandidateFamilyDetail candidateFamilyDetail);
}
