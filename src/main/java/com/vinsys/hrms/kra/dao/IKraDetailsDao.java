package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraDetails;

public interface IKraDetailsDao extends JpaRepository<KraDetails, Long> {

	public KraDetails findByIdAndIsActiveAndKra(Long id, String isActive, Kra kra);

	public List<KraDetails> findByKraAndIsActiveOrderByIdAsc(Kra kra, String isActive);

	public long countByKraAndIsActive(Kra kra, String isActive);

//	@Query(value="select * from tbl_trn_kra_details where is_active =?1 and kra_id =?2 and category_id =?3 and sub_category_id =?4 and id=?5",nativeQuery=true)
//	public EmployeeACN findByEmpIdAndIsActive(Long empId,String isActive);

	@Query(value = "select * from tbl_trn_kra_details where is_active =?1 and id=?2", nativeQuery = true)
	public KraDetails findByIdAndIsActive(String isActive, Long id);

	@Query(value = "SELECT \r\n" + "    ROUND(\r\n"
			+ "        CAST(SUM(tmkr.value * ttkd.objective_weightage) AS numeric) / \r\n"
			+ "        CAST(SUM(ttkd.objective_weightage) AS numeric),\r\n" + "        2\r\n" + "    ) AS avg\r\n"
			+ "FROM \r\n" + "    tbl_trn_kra_details ttkd\r\n" + "JOIN \r\n" + "    tbl_mst_kra_rating tmkr \r\n"
			+ "    ON tmkr.id = ttkd.manager_rating \r\n" + "WHERE \r\n" + "    ttkd.kra_id = ?1\r\n"
			+ "    AND ttkd.is_active = ?2\r\n" + "    AND ttkd.weightage != ?3", nativeQuery = true)
	Double calculateAverage(Long id, String status, Float value);

	@Query(value = "SELECT ROUND(\r\n"
			+ "  CAST((SUM(tmkr.value * ttkd.objective_weightage) / NULLIF(SUM(ttkd.objective_weightage), 0)) AS numeric),\r\n"
			+ "  2\r\n" + ") AS avg\r\n" + "FROM tbl_trn_kra_details ttkd\r\n"
			+ "JOIN tbl_mst_kra_rating tmkr ON tmkr.id = ttkd.self_rating\r\n" + "WHERE ttkd.kra_id = ?1\r\n"
			+ "  AND ttkd.is_active = ?2\r\n" + "  AND ttkd.weightage != ?3", nativeQuery = true)
	Double calculateSelfRatingAverage(Long kraId, String status, Float weightage);

	public List<KraDetails> findByIsActiveAndSubcategoryIdAndKraCycleAndKra(String name, Long subcategoryId,
			KraCycle cycle, Kra kra);

	public List<KraDetails> findByIsActiveAndKra(String name, Kra kra);

	public List<KraDetails> findByKraAndIsActiveAndIsColor(Kra kra, String isactive, String iscolor);

	public List<KraDetails> findByKraAndIsActiveAndIsColorOrderByIdAsc(Kra kra, String isactive, String iscolor);

	public KraDetails findByIdAndIsActive(Long id, String name);

	public boolean existsByKraAndKraCycleAndSubcategoryIdAndDescriptionAndIsActive(Kra kra, KraCycle cycle, long id,
			String name, String name2);

	public List<KraDetails> findByKraAndIsActiveAndCategoryIdOrderByIdAsc(Kra kra, String name, long id);

	public boolean existsByKraAndKraDetailsAndCategoryIdAndIsActive(Kra targetKra, String kraDetails, long l,
			String name);

	public boolean existsByMeasurementCriteriaAndIsActive(String metric, String isactive);

	@Query(value = "SELECT * FROM tbl_trn_kra_details WHERE mst_kra_cycle_id = :cycleId", nativeQuery = true)
	List<KraDetails> findByCycleId(@Param("cycleId") Long cycleId);

	@Modifying
	@Query(value="DELETE FROM tbl_trn_kra_details WHERE mst_kra_cycle_id = :cycleId", nativeQuery = true)
	void deleteByCycleId(@Param("cycleId") Long cycleId);

	@Query(value = "SELECT * FROM tbl_trn_kra_details WHERE kra_id = :kraId", nativeQuery = true)
	public List<KraDetails> findByKraId(Long kraId);


}
