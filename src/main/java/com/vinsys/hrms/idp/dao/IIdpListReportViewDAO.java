package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.entity.IdpListReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for accessing the vw_idp_list_report view
 */
@Repository
public interface IIdpListReportViewDAO extends JpaRepository<IdpListReportView, Long> {

    /**
     * Find IDPs by employee ID
     */
    List<IdpListReportView> findByEmployeeId(Long employeeId);

    /**
     * Find IDPs by department ID
     */
    List<IdpListReportView> findByEmpDepartmentId(Long departmentId);

    /**
     * Find IDPs by department IDs
     */
    List<IdpListReportView> findByEmpDepartmentIdIn(List<Long> departmentIds);

    /**
     * Find IDPs by division ID
     */
    List<IdpListReportView> findByEmpDivisionId(Long divisionId);

    /**
     * Find IDPs by division IDs
     */
    List<IdpListReportView> findByEmpDivisionIdIn(List<Long> divisionIds);

    /**
     * Find IDPs by reporting manager ID
     */
    List<IdpListReportView> findByRmEmployeeId(Long rmEmployeeId);

    /**
     * Find all active IDPs
     */
    @Query("SELECT v FROM IdpListReportView v WHERE v.idpRecordStatus = 'Active'")
    List<IdpListReportView> findAllActive();

    /**
     * Find IDPs by employee IDs
     */
    List<IdpListReportView> findByEmployeeIdIn(List<Long> employeeIds);

    /**
     * Find IDP by ID and active status
     */
    @Query("SELECT v FROM IdpListReportView v WHERE v.id = :idpId AND v.idpRecordStatus = 'Active'")
    IdpListReportView findByIdpId(@Param("idpId") Long idpId);
}
