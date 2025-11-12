package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterReimbursementTravelType;

public interface IReimbursementTravelTypeDAO extends JpaRepository<MasterReimbursementTravelType, Long> {

	@Query(value = "select count(travel) from tbl_mst_reim_travel_type travel where travel.is_active ='Y'", nativeQuery = true)
	public long countTravelType();

	List<MasterReimbursementTravelType> findByIsActiveOrderByIdAsc(String isActive);
	
	@Query(value="select * from tbl_mst_travel_type tmtt where id=?1 and is_active=?2",nativeQuery=true)
	public MasterReimbursementTravelType findByIdAndIsActive(Long id,String isActive);

}
