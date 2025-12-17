package com.vinsys.hrms.kra.dao;

import com.vinsys.hrms.kra.entity.KraDetailsReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IKraDetailsReportDAO extends JpaRepository<KraDetailsReport, Long> {
    //List<KraDetailsReport> findByCycle_id(Long cycleId);
    @Query("SELECT k FROM KraDetailsReport k WHERE k.cycle_id = :cycleId AND k.departmentId = :departmentId")
    List<KraDetailsReport> findByCycleIdAndDepartmentId(@Param("cycleId") Long cycleId, @Param("departmentId") Long departmentId);
    //List<KraDetailsReport> findByCycleIdAndDepartmentIdIn(Long cycleId, List<Long> departmentIds);
}
