package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterScreenType;

public interface IMasterScreenTypeDAO extends JpaRepository<MasterScreenType, Long> {
	
	List<MasterScreenType> findByRoleAndIsActive(Long role ,String isActive);

	@Query(value ="select * from tbl_mst_screen_type where id =?1 and is_active =?2", nativeQuery = true)
	public MasterScreenType findByIdAndIsActive(Long screenId, String active);

}
