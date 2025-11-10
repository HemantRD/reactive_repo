package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.kra.entity.HodCycleFinalRating;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraCycle;

public interface IHodCycleFinalRatingDao extends JpaRepository<HodCycleFinalRating,Long>{


	HodCycleFinalRating findByEmployeeAndMcMemberIdAndCycleIdAndIsActive(Employee employee, Employee loggedInEmployee,
			KraCycle cycle, String name);

	HodCycleFinalRating findByIsActiveAndKra(String name, Kra kra);
	
	HodCycleFinalRating findByEmployeeAndCycleId(Employee employee,
			KraCycle cycle);
	
	HodCycleFinalRating findByKra(Kra kra);
	
	@Query("SELECT h FROM HodCycleFinalRating h " + "WHERE h.employee.id = :employeeId "
			+ "AND h.cycleId.year.id = :year " + "AND h.cycleId.id = ("
			+ "SELECT MAX(c.id) FROM KraCycle c WHERE c.year.id = :year)")
	List<HodCycleFinalRating> findLastCycleRatingForYear(@Param("employeeId") Long employeeId,
			@Param("year") Long yearId);
	
	
	

}
