package com.vinsys.hrms.idp.dao;

import mssql.googlecode.concurrentlinkedhashmap.Weighers;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vinsys.hrms.idp.entity.Idp;

public interface IIdpDAO extends JpaRepository<Idp, Long> {
    Idp findByActiveFlowId(Long activeFlowId);
}