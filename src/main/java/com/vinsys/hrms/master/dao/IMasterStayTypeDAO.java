package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterStayType;

public interface IMasterStayTypeDAO extends JpaRepository<MasterStayType, Long> {
	
	@Query(value="select count(stay) from tbl_mst_reim_stay_type stay where stay.is_active =?1",nativeQuery = true)
	public long countStayType(String isActive);
	
	List<MasterStayType> findByIsActive(String isActive);
	
	@Query(value="select * from tbl_mst_reim_stay_type stay where stay.id=?1 and stay.is_active =?2",nativeQuery = true)
	public MasterStayType findByIdAndIsActive(Long id, String IsActive);

}
