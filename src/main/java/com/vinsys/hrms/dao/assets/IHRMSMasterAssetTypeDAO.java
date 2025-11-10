package com.vinsys.hrms.dao.assets;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.assets.MasterAssetType;

public interface IHRMSMasterAssetTypeDAO extends JpaRepository<MasterAssetType, Long> {

	@Query("SELECT astp FROM MasterAssetType astp WHERE astp.organization.id = ?1 "
			+ " AND astp.division.id = ?2 AND astp.isActive = ?3")
	public List<MasterAssetType> findAllActiveMasterAssetTypeByOrgDiv(long orgId, long divId, String isActive);
}
