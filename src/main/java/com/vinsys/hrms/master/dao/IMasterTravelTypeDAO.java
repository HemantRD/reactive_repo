package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterTravelType;

public interface IMasterTravelTypeDAO extends JpaRepository<MasterTravelType, Long>{

	List<MasterTravelType> findByIsActive(String isActive);
	
	@Query(value="select count(tmtt) from tbl_mst_travel_type tmtt",nativeQuery=true)
	public long countMasterTravelType();
	
	
}
