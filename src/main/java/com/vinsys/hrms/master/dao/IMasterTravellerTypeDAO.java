package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterTravellerType;

public interface IMasterTravellerTypeDAO extends JpaRepository<MasterTravellerType, Long>{
	
	@Query(value="select count(tmtt) from tbl_mst_traveller_type tmtt",nativeQuery = true)
	public long countTravellerType();
	
	List<MasterTravellerType> findByIsActive(String isActive);
	
	MasterTravellerType findByIdAndIsActive(Long Id, String isActive);

}
