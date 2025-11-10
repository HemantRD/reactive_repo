package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.KraYear;

public interface IKraYearDao extends JpaRepository<KraYear, Long> {

	@Query(value = "select * from tbl_mst_kra_year where is_active=?1", nativeQuery = true)
	public KraYear findByIsActive(String isActive);

	public List<KraYear> getByIsActive(String name);
	
	@Query(value = "select * from tbl_mst_kra_year order by year ASC", nativeQuery = true)
	public List<KraYear> findAllYearsByAsc();
	
	@Query(value = "select * from tbl_mst_kra_year where year=?1", nativeQuery = true)
	public KraYear findByYear(String year);
	
	@Query(value = "select * from tbl_mst_kra_year where year!=?1", nativeQuery = true)
	public List<KraYear> findYearList(String year);

	public KraYear findByIdAndIsActive(Long id, String name);

	public KraYear findByYearAndIsActive(String yearname, String name);
	
	public KraYear findByLabel(String label);

	
}
