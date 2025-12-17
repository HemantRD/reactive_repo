package com.vinsys.hrms.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.OrganizationHoliday;

public interface IHRMSOrganizationHolidayDAO extends JpaRepository<OrganizationHoliday, Long> {

	@Query("select holiday from OrganizationHoliday holiday "
			+ " where holiday.orgId = ?1 and holiday.division.id = ?2 and holiday.branch.id = ?3"
			+ " and (holiday.holidayDate between ?4 and ?5)")
	public List<OrganizationHoliday> getHolidayListByOrgBranchDivId(long orgId, long divId, long branchId, Date from,
			Date to);
	
	
	@Query("select holiday from OrganizationHoliday holiday "
			+ " where holiday.orgId = ?1 "
			+ " and holiday.division.id = ?2 "
			+ " and holiday.branch.id = ?3 "
			+ " and holiday.holidayYear = ?4 order by holiday.holidayDate")
	public List<OrganizationHoliday> getHolidayListByOrgBranchDivIdYear(long orgId, long divId, long branchId, long year);
	
	@Query("select holiday from OrganizationHoliday holiday "
			+ " where holiday.orgId = ?1 and holiday.division.id = ?2 and holiday.branch.id = ?3"
			+ " and holiday.holidayDate= ?4")
	public OrganizationHoliday getHoliday(long orgId, long divId, long branchId, Date date);
	
	@Query("SELECT COUNT(holiday) FROM OrganizationHoliday holiday "
	        + "WHERE holiday.orgId = ?1 "
	        + "AND holiday.division.id = ?2 "
	        + "AND holiday.branch.id = ?3 "
	        + "AND holiday.holidayYear = ?4")
	long countHolidayListByOrgBranchDivIdYear(long orgId, long divId, long branchId, long year);

}
