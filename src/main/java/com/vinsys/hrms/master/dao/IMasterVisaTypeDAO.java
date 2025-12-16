package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterVisaType;

public interface IMasterVisaTypeDAO extends JpaRepository<MasterVisaType, Long>{

	@Query(value="select count(tmvt) from tbl_mst_visa_type tmvt where tmvt.is_active ='Y'",nativeQuery = true)
	public long countVisaType();
	
	List<MasterVisaType> findByIsActiveOrderById(String isActive);
}
