package com.vinsys.hrms.idp.entity;

import java.time.Instant;
import javax.persistence.*;

import com.vinsys.hrms.entity.Employee;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "tbl_map_employee_idp_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapEmployeeIdpStatusHistory {

    @Id
    @SequenceGenerator(name = "seq_map_employee_idp_status_history", sequenceName = "seq_map_employee_idp_status_history", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_employee_idp_status_history")
    private Long id;

    // Foreign key reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_idp_status_id", nullable = false)
    private MapEmployeeIdpStatus employeeIdpStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "idp_submission_status", length = 20, nullable = false)
    private String idpSubmissionStatus;

    @Column(name = "record_status", length = 20)
    private String recordStatus;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;
}
