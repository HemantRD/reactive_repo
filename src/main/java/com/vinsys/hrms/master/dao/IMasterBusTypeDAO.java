package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterBusType;

public interface IMasterBusTypeDAO extends JpaRepository<MasterBusType, Long> {

	@Query(value = "select count(tmbt) from tbl_mst_bus_type tmbt", nativeQuery = true)
	public long countBusType();

	List<MasterBusType> findByIsActiveOrderById(String isActive);

	@Query(value = "select * from tbl_mst_bus_type tmbt where  id=?1 and  is_active =?2 ", nativeQuery = true)
	public MasterBusType findByIdAndIsActive(Long id, String isActive);
}
