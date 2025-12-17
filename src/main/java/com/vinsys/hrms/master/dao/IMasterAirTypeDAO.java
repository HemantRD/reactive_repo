package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterAirType;

public interface IMasterAirTypeDAO extends JpaRepository<MasterAirType,Long> {

	@Query(value="select count(tmat) from tbl_mst_air_type tmat",nativeQuery = true)
	public long countAirType();
	
	List<MasterAirType> findByIsActive(String isActive);
}
