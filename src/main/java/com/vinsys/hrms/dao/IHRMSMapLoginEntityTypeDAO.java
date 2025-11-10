package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.entity.MapLoginEntityType;

public interface IHRMSMapLoginEntityTypeDAO extends JpaRepository<MapLoginEntityType, Long> {

	@Query(value = "SELECT login_entity_type_id FROM tbl_map_login_entity_type WHERE login_entity_id = ?1", nativeQuery = true)
	List<Long> findLoginEntityTypeIdsByLoginEntityId(Long loginEntityId);

}
