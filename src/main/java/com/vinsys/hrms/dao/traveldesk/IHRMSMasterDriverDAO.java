package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.MasterDriver;

public interface IHRMSMasterDriverDAO extends JpaRepository<MasterDriver, Long> {

	@Query("select driver from MasterDriver driver where driver.organization.id = ?1 AND driver.division.id = ?2 "
			+ " AND driver.isActive =?3 ")
	public List<MasterDriver> findAllDriverByOrgIdandDivId(long orgId, long divId, String isActive);

	@Query(" SELECT md FROM MasterDriver md WHERE md.id = ?1 AND md.isActive = ?2 ")
	public MasterDriver findDriverById(long driverId, String isActive);
	
	@Query(" SELECT md FROM MasterDriver md WHERE md.employee.id = ?1 AND md.isActive = ?2 ")
	public MasterDriver findDriverByEmployeeId(long employeeId, String isActive);
	
	@Query("select driver from MasterDriver driver where  driver.division.id = ?1 and  driver.isActive =?2 ")
	public List<MasterDriver> findByDivIdAndIsActive(long divId,String isActive);
	
	@Query(value="select count(tmd) from tbl_mst_driver tmd where tmd.division_id =?1 and tmd.is_active=?2",nativeQuery=true)
	public long countOfDriver(long divId,String isActive);
	
	@Query("select driver from MasterDriver driver where  driver.id = ?1 and  driver.isActive =?2 ")
	public MasterDriver findByIdAndIsActive(long id,String isActive);
	
}
