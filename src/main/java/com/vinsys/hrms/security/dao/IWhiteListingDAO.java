package com.vinsys.hrms.security.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.security.entity.WhiteListing;

public interface IWhiteListingDAO extends JpaRepository<WhiteListing, Long> {

	@Query(value = "select * from tbl_mst_whitelistings where is_active = 'Y' order by sequence", nativeQuery = true)
	List<WhiteListing> findByIsActive();
}
