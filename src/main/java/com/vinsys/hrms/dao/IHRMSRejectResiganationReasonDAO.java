package com.vinsys.hrms.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterRejectResiganationReason;;

public interface IHRMSRejectResiganationReasonDAO extends JpaRepository<MasterRejectResiganationReason, Long> {
	
	@Query("select reasons from MasterRejectResiganationReason reasons where reasons.isActive = ?1")
	public List<MasterRejectResiganationReason> findByIsActive(String isActive);

}
