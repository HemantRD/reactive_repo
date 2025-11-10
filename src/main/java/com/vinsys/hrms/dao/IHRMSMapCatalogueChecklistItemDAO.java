package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MapCatalogueChecklistItem;

public interface IHRMSMapCatalogueChecklistItemDAO extends JpaRepository<MapCatalogueChecklistItem, Long> {

	public List<MapCatalogueChecklistItem> findByisActiveAndOrgId(String isActive,Long orgId);
	
	@Query("select catChecklistItem from  MapCatalogueChecklistItem catChecklistItem where catChecklistItem.catalogue.id=?1 and catChecklistItem.orgId = ?2 ")
	public List<MapCatalogueChecklistItem> findBycatalogueidAndOrgId(long id,Long orgId);
	
	@Query("select count(*) from MapCatalogueChecklistItem items where items.catalogue.id = ?1 and items.orgId= ?2 ")
	public int countByCatalogueIdAndOrgId(Long catalogueId,Long orgId);

}
