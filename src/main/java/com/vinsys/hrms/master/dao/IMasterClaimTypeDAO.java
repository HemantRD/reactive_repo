package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterClaimType;

public interface IMasterClaimTypeDAO extends JpaRepository<MasterClaimType, Long> {

	@Query(value = "select count(typs) from tbl_mst_reim_claim_type typs where typs.is_active ='Y' and typs.claim_category =?1", nativeQuery = true)
	public long countClaimType(Long claimCategory);

	List<MasterClaimType> findByIsActiveOrderByIdAsc(String isActive);

	@Query(value = "select * from tbl_mst_reim_claim_type typs where claim_category =?1 and is_active =?2", nativeQuery = true)
	List<MasterClaimType> findByClaimCategoryAndIsActive(Long claimCategory, String isActive);

	@Query(value = "select * from tbl_mst_reim_claim_type typs where id =?1 and is_active =?2", nativeQuery = true)
	MasterClaimType findByIdAndIsActive(Long claimTypeId, String isActive);
	
	@Query(value="select count(typs) from tbl_mst_reim_claim_type typs where typs.is_active=?1",nativeQuery=true)
	public long countofClaimTypeByIsActive(String isActive);
	

}
