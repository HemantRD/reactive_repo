package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.CurrencyMaster;

public interface ICurrencyMasterDAO extends JpaRepository<CurrencyMaster, Long> {

	@Query(value = "select tmc.* from tbl_mst_currency tmc where tmc.is_active ='Y' order by tmc.id asc", nativeQuery = true)
	List<CurrencyMaster> findByIsActive(String isActive);

	@Query(value = "select count(currency) from tbl_mst_currency currency  where currency.is_active ='Y'", nativeQuery = true)
	public long currencyCount();

	@Query(value = "select tmc.* from tbl_mst_currency tmc where tmc.is_active =?1 and tmc.id = ?2 ", nativeQuery = true)
	CurrencyMaster findByIsActiveAndId(String isActive, Long id);
}
