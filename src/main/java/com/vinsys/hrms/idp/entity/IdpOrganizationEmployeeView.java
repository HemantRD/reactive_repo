package com.vinsys.hrms.idp.entity;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Entity representing the vw_idp_organization_employees database view
 * This view combines employee data with department, division, branch, and reporting manager information
 * for IDP organization employee filtering and listing
 */
@Entity
@Table(name = "vw_idp_organization_employees")
@Immutable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdpOrganizationEmployeeView {

    @Id
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "employee_code")
    private String employeeCode;

    @Column(name = "official_email_id")
    private String officialEmailId;

    @Column(name = "official_mobile_number")
    private String officialMobileNumber;

    @Column(name = "emp_first_name")
    private String empFirstName;

    @Column(name = "emp_middle_name")
    private String empMiddleName;

    @Column(name = "emp_last_name")
    private String empLastName;

    @Column(name = "contact_no")
    private Long contactNo;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "division_id")
    private Long divisionId;

    @Column(name = "division_name")
    private String divisionName;

    @Column(name = "designation_id")
    private Long designationId;

    @Column(name = "designation_name")
    private String designationName;

    @Column(name = "grade")
    private String grade;

    @Column(name = "grade_id")
    private Long gradeId;

    @Column(name = "reporting_manager_id")
    private Long reportingManagerId;

    @Column(name = "rm_employee_code")
    private String rmEmployeeCode;

    @Column(name = "rm_first_name")
    private String rmFirstName;

    @Column(name = "rm_last_name")
    private String rmLastName;

    @Column(name = "idp_submission_status")
    private String idpSubmissionStatus;

    /**
     * Get full employee name
     * @return concatenated employee name
     */
    public String getFullEmployeeName() {
        StringBuilder name = new StringBuilder();
        if (empFirstName != null) name.append(empFirstName);
        if (empMiddleName != null) name.append(" ").append(empMiddleName);
        if (empLastName != null) name.append(" ").append(empLastName);
        return name.toString().trim();
    }

    /**
     * Get reporting manager full name
     */
    public String getReportingManagerName() {
        StringBuilder name = new StringBuilder();
        if (rmFirstName != null) name.append(rmFirstName);
        if (rmLastName != null) name.append(" ").append(rmLastName);
        return name.toString().trim();
    }
}
