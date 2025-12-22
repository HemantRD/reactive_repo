package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.PrimaryRequester;

public interface IPrimaryRequesterTypeDAO extends JpaRepository<PrimaryRequester, Long>{

	@Query(value="select count(*) from tbl_mst_reim_requester_type  where is_active =?1",nativeQuery = true)
	public long countTravellerType(String isActive);
	
	List<PrimaryRequester> findByIsActive(String isActive);
}
