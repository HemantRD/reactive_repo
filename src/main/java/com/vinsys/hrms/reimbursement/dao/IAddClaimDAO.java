package com.vinsys.hrms.reimbursement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.reimbursement.entity.AddClaim;

public interface IAddClaimDAO extends JpaRepository<AddClaim, Long> {
	

	@Query(value = "SELECT * FROM tbl_trn_reim_claims where id=?1 and is_active=?2", nativeQuery = true)
	public AddClaim findByIdAndIsActive(Long id,String isActive);

}
