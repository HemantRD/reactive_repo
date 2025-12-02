package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.KraDetailsLite;

public interface IKraDetailsLiteDao extends JpaRepository<KraDetailsLite, Long> {

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id "
			+ " from vw_kra_details where division_id =?1  ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDivision(Long divId, Pageable pageable);

	@Query(value = "select count(*)" + " from vw_kra_details where division_id =?1 ", nativeQuery = true)
	long getKraDetailsCountByDivision(Long divId);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id ,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDept(Long departmentId, Pageable pageable);
	
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id ,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and status =?2 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndStatus(Long departmentId,String status, Pageable pageable);

	@Query(value = "select count(*)" + " from vw_kra_details where department_id =?1 ", nativeQuery = true)
	long getKraDetailsCountByDept(Long departmentId);
	
	@Query(value = "select count(*)" + " from vw_kra_details where department_id =?1 and status= ?2 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndStatus(Long departmentId,String status);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id  "
			+ " from vw_kra_details where division_id =?1 and employee_id =?2 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDivisionAndEmp(Long divId, Long empId, Pageable pageable);

	@Query(value = "select count(*)"
			+ " from vw_kra_details where division_id =?1 and employee_id =?2 ", nativeQuery = true)
	long getKraDetailsCountByDivisionAndEmp(Long divId, Long empId);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndEmp(Long departmentId, Long empId, Pageable pageable);
	
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 and status=?3 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndEmpAndStatus(Long deptid, Long empId, String status, Pageable pageable);

	@Query(value = "select count(*)"
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 and status =?3 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndEmpAndStatus(Long deptid, Long empId, String status);

	@Query(value = "select count(*)"
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndEmp(Long departmentId, Long empId);

	@Query(value = "select employee_id ,first_name ,last_name ,status ,department_name ,division_id ,self_rating ,rm_rating ,hod_rating ,reporting_manager_id ,hr_rating,year,department_id,kra_id,pending_with,emp_mail_id,rm_first_name,rm_last_name "
			+ " from vw_kra_details where division_id =?1  ", nativeQuery = true)
	public List<KraDetailsLite> getKraByDivision(Long divId);

	// division filter removed
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade  "
			+ " from vw_kra_details where employee_id =?1 and status=?2 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByEmpAndStatus(Long empId,String status, Pageable pageable);
	
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade  "
			+ " from vw_kra_details where employee_id =?1 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByEmp(Long empId, Pageable pageable);

	@Query(value = "select count(*)" + " from vw_kra_details where employee_id =?1 ", nativeQuery = true)
	long getKraDetailsCountByEmp(Long empId);
	
	@Query(value = "select count(*)" + " from vw_kra_details where employee_id =?1 and status=?2 ", nativeQuery = true)
	long getKraDetailsCountByEmpAndStatus(Long empId,String status);
	
	

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade "
			+ " from vw_kra_details ", nativeQuery = true)
	List<KraDetailsLite> getKraDetails(Pageable pageable);

	@Query(value = "select count(*)" + " from vw_kra_details ", nativeQuery = true)
	long getKraDetailsCount();
	
	
	@Query(value = "select employee_id ,first_name ,last_name ,status ,department_name,department_id ,division_id ,self_rating ,rm_rating ,hod_rating ,reporting_manager_id ,hr_rating,year,department_id,kra_id,pending_with,emp_mail_id,rm_first_name,rm_last_name,country_id,country_name,cycle_id,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated "
			+ " from vw_kra_details  ", nativeQuery = true)
	public List<KraDetailsLite> getKraListForReport();
	
	
	
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 and country_id=?3 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndEmpAndCountryId(Long departmentId, Long empId,Long countryId, Pageable pageable);
	
	//with status for kralist
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade "
	+ " from vw_kra_details where department_id =?1 and employee_id =?2 and country_id=?3 and status=?4", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndEmpAndCountryId(Long deptid, Long empId, Long countryId, String status,
			Pageable pageable);
	
	@Query(value = "select count(*)"
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 and country_id=?3 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndEmpAndCountryId(Long departmentId, Long empId,Long countryId);
	
	//with status for kralist
	@Query(value = "select count(*)"
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 and country_id=?3 and status=?4 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndEmpAndCountryId(Long departmentId, Long empId,Long countryId,String status);
	
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and country_id=?2 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndCountryId(Long departmentId,Long countryId, Pageable pageable);
	
	//with status for kralist
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and country_id=?2 and status=?3 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndCountryId(Long departmentId,Long countryId,String status, Pageable pageable);
	

	@Query(value = "select count(*)"
			+ " from vw_kra_details where department_id =?1 and country_id=?2 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndCountryId(Long departmentId,Long countryId);
	
	@Query(value = "select count(*)"
			+ " from vw_kra_details where department_id =?1 and country_id=?2 and status=?3 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndCountryId(Long departmentId,Long countryId,String status);
	
	
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade  "
			+ " from vw_kra_details where country_id =?1 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByCountryId(Long countryId, Pageable pageable);
	

	@Query(value = "select count(*)" + " from vw_kra_details where country_id =?1 ", nativeQuery = true)
	long getKraDetailsCountByCountryId(Long countryId);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade  "
			+ " from vw_kra_details where country_id =?1 and status=?2", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByCountryIdAndStatus(Long countryId, String status, Pageable pageable);

	@Query(value = "select count(*)" + " from vw_kra_details where country_id =?1 and status=?2 ", nativeQuery = true)
	long getKraDetailsCountByCountryIdAndStatus(Long countryId, String status);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade  "
			+ " from vw_kra_details where status=?1", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByStatus(String status, Pageable pageable);

	@Query(value = "select count(*)" + " from vw_kra_details where status=?1 ", nativeQuery = true)
	long getKraDetailsCountByStatus(String status);


	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade  "
			+ " from vw_kra_details where year=?1", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByYear(String year, Pageable pageable);

	@Query(value = "select count(*)" + " from vw_kra_details where year=?1 ", nativeQuery = true)
	long getKraDetailsCountByYear(String kraYear);

	//with year
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 and country_id=?3 and status=?4 and year=?5", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndEmpAndCountryIdAndYear(Long deptid, Long empId, Long countryId,
			String status, String year, Pageable pageable);

	
	@Query(value = "select count(*)"
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 and country_id=?3 and status=?4 and year=?5 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndEmpAndCountryIdAndYear(Long deptid, Long empId, Long countryId,String status, String year);

	
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and country_id=?2 and status=?3 and year=?4 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndCountryIdAndYear(Long deptid, Long countryId, String status, String year,
			Pageable pageable);

	@Query(value = "select count(*)"
			+ " from vw_kra_details where department_id =?1 and country_id=?2 and status=?3 and year=?4 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndCountryIdAndYear(Long deptid, Long countryId, String status, String year);

	
	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 and status=?3 and year=?4 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndEmpAndStatusAndYear(Long deptid, Long empId, String status, String year,
			Pageable pageable);

	@Query(value = "select count(*)"
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 and status =?3 and year=?4 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndEmpAndStatusAndYear(Long deptid, Long empId, String status, String year);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id ,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and status =?2 and year=?3 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndStatusAndYear(Long deptid, String status, String year,
			Pageable pageable);

	@Query(value = "select count(*)" + " from vw_kra_details where department_id =?1 and status= ?2 and year=?3 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndStatusAndYear(Long deptid, String status, String trim);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade  "
			+ " from vw_kra_details where employee_id =?1 and status=?2 and year=?3 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByEmpAndStatusAndYear(Long empId, String status, String year, Pageable pageable);

	@Query(value = "select count(*)" + " from vw_kra_details where employee_id =?1 and status=?2 and year=?3", nativeQuery = true)
	long getKraDetailsCountByEmpAndStatusAndYear(Long empId, String status, String year);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade  "
			+ " from vw_kra_details where country_id =?1 and status=?2 and year=?3", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByCountryIdAndStatusAndYear(Long countryId, String status, String year,
			Pageable pageable);

	@Query(value = "select count(*)" + " from vw_kra_details where country_id =?1 and status=?2 and year=?3 ", nativeQuery = true)
	long getKraDetailsCountByCountryIdAndStatusAndYear(Long countryId, String status, String year);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,rm_final_rating,total_self_rating,hod_calibrated_rating,is_hr_calibrated,grade  "
			+ " from vw_kra_details where status=?1 and year=?2", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByStatusAndYear(String status, String year, Pageable pageable);

	@Query(value = "select count(*)" + " from vw_kra_details where status=?1 and year=?2", nativeQuery = true)
	long getKraDetailsCountByStatusAndYear(String status, String year);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 and country_id=?3 and year=?4 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndEmpAndCountryIdAndYear(Long deptid, Long empId, Long countryId,
			String year, Pageable pageable);

	@Query(value = "select count(*)"
			+ " from vw_kra_details where department_id =?1 and employee_id =?2 and country_id =?3 and year=?4 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndEmpAndCountryIdAndYear(Long deptid, Long empId, Long countryId, String trim);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,is_hr_calibrated,grade "
			+ " from vw_kra_details where department_id =?1 and country_id =?2 and year=?3 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByDeptAndCountryIdAndYear(Long deptid, Long countryId, String year,
			Pageable pageable);

	@Query(value = "select count(*)"
			+ " from vw_kra_details where department_id =?1 and country_id =?2 and year=?3 ", nativeQuery = true)
	long getKraDetailsCountByDeptAndCountryIdAndYear(Long deptid, Long countryId, String trim);

	@Query(value = "select employee_id ,first_name ,last_name ,kra_id ,status ,pending_with ,department_id,department_name,division_id,self_rating,	rm_rating,	hod_rating,	hr_rating,year ,emp_mail_id,rm_first_name,rm_last_name,reporting_manager_id,cycle_id,country_id,country_name,is_hr_calibrated,grade "
			+ " from vw_kra_details where country_id =?1 and year=?2 ", nativeQuery = true)
	List<KraDetailsLite> getKraDetailsByCountryIdAndYear(Long countryId, String year, Pageable pageable);

	@Query(value = "select count(*)"
			+ " from vw_kra_details where country_id =?1 and year=?2 ", nativeQuery = true)
	long getKraDetailsCountByCountryIdAndYear(Long countryId, String trim);

	
	


	

	

}
