package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterConfig;

public interface IMasterConfigDAO extends JpaRepository<MasterConfig, Long>{

	@Query(value = "select * from tbl_mst_master_config where key=?1 and category_id=?2 ", nativeQuery = true)
	public MasterConfig findByKeyAndCategoryId(String key,long catId);
		
}
