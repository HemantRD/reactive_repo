package com.vinsys.hrms.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.entity.MapLoginEntityType;

public interface IHRMSMapLoginEntityTypeDAO extends JpaRepository<MapLoginEntityType, Long> {

	@Query(value = "SELECT login_entity_type_id FROM tbl_map_login_entity_type WHERE login_entity_id = ?1", nativeQuery = true)
	List<Long> findLoginEntityTypeIdsByLoginEntityId(Long loginEntityId);
	
	
	@Query(value = "SELECT login_entity_id FROM tbl_map_login_entity_type WHERE login_entity_type_id = ?1", nativeQuery = true)
	List<Long> findLoginEntityIdsByLoginEntityTypeId(Long loginEntityId);
	
	@Query(value = "SELECT * FROM tbl_map_login_entity_type WHERE login_entity_id = ?1 AND login_entity_type_id=?2 ", nativeQuery = true)
	List<Long> findByLoginEntityIdAndRole(Long loginEntityId, Long LoginEntityTypeId);
	
	@Query(value = "SELECT * FROM tbl_map_login_entity_type WHERE login_entity_id = ?1 AND login_entity_type_id=?2 ", nativeQuery = true)
	List<MapLoginEntityType> findByLoginEntityIdsAndRole(Long loginEntityId, Long LoginEntityTypeId);

	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO tbl_map_login_entity_type " + " (login_entity_id, login_entity_type_id, org_id) "
			+ " VALUES(:loginEntityId, :loginEntityTypeId, :orgId)", nativeQuery = true)
	int addRole(@Param("loginEntityId") Long loginEntityId, @Param("loginEntityTypeId") Long loginEntityTypeId,
			@Param("orgId") Long orgId);
	
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM tbl_map_login_entity_type " + "WHERE login_entity_id = :loginEntityId "
			+ "AND login_entity_type_id = :loginEntityTypeId " + "AND org_id = :orgId", nativeQuery = true)
	int deleteRole(@Param("loginEntityId") Long loginEntityId, @Param("loginEntityTypeId") Long loginEntityTypeId,
			@Param("orgId") Long orgId);

}
