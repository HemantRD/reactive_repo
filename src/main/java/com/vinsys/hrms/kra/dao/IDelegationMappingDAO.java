package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.kra.entity.DelegationMapping;

public interface IDelegationMappingDAO extends JpaRepository<DelegationMapping, Long> {

	List<DelegationMapping> findByDelegationToAndIsActive(Long loggedInEmpId, String name);

	public List<DelegationMapping> findByIsActive(String isActive, Pageable pageable);

	long countByIsActive(String isActive);

	public DelegationMapping findByDelegationForAndIsActive(Long empId, String name);

	DelegationMapping findByDelegationToAndDelegationForAndIsActive(Long loggedInEmpId, Long employeeId, String name);
	
	@Query("SELECT d FROM DelegationMapping d " +
		       "LEFT JOIN Employee e1 ON d.delegationFor = e1.id " +
		       "LEFT JOIN Employee e2 ON d.delegationTo = e2.id " +
		       "WHERE d.isActive = :isActive AND (" +
		       "LOWER(e1.candidate.firstName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
		       "LOWER(e1.candidate.lastName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
		       "LOWER(e2.candidate.firstName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
		       "LOWER(e2.candidate.lastName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
		       "CAST(d.delegationBy AS string) LIKE CONCAT('%', :searchText, '%')" +
		       ")")
	List<DelegationMapping> searchByIsActiveAndText(@Param("isActive") String isActive,
			@Param("searchText") String searchText, Pageable pageable);
	
	@Query("SELECT COUNT(d) FROM DelegationMapping d " +
		       "LEFT JOIN Employee e1 ON d.delegationFor = e1.id " +
		       "LEFT JOIN Employee e2 ON d.delegationTo = e2.id " +
		       "WHERE d.isActive = :isActive AND (" +
		       "LOWER(e1.candidate.firstName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
		       "LOWER(e1.candidate.lastName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
		       "LOWER(e2.candidate.firstName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
		       "LOWER(e2.candidate.lastName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
		       "CAST(d.delegationBy AS string) LIKE CONCAT('%', :searchText, '%')" +
		       ")")
	    long countSearchByIsActiveAndText(@Param("isActive") String isActive,
	                                       @Param("searchText") String searchText);
	
	public DelegationMapping findByIdAndIsActive(Long id, String isActive);

}
