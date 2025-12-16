package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.master.entity.MasterRelationship;

public interface IMasterRelationshipDAO extends JpaRepository<MasterRelationship, Long>{

	List<MasterRelationship> findByIsActive(String isActive);

	public boolean existsByRelation(String relation);
}
