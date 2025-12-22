package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.KraCountForHR;

public interface IKraCountForHRDAO extends JpaRepository<KraCountForHR, Long>{

	
	@Query(value = "select department_id , department_name , "
			+ "	COUNT(case when status = 'INCOMPLETE' then 1 end) as incomplete , "
			+ "	COUNT(case when status = 'NOTSUBMITTED' then 1 end) as notsubmitted , "
			+ "	COUNT(case when status = 'INPROCESS' then 1 end) as inprocess , "
			+ "	COUNT(case when status = 'APPROVED' then 1 end) as approved , "
			+ " COUNT(case when status = 'REJECTED' then 1 end) as rejected, sum(1) as total "
			+ " from vw_kra_details vkd group by "
			+ "	department_id,department_name  order by  department_id", nativeQuery = true)
	public List<KraCountForHR> getKRADetails();

}
