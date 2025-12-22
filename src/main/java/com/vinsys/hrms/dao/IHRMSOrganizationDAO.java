package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Organization;

public interface IHRMSOrganizationDAO extends JpaRepository<Organization, Long> {

	public Organization findByOrganizationName(String org);

	/*
	 * @Query(value =
	 * "select o from Organization o where o.subscription.subscriptionType =  ?1")
	 * public Organization findByCustomQuery(String stype);
	 */
	
	
	@Query(value=" SELECT * FROM tbl_organization  WHERE id = ?1 ",nativeQuery = true)
	public Organization findByOrgId(long orgId);

	public List<Organization> findByIsActive(String name);

	public Organization findByIsActiveAndId(String name, long orgId);
}
