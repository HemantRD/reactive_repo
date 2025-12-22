package com.vinsys.hrms.idp.entity;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.Instant;

/**
 * Entity representing the vw_idp_list_report database view
 * This view combines IDP data with employee, department, division, and reporting manager information
 */
@Entity
@Table(name = "vw_idp_list_report")
@Immutable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdpListReportView {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "idp_created_date")
    private Instant idpCreatedDate;

    @Column(name = "idp_status")
    private String idpStatus;

    @Column(name = "idp_record_status")
    private String idpRecordStatus;

    @Column(name = "idp_active_flow_id")
    private Long idpActiveFlowId;

    @Column(name = "fh_id")
    private Long fhId;

    @Column(name = "pending_with_role")
    private String pendingWithRole;

    @Column(name = "pending_with_employee_id")
    private Long pendingWithEmployeeId;

    @Column(name = "fh_start_date")
    private Instant fhStartDate;

    @Column(name = "fh_action_type")
    private String fhActionType;

    @Column(name = "fh_action_date")
    private Instant fhActionDate;

    @Column(name = "fh_status")
    private String fhStatus;

    @Column(name = "fh_record_status")
    private String fhRecordStatus;

    @Column(name = "emp_first_name")
    private String empFirstName;

    @Column(name = "emp_middle_name")
    private String empMiddleName;

    @Column(name = "emp_last_name")
    private String empLastName;

    @Column(name = "emp_department_id")
    private Long empDepartmentId;

    @Column(name = "emp_division_id")
    private Long empDivisionId;

    @Column(name = "rm_employee_id")
    private Long rmEmployeeId;

    /**
     * Get full employee name
     */
    public String getFullEmployeeName() {
        StringBuilder name = new StringBuilder();
        if (empFirstName != null) name.append(empFirstName);
        if (empMiddleName != null) name.append(" ").append(empMiddleName);
        if (empLastName != null) name.append(" ").append(empLastName);
        return name.toString().trim();
    }
}
