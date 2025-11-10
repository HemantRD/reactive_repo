package com.vinsys.hrms.kra.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.kra.entity.KraDisplayStatus;

public interface IKraDisplayStatusDao extends JpaRepository<KraDisplayStatus,Long> {

	KraDisplayStatus findByDisplayName(String displaystatus);

	

}
