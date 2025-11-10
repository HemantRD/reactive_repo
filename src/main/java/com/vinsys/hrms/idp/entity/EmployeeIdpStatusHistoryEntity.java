package com.vinsys.hrms.idp.entity;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
public class EmployeeIdpStatusHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_idp_status_id", nullable = false)
    private EmployeeIdpStatusEntity employeeIdpStatus;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "record_status", length = 20)
    private String recordStatus;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;
}
