package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterTripType;

public interface IMasterTripTypeDAO extends JpaRepository<MasterTripType, Long> {
	
	
	List<MasterTripType> findByIsActive(String isActive);
	
	@Query(value="select count(tmtt) from tbl_mst_trip_type tmtt",nativeQuery=true)
	public long countMasterTripType();

	@Query(value = "select * from tbl_mst_trip_type tmbt where  id=?1 and  is_active =?2 ", nativeQuery = true)
	public MasterTripType findByIdAndIsActive(Long id, String isActive);
}
