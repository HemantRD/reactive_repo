package com.vinsys.hrms.dao.assets;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.assets.CompanyAsset;

public interface IHRMSCompanyAssetDAO extends JpaRepository<CompanyAsset, Long> {

	public List<CompanyAsset> findByemployee(Employee CompanyAsset);
	
	@Query(" SELECT ca FROM CompanyAsset ca WHERE ca.isActive = 'Y' and ca.employee.id= ?1")
	public List<CompanyAsset> findByEmployeeActive(long EmpId);
	
}
