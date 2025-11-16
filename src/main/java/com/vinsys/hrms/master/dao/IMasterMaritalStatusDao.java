package com.vinsys.hrms.master.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.employee.vo.EmployeeAddressVO;
import com.vinsys.hrms.master.entity.MasterMaritialStatus;

public interface IMasterMaritalStatusDao extends JpaRepository<MasterMaritialStatus, Long> {
	@Query(value = "select * from tbl_mst_marital_status tmms where is_active ='Y' order by id asc", nativeQuery = true)
	public List<MasterMaritialStatus> findByIsActive(String isActive);

	@Query(value = "select count(*) from tbl_mst_marital_status tmms where is_active ='Y'", nativeQuery = true)
	public long countOfRecord(String isActive);
	
	public MasterMaritialStatus findByMaritalStatusAndIsActive(String maritalStatus, String isActive);

	public Collection<MasterMaritialStatus> findAllByIsActive(String name);
}
