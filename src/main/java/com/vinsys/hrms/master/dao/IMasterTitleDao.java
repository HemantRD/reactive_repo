package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterTitle;

public interface IMasterTitleDao extends JpaRepository<MasterTitle, Long> {
	@Query(value = "select * from tbl_mst_title tmt where is_active ='Y' order by id asc", nativeQuery = true)
	public List<MasterTitle> findByIsActive(String isActive);

	@Query(value = "select count(*) from tbl_mst_title tmt where is_active ='Y'", nativeQuery = true)
	public long countOfRecord(String isActive);
	
	public MasterTitle findByTitleAndIsActive(String title,String isActive);

	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_title WHERE LOWER(title) = LOWER(:title)", nativeQuery = true)
	public boolean existsByTitle(String title);

	public MasterTitle findByIdAndIsActive(long id, String isactive);
	
	@Query(value = "SELECT * FROM tbl_mst_title "
			+ "WHERE org_id = ?1 AND (title ILIKE %?2% OR title_description ILIKE %?2%) "
			+ "ORDER BY is_active DESC", nativeQuery = true)
	List<MasterTitle> searchMasterTitleByOrgIdAndText(Long orgId, String keyword, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_mst_title "
			+ "WHERE org_id = ?1 AND (title ILIKE %?2% OR title_description ILIKE %?2%)", nativeQuery = true)
	long countMasterTitleByOrgIdAndText(Long orgId, String keyword);
}
