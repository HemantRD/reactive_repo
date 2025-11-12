package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.LoginEntity;

public interface IHRMSLoginDAO extends JpaRepository<LoginEntity, Long> {

	// @Query("select login from LoginEntity login join fetch login.organization
	// where login.username=?1 and login.password=?2")
	@Query("select login from LoginEntity login join  fetch login.organization where LOWER(login.username)=LOWER(?1) and login.password=?2")
	public LoginEntity findByCustomQuery(String username, String password);

	public LoginEntity findByUsername(String username);

//	@Query(value = "select tle.* from tbl_login_entity tle join tbl_candidate tc on tle.id = tc.login_entity_id\r\n"
//			+ "where tle.is_active =?1 and tc.is_active =?1 and LOWER(tle.username) =LOWER(?2) and tle.password =?3 ", nativeQuery = true)
//	public LoginEntity findByEmpCustomQuery(String isActive, String username, String password);

	@Query(value = "select tle.org_id from tbl_login_entity tle join tbl_candidate tc on tle.id = tc.login_entity_id "
			+ "where tle.is_active =?1 and tc.is_active =?1 and LOWER(tle.username) =LOWER(?2) ", nativeQuery = true)
	public Long getOrgIdByUsername(String isActive, String username);

	@Query(value = "select tle.* from tbl_login_entity tle join tbl_candidate tc on tle.id = tc.login_entity_id\r\n"
			+ "where tle.is_active =?1 and tc.is_active =?1 and LOWER(tle.username) =LOWER(?2) and tle.password =?3 and tle.org_id=?4 ", nativeQuery = true)
	public LoginEntity getDetailsByOrgId(String isActive, String username, String password, Long orgId);

	@Query("select login from LoginEntity login join  fetch login.organization where LOWER(login.username)=LOWER(?1) and login.password=?2 and login.organization.id= ?3")
	public LoginEntity findByCustomQueryByOrgId(String username, String password,Long orgId);
	
	@Query(value = "select tle.* from tbl_login_entity tle join tbl_candidate tc on tle.id = tc.login_entity_id\r\n"
			+ "where tle.is_active =?1 and tc.is_active =?1 and LOWER(tle.username) =LOWER(?2) and tle.org_id=?3 ", nativeQuery = true)
	public LoginEntity findByUsernameIsActiveOrgId(String isActive, String username, Long orgId);
	
	
	@Query("select login from LoginEntity login  where id= ?1")
	public LoginEntity findByLoginId(Long id);
	
	
	@Query("select login from LoginEntity login where is_active  =?1 and id =?2")
	public LoginEntity findByLoginEntityId(String isActive, Long Id);
	
}
