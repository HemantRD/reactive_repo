package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterClaimCategory;


public interface IMasterClaimCategoryDAO extends JpaRepository<MasterClaimCategory, Long>{
	
	@Query(value="select count(category) from tbl_mst_reim_claim_category category where category.is_active ='Y'",nativeQuery = true)
	public long countCategory();
	
	List<MasterClaimCategory> findByIsActive(String isActive);
	
	@Query(value="select * from tbl_mst_reim_claim_category category where category.id=?1 and category.is_active ='Y'",nativeQuery = true)
	public MasterClaimCategory findByIdAndIsActive(Long id, String isActive);

}
