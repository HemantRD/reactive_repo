package com.vinsys.hrms.idp.dao;

import mssql.googlecode.concurrentlinkedhashmap.Weighers;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vinsys.hrms.idp.entity.Idp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IIdpDAO extends JpaRepository<Idp, Long> {
    List<Idp> findByEmployeeId(Long employeeId);

    List<Idp> findByEmployeeIdIn(List<Long> empIds);

    Idp findByIdAndActiveFlowIdAndRecordStatus(Long id, Long activeFlowId, String recordStatus);

}
