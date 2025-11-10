package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterNotes;

public interface IMasterNotesDAO extends JpaRepository<MasterNotes, Long> {
	
	@Query(value = "select * from tbl_mst_notes where id = ?1 and is_active = ?2", nativeQuery = true)
	MasterNotes findByIdAndIsActive(Long id, String active);

	List<MasterNotes> findByIsActive(String name); 
	
	@Query(value = "select * from tbl_mst_notes where screen_id = ?1 and is_active =?2", nativeQuery = true)
	List<MasterNotes> findByScreenIdAndIsActive(Long screenId, String active);
	
	@Query(value = "select * from tbl_mst_notes where screen_id = ?1", nativeQuery = true)
	List<MasterNotes> findByScreenId(Long screenId);

}
