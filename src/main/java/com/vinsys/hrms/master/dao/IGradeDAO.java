package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.master.entity.GradeMaster;

public interface IGradeDAO extends JpaRepository<GradeMaster, Long> {

	List<GradeMaster> findByIsActive(String isActive);

	List<GradeMaster> findByGradeDescription(String grade);
	
	@Query(value = "SELECT * FROM tbl_mst_grade WHERE grade_description = :description", nativeQuery = true)
	GradeMaster findByGradeName(@Param("description") String description);

	

	@Query(value = "SELECT *\r\n"
			+ "FROM tbl_mst_grade\r\n"
			+ "WHERE is_active = ?1\r\n"
			+ "ORDER BY\r\n"
			+ "    CAST(NULLIF(regexp_replace(grade_description, '[^0-9]', '', 'g'), '') AS INTEGER),\r\n"
			+ "    grade_description;", nativeQuery = true)
	public List<GradeMaster> findAllSortedGrades(String isActive);

	GradeMaster findByIdAndIsActive(Long id, String isactive);

	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_grade WHERE LOWER(grade_description) = LOWER(:gradeDescription)", nativeQuery = true)
	public boolean existsByGradeDescription(String gradeDescription);
	
//	@Query(value = "SELECT * FROM tbl_mst_grade " + "WHERE org_id = ?1 "
//			+ "AND (grade_description ILIKE %?2% OR career_level ILIKE %?2%)", nativeQuery = true)
	@Query(value = "SELECT * FROM tbl_mst_grade " + "WHERE org_id = ?1 "
			+ "AND (grade_description ILIKE %?2% OR career_level ILIKE %?2%) "
			+ "ORDER BY is_active DESC", nativeQuery = true)
	List<GradeMaster> searchGradeByOrgIdAndText(Long orgId, String searchText, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_mst_grade " + "WHERE org_id = ?1 "
			+ "AND (grade_description ILIKE %?2% OR career_level ILIKE %?2%)", nativeQuery = true)
	long countGradeByOrgIdAndText(Long orgId, String searchText);

}
