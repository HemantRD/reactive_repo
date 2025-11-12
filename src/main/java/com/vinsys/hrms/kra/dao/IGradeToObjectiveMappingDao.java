package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.kra.entity.GradeToObjectiveMapping;
import com.vinsys.hrms.kra.entity.KraYear;
import com.vinsys.hrms.master.entity.GradeMaster;
import com.vinsys.hrms.master.entity.KpiTypeMaster;

public interface IGradeToObjectiveMappingDao extends JpaRepository<GradeToObjectiveMapping, Long>{

	

	List<GradeToObjectiveMapping> findByDepartmentIdAndGrade(Long departmentId, String grade);

	//boolean existsByDepartmentIdAndGradeIdIn(Long departmentId, List<Long> gradeId);
	boolean existsByDepartmentIdAndGradeIdInAndIsActive(Long departmentId, List<Long> gradeId, String isActive);


	//List<GradeToObjectiveMapping> findByDepartmentIdAndGradeIdInAndKpiTyp(Long departmentId, List<Long> gradeIds,KpiTypeMaster orElseThrow);
	
	List<GradeToObjectiveMapping> findByDepartmentIdAndGradeIdInAndKpiTypeAndIsActive(
		    Long departmentId, 
		    List<Long> gradeIds, 
		    KpiTypeMaster kpiTyp, 
		    String isActive
		);

	
	List<GradeToObjectiveMapping> findByIsActiveOrderByIdDesc(String name, Pageable pageable);


	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where  \"year\"=?1 and department_id=?2 and grade_id=?3 and is_active=?4 order by id desc ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByYearAndMasterDepartmentAndGradeMasterAndIsActive(KraYear year,
			MasterDepartment department, GradeMaster grades, String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where \"year\"=?1 and department_id=?2 and grade_id=?3 and is_active=?4", nativeQuery = true)
	long countByYearAndMasterDepartmentAndGradeMasterAndIsActive(KraYear year, MasterDepartment department,
			GradeMaster grades, String name);

	long countByIsActive(String name);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where  \"year\"=?1 and department_id=?2 and is_active=?3 order by id desc ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByYearAndMasterDepartmentAndIsActive(KraYear year, MasterDepartment department,
			String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where \"year\"=?1 and department_id=?2 and is_active=?3", nativeQuery = true)
	long countByYearAndMasterDepartmentAndIsActive(KraYear year, MasterDepartment department, String name);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where  \"year\"=?1  and grade_id=?2 and is_active=?3 order by id desc ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByYearAndGradeMasterAndIsActive(KraYear year, GradeMaster grades, String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where \"year\"=?1 and grade_id=?2 and is_active=?3", nativeQuery = true)
	long countByYearAndGradeMasterAndIsActive(KraYear year, GradeMaster grades, String name);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where department_id=?1 and grade_id=?2 and is_active=?3 order by id desc ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByMasterDepartmentAndGradeMasterAndIsActive(MasterDepartment department,
			GradeMaster grades, String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where department_id=?1 and grade_id=?2 and is_active=?3", nativeQuery = true)
	long countByMasterDepartmentAndGradeMasterAndIsActive(MasterDepartment department, GradeMaster grades, String name);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where department_id=?1 and is_active=?2 order by id desc  ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByMasterDepartmentAndIsActive(MasterDepartment department, String name,
			Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where department_id=?1 and is_active=?2", nativeQuery = true)
	long countByMasterDepartmentAndIsActive(MasterDepartment department, String name);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where  year=?1 and is_active=?2 order by id desc ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByYearAndIsActive(KraYear year, String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where year=?1 and is_active=?2", nativeQuery = true)
	long countByYearAndIsActive(KraYear year, String name);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where grade_id=?1 and is_active=?2 order by id desc  ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByGradeMasterAndIsActive(GradeMaster grades, String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where grade_id=?1 and is_active=?2", nativeQuery = true)
	long countByGradeMasterAndIsActive(GradeMaster grades, String name);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where  \"year\"=?1 and department_id=?2 and grade_id=?3 and kpi_type=?4 and is_active=?5 order by id desc ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByYearAndMasterDepartmentAndGradeMasterAndKpiTypeMasterAndIsActive(KraYear year,
			MasterDepartment department, GradeMaster grades, KpiTypeMaster typemaster, String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where \"year\"=?1 and department_id=?2 and grade_id=?3 and kpi_type=?4 and is_active=?5", nativeQuery = true)
	long countByYearAndMasterDepartmentAndGradeMasterAndKpiTypeMasterAndIsActive(KraYear year,
			MasterDepartment department, GradeMaster grades, KpiTypeMaster typemaster, String name);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where  \"year\"=?1 and department_id=?2 and kpi_type=?3 and is_active=?4 order by id desc", nativeQuery = true)
	List<GradeToObjectiveMapping> findByYearAndMasterDepartmentAndKpiTypeMasterAndIsActive(KraYear year,
			MasterDepartment department, KpiTypeMaster typemaster, String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where \"year\"=?1 and department_id=?2 and kpi_type=?3 and is_active=?4", nativeQuery = true)
	long countByYearAndMasterDepartmentAndKpiTypeMasterAndIsActive(KraYear year, MasterDepartment department,
			KpiTypeMaster typemaster, String name);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where  \"year\"=?1  and grade_id=?2 and kpi_type=?3 and is_active=?4 order by id desc  ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByYearAndGradeMasterAndKpiTypeMasterAndIsActive(KraYear year, GradeMaster grades,
			Long kpiTypeId, String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where \"year\"=?1 and grade_id=?2 and kpi_type=?3 and is_active=?4", nativeQuery = true)
	long countByYearAndGradeMasterAndKpiTypeMasterAndIsActive(KraYear year, GradeMaster grades, Long kpiTypeId,
			String name);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where department_id=?1 and grade_id=?2 and kpi_type=?3 and is_active=?4 order by id desc  ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByMasterDepartmentAndGradeMasterAndKpiTypeMasterAndIsActive(
			MasterDepartment department, GradeMaster grades, KpiTypeMaster typemaster, String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where department_id=?1 and grade_id=?2 and kpi_type=?3 and is_active=?4", nativeQuery = true)
	long countByMasterDepartmentAndGradeMasterAndKpiTypeMasterAndIsActive(MasterDepartment department,
			GradeMaster grades, KpiTypeMaster typemaster, String name);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where kpi_type=?1 and is_active=?2 order by id desc", nativeQuery = true)
	List<GradeToObjectiveMapping> findByKpiTypeMasterAndIsActive(KpiTypeMaster typemaster, String name,
			Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where kpi_type=?1 and is_active=?2", nativeQuery = true)
	long countByKpiTypeMasterAndIsActive(KpiTypeMaster typemaster, String name);

	
	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where kpi_type=?1 and is_active=?2 and year=?3 ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByKpiTypeMasterAndIsActiveAndYear(long kpitype, String name,KraYear year);

	@Query(value = "select * from  tbl_map_grade_to_objective obj "
			+ "	where  \"year\"=?1  and kpi_type=?2 and is_active=?3 order by id desc  ", nativeQuery = true)
	List<GradeToObjectiveMapping> findByYearAndKpiTypeMasterAndIsActive(KraYear year, KpiTypeMaster typemaster,
			String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_map_grade_to_objective obj where \"year\"=?1 and kpi_type=?2 and is_active=?3", nativeQuery = true)
	long countByYearAndKpiTypeMasterAndIsActive(KraYear year, KpiTypeMaster typemaster, String name);

	List<GradeToObjectiveMapping> findByKpiTypeAndIsActive(KpiTypeMaster orElseThrow, String isActiString);

//	@Query(value = "SELECT tmd.department_name, a.kpi_objective, tmokt.kpi_type, tmmc.measurement_criteria " +
//            "FROM tbl_map_grade_to_objective a " +
//            "JOIN tbl_mst_department tmd ON tmd.id = a.department_id " +
//            "JOIN tbl_mst_org_kpi_type tmokt ON tmokt.id = a.kpi_type " +
//            "JOIN tbl_mst_measurement_criteria tmmc ON tmmc.id = a.metric_type " +
//            "WHERE (tmd.department_name ILIKE CONCAT('%', :keyword, '%') " +
//            "OR a.kpi_objective ILIKE CONCAT('%', :keyword, '%') " +
//            "OR tmokt.kpi_type ILIKE CONCAT('%', :keyword, '%') " +
//            "OR tmmc.measurement_criteria ILIKE CONCAT('%', :keyword, '%'))",
//    nativeQuery = true)
//	List<GradeToObjectiveMapping> findByOrgKpiInfoByKeyword(String keyword, String name, Pageable pageable);
//
//	long countByOrgKpiInfoByKeyword(String keyword, String name);
}
