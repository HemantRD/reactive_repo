package com.vinsys.hrms.master.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterTravelApproverSlab;

public interface IMasterTravelApproverSlabDAO extends JpaRepository<MasterTravelApproverSlab, Long> {

	public MasterTravelApproverSlab findByIsActive(String isActive);

//	@Query(value="select * from tbl_mst_travel_approver_slab slab where ?1 between min_amount and max_amount",nativeQuery = true)
//	public MasterTravelApproverSlab findByAmount(float amount);

	@Query(value = "select * from tbl_mst_travel_approver_slab slab where ?1 between min_amount and max_amount and currency = ?2 and is_active = ?3 ", nativeQuery = true)
	public MasterTravelApproverSlab findByAmountAndCurrencyAndIsActive(float amount, Long currencyId, String isActive);

}
