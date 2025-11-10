package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterTitle;

public interface IHRMSMasterTitleDAO extends JpaRepository<MasterTitle, Long> {

	public List<Object> findByisActive(String isActive);

	public MasterTitle findByTitle(String title);
	
	@Query(value = "SELECT * FROM tbl_mst_title "
			+ "WHERE org_id = ?1 AND (title ILIKE %?2% OR title_description ILIKE %?2%) "
			+ "ORDER BY is_active DESC", nativeQuery = true)
	List<MasterTitle> searchMasterTitleByOrgIdAndText(Long orgId, String keyword, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_mst_title "
			+ "WHERE org_id = ?1 AND (title ILIKE %?2% OR title_description ILIKE %?2%)", nativeQuery = true)
	long countMasterTitleByOrgIdAndText(Long orgId, String keyword);
	
}
