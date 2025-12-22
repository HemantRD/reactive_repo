package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.entity.IdpOrganizationEmployeeView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Repository for accessing the vw_idp_organization_employees view
 * Provides methods to fetch employee data filtered by various criteria for IDP management
 */
@Repository
public interface IIdpOrganizationEmployeeViewDAO extends JpaRepository<IdpOrganizationEmployeeView, Long> {

    @Query("select e " +
            " from IdpOrganizationEmployeeView e" +
            "    where " +
            "     (:keyword is null or :keyword = '' OR " +
            "        LOWER(CONCAT(e.empFirstName, ' ', COALESCE(e.empMiddleName, ''), ' ', e.empLastName)) LIKE CONCAT('%', :keyword, '%') OR " +
            "     LOWER(e.employeeCode) LIKE CONCAT('%', :keyword, '%') OR " +
            "     LOWER(e.officialEmailId) LIKE CONCAT('%', :keyword, '%'))" +
            "    and (:branchId is null or e.branchId=:branchId)" +
            "    and (:branchName is null or e.branchName LIKE CONCAT('%', :branchName, '%'))" +
            "    and (:departmentId is null or e.departmentId=:departmentId)" +
            "    and (:gradeId is null or e.gradeId=:gradeId)" +
            "    and (:idpSubmissionStatus is null or e.idpSubmissionStatus=:idpSubmissionStatus)"
    )
    Page<IdpOrganizationEmployeeView> findOrgEmployeesByPage(@RequestParam("keyword") String keyword,
                                                             Long branchId, String branchName, Long departmentId,
                                                             Long gradeId, String idpSubmissionStatus, Pageable pageable);

    @Query("select e " +
            " from IdpOrganizationEmployeeView e" +
            "    where " +
            "     (:keyword is null or :keyword = '' OR " +
            "        LOWER(CONCAT(e.empFirstName, ' ', COALESCE(e.empMiddleName, ''), ' ', e.empLastName)) LIKE CONCAT('%', :keyword, '%') OR " +
            "     LOWER(e.employeeCode) LIKE CONCAT('%', :keyword, '%') OR " +
            "     LOWER(e.officialEmailId) LIKE CONCAT('%', :keyword, '%'))" +
            "    and (:branchId is null or e.branchId=:branchId)" +
            "    and (:branchName is null or e.branchName LIKE CONCAT('%', :branchName, '%'))" +
            "    and (:departmentId is null or e.departmentId=:departmentId)" +
            "    and (:gradeId is null or e.gradeId=:gradeId)" +
            "    and (:idpSubmissionStatus is null or e.idpSubmissionStatus=:idpSubmissionStatus)"
    )
    List<IdpOrganizationEmployeeView> findOrgEmployeesExcel(@RequestParam("keyword") String keyword,
                                                             Long branchId, String branchName, Long departmentId,
                                                             Long gradeId, String idpSubmissionStatus, Sort sort);

    List<IdpOrganizationEmployeeView> findByEmployeeIdIn(List<Long> employeeIds);
}

