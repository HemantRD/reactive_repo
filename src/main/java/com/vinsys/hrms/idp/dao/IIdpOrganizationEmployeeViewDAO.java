package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.entity.IdpOrganizationEmployeeView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for accessing the vw_idp_organization_employees view
 * Provides methods to fetch employee data filtered by various criteria for IDP management
 */
@Repository
public interface IIdpOrganizationEmployeeViewDAO extends JpaRepository<IdpOrganizationEmployeeView, Long> {


    /**
     * Find employees by organization ID and branch ID with pagination
     */
    Page<IdpOrganizationEmployeeView> findByBranchId(Long branchId, Pageable pageable);

    /**
     * Find employees by organization ID and branch name with pagination
     */
    @Query("SELECT e FROM IdpOrganizationEmployeeView e WHERE e.branchName LIKE %:branchName%")
    Page<IdpOrganizationEmployeeView> findByBranchName(@Param("branchName") String branchName, Pageable pageable);

    /**
     * Find employees by organization ID and department ID with pagination
     */
    Page<IdpOrganizationEmployeeView> findByDepartmentId(Long departmentId, Pageable pageable);

    /**
     * Find employees by organization ID and grade ID with pagination
     */
    Page<IdpOrganizationEmployeeView> findByGradeId(Long gradeId, Pageable pageable);

    /**
     * Find employees by organization ID and keyword (searches in employee name and code) with pagination
     */
//    @Query("SELECT e FROM IdpOrganizationEmployeeView e WHERE " +
//            "(CONCAT(e.empFirstName, ' ', COALESCE(e.empMiddleName, ''), ' ', e.empLastName) ILIKE CONCAT('%', :keyword, '%') OR " +
//            "e.employeeCode ILIKE CONCAT('%', :keyword, '%') OR " +
//            "e.officialEmailId ILIKE CONCAT('%', :keyword, '%'))")
    @Query("SELECT e FROM IdpOrganizationEmployeeView e WHERE " +
            "(CONCAT(e.empFirstName, ' ', COALESCE(e.empMiddleName, ''), ' ', e.empLastName) LIKE CONCAT('%', :keyword, '%') OR " +
            "e.employeeCode LIKE CONCAT('%', :keyword, '%') OR " +
            "e.officialEmailId LIKE CONCAT('%', :keyword, '%'))")
    Page<IdpOrganizationEmployeeView> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    List<IdpOrganizationEmployeeView> findByEmployeeIdIn(List<Long> employeeIds);
}

